﻿///
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
    /// Exposes API's to provide condition on where clause to calculate group concat.
    /// </summary>
    public interface IGroupConcatClause
    {

        /// <summary>
        /// Used to specify EQUAL TO (=) condition.
        /// </summary>
        /// <param name="value">Value for which EQUAL TO (=) condition will be applied.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat EqualTo(Object value);


        /// <summary>
        /// Used to specify NOT EQUAL TO (!=) condition.
        /// </summary>
        /// <param name="value">Value for which NOT EQUAL TO (=) condition will be applied.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat NotEqualTo(Object value);

        /// <summary>
        /// Used to specify GREATER THAN (>) condition.
        /// </summary>
        /// <param name="value">Value for while GREATER THAN (>) condition will be specified.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat GreaterThan(Object value);

        /// <summary>
        /// Used to specify GREATER THAN EQUAL (>=) condition.
        /// </summary>
        /// <param name="value">Value for which GREATER THAN EQUAL (>=) condition will be specified.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat GreaterThanEqual(Object value);

        /// <summary>
        /// Used to specify LESS THAN (<) condition.
        /// </summary>
        /// <param name="value">Value for which LESS THAN (<) condition will be specified.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat LessThan(Object value);


        /// <summary>
        /// Used to specify LESS THAN EQUAL (<=) condition.
        /// </summary>
        /// <param name="value">Value for which LESS THAN EQUAL (<=) condition will be specified.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat LessThanEqual(Object value);

        /// <summary>
        /// Used to specify BETWEEN condition.
        /// </summary>
        /// <param name="start">Start Range.</param>
        /// <param name="end">End Range.</param>
        /// <returns>IGroupConcat Interface</returns>
        IGroupConcat Between(Object start, Object end);

        /// <summary>
        /// Used to specify LIKE condition.
        /// </summary>
        /// <param name="like">LIKE condition.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat Like(Object like);

        /// <summary>
        /// Used to specify IN condition.
        /// </summary>
        /// <param name="values">Values for IN condition.</param>
        /// <returns>IGroupConcat Interface.</returns>
        IGroupConcat In(Object[] values);
    }
}
