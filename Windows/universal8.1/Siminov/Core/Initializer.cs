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




using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core
{


    /// <summary>
    /// It implements IInitializer Interface.
    /// It handle initialization of framework.
    /// </summary>
    public class Initializer : IInitializer
    {

        private ResourceManager resourceManager = ResourceManager.GetInstance();

        private ICollection<Object> parameters = new List<Object>();


        /// <summary>
        /// Add Initialization Parameter.
        /// </summary>
        /// <param name="parameter"></param>
        public void AddParameter(Object parameter)
        {
            parameters.Add(parameter);
        }


        /// <summary>
        /// It is used to initialize and start the framework
        /// </summary>
        public void Initialize()
        {

            IEnumerator<Object> iterator = parameters.GetEnumerator();
            while (iterator.MoveNext())
            {

                Object value = iterator.Current;
                resourceManager.AddApplicationContext(value);
            }

            Siminov.Start();
        }

    }
}
