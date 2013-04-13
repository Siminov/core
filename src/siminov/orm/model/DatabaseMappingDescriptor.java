/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution|support@siminov.com]
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

package siminov.orm.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import siminov.orm.Constants;


/**
 * Exposes methods to GET and SET Library Descriptor information as per define in DatabaseDescriptor.si.xml or LibraryDescriptor.si.xml  file by application.
	<p>
		<pre>
		
Example:
	{@code

	<database-mapping>
	
		<table table_name="LIQUOR" class_name="siminov.orm.template.model.Liquor">
			
			<column variable_name="liquorType" column_name="LIQUOR_TYPE">
				<property name="type">TEXT</property>
				<property name="primary_key">true</property>
				<property name="not_null">true</property>
				<property name="unique">true</property>
			</column>		
	
			<column variable_name="description" column_name="DESCRIPTION">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="history" column_name="HISTORY">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="link" column_name="LINK">
				<property name="type">TEXT</property>
				<property name="default">www.wikipedia.org</property>
			</column>
	
			<column variable_name="alcholContent" column_name="ALCHOL_CONTENT">
				<property name="type">TEXT</property>
			</column>
	
			<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
				<column>HISTORY</column>
			</index>
										
		</table>
	
	</database-mapping>		
		
		}
	
		</pre>
	</p>
 *
 */
public class DatabaseMappingDescriptor {

	private String tableName = null;
	private String className = null;
	
	private Map<String, Column> columnsBasedOnColumnNames = new LinkedHashMap<String, Column>();
	private Map<String, Column> columnsBasedOnVariableNames = new LinkedHashMap<String, Column>();
	
	private Map<String, Index> indexes = new LinkedHashMap<String, Index>();

	private Map<String, Relationship> relationshipsBasedOnRefer = new LinkedHashMap<String, Relationship>(); 
	private Map<String, Relationship> relationshipsBasedOnReferTo = new LinkedHashMap<String, Relationship>();
	
	
	/**
	 * Get table name.
	 * @return Name of table.
	 */
	public String getTableName() {
		return this.tableName;
	}
	
	/**
	 * Set table name as per defined in DatabaseMappingDescriptor.si.xml file.
	 * @param tableName Name of table.
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * Get POJO class name.
	 * @return POJO class name.
	 */
	public String getClassName() {
		return this.className;
	}
	
	/**
	 * Set POJO class name as per defined in DatabaseMappingDescriptor.si.xml file.
	 * @param className POJO class name.
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * Check whether column exists based on column name.
	 * @param columnName Name of column.
	 * @return TRUE: If column exists, FALSE: If column do not exists.
	 */
	public boolean containsColumnBasedOnColumnName(final String columnName) {
		return this.columnsBasedOnColumnNames.containsKey(columnName);
	}
	
	/**
	 * Check whether column exists based on variable name.
	 * @param variableName Name of variable.
	 * @return TRUE: If column exists, FALSE: If column do not exists.
	 */
	public boolean containsColumnBasedOnVariableName(final String variableName) {
		return this.columnsBasedOnVariableNames.containsKey(variableName);
	}
	
	/**
	 * Get column based on column name.
	 * @param columnName Name of column name.
	 * @return Column object.
	 */
	public Column getColumnBasedOnColumnName(final String columnName) {
		return this.columnsBasedOnColumnNames.get(columnName);
	}

	/**
	 * Get column based on variable name.
	 * @param variableName Name of variable.
	 * @return Column object.
	 */
	public Column getColumnBasedOnVariableName(final String variableName) {
		return this.columnsBasedOnVariableNames.get(variableName);
	}
	
	/**
	 * Get all column names.
	 * @return Iterator of all column names.
	 */
	public Iterator<String> getColumnNames() {
		return this.columnsBasedOnColumnNames.keySet().iterator();
	}
	
	/**
	 * Get all columns.
	 * @return Iterator of all columns.
	 */
	public Iterator<Column> getColumns() {
		return this.columnsBasedOnVariableNames.values().iterator();
	}

	/**
	 * Add column to DatabaseMapping object.
	 * @param column Column object.
	 */
	public void addColumn(final Column column) {
		this.columnsBasedOnVariableNames.put(column.getVariableName(), column);
		this.columnsBasedOnColumnNames.put(column.getColumnName(), column);
	}
	
	/**
	 * Remove column based on variable name.
	 * @param variableName Name of variable.
	 */
	public void removeColumnBasedOnVariableName(final String variableName) {
		removeColumn(getColumnBasedOnVariableName(variableName));
	}
	
	/**
	 * Remove column based on column name.
	 * @param columnName Name of column.
	 */
	public void removeColumnBasedOnColumnName(final String columnName) {
		removeColumn(getColumnBasedOnColumnName(columnName));
	}
	
