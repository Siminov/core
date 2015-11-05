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

package siminov.core;


/**
 * Contains constant values used in the framework
 * All constants referred in the framework are defined in this class
 */
public interface Constants {

	// SIMINOV Descriptor Extension

	/**
	 * Siminov Descriptor Extension.
	 */
	public String SIMINOV_DESCRIPTOR_EXTENSION = ".si";

	// Application Descriptor Constants.

	/**
	 * ApplicationDescriptor.si.xml core TAG.
	 */
	public String APPLICATION_DESCRIPTOR_SIMINOV = "siminov";

	/**
	 * ApplicationDescriptor.si.xml File Name.
	 */
	public String APPLICATION_DESCRIPTOR_FILE_NAME = "ApplicationDescriptor.si.xml";

	/**
	 * ApplicationDescriptor.si.xml property TAG.
	 */
	public String APPLICATION_DESCRIPTOR_PROPERTY = "property";

	/**
	 * ApplicationDescriptor.si.xml name TAG.
	 */
	public String APPLICATION_DESCRIPTOR_NAME = "name";

	/**
	 * ApplicationDescriptor.si.xml description TAG.
	 */
	public String APPLICATION_DESCRIPTOR_DESCRIPTION = "description";

	/**
	 * ApplicationDescriptor.si.xml version TAG.
	 */
	public String APPLICATION_DESCRIPTOR_VERSION = "version";

	public String APPLICATION_DESCRIPTOR_DEPLOY = "deploy";
	
	public String APPLICATION_DESCRIPTOR_DEPLOY_DEVELOPMENT = "development";

	public String APPLICATION_DESCRIPTOR_DEPLOY_BETA = "beta";

	public String APPLICATION_DESCRIPTOR_DEPLOY_PRODUCTION = "production";

	
	
	/**
	 * ApplicationDescriptor.si.xml database-descriptor TAG.
	 */
	public String APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR = "database-descriptor";

	/**
	 * ApplicationDescriptor.si.xml event-handler TAG.
	 */
	public String APPLICATION_DESCRIPTOR_EVENT_HANDLER = "event-handler";

	
	/**
	 * ApplicationDescriptor.si.xml library descriptor TAG.
	 */
	public String APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR = "library-descriptor";
 	
	
	// DatabaseDescriptor Constants.

	/**
	 * DatabaseDescriptor.si.xml database-descriptor TAG.
	 */
	public String DATABASE_DESCRIPTOR = "database-descriptor";

	/**
	 * DatabaseDescriptor.si.xml property TAG.
	 */
	public String DATABASE_DESCRIPTOR_PROPERTY = "property";

	/**
	 * DatabaseDescriptor.si.xml property name TAG.
	 */
	public String DATABASE_DESCRIPTOR_PROPERTY_NAME = "name";

	/**
	 * DatabaseDescriptor.si.xml description TAG.
	 */
	public String DATABASE_DESCRIPTOR_DESCRIPTION = "description";

	/**
	 * DatabaseDescriptor.si.xml database_name TAG.
	 */
	public String DATABASE_DESCRIPTOR_DATABASE_NAME = "database_name";
	
	/**
	 * DatabaseDescriptor.si.xml type TAG.
	 */
	public String DATABASE_DESCRIPTOR_TYPE = "type";

	/**
	 * DatabaseDescriptor.si.xml version TAG.
	 */
	public String DATABASE_DESCRIPTOR_VERSION = "version";
	

	/**
	 * DatabaseDescriptor.si.xml entity-descriptor TAG.
	 */
	public String DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR = "entity-descriptor";

	/**
	 * DatabaseDescriptor.si.xml is_locking_required TAG.
	 */
	public String DATABASE_DESCRIPTOR_TRANSACTION_SAFE = "transaction_safe";

	/**
	 * DatabaseDescriptor.si.xml external_storage TAG.
	 */
	public String DATABASE_DESCRIPTOR_EXTERNAL_STORAGE = "external_storage";


	// Library Descriptor Constants.

	/**
	 * LibraryDescriptor.si.xml File Name.
	 */
	public String LIBRARY_DESCRIPTOR_FILE_NAME = "LibraryDescriptor.si.xml";

	/**
	 * LibraryDescriptor.si.xml library TAG.
	 */
	public String LIBRARY_DESCRIPTOR_LIBRARY_DESCRIPTOR = "library-descriptor";

	/**
	 * LibraryDescriptor.si.xml property TAG.
	 */
	public String LIBRARY_DESCRIPTOR_PROPERTY = "property";

