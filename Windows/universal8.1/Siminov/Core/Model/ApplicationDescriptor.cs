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



using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Model
{

    /// <summary>
    /// Exposes methods to GET and SET Application Descriptor information as per define in ApplicationDescriptor.xml file by application.
    /// <para>
    /// Example:
    /// <code>
    ///        <siminov>
    ///    
    ///            <!-- General Application Description Properties -->
    ///	
    ///                <!-- Mandatory Field -->
    ///            <property name="name">application_name</property>	
    ///	
    ///                <!-- Optional Field -->
    ///            <property name="description">application_description</property>
    ///	
    ///                <!-- Mandatory Field (Default is 0.0) -->
    ///            <property name="version">application_version</property>
    ///
    ///
    ///            <!-- Database Descriptors Used By Application (zero-to-many) -->	
    ///                <!-- Optional Field's -->
    ///            <database-descriptors>
    ///                <database-descriptor>full_path_of_database_descriptor_file</database-descriptor>
    ///            </database-descriptors>
    ///		
    ///
    ///            <!-- Library Descriptors Used By Application (zero-to-many) -->
    ///                <!-- Optional Field's -->
    ///            <library-descriptors>
    ///                <library-descriptor>full_path_of_library_descriptor_file</library-descriptor>   
    ///            </library-descriptors>
    ///	
    ///		
    ///            <!-- Event Handlers Implemented By Application (zero-to-many) -->
    ///	
    ///                <!-- Optional Field's -->
    ///            <event-handlers>
    ///                <event-handler>full_class_path_of_event_handler_(ISiminovHandler/IDatabaseHandler)</event-handler>
    ///            </event-handlers>
    ///
    ///        </siminov>
    /// </code>
    /// </para>
    /// </summary>
    public class ApplicationDescriptor : IDescriptor
    {

        protected IDictionary<String, String> properties = new Dictionary<String, String>();

        protected ICollection<String> databaseDescriptorPaths = new LinkedList<String>();
        protected IDictionary<String, DatabaseDescriptor> databaseDescriptorsBasedOnName = new Dictionary<String, DatabaseDescriptor>();
        protected IDictionary<String, DatabaseDescriptor> databaseDescriptorsBasedOnPath = new Dictionary<String, DatabaseDescriptor>();

        protected ICollection<String> events = new LinkedList<String>();

        protected ICollection<String> libraryDescriptorPaths = new LinkedList<String>();


        /// <summary>
        /// Get Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <returns>Application Descriptor Name</returns>
        public String GetName()
        {
            if (this.properties.ContainsKey(Constants.APPLICATION_DESCRIPTOR_NAME))
            {
                return this.properties[Constants.APPLICATION_DESCRIPTOR_NAME];
            }

            return null;
        }

        /// <summary>
        /// Set Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <param name="name">Name of Application Descriptor</param>
        public void SetName(String name)
        {
            this.properties.Add(Constants.APPLICATION_DESCRIPTOR_NAME, name);
        }

        /// <summary>
        /// Set Description of Application as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <returns>Description of application</returns>
        public String GetDescription()
        {
            if (this.properties.ContainsKey(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION))
            {
                return this.properties[Constants.APPLICATION_DESCRIPTOR_DESCRIPTION];
            }

            return null;
        }

        /// <summary>
        /// Set Description of Application as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <param name="description">Description of application</param>
        public void SetDescription(String description)
        {
            this.properties.Add(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION, description);
        }

        /// <summary>
        /// Get Version of Application as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <returns>Version of application</returns>
        public double GetVersion()
        {
            if (this.properties.ContainsKey(Constants.APPLICATION_DESCRIPTOR_VERSION))
            {

                String version = this.properties[Constants.APPLICATION_DESCRIPTOR_VERSION];
                if (version == null || version.Length <= 0)
                {
                    return 0.0;
                }

                return Double.Parse(version);
            }

            return 0;
        }

        /// <summary>
        /// Set Version of Application as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <param name="version">Version of application</param>
        public void SetVersion(double version)
        {
            this.properties.Add(Constants.APPLICATION_DESCRIPTOR_VERSION, Convert.ToString(version));
        }

        /// <summary>
        /// Get all Properties defined in descriptor.
        /// </summary>
        /// <returns>All Property Values</returns>
        public IEnumerator<String> GetProperties()
        {
            return this.properties.Keys.GetEnumerator();
        }

        /// <summary>
        /// Get Property based on name provided.
        /// </summary>
        /// <param name="name">Name of Property</param>
        /// <returns>Property value</returns>
        public String GetProperty(String name)
        {
            if (this.properties.ContainsKey(name))
            {
                return this.properties[name];
            }

            return null;
        }

        /// <summary>
        /// Check whether Property exist or not.
        /// </summary>
        /// <param name="name">Name of Property</param>
        /// <returns>true/false, TRUE if property exist, FALSE if property does not exist.</returns>
        public bool ContainProperty(String name)
        {
            return this.properties.ContainsKey(name);
        }

        /// <summary>
        /// Add Property in property pool.
        /// </summary>
        /// <param name="name">Name of Property</param>
        /// <param name="value">value of Property</param>
        public void AddProperty(String name, String value)
        {
            this.properties.Add(name, value);
        }

        /// <summary>
        /// Remove Property from property pool.
        /// </summary>
        /// <param name="name">Name of Property</param>
        public void RemoveProperty(String name)
        {
            if (this.properties.ContainsKey(name))
            {
                this.properties.Remove(name);
            }
        }

        /// <summary>
        /// Check whether database needed by application or not.
        /// </summary>
        /// <returns>TRUE: If database needed by application, FALSE: If database is not needed by application.</returns>
        public bool IsDatabaseNeeded()
        {
            return this.databaseDescriptorPaths.Count() > 0 ? true : false;
        }

        /// <summary>
        /// Check whether database descriptor exists in Resources or not.
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor object</param>
        /// <returns>TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.</returns>
        public bool ContainsDatabaseDescriptor(DatabaseDescriptor databaseDescriptor)
        {
            return this.databaseDescriptorsBasedOnName.Values.Contains(databaseDescriptor);
        }

        /// <summary>
        /// Check whether database descriptor exists in Resources or not, based on database descriptor path.
        /// </summary>
        /// <param name="containDatabaseDescriptorPath">Database Descriptor path</param>
        /// <returns>TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.</returns>
        public bool ContainsDatabaseDescriptorBasedOnPath(String containDatabaseDescriptorPath)
        {
            return this.databaseDescriptorsBasedOnPath.ContainsKey(containDatabaseDescriptorPath);
        }

        /// <summary>
        /// Check whether database descriptor exists in Resources or not, based on Database Descriptor name.
        /// </summary>
        /// <param name="databaseDescriptorName">Database Descriptor Name</param>
        /// <returns>TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.</returns>
        public bool ContainsDatabaseDescriptorBasedOnName(String databaseDescriptorName)
        {
            return this.databaseDescriptorsBasedOnName.ContainsKey(databaseDescriptorName);
        }

        /// <summary>
        /// Get Database Descriptor based on Database Descriptor Name.
        /// </summary>
        /// <param name="databaseDescriptorName">Database Desciptor Name</param>
        /// <returns>Database Descriptor Object</returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnName(String databaseDescriptorName)
        {
            if (this.databaseDescriptorsBasedOnName.ContainsKey(databaseDescriptorName))
            {
                return this.databaseDescriptorsBasedOnName[databaseDescriptorName];
            }

            return null;
        }

        /// <summary>
        /// Get Database Descriptor based on Database Descriptor Path.
        /// </summary>
        /// <param name="databaseDescriptorPath">Database Descriptor Path</param>
        /// <returns>Database Descriptor Object</returns>
        public DatabaseDescriptor GetDatabaseDescriptorBasedOnPath(String databaseDescriptorPath)
        {
            if (this.databaseDescriptorsBasedOnPath.ContainsKey(databaseDescriptorPath))
            {
                return this.databaseDescriptorsBasedOnPath[databaseDescriptorPath];
            }

            return null;
        }

        /// <summary>
        /// Get all database descriptor paths as per contained in ApplicationDescriptor.xml file.
        /// </summary>
        /// <returns>Iterator which contains all database descriptor paths</returns>
        public IEnumerator<String> GetDatabaseDescriptorPaths()
        {
            return this.databaseDescriptorPaths.GetEnumerator();
        }

        /// <summary>
        /// Get all database descriptor names as per needed by application.
        /// </summary>
        /// <returns>Iterator which contains all database descriptor names</returns>
        public IEnumerator<String> GetDatabaseDescriptorNames()
        {
            return this.databaseDescriptorsBasedOnName.Keys.GetEnumerator();
        }

        /// <summary>
        /// Add Database Descriptor path as per contained in ApplicationDescriptor.xml file.
        /// </summary>
        /// <param name="databaseDescriptorPath">DatabaseDescriptor path</param>
        public void AddDatabaseDescriptorPath(String databaseDescriptorPath)
        {
            this.databaseDescriptorPaths.Add(databaseDescriptorPath);
        }

        /// <summary>
        /// Get all database descriptor objects contains by Siminov.
        /// </summary>
        /// <returns>Iterator which contains all database descriptor objects</returns>
        public IEnumerator<DatabaseDescriptor> GetDatabaseDescriptors()
        {
            return this.databaseDescriptorsBasedOnName.Values.GetEnumerator();
        }

        /// <summary>
        /// Add Database Descriptor object in respect to database descriptor path.
        /// </summary>
        /// <param name="databaseDescriptorPath">Database Descriptor Path</param>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        public void AddDatabaseDescriptor(String databaseDescriptorPath, DatabaseDescriptor databaseDescriptor)
        {
            this.databaseDescriptorsBasedOnPath.Add(databaseDescriptorPath, databaseDescriptor);
            this.databaseDescriptorsBasedOnName.Add(databaseDescriptor.GetDatabaseName(), databaseDescriptor);
        }

        /// <summary>
        /// Remove Database Descriptor from Resources based on database path provided, as per defined in ApplicationDescriptor.xml file
        /// </summary>
        /// <param name="databaseDescriptorPath">Database Descriptor Path</param>
        public void RemoveDatabaseDescriptorBasedOnPath(String databaseDescriptorPath)
        {
            this.databaseDescriptorPaths.Remove(databaseDescriptorPath);

            DatabaseDescriptor databaseDescriptor = this.databaseDescriptorsBasedOnPath[databaseDescriptorPath];
            this.databaseDescriptorsBasedOnPath.Remove(databaseDescriptorPath);

            this.databaseDescriptorsBasedOnName.Values.Remove(databaseDescriptor);
        }

        /// <summary>
        /// Remove Database Descriptor from Resources based in database name provided, as per defined in DatabaseDescriptor.xml file
        /// </summary>
        /// <param name="databaseDescriptorName">DatabaseDescriptor Name</param>
        public void RemoveDatabaseDescriptorBasedOnName(String databaseDescriptorName)
        {
            DatabaseDescriptor databaseDescriptor = this.databaseDescriptorsBasedOnName[databaseDescriptorName];
            ICollection<String> keys = this.databaseDescriptorsBasedOnPath.Keys;

            String keyMatched = null;
            bool found = false;
            foreach (String key in keys)
            {
                DatabaseDescriptor descriptor = this.databaseDescriptorsBasedOnPath[key];
                if (databaseDescriptor == descriptor)
                {
                    keyMatched = key;
                    found = true;
                    break;
                }
            }

            if (found)
            {
                RemoveDatabaseDescriptorBasedOnPath(keyMatched);
            }
        }

        /// <summary>
        /// Remove DatabaseDescriptor object from Resources.
        /// </summary>
        /// <param name="databaseDescriptor">DatabaseDescriptor object which needs to be removed</param>
        public void RemoveDatabaseDescriptor(DatabaseDescriptor databaseDescriptor)
        {
            RemoveDatabaseDescriptorBasedOnName(databaseDescriptor.GetDatabaseName());
        }


        /// <summary>
        /// Add library descriptor path
        /// </summary>
        /// <param name="libraryDescriptorPath">Library Descriptor Path</param>
        public void AddLibraryDescriptorPath(String libraryDescriptorPath)
        {
            this.libraryDescriptorPaths.Add(libraryDescriptorPath);
        }

        /// <summary>
        /// Get all library descriptor paths
        /// </summary>
        /// <returns>Library Descriptor Paths</returns>
        public IEnumerator<String> GetLibraryDescriptorPaths()
        {
            return this.libraryDescriptorPaths.GetEnumerator();
        }

        /// <summary>
        /// Check whether it contains library descriptor path or not
        /// </summary>
        /// <param name="libraryDescriptorPath">Path of Library Descriptor</param>
        /// <returns>(true/false) TRUE: If library descriptor path exists | FALSE: If library descriptor path does not exists.</returns>
        public bool ContainLibraryDescriptorPath(String libraryDescriptorPath)
        {
            return this.libraryDescriptorPaths.Contains(libraryDescriptorPath);
        }


        /// <summary>
        /// Remove library descriptor path
        /// </summary>
        /// <param name="libraryDescriptorPath">Path of library descriptor</param>
        public void RemoveLibraryDescriptorPath(String libraryDescriptorPath)
        {
            this.libraryDescriptorPaths.Add(libraryDescriptorPath);
        }


        /// <summary>
        /// Get all event handlers as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <returns>All event handlers defined in ApplicationDescriptor.xml file</returns>
        public IEnumerator<String> GetEvents()
        {
            return this.events.ToList().GetEnumerator();
        }

        /// <summary>
        /// Add event as per defined in ApplicationDescriptor.xml file.
        /// </summary>
        /// <param name="eventName">Event Handler class name</param>
        public void AddEvent(String eventName)
        {
            this.events.Add(eventName);
        }

        /// <summary>
        /// Remove event as per defined event name
        /// </summary>
        /// <param name="eventName">Name of the event</param>
        public void RemoveEvent(String eventName)
        {
            this.events.Remove(eventName);
        }

    }
}
