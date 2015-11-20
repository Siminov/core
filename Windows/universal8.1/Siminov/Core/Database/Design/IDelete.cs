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
    /// Exposes API's to delete tuples from table.
    /// </summary>
    public interface IDelete
    {

        /// <summary>
        /// Column name of which condition will be specified.
        /// </summary>
        /// <param name="column">Name of column.</param>
        /// <returns><c>IDeleteClause </c>Interface.</returns>
        IDeleteClause Where(String column);

        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="whereClause">Manually created where clause.</param>
        /// <returns><c>IDelete </c>Interface.</returns>
        IDelete WhereClause(String whereClause);

        /// <summary>
        /// Used to specify AND condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns><c>IDeleteClause </c>Interface.</returns>
        IDeleteClause And(String column);

        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns><c>IDeleteClause </c>Interface.</returns>
        IDeleteClause Or(String column);


        /// <summary>
        /// Used to delete, this method should be called in last to delete tuples from table.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">Throws exception if any error occur while deleting tuples from table. </exception>
        void Execute();

    }
}
