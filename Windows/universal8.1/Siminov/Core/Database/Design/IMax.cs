///
/// [SIMINOV FRAMEWORK - CORE]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes API's to returns the maximum value of all values in the group.
    /// The maximum value is the value that would be returned last in an ORDER BY on the same column. 
    /// Aggregate max() returns NULL if and only if there are no non-NULL values in the group.
    /// </summary>
    public interface IMax
    {

        /// <summary>
        /// Column name of which condition will be specified.
        /// </summary>
        /// <param name="column">Name of column.</param>
        /// <returns>IMaxClause Interface</returns>
        IMaxClause Where(String column);

        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="whereClause">Manually created where clause</param>
        /// <returns>IMax Interface</returns>
        IMax WhereClause(String whereClause);

        /// <summary>
        /// Used to specify AND condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified</param>
        /// <returns>IMaxClause Interface</returns>
        IMaxClause And(String column);


        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns>IMaxClause Interface</returns>
        IMaxClause Or(String column);


        /// <summary>
        /// Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
        /// </summary>
        /// <param name="columns">Name of columns</param>
        /// <returns>IMax Interface</returns>
        IMax GroupBy(String[] columns);


        /// <summary>
        /// Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be applied</param>
        /// <returns>IMaxClause Interface</returns>
        IMaxClause Having(String column);


        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="havingClause">Where clause</param>
        /// <returns>IMax Interface</returns>
        IMax HavingClause(String havingClause);

        /// <summary>
        /// Used to provide name of column for which maximum will be calculated.
        /// </summary>
        /// <param name="column">Name of column</param>
        /// <returns>IMax Interface</returns>
        IMax Column(String column);

        /// <summary>
        /// Used to get maximum, this method should be called in last to calculate maximum.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <returns>Return maximum</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">Throws exception if any error occur while calculating maximum. </exception>
        double Execute();
    }
}
