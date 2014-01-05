/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution LLP|support@siminov.com]
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

package siminov.orm;


public interface Constants {

	// SIMINOV Descriptor Extension

	/**
	 * Siminov Descriptor Extension.
	 */
	public String SIMINOV_DESCRIPTOR_EXTENSION = ".si";

	// ApplicationDescriptor Constants.

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

	/**
	 * ApplicationDescriptor.si.xml load_initially TAG.
	 */
	public String APPLICATION_DESCRIPTOR_LOAD_INITIALLY = "load_initially";

	/**
	 * ApplicationDescriptor.si.xml database-descriptors TAG.
	 */
	public String APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTORS = "database-descriptors";

	/**
	 * ApplicationDescriptor.si.xml database-descriptor TAG.
	 */
	public String APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR = "database-descriptor";

	/**
	 * ApplicationDescriptor.si.xml event-handlers TAG.
	 */
	public String APPLICATION_DESCRIPTOR_EVENT_HANDLERS = "event-handlers";

	/**
	 * ApplicationDescriptor.si.xml event-handler TAG.
	 */
	public String APPLICATION_DESCRIPTOR_EVENT_HANDLER = "event-handler";

	public String APPLICATION_DESCRIPTOR_LIBRARY = "library";
 	
	
	// DatabaseDescriptor Constants.

	/**
	 * DatabaseDescriptor.si.xml database-descriptor TAG.
	 */
	public String DATABASE_DESCRIPTOR = "database-descriptor";

	public String DATABASE_DESCRIPTOR_FILE_NAME = "DescriptorDescriptor.si.xml";
	
	/**
	 * DatabaseDescriptor.si.xml property TAG.
	 */
	public String DATABASE_DESCRIPTOR_PROPERTY = "property";

	public String DATABASE_DESCRIPTOR_PROPERTY_NAME = "name";

	/**
	 * DatabaseDescriptor.si.xml description TAG.
	 */
	public String DATABASE_DESCRIPTOR_DESCRIPTION = "description";

	/**
	 * DatabaseDescriptor.si.xml database_name TAG.
	 */
	public String DATABASE_DESCRIPTOR_DATABASE_NAME = "database_name";
	
	public String DATABASE_DESCRIPTOR_TYPE = "type";

	public String DATABASE_DESCRIPTOR_VERSION = "version";
	
	/**
	 * DatabaseDescriptor.si.xml database-mappings TAG.
	 */
	public String DATABASE_DESCRIPTOR_DATABASE_MAPPINGS = "database-mappings";

	/**
	 * DatabaseDescriptor.si.xml database-mapping TAG.
	 */
	public String DATABASE_DESCRIPTOR_DATABASE_MAPPING = "database-mapping";

	/**
	 * DatabaseDescriptor.si.xml is_locking_required TAG.
	 */
	public String DATABASE_DESCRIPTOR_TRANSACTION_SAFE = "transaction_safe";

	/**
	 * DatabaseDescriptor.si.xml external_storage TAG.
	 */
	public String DATABASE_DESCRIPTOR_EXTERNAL_STORAGE = "external_storage";


	// LibraryDescriptor Constants.

	/**
	 * LibraryDescriptor.si.xml File Name.
	 */
	public String LIBRARY_DESCRIPTOR_FILE_NAME = "LibraryDescriptor.si.xml";

	/**
	 * LibraryDescriptor.si.xml library TAG.
	 */
	public String LIBRARY_DESCRIPTOR_LIBRARY = "library";

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
	 * LibraryDescriptor.si.xml database-mappings TAG.
	 */
	public String LIBRARY_DESCRIPTOR_DATABASE_MAPPINGS = "database-mappings";

	/**
	 * LibraryDescriptor.si.xml database-mapping TAG.
	 */
	public String LIBRARY_DESCRIPTOR_DATABASE_MAPPING = "database-mapping";

	
	public String LIBRARY_DESCRIPTOR_DATABASE_MAPPING_SEPRATOR = ".";
	

	// DatabaseMappingDescriptor Constants.

	/**
	 * DatabaseMappingDescriptor.si.xml database-mapping TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_DATABASE_MAPPING = "database-mapping";

	/**
	 * DatabaseMappingDescriptor.si.xml table TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_TABLE = "table";

	/**
	 * DatabaseMappingDescriptor.si.xml table_name TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_TABLE_NAME = "table_name";

	/**
	 * DatabaseMappingDescriptor.si.xml class_name TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_CLASS_NAME = "class_name";

	/**
	 * DatabaseMappingDescriptor.si.xml column TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_COLUMN = "column";

	/**
	 * DatabaseMappingDescriptor.si.xml variable_name TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_VARIABLE_NAME = "variable_name";

	/**
	 * DatabaseMappingDescriptor.si.xml column_name TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_COLUMN_NAME = "column_name";

	/**
	 * DatabaseMappingDescriptor.si.xml property TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_PROPERTY = "property";

	/**
	 * DatabaseMappingDescriptor.si.xml name TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_NAME = "name";

	/**
	 * DatabaseMappingDescriptor.si.xml type TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_TYPE = "type";

	/**
	 * DatabaseMappingDescriptor.si.xml primary_key TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY = "primary_key";

	/**
	 * DatabaseMappingDescriptor.si.xml unique TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_UNIQUE = "unique";

	/**
	 * DatabaseMappingDescriptor.si.xml true TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_UNIQUE_TRUE = "true";

	/**
	 * DatabaseMappingDescriptor.si.xml false TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_UNIQUE_FALSE = "false";

	/**
	 * DatabaseMappingDescriptor.si.xml index TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_INDEX = "index";

	/**
	 * DatabaseMappingDescriptor.si.xml not_null TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_NOT_NULL = "not_null";

	/**
	 * DatabaseMappingDescriptor.si.xml true TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_NOT_NULL_TRUE = "true";

	/**
	 * DatabaseMappingDescriptor.si.xml false TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_NOT_NULL_FALSE = "false";

	/**
	 * DatabaseMappingDescriptor.si.xml default TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE = "default";

	/**
	 * DatabaseMappingDescriptor.si.xml check TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_CHECK = "check";

	/**
	 * DatabaseMappingDescriptor.si.xml Relationships TAG.
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS = "relationships";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER = "refer";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER_TO = "refer_to";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_UPDATE = "on_update";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_DELETE = "on_delete";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE = "one-to-one";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY = "one-to-many";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE = "many-to-one";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY = "many-to-many";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD = "load";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD_TRUE = "true";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD_FALSE = "false";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_CASCADE = "cascade";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_RESTRICT = "restrict";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_NO_ACTION = "no_action";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_NULL = "set_null";

	/**
	 * DatabaseMappingDescriptor.si.xml
	 */
	public String DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_DEFAULT = "set_default";

