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
    /// Exposes methods to quickly parse entity descriptor defined by application.
    /// </summary>
    public class QuickEntityDescriptorReader : SiminovSAXDefaultHandler
    {

        private StringBuilder tempValue = new StringBuilder();
        private String propertyName;

        private String finalEntityDescriptorBasedOnClassName = null;

        //private Context context = null;

        private EntityDescriptor entityDescriptor = null;

        private bool doesMatch = false;

        private ResourceManager resourceManager = ResourceManager.GetInstance();


        /// <summary>
        /// QucikEntityDescriptorReader Constructor
        /// </summary>
        /// <param name="findEntityDescriptorBasedOnClassName">Name of the entity descriptor class name</param>
        /// <exception cref="Siminov.Core.Exception.SiminovException"></exception>
        public QuickEntityDescriptorReader(String findEntityDescriptorBasedOnClassName)
        {

            if (findEntityDescriptorBasedOnClassName == null || findEntityDescriptorBasedOnClassName.Length <= 0)
            {
                Log.Log.Error(typeof(QuickEntityDescriptorReader).FullName, "Constructor", "Invalid Entity Descriptor Class Name Which Needs To Be Searched.");
                throw new SiminovException(typeof(QuickEntityDescriptorReader).FullName, "Constructor", "Invalid Entity Descriptor Class Name Which Needs To Be Searched.");
            }

            this.finalEntityDescriptorBasedOnClassName = findEntityDescriptorBasedOnClassName;
        }


        /// <summary>
        /// Parse the entity descriptor descriptor defined
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.SiminovException">Any exception during parsing the descriptor file</exception>
        public void Process()
        {

            ApplicationDescriptor applicationDescriptor = resourceManager.GetApplicationDescriptor();
            if (applicationDescriptor == null)
            {
                Log.Log.Error(typeof(QuickEntityDescriptorReader).FullName, "Process", "Invalid Application Descriptor Found");
                throw new DeploymentException(typeof(QuickEntityDescriptorReader).FullName, "Process", "Invalid Application Descriptor Found.");
            }

            if (!applicationDescriptor.IsDatabaseNeeded())
            {
                doesMatch = false;
                return;
            }


            IEnumerator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.GetDatabaseDescriptors();
            while (databaseDescriptors.MoveNext())
            {
                DatabaseDescriptor databaseDescriptor = databaseDescriptors.Current;
                IEnumerator<String> entityDescriptors = databaseDescriptor.GetEntityDescriptorPaths();

                while (entityDescriptors.MoveNext())
                {
                    String entityDescriptorPath = entityDescriptors.Current;

                    Stream entityDescriptorStream = null;

                    try
                    {
                        ParseMessage(entityDescriptorStream);
                    }
                    catch (SiminovException exception)
                    {
                        Log.Log.Error(typeof(QuickEntityDescriptorReader).FullName, "Process", "Exception caught while parsing ENTITY-DESCRIPTOR: " + entityDescriptorPath + ", " + exception.GetMessage());
                        throw new SiminovException(typeof(QuickEntityDescriptorReader).FullName, "Process", "Exception caught while parsing ENTITY-DESCRIPTOR: " + entityDescriptorPath + ", " + exception.GetMessage());
                    }

                    if (doesMatch)
                    {

                        EntityDescriptorReader entityDescriptorParser = new EntityDescriptorReader(entityDescriptorPath);

                        this.entityDescriptor = entityDescriptorParser.GetEntityDescriptor();
                        databaseDescriptor.AddEntityDescriptor(entityDescriptorPath, entityDescriptor);

                        return;
                    }
                }

            }
        }

        public override void StartElement(XmlReader reader, IDictionary<String, String> attributes)
        {

            String localName = reader.Name;
            tempValue = new StringBuilder();

            if (localName.Equals(Constants.ENTITY_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {
                propertyName = attributes[Constants.ENTITY_DESCRIPTOR_NAME];
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

            if (localName.Equals(Constants.ENTITY_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {

                if (propertyName.Equals(Constants.ENTITY_DESCRIPTOR_CLASS_NAME, StringComparison.OrdinalIgnoreCase))
                {

                    if (tempValue.ToString().Equals(finalEntityDescriptorBasedOnClassName))
                    {
                        doesMatch = true;
                    }

                    throw new PrematureEndOfParseException(typeof(QuickEntityDescriptorReader).FullName, "startElement", "Class Name: " + tempValue.ToString());
                }
            }
        }


        /// <summary>
        /// Get entity descriptor object.
        /// </summary>
        /// <returns>Entity Descriptor Object</returns>
        public EntityDescriptor GetEntityDescriptor()
        {
            return this.entityDescriptor;
        }

    }
}
