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



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Events
{

    /// <summary>
    /// Exposes events to deal with life cycle of SIMINOV FRAMEWORK. 
    /// It has methods such as (onCoreInitialized, onFirstTimeSiminovInitialized, onCoreStoped).
    /// </summary>
    public interface ISiminovEvents
    {

        /// <summary>
        /// This event gets fired when SIMINOV is initialize for first time.
        /// </summary>
        void OnFirstTimeSiminovInitialized();


        /// <summary>
        /// This event gets fired when SIMINOV is initialize.
        /// </summary>
        void OnSiminovInitialized();



        /// <summary>
        /// This event gets fired when SIMINOV is stopped.
        /// </summary>
        void OnSiminovStopped();
    }
}