	// Annotations Constants.

	// DatabaseMapping Constants.

	/**
	 * Annotation DatabaseMapping tableName TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_METHOD_GET_TABLE_NAME = "tableName";

	// DatabaseMappingColumn Constants.

	/**
	 * Annotation DatabaseMapping columnName TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_METHOD_GET_COLUMN_NAME = "columnName";

	/**
	 * Annotation DatabaseMapping properties TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTIES_METHOD_GET_PROPERTIES = "properties";

	// DatabaseMappingIndex Constants.

	/**
	 * Annotation DatabaseMapping name TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_NAME = "name";

	/**
	 * Annotation DatabaseMapping value TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_VALUE = "value";

	/**
	 * Annotation DatabaseMapping unique TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_IS_UNIQUE = DATABASE_MAPPING_DESCRIPTOR_UNIQUE;

	// DatabaseMappingIndexColumn Constants,

	/**
	 * Annotation DatabaseMapping column TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_INDEX_COLUMN_METHOD_GET_COLUMN = "column";

	// DatabaseMappingIndexes Constants.

	/**
	 * Annotation DatabaseMapping value TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_INDEXES_METHOD_GET_VALUES = "value";

	// DatabaseMappingColumnProperty Constants.

	/**
	 * Annotation DatabaseMapping name TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_METHOD_GET_NAME = "name";

	/**
	 * Annotation DatabaseMapping value TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_METHOD_GET_VALUE = "value";

	/**
	 * Annotation DatabaseMapping primary_key TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_PRIMARY_KEY = DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY;

	/**
	 * Annotation DatabaseMapping unique TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_UNIQUE = DATABASE_MAPPING_DESCRIPTOR_UNIQUE;

	/**
	 * Annotation DatabaseMapping not_null TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_NOT_NULL = DATABASE_MAPPING_DESCRIPTOR_NOT_NULL;

	/**
	 * Annotation DatabaseMapping default TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_DEFAULT = DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE;

	/**
	 * Annotation DatabaseMapping check TYPE.
	 */
	public String ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_CHECK = DATABASE_MAPPING_DESCRIPTOR_CHECK;

	// DatabaseMappingRelationshipProperty Constants.

	/**
	 * Annotation DatabaseMapping Relationship Property Get Name.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_PROPERTY_METHOD_GET_NAME = "name";

	/**
	 * Annotation DatabaseMapping Relationship Property Get Value.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_PROPERTY_METHOD_GET_VALUE = "value";

	/**
	 * Annotation DatabaseMapping Relationship Property Load.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_PROPERTY_LOAD = "load";

	// DatabaseMappingOneToOneRelationship Constants.

	/**
	 * Annotation DatabaseMapping Relationship On Update Attribute.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_ON_UPDATE_ATTRIBUTE = "onUpdate";

	/**
	 * Annotation DatabaseMapping Relationship On Delete Attribute.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_ON_DELETE_ATTRIBUTE = "onDelete";

	/**
	 * Annotation DatabaseMapping Relationship Get Properties.
	 */
	public String ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_PROPERTIES_METHOD_GET_PROPERTIES = "properties";

	// QueryBuilder Constants.

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

	/**
	 * Query Builder ISNULL Constant.
	 */
	public String QUERY_BUILDER_IS_NULL = "ISNULL";

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
	 * SQLite Database File Extension.
	 */
	public String SQLITE_DATABASE_EXTENSION = ".db";

	/**
	 * SQLite Enable Foreign Key Query.
	 */
	public String SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING = "PRAGMA foreign_keys=ON;";

	// DatabaseUtils Path Constants.

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
	 * TRUE Values
	 */
	public String TRUE = "true";

	/**
	 * FALSE Values
	 */
	public String FALSE = "false";

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
	 * Android Metadata Table Name
	 */
	public String ANDROID_METADATA_TABLE_NAME = "android_metadata";

	
	/**
	 * Inline Resource Constants
	 */
	public String RESOURCE_OPEN_CURLY_BRACKET = "{";
	public String RESOURCE_CLOSE_CURLY_BRACKET = "}";
	public String RESOURCE_SLASH = "-";
	public String RESOURCE_COMMA = ",";
	public String RESOURCE_SPACE = " ";
	public String RESOURCE_INLINE_REFERENCE = "@inline";
	public String RESOURCE_SELF_REFERENCE = "@self";
	public String RESOURCE_REFER_REFERENCE = "@refer";
	public String RESOURCE_DOT = ".";

	public String NEW_LINE = "\n";
	
}
