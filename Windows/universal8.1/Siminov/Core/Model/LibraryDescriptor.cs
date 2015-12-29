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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Model
{


    /// <summary>
    /// Exposes methods to GET and SET Library Descriptor information as per define in LibraryDescriptor.xml file by application.
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
    ///            <!-- Entity Descriptor Needed Under This Library Descriptor -->
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
    public class LibraryDescriptor : IDescriptor
    {

        protected IDictionary<String, String> properties = new Dictionary<String, String>();

        protected ICollection<String> entityDescriptorPaths = new LinkedList<String>();

        protected IDictionary<String, EntityDescriptor> entityDescriptorBasedOnTableName = new Dictionary<String, EntityDescriptor>();
        protected IDictionary<String, EntityDescriptor> entityDescriptorBasedOnClassName = new Dictionary<String, EntityDescriptor>();
        protected IDictionary<String, EntityDescriptor> entityDescriptorBasedOnPath = new Dictionary<String, EntityDescriptor>();


        /// <summary>
        /// Get library name.
        /// </summary>
        /// <returns></returns>
        public String GetName()
        {
            if (this.properties.ContainsKey(Constants.LIBRARY_DESCRIPTOR_NAME))
            {
                return this.properties[Constants.LIBRARY_DESCRIPTOR_NAME];
            }

            return null;
        }


        /// <summary>
        /// Set library name as per defined in LibraryDescriptor.xml
        /// </summary>
        /// <param name="name">name</param>
        public void SetName(String name)
        {
            this.properties.Add(Constants.LIBRARY_DESCRIPTOR_NAME, name);
        }


        /// <summary>
        /// Get descriptor as per defined in LibraryDescriptor.xml
        /// </summary>
        /// <returns></returns>
        public String GetDescription()
        {
            if (this.properties.ContainsKey(Constants.LIBRARY_DESCRIPTOR_DESCRIPTION))
            {
                return this.properties[Constants.LIBRARY_DESCRIPTOR_DESCRIPTION];
            }

            return null;
        }


        /// <summary>
        /// Set description as per defined in LibraryDescritor.core.xml
        /// </summary>
        /// <param name="description">description</param>
        public void SetDescription(String description)
        {
            this.properties.Add(Constants.LIBRARY_DESCRIPTOR_DESCRIPTION, description);
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
            return this.entityDescriptorBasedOnTableName.ContainsKey(tableName);
        }


        /// <summary>
        /// Check whether entity descriptor object exists or not, based on mapped class name.
        /// </summary>
        /// <param name="className">Mapped class name</param>
        /// <returns>TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.</returns>
        public bool ContainsEntityDescriptorBasedOnClassName(String className)
        {
            return this.entityDescriptorBasedOnClassName.ContainsKey(className);
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
        ///            <entity-descriptors>
        ///                <entity-descriptor>Entity-Descriptors/Book.xml</entity-descriptor>
        ///                <entity-descriptor>Entity-Descriptors/Lession.xml</entity-descriptor>
        ///            </entity-descriptors>
        ///        </database-descriptor>
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptorPath">Path of database descriptor</param>
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
            return this.entityDescriptorBasedOnClassName.Values.GetEnumerator();
        }


        /// <summary>
        /// Get entity descriptor object based on table name.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        /// <returns>EntityDescriptor object based on table name</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnTableName(String tableName)
        {
            if (this.entityDescriptorBasedOnTableName.ContainsKey(tableName))
            {
                return this.entityDescriptorBasedOnTableName[tableName];
            }

            return null;
        }


        /// <summary>
        /// Get entity descriptor object based on mapped class name.
        /// </summary>
        /// <param name="className">Mapped class name</param>
        /// <returns>Entity Descriptor object</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnClassName(String className)
        {
            if (this.entityDescriptorBasedOnClassName.ContainsKey(className))
            {
                return this.entityDescriptorBasedOnClassName[className];
            }

            return null;
        }


        /// <summary>
        /// Get entity descriptor object based on path.
        /// </summary>
        /// <param name="libraryEntityDescriptorPath">Library Database path as per defined in Database Descriptor.xml file.</param>
        /// <returns>Entity Descriptor object</returns>
        public EntityDescriptor GetEntityDescriptorBasedOnPath(String libraryEntityDescriptorPath)
        {
            if (this.entityDescriptorBasedOnPath.ContainsKey(libraryEntityDescriptorPath))
            {
                return this.entityDescriptorBasedOnPath[libraryEntityDescriptorPath];
            }

            return null;
        }


        /// <summary>
        /// Add entity descriptor object in respect to entity descriptor path.
        /// </summary>
        /// <param name="libraryEntityDescriptorPath">Library Entity Descriptor Path</param>
        /// <param name="entityDescriptor">Entity Descriptor Object</param>
        public void AddEntityDescriptor(String libraryEntityDescriptorPath, EntityDescriptor entityDescriptor)
        {
            this.entityDescriptorBasedOnPath.Add(libraryEntityDescriptorPath, entityDescriptor);
            this.entityDescriptorBasedOnTableName.Add(entityDescriptor.GetTableName(), entityDescriptor);
            this.entityDescriptorBasedOnClassName.Add(entityDescriptor.GetClassName(), entityDescriptor);
        }


        /// <summary>
        /// Remove entity descriptor object based on entity descriptor path.
        /// </summary>
        /// <param name="entityDescriptorPath">Entity Descriptor Path</param>
        public void RemoveEntityDescriptorBasedOnPath(String entityDescriptorPath)
        {
            this.entityDescriptorPaths.Remove(entityDescriptorPath);

            EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnPath[entityDescriptorPath];
            this.entityDescriptorBasedOnPath.Remove(entityDescriptorPath);

            this.entityDescriptorBasedOnClassName.Values.Remove(entityDescriptor);
            this.entityDescriptorBasedOnTableName.Values.Remove(entityDescriptor);
        }


        /// <summary>
        /// Remove entity descriptor object based on mapped class name.
        /// </summary>
        /// <param name="className">Mapped class name</param>
        public void RemoveEntityDescriptorBasedOnClassName(String className)
        {
            EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnClassName[className];
            ICollection<String> keys = this.entityDescriptorBasedOnPath.Keys;

            String keyMatched = null;
            bool found = false;
            foreach (String key in keys)
            {
                EntityDescriptor entity = this.entityDescriptorBasedOnPath[key];
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
            EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnTableName[tableName];
            RemoveEntityDescriptorBasedOnClassName(entityDescriptor.GetClassName());
        }


        /// <summary>
        /// Remove entity descriptor object based on entity descriptor object.
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which needs to be removed.</param>
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
