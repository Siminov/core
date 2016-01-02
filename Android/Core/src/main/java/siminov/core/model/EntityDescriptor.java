/**
 * [SIMINOV FRAMEWORK - CORE]
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

package siminov.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import siminov.core.Constants;
import siminov.core.utils.EmptyIterator;


/**
 * Exposes methods to GET and SET Library Descriptor information as per define in DatabaseDescriptor.xml or LibraryDescriptor.xml  file by application.
	<p>
		<pre>
		
Example:
	{@code



	<!-- Design Of EntityDescriptor.xml -->

	<entity-descriptor>

		<!-- General Properties Of Table And Class -->

		<!-- Mandatory Field -->
		<!-- NAME OF TABLE -->
		<property name="table_name">name_of_table</property>

		<!-- Mandatory Field -->
		<!-- MAPPED CLASS NAME -->
		<property name="class_name">mapped_class_name</property>


		<!-- Optional Field -->
		<attributes>

			<!-- Column Properties Required Under This Table -->

				<!-- Optional Field -->
			<attribute>

				<!-- Mandatory Field -->
					<!-- COLUMN_NAME: Mandatory Field -->
				<property name="column_name">column_name_of_table</property>

				<!-- Mandatory Field -->
					<!-- VARIABLE_NAME: Mandatory Field -->
				<property name="variable_name">variable_name</property>

				<!-- Mandatory Field -->
				<property name="type">variable_data_type</property>

				<!-- Optional Field (Default is false) -->
				<property name="primary_key">true/false</property>

				<!-- Optional Field (Default is false) -->
				<property name="not_null">true/false</property>

				<!-- Optional Field (Default is false) -->
				<property name="unique">true/false</property>

				<!-- Optional Field -->
				<property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>

				<!-- Optional Field -->
				<property name="default">default_value_of_column (Eg: 0.1)</property>

			</attribute>

		</attributes>


		<!-- Optional Field -->
		<indexes>

			<!-- Index Properties -->
			<index>

				<!-- Mandatory Field -->
					<!-- NAME OF INDEX -->
				<property name="name">name_of_index</property>

				<!-- Mandatory Field -->
					<!-- UNIQUE: Optional Field (Default is false) -->
				<property name="unique">true/false</property>

				<!-- Optional Field -->
					<!-- Name of the column -->
				<property name="column">column_name_needs_to_add</property>

			</index>

		</indexes>


		<!-- Map Relationship Properties -->

		<!-- Optional Field's -->
		<relationships>

			<relationship>

				<!-- Mandatory Field -->
					<!-- Type of Relationship -->
				<property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>

				<!-- Mandatory Field -->
					<!-- REFER -->
				<property name="refer">class_variable_name</property>

				<!-- Mandatory Field -->
					<!-- REFER TO -->
				<property name="refer_to">map_to_class_name</property>

				<!-- Optional Field -->
				<property name="on_update">cascade/restrict/no_action/set_null/set_default</property>

				<!-- Optional Field -->
				<property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>

				<!-- Optional Field (Default is false) -->
				<property name="load">true/false</property>

			</relationship>

		</relationships>

	</entity-descriptor>



	}
	
		</pre>
	</p>
 *
 */
public class EntityDescriptor implements IDescriptor {

	private Map<String, String> properties = new HashMap<String, String> ();
	
	protected Map<String, Attribute> attributeBasedOnColumnNames = new LinkedHashMap<String, Attribute>();
	protected Map<String, Attribute> attributeBasedOnVariableNames = new LinkedHashMap<String, Attribute>();
	
	protected Map<String, Index> indexes = new LinkedHashMap<String, Index>();

	protected Map<String, Relationship> relationshipsBasedOnRefer = new LinkedHashMap<String, Relationship>(); 
	protected Map<String, Relationship> relationshipsBasedOnReferTo = new LinkedHashMap<String, Relationship>();
	
	
	/**
	 * Get table name.
	 * @return Name of table.
	 */
	public String getTableName() {
		return this.properties.get(Constants.ENTITY_DESCRIPTOR_TABLE_NAME);
	}
	
	/**
	 * Set table name as per defined in EntityDescriptor.xml file.
	 * @param tableName Name of table.
	 */
	public void setTableName(final String tableName) {
		this.properties.put(Constants.ENTITY_DESCRIPTOR_TABLE_NAME, tableName);
	}
	
