///
/// [SIMINOV FRAMEWORK - CORE]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

/**
 * Contains constant values used in the framework
 * All constants referred in the framework are defined in this class
 */

#import <Foundation/Foundation.h>


/**
 * Siminov Descriptor Extension.
 */
static NSString * const SIMINOV_DESCRIPTOR_EXTENSION = @".si";

// ApplicationDescriptor Constants.

/**
 * ApplicationDescriptor.xml core TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_SIMINOV = @"Siminov";

/**
 * ApplicationDescriptor.xml File Name.
 */
static NSString * const APPLICATION_DESCRIPTOR_FILE_NAME = @"ApplicationDescriptor";

/**
 * ApplicationDescriptor.xml property TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_PROPERTY = @"property";

/**
 * ApplicationDescriptor.xml name TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_NAME = @"name";

/**
 * ApplicationDescriptor.xml description TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_DESCRIPTION = @"description";

/**
 * ApplicationDescriptor.xml version TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_VERSION = @"version";

/**
 * Application Descriptor Deploy
 */
static NSString * const APPLICATION_DESCRIPTOR_DEPLOY = @"deploy";

/**
 * Application Descriptor Deploy Development
 */
static NSString * const APPLICATION_DESCRIPTOR_DEPLOY_DEVELOPMENT = @"development";

/**
 * Application Descriptor Deploy Beta
 */
static NSString * const APPLICATION_DESCRIPTOR_DEPLOY_BETA = @"beta";

/**
 * Application Descriptor Deploy Production
 */
static NSString * const APPLICATION_DESCRIPTOR_DEPLOY_PRODUCTION = @"production";


/**
 * ApplicationDescriptor.xml load_initially TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_LOAD_INITIALLY = @"load_initially";

/**
 * ApplicationDescriptor.xml database-descriptors TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTORS = @"database-descriptors";

/**
 * ApplicationDescriptor.xml database-descriptor TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR = @"database-descriptor";

/**
 * ApplicationDescriptor.xml event-handlers TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR = @"library-descriptor";

/**
 * ApplicationDescriptor.xml event-handler TAG.
 */
static NSString * const APPLICATION_DESCRIPTOR_EVENT_HANDLER = @"event-handler";

// DatabaseDescriptor Constants.

/**
 * DatabaseDescriptor.xml database-descriptor TAG.
 */
static NSString * const DATABASE_DESCRIPTOR = @"database-descriptor";

/**
 * DatabaseDescriptor.xml property TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_PROPERTY = @"property";

/**
 * Database Descriptor Property Name
 */
static NSString * const DATABASE_DESCRIPTOR_PROPERTY_NAME = @"name";

/**
 * DatabaseDescriptor.xml description TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_DESCRIPTION = @"description";

/**
 * DatabaseDescriptor.xml database_name TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_DATABASE_NAME = @"database_name";

/**
 * DatabaseDescriptor.xml type TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_TYPE = @"type";

/**
 * DatabaseDescriptor.xml version TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_VERSION = @"version";

/**
 * DatabaseDescriptor.xml entity-descriptor TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR = @"entity-descriptor";

/**
 * DatabaseDescriptor.xml is_locking_required TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_TRANSACTION_SAFE = @"transaction_safe";

/**
 * DatabaseDescriptor.xml external_storage TAG.
 */
static NSString * const DATABASE_DESCRIPTOR_EXTERNAL_STORAGE = @"external_storage";


// LibraryDescriptor Constants.

/**
 * LibraryDescriptor.xml File Name.
 */
static NSString * const LIBRARY_DESCRIPTOR_FILE_NAME = @"LibraryDescriptor.xml";

/**
 * DatabaseDescriptor.xml library TAG.
 */
static NSString * const LIBRARY_DESCRIPTOR_LIBRARY_DESCRIPTOR = @"library-descriptor";

/**
 * LibraryDescriptor.xml property TAG.
 */
static NSString * const LIBRARY_DESCRIPTOR_PROPERTY = @"property";

/**
 * LibraryDescriptor.xml name TAG.
 */
static NSString * const LIBRARY_DESCRIPTOR_NAME = @"name";

/**
 * LibraryDescriptor.xml description TAG.
 */
static NSString * const LIBRARY_DESCRIPTOR_DESCRIPTION = @"description";

/**
 * LibraryDescriptor.xml entity-descriptor TAG.
 */
static NSString * const LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR = @"entity-descriptor";

/**
 * LibraryDescriptor.xml entity-descriptor seprator
 */
static NSString * const LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR = @".";


// EntityDescriptor Constants.

/**
 * EntityDescriptor.xml entity-descriptor TAG.
 */
