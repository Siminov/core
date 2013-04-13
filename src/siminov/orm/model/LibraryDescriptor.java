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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import siminov.orm.Constants;


/**
 * Exposes methods to GET and SET Library Descriptor information as per define in LibraryDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	<library>
	
		<property name="name">SIMINOV LIBRARY TEMPLATE</property>
		<property name="description">Siminov Library Template</property>
	
		<!-- Database Mappings -->
			<database-mappings>
				<database-mapping path="Credential.core.xml" />
			</database-mappings>
	
			 	<!-- OR -->
			 
			<database-mappings>
				<database-mapping path="com.core.library.template.model.Credential" />
			</database-mappings>
		 
	</library>
	}
	
		</pre>
	</p>
 *
 */
public class LibraryDescriptor {

	private Map<String, String> properties = new HashMap<String, String> ();
	
	private Collection<String> databaseMappingPaths = new ConcurrentLinkedQueue<String> ();
	
	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnTableName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnClassName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnPath = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();

	/**
	 * Get library name.
	 * @return
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
	 * @return
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
	 * @param databaseMappingPath Database Mapping path as per defined in Database Descriptor.xml file.
	 * @return Database Mapping object.
	 */
	public DatabaseMappingDescriptor getDatabseMappingBasedOnPath(final String libraryDatabaseMappingPath) {
		return this.databaseMappingsBasedOnPath.get(libraryDatabaseMappingPath);
	}
	
	/**
	 * Add database mapping object in respect to database mapping path.
	 * @param databaseMappingPath Database Mapping Path.
	 * @param databaseMapping Database Mapping object.
	 */
	public void addDatabaseMapping(final String libraryDatabaseMappingPath, final DatabaseMappingDescriptor databaseMapping) {
		this.databaseMappingsBasedOnPath.put(libraryDatabaseMappingPath, databaseMapping);
		this.databaseMappingsBasedOnTableName.put(databaseMapping.getTableName(), databaseMapping);
		this.databaseMappingsBasedOnClassName.put(databaseMapping.getClassName(), databaseMapping);
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