	/**
	 * Get mapped class name.
	 * @return Mapped class name.
	 */
	public String getClassName() {
		return this.properties.get(Constants.ENTITY_DESCRIPTOR_CLASS_NAME);
	}
	
	/**
	 * Set mapped class name as per defined in EntityDescriptor.xml file.
	 * @param className Mapped class name.
	 */
	public void setClassName(final String className) {
		this.properties.put(Constants.ENTITY_DESCRIPTOR_CLASS_NAME, className);
	}

	/**
	 * Check whether column exists based on column name.
	 * @param columnName Name of column.
	 * @return TRUE: If column exists, FALSE: If column do not exists.
	 */
	public boolean containsAttributeBasedOnColumnName(final String columnName) {
		return this.attributeBasedOnColumnNames.containsKey(columnName);
	}
	
	/**
	 * Check whether column exists based on variable name.
	 * @param variableName Name of variable.
	 * @return TRUE: If column exists, FALSE: If column do not exists.
	 */
	public boolean containsAttributeBasedOnVariableName(final String variableName) {
		return this.attributeBasedOnVariableNames.containsKey(variableName);
	}
	
	/**
	 * Get column based on column name.
	 * @param columnName Name of column name.
	 * @return Column object.
	 */
	public Attribute getAttributeBasedOnColumnName(final String columnName) {
		return this.attributeBasedOnColumnNames.get(columnName);
	}

	/**
	 * Get column based on variable name.
	 * @param variableName Name of variable.
	 * @return Column object.
	 */
	public Attribute getAttributeBasedOnVariableName(final String variableName) {
		return this.attributeBasedOnVariableNames.get(variableName);
	}
	
	/**
	 * Get all column names.
	 * @return Iterator of all column names.
	 */
	public Iterator<String> getColumnNames() {
		return this.attributeBasedOnColumnNames.keySet().iterator();
	}
	
	/**
	 * Get all columns.
	 * @return Iterator of all columns.
	 */
	public Iterator<Attribute> getAttributes() {
		return this.attributeBasedOnVariableNames.values().iterator();
	}

	/**
	 * Add column to Entity Descriptor object.
	 * @param attribute Column object.
	 */
	public void addAttribute(final Attribute attribute) {
		this.attributeBasedOnVariableNames.put(attribute.getVariableName(), attribute);
		this.attributeBasedOnColumnNames.put(attribute.getColumnName(), attribute);
	}
	
	/**
	 * Remove column based on variable name.
	 * @param variableName Name of variable.
	 */
	public void removeAttributeBasedOnVariableName(final String variableName) {
		removeAttribute(getAttributeBasedOnVariableName(variableName));
	}
	
	/**
	 * Remove column based on column name.
	 * @param columnName Name of column.
	 */
	public void removeAttributeBasedOnColumnName(final String columnName) {
		removeAttribute(getAttributeBasedOnColumnName(columnName));
	}
	
	/**
	 * Remove column based on column object.
	 * @param attribute Column object which need to be removed.
	 */
	public void removeAttribute(final Attribute attribute) {
		this.attributeBasedOnColumnNames.values().remove(attribute);
	}
	
	/**
	 * Check whether index exists based in index name.
	 * @param indexName Name of index.
	 * @return TRUE: If index exists, FALSE: If index do not exists.
	 */
	public boolean containsIndex(final String indexName) {
		return this.indexes.containsKey(indexName);
	}
	
	/**
	 * Get index object based on index name.
	 * @param indexName Name of index.
	 * @return Index object.
	 */
	public Index getIndex(final String indexName) {
		return this.indexes.get(indexName);
	}
	
	/**
	 * Get all index names.
	 * @return Iterator which contains all index names.
	 */
	public Iterator<String> getIndexNames() {
		return this.indexes.keySet().iterator();
	}
	
	/**
	 * Get all indexes.
	 * @return Iterator which contain all indexes.
	 */
	public Iterator<Index> getIndexes() {
		return this.indexes.values().iterator();
	}
	
	/**
	 * Add index to Entity Descriptor object.
	 * @param index Index object.
	 */
	public void addIndex(final Index index) {
		this.indexes.put(index.getName(), index);
	}
	