static NSString * const ENTITY_DESCRIPTOR = @"entity-descriptor";

/**
 * EntityDescriptor.xml table-name TAG
 */
static NSString * const ENTITY_DESCRIPTOR_TABLE_NAME = @"table_name";

/**
 * EntityDescriptor.xml class-name TAG
 */
static NSString * const ENTITY_DESCRIPTOR_CLASS_NAME = @"class_name";

/**
 * EntityDescriptor.xml attribute TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE = @"attribute";

/**
 * EntityDescriptor.xml attribute variable name TAG
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME = @"variable_name";

/**
 * EntityDescriptor.xml attribute class name TAG
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME = @"column_name";

/**
 * EntityDescriptor.xml property TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_PROPERTY = @"property";

/**
 * EntityDescriptor.xml name TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_NAME = @"name";

/**
 * EntityDescriptor.xml type TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE = @"type";

/**
 * EntityDescriptor.xml attribute primary_key TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY = @"primary_key";

/**
 * EntityDescriptor.xml attribute unique TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE = @"unique";

/**
 * EntityDescriptor.xml attribute not_null TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL = @"not_null";

/**
 * EntityDescriptor.xml attribute default TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE = @"default";

/**
 * EntityDescriptor.xml attribute check TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK = @"check";


/**
 * EntityDescriptor.xml index TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_INDEX = @"index";

/**
 * Entity Descriptor Index Name
 */
static NSString * const ENTITY_DESCRIPTOR_INDEX_NAME = @"name";

/**
 * Entity Descriptor Index Unique
 */
static NSString * const ENTITY_DESCRIPTOR_INDEX_UNIQUE = @"unique";

/**
 * Entity Descriptor Index Column
 */
static NSString * const ENTITY_DESCRIPTOR_INDEX_COLUMN = @"column";

/**
 * EntityDescriptor.xml Relationships TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP = @"relationship";

/**
 * EntityDescriptor.xml relationship refer TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_REFER = @"refer";

/**
 * EntityDescriptor.xml relationship refer to TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO = @"refer_to";

/**
 * EntityDescriptor.xml relationship on update TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE = @"on_update";

/**
 * EntityDescriptor.xml relationship on delete TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE = @"on_delete";

/**
 * EntityDescriptor.xml relationship type TAG.
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE = @"type";

/**
 * EntityDescriptor.xml relationship type one to one TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE = @"one-to-one";

/**
 * EntityDescriptor.xml relationship type one to many TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY = @"one-to-many";

/**
 * EntityDescriptor.xml relationship type many to one TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE = @"many-to-one";

/**
 * EntityDescriptor.xml relationship type many to many TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY = @"many-to-many";

/**
 * EntityDescriptor.xml relationship load TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD = @"load";

/**
 * EntityDescriptor.xml relationship cascade TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE = @"cascade";

/**
 * EntityDescriptor.xml relationship restrict TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT = @"restrict";

/**
 * EntityDescriptor.xml relationship restrict TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION = @"no_action";

/**
 * EntityDescriptor.xml relationship set null TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL = @"set_null";

/**
 * EntityDescriptor.xml relationship set default TAG
 */
static NSString * const ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT = @"set_default";


// QueryBuilder Constants.

/**
 * Query Builder ON DELETE Constant.
 */
static NSString * const QUERY_BUILDER_ON_DELETE = @"ON DELETE";

/**
 * Query Builder ON UPDATE Constant.
 */
static NSString * const QUERY_BUILDER_ON_UPDATE = @"ON UPDATE";

/**
 * Query Builder CASCADE Constant.
 */
static NSString * const QUERY_BUILDER_CASCADE = @"CASCADE";

/**
 * Query Builder RESTRICT Constant.
 */
static NSString * const QUERY_BUILDER_RESTRICT = @"RESTRICT";

/**
 * Query Builder NO ACTION Constant.
 */
static NSString * const QUERY_BUILDER_NO_ACTION = @"NO ACTION";

/**
 * Query Builder SET NULL Constant.
 */
static NSString * const QUERY_BUILDER_SET_NULL = @"SET NULL";

/**
 * Query Builder SET DEFAULT Constant.
 */
static NSString * const QUERY_BUILDER_SET_DEFAULT = @"SET DEFAULT";

// SQLite Database Method Names.

/**
 * SQLite setLockingEnabled Method.
 */
static NSString * const SQLITE_DATABASE_ENABLE_LOCKING = @"setLockingEnabled";

/**
 * SQLite beginTransaction Method.
 */
