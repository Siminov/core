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
    /// Exposes API's to get tuples from table based on information provided.
    /// </summary>
    public interface ISelect
    {


        /// <summary>
        /// Used to specify DISTINCT condition.
        /// </summary>
        /// <returns>ICount Interface</returns>
        ISelect Distinct();


        /// <summary>
        /// Column name of which condition will be specified.
        /// </summary>
        /// <param name="column">Name of column</param>
        /// <returns>ISelectClause Interface</returns>
        ISelectClause Where(String column);


        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="whereClause">Manually created where clause</param>
        /// <returns>ISelect Interface</returns>
        ISelect WhereClause(String whereClause);

        /// <summary>
        /// Used to specify AND condition between where clause
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified</param>
        /// <returns>ISelectClause Interface</returns>
        ISelectClause And(String column);


        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified</param>
        /// <returns>ISelectClause Interface</returns>
        ISelectClause Or(String column);


        /// <summary>
        /// Used to specify ORDER BY keyword to sort the result-set.
        /// </summary>
        /// <param name="columns">Name of columns which need to be sorted</param>
        /// <returns>ISelect Interface</returns>
        ISelect OrderBy(String[] columns);


        /// <summary>
        /// Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
        /// </summary>
        /// <param name="columns">Name of columns which need to be sorted</param>
        /// <returns>ISelect Interface</returns>
        ISelect AscendingOrderBy(String[] columns);


        /// <summary>
        /// Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
        /// </summary>
        /// <param name="columns">Name of columns which need to be sorted</param>
        /// <returns>ISelect Interface</returns>
        ISelect DescendingOrderBy(String[] columns);


        /// <summary>
        /// Used to specify the range of data need to fetch from table
        /// </summary>
        /// <param name="limit">LIMIT of data</param>
        /// <returns>ISelect Interface</returns>
        ISelect Limit(int limit);


        /// <summary>
        /// Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
        /// </summary>
        /// <param name="columns">Name of columns</param>
        /// <returns>ISelect Interface</returns>
        ISelect GroupBy(String[] columns);


        /// <summary>
        /// Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be applied</param>
        /// <returns>ISelectClause Interface</returns>
        ISelectClause Having(String column);


        /// <summary>
        /// Used to provide manually created Where clause, instead of using API's.
        /// </summary>
        /// <param name="havingClause">Where clause</param>
        /// <returns>ISelect Interface</returns>
        ISelect HavingClause(String havingClause);

        /// <summary>
        /// Used to provide name of columns only for which data will be fetched.
        /// </summary>
        /// <param name="columns">Name of columns</param>
        /// <returns>ISelect Interface</returns>
        ISelect Columns(String[] columns);

        /*
         * 
         * @return .
         * @throws . 
         */
        /// <summary>
        /// Used to get tuples, this method should be called in last to get tuples from table.
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <returns>Return array of model objects</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">Throws exception if any error occur while getting tuples from table</exception>
        Object[] Execute();
    }
}
