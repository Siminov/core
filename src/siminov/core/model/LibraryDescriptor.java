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

package siminov.core.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import siminov.core.Constants;


/**
 * Exposes methods to GET and SET Library Descriptor information as per define in LibraryDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	
	<library-descriptor>
	
	    <!-- General Properties Of Library -->
	    
	    <!-- Mandatory Field -->
		<property name="name">name_of_library</property>
		
		<!-- Optional Field -->
		<property name="description">description_of_library</property>
	
		
		
		<!-- Database Mappings Needed Under This Library Descriptor -->
		
		<!-- Optional Field -->
			<!-- Database Mapping Descriptors -->
		<database-mapping-descriptors>
			<database-mapping-descriptor>name_of_database_descriptor.full_path_of_database_mapping_descriptor_file</database-mapping-descriptor>
		</database-mapping-descriptors>
		 
	</library-descriptor>
	
	}
	
		</pre>
	</p>
 *
 */
public class LibraryDescriptor implements IDescriptor {

	protected Map<String, String> properties = new HashMap<String, String> ();
	
	protected Collection<String> databaseMappingPaths = new ConcurrentLinkedQueue<String> ();
	
	protected Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnTableName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	protected Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnClassName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	protected Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnPath = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();


	/**
	 * Get library name.
	 */
	public String getName() {
		return this.properties.get(Constants.LIBRARY_DESCRIPTOR_NAME);
	}
	
	/**
	 * Set library name as per defined in LibraryDescriptor.si.xml
	 * @param name
	 */
	public void setName(final String name) {
		this.properties.put(Constants.LIBRARY_DESCRIPTOR_NAME, name);
	}
	
	/**
	 * Get descriptor as per defined in LibraryDescriptor.si.xml
	 */
	public String getDescription() {
		return this.properties.get(Constants.LIBRARY_DESCRIPTOR_DESCRIPTION);
	}
	
