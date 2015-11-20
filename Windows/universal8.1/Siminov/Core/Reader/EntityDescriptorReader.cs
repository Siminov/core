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
    /// Exposes methods to parse Library Descriptor information as per define in DatabaseDescriptor.xml or LibraryDescriptor.xml  file by application.
    /// <para>
    /// <code>
    ///<!-- Design Of EntityDescriptor.xml -->
    ///
    ///<entity-descriptor>
    ///
    ///  <!-- General Properties Of Table And Class -->
    ///
    ///  <!-- Mandatory Field -->
    ///  <!-- NAME OF TABLE -->
    ///  <property name="table_name">name_of_table</property>
    ///
    ///  <!-- Mandatory Field -->
    ///  <!-- MAPPED CLASS NAME -->
    ///  <property name="class_name">mapped_class_name</property>
    ///
    ///
    ///  <!-- Optional Field -->
    ///  <attributes>
    ///
    ///    <!-- Column Properties Required Under This Table -->
    ///
    ///    <!-- Optional Field -->
    ///    <attribute>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- COLUMN_NAME: Mandatory Field -->
    ///      <property name="column_name">column_name_of_table</property>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- VARIABLE_NAME: Mandatory Field -->
    ///      <property name="variable_name">class_variable_name</property>
    ///
    ///      <!-- Mandatory Field -->
    ///      <property name="type">java_variable_data_type</property>
    ///
    ///      <!-- Optional Field (Default is false) -->
    ///      <property name="primary_key">true/false</property>
    ///
    ///      <!-- Optional Field (Default is false) -->
    ///      <property name="not_null">true/false</property>
    ///
    ///      <!-- Optional Field (Default is false) -->
    ///      <property name="unique">true/false</property>
    ///
    ///      <!-- Optional Field -->
    ///      <property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
    ///
    ///      <!-- Optional Field -->
    ///      <property name="default">default_value_of_column (Eg: 0.1)</property>
    ///
    ///    </attribute>
    ///
    ///  </attributes>
    ///
    ///
    ///  <!-- Optional Field -->
    ///  <indexes>
    ///
    ///    <!-- Index Properties -->
    ///    <index>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- NAME OF INDEX -->
    ///      <property name="name">name_of_index</property>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- UNIQUE: Optional Field (Default is false) -->
    ///      <property name="unique">true/false</property>
    ///
    ///      <!-- Optional Field -->
    ///      <!-- Name of the column -->
    ///      <property name="column">column_name_needs_to_add</property>
    ///
    ///    </index>
    ///
    ///  </indexes>
    ///
    ///
    ///  <!-- Map Relationship Properties -->
    ///
    ///  <!-- Optional Field's -->
    ///  <relationships>
    ///
    ///    <relationship>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- Type of Relationship -->
    ///      <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- REFER -->
    ///      <property name="refer">class_variable_name</property>
    ///
    ///      <!-- Mandatory Field -->
    ///      <!-- REFER TO -->
    ///      <property name="refer_to">map_to_class_name</property>
    ///
    ///      <!-- Optional Field -->
    ///      <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>
    ///
    ///      <!-- Optional Field -->
    ///      <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>
    ///
    ///      <!-- Optional Field (Default is false) -->
    ///      <property name="load">true/false</property>
    ///
    ///    </relationship>
    ///
    ///  </relationships>
    ///
    ///</entity-descriptor>
    ///
    /// </code>
    /// </para>
    /// </summary>
    public class EntityDescriptorReader : SiminovSAXDefaultHandler
    {

        private StringBuilder tempValue = new StringBuilder();
        private String propertyName = null;

        private String entityDescriptorName = null;

        private ResourceManager resourceManager = ResourceManager.GetInstance();

        private EntityDescriptor entityDescriptor = null;

        private EntityDescriptor.Attribute currentAttribute = null;
        private EntityDescriptor.Index currentIndex = null;
        private EntityDescriptor.Relationship currectRelationship = null;

        private bool isAttribute = false;
        private bool isIndex = false;
        private bool isRelationship = false;


        /**
         * EntityDescriptorReader Constructor
         * @param entityDescriptorName Name of the entity descriptor name
         */
        public EntityDescriptorReader(String entityDescriptorName)
        {
            this.entityDescriptorName = entityDescriptorName;

            if (entityDescriptorName == null || entityDescriptorName.Length <= 0)
            {
                Log.Log.Error(typeof(EntityDescriptorReader).FullName, "Constructor", "Invalid name found. ENTITY_DESCRIPTOR-MODEL: " + this.entityDescriptorName);
                throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "Constructor", "Invalid name found. ENTITY_DESCRIPTOR-MODEL: " + this.entityDescriptorName);
            }


            String entityDescriptorFileName = null;
            String entityDescriptorFilePath = null;

            entityDescriptorFilePath = this.entityDescriptorName.Substring(0, entityDescriptorName.LastIndexOf("/"));
            entityDescriptorFileName = this.entityDescriptorName.Substring(this.entityDescriptorName.LastIndexOf("/") + 1, (this.entityDescriptorName.Length - this.entityDescriptorName.LastIndexOf("/")) - 1);

            Stream entityDescriptorStream = null;

            try
            {
                entityDescriptorStream = FileUtils.SearchFile(entityDescriptorFilePath, entityDescriptorFileName, FileUtils.INSTALLED_FOLDER);
            }
            catch (System.Exception ioException)
            {
                Log.Log.Error(typeof(EntityDescriptorReader).FullName, "Constructor", "IOException caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName + ", " + ioException.Message);
                throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "Constructor", "IOException caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName + "," + ioException.Message);
            }


            try
            {
                ParseMessage(entityDescriptorStream);
            }
            catch (SiminovException exception)
            {
                Log.Log.Error(typeof(EntityDescriptorReader).FullName, "Constructor", "Exception caught while parsing ENTITY-DESCRIPTOR: " + this.entityDescriptorName + ", " + exception.GetMessage());
                throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "Constructor", "Exception caught while parsing ENTITY-DESCRIPTOR: " + this.entityDescriptorName + ", " + exception.GetMessage());
            }

            DoValidation();
        }

        public override void StartElement(XmlReader reader, IDictionary<String, String> attributes)
        {

            String localName = reader.Name;
            tempValue = new StringBuilder();

            if (localName.Equals(Constants.ENTITY_DESCRIPTOR, StringComparison.OrdinalIgnoreCase))
            {
                entityDescriptor = new EntityDescriptor();
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_PROPERTY, StringComparison.OrdinalIgnoreCase))
            {
                propertyName = attributes[Constants.ENTITY_DESCRIPTOR_NAME];
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE, StringComparison.OrdinalIgnoreCase))
            {
                currentAttribute = new EntityDescriptor.Attribute();
                isAttribute = true;
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_INDEX, StringComparison.OrdinalIgnoreCase))
            {
                currentIndex = new EntityDescriptor.Index();
                isIndex = true;
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP, StringComparison.OrdinalIgnoreCase))
            {
                currectRelationship = new EntityDescriptor.Relationship();
                isRelationship = true;
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
                ProcessProperty();
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE, StringComparison.OrdinalIgnoreCase))
            {
                entityDescriptor.AddAttribute(currentAttribute);
                isAttribute = false;
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_INDEX, StringComparison.OrdinalIgnoreCase))
            {
                entityDescriptor.AddIndex(currentIndex);
                isIndex = false;
            }
            else if (localName.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP, StringComparison.OrdinalIgnoreCase))
            {
                entityDescriptor.AddRelationship(currectRelationship);
                isRelationship = false;
            }
        }

        public EntityDescriptor GetEntityDescriptor()
        {
            return this.entityDescriptor;
        }


        private void ProcessProperty()
        {

            if (isAttribute)
            {
                currentAttribute.AddProperty(propertyName, tempValue.ToString());

                if (propertyName.Equals(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME, StringComparison.OrdinalIgnoreCase))
                {

                    char[] charArray = tempValue.ToString().ToCharArray();
                    charArray[0] = Char.ToUpper(charArray[0]);
                    String getterMethodName = "Get" + new String(charArray);
                    String setterMethodName = "Set" + new String(charArray);

                    currentAttribute.SetGetterMethodName(getterMethodName);
                    currentAttribute.SetSetterMethodName(setterMethodName);
                }
            }
            else if (isIndex)
            {

                if (propertyName.Equals(Constants.ENTITY_DESCRIPTOR_INDEX_COLUMN, StringComparison.OrdinalIgnoreCase))
                {
                    currentIndex.AddColumn(tempValue.ToString());
                }
                else
                {
                    currentIndex.AddProperty(propertyName, tempValue.ToString());
                }
            }
            else if (isRelationship)
            {
                currectRelationship.AddProperty(propertyName, tempValue.ToString());

                if (propertyName.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER, StringComparison.OrdinalIgnoreCase))
                {

                    char[] charArray = tempValue.ToString().ToCharArray();
                    charArray[0] = Char.ToUpper(charArray[0]);
                    String getterReferMethodName = "Get" + new String(charArray);
                    String setterReferMethodName = "Set" + new String(charArray);

                    currectRelationship.SetGetterReferMethodName(getterReferMethodName);
                    currectRelationship.SetSetterReferMethodName(setterReferMethodName);
                }
            }
            else
            {
                entityDescriptor.AddProperty(propertyName, tempValue.ToString());
            }
        }

        private void DoValidation()
        {
            /*
             * Validate Table Name field.
             */
            String tableName = entityDescriptor.GetTableName();
            if (tableName == null || tableName.Length <= 0)
            {
                Log.Log.Error(typeof(EntityDescriptorReader).FullName, "DoValidation", "TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
                throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "DoValidation", "TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
            }

            /*
             * Validate Class Name field.
             */
            String className = entityDescriptor.GetClassName();
            if (className == null || className.Length <= 0)
            {
                Log.Log.Error(typeof(EntityDescriptorReader).FullName, "DoValidation", "CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
                throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "DoValidation", "CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
            }

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                /*
                 * Validate Variable Name field.
                 */
                String variableName = attribute.GetVariableName();
                if (variableName == null || variableName.Length <= 0)
                {
                    Log.Log.Error(typeof(EntityDescriptorReader).FullName, "DoValidation", "VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                    throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "DoValidation", "VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                }

                /*
                 * Validate Column Name filed.
                 */
                String columnName = attribute.GetColumnName();
                if (columnName == null || columnName.Length <= 0)
                {
                    Log.Log.Error(typeof(EntityDescriptorReader).FullName, "DoValidation", "COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                    throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "DoValidation", "COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                }

                /*
                 * Validate Type field.
                 */
                String type = attribute.GetType();
                if (type == null || type.Length <= 0)
                {
                    Log.Log.Error(typeof(EntityDescriptorReader).FullName, "DoValidation", "COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                    throw new DeploymentException(typeof(EntityDescriptorReader).FullName, "DoValidation", "COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
                }
            }
        }

    }
}
