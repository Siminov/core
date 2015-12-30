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



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core
{



    /// <summary>
    /// Contains constant values used in the framework
    /// All constants referred in the framework are defined in this class
    /// </summary>
    public abstract class Constants
    {

        // SIMINOV Descriptor Extension


        /// <summary>
        /// Siminov Descriptor Extension.
        /// </summary>
        public static readonly String SIMINOV_DESCRIPTOR_EXTENSION = ".si";

        // Application Descriptor Constants.


        /// <summary>
        /// ApplicationDescriptor.xml core TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_SIMINOV = "siminov";


        /// <summary>
        /// ApplicationDescriptor.xml File Name.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_FILE_NAME = "ApplicationDescriptor.xml";


        /// <summary>
        /// ApplicationDescriptor.xml property TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_PROPERTY = "property";


        /// <summary>
        /// ApplicationDescriptor.xml name TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_NAME = "name";


        /// <summary>
        /// ApplicationDescriptor.xml description TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_DESCRIPTION = "description";


        /// <summary>
        /// ApplicationDescriptor.xml version TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_VERSION = "version";


        /// <summary>
        /// ApplicationDescriptor.xml database-descriptor TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR = "database-descriptor";


        /// <summary>
        /// ApplicationDescriptor.xml event-handler TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_EVENT_HANDLER = "event-handler";



        /// <summary>
        /// ApplicationDescriptor.xml library descriptor TAG.
        /// </summary>
        public static readonly String APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR = "library-descriptor";


        // DatabaseDescriptor Constants.


        /// <summary>
        /// DatabaseDescriptor.xml database-descriptor TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR = "database-descriptor";


        /// <summary>
        /// DatabaseDescriptor.xml property TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_PROPERTY = "property";


        /// <summary>
        /// DatabaseDescriptor.xml property name TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_PROPERTY_NAME = "name";


        /// <summary>
        /// DatabaseDescriptor.xml description TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_DESCRIPTION = "description";


        /// <summary>
        /// DatabaseDescriptor.xml database_name TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_DATABASE_NAME = "database_name";


        /// <summary>
        /// DatabaseDescriptor.xml type TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_TYPE = "type";


        /// <summary>
        /// DatabaseDescriptor.xml version TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_VERSION = "version";



        /// <summary>
        /// DatabaseDescriptor.xml entity-descriptor TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR = "entity-descriptor";


        /// <summary>
        /// DatabaseDescriptor.xml is_locking_required TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_TRANSACTION_SAFE = "transaction_safe";


        /// <summary>
        /// DatabaseDescriptor.xml external_storage TAG.
        /// </summary>
        public static readonly String DATABASE_DESCRIPTOR_EXTERNAL_STORAGE = "external_storage";


        // Library Descriptor Constants.


        /// <summary>
        /// LibraryDescriptor.xml File Name.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_FILE_NAME = "LibraryDescriptor.xml";


        /// <summary>
        /// LibraryDescriptor.xml library TAG.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_LIBRARY_DESCRIPTOR = "library-descriptor";


        /// <summary>
        /// LibraryDescriptor.xml property TAG.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_PROPERTY = "property";


        /// <summary>
        /// LibraryDescriptor.xml name TAG.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_NAME = "name";


        /// <summary>
        /// LibraryDescriptor.xml description TAG.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_DESCRIPTION = "description";


        /// <summary>
        /// LibraryDescriptor.xml entity-descriptor TAG.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR = "entity-descriptor";



        /// <summary>
        /// LibraryDescriptor.xml entity descriptor seprator.
        /// </summary>
        public static readonly String LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR = ".";


        // Entity Descriptor Constants.


        /// <summary>
        /// EntityDescriptor.xml entity-descriptor TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR = "entity-descriptor";

        
        /// <summary>
        /// EntityDescriptor.xml table_name TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_TABLE_NAME = "table_name";


        /// <summary>
        /// EntityDescriptor.xml class_name TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_CLASS_NAME = "class_name";


        /// <summary>
        /// EntityDescriptor.xml column TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE = "attribute";


        /// <summary>
        /// EntityDescriptor.xml variable_name TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME = "variable_name";


        /// <summary>
        /// EntityDescriptor.xml column_name TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME = "column_name";


        /// <summary>
        /// EntityDescriptor.xml property TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_PROPERTY = "property";


        /// <summary>
        /// EntityDescriptor.xml name TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_NAME = "name";


        /// <summary>
        /// EntityDescriptor.xml type TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE = "type";


        /// <summary>
        /// EntityDescriptor.xml primary_key TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY = "primary_key";


        /// <summary>
        /// EntityDescriptor.xml unique TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE = "unique";


        /// <summary>
        /// EntityDescriptor.xml index TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_INDEX = "index";

        /// <summary>
        /// EntityDescriptor.xml index name TAG
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_INDEX_NAME = "name";

        /// <summary>
        /// EntityDescriptor.xml index unique TAG
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_INDEX_UNIQUE = "unique";

        /// <summary>
        /// EntityDescriptor.xml index column TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_INDEX_COLUMN = "column";

        /// <summary>
        /// EntityDescriptor.xml not_null TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL = "not_null";


        /// <summary>
        /// EntityDescriptor.xml default TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE = "default";


        /// <summary>
        /// EntityDescriptor.xml check TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK = "check";


        /// <summary>
        /// EntityDescriptor.xml Relationships TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP = "relationship";


        /// <summary>
        /// EntityDescriptor.xml Relationship refer TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_REFER = "refer";


        /// <summary>
        /// EntityDescriptor.xml Relationship refer_to TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO = "refer_to";


        /// <summary>
        /// EntityDescriptor.xml Relationship on_update TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE = "on_update";


        /// <summary>
        /// EntityDescriptor.xml Relationship on_delete TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE = "on_delete";


        /// <summary>
        /// EntityDescriptor.xml Relationship Type TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE = "type";


        /// <summary>
        /// EntityDescriptor.xml Relationship Type one-to-one TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE = "one-to-one";


        /// <summary>
        /// EntityDescriptor.xml Relationship Type one-to-many TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY = "one-to-many";


        /// <summary>
        /// EntityDescriptor.xml Relationship Type many-to-one TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE = "many-to-one";


        /// <summary>
        /// EntityDescriptor.xml Relationship Type many-to-many TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY = "many-to-many";


        /// <summary>
        /// EntityDescriptor.xml Relationship load TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD = "load";


        /// <summary>
        /// EntityDescriptor.xml Relationship Cascade TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE = "cascade";


        /// <summary>
        /// EntityDescriptor.xml Relationship Restrict TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT = "restrict";


        /// <summary>
        /// EntityDescriptor.xml Relationship no_action TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION = "no_action";


        /// <summary>
        /// EntityDescriptor.xml Relationship set-null TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL = "set_null";


        /// <summary>
        /// EntityDescriptor.xml relationship set_default TAG.
        /// </summary>
        public static readonly String ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT = "set_default";



        // Query Builder Constants.


        /// <summary>
        /// Query Builder ON DELETE Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_ON_DELETE = "ON DELETE";


        /// <summary>
        /// Query Builder ON UPDATE Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_ON_UPDATE = "ON UPDATE";


        /// <summary>
        /// Query Builder CASCADE Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_CASCADE = "CASCADE";


        /// <summary>
        /// Query Builder RESTRICT Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_RESTRICT = "RESTRICT";


        /// <summary>
        /// Query Builder NO ACTION Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_NO_ACTION = "NO ACTION";


        /// <summary>
        /// Query Builder SET NULL Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_SET_NULL = "SET NULL";


        /// <summary>
        /// Query Builder SET DEFAULT Constant.
        /// </summary>
        public static readonly String QUERY_BUILDER_SET_DEFAULT = "SET DEFAULT";


        // SQLite Database Method Names.


        /// <summary>
        /// SQLite setLockingEnabled Method.
        /// </summary>
        public static readonly String SQLITE_DATABASE_ENABLE_LOCKING = "setLockingEnabled";


        /// <summary>
        /// SQLite beginTransaction Method.
        /// </summary>
        public static readonly String SQLITE_DATABASE_BEGIN_TRANSACTION = "BeginTransaction";


        /// <summary>
        /// SQLite setTransactionSuccessful Method.
        /// </summary>
        public static readonly String SQLITE_DATABASE_COMMIT_TRANSACTION = "Commit";


        /// <summary>
        /// SQLite endTransaction Method.
        /// </summary>
        public static readonly String SQLITE_DATABASE_END_TRANSACTION = "endTransaction";



        /// <summary>
        /// SQLite Enable Foreign Key Query.
        /// </summary>
        public static readonly String SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING = "PRAGMA foreign_keys=ON;";

        // Database Utils Path Constants.


        /// <summary>
        /// Database Path Data.
        /// </summary>
        public static readonly String DATABASE_PATH_DATA = "data";


        /// <summary>
        /// Database Path Constant.
        /// </summary>
        public static readonly String DATABASE_PATH_DATABASE = "Databases";


        /// <summary>
        /// XMl File Extension.
        /// </summary>
        public static readonly String XML_FILE_EXTENSION = ".xml";



        /// <summary>
        /// Database Constants.
        /// </summary>
        public static readonly String SQLITE_DATABASE = "Sqlite";


        /// <summary>
        /// Form Create Table Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER = "FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Create Table Query Column Names Parameter.
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER = "FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER";


        /// <summary>
        /// Form Create Table Query Column Types Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER = "FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER";


        /// <summary>
        /// Form Create Table Query Default Values Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER = "FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER";


        /// <summary>
        /// Form Create Table Query Checks Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER = "FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER";


        /// <summary>
        /// Form Create Table Query Primary Keys Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER = "FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER";


        /// <summary>
        /// Form Create Table Query Not Nulls Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER = "FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER";


        /// <summary>
        /// Form Create Table Query Unique Columns Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER = "FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER";


        /// <summary>
        /// Form Create Table Query Foreign Keys Parameter
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER = "FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER";




        /// <summary>
        /// Form Create Index Query Index Name Parameter
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER = "FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER";


        /// <summary>
        /// Form Create Index Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER = "FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Create Index Query Column Names Parameter
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER = "FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER";


        /// <summary>
        /// Form Create Index Query Is Unique Parameter
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER = "FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER";




        /// <summary>
        /// Form Drop Table Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER = "FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER";




        /// <summary>
        /// Form Drop Index Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER = "FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Drop Index Query Index Name Parameter
        /// </summary>
        public static readonly String FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER = "FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER";




        /// <summary>
        /// Form Select Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_TABLE_NAME_PARAMETER = "FORM_SELECT_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Select Query Distinct Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_DISTINCT_PARAMETER = "FORM_SELECT_QUERY_DISTINCT_PARAMETER";


        /// <summary>
        /// Form Select Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Select Query Column Names Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER = "FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER";


        /// <summary>
        /// Form Select Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_GROUP_BYS_PARAMETER = "FORM_SELECT_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Select Query Having Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_HAVING_PARAMETER = "FORM_SELECT_QUERY_HAVING_PARAMETER";


        /// <summary>
        /// Form Select Query Order Bys Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_ORDER_BYS_PARAMETER = "FORM_SELECT_QUERY_ORDER_BYS_PARAMETER";


        /// <summary>
        /// Form Select Query Which Order By Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER = "FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER";


        /// <summary>
        /// Form Select Query Limit Parameter
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_LIMIT_PARAMETER = "FORM_SELECT_QUERY_LIMIT_PARAMETER";




        /// <summary>
        /// Form Save Bind Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER = "FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Save Bind Query Column Names Parameter
        /// </summary>
        public static readonly String FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER = "FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER";



        /// <summary>
        /// Form Update Bind Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER = "FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Update Bind Query Column Names Parameter
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER = "FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER";


        /// <summary>
        /// Form Update Bind Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER";




        /// <summary>
        /// Form Delete Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_DELETE_QUERY_TABLE_NAME_PARAMETER = "FORM_DELETE_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Delete Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER";




        /// <summary>
        /// Form Count Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_TABLE_NAME_PARAMETER = "FORM_COUNT_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Count Query Column Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_COLUMN_PARAMETER = "FORM_COUNT_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Count Query Distinct Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_DISTINCT_PARAMETER = "FORM_COUNT_QUERY_DISTINCT_PARAMETER";


        /// <summary>
        /// Form Count Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Count Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_GROUP_BYS_PARAMETER = "FORM_COUNT_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Count Query Having Parameter
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_HAVING_PARAMETER = "FORM_COUNT_QUERY_HAVING_PARAMETER";



        /// <summary>
        /// Form Avg Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_AVG_QUERY_TABLE_NAME_PARAMETER = "FORM_AVG_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Avg Query Column Parameter
        /// </summary>
        public static readonly String FORM_AVG_QUERY_COLUMN_PARAMETER = "FORM_AVG_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Avg Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Avg Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_AVG_QUERY_GROUP_BYS_PARAMETER = "FORM_AVG_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Avg Query Having Parameter
        /// </summary>
        public static readonly String FORM_AVG_QUERY_HAVING_PARAMETER = "FORM_AVG_QUERY_HAVING_PARAMETER";




        /// <summary>
        /// Form Sum Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_SUM_QUERY_TABLE_NAME_PARAMETER = "FORM_SUM_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Sum Query Column Parameter
        /// </summary>
        public static readonly String FORM_SUM_QUERY_COLUMN_PARAMETER = "FORM_SUM_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Sum Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Sum Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_SUM_QUERY_GROUP_BYS_PARAMETER = "FORM_SUM_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Sum Query Having Parameter
        /// </summary>
        public static readonly String FORM_SUM_QUERY_HAVING_PARAMETER = "FORM_SUM_QUERY_HAVING_PARAMETER";



        /// <summary>
        /// Form Total Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER = "FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Total Query Column Parameter
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_COLUMN_PARAMETER = "FORM_TOTAL_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Total Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Total Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER = "FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Total Query Having Parameter
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_HAVING_PARAMETER = "FORM_TOTAL_QUERY_HAVING_PARAMETER";




        /// <summary>
        /// Form Max Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_MAX_QUERY_TABLE_NAME_PARAMETER = "FORM_MAX_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Max Query Column Parameter
        /// </summary>
        public static readonly String FORM_MAX_QUERY_COLUMN_PARAMETER = "FORM_MAX_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Max Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Max Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_MAX_QUERY_GROUP_BYS_PARAMETER = "FORM_MAX_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Max Query Having Parameter
        /// </summary>
        public static readonly String FORM_MAX_QUERY_HAVING_PARAMETER = "FORM_MAX_QUERY_HAVING_PARAMETER";




        /// <summary>
        /// Form Min Query Table Name Parameter 
        /// </summary>
        public static readonly String FORM_MIN_QUERY_TABLE_NAME_PARAMETER = "FORM_MIN_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Min Query Column Parameter
        /// </summary>
        public static readonly String FORM_MIN_QUERY_COLUMN_PARAMETER = "FORM_MIN_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Min Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Min Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_MIN_QUERY_GROUP_BYS_PARAMETER = "FORM_MIN_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Min Query Having Parameter
        /// </summary>
        public static readonly String FORM_MIN_QUERY_HAVING_PARAMETER = "FORM_MIN_QUERY_HAVING_PARAMETER";




        /// <summary>
        /// Form Group Concat Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER = "FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Group Concat Query Column Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER = "FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER";


        /// <summary>
        /// Form Group Concat Query Where Clause Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER = "FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER";


        /// <summary>
        /// Form Group Concat Query Group Bys Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER = "FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER";


        /// <summary>
        /// Form Group Concat Query Having Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER = "FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER";


        /// <summary>
        /// Form Group Concat Query Delimiter Parameter
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER = "FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER";



        /// <summary>
        /// Form Alter Add Column Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER = "FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Alter Add Column Query Column Name Parameter
        /// </summary>
        public static readonly String FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER = "FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER";



        /// <summary>
        /// Form Foreign Keys Database Descriptor Parameter
        /// </summary>
        public static readonly String FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER = "FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER";



        /// <summary>
        /// Form Table Info Query Table Name Parameter
        /// </summary>
        public static readonly String FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER = "FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER";


        /// <summary>
        /// Form Table Info Query Name
        /// </summary>
        public static readonly String FORM_TABLE_INFO_QUERY_NAME = "name";




        /// <summary>
        /// Form Update Database Version Query Database Version Parameter
        /// </summary>
        public static readonly String FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER = "FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER";




        /// <summary>
        /// Form Table Names Name
        /// </summary>
        public static readonly String FORM_TABLE_NAMES_NAME = "name";




        /// <summary>
        /// Android Meta data Table Name
        /// </summary>
        public static readonly String ANDROID_METADATA_TABLE_NAME = "android_metadata";


        /// <summary>
        /// New Line 
        /// </summary>
        public static readonly String NEW_LINE = "\n";
    }
}
