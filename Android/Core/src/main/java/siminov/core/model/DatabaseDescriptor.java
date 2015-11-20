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
 * Exposes methods to GET and SET Database Descriptor information as per define in DatabaseDescriptor.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	
	<database-descriptor>
	
	    <!-- General Database Descriptor Properties -->
	    
		    <!-- Mandatory Field -->
		<property name="database_name">name_of_database_file</property>
	
			<!-- Optional Field (Default is sqlite)-->
		<property name="type">type_of_database</property>
	
			<!-- Mandatory Field -->
		<property name="version">database_version</property>
				
			<!-- Optional Field -->
		<property name="description">database_description</property>
	
			<!-- Optional Field (Default is false) -->
		<property name="transaction_safe">true/false</property>
		
			<!-- Optional Field (Default is false) -->
		<property name="external_storage">true/false</property>
			
	
	
		<!-- Entity Descriptor Paths Needed Under This Database Descriptor -->
		
			<!-- Optional Field -->
		<entity-descriptors>
			<entity-descriptor>full_path_of_entity_descriptor_file</entity-descriptor>
		</entity-descriptors>
		
	</database-descriptor>

	}
	
		</pre>
	</p>
*/
public class DatabaseDescriptor implements IDescriptor {
	
	protected Map<String, String> properties = new HashMap<String, String> ();
	
	protected Collection<String> entityDescriptorPaths = new ConcurrentLinkedQueue<String> ();

	protected Map<String, EntityDescriptor> entityDescriptorsBasedOnTableName = new ConcurrentHashMap<String, EntityDescriptor>();
	protected Map<String, EntityDescriptor> entityDescriptorsBasedOnClassName = new ConcurrentHashMap<String, EntityDescriptor>();
	protected Map<String, EntityDescriptor> entityDescriptorsBasedOnPath = new ConcurrentHashMap<String, EntityDescriptor>();
	
