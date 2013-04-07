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

import siminov.orm.database.design.IDataTypeHandler;

public class DataTypeHandler implements IDataTypeHandler {

	private static final String SQLITE_DATA_TYPE_INTEGER = "INTEGER";
	private static final String SQLITE_DATA_TYPE_TEXT = "TEXT";
	private static final String SQLITE_DATA_TYPE_REAL = "REAL";
	private static final String SQLITE_DATA_TYPE_NONE = "NONE";
	private static final String SQLITE_DATA_TYPE_NUMERIC = "NUMERIC";

	
	public String convert(String dataType) {
		
		if(dataType.equalsIgnoreCase(int.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else if(dataType.equalsIgnoreCase(Integer.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else if(dataType.equalsIgnoreCase(long.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else if(dataType.equalsIgnoreCase(Long.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else if(dataType.equalsIgnoreCase(float.class.getName())) {
			return SQLITE_DATA_TYPE_REAL;
		} else if(dataType.equalsIgnoreCase(Float.class.getName())) {
			return SQLITE_DATA_TYPE_REAL;
		} else if(dataType.equalsIgnoreCase(boolean.class.getName())) {
			return SQLITE_DATA_TYPE_NUMERIC;
		} else if(dataType.equalsIgnoreCase(Boolean.class.getName())) {
			return SQLITE_DATA_TYPE_NUMERIC;
		} else if(dataType.equalsIgnoreCase(char.class.getName())) {
			return SQLITE_DATA_TYPE_TEXT;
		} else if(dataType.equalsIgnoreCase(Character.class.getName())) {
			return SQLITE_DATA_TYPE_TEXT;
		} else if(dataType.equalsIgnoreCase(String.class.getName())) {
			return SQLITE_DATA_TYPE_TEXT;
		} else if(dataType.equalsIgnoreCase(byte.class.getName())) {
			return SQLITE_DATA_TYPE_NONE;
		} else if(dataType.equalsIgnoreCase(Byte.class.getName())) {
			return SQLITE_DATA_TYPE_NONE;
		} else if(dataType.equalsIgnoreCase(void.class.getName())) {
			return SQLITE_DATA_TYPE_NONE;
		} else if(dataType.equalsIgnoreCase(Void.class.getName())) {
			return SQLITE_DATA_TYPE_NONE;
		} else if(dataType.equalsIgnoreCase(short.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else if(dataType.equalsIgnoreCase(Short.class.getName())) {
			return SQLITE_DATA_TYPE_INTEGER;
		} else {
			return SQLITE_DATA_TYPE_NONE;
		}
		
	}
	
}
