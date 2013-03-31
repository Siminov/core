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

package siminov.orm.events;

/**
 * Exposes events to deal with life cycle of SIMINOV FRAMEWORK. 
 * It has methods such as (coreInitialized, firstTimeSiminovInitialized, coreStoped).
 */
public interface ISiminovEvents {

	/**
	 * This event gets fired when SIMINOV is initialize for first time.
	 */
	public void firstTimeSiminovInitialized();

	
	/**
	 * This event gets fired when SIMINOV is initialize.
	 */
	public void siminovInitialized();
	
	
	/**
	 * This event gets fired when SIMINOV is stopped.
	 */
	public void siminovStopped();
	
}
