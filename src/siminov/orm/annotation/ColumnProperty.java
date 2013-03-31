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
 * Exposes methods to GET properties as per in POJO Class Annotation by application.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnProperty {

	/**
	 * Method name to get column name.
	 */
	public String METHOD_GET_NAME = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_METHOD_GET_NAME;

	/**
	 * Method name to get column value.
	 */
	public String METHOD_GET_VALUE = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_METHOD_GET_VALUE;

	/**
	 * Primary Key property name.
	 */
	public String PRIMARY_KEY = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_PRIMARY_KEY;
	
	/**
	 * Unique property name.
	 */
	public String UNIQUE = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_UNIQUE;

	/**
	 * Not Null property name.
	 */
	public String NOT_NULL = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_NOT_NULL;

	/**
	 * Default property name.
	 */
	public String DEFAULT = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_DEFAULT;

	/**
	 * Check property name.
	 */
	public String CHECK = Constants.ANNOTATION_DATABASE_MAPPING_COLUMN_PROPERTY_CHECK;
	
	
	/**
	 * Get property name.
	 * @return Property Name.
	 */
	public String name();
	
	/**
	 * Get property value.
	 * @return Property Value.
	 */
	public String value();
	
}
