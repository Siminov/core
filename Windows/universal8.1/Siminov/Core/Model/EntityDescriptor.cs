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

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Siminov.Core.Model;
using System.Collections;

namespace Siminov.Core.Model
{

    /// <summary>
    /// Exposes methods to GET and SET Library Descriptor information as per define in DatabaseDescriptor.xml or LibraryDescriptor.xml  file by application.
    /// <para>
    /// Example:
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
    public class EntityDescriptor : IDescriptor
    {
        protected IDictionary<String, String> properties = new Dictionary<String, String>();

        protected IDictionary<String, Attribute> attributeBasedOnColumnNames = new Dictionary<String, Attribute>();
        protected IDictionary<String, Attribute> attributeBasedOnVariableNames = new Dictionary<String, Attribute>();

        protected IDictionary<String, Index> indexes = new Dictionary<String, Index>();

        protected IDictionary<String, Relationship> relationshipsBasedOnRefer = new Dictionary<String, Relationship>();
        protected IDictionary<String, Relationship> relationshipsBasedOnReferTo = new Dictionary<String, Relationship>();


        /// <summary>
        /// Get table name
        /// </summary>
        /// <returns>Name of table</returns>
        public String GetTableName()
        {
            return this.properties[Constants.ENTITY_DESCRIPTOR_TABLE_NAME];
        }

        /// <summary>
        /// Set table name as per defined in EntityDescriptor.xml file.
        /// </summary>
        /// <param name="tableName">Name of table</param>
        public void SetTableName(String tableName)
        {
            this.properties.Add(Constants.ENTITY_DESCRIPTOR_TABLE_NAME, tableName);
        }

        /// <summary>
        /// Get Entity class name.
        /// </summary>
        /// <returns>CSharp class name</returns>
        public String GetClassName()
        {
            return this.properties[Constants.ENTITY_DESCRIPTOR_CLASS_NAME];
        }

        /// <summary>
        /// Set Entity class name as per defined in EntityDescriptor.xml file.
        /// </summary>
        /// <param name="className">CSharp class name</param>
        public void SetClassName(String className)
        {
            this.properties.Add(Constants.ENTITY_DESCRIPTOR_CLASS_NAME, className);
        }

        /// <summary>
        /// Check whether column exists based on column name.
        /// </summary>
        /// <param name="columnName">Name of column</param>
        /// <returns>TRUE: If column exists, FALSE: If column do not exists.</returns>
        public bool ContainsAttributeBasedOnColumnName(String columnName)
        {
            return this.attributeBasedOnColumnNames.ContainsKey(columnName);
        }

        /// <summary>
        /// Check whether column exists based on variable name.
        /// </summary>
        /// <param name="variableName">Name of variable</param>
        /// <returns>TRUE: If column exists, FALSE: If column do not exists.</returns>
        public bool ContainsAttributeBasedOnVariableName(String variableName)
        {
            return this.attributeBasedOnVariableNames.ContainsKey(variableName);
        }

        /// <summary>
        /// Get column based on column name.
        /// </summary>
        /// <param name="columnName">Name of column name</param>
        /// <returns>Column object</returns>
        public Attribute GetAttributeBasedOnColumnName(String columnName)
        {
            if (this.attributeBasedOnColumnNames.ContainsKey(columnName))
            {
                return this.attributeBasedOnColumnNames[columnName];
            }

            return null;
        }

        /// <summary>
        /// Get column based on variable name.
        /// </summary>
        /// <param name="variableName">Name of variable</param>
        /// <returns>Column object</returns>
        public Attribute GetAttributeBasedOnVariableName(String variableName)
        {
            if (this.attributeBasedOnVariableNames.ContainsKey(variableName))
            {
                return this.attributeBasedOnVariableNames[variableName];
            }

            return null;
        }

        /// <summary>
        /// Get all column names
        /// </summary>
        /// <returns>Iterator of all column names</returns>
        public IEnumerator<String> GetColumnNames()
        {
            return this.attributeBasedOnColumnNames.Keys.GetEnumerator();
        }

        /// <summary>
        /// Get all columns
        /// </summary>
        /// <returns>Iterator of all columns</returns>
        public IEnumerator<Attribute> GetAttributes()
        {
            return this.attributeBasedOnVariableNames.Values.GetEnumerator();
        }

