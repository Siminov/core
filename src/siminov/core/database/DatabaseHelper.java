/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package siminov.core.database;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import siminov.core.Constants;
import siminov.core.Siminov;
import siminov.core.database.design.IDataTypeHandler;
import siminov.core.database.design.IDatabaseImpl;
import siminov.core.database.design.IQueryBuilder;
import siminov.core.events.IDatabaseEvents;
import siminov.core.exception.DatabaseException;
import siminov.core.exception.DeploymentException;
import siminov.core.exception.SiminovException;
import siminov.core.log.Log;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.DatabaseMappingDescriptor;
import siminov.core.model.DatabaseMappingDescriptor.Attribute;
import siminov.core.model.DatabaseMappingDescriptor.Index;
import siminov.core.model.DatabaseMappingDescriptor.Relationship;
import siminov.core.resource.ResourceManager;
import siminov.core.utils.ClassUtils;


/**
 * It provides utility methods to deal with database. 
 * It has methods to create, delete, and perform other common database management tasks.
 */
public abstract class DatabaseHelper implements Constants {

	private static ResourceManager resourceManager = ResourceManager.getInstance();

	
	/**
	 * It is used to create instance of IDatabase implementation.
	 * @param databaseDescriptor
	 * @return DatabaseBundle Database Bundle instance object.
	 * @throws DatabaseException
	 */
	public static DatabaseBundle createDatabase(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		return DatabaseFactory.getInstance().getDatabaseBundle(databaseDescriptor);
	}
	

	
	/**
	 * Upgrade Existing Database.
	 * @param databaseDescriptor
	 * @throws DatabaseException throws If any exception thrown.
	 */
	public static void upgradeDatabase(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {

		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());

		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "upgradeDatabase", "No Database Instance Found For, DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "upgradeDatabase", "No Database Instance Found For, DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		
		/*
		 * Fetch Database Version
		 */
		String fetchDatabaseVersionQuery = queryBuilder.formFetchDatabaseVersionQuery(null);
		Log.debug(DatabaseHelper.class.getName(), "upgradeDatabase", "Fetch Database Version Query: " + fetchDatabaseVersionQuery);


		double currentDatabaseVersion = 0;
		Iterator<Map<String, Object>> datas = database.executeSelectQuery(databaseDescriptor, null, fetchDatabaseVersionQuery);

		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {
				currentDatabaseVersion = ((Long) values.next()).doubleValue();
			}
		}

		
		if(currentDatabaseVersion == databaseDescriptor.getVersion()) {
			return;
		}
		
		Collection<DatabaseMappingDescriptor> allDatabaseMappingDescriptor = new ArrayList<DatabaseMappingDescriptor>();
		Iterator<DatabaseMappingDescriptor> allDatabaseMappingDescriptorIterator = resourceManager.getDatabaseMappingDescriptors();
		while(allDatabaseMappingDescriptorIterator.hasNext()) {
			allDatabaseMappingDescriptor.add(allDatabaseMappingDescriptorIterator.next());
		}
		
		
		
		Collection<String> tableNames = new ArrayList<String>();

		Iterator<DatabaseMappingDescriptor> databaseMappingDescriptors = databaseDescriptor.getDatabaseMappingDescriptors();

		/*
		 * Get Table Names
		 */
		String fetchTableNamesQuery = queryBuilder.formTableNames(null);
		Log.debug(DatabaseHelper.class.getName(), "upgradeDatabase", "Fetch Table Names, " + fetchTableNamesQuery);
		
		datas = database.executeSelectQuery(databaseDescriptor, null, fetchTableNamesQuery);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Iterator<String> keys = data.keySet().iterator();

