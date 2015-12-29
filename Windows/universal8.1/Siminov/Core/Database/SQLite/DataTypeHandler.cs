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



using Siminov.Core.Database.Design;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Sqlite
{

    /// <summary>
    /// Provides the IDataTypeHandler implementation for SQLite database
    /// </summary>
    public class DataTypeHandler : IDataTypeHandler
    {

        public override String Convert(String dataType)
        {

            /*
             * CSharp Data Type Conversation
             */
            if (dataType.Equals(IDataTypeHandler.CSHARP_INT_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_INTEGER_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_LONG_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_INTEGER_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_FLOAT_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_REAL_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_DOUBLE_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_REAL_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_DOUBLE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_REAL_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_BOOLEAN_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_BOOLEAN_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_CHAR_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_CHARACTER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_BYTE_PRIMITITVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_NONE_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_BYTE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_NONE_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_VOID_PRIMITITVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_NONE_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.CSHARP_SHORT_PRIMITITVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_INTEGER_DATA_TYPE;
            }


          /*
           * JavaScript Data Type Conversation
           */
            else if (dataType.Equals(IDataTypeHandler.JAVASCRIPT_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_REAL_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.JAVASCRIPT_NUMBER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_REAL_DATA_TYPE;
            }
            else if (dataType.Equals(IDataTypeHandler.JAVASCRIPT_BOOLEAN_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
            {
                return IDataTypeHandler.SQLITE_TEXT_DATA_TYPE;
            }


          /*
           * Other Data Type
           */
            else
            {
                return IDataTypeHandler.SQLITE_NONE_DATA_TYPE;
            }

        }
    }
}
