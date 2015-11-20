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
    /// It is a collection of below database items:
    /// <list type="bullet">
    /// <item>
    ///     <description>Database Instance</description>
    /// </item>
    /// <item>
    ///     <description>Query Builder Instance</description>
    /// </item>
    /// <item>
    ///     <description>Data Type Handler Instance</description>
    /// </item>
    /// </list>
    /// </summary>
    public class DatabaseBundle
    {

        private IDatabaseImpl database = null;
        private IQueryBuilder queryBuilder = null;
        private IDataTypeHandler dataTypeHandler = null;

        /// <summary>
        /// It returns the database instance
        /// </summary>
        /// <returns>IDatabaseImpl instance object</returns>
        public IDatabaseImpl GetDatabase()
        {
            return this.database;
        }

        /// <summary>
        /// It sets the database instance
        /// </summary>
        /// <param name="database">IDatabaseImpl instance object</param>
        public void SetDatabase(IDatabaseImpl database)
        {
            this.database = database;
        }

        /// <summary>
        /// It returns the query builder instance
        /// </summary>
        /// <returns>IQueryBuilder Query builder instance object</returns>
        public IQueryBuilder GetQueryBuilder()
        {
            return this.queryBuilder;
        }

        /// <summary>
        /// It sets the query builder instance
        /// </summary>
        /// <param name="queryBuilder">IQueryBuilder instance object</param>
        public void SetQueryBuilder(IQueryBuilder queryBuilder)
        {
            this.queryBuilder = queryBuilder;
        }


        /// <summary>
        /// It returns the data type handler instance
        /// </summary>
        /// <returns>IDataTypeHandler Data Type Handler instance object</returns>
        public IDataTypeHandler GetDataTypeHandler()
        {
            return this.dataTypeHandler;
        }

        /// <summary>
        /// It sets the data type handler instance
        /// </summary>
        /// <param name="dataTypeHandler">Data Type Handler instance object</param>
        public void SetDataTypeHandler(IDataTypeHandler dataTypeHandler)
        {
            this.dataTypeHandler = dataTypeHandler;
        }
    }
}
