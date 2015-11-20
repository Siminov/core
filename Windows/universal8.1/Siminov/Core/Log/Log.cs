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

namespace Siminov.Core.Log
{
    public abstract class Log
    {

        private static String TAG = "SIMINOV";

        /// <summary>
        /// Log info messages
        /// </summary>
        /// <param name="className">Class Name</param>
        /// <param name="methodName">Method Name</param>
        /// <param name="message">Message</param>
        public static void Important(String className, String methodName, String message)
        {

            if (IsEmulator())
            {
                System.Diagnostics.Debug.WriteLine(PrepareMessage(className, methodName, message));
            }

            //android.util.Log.i(TAG, PrepareMessage(className, methodName, message));
        }

        /// <summary>
        /// Log error messages
        /// </summary>
        /// <param name="className">Class Name</param>
        /// <param name="methodName">Method Name</param>
        /// <param name="message">Message</param>
        public static void Error(String className, String methodName, String message)
        {

            if (IsEmulator())
            {
                System.Diagnostics.Debug.WriteLine(PrepareMessage(className, methodName, message));
            }

            //android.util.Log.e(TAG, PrepareMessage(className, methodName, message));
        }

        /// <summary>
        /// Log debug messages
        /// </summary>
        /// <param name="className">Class Name</param>
        /// <param name="methodName">Method Name</param>
        /// <param name="message">Message</param>
        public static void Debug(String className, String methodName, String message)
        {

            if (IsEmulator())
            {
                System.Diagnostics.Debug.WriteLine(PrepareMessage(className, methodName, message));
            }

            //android.util.Log.d(TAG, PrepareMessage(className, methodName, message));
        }

        private static String PrepareMessage(String className, String methodName, String message)
        {
            return "Class Name: " + className + ", Method Name: " + methodName + ", Message: " + message;
        }

        private static bool IsEmulator()
        {
            //return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
            return true;
        }
    }
}