static NSString * const SQLITE_DATABASE_BEGIN_TRANSACTION = @"beginTransaction";

/**
 * SQLite setTransactionSuccessful Method.
 */
static NSString * const SQLITE_DATABASE_COMMIT_TRANSACTION = @"setTransactionSuccessful";

/**
 * SQLite endTransaction Method.
 */
static NSString * const SQLITE_DATABASE_END_TRANSACTION = @"endTransaction";

/**
 * SQLite Enable Foreign Key Query.
 */
static NSString * const SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING = @"PRAGMA foreign_keys=ON;";

// DatabaseUtils Path Constants.

/**
 * Database Path Data.
 */
static NSString * const DATABASE_PATH_DATA = @"data";

/**
 * Database Path Constant.
 */
static NSString * const DATABASE_PATH_DATABASE = @"Databases";

/**
 * XMl File Extension.
 */
static NSString * const XML_FILE_EXTENSION = @".xml";

/*
 * Database Constants.
 */
static NSString * const SQLITE_DATABASE = @"Sqlite";

/**
 * Form Create Table Query Table Name Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER = @"FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Create Table Query Column Names Parameter.
 */
static NSString * const FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER = @"FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER";

/**
 * Form Create Table Query Column Types Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER = @"FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER";

/**
 * Form Create Table Query Default Values Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER = @"FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER";

/**
 * Form Create Table Query Checks Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER = @"FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER";

/**
 * Form Create Table Query Primary Keys Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER = @"FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER";

/**
 * Form Create Table Query Not Nulls Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER = @"FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER";

/**
 * Form Create Table Query Unique Columns Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER = @"FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER";

/**
 * Form Create Table Query Foreign Keys Parameter
 */
static NSString * const FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER = @"FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER";


/**
 * Form Create Index Query Index Name Parameter
 */
static NSString * const FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER = @"FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER";

/**
 * Form Create Index Query Table Name Parameter
 */
static NSString * const FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER = @"FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Create Index Query Column Names Parameter
 */
static NSString * const FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER = @"FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER";

/**
 * Form Create Index Query Is Unique Parameter
 */
static NSString * const FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER = @"FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER";


/**
 * Form Drop Table Query Table Name Parameter
 */
static NSString * const FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER = @"FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER";


/**
 * Form Drop Index Query Table Name Parameter
 */
static NSString * const FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER = @"FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Drop Index Query Index Name Parameter
 */
static NSString * const FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER = @"FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER";



/**
 * Form Select Query Table Name Parameter
 */
static NSString * const FORM_SELECT_QUERY_TABLE_NAME_PARAMETER = @"FORM_SELECT_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Select Query Distinct Parameter
 */
static NSString * const FORM_SELECT_QUERY_DISTINCT_PARAMETER = @"FORM_SELECT_QUERY_DISTINCT_PARAMETER";

/**
 * Form Select Query Where Clause Parameter
 */
static NSString * const FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Select Query Column Names Parameter
 */
static NSString * const FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER = @"FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER";

/**
 * Form Select Query Group Bys Parameter
 */
static NSString * const FORM_SELECT_QUERY_GROUP_BYS_PARAMETER = @"FORM_SELECT_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Select Query Having Parameter
 */
static NSString * const FORM_SELECT_QUERY_HAVING_PARAMETER = @"FORM_SELECT_QUERY_HAVING_PARAMETER";

/**
 * Form Select Query Order Bys Parameter
 */
static NSString * const FORM_SELECT_QUERY_ORDER_BYS_PARAMETER = @"FORM_SELECT_QUERY_ORDER_BYS_PARAMETER";

/**
 * Form Select Query Which Order By Parameter
 */
static NSString * const FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER = @"FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER";

/**
 * Form Select Query Limit Parameter
 */
static NSString * const FORM_SELECT_QUERY_LIMIT_PARAMETER = @"FORM_SELECT_QUERY_LIMIT_PARAMETER";



/**
 * Form Save Bind Query Table Name Parameter
 */
static NSString * const FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER = @"FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Save Bind Query Column Names Parameter
 */
static NSString * const FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER = @"FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER";


/**
 * Form Update Bind Query Table Name Parameter
 */
static NSString * const FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER = @"FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Update Bind Query Column Names Parameter
 */
static NSString * const FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER = @"FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER";

/**
 * Form Update Bind Query Where Clause Parameter
 */
static NSString * const FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER";



/**
 * Form Delete Query Table Name Parameter
 */
static NSString * const FORM_DELETE_QUERY_TABLE_NAME_PARAMETER = @"FORM_DELETE_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Delete Query Where Clause Parameter
 */
