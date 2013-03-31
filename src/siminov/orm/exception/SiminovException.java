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

package siminov.orm.exception;

/**
 * This is general exception, which is thrown through core API, if any exception occur while performing any tasks.
 */
public class SiminovException extends Exception {

	private String className = null;
	private String methodName = null;
	private String message = null;
	
	public SiminovException(final String className, final String methodName, final String message) {
		this.className = className;
		this.methodName = methodName;
		this.message = message;
	}
	
	/**
	 * Get POJO class name.
	 * @return POJO Class Name.
	 */
	public String getClassName() {
		return this.className;
	}
	
	/**
	 * Set POJO class name.
	 * @param className POJO Class Name.
	 */
	public void setClassName(final String className) {
		this.className = className;
	}
	
	/**
	 * Get method Name.
	 * @return Name Of Method.
	 */
	public String getMethodName() {
		return this.methodName;
	}
	
	/**
	 * Set method Name.
	 * @param methodName Name Of Method.
	 */
	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Get message.
	 * @return Message.
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Set message.
	 * @param message Message.
	 */
	public void setMessage(final String message) {
		this.message = message;
	}
	
}
