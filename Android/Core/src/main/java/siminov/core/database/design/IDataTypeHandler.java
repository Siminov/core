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
	 * Data Types
	 */
	
	/**
	 * Integer Data Type
	 */
	public String INTEGER_DATA_TYPE = "integer";
	
	/**
	 * Primitive Integer Data Type
	 */
	public String PRIMITIVE_INTEGER_DATA_TYPE = "primitive-integer";
	
	/** 
	 * Long Data Type
	 */
	public String LONG_DATA_TYPE = "long";
	
	/** 
	 * Primitive Long Data Type
	 */
	public String PRIMITIVE_LONG_DATA_TYPE = "primitive-long";
	
	/**
	 * Float Data Type
	 */
	public String FLOAT_DATA_TYPE = "float";
	
	/**
	 * Primitive Float Data Type
	 */
	public String PRIMITIVE_FLOAT_DATA_TYPE = "primitive-float";
	
	/**
	 * Double Data Type
	 */
	public String DOUBLE_DATA_TYPE = "double";
	
	/**
	 * Primitive Double Data Type
	 */
	public String PRIMITIVE_DOUBLE_DATA_TYPE = "primitive-double";
	
	/**
	 * Boolean Data Type
	 */
	public String BOOLEAN_DATA_TYPE = "boolean";
	
	/**
	 * Primitive Boolean Data Type
	 */
	public String PRIMITIVE_BOOLEAN_DATA_TYPE = "primitive-boolean";
	
	/**
	 * String Data Type
	 */
	public String STRING_DATA_TYPE = "string";
	
	
	/**
	 * Byte Data Type
	 */
	public String BYTE_DATA_TYPE = "byte";
	
	/**
	 * Primitive Byte Data Type
	 */
	public String PRIMITIVE_BYTE_DATA_TYPE = "primitive-byte";

	
	/**
	 * Converts java variable data type to database column data type.
	 * @param dataType Java variable data type.
	 * @return column data type.
	 */
	public String convert(String dataType);
	
}