        /// <summary>
        /// Add column to Entity Descriptor object
        /// </summary>
        /// <param name="attribute">Column object</param>
        public void AddAttribute(Attribute attribute)
        {
            this.attributeBasedOnVariableNames.Add(attribute.GetVariableName(), attribute);
            this.attributeBasedOnColumnNames.Add(attribute.GetColumnName(), attribute);
        }

        /// <summary>
        /// Remove column based on variable name.
        /// </summary>
        /// <param name="variableName">Name of variable</param>
        public void RemoveAttributeBasedOnVariableName(String variableName)
        {
            RemoveAttribute(GetAttributeBasedOnVariableName(variableName));
        }

        /// <summary>
        /// Remove column based on column name.
        /// </summary>
        /// <param name="columnName">Name of column</param>
        public void RemoveAttributeBasedOnColumnName(String columnName)
        {
            RemoveAttribute(GetAttributeBasedOnColumnName(columnName));
        }

        /// <summary>
        /// Remove column based on column object.
        /// </summary>
        /// <param name="attribute">Column object which need to be removed</param>
        public void RemoveAttribute(Attribute attribute)
        {
            this.attributeBasedOnColumnNames.Values.Remove(attribute);
        }

        /// <summary>
        /// Check whether index exists based in index name.
        /// </summary>
        /// <param name="indexName">Name of index</param>
        /// <returns>TRUE: If index exists, FALSE: If index do not exists.</returns>
        public bool ContainsIndex(String indexName)
        {
            return this.indexes.ContainsKey(indexName);
        }

        /// <summary>
        /// Get index object based on index name.
        /// </summary>
        /// <param name="indexName">Name of index</param>
        /// <returns>Index object</returns>
        public Index GetIndex(String indexName)
        {
            if (this.indexes.ContainsKey(indexName))
            {
                return this.indexes[indexName];
            }

            return null;
        }

        /// <summary>
        /// Get all index names.
        /// </summary>
        /// <returns>Iterator which contains all index names</returns>
        public IEnumerator<String> GetIndexNames()
        {
            return this.indexes.Keys.GetEnumerator();
        }

        /// <summary>
        /// Get all indexes.
        /// </summary>
        /// <returns>Iterator which contain all indexes</returns>
        public IEnumerator<Index> GetIndexes()
        {
            return this.indexes.Values.GetEnumerator();
        }

        /// <summary>
        /// Add index to Entity Descriptor object.
        /// </summary>
        /// <param name="index">Index object</param>
        public void AddIndex(Index index)
        {
            this.indexes.Add(index.GetName(), index);
        }

        /// <summary>
        /// Remove index object.
        /// </summary>
        /// <param name="indexName">Name of index</param>
        public void RemoveIndexBasedOnName(String indexName)
        {
            RemoveIndex(GetIndex(indexName));
        }

        /// <summary>
        /// Remove index object.
        /// </summary>
        /// <param name="index">Index object</param>
        public void RemoveIndex(Index index)
        {
            if (this.indexes.ContainsKey(index.GetName()))
            {
                this.indexes.Remove(index.GetName());
            }
        }


        /// <summary>
        /// Get iterator of relationship objects. 
        /// </summary>
        /// <returns>Relationship objects</returns>
        public IEnumerator<Relationship> GetRelationships()
        {
            return this.relationshipsBasedOnRefer.Values.GetEnumerator();
        }

        /// <summary>
        /// Get iterator of relationship objects based on refer.
        /// </summary>
        /// <param name="refer">Name of refer</param>
        /// <returns>Relationship object based on refer</returns>
        public Relationship GetRelationshipBasedOnRefer(String refer)
        {
            if (this.relationshipsBasedOnRefer.ContainsKey(refer))
            {
                return this.relationshipsBasedOnRefer[refer];
            }

            return null;
        }

        /// <summary>
        /// Get relationship object based on refer to.
        /// </summary>
        /// <param name="referTo">Name of refer to</param>
        /// <returns>Relationship object based on refer to</returns>
        public Relationship GetRelationshipBasedOnReferTo(String referTo)
        {
            if (this.relationshipsBasedOnReferTo.ContainsKey(referTo))
            {
                return this.relationshipsBasedOnReferTo[referTo];
            }

            return null;
        }

