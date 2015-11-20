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

namespace Siminov.Core.Model
{


    /// <summary>
    /// It exposes common API for all the descriptor
    /// It has method to get and set properties of descriptor
    /// </summary>
    public interface IDescriptor
    {


        /// <summary>
        /// Get all the properties of descriptor
        /// </summary>
        /// <returns>All properties</returns>
        IEnumerator<String> GetProperties();


        /// <summary>
        /// Get the property value based on the property name
        /// </summary>
        /// <param name="name">Name of the property</param>
        /// <returns>Value of the property</returns>
        String GetProperty(String name);



        /// <summary>
        /// Check whether property exists or not 
        /// </summary>
        /// <param name="name">Name of the property</param>
        /// <returns>(true/false) TRUE: If property exists | FALSE: If property does not exists.</returns>
        bool ContainProperty(String name);


        /// <summary>
        /// Add property to the descriptor
        /// </summary>
        /// <param name="name">Name of the property</param>
        /// <param name="value">Value of the property</param>
        void AddProperty(String name, String value);


        /// <summary>
        /// Remove property from the descriptor
        /// </summary>
        /// <param name="name">Name of the property</param>
        void RemoveProperty(String name);
    }
}