	/**
	 * Remove column based on column object.
	 * @param column Column object which need to be removed.
	 */
	public void removeColumn(final Column column) {
		this.columnsBasedOnColumnNames.values().remove(column);
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
	 * Add index to DatabaseMapping object.
	 * @param index Index object.
	 */
	public void addIndex(final Index index) {
		this.indexes.put(index.getName(), index);
	}
	
	/**
	 * Remove index object.
	 * @param indexName Name of index.
	 */
	public void removeIndex(final String indexName) {
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
			
			if(relationship.getRelationshipType().equalsIgnoreCase(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE)) {
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
			
			if(relationship.getRelationshipType().equalsIgnoreCase(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
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
			
			if(relationship.getRelationshipType().equalsIgnoreCase(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE)) {
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
			
			if(relationship.getRelationshipType().equalsIgnoreCase(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
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
		this.relationshipsBasedOnReferTo.put(relationship.getRefer(), relationship);
	}
	
	/**
	 * Exposes methods to GET and SET Column information as per define in DatabaseMappingDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code

	<database-mapping>
	
		<table table_name="LIQUOR" class_name="siminov.orm.template.model.Liquor">
			
			<column variable_name="liquorType" column_name="LIQUOR_TYPE">
				<property name="type">TEXT</property>
				<property name="primary_key">true</property>
				<property name="not_null">true</property>
				<property name="unique">true</property>
			</column>		
			
	</database-mapping>
	}
	
		</pre>
	</p>
	 *
	 */
	public static class Column {
		private String variableName = null;
		private String columnName = null;

		private String getterMethodName = null;
		private String setterMethodName = null;

		private Map<String, String> properties = new HashMap<String, String> ();
		
		
		/**
		 * Get variable name.
		 * @return
		 */
		public String getVariableName() {
			return this.variableName;
		}
		
		/**
		 * Set variable name as per defined in DatabaseMapping.core.xml file.
		 * @param variableName Name of variable.
		 */
		public void setVariableName(final String variableName) {
			this.variableName = variableName;
		}
		
		/**
		 * Get column name.
		 * @return Name Of Column. 
		 */
		public String getColumnName() {
			return this.columnName;
		}
		
		/**
		 * Set column name as per defined in DatabaseMapping.core.xml file.
		 * @param columnName Name of column name.
		 */
		public void setColumnName(final String columnName) {
			this.columnName = columnName;
		}
		
		/**
		 * Get type of column.
		 * @return Type of column.
		 */
		public String getType() {
			return this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_TYPE);
		}
		
		/**
		 * Set type of column as per defined in DatabaseMapping.core.xml file.
		 * @param type Type of column.
		 */
		public void setType(final String type) {
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_TYPE, type);
		}
		
		/**
		 * Get POJO class column getter method name.
		 * @return POJO class column getter method name.
		 */
		public String getGetterMethodName() {
			return this.getterMethodName;
		}
		
		/**
		 * Set POJO class column getter method name.
		 * @param getMethodName POJO class coumn getter method name. 
		 */
		public void setGetterMethodName(final String getMethodName) {
			this.getterMethodName = getMethodName;
		}
		
		/**
		 * Get POJO class column setter method name.
		 * @return POJO class column setter method name.
		 */
		public String getSetterMethodName() {
			return this.setterMethodName;
		}
		
		/**
		 * Set POJO class column setter method name.
		 * @param setMethodName POJO class column setter method name.
		 */
		public void setSetterMethodName(final String setMethodName) {
			this.setterMethodName = setMethodName;
		}
		
		/**
		 * Get default value of column.
		 * @return Default value of column.
		 */
		public String getDefaultValue() {
			return this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE);
		}
		
		/**
		 * Set default value of column as per defined in DatabaseMapping.core.xml file.
		 * @param defaultValue Default value of column.
		 */
		public void setDefaultValue(final String defaultValue) {
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_DEFAULT_VALUE, defaultValue);
		}
		
		/**
		 * Get check constraint of column.
		 * @return Check constraint of column.
		 */
		public String getCheck() {
			return this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_CHECK);
		}
		
		/**
		 * Set check constraint of column as per defined in DatabaseMapping.core.xml file.
		 * @param check Check constraint.
		 */
		public void setCheck(final String check) {
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_CHECK, check);
		}
		
		/**
		 * Check whether column is primary key.
		 * @return TRUE: If column is primary key, FALSE: If column is not primary key.
		 */
		public boolean isPrimaryKey() {
			String primaryKey = this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY);
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
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_PRIMARY_KEY, Boolean.toString(primaryKey));
		}
		