        /// <summary>
        /// Get one to one relationship object.
        /// </summary>
        /// <returns>Iterator of relationship objects</returns>
        public IEnumerator<Relationship> GetOneToOneRelationships()
        {

            ICollection<Relationship> oneToOneRelationships = new List<Relationship>();
            ICollection<Relationship> relationships = relationshipsBasedOnRefer.Values;

            IEnumerator<Relationship> relationshipsIterator = relationships.GetEnumerator();
            while (relationshipsIterator.MoveNext())
            {
                Relationship relationship = relationshipsIterator.Current;

                if (relationship.GetRelationshipType().Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE, StringComparison.OrdinalIgnoreCase))
                {
                    oneToOneRelationships.Add(relationship);
                }
            }

            return oneToOneRelationships.GetEnumerator();
        }

        /// <summary>
        /// Get one to many relationship object.
        /// </summary>
        /// <returns>Iterator of relationship objects</returns>
        public IEnumerator<Relationship> GetOneToManyRelationships()
        {

            ICollection<Relationship> oneToManyRelationships = new List<Relationship>();
            ICollection<Relationship> relationships = relationshipsBasedOnRefer.Values;

            IEnumerator<Relationship> relationshipsIterator = relationships.GetEnumerator();
            while (relationshipsIterator.MoveNext())
            {
                Relationship relationship = relationshipsIterator.Current;

                if (relationship.GetRelationshipType().Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    oneToManyRelationships.Add(relationship);
                }
            }

            return oneToManyRelationships.GetEnumerator();
        }

        /// <summary>
        /// Get many to one relationship object.
        /// </summary>
        /// <returns>Iterator of relationship objects</returns>
        public IEnumerator<Relationship> GetManyToOneRelationships()
        {

            ICollection<Relationship> manyToOneRelationships = new List<Relationship>();
            ICollection<Relationship> relationships = relationshipsBasedOnRefer.Values;

            IEnumerator<Relationship> relationshipsIterator = relationships.GetEnumerator();
            while (relationshipsIterator.MoveNext())
            {
                Relationship relationship = relationshipsIterator.Current;

                if (relationship.GetRelationshipType().Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE, StringComparison.OrdinalIgnoreCase))
                {
                    manyToOneRelationships.Add(relationship);
                }
            }

