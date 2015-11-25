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
    /// Exposes methods to parse Application Descriptor information as per define in ApplicationDescriptor.xml file by application.
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
    public class ApplicationDescriptorReader : SiminovSAXDefaultHandler
    {

        private ApplicationDescriptor applicationDescriptor = null;

        private ResourceManager resourceManager = ResourceManager.GetInstance();

        private StringBuilder tempValue = new StringBuilder();
        private String propertyName = null;


        /// <summary>
        /// ApplicationDescriptorReader Constructor
        /// </summary>
        public ApplicationDescriptorReader()
        {

            /*
             * Parse ApplicationDescriptor.
             */
            Stream applicationDescriptorStream = null;

            try
            {
                #if XAMARIN
                applicationDescriptorStream = FileUtils.ReadFileFromEmbeddedResources("Assets." + Constants.APPLICATION_DESCRIPTOR_FILE_NAME);
                #elif WINDOWS
                applicationDescriptorStream = FileUtils.ReadFile("Assets", Constants.APPLICATION_DESCRIPTOR_FILE_NAME, FileUtils.INSTALLED_FOLDER);
                #endif

            }
            catch (System.Exception ioException)
            {
                Log.Log.Error(typeof(ApplicationDescriptorReader).FullName, "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.Message);
                throw new DeploymentException(typeof(ApplicationDescriptorReader).FullName, "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.Message);
            }

            try
            {
                ParseMessage(applicationDescriptorStream);
            }
            catch (SiminovException exception)
            {
                Log.Log.Error(typeof(ApplicationDescriptorReader).FullName, "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.GetMessage());
                throw new DeploymentException(typeof(ApplicationDescriptorReader).FullName, "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.GetMessage());
            }

            DoValidation();
        }

        public override void StartElement(XmlReader reader, IDictionary<String, String> attributes)
        {

            String localName = reader.Name;
            tempValue = new StringBuilder();

            if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_SIMINOV, StringComparison.OrdinalIgnoreCase))
            {
                applicationDescriptor = new ApplicationDescriptor();
            }
            else if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_PROPERTY))
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

            if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {
                applicationDescriptor.AddProperty(propertyName, tempValue.ToString());
            }
            else if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {
                applicationDescriptor.AddDatabaseDescriptorPath(tempValue.ToString());
            }
            else if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_EVENT_HANDLER, StringComparison.OrdinalIgnoreCase))
            {

                if (tempValue == null || tempValue.Length <= 0)
                {
                    return;
                }

                applicationDescriptor.AddEvent(tempValue.ToString());
            }
            else if (localName.Equals(Constants.APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {

                if (tempValue == null || tempValue.Length <= 0)
                {
                    return;
                }

                applicationDescriptor.AddLibraryDescriptorPath(tempValue.ToString());
            }
        }

        private void InitializeProperty(IDictionary<String, String> attributes)
        {
            propertyName = attributes[Constants.APPLICATION_DESCRIPTOR_NAME];
        }

        private void DoValidation()
        {

            /*
             * Validate Application Name field.
             */
            String name = applicationDescriptor.GetName();
            if (name == null || name.Length <= 0)
            {
                Log.Log.Error(typeof(ApplicationDescriptorReader).FullName, "DoValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
                throw new DeploymentException(typeof(ApplicationDescriptorReader).FullName, "DoValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
            }
        }


        /// <summary>
        /// Get application descriptor object. 
        /// </summary>
        /// <returns>Application Descriptor Object</returns>
        public ApplicationDescriptor GetApplicationDescriptor()
        {
            return this.applicationDescriptor;
        }

    }
}