		/**
		 * Check whether column is unique or not.
		 * @return TRUE: If column is unique, FALSE: If column is not unique.
		 */
		public boolean isUnique() {
			String unique = this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_UNIQUE);
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
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_UNIQUE, Boolean.toString(isUnique));
		}
		
		/**
		 * Check whether column value can be not or not.
		 * @return TRUE: If column value can be null, FALSE: If column value can not be null.
		 */
		public boolean isNotNull() {
			String notNull = this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_NOT_NULL);
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
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_NOT_NULL, Boolean.toString(isNotNull));
		}
		
		public Iterator<String> getProperties() {
			return this.properties.keySet().iterator();
		}
		
		public String getProperty(String name) {
			return this.properties.get(name);
		}

		public boolean containProperty(String name) {
			return this.properties.containsKey(name);
		}
		
		public void addProperty(String name, String value) {
			this.properties.put(name, value);
		}
		
		public void removeProperty(String name) {
			this.properties.remove(name);
		}
		
	}

	
	/**
	 * Exposes methods to GET and SET Reference Map information as per define in DatabaseMappingDescriptor.si.xml file by application.
	<p>
		<pre>
	
Example:
	{@code
	<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
		<column>HISTORY</column>
	</index>
	}
	
		</pre>
	</p>
	 *
	 */
	public static class Index {
		private String name = null;
		private Collection<String> columns = new LinkedList<String> ();

		private boolean unique;
	
		/**
		 * Get index name.
		 * @return Index Name.
		 */
		public String getName() {
			return this.name;
		}
		
		/**
		 * Set index name as per defined in DatabaseMapping.core.xml file.
		 * @param name Index Name.
		 */
		public void setName(final String name) {
			this.name = name;
		}
		
		/**
		 * Check whether index should be unique or not.
		 * @return TRUE: If index is unique, FALSE: If index is not unqiue.
		 */
		public boolean isUnique() {
			return this.unique;
		}
		
		/**
		 * Set whether unqiue is unique or not.
		 * @param unique TRUE: If index is unique, FALSE: If index is not unique.
		 */
		public void setUnique(final boolean unique) {
			this.unique = unique;
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
		
	}

	/**
	 * Contains relationship details.
	 */
	public static class Relationship {
		
		private String relationshipType = null;
		
		private String refer = null;
		private String referTo = null;
		
		private String onUpdate = null;
		private String onDelete = null;

		private String getterReferMethodName = null;
		private String setterReferMethodName = null;
		
		private Map<String, String> properties = new HashMap<String, String> ();
		
		private DatabaseMappingDescriptor referedDatabaseMappingDescriptor = null;
		
		/**
		 * Get relationship type.
		 * @return Type of relationship.
		 */
		public String getRelationshipType() {
			return this.relationshipType;
		}
		
		/**
		 * Set relationship type.
		 * @param relationshipType Type of relationship.
		 */
		public void setRelationshipType(String relationshipType) {
			this.relationshipType = relationshipType;
		}
		
		/**
		 * Get refer.
		 * @return Name of refer.
		 */
		public String getRefer() {
			return this.refer;
		}
		
		/**
		 * Set refer.
		 * @param refer Name of refer.
		 */
		public void setRefer(String refer) {
			this.refer = refer;
		}
		
		/**
		 * Get refer to.
		 * @return Name of refer to.
		 */
		public String getReferTo() {
			return this.referTo;
		}

		/**
		 * Set refer to.
		 * @param referTo Name of refer to.
		 */
		public void setReferTo(String referTo) {
			this.referTo = referTo;
		}
		
		/**
		 * Get on update.
		 * @return Action on update.
		 */
		public String getOnUpdate() {
			return this.onUpdate;
		}
		
		/**
		 * Set on update.
		 * @param onUpdate Action on update.
		 */
		public void setOnUpdate(String onUpdate) {
			this.onUpdate = onUpdate;
		}
		
		/**
		 * Get on delete.
		 * @return Action on delete.
		 */
		public String getOnDelete() {
			return this.onDelete;
		}
		
		/**
		 * Set on delete.
		 * @param onDelete Action on delete.
		 */
		public void setOnDelete(String onDelete) {
			this.onDelete = onDelete;
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
			String load = this.properties.get(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD);
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
			this.properties.put(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_LOAD, Boolean.toString(load));
		}
		
		public Iterator<String> getProperties() {
			return this.properties.keySet().iterator();
		}
		
		public String getProperty(String name) {
			return this.properties.get(name);
		}

		public boolean containProperty(String name) {
			return this.properties.containsKey(name);
		}
		
		public void addProperty(String name, String value) {
			this.properties.put(name, value);
		}
		
		public void removeProperty(String name) {
			this.properties.remove(name);
		}

		
		/**
		 * Get database mapping descriptor object.
		 * @return DatabaseMappingDescriptor object.
		 */
		public DatabaseMappingDescriptor getReferedDatabaseMappingDescriptor() {
			return this.referedDatabaseMappingDescriptor;
		}
		
		/**
		 * Set refered database mapping descriptor object.
		 * @param referedDatabaseMappingDescriptor DatabaseMappingDescriptor object.
		 */
		public void setReferedDatabaseMappingDescriptor(DatabaseMappingDescriptor referedDatabaseMappingDescriptor) {
			this.referedDatabaseMappingDescriptor = referedDatabaseMappingDescriptor;
		}
		
	}
	
}
