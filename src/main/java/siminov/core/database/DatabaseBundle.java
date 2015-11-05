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

package siminov.core.database;

import siminov.core.database.design.IDataTypeHandler;
import siminov.core.database.design.IDatabaseImpl;
import siminov.core.database.design.IQueryBuilder;

/**
 * It is a collection of below database items:
 * 	1. Database Instance
 * 	2. Query Builder Instance
 *  3. Data Type Handler Instance
 */
public class DatabaseBundle {

	private IDatabaseImpl database = null;
	private IQueryBuilder queryBuilder = null;
	private IDataTypeHandler dataTypeHandler = null;
	
	/**
	 * It returns the database instance
	 * @return IDatabaseImpl instance object.
	 */
	public IDatabaseImpl getDatabase() {
		return this.database;
	}
	
	/**
	 * It sets the database instance
	 * @param database IDatabaseImpl instance object.
	 */
	public void setDatabase(IDatabaseImpl database) {
		this.database = database;
	}
	
	/**
	 * It returns the query builder instance
	 * @return IQueryBuilder Query builder instance object.
	 */
	public IQueryBuilder getQueryBuilder() {
		return this.queryBuilder;
	}
	
	/**
	 * It sets the query builder instance
	 * @param queryBuilder IQueryBuilder instance object.
	 */
	public void setQueryBuilder(IQueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	
	
	/**
	 * It returns the data type handler instance.
	 * @return IDataTypeHandler Data Type Handler instance object.
	 */
	public IDataTypeHandler getDataTypeHandler() {
		return this.dataTypeHandler;
	}
	
	/**
	 * It sets the data type handler instance.
	 * @param dataTypeHandler Data Type Handler instance object.
	 */
	public void setDataTypeHandler(IDataTypeHandler dataTypeHandler) {
		this.dataTypeHandler = dataTypeHandler;
	}
}
