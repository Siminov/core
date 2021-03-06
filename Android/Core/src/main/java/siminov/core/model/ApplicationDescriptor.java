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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import siminov.core.Constants;


/**
 * Exposes methods to GET and SET Application Descriptor information as per define in ApplicationDescriptor.xml file by application.
	<p>
		<pre>
		
Example:
	{@code



	<siminov>

		<!-- General Application Description Properties -->

		<!-- Mandatory Field -->
		<property name="name">application_name</property>

		<!-- Optional Field -->
		<property name="description">application_description</property>

		<!-- Mandatory Field (Default is 0.0) -->
		<property name="version">application_version</property>


		<!-- Database Descriptors Used By Application (zero-to-many) -->
			<!-- Optional Field's -->
		<database-descriptors>
			<database-descriptor>full_path_of_database_descriptor_file</database-descriptor>
		</database-descriptors>


		<!-- Library Descriptors Used By Application (zero-to-many) -->
			<!-- Optional Field's -->
		<library-descriptors>
			<library-descriptor>full_path_of_library_descriptor_file</library-descriptor>
		</library-descriptors>


		<!-- Event Handlers Implemented By Application (zero-to-many) -->

			<!-- Optional Field's -->
		<event-handlers>
			<event-handler>full_class_path_of_event_handler_(ISiminovHandler/IDatabaseHandler)</event-handler>
		</event-handlers>

	</siminov>


	}
	
		</pre>
	</p>
 *
 */
public class ApplicationDescriptor implements IDescriptor {

	protected Map<String, String> properties = new HashMap<String, String> ();
	
	protected Collection<String> databaseDescriptorPaths = new ConcurrentLinkedQueue<String> ();
	protected Map<String, DatabaseDescriptor> databaseDescriptorsBasedOnName = new ConcurrentHashMap<String, DatabaseDescriptor>();
	protected Map<String, DatabaseDescriptor> databaseDescriptorsBasedOnPath = new ConcurrentHashMap<String, DatabaseDescriptor>();

	protected Collection<String> events = new ConcurrentLinkedQueue<String> ();

	protected Collection<String> libraryDescriptorPaths = new ConcurrentLinkedQueue<String>();
	
	
	/**
	 * Get Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
	 * @return Application Descriptor Name.
	 */
	public String getName() {
		return this.properties.get(Constants.APPLICATION_DESCRIPTOR_NAME);
	}

