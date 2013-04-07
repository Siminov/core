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
import java.util.Map;

import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;



/**
 * Exposes methods to deal with actual database object. 
 * It has methods to open, create, close, and execute query's.
 */
public interface IDatabase {

	/**
	 * Open/Create the database through Database Descriptor.
	 * <p>
	 * By default add CREATE_IF_NECESSARY flag so that if database does not exist it will create.

	 * @param databaseMappings Database-Descriptor object which defines the schema of database.
	 * @throws DatabaseException If the database cannot be opened or create.
	 */
	public void openOrCreate(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;
	
	/**
	 * Close the existing opened database through Database Descriptor.
	 *
	 * @param databaseMappings Database-Descriptor object which defines the schema of database.
	 * @throws DatabaseException If the database cannot be closed.
	 */
	public void close(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;

	/**
	 	Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data. 
		<p>
		It has no means to return any data (such as the number of affected rows). Instead, you're encouraged to use insert, update, delete, when possible. 
	 	
	 	@param databaseMappings Database-Descriptor object which defines the schema of database.
	 	@param databaseMappingDescriptor Database-Mapping-Descriptor object which defines the structure of table.
		@param query Query which needs to be executed.
	 	@throws DatabaseException If any error occur while executing query provided.
	 */
	public void executeQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException;

	/**
	 	A pre-compiled statement that can be reused. The statement cannot return multiple rows, but 1x1 result sets are allowed.
	 	
	 	@param databaseMappings Database-Descriptor object which defines the schema of database.
	 	@param databaseMappingDescriptor Database-Mapping-Descriptor object which defines the structure of table.
	 	@param query A pre-compiled statement.
	 	@param columnValues Column values
	 	@throws DatabaseException If any error occur while inserting or updating tuple.
	 */
	public void executeBindQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query, final Iterator<Object> columnValues) throws DatabaseException;
	
	/**
	 	Query the given table, returning a Cursor over the result set.
		
	 	@param databaseMappings Database-Descriptor object which defines the schema of database.
	 	@param databaseMappingDescriptor Database-Mapping-Descriptor object which defines the structure of table.
		@param query Query based on which tuples will be fetched from database.
	 	@return A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.
	 	@throws DatabaseException If any error occur while getting tuples from a single table.
	 */
	public Iterator<Map<String, Object>> executeFetchQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException;

	/**
	 * Executes the method on database object.
	 * @param methodName Name Of Database Method.
	 * @param parameters Parameters Needed By Database Method.
	 * @throws DatabaseException If any exeception occur which invoking method in database object.
	 */
	public void executeMethod(final String methodName, final Object parameters) throws DatabaseException;
	
}