	/**
	 * LibraryDescriptor.si.xml name TAG.
	 */
	public String LIBRARY_DESCRIPTOR_NAME = "name";

	/**
	 * LibraryDescriptor.si.xml description TAG.
	 */
	public String LIBRARY_DESCRIPTOR_DESCRIPTION = "description";

	/**
	 * LibraryDescriptor.si.xml entity-descriptor TAG.
	 */
	public String LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR = "entity-descriptor";

	
	/**
	 * LibraryDescriptor.si.xml entity descriptor seprator.
	 */
	public String LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR = ".";
	

	// Entity Descriptor Constants.

	/**
	 * EntityDescriptor.si.xml entity descriptor TAG.
	 */
	public String ENTITY_DESCRIPTOR = "entity-descriptor";

	/**
	 * EntityDescriptor.si.xml table_name TAG.
	 */
	public String ENTITY_DESCRIPTOR_TABLE_NAME = "table_name";

	/**
	 * EntityDescriptor.si.xml class_name TAG.
	 */
	public String ENTITY_DESCRIPTOR_CLASS_NAME = "class_name";

	/**
	 * EntityDescriptor.si.xml column TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE = "attribute";

	/**
	 * EntityDescriptor.si.xml variable_name TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME = "variable_name";

	/**
	 * EntityDescriptor.si.xml column_name TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME = "column_name";

	/**
	 * EntityDescriptor.si.xml property TAG.
	 */
	public String ENTITY_DESCRIPTOR_PROPERTY = "property";

	/**
	 * EntityDescriptor.si.xml name TAG.
	 */
	public String ENTITY_DESCRIPTOR_NAME = "name";

	/**
	 * EntityDescriptor.si.xml type TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE = "type";

	/**
	 * EntityDescriptor.si.xml primary_key TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY = "primary_key";

	/**
	 * EntityDescriptor.si.xml unique TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE = "unique";

	/**
	 * EntityDescriptor.si.xml not_null TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL = "not_null";

	/**
	 * EntityDescriptor.si.xml default TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE = "default";

	/**
	 * EntityDescriptor.si.xml check TAG.
	 */
	public String ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK = "check";

	/**
	 * EntityDescriptor.si.xml index TAG.
	 */
	public String ENTITY_DESCRIPTOR_INDEX = "index";

	/**
	 * EntityDescriptor.si.zml index name TAG.
	 */
	public String ENTITY_DESCRIPTOR_INDEX_NAME = "name";
	
	/**
	 * EntityDescriptor.si.xml index unique TAG.
	 */
	public String ENTITY_DESCRIPTOR_INDEX_UNIQUE = "unique";
	
	/**
	 * EntityDescriptor.si.xml index column TAG.
	 */
	public String ENTITY_DESCRIPTOR_INDEX_COLUMN = "column";
	
	
	/**
	 * EntityDescriptor.si.xml Relationship TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP = "relationship";

	/**
	 * EntityDescriptor.si.xml Relationship Type TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE = "type";
	
	/**
	 * EntityDescriptor.si.xml Relationship Refer TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_REFER = "refer";

	/**
	 * EntityDescriptor.si.xml Relationship Refer To TAG,
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO = "refer_to";

	/**
	 * EntityDescriptor.si.xml Relationship On Update TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE = "on_update";

	/**
	 * EntityDescriptor.si.xml Relationship On Delete TAG,
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE = "on_delete";

	/**
	 * EntityDescriptor.si.xml Relationship Type One To One TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE = "one-to-one";

	/**
	 * EntityDescriptor.si.xml Relationship Type One To Many TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY = "one-to-many";

	/**
	 * EntityDescriptor.si.xml Relationship Type Many To One TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE = "many-to-one";

	/**
	 * EntityDescriptor.si.xml Relationship Type Many To Many TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY = "many-to-many";

	/**
	 * EntityDescriptor.si.xml Relationship Load TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD = "load";

	/**
	 * EntityDescriptor.si.xml Relationship Cascade TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE = "cascade";

	/**
	 * EntityDescriptor.si.xml Relationship Restrict TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT = "restrict";

	/**
	 * EntityDescriptor.si.xml Relationship No Action TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION = "no_action";

	/**
	 * EntityDescriptor.si.xml Relationship Set Null TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL = "set_null";

	/**
	 * EntityDescriptor.si.xml Relationship Set Default TAG.
	 */
	public String ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT = "set_default";


	
	// Query Builder Constants.

	/**
	 * Query Builder ON DELETE Constant.
	 */
	public String QUERY_BUILDER_ON_DELETE = "ON DELETE";

