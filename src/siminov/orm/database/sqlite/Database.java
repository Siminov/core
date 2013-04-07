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

package siminov.orm.database.sqlite;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import siminov.orm.database.DatabaseUtils;
import siminov.orm.database.design.IDatabase;
import siminov.orm.exception.DatabaseException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;


/**
 * Provides IDatabase implementation for SQLite database.
 */
public class Database implements IDatabase {

	private SQLiteDatabase sqliteDatabase = null;
	
	public void openOrCreate(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {

		String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);

		File file = new File(databasePath);
		if(!file.exists()) {
			try {
				file.mkdirs();
			} catch(Exception exception) {
				Log.loge(Database.class.getName(), "openOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
				throw new DeploymentException(Database.class.getName(), "openOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
			}
		}
		
		String databaseName = databaseDescriptor.getDatabaseName();
		if(databaseName == null || databaseName.length() <= 0) {
			Log.loge(getClass().getName(), "doValidation", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
			throw new DatabaseException(getClass().getName(), "doValidation", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName());
		}
		
		if(!databaseName.endsWith(".db")) {
			databaseName = databaseName + ".db";
		}
		
		try {
			sqliteDatabase = SQLiteDatabase.openDatabase(databasePath + databaseName, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch(SQLiteException sqliteException) {
			Log.loge(Database.class.getName(), "openOrCreate", "SQLiteException caught while opening database, " + sqliteException.getMessage());
			throw new DatabaseException(Database.class.getName(), "openOrCreate", "SQLiteException caught while opening database, " + sqliteException.getMessage());
		}
	}
	
	public void close(final DatabaseDescriptor databaseDescriptor) throws DatabaseException {
		sqliteDatabase.close();
	}

	public void executeQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException {
		Log.logd(Database.class.getName(), "executeQuery(" + query + ")", "QUERY: " + query);
		
		try {
			sqliteDatabase.execSQL(query);			
		} catch(SQLiteException sqliteException) {
			Log.loge(Database.class.getName(), "executeQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.getMessage());
			throw new DatabaseException(Database.class.getName(), "executeQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.getMessage());
		}
	}
	
	public void executeBindQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query, final Iterator<Object> columnValues) throws DatabaseException {
		Log.logd(Database.class.getName(), "executeBindQuery", "QUERY: " + query);
		
		SQLiteStatement statement =null;
		
		try {
			statement = sqliteDatabase.compileStatement(query);
		} catch(SQLiteException sqliteException) {
			Log.loge(Database.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while compiling statement, " + sqliteException.getMessage());

			int index = 0;
			while(columnValues.hasNext()) {
				Object columnValue = columnValues.next();
				
				if(columnValue instanceof String) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX: " + index + ", VALUE: " + columnValue);
				} else if(columnValue instanceof Long) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Long) columnValue).longValue());
				} else if(columnValue instanceof Double) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Double) columnValue).doubleValue());
				} else if(columnValue instanceof Float) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + ((Float) columnValue).floatValue());
				} else if(columnValue instanceof byte[]) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + new String((byte[]) columnValue));
				} else {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + columnValue);
				}
				
				index++;
			}

			throw new DatabaseException(Database.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while compiling statement, " + sqliteException.getMessage());
		}
		
		final Collection<Object> duplicateColumnValues = new LinkedList<Object>();
		
		int index = 0;
		while(columnValues.hasNext()) {
			Object columnValue = columnValues.next();
			
			if(columnValue instanceof String) {
				statement.bindString(index + 1, (String) columnValue);
			} else if(columnValue instanceof Integer) {
				statement.bindLong(index + 1, ((Integer) columnValue));
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

		try {
			statement.execute();
		} catch(SQLiteException sqliteException) {
			Log.loge(Database.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while executing statement, " + sqliteException.getMessage());
			
			index = 0;

			Iterator<Object> newColumnValues = duplicateColumnValues.iterator();
			while(newColumnValues.hasNext()) {
				Object columnValue = newColumnValues.next();
				
				if(columnValue instanceof String) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + columnValue);
				} else if(columnValue instanceof Integer) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Integer) columnValue).longValue());
				} else if(columnValue instanceof Long) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Long) columnValue).longValue());
				} else if(columnValue instanceof Double) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Double) columnValue).doubleValue());
				} else if(columnValue instanceof Float) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + ((Float) columnValue).floatValue());
				} else if(columnValue instanceof byte[]) {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-NAME: " + index + ", VALUE: " + new String((byte[]) columnValue));
				} else {
					Log.loge(Database.class.getName(), "executeBindQuery", "COLUMN-INDEX " + index + ", VALUE: " + columnValue);
				}
				
				index++;
			}
			
			statement.clearBindings();
			statement.close();

			throw new DatabaseException(Database.class.getName(), "executeBindQuery(" + query + ")", "SQLiteException caught while executing statement, " + sqliteException.getMessage());
		}

		statement.clearBindings();
		statement.close();
	}
	
	public Iterator<Map<String, Object>> executeFetchQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException {
			SQLiteCursor sqliteCursor = (SQLiteCursor) sqliteDatabase.rawQuery(query, null);
			Collection<Map<String, Object>> tuples = new ArrayList<Map<String,Object>>();
			
			while(sqliteCursor.moveToNext()) {
				HashMap<String, Object> tuple = new HashMap<String, Object>();
				
				String[] columnNames = sqliteCursor.getColumnNames();
				if(columnNames == null || columnNames.length <= 0) {
					continue;
				}
				
				for(int i = 0;i < columnNames.length;i++) {
					boolean isString = sqliteCursor.isString(i);
					boolean isLong = sqliteCursor.isLong(i);
					boolean isFloat = sqliteCursor.isFloat(i);
					boolean isBlob = sqliteCursor.isBlob(i);
					
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
				Log.loge(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		} else {
			if(parameter == null) {
				try {
					method = sqliteDatabase.getClass().getMethod(methodName, new Class[] {});	
				} catch(Exception exception) {
					Log.loge(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
					throw new DatabaseException(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				}
			} else {
				try {
					method = sqliteDatabase.getClass().getMethod(methodName, parameter.getClass());	
				} catch(Exception exception) {
					Log.loge(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
					throw new DatabaseException(Database.class.getName(), "invokeMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				}
			}
			
		}
		
		
		method.setAccessible(true);
		
		if(parameter == null) {
			try {
				 method.invoke(sqliteDatabase, new Object[] {});	
			} catch(Exception exception) {
				Log.loge(Database.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(Database.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		} else {
			try {
				 method.invoke(sqliteDatabase, new Object[] {parameter});	
			} catch(Exception exception) {
				Log.loge(Database.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
				throw new DatabaseException(Database.class.getName(), "invokeMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.getMessage());
			}
		}
	}

}
