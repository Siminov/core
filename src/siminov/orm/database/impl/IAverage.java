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

package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

/**
 * Exposes API's to get average value of all non-NULL X within a group. 
 * String and BLOB values that do not look like numbers are interpreted as 0.
 * The result of avg() is always a floating point value as long as at there is at least one non-NULL input even if all inputs are integers.
 * The result of avg() is NULL if and only if there are no non-NULL inputs.
 */
public interface IAverage {

	public String INTERFACE_NAME = IAverage.class.getName();
	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return IAerageClause Interface.
	 */
	public IAverageClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return IAverage Interface.
	 */
	public IAverage whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IAerageClause Interface.
	 */
	public IAverageClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IAverageClause Interface.
	 */
	public IAverageClause or(String column);

	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return IAverage Interface.
	 */
	public IAverage groupBy(String...columns);

	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return IAverageClause Interface.
	 */
	public IAverageClause having(String column);

	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return IAverage Interface.
	 */
	public IAverage havingClause(String havingClause);
	
	/**
	 * Used to provide name of column for which average will be calculated.
	 * @param column Name of column.
	 * @return IAverage Interface.
	 */
	public IAverage column(String column);
	
	/**
	 * Used to get average, this method should be called in last to calculate average.
	 * @return Return average.
	 * @throws DatabaseException Throws exception if any error occur while calculating average. 
	 */
	public Object execute() throws DatabaseException;

}
