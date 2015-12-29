/**
 * [SIMINOV FRAMEWORK]
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

package siminov.core;


/**
 * It exposes APIs to deal with Siminov Initialization.
 * Using this application developer can pass parameters needed by Siminov Framework to work functionally.
 */
public interface IInitializer {

	/**
	 * Add parameters needed by Siminov Framework.
	 * @param object Initialization Parameter
	 */
	public void addParameter(Object object);
	
	/**
	 * Start Siminov Framework.
	 */
	public void initialize();
}
