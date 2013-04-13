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
 * Exposes methods to GET and SET Database Descriptor information as per define in DatabaseDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code

	<database-descriptor>
	
		<property name="database_name">SIMINOV-TEMPLATE</property>
		<property name="description">Siminov Template Database Config</property>
		<property name="is_locking_required">true</property>
		<property name="external_storage">false</property>

		<!-- Attributes Are Used To Pass Additional Parameters To Database -->
			<!-- Optional Field -->
		<attributes>
		    <attribute name="name_of_attribute">value_of_attribute</attribute>
		</attributes>

		
		<!-- Database Mappings -->
			<database-mappings>
				<database-mapping path="Liquor-Mappings/Liquor.core.xml" />
				<database-mapping path="Liquor-Mappings/LiquorBrand.core.xml" />
			</database-mappings>
	
			 	<!-- OR -->

			<database-mappings>
				<database-mapping path="com.core.template.model.Liquor" />
				<database-mapping path="com.core.template.model.LiquorBrand" />
			</database-mappings>
		

		<!-- Libraries -->
		<libraries>
			<library>com.core.library.template.resources</library>
		</libraries>
				
	</database-descriptor>

	}
	
		</pre>
	</p>
*/
public class DatabaseDescriptor {
	
	private Map<String, String> properties = new HashMap<String, String> ();
	
	private Collection<String> databaseMappingPaths = new ConcurrentLinkedQueue<String> ();
	private Collection<String> libraryPaths = new ConcurrentLinkedQueue<String> ();

	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnTableName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnClassName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	private Map<String, DatabaseMappingDescriptor> databaseMappingsBasedOnPath = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	
	private Map<String, LibraryDescriptor> libraryDescriptorsBasedOnName = new HashMap<String, LibraryDescriptor>();
	private Map<String, LibraryDescriptor> libraryDescriptorsBasedOnPath = new HashMap<String, LibraryDescriptor>();