	/**
	 * Query Builder ON UPDATE Constant.
	 */
	public String QUERY_BUILDER_ON_UPDATE = "ON UPDATE";

	/**
	 * Query Builder CASCADE Constant.
	 */
	public String QUERY_BUILDER_CASCADE = "CASCADE";

	/**
	 * Query Builder RESTRICT Constant.
	 */
	public String QUERY_BUILDER_RESTRICT = "RESTRICT";

	/**
	 * Query Builder NO ACTION Constant.
	 */
	public String QUERY_BUILDER_NO_ACTION = "NO ACTION";

	/**
	 * Query Builder SET NULL Constant.
	 */
	public String QUERY_BUILDER_SET_NULL = "SET NULL";

	/**
	 * Query Builder SET DEFAULT Constant.
	 */
	public String QUERY_BUILDER_SET_DEFAULT = "SET DEFAULT";


	// SQLite Database Method Names.

	/**
	 * SQLite setLockingEnabled Method.
	 */
	public String SQLITE_DATABASE_ENABLE_LOCKING = "setLockingEnabled";

	/**
	 * SQLite beginTransaction Method.
	 */
	public String SQLITE_DATABASE_BEGIN_TRANSACTION = "beginTransaction";

	/**
	 * SQLite setTransactionSuccessful Method.
	 */
	public String SQLITE_DATABASE_COMMIT_TRANSACTION = "setTransactionSuccessful";

	/**
	 * SQLite endTransaction Method.
	 */
	public String SQLITE_DATABASE_END_TRANSACTION = "endTransaction";


	/**
	 * SQLite Enable Foreign Key Query.
	 */
	public String SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING = "PRAGMA foreign_keys=ON;";

	// Database Utils Path Constants.

	/**
	 * Database Path Data.
	 */
	public String DATABASE_PATH_DATA = "data";

	/**
	 * Database Path Constant.
	 */
	public String DATABASE_PATH_DATABASE = "Databases";

	/**
	 * XMl File Extension.
	 */
	public String XML_FILE_EXTENSION = ".xml";


	/**
	 * Database Constants.
	 */
	public String SQLITE_DATABASE = "sqlite";


	/**
	 * Form Create Table Query Table Name Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER = "FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Create Table Query Column Names Parameter.
	 */
	public String FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER = "FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER";
	
	/**
	 * Form Create Table Query Column Types Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER = "FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER";
	
	/**
	 * Form Create Table Query Default Values Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER = "FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER";
	
	/**
	 * Form Create Table Query Checks Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER = "FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER";
	
	/**
	 * Form Create Table Query Primary Keys Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER = "FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER";
	
	/**
	 * Form Create Table Query Not Nulls Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER = "FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER";
	
	/**
	 * Form Create Table Query Unique Columns Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER = "FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER";
	
	/**
	 * Form Create Table Query Foreign Keys Parameter
	 */
	public String FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER = "FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER";
	

	
	/**
	 * Form Create Index Query Index Name Parameter
	 */
	public String FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER = "FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER";
	
	/**
	 * Form Create Index Query Table Name Parameter
	 */
	public String FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER = "FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Create Index Query Column Names Parameter
	 */
	public String FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER = "FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER";
	
	/**
	 * Form Create Index Query Is Unique Parameter
	 */
	public String FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER = "FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER";
	

	
	/**
	 * Form Drop Table Query Table Name Parameter
	 */
	public String FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER = "FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER";

	
	
	/**
	 * Form Drop Index Query Table Name Parameter
	 */
	public String FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER = "FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Drop Index Query Index Name Parameter
	 */
	public String FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER = "FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER";

	
	
	/**
	 * Form Select Query Table Name Parameter
	 */
	public String FORM_SELECT_QUERY_TABLE_NAME_PARAMETER = "FORM_SELECT_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Select Query Distinct Parameter
	 */
	public String FORM_SELECT_QUERY_DISTINCT_PARAMETER = "FORM_SELECT_QUERY_DISTINCT_PARAMETER";
	
	/**
	 * Form Select Query Where Clause Parameter
	 */
	public String FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Select Query Column Names Parameter
	 */
	public String FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER = "FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER";
	
	/**
	 * Form Select Query Group Bys Parameter
	 */
	public String FORM_SELECT_QUERY_GROUP_BYS_PARAMETER = "FORM_SELECT_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Select Query Having Parameter
	 */
	public String FORM_SELECT_QUERY_HAVING_PARAMETER = "FORM_SELECT_QUERY_HAVING_PARAMETER";
	
