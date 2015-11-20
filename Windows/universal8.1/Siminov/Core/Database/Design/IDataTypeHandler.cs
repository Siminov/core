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

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes convert API which is responsible to provide column data type based on java variable data type.
    /// </summary>
    public abstract class IDataTypeHandler
    {


        /// <summary>
        /// SQLite Integer Data Type
        /// </summary>
        public static readonly String SQLITE_INTEGER_DATA_TYPE = "INTEGER";

        /// <summary>
        /// SQLite Text Data Type
        /// </summary>
        public static readonly String SQLITE_TEXT_DATA_TYPE = "TEXT";

        /// <summary>
        /// SQLite Real Data Type
        /// </summary>
        public static readonly String SQLITE_REAL_DATA_TYPE = "REAL";

        /// <summary>
        /// SQLite None Data Type
        /// </summary>
        public static readonly String SQLITE_NONE_DATA_TYPE = "NONE";

        /// <summary>
        /// SQLite Numeric Data Type
        /// </summary>
        public static readonly String SQLITE_NUMERIC_DATA_TYPE = "NUMERIC";


        /*
         * Java Data Types
         */

        /// <summary>
        /// CSharp Int Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_INT_PRIMITIVE_DATA_TYPE = typeof(int).FullName;


        /// <summary>
        /// CSharp Long Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_LONG_PRIMITIVE_DATA_TYPE = typeof(long).FullName;

        /// <summary>
        /// CSharp Float Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_FLOAT_PRIMITIVE_DATA_TYPE = typeof(float).FullName;


        /// <summary>
        /// CSharp Double Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_DOUBLE_PRIMITIVE_DATA_TYPE = typeof(double).FullName;


        /// <summary>
        /// CSharp Double Data Type
        /// </summary>
        public static readonly String CSHARP_DOUBLE_DATA_TYPE = typeof(Double).FullName;


        /// <summary>
        /// CSharp Boolean Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_BOOLEAN_PRIMITIVE_DATA_TYPE = typeof(bool).FullName;


        /// <summary>
        /// CSharp Boolean Data Type
        /// </summary>
        public static readonly String CSHARP_BOOLEAN_DATA_TYPE = typeof(Boolean).FullName;

        /// <summary>
        /// CSharp Char Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_CHAR_PRIMITIVE_DATA_TYPE = typeof(char).FullName;

        /// <summary>
        /// CSharp Character Data Type
        /// </summary>
        public static readonly String CSHARP_CHARACTER_DATA_TYPE = typeof(Char).FullName;


        /// <summary>
        /// CSharp String Data Type
        /// </summary>
        public static readonly String CSHARP_STRING_DATA_TYPE = typeof(String).FullName;

        /// <summary>
        /// CSharp Byte Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_BYTE_PRIMITITVE_DATA_TYPE = typeof(byte).FullName;


        /// <summary>
        /// CSharp Byte Data Type
        /// </summary>
        public static readonly String CSHARP_BYTE_DATA_TYPE = typeof(Byte).FullName;


        /// <summary>
        /// CSharp Void Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_VOID_PRIMITITVE_DATA_TYPE = typeof(void).FullName;

        /// <summary>
        /// CSharp Short Primitive Data Type
        /// </summary>
        public static readonly String CSHARP_SHORT_PRIMITITVE_DATA_TYPE = typeof(short).FullName;


        /*
         * JavaScript Data Types 
         */


        /// <summary>
        /// JavaScript String Data Type
        /// </summary>
        public static readonly String JAVASCRIPT_STRING_DATA_TYPE = "String";

        /// <summary>
        /// JavaScript Number Data Type
        /// </summary>
        public static readonly String JAVASCRIPT_NUMBER_DATA_TYPE = "Number";

        /// <summary>
        /// JavaScript Boolean Data Type
        /// </summary>
        public static readonly String JAVASCRIPT_BOOLEAN_DATA_TYPE = "Boolean";


        /// <summary>
        /// Converts java variable data type to database column data type.
        /// </summary>
        /// <param name="dataType">Java variable data type.</param>
        /// <returns>column data type</returns>
        public abstract String Convert(String dataType);

    }
}
