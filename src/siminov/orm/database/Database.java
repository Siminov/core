/** 

o * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution|support@siminov.com]
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

package siminov.orm.database;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import siminov.orm.Constants;
import siminov.orm.Siminov;
import siminov.orm.database.design.IDataTypeHandler;
import siminov.orm.database.design.IDatabase;
import siminov.orm.database.design.IQueryBuilder;
import siminov.orm.database.impl.IAverage;
import siminov.orm.database.impl.ICount;
import siminov.orm.database.impl.IDelete;
import siminov.orm.database.impl.IGroupConcat;
import siminov.orm.database.impl.IMax;
import siminov.orm.database.impl.IMin;
import siminov.orm.database.impl.ISelect;
import siminov.orm.database.impl.ISum;
import siminov.orm.database.impl.ITotal;
import siminov.orm.events.IDatabaseEvents;
import siminov.orm.exception.DatabaseException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor.Column;
import siminov.orm.model.DatabaseMappingDescriptor.Index;
import siminov.orm.model.DatabaseMappingDescriptor.Relationship;
import siminov.orm.resource.Resources;
import siminov.orm.utils.ClassUtils;


/**
 * Exposes methods to deal with database. 
 * It has methods to create, delete, and perform other common database management tasks.
 */
public abstract class Database implements Constants {

	/**
	 * It is used to create instance of IDatabase implementation.
	 * @param databaseDescriptor
	 * @return
	 * @throws DatabaseException
	 */
	public static DatabaseBundle createDatabase(DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		return DatabaseFactory.getInstance().getDatabaseBundle(databaseDescriptor);
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
		
	<database-mapping>
	
		<table table_name="LIQUOR" class_name="com.core.template.model.Liquor">
			
			<column variable_name="liquorType" column_name="LIQUOR_TYPE">
				<property name="type">TEXT</property>
				<property name="primary_key">true</property>
				<property name="not_null">true</property>
				<property name="unique">true</property>
			</column>		
	
			<column variable_name="description" column_name="DESCRIPTION">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="history" column_name="HISTORY">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="link" column_name="LINK">
				<property name="type">TEXT</property>
				<property name="default">www.wikipedia.org</property>
			</column>
	
			<column variable_name="alcholContent" column_name="ALCHOL_CONTENT">
				<property name="type">TEXT</property>
			</column>
	
			<maps>
				<map map="liquorBrands" map_to="com.core.template.model.LiquorBrand">
					<property name="load">false</property>
					<property name="relationship_type">ONE-TO-MANY</property>
				</map>
			</maps>
			
			<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
				<column>HISTORY</column>
			</index>
										
		</table>
	
	</database-mapping>		
			}
					</p>
	  			</li>

	  			<li> Describing table through Defining Annotations in Java POJO. And creation of table will be handled by SIMINOV.
	  			  	<p>	
SIMINOV will read each class Annotations defined by developer and create table's in database.
	
	Example: 
		
		@Table(tableName=Liquor.TABLE_NAME)
		@Indexes({
			@Index(name="LIQUOR_INDEX_BASED_ON_LINK", unique=true, value={
				@IndexColumn(column=Liquor.LINK)
			}), 
		})
		public class Liquor extends Database implements Serializable {
		
			//Table Name
			transient public static final String TABLE_NAME = "LIQUOR";	
			
			//Column Names
			transient public static final String LIQUOR_TYPE = "LIQUOR_TYPE";
			transient public static final String DESCRIPTION = "DESCRIPTION";
			transient public static final String HISTORY = "HISTORY";
			transient public static final String LINK = "LINK";
			transient public static final String ALCHOL_CONTENT = "ALCHOL_CONTENT";
			
			//Liquor Types 
			transient public static final String LIQUOR_TYPE_GIN = "Gin"; 
			transient public static final String LIQUOR_TYPE_RUM = "Rum"; 
			transient public static final String LIQUOR_TYPE_TEQUILA = "Tequila"; 
			transient public static final String LIQUOR_TYPE_VODKA = "Vodka"; 
			transient public static final String LIQUOR_TYPE_WHISKEY = "Whiskey"; 
			transient public static final String LIQUOR_TYPE_BEER = "Beer"; 
			transient public static final String LIQUOR_TYPE_WINE = "Wine";
			
			
			//Variables
			{@code
			@Column(columnName=LIQUOR_TYPE,
					properties={
						@Property(name=Property.PRIMARY_KEY, value=Property.PRIMARY_KEY_TRUE),
						@Property(name=Property.NOT_NULL, value=Property.NOT_NULL_TRUE), 
						@Property(name=Property.UNIQUE, value=Property.UNIQUE_TRUE)
						}
					)
			}
			private String liquorType = null;
			
			{@code
			@Column(columnName=DESCRIPTION)
			}
			private String description = null;
			
			{@code
			@Column(columnName=HISTORY)
			}
			private String history = null;
		
			{@code
			@Column(columnName=LINK,
					properties={
						@Property(name=Property.DEFAULT, value="www.wikipedia.org")
						}
					)
			}
			private String link = null;
			
			{@code
			@Column(columnName=ALCHOL_CONTENT)
			}
			private String alcholContent = null;
		
			{@code
			@Map(mapTo="com.core.template.model.LiquorBrand",
					properties={
						@Property(name=Property.LOAD, value=Property.LOAD_FALSE),
						@Property(name=Property.RELATIONSHIP_TYPE, value=Property.RELATIONSHIP_ONE_TO_MANY),
						}
				)
			}
			private Collection<LiquorBrand> liquorBrands = null;
			
			//Methods
			
			public String getLiquorType() {
				return this.liquorType;
			}
			
			public void setLiquorType(String liquorType) {
				this.liquorType = liquorType;
			}
			
			public String getDescription() {
				return this.description;
			}
			
			public void setDescription(String description) {
				this.description = description;
			}
			
			public String getHistory() {
				return this.history;
			}
			
			public void setHistory(String history) {
				this.history = history;
			}
			
			public String getLink() {
				return this.link;
			}
			
			public void setLink(String link) {
				this.link = link;
			}
			
			public String getAlcholContent() {
				return this.alcholContent;
			}
			
			public void setAlcholContent(String alcholContent) {
				this.alcholContent = alcholContent;
			}
		
			public Collection<LiquorBrand> getLiquorBrands() {
				return this.liquorBrands;
			}
			
			public void setLiquorBrands(Collection<LiquorBrand> liquorBrands) {
				this.liquorBrands = liquorBrands;
			}
		}
					</p>
	  			</li>
	  			