	/**
	 * Get database descriptor name as defined in DatabaseDescriptor.si.xml file.
	 * @return Database Descriptor Name.
	 */
	public String getDatabaseName() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME);
	}
	
	/**
	 * Set database descriptor name as per defined in DatabaseDescriptor.si.xml file.
	 * @param databaseName Database Descriptor Name.
	 */
	public void setDatabaseName(final String databaseName) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME, databaseName);
	}
	
	public String getType() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_TYPE);
	}
	
	public void setType(String type) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_TYPE, type);
	}
	
	/**
	 * Get description as per defined in DatabaseDescriptor.si.xml file.
	 * @return Description defined in DatabaseDescriptor.si.xml file.
	 */
	public String getDescription() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_TYPE);
	}
	
	/**
	 * Set description as per defined in DatabaseDescritor.xml file.
	 * @param description Description defined in DatabaseDescriptor.si.xml file.
	 */
	public void setDescription(final String description) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_DESCRIPTION, description);
	}
	
	
	/**
	 * Check whether database needs to be stored on SDCard or not.
	 * @return TRUE: If external_storage defined as true in DatabaseDescriptor.si.xml file, FALSE: If external_storage defined as false in DatabaseDescritor.xml file.
	 */
	public boolean isExternalStorageEnable() {
		String externalStorage = this.properties.get(Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE);
		if(externalStorage == null || externalStorage.length() <= 0) {
			return false;
		} else if(externalStorage != null && externalStorage.length() > 0 && externalStorage.equalsIgnoreCase("true")) {
			return true;
		}

		
		return false;
	}
	
	/**
	 * Set the external storage value as per defined in DatabaseDescriptor.si.xml file.
	 * @param isExternalStorageEnable (true/false) External Storage Enable Or Not.
	 */
	public void setExternalStorageEnable(final boolean isExternalStorageEnable) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE, Boolean.toString(isExternalStorageEnable));
	}
	
	/**
	 * Check whether database transactions to make multi-threading safe or not.
	 * @return TRUE: If locking is required as per defined in DatabaseDescriptor.si.xml file, FALSE: If locking is not required as per defined in DatabaseDescriptor.si.xml file.
	 */
	public boolean isLockingRequired() {
		String isLockingRequired = this.properties.get(Constants.DATABASE_DESCRIPTOR_IS_LOCKING_REQUIRED);
		if(isLockingRequired == null || isLockingRequired.length() <= 0) {
			return false;
		} else if(isLockingRequired != null && isLockingRequired.length() > 0 && isLockingRequired.equalsIgnoreCase("true")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set database locking as per defined in DatabaseDescriptor.si.xml file.
	 * @param isLockingRequired (true/false) database locking as per defined in DatabaseDescriptor.si.xml file.
	 */
	public void setLockingRequired(final boolean isLockingRequired) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_IS_LOCKING_REQUIRED, Boolean.toString(isLockingRequired));
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
	public Iterator<DatabaseMappingDescriptor> getDatabaseMappings() {
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
	public DatabaseMappingDescriptor getDatabseMappingBasedOnPath(final String databaseMappingPath) {
		return this.databaseMappingsBasedOnPath.get(databaseMappingPath);
	}
	
	/**
	 * Add database mapping object in respect to database mapping path.
	 * @param databaseMappingPath Database Mapping Path.
	 * @param databaseMapping Database Mapping object.
	 */
	public void addDatabaseMapping(final String databaseMappingPath, final DatabaseMappingDescriptor databaseMapping) {
		this.databaseMappingsBasedOnPath.put(databaseMappingPath, databaseMapping);
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
	
	/**
	 * Check whether library exists or not based on library name provided.
	 * @param libraryName Name of library as per defined in LibraryDescriptor.si.xml file.
	 * @return
	 */
	public boolean containsLibraryBasedOnName(final String libraryName) {
		return this.libraryDescriptorsBasedOnName.containsKey(libraryName);
	}

	/**
	 * Check whether library exists or not based on library path provided.
	 * @param libraryPath Path of library as per defined in DatabaseDescriptor.si.xml file.
	 * @return
	 */
	public boolean containsLibraryBasedOnPath(final String libraryPath) {
		return this.libraryDescriptorsBasedOnPath.containsKey(libraryPath);
	}
	
	/**
	 * Get all library paths as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contains all library paths.
	 */
	public Iterator<String> getLibraryPaths() {
		return this.libraryPaths.iterator();
	}
	
	/**
	 * Add library path as per defined in DatabaseDescriptor.si.xml file.
	 * @param libraryPath Library path defined in DatabaseDescriptor.si.xml file.
	 */
	public void addLibraryPath(final String libraryPath) {
		this.libraryPaths.add(libraryPath);
	}
	
	/**
	 * Get all library descriptor paths contained.
	 * @return Iterator which contains all library descriptor objects.
	 */
	public Iterator<LibraryDescriptor> getLibraryDescriptors() {
		return this.libraryDescriptorsBasedOnName.values().iterator();
	}

	/**
	 * Get all library descriptor objects in sorted order as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contains all library descriptor objects in sorted order.
	 */
	public Iterator<LibraryDescriptor> orderedLibraryDescriptors() {
		Collection<LibraryDescriptor> orderedLibraryDescriptors = new LinkedList<LibraryDescriptor> ();
		
		for(String libraryName : libraryPaths) {
			orderedLibraryDescriptors.add(getLibraryDescriptorBasedOnPath(libraryName));
		}
		
		return orderedLibraryDescriptors.iterator();
	}

	/**
	 * Get library descriptor object based on library descriptor path.
	 * @param libraryPath Library Descriptor path.
	 * @return
	 */
	public LibraryDescriptor getLibraryDescriptorBasedOnPath(final String libraryPath) {
		return this.libraryDescriptorsBasedOnPath.get(libraryPath);
	}

	/**
	 * Add library object in respect to library descriptor path.
	 * @param libraryPath Library Descriptor Path.
	 * @param libraryDescriptor Library Descriptor Object.
	 */
	public void addLibrary(final String libraryPath, final LibraryDescriptor libraryDescriptor) {
		this.libraryDescriptorsBasedOnPath.put(libraryPath, libraryDescriptor);
		this.libraryDescriptorsBasedOnName.put(libraryDescriptor.getName(), libraryDescriptor);
	}
	
	/**
	 * Remove library descriptor based on library path defined in DatabaseDescriptor.si.xml file.
	 * @param libraryPath Library Descriptor Path.
	 */
	public void removeLibraryBasedOnPath(final String libraryPath) {
		this.libraryPaths.remove(libraryPath);
		
		LibraryDescriptor libraryDescriptor = this.libraryDescriptorsBasedOnPath.get(libraryPath);
		this.libraryDescriptorsBasedOnPath.remove(libraryPath);
		
		this.libraryDescriptorsBasedOnName.values().remove(libraryDescriptor);
	}
	
	/**
	 * Remove library descriptor based on library name defined in LibraryDescriptor.si.xml file.
	 * @param libraryName Library Name.
	 */
	public void removeLibraryBasedOnName(final String libraryName) {
		removeLibrary(this.libraryDescriptorsBasedOnName.get(libraryName));
	}
	
	/**
	 * Remove library descriptor object based on library descriptor object.
	 * @param removeLibraryDescriptor Library Descriptor object.
	 */
	public void removeLibrary(final LibraryDescriptor removeLibraryDescriptor) {
		LibraryDescriptor libraryDescriptor = this.libraryDescriptorsBasedOnName.get(removeLibraryDescriptor.getName());
		Collection<String> keys = this.libraryDescriptorsBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			LibraryDescriptor descriptor = this.libraryDescriptorsBasedOnPath.get(key);
			if(libraryDescriptor == descriptor) {
				keyMatched = key;
				found = true;
				break;
			}
		}
		
		if(found) {
			removeLibraryBasedOnPath(keyMatched);
		}
	}
	
	/**
	 * Check whether library is needed by Database Descriptor or not.
	 * @return TRUE: If library exists, FALSE: If no library exists.
	 */
	public boolean isLibrariesNeeded() {
		return this.libraryPaths.size() > 0 ? true : false;
	}
	
}
