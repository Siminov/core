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
    /// Exposes methods to GET and SET Database Descriptor information as per define in DatabaseDescriptor.xml file by application.
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
    ///
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
    public class DatabaseDescriptor : IDescriptor
    {

        protected IDictionary<String, String> properties = new Dictionary<String, String>();

        protected ICollection<String> entityDescriptorPaths = new LinkedList<String>();

        protected IDictionary<String, EntityDescriptor> entityDescriptorsBasedOnTableName = new Dictionary<String, EntityDescriptor>();
        protected IDictionary<String, EntityDescriptor> entityDescriptorsBasedOnClassName = new Dictionary<String, EntityDescriptor>();
        protected IDictionary<String, EntityDescriptor> entityDescriptorsBasedOnPath = new Dictionary<String, EntityDescriptor>();

        /// <summary>
        /// Get database descriptor name as defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <returns>Database Descriptor Name</returns>
        public String GetDatabaseName()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME))
            {
                return this.properties[Constants.DATABASE_DESCRIPTOR_DATABASE_NAME];
            }

            return null;
        }

        /// <summary>
        /// Set database descriptor name as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <param name="databaseName">Database Descriptor Name</param>
        public void SetDatabaseName(String databaseName)
        {
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME, databaseName);
        }

        /// <summary>
        /// Get type of database
        /// </summary>
        /// <returns>Type of database</returns>
        public String GetType()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_TYPE))
            {
                return this.properties[Constants.DATABASE_DESCRIPTOR_TYPE];
            }

            return null;
        }

        /// <summary>
        /// Set type of database
        /// </summary>
        /// <param name="type">Type of database</param>
        public void SetType(String type)
        {
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_TYPE, type);
        }

        /// <summary>
        /// Get version of database 
        /// </summary>
        /// <returns>Version of database</returns>
        public double GetVersion()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_VERSION))
            {

                String version = this.properties[Constants.DATABASE_DESCRIPTOR_VERSION];
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
        public void SetVersion(long version)
        {
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_VERSION, Convert.ToString(version));
        }


        /// <summary>
        /// Get description as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <returns>Description defined in DatabaseDescriptor.xml file</returns>
        public String GetDescription()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_DESCRIPTION))
            {
                return this.properties[Constants.DATABASE_DESCRIPTOR_DESCRIPTION];
            }

            return null;
        }

        /// <summary>
        /// Set description as per defined in DatabaseDescritor.xml file.
        /// </summary>
        /// <param name="description">Description defined in DatabaseDescriptor.xml file</param>
        public void SetDescription(String description)
        {
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_DESCRIPTION, description);
        }


        /// <summary>
        /// Check whether database needs to be stored on SDCard or not.
        /// </summary>
        /// <returns>TRUE: If external_storage defined as true in DatabaseDescriptor.xml file, FALSE: If external_storage defined as false in DatabaseDescritor.xml file.</returns>
        public bool IsExternalStorageEnable()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE))
            {

                String externalStorage = this.properties[Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE];
                if (externalStorage == null || externalStorage.Length <= 0)
                {
                    return false;
                }
                else if (externalStorage != null && externalStorage.Length > 0 && externalStorage.Equals("true", StringComparison.OrdinalIgnoreCase))
                {
                    return true;
                }

                return false;
            }

            return false;
        }

        /// <summary>
        /// Set the external storage value as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <param name="isExternalStorageEnable">(true/false) External Storage Enable Or Not</param>
        public void SetExternalStorageEnable(bool isExternalStorageEnable)
        {
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE, Convert.ToString(isExternalStorageEnable));
        }

        /// <summary>
        /// Check whether database transactions to make multi-threading safe or not.
        /// </summary>
        /// <returns>TRUE: If locking is required as per defined in DatabaseDescriptor.xml file, FALSE: If locking is not required as per defined in DatabaseDescriptor.xml file.</returns>
        public bool IsTransactionSafe()
        {
            if (this.properties.ContainsKey(Constants.DATABASE_DESCRIPTOR_TRANSACTION_SAFE))
            {
                String transactionSafe = this.properties[Constants.DATABASE_DESCRIPTOR_TRANSACTION_SAFE];
                if (transactionSafe == null || transactionSafe.Length <= 0)
                {
                    return false;
                }
                else if (transactionSafe != null && transactionSafe.Length > 0 && transactionSafe.Equals("true", StringComparison.OrdinalIgnoreCase))
                {
                    return true;
                }

                return false;
            }

            return false;
        }

        /// <summary>
        /// Set database locking as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <param name="transactionSafe">(true/false) database locking as per defined in DatabaseDescriptor.xml file.</param>
        public void SetTransactionSafe(bool transactionSafe)
        {
            String transaction = transactionSafe ? Boolean.TrueString : Boolean.FalseString;
            this.properties.Add(Constants.DATABASE_DESCRIPTOR_TRANSACTION_SAFE, transaction);
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
        /// Check whether entity descriptor object exists or not, based on table name.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        /// <returns>TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.</returns>
        public bool ContainsEntityDescriptorBasedOnTableName(String tableName)
        {
            return this.entityDescriptorsBasedOnTableName.ContainsKey(tableName);
        }

        /// <summary>
        /// Check whether entity descriptor object exists or not, based on mapped class name.
        /// </summary>
        /// <param name="className">Mapped class name</param>
        /// <returns>TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.</returns>
        public bool ContainsEntityDescriptorBasedOnClassName(String className)
        {
            return this.entityDescriptorsBasedOnClassName.ContainsKey(className);
        }

        /// <summary>
        /// Get all entity descriptor paths as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <returns>Iterator which contain all entity descriptor paths</returns>
        public IEnumerator<String> GetEntityDescriptorPaths()
        {
            return this.entityDescriptorPaths.GetEnumerator();
        }

        /// <summary>
        /// Add entity descriptor path as per defined in DatabaseDescriptor.xml file.
        /// <para>
        /// EXAMPLE:
        /// <code>
        ///        <database-descriptor>
        ///            <entity-descriptor>
        ///                <entity-descriptor>Liquor-Mappings/Liquor.xml</entity-descriptor>
        ///                <entity-descriptor>Liquor-Mappings/LiquorBrand.xml</entity-descriptor>
        ///            </entity-descriptor>
        ///        </database-descriptor>
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptorPath"></param>
        public void AddEntityDescriptorPath(String entityDescriptorPath)
        {
            this.entityDescriptorPaths.Add(entityDescriptorPath);
        }

        /// <summary>
        /// Get all entity descriptor objects contained.
        /// </summary>
        /// <returns>All entity descriptor objects</returns>
        public IEnumerator<EntityDescriptor> GetEntityDescriptors()
        {
            return this.entityDescriptorsBasedOnClassName.Values.GetEnumerator();
        }

        /// <summary>
        /// Get entity descriptor object based on table name.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        /// <returns>Entity Descriptor object based on table name</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnTableName(String tableName)
        {
            if (this.entityDescriptorsBasedOnTableName.ContainsKey(tableName))
            {
                return this.entityDescriptorsBasedOnTableName[tableName];
            }

            return null;
        }

        /// <summary>
        /// Get entity descriptor object based on mapped class name.
        /// </summary>
        /// <param name="className">Model class name</param>
        /// <returns>Entity Descriptor object</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnClassName(String className)
        {
            if (this.entityDescriptorsBasedOnClassName.ContainsKey(className))
            {
                return this.entityDescriptorsBasedOnClassName[className];
            }

            return null;
        }

        /// <summary>
        /// Get entity descriptor object based on path.
        /// </summary>
        /// <param name="entityDescriptorPath">Entity descriptor path as per defined in Database Descriptor.xml file.</param>
        /// <returns>Entity Descriptor object</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnPath(String entityDescriptorPath)
        {
            if (this.entityDescriptorsBasedOnPath.ContainsKey(entityDescriptorPath))
            {
                return this.entityDescriptorsBasedOnPath[entityDescriptorPath];
            }

            return null;
        }

        /// <summary>
        /// Add entity descriptor object in respect to entity descriptor path.
        /// </summary>
        /// <param name="entityDescriptorPath">Entity Descriptor Path</param>
        /// <param name="entityDescriptor">Entity Descriptor object</param>
        public void AddEntityDescriptor(String entityDescriptorPath, EntityDescriptor entityDescriptor)
        {
            this.entityDescriptorsBasedOnPath.Add(entityDescriptorPath, entityDescriptor);
            this.entityDescriptorsBasedOnTableName.Add(entityDescriptor.GetTableName(), entityDescriptor);
            this.entityDescriptorsBasedOnClassName.Add(entityDescriptor.GetClassName(), entityDescriptor);
        }

        /// <summary>
        /// Remove entity descriptor object based on entity descriptor path.
        /// </summary>
        /// <param name="entityDescriptorPath">Entity Descriptor Path</param>
        public void RemoveEntityDescriptorBasedOnPath(String entityDescriptorPath)
        {
            this.entityDescriptorPaths.Remove(entityDescriptorPath);

            EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnPath[entityDescriptorPath];
            this.entityDescriptorsBasedOnPath.Remove(entityDescriptorPath);

            this.entityDescriptorsBasedOnClassName.Values.Remove(entityDescriptor);
            this.entityDescriptorsBasedOnTableName.Values.Remove(entityDescriptor);
        }

        /// <summary>
        /// Remove entity descriptor object based on mapped class name.
        /// </summary>
        /// <param name="className">Mapped class name</param>
        public void RemoveEntityDescriptorBasedOnClassName(String className)
        {
            EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnClassName[className];
            ICollection<String> keys = this.entityDescriptorsBasedOnPath.Keys;

            String keyMatched = null;
            bool found = false;
            foreach (String key in keys)
            {
                EntityDescriptor entity = this.entityDescriptorsBasedOnPath[key];
                if (entityDescriptor == entity)
                {
                    keyMatched = key;
                    found = true;
                    break;
                }
            }

            if (found)
            {
                RemoveEntityDescriptorBasedOnPath(keyMatched);
            }
        }

        /// <summary>
        /// Remove entity descriptor object based on table name.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        public void RemoveEntityDescriptorBasedOnTableName(String tableName)
        {
            EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnTableName[tableName];
            RemoveEntityDescriptorBasedOnClassName(entityDescriptor.GetClassName());
        }

        /// <summary>
        /// Remove entity descriptor object based on entity descriptor object.
        /// </summary>
        /// <param name="entityDescriptor">Entity descriptor object which needs to be removed</param>
        public void RemoveEntityDescriptor(EntityDescriptor entityDescriptor)
        {
            RemoveEntityDescriptorBasedOnClassName(entityDescriptor.GetClassName());
        }

        /// <summary>
        /// Get all entity descriptor objects in sorted order. The order will be as per defined in DatabaseDescriptor.xml file.
        /// </summary>
        /// <returns>Iterator which contains all entity descriptor objects</returns>
        public IEnumerator<EntityDescriptor> OrderedEntityDescriptors()
        {
            ICollection<EntityDescriptor> orderedEntityDescriptors = new LinkedList<EntityDescriptor>();

            foreach (String entityDescriptorPath in this.entityDescriptorPaths)
            {
                orderedEntityDescriptors.Add(GetEntityDescriptorBasedOnPath(entityDescriptorPath));
            }

            return orderedEntityDescriptors.GetEnumerator();
        }
    }
}
