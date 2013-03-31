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
 * Exposes methods to GET one to many relationship properties as per in POJO Class Annotation by application.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {

	/**
	 * Method name to get on update attribute.
	 */
	public String METHOD_GET_ON_UPDATE = Constants.ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_ON_UPDATE_ATTRIBUTE;

	/**
	 * Method name to get on delete attribute.
	 */
	public String METHOD_GET_ON_DELETE = Constants.ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_ON_DELETE_ATTRIBUTE;

	/**
	 * Method name to get properties.
	 */
	public String METHOD_GET_PROPERTIES = Constants.ANNOTATION_DATABASE_MAPPING_RELATIONSHIP_PROPERTIES_METHOD_GET_PROPERTIES;

	/**
	 * Get on update action.
	 * @return OnUpdate Action.
	 */
	public String onUpdate();
	
	/**
	 * Get on delete action.
	 * @return OnDelete Action.
	 */
	public String onDelete();

	/**
	 * Get relationship properties.
	 * @return Relationship Properties.
	 */
	public RelationshipProperty[] properties() default{};

}