static NSString * const FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER";



/**
 * Form Count Query Table Name Parameter
 */
static NSString * const FORM_COUNT_QUERY_TABLE_NAME_PARAMETER = @"FORM_COUNT_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Count Query Column Parameter
 */
static NSString * const FORM_COUNT_QUERY_COLUMN_PARAMETER = @"FORM_COUNT_QUERY_COLUMN_PARAMETER";

/**
 * Form Count Query Distinct Parameter
 */
static NSString * const FORM_COUNT_QUERY_DISTINCT_PARAMETER = @"FORM_COUNT_QUERY_DISTINCT_PARAMETER";

/**
 * Form Count Query Where Clause Parameter
 */
static NSString * const FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Count Query Group Bys Parameter
 */
static NSString * const FORM_COUNT_QUERY_GROUP_BYS_PARAMETER = @"FORM_COUNT_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Count Query Having Parameter
 */
static NSString * const FORM_COUNT_QUERY_HAVING_PARAMETER = @"FORM_COUNT_QUERY_HAVING_PARAMETER";



/**
 * Form Avg Query Table Name Parameter
 */
static NSString * const FORM_AVG_QUERY_TABLE_NAME_PARAMETER = @"FORM_AVG_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Avg Query Column Parameter
 */
static NSString * const FORM_AVG_QUERY_COLUMN_PARAMETER = @"FORM_AVG_QUERY_COLUMN_PARAMETER";

/**
 * Form Avg Query Where Clause Parameter
 */
static NSString * const FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Avg Query Group Bys Parameter
 */
static NSString * const FORM_AVG_QUERY_GROUP_BYS_PARAMETER = @"FORM_AVG_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Avg Query Having Parameter
 */
static NSString * const FORM_AVG_QUERY_HAVING_PARAMETER = @"FORM_AVG_QUERY_HAVING_PARAMETER";



/**
 * Form Sum Query Table Name Parameter
 */
static NSString * const FORM_SUM_QUERY_TABLE_NAME_PARAMETER = @"FORM_SUM_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Sum Query Column Parameter
 */
static NSString * const FORM_SUM_QUERY_COLUMN_PARAMETER = @"FORM_SUM_QUERY_COLUMN_PARAMETER";

/**
 * Form Sum Query Where Clause Parameter
 */
static NSString * const FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Sum Query Group Bys Parameter
 */
static NSString * const FORM_SUM_QUERY_GROUP_BYS_PARAMETER = @"FORM_SUM_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Sum Query Having Parameter
 */
static NSString * const FORM_SUM_QUERY_HAVING_PARAMETER = @"FORM_SUM_QUERY_HAVING_PARAMETER";



/**
 * Form Total Query Table Name Parameter
 */
static NSString * const FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER = @"FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Total Query Column Parameter
 */
static NSString * const FORM_TOTAL_QUERY_COLUMN_PARAMETER = @"FORM_TOTAL_QUERY_COLUMN_PARAMETER";

/**
 * Form Total Query Where Clause Parameter
 */
static NSString * const FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Total Query Group Bys Parameter
 */
static NSString * const FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER = @"FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Total Query Having Parameter
 */
static NSString * const FORM_TOTAL_QUERY_HAVING_PARAMETER = @"FORM_TOTAL_QUERY_HAVING_PARAMETER";



/**
 * Form Max Query Table Name Parameter
 */
static NSString * const FORM_MAX_QUERY_TABLE_NAME_PARAMETER = @"FORM_MAX_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Max Query Column Parameter
 */
static NSString * const FORM_MAX_QUERY_COLUMN_PARAMETER = @"FORM_MAX_QUERY_COLUMN_PARAMETER";

/**
 * Form Max Query Where Clause Parameter
 */
static NSString * const FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Max Query Group Bys Parameter
 */
static NSString * const FORM_MAX_QUERY_GROUP_BYS_PARAMETER = @"FORM_MAX_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Max Query Having Parameter
 */
static NSString * const FORM_MAX_QUERY_HAVING_PARAMETER = @"FORM_MAX_QUERY_HAVING_PARAMETER";



/**
 * Form Min Query Table Name Parameter
 */
static NSString * const FORM_MIN_QUERY_TABLE_NAME_PARAMETER = @"FORM_MIN_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Min Query Column Parameter
 */
static NSString * const FORM_MIN_QUERY_COLUMN_PARAMETER = @"FORM_MIN_QUERY_COLUMN_PARAMETER";

/**
 * Form Min Query Where Clause Parameter
 */
static NSString * const FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Min Query Group Bys Parameter
 */
