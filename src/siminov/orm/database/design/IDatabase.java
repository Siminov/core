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

import java.util.Iterator;
import java.util.Map;

import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;


public interface IDatabase {

	public void createTable() throws DatabaseException;

	public void dropTable() throws DatabaseException;

	public void dropIndex(String indexName) throws DatabaseException;
	
	public ISelect select() throws DatabaseException;

	public Object[] select(String query) throws DatabaseException;

	public void save() throws DatabaseException;

	public void update() throws DatabaseException;

	public void saveOrUpdate() throws DatabaseException;

	public IDelete delete() throws DatabaseException;

	public ICount count() throws DatabaseException;

	public IAverage avg() throws DatabaseException;

	public ISum sum() throws DatabaseException;

	public ITotal total() throws DatabaseException;

	public IMin min() throws DatabaseException;

	public IMax max() throws DatabaseException;

	public IGroupConcat groupConcat() throws DatabaseException;

	public DatabaseDescriptor getDatabaseDescriptor() throws DatabaseException;

	public DatabaseMappingDescriptor getDatabaseMappingDescriptor() throws DatabaseException;

	public String getTableName() throws DatabaseException;

	public Iterator<String> getColumnNames() throws DatabaseException;

	public Map<String, Object> getColumnValues() throws DatabaseException;

	public Map<String, String> getColumnTypes() throws DatabaseException;

	public Iterator<String> getPrimaryKeys() throws DatabaseException;

	public Iterator<String> getMandatoryFields() throws DatabaseException;

	public Iterator<String> getUniqueFields() throws DatabaseException;

	public Iterator<String> getForeignKeys() throws DatabaseException;
}
