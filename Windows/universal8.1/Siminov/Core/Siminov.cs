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



using Siminov.Core.Utils;
using Siminov.Core.Database;
using Siminov.Core.Database.Design;
using Siminov.Core.Events;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using Siminov.Core.Reader;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core
{


    /// <summary>
    /// Exposes methods to deal with SIMINOV FRAMEWORK.
    /// <para>
    /// Such As
    /// <list type="bullet">
    /// <item>
    ///     <description>Initializer: Entry point to the SIMINOV.</description>
    /// </item>
    /// <item>
    ///     <description>Shutdown: Exit point from the SIMINOV.</description>
    /// </item>
    /// </list>
    /// </para>
    /// </summary>
    public class Siminov
    {

        protected static bool isActive = false;
        protected static bool firstTimeProcessed = false;

        protected static ResourceManager coreResourceManager = ResourceManager.GetInstance();


        /// <summary>
        /// It is used to check whether SIMINOV FRAMEWORK is active or not.
        /// <para>
        /// SIMINOV become active only when deployment of application is successful.
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DeploymentException">If SIMINOV is not active it will throw DeploymentException which is RuntimeException.</exception>
        public static void IsActive()
        {
            if (!isActive)
            {
                throw new DeploymentException(typeof(Siminov).FullName, "IsActive", "Siminov Not Active.");
            }
        }



        /// <summary>
        /// Returns the IInitializer instance.
        /// </summary>
        /// <returns>Instance of IInitializer</returns>
        public static IInitializer Initializer()
        {
            return new Initializer();
        }


        /// <summary>
        /// It is the entry point to the SIMINOV FRAMEWORK.
        /// <para>
        /// When application starts it should call this method to activate SIMINOV-FRAMEWORK, by providing ApplicationContext as the parameter.
        /// Siminov will read all descriptor defined by application, and do necessary processing.
        /// EXAMPLE
        /// There are two ways to make a call.
        /// <list type="bullet">
        /// <item>
        /// Call it from Application class.
        /// <code>
        ///         public App()
        ///            {
        ///                this.InitializeComponent();
        ///                this.Suspending += this.OnSuspending;
        ///
        ///                new Siminov.Core.Sample.Siminov().InitializeSiminov();
        ///            }
        /// </code>
        /// </item>
        /// </list>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DeploymentException">If any exception occur while deploying application it will through DeploymentException, which is RuntimeException.</exception>
        public static void Start()
        {

            if (isActive)
            {
                return;
            }

            Process();

            isActive = true;

            ISiminovEvents coreEventHandler = coreResourceManager.GetSiminovEventHandler();
            if (coreResourceManager.GetSiminovEventHandler() != null)
            {
                if (firstTimeProcessed)
                {
                    coreEventHandler.OnFirstTimeSiminovInitialized();
                }
                else
                {
                    coreEventHandler.OnSiminovInitialized();
                }
            }
        }



        /// <summary>
        /// It is used to stop all service started by SIMINOV.
        /// When application shutdown they should call this. It do following services: 
        /// 
        /// <list type="bullet">
        /// <item>
        ///     <description>Close all database's opened by SIMINOV.</description>
        /// </item>
        /// <item>
        ///     <description>Deallocate all resources held by SIMINOV.</description>
        /// </item>
        /// </list>
        /// </summary>
        public static void Shutdown()
        {
            IsActive();

            IEnumerator<DatabaseDescriptor> databaseDescriptors = coreResourceManager.GetDatabaseDescriptors();

            bool failed = false;
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                DatabaseBundle databaseBundle = coreResourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
                IDatabaseImpl database = databaseBundle.GetDatabase();

                try
                {
                    database.Close(databaseDescriptor);
                }
                catch (DatabaseException databaseException)
                {
                    failed = true;

                    Log.Log.Error(typeof(Siminov).FullName, "Shutdown", "DatabaseException caught while closing database, " + databaseException.GetMessage());
                    continue;
                }
            }

            if (failed)
            {
                throw new SiminovCriticalException(typeof(Siminov).FullName, "Shutdown", "DatabaseException caught while closing database.");
            }

            ISiminovEvents coreEventHandler = coreResourceManager.GetSiminovEventHandler();
            if (coreResourceManager.GetSiminovEventHandler() != null)
            {
                coreEventHandler.OnSiminovStopped();
            }
        }


        private static void Process()
        {

            ProcessApplicationDescriptor();
            ProcessDatabaseDescriptors();
            ProcessLibraries();
            ProcessEntityDescriptors();

            ProcessDatabase();
        }


        /// <summary>
        /// It process ApplicationDescriptor.xml file defined in Application, and stores in Resource Manager.
        /// </summary>
        protected static void ProcessApplicationDescriptor()
        {
            ApplicationDescriptorReader applicationDescriptorParser = new ApplicationDescriptorReader();

            ApplicationDescriptor applicationDescriptor = applicationDescriptorParser.GetApplicationDescriptor();
            if (applicationDescriptor == null)
            {
                Log.Log.Debug(typeof(Siminov).FullName, "ProcessApplicationDescriptor", "Invalid Application Descriptor Found.");
                throw new DeploymentException(typeof(Siminov).FullName, "ProcessApplicationDescriptor", "Invalid Application Descriptor Found.");
            }

            coreResourceManager.SetApplicationDescriptor(applicationDescriptor);
        }



        /// <summary>
        /// It process all DatabaseDescriptor.xml files defined by Application and stores in Resource Manager.
        /// </summary>
        protected static void ProcessDatabaseDescriptors()
        {
            IEnumerator<String> databaseDescriptorPaths = coreResourceManager.GetApplicationDescriptor().GetDatabaseDescriptorPaths();
            while (databaseDescriptorPaths.MoveNext())
            {

                String databaseDescriptorPath = databaseDescriptorPaths.Current;

                DatabaseDescriptorReader databaseDescriptorParser = new DatabaseDescriptorReader(databaseDescriptorPath);

                DatabaseDescriptor databaseDescriptor = databaseDescriptorParser.GetDatabaseDescriptor();
                if (databaseDescriptor == null)
                {
                    Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
                    throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
                }

                coreResourceManager.GetApplicationDescriptor().AddDatabaseDescriptor(databaseDescriptorPath, databaseDescriptor);
            }
        }



        /// <summary>
        /// It process all LibraryDescriptor.xml files defined by application, and stores in Resource Manager.
        /// </summary>
        protected static void ProcessLibraries()
        {

            ApplicationDescriptor applicationDescriptor = coreResourceManager.GetApplicationDescriptor();
            IEnumerator<String> libraries = applicationDescriptor.GetLibraryDescriptorPaths();

            while (libraries.MoveNext())
            {

                String library = libraries.Current;

                /*
                 * Parse LibraryDescriptor.
                 */
                LibraryDescriptorReader libraryDescriptorReader = new LibraryDescriptorReader(library);
                LibraryDescriptor libraryDescriptor = libraryDescriptorReader.GetLibraryDescriptor();


                /*
                 * Map Entity Descriptors
                 */
                IEnumerator<String> entityDescriptors = libraryDescriptor.GetEntityDescriptorPaths();
                while (entityDescriptors.MoveNext())
                {

                    String libraryEntityDescriptorPath = entityDescriptors.Current;

                    String databaseDescriptorName = libraryEntityDescriptorPath.Substring(0, libraryEntityDescriptorPath.IndexOf(Constants.LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR));
                    String entityDescriptor = libraryEntityDescriptorPath.Substring(libraryEntityDescriptorPath.IndexOf(Constants.LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR) + 1, ((libraryEntityDescriptorPath.Length - libraryEntityDescriptorPath.IndexOf(Constants.LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR_SEPRATOR)) - 1));


                    IEnumerator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.GetDatabaseDescriptors();
                    while (databaseDescriptors.MoveNext())
                    {

                        DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                        if (databaseDescriptor.GetDatabaseName().Equals(databaseDescriptorName, StringComparison.OrdinalIgnoreCase))
                        {
                            databaseDescriptor.AddEntityDescriptorPath(library.Replace(".", "/") + FileUtils.Separator + entityDescriptor);
                        }
                    }
                }
            }
        }




        /// <summary>
        /// It process all EntityDescriptor.xml file defined in Application, and stores in Resource Manager.
        /// </summary>
        protected static void ProcessEntityDescriptors()
        {
            DoesDatabaseExists();

            ApplicationDescriptor applicationDescriptor = coreResourceManager.GetApplicationDescriptor();

            IEnumerator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {

                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                IEnumerator<String> entityDescriptorPaths = databaseDescriptor.GetEntityDescriptorPaths();

                while (entityDescriptorPaths.MoveNext())
                {
                    String entityDescriptorPath = entityDescriptorPaths.Current;
                    EntityDescriptorReader entityDescriptorParser = new EntityDescriptorReader(entityDescriptorPath);

                    databaseDescriptor.AddEntityDescriptor(entityDescriptorPath, entityDescriptorParser.GetEntityDescriptor());
                }
            }
        }



        /// <summary>
        /// It process all DatabaseDescriptor.xml and initialize Database and stores in Resource Manager.
        /// </summary>
        protected static void ProcessDatabase()
        {

            ApplicationDescriptor applicationDescriptor = coreResourceManager.GetApplicationDescriptor();
            IEnumerator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.GetDatabaseDescriptors();

            while (databaseDescriptors.MoveNext())
            {

                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;

                String databasePath = new DatabaseUtils().GetDatabasePath(databaseDescriptor);
                DatabaseBundle databaseBundle = null;

                try
                {
                    databaseBundle = DatabaseHelper.CreateDatabase(databaseDescriptor);
                }
                catch (DatabaseException databaseException)
                {
                    Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while getting database instance from database factory, DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName() + ", " + databaseException.GetMessage());
                    throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                }

                IDatabaseImpl database = databaseBundle.GetDatabase();
                IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();



                /*
                 * If Database exists then open and return.
                 * If Database does not exists create the database.
                 */

                String databaseName = databaseDescriptor.GetDatabaseName();
                if (!databaseName.EndsWith(".db"))
                {
                    databaseName = databaseName + ".db";
                }


                bool fileExists = FileUtils.DoesFileExists(databasePath, databaseName, FileUtils.LOCAL_FOLDER);
                if (fileExists)
                {

                    /*
                     * Open Database
                     */
                    try
                    {
                        database.OpenOrCreate(databaseDescriptor);
                    }
                    catch (DatabaseException databaseException)
                    {
                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while opening database, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }


                    /*
                     * Enable Foreign Key Constraints
                     */
                    try
                    {
                        database.ExecuteQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DeleteFile(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }


                    /*
                     * Safe MultiThread Transaction
                     */
                    /*try
                    {
                        database.ExecuteMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.IsTransactionSafe());
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DoesFileExists(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }*/



                    /*
                     * Upgrade Database
                     */
                    try
                    {
                        DatabaseHelper.UpgradeDatabase(databaseDescriptor);
                    }
                    catch (DatabaseException databaseException)
                    {
                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while upgrading database, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }
                }
                else
                {

                    /*
                     * Create Database Directory
                     */
                    try
                    {
                        FileUtils.GetFolder(databasePath, FileUtils.LOCAL_FOLDER);
                    }
                    catch (System.Exception exception)
                    {
                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName() + ", " + exception.Message);
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", exception.Message);
                    }


                    /*
                     * Create Database File.
                     */
                    try
                    {
                        database.OpenOrCreate(databaseDescriptor);
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DeleteFile(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while creating database, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }


                    /*
                     * Set Database Version
                     */
                    IDictionary<String, Object> parameters = new Dictionary<String, Object>();
                    parameters.Add(IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER, databaseDescriptor.GetVersion());


                    try
                    {

                        String updateDatabaseVersionQuery = queryBuilder.FormUpdateDatabaseVersionQuery(parameters);
                        database.ExecuteQuery(databaseDescriptor, null, updateDatabaseVersionQuery);
                    }
                    catch (DatabaseException databaseException)
                    {
                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "Database Exception caught while updating database version, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }



                    IDatabaseEvents databaseEventHandler = coreResourceManager.GetDatabaseEventHandler();
                    if (databaseEventHandler != null)
                    {
                        databaseEventHandler.OnDatabaseCreated(databaseDescriptor);
                    }


                    /*
                     * Enable Foreign Key Constraints
                     */
                    try
                    {
                        database.ExecuteQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DeleteFile(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }


                    /*
                     * Safe MultiThread Transaction
                     */
                    /*try
                    {
                        database.ExecuteMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.IsTransactionSafe());
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DeleteFile(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }*/



                    /*
                     * Create Tables
                     */
                    try
                    {
                        DatabaseHelper.CreateTables(databaseDescriptor.OrderedEntityDescriptors());
                    }
                    catch (DatabaseException databaseException)
                    {
                        FileUtils.DeleteFile(databasePath, databaseDescriptor.GetDatabaseName(), FileUtils.LOCAL_FOLDER);

                        Log.Log.Error(typeof(Siminov).FullName, "ProcessDatabase", "DatabaseException caught while creating tables, " + databaseException.GetMessage());
                        throw new DeploymentException(typeof(Siminov).FullName, "ProcessDatabase", databaseException.GetMessage());
                    }

                }

            }
        }



        /// <summary>
        /// It is used to check whether database exists or not. 
        /// </summary>
        protected static void DoesDatabaseExists()
        {

            ApplicationDescriptor applicationDescriptor = coreResourceManager.GetApplicationDescriptor();
            IEnumerator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.GetDatabaseDescriptors();

            bool databaseExists = true;
            while (databaseDescriptors.MoveNext())
            {

                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                String databasePath = new DatabaseUtils().GetDatabasePath(databaseDescriptor);

                String databaseName = databaseDescriptor.GetDatabaseName();
                if (!databaseName.EndsWith(".db"))
                {
                    databaseName = databaseName + ".db";
                }


                bool fileExists = FileUtils.DoesFileExists(databasePath, databaseName, FileUtils.LOCAL_FOLDER);
                if (!fileExists)
                {
                    databaseExists = false;
                }
            }

            if (!databaseExists)
            {
                firstTimeProcessed = true;
            }
            else
            {
                firstTimeProcessed = false;
            }
        }

    }
}
