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



using Siminov.Core.Exception;
using Siminov.Core.Log;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Utils
{


    /// <summary>
    /// Exposes utility methods which can be used by both SIMINOV/Application. 
    /// </summary>
    public class Utils
    {


        /// <summary>
        /// Get string from input stream.
        /// </summary>
        /// <param name="inputStream">Input Stream</param>
        /// <returns>String</returns>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while getting string from input stream.</exception>
        public static String ToString(StreamReader inputStream)
        {
            if (inputStream == null)
            {
                Log.Log.Error(typeof(Utils).FullName, "ToString", "Invalid InputStream Found.");
                throw new SiminovException(typeof(Utils).FullName, "ToString", "Invalid InputStream Found.");
            }

            return inputStream.ReadToEnd();
        }


        /// <summary>
        /// Get input stream from string.
        /// </summary>
        /// <param name="value">String</param>
        /// <returns>Input Stream</returns>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while getting input stream from string.</exception>
        public static StreamReader ToInputStream(String value)
        {
            if (value == null || value.Length <= 0)
            {
                Log.Log.Error(typeof(Utils).FullName, "ToInputStream", "Invalid String Found.");
                throw new SiminovException(typeof(Utils).FullName, "ToInputStream", "Invalid String Found.");
            }

            byte[] byteArray = Encoding.UTF8.GetBytes(value);
            MemoryStream stream = new MemoryStream(byteArray);

            return new StreamReader(stream);
        }


        /// <summary>
        /// Get input stream from string.
        /// </summary>
        /// <param name="bytes">Byte Array</param>
        /// <returns>Input Stream</returns>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while getting input stream from byte array.</exception>
        public static StreamReader ToInputStream(byte[] bytes)
        {
            if (bytes == null || bytes.Length <= 0)
            {
                Log.Log.Error(typeof(Utils).FullName, "ToInputStream", "Invalid ByteArray Found.");
                throw new SiminovException(typeof(Utils).FullName, "ToInputStream", "Invalid ByteArray Found.");
            }

            MemoryStream stream = new MemoryStream(bytes);
            return new StreamReader(stream);
        }


        /// <summary>
        /// Get generated unique id.
        /// </summary>
        /// <returns>Long Unique Id</returns>
        public static double GenerateUniqueId()
        {
            return new Random().NextDouble();
        }


        /// <summary>
        /// Check whether application is running on emulator or not.
        /// </summary>
        /// <returns>TRUE: If application is running on emulator, FALSE: If application is not running on emulator.</returns>
        public static bool isEmulator()
        {
            //return Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
            return true;
        }

    }
}
