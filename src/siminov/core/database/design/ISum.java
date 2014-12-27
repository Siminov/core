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

package siminov.core.database.design;

import siminov.core.exception.DatabaseException;

/**
 * Exposes API's to return sum of all non-NULL values in the group.
 * If there are no non-NULL input rows then sum() returns NULL but total() returns 0.0.
 * NULL is not normally a helpful result for the sum of no rows but the SQL standard requires it and most other SQL database engines implement sum() that way so SQLite does it in the same way in order to be compatible.
 * The result of sum() is an integer value if all non-NULL inputs are integers. 
 */
public interface ISum {

	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return ISumClause Interface.
	 */
	public ISumClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return ISum Interface.
	 */
	public ISum whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ISumClause Interface.
	 */
	public ISumClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ISumClause Interface.
	 */
	public ISumClause or(String column);

	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return ISum Interface.
	 */
	public ISum groupBy(String...columns);
	
	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return ISumClause Interface.
	 */
	public ISumClause having(String column);

	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return ISum Interface.
	 */
	public ISum havingClause(String havingClause);
	
	/**
	 * Used to provide name of column for which sum will be calculated.
	 * @param column Name of column.
	 * @return ISum Interface.
	 */
	public ISum column(String column);
	
	/**
	 * Used to get sum, this method should be called in last to calculate sum.
	 * @return Return sum.
	 * @throws DatabaseException Throws exception if any error occur while calculating sum. 
	 */
	public<T> T execute() throws DatabaseException;

}
