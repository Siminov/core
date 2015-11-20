/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
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



using Siminov.Core.Resource;
using Siminov.Core.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Events
{
    /// <summary>
    /// It provides the event handler instances.
    /// </summary>
    public class EventHandler
    {

        private static EventHandler eventHandler = null;

        private ISiminovEvents coreEventHandler = null;
        private IDatabaseEvents databaseEventHandler = null;

        private ResourceManager resourceManager = ResourceManager.GetInstance();

        /// <summary>
        /// Event Handler Constructor
        /// </summary>
        private EventHandler()
        {

            IEnumerator<String> events = resourceManager.GetApplicationDescriptor().GetEvents();
            while (events.MoveNext())
            {
                String eventNotifier = events.Current;

                Object model = ClassUtils.CreateClassInstance(eventNotifier);
                if (model is ISiminovEvents)
                {
                    coreEventHandler = (ISiminovEvents)model;
                }
                else if (model is IDatabaseEvents)
                {
                    databaseEventHandler = (IDatabaseEvents)model;
                }
            }
        }

        /// <summary>
        /// Returns the singleton instance of Event Handler
        /// </summary>
        /// <returns>EventHandler Singleton instance of Event Handler</returns>
        public static EventHandler GetInstance()
        {
            if (eventHandler == null)
            {
                eventHandler = new EventHandler();
            }

            return eventHandler;
        }

        /// <summary>
        /// Get core event handler registered by application.
        /// </summary>
        /// <returns>ISiminovEvents object implemented by application</returns>
        public ISiminovEvents GetSiminovEventHandler()
        {
            return this.coreEventHandler;
        }

        /// <summary>
        /// Get database event handler registered by application.
        /// </summary>
        /// <returns>IDatabaseEvents object implemented by application</returns>
        public IDatabaseEvents GetDatabaseEventHandler()
        {
            return this.databaseEventHandler;
        }
    }
}
