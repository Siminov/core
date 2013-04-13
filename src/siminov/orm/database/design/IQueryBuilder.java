/** 
 * [SIMINOV FRAMEWORK]
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

package siminov.orm.database.design;

import java.util.Iterator;

import siminov.orm.model.DatabaseMappingDescriptor;

/**
 * Exposes API's to build database queries.
 */
public interface IQueryBuilder {

	/**
	 * Build query to create table.
	 * @param tableName Name of table.
	 * @param columnNames Name of columns.
	 * @param columnTypes Type of columns.
	 * @param defaultValues Default value of columns.
	 * @param checks Check condition of columns.
	 * @param primaryKeys Primary Keys of table.
	 * @param isNotNull Not Null fields of table.
	 * @param uniqueColumns Unique Columns of table.
	 * @param foreignKeys Foreign Keys of table.
	 * @return Generated Query.
	 */
	public String formCreateTableQuery(final String tableName, final Iterator<String> columnNames, final Iterator<String> columnTypes, final Iterator<String> defaultValues, final Iterator<String> checks, final Iterator<String> primaryKeys, final Iterator<Boolean> isNotNull, final Iterator<String> uniqueColumns, final String foreignKeys);

	/**
	 * Build query to create index on table.
	 * @param indexName Name of index.
	 * @param tableName Name of table.
	 * @param columnNames Name of columns.
	 * @param isUnique Whether index is unique or not.
	 * @return Generated Query.
	 */
	public String formCreateIndexQuery(final String indexName, final String tableName, final Iterator<String> columnNames, final boolean isUnique);
	
	/**
	 * Build query to drop table from database.
	 * @param tableName Name of table.
	 * @return Generated Query.
	 */
	public String formDropTableQuery(final String tableName);
	
	/**
	 * Build query to drop index from table.
	 * @param tableName Name of table.
	 * @param indexName Name of index.
	 * @return Generated Query.
	 */
	public String formDropIndexQuery(String tableName, String indexName);
	
	/**
	 * Build query to fetch data from table.
	 * @param tableName Name of table.
	 * @param distinct Distinct TRUE/FALSE.
	 * @param whereClause Where clause based on which data will be fetched from table.
	 * @param columnNames Name of columns.
	 * @param groupBys Group by value.
	 * @param having Having clause
	 * @param orderBy Order By value.
	 * @param limit Limit of data fetched from table.
	 * @return Generated Query.
	 */
	public String formSelectQuery(final String tableName, final boolean distinct, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBys, final String having, final Iterator<String> orderBy, final String whichOrderBy, final String limit);

	/**
	 * Build query to save data in table.
	 * @param tableName Name of table.
	 * @param columnNames Name of columns.
	 * @return Generated Query.
	 */
	public String formSaveBindQuery(final String tableName, final Iterator<String> columnNames);
	
	/**
	 * Build query to update data in table.
	 * @param tableName Name of table.
	 * @param columnNames Name of columns.
	 * @param whereClause Where clause based on which data will be updated.
	 * @return Generated Query.
	 */
	public String formUpdateBindQuery(final String tableName, final Iterator<String> columnNames, final String whereClause);
	
	/**
	 * Build query to delete data from table.
	 * @param tableName Name of table.
	 * @param whereClause Where clause based on which data will be deleted from table.
	 * @return Generated Query.
	 */
	public String formDeleteQuery(final String tableName, final String whereClause);
	
	/**
	 * Build query to get count of tuples.
	 * @param tableName Name of table.
	 * @param column Name of column.
	 * @param distinct Distinct TRUE/FALSE.
	 * @param whereClause Where clause based on which count will be decided.
	 * @param groupBys Group By Column Names.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formCountQuery(final String tableName, final String column, final boolean distinct, final String whereClause, final Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to get average of tuples.
	 * @param tableName Name of table.
	 * @param column Name of column of which average needs to be find.
	 * @param whereClause Where clause based on which average will be decided.
	 * @param groupBys Group By Column Names.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formAvgQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to get sum of tuples.
	 * @param tableName Name of table.
	 * @param column Name of column.
	 * @param groupBys Group By Column Names.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formSumQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to get total of tuples.
	 * @param tableName Name of table.
	 * @param column Name of column.
	 * @param groupBys Group By Column Names.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formTotalQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having);

	/**
	 * Build query to get maximum of tuples.
	 * @param tableName Name of table.
	 * @param columnName Name of column of which maximum value needs to be found.
	 * @param groupBy Group by clause.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formMaxQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to get minimum of tuples.
	 * @param tableName Name of table.
	 * @param columnName Name of column.
	 * @param groupBy Group by clause.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formMinQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to get group concat of tuples.
	 * @param tableName Name of table.
	 * @param columnName Name of column.
	 * @param delimiter Delimiter of group concat.
	 * @param whereClause Where clause for group concat.s
	 * @param groupBy Group by clause.
	 * @param having Having clause for group by.
	 * @return Generated Query.
	 */
	public String formGroupConcatQuery(final String tableName, final String column, final String delimiter, final String whereClause, Iterator<String> groupBys, final String having);
	
	/**
	 * Build query to generate trigger.
	 * @param databaseMappingDescriptor Database Descriptor Object.
	 * @return Generated Query.
	 */
	public Iterator<String> formTriggers(final DatabaseMappingDescriptor databaseMappingDescriptor);
	
	/**
	 * Build query to generate foreign keys of table.
	 * @param databaseMappingDescriptor Database Descriptor Object.
	 * @return Generated Query.
	 */
	public String formForeignKeys(final DatabaseMappingDescriptor databaseMappingDescriptor);
	
}
