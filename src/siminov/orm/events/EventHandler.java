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

import java.util.Iterator;

import siminov.orm.resource.Resources;
import siminov.orm.utils.ClassUtils;


public class EventHandler {

	private static EventHandler eventHandler = null;
	
	private ISiminovEvents coreEventHandler = null;
	private IDatabaseEvents databaseEventHandler = null;
	
	private Resources resources = Resources.getInstance();
	
	private EventHandler() {
		
		Iterator<String> events = resources.getApplicationDescriptor().getEvents();
		while(events.hasNext()) {
			String event = events.next();
			
			Object object = ClassUtils.createClassInstance(event);
			if(object instanceof ISiminovEvents) {
				coreEventHandler = (ISiminovEvents) object;
			} else if(object instanceof IDatabaseEvents) {
				databaseEventHandler = (IDatabaseEvents) object;
			}
		}
	}
	
	public static EventHandler getInstance() {
		if(eventHandler == null) {
			eventHandler = new EventHandler();
		}
		
		return eventHandler;
	}

	/**
	 * Get core event handler registered by application.
	 * @return ISiminovEvents object implemented by application.
	 */
	public ISiminovEvents getSiminovEventHandler() {
		return this.coreEventHandler;
	}
	
	/**
	 * Get database event handler registered by application.
	 * @return IDatabaseEvents object implemented by application.
	 */
	public IDatabaseEvents getDatabaseEventHandler() {
		return this.databaseEventHandler;
	}
	
}