	  			<li> Manually creating table structure using DatabaseMapping POJO class. 
	  				
	Example: 
	
		//Defines structure for Beer table.
		DatabaseMapping databaseMapping = new DatabaseMapping();
		databaseMapping.setTableName("LIQUOR");
		databaseMapping.setClassName(Liquor.class.getName());
	
		//Add Liquor Type.
		DatabaseMapping.Column liquorType = databaseMapping.new Column();
		liquorType.setVariableName("liquorType");
		liquorType.setColumnName("LIQUOR_TYPE");
	
		liquorType.setType("TEXT");
		
		liquorType.setPrimaryKey(true);
		liquorType.setNotNull(true);
		liquorType.setUnique(false);
	
		liquorType.setGetterMethodName("getLiquorType");
		liquorType.setSetterMethodName("setLiquorType");
		
		databaseMapping.addColumn(liquorType);
	
		//Add Liquor Description.
		DatabaseMapping.Column description = databaseMapping.new Column();
		description.setVariableName("description");
		description.setColumnName("DESCRIPTION");
	
		description.setType("TEXT");
		
		description.setGetterMethodName("getDescription");
		description.setSetterMethodName("setDescription");
		
		databaseMapping.addColumn(description);
	
		//Add History.
		DatabaseMapping.Column history = databaseMapping.new Column();
		history.setVariableName("history");
		history.setColumnName("HISTORY");
	
		history.setType("TEXT");
		
		history.setGetterMethodName("getHistory");
		history.setSetterMethodName("setHistory");
		
		databaseMapping.addColumn(history);
	
		//Add Link.
		DatabaseMapping.Column link = databaseMapping.new Column();
		link.setVariableName("history");
		link.setColumnName("HISTORY");
	
		link.setType("TEXT");
		link.setDefault("www.wikipedia.org");
		
		link.setGetterMethodName("getLink");
		link.setSetterMethodName("setLink");
		
		databaseMapping.addColumn(link);
	
		//Add Alchol Content.
		DatabaseMapping.Column alcholContent = databaseMapping.new Column();
		alcholContent.setVariableName("alcholContent");
		alcholContent.setColumnName("ALCHOL_CONTENT");
	
		alcholContent.setType("TEXT");
		
		alcholContent.setGetterMethodName("getAlcholContent");
		alcholContent.setSetterMethodName("setAlcholContent");
		
		databaseMapping.addColumn(alcholContent);
	
		//Create Index On Liquor table.
		DatabaseMapping.Index indexOnLiquor = databaseMapping.new Index();
		indexOnLiquor.setName("LIQUOR_INDEX_BASED_ON_LINK");
		indexOnLiquor.setUnique(true);
		
		//Add Columns on which we need index.
		indexOnLiquor.addColumn("LINK");
		
		databaseMapping.addIndex(indexOnLiquor);
		
		Collection<DatabaseMapping> databaseMappings = new ArrayList<DatabaseMapping> ();
		databaseMappings.add(databaseMapping);
		
		try {
			Database.createTables(databaseMappings.iterator());
		} catch(DatabaseException databaseException) {
			//Log It.
		}
	  			</li>
	  		</ul> 
	  	</pre>
	  </p>
	  
	 * @param databaseMappings Database-mapping objects which defines the structure of each table.
	 * @throws DatabaseException If not able to create table in SQLite.
	 */
	public static void createTables(final Iterator<DatabaseMappingDescriptor> databaseMappings) throws DatabaseException {
		/*
		 * 1. Validate whether SIMINOV is ACTIVE or not.
		 * 2. Iterate through each database mappings.
		 */
		Siminov.validateSiminov();

		while(databaseMappings.hasNext()) {
			createTable(databaseMappings.next());
		}
	}

