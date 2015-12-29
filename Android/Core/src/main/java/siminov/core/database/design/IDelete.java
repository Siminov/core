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
 * Exposes API's to delete tuples from table.
 */
public interface IDelete {

	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return IDelete Interface.
	 */
	public IDelete whereClause(String whereClause); 

	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause or(String column);

	
	/**
	 * Used to delete, this method should be called in last to delete tuples from table.
	 * @throws DatabaseException Throws exception if any error occur while deleting tuples from table. 
	 */
	public<T> T execute() throws DatabaseException;

}