	/**
	 * Form Select Query Order Bys Parameter
	 */
	public String FORM_SELECT_QUERY_ORDER_BYS_PARAMETER = "FORM_SELECT_QUERY_ORDER_BYS_PARAMETER";
	
	/**
	 * Form Select Query Which Order By Parameter
	 */
	public String FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER = "FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER";
	
	/**
	 * Form Select Query Limit Parameter
	 */
	public String FORM_SELECT_QUERY_LIMIT_PARAMETER = "FORM_SELECT_QUERY_LIMIT_PARAMETER";

	
	
	/**
	 * Form Save Bind Query Table Name Parameter
	 */
	public String FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER = "FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Save Bind Query Column Names Parameter
	 */
	public String FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER = "FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER";

	
	/**
	 * Form Update Bind Query Table Name Parameter
	 */
	public String FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER = "FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Update Bind Query Column Names Parameter
	 */
	public String FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER = "FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER";
	
	/**
	 * Form Update Bind Query Where Clause Parameter
	 */
	public String FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER";
	

	
	/**
	 * Form Delete Query Table Name Parameter
	 */
	public String FORM_DELETE_QUERY_TABLE_NAME_PARAMETER = "FORM_DELETE_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Delete Query Where Clause Parameter
	 */
	public String FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER";
	
	
	
	/**
	 * Form Count Query Table Name Parameter
	 */
	public String FORM_COUNT_QUERY_TABLE_NAME_PARAMETER = "FORM_COUNT_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Count Query Column Parameter
	 */
	public String FORM_COUNT_QUERY_COLUMN_PARAMETER = "FORM_COUNT_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Count Query Distinct Parameter
	 */
	public String FORM_COUNT_QUERY_DISTINCT_PARAMETER = "FORM_COUNT_QUERY_DISTINCT_PARAMETER";
	
	/**
	 * Form Count Query Where Clause Parameter
	 */
	public String FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Count Query Group Bys Parameter
	 */
	public String FORM_COUNT_QUERY_GROUP_BYS_PARAMETER = "FORM_COUNT_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Count Query Having Parameter
	 */
	public String FORM_COUNT_QUERY_HAVING_PARAMETER = "FORM_COUNT_QUERY_HAVING_PARAMETER";


	
	/**
	 * Form Avg Query Table Name Parameter
	 */
	public String FORM_AVG_QUERY_TABLE_NAME_PARAMETER = "FORM_AVG_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Avg Query Column Parameter
	 */
	public String FORM_AVG_QUERY_COLUMN_PARAMETER = "FORM_AVG_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Avg Query Where Clause Parameter
	 */
	public String FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Avg Query Group Bys Parameter
	 */
	public String FORM_AVG_QUERY_GROUP_BYS_PARAMETER = "FORM_AVG_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Avg Query Having Parameter
	 */
	public String FORM_AVG_QUERY_HAVING_PARAMETER = "FORM_AVG_QUERY_HAVING_PARAMETER";
	

	
	/**
	 * Form Sum Query Table Name Parameter
	 */
	public String FORM_SUM_QUERY_TABLE_NAME_PARAMETER = "FORM_SUM_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Sum Query Column Parameter
	 */
	public String FORM_SUM_QUERY_COLUMN_PARAMETER = "FORM_SUM_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Sum Query Where Clause Parameter
	 */
	public String FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Sum Query Group Bys Parameter
	 */
	public String FORM_SUM_QUERY_GROUP_BYS_PARAMETER = "FORM_SUM_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Sum Query Having Parameter
	 */
	public String FORM_SUM_QUERY_HAVING_PARAMETER = "FORM_SUM_QUERY_HAVING_PARAMETER";


	
	/**
	 * Form Total Query Table Name Parameter
	 */
	public String FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER = "FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Total Query Column Parameter
	 */
	public String FORM_TOTAL_QUERY_COLUMN_PARAMETER = "FORM_TOTAL_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Total Query Where Clause Parameter
	 */
	public String FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Total Query Group Bys Parameter
	 */
	public String FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER = "FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Total Query Having Parameter
	 */
	public String FORM_TOTAL_QUERY_HAVING_PARAMETER = "FORM_TOTAL_QUERY_HAVING_PARAMETER";


	
	/**
	 * Form Max Query Table Name Parameter
	 */
	public String FORM_MAX_QUERY_TABLE_NAME_PARAMETER = "FORM_MAX_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Max Query Column Parameter
	 */
	public String FORM_MAX_QUERY_COLUMN_PARAMETER = "FORM_MAX_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Max Query Where Clause Parameter
	 */
	public String FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Max Query Group Bys Parameter
	 */
	public String FORM_MAX_QUERY_GROUP_BYS_PARAMETER = "FORM_MAX_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Max Query Having Parameter
	 */
	public String FORM_MAX_QUERY_HAVING_PARAMETER = "FORM_MAX_QUERY_HAVING_PARAMETER";
	

	
	/**
	 * Form Min Query Table Name Parameter 
	 */
	public String FORM_MIN_QUERY_TABLE_NAME_PARAMETER = "FORM_MIN_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Min Query Column Parameter
	 */
	public String FORM_MIN_QUERY_COLUMN_PARAMETER = "FORM_MIN_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Min Query Where Clause Parameter
	 */
	public String FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Min Query Group Bys Parameter
	 */
	public String FORM_MIN_QUERY_GROUP_BYS_PARAMETER = "FORM_MIN_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Min Query Having Parameter
	 */
	public String FORM_MIN_QUERY_HAVING_PARAMETER = "FORM_MIN_QUERY_HAVING_PARAMETER";


	
	/**
	 * Form Group Concat Query Table Name Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER = "FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Group Concat Query Column Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER = "FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER";
	
	/**
	 * Form Group Concat Query Where Clause Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER";
	
	/**
	 * Form Group Concat Query Group Bys Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER = "FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER";
	
	/**
	 * Form Group Concat Query Having Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER = "FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER";
	
	/**
	 * Form Group Concat Query Delimiter Parameter
	 */
	public String FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER = "FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER";

	
	/**
	 * Form Alter Add Column Query Table Name Parameter
	 */
	public String FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER = "FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Alter Add Column Query Column Name Parameter
	 */
	public String FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER = "FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER";

	
	/**
	 * Form Foreign Keys Database Descriptor Parameter
	 */
	public String FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER = "FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER";


	
	/**
	 * Form Table Info Query Table Name Parameter
	 */
	public String FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER = "FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER";
	
