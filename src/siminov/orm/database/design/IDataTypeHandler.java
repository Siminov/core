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

/**
 * Exposes convert API which is responsible to provide column data type based on java variable data type.
 */
public interface IDataTypeHandler {

	/**
	 * Converts java variable data type to database column data type.
	 * @param dataType Java variable data type.
	 * @return column data type.
	 */
	public String convert(String dataType);
	
}