static NSString * const FORM_MIN_QUERY_GROUP_BYS_PARAMETER = @"FORM_MIN_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Min Query Having Parameter
 */
static NSString * const FORM_MIN_QUERY_HAVING_PARAMETER = @"FORM_MIN_QUERY_HAVING_PARAMETER";



/**
 * Form Group Concat Query Table Name Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Group Concat Query Column Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER";

/**
 * Form Group Concat Query Where Clause Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER";

/**
 * Form Group Concat Query Group Bys Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER";

/**
 * Form Group Concat Query Having Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER";

/**
 * Form Group Concat Query Delimiter Parameter
 */
static NSString * const FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER = @"FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER";


/**
 * Form Alter Add Column Query Table Name Parameter
 */
static NSString * const FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER = @"FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Alter Add Column Query Column Name Parameter
 */
static NSString * const FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER = @"FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER";


/**
 * Form Foreign Keys Database Descriptor Parameter
 */
static NSString * const FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER = @"FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER";



/**
 * Form Table Info Query Table Name Parameter
 */
static NSString * const FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER = @"FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER";

/**
 * Form Table Info Query Name
 */
static NSString * const FORM_TABLE_INFO_QUERY_NAME = @"name";

/**
 * Form Update Database Version Query Database Version Parameter
 */
static NSString * const FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER = @"FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER";

/**
 * Form Table Names Name
 */
static NSString * const FORM_TABLE_NAMES_NAME = @"name";

/**
 * Android Metadata Table Name
 */
static NSString * const IOS_METADATA_TABLE_NAME = @"ios_metadata";


/**
 * New Line
 */
static NSString * const NEW_LINE = @"\n";


/**
 * File type
 */
static NSString * const FILE_TYPE = @".xml";

/**
 * File type
 */
static NSString * const DIRECTORY_NAME = @"Assets";

/**
 * Database Query Form Table Index
 */
static NSString * const DATABASE_QUERY_FROM_TABLE_INDEX = @"FROM";



/*
 * Data Types
 */

/**
 * Integer Data Type
 */
static NSString * const INTEGER_DATA_TYPE = @"integer";

/**
 * Primitive Integer Data Type
 */
static NSString * const PRIMITIVE_INTEGER_DATA_TYPE = @"primitive-integer";

/**
 * Long Data Type
 */
static NSString * const LONG_DATA_TYPE = @"long";

/**
 * Primitive Long Data Type
 */
static NSString * const PRIMITIVE_LONG_DATA_TYPE = @"primitive-long";

/**
 * Float Data Type
 */
static NSString * const FLOAT_DATA_TYPE = @"float";

/**
 * Primitive Float Data Type
 */
static NSString * const PRIMITIVE_FLOAT_DATA_TYPE = @"primitive-float";

/**
 * Double Data Type
 */
static NSString * const DOUBLE_DATA_TYPE = @"double";

/**
 * Primitive Double Data Type
 */
static NSString * const PRIMITIVE_DOUBLE_DATA_TYPE = @"primitive-double";

/**
 * Boolean Data Type
 */
static NSString * const BOOLEAN_DATA_TYPE = @"boolean";

/**
 * Primitive Boolean Data Type
 */
static NSString * const PRIMITIVE_BOOLEAN_DATA_TYPE = @"primitive-boolean";

/**
 * String Data Type
 */
static NSString * const STRING_DATA_TYPE = @"string";


/**
 * Byte Data Type
 */
static NSString * const BYTE_DATA_TYPE = @"byte";

/**
 * Primitive Byte Data Type
 */
static NSString * const PRIMITIVE_BYTE_DATA_TYPE = @"primitive-byte";

/**
 * Native Primitive Integer Data Type
 */
static NSString * const NATIVE_PRIMITIVE_INTEGER_DATA_TYPE = @"int";

/**
 * Native Primitive Long Data Type
 */
static NSString * const NATIVE_PRIMITIVE_LONG_DATA_TYPE = @"long";

/**
 * Native Primitive Float Data Type
 */
static NSString * const NATIVE_PRIMITIVE_FLOAT_DATA_TYPE = @"float";

/**
 * Native Primitive Double Data Type
 */
static NSString * const NATIVE_PRIMITIVE_DOUBLE_DATA_TYPE = @"double";

/**
 * Native Primitive Boolean Data Type
 */
static NSString * const NATIVE_PRIMITIVE_BOOLEAN_DATA_TYPE = @"bool";

/**
 * Native String Data Type
 */
static NSString * const NATIVE_STRING_DATA_TYPE = @"NSString";


