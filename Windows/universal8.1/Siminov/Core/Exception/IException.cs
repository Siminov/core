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



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Exception
{

    /// <summary>
    /// Exposes method to deal with exception
    /// It has methods to set exception class name, exception method name, exception message.
    /// </summary>
    public interface IException
    {

        /// <summary>
        /// Get exception class name
        /// </summary>
        /// <returns>Exception Class Name</returns>
        String GetClassName();

        /// <summary>
        /// Set exception class name	
        /// </summary>
        /// <param name="className">Name of exception class</param>
        void SetClassName(String className);


        /// <summary>
        /// Get exception method name
        /// </summary>
        /// <returns>Name of exception method</returns>
        String GetMethodName();


        /// <summary>
        /// Set exception method name
        /// </summary>
        /// <param name="methodName">Name of method</param>
        void SetMethodName(String methodName);


        /// <summary>
        /// Get exception message
        /// </summary>
        /// <returns>Exception message</returns>
        String GetMessage();


        /// <summary>
        /// Set exception message
        /// </summary>
        /// <param name="message">Exception message</param>
        void SetMessage(String message);
    }
}
