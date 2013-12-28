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

package siminov.orm.database.design;

import siminov.orm.exception.DatabaseException;

/**
 * Exposes API's to get count of the number of times that X is not NULL in a group.
 * The count(*) function (with no arguments) returns the total number of rows in the group.
 */
public interface ICount {

	public String INTERFACE_NAME = ICount.class.getName();

	/**
	 * Used to specify DISTINCT condition.
	 * @return ICount Interface.
	 */
	public ICount distinct();
	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return ICountClause Interface.
	 */
	public ICountClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return ICount Interface.
	 */
	public ICount whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ICountClause Interface.
	 */
	public ICountClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return ICountClause Interface.
	 */
	public ICountClause or(String column);

	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return ICount Interface.
	 */
	public ICount groupBy(String...columns);
	
	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return ICountClause Interface.
	 */
	public ICountClause having(String column);

	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return ICount Interface.
	 */
	public ICount havingClause(String havingClause);
	
	/**
	 * Used to provide name of column for which count will be calculated.
	 * @param column Name of column.
	 * @return ICount Interface.
	 */
	public ICount column(String column);
	
	/**
	 * Used to get count, this method should be called in last to calculate count.
	 * @return Return count.
	 * @throws DatabaseException Throws exception if any error occur while calculating count. 
	 */
	public Object execute() throws DatabaseException;
	
}
