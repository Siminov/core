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

package siminov.orm.database.design;

import siminov.orm.exception.DatabaseException;

/**
 * Exposes API's to get tuples from table based on information provided.
 */
public interface ISelect {

	/**
	 * Used to specify DISTINCT condition.
	 * @return ICount Interface.
	 */
	public ISelect distinct();
	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return ISelectClause Interface.
	 */
	public ISelectClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return ISelect Interface.
	 */
	public ISelect whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ISelectClause Interface.
	 */
	public ISelectClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ISelectClause Interface.
	 */
	public ISelectClause or(String column);
	
	/**
	 * Used to specify ORDER BY keyword to sort the result-set.
	 * @param columns Name of columns which need to be sorted.
	 * @return ISelect Interface.
	 */
	public ISelect orderBy(String...columns);
	
	/**
	 * Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return ISelect Interface.
	 */
	public ISelect ascendingOrderBy(String...columns);
	
	/**
	 * Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return ISelect Interface.
	 */
	public ISelect descendingOrderBy(String...columns);

	/**
	 * Used to specify the range of data need to fetch from table.
	 * @param limit LIMIT of data.
	 * @return ISelect Interface.
	 */
	public ISelect limit(int limit);
	
	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return ISelect Interface.
	 */
	public ISelect groupBy(String...columns);
	
	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return ISelectClause Interface.
	 */
	public ISelectClause having(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return ISelect Interface.
	 */
	public ISelect havingClause(String havingClause);
	
	/**
	 * Used to provide name of columns only for which data will be fetched.
	 * @param column Name of columns.
	 * @return ISelect Interface.
	 */
	public ISelect columns(String...columns);
	
	/**
	 * Used to get tuples, this method should be called in last to get tuples from table.
	 * @return Return array of model objects.
	 * @throws DatabaseException Throws exception if any error occur while getting tuples from table. 
	 */
	public<T> T execute() throws DatabaseException;
	
}
