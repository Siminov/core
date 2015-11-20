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

namespace Siminov.Core.Exception
{
    public class SiminovException : System.Exception, IException
    {

        private String className = null;
        private String methodName = null;
        private String message = null;


        /// <summary>
        /// SiminovException Constructor
        /// </summary>
        /// <param name="className">Name of class</param>
        /// <param name="methodName">Name of method</param>
        /// <param name="message">Exception message</param>
        public SiminovException(String className, String methodName, String message)
        {
            this.className = className;
            this.methodName = methodName;
            this.message = message;
        }


        /// <summary>
        /// Get CSharp class name
        /// </summary>
        /// <returns>CSharp Class Name</returns>
        public String GetClassName()
        {
            return this.className;
        }

        /// <summary>
        /// Set CSharp class name
        /// </summary>
        /// <param name="className">CSharp Class Name</param>
        public void SetClassName(String className)
        {
            this.className = className;
        }

        /// <summary>
        /// Get method Name
        /// </summary>
        /// <returns>Name Of Method</returns>
        public String GetMethodName()
        {
            return this.methodName;
        }

        /// <summary>
        /// Set method Name
        /// </summary>
        /// <param name="methodName">Name Of Method</param>
        public void SetMethodName(String methodName)
        {
            this.methodName = methodName;
        }

        /// <summary>
        /// Get message
        /// </summary>
        /// <returns>Message</returns>
        public String GetMessage()
        {
            return this.message;
        }

        /// <summary>
        /// Set message
        /// </summary>
        /// <param name="message">Message</param>
        public void SetMessage(String message)
        {
            this.message = message;
        }
    }
}
