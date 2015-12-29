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



using Siminov.Core.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes API's to get average value of all non-NULL X within a group. 
    /// String and BLOB values that do not look like numbers are interpreted as 0.
    /// The result of avg() is always a floating point value as long as at there is at least one non-NULL input even if all inputs are integers.
    /// The result of avg() is NULL if and only if there are no non-NULL inputs.
    /// </summary>

    public interface IAverage
    {

        /// <summary>
        /// Column name of which condition will be specified.
        /// </summary>
        /// <param name="column">Name of column.</param>
        /// <returns><c>IAverageClause</c> Interface.</returns>
        IAverageClause Where(String column);

        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="whereClause">Manually created where clause.</param>
        /// <returns><c>IAverage</c> Interface.</returns>
        IAverage WhereClause(String whereClause);

        /// <summary>
        /// Used to specify AND condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns><c>IAerageClause</c> Interface.</returns>
        IAverageClause And(String column);

        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified.</param>
        /// <returns><c>IAverageClause</c> Interface.</returns>
        IAverageClause Or(String column);


        /// <summary>
        /// Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns. 
        /// </summary>
        /// <param name="columns">Name of columns.</param>
        /// <returns><c>IAverage</c> Interface.</returns>
        IAverage GroupBy(String[] columns);


        /// <summary>
        /// Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be applied.</param>
        /// <returns><c>IAverageClause</c> Interface.</returns>
        IAverageClause Having(String column);


        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="havingClause">Where clause.</param>
        /// <returns><c>IAverage</c> Interface.</returns>
        IAverage HavingClause(String havingClause);


        /// <summary>
        /// Used to provide name of column for which average will be calculated.
        /// </summary>
        /// <param name="column">Name of column.</param>
        /// <returns><c>IAverage</c> Interface.</returns>
        IAverage Column(String column);


        /// <summary>
        /// Used to get average, this method should be called in last to calculate average.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <returns>Average.</returns>
        /// <exception cref="Siminov.Core.Exceptions.DatabaseException">Throws exception if any error occur while calculating average.</exception>
        double Execute();
    }
}
