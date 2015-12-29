/** 
 * [SIMINOV FRAMEWORK]
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




using Siminov.Core.Database.Design;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Sqlite
{

    /// <summary>
    /// Provides the IQueryBuilder implementation for SQLite
    /// </summary>
    public class QueryBuilder : IQueryBuilder
    {
        public override String FormTableInfoQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER];
            return "pragma table_info(" + tableName + ")";
        }



        public override String FormFetchDatabaseVersionQuery(IDictionary<String, Object> parameters)
        {
            return "PRAGMA user_version;";
        }


        public override String FormUpdateDatabaseVersionQuery(IDictionary<String, Object> parameters)
        {
            Double databaseVersion = (Double)parameters[IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER];
            return "PRAGMA user_version=" + databaseVersion;
        }


        public override String FormAlterAddColumnQuery(IDictionary<String, Object> parameters)
        {
            String tableName = (String)parameters[IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER];
            String columnName = (String)parameters[IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER];

            return "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT";
        }



        public override String FormTableNames(IDictionary<String, Object> parameters)
        {
            return "SELECT * FROM sqlite_master WHERE type='table'";
        }



        public override String FormCreateTableQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER];
            IEnumerator<String> columnNames = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER];
            IEnumerator<String> columnTypes = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER];
            IEnumerator<String> defaultValues = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER];
            IEnumerator<String> checks = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER];
            IEnumerator<String> primaryKeys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER];
            IEnumerator<Boolean> isNotNull = (IEnumerator<Boolean>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER];
            IEnumerator<String> uniqueColumns = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER];
            String foreignKeys = (String)parameters[IQueryBuilder.FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("CREATE TABLE IF NOT EXISTS " + tableName + " (");

            int index = 0;
            while (columnNames.MoveNext() && columnTypes.MoveNext() && isNotNull.MoveNext() && defaultValues.MoveNext() && checks.MoveNext())
            {
                String columnName = columnNames.Current;
                String columnType = columnTypes.Current;

                bool notNull = isNotNull.Current;

                String defaultValue = defaultValues.Current;
                String check = checks.Current;

                if (index == 0)
                {
                    query.Append(columnName + " " + columnType);
                }
                else
                {
                    query.Append(", " + columnName + " " + columnType);
                }

                if (notNull)
                {
                    query.Append(" " + "NOT NULL");
                }

                if (defaultValue != null && defaultValue.Length > 0)
                {
                    query.Append(" DEFAULT '" + defaultValue + "' ");
                }

                if (check != null && check.Length > 0)
                {
                    query.Append(" CHECK('" + check + "')");
                }

                index++;
            }

            StringBuilder primaryKey = new StringBuilder();

            index = 0;

            bool isPrimaryKeysPresent = false;
            primaryKey.Append("PRIMARY KEY(");
            while (primaryKeys.MoveNext())
            {

                if (index == 0)
                {
                    primaryKey.Append(primaryKeys.Current);
                    isPrimaryKeysPresent = true;
                }
                else
                {
                    primaryKey.Append(", " + primaryKeys.Current);
                }

                index++;
            }
            primaryKey.Append(")");

            StringBuilder uniqueColumn = new StringBuilder();

            index = 0;

            bool isUniqueKeysPresent = false;
            uniqueColumn.Append("UNIQUE (");
            while (uniqueColumns.MoveNext())
            {

                if (index == 0)
                {
                    uniqueColumn.Append(uniqueColumns.Current);
                    isUniqueKeysPresent = true;
                }
                else
                {
                    uniqueColumn.Append(", " + uniqueColumns.Current);
                }

                index++;
            }

            uniqueColumn.Append(")");

            if (isPrimaryKeysPresent)
            {
                query.Append(", " + primaryKey);
            }

            if (isUniqueKeysPresent)
            {
                query.Append(", " + uniqueColumn);
            }

            if (foreignKeys.Length > 0)
            {
                query.Append(", " + foreignKeys);
            }

            query.Append(")");

            return query.ToString();
        }


        public override String FormCreateIndexQuery(IDictionary<String, Object> parameters)
        {

            String indexName = (String)parameters[IQueryBuilder.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER];
            String tableName = (String)parameters[IQueryBuilder.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER];
            IEnumerator<String> columnNames = (IEnumerator<String>)parameters[IQueryBuilder.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER];
            bool isUnique = (bool)parameters[IQueryBuilder.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER];

            StringBuilder query = new StringBuilder();
            if (isUnique)
            {
                query.Append(" CREATE UNIQUE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + "(");
            }
            else
            {
                query.Append(" CREATE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + "(");
            }

            int index = 0;
            while (columnNames.MoveNext())
            {
                if (index == 0)
                {
                    query.Append(columnNames.Current);
                }
                else
                {
                    query.Append(", " + columnNames.Current);
                }

                index++;
            }

            query.Append(")");
            return query.ToString();
        }

        public override String FormDropTableQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("DROP TABLE IF EXISTS " + tableName);

            return query.ToString();
        }

        public override String FormDropIndexQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER];
            String indexName = (String)parameters[IQueryBuilder.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("DROP INDEX IF EXISTS " + indexName + " ON " + tableName);

            return query.ToString();
        }

        public override String FormSelectQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER];
            bool distinct = (bool)parameters[IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> columnNames = (IEnumerator<String>)parameters[IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER];
            IEnumerator<String> orderBy = (IEnumerator<String>)parameters[IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER];
            String whichOrderBy = (String)parameters[IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER];
            String limit = (String)parameters[IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER];


            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            StringBuilder orderBysBuilder = new StringBuilder();

            index = 0;
            if (orderBy != null)
            {
                while (orderBy.MoveNext())
                {
                    if (index == 0)
                    {
                        orderBysBuilder.Append(orderBy.Current);
                    }
                    else
                    {
                        orderBysBuilder.Append(", " + orderBy.Current);
                    }

                    index++;
                }
            }

            return FormSelectQuery(tableName, distinct, whereClause, columnNames, groupBysBuilder.ToString(), having, orderBysBuilder.ToString(), whichOrderBy, limit);
        }

        private String FormSelectQuery(String table, bool distinct, String whereClause, IEnumerator<String> columnsNames, String groupBys, String having, String orderBys, String whichOrderBy, String limit)
        {
            if ((groupBys == null || groupBys.Length <= 0) && !(having == null || having.Length <= 0))
            {
                throw new ArgumentException(
                        "HAVING clauses are only permitted when using a groupBy clause");
            }

            if (!(limit == null || limit.Length <= 0))
            {
                Regex rx = new Regex("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
                MatchCollection matches = rx.Matches(limit);

                if (matches.Count <= 0)
                {
                    throw new ArgumentException("invalid LIMIT clauses:" + limit);
                }
            }

            StringBuilder query = new StringBuilder(120);

            query.Append("SELECT ");
            if (distinct)
            {
                query.Append("DISTINCT ");
            }
            else
            {
                query.Append("* ");
            }

            if (columnsNames != null)
            {
                if (columnsNames.MoveNext())
                {
                    AppendColumns(query, columnsNames);
                }
            }

            query.Append("FROM ");
            query.Append(table);
            AppendClause(query, " WHERE ", whereClause);
            AppendClause(query, " GROUP BY ", groupBys);
            AppendClause(query, " HAVING ", having);

            if (whichOrderBy != null && whichOrderBy.Length > 0)
            {
                AppendClause(query, " ORDER BY ", orderBys + " " + whichOrderBy);
            }
            else
            {
                AppendClause(query, " ORDER BY ", orderBys);
            }

            AppendClause(query, " LIMIT ", limit);

            return query.ToString();
        }

        private static void AppendClause(StringBuilder s, String name, String clause)
        {
            if (!(clause == null || clause.Length <= 0))
            {
                s.Append(name);
                s.Append(clause);
            }
        }

        private static void AppendColumns(StringBuilder s, IEnumerator<String> columns)
        {

            int index = 0;
            while (columns.MoveNext())
            {
                String column = columns.Current;

                if (column != null)
                {
                    if (index > 0)
                    {
                        s.Append(", ");
                    }

                    s.Append(column);
                    index++;
                }
            }

            s.Append(' ');
        }

        public override String FormSaveBindQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER];
            IEnumerator<String> columnNames = (IEnumerator<String>)parameters[IQueryBuilder.FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER];

            StringBuilder query = new StringBuilder();
            query.Append("INSERT INTO " + tableName + "(");

            int index = 0;
            while (columnNames.MoveNext())
            {
                if (index == 0)
                {
                    query.Append(columnNames.Current);
                }
                else
                {
                    query.Append(", " + columnNames.Current);
                }

                index++;
            }

            query.Append(") VALUES(");

            for (int i = 0; i < index; i++)
            {
                if (i == 0)
                {
                    query.Append("?");
                }
                else
                {
                    query.Append(", ?");
                }
            }

            query.Append(")");
            return query.ToString();
        }


        public override String FormUpdateBindQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER];
            IEnumerator<String> columnNames = (IEnumerator<String>)parameters[IQueryBuilder.FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("UPDATE " + tableName + " SET ");

            int index = 0;
            while (columnNames.MoveNext())
            {
                if (index == 0)
                {
                    query.Append(columnNames.Current + "= ?");
                }
                else
                {
                    query.Append(", " + columnNames.Current + "= ?");
                }

                index++;
            }

            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" WHERE " + whereClause);
            }

            return query.ToString();
        }


        public override String FormDeleteQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("DELETE FROM " + tableName);

            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" WHERE " + whereClause);
            }

            return query.ToString();
        }


        public override String FormCountQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_COUNT_QUERY_COLUMN_PARAMETER];
            bool distinct = (bool)parameters[IQueryBuilder.FORM_COUNT_QUERY_DISTINCT_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_COUNT_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            if (column != null && column.Length > 0)
            {
                if (distinct)
                {
                    query.Append("SELECT COUNT(DISTINCT " + column + " ) FROM " + tableName);
                }
                else
                {
                    query.Append("SELECT COUNT(" + column + ") FROM " + tableName);
                }
            }
            else
            {
                query.Append("SELECT COUNT(*) FROM " + tableName);
            }

            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }


        public override String FormAvgQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_AVG_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_AVG_QUERY_COLUMN_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_AVG_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_AVG_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("SELECT AVG(" + column + ")" + " FROM " + tableName);

            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }

        public override String FormSumQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_SUM_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_SUM_QUERY_COLUMN_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_SUM_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_SUM_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("SELECT SUM(" + column + ")" + " FROM " + tableName);


            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }


        public override String FormTotalQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_TOTAL_QUERY_COLUMN_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_TOTAL_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("SELECT TOTAL(" + column + ")" + " FROM " + tableName);


            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }

        public override String FormMaxQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_MAX_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_MAX_QUERY_COLUMN_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_MAX_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_MAX_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("SELECT MAX(" + column + ")" + " FROM " + tableName);


            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }


        public override String FormMinQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_MIN_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_MIN_QUERY_COLUMN_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_MIN_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_MIN_QUERY_HAVING_PARAMETER];


            StringBuilder query = new StringBuilder();
            query.Append("SELECT MIN(" + column + ")" + " FROM " + tableName);


            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" " + " WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }


        public override String FormGroupConcatQuery(IDictionary<String, Object> parameters)
        {

            String tableName = (String)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER];
            String column = (String)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER];
            String delimiter = (String)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER];
            String whereClause = (String)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER];
            String having = (String)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER];
            IEnumerator<String> groupBys = (IEnumerator<String>)parameters[IQueryBuilder.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER];


            StringBuilder query = new StringBuilder();
            if (delimiter == null || delimiter.Length <= 0)
            {
                query.Append("SELECT GROUP_CONCAT(" + column + ")" + " FROM " + tableName);
            }
            else
            {
                query.Append("SELECT GROUP_CONCAT(" + column + ", " + delimiter + ")" + " FROM " + tableName);
            }

            if (whereClause != null && whereClause.Length > 0)
            {
                query.Append(" WHERE " + whereClause);
            }

            StringBuilder groupBysBuilder = new StringBuilder();

            int index = 0;
            if (groupBys != null)
            {
                while (groupBys.MoveNext())
                {
                    if (index == 0)
                    {
                        groupBysBuilder.Append(groupBys.Current);
                    }
                    else
                    {
                        groupBysBuilder.Append(", " + groupBys.Current);
                    }

                    index++;
                }
            }

            AppendClause(query, " GROUP BY ", groupBysBuilder.ToString());
            AppendClause(query, " HAVING ", having);

            return query.ToString();
        }



        public override String FormForeignKeyQuery(IDictionary<String, Object> parameters)
        {

            EntityDescriptor child = (EntityDescriptor)parameters[IQueryBuilder.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER];

            IEnumerator<EntityDescriptor.Relationship> oneToManyRealtionships = child.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRealtionships = child.GetManyToManyRelationships();

            ICollection<EntityDescriptor.Relationship> relationships = new List<EntityDescriptor.Relationship>();

            while (oneToManyRealtionships.MoveNext())
            {
                EntityDescriptor.Relationship relationship = oneToManyRealtionships.Current;

                relationships.Add(relationship);
            }

            while (manyToManyRealtionships.MoveNext())
            {
                EntityDescriptor.Relationship relationship = manyToManyRealtionships.Current;

                relationships.Add(relationship);
            }

            StringBuilder foreignKeysQuery = new StringBuilder();
            IEnumerator<EntityDescriptor.Relationship> relationshipsIterator = relationships.GetEnumerator();
            while (relationshipsIterator.MoveNext())
            {

                StringBuilder foreignKeyQuery = new StringBuilder();
                EntityDescriptor.Relationship relationship = relationshipsIterator.Current;

                EntityDescriptor referedEntityDescriptor = relationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = ResourceManager.GetInstance().RequiredEntityDescriptorBasedOnClassName(relationship.GetReferTo());
                    relationship.SetReferedEntityDescriptor(referedEntityDescriptor);

                    relationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                String parentTable = referedEntityDescriptor.GetTableName();
                ICollection<EntityDescriptor.Attribute> foreignAttributes = null;
                try
                {
                    foreignAttributes = GetForeignKeys(referedEntityDescriptor);
                }
                catch (DatabaseException databaseException)
                {
                    Log.Log.Error(typeof(QueryBuilder).FullName, "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.GetMessage());
                    throw new DeploymentException(typeof(QueryBuilder).FullName, "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.GetMessage());
                }

                IEnumerator<EntityDescriptor.Attribute> foreignAttributesIterate = foreignAttributes.GetEnumerator();

                ICollection<String> foreignKeys = new List<String>();
                while (foreignAttributesIterate.MoveNext())
                {
                    foreignKeys.Add(foreignAttributesIterate.Current.GetColumnName());
                }

                IEnumerator<String> foreignKeysIterate = foreignKeys.GetEnumerator();

                foreignKeyQuery.Append("FOREIGN KEY(");

                int index = 0;
                while (foreignKeysIterate.MoveNext())
                {
                    if (index == 0)
                    {
                        foreignKeyQuery.Append(foreignKeysIterate.Current);
                    }
                    else
                    {
                        foreignKeyQuery.Append(", " + foreignKeysIterate.Current);
                    }

                    index++;
                }

                foreignKeyQuery.Append(") REFERENCES " + parentTable + "(");
                foreignKeysIterate = foreignKeys.GetEnumerator();

                index = 0;
                while (foreignKeysIterate.MoveNext())
                {
                    if (index == 0)
                    {
                        foreignKeyQuery.Append(foreignKeysIterate.Current);
                    }
                    else
                    {
                        foreignKeyQuery.Append(", " + foreignKeysIterate.Current);
                    }

                    index++;
                }

                foreignKeyQuery.Append(")");

                String onDeleteAction = relationship.GetOnDelete();
                String onUpdateAction = relationship.GetOnUpdate();

                if (onDeleteAction != null && onDeleteAction.Length > 0)
                {
                    if (onDeleteAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE, StringComparison.OrdinalIgnoreCase))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_DELETE + " " + Constants.QUERY_BUILDER_CASCADE);
                    }
                    else if (onDeleteAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_DELETE + " " + Constants.QUERY_BUILDER_RESTRICT);
                    }
                    else if (onDeleteAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_DELETE + " " + Constants.QUERY_BUILDER_NO_ACTION);
                    }
                    else if (onDeleteAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_DELETE + " " + Constants.QUERY_BUILDER_SET_NULL);
                    }
                    else if (onDeleteAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_DELETE + " " + Constants.QUERY_BUILDER_SET_DEFAULT);
                    }
                }

                if (onUpdateAction != null && onUpdateAction.Length > 0)
                {
                    if (onUpdateAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_UPDATE + " " + Constants.QUERY_BUILDER_CASCADE);
                    }
                    else if (onUpdateAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_UPDATE + " " + Constants.QUERY_BUILDER_RESTRICT);
                    }
                    else if (onUpdateAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_UPDATE + " " + Constants.QUERY_BUILDER_NO_ACTION);
                    }
                    else if (onUpdateAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_UPDATE + " " + Constants.QUERY_BUILDER_SET_NULL);
                    }
                    else if (onUpdateAction.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT))
                    {
                        foreignKeyQuery.Append(" " + Constants.QUERY_BUILDER_ON_UPDATE + " " + Constants.QUERY_BUILDER_SET_DEFAULT);
                    }
                }

                if (foreignKeyQuery.Length > 0)
                {
                    if (foreignKeysQuery.Length <= 0)
                    {
                        foreignKeysQuery.Append(" " + foreignKeyQuery);
                    }
                    else
                    {
                        foreignKeysQuery.Append(", " + foreignKeyQuery);
                    }
                }
            }

            return foreignKeysQuery.ToString();
        }

        private ICollection<EntityDescriptor.Attribute> GetForeignKeys(EntityDescriptor entityDescriptor)
        {
            IEnumerator<EntityDescriptor.Relationship> oneToManyRealtionships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRealtionships = entityDescriptor.GetManyToManyRelationships();

            ICollection<EntityDescriptor.Attribute> foreignAttributes = new List<EntityDescriptor.Attribute>();

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;
                if (attribute.IsPrimaryKey())
                {
                    foreignAttributes.Add(attribute);
                }
            }

            while (oneToManyRealtionships.MoveNext())
            {

                EntityDescriptor.Relationship relationship = oneToManyRealtionships.Current;
                EntityDescriptor referedEntityDescriptor = relationship.GetReferedEntityDescriptor();

                ICollection<EntityDescriptor.Attribute> referedForeignKeys = GetForeignKeys(referedEntityDescriptor);
                IEnumerator<EntityDescriptor.Attribute> referedForeignKeysIterate = referedForeignKeys.GetEnumerator();

                while (referedForeignKeysIterate.MoveNext())
                {
                    foreignAttributes.Add(referedForeignKeysIterate.Current);
                }
            }

            while (manyToManyRealtionships.MoveNext())
            {

                EntityDescriptor.Relationship relationship = manyToManyRealtionships.Current;
                EntityDescriptor referedEntityDescriptor = relationship.GetReferedEntityDescriptor();

                ICollection<EntityDescriptor.Attribute> referedForeignKeys = GetForeignKeys(referedEntityDescriptor);
                IEnumerator<EntityDescriptor.Attribute> referedForeignKeysIterate = referedForeignKeys.GetEnumerator();

                while (referedForeignKeysIterate.MoveNext())
                {
                    foreignAttributes.Add(referedForeignKeysIterate.Current);
                }
            }

            return foreignAttributes;
        }
    }
}
