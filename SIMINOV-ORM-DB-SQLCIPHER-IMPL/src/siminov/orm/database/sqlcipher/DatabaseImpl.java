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

package siminov.orm.database.sqlcipher;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.sqlcipher.CrossProcessCursorWrapper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteStatement;
import siminov.orm.database.design.IDatabaseImpl;
import siminov.orm.exception.DatabaseException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor.Column;
import siminov.orm.resource.Resources;

public class DatabaseImpl implements IDatabaseImpl {

	private SQLiteDatabase sqliteDatabase = null;
	
	private String DATABASE_ATTRIBUTE_PASSWORD = "password";
	
	public void openOrCreate(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		
		String databasePath = new siminov.orm.database.DatabaseUtils().getDatabasePath(databaseDescriptor);

		File file = new File(databasePath);
		if(!file.exists()) {
			try {
				file.mkdirs();
			} catch(Exception exception) {
				Log.loge(DatabaseImpl.class.getName(), "openOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
				throw new DeploymentException(DatabaseImpl.class.getName(), "openOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
			}
		}

		SQLiteDatabase.loadLibs(Resources.getInstance().getApplicationContext());
		
		String databaseName = databaseDescriptor.getDatabaseName();
		if(databaseName == null || databaseName.length() <= 0) {
			Log.loge(getClass().getName(), "doValidation", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DatabaseException(getClass().getName(), "doValidation", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		if(!databaseName.endsWith(".db")) {
			databaseName = databaseName + ".db";
		}

		String password = databaseDescriptor.getProperty(DATABASE_ATTRIBUTE_PASSWORD);
		
		try {
			sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(databasePath + databaseName, password, null);
		} catch(SQLiteException sqliteException) {
			Log.loge(DatabaseImpl.class.getName(), "openOrCreate", "SQLiteException caught while opening database, " + sqliteException.getMessage());
			throw new DatabaseException(DatabaseImpl.class.getName(), "openOrCreate", "SQLiteException caught while opening database, " + sqliteException.getMessage());
		}
	}
	
	public void close(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		sqliteDatabase.close();
	}

	public void executeQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException {
		Log.logd(DatabaseImpl.class.getName(), "executeQuery(" + query + ")", "QUERY: " + query);
		
		try {
			sqliteDatabase.execSQL(query);			
		} catch(SQLiteException sqliteException) {
			Log.loge(DatabaseImpl.class.getName(), "executeQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.getMessage());
			throw new DatabaseException(DatabaseImpl.class.getName(), "executeQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.getMessage());
		}
	}
	
	public void executeBindQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query, final Iterator<Object> columnValues) throws DatabaseException {
		Log.logd(DatabaseImpl.class.getName(), "executeBindQuery", "QUERY: " + query);
		
		SQLiteStatement statement =null;
		
		try {
			statement = sqliteDatabase.compileStatement(query);
		} catch(SQLiteException sqliteException) {
			Log.loge(DatabaseImpl.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while compiling statement, " + sqliteException.getMessage());

			int index = 0;
			while(columnValues.hasNext()) {
				Object columnValue = columnValues.next();
				
				if(columnValue instanceof String) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX: " + index + ", VALUE: " + columnValue);
				} else if(columnValue instanceof Long) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Long) columnValue).longValue());
				} else if(columnValue instanceof Double) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Double) columnValue).doubleValue());
				} else if(columnValue instanceof Float) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Float) columnValue).floatValue());
				} else if(columnValue instanceof byte[]) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + new String((byte[]) columnValue));
				} else {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + columnValue);
				}
				
				index++;
			}

			throw new DatabaseException(DatabaseImpl.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while compiling statement, " + sqliteException.getMessage());
		}
		
		final Collection<Object> duplicateColumnValues = new LinkedList<Object>();
		
		int index = 0;
		while(columnValues.hasNext()) {
			Object columnValue = columnValues.next();
			
			if(columnValue instanceof String) {
				statement.bindString(index + 1, (String) columnValue);
			} else if(columnValue instanceof Long) {
				statement.bindLong(index + 1, (Long) columnValue);
			} else if(columnValue instanceof Double) {
				statement.bindDouble(index + 1, (Double) columnValue);
			} else if(columnValue instanceof Float) {
				statement.bindDouble(index + 1, (Float) columnValue);
			} else if(columnValue instanceof byte[]) {
				statement.bindBlob(index + 1, (byte[]) columnValue);
			} else {
				statement.bindNull(index + 1);
			}
			
			duplicateColumnValues.add(columnValue);
			index++;
		} 

		Iterator<Object> newColumnValues = duplicateColumnValues.iterator();
		
		try {
			statement.execute();
		} catch(SQLiteException sqliteException) {
			Log.loge(DatabaseImpl.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while executing statement, " + sqliteException.getMessage());
			
			index = 0;
			while(newColumnValues.hasNext()) {
				Object columnValue = newColumnValues.next();
				
				if(columnValue instanceof String) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + columnValue);
				} else if(columnValue instanceof Long) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Long) columnValue).longValue());
				} else if(columnValue instanceof Double) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Double) columnValue).doubleValue());
				} else if(columnValue instanceof Float) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Float) columnValue).floatValue());
				} else if(columnValue instanceof byte[]) {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + new String((byte[]) columnValue));
				} else {
					Log.loge(DatabaseImpl.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + columnValue);
				}
				
				index++;
			}
			
			throw new DatabaseException(DatabaseImpl.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while executing statement, " + sqliteException.getMessage());
		}

		statement.clearBindings();
		statement.close();
	}
	
	public Iterator<Map<String, Object>> executeFetchQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException {
			CrossProcessCursorWrapper sqliteCursor = (CrossProcessCursorWrapper) sqliteDatabase.rawQuery(query, null);
			Collection<Map<String, Object>> tuples = new ArrayList<Map<String,Object>>();
			
			while(sqliteCursor.moveToNext()) {
				HashMap<String, Object> tuple = new HashMap<String, Object>();
				
				String[] columnNames = sqliteCursor.getColumnNames();
				if(columnNames == null || columnNames.length <= 0) {
					continue;
				}
				
				for(int i = 0;i < columnNames.length;i++) {
					String columnName = sqliteCursor.getColumnName(i);
					Column column = databaseMappingDescriptor.getColumnBasedOnColumnName(columnName);
					
					if(column == null) {
						Log.loge(DatabaseImpl.class.getName(), "executeFetchQuery", "No Column Object Found For Column Name: " + columnName);
						throw new DatabaseException(DatabaseImpl.class.getName(), "executeFetchQuery", "No Column Object Found For Column Name: " + columnName);
					}
					
					String columnType = column.getType();
					if(columnType == null || columnType.length() <= 0) {
						Log.loge(DatabaseImpl.class.getName(), "executeFetchQuery", "No Column Type Found For Column Name: " + columnName);
						throw new DatabaseException(DatabaseImpl.class.getName(), "executeFetchQuery", "No Column Type Found For Column Name: " + columnName);
					}
					
					boolean isString = false;
					boolean isLong = false;
					boolean isFloat = false;
					boolean isBlob = false;
					
					DataTypeHandler dataTypeHandler = new DataTypeHandler();
					
					String SQLITE_DATA_TYPE_INTEGER = "INTEGER";
					String SQLITE_DATA_TYPE_TEXT = "TEXT";
					String SQLITE_DATA_TYPE_REAL = "REAL";
					String SQLITE_DATA_TYPE_NONE = "NONE";
					String SQLITE_DATA_TYPE_NUMERIC = "NUMERIC";

					
					String sqliteDataType = dataTypeHandler.convert(columnType);
					
					if(sqliteDataType.equalsIgnoreCase(SQLITE_DATA_TYPE_INTEGER)) {
						isLong = true;
					} else if(sqliteDataType.equalsIgnoreCase(SQLITE_DATA_TYPE_TEXT)) {
						isString = true;
					} else if(sqliteDataType.equalsIgnoreCase(SQLITE_DATA_TYPE_REAL)) {
						isFloat = true;
					} else if(sqliteDataType.equalsIgnoreCase(SQLITE_DATA_TYPE_NUMERIC)) {
						
					} else if(sqliteDataType.equalsIgnoreCase(SQLITE_DATA_TYPE_NONE)) {
						isBlob = true;
					}
					
					if(isString) {
						tuple.put(columnNames[i], sqliteCursor.getString(i));
					} else if(isLong) {
						tuple.put(columnNames[i], sqliteCursor.getLong(i));
					} else if(isFloat) {
						tuple.put(columnNames[i], sqliteCursor.getFloat(i));
					} else if(isBlob) {
						tuple.put(columnNames[i], sqliteCursor.getBlob(i));
					}
				}
				
				tuples.add(tuple);
			}
			
			sqliteCursor.close();
		return tuples.iterator();
	}
	
	public void executeMethod(final String methodName, final Object parameter) throws DatabaseException {
		Method method = null;
		
		if(parameter instanceof Boolean) {
			try {
				method = sqliteDatabase.getClass().getMethod(methodName, boolean.class);	
			} catch(Exception exception) {
				Log.loge(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		} else {
			if(parameter == null) {
				try {
					method = sqliteDatabase.getClass().getMethod(methodName, new Class[] {});	
				} catch(Exception exception) {
					Log.loge(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
					throw new DatabaseException(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				}
			} else {
				try {
					method = sqliteDatabase.getClass().getMethod(methodName, parameter.getClass());	
				} catch(Exception exception) {
					Log.loge(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
					throw new DatabaseException(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				}
			}
			
		}
		
		
		method.setAccessible(true);
		
		if(parameter == null) {
			try {
				 method.invoke(sqliteDatabase, new Object[] {});	
			} catch(Exception exception) {
				Log.loge(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		} else {
			try {
				 method.invoke(sqliteDatabase, new Object[] {parameter});	
			} catch(Exception exception) {
				Log.loge(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(DatabaseImpl.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		}
	}

}