	/**
	   Is used to create a new table in an database.
	  	<p>
			<pre> Manually creating table structure using DatabaseMapping POJO class. 
  					
	Example: 
	
		{@code
	
		//Defines structure for Liquor table.
		DatabaseMapping databaseMapping = new DatabaseMapping();
		databaseMapping.setTableName("LIQUOR");
		databaseMapping.setClassName(Liquor.class.getName());
	
		//Add Liquor Type.
		DatabaseMapping.Column liquorType = databaseMapping.new Column();
		liquorType.setVariableName("liquorType");
		liquorType.setColumnName("LIQUOR_TYPE");
	
		liquorType.setType("TEXT");
		
		liquorType.setPrimaryKey(true);
		liquorType.setNotNull(true);
		liquorType.setUnique(false);
	
		liquorType.setGetterMethodName("getLiquorType");
		liquorType.setSetterMethodName("setLiquorType");
		
		databaseMapping.addColumn(liquorType);
	
		//Add Liquor Description.
		DatabaseMapping.Column description = databaseMapping.new Column();
		description.setVariableName("description");
		description.setColumnName("DESCRIPTION");
	
		description.setType("TEXT");
		
		description.setGetterMethodName("getDescription");
		description.setSetterMethodName("setDescription");
		
		databaseMapping.addColumn(description);
	
		//Add History.
		DatabaseMapping.Column history = databaseMapping.new Column();
		history.setVariableName("history");
		history.setColumnName("HISTORY");
	
		history.setType("TEXT");
		
		history.setGetterMethodName("getHistory");
		history.setSetterMethodName("setHistory");
		
		databaseMapping.addColumn(history);
	
		//Add Link.
		DatabaseMapping.Column link = databaseMapping.new Column();
		link.setVariableName("history");
		link.setColumnName("HISTORY");
	
		link.setType("TEXT");
		link.setDefault("www.wikipedia.org");
		
		link.setGetterMethodName("getLink");
		link.setSetterMethodName("setLink");
		
		databaseMapping.addColumn(link);
	
		//Add Alchol Content.
		DatabaseMapping.Column alcholContent = databaseMapping.new Column();
		alcholContent.setVariableName("alcholContent");
		alcholContent.setColumnName("ALCHOL_CONTENT");
	
		alcholContent.setType("TEXT");
		
		alcholContent.setGetterMethodName("getAlcholContent");
		alcholContent.setSetterMethodName("setAlcholContent");
		
		databaseMapping.addColumn(alcholContent);
	
		//Create Index On Liquor table.
		DatabaseMapping.Index indexOnLiquor = databaseMapping.new Index();
		indexOnLiquor.setName("LIQUOR_INDEX_BASED_ON_LINK");
		indexOnLiquor.setUnique(true);
		
		//Add Columns on which we need index.
		indexOnLiquor.addColumn("LINK");
		
		databaseMapping.addIndex(indexOnLiquor);
		
		try {
			Database.createTables(databaseMapping);
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
		Siminov.validateSiminov();

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
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		IDataTypeHandler dataTypeHandler = databaseBundle.getDataTypeHandler();
		
		if(database == null) {
			Log.loge(Database.class.getName(), "createTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "createTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
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
		
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();

		while(columns.hasNext()) {
			Column column = columns.next();
			
			columnNames.add(column.getColumnName());
			columnTypes.add(dataTypeHandler.convert(column.getType()));
			isNotNull.add(column.isNotNull());

			defaultValues.add(column.getDefaultValue());
			checks.add(column.getCheck());
			
			boolean isPrimary = column.isPrimaryKey();
			boolean isUnique = column.isUnique();
			
			if(isPrimary) {
				primaryKeys.add(column.getColumnName());
			}
			
			if(isUnique) {
				uniqueKeys.add(column.getColumnName());
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

			Collection<Column> foreignColumns = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Column> foreignColumnsIterator = foreignColumns.iterator();
			
			while(foreignColumnsIterator.hasNext()) {
				Column foreignColumn = foreignColumnsIterator.next();
				
				columnNames.add(foreignColumn.getColumnName());
				columnTypes.add(dataTypeHandler.convert(foreignColumn.getType()));
				isNotNull.add(foreignColumn.isNotNull());

				defaultValues.add(foreignColumn.getDefaultValue());
				checks.add(foreignColumn.getCheck());
					
				boolean isPrimary = foreignColumn.isPrimaryKey();
				if(isPrimary) {
					primaryKeys.add(foreignColumn.getColumnName());
				}
				
				boolean isUnique = foreignColumn.isUnique();
				if(isUnique) {
					uniqueKeys.add(foreignColumn.getColumnName());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(column.getColumnName());
					columnTypes.add(dataTypeHandler.convert(column.getType()));
					isNotNull.add(column.isNotNull());

					defaultValues.add(column.getDefaultValue());
					checks.add(column.getCheck());
					
					if(isPrimary) {
						primaryKeys.add(column.getColumnName());
					}
					
					boolean isUnique = column.isUnique();
					if(isUnique) {
						uniqueKeys.add(column.getColumnName());
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
		Iterator<String> triggers = null;
		
		int androidSDKVersion = android.os.Build.VERSION.SDK_INT;
		if(androidSDKVersion < 8) {
			triggers = queryBuilder.formTriggers(databaseMappingDescriptor);
		} else {
			foreignKeys = queryBuilder.formForeignKeys(databaseMappingDescriptor);
		}
		
		/*
		 * Call QueryBuilder.formCreateTableQuery, get query to create table.
		 * After forming create table query call executeQuery method to create table in database.
		 */
		String query = queryBuilder.formCreateTableQuery(tableName, columnNames.iterator(), columnTypes.iterator(), defaultValues.iterator(), checks.iterator(), primaryKeys.iterator(), isNotNull.iterator(), uniqueKeys.iterator(), foreignKeys);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		/*
		 * Create all triggers.
		 */
		if(triggers != null) {
			while(triggers.hasNext()) {
				database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, triggers.next());
			}
		}
		
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

		Resources resources = Resources.getInstance();
		IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.tableCreated(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor);
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
	public static void createIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final Index index) throws DatabaseException {
		
		Siminov.validateSiminov();

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
	public static void createIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final String indexName, final Iterator<String> columnNames, final boolean isUnique) throws DatabaseException {
		
		Siminov.validateSiminov();

		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.loge(Database.class.getName(), "createIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "createIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		final Collection<String> columnNamesCollection = new ArrayList<String> ();
		while(columnNames.hasNext()) {
			columnNamesCollection.add(columnNames.next());
		}
		
		String query = queryBuilder.formCreateIndexQuery(indexName, databaseMappingDescriptor.getTableName(), columnNamesCollection.iterator(), isUnique);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		Resources resources = Resources.getInstance();
		IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			Index index = new Index();
			index.setName(indexName);
			index.setUnique(isUnique);
			
			Iterator<String> columnNamesIterator = columnNamesCollection.iterator();
			while(columnNamesIterator.hasNext()) {
				index.addColumn(columnNamesIterator.next());
			}
			
			databaseEventHandler.indexCreated(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, index);
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

	try {
		new Liquor().createIndex(indexOnLiquor);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	
			</pre>
		</p>
	 * @param index Index object which defines the structure of index needs to create.
	 * @throws DatabaseException If not able to create index on table.
	 */
	public void createIndex(final Index index) throws DatabaseException {
		
		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		createIndex(databaseMapping, index);
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
	 * @param indexName Name of index.
	 * @param columnNames Iterator over column names.
	 * @param isUnique true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
	 * @throws DatabaseException If not able to create index on table.
	 */
	public void createIndex(final String indexName, final Iterator<String> columnNames, final boolean isUnique) throws DatabaseException {
		
		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		createIndex(databaseMapping, indexName, columnNames, isUnique);
	}

	/**
	   Is used to drop a index on a table in database.
	  	<p>
			<pre> Create Index On Liquor table.
	
	{@code
	
	String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
	
	try {
		new Liquor().dropIndex(indexName);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @param index Name of a index needs to be drop.
	 * @throws DatabaseException If not able to drop index on table.
	 */
	public void dropIndex(final Index index) throws DatabaseException {

		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		dropIndex(databaseMapping, index.getName());
	}

	public void dropIndex(final String indexName) throws DatabaseException {

		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		dropIndex(databaseMapping, indexName);
	}

	public static void dropIndex(final DatabaseMappingDescriptor databaseMapping, final Index index) throws DatabaseException {

		Siminov.validateSiminov();
		dropIndex(databaseMapping, index.getName());
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
	public static void dropIndex(final DatabaseMappingDescriptor databaseMappingDescriptor, final String indexName) throws DatabaseException {
		
		Siminov.validateSiminov();

		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.loge(Database.class.getName(), "dropIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
			throw new DeploymentException(Database.class.getName(), "dropIndex", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName() + ", " + " INDEX-NAME: " + indexName);
		}

		String query = queryBuilder.formDropIndexQuery(databaseMappingDescriptor.getTableName(), indexName);
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
		
		Resources resources = Resources.getInstance();
		IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.indexDropped(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, databaseMappingDescriptor.getIndex(indexName));
		}
	}
	
	/**
	 * It drop's the table from database
	  	<p>
			<pre> 

	{@code

	try {
		new Liquor().dropTable();
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
			</pre>
		</p>
	 * @throws DatabaseException If not able to drop table.
	 */
	public void dropTable() throws DatabaseException {
		
		Siminov.validateSiminov();

		final DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		dropTable(databaseMapping);
	}

	/**
	 * It drop's the table from database based on database-mapping.
	  	<p>
			<pre> Drop the Liquor table.
	
	{@code
	
	DatabaseMapping databaseMapping = new Liquor().getDatabaseMapping();
	
	try {
		Database.dropTable(databaseMapping);
	} catch(DatabaseException databaseException) {
		//Log It.
	}
	
	}
	
			</pre>
		</p>
	 * @param databaseMappingDescriptor Database-mapping object which defines the structure of table.
	 * @throws DatabaseException If not able to drop table.
	 */
	public static void dropTable(final DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {

		Siminov.validateSiminov();

		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.loge(Database.class.getName(), "dropTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "dropTable", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		String tableName = databaseMappingDescriptor.getTableName();
		database.executeQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, queryBuilder.formDropTableQuery(tableName));
		
		Resources resources = Resources.getInstance();
		IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.tableDropped(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor);
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
	public static void dropDatabase(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.validateSiminov();

		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "dropDatabase", "No Database Instance Found For DATABASE-MAPPING: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(Database.class.getName(), "dropDatabase", "No Database Instance Found For DATABASE-MAPPING: " + databaseDescriptor.getDatabaseName());
		}

		Resources resources = Resources.getInstance();
		String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);

		File file = new File(databasePath + databaseDescriptor.getDatabaseName());
		database.close(databaseDescriptor);
		file.delete();
		
		resources.removeDatabaseBundle(databaseBundle);
		
		IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
		if(databaseEventHandler != null) {
			databaseEventHandler.databaseDropped(databaseDescriptor);
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
	public static void beginTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "beginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(Database.class.getName(), "beginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
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
	public static void commitTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(Database.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
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
	public static void endTransaction(final DatabaseDescriptor databaseDescriptor) {
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DeploymentException(Database.class.getName(), "commitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		try {
			database.executeMethod(SQLITE_DATABASE_END_TRANSACTION, null);
		} catch(DatabaseException databaseException) {
			Log.loge(Database.class.getName(), "commitTransaction", "DatabaseException caught while executing end transaction method, " + databaseException.getMessage());
		}
	}
	
	
	/**
	 * Fetch tuples from table.

	<pre>

Example:
	{@code

	Liquor[] liquor =  new Liquor().select()
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.and(Liquor.ALCHOL_CONTENT).equalTo("90%")
					.fetch();
	
	}
	</pre>


	 * @return ISelect to provide extra information based on which tuples will be fetched from table.
	 * @throws DatabaseException if any error occur while fetching tuples from table.
	 */
	public ISelect select() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ISelect.INTERFACE_NAME);
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
		
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "select", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "select", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, queryBuilder.formSelectQuery(databaseMappingDescriptor.getTableName(), distinct, whereClause, columnNames, groupBy, having, orderBy, whichOrderBy, limit));
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
			Log.loge(Database.class.getName(), "select", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DatabaseException(Database.class.getName(), "select", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
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
		
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
		
		if(database == null) {
			Log.loge(Database.class.getName(), "lazyFetch", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "lazyFetch", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		String query = queryBuilder.formSelectQuery(databaseMappingDescriptor.getTableName(), distinct, whereClause, columnNames, groupBy, having, orderBy, whichOrderBy, limit);
		Iterator<Object> tuples = parseAndInflateData(databaseMappingDescriptor, database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query));
			
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
			Log.loge(Database.class.getName(), "lazyFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DatabaseException(Database.class.getName(), "lazyFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
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
	public Object[] select(final String query) throws DatabaseException {
		Siminov.validateSiminov();
		
		/*
		 * 1. Get database mapping object for mapped invoked class object.
		 */
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMapping.getClassName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "fetchManual", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
			throw new DeploymentException(Database.class.getName(), "fetchManual", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
		}
		
		/*
		 * 4. Pass all parameters to executeFetchQuery and get cursor.
		 */
		Iterator<Object> tuples = parseAndInflateData(databaseMapping, database.executeFetchQuery(getDatabaseDescriptor(), databaseMapping, query));
			
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
			classObject = Class.forName(databaseMapping.getClassName());
		} catch(Exception exception) {
			Log.loge(Database.class.getName(), "manualFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMapping.getClassName());
			throw new DatabaseException(Database.class.getName(), "manualFetch", "Exception caught while making class object for return type, DATABASE-MAPPING: " + databaseMapping.getClassName());
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
	public void save() throws DatabaseException {
		Siminov.validateSiminov();
		
		save(this);
	}

	static void save(final Object object) throws DatabaseException {
		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
		 * 3. Using QueryBuilder form insert bind query.
		 * 4. Pass query to executeBindQuery method for insertion.
		 * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		
		if(object == null) {
			Log.logd(Database.class.getName(), "save", "Invalid Object Found.");
			return;
		}
		
		/*
		 * 1. Get mapped database mapping object for invoked class object.
		 */
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "save", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "save", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
		 */
		String tableName = databaseMappingDescriptor.getTableName();
		
		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();

		Iterator<DatabaseMappingDescriptor.Column> columns = databaseMappingDescriptor.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();
			
			try {
				columnNames.add(column.getColumnName());
				columnValues.add(ClassUtils.getValue(object, column.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "save", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "save", siminovException.getMessage());
			} 
		}
		
		processManyToOneRelationship(object, columnNames, columnValues);
		processManyToManyRelationship(object, columnNames, columnValues);
		
		
		/*
		 * 3. Using QueryBuilder form insert bind query.
		 */
		String query = queryBuilder.formSaveBindQuery(tableName, columnNames.iterator());
		
		
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
					Log.loge(Database.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				save(value);
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}
				
				@SuppressWarnings("unchecked")
				Collection<Object> values = (Collection<Object>) value;
				Iterator<Object> valuesIterator = values.iterator();
				
				while(valuesIterator.hasNext()) {
					save(valuesIterator.next());
				}
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "save", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}
				
				@SuppressWarnings("unchecked")
				Collection<Object> values = (Collection<Object>) value;
				Iterator<Object> valuesIterator = values.iterator();
				
				while(valuesIterator.hasNext()) {
					save(valuesIterator.next());
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
	public void update() throws DatabaseException {
		Siminov.validateSiminov();
	
		update(this);
	}

	static void update(final Object object) throws DatabaseException {
		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys for updation purpose.
		 * 4. Using QueryBuilder form update bind query.
		 * 5. Pass query to executeBindQuery method for updation.
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */

		if(object == null) {
			Log.logd(Database.class.getName(), "update", "Invalid Object Found.");
			return;
		}

		/*
		 * 1. Get mapped database mapping object for invoked class object.
		 */
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMapping.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "update", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
			throw new DeploymentException(Database.class.getName(), "update", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
		}

		StringBuilder whereClause = new StringBuilder();
		String tableName = databaseMapping.getTableName();

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		Iterator<DatabaseMappingDescriptor.Column> columns = databaseMapping.getColumns();

		Collection<String> columnNames = new LinkedList<String>();
		Collection<Object> columnValues = new LinkedList<Object>();
		
		while(columns.hasNext()) {
			Column column = columns.next();
			Object columnValue = null;
			try {
				columnNames.add(column.getColumnName());
				columnValue = ClassUtils.getValue(object, column.getGetterMethodName());
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMapping.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "update", siminovException.getMessage());
			} 
			
			columnValues.add(columnValue);
			
			if(column.isPrimaryKey()) {
				if(whereClause.length() == 0) {
					whereClause.append(column.getColumnName() + "= '" + columnValue + "'");
				} else {
					whereClause.append(" AND " + column.getColumnName() + "= '" + columnValue + "'");
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
		String query = queryBuilder.formUpdateBindQuery(tableName, columnNames.iterator(), whereClause.toString());

		/*
		 * 5. Pass query to executeBindQuery method for updation.
		 */
		
		Iterator<Object> values = columnValues.iterator();
		database.executeBindQuery(getDatabaseDescriptor(databaseMapping.getClassName()), databaseMapping, query, values);
		
		/*
		 * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
		 */
		Iterator<DatabaseMappingDescriptor.Relationship> relationships = databaseMapping.getRelationships();
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
					Log.loge(Database.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMapping.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				update(value);
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMapping.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}
				
				@SuppressWarnings("unchecked")
				Collection<Object> returnedValue = (Collection<Object>) value;
				Iterator<Object> valuesIterator = returnedValue.iterator();
				
				while(valuesIterator.hasNext()) {
					update(valuesIterator.next());
				}
			} else if(relationshipType.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
				Object value = null;
				try {
					value = ClassUtils.getValue(object, relationship.getGetterReferMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMapping.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "update", siminovException.getMessage());
				} 

				
				if(value == null) {
					continue;
				}

				@SuppressWarnings("unchecked")
				Collection<Object> returnedValue = (Collection<Object>) value;
				Iterator<Object> valuesIterator = returnedValue.iterator();
				
				while(valuesIterator.hasNext()) {
					update(valuesIterator.next());
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
	public void saveOrUpdate() throws DatabaseException {
		Siminov.validateSiminov();
		
		saveOrUpdate(this);
	}

	static void saveOrUpdate(final Object object) throws DatabaseException {
		/*
		 * 1. Get mapped database mapping object for object class name.
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 * 3. Form where clause based on primary keys to fetch objects from database table. IF EXISTS: call update method, ELSE: class save method.
		 * 4. IF EXISTS: call update method, ELSE: call save method.
		 */
		
		if(object == null) {
			Log.logd(Database.class.getName(), "saveOrUpdate", "Invalid Object Found.");
			return;
		}
		
		/*
		 * 1. Get mapped database mapping object for object class name.
		 */
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();

		if(database == null) {
			Log.loge(Database.class.getName(), "saveOrUpdate", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "saveOrUpdate", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		/*
		 * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
		 */
		
		StringBuilder whereClause = new StringBuilder();
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();

			if(column.isPrimaryKey()) {
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, column.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "saveOrUpdate", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "saveOrUpdate", siminovException.getMessage());
				} 

				
				if(whereClause.length() <= 0) {
					whereClause.append(column.getColumnName() + "= '" + columnValue + "'");
				} else {
					whereClause.append(" AND " + column.getColumnName() + "= '" + columnValue + "'");
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
			Log.logd(Database.class.getName(), "delete", "Invalid Object Found.");
			return;
		}

		/*
		 * 1. Get mapped database mapping object for object parameter class name.
		 */
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor(object.getClass().getName());
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMapping.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "delete", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
			throw new DeploymentException(Database.class.getName(), "delete", "No Database Instance Found For DATABASE-MAPPING: " + databaseMapping.getClassName());
		}
		
		StringBuilder where = new StringBuilder();
		
		if(whereClause == null || whereClause.length() <= 0) {
			Iterator<Column> columns = databaseMapping.getColumns();
			
			while(columns.hasNext()) {
				Column column = columns.next();
				
				Object columnValue = null;
				try {
					columnValue = ClassUtils.getValue(object, column.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "delete", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMapping.getClassName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "delete", siminovException.getMessage());
				} 

				
				/*
				 * 3. Form where clause based on primary keys for deletion purpose.
				 */
				if(column.isPrimaryKey()) {
					if(where.length() <= 0) {
						where.append(column.getColumnName() + "= '" + columnValue + "'");							
					} else {
						where.append(" AND " + column.getColumnName() + "= '" + columnValue + "'");							
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
		String query = queryBuilder.formDeleteQuery(databaseMapping.getTableName(), where.toString());
		/*
		 * 5. Pass query to executeBindQuery method for deletion.
		 */
		database.executeQuery(getDatabaseDescriptor(databaseMapping.getClassName()), databaseMapping, query);
	}
	
	/**
		It deletes a record to any single table in a relational database.
	
	   	<pre>

Example:

	{@code

	Liquor beer = new Liquor();
	beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
	beer.setDescription(applicationContext.getString(R.string.beer_description));
	beer.setHistory(applicationContext.getString(R.string.beer_history));
	beer.setLink(applicationContext.getString(R.string.beer_link));
	beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
  
	try {
		beer.delete();
	} catch(DatabaseException de) {
		//Log it.
	}
			
	OR
			
	try {
		new Liquor().delete();
	} catch(DatabaseException de) {
		//Log It.
	}
	
	}

	    </pre>
	 
	   	@throws DatabaseException If any error occurs while saving tuples in database.
	 */
	public IDelete delete() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IDelete.INTERFACE_NAME, this);
	}


	/**
 	Returns the count of rows based on information provided.
 	
	<pre>

Example:
	{@code

	int count = 0;
	
	try {
		count = new Liquor().count().
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();
		
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}
    
    </pre>
 
 	@return ICount to provide extra information based on which count will be calculated.
 	@throws DatabaseException If any error occur while find count.
 */
	public ICount count() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ICount.INTERFACE_NAME);
	}
	
	static final int count(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final boolean distinct, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "count(" + whereClause + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formCountQuery(databaseMappingDescriptor.getTableName(), column, distinct, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	/**
	 	Returns the average based on column name provided.
	 	
		<pre>

Example:
	{@code

	int average = 0;
	
	try {
		average = new Liquor().avg()
					.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();

	} catch(DatabaseException de) {
		//Log it.
	}

	}
	    </pre>
	 
	 	@return IAverage to provide extra information based on which average will be calculated.
	 	@throws DatabaseException If any error occur while finding average.
	 */
	public IAverage avg() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IAverage.INTERFACE_NAME);
	}

	
	static final int avg(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "avg(" + column + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "avg(" + column + ")", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formAvgQuery(databaseMappingDescriptor.getTableName(), column, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	
	/**
	 	Returns the sum based on column name provided.
	 	
		<pre>

Example:
	{@code

	int sum = 0;
	
	try {
		sum = new Liquor().sum()
					.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();

	} catch(DatabaseException de) {
		//Log it.
	}
			
	}			
	    </pre>
	 
	 	@return ISum to provide extra information based on which sum will be calculated.
	 	@throws DatabaseException If any error occur while finding sum.
	 */
	public ISum sum() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ISum.INTERFACE_NAME);
	}

	static final int sum(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "sum", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "sum", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formSumQuery(databaseMappingDescriptor.getTableName(), column, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	/**
	 	Returns the total based on column name provided.
	 	
		<pre>

Example:
	{@code

	int total = 0;
	
	try {
		total = new Liquor().total()
					.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();
		
	} catch(DatabaseException de) {
		//Log it.
	}
	    
	}    
	    </pre>
	 
	 	@return ITotal to provide extra information based on which total will be calculated.
	 	@throws DatabaseException If any error occur while finding total.
	 */
	public ITotal total() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), ITotal.INTERFACE_NAME);
	}

	
	static final int total(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "total", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "total", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formTotalQuery(databaseMappingDescriptor.getTableName(), column, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	/**
	 	Returns the minimum based on column name provided.
	 	
		<pre>

Example:
	{@code

	int minimum = 0;
	
	try {
		minimum = new Liquor().min()
					.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();
		
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}
	    
	    </pre>
	 
	 	@return IMin to provide extra information based on which minimum will be calculated.
	 	@throws DatabaseException If any error occur while finding minimum.
	 */
	public IMin min() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IMin.INTERFACE_NAME);
	}
	
	static final int min(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "min", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "min", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formMinQuery(databaseMappingDescriptor.getTableName(), column, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	/**
	 	Returns the minimum based on column name provided.
	 	
		<pre>
	
	Example:
	{@code
	
	int maximum = 0;
	
	try {
		maximum = new Liquor().max()
					.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
					.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
					.execute();
		
	} catch(DatabaseException de) {
		//Log it.
	}
	
	}
	    
	    </pre>
	 
	 	@return IMax to provide extra information based on which maximum will be calculated.
	 	@throws DatabaseException If any error occur while finding minimum.
	 */
	public IMax max() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IMax.INTERFACE_NAME);
	}
	
	static final int max(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String whereClause, final Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "max", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "max", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formMaxQuery(databaseMappingDescriptor.getTableName(), column, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	
	/**
	 	Returns the group concat based on column name provided.
	 	
		<pre>

Example:
	{@code
	
	int groupConcat = 0;
	
	try {
		groupConcat = new Liquor().groupConcat()
						.column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE)
						.where(Liquor.LIQUOR_TYPE).equalTo("RUM")
						.execute();
						
	} catch(DatabaseException de) {
		//Log it.
	}

	}
	    </pre>
	 
	 	@return IGroupConcat to provide extra information based on which group concat will be calculated.
	 	@throws DatabaseException If any error occur while finding group concat.
	 */
	public IGroupConcat groupConcat() throws DatabaseException {
		return new Select(getDatabaseMappingDescriptor(), IGroupConcat.INTERFACE_NAME);
	}

	static final String groupConcat(final DatabaseMappingDescriptor databaseMappingDescriptor, final String column, final String delimiter, final String whereClause, Iterator<String> groupBys, final String having) throws DatabaseException {
		
		Siminov.validateSiminov();
		
		DatabaseBundle databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
		IDatabase database = databaseBundle.getDatabase();
		IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();

		if(database == null) {
			Log.loge(Database.class.getName(), "groupConcat", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
			throw new DeploymentException(Database.class.getName(), "groupConcat", "No Database Instance Found For DATABASE-MAPPING: " + databaseMappingDescriptor.getClassName());
		}

		String query = queryBuilder.formGroupConcatQuery(databaseMappingDescriptor.getTableName(), column, delimiter, whereClause, groupBys, having);

		Iterator<Map<String, Object>> datas = database.executeFetchQuery(getDatabaseDescriptor(databaseMappingDescriptor.getClassName()), databaseMappingDescriptor, query);
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
	public DatabaseDescriptor getDatabaseDescriptor() throws DatabaseException {
		Siminov.validateSiminov();

		return getDatabaseDescriptor(getClass().getName());
	}
	
	public static DatabaseDescriptor getDatabaseDescriptor(String className) throws DatabaseException {
		Resources resources = Resources.getInstance();
		return resources.getDatabaseDescriptorBasedOnClassName(className);
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
	public DatabaseMappingDescriptor getDatabaseMappingDescriptor() throws DatabaseException {
		Siminov.validateSiminov();
		
		return getDatabaseMappingDescriptor(getClass().getName());
	}

	static DatabaseMappingDescriptor getDatabaseMappingDescriptor(String className) throws DatabaseException {
		try {
			return Resources.getInstance().requiredDatabaseMappingDescriptorBasedOnClassName(className);
		} catch(SiminovException siminovException) {
			Log.loge(Database.class.getName(), "getDatabaseMappingDescriptor", "SiminovException caught while getting required database mapping descriptor object, CLASS-NAME: " + className + ", " + siminovException.getMessage());
			throw new DatabaseException(Database.class.getName(), "getDatabaseMappingDescriptor", siminovException.getMessage());
		}
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
	public String getTableName() throws DatabaseException {
		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getTableName(databaseMappingDescriptor);
	}
	
	
	static String getTableName(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
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
	public Iterator<String> getColumnNames() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getColumnNames(databaseMappingDescriptor);
	}
	
	static Iterator<String> getColumnNames(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		Iterator<DatabaseMappingDescriptor.Column> columns = databaseMappingDescriptor.getColumns();

		Collection<String> columnNames = new ArrayList<String>();
		while(columns.hasNext()) {
			columnNames.add(columns.next().getColumnName());
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

			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(columns.next().getColumnName());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					columnNames.add(columns.next().getColumnName());
				}
			}
		}
		

		return columnNames.iterator();
	}
	
	
	static Map<String, Object> getColumnValues(final Object object, DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		Map<String, Object> columnNameAndItsValues = new HashMap<String, Object>();

		Iterator<DatabaseMappingDescriptor.Column> columns = databaseMappingDescriptor.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();
			
			try {
				columnNameAndItsValues.put(column.getColumnName(), ClassUtils.getValue(object, column.getGetterMethodName()));
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "getColumnValues", siminovException.getMessage());
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

			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(column.getColumnName(), ClassUtils.getValue(object, column.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "getColumnValues", siminovException.getMessage());
					} 
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNameAndItsValues.put(column.getColumnName(), ClassUtils.getValue(object, column.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "getColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "getColumnValues", siminovException.getMessage());
					} 
				}
			}
		}
		
		return columnNameAndItsValues;
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
	public Map<String, Object> getColumnValues() throws DatabaseException {
		Siminov.validateSiminov();

		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getColumnValues(this, databaseMappingDescriptor);
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
	public Map<String, String> getColumnTypes() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getColumnTypes(databaseMappingDescriptor);
	}

	static Map<String, String> getColumnTypes(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		
		Map<String, String> columnTypes = new HashMap<String, String> ();
		Iterator<DatabaseMappingDescriptor.Column> columns = databaseMappingDescriptor.getColumns();
		
		while(columns.hasNext()) {
			Column column = columns.next();
			columnTypes.put(column.getColumnName(), column.getType());
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

			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					columnTypes.put(column.getColumnName(), column.getType());
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					columnTypes.put(column.getColumnName(), column.getType());
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
	public Iterator<String> getPrimaryKeys() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptor();
		return getPrimaryKeys(databaseMapping);
	}
	
	static Iterator<String> getPrimaryKeys(final DatabaseMappingDescriptor databaseMapping) throws DatabaseException {
		Iterator<Column> columns = databaseMapping.getColumns();
		Collection<String> primaryKeys = new ArrayList<String>();

		while(columns.hasNext()) {
			Column column = columns.next();
			
			boolean isPrimary = column.isPrimaryKey();
			if(isPrimary) {
				primaryKeys.add(column.getColumnName());
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
	public Iterator<String> getMandatoryFields() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getMandatoryFields(databaseMappingDescriptor);
	}
	
	static Iterator<String> getMandatoryFields(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();
		Collection<String> isMandatoryFieldsVector = new ArrayList<String>();
		
		while(columns.hasNext()) {
			Column column = columns.next();
			
			if(column.isNotNull()) {
				isMandatoryFieldsVector.add(column.getColumnName());
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

			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					if(column.isNotNull()) {
						isMandatoryFieldsVector.add(column.getColumnName());
					}
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					if(column.isNotNull()) {
						isMandatoryFieldsVector.add(column.getColumnName());
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
	public Iterator<String> getUniqueFields() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		return getUniqueFields(databaseMappingDescriptor);
	}

	static Iterator<String> getUniqueFields(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();
		Collection<String> isUniqueFieldsVector = new ArrayList<String>();
		
		while(columns.hasNext()) {
			Column column = columns.next();
			
			boolean isUnique = column.isUnique();
			if(isUnique) {
				isUniqueFieldsVector.add(column.getColumnName());
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

			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {

					boolean isUnique = column.isUnique();
					if(isUnique) {
						isUniqueFieldsVector.add(column.getColumnName());
					}
				}
			}
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship manyToManyRelationship = manyToManyRelationships.next();
			DatabaseMappingDescriptor parentDatabaseMappingDescriptor = manyToManyRelationship.getReferedDatabaseMappingDescriptor();
			
			Iterator<Column> parentColumns = parentDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {

					boolean isUnique = column.isUnique();
					if(isUnique) {
						isUniqueFieldsVector.add(column.getColumnName());
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
	public Iterator<String> getForeignKeys() throws DatabaseException {
		Siminov.validateSiminov();
		
		DatabaseMappingDescriptor databaseMappingDescriptor = getDatabaseMappingDescriptor();
		Collection<Column> columns = getForeignKeys(databaseMappingDescriptor);
		Iterator<Column> columnsIterate = columns.iterator();
		
		Collection<String> foreignKeys = new ArrayList<String>();
		while(columnsIterate.hasNext()) {
			foreignKeys.add(columnsIterate.next().getColumnName());
		}
		
		return foreignKeys.iterator();
	}

	static Collection<Column> getForeignKeys(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		Iterator<Relationship> oneToManyRealtionships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = databaseMappingDescriptor.getManyToManyRelationships();
		
		Collection<Column> foreignColumns = new ArrayList<Column>();
		
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();
			if(column.isPrimaryKey()) {
				foreignColumns.add(column);
			}
		}
		
		while(oneToManyRealtionships.hasNext()) {
			
			Relationship relationship = oneToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Column> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Column> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignColumns.add(referedForeignKeysIterate.next());
			}
		}

		while(manyToManyRealtionships.hasNext()) {
			
			Relationship relationship = manyToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Column> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Column> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignColumns.add(referedForeignKeysIterate.next());
			}
		}

		return foreignColumns;
	}
	
	/**
 		Iterates the provided cursor, and returns tuples in form of actual objects.
	 */
	static Iterator<Object> parseAndInflateData(final DatabaseMappingDescriptor databaseMappingDescriptor, Iterator<Map<String, Object>> values) throws DatabaseException {
		Siminov.validateSiminov();

		Collection<Object> tuples = new LinkedList<Object>();
		while(values.hasNext()) {
			
			Map<String, Object> value = values.next();
			
			Set<String> columnNames = value.keySet();
			Iterator<String> columnNamesIterate = columnNames.iterator();
			
			Map<String, Object> data = new HashMap<String, Object>();
			while(columnNamesIterate.hasNext()) {
				String columnName = columnNamesIterate.next();
				
				if(databaseMappingDescriptor.containsColumnBasedOnColumnName(columnName)) {
					data.put(databaseMappingDescriptor.getColumnBasedOnColumnName(columnName).getSetterMethodName(), value.get(columnName));
				}
			}

			Object object = null;
			
			try {
				object = ClassUtils.createAndInflateObject(databaseMappingDescriptor.getClassName(), data);
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "parseAndInflateData", "SiminovException caught while create and inflate object through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "parseAndInflateData", siminovException.getMessage());
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
			Iterator<String> foreignKeys = getPrimaryKeys(databaseMappingDescriptor);
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				Column column = databaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);
				Object columnValue = null;
				
				try {
					columnValue = ClassUtils.getValue(object, column.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "processOneToOneRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + column.getGetterMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
				}

				if(whereClause.length() <= 0) {
					whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
				} else {
					whereClause.append(", " + foreignKey + "='" + columnValue.toString() + "'");  
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
				Log.loge(Database.class.getName(), "processOneToOneRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processOneToOneRelationship", siminovException.getMessage());
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
			Iterator<String> foreignKeys = getPrimaryKeys(databaseMappingDescriptor);
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				Column column = databaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);
				Object columnValue = null;
				
				try {
					columnValue = ClassUtils.getValue(object, column.getGetterMethodName());
				} catch(SiminovException siminovException) {
					Log.loge(Database.class.getName(), "processOneToManyRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + column.getGetterMethodName() + ", " + siminovException.getMessage());
					throw new DatabaseException(Database.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
				}

				if(whereClause.length() <= 0) {
					whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
				} else {
					whereClause.append(", " + foreignKey + "='" + columnValue.toString() + "'");  
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
				ClassUtils.invokeMethod(object, oneToManyRelationship.getSetterReferMethodName(), new Class[] {referedCollection.getClass()}, new Object[] {referedCollection});
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "processOneToManyRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + oneToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processOneToManyRelationship", siminovException.getMessage());
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
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}
			
			processManyToOneRelationship(referedObject, columnNames, columnValues);
			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNames.add(column.getColumnName());
						columnValues.add(ClassUtils.getValue(referedObject, column.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + referedObject.getClass().getName() + ", " + " METHOD-NAME: " + column.getGetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
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
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			processManyToOneRelationship(referedObject, whereClause);
			
			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					Object columnValue = null;
					try {
						columnValue = ClassUtils.getValue(referedObject, column.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", siminovException.getMessage());
					} 

					
					if(whereClause.length() <= 0) {
						whereClause.append(column.getColumnName() + "= '" + columnValue + "'");
					} else {
						whereClause.append(" AND " + column.getColumnName() + "= '" + columnValue + "'");
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
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			
			processManyToOneRelationship(referedObject, data);

			if(manyToOneRelationship.isLoad()) {

				StringBuilder whereClause = new StringBuilder();

				Iterator<String> foreignKeys = getPrimaryKeys(referedDatabaseMappingDescriptor);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Column column = referedDatabaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);
					Object columnValue = data.get(column.getColumnName());

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}
				
				Object[] fetchedObjects = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
				referedObject = fetchedObjects[0];
				
			} else {
				Iterator<String> foreignKeys = getPrimaryKeys(referedDatabaseMappingDescriptor);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Column column = referedDatabaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);

					Object columnValue = data.get(column.getColumnName());
					if(columnValue == null) {
						continue;
					}
					
					try {
						ClassUtils.invokeMethod(referedObject, column.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + column.getSetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + column.getSetterMethodName() + ", " + siminovException.getMessage());
					}
				}
			}
			

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.getReferTo());
			}

			
			try {
				ClassUtils.invokeMethod(object, manyToOneRelationship.getSetterReferMethodName(), new Class[] {referedObject.getClass()}, new Object[] {referedObject});
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToOneRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
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
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			processManyToManyRelationship(referedObject, columnNames, columnValues);

			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					try {
						columnNames.add(column.getColumnName());
						columnValues.add(ClassUtils.getValue(object, column.getGetterMethodName()));
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
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
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.getGetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
			}

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			processManyToManyRelationship(referedObject, whereClause);

			Iterator<Column> parentColumns = referedDatabaseMappingDescriptor.getColumns();
			while(parentColumns.hasNext()) {
				Column column = parentColumns.next();
				
				boolean isPrimary = column.isPrimaryKey();
				if(isPrimary) {
					Object columnValue = null;
					try {
						columnValue = ClassUtils.getValue(referedObject, column.getGetterMethodName());
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", siminovException.getMessage());
					} 

					
					if(whereClause.length() <= 0) {
						whereClause.append(column.getColumnName() + "= '" + columnValue + "'");
					} else {
						whereClause.append(" AND " + column.getColumnName() + "= '" + columnValue + "'");
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
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}

			
			processManyToManyRelationship(referedObject, data);
			
			if(manyToManyRelationship.isLoad()) {

				StringBuilder whereClause = new StringBuilder();

				Iterator<String> foreignKeys = getPrimaryKeys(referedDatabaseMappingDescriptor);
				while(foreignKeys.hasNext()) {
					String foreignKey = foreignKeys.next();
					Column column = referedDatabaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);
					Object columnValue = data.get(column.getColumnName());

					if(whereClause.length() <= 0) {
						whereClause.append(foreignKey + "='" + columnValue.toString() + "'"); 
					} else {
						whereClause.append(" AND " + foreignKey + "='" + columnValue.toString() + "'");  
					}
				}
				
				Object[] fetchedObjects = lazyFetch(referedDatabaseMappingDescriptor, false, whereClause.toString(), null, null, null, null, null, null);
				referedObject = fetchedObjects[0];

			} else {
				Iterator<String> primaryKeys = getPrimaryKeys(referedDatabaseMappingDescriptor);
				while(primaryKeys.hasNext()) {
					String foreignKey = primaryKeys.next();
					Column column = referedDatabaseMappingDescriptor.getColumnBasedOnColumnName(foreignKey);

					Object columnValue = data.get(column.getColumnName());
					if(columnValue == null) {
						continue;
					}
					
					try {
						ClassUtils.invokeMethod(referedObject, column.getSetterMethodName(), new Class[] {columnValue.getClass()}, new Object[] {columnValue});
					} catch(SiminovException siminovException) {
						Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + column.getSetterMethodName() + ", " + siminovException.getMessage());
						throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedDatabaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + column.getSetterMethodName() + ", " + siminovException.getMessage());
					}
				}
			}

			if(referedObject == null) {
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.getReferTo());
			}
			
			try {
				ClassUtils.invokeMethod(object, manyToManyRelationship.getSetterReferMethodName(), new Class[] {referedObject.getClass()}, new Object[] {referedObject});
			} catch(SiminovException siminovException) {
				Log.loge(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
				throw new DatabaseException(Database.class.getName(), "processManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + databaseMappingDescriptor.getClassName() + ", METHOD-NAME: " + manyToManyRelationship.getSetterReferMethodName() + ", " + siminovException.getMessage());
			}
		}
	}

	
}
