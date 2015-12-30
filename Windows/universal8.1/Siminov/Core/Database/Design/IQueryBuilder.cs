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

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes API's to build database queries.
    /// </summary>
    public abstract class IQueryBuilder
    {

        /*
         * Form Table Info Query	
         */

        /// <summary>
        /// Table name parameter to create get table info query
        /// </summary>
        public static readonly String FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Build query to get table info.
        /// </summary>
        /// <param name="parameters">Required to build query.</param>
        /// <returns>Table Info Query.</returns>
        public abstract String FormTableInfoQuery(IDictionary<String, Object> parameters);



        /*
         * 	Form Fetch Database Version Query
         */

        /// <summary>
        /// Build query to get database version.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Fetch Database Version Query</returns>
        public abstract String FormFetchDatabaseVersionQuery(IDictionary<String, Object> parameters);



        /*
         * Form Update Database Version Query.	
         */

        /// <summary>
        /// Database version parameter to create update database query
        /// </summary>
        public static readonly String FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER = Constants.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER;

        /// <summary>
        /// Build query to update database version.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Update Database Version Query</returns>
        public abstract String FormUpdateDatabaseVersionQuery(IDictionary<String, Object> parameters);



        /*
         * Form Alter Add Column Query	
         */

        /// <summary>
        /// Table name parameter to create alter add column query
        /// </summary>
        public static readonly String FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column name parameter to create alter add column query
        /// </summary>
        public static readonly String FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER = Constants.FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER;

        /// <summary>
        /// Build query to alter add new column to table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Alter Add New Column Query</returns>
        public abstract String FormAlterAddColumnQuery(IDictionary<String, Object> parameters);



        /*
         * Form Table Names	
         */

        /// <summary>
        /// Build query to get all table names exists in database
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Table Names Query</returns>
        public abstract String FormTableNames(IDictionary<String, Object> parameters);



        /*
         * Form Create Table Query	
         */
        /// <summary>
        /// Table name parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column names parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER;

        /// <summary>
        /// Column types parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER;

        /// <summary>
        /// Default values parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER;

        /// <summary>
        /// Checks parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER;

        /// <summary>
        /// Primary keys parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER;

        /// <summary>
        /// Not nulls parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER;

        /// <summary>
        /// Unique columns parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER;

        /// <summary>
        /// Foreign keys parameter to form create table query
        /// </summary>
        public static readonly String FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER = Constants.FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER;

        /// <summary>
        /// Build query to create table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Create Table Query</returns>
        public abstract String FormCreateTableQuery(IDictionary<String, Object> parameters);



        /*
         * Form Create Index Query
         */
        /// <summary>
        /// Index name parameter to form create index query
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER = Constants.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER;

        /// <summary>
        /// Table name parameter to form create index query
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column names parameter to form create index query
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER = Constants.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER;

        /// <summary>
        /// Is unique parameter to form create index query
        /// </summary>
        public static readonly String FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER = Constants.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER;

        /// <summary>
        /// Build query to create index.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Create Index Query</returns>
        public abstract String FormCreateIndexQuery(IDictionary<String, Object> parameters);



        /*
         * Form Drop Table Query
         */
        /// <summary>
        /// Table name parameter to create drop table query
        /// </summary>
        public static readonly String FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Build query to drop table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Drop Table Query</returns>
        public abstract String FormDropTableQuery(IDictionary<String, Object> parameters);


        /*
         * Form Drop Index Query
         */
        /// <summary>
        /// Table name parameter to create drop index query
        /// </summary>
        public static readonly String FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Index name parameter to create drop index query
        /// </summary>
        public static readonly String FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER = Constants.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER;

        /// <summary>
        /// Build query to drop index from table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Drop Index Query</returns>
        public abstract String FormDropIndexQuery(IDictionary<String, Object> parameters);



        /*
         * Form Select Query	
         */

        /// <summary>
        /// Table name parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Distinct parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_DISTINCT_PARAMETER = Constants.FORM_SELECT_QUERY_DISTINCT_PARAMETER;

        /// <summary>
        /// Where clause parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Column names parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER = Constants.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER;

        /// <summary>
        /// Group bys parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER;

        /// <summary>
        /// Having parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_HAVING_PARAMETER = Constants.FORM_SELECT_QUERY_HAVING_PARAMETER;

        /// <summary>
        /// Order by parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_ORDER_BYS_PARAMETER = Constants.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER;

        /// <summary>
        /// Which order by parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER = Constants.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER;

        /// <summary>
        /// Limit parameter to create select query
        /// </summary>
        public static readonly String FORM_SELECT_QUERY_LIMIT_PARAMETER = Constants.FORM_SELECT_QUERY_LIMIT_PARAMETER;

        /// <summary>
        /// Build query to fetch tuples from table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Select Query</returns>
        public abstract String FormSelectQuery(IDictionary<String, Object> parameters);



        /*
         * Form Save Bind Query	
         */
        /// <summary>
        /// Table name parameter to create save bind query
        /// </summary>
        public static readonly String FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column names parameter to create save bind query
        /// </summary>
        public static readonly String FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER = Constants.FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER;

        /// <summary>
        /// Build query to insert data into table.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Save Query</returns>
        public abstract String FormSaveBindQuery(IDictionary<String, Object> parameters);



        /*
         * Form Update Bind Query	
         */
        /// <summary>
        /// Table name parameter to create update bind query
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column names parameter to create update bind query
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER = Constants.FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER;

        /// <summary>
        /// Where clause parameter to create update bind query
        /// </summary>
        public static readonly String FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Build query to update tuples.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Update Query</returns>
        public abstract String FormUpdateBindQuery(IDictionary<String, Object> parameters);



        /*
         * Form Delete Query	
         */
        /// <summary>
        /// Table name parameter to create delete query
        /// </summary>
        public static readonly String FORM_DELETE_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Where clause parameter to create delete query
        /// </summary>
        public static readonly String FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Build query to delete tuples.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Delete Query</returns>
        public abstract String FormDeleteQuery(IDictionary<String, Object> parameters);



        /*
         * Form Count Query	
         */
        /// <summary>
        /// Table name parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_COLUMN_PARAMETER = Constants.FORM_COUNT_QUERY_COLUMN_PARAMETER;

        /// <summary>
        /// Distinct parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_DISTINCT_PARAMETER = Constants.FORM_COUNT_QUERY_DISTINCT_PARAMETER;

        /// <summary>
        /// Where clause parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Group bys parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER;


        /// <summary>
        /// Having parameter to create count query
        /// </summary>
        public static readonly String FORM_COUNT_QUERY_HAVING_PARAMETER = Constants.FORM_COUNT_QUERY_HAVING_PARAMETER;

        /// <summary>
        /// Build query to get count of tuples.
        /// </summary>
        /// <param name="parameters">Required to build query.</param>
        /// <returns>Count Query</returns>
        public abstract String FormCountQuery(IDictionary<String, Object> parameters);


        /*
         * Form Average Query.
         */
        /// <summary>
        /// Table name parameter to create avg query
        /// </summary>
        public static readonly String FORM_AVG_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_AVG_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column parameter to create avg query
        /// </summary>
        public static readonly String FORM_AVG_QUERY_COLUMN_PARAMETER = Constants.FORM_AVG_QUERY_COLUMN_PARAMETER;

        /// <summary>
        /// Where clause parameter to create avg query
        /// </summary>
        public static readonly String FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Group bys parameter to create avg query
        /// </summary>
        public static readonly String FORM_AVG_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_AVG_QUERY_GROUP_BYS_PARAMETER;

        /// <summary>
        /// Having parameter to create avg query
        /// </summary>
        public static readonly String FORM_AVG_QUERY_HAVING_PARAMETER = Constants.FORM_AVG_QUERY_HAVING_PARAMETER;

        /// <summary>
        /// Build query to get Average.
        /// </summary>
        /// <param name="parameters">Required to build query.</param>
        /// <returns>Average Query</returns>
        public abstract String FormAvgQuery(IDictionary<String, Object> parameters);



        /*
         * Form Sum Query.
         */
        /// <summary>
        /// Table name parameter to create sum query
        /// </summary>
        public static readonly String FORM_SUM_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_SUM_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column parameter to create sum query
        /// </summary>
        public static readonly String FORM_SUM_QUERY_COLUMN_PARAMETER = Constants.FORM_SUM_QUERY_COLUMN_PARAMETER;

        /// <summary>
        /// Where clause parameter to create sum query
        /// </summary>
        public static readonly String FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Group bys parameter to create sum query
        /// </summary>
        public static readonly String FORM_SUM_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_SUM_QUERY_GROUP_BYS_PARAMETER;

        /// <summary>
        /// Having parameter to create sum query
        /// </summary>
        public static readonly String FORM_SUM_QUERY_HAVING_PARAMETER = Constants.FORM_SUM_QUERY_HAVING_PARAMETER;

        /// <summary>
        /// Build query to get Sum.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Sum Query</returns>
        public abstract String FormSumQuery(IDictionary<String, Object> parameters);



        /*
         * Form Total Query	
         */
        /// <summary>
        /// Parameter to create total query
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column parameter to create total query
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_COLUMN_PARAMETER = Constants.FORM_TOTAL_QUERY_COLUMN_PARAMETER;

        /// <summary>
        /// Where clause parameter to create total query
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Group Bys parameter to create total query
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER;

        /// <summary>
        /// Having parameter to create total query
        /// </summary>
        public static readonly String FORM_TOTAL_QUERY_HAVING_PARAMETER = Constants.FORM_TOTAL_QUERY_HAVING_PARAMETER;

        /// <summary>
        /// Build query to get total.
        /// </summary>
        /// <param name="parameters">Required to build query.</param>
        /// <returns>Total Query</returns>
        public abstract String FormTotalQuery(IDictionary<String, Object> parameters);



        /*
         * Form Max Query.	
         */
        /// <summary>
        /// Table name parameter to create max query
        /// </summary>
        public static readonly String FORM_MAX_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_MAX_QUERY_TABLE_NAME_PARAMETER;

        /// <summary>
        /// Column parameter to create max query
        /// </summary>
        public static readonly String FORM_MAX_QUERY_COLUMN_PARAMETER = Constants.FORM_MAX_QUERY_COLUMN_PARAMETER;

        /// <summary>
        /// Where clause parameter to create max query
        /// </summary>
        public static readonly String FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER;

        /// <summary>
        /// Group bys parameter to create max query
        /// </summary>
        public static readonly String FORM_MAX_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_MAX_QUERY_GROUP_BYS_PARAMETER;


        /// <summary>
        /// Having parameter to create max query
        /// </summary>
        public static readonly String FORM_MAX_QUERY_HAVING_PARAMETER = Constants.FORM_MAX_QUERY_HAVING_PARAMETER;


        /// <summary>
        /// Build query to get Max.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Max Query</returns>
        public abstract String FormMaxQuery(IDictionary<String, Object> parameters);



        /*
         * Form Minimum Query	
         */
        /// <summary>
        /// Table name parameter to create min query
        /// </summary>
        public static readonly String FORM_MIN_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_MIN_QUERY_TABLE_NAME_PARAMETER;


        /// <summary>
        /// Column parameter to create min query
        /// </summary>
        public static readonly String FORM_MIN_QUERY_COLUMN_PARAMETER = Constants.FORM_MIN_QUERY_COLUMN_PARAMETER;


        /// <summary>
        /// Where clause parameter to create min query
        /// </summary>
        public static readonly String FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER;


        /// <summary>
        /// Group bys parameter to create min query
        /// </summary>
        public static readonly String FORM_MIN_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_MIN_QUERY_GROUP_BYS_PARAMETER;


        /// <summary>
        /// Having parameter to create min query
        /// </summary>
        public static readonly String FORM_MIN_QUERY_HAVING_PARAMETER = Constants.FORM_MIN_QUERY_HAVING_PARAMETER;


        /// <summary>
        /// Build query to get Minimum Query.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Minimum Query</returns>
        public abstract String FormMinQuery(IDictionary<String, Object> parameters);



        /*
         * Form Group Concat Query	
         */


        /// <summary>
        /// Table name parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER;


        /// <summary>
        /// Column parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER;


        /// <summary>
        /// Where clause parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER;


        /// <summary>
        /// Group bys parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER;


        /// <summary>
        /// Having parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER;


        /// <summary>
        /// Delimiter parameter to create group concat query
        /// </summary>
        public static readonly String FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER = Constants.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER;


        /// <summary>
        /// Build query to get group concat.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Group Concat Query</returns>
        public abstract String FormGroupConcatQuery(IDictionary<String, Object> parameters);


        /*
         * Form Foreign Keys Query	
         */
        /// <summary>
        /// Database descriptor parameter to create foreign keys
        /// </summary>
        public static readonly String FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER = Constants.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER;


        /// <summary>
        /// Build query to get foreign keys.
        /// </summary>
        /// <param name="parameters">Required to build query</param>
        /// <returns>Foreign Keys</returns>
        public abstract String FormForeignKeyQuery(IDictionary<String, Object> parameters);

    }
}
