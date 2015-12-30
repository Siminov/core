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
    /// Exposes methods to parse Library Descriptor information as per define in LibraryDescriptor.xml file by application.
    /// <para>
    /// Example:
    /// <code>
    ///        <library-descriptor>
    ///
    ///            <!-- General Properties Of Library -->
    ///    
    ///            <!-- Mandatory Field -->
    ///            <property name="name">name_of_library</property>
    ///	
    ///            <!-- Optional Field -->
    ///            <property name="description">description_of_library</property>
    ///
    ///	
    ///	
    ///            <!-- Entity Descriptors Needed Under This Library Descriptor -->
    ///	
    ///            <!-- Optional Field -->
    ///                <!-- Entity Descriptors -->
    ///            <entity-descriptors>
    ///                <entity-descriptor>name_of_database_descriptor.full_path_of_entity_descriptor_file</entity-descriptor>
    ///            </entity-descriptors>
    ///	 
    ///        </library-descriptor>
    /// </code>
    /// </para>
    /// </summary>
    public class LibraryDescriptorReader : SiminovSAXDefaultHandler
    {

        private String libraryName = null;

        private LibraryDescriptor libraryDescriptor = null;

        private StringBuilder tempValue = new StringBuilder();
        private String propertyName = null;



        /// <summary>
        /// LibraryDescriptorReader Constructor
        /// </summary>
        /// <param name="libraryName">Name of the library</param>
        public LibraryDescriptorReader(String libraryName)
        {

            if (libraryName == null || libraryName.Length <= 0)
            {
                Log.Log.Error(typeof(LibraryDescriptorReader).FullName, "Constructor", "Invalid Library Name Found.");
                throw new DeploymentException(typeof(LibraryDescriptorReader).FullName, "Constructor", "Invalid Library Name Found.");
            }

            this.libraryName = libraryName;
            this.libraryName = this.libraryName.Replace(".", "/");


            #if XAMARIN
                Stream libraryDescriptorStream = FileUtils.ReadFileFromEmbeddedResources(this.libraryName + "." + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);
            #elif WINDOWS
                Stream libraryDescriptorStream = FileUtils.SearchFile(this.libraryName, Constants.LIBRARY_DESCRIPTOR_FILE_NAME, FileUtils.INSTALLED_FOLDER);
            #endif

            if (libraryDescriptorStream == null)
            {
                Log.Log.Error(typeof(LibraryDescriptorReader).FullName, "Constructor", "Invalid Library Descriptor Stream Found, LIBRARY-NAME: " + this.libraryName + FileUtils.Separator + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);
                throw new DeploymentException(typeof(LibraryDescriptorReader).FullName, "Constructor", "Invalid Library Descriptor Stream Found, LIBRARY-NAME: " + this.libraryName + FileUtils.Separator + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);
            }

            try
            {
                ParseMessage(libraryDescriptorStream);
            }
            catch (SiminovException exception)
            {
                Log.Log.Error(typeof(LibraryDescriptorReader).FullName, "Constructor", "Exception caught while parsing LIBRARY-DESCRIPTOR: " + this.libraryName + ", " + exception.GetMessage());
                throw new DeploymentException(typeof(LibraryDescriptorReader).FullName, "Constructor", "Exception caught while parsing LIBRARY-DESCRIPTOR: " + this.libraryName + ", " + exception.GetMessage());
            }

            DoValidation();
        }

        public override void StartElement(XmlReader reader, IDictionary<String, String> attributes)
        {

            String localName = reader.Name;
            tempValue = new StringBuilder();

            if (localName.Equals(Constants.LIBRARY_DESCRIPTOR_LIBRARY_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {
                libraryDescriptor = new LibraryDescriptor();
            }
            else if (localName.Equals(Constants.LIBRARY_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
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

            if (localName.Equals(Constants.LIBRARY_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {
                libraryDescriptor.AddProperty(propertyName, tempValue.ToString());
            }
            else if (localName.Equals(Constants.LIBRARY_DESCRIPTOR_ENTITY_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {
                libraryDescriptor.AddEntityDescriptorPath(tempValue.ToString());
            }
        }

        private void InitializeProperty(IDictionary<String, String> attributes)
        {
            propertyName = attributes[Constants.LIBRARY_DESCRIPTOR_NAME];
        }

        private void DoValidation()
        {

            /*
             * Validation for name field.
             */
            String name = libraryDescriptor.GetName();
            if (name == null || name.Length <= 0)
            {
                Log.Log.Error(typeof(LibraryDescriptorReader).FullName, "DoValidation", "LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: " + this.libraryName);
                throw new DeploymentException(typeof(LibraryDescriptorReader).FullName, "DoValidation", "LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: " + this.libraryName);
            }
        }


        /// <summary>
        /// Get library descriptor object.
        /// </summary>
        /// <returns>Library Descriptor Object</returns>
        public LibraryDescriptor GetLibraryDescriptor()
        {
            return this.libraryDescriptor;
        }

    }
}