			while(keys.hasNext()) {
				String key = keys.next();
						
				if(key.equals(FORM_TABLE_NAMES_NAME)) {
					tableNames.add((String) data.get(key));
				}
			}
		}
		
		
		/*
		 * Create Or Upgrade Table
		 */
		while(databaseMappingDescriptors.hasNext()) {
			DatabaseMappingDescriptor databaseMappingDescriptor = databaseMappingDescriptors.next();
			
			boolean contain = false;
			for(String tableName: tableNames) {
				
				if(tableName.equalsIgnoreCase(databaseMappingDescriptor.getTableName())) {
					contain = true;
					break;
				}
			}
			
			if(contain) {
				DatabaseHelper.upgradeTable(databaseMappingDescriptor);
			} else {
				DatabaseHelper.createTable(databaseMappingDescriptor);
			}
		}
		
		
		/*
		 * Drop Table
		 */
		for(String tableName: tableNames) {
			if(tableName.equalsIgnoreCase(ANDROID_METADATA_TABLE_NAME)) {
				continue;
			}
			
			
			boolean contain = false;
			
			for(DatabaseMappingDescriptor databaseMappingDescriptor: allDatabaseMappingDescriptor) {
				
				if(tableName.equalsIgnoreCase(databaseMappingDescriptor.getTableName())) {
					contain = true;
					break;
				}
			}

			if(!contain) {

				Map<String, Object> parameters = new HashMap<String, Object> ();
				parameters.put(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);

				database.executeQuery(databaseDescriptor, null, queryBuilder.formDropTableQuery(parameters));
			}
		}
		
		
		
		
		/*
		 * Update Database Version
		 */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER, databaseDescriptor.getVersion());
		
		String updateDatabaseVersionQuery = queryBuilder.formUpdateDatabaseVersionQuery(parameters);
		Log.debug(DatabaseHelper.class.getName(), "upgradeDatabase", "Update Database Version Query: " + updateDatabaseVersionQuery);
		
		database.executeQuery(databaseDescriptor, null, updateDatabaseVersionQuery);
	}
	
	
	/**
	 * Upgrade Table.
	 * @param databaseMappingDescriptor object related to table.
	 * @throws DatabaseException If any exception thrown while upgrading table.
	 */
	public static void upgradeTable(final DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "upgradeTable", "No Database Instance Found For, TABLE-NAME: " + databaseMappingDescriptor.getTableName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "upgradeTable", "No Database Instance Found For, TABLE-NAME: " + databaseMappingDescriptor.getTableName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		
		String tableInfoQuery = queryBuilder.formTableInfoQuery(parameters);
		Log.debug(DatabaseHelper.class.getName(), "upgradeTable", "Table Info Query: " + tableInfoQuery);
		
		
		Collection<Attribute> newAttributes = new ArrayList<Attribute>();
		Collection<String> oldAttributes = new ArrayList<String>();
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
		

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(null, null, tableInfoQuery);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Iterator<String> keys = data.keySet().iterator();

			while(keys.hasNext()) {
				String key = keys.next();
						
				if(key.equals(FORM_TABLE_INFO_QUERY_NAME)) {
					oldAttributes.add((String) data.get(key));
				}
			}
		}

		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
				
			boolean contain = false;
			for(String oldColumn: oldAttributes) {
				
				if(oldColumn.equalsIgnoreCase(attribute.getColumnName())) {
					contain = true;
					break;
				}
			}
			
			if(!contain) {
				newAttributes.add(attribute);
			}
		}
		
		
		for(Attribute column: newAttributes) {
			
			String columnName = column.getColumnName();
			
			parameters = new HashMap<String, Object>();
			parameters.put(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
			parameters.put(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER, columnName);
			
			String addColumnQuery = queryBuilder.formAlterAddColumnQuery(parameters);
			Log.debug(DatabaseHelper.class.getName(), "upgradeTable", "Add New Column Query: " + addColumnQuery);
			
			
			database.executeQuery(null, null, addColumnQuery);
		}
	}
	
	
	/**
	   Is used to create a new table in an database.
	  	<p>
	  	Using SIMINOV there are three ways to create table in database.
	   	
	   	<pre> 
	  		<ul>
	  			<li> Describing table structure in form of DATABASE-MAPPING-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
	  				<p>
SIMINOV will parse each DATABASE-MAPPING-DESCRIPTOR XML defined by developer and create table's in database.
	  				
Example:
	{@code
		
		<database-mapping-descriptor>
		
			<entity table_name="LIQUOR" class_name="siminov.core.template.model.Liquor">
				
				<attribute variable_name="liquorType" column_name="LIQUOR_TYPE">
					<property name="type">java.lang.String</property>
					<property name="primary_key">true</property>
					<property name="not_null">true</property>
					<property name="unique">true</property>
				</attribute>		
		
				<attribute variable_name="description" column_name="DESCRIPTION">
					<property name="type">java.lang.String</property>
				</attribute>
		
				<attribute variable_name="history" column_name="HISTORY">
					<property name="type">java.lang.String</property>
				</attribute>
		
				<attribute variable_name="link" column_name="LINK">
					<property name="type">java.lang.String</property>
					<property name="default">www.wikipedia.org</property>
				</attribute>
		
				<attribute variable_name="alcholContent" column_name="ALCHOL_CONTENT">
					<property name="type">java.lang.String</property>
				</attribute>
		
				<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
					<attribute>HISTORY</attribute>
				</index>
		
				<relationships>
		
				    <one-to-many refer="liquorBrands" refer_to="siminov.core.template.model.LiquorBrand" on_update="cascade" on_delete="cascade">
						<property name="load">true</property>
					</one-to-many>		
				    
				</relationships>
													
			</entity>
		
		</database-mapping-descriptor>		
			}
					</p>
	  			</li>
	  		</ul> 
	  	</pre>
	  </p>
	  
	 * @param databaseMappingDescriptors Database-mapping objects which defines the structure of each table.
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public static void createTables(final Iterator<DatabaseMappingDescriptor> databaseMappingDescriptors) throws DatabaseException {

		while(databaseMappingDescriptors.hasNext()) {
			createTable(databaseMappingDescriptors.next());
		}
	}

	/**
	   Is used to create a new table in an database.
	  	<p>
			<pre> Manually creating table structure using DatabaseMapping POJO class. 
  					
	Example: 
	
		{@code
	
		Liquor liquor = new Liquor();
		
		try {
			Database.createTables(liquor.getDatabaseMappingDescriptor());
		} catch(DatabaseException databaseException) {
			//Log It.
		}
		
		}
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public static void createTable(final DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {

		/*
		 * 1. Get IDatabase with respect to current database mapping class name.
		 * 2. Get Table Name, and all columns.
		 * 3. Get all attributes and properties from database mapping.
		 * 		LIKE(COLUMN NAMES, COLUMN TYPES, PRIMARY KEYS, UNIQUE's, NOT NULL, DEFAULT VALUES, CHECKS, ).
		 * 
		 * 4. If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined, 
		 * 		because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
		 * 		Else get foreign keys.
		 * 
		 * 5. Call QueryBuilder.formCreateTableQuery, get query to create table.
		 * 	After forming create table query call executeQuery method to create table in database.
		 *
		 * 6. Create all triggers.
		 * 7. Create Index for table if its defined.
		 *		Get all attributes and properties of index.
		 * 		LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
		 * 		After forming index query call executeQuery method to create index.
		 * 
		 */

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());

		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		IDataTypeHandler dataTypeHandler = databaseBundle.getDataTypeHandler();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "createTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "createTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		String tableName = databaseMappingDescriptor.getTableName();

		/*
		 * Get all attributes and properties from database mapping. 
		 * LIKE(COLUMN NAMES, COLUMN TYPES, DEFAULT VALUES, CHECKS, NOT NULL, PRIMARY KEYS, UNIQUE's ).
		 */
		Collection<String> columnNames = new LinkedList<String>();
		Collection<String> columnTypes = new LinkedList<String>();
		
		Collection<String> defaultValues = new LinkedList<String>();
		Collection<String> checks = new LinkedList<String>();
		
		Collection<Boolean> isNotNull = new LinkedList<Boolean>();
		
		Collection<String> primaryKeys = new LinkedList<String>();
		Collection<String> uniqueKeys = new LinkedList<String>();
		
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();

		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			columnNames.add(attribute.getColumnName());
			columnTypes.add(dataTypeHandler.convert(attribute.getType()));
			isNotNull.add(attribute.isNotNull());

			defaultValues.add(attribute.getDefaultValue());
			checks.add(attribute.getCheck());
			
			boolean isPrimary = attribute.isPrimaryKey();
			boolean isUnique = attribute.isUnique();
			
			if(isPrimary) {
				primaryKeys.add(attribute.getColumnName());
			}
			
			if(isUnique) {
				uniqueKeys.add(attribute.getColumnName());
			}
		}
		
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Collection<Attribute> foreignAttributes = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Attribute> foreignAttributesIterator = foreignAttributes.iterator();
			
			while(foreignAttributesIterator.hasNext()) {
				Attribute foreignAttribute = foreignAttributesIterator.next();
				
				columnNames.add(foreignAttribute.getColumnName());
				columnTypes.add(dataTypeHandler.convert(foreignAttribute.getType()));
				isNotNull.add(foreignAttribute.isNotNull());

				defaultValues.add(foreignAttribute.getDefaultValue());
				checks.add(foreignAttribute.getCheck());
					
				boolean isPrimary = foreignAttribute.isPrimaryKey();
				if(isPrimary) {
					primaryKeys.add(foreignAttribute.getColumnName());
				}
				
				boolean isUnique = foreignAttribute.isUnique();
				if(isUnique) {
					uniqueKeys.add(foreignAttribute.getColumnName());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(attribute.getColumnName());
					columnTypes.add(dataTypeHandler.convert(attribute.getType()));
					isNotNull.add(attribute.isNotNull());

					defaultValues.add(attribute.getDefaultValue());
					checks.add(attribute.getCheck());
					
					if(isPrimary) {
						primaryKeys.add(attribute.getColumnName());
					}
					
					boolean isUnique = attribute.isUnique();
					if(isUnique) {
						uniqueKeys.add(attribute.getColumnName());
					}
				}
			}
		}

		/*
		 * If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined, 
		 * because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
		 * Else get foreign keys.
		 */
		String foreignKeys = "";
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER, databaseMappingDescriptor);
		
		foreignKeys = queryBuilder.formForeignKeyQuery(parameters);

		
		/*
		 * Call QueryBuilder.formCreateTableQuery, get query to create table.
		 * After forming create table query call executeQuery method to create table in database.
		 */
		
		parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER, columnNames.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER, columnTypes.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER, defaultValues.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER, checks.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER, primaryKeys.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER, isNotNull.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER, uniqueKeys.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER, foreignKeys);
		
		String query = queryBuilder.formCreateTableQuery(parameters);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		
		/*
		 * Create Index for table if its defined.
		 */
		Iterator<Index> indexes = databaseMappingDescriptor.getIndexes();
		while(indexes.hasNext()) {
			
			/*
			 * Get all attributes and properties of index.
			 * LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
			 * 
			 * After forming index query call executeQuery method to create index.
			 */
			
			createIndex(databaseMappingDescriptor, indexes.next());
		}

		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onTableCreated(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor);
		}
	}

	/**
	 * It drop's the table from database based on database mapping descriptor.
	  	<p>
			<pre> Drop the Liquor table.
	
	{@code
	
	DatabaseMappingDescriptor databaseMappingDescriptor = new Liquor().getDatabaseMappingDescriptor();
	
	try {
		Database.dropTable(databaseMappingDescriptor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @throws DatabaseException If not able to drop table.
	 */
	static void dropTable(final DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {

		Siminov.isActive();

		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		

		String tableName = databaseMappingDescriptor.getTableName();
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);

		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, queryBuilder.formDropTableQuery(parameters));
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onTableDropped(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor);
		}
	}
	
	
	/**
	   Is used to create a new index on a table in database.
	  	<p>
			<pre> Create Index On Liquor table.
	
	{@code
	
	DatabaseMapping.Index indexOnLiquor = databaseMapping.new Index();
	indexOnLiquor.setName("LIQUOR_INDEX_BASED_ON_LINK");
	indexOnLiquor.setUnique(true);
	
	//Add Columns on which we need index.
	indexOnLiquor.addColumn("LINK");

	DatabaseMapping databaseMapping = new Liquor().getDatabaseMapping();

	try {
		Database.createIndex(databaseMapping, indexOnLiquor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @param index Index object which defines the structure of index needs to create.
	 * @throws DatabaseException If not able to create index on table.
	 */
	static void createIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final Index index) throws DatabaseException {
		
		String indexName = index.getName();
		Iterator<String> columnNames = index.getColumns();
		boolean isUnique = index.isUnique();

		createIndex(databaseMappingDescriptor, indexName, columnNames, isUnique);
	}

	/**
	   Is used to create a new index on a table in database.
	  	<p>
			<pre> Create Index On Liquor table.
	
	{@code
	
	String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
	boolean isUnique = true;
	
	Collection<String> columnNames = new ArrayList<String>();
	columnNames.add("LINK");
	
	try {
		new Liquor().createIndex(indexName, columnNames.iterator(), isUnique);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @param indexName Name of index.
	 * @param columnNames Iterator over column names.
	 * @param isUnique true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
	 * @throws DatabaseException If not able to create index on table.
	 */
	static void createIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final String indexName, final Iterator<String> columnNames, final boolean isUnique) throws DatabaseException {
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "createIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "createIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		final Collection<String> columnNamesCollection = new ArrayList<String> ();
		while(columnNames.hasNext()) {
			columnNamesCollection.add(columnNames.next());
		}

		
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER, columnNamesCollection.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER, isUnique);
		
		
		String query = queryBuilder.formCreateIndexQuery(parameters);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			Index index = new Index();
			index.setName(indexName);
			index.setUnique(isUnique);
			
			Iterator<String> columnNamesIterator = columnNamesCollection.iterator();
			while(columnNamesIterator.hasNext()) {
				index.addColumn(columnNamesIterator.next());
			}
			
			databaseEventHandler.onIndexCreated(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, index);
		}
	}

	/**
	   Is used to drop a index on a table in database.
	  	<p>
			<pre> Create Index On Liquor table.
	
	{@code
	
	String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
	DatabaseMapping databaseMapping = new Liquor().getDatabaseMapping();
	
	try {
		Database.dropIndex(databaseMapping, indexName);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @param indexName Name of a index needs to be drop.
	 * @throws DatabaseException If not able to drop index on table.
	 */
	static void dropIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final String indexName) throws DatabaseException {
		Siminov.isActive();

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());

		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
		}

		
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);

		
		String query = queryBuilder.formDropIndexQuery(parameters);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onIndexDropped(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, databaseMappingDescriptor.getIndex(indexName));
		}
	}
	
	
	/**
	 * It drop's the whole database based on database-descriptor.
	  	<p>
			<pre> Drop the Liquor table.
	
	{@code
	
	DatabaseDescriptor databaseDescriptor = new Liquor().getDatabaseDescriptor();
	
	try {
		Database.dropDatabase(databaseDescriptor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param databaseMapping Database-mapping object which defines the structure of table.
	 * @throws DatabaseException If not able to drop database.
	 */
	static void dropDatabase(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.isActive();

		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropDatabase", "No Database Instance Found For DATABASE-MAPPING: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropDatabase", "No Database Instance Found For DATABASE-MAPPING: " + databaseDescriptor.getDatabaseName());
		}

		String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);

		File file = new File(databasePath + databaseDescriptor.getDatabaseName());
		database.close(databaseDescriptor);
		file.delete();
		
		resourceManager.removeDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onDatabaseDropped(databaseDescriptor);
		}
	}

	/**
	   Begins a transaction in EXCLUSIVE mode.
	   <p>
	   Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
	   The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
	  
	   <pre>

Example: Make Beer Object

	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));

	DatabaseDescriptor databaseDescriptor = beer.getDatabaseDescriptor();
  
	try {
		Database.beginTransaction(databaseDescriptor);
	
		beer.save();
  
		Database.commitTransaction(databaseDescriptor);
	} catch(DatabaseException de) {
		//Log it.
	} finally {
		Database.endTransaction(databaseDescriptor);
	}
	  		
	}  			
	   </pre>
	  
	 * @throws DatabaseException If beginTransaction does not starts.
	 */
	static void beginTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "beginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "beginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		database.executeMethod(SQLITE_DATABASE_BEGIN_TRANSACTION, null);
	}
	
	/**
	   Marks the current transaction as successful. 
	   <p> Finally it will End a transaction.
	   <pre>

Example: Make Beer Object
	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));

	DatabaseDescriptor databaseDescriptor = beer.getDatabaseDescriptor();
  
	try {
		Database.beginTransaction(databaseDescriptor);
  		
		beer.save();
  
		Database.commitTransaction(databaseDescriptor);
	} catch(DatabaseException de) {
		//Log it.
	} finally {
		Database.endTransaction(databaseDescriptor);
	}
	
	}

	    </pre>
	 * @throws DatabaseException If not able to commit the transaction.
	 */
	static void commitTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}

		database.executeMethod(SQLITE_DATABASE_COMMIT_TRANSACTION, null);
	}

	/**
	 * End the current transaction.
	
	<pre>

Example:
	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));

	DatabaseDescriptor databaseDescriptor = beer.getDatabaseDescriptor();
  
	try {
		Database.beginTransaction(databaseDescriptor);
  		
		beer.save();
  
		Database.commitTransaction(databaseDescriptor);
	} catch(DatabaseException de) {
		//Log it.
	} finally {
		Database.endTransaction(databaseDescriptor);
	}
	
	}
	</pre>

	 * @param databaseDescriptor Database Descriptor Object.
	 */
	static void endTransaction(final DatabaseDescriptor databaseDescriptor) {
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		try {
			database.executeMethod(SQLITE_DATABASE_END_TRANSACTION, null);
		} catch(DatabaseException databaseException) {
			Log.error(DatabaseHelper.class.getName(), "commitTransaction", "DatabaseException caught while executing end transaction method, " + databaseException.getMessage());
		}
	}
	
	
	static Object[] select(final DatabaseMappingDescriptor databaseMappingDescriptor, final boolean distinct, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBy, final String having, final Iterator<String> orderBy, final String whichOrderBy, final String limit) throws DatabaseException {
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 * 2. Traverse group by's and form a single string.
		 * 3. Traverse order by'z and form a single string.
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 * 5. Pass got cursor and mapped database mapping object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
		 */
		
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "select", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "select", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */

		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER, distinct);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER, columnNames);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER, groupBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER, having);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER, orderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER, whichOrderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER, limit);
		
		
		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, queryBuilder.formSelectQuery(parameters));
		Collection<Map<String, Object>> datasBundle = new LinkedList<Map<String,Object>>();
		while(datas.hasNext()) {
			datasBundle.add(datas.next());
		}
		
		Iterator<Object> tuples = parseAndInflateData(databaseMappingDescriptor, datasBundle.iterator());
		datas = datasBundle.iterator();	
		
		/*
		 * 5. Pass got cursor and mapped database mapping object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 */
		
		Collection<Object> tuplesCollection = new LinkedList<Object> ();
		while(tuples.hasNext() && datas.hasNext()) {
			Object tuple = tuples.next();
			Map<String, Object> data = datas.next();
			
			tuplesCollection.add(tuple);
			
			processOneToOneRelationship(tuple);
			processOneToManyRelationship(tuple);

			processManyToOneRelationship(tuple, data);
			processManyToManyRelationship(tuple, data);
			
		}
		
		
		Class<?> classObject = null;
		try {
			classObject = Class.forName(databaseMappingDescriptor.getClassName());
		} catch(Exception exception) {
			Log.error(DatabaseHelper.class.getName(), "select", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "select", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		Object returnType = Array.newInstance(classObject, tuplesCollection.size());
		Iterator<Object> tuplesIterator = tuplesCollection.iterator();
		
		int index = 0;
		while(tuplesIterator.hasNext()) {
			Array.set(returnType, index++, tuplesIterator.next());
		}
		
		Object[] returnTypes = (Object[]) returnType;
		return returnTypes;
	}

	static Object[] lazyFetch(final DatabaseMappingDescriptor databaseMappingDescriptor, final boolean distinct, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBy, final String having, final Iterator<String> orderBy, final String whichOrderBy, final String limit) throws DatabaseException {
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 * 2. Traverse group by's and form a single string.
		 * 3. Traverse order by'z and form a single string.
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 * 5. Pass got cursor and mapped database mapping object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
		 */
		
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "lazyFetch", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "lazyFetch", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER, distinct);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER, columnNames);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER, groupBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER, having);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER, orderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER, whichOrderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER, limit);
		
		
		String query = queryBuilder.formSelectQuery(parameters);
		Iterator<Object> tuples = parseAndInflateData(databaseMappingDescriptor, database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query));
			
		/*
		 * 5. Pass got cursor and mapped database mapping object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 */
		
		Collection<Object> tuplesCollection = new LinkedList<Object> ();
		while(tuples.hasNext()) {
			Object tuple = tuples.next();
			tuplesCollection.add(tuple);
		}

		Class<?> classObject = null;
		try {
			classObject = Class.forName(databaseMappingDescriptor.getClassName());
		} catch(Exception exception) {
			Log.error(DatabaseHelper.class.getName(), "lazyFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "lazyFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		Object returnType = Array.newInstance(classObject, tuplesCollection.size());
		Iterator<Object> tuplesIterator = tuplesCollection.iterator();
		
		int index = 0;
		while(tuplesIterator.hasNext()) {
			Array.set(returnType, index++, tuplesIterator.next());
		}
		
		Object[] returnTypes = (Object[]) returnType;
		return returnTypes;
	}
	
	/**
	 	Returns all tuples based on manual query from mapped table for invoked class object.
	 
	 	<pre>
	 	
Example:
	
	{@code
	
	String query = "SELECT * FROM LIQUOR";
	
	Liquor[] liquors = null;
	try {
		liquors = new Liquor().select(query);
	} catch(DatabaseException de) {
		//Log it.
	}
	 		
	} 			
	 	</pre>
	
	 	@param query Manual query on which tuples need to be fetched.
	 	@return Array Of Objects.
	 	@throws DatabaseException If any error occur while getting tuples from a single table.
	 */
	static Object[] select(final Object object, final String query) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "fetchManual", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "fetchManual", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		Iterator<Object> tuples = parseAndInflateData(databaseMappingDescriptor, database.executeSelectQuery(getDatabaseDescriptor(object.getClass().getName()), databaseMappingDescriptor, query));
			
		/*
		 * 5. Pass got cursor and mapped database mapping object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 */
		
		Collection<Object> tuplesCollection = new LinkedList<Object> ();
		while(tuples.hasNext()) {
			Object tuple = tuples.next();
			tuplesCollection.add(tuple);
		}

		Class<?> classObject = null;
		try {
			classObject = Class.forName(databaseMappingDescriptor.getClassName());
		} catch(Exception exception) {
			Log.error(DatabaseHelper.class.getName(), "manualFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "manualFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		Object returnType = Array.newInstance(classObject, tuplesCollection.size());
		Iterator<Object> tuplesIterator = tuplesCollection.iterator();
		
		int index = 0;
		while(tuplesIterator.hasNext()) {
			Array.set(returnType, index++, tuplesIterator.next());
		}
		
		Object[] returnTypes = (Object[]) returnType;
		return returnTypes;
	}

	
	/**
		It adds a record to any single table in a relational database.

	   	<pre>
	   	
Example: Make Liquor Object

	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
  
	try {
		beer.save();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}

	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void save(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
		 * 3. Using QueryBuilder form insert bind query.
		 * 4. Pass query to executeBindQuery method for insertion.
		 * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		
		if(object == null) {
			Log.debug(DatabaseHelper.class.getName(), "save", "Invalid Object Found.");
			return;
		}
		
		/*
		 * 1. Get mapped database mapping object for invoked class object.
		 */
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "save", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "save", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
		 */
		String tableName = databaseMappingDescriptor.getTableName();
		
		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();

		Iterator<DatabaseMappingDescriptor.Attribute> attributes = databaseMappingDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			try {
				columnNames.add(attribute.getColumnName());
				columnValues.add(ClassUtils.getValue(object, attribute.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
			} 
		}
		
		processManyToOneRelationship(object, columnNames, columnValues);
		processManyToManyRelationship(object, columnNames, columnValues);
		
		
		/*
		 * 3. Using QueryBuilder form insert bind query.
		 */
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER, tableName);
		parameters.put(IQueryBuilder.FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER, columnNames.iterator());

		
		String query = queryBuilder.formSaveBindQuery(parameters);
		
		
		/*
		 * 4. Pass query to executeBindQuery method for insertion.
		 */
		database.executeBindQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query, columnValues.iterator());
		
		
		/*
		 * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
		 */
		Iterator<DatabaseMappingDescriptor.Relationship> relationships = databaseMappingDescriptor.getRelationships();
		while(relationships.hasNext()) {
			DatabaseMappingDescriptor.Relationship relationship = relationships.next();
			
			boolean isLoad = relationship.isLoad();
			if(!isLoad) {
				continue;
			}
			
			String relationshipType = relationship.getRelationshipType();
			if(relationshipType == null || relationshipType.length() <= 0) {
				continue;
			}
			
			if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				saveOrUpdate(value);
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
				Iterator<?> values = null;
				try {
					values = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(values == null) {
					continue;
				}
				
				while(values.hasNext()) {
					saveOrUpdate(values.next());
				}
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
				Iterator<?> values = null;
				try {
					values = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(values == null) {
					continue;
				}
				
				while(values.hasNext()) {
					saveOrUpdate(values.next());
				}
			}
		}
	}

	/**
		It updates a record to any single table in a relational database.
	
	   	<pre>

Example: Make Beer Object

	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
 
	try {
		beer.update();
	} catch(DatabaseException de) {
		//Log it.
	}

	}
	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void update(final Object object) throws DatabaseException {
		Siminov.isActive();
	
		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys for updation purpose.
		 * 4. Using QueryBuilder form update bind query.
		 * 5. Pass query to executeBindQuery method for updation.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */

		if(object == null) {
			Log.debug(DatabaseHelper.class.getName(), "update", "Invalid Object Found.");
			return;
		}

		/*
		 * 1. Get mapped database mapping object for invoked class object.
		 */
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "update", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "update", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		StringBuilder whereClause = new StringBuilder();
		String tableName = databaseMappingDescriptor.getTableName();

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		Iterator<DatabaseMappingDescriptor.Attribute> attributes = databaseMappingDescriptor.getAttributes();

		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			Object columnValue = null;
			try {
				columnNames.add(attribute.getColumnName());
				columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
			} 
			
			columnValues.add(columnValue);
			
			if(attribute.isPrimaryKey()) {
				if(whereClause.length() == 0) {
					whereClause.append(attribute.getColumnName() + "= '" + columnValue + "'");
				} else {
					whereClause.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");
				}
			}
		}
		
		processManyToOneRelationship(object, whereClause);
		processManyToManyRelationship(object, whereClause);

		processManyToOneRelationship(object, columnNames, columnValues);
		processManyToManyRelationship(object, columnNames, columnValues);
		

		/*
		 * 4. Using QueryBuilder form update bind query.
		 */
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER, tableName);
		parameters.put(IQueryBuilder.FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER, columnNames.iterator());
		parameters.put(IQueryBuilder.FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER, whereClause.toString());
		
		
		String query = queryBuilder.formUpdateBindQuery(parameters);

		/*
		 * 5. Pass query to executeBindQuery method for updation.
		 */
		
		Iterator<Object> values = columnValues.iterator();
		database.executeBindQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query, values);
		
		/*
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		Iterator<DatabaseMappingDescriptor.Relationship> relationships = databaseMappingDescriptor.getRelationships();
		while(relationships.hasNext()) {
			DatabaseMappingDescriptor.Relationship relationship = relationships.next();
			
			boolean isLoad = relationship.isLoad();
			if(!isLoad) {
				continue;
			}
			
			String relationshipType = relationship.getRelationshipType();
			if(relationshipType == null || relationshipType.length() <= 0) {
				continue;
			}
			
			if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				saveOrUpdate(value);
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
				Iterator<?> relationshipValues = null;
				try {
					relationshipValues = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(relationshipValues == null) {
					continue;
				}
				
				while(relationshipValues.hasNext()) {
					saveOrUpdate(relationshipValues.next());
				}
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
				Iterator<?> relationshipValues = null;
				try {
					relationshipValues = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(relationshipValues == null) {
					continue;
				}

				while(relationshipValues.hasNext()) {
					saveOrUpdate(relationshipValues.next());
				}
			}
		}
	}
	
	
	/**
		It finds out whether tuple exists in table or not.
		IF NOT EXISTS:
			adds a record to any single table in a relational database.
		ELSE:
			updates a record to any single table in a relational database.
	
	   	<pre>
	   	
Example: Make Beer Object

	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
  
	try {
		beer.saveOrUpdate();
	} catch(DatabaseException de) {
		//Log it.
	}
			
	}			
				
	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void saveOrUpdate(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get mapped database mapping object for object class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys to fetch objects from database table. IF EXISTS: call update method, ELSE: class save method.
		 * 4. IF EXISTS: call update method, ELSE: call save method.
		 */
		
		if(object == null) {
			Log.debug(DatabaseHelper.class.getName(), "saveOrUpdate", "Invalid Object Found.");
			return;
		}
		
		/*
		 * 1. Get mapped database mapping object for object class name.
		 */
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "saveOrUpdate", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "saveOrUpdate", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		
		StringBuilder whereClause = new StringBuilder();
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();

			if(attribute.isPrimaryKey()) {
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "saveOrUpdate", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "saveOrUpdate", siminovException.getMessage());
				} 

				
				if(whereClause.length() <= 0) {
					whereClause.append(attribute.getColumnName() + "= '" + columnValue + "'");
				} else {
					whereClause.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");
				}
			}
		}
		
		processManyToOneRelationship(object, whereClause);
		processManyToManyRelationship(object, whereClause);

		if(whereClause == null || whereClause.length() <= 0) {
			save(object);
			return;
		}

		
		/*
		 * 4. IF EXISTS: call update method, ELSE: call save method.
		 */
		int count = count(databaseMappingDescriptor, null, false, whereClause.toString(), null, null);
		if(count <= 0) {
			save(object);
		} else {
			update(object);
		}
	}
	

	static void delete(final Object object, final String whereClause) throws DatabaseException {
		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys for deletion purpose.
		 * 4. Using QueryBuilder form update bind query.
		 * 5. Pass query to executeBindQuery method for deletion.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		
		if(object == null) {
			Log.debug(DatabaseHelper.class.getName(), "delete", "Invalid Object Found.");
			return;
		}

		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 */
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "delete", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "delete", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		StringBuilder where = new StringBuilder();
		
		if(whereClause == null || whereClause.length() <= 0) {
			Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
			
			while(attributes.hasNext()) {
				Attribute attribute = attributes.next();
				
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "delete", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "delete", siminovException.getMessage());
				} 

				
				/*
				 * 3. Form where clause based on primary keys for deletion purpose.
				 */
				if(attribute.isPrimaryKey()) {
					if(where.length() <= 0) {
						where.append(attribute.getColumnName() + "= '" + columnValue + "'");							
					} else {
						where.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");							
					}
				}
			}

			processManyToOneRelationship(object, where);
			processManyToManyRelationship(object, where);
			
		} else {
			where.append(whereClause);
		}
		
		/*
		 * 4. Using QueryBuilder form update bind query.
		 */
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER, where.toString());

		
		String query = queryBuilder.formDeleteQuery(parameters);
		/*
		 * 5. Pass query to executeBindQuery method for deletion.
		 */
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
	}
	

	static final int count(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final boolean distinct, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_DISTINCT_PARAMETER, distinct);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formCountQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}

			}
		}
		
		return 0;

	}
	
	static final int avg(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "avg(" + column + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "avg(" + column + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formAvgQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}
			
			}
		}
		
		return 0;
		
	}
	
	
	static final int sum(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "sum", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "sum", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formSumQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}

			}
		}
		
		return 0;
		
	}
	
	static final int total(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "total", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "total", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formTotalQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}

			}
		}
		
		return 0;
		
	}
	
	static final int min(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "min", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "min", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formMinQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}

			}
		}
		
		return 0;
		
	}
	
	static final int max(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "max", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "max", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formMaxQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {

				Object value = values.next();
				if(value instanceof Integer) {
					return ((Integer) value).intValue();
				} else if(value instanceof Long) {
					return ((Long) value).intValue();
				} else if(value instanceof Float) {
					return ((Float) value).intValue();
				}

			}
		}
		
		return 0;
		
	}
	
	static final String groupConcat(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String delimiter, final String whereClause, Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(databaseMappingDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "groupConcat", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "groupConcat", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER, databaseMappingDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER, delimiter);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER, having);
		
		
		String query = queryBuilder.formGroupConcatQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		while(datas.hasNext()) {
			Map<String, Object> data = datas.next();
			Collection<Object> parse = data.values();

			Iterator<Object> values = parse.iterator();
			while(values.hasNext()) {
				return ((String) values.next());
			}
		}
		
		return null;
		
	}

	/**
	 * Returns database descriptor object based on the POJO class called.

	 <pre>
Example:

	{@code
	try {
		DatabaseDescriptor databaseDescriptor = new Liquor().getDatabaseDescriptor();
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	</pre>
	
	 * @return Database Descriptor Object.
	 * @throws DatabaseException If any error occur while getting database descriptor object.
	 */
	static DatabaseDescriptor getDatabaseDescriptor(final String className) throws DatabaseException {
		return resourceManager.getDatabaseDescriptorBasedOnClassName(className);
	}
	
	/**
	 	Returns the actual database mapping object mapped for invoked class object.
	 
	 	<pre>
	 	
Example:
	{@code
	 			
	DatabaseMapping databaseMapping = null;
	try {
		databaseMapping = new Liquor().getDatabaseMapping();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	} 			
 		
	 	</pre>
	 	
	 	@return DatabaseMapping Object
	 	@throws DatabaseException If database mapping object not mapped for invoked class object.
	 */
	static DatabaseMappingDescriptor getDatabaseMappingDescriptor(final String className) throws DatabaseException {
		return resourceManager.requiredDatabaseMappingDescriptorBasedOnClassName(className);
	}

	/**
	 	Returns the mapped table name for invoked class object.
	 
	 	<pre>

Example:
	
	{@code
	
	String tableName = null;
	try {
		tableName = new Liquor().getTableName();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}
	 			
	 	</pre>
	 
	 	@return Mapped Table name.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static String getTableName(final Object object) throws DatabaseException {
		Siminov.isActive();

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		return databaseMappingDescriptor.getTableName();
	}
	
	
	/**
	 	Returns all column names of mapped table.
	 	
	 	<pre>

Example:
	
	{@code
	
	Iterator<String> columnNames = null;
	try {
		columnNames = new Liquor().getColumnNames();
	} catch(DatabaseException de) {
		//Log it.
	}
	 		
	} 			
	 	</pre>
	 	
	 	@return All column names of mapped table.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Iterator<String> getColumnNames(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		Iterator<DatabaseMappingDescriptor.Attribute> attributes = databaseMappingDescriptor.getAttributes();
		Collection<String> columnNames = new ArrayList<String>();
		
		while(attributes.hasNext()) {
			columnNames.add(attributes.next().getColumnName());
		}

		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(attributes.next().getColumnName());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(attributes.next().getColumnName());
				}
			}
		}
		

		return columnNames.iterator();
	}
	
	
	
	/**
	 	Returns all column values in the same order of column names for invoked class object.
	 	
	 	<pre>

Example:
	{@code
	
	Map<String, Object> values = null;
	try {
		values = new Liquor().getColumnValues();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}	
	 	</pre>
	 	
	 	@return All column values for invoked object.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Map<String, Object> getColumnValues(final Object object) throws DatabaseException {
		Siminov.isActive();

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		Map<String, Object> columnNameAndItsValues = new HashMap<String, Object>();
		Iterator<DatabaseMappingDescriptor.Attribute> attributes = databaseMappingDescriptor.getAttributes();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			try {
				columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "getColumnValues", siminovException.getMessage());
			} 
		}
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "getColumnValues", siminovException.getMessage());
					} 
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "getColumnValues", siminovException.getMessage());
					} 
				}
			}
		}
		
		return columnNameAndItsValues;
	}
	
	
	/**
	 	Returns all columns with there data types for invoked class object.
	
	 	<pre>

Example:

	{@code
	
	Map<String, String> columnTypes = null;
	try {
		columnTypes = new Liquor().getColumnTypes();
	} catch(DatabaseException de) {
		//Log it.
	}	
	
	} 		
	 	</pre>
	 	
	 	@return All columns with there data types.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Map<String, String> getColumnTypes(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		Map<String, String> columnTypes = new HashMap<String, String> ();
		Iterator<DatabaseMappingDescriptor.Attribute> attributes = databaseMappingDescriptor.getAttributes();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			columnTypes.put(attribute.getColumnName(), attribute.getType());
		}
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					columnTypes.put(attribute.getColumnName(), attribute.getType());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					columnTypes.put(attribute.getColumnName(), attribute.getType());
				}
			}
		}

		return columnTypes;		
	}

	
	/**
	 	Returns all primary keys of mapped table for invoked class object.
	 	
	 	<pre>

Example:
	 		
	{@code
	
	Iterator<String> primaryKeys = null;
	try {
		primaryKeys = new Liquor().getPrimeryKeys();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	} 	
	 	</pre>
	 
	 	@return All primary keys.
	 	@throws DatabaseException If not mapped table found for invoked class object.
	 */
	static Iterator<String> getPrimaryKeys(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor(object.getClass().getName());

		Iterator<Attribute> attributes = databaseMapping.getAttributes();
		Collection<String> primaryKeys = new ArrayList<String>();

		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			boolean isPrimary = attribute.isPrimaryKey();
			if(isPrimary) {
				primaryKeys.add(attribute.getColumnName());
			}
		}

		return primaryKeys.iterator();
	}
	
	
	/**
	 	Returns all mandatory fields which are associated with mapped table for invoked class object.
	 
	 	<pre>

Example:
	 			
	{@code
	
	Iterator<String> mandatoryFields = null;
	try {
		mandatoryFields = new Liquor().getMandatoryFields();
	} catch(DatabaseException de) {
		//Log it.
	}

	} 			
	 	</pre>
	 
	 	@return All mandatory fields for mapped table.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Iterator<String> getMandatoryFields(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
		Collection<String> isMandatoryFieldsVector = new ArrayList<String>();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			if(attribute.isNotNull()) {
				isMandatoryFieldsVector.add(attribute.getColumnName());
			}
		}
		

		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					if(attribute.isNotNull()) {
						isMandatoryFieldsVector.add(attribute.getColumnName());
					}
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					if(attribute.isNotNull()) {
						isMandatoryFieldsVector.add(attribute.getColumnName());
					}
				}
			}
		}
		
		return isMandatoryFieldsVector.iterator();
	}
	
	/**
	 	Returns all unique fields which are associated with mapped table for invoked class object.
	 
	 	<pre>

Example:
	 			
	{@code
	 			
	Iterator<String> uniqueFields = null;
	try {
		uniqueFields = new Liquor().getUniqueFields();
	} catch(DatabaseException de) {
		//Log it.
	}
	 		
	} 			
	 	</pre>
	 
	 	@return All unique fields for mapped table.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Iterator<String> getUniqueFields(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
		Collection<String> isUniqueFieldsVector = new ArrayList<String>();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			boolean isUnique = attribute.isUnique();
			if(isUnique) {
				isUniqueFieldsVector.add(attribute.getColumnName());
			}
		}
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {

					boolean isUnique = attribute.isUnique();
					if(isUnique) {
						isUniqueFieldsVector.add(attribute.getColumnName());
					}
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Attribute> parentAttributes = parentDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {

					boolean isUnique = attribute.isUnique();
					if(isUnique) {
						isUniqueFieldsVector.add(attribute.getColumnName());
					}
				}
			}
		}
		

		return isUniqueFieldsVector.iterator();
	}
	
	/**
	 	Returns all foreign keys of mapped table for invoked class object.
	 
	 	<pre>
	 		
Example:
	 			
	{@code
	 			
	Iterator<String> foreignKeys = null;
	try {
		foreignKeys = new Liquor().getForeignKeys();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	} 			
	 		
	 	</pre>
	 
	 	@return All foreign keys of mapped table.
	 	@throws DatabaseException If no mapped table found for invoked class object.
	 */
	static Iterator<String> getForeignKeys(final Object object) throws DatabaseException {
		Siminov.isActive();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Collection<Attribute> attributes = getForeignKeys(databaseMappingDescriptor);
		Iterator<Attribute> attributesIterate = attributes.iterator();
		
		Collection<String> foreignKeys = new ArrayList<String>();
		while(attributesIterate.hasNext()) {
			foreignKeys.add(attributesIterate.next().getColumnName());
		}
		
		return foreignKeys.iterator();
	}

	static Collection<Attribute> getForeignKeys(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		Iterator<Relationship> oneToManyRealtionships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = databaseMappingDescriptor.getManyToManyRelationships();
		
		Collection<Attribute> foreignAttributes = new ArrayList<Attribute>();
		
		Iterator<Attribute> attributes = databaseMappingDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			if(attribute.isPrimaryKey()) {
				foreignAttributes.add(attribute);
			}
		}
		
		while(oneToManyRealtionships.hasNext()) {
			
			Relationship relationship = oneToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Attribute> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignAttributes.add(referedForeignKeysIterate.next());
			}
		}

		while(manyToManyRealtionships.hasNext()) {
			
			Relationship relationship = manyToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Attribute> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignAttributes.add(referedForeignKeysIterate.next());
			}
		}

		return foreignAttributes;
	}
	
	/**
 		Iterates the provided cursor, and returns tuples in form of actual objects.
	 */
	static Iterator<Object> parseAndInflateData(final DatabaseMappingDescriptor databaseMappingDescriptor, Iterator<Map<String, Object>> values) throws DatabaseException {
		Siminov.isActive();

		Collection<Object> tuples = new LinkedList<Object>();
		while(values.hasNext()) {
			
			Map<String, Object> value = values.next();
			
			Set<String> columnNames = value.keySet();
			Iterator<String> columnNamesIterate = columnNames.iterator();
			
			Map<String, Object> data = new HashMap<String, Object>();
			while(columnNamesIterate.hasNext()) {
				String columnName = columnNamesIterate.next();
				
				if(databaseMappingDescriptor.containsAttributeBasedOnColumnName(columnName)) {
					data.put(databaseMappingDescriptor.getAttributeBasedOnColumnName(columnName).getSetterMethodName(), value.get(columnName));
				}
			}

			Object object = null;
			
			try {
				object = ClassUtils.createAndInflateObject(databaseMappingDescriptor.getClassName(), data);
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "parseAndInflateData", "SiminovException caught while create and inflate object through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "parseAndInflateData", siminovException.getMessage());
			} 
			
			tuples.add(object);
		}
		
		return tuples.iterator();
	}

	static void processOneToOneRelationship(final Object object) throws DatabaseException {

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<DatabaseMappingDescriptor.Relationship> oneToOneRelationships = databaseMappingDescriptor.getOneToOneRelationships();
		
		while(oneToOneRelationships.hasNext()) {
			
			DatabaseMappingDescriptor.Relationship oneToOneRelationship = oneToOneRelationships.next();

			boolean isLoad = oneToOneRelationship.isLoad();
			if(!isLoad) {
				continue;
			}

			
			StringBuilder whereClause = new StringBuilder();
			Iterator<String> foreignKeys = getPrimaryKeys(object);
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				Attribute attribute = databaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);
				Object columnValue = null;
				
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + attribute.getGetterMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
				}

				if(whereClause.length() <= 0) {
					whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
				} else {
					whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
				}
			}

			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToOneRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToOneRelationship.getReferTo());
				oneToOneRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Object referedObject = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
			Object[] referedObjects = (Object[]) referedObject;

			if(referedObjects == null || referedObjects.length <= 0) {
				return;
			}
			
			if(referedObjects[0] == null) {
				return;
			}
			
			try {
				ClassUtils.invokeMethod(object, oneToOneRelationship.getSetterReferMethodName(), new Class[] {referedObjects[0].getClass()}, new Object[] {referedObjects[0]});
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
			}
			
		}
	}

	static void processOneToManyRelationship(final Object object) throws DatabaseException {

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<DatabaseMappingDescriptor.Relationship> oneToManyRelationships = databaseMappingDescriptor.getOneToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			
			DatabaseMappingDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.next();
			
			boolean isLoad = oneToManyRelationship.isLoad();
			if(!isLoad) {
				continue;
			}

			
			StringBuilder whereClause = new StringBuilder();
			Iterator<String> foreignKeys = getPrimaryKeys(object);
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				Attribute attribute = databaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);
				Object columnValue = null;
				
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processOneToManyRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + attribute.getGetterMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
				}

				if(whereClause.length() <= 0) {
					whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
				} else {
					whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
				}
			}

			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = oneToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			
			Object referedObject = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
			Object[] referedObjects = (Object[]) referedObject;

			Collection<Object> referedCollection = new ArrayList<Object>();
			if(referedObjects != null && referedObjects.length > 0) {
				for(int i = 0;i < referedObjects.length;i++) {
					referedCollection.add(referedObjects[i]);
				}
			}
			
			try {
				ClassUtils.invokeMethod(object, oneToManyRelationship.getSetterReferMethodName(), new Class[] {Iterator.class}, new Object[] {referedCollection.iterator()});
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processOneToManyRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
			}
		}
		
	}
	
	static void processManyToOneRelationship(final Object object, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToOneRelationships = databaseMappingDescriptor.getManyToOneRelationships();
		
		while(manyToOneRelationships.hasNext()) {
			Relationship manyToOneRelationship = manyToOneRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToOneRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToOneRelationship.getReferTo());
				manyToOneRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Object referedObject = null;
			try {
				referedObject = ClassUtils.getValue(object, manyToOneRelationship.getGetterReferMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}
			
			processManyToOneRelationship(referedObject, columnNames, columnValues);
			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNames.add(attribute.getColumnName());
						columnValues.add(ClassUtils.getValue(referedObject, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + referedObject.getClass().getName() + ", " + " METHOD-NAME: " + attribute.getGetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
					} 
				}
			}
		}
	}
	
	static void processManyToOneRelationship(final Object object, final StringBuilder whereClause) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToOneRelationships = databaseMappingDescriptor.getManyToOneRelationships();

		while(manyToOneRelationships.hasNext()) {
			Relationship manyToOneRelationship = manyToOneRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToOneRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToOneRelationship.getReferTo());
				manyToOneRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Object referedObject = null;
			try {
				referedObject = ClassUtils.getValue(object, manyToOneRelationship.getGetterReferMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			processManyToOneRelationship(referedObject, whereClause);
			
			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					Object columnValue = null;
					try {
						columnValue = ClassUtils.getValue(referedObject, attribute.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
					} 

					
					if(whereClause.length() <= 0) {
						whereClause.append(attribute.getColumnName() + "= '" + columnValue + "'");
					} else {
						whereClause.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");
					}
				}
			}
		}

	}
	
	static void processManyToOneRelationship(final Object object, Map<String, Object> data) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToOneRelationships = databaseMappingDescriptor.getManyToOneRelationships();

		while(manyToOneRelationships.hasNext()) {
			Relationship manyToOneRelationship = manyToOneRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToOneRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToOneRelationship.getReferTo());
				manyToOneRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Object referedObject = ClassUtils.createClassInstance(manyToOneRelationship.getReferedDatabaseMappingDescriptor().getClassName());
			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			
			processManyToOneRelationship(referedObject, data);

			if(manyToOneRelationship.isLoad()) {

				StringBuilder whereClause = new StringBuilder();

				Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Attribute attribute = referedDatabaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);
					Object columnValue = data.get(attribute.getColumnName());

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}
				
				Object[] fetchedObjects = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
				referedObject = fetchedObjects[0];
				
			} else {
				Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Attribute attribute = referedDatabaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);

					Object columnValue = data.get(attribute.getColumnName());
					if(columnValue == null) {
						continue;
					}
					
					try {
						ClassUtils.invokeMethod(referedObject, attribute.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
					}
				}
			}
			

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			
			try {
				ClassUtils.invokeMethod(object, manyToOneRelationship.getSetterReferMethodName(), new Class[] {referedObject.getClass()}, new Object[] {referedObject});
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
			}
		}

	}

	static void processManyToManyRelationship(final Object object, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToManyRelationship.getReferTo());
				manyToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}
			
			Object referedObject = null;
			try {
				referedObject = ClassUtils.getValue(object, manyToManyRelationship.getGetterReferMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			processManyToManyRelationship(referedObject, columnNames, columnValues);

			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNames.add(attribute.getColumnName());
						columnValues.add(ClassUtils.getValue(object, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
					} 
				}
			}
		}
	}
	
	static void processManyToManyRelationship(final Object object, final StringBuilder whereClause) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToManyRelationships();
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToManyRelationship.getReferTo());
				manyToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}
			
			Object referedObject = null;
			try {
				referedObject = ClassUtils.getValue(object, manyToManyRelationship.getGetterReferMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			processManyToManyRelationship(referedObject, whereClause);

			Iterator<Attribute> parentAttributes = referedDatabaseMappingDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					Object columnValue = null;
					try {
						columnValue = ClassUtils.getValue(referedObject, attribute.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
					} 

					
					if(whereClause.length() <= 0) {
						whereClause.append(attribute.getColumnName() + "= '" + columnValue + "'");
					} else {
						whereClause.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");
					}
				}
			}
		}

		
	}
	
	static void processManyToManyRelationship(final Object object, Map<String, Object> data) throws DatabaseException {
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		Iterator<Relationship> manyToManyRelationships = databaseMappingDescriptor.getManyToOneRelationships();

		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			if(referedDatabaseMappingDescriptor == null) {
				referedDatabaseMappingDescriptor = getDatabaseMappingDescriptor(manyToManyRelationship.getReferTo());
				manyToManyRelationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			}

			Object referedObject = ClassUtils.createClassInstance(manyToManyRelationship.getReferedDatabaseMappingDescriptor().getClassName());
			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			
			processManyToManyRelationship(referedObject, data);
			
			if(manyToManyRelationship.isLoad()) {

				StringBuilder whereClause = new StringBuilder();

				Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Attribute attribute = referedDatabaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);
					Object columnValue = data.get(attribute.getColumnName());

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}
				
				Object[] fetchedObjects = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
				referedObject = fetchedObjects[0];

			} else {
				Iterator<String> primaryKeys = getPrimaryKeys(referedObject);
				while(primaryKeys.hasNext()) {
					String foreignKey = primaryKeys.next();
					Attribute attribute = referedDatabaseMappingDescriptor.getAttributeBasedOnColumnName(foreignKey);

					Object columnValue = data.get(attribute.getColumnName());
					if(columnValue == null) {
						continue;
					}
					
					try {
						ClassUtils.invokeMethod(referedObject, attribute.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
					}
				}
			}

			if(referedObject == null) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}
			
			try {
				ClassUtils.invokeMethod(object, manyToManyRelationship.getSetterReferMethodName(), new Class[] {referedObject.getClass()}, new Object[] {referedObject});
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
			}
		}
	}
}
