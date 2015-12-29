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


using Siminov.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes methods to deal with actual database object.
    /// It has methods to open, create, close, and execute query's.
    /// </summary>
    public interface IDatabaseImpl
    {


        /// <summary>
        /// Open/Create the database through Database Descriptor.
        /// <para>
        /// By default add CREATE_IF_NECESSARY flag so that if database does not exist it will create.
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor">Database-Descriptor object which defines the schema of database.</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If the database cannot be opened or create.</exception>
        void OpenOrCreate(DatabaseDescriptor databaseDescriptor);


        /// <summary>
        /// Close the existing opened database through Database Descriptor.
        /// </summary>
        /// <param name="databaseDescriptor">Database-Descriptor object which defines the schema of database.</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If the database cannot be closed.</exception>
        void Close(DatabaseDescriptor databaseDescriptor);


        /// <summary>
        /// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data. 
        /// <para>
        /// It has no means to return any data (such as the number of affected rows). Instead, you're encouraged to use insert, update, delete, when possible. 
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor">Database-Descriptor object which defines the schema of database.</param>
        /// <param name="entityDescriptor">Entity-Descriptor object which defines the structure of table.</param>
        /// <param name="query">Query which needs to be executed.</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while executing query provided.</exception>
        void ExecuteQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query);


        /// <summary>
        /// A pre-compiled statement that can be reused. The statement cannot return multiple rows, but 1x1 result sets are allowed.
        /// </summary>
        /// <param name="databaseDescriptor">Database-Descriptor object which defines the schema of database.</param>
        /// <param name="entityDescriptor">Entity-Descriptor object which defines the structure of table.</param>
        /// <param name="query">A pre-compiled statement.</param>
        /// <param name="columnValues">Column values</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while inserting or updating tuple.</exception>
        void ExecuteBindQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query, IEnumerator<Object> columnValues);


        /// <summary>
        /// Query the given table, returning a Cursor over the result set.
        /// </summary>
        /// <param name="databaseDescriptor">Database-Descriptor object which defines the schema of database.</param>
        /// <param name="entityDescriptor">Entity-Descriptor object which defines the structure of table.</param>
        /// <param name="query">Query based on which tuples will be fetched from database.</param>
        /// <returns>A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting tuples from a single table.</exception>
        IEnumerator<IDictionary<String, Object>> ExecuteSelectQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query);


        /// <summary>
        /// Executes the method on database object.
        /// </summary>
        /// <param name="methodName">Name Of Database Method.</param>
        /// <param name="parameters">Parameters Needed By Database Method.</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exeception occur which invoking method in database object.</exception>
        void ExecuteMethod(String methodName, Object parameters);

    }
}
