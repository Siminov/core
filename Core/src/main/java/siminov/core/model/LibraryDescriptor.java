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
 * Exposes methods to GET and SET Library Descriptor information as per define in LibraryDescriptor.xml file by application.
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
	
		
		<!-- Entity Descriptor Needed Under This Library Descriptor -->
		
		<!-- Optional Field -->
			<!-- Entity Descriptors -->
		<entity-descriptors>
			<entity-descriptor>name_of_database_descriptor.full_path_of_entity_descriptor_file</entity-descriptor>
		</entity-descriptors>
		 
	</library-descriptor>
	
	}
	
		</pre>
	</p>
 *
 */
public class LibraryDescriptor implements IDescriptor {

	protected Map<String, String> properties = new HashMap<String, String> ();
	
	protected Collection<String> entityDescriptorPaths = new ConcurrentLinkedQueue<String> ();
	
	protected Map<String, EntityDescriptor> entityDescriptorBasedOnTableName = new ConcurrentHashMap<String, EntityDescriptor>();
	protected Map<String, EntityDescriptor> entityDescriptorBasedOnClassName = new ConcurrentHashMap<String, EntityDescriptor>();
	protected Map<String, EntityDescriptor> entityDescriptorBasedOnPath = new ConcurrentHashMap<String, EntityDescriptor>();


	/**
	 * Get library name.
	 */
	public String getName() {
		return this.properties.get(Constants.LIBRARY_DESCRIPTOR_NAME);
	}
	
	/**
	 * Set library name as per defined in LibraryDescriptor.xml
	 * @param name
	 */
	public void setName(final String name) {
		this.properties.put(Constants.LIBRARY_DESCRIPTOR_NAME, name);
	}
	
	/**
	 * Get descriptor as per defined in LibraryDescriptor.xml
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
	 * Check whether entity descriptor object exists or not, based on table name.
	 * @param tableName Name of table.
	 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
	 */
	public boolean containsEntityDescriptorBasedOnTableName(final String tableName) {
		return this.entityDescriptorBasedOnTableName.containsKey(tableName);
	}
	
	/**
	 * Check whether entity descriptor object exists or not, based on mapped class name.
	 * @param className Mapped class name.
	 * @return TRUE: If entity descriptor exists, FALSE: If entity descriptor does not exists.
	 */
	public boolean containsEntityDescriptorBasedOnClassName(final String className) {
		return this.entityDescriptorBasedOnClassName.containsKey(className);
	}

	/**
	 * Get all entity descriptor paths as per defined in DatabaseDescriptor.xml file.
	 * @return Iterator which contain all entity descriptor paths.
	 */
	public Iterator<String> getEntityDescriptorPaths() {
		return this.entityDescriptorPaths.iterator();
	}

	/**
	 * Add entity descriptor path as per defined in EntityDescriptor.xml file.
	 	<p>
	 		<pre>
	 		
EXAMPLE:
	{@code
	<!-- Entity Descriptor Needed Under This Library Descriptor -->
	
	<!-- Optional Field -->
		<!-- Entity Descriptors -->
	<entity-descriptors>
		<entity-descriptor>name_of_database_descriptor.full_path_of_entity_descriptor_file</entity-descriptor>
	</entity-descriptors>
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
		return this.entityDescriptorBasedOnClassName.values().iterator();
	}

	/**
	 * Get entity descriptor object based on table name.
	 * @param tableName Name of table.
	 * @return EntityDescriptor object based on table name.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnTableName(final String tableName) {
		return this.entityDescriptorBasedOnTableName.get(tableName);
	}

	/**
	 * Get entity descriptor object based on mapped class name.
	 * @param className mapped class name.
	 * @return Entity Descriptor object.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnClassName(final String className) {
		return this.entityDescriptorBasedOnClassName.get(className);
	}

	/**
	 * Get entity descriptor object based on path.
	 * @param libraryEntityDescriptorPath Library entity descriptor path as per defined in Entity Descriptor.xml file.
	 * @return Entity Descriptor object.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnPath(final String libraryEntityDescriptorPath) {
		return this.entityDescriptorBasedOnPath.get(libraryEntityDescriptorPath);
	}
	
	/**
	 * Add entity descriptor object in respect to entity descriptor path.
	 * @param libraryEntityDescriptorPath Library Entity Descriptor Path.
	 * @param entityDescriptor Entity Descriptor object.
	 */
	public void addEntityDescriptor(final String libraryEntityDescriptorPath, final EntityDescriptor entityDescriptor) {
		this.entityDescriptorBasedOnPath.put(libraryEntityDescriptorPath, entityDescriptor);
		this.entityDescriptorBasedOnTableName.put(entityDescriptor.getTableName(), entityDescriptor);
		this.entityDescriptorBasedOnClassName.put(entityDescriptor.getClassName(), entityDescriptor);
	}

	/**
	 * Remove entity descriptor object based on entity descriptor path.
	 * @param entityDescriptorPath Entity Descriptor Path.
	 */
	public void removeEntityDescriptorBasedOnPath(final String entityDescriptorPath) {
		this.entityDescriptorPaths.remove(entityDescriptorPath);
		
		EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnPath.get(entityDescriptorPath);
		this.entityDescriptorBasedOnPath.remove(entityDescriptorPath);
		
		this.entityDescriptorBasedOnClassName.values().remove(entityDescriptor);
		this.entityDescriptorBasedOnTableName.values().remove(entityDescriptor);
	}
	
	/**
	 * Remove entity descriptor object based on mapped class name.
	 * @param className mapped class name.
	 */
	public void removeEntityDescriptorBasedOnClassName(final String className) {
		EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnClassName.get(className);
		Collection<String> keys = this.entityDescriptorBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			EntityDescriptor entityDescriptorBasedOnPath = this.entityDescriptorBasedOnPath.get(key);
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
		EntityDescriptor entityDescriptor = this.entityDescriptorBasedOnTableName.get(tableName);
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