	/**
	 * Get database descriptor name as defined in DatabaseDescriptor.xml file.
	 * @return Database Descriptor Name.
	 */
	public String getDatabaseName() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME);
	}
	
	/**
	 * Set database descriptor name as per defined in DatabaseDescriptor.xml file.
	 * @param databaseName Database Descriptor Name.
	 */
	public void setDatabaseName(final String databaseName) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_DATABASE_NAME, databaseName);
	}
	
	/**
	 * Get type of database
	 * @return Type of database
	 */
	public String getType() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_TYPE);
	}
	
	/**
	 * Set type of database
	 * @param type Type of database
	 */
	public void setType(String type) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_TYPE, type);
	}

	/**
	 * Get version of database 
	 * @return Version of database
	 */
	public double getVersion() {
		String version = this.properties.get(Constants.DATABASE_DESCRIPTOR_VERSION);
		if(version == null || version.length() <= 0) {
			return 0.0;
		}
		
		return Long.valueOf(version);
	}
	
	/**
	 * Set Version of Application as per defined in ApplicationDescriptor.xml file.
	 * @param version Version of application.
	 */
	public void setVersion(final long version) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_VERSION, Long.toString(version));
	}
	
	
	/**
	 * Get description as per defined in DatabaseDescriptor.xml file.
	 * @return Description defined in DatabaseDescriptor.xml file.
	 */
	public String getDescription() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_DESCRIPTION);
	}
	
	/**
	 * Set description as per defined in DatabaseDescritor.xml file.
	 * @param description Description defined in DatabaseDescriptor.xml file.
	 */
	public void setDescription(final String description) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_DESCRIPTION, description);
	}
	
	
	/**
	 * Check whether database needs to be stored on SDCard or not.
	 * @return TRUE: If external_storage defined as true in DatabaseDescriptor.xml file, FALSE: If external_storage defined as false in DatabaseDescritor.xml file.
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
	 * Set the external storage value as per defined in DatabaseDescriptor.xml file.
	 * @param isExternalStorageEnable (true/false) External Storage Enable Or Not.
	 */
	public void setExternalStorageEnable(final boolean isExternalStorageEnable) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_EXTERNAL_STORAGE, Boolean.toString(isExternalStorageEnable));
	}
	
	/**
	 * Check whether database transactions to make multi-threading safe or not.
	 * @return TRUE: If locking is required as per defined in DatabaseDescriptor.xml file, FALSE: If locking is not required as per defined in DatabaseDescriptor.xml file.
	 */
	public boolean isTransactionSafe() {
		
		String transactionSafe = this.properties.get(Constants.DATABASE_DESCRIPTOR_TRANSACTION_SAFE);
		if(transactionSafe == null || transactionSafe.length() <= 0) {
			return false;
		} else if(transactionSafe != null && transactionSafe.length() > 0 && transactionSafe.equalsIgnoreCase("true")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set database locking as per defined in DatabaseDescriptor.xml file.
	 * @param transactionSafe (true/false) database locking as per defined in DatabaseDescriptor.xml file.
	 */
	public void setTransactionSafe(final boolean transactionSafe) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_TRANSACTION_SAFE, Boolean.toString(transactionSafe));
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
	 * Check whether entity descriptor object exists or not, based on table name.
	 * @param tableName Name of table.
	 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
	 */
	public boolean containsEntityDescriptorBasedOnTableName(final String tableName) {
		return this.entityDescriptorsBasedOnTableName.containsKey(tableName);
	}
	
	/**
	 * Check whether entity descriptor object exists or not, based on mapped class name.
	 * @param className Mapped class name.
	 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
	 */
	public boolean containsEntityDescriptorBasedOnClassName(final String className) {
		return this.entityDescriptorsBasedOnClassName.containsKey(className);
	}
	
	/**
	 * Get all entity descriptor paths as per defined in DatabaseDescriptor.xml file.
	 * @return Iterator which contain all entity descriptor paths.
	 */
	public Iterator<String> getEntityDescriptorPaths() {
		return this.entityDescriptorPaths.iterator();
	}

	/**
	 * Add entity descriptor path as per defined in DatabaseDescriptor.xml file.
	 	<p>
	 		<pre>
	 		
EXAMPLE:
	{@code
	<database-descriptor>
		<entity-descriptors>
			<entity-descriptor>full_path_of_entity_descriptor_file</entity-descriptor>
		</entity-descriptors>
	</database-descriptor>
	}
	
		</pre>
	</p>
	 
	 * @param entityDescriptorPath Entity Descriptor Path.
	 */
	public void addEntityDescriptorPath(final String entityDescriptorPath) {
		this.entityDescriptorPaths.add(entityDescriptorPath);
	}

	/**
	 * Get all entity descriptor objects contained.
	 * @return All entity descriptor objects.
	 */
	public Iterator<EntityDescriptor> getEntityDescriptors() {
		return this.entityDescriptorsBasedOnClassName.values().iterator();
	}

	/**
	 * Get entity descriptor object based on table name.
	 * @param tableName Name of table.
	 * @return Entity descriptor object based on table name.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnTableName(final String tableName) {
		return this.entityDescriptorsBasedOnTableName.get(tableName);
	}

	/**
	 * Get entity descriptor object based on mapped class name.
	 * @param className mapped class name.
	 * @return Entity Descriptor.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnClassName(final String className) {
		return this.entityDescriptorsBasedOnClassName.get(className);
	}
	
	/**
	 * Get entity descriptor object based on path.
	 * @param entityDescriptorPath Entity Descriptor path as per defined in Database Descriptor.xml file.
	 * @return Entity Descriptor.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnPath(final String entityDescriptorPath) {
		return this.entityDescriptorsBasedOnPath.get(entityDescriptorPath);
	}
	
	/**
	 * Add entity descriptor object in respect to entity descriptor path.
	 * @param entityDescriptorPath Entity Descriptor Path.
	 * @param entityDescriptor Entity Descriptor.
	 */
	public void addEntityDescriptor(final String entityDescriptorPath, final EntityDescriptor entityDescriptor) {
		this.entityDescriptorsBasedOnPath.put(entityDescriptorPath, entityDescriptor);
		this.entityDescriptorsBasedOnTableName.put(entityDescriptor.getTableName(), entityDescriptor);
		this.entityDescriptorsBasedOnClassName.put(entityDescriptor.getClassName(), entityDescriptor);
	}

	/**
	 * Remove entity descriptor object based on entity descriptor path.
	 * @param entityDescriptorPath Entity Descriptor Path.
	 */
	public void removeEntityDescriptorBasedOnPath(final String entityDescriptorPath) {
		this.entityDescriptorPaths.remove(entityDescriptorPath);
		
		EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnPath.get(entityDescriptorPath);
		this.entityDescriptorsBasedOnPath.remove(entityDescriptorPath);
		
		this.entityDescriptorsBasedOnClassName.values().remove(entityDescriptor);
		this.entityDescriptorsBasedOnTableName.values().remove(entityDescriptor);
	}
	
	/**
	 * Remove entity descriptor object based on POJO class name.
	 * @param className Mapped class name.
	 */
	public void removeEntityDescriptorBasedOnClassName(final String className) {
		
		EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnClassName.get(className);
		Collection<String> keys = this.entityDescriptorsBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			
			EntityDescriptor entityDescriptorBasedOnPath = this.entityDescriptorsBasedOnPath.get(key);
			if(entityDescriptor == entityDescriptorBasedOnPath) {
				keyMatched = key;
				found = true;
				break;
			}
		}
		
		if(found) {
			removeEntityDescriptorBasedOnPath(keyMatched);
		}
	}
	
	/**
	 * Remove entity descriptor object based on table name.
	 * @param tableName Name of table.
	 */
	public void removeEntityDescriptorBasedOnTableName(final String tableName) {
		EntityDescriptor entityDescriptor = this.entityDescriptorsBasedOnTableName.get(tableName);
		removeEntityDescriptorBasedOnClassName(entityDescriptor.getClassName());
	}
	
	/**
	 * Remove entity descriptor object based on entity descriptor object.
	 * @param entityDescriptor Entity descriptor object which needs to be removed.
	 */
	public void removeEntityDescriptor(final EntityDescriptor entityDescriptor) {
		removeEntityDescriptorBasedOnClassName(entityDescriptor.getClassName());
	}
	
	/**
	 * Get all entity descriptor objects in sorted order. The order will be as per defined in DatabaseDescriptor.xml file.
	 * @return Iterator which contains all entity descriptor objects.
	 */
	public Iterator<EntityDescriptor> orderedEntityDescriptors() {
		Collection<EntityDescriptor> orderedEntityDescriptors = new LinkedList<EntityDescriptor> ();
		
		for(String entityDescriptorPath : this.entityDescriptorPaths) {
			orderedEntityDescriptors.add(getEntityDescriptorBasedOnPath(entityDescriptorPath));
		}
 		
		return orderedEntityDescriptors.iterator();
	}
	
}
