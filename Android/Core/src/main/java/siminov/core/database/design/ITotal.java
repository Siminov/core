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

package siminov.core.database.design;

import siminov.core.exception.DatabaseException;

/**
 * Exposes API's to return total of all non-NULL values in the group.
 * The non-standard total() function is provided as a convenient way to work around this design problem in the SQL language.
 * The result of total() is always a floating point value.
 */
public interface ITotal {

	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return ITotalClause Interface.
	 */
	public ITotalClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return ITotal Interface.
	 */
	public ITotal whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ITotalClause Interface.
	 */
	public ITotalClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ITotalClause Interface.
	 */
	public ITotalClause or(String column);

	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return ITotal Interface.
	 */
	public ITotal groupBy(String...columns);
	
	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return ITotalClause Interface.
	 */
	public ITotalClause having(String column);

	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return ITotal Interface.
	 */
	public ITotal havingClause(String havingClause);
	
	/**
	 * Used to provide name of column for which total will be calculated.
	 * @param column Name of column.
	 * @return ITotal Interface.
	 */
	public ITotal column(String column);
	
	/**
	 * Used to get total, this method should be called in last to calculate total.
	 * @return Return total.
	 * @throws DatabaseException Throws exception if any error occur while calculating total. 
	 */
	public<T> T execute() throws DatabaseException;
	
}
