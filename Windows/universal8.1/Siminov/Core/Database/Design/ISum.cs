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

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes API's to return sum of all non-NULL values in the group.
    /// If there are no non-NULL input rows then sum() returns NULL but total() returns 0.0.
    /// NULL is not normally a helpful result for the sum of no rows but the SQL standard requires it and most other SQL database engines implement sum() that way so SQLite does it in the same way in order to be compatible.
    /// The result of sum() is an integer value if all non-NULL inputs are integers. 
    /// </summary>
    public interface ISum
    {

        /// <summary>
        /// Column name of which condition will be specified.
        /// </summary>
        /// <param name="column">Name of column</param>
        /// <returns>ISumClause Interface</returns>
        ISumClause Where(String column);


        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="whereClause">Manually created where clause</param>
        /// <returns>ISum Interface</returns>
        ISum WhereClause(String whereClause);

        /// <summary>
        /// Used to specify AND condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns>ISumClause Interface</returns>
        ISumClause And(String column);

        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns>ISumClause Interface</returns>
        ISumClause Or(String column);

        /// <summary>
        /// Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
        /// </summary>
        /// <param name="columns">Name of columns</param>
        /// <returns>ISum Interface</returns>
        ISum GroupBy(String[] columns);

        /// <summary>
        /// Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be applied</param>
        /// <returns>ISumClause Interface</returns>
        ISumClause Having(String column);

        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="havingClause">Where clause</param>
        /// <returns>ISum Interface</returns>
        ISum HavingClause(String havingClause);

        /// <summary>
        /// Used to provide name of column for which sum will be calculated.
        /// </summary>
        /// <param name="column">Name of column</param>
        /// <returns>ISum Interface</returns>
        ISum Column(String column);

        /// <summary>
        /// Used to get sum, this method should be called in last to calculate sum.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <returns>Return sum</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">Throws exception if any error occur while calculating sum. </exception>
        double Execute();
    }
}
