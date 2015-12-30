/**
 * [SIMINOV FRAMEWORK - CORE]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
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
import siminov.core.model.EntityDescriptor;
import siminov.core.model.EntityDescriptor.Attribute;
import siminov.core.model.EntityDescriptor.Index;
import siminov.core.model.EntityDescriptor.Relationship;
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
				
				Object value = values.next();
				if(value instanceof String) {
					currentDatabaseVersion = Double.valueOf((String) value);
				} else if(value instanceof Integer) {
					currentDatabaseVersion = Double.valueOf(((Integer) value).doubleValue());
				} else if(value instanceof Long) {
					currentDatabaseVersion = Double.valueOf(((Long) value).doubleValue());
				} else if(value instanceof Float) {
					currentDatabaseVersion = Double.valueOf(((Float) value).doubleValue());
				} else if(value instanceof Double) {
					currentDatabaseVersion = Double.valueOf(((Double) value).doubleValue());
				}
			}
		}

		
		if(currentDatabaseVersion == databaseDescriptor.getVersion()) {
			return;
		}
		
		Collection<EntityDescriptor> allEntityDescriptor = new ArrayList<EntityDescriptor>();
		Iterator<EntityDescriptor> allEntityDescriptorIterator = resourceManager.getEntityDescriptors();
		while(allEntityDescriptorIterator.hasNext()) {
			allEntityDescriptor.add(allEntityDescriptorIterator.next());
		}
		
		
		
		Collection<String> tableNames = new ArrayList<String>();

		Iterator<EntityDescriptor> entityDescriptors = databaseDescriptor.getEntityDescriptors();

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
		while(entityDescriptors.hasNext()) {
			EntityDescriptor entityDescriptor = entityDescriptors.next();
			
			boolean contain = false;
			for(String tableName: tableNames) {
				
				if(tableName.equalsIgnoreCase(entityDescriptor.getTableName())) {
					contain = true;
					break;
				}
			}
			
			if(contain) {
				DatabaseHelper.upgradeTable(entityDescriptor);
			} else {
				DatabaseHelper.createTable(entityDescriptor);
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
			
			for(EntityDescriptor entityDescriptor: allEntityDescriptor) {
				
				if(tableName.equalsIgnoreCase(entityDescriptor.getTableName())) {
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
	 * @param entityDescriptor object related to table.
	 * @throws DatabaseException If any exception thrown while upgrading table.
	 */
	public static void upgradeTable(final EntityDescriptor entityDescriptor) throws DatabaseException {

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "upgradeTable", "No Database Instance Found For, TABLE-NAME: " + entityDescriptor.getTableName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "upgradeTable", "No Database Instance Found For, TABLE-NAME: " + entityDescriptor.getTableName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		
		String tableInfoQuery = queryBuilder.formTableInfoQuery(parameters);
		Log.debug(DatabaseHelper.class.getName(), "upgradeTable", "Table Info Query: " + tableInfoQuery);
		
		
		Collection<Attribute> newAttributes = new ArrayList<Attribute>();
		Collection<String> oldAttributes = new ArrayList<String>();
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
		

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
			parameters.put(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
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
	  			<li> Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
	  				<p>
SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.
	  				
Example:
	{@code
		

<!-- Design Of EntityDescriptor.xml -->

<entity-descriptor>

    <!-- General Properties Of Table And Class -->
    
    	<!-- Mandatory Field -->
    		<!-- NAME OF TABLE -->
    <property name="table_name">name_of_table</property>
    
    	<!-- Mandatory Field -->
    		<!-- MAPPED CLASS NAME -->
    <property name="class_name">mapped_class_name</property>
    
    
    	<!-- Optional Field -->
    <attributes>
        
	    <!-- Column Properties Required Under This Table -->
	    
			<!-- Optional Field -->
		<attribute>
		    
			    <!-- Mandatory Field -->
					<!-- COLUMN_NAME: Mandatory Field -->
   		    <property name="column_name">column_name_of_table</property>
		    			
    		    <!-- Mandatory Field -->
					<!-- VARIABLE_NAME: Mandatory Field -->
		    <property name="variable_name">class_variable_name</property>
		    		    
			    <!-- Mandatory Field -->
			<property name="type">java_variable_data_type</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="primary_key">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="not_null">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="unique">true/false</property>
			
				<!-- Optional Field -->
			<property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
			
				<!-- Optional Field -->
			<property name="default">default_value_of_column (Eg: 0.1)</property>
		
		</attribute>		

    </attributes>
		
		
		<!-- Optional Field -->
    <indexes>
        
		<!-- Index Properties -->
		<index>
		    
			    <!-- Mandatory Field -->
			    	<!-- NAME OF INDEX -->
		    <property name="name">name_of_index</property>
		    
			    <!-- Mandatory Field -->
					<!-- UNIQUE: Optional Field (Default is false) -->
		    <property name="unique">true/false</property>
		    
		    	<!-- Optional Field -->
		    		<!-- Name of the column -->
		    <property name="column">column_name_needs_to_add</property>
		    
		</index>
        
    </indexes>
    
		
	<!-- Map Relationship Properties -->
				
		<!-- Optional Field's -->	
	<relationships>
		    
	    <relationship>
	        
	        	<!-- Mandatory Field -->
	        		<!-- Type of Relationship -->
	        <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER -->
	        <property name="refer">class_variable_name</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER TO -->
	        <property name="refer_to">map_to_class_name</property>
	            
	        	<!-- Optional Field -->
	        <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>    
	            
	        	<!-- Optional Field -->    
	        <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>    
	            
				<!-- Optional Field (Default is false) -->
	       	<property name="load">true/false</property>	            
	        
	    </relationship>
	    
	</relationships>

</entity-descriptor>
	
			}
					</p>
	  			</li>
	  		</ul> 
	  	</pre>
	  </p>
	  
	 * @param entityDescriptors Entity Descriptor objects which defines the structure of each table.
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public static void createTables(final Iterator<EntityDescriptor> entityDescriptors) throws DatabaseException {

		while(entityDescriptors.hasNext()) {
			createTable(entityDescriptors.next());
		}
	}

	/**
	   Is used to create a new table in an database.
	  	<p>
			<pre> Manually creating table structure using Entity Descriptor mapped class. 
  					
	Example: 
	
		{@code
	
		Book book = new Book();
		
		try {
			Database.createTables(book.getEntityDescriptor());
		} catch(DatabaseException databaseException) {
			//Log It.
		}
		
		}
			</pre>
		</p>
	 * @param entityDescriptor Entity Descriptor object which defines the structure of table.
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public static void createTable(final EntityDescriptor entityDescriptor) throws DatabaseException {

		/*
		 * 1. Get IDatabase with respect to current entity descriptor class name.
		 * 2. Get Table Name, and all columns.
		 * 3. Get all attributes and properties from entity descriptor.
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

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());

		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		IDataTypeHandler dataTypeHandler = databaseBundle.getDataTypeHandler();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "createTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "createTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}
		
		String tableName = entityDescriptor.getTableName();

		/*
		 * Get all attributes and properties from entity descriptor. 
		 * LIKE(COLUMN NAMES, COLUMN TYPES, DEFAULT VALUES, CHECKS, NOT NULL, PRIMARY KEYS, UNIQUE's ).
		 */
		Collection<String> columnNames = new LinkedList<String>();
		Collection<String> columnTypes = new LinkedList<String>();
		
		Collection<String> defaultValues = new LinkedList<String>();
		Collection<String> checks = new LinkedList<String>();
		
		Collection<Boolean> isNotNull = new LinkedList<Boolean>();
		
		Collection<String> primaryKeys = new LinkedList<String>();
		Collection<String> uniqueKeys = new LinkedList<String>();
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();

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
		 * Add All Relationships
		 */
		Iterator<Relationship> oneToOneRelationships = entityDescriptor.getOneToOneRelationships();
		Iterator<Relationship> manyToOneRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();

		
		while(oneToOneRelationships.hasNext()) {
			Relationship oneToOneRelationship = oneToOneRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToOneRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToOneRelationship.getReferTo());
				oneToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}
			
			
			Collection<Attribute> foreignAttributes = getForeignKeys(referedEntityDescriptor);
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
		
		
		while(manyToOneRelationships.hasNext()) {
			Relationship oneToManyRelationship = manyToOneRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			Collection<Attribute> foreignAttributes = getForeignKeys(referedEntityDescriptor);
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
			EntityDescriptor referedEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(manyToManyRelationship.getReferTo());
				manyToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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

			Iterator<Relationship> referedOneToOneRelationships = referedEntityDescriptor.getOneToOneRelationships();
			while(referedOneToOneRelationships.hasNext()) {

				Relationship referedOneToOneRelationship = referedOneToOneRelationships.next();
				EntityDescriptor referedOneToOneEntityDescriptor = referedOneToOneRelationship.getReferedEntityDescriptor();
				if(referedOneToOneEntityDescriptor == null) {
					referedOneToOneEntityDescriptor = getEntityDescriptor(referedOneToOneRelationship.getReferTo());
					referedOneToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Collection<Attribute> foreignAttributes = getForeignKeys(referedOneToOneEntityDescriptor);
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
		}

		/*
		 * If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined, 
		 * because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
		 * Else get foreign keys.
		 */
		String foreignKeys = "";
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER, entityDescriptor);
		
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
		database.executeQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
		
		
		/*
		 * Create Index for table if its defined.
		 */
		Iterator<Index> indexes = entityDescriptor.getIndexes();
		while(indexes.hasNext()) {
			
			/*
			 * Get all attributes and properties of index.
			 * LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
			 * 
			 * After forming index query call executeQuery method to create index.
			 */
			
			createIndex(entityDescriptor, indexes.next());
		}

		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onTableCreated(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor);
		}
	}

	/**
	 * It drop's the table from database based on entity descriptor.
	  	<p>
			<pre> Drop the Book table.
	
	{@code
	
	EntityDescriptor entityDescriptor = new Book().getEntityDescriptor();
	
	try {
		Database.dropTable(entityDescriptor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	
			</pre>
		</p>
	 * @param entityDescriptor Entity Descriptor object which defines the structure of table.
	 * @throws DatabaseException If not able to drop table.
	 */
	static void dropTable(final EntityDescriptor entityDescriptor) throws DatabaseException {

		Siminov.isActive();

		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}
		

		String tableName = entityDescriptor.getTableName();
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);

		database.executeQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, queryBuilder.formDropTableQuery(parameters));
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onTableDropped(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor);
		}
	}
	
	
	/**
	   Is used to create a new index on a table in database.
	  	<p>
			<pre> Create Index On Book table.
	
	{@code
	
	EntityDescriptor.Index indexOnBook = entityDescriptor.new Index();
	indexOnBook.setName("BOOK_INDEX_BASED_ON_AUTHOR");
	indexOnBook.setUnique(true);
	
	//Add Columns on which we need index.
	indexOnBook.addColumn("LINK");

	EntityDescriptor entityDescriptor = new Book().getEntityDescriptor();

	try {
		Database.createIndex(entityDescriptor, indexOnBook);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param entityDescriptor Entity Descriptor object which defines the structure of table.
	 * @param index Index object which defines the structure of index needs to create.
	 * @throws DatabaseException If not able to create index on table.
	 */
	static void createIndex(final EntityDescriptor entityDescriptor, final Index index) throws DatabaseException {
		
		String indexName = index.getName();
		Iterator<String> columnNames = index.getColumns();
		boolean isUnique = index.isUnique();

		createIndex(entityDescriptor, indexName, columnNames, isUnique);
	}

	/**
	   Is used to create a new index on a table in database.
	  	<p>
			<pre> Create Index On Book table.
	
	{@code
	
	String indexName = "BOOK_INDEX_BASED_ON_AUTHOR";
	boolean isUnique = true;
	
	Collection<String> columnNames = new ArrayList<String>();
	columnNames.add("LINK");
	
	try {
		new Book().createIndex(indexName, columnNames.iterator(), isUnique);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param entityDescriptor Entity Descriptor object which defines the structure of table.
	 * @param indexName Name of index.
	 * @param columnNames Iterator over column names.
	 * @param isUnique true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
	 * @throws DatabaseException If not able to create index on table.
	 */
	static void createIndex(final EntityDescriptor entityDescriptor, final String indexName, final Iterator<String> columnNames, final boolean isUnique) throws DatabaseException {
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "createIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "createIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		final Collection<String> columnNamesCollection = new ArrayList<String> ();
		while(columnNames.hasNext()) {
			columnNamesCollection.add(columnNames.next());
		}

		
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER, columnNamesCollection.iterator());
		parameters.put(IQueryBuilder.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER, isUnique);
		
		
		String query = queryBuilder.formCreateIndexQuery(parameters);
		database.executeQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			Index index = new Index();
			index.setName(indexName);
			index.setUnique(isUnique);
			
			Iterator<String> columnNamesIterator = columnNamesCollection.iterator();
			while(columnNamesIterator.hasNext()) {
				index.addColumn(columnNamesIterator.next());
			}
			
			databaseEventHandler.onIndexCreated(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, index);
		}
	}

	/**
	   Is used to drop a index on a table in database.
	  	<p>
			<pre> Create Index On Book table.
	
	{@code
	
	String indexName = "BOOK_INDEX_BASED_ON_AUTHOR";
	EntityDescriptor entityDescriptor = new Book().getEntityDescriptor();
	
	try {
		Database.dropIndex(entityDescriptor, indexName);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param entityDescriptor Entity Descriptor object which defines the structure of table.
	 * @param indexName Name of a index needs to be drop.
	 * @throws DatabaseException If not able to drop index on table.
	 */
	static void dropIndex(final EntityDescriptor entityDescriptor, final String indexName) throws DatabaseException {
		Siminov.isActive();

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());

		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
		}

		
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);

		
		String query = queryBuilder.formDropIndexQuery(parameters);
		database.executeQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
		
		IDatabaseEvents databaseEventHandler = resourceManager.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.onIndexDropped(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, entityDescriptor.getIndex(indexName));
		}
	}
	
	
	/**
	 * It drop's the whole database based on database-descriptor.
	  	<p>
			<pre> Drop the Book table.
	
	{@code
	
	DatabaseDescriptor databaseDescriptor = new Book().getDatabaseDescriptor();
	
	try {
		Database.dropDatabase(databaseDescriptor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param databaseDescriptor Database Descriptor object which defines the structure of table.
	 * @throws DatabaseException If not able to drop database.
	 */
	static void dropDatabase(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.isActive();

		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "dropDatabase", "No Database Instance Found For ENTITY-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "dropDatabase", "No Database Instance Found For ENTITY-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
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

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	DatabaseDescriptor databaseDescriptor = cBook.getDatabaseDescriptor();
  
	try {
		Database.beginTransaction(databaseDescriptor);

		cBook.save();
  
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
		 * 1. Get entity descriptor object for mapped invoked class object.
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

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	DatabaseDescriptor databaseDescriptor = cBook.getDatabaseDescriptor();
  
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
		 * 1. Get entity descriptor object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}

		database.executeMethod(SQLITE_DATABASE_COMMIT_TRANSACTION, null);
		
		try {
			database.executeMethod(SQLITE_DATABASE_END_TRANSACTION, true);
		} catch(DatabaseException databaseException) {
			Log.error(DatabaseHelper.class.getName(), "commitTransaction", "DatabaseException caught while executing end transaction method, " + databaseException.getMessage());
			throw new DeploymentException(DatabaseHelper.class.getName(), "commitTransaction", databaseException.getMessage());
		}
	}

	
	static Object[] select(final Object object, final Object parentObject, final EntityDescriptor entityDescriptor, final boolean distinct, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBy, final String having, final Iterator<String> orderBy, final String whichOrderBy, final String limit) throws DatabaseException {
		/*
		 * 1. Get entity descriptor object for mapped invoked class object.
		 * 2. Traverse group by's and form a single string.
		 * 3. Traverse order by'z and form a single string.
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
		 */
		
		Siminov.isActive();
		
		/*
		 * 1. Get entity descriptor object for mapped invoked class object.
		 */
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */

		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER, distinct);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER, columnNames);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER, groupBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER, having);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER, orderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER, whichOrderBy);
		parameters.put(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER, limit);
		
		
		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, queryBuilder.formSelectQuery(parameters));
		Collection<Map<String, Object>> datasBundle = new LinkedList<Map<String,Object>>();
		while(datas.hasNext()) {
			datasBundle.add(datas.next());
		}
		
		Iterator<Object> tuples = parseAndInflateData(object, parentObject, entityDescriptor, datasBundle.iterator());
		datas = datasBundle.iterator();	
		
		/*
		 * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 */
		Collection<Object> tuplesCollection = new LinkedList<Object> ();
		if(object != null) {
			
			while(tuples.hasNext() && datas.hasNext()) {
				Object tuple = tuples.next();
				Map<String, Object> data = datas.next();
				
				tuplesCollection.add(tuple);
				
				//processOneToOneRelationship(tuple, parentObject);
				//processOneToManyRelationship(tuple, parentObject);

				//processManyToOneRelationship(tuple, parentObject, data);
				//processManyToManyRelationship(tuple, parentObject, data);
				
				RelationshipHelper.processRelationship(tuple, parentObject);
				RelationshipHelper.processRelationship(tuple, parentObject, data);
				
			}
		}
		
		Class<?> classObject = null;
		try {
			classObject = Class.forName(entityDescriptor.getClassName());
		} catch(Exception exception) {
			Log.error(DatabaseHelper.class.getName(), "select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
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
	
	String query = "SELECT * FROM BOOK";
	
	Book[] books = null;
	try {
		books = new Book().select(query);
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
		 * 1. Get entity descriptor object for mapped invoked class object.
		 */
		
		//EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		query.trim();
		String tableName = query.substring(query.indexOf(Constants.DATABASE_QUERY_FROM_TABLE_INDEX) + 5, query.length());
		if(tableName.indexOf(" ") != -1) {
			tableName = tableName.substring(0, tableName.indexOf(" "));
		}
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptorBasedOnTableName(tableName);
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + tableName);
			throw new DeploymentException(DatabaseHelper.class.getName(), "select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + tableName);
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		Iterator<Object> tuples = parseAndInflateData(object, null, null, database.executeSelectQuery(databaseDescriptor, null, query));
			
		/*
		 * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
		 */
		
		Collection<Object> tuplesCollection = new LinkedList<Object> ();
		while(tuples.hasNext()) {
			Object tuple = tuples.next();
			tuplesCollection.add(tuple);
		}

		Class<?> classObject = null;
		try {
			classObject = Class.forName(object.getClass().getName());
		} catch(Exception exception) {
			Log.error(DatabaseHelper.class.getName(), "manualFetch", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + object.getClass().getName());
			throw new DatabaseException(DatabaseHelper.class.getName(), "manualFetch", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + object.getClass().getName());
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
	   	
Example: Make Book Object

	{@code

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	try {
		cBook.save();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}

	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void save(final Object object) throws DatabaseException {
		save(object, null);
	}
	
	
	private static void save(final Object object, final Object parentObject) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get mapped entity descriptor object for object parameter class name.
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
		 * 1. Get mapped entity descriptor object for invoked class object.
		 */
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "save", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "save", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
		 */
		String tableName = entityDescriptor.getTableName();
		
		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();

		Iterator<EntityDescriptor.Attribute> attributes = entityDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			try {
				columnNames.add(attribute.getColumnName());
				columnValues.add(ClassUtils.getValue(object, attribute.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
			} 
		}
		
		
		RelationshipHelper.processRelationship(object, null, columnNames, columnValues);
		
		
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
		database.executeBindQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query, columnValues.iterator());
		
		
		/*
		 * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
		 */
		Iterator<EntityDescriptor.Relationship> relationships = entityDescriptor.getRelationships();
		while(relationships.hasNext()) {
			EntityDescriptor.Relationship relationship = relationships.next();
			
			boolean isLoad = relationship.isLoad();
			if(!isLoad) {
				continue;
			}
			
			String relationshipType = relationship.getType();
			if(relationshipType == null || relationshipType.length() <= 0) {
				continue;
			}
			
			if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE)) {
				
				if(parentObject != null && relationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				/*
				 * Get Refer Object
				 */
				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(referedObject == null) {
					continue;
				}

				/*
				 * Inflate Dependent Object
				 */
				EntityDescriptor referedEntityDescriptor = getEntityDescriptor(referedObject.getClass().getName());
				Relationship referedRelationship = referedEntityDescriptor.getRelationshipBasedOnReferTo(object.getClass().getName());
				
				
				try {
					ClassUtils.invokeMethod(referedObject, referedRelationship.getSetterReferMethodName(), new Class[] {object.getClass()}, new Object[] {object});
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while setting up one to one relationship mapping, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				}
				
				saveOrUpdate(referedObject, object);
			} else if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY)) {
				Iterator<?> values = null;
				try {
					values = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(values == null) {
					continue;
				}
				
				while(values.hasNext()) {
					saveOrUpdate(values.next());
				}
			} else if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY)) {
				
				if(parentObject != null && relationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				/*
				 * Get Refer Object
				 */
				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(referedObject == null) {
					continue;
				}
				
				saveOrUpdate(referedObject, object);
			}
		}
	}

	
	/**
		It updates a record to any single table in a relational database.
	
	   	<pre>

Example: Make Beer Object

	{@code

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	try {
		cBook.update();
	} catch(DatabaseException de) {
		//Log it.
	}

	}
	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void update(final Object object) throws DatabaseException {
		update(object, null);
	}
	
	private static void update(final Object object, final Object parentObject) throws DatabaseException {
		Siminov.isActive();
	
		/*
		 * 1. Get mapped entity descriptor object for object parameter class name.
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
		 * 1. Get mapped entity descriptor object for invoked class object.
		 */
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "update", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "update", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		StringBuilder whereClause = new StringBuilder();
		String tableName = entityDescriptor.getTableName();

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		Iterator<EntityDescriptor.Attribute> attributes = entityDescriptor.getAttributes();

		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			Object columnValue = null;
			try {
				columnNames.add(attribute.getColumnName());
				columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
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
		
		
		RelationshipHelper.processRelationship(object, parentObject, whereClause);
		RelationshipHelper.processRelationship(object, parentObject, columnNames, columnValues);
		
		
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
		database.executeBindQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query, values);
		
		/*
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		Iterator<EntityDescriptor.Relationship> relationships = entityDescriptor.getRelationships();
		while(relationships.hasNext()) {
			EntityDescriptor.Relationship relationship = relationships.next();
			
			boolean isLoad = relationship.isLoad();
			if(!isLoad) {
				continue;
			}
			
			String relationshipType = relationship.getType();
			if(relationshipType == null || relationshipType.length() <= 0) {
				continue;
			}
			
			if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				saveOrUpdate(value);
			} else if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY)) {
				Iterator<?> relationshipValues = null;
				try {
					relationshipValues = (Iterator<?>) ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(relationshipValues == null) {
					continue;
				}
				
				while(relationshipValues.hasNext()) {
					saveOrUpdate(relationshipValues.next());
				}
			} else if(relationshipType.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				saveOrUpdate(value);
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

	Book cBook = new Book();
	cBook.setTitle(Book.BOOK_TYPE_C);
	cBook.setDescription(applicationContext.getString(R.string.c_description));
	cBook.setAuthor(applicationContext.getString(R.string.c_author));
	cBook.setLink(applicationContext.getString(R.string.c_link));

	try {
		cBook.saveOrUpdate();
	} catch(DatabaseException de) {
		//Log it.
	}
			
	}			
				
	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	static void saveOrUpdate(final Object object) throws DatabaseException {
		saveOrUpdate(object, null);
	}
	
	
	private static void saveOrUpdate(final Object object, final Object parentObject) throws DatabaseException {
		Siminov.isActive();
		
		/*
		 * 1. Get mapped entity descriptor object for object class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys to fetch objects from database table. IF EXISTS: call update method, ELSE: class save method.
		 * 4. IF EXISTS: call update method, ELSE: call save method.
		 */
		
		if(object == null) {
			Log.debug(DatabaseHelper.class.getName(), "saveOrUpdate", "Invalid Object Found.");
			return;
		}
		
		/*
		 * 1. Get mapped entity descriptor object for object class name.
		 */
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "saveOrUpdate", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "saveOrUpdate", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		
		StringBuilder whereClause = new StringBuilder();
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();

			if(attribute.isPrimaryKey()) {
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "saveOrUpdate", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "saveOrUpdate", siminovException.getMessage());
				} 

				
				if(whereClause.length() <= 0) {
					whereClause.append(attribute.getColumnName() + "= '" + columnValue + "'");
				} else {
					whereClause.append(" AND " + attribute.getColumnName() + "= '" + columnValue + "'");
				}
			}
		}
		
		
		RelationshipHelper.processRelationship(object, null, whereClause);
		
		
		if(whereClause == null || whereClause.length() <= 0) {
			save(object, parentObject);
			return;
		}

		
		/*
		 * 4. IF EXISTS: call update method, ELSE: call save method.
		 */
		int count = count(entityDescriptor, null, false, whereClause.toString(), null, null);
		if(count <= 0) {
			save(object, parentObject);
		} else {
			update(object, parentObject);
		}
	}
	

	static void delete(final Object object, final String whereClause) throws DatabaseException {
		/*
		 * 1. Get mapped entity descriptor object for object parameter class name.
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
		 * 1. Get mapped entity descriptor object for object parameter class name.
		 */
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "delete", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "delete", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}
		
		StringBuilder where = new StringBuilder();
		
		if(whereClause == null || whereClause.length() <= 0) {
			Iterator<Attribute> attributes = entityDescriptor.getAttributes();
			
			while(attributes.hasNext()) {
				Attribute attribute = attributes.next();
				
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "delete", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
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

			RelationshipHelper.processRelationship(object, null, where);
		} else {
			where.append(whereClause);
		}
		
		/*
		 * 4. Using QueryBuilder form update bind query.
		 */
		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER, where.toString());

		
		String query = queryBuilder.formDeleteQuery(parameters);
		/*
		 * 5. Pass query to executeBindQuery method for deletion.
		 */
		database.executeQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
	}
	

	static final int count(final EntityDescriptor entityDescriptor, final String column, final boolean distinct, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_DISTINCT_PARAMETER, distinct);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_COUNT_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formCountQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	static final int avg(final EntityDescriptor entityDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "avg(" + column + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "avg(" + column + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_AVG_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formAvgQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	
	static final int sum(final EntityDescriptor entityDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "sum", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "sum", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_SUM_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formSumQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	static final int total(final EntityDescriptor entityDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "total", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "total", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_TOTAL_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formTotalQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	static final int min(final EntityDescriptor entityDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();

		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "min", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "min", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_MIN_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formMinQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	static final int max(final EntityDescriptor entityDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "max", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "max", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_MAX_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formMaxQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
	
	static final String groupConcat(final EntityDescriptor entityDescriptor, final String column, final String delimiter, final String whereClause, Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.isActive();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptor(entityDescriptor.getClassName());
		DatabaseBundle databaseBundle = resourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
		
		IDatabaseImpl database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.error(DatabaseHelper.class.getName(), "groupConcat", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
			throw new DeploymentException(DatabaseHelper.class.getName(), "groupConcat", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.getClassName());
		}

		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.getTableName());
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER, column);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER, delimiter);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER, groupBys);
		parameters.put(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER, having);
		
		
		String query = queryBuilder.formGroupConcatQuery(parameters);

		Iterator<Map<String, Object>> datas = database.executeSelectQuery(getDatabaseDescriptor(entityDescriptor.getClassName()), entityDescriptor, query);
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
		DatabaseDescriptor databaseDescriptor = new Book().getDatabaseDescriptor();
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
	
	static DatabaseDescriptor getDatabaseDescriptorBasedOnTableName(final String tableName) throws DatabaseException {
		return resourceManager.getDatabaseDescriptorBasedOnTableName(tableName);
	}
	
	/**
	 	Returns the actual entity descriptor object mapped for invoked class object.
	 
	 	<pre>
	 	
Example:
	{@code
	 			
	EntityDescriptor entityDescriptor = null;
	try {
		entityDescriptor = new Book().getEntityDescriptor();
	} catch(DatabaseException de) {
		//Log it.
	}
	
	} 			
 		
	 	</pre>
	 	
	 	@return EntityDescriptor Object
	 	@throws DatabaseException If entity descriptor object not mapped for invoked class object.
	 */
	static EntityDescriptor getEntityDescriptor(final String className) throws DatabaseException {
		return resourceManager.requiredEntityDescriptorBasedOnClassName(className);
	}

	/**
	 	Returns the mapped table name for invoked class object.
	 
	 	<pre>

Example:
	
	{@code
	
	String tableName = null;
	try {
		tableName = new Book().getTableName();
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

		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		return entityDescriptor.getTableName();
	}
	
	
	/**
	 	Returns all column names of mapped table.
	 	
	 	<pre>

Example:
	
	{@code
	
	Iterator<String> columnNames = null;
	try {
		columnNames = new Book().getColumnNames();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		
		Iterator<EntityDescriptor.Attribute> attributes = entityDescriptor.getAttributes();
		Collection<String> columnNames = new ArrayList<String>();
		
		while(attributes.hasNext()) {
			columnNames.add(attributes.next().getColumnName());
		}

		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
			EntityDescriptor parentEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			
			Iterator<Attribute> parentAttributes = parentEntityDescriptor.getAttributes();
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
		values = new Book().getColumnValues();
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

		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		
		Map<String, Object> columnNameAndItsValues = new HashMap<String, Object>();
		Iterator<EntityDescriptor.Attribute> attributes = entityDescriptor.getAttributes();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			
			try {
				columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "getColumnValues", siminovException.getMessage());
			} 
		}
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "getColumnValues", siminovException.getMessage());
					} 
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			EntityDescriptor parentEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			
			Iterator<Attribute> parentAttributes = parentEntityDescriptor.getAttributes();
			while(parentAttributes.hasNext()) {
				Attribute attribute = parentAttributes.next();
				
				boolean isPrimary = attribute.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(attribute.getColumnName(), ClassUtils.getValue(object, attribute.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
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
		columnTypes = new Book().getColumnTypes();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		
		Map<String, String> columnTypes = new HashMap<String, String> ();
		Iterator<EntityDescriptor.Attribute> attributes = entityDescriptor.getAttributes();
		
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			columnTypes.put(attribute.getColumnName(), attribute.getType());
		}
		
		/*
		 * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
		 */
		Iterator<Relationship> oneToManyRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
			EntityDescriptor parentEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			
			Iterator<Attribute> parentAttributes = parentEntityDescriptor.getAttributes();
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
		primaryKeys = new Book().getPrimeryKeys();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());

		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
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
		mandatoryFields = new Book().getMandatoryFields();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
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
		Iterator<Relationship> oneToManyRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
			EntityDescriptor parentEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			
			Iterator<Attribute> parentAttributes = parentEntityDescriptor.getAttributes();
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
		uniqueFields = new Book().getUniqueFields();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
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
		Iterator<Relationship> oneToManyRelationships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = oneToManyRelationships.next();
			EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
			if(referedEntityDescriptor == null) {
				referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
				oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
			}

			
			Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
			EntityDescriptor parentEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
			
			Iterator<Attribute> parentAttributes = parentEntityDescriptor.getAttributes();
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
		foreignKeys = new Book().getForeignKeys();
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
		
		EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
		Collection<Attribute> attributes = getForeignKeys(entityDescriptor);
		Iterator<Attribute> attributesIterate = attributes.iterator();
		
		Collection<String> foreignKeys = new ArrayList<String>();
		while(attributesIterate.hasNext()) {
			foreignKeys.add(attributesIterate.next().getColumnName());
		}
		
		return foreignKeys.iterator();
	}

	static Collection<Attribute> getForeignKeys(EntityDescriptor entityDescriptor) throws DatabaseException {
		Iterator<Relationship> oneToManyRealtionships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = entityDescriptor.getManyToManyRelationships();
		
		Collection<Attribute> foreignAttributes = new ArrayList<Attribute>();
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			if(attribute.isPrimaryKey()) {
				foreignAttributes.add(attribute);
			}
		}
		
		while(oneToManyRealtionships.hasNext()) {
			
			Relationship relationship = oneToManyRealtionships.next();
			EntityDescriptor referedEntityDescriptor = relationship.getReferedEntityDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedEntityDescriptor);
			Iterator<Attribute> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignAttributes.add(referedForeignKeysIterate.next());
			}
		}

		while(manyToManyRealtionships.hasNext()) {
			
			Relationship relationship = manyToManyRealtionships.next();
			EntityDescriptor referedEntityDescriptor = relationship.getReferedEntityDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedEntityDescriptor);
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
	static Iterator<Object> parseAndInflateData(final Object object, final Object parentObject, final EntityDescriptor entityDescriptor, Iterator<Map<String, Object>> values) throws DatabaseException {
		Siminov.isActive();

		Collection<Object> tuples = new LinkedList<Object>();
		while(values.hasNext()) {
			
			Map<String, Object> value = values.next();
			
			Set<String> columnNames = value.keySet();
			Iterator<String> columnNamesIterate = columnNames.iterator();
			
			Map<String, Object> data = new HashMap<String, Object>();
			while(columnNamesIterate.hasNext()) {
				String columnName = columnNamesIterate.next();
				
				if(entityDescriptor != null && entityDescriptor.containsAttributeBasedOnColumnName(columnName)) {
					data.put(entityDescriptor.getAttributeBasedOnColumnName(columnName).getSetterMethodName(), value.get(columnName));
				}
			}

			Object inflatedObject = null;
			String className;
			
			if(entityDescriptor == null) {
				className = object.getClass().getName();				
			} else {
				className = entityDescriptor.getClassName();
			}
			
			
			try {
				inflatedObject = ClassUtils.createAndInflateObject(className, data);
			} catch(SiminovException siminovException) {
				Log.error(DatabaseHelper.class.getName(), "parseAndInflateData", "SiminovException caught while create and inflate object through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(DatabaseHelper.class.getName(), "parseAndInflateData", siminovException.getMessage());
			} 
			
			tuples.add(inflatedObject);
		}
		
		return tuples.iterator();
	}

	
	private static class RelationshipHelper {
		
		public static void processRelationship(final Object object, final Object parentObject) throws DatabaseException {
			
			processOneToOneRelationship(object, parentObject);
			processOneToManyRelationship(object, parentObject);
			processManyToOneRelationship(object, parentObject);
			processManyToManyRelationship(object, parentObject);
		}
		
		
		public static void processRelationship(final Object object, final Object parentObject, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
			
			processOneToOneRelationship(object, parentObject, columnNames, columnValues);
			processOneToManyRelationship(object, parentObject, columnNames, columnValues);
			processManyToOneRelationship(object, parentObject, columnNames, columnValues);
			processManyToManyRelationship(object, parentObject, columnNames, columnValues);
		}
		
		
		public static void processRelationship(final Object object, final Object parentObject, final StringBuilder whereClause) throws DatabaseException {
			
			processOneToOneRelationship(object, parentObject, whereClause);
			processOneToManyRelationship(object, parentObject, whereClause);
			processManyToOneRelationship(object, parentObject, whereClause);
			processManyToManyRelationship(object, parentObject, whereClause);
		}		
		
		public static void processRelationship(final Object object, final Object parentObject, Map<String, Object> data) throws DatabaseException {
			
			processOneToOneRelationship(object, parentObject, data);
			processOneToManyRelationship(object, parentObject, data);
			processManyToOneRelationship(object, parentObject, data);
			processManyToManyRelationship(object, parentObject, data);
		}
		
		
		/*
		 * Process One To One Relationship
		 */

		private static void processOneToOneRelationship(final Object object, final Object parentObject) throws DatabaseException {

			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<EntityDescriptor.Relationship> oneToOneRelationships = entityDescriptor.getOneToOneRelationships();
			
			while(oneToOneRelationships.hasNext()) {
				
				EntityDescriptor.Relationship oneToOneRelationship = oneToOneRelationships.next();

				boolean isLoad = oneToOneRelationship.isLoad();
				if(!isLoad) {
					continue;
				}

				
				StringBuilder whereClause = new StringBuilder();
				Iterator<String> foreignKeys = getPrimaryKeys(object);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Attribute attribute = entityDescriptor.getAttributeBasedOnColumnName(foreignKey);
					Object columnValue = null;
					
					try {
						columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + attribute.getGetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
					}

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}

				EntityDescriptor referedEntityDescriptor = oneToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(oneToOneRelationship.getReferTo());
					oneToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				
				if(object != null && object.getClass().getName().equalsIgnoreCase(referedEntityDescriptor.getClassName())) {
					continue;
				}
				
				Object referedObject = select(null, null, referedEntityDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
				Object[] referedObjects = (Object[]) referedObject;

				if(referedObjects == null || referedObjects.length <= 0) {
					continue;
				}
				
				if(referedObjects[0] == null) {
					continue;
				}
				
				try {
					ClassUtils.invokeMethod(object, oneToOneRelationship.getSetterReferMethodName(), new Class[] {referedObjects[0].getClass()}, new Object[] {referedObjects[0]});
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
				}
			}
		}


		private static void processOneToOneRelationship(final Object object, final Object parentObject, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> oneToOneRelationships = entityDescriptor.getOneToOneRelationships();
			
			while(oneToOneRelationships.hasNext()) {
				Relationship oneToOneRelationship = oneToOneRelationships.next();
				if(parentObject != null && oneToOneRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = oneToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(oneToOneRelationship.getReferTo());
					oneToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, oneToOneRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + oneToOneRelationship.getReferTo());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + oneToOneRelationship.getReferTo());
				}
				
				
				processOneToManyRelationship(referedObject, object, columnNames, columnValues);
				processManyToOneRelationship(referedObject, object, columnNames, columnValues);
				processManyToManyRelationship(referedObject, object, columnNames, columnValues);
				
				
				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
		
		
		private static void processOneToManyRelationship(final Object object, final Object parentObject, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
			
			
		}

		private static void processOneToOneRelationship(final Object object, final Object parentObject, final StringBuilder whereClause) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> oneToOneRelationships = entityDescriptor.getOneToOneRelationships();

			while(oneToOneRelationships.hasNext()) {
				Relationship oneToOneRelationship = oneToOneRelationships.next();
				if(parentObject != null && oneToOneRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = oneToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(oneToOneRelationship.getReferTo());
					oneToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, oneToOneRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + oneToOneRelationship.getReferTo());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + oneToOneRelationship.getReferTo());
				}

				
				processOneToManyRelationship(referedObject, object, whereClause);
				processManyToOneRelationship(referedObject, object, whereClause);
				processManyToManyRelationship(referedObject, object, whereClause);
				
				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
				while(parentAttributes.hasNext()) {
					Attribute attribute = parentAttributes.next();
					
					boolean isPrimary = attribute.isPrimaryKey();
					if(isPrimary) {
						Object columnValue = null;
						try {
							columnValue = ClassUtils.getValue(referedObject, attribute.getGetterMethodName());
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processOneToOneRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
							throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
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
		
		private static void processOneToManyRelationship(final Object object, final Object parentObject, final StringBuilder whereClause) throws DatabaseException {
			
		}
		

		private static void processOneToManyRelationship(final Object object, final Object parentObject) throws DatabaseException {

			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.getOneToManyRelationships();
			
			while(oneToManyRelationships.hasNext()) {
				
				EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.next();
				
				boolean isLoad = oneToManyRelationship.isLoad();
				if(!isLoad) {
					continue;
				}

				
				StringBuilder whereClause = new StringBuilder();
				Iterator<String> foreignKeys = getPrimaryKeys(object);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Attribute attribute = entityDescriptor.getAttributeBasedOnColumnName(foreignKey);
					Object columnValue = null;
					
					try {
						columnValue = ClassUtils.getValue(object, attribute.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.error(DatabaseHelper.class.getName(), "processOneToManyRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + attribute.getGetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
					}

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}

				EntityDescriptor referedEntityDescriptor = oneToManyRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(oneToManyRelationship.getReferTo());
					oneToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				
				Object referedObject = select(null, null, referedEntityDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
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
					Log.error(DatabaseHelper.class.getName(), "processOneToManyRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
				}
			}
		}

		
		private static void processManyToOneRelationship(final Object object, final Object parentObject) throws DatabaseException {

		}
		
		private static void processManyToManyRelationship(final Object object, final Object parentObject) throws DatabaseException {

		}
		
		
		private static void processManyToOneRelationship(final Object object, final Object parentObject, final Collection<String> columnNames, final Collection<Object> columnValues) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToOneRelationships = entityDescriptor.getManyToOneRelationships();
			
			while(manyToOneRelationships.hasNext()) {
				Relationship manyToOneRelationship = manyToOneRelationships.next();
				if(parentObject != null && manyToOneRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = manyToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToOneRelationship.getReferTo());
					manyToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, manyToOneRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				}
				
				
				processOneToOneRelationship(referedObject, object, columnNames, columnValues);
				processManyToOneRelationship(referedObject, object, columnNames, columnValues);
				processManyToManyRelationship(referedObject, object, columnNames, columnValues);
				
				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
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
		
		private static void processManyToOneRelationship(final Object object, final Object parentObject, final StringBuilder whereClause) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToOneRelationships = entityDescriptor.getManyToOneRelationships();

			while(manyToOneRelationships.hasNext()) {
				Relationship manyToOneRelationship = manyToOneRelationships.next();
				if(parentObject != null && manyToOneRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = manyToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToOneRelationship.getReferTo());
					manyToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, manyToOneRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				}

				
				processOneToOneRelationship(referedObject, object, whereClause);
				processManyToOneRelationship(referedObject, object, whereClause);
				processManyToManyRelationship(referedObject, object, whereClause);
				
				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
				while(parentAttributes.hasNext()) {
					Attribute attribute = parentAttributes.next();
					
					boolean isPrimary = attribute.isPrimaryKey();
					if(isPrimary) {
						Object columnValue = null;
						try {
							columnValue = ClassUtils.getValue(referedObject, attribute.getGetterMethodName());
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
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
		
		private static void processOneToOneRelationship(final Object object, final Object parentObject, Map<String, Object> data) throws DatabaseException {
			
		}
		
		private static void processOneToManyRelationship(final Object object, final Object parentObject, Map<String, Object> data) throws DatabaseException {
			
		}
		
		private static void processManyToOneRelationship(final Object object, final Object parentObject, Map<String, Object> data) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToOneRelationships = entityDescriptor.getManyToOneRelationships();

			while(manyToOneRelationships.hasNext()) {
				Relationship manyToOneRelationship = manyToOneRelationships.next();
				if(parentObject != null && manyToOneRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				EntityDescriptor referedEntityDescriptor = manyToOneRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToOneRelationship.getReferTo());
					manyToOneRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = ClassUtils.createClassInstance(manyToOneRelationship.getReferedEntityDescriptor().getClassName());
				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
					return;
				}

				
				processOneToOneRelationship(referedObject, object, data);
				processManyToOneRelationship(referedObject, object, data);
				processManyToManyRelationship(referedObject, object, data);
				
				if(manyToOneRelationship.isLoad()) {

					StringBuilder whereClause = new StringBuilder();

					Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
					while(foreignKeys.hasNext()) {
						String foreignKey = foreignKeys.next();
						Attribute attribute = referedEntityDescriptor.getAttributeBasedOnColumnName(foreignKey);
						Object columnValue = data.get(attribute.getColumnName());

						if(whereClause.length() <= 0) {
							whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
						} else {
							whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
						}
					}
					
					Object[] fetchedObjects = select(null, null, referedEntityDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
					if(fetchedObjects == null || fetchedObjects.length <= 0) {
						return;
					}
					
					referedObject = fetchedObjects[0];
					
				} else {
					Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
					while(foreignKeys.hasNext()) {
						String foreignKey = foreignKeys.next();
						Attribute attribute = referedEntityDescriptor.getAttributeBasedOnColumnName(foreignKey);

						Object columnValue = data.get(attribute.getColumnName());
						if(columnValue == null) {
							continue;
						}
						
						try {
							ClassUtils.invokeMethod(referedObject, attribute.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
							throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
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
					Log.error(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				}
			}

		}

		private static void processManyToManyRelationship(final Object object, final Object parentObject, Collection<String> columnNames, Collection<Object> columnValues) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
			
			while(manyToManyRelationships.hasNext()) {
				Relationship manyToManyRelationship = manyToManyRelationships.next();
				if(parentObject != null && manyToManyRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToManyRelationship.getReferTo());
					manyToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}
				
				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, manyToManyRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
					//throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				
					return;
				}

				
				processRelationship(referedObject, object, columnNames, columnValues);
					
				
				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
				while(parentAttributes.hasNext()) {
					Attribute attribute = parentAttributes.next();
					
					boolean isPrimary = attribute.isPrimaryKey();
					if(isPrimary) {
						try {
							columnNames.add(attribute.getColumnName());
							columnValues.add(ClassUtils.getValue(referedObject, attribute.getGetterMethodName()));
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
							throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
						} 
					}
				}
			}
		}
		
		private static void processManyToManyRelationship(final Object object, final Object parentObject, final StringBuilder whereClause) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToManyRelationships();
			
			while(manyToManyRelationships.hasNext()) {
				Relationship manyToManyRelationship = manyToManyRelationships.next();
				if(parentObject != null && manyToManyRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToManyRelationship.getReferTo());
					manyToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}
				
				Object referedObject = null;
				try {
					referedObject = ClassUtils.getValue(object, manyToManyRelationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
				}

				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
					return;
				}

				processRelationship(referedObject, object, whereClause);

				Iterator<Attribute> parentAttributes = referedEntityDescriptor.getAttributes();
				while(parentAttributes.hasNext()) {
					Attribute attribute = parentAttributes.next();
					
					boolean isPrimary = attribute.isPrimaryKey();
					if(isPrimary) {
						Object columnValue = null;
						try {
							columnValue = ClassUtils.getValue(referedObject, attribute.getGetterMethodName());
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.getClassName() + ", " + siminovException.getMessage());
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
		
		private static void processManyToManyRelationship(final Object object, final Object parentObject, Map<String, Object> data) throws DatabaseException {
			EntityDescriptor entityDescriptor = getEntityDescriptor(object.getClass().getName());
			Iterator<Relationship> manyToManyRelationships = entityDescriptor.getManyToOneRelationships();

			while(manyToManyRelationships.hasNext()) {
				Relationship manyToManyRelationship = manyToManyRelationships.next();
				if(parentObject != null && manyToManyRelationship.getReferTo().equalsIgnoreCase(parentObject.getClass().getName())) {
					continue;
				}
				
				
				EntityDescriptor referedEntityDescriptor = manyToManyRelationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = getEntityDescriptor(manyToManyRelationship.getReferTo());
					manyToManyRelationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				Object referedObject = ClassUtils.createClassInstance(manyToManyRelationship.getReferedEntityDescriptor().getClassName());
				if(referedObject == null) {
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
					return;
				}

				
				processRelationship(referedObject, object, data);
				
				if(manyToManyRelationship.isLoad()) {

					StringBuilder whereClause = new StringBuilder();

					Iterator<String> foreignKeys = getPrimaryKeys(referedObject);
					while(foreignKeys.hasNext()) {
						String foreignKey = foreignKeys.next();
						Attribute attribute = referedEntityDescriptor.getAttributeBasedOnColumnName(foreignKey);
						Object columnValue = data.get(attribute.getColumnName());

						if(whereClause.length() <= 0) {
							whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
						} else {
							whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
						}
					}
					
					Object[] fetchedObjects = select(null, null, referedEntityDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
					if(fetchedObjects == null || fetchedObjects.length <= 0) {
						return;
					}
					
					referedObject = fetchedObjects[0];

				} else {
					Iterator<String> primaryKeys = getPrimaryKeys(referedObject);
					while(primaryKeys.hasNext()) {
						String foreignKey = primaryKeys.next();
						Attribute attribute = referedEntityDescriptor.getAttributeBasedOnColumnName(foreignKey);

						Object columnValue = data.get(attribute.getColumnName());
						if(columnValue == null) {
							continue;
						}
						
						try {
							ClassUtils.invokeMethod(referedObject, attribute.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
						} catch(SiminovException siminovException) {
							Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
							throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.getClassName() + ", METHOD-NAME: " + attribute.getSetterMethodName() + ", " + siminovException.getMessage());
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
					Log.error(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(DatabaseHelper.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				}
			}
		}
	}
}
