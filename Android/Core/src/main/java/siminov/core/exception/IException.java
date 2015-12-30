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


package siminov.core.exception;


/**
 * Exposes method to deal with exception
 * It has methods to set exception class name, exception method name, exception message.
 */
public interface IException {

	
	/**
	 * Get exception class name
	 * @return Exception Class Name
	 */
	public String getClassName();

	/**
	 * Set exception class name	
	 * @param className Name of exception class
	 */
	public void setClassName(final String className);
	
	
	/**
	 * Get exception method name
	 * @return Name of exception method
	 */
	public String getMethodName();
	
	
	/**
	 * Set exception method name
	 * @param methodName Name of method
	 */
	public void setMethodName(final String methodName);

	
	/**
	 * Get exception message
	 * @return Exception message
	 */
	public String getMessage();
	
	
	/**
	 * Set exception message
	 * @param message Exception message
	 */
	public void setMessage(final String message);
}
