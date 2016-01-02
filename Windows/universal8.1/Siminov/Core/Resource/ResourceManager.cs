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


using Siminov.Core.Database;
using Siminov.Core.Events;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using Siminov.Core.Reader;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Resource
{


    /// <summary>
    /// It handles and provides all resources needed by SIMINOV.
    /// Such As: Provides Application Descriptor, Database Descriptor, Library Descriptor, Entity Descriptor.
    /// </summary>
    public class ResourceManager
    {

        /*
         * Resources.
         */
        //private Context applicationContext = null;

        private ApplicationDescriptor applicationDescriptor = null;
        private DatabaseFactory databaseFactory = null;

        private static ResourceManager resources = null;

        protected ICollection<Object> applicationContexts = new LinkedList<Object>();

        /// <summary>
        /// Resource Private Constructor
        /// </summary>
        private ResourceManager()
        {
            databaseFactory = DatabaseFactory.GetInstance();
        }


        /// <summary>
        /// Get Application Contexts
        /// </summary>
        /// <returns>Application Contexts</returns>
        public IEnumerator<Object> GetApplicationContexts()
        {
            return this.applicationContexts.GetEnumerator();
        }


        /// <summary>
        /// Add Application Context
        /// </summary>
        /// <param name="applicationContext">Application Context</param>
        public void AddApplicationContext(Object applicationContext)
        {
            this.applicationContexts.Add(applicationContext);
        }

        /// <summary>
        /// It provides an singleton instance of ResourceManager class.
        /// </summary>
        /// <returns>Resources instance</returns>
        public static ResourceManager GetInstance()
        {
            if (resources == null)
            {
                resources = new ResourceManager();
            }

            return resources;
        }



        /// <summary>
        /// Get Application Descriptor object of application.
        /// </summary>
        /// <returns>Application Descriptor</returns>
        public ApplicationDescriptor GetApplicationDescriptor()
        {
            return this.applicationDescriptor;
        }


        /// <summary>
        /// Set Application Descriptor of application.
        /// </summary>
        /// <param name="applicationDescriptor">Application Descriptor object</param>
        public void SetApplicationDescriptor(ApplicationDescriptor applicationDescriptor)
        {
            this.applicationDescriptor = applicationDescriptor;
        }


        /// <summary>
        /// Get iterator of all database descriptors provided in Application Descriptor file.
        /// <para>
        /// Example: ApplicationDescriptor.xml
        /// <code>
        ///        <siminov>
        ///	
        ///            <database-descriptors>
        ///                <database-descriptor>DatabaseDescriptor.xml</database-descriptor>
        ///            </database-descriptors>
        ///
        ///        </siminov>
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>Iterator which contains all database descriptor paths provided.</returns>
        public IEnumerator<String> GetDatabaseDescriptorPaths()
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorsPaths", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
            }

            return this.applicationDescriptor.GetDatabaseDescriptorPaths();
        }

        /// <summary>
        /// Get DatabaseDescriptor based on path provided as per defined in Application Descriptor file.
        /// <para>
        /// Example: ApplicationDescriptor.xml
        /// <code>
        ///        <siminov>
        ///	
        ///            <database-descriptors>
        ///                <database-descriptor>DatabaseDescriptor.xml</database-descriptor>
        ///            </database-descriptors>
        ///
        ///        </siminov>
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptorPath">Iterator which contains all database descriptor paths provided.</param>
        /// <returns>Database Descriptor</returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnPath(String databaseDescriptorPath)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorBasedOnPath", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
            }

            return this.applicationDescriptor.GetDatabaseDescriptorBasedOnPath(databaseDescriptorPath);
        }


        /// <summary>
        /// Get Database Descriptor based on database descriptor name provided as per defined in Database Descriptor file.
        /// <para>
        /// Example: DatabaseDescriptor.xml
        /// <code>
        ///        <database-descriptor>
        ///
        ///            <property name="database_name">SIMINOV-CORE-SAMPLE</property>
        ///
        ///        </database-descriptor>
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptorName">Database Descriptor object based on database descriptor name provided.</param>
        /// <returns></returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnName(String databaseDescriptorName)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorBasedOnName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
            }

            return this.applicationDescriptor.GetDatabaseDescriptorBasedOnName(databaseDescriptorName);
        }


        /// <summary>
        /// Get all Database Descriptors object.
        /// </summary>
        /// <returns>Iterator which contains all Database Descriptors.</returns>
        public IEnumerator<DatabaseDescriptor> GetDatabaseDescriptors()
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptors", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            return this.applicationDescriptor.GetDatabaseDescriptors();
        }


        /// <summary>
        /// Get Database Descriptor based on CSharp class name provided.
        /// </summary>
        /// <param name="className">CSharp class name</param>
        /// <returns>Database Descriptor object in respect to CSharp class name.</returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnClassName(String className)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnClassName(className);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor;
                }
            }

            return null;
        }



        /// <summary>
        /// Get database descriptor name based on class name
        /// </summary>
        /// <param name="className">Name of Class</param>
        /// <returns>Database Descriptor Name</returns>
        public String GetDatabaseDescriptorNameBasedOnClassName(String className)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorNameBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnClassName(className);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor.GetDatabaseName();
                }
            }

            return null;
        }



        /// <summary>
        /// Get Database Descriptor based on table name provided.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        /// <returns>Database Descriptor object in respect to table name.</returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnTableName(String tableName)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnTableName(tableName);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor;
                }
            }

            return null;
        }


        /// <summary>
        /// Get database descriptor name based on table name
        /// </summary>
        /// <param name="tableName">Name of Table</param>
        /// <returns>Database Descriptor Name</returns>
        public String GetDatabaseDescriptorNameBasedOnTableName(String tableName)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetDatabaseDescriptorNameBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnTableName(tableName);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor.GetDatabaseName();
                }
            }

            return null;
        }

        /// <summary>
        /// Get Entity Descriptor based on mapped class name provided.
        /// </summary>
        /// <param name="className">CSharp class name</param>
        /// <returns>Entity Descriptor object in respect to mapped class name.</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnClassName(String className)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetEntityDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnClassName(className);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor.GetEntityDescriptorBasedOnClassName(className);
                }
            }

            return null;
        }


        /// <summary>
        /// Get Entity Descriptor based on table name provided.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        /// <returns>Entity Descriptor object in respect to table name.</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnTableName(String tableName)
        {
            if (this.applicationDescriptor == null)
            {
                throw new DeploymentException(typeof(ResourceManager).FullName, "GetEntityDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
            }

            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                bool containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.ContainsEntityDescriptorBasedOnTableName(tableName);

                if (containsEntityDescriptorInDatabaseDescriptor)
                {
                    return databaseDescriptor.GetEntityDescriptorBasedOnTableName(tableName);
                }
            }

            return null;
        }


        /// <summary>
        /// Get all entity descriptors
        /// </summary>
        /// <returns>Entity Descriptors</returns>
        public IEnumerator<EntityDescriptor> GetEntityDescriptors()
        {
            ICollection<EntityDescriptor> entityDescriptors = new LinkedList<EntityDescriptor>();
            IEnumerator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.GetDatabaseDescriptors();

            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;

                IEnumerator<EntityDescriptor> descriptors = databaseDescriptor.GetEntityDescriptors();
                while (descriptors.MoveNext())
                {
                    EntityDescriptor entityDescriptor = descriptors.Current;
                    entityDescriptors.Add(entityDescriptor);
                }
            }

            return entityDescriptors.GetEnumerator();
        }


        /// <summary>
        /// Get entity descriptor Object based on class name provided. If entity descriptor object not present in resource layer, it will parse EntityDescriptor.xml file defined by application and will place it in resource layer.
        /// </summary>
        /// <param name="className">Full name of class.</param>
        /// <returns>EntityDescriptor object</returns>
        /// <exception cref="Siminov.Core.Exception.SiminovException">If any exception occur while getting entity descriptor object.</exception>
        public EntityDescriptor RequiredEntityDescriptorBasedOnClassName(String className)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptorBasedOnClassName(className);

            if (entityDescriptor == null)
            {
                Log.Log.Debug(typeof(ResourceManager).FullName, "RequiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, MODEL: " + className);

                QuickEntityDescriptorReader quickEntityDescriptorParser = null;
                try
                {
                    quickEntityDescriptorParser = new QuickEntityDescriptorReader(className);
                    quickEntityDescriptorParser.Process();
                }
                catch (SiminovException ce)
                {
                    Log.Log.Error(typeof(ResourceManager).FullName, "RequiredEntityDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: " + className + ", " + ce.GetMessage());
                    throw new SiminovCriticalException(typeof(ResourceManager).FullName, "RequiredEntityDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: " + className + ", " + ce.GetMessage());
                }

                EntityDescriptor foundEntityDescriptor = quickEntityDescriptorParser.GetEntityDescriptor();
                if (foundEntityDescriptor == null)
                {
                    Log.Log.Error(typeof(ResourceManager).FullName, "RequiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: " + className);
                    throw new SiminovCriticalException(typeof(ResourceManager).FullName, "RequiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: " + className);
                }

                return foundEntityDescriptor;
            }

            return entityDescriptor;
        }




        /// <summary>
        /// Get IDatabase object based on Database Descriptor name.
        /// </summary>
        /// <param name="databaseName">Name of Database Descriptor</param>
        /// <returns>IDatabase object</returns>
        public DatabaseBundle GetDatabaseBundle(String databaseName)
        {

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptorBasedOnName(databaseName);
            return this.databaseFactory.GetDatabaseBundle(databaseDescriptor);
        }

        /// <summary>
        /// Get all IDatabase objects contain by application.
        /// </summary>
        /// <returns>Iterator which contains all IDatabase objects</returns>
        public IEnumerator<DatabaseBundle> GetDatabaseBundles()
        {
            return this.databaseFactory.GetDatabaseBundles();
        }


        /// <summary>
        /// Remove IDatabase object from Resources based on Database Descriptor name.
        /// </summary>
        /// <param name="databaseDescriptorName">Database Descriptor name</param>
        public void RemoveDatabaseBundle(String databaseDescriptorName)
        {

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptorBasedOnName(databaseDescriptorName);
            this.databaseFactory.RemoveDatabaseBundle(databaseDescriptor);
        }


        /// <summary>
        /// Get SIMINOV-EVENT Handler
        /// </summary>
        /// <returns>ISiminovEvents implementation object as per defined by application.</returns>
        public ISiminovEvents GetSiminovEventHandler()
        {
            return Events.EventHandler.GetInstance().GetSiminovEventHandler();
        }


        /// <summary>
        /// Get DATABASE-EVENT Handler
        /// </summary>
        /// <returns>IDatabaseEvents implementation object as per defined by application.</returns>
        public IDatabaseEvents GetDatabaseEventHandler()
        {
            return Events.EventHandler.GetInstance().GetDatabaseEventHandler();
        }

    }
}