	/**
	 * Set Application Descriptor Name as per defined in ApplicationDescriptor.xml file.
	 * @param name Name of Application Descriptor.
	 */
	public void setName(final String name) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_NAME, name);
	}
	
	/**
	 * Set Description of Application as per defined in ApplicationDescriptor.xml file.
	 * @return Description of application.
	 */
	public String getDescription() {
		return this.properties.get(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION);
	}
	
	/**
	 * Set Description of Application as per defined in ApplicationDescriptor.xml file.
	 * @param description Description of application.
	 */
	public void setDescription(final String description) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION, description);
	}
	
	/**
	 * Get Version of Application as per defined in ApplicationDescriptor.xml file.
	 * @return Version of application.
	 */
	public double getVersion() {
		String version = this.properties.get(Constants.APPLICATION_DESCRIPTOR_VERSION);
		if(version == null || version.length() <= 0) {
			return 0.0;
		}
		
		return Double.valueOf(version);
	}
	
	/**
	 * Set Version of Application as per defined in ApplicationDescriptor.xml file.
	 * @param version Version of application.
	 */
	public void setVersion(final double version) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_VERSION, Double.toString(version));
	}

	public String getDeploy() {
		return this.properties.get(Constants.APPLICATION_DESCRIPTOR_DEPLOY);
	}
	
	public void setDeploy(String deploy) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_DEPLOY, deploy);
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
	 * Check whether database needed by application or not.
	 * @return TRUE: If database needed by application, FALSE: If database is not needed by application.
	 */
	public boolean isDatabaseNeeded() {
		return this.databaseDescriptorPaths.size() > 0 ? true : false;
	}
	
	/**
	 * Check whether database descriptor exists in Resources or not.
	 * @param databaseDescriptor Database Descriptor object.
	 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
	 */
	public boolean containsDatabaseDescriptor(final DatabaseDescriptor databaseDescriptor) {
		return this.databaseDescriptorsBasedOnName.containsValue(databaseDescriptor);
	}

	/**
	 * Check whether database descriptor exists in Resources or not, based on database descriptor path.
	 * @param containDatabaseDescriptorPath Database Descriptor path.
	 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
	 */
	public boolean containsDatabaseDescriptorBasedOnPath(final String containDatabaseDescriptorPath) {
		return this.databaseDescriptorsBasedOnPath.containsKey(containDatabaseDescriptorPath);
	}

	/**
	 * Check whether database descriptor exists in Resources or not, based on Database Descriptor name.
	 * @param databaseDescriptorName Database Descriptor Name.
	 * @return TRUE: If Database Descriptor exists in Resources, FALSE: If Database Descriptor does not exists in Resources.
	 */
	public boolean containsDatabaseDescriptorBasedOnName(final String databaseDescriptorName) {
		return this.databaseDescriptorsBasedOnName.containsKey(databaseDescriptorName);
	}
	
	/**
	 * Get Database Descriptor based on Database Descriptor Name.
	 * @param databaseDescriptorName Database Desciptor Name.
	 * @return Database Descriptor Object.
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnName(final String databaseDescriptorName) {
		return this.databaseDescriptorsBasedOnName.get(databaseDescriptorName);
	}

	/**
	 * Get Database Descriptor based on Database Descriptor Path.
	 * @param databaseDescriptorPath Database Descriptor Path.
	 * @return Database Descriptor Object.
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath) {
		return this.databaseDescriptorsBasedOnPath.get(databaseDescriptorPath);
	}
	
	/**
	 * Get all database descriptor paths as per contained in ApplicationDescriptor.xml file.
	 * @return Iterator which contains all database descriptor paths.
	 */
	public Iterator<String> getDatabaseDescriptorPaths() {
		return this.databaseDescriptorPaths.iterator();
	}
	
	/**
	 * Get all database descriptor names as per needed by application.
	 * @return Iterator which contains all database descriptor names.
	 */
	public Iterator<String> getDatabaseDescriptorNames() {
		return this.databaseDescriptorsBasedOnName.keySet().iterator();
	}
	
	/**
	 * Add Database Descriptor path as per contained in ApplicationDescriptor.xml file.
	 * @param databaseDescriptorPath DatabaseDescriptor path.
	 */
	public void addDatabaseDescriptorPath(final String databaseDescriptorPath) {
		this.databaseDescriptorPaths.add(databaseDescriptorPath);
	}
	
	/**
	 * Get all database descriptor objects contains by Siminov.
	 * @return Iterator which contains all database descriptor objects.
	 */
	public Iterator<DatabaseDescriptor> getDatabaseDescriptors() {
		return this.databaseDescriptorsBasedOnName.values().iterator();
	}
	
	/**
	 * Add Database Descriptor object in respect to database descriptor path.
	 * @param databaseDescriptorPath Database Descriptor Path.
	 * @param databaseDescriptor Database Descriptor Object.
	 */
	public void addDatabaseDescriptor(final String databaseDescriptorPath, final DatabaseDescriptor databaseDescriptor) {
		this.databaseDescriptorsBasedOnPath.put(databaseDescriptorPath, databaseDescriptor);
		this.databaseDescriptorsBasedOnName.put(databaseDescriptor.getDatabaseName(), databaseDescriptor);
	}

	/**
	 * Remove Database Descriptor from Resources based on database path provided, as per defined in ApplicationDescriptor.xml file
	 * @param databaseDescriptorPath Database Descriptor Path.
	 */
	public void removeDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath) {
		this.databaseDescriptorPaths.remove(databaseDescriptorPath);
		
		DatabaseDescriptor databaseDescriptor = this.databaseDescriptorsBasedOnPath.get(databaseDescriptorPath);
		this.databaseDescriptorsBasedOnPath.remove(databaseDescriptorPath);
		
		this.databaseDescriptorsBasedOnName.values().remove(databaseDescriptor);
	}
	
	/**
	 * Remove Database Descriptor from Resources based in database name provided, as per defined in DatabaseDescriptor.xml file
	 * @param databaseDescriptorName DatabaseDescriptor Name.
	 */
	public void removeDatabaseDescriptorBasedOnName(final String databaseDescriptorName) {
		DatabaseDescriptor databaseDescriptor = this.databaseDescriptorsBasedOnName.get(databaseDescriptorName);
		Collection<String> keys = this.databaseDescriptorsBasedOnPath.keySet();
		
		String keyMatched = null;
		boolean found = false;
		for(String key : keys) {
			DatabaseDescriptor descriptor = this.databaseDescriptorsBasedOnPath.get(key);
			if(databaseDescriptor == descriptor) {
				keyMatched = key;
				found = true;
				break;
			}
		}
		
		if(found) {
			removeDatabaseDescriptorBasedOnPath(keyMatched);
		}
	}
	
	/**
	 * Remove DatabaseDescriptor object from Resources.
	 * @param databaseDescriptor DatabaseDescriptor object which needs to be removed.
	 */
	public void removeDatabaseDescriptor(final DatabaseDescriptor databaseDescriptor) {
		removeDatabaseDescriptorBasedOnName(databaseDescriptor.getDatabaseName());
	}
	
	
	/**
	 * Add library descriptor path
	 * @param libraryDescriptorPath Library Descriptor Path 
	 */
	public void addLibraryDescriptorPath(final String libraryDescriptorPath) {
		this.libraryDescriptorPaths.add(libraryDescriptorPath);
	}
	
	/**
	 * Get all library descriptor paths
	 * @return Library Descriptor Paths
	 */
	public Iterator<String> getLibraryDescriptorPaths() {
		return this.libraryDescriptorPaths.iterator();
	}
	
	/**
	 * Check whether it contains library descriptor path or not
	 * @param libraryDescriptorPath Path of Library Descriptor
	 * @return (true/false) TRUE: If library descriptor path exists | FALSE: If library descriptor path does not exists.
	 */
	public boolean containLibraryDescriptorPath(final String libraryDescriptorPath) {
		return this.libraryDescriptorPaths.contains(libraryDescriptorPath);
	}
	
	
	/**
	 * Remove library descriptor path
	 * @param libraryDescriptorPath Path of library descriptor 
	 */
	public void removeLibraryDescriptorPath(final String libraryDescriptorPath) {
		this.libraryDescriptorPaths.add(libraryDescriptorPath);
	}
	
	
	/**
	 * Get all event handlers as per defined in ApplicationDescriptor.xml file.
	 * @return All event handlers defined in ApplicationDescriptor.xml file
	 */
	public  Iterator<String> getEvents() {
		return this.events.iterator();
	}
	
	/**
	 * Add event as per defined in ApplicationDescriptor.xml file.
	 * @param event Event Handler class name.
	 */
	public void addEvent(final String event) {
		this.events.add(event);
	}

	/**
	 * Remove event as per defined event name
	 * @param event Name of the event
	 */
	public void removeEvent(final String event) {
		this.events.remove(event);
	}
	
}