	/**
	 * Remove index object.
	 * @param indexName Name of index.
	 */
	public void removeIndexBasedOnName(final String indexName) {
		removeIndex(getIndex(indexName));
	}
	
	/**
	 * Remove index object.
	 * @param index Index object.
	 */
	public void removeIndex(final Index index) {
		this.indexes.remove(index.getName());
	}


	/**
	 * Get iterator of relationship objects. 
	 * @return Relationship objects.
	 */
	public Iterator<Relationship> getRelationships() {
		return this.relationshipsBasedOnRefer.values().iterator();
	}
	
	/**
	 * Get iterator of relationship objects based on refer.
	 * @param refer Name of refer.
	 * @return Relationship object based on refer.
	 */
	public Relationship getRelationshipBasedOnRefer(String refer) {
		return this.relationshipsBasedOnRefer.get(refer);
	}
	
	/**
	 * Get relationship object based on refer to.
	 * @param referTo Name of refer to.
	 * @return Relationship object based on refer to.
	 */
	public Relationship getRelationshipBasedOnReferTo(String referTo) {
		return this.relationshipsBasedOnReferTo.get(referTo);
	}
	
	/**
	 * Get one to one relationship object.
	 * @return Iterator of relationship objects.
	 */
	public Iterator<Relationship> getOneToOneRelationships() {
		
		Collection<Relationship> oneToOneRelationships = new ArrayList<Relationship>();
		Collection<Relationship> relationships = relationshipsBasedOnRefer.values();

		Iterator<Relationship> relationshipsIterator = relationships.iterator();
		while(relationshipsIterator.hasNext()) {
			Relationship relationship = relationshipsIterator.next();
			
			if(relationship.getType().equalsIgnoreCase(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE)) {
				oneToOneRelationships.add(relationship);
			}
		}
	
		return oneToOneRelationships.iterator();
	}
	
	/**
	 * Get one to many relationship object.
	 * @return Iterator of relationship objects.
	 */
	public Iterator<Relationship> getOneToManyRelationships() {

		Collection<Relationship> oneToManyRelationships = new ArrayList<Relationship>();
		Collection<Relationship> relationships = relationshipsBasedOnRefer.values();

		Iterator<Relationship> relationshipsIterator = relationships.iterator();
		while(relationshipsIterator.hasNext()) {
			Relationship relationship = relationshipsIterator.next();
			
			if(relationship.getType().equalsIgnoreCase(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY)) {
				oneToManyRelationships.add(relationship);
			}
		}
	
		return oneToManyRelationships.iterator();
	}
	
	/**
	 * Get many to one relationship object.
	 * @return Iterator of relationship objects.
	 */
	public Iterator<Relationship> getManyToOneRelationships() {
		
		Collection<Relationship> manyToOneRelationships = new ArrayList<Relationship>();
		Collection<Relationship> relationships = relationshipsBasedOnRefer.values();

		Iterator<Relationship> relationshipsIterator = relationships.iterator();
		while(relationshipsIterator.hasNext()) {
			Relationship relationship = relationshipsIterator.next();
			
			if(relationship.getType().equalsIgnoreCase(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_ONE)) {
				manyToOneRelationships.add(relationship);
			}
		}
	
		return manyToOneRelationships.iterator();
	}
	
	/**
	 * Get many to many relationship object.
	 * @return Iterator of relationship objects.
	 */
	public Iterator<Relationship> getManyToManyRelationships() {
		
		Collection<Relationship> manyToManyRelationships = new ArrayList<Relationship>();
		Collection<Relationship> relationships = relationshipsBasedOnRefer.values();

		Iterator<Relationship> relationshipsIterator = relationships.iterator();
		while(relationshipsIterator.hasNext()) {
			Relationship relationship = relationshipsIterator.next();
			
			if(relationship.getType().equalsIgnoreCase(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY)) {
				manyToManyRelationships.add(relationship);
			}
		}
	
		return manyToManyRelationships.iterator();
	}
	
	/**
	 * Add relationship object.
	 * @param relationship Relationship object.
	 */
	public void addRelationship(Relationship relationship) {
		this.relationshipsBasedOnRefer.put(relationship.getRefer(), relationship);
		this.relationshipsBasedOnReferTo.put(relationship.getReferTo(), relationship);
	}

	
	/**
	 * Get all Properties defined in descriptor.
	 * @return All Property Values.
	 */
	public Iterator<String> getProperties() {
		return this.properties.keySet().iterator();
	}
	
