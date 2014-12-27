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

package siminov.core.database.design;

/**
 * Exposes convert API which is responsible to provide column data type based on java variable data type.
 */
public interface IDataTypeHandler {

	/*
	 * SQLite Data Types
	 */
	
	/**
	 * SQLite Integer Data Type
	 */
	public String SQLITE_INTEGER_DATA_TYPE = "INTEGER";
	
	/**
	 * SQLite Text Data Type
	 */
	public String SQLITE_TEXT_DATA_TYPE = "TEXT";
	
	/**
	 * SQLite Real Data Type
	 */
	public String SQLITE_REAL_DATA_TYPE = "REAL";
	
	/**
	 * SQLite None Data Type
	 */
	public String SQLITE_NONE_DATA_TYPE = "NONE";
	
	/**
	 * SQLite Numeric Data Type
	 */
	public String SQLITE_NUMERIC_DATA_TYPE = "NUMERIC";
	
	
	/*
	 * Java Data Types
	 */
	
	/**
	 * Java Int Primitive Data Type
	 */
	public String JAVA_INT_PRIMITIVE_DATA_TYPE = int.class.getName();
	
	/**
	 * Java Integer Data Type
	 */
	public String JAVA_INTEGER_DATA_TYPE = Integer.class.getName();
	
	/**
	 * Java Long Primitive Data Type
	 */
	public String JAVA_LONG_PRIMITIVE_DATA_TYPE = long.class.getName();
	
	/**
	 * Java Long Data Type
	 */
	public String JAVA_LONG_DATA_TYPE = Long.class.getName();
	
	/**
	 * Java Float Primitive Data Type
	 */
	public String JAVA_FLOAT_PRIMITIVE_DATA_TYPE = float.class.getName();
	
	/**
	 * Java Float Data Type
	 */
	public String JAVA_FLOAT_DATA_TYPE = Float.class.getName();
	
	/**
	 * Java Double Primitive Data Type
	 */
	public String JAVA_DOUBLE_PRIMITIVE_DATA_TYPE = double.class.getName();
	
	/**
	 * Java Double Data Type
	 */
	public String JAVA_DOUBLE_DATA_TYPE = Double.class.getName();
	
	/**
	 * Java Boolean Primitive Data Type
	 */
	public String JAVA_BOOLEAN_PRIMITIVE_DATA_TYPE = boolean.class.getName();
	
	/**
	 * Java Boolean Data Type
	 */
	public String JAVA_BOOLEAN_DATA_TYPE = Boolean.class.getName();
	
	/**
	 * Java Char Primitive Data Type
	 */
	public String JAVA_CHAR_PRIMITIVE_DATA_TYPE = char.class.getName();
	
	/**
	 * Java Character Data Type
	 */
	public String JAVA_CHARACTER_DATA_TYPE = Character.class.getName();
	
	/**
	 * Java String Data Type
	 */
	public String JAVA_STRING_DATA_TYPE = String.class.getName();
	
	/**
	 * Java Byte Primitive Data Type
	 */
	public String JAVA_BYTE_PRIMITITVE_DATA_TYPE = byte.class.getName();
	
	/**
	 * Java Byte Data Type
	 */
	public String JAVA_BYTE_DATA_TYPE = Byte.class.getName();
	
	/**
	 * Java Void Primitive Data Type
	 */
	public String JAVA_VOID_PRIMITITVE_DATA_TYPE = void.class.getName();
	
	/**
	 * Java Void Data Type
	 */
	public String JAVA_VOID_DATA_TYPE = Void.class.getName();
	
	/**
	 * Java Short Primitive Data Type
	 */
	public String JAVA_SHORT_PRIMITITVE_DATA_TYPE = short.class.getName();
	
	/**
	 * Java Short Data Type
	 */
	public String JAVA_SHORT_DATA_TYPE = Short.class.getName();
	
	
	/*
	 * JavaScript Data Types 
	 */
	
	/**
	 * JavaScript String Data Type
	 */
	public String JAVASCRIPT_STRING_DATA_TYPE = "String";
	
	/**
	 * JavaScript Number Data Type
	 */
	public String JAVASCRIPT_NUMBER_DATA_TYPE = "Number";
	
	/**
	 * JavaScript Boolean Data Type
	 */
	public String JAVASCRIPT_BOOLEAN_DATA_TYPE = "Boolean";

	
	
	/**
	 * Converts java variable data type to database column data type.
	 * @param dataType Java variable data type.
	 * @return column data type.
	 */
	public String convert(String dataType);
	
}