	/**
	 * Set description as per defined in LibraryDescritor.core.xml
	 * @param description
	 */
	public void setDescription(final String description) {
		this.properties.put(Constants.LIBRARY_DESCRIPTOR_DESCRIPTION, description);
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
	 * Check whether database mapping object exists or not, based on table name.
	 * @param tableName Name of table.
	 * @return TRUE: If database mapping exists, FALSE: If database mapping does not exists.
	 */
	public boolean containsDatabaseMappingBasedOnTableName(final String tableName) {
		return this.databaseMappingsBasedOnTableName.containsKey(tableName);
	}
	
	/**
	 * Check whether database mapping object exists or not, based on POJO class name.
	 * @param className POJO class name.
	 * @return TRUE: If database mapping exists, FALSE: If database mapping does not exists.
	 */
	public boolean containsDatabaseMappingBasedOnClassName(final String className) {
		return this.databaseMappingsBasedOnClassName.containsKey(className);
	}

	/**
	 * Get all database mapping paths as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contain all database mapping paths.
	 */
	public Iterator<String> getDatabaseMappingPaths() {
		return this.databaseMappingPaths.iterator();
	}

	/**
	 * Add database mapping path as per defined in DatabaseDescriptor.si.xml file.
	 	<p>
	 		<pre>
	 		
EXAMPLE:
	{@code
	<database-descriptor>
		<database-mappings>
			<database-mapping path="Liquor-Mappings/Liquor.xml" />
			<database-mapping path="Liquor-Mappings/LiquorBrand.xml" />
		</database-mappings>
	</database-descriptor>
	}
	
		</pre>
	</p>
	 
	 * @param databaseMappingPath Database Mapping Path.
	 */
	public void addDatabaseMappingPath(final String databaseMappingPath) {
		this.databaseMappingPaths.add(databaseMappingPath);
	}
	
	/**
	 * Get all database mapping objects contained.
	 * @return All database mapping objects.
	 */
	public Iterator<DatabaseMappingDescriptor> getDatabseMappings() {
		return this.databaseMappingsBasedOnClassName.values().iterator();
	}

	/**
	 * Get database mapping object based on table name.
	 * @param tableName Name of table.
	 * @return DatabaseMapping object based on table name.
	 */
	public DatabaseMappingDescriptor getDatabseMappingBasedOnTableName(final String tableName) {
		return this.databaseMappingsBasedOnTableName.get(tableName);
	}

	/**
	 * Get database mapping object based on POJO class name.
	 * @param className POJO class name.
	 * @return Database Mapping object.
	 */
	public DatabaseMappingDescriptor getDatabseMappingBasedOnClassName(final String className) {
		return this.databaseMappingsBasedOnClassName.get(className);
	}

	/**
	 * Get database mapping object based on path.
	 * @param libraryDatabaseMappingPath Library Database path as per defined in Database Descriptor.xml file.
	 * @return Database Mapping object.
	 */
	public DatabaseMappingDescriptor getDatabseMappingBasedOnPath(final String libraryDatabaseMappingPath) {
		return this.databaseMappingsBasedOnPath.get(libraryDatabaseMappingPath);
	}
	
	/**
	 * Add database mapping object in respect to database mapping path.
	 * @param libraryDatabaseMappingPath Library Database Mapping Path.
	 * @param databaseMappingDescriptor Database Mapping object.
	 */
	public void addDatabaseMapping(final String libraryDatabaseMappingPath, final DatabaseMappingDescriptor databaseMappingDescriptor) {
		this.databaseMappingsBasedOnPath.put(libraryDatabaseMappingPath, databaseMappingDescriptor);
		this.databaseMappingsBasedOnTableName.put(databaseMappingDescriptor.getTableName(), databaseMappingDescriptor);
		this.databaseMappingsBasedOnClassName.put(databaseMappingDescriptor.getClassName(), databaseMappingDescriptor);
	}

	/**
	 * Remove database mapping object based on database mapping path.
	 * @param databaseMappingPath Database Mapping Path.
	 */
	public void removeDatabaseMappingBasedOnPath(final String databaseMappingPath) {
		this.databaseMappingPaths.remove(databaseMappingPath);
		
		DatabaseMappingDescriptor databaseMapping = this.databaseMappingsBasedOnPath.get(databaseMappingPath);
		this.databaseMappingsBasedOnPath.remove(databaseMappingPath);
		
		this.databaseMappingsBasedOnClassName.values().remove(databaseMapping);
		this.databaseMappingsBasedOnTableName.values().remove(databaseMapping);
	}
	
	/**
	 * Remove database mappping object based on POJO class name.
	 * @param className POJO class name.
	 */
	public void removeDatabaseMappingBasedOnClassName(final String className) {
		DatabaseMappingDescriptor databaseMapping = this.databaseMappingsBasedOnClassName.get(className);
		Collection<String> keys = this.databaseMappingsBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			DatabaseMappingDescriptor mapping = this.databaseMappingsBasedOnPath.get(key);
			if(databaseMapping == mapping) {
				keyMatched = key;
				found = true;
				break;
			}
		}
		
		if(found) {
			removeDatabaseMappingBasedOnPath(keyMatched);
		}
	}
	
	/**
	 * Remove database mapping object based on table name.
	 * @param tableName Name of table.
	 */
	public void removeDatabaseMappingBasedOnTableName(final String tableName) {
		DatabaseMappingDescriptor databaseMapping = this.databaseMappingsBasedOnTableName.get(tableName);
		removeDatabaseMappingBasedOnClassName(databaseMapping.getClassName());
	}
	
	/**
	 * Remove database mapping object based on database mapping object.
	 * @param databaseMapping Database Mapping object which needs to be removed.
	 */
	public void removeDatabaseMapping(final DatabaseMappingDescriptor databaseMapping) {
		removeDatabaseMappingBasedOnClassName(databaseMapping.getClassName());
	}
	
	/**
	 * Get all database mapping objects in sorted order. The order will be as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contains all database mapping objects.
	 */
	public Iterator<DatabaseMappingDescriptor> orderedDatabaseMappings() {
		Collection<DatabaseMappingDescriptor> orderedDatabaseMappings = new LinkedList<DatabaseMappingDescriptor> ();
		
		for(String databaseMappingPath : this.databaseMappingPaths) {
			orderedDatabaseMappings.add(getDatabseMappingBasedOnPath(databaseMappingPath));
		}
		
		return orderedDatabaseMappings.iterator();
	}

}