	/**
	 * Form Table Info Query Name
	 */
	public String FORM_TABLE_INFO_QUERY_NAME = "name";


	
	/**
	 * Form Update Database Version Query Database Version Parameter
	 */
	public String FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER = "FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER";
	

	
	/**
	 * Form Table Names Name
	 */
	public String FORM_TABLE_NAMES_NAME = "name";
	

	
	/**
	 * Android Meta data Table Name
	 */
	public String ANDROID_METADATA_TABLE_NAME = "android_metadata";
	
	/**
	 * New Line 
	 */
	public String NEW_LINE = "\n";
	
	/**
	 * Database Query From Table Index
	 */
	public String DATABASE_QUERY_FROM_TABLE_INDEX = "FROM";
	
	

	/*
	 * Data Types
	 */
	
	/**
	 * Integer Data Type
	 */
	public String INTEGER_DATA_TYPE = "integer";
	
	/**
	 * Primitive Integer Data Type
	 */
	public String PRIMITIVE_INTEGER_DATA_TYPE = "primitive-integer";
	
	/** 
	 * Long Data Type
	 */
	public String LONG_DATA_TYPE = "long";
	
	/** 
	 * Primitive Long Data Type
	 */
	public String PRIMITIVE_LONG_DATA_TYPE = "primitive-long";
	
	/**
	 * Float Data Type
	 */
	public String FLOAT_DATA_TYPE = "float";
	
	/**
	 * Primitive Float Data Type
	 */
	public String PRIMITIVE_FLOAT_DATA_TYPE = "primitive-float";
	
	/**
	 * Double Data Type
	 */
	public String DOUBLE_DATA_TYPE = "double";
	
	/**
	 * Primitive Double Data Type
	 */
	public String PRIMITIVE_DOUBLE_DATA_TYPE = "primitive-double";
	
	/**
	 * Boolean Data Type
	 */
	public String BOOLEAN_DATA_TYPE = "boolean";
	
	/**
	 * Primitive Boolean Data Type
	 */
	public String PRIMITIVE_BOOLEAN_DATA_TYPE = "primitive-boolean";
	
	/**
	 * String Data Type
	 */
	public String STRING_DATA_TYPE = "string";
	
	
	/**
	 * Byte Data Type
	 */
	public String BYTE_DATA_TYPE = "byte";
	
	/**
	 * Primitive Byte Data Type
	 */
	public String PRIMITIVE_BYTE_DATA_TYPE = "primitive-byte";

}