	/**
	 * Get Property based on name provided.
	 * @param name Name of Property.
	 * @return Property value.
	 */
	public String getProperty(String name) {
		return this.properties.get(name);
	}

	/**
	 * Check whether Property exist or not.
	 * @param name Name of Property.
	 * @return true/false, TRUE if property exist, FALSE if property does not exist.
	 */
	public boolean containProperty(String name) {
		return this.properties.containsKey(name);
	}
	
	/**
	 * Add Property in property pool.
	 * @param name Name of Property.
	 * @param value value of Property.
	 */
	public void addProperty(String name, String value) {
		this.properties.put(name, value);
	}
	
	/**
	 * Remove Property from property pool.
	 * @param name Name of Property.
	 */
	public void removeProperty(String name) {
		this.properties.remove(name);
	}
	
	
	
	/**
	 * Exposes methods to GET and SET Column information as per define in EntityDescriptor.xml file by application.
	<p>
		<pre>
		
Example:
	{@code

    	<!-- Optional Field -->
    <attributes>
        
	    <!-- Column Properties Required Under This Table -->
	    
			<!-- Optional Field -->
		<attribute>
		    
			    <!-- Mandatory Field -->
					<!-- COLUMN_NAME: Mandatory Field -->
   		    <property name="column_name">column_name_of_table</property>
		    			
    		    <!-- Mandatory Field -->
					<!-- VARIABLE_NAME: Mandatory Field -->
		    <property name="variable_name">class_variable_name</property>
		    		    
			    <!-- Mandatory Field -->
			<property name="type">java_variable_data_type</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="primary_key">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="not_null">true/false</property>
			
				<!-- Optional Field (Default is false) -->
			<property name="unique">true/false</property>
			
				<!-- Optional Field -->
			<property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
			
				<!-- Optional Field -->
			<property name="default">default_value_of_column (Eg: 0.1)</property>
		
		</attribute>		

    </attributes>
	}
	
		</pre>
	</p>
	 *
	 */
	public static class Attribute implements IDescriptor {
		
		private String getterMethodName = null;
		private String setterMethodName = null;

