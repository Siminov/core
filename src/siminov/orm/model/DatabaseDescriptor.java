/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution LLP|support@siminov.com]
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
				<database-mapping path="Liquor-Mappings/Liquor.si.xml" />
				<database-mapping path="Liquor-Mappings/LiquorBrand.si.xml" />
			</database-mappings>
	
			 	<!-- OR -->

			<database-mappings>
				<database-mapping path="siminov.orm.template.model.Liquor" />
				<database-mapping path="siminov.orm.template.model.LiquorBrand" />
			</database-mappings>
		

		<!-- Libraries -->
		<libraries>
			<library>siminov.orm.template.resources</library>
		</libraries>
				
	</database-descriptor>

	}
	
		</pre>
	</p>
*/
public class DatabaseDescriptor implements IDescriptor {
	
	protected Map<String, String> properties = new HashMap<String, String> ();
	
	protected Collection<String> databaseMappingDescriptorPaths = new ConcurrentLinkedQueue<String> ();

	protected Map<String, DatabaseMappingDescriptor> databaseMappingDescriptorsBasedOnTableName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	protected Map<String, DatabaseMappingDescriptor> databaseMappingDescriptorsBasedOnClassName = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	protected Map<String, DatabaseMappingDescriptor> databaseMappingDescriptorsBasedOnPath = new ConcurrentHashMap<String, DatabaseMappingDescriptor>();
	
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

	public double getVersion() {
		String version = this.properties.get(Constants.DATABASE_DESCRIPTOR_VERSION);
		if(version == null || version.length() <= 0) {
			return 0.0;
		}
		
		return Long.valueOf(version);
	}
	