            return manyToOneRelationships.GetEnumerator();
        }

        /// <summary>
        /// Get many to many relationship object.
        /// </summary>
        /// <returns>Iterator of relationship objects</returns>
        public IEnumerator<Relationship> GetManyToManyRelationships()
        {

            ICollection<Relationship> manyToManyRelationships = new List<Relationship>();
            ICollection<Relationship> relationships = relationshipsBasedOnRefer.Values;

            IEnumerator<Relationship> relationshipsIterator = relationships.GetEnumerator();
            while (relationshipsIterator.MoveNext())
            {
                Relationship relationship = relationshipsIterator.Current;

                if (relationship.GetRelationshipType().Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    manyToManyRelationships.Add(relationship);
                }
            }

            return manyToManyRelationships.GetEnumerator();
        }

        /// <summary>
        /// Add relationship object
        /// </summary>
        /// <param name="relationship">Relationship object</param>
        public void AddRelationship(Relationship relationship)
        {
            this.relationshipsBasedOnRefer.Add(relationship.GetRefer(), relationship);
            this.relationshipsBasedOnReferTo.Add(relationship.GetRefer(), relationship);
        }


        /// <summary>
        /// Get all Properties defined in descriptor.
        /// </summary>
        /// <returns>All Property Values</returns>
        public IEnumerator<String> GetProperties()
        {
            ICollection<String> list = new List<String>();
            return list.GetEnumerator();
        }

        /// <summary>
        /// Get Property based on name provided.
        /// </summary>
        /// <param name="name">Name of Property</param>
        /// <returns>Property value</returns>
        public String GetProperty(String name)
        {
            return this.properties[name];
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
            this.properties.Remove(name);
        }

        /// <summary>
        /// Exposes methods to GET and SET Column information as per define in EntityDescriptor.xml file by application.
        /// <para>
        /// Example:
        /// <code>
        ///    <entity-descriptor>
        ///    	
        ///         <property name="table_name">BOOK</property>
        ///         <property name="class_name">Siminov.Core.Sample.Model.Book</property>        
        /// 
        ///        <attributes>
        ///	
        ///            <attribute>
        ///                <property name="variable_name">title</property>
        ///                <property name="column_name">TITLE</property>
        ///                <property name="type">TEXT</property>
        ///                <property name="primary_key">true</property>
        ///                <property name="not_null">true</property>
        ///                <property name="unique">true</property>
        ///            </attribute>
        ///            
        ///	        </attributes>
        ///	        
        ///    </entity-descriptor>
        /// </code>
        /// </para>
        /// </summary>
        public class Attribute : IDescriptor
        {
            private String getterMethodName = null;
            private String setterMethodName = null;

            private IDictionary<String, String> properties = new Dictionary<String, String>();


            /// <summary>
            /// Get variable name.
            /// </summary>
            /// <returns>Name of variable</returns>
            public String GetVariableName()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME];
            }

            /// <summary>
            /// Set variable name as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="variableName">Name of variable</param>
            public void SetVariableName(String variableName)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME, variableName);
            }

            /// <summary>
            /// Get column name
            /// </summary>
            /// <returns>Name Of Column</returns>
            public String GetColumnName()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME];
            }

            /// <summary>
            /// Set column name as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="columnName">Name of column name</param>
            public void SetColumnName(String columnName)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME, columnName);
            }

            /// <summary>
            /// Get type of column.
            /// </summary>
            /// <returns>Type of column</returns>
            public String GetType()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE))
                {
                    return (String) this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE];
                }

                return null;
            }

            /// <summary>
            /// Set type of column as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="type">Type of column</param>
            public void SetType(String type)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE, type);
            }

            /// <summary>
            /// Get mapped class column getter method name.
            /// </summary>
            /// <returns>Mapped class column getter method name</returns>
            public String GetGetterMethodName()
            {
                return this.getterMethodName;
            }

            /// <summary>
            /// Set mapped class column getter method name.
            /// </summary>
            /// <param name="getMethodName">Mapped class coumn getter method name</param>
            public void SetGetterMethodName(String getMethodName)
            {
                this.getterMethodName = getMethodName;
            }

            /// <summary>
            /// Get mapped class column setter method name.
            /// </summary>
            /// <returns>Mapped class column setter method name</returns>
            public String GetSetterMethodName()
            {
                return this.setterMethodName;
            }

            /// <summary>
            /// Set mapped class column setter method name.
            /// </summary>
            /// <param name="setMethodName">Mapped class column setter method name</param>
            public void SetSetterMethodName(String setMethodName)
            {
                this.setterMethodName = setMethodName;
            }

            /// <summary>
            /// Get default value of column.
            /// </summary>
            /// <returns>Default value of column</returns>
            public String GetDefaultValue()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE))
                {
                    return this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE];
                }

                return null;
            }

            /// <summary>
            /// Set default value of column as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="defaultValue">Default value of column</param>
            public void SetDefaultValue(String defaultValue)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE, defaultValue);
            }

            /// <summary>
            /// Get check constraint of column.
            /// </summary>
            /// <returns>Check constraint of column</returns>
            public String GetCheck()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK))
                {
                    return this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK];
                }

                return null;
            }

            /// <summary>
            /// Set check constraint of column as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="check">Check constraint</param>
            public void SetCheck(String check)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK, check);
            }

            /// <summary>
            /// Check whether column is primary key.
            /// </summary>
            /// <returns>TRUE: If column is primary key, FALSE: If column is not primary key.</returns>
            public bool IsPrimaryKey()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY))
                {
                    String primaryKey = this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY];
                    if (primaryKey == null || primaryKey.Length <= 0)
                    {
                        return false;
                    }
                    else if (primaryKey != null && primaryKey.Length > 0 && primaryKey.Equals("true", StringComparison.OrdinalIgnoreCase))
                    {
                        return true;
                    }

                    return false;
                }

                return false;
            }

            /// <summary>
            /// Set column as primary key or not.
            /// </summary>
            /// <param name="primaryKey">TRUE: If column is primary key, FALSE: If column is not primary key.</param>
            public void SetPrimaryKey(bool primaryKey)
            {
                String primaryKeyValue = primaryKey ? Boolean.TrueString : Boolean.FalseString;
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY, primaryKeyValue);
            }

            /// <summary>
            /// Check whether column is unique or not.
            /// </summary>
            /// <returns>TRUE: If column is unique, FALSE: If column is not unique.</returns>
            public bool IsUnique()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE))
                {
                    String unique = this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE];
                    if (unique == null || unique.Length <= 0)
                    {
                        return false;
                    }
                    else if (unique != null && unique.Length > 0 && unique.Equals("true", StringComparison.OrdinalIgnoreCase))
                    {
                        return true;
                    }

                    return false;
                }

                return false;
            }

            /// <summary>
            /// Set whether column is unique or not.
            /// </summary>
            /// <param name="isUnique">TRUE: If column is unique, FALSE: If column is not unique</param>
            public void SetUnique(bool isUnique)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE, Convert.ToString(isUnique));
            }

            /// <summary>
            /// Check whether column value can be not or not.
            /// </summary>
            /// <returns>TRUE: If column value can be null, FALSE: If column value can not be null.</returns>
            public bool IsNotNull()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL))
                {
                    String notNull = this.properties[Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL];
                    if (notNull == null || notNull.Length <= 0)
                    {
                        return false;
                    }
                    else if (notNull != null && notNull.Length > 0 && notNull.Equals("true", StringComparison.OrdinalIgnoreCase))
                    {
                        return true;
                    }

                    return false;
                }

                return false;
            }

            /// <summary>
            /// Set whether column can be null or not.
            /// </summary>
            /// <param name="isNotNull">TRUE: If column value can be null, FALSE: If column value can not be null.</param>
            public void SetNotNull(bool isNotNull)
            {
                String isNotNullValue = isNotNull ? Boolean.TrueString : Boolean.FalseString;
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL, isNotNullValue);
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
                return this.properties[name];
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
                this.properties.Remove(name);
            }
        }


        /// <summary>
        /// Exposes methods to GET and SET Reference Map information as per define in EntityDescriptor.xml file by application.
        /// <para>
        /// Example:
        /// <code>
        ///        <index>
        ///            <property name="name">BOOK_INDEX_BASED_ON_AUTHOR</property>
        ///            <property name="unique">true</property>
        ///            <property name="column">AUTHOR</property>
        ///        </index>
        /// </code>
        /// </para>
        /// </summary>
        public class Index : IDescriptor
        {

            private IDictionary<String, String> properties = new Dictionary<String, String>();
            private ICollection<String> columns = new LinkedList<String>();

            /// <summary>
            /// Get index name.
            /// </summary>
            /// <returns>Index Name</returns>
            public String GetName()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_INDEX_NAME];
            }

            /// <summary>
            /// Set index name as per defined in EntityDescriptor.xml file.
            /// </summary>
            /// <param name="name">Index Name</param>
            public void SetName(String name)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_INDEX_NAME, name);
            }

            /// <summary>
            /// Check whether index should be unique or not.
            /// </summary>
            /// <returns>TRUE: If index is unique, FALSE: If index is not unqiue.</returns>
            public bool IsUnique()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_INDEX_UNIQUE))
                {
                    String unique = this.properties[Constants.ENTITY_DESCRIPTOR_INDEX_UNIQUE];
                    if (unique == null || unique.Length <= 0)
                    {
                        return false;
                    }
                    else if (unique != null && unique.Length > 0 && unique.Equals("true", StringComparison.OrdinalIgnoreCase))
                    {
                        return true;
                    }
                }

                return false;
            }

            /// <summary>
            /// Set whether unqiue is unique or not.
            /// </summary>
            /// <param name="unique">TRUE: If index is unique, FALSE: If index is not unique.</param>
            public void SetUnique(bool unique)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_INDEX_UNIQUE, Convert.ToString(unique));
            }

            /// <summary>
            /// Check whether index contain column or not.
            /// </summary>
            /// <param name="column">Name of column</param>
            /// <returns>TRUE: If index contains column, FALSE: If index does not contain column.</returns>
            public bool ContainsColumn(String column)
            {
                return this.columns.Contains(column);
            }

            /// <summary>
            /// Get all columns.
            /// </summary>
            /// <returns>Iterator which contain all columns.</returns>
            public IEnumerator<String> GetColumns()
            {
                return this.columns.GetEnumerator();
            }

            /// <summary>
            /// Add column to index.
            /// </summary>
            /// <param name="column">Name of column</param>
            public void AddColumn(String column)
            {
                this.columns.Add(column);
            }

            /// <summary>
            /// Remove column from index.
            /// </summary>
            /// <param name="column">Name of column</param>
            public void RemoveColumn(String column)
            {
                this.columns.Remove(column);
            }

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
                return this.properties[name];
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
            /// <param name="name">Name of Property.</param>
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
                this.properties.Remove(name);
            }
        }

        /// <summary>
        /// Contains relationship details.
        /// </summary>
        public class Relationship : IDescriptor
        {
            private String getterReferMethodName = null;
            private String setterReferMethodName = null;

            private IDictionary<String, String> properties = new Dictionary<String, String>();

            private EntityDescriptor referedEntityDescriptor = null;

            /// <summary>
            /// Get relationship type.
            /// </summary>
            /// <returns>Type of relationship</returns>
            public String GetRelationshipType()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE];
            }


            /// <summary>
            /// Set relationship type.
            /// </summary>
            /// <param name="relationshipType">Type of relationship</param>
            public void SetRelationshipType(String relationshipType)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE, relationshipType);
            }

            /// <summary>
            /// Get refer.
            /// </summary>
            /// <returns>Name of refer</returns>
            public String GetRefer()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER];
            }


            /// <summary>
            /// Set refer.
            /// </summary>
            /// <param name="refer">Name of refer</param>
            public void SetRefer(String refer)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER, refer);
            }


            /// <summary>
            /// Get refer to.
            /// </summary>
            /// <returns>Name of refer to</returns>
            public String GetReferTo()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO];
            }


            /// <summary>
            /// Set refer to.
            /// </summary>
            /// <param name="referTo">Name of refer to</param>
            public void SetReferTo(String referTo)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO, referTo);
            }


            /// <summary>
            /// Get on update.
            /// </summary>
            /// <returns>Action on update</returns>
            public String GetOnUpdate()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE];
            }


            /// <summary>
            /// Set on update.
            /// </summary>
            /// <param name="onUpdate">Action on update</param>
            public void SetOnUpdate(String onUpdate)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE, onUpdate);
            }


            /// <summary>
            /// Get on delete.
            /// </summary>
            /// <returns>Action on delete</returns>
            public String GetOnDelete()
            {
                return this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE];
            }


            /// <summary>
            /// Set on delete.
            /// </summary>
            /// <param name="onDelete">Action on delete</param>
            public void SetOnDelete(String onDelete)
            {
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE, onDelete);
            }


            /// <summary>
            /// Get getter refer method name
            /// </summary>
            /// <returns>Getter refer method name</returns>
            public String GetGetterReferMethodName()
            {
                return this.getterReferMethodName;
            }


            /// <summary>
            /// Set getter refer method name.
            /// </summary>
            /// <param name="getterReferMethodName">Name of getter refer method name</param>
            public void SetGetterReferMethodName(String getterReferMethodName)
            {
                this.getterReferMethodName = getterReferMethodName;
            }


            /// <summary>
            /// Get setter refer method name.
            /// </summary>
            /// <returns>Name of setter refer method name</returns>
            public String GetSetterReferMethodName()
            {
                return this.setterReferMethodName;
            }


            /// <summary>
            /// Set setter refer method name.
            /// </summary>
            /// <param name="setterReferMethodName">Name of setter refer name</param>
            public void SetSetterReferMethodName(String setterReferMethodName)
            {
                this.setterReferMethodName = setterReferMethodName;
            }


            /// <summary>
            /// Check whether load property value is set to TRUE/FASLE.
            /// </summary>
            /// <returns>TRUE: If load property value is set to true; FALSE: If load property value is set to false.</returns>
            public bool IsLoad()
            {
                if (this.properties.ContainsKey(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD))
                {
                    String load = this.properties[Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD];
                    if (load == null || load.Length <= 0)
                    {
                        return false;
                    }
                    else if (load != null && load.Length > 0 && load.Equals("true", StringComparison.OrdinalIgnoreCase))
                    {
                        return true;
                    }

                    return false;
                }

                return false;
            }


            /// <summary>
            /// Set load property value.
            /// </summary>
            /// <param name="load">TRUE: If load property value is true; FALSE: If load property value is false.</param>
            public void SetLoad(bool load)
            {
                String loadValue = load ? Boolean.TrueString : Boolean.FalseString;
                this.properties.Add(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD, loadValue);
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
            /// Get entity descriptor object.
            /// </summary>
            /// <returns>EntityDescriptor object</returns>
            public EntityDescriptor GetReferedEntityDescriptor()
            {
                return this.referedEntityDescriptor;
            }


            /// <summary>
            /// Set refered entity descriptor object.
            /// </summary>
            /// <param name="referedEntityDescriptor">EntityDescriptor object</param>
            public void SetReferedEntityDescriptor(EntityDescriptor referedEntityDescriptor)
            {
                this.referedEntityDescriptor = referedEntityDescriptor;
            }
        }
    }
}