		private Map<String, String> properties = new HashMap<String, String> ();
		
		
		/**
		 * Get variable name.
		 */
		public String getVariableName() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME);
		}
		
		/**
		 * Set variable name as per defined in EntityDescriptor.core.xml file.
		 * @param variableName Name of variable.
		 */
		public void setVariableName(final String variableName) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME, variableName);
		}
		
		/**
		 * Get column name.
		 * @return Name Of Column. 
		 */
		public String getColumnName() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME);
		}
		
		/**
		 * Set column name as per defined in EntityDescriptor.core.xml file.
		 * @param columnName Name of column name.
		 */
		public void setColumnName(final String columnName) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_COLUMN_NAME, columnName);
		}
		
		/**
		 * Get type of column.
		 * @return Type of column.
		 */
		public String getType() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE);
		}
		
		/**
		 * Set type of column as per defined in EntityDescriptor.core.xml file.
		 * @param type Type of column.
		 */
		public void setType(final String type) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_TYPE, type);
		}
		
		/**
		 * Get mapped class column getter method name.
		 * @return Mapped class column getter method name.
		 */
		public String getGetterMethodName() {
			return this.getterMethodName;
		}
		
		/**
		 * Set mapped class column getter method name.
		 * @param getMethodName Mapped class coumn getter method name.
		 */
		public void setGetterMethodName(final String getMethodName) {
			this.getterMethodName = getMethodName;
		}
		
		/**
		 * Get mapped class column setter method name.
		 * @return Mapped class column setter method name.
		 */
		public String getSetterMethodName() {
			return this.setterMethodName;
		}
		
		/**
		 * Set mapped class column setter method name.
		 * @param setMethodName Mapped class column setter method name.
		 */
		public void setSetterMethodName(final String setMethodName) {
			this.setterMethodName = setMethodName;
		}
		
		/**
		 * Get default value of column.
		 * @return Default value of column.
		 */
		public String getDefaultValue() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE);
		}
		
		/**
		 * Set default value of column as per defined in EntityDescriptor.core.xml file.
		 * @param defaultValue Default value of column.
		 */
		public void setDefaultValue(final String defaultValue) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_DEFAULT_VALUE, defaultValue);
		}
		
		/**
		 * Get check constraint of column.
		 * @return Check constraint of column.
		 */
		public String getCheck() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK);
		}
		
		/**
		 * Set check constraint of column as per defined in EntityDescriptor.core.xml file.
		 * @param check Check constraint.
		 */
		public void setCheck(final String check) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_CHECK, check);
		}
		
		/**
		 * Check whether column is primary key.
		 * @return TRUE: If column is primary key, FALSE: If column is not primary key.
		 */
		public boolean isPrimaryKey() {
			
			String primaryKey = this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY);
			if(primaryKey == null || primaryKey.length() <= 0) {
				return false;
			} else if(primaryKey != null && primaryKey.length() > 0 && primaryKey.equalsIgnoreCase("true")) {
				return true;
			}
			
			return false;
		}
		
		/**
		 * Set column as primary key or not.
		 * @param primaryKey TRUE: If column is primary key, FALSE: If column is not primary key.
		 */
		public void setPrimaryKey(final boolean primaryKey) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_PRIMARY_KEY, Boolean.toString(primaryKey));
		}
		
		/**
		 * Check whether column is unique or not.
		 * @return TRUE: If column is unique, FALSE: If column is not unique.
		 */
		public boolean isUnique() {
			
			String unique = this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE);
			if(unique == null || unique.length() <= 0) {
				return false;
			} else if(unique != null && unique.length() > 0 && unique.equalsIgnoreCase("true")) {
				return true;
			}
			
			return false;
		}
		
		/**
		 * Set whether column is unique or not.
		 * @param isUnique TRUE: If column is unique, FALSE: If column is not unique
		 */
		public void setUnique(final boolean isUnique) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_UNIQUE, Boolean.toString(isUnique));
		}
		
		/**
		 * Check whether column value can be not or not.
		 * @return TRUE: If column value can be null, FALSE: If column value can not be null.
		 */
		public boolean isNotNull() {
			
			String notNull = this.properties.get(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL);
			if(notNull == null || notNull.length() <= 0) {
				return false;
			} else if(notNull != null && notNull.length() > 0 && notNull.equalsIgnoreCase("true")) {
				return true;
			}
			
			return false;
		}
		
		/**
		 * Set whether column can be null or not.
		 * @param isNotNull TRUE: If column value can be null, FALSE: If column value can not be null.
		 */
		public void setNotNull(final boolean isNotNull) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_ATTRIBUTE_NOT_NULL, Boolean.toString(isNotNull));
		}
		
		/**
		 * Get all Properties defined in descriptor.
		 * @return All Property Values.
		 */
		public Iterator<String> getProperties() {
			return this.properties.keySet().iterator();
		}
		
		/**
		 * Get Property based on name provided.
		 * @param name Name of Property.
		 * @return Property value.
		 */
		public String getProperty(String name) {
			return this.properties.get(name);
		}

		/**
		 * Check whether Property exist or not.
		 * @param name Name of Property.
		 * @return true/false, TRUE if property exist, FALSE if property does not exist.
		 */
		public boolean containProperty(String name) {
			return this.properties.containsKey(name);
		}
		
		/**
		 * Add Property in property pool.
		 * @param name Name of Property.
		 * @param value value of Property.
		 */
		public void addProperty(String name, String value) {
			this.properties.put(name, value);
		}
		
		/**
		 * Remove Property from property pool.
		 * @param name Name of Property.
		 */
		public void removeProperty(String name) {
			this.properties.remove(name);
		}
	}

	
	/**
	 * Exposes methods to GET and SET Reference Map information as per define in EntityDescriptor.xml file by application.
	<p>
		<pre>
	
Example:
	{@code
		<!-- Optional Field -->
    <indexes>
        
		<!-- Index Properties -->
		<index>
		    
			    <!-- Mandatory Field -->
			    	<!-- NAME OF INDEX -->
		    <property name="name">name_of_index</property>
		    
			    <!-- Mandatory Field -->
					<!-- UNIQUE: Optional Field (Default is false) -->
		    <property name="unique">true/false</property>
		    
		    	<!-- Optional Field -->
		    		<!-- Name of the column -->
		    <property name="column">column_name_needs_to_add</property>
		    
		</index>
        
    </indexes>
	}
	
		</pre>
	</p>
	 *
	 */
	public static class Index implements IDescriptor {
		
		private Map<String, String> properties = new HashMap<String, String> ();
		private Collection<String> columns = new LinkedList<String> ();

		private boolean unique;
	
		/**
		 * Get index name.
		 * @return Index Name.
		 */
		public String getName() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_INDEX_NAME);
		}
		
		/**
		 * Set index name as per defined in EntityDescriptor.xml file.
		 * @param name Index Name.
		 */
		public void setName(final String name) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_INDEX_NAME, name);
		}
		
		/**
		 * Check whether index should be unique or not.
		 * @return TRUE: If index is unique, FALSE: If index is not unqiue.
		 */
		public boolean isUnique() {
			
			String unique = this.properties.get(Constants.ENTITY_DESCRIPTOR_INDEX_UNIQUE);
			if(unique == null || unique.length() <= 0) {
				return false;
			} else if(unique != null && unique.length() > 0 && unique.equalsIgnoreCase("true")) {
				return true;
			}
			
			return false;
		}
		
		/**
		 * Set whether unqiue is unique or not.
		 * @param unique TRUE: If index is unique, FALSE: If index is not unique.
		 */
		public void setUnique(final boolean unique) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_INDEX_UNIQUE, Boolean.toString(unique));
		}	
		
		/**
		 * Check whether index contain column or not.
		 * @param column Name of column.
		 * @return TRUE: If index contains column, FALSE: If index does not contain column.
		 */
		public boolean containsColumn(final String column) {
			return this.columns.contains(column);
		}
		
		/**
		 * Get all columns.
		 * @return Iterator which contain all columns.
		 */
		public Iterator<String> getColumns() {
			return this.columns.iterator();
		}
		
		/**
		 * Add column to index.
		 * @param column Name of column.
		 */
		public void addColumn(final String column) {
			this.columns.add(column);
		}

		/**
		 * Remove column from index.
		 * @param column Name of column.
		 */
		public void removeColumn(final String column) {
			this.columns.remove(column);
		}
		
		public Iterator<String> getProperties() {
			return this.properties.keySet().iterator();
		}

		/**
		 * Get Property based on name provided.
		 * @param name Name of Property.
		 * @return Property value.
		 */
		public String getProperty(String name) {
			return this.properties.get(name);
		}

		/**
		 * Check whether Property exist or not.
		 * @param name Name of Property.
		 * @return true/false, TRUE if property exist, FALSE if property does not exist.
		 */
		public boolean containProperty(String name) {
			return this.properties.containsKey(name);
		}
		
		/**
		 * Add Property in property pool.
		 * @param name Name of Property.
		 * @param value value of Property.
		 */
		public void addProperty(String name, String value) {
			this.properties.put(name, value);
		}
		
		/**
		 * Remove Property from property pool.
		 * @param name Name of Property.
		 */
		public void removeProperty(String name) {
			this.properties.remove(name);
		}
	}

	
	/**
	 * Contains relationship details.
	 * 
		<p>
		<pre>
	
Example:
	{@code
	
	  	<!-- Map Relationship Properties -->
				
		<!-- Optional Field's -->	
	<relationships>
		    
	    <relationship>
	        
	        	<!-- Mandatory Field -->
	        		<!-- Type of Relationship -->
	        <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER -->
	        <property name="refer">class_variable_name</property>
	        
	        	<!-- Mandatory Field -->
	        		<!-- REFER TO -->
	        <property name="refer_to">map_to_class_name</property>
	            
	        	<!-- Optional Field -->
	        <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>    
	            
	        	<!-- Optional Field -->    
	        <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>    
	            
				<!-- Optional Field (Default is false) -->
	       	<property name="load">true/false</property>	            
	        
	    </relationship>
	    
	</relationships>
	
	}
	
		</pre>
	</p>
	 * 
	 */
	public static class Relationship implements IDescriptor {
		
		private String getterReferMethodName = null;
		private String setterReferMethodName = null;
		
		private Map<String, String> properties = new HashMap<String, String> ();
		
		private EntityDescriptor referedEntityDescriptor = null;
		
		/**
		 * Get relationship type.
		 * @return Type of relationship.
		 */
		public String getType() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE);
		}
		
		/**
		 * Set relationship type.
		 * @param type Type of relationship.
		 */
		public void setType(String type) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE, type);
		}
		
		/**
		 * Get refer.
		 * @return Name of refer.
		 */
		public String getRefer() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER);
		}
		
		/**
		 * Set refer.
		 * @param refer Name of refer.
		 */
		public void setRefer(String refer) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER, refer);
		}
		
		/**
		 * Get refer to.
		 * @return Name of refer to.
		 */
		public String getReferTo() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO);
		}

		/**
		 * Set refer to.
		 * @param referTo Name of refer to.
		 */
		public void setReferTo(String referTo) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_REFER_TO, referTo);
		}
		
		/**
		 * Get on update.
		 * @return Action on update.
		 */
		public String getOnUpdate() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE);
		}
		
		/**
		 * Set on update.
		 * @param onUpdate Action on update.
		 */
		public void setOnUpdate(String onUpdate) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_UPDATE, onUpdate);
		}
		
		/**
		 * Get on delete.
		 * @return Action on delete.
		 */
		public String getOnDelete() {
			return this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE);
		}
		
		/**
		 * Set on delete.
		 * @param onDelete Action on delete.
		 */
		public void setOnDelete(String onDelete) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_ON_DELETE, onDelete);
		}
		
		/**
		 * Get getter refer method name.
		 * @return Getter refer method name.
		 */
		public String getGetterReferMethodName() {
			return this.getterReferMethodName;
		}
		
		/**
		 * Set getter refer method name.
		 * @param getterReferMethodName Name of getter refer method name.
		 */
		public void setGetterReferMethodName(String getterReferMethodName) {
			this.getterReferMethodName = getterReferMethodName;
		}
		
		/**
		 * Get setter refer method name.
		 * @return Name of setter refer method name.
		 */
		public String getSetterReferMethodName() {
			return this.setterReferMethodName;
		}
		
		/**
		 * Set setter refer method name.
		 * @param setterReferMethodName Name of setter refer name.
		 */
		public void setSetterReferMethodName(String setterReferMethodName) {
			this.setterReferMethodName = setterReferMethodName;
		}
		
		/**
		 * Check whether load property value is set to TRUE/FASLE.
		 * @return TRUE: If load property value is set to true; FALSE: If load property value is set to false.
		 */
		public boolean isLoad() {
			
			String load = this.properties.get(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD);
			if(load == null || load.length() <= 0) {
				return false;
			} else if(load != null && load.length() > 0 && load.equalsIgnoreCase("true")) {
				return true;
			}
			
			return false;
		}
		
		/**
		 * Set load property value.
		 * @param load TRUE: If load property value is true; FALSE: If load property value is false.
		 */
		public void setLoad(boolean load) {
			this.properties.put(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_LOAD, Boolean.toString(load));
		}
		
		/**
		 * Get all Properties defined in descriptor.
		 * @return All Property Values.
		 */
		public Iterator<String> getProperties() {
			return this.properties.keySet().iterator();
		}
		
		/**
		 * Get Property based on name provided.
		 * @param name Name of Property.
		 * @return Property value.
		 */
		public String getProperty(String name) {
			return this.properties.get(name);
		}

		/**
		 * Check whether Property exist or not.
		 * @param name Name of Property.
		 * @return true/false, TRUE if property exist, FALSE if property does not exist.
		 */
		public boolean containProperty(String name) {
			return this.properties.containsKey(name);
		}
		
		/**
		 * Add Property in property pool.
		 * @param name Name of Property.
		 * @param value value of Property.
		 */
		public void addProperty(String name, String value) {
			this.properties.put(name, value);
		}
		
		/**
		 * Remove Property from property pool.
		 * @param name Name of Property.
		 */
		public void removeProperty(String name) {
			this.properties.remove(name);
		}
		
		
		/**
		 * Get entity descriptor object.
		 * @return EntityDescriptor object.
		 */
		public EntityDescriptor getReferedEntityDescriptor() {
			return this.referedEntityDescriptor;
		}
		
		/**
		 * Set refered entity descriptor object.
		 * @param referedEntityDescriptor EntityDescriptor object.
		 */
		public void setReferedEntityDescriptor(EntityDescriptor referedEntityDescriptor) {
			this.referedEntityDescriptor = referedEntityDescriptor;
		}
	}
}