	/**
	 * Set Version of Application as per defined in ApplicationDescriptor.si.xml file.
	 * @param version Version of application.
	 */
	public void setVersion(final long version) {
		this.properties.put(Constants.DATABASE_DESCRIPTOR_VERSION, Long.toString(version));
	}
	
	
	/**
	 * Get description as per defined in DatabaseDescriptor.si.xml file.
	 * @return Description defined in DatabaseDescriptor.si.xml file.
	 */
	public String getDescription() {
		return this.properties.get(Constants.DATABASE_DESCRIPTOR_DESCRIPTION);
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
	 * Set database locking as per defined in DatabaseDescriptor.si.xml file.
	 * @param transactionSafe (true/false) database locking as per defined in DatabaseDescriptor.si.xml file.
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
	 * Check whether database mapping object exists or not, based on table name.
	 * @param tableName Name of table.
	 * @return TRUE: If database mapping exists, FALSE: If database mapping does not exists.
	 */
	public boolean containsDatabaseMappingDescriptorBasedOnTableName(final String tableName) {
		return this.databaseMappingDescriptorsBasedOnTableName.containsKey(tableName);
	}
	
	/**
	 * Check whether database mapping object exists or not, based on POJO class name.
	 * @param className POJO class name.
	 * @return TRUE: If database mapping exists, FALSE: If database mapping does not exists.
	 */
	public boolean containsDatabaseMappingDescriptorBasedOnClassName(final String className) {
		return this.databaseMappingDescriptorsBasedOnClassName.containsKey(className);
	}
	
	/**
	 * Get all database mapping paths as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contain all database mapping paths.
	 */
	public Iterator<String> getDatabaseMappingDescriptorPaths() {
		return this.databaseMappingDescriptorPaths.iterator();
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
	 
	 * @param databaseMappingDescriptorPath Database Mapping Path.
	 */
	public void addDatabaseMappingDescriptorPath(final String databaseMappingDescriptorPath) {
		this.databaseMappingDescriptorPaths.add(databaseMappingDescriptorPath);
	}

	/**
	 * Get all database mapping objects contained.
	 * @return All database mapping objects.
	 */
	public Iterator<DatabaseMappingDescriptor> getDatabaseMappingDescriptors() {
		return this.databaseMappingDescriptorsBasedOnClassName.values().iterator();
	}

	/**
	 * Get database mapping object based on table name.
	 * @param tableName Name of table.
	 * @return DatabaseMapping object based on table name.
	 */
	public DatabaseMappingDescriptor getDatabseMappingDescriptorBasedOnTableName(final String tableName) {
		return this.databaseMappingDescriptorsBasedOnTableName.get(tableName);
	}

	/**
	 * Get database mapping object based on POJO class name.
	 * @param className POJO class name.
	 * @return Database Mapping object.
	 */
	public DatabaseMappingDescriptor getDatabseMappingDescriptorBasedOnClassName(final String className) {
		return this.databaseMappingDescriptorsBasedOnClassName.get(className);
	}
	
	/**
	 * Get database mapping object based on path.
	 * @param databaseMappingDescriptorPath Database Mapping path as per defined in Database Descriptor.xml file.
	 * @return Database Mapping object.
	 */
	public DatabaseMappingDescriptor getDatabseMappingDescriptorBasedOnPath(final String databaseMappingDescriptorPath) {
		return this.databaseMappingDescriptorsBasedOnPath.get(databaseMappingDescriptorPath);
	}
	
	/**
	 * Add database mapping object in respect to database mapping path.
	 * @param databaseMappingDescriptorPath Database Mapping Path.
	 * @param databaseMappingDescriptor Database Mapping object.
	 */
	public void addDatabaseMappingDescriptor(final String databaseMappingDescriptorPath, final DatabaseMappingDescriptor databaseMappingDescriptor) {
		this.databaseMappingDescriptorsBasedOnPath.put(databaseMappingDescriptorPath, databaseMappingDescriptor);
		this.databaseMappingDescriptorsBasedOnTableName.put(databaseMappingDescriptor.getTableName(), databaseMappingDescriptor);
		this.databaseMappingDescriptorsBasedOnClassName.put(databaseMappingDescriptor.getClassName(), databaseMappingDescriptor);
	}

	/**
	 * Remove database mapping object based on database mapping path.
	 * @param databaseMappingDescriptorPath Database Mapping Path.
	 */
	public void removeDatabaseMappingDescriptorBasedOnPath(final String databaseMappingDescriptorPath) {
		this.databaseMappingDescriptorPaths.remove(databaseMappingDescriptorPath);
		
		DatabaseMappingDescriptor databaseMappingDescriptor = this.databaseMappingDescriptorsBasedOnPath.get(databaseMappingDescriptorPath);
		this.databaseMappingDescriptorsBasedOnPath.remove(databaseMappingDescriptorPath);
		
		this.databaseMappingDescriptorsBasedOnClassName.values().remove(databaseMappingDescriptor);
		this.databaseMappingDescriptorsBasedOnTableName.values().remove(databaseMappingDescriptor);
	}
	
	/**
	 * Remove database mapping object based on POJO class name.
	 * @param className POJO class name.
	 */
	public void removeDatabaseMappingDescriptorBasedOnClassName(final String className) {
		DatabaseMappingDescriptor databaseMappingDescriptor = this.databaseMappingDescriptorsBasedOnClassName.get(className);
		Collection<String> keys = this.databaseMappingDescriptorsBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			DatabaseMappingDescriptor mapping = this.databaseMappingDescriptorsBasedOnPath.get(key);
			if(databaseMappingDescriptor == mapping) {
				keyMatched = key;
				found = true;
				break;
			}
		}
		
		if(found) {
			removeDatabaseMappingDescriptorBasedOnPath(keyMatched);
		}
	}
	
	/**
	 * Remove database mapping object based on table name.
	 * @param tableName Name of table.
	 */
	public void removeDatabaseMappingDescriptorBasedOnTableName(final String tableName) {
		DatabaseMappingDescriptor databaseMappingDescriptor = this.databaseMappingDescriptorsBasedOnTableName.get(tableName);
		removeDatabaseMappingDescriptorBasedOnClassName(databaseMappingDescriptor.getClassName());
	}
	
	/**
	 * Remove database mapping object based on database mapping object.
	 * @param databaseMappingDescriptor Database Mapping object which needs to be removed.
	 */
	public void removeDatabaseMappingDescriptor(final DatabaseMappingDescriptor databaseMappingDescriptor) {
		removeDatabaseMappingDescriptorBasedOnClassName(databaseMappingDescriptor.getClassName());
	}
	
	/**
	 * Get all database mapping objects in sorted order. The order will be as per defined in DatabaseDescriptor.si.xml file.
	 * @return Iterator which contains all database mapping objects.
	 */
	public Iterator<DatabaseMappingDescriptor> orderedDatabaseMappingDescriptors() {
		Collection<DatabaseMappingDescriptor> orderedDatabaseMappingDescriptors = new LinkedList<DatabaseMappingDescriptor> ();
		
		for(String databaseMappingDescriptorPath : this.databaseMappingDescriptorPaths) {
			orderedDatabaseMappingDescriptors.add(getDatabseMappingDescriptorBasedOnPath(databaseMappingDescriptorPath));
		}
 		
		return orderedDatabaseMappingDescriptors.iterator();
	}
	
}
