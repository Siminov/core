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

package siminov.orm.database;

import java.util.Hashtable;

import siminov.orm.database.design.IDataTypeHandler;
import siminov.orm.database.design.IDatabase;
import siminov.orm.database.design.IQueryBuilder;
import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.utils.ClassUtils;


/**
 * Exposes methods to create database instance based on Database Type needed. 
 * It has methods to GET DatabaseFactory, GET Database.
 */
public class DatabaseFactory {

	private static DatabaseFactory databaseFactory = null;
	private Hashtable<String, DatabaseBundle> databaseBundles = new Hashtable<String, DatabaseBundle> ();

	private String DATABASE_PACKAGE_NAME = "siminov.orm.database";
	private String DATABASE_CLASS_NAME = "Database";
	private String DATABASE_QUERY_BUILDER = "QueryBuilder";
	private String DATABASE_DATA_TYPE_HANDLER = "DataTypeHandler";
	
	
	private DatabaseFactory() {
		
	}
	
	/**
	 * Get DatabaseFactory Instance.
	 * @return DatabaseFactory Instance.
	 */
	public static DatabaseFactory getInstance() {
		if(databaseFactory == null) {
			databaseFactory = new DatabaseFactory();
		}	
		
		return databaseFactory;
	}
	
	/**
	 * Get IDatabase Instance.
	 * @param databaseDescriptor DatabaseDescriptor Object.
	 * @return IDatabase Object.
	 * @throws DatabaseException If not able to create IDatabase instance.
	 */
	public DatabaseBundle getDatabaseBundle(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		if(databaseBundles.contains(databaseDescriptor.getDatabaseName())) {
			return databaseBundles.get(databaseDescriptor.getDatabaseName());
		}

		String type = databaseDescriptor.getType();
		String packageName = DATABASE_PACKAGE_NAME + "." + type;

		DatabaseBundle databaseBundle = getDatabaseBundle(packageName);
		databaseBundles.put(databaseDescriptor.getDatabaseName(), databaseBundle);
		
		return databaseBundle;
	}

	private DatabaseBundle getDatabaseBundle(String packageName) {
		
		IDatabase database = (IDatabase) ClassUtils.createClassInstance(packageName + "." + DATABASE_CLASS_NAME);
		IQueryBuilder queryBuilder = (IQueryBuilder) ClassUtils.createClassInstance(packageName + "." + DATABASE_QUERY_BUILDER);
		IDataTypeHandler dataTypeHandler = (IDataTypeHandler) ClassUtils.createClassInstance(packageName + "." + DATABASE_DATA_TYPE_HANDLER);
		
		DatabaseBundle databaseBundle = new DatabaseBundle();
		databaseBundle.setDatabase(database);
		databaseBundle.setQueryBuilder(queryBuilder);
		databaseBundle.setDataTypeHandler(dataTypeHandler);
		
		return databaseBundle;
		
	}
	
}
