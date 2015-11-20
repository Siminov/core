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



using Siminov.Core.Database.Design;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database
{

    /// <summary>
    /// It is used to create where clause used in database query.
    /// It implements all the Clauses which are used to in the where clause.	
    /// </summary>
    public class SumClauseImpl : ISumClause
    {

        internal static readonly String EQUAL_TO = "=";
        internal static readonly String NOT_EQUAL_TO = "!=";
        internal static readonly String GREATER_THAN = ">";
        internal static readonly String GREATER_THAN_EQUAL = ">=";
        internal static readonly String LESS_THAN = "<";
        internal static readonly String LESS_THAN_EQUAL = "<=";
        internal static readonly String BETWEEN = "BETWEEN";
        internal static readonly String LIKE = "LIKE";
        internal static readonly String IN = "IN";
        internal static readonly String AND = "AND";
        internal static readonly String OR = "OR";

        internal static readonly String ASC_ORDER_BY = "ASC";
        internal static readonly String DESC_ORDER_BY = "DESC";

        private StringBuilder whereClause = new StringBuilder();

        private SumImpl where = null;

        /// <summary>
        /// Clause Constructor
        /// </summary>
        /// <param name="where">Where clause</param>
        public SumClauseImpl(SumImpl where)
        {
            this.where = where;
        }

        /// <summary>
        /// Add column
        /// </summary>
        /// <param name="column">Name of column</param>
        internal void AddCol(String column)
        {
            whereClause.Append(column);
        }

        /// <summary>
        /// Used to specify EQUAL TO (=) condition.
        /// </summary>
        /// <param name="value">Value for which EQUAL TO (=) condition will be applied.</param>
        /// <returns>Where object</returns>
        public ISum EqualTo(Object value)
        {
            whereClause.Append(EQUAL_TO + " '" + value.ToString() + "' ");
            return this.where;
        }

        /// <summary>
        /// Used to specify NOT EQUAL TO (!=) condition.
        /// </summary>
        /// <param name="value">Value for which NOT EQUAL TO (=) condition will be applied</param>
        /// <returns>Where object</returns>
        public ISum NotEqualTo(Object value)
        {
            whereClause.Append(NOT_EQUAL_TO + " '" + value + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify GREATER THAN (>) condition.
        /// </summary>
        /// <param name="value">Value for while GREATER THAN (>) condition will be specified.</param>
        /// <returns>Where object</returns>
        public ISum GreaterThan(Object value)
        {
            whereClause.Append(GREATER_THAN + " '" + value + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify GREATER THAN EQUAL (>=) condition.
        /// </summary>
        /// <param name="value">Value for which GREATER THAN EQUAL (>=) condition will be specified.</param>
        /// <returns>Where object</returns>
        public ISum GreaterThanEqual(Object value)
        {
            whereClause.Append(GREATER_THAN_EQUAL + " '" + value + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify LESS THAN (<) condition.
        /// </summary>
        /// <param name="value">Value for which LESS THAN (<) condition will be specified</param>
        /// <returns>Where object</returns>
        public ISum LessThan(Object value)
        {
            whereClause.Append(LESS_THAN + " '" + value + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify LESS THAN EQUAL (<=) condition.
        /// </summary>
        /// <param name="value">Value for which LESS THAN EQUAL (<=) condition will be specified.</param>
        /// <returns>Where object</returns>
        public ISum LessThanEqual(Object value)
        {
            whereClause.Append(LESS_THAN_EQUAL + " '" + value + "' ");
            return this.where;
        }

        /// <summary>
        /// Used to specify BETWEEN condition.
        /// </summary>
        /// <param name="start">Start Range</param>
        /// <param name="end">End Range</param>
        /// <returns>Where object</returns>
        public ISum Between(Object start, Object end)
        {
            whereClause.Append(BETWEEN + " '" + start + "' " + AND + " '" + end + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify LIKE condition.
        /// </summary>
        /// <param name="like">LIKE condition</param>
        /// <returns>Where object</returns>
        public ISum Like(Object like)
        {
            whereClause.Append(LIKE + " '" + like + "' ");
            return this.where;
        }


        /// <summary>
        /// Used to specify IN condition.
        /// </summary>
        /// <param name="values">Values for IN condition</param>
        /// <returns>Where object</returns>
        public ISum In(Object[] values)
        {
            whereClause.Append(IN + "(");

            if (values != null && values.Length > 0)
            {
                for (int i = 0; i < values.Length; i++)
                {
                    if (i == 0)
                    {
                        whereClause.Append("'" + values[i] + "'");
                        continue;
                    }

                    whereClause.Append(" ,'" + values[i] + "'");
                }
            }

            whereClause.Append(")");

            return this.where;
        }


        /// <summary>
        /// Used to specify AND condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified</param>
        internal void And(String column)
        {
            whereClause.Append(" " + AND + " " + column);
        }

        /// <summary>
        /// Used to specify OR condition between where clause.
        /// </summary>
        /// <param name="column">Name of column on which condition need to be specified</param>
        internal void Or(String column)
        {
            whereClause.Append(" " + OR + " " + column);
        }


        /// <summary>
        /// It returns the where clause.
        /// </summary>
        /// <returns>String where clause</returns>
        public override String ToString()
        {
            return whereClause.ToString();
        }
    }
}
