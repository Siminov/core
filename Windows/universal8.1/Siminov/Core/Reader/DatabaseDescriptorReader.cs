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


#if __MOBILE__
#define XAMARIN
#endif

#if !__MOBILE__
#define WINDOWS
#endif


using Siminov.Core.Utils;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace Siminov.Core.Reader
{


    /// <summary>
    /// Exposes methods to parse Database Descriptor information as per define in DatabaseDescriptor.xml file by application.
    /// <para>
    /// Example:
    /// <code>
    ///        <database-descriptor>
    ///
    ///            <!-- General Database Descriptor Properties -->
    ///    
    ///                <!-- Mandatory Field -->
    ///            <property name="database_name">name_of_database_file</property>
    ///
    ///                <!-- Optional Field (Default is sqlite)-->
    ///            <property name="type">type_of_database</property>
    ///
    ///                <!-- Mandatory Field -->
    ///            <property name="version">database_version</property>
    ///			
    ///                <!-- Optional Field -->
    ///            <property name="description">database_description</property>
    ///
    ///                <!-- Optional Field (Default is false) -->
    ///            <property name="transaction_safe">true/false</property>
    ///	
    ///                <!-- Optional Field (Default is false) -->
    ///            <property name="external_storage">true/false</property>
    ///
    ///            <!-- Entity Descriptor Paths Needed Under This Database Descriptor -->
    ///	
    ///                <!-- Optional Field -->
    ///            <entity-descriptors>
    ///                <entity-descriptor>full_path_of_entity_descriptor_file</entity-descriptor>
    ///            </entity-descriptors>
    ///	
    ///        </database-descriptor>
    /// </code>
    /// </para>
    /// </summary>
    public class DatabaseDescriptorReader : SiminovSAXDefaultHandler
    {

        private String databaseDescriptorPath = null;

        private DatabaseDescriptor databaseDescriptor = null;

        private ResourceManager resourceManager = ResourceManager.GetInstance();

        private StringBuilder tempValue = new StringBuilder();
        private String propertyName = null;



        /// <summary>
        /// DatabaseDescriptorReader Constructor
        /// </summary>
        /// <param name="databaseDescriptorPath">Path of the database descriptor</param>
        public DatabaseDescriptorReader(String databaseDescriptorPath)
        {

            if (databaseDescriptorPath == null || databaseDescriptorPath.Length <= 0)
            {
                Log.Log.Error(typeof(DatabaseDescriptorReader).FullName, "Constructor", "Invalid Database Descriptor path found.");
                throw new DeploymentException(typeof(DatabaseDescriptorReader).FullName, "Constructor", "Invalid Database Descriptor path found.");
            }

            this.databaseDescriptorPath = databaseDescriptorPath;

            /*
             * Parse ApplicationDescriptor.
             */
            Stream databaseDescriptorStream = null;

            try
            {

                //databaseDescriptorStream = getClass().getClassLoader().getResourceAsStream(this.databaseDescriptorPath);
                //if(databaseDescriptorStream == null) 
                //{

                #if XAMARIN
                databaseDescriptorStream = FileUtils.ReadFileFromEmbeddedResources("Assets." + this.databaseDescriptorPath);                
                #elif WINDOWS
                databaseDescriptorStream = FileUtils.ReadFile("Assets", this.databaseDescriptorPath, FileUtils.INSTALLED_FOLDER);
                #endif
                //}
            }
            catch (System.Exception ioException)
            {
                Log.Log.Error(typeof(DatabaseDescriptorReader).FullName, "Constructor", "IOException caught while getting input stream of database descriptor, DATABASE-DESCRIPTOR-PATH: " + databaseDescriptorPath + ", " + ioException.Message);

                try
                {
                    String entityDescriptorFileName = null;
                    String entityDescriptorFilePath = null;

                    entityDescriptorFilePath = this.databaseDescriptorPath.Substring(0, databaseDescriptorPath.LastIndexOf("/"));
                    entityDescriptorFileName = this.databaseDescriptorPath.Substring(this.databaseDescriptorPath.LastIndexOf("/") + 1, (this.databaseDescriptorPath.Length - this.databaseDescriptorPath.LastIndexOf("/")) - 1);

                    databaseDescriptorStream = FileUtils.SearchFile(entityDescriptorFilePath, entityDescriptorFileName, FileUtils.INSTALLED_FOLDER);
                }
                catch (System.Exception exception)
                {
                    Log.Log.Error(typeof(DatabaseDescriptorReader).FullName, "Constructor", "Exception caught while getting database descriptor file stream, " + exception.Message);
                    throw new DeploymentException(typeof(DatabaseDescriptorReader).FullName, "Constructor", exception.Message);
                }
            }


            try
            {
                ParseMessage(databaseDescriptorStream);
            }
            catch (SiminovException exception)
            {
                Log.Log.Error(typeof(DatabaseDescriptorReader).FullName, "Constructor", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + exception.GetMessage());
                throw new DeploymentException(typeof(DatabaseDescriptorReader).FullName, "Constructor", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + exception.GetMessage());
            }

            DoValidation();
        }

        public override void StartElement(XmlReader reader, IDictionary<String, String> attributes)
        {
            String localName = reader.Name;
            tempValue = new StringBuilder();

            if (localName.Equals(Constants.DATABASE_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {
                databaseDescriptor = new DatabaseDescriptor();
            }
            else if (localName.Equals(Constants.DATABASE_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {
                InitializeProperty(attributes);
            }
        }

        public override void Characters(String value)
        {

            if (value == null || value.Length <= 0 || value.Equals(Constants.NEW_LINE, StringComparison.OrdinalIgnoreCase))
            {
                return;
            }

            value = value.Trim();
            tempValue.Append(value);
        }

        public override void EndElement(String localName)
        {

            if (localName.Equals(Constants.DATABASE_DESCRIPTOR_PROPERTY))
            {
                databaseDescriptor.AddProperty(propertyName, tempValue.ToString());
            }
            else if (localName.Equals(Constants.DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR))
            {
                databaseDescriptor.AddEntityDescriptorPath(tempValue.ToString());
            }
        }

        private void InitializeProperty(IDictionary<String, String> attributes)
        {
            propertyName = attributes[Constants.DATABASE_DESCRIPTOR_PROPERTY_NAME];
        }

        private void DoValidation()
        {


        }


        /// <summary>
        /// Get database descriptor object.
        /// </summary>
        /// <returns>Database Descriptor Object</returns>
        public DatabaseDescriptor GetDatabaseDescriptor()
        {
            if (databaseDescriptor.GetType() == null || databaseDescriptor.GetType().Length <= 0)
            {
                databaseDescriptor.SetType(Constants.SQLITE_DATABASE);
            }

            return this.databaseDescriptor;
        }

    }
}
