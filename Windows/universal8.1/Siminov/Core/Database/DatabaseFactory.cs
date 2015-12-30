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



using Siminov.Core.Database.Design;
using Siminov.Core.Model;
using Siminov.Core.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database
{

    /// <summary>
    /// Exposes methods to interact with database. 
    /// It has methods to create, delete, and perform other common database management tasks.
    /// </summary>
    public class DatabaseFactory
    {

        private static DatabaseFactory databaseFactory = null;
        private IDictionary<String, DatabaseBundle> databaseBundles = new Dictionary<String, DatabaseBundle>();

        private String DATABASE_PACKAGE_NAME = "Siminov.Core.Database";
        private String DATABASE_CLASS_NAME = "DatabaseImpl";
        private String DATABASE_QUERY_BUILDER = "QueryBuilder";
        private String DATABASE_DATA_TYPE_HANDLER = "DataTypeHandler";

        /// <summary>
        /// Database Factory Private Constructor
        /// </summary>
        private DatabaseFactory()
        {

        }

        /// <summary>
        /// Get DatabaseFactory Instance
        /// </summary>
        /// <returns>DatabaseFactory Instance</returns>
        public static DatabaseFactory GetInstance()
        {
            if (databaseFactory == null)
            {
                databaseFactory = new DatabaseFactory();
            }

            return databaseFactory;
        }

        /// <summary>
        /// Get IDatabase Instance
        /// </summary>
        /// <param name="databaseDescriptor">DatabaseDescriptor Object</param>
        /// <returns>IDatabase Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create IDatabase instance</exception>
        public DatabaseBundle GetDatabaseBundle(DatabaseDescriptor databaseDescriptor)
        {
            if (databaseBundles.ContainsKey(databaseDescriptor.GetDatabaseName()))
            {
                return databaseBundles[databaseDescriptor.GetDatabaseName()];
            }

            String type = databaseDescriptor.GetType();
            String packageName = DATABASE_PACKAGE_NAME + "." + type;

            DatabaseBundle databaseBundle = GetDatabaseBundle(packageName);
            databaseBundles.Add(databaseDescriptor.GetDatabaseName(), databaseBundle);

            return databaseBundle;
        }


        /// <summary>
        /// Returns all database bundles
        /// </summary>
        /// <returns>Iterator<DatabaseHandler> All Database Bundle instances</returns>
        public IEnumerator<DatabaseBundle> GetDatabaseBundles()
        {
            return databaseBundles.Values.GetEnumerator();
        }

        /// <summary>
        /// Removes database bundle instance
        /// </summary>
        /// <param name="databaseDescriptor">Database descriptor instance object</param>
        public void RemoveDatabaseBundle(DatabaseDescriptor databaseDescriptor)
        {
            this.databaseBundles.Remove(databaseDescriptor.GetDatabaseName());
        }


        /// <summary>
        /// Returns database bundle instance
        /// </summary>
        /// <param name="packageName">Name of the package</param>
        /// <returns>DatabaseBundle Instance of database bundle</returns>
        private DatabaseBundle GetDatabaseBundle(String packageName)
        {

            IDatabaseImpl database = (IDatabaseImpl)ClassUtils.CreateClassInstance(packageName + "." + DATABASE_CLASS_NAME);
            IQueryBuilder queryBuilder = (IQueryBuilder)ClassUtils.CreateClassInstance(packageName + "." + DATABASE_QUERY_BUILDER);
            IDataTypeHandler dataTypeHandler = (IDataTypeHandler)ClassUtils.CreateClassInstance(packageName + "." + DATABASE_DATA_TYPE_HANDLER);

            DatabaseBundle databaseBundle = new DatabaseBundle();
            databaseBundle.SetDatabase(database);
            databaseBundle.SetQueryBuilder(queryBuilder);
            databaseBundle.SetDataTypeHandler(dataTypeHandler);

            return databaseBundle;
        }

    }
}
