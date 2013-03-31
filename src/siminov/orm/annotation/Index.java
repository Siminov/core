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

package siminov.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import siminov.orm.Constants;


/**
 * Exposes methods to GET index properties as per in POJO Class Annotation by application.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

	/**
	 * Method name to get name.
	 */
	public String METHOD_GET_NAME = Constants.ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_NAME;

	/**
	 * Method name to get value.
	 */
	public String METHOD_GTE_VALUE = Constants.ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_VALUE;

	/**
	 * Method name get whether index is unique or not.
	 */
	public String METHOD_GET_IS_UNIQUE = Constants.ANNOTATION_DATABASE_MAPPING_INDEX_METHOD_GET_IS_UNIQUE;

	/**
	 * Get index name.
	 * @return Name Of Index.
	 */
	public String name();
	
	/**
	 * Get index columns.
	 * @return Index Columns.
	 */
	public IndexColumn[] value();
	
	/**
	 * Check if index is unique.
	 * @return TRUE: If index is unique, FALSE: If index is not unique.
	 */
	public boolean unique();
	
}
