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

package siminov.orm.database.sqlite;

import siminov.orm.database.design.IDataTypeHandler;

public class DataTypeHandler implements IDataTypeHandler {

	
	public String convert(String dataType) {
	
		/*
		 * Java Data Type Conversation
		 */
		if(dataType.equalsIgnoreCase(JAVA_INT_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_INTEGER_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_LONG_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_LONG_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_FLOAT_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_FLOAT_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_DOUBLE_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_DOUBLE_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_BOOLEAN_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_BOOLEAN_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_CHAR_PRIMITIVE_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_CHARACTER_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_STRING_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_BYTE_PRIMITITVE_DATA_TYPE)) {
			return SQLITE_NONE_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_BYTE_DATA_TYPE)) {
			return SQLITE_NONE_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_VOID_PRIMITITVE_DATA_TYPE)) {
			return SQLITE_NONE_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_VOID_DATA_TYPE)) {
			return SQLITE_NONE_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_SHORT_PRIMITITVE_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVA_SHORT_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		}

		
		/*
		 * JavaScript Data Type Conversation
		 */
		else if(dataType.equalsIgnoreCase(JAVASCRIPT_STRING_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVASCRIPT_NUMBER_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(JAVASCRIPT_BOOLEAN_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		}
		
		
		/*
		 * Other Data Type
		 */
		else {
			return SQLITE_NONE_DATA_TYPE;
		}
		
	}
	
}
