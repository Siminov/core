/**
 * [SIMINOV FRAMEWORK - CORE]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
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


package siminov.core.model;

import java.util.Iterator;


/**
 * It exposes common API for all the descriptor
 * It has method to get and set properties of descriptor
 */
public interface IDescriptor {

	/**
	 * Get all the properties of descriptor
	 * @return All properties
	 */
	public Iterator<String> getProperties();
	
	/**
	 * Get the property value based on the property name
	 * @param name Name of the property
	 * @return Value of the property
	 */
	public String getProperty(String name);

	
	/**
	 * Check whether property exists or not 
	 * @param name Name of the property
	 * @return (true/false) TRUE: If property exists | FALSE: If property does not exists.
	 */
	public boolean containProperty(String name);
	
	/**
	 * Add property to the descriptor
	 * @param name Name of the property
	 * @param value Value of the property
	 */
	public void addProperty(String name, String value);	
	
	/**
	 * Remove property from the descriptor
	 * @param name Name of the property
	 */
	public void removeProperty(String name);
	
}
