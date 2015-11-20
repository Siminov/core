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



using Siminov.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Events
{

    /// <summary>
    /// Exposes methods which deal with events associated with database operation's.
    /// It has methods such as (databaseCreated, databaseDroped, tableCreated, tableDroped, indexCreated).
    /// </summary>
    public interface IDatabaseEvents
    {

        /// <summary>
        /// This event is fired when database gets created as per database descriptor.
        /// </summary>
        /// <param name="databaseDescriptor">contains meta data associated with database</param>
        void OnDatabaseCreated(DatabaseDescriptor databaseDescriptor);

        /// <summary>
        /// This event is fired when database is dropped.
        /// </summary>
        /// <param name="databaseDescriptor">contains meta data associated with dropped database</param>
        void OnDatabaseDropped(DatabaseDescriptor databaseDescriptor);



        /// <summary>
        /// This event is fired when a table is created.
        /// </summary>
        /// <param name="databaseDescriptor">Database descriptor object</param>
        /// <param name="entityDescriptor">Entity descriptor object</param>
        void OnTableCreated(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor);

        /// <summary>
        /// This event is fired when a table is dropped.
        /// </summary>
        /// <param name="databaseDescriptor">Database descriptor object</param>
        /// <param name="entityDescriptor">Entity descriptor object</param>
        void OnTableDropped(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor);



        /// <summary>
        /// This event is fired when a index is created on table.
        /// </summary>
        /// <param name="databaseDescriptor">Database descriptor object</param>
        /// <param name="entityDescriptor">Entity descriptor object</param>
        /// <param name="index">meta data about index got created</param>
        void OnIndexCreated(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, EntityDescriptor.Index index);



        /// <summary>
        /// This event is fired when a index is dropped.
        /// </summary>
        /// <param name="databaseDescriptor">Database descriptor object</param>
        /// <param name="entityDescriptor">Entity descriptor object</param>
        /// <param name="index">meta data about index got dropped</param>
        void OnIndexDropped(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, EntityDescriptor.Index index);

    }
}
