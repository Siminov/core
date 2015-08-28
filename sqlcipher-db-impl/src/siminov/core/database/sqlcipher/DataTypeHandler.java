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

package siminov.core.database.sqlcipher;

import siminov.core.database.design.IDataTypeHandler;
import siminov.core.exception.DeploymentException;

public class DataTypeHandler implements IDataTypeHandler {
	
	public String convert(String dataType) {
		
		/*
		 * Data Type Conversation
		 */
		if(dataType.equalsIgnoreCase(INTEGER_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(PRIMITIVE_INTEGER_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(LONG_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(PRIMITIVE_LONG_DATA_TYPE)) {
			return SQLITE_INTEGER_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(FLOAT_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(PRIMITIVE_FLOAT_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(DOUBLE_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(PRIMITIVE_DOUBLE_DATA_TYPE)) {
			return SQLITE_REAL_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(BOOLEAN_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(PRIMITIVE_BOOLEAN_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		} else if(dataType.equalsIgnoreCase(STRING_DATA_TYPE)) {
			return SQLITE_TEXT_DATA_TYPE;
		}

		
		/*
		 * Other Data Type
		 */
		else {
			throw new DeploymentException(DataTypeHandler.class.getName(), "convert", dataType + " Data Type Not Supported On Android");
		}
		
	}
	
}
