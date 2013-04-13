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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import siminov.orm.Constants;


/**
 * Exposes methods to GET and SET Application Descriptor information as per define in ApplicationDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	<core>
	
		<property name="name">SIMINOV TEMPLATE</property>	
		<property name="description">Siminov Template Application</property>
		<property name="version">0.9</property>
	
		<property name="load_initially">true</property>
	
		<!-- DATABASE-DESCRIPTORS -->
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
		</database-descriptors>
	
		
		<!-- SIMINOV EVENTS -->
		<event-handlers>
		    <event-handler>com.core.template.events.SiminovEventHandler</event-handler>
		    <event-handler>com.core.template.events.DatabaseEventHandler</event-handler>
		</event-handlers>
			
	</core>
	}
	
		</pre>
	</p>
 *
 */
public class ApplicationDescriptor {

	private Map<String, String> properties = new HashMap<String, String> ();
	
	private Collection<String> databaseDescriptorPaths = new ConcurrentLinkedQueue<String> ();
	private Map<String, DatabaseDescriptor> databaseDescriptorsBasedOnName = new ConcurrentHashMap<String, DatabaseDescriptor>();
	private Map<String, DatabaseDescriptor> databaseDescriptorsBasedOnPath = new ConcurrentHashMap<String, DatabaseDescriptor>();

	private Collection<String> events = new ConcurrentLinkedQueue<String> ();
	
	/**
	 * Get Application Descriptor Name as per defined in ApplicationDescriptor.si.xml file.
	 * @return Application Descriptor Name.
	 */
	public String getName() {
		return this.properties.get(Constants.APPLICATION_DESCRIPTOR_NAME);
	}

	/**
	 * Set Application Descriptor Name as per defined in ApplicationDescriptor.si.xml file.
	 * @param name Name of Application Descriptor.
	 */
	public void setName(final String name) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_NAME, name);
	}
	
	/**
	 * Set Description of Application as per defined in ApplicationDescriptor.si.xml file.
	 * @return Description of application.
	 */
	public String getDescription() {
		return this.properties.get(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION);
	}
	
	/**
	 * Set Description of Application as per defined in ApplicationDescriptor.si.xml file.
	 * @param description Description of application.
	 */
	public void setDescription(final String description) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_DESCRIPTION, description);
	}
	
	/**
	 * Get Version of Application as per defined in ApplicationDescriptor.si.xml file.
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
	 * Set Version of Application as per defined in ApplicationDescriptor.si.xml file.
	 * @param version Version of application.
	 */
	public void setVersion(final double version) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_VERSION, Double.toString(version));
	}

	/**
	 * It defines the behavior of SIMINOV. (Should core load all database mapping at initialization or on demand).
	 * @return TRUE: If load initially is set to true, FALSE: If load initially is set to false.
	 */
	public boolean isLoadInitially() {
		String isLoadInitially = this.properties.get(Constants.APPLICATION_DESCRIPTOR_LOAD_INITIALLY);
		if(isLoadInitially == null || isLoadInitially.length() <= 0) {
			return true;
		} else if(isLoadInitially != null && isLoadInitially.length() > 0 && isLoadInitially.equalsIgnoreCase("true")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Set load initially to true or false.
	 * @param initialLoad (true/false) defined by ApplicationDescriptor.si.xml file.
	 */
	public void setLoadInitially(final boolean initialLoad) {
		this.properties.put(Constants.APPLICATION_DESCRIPTOR_LOAD_INITIALLY, Boolean.toString(initialLoad));
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
	 * Get all database descriptor paths as per contained in ApplicationDescriptor.si.xml file.
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
	 * Add Database Descriptor path as per contained in ApplicationDescriptor.si.xml file.
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
	 * Remove Database Descriptor from Resources based on database path provided, as per defined in ApplicationDescriptor.si.xml file
	 * @param databaseDescriptorPath Database Descriptor Path.
	 */
	public void removeDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath) {
		this.databaseDescriptorPaths.remove(databaseDescriptorPath);
		
		DatabaseDescriptor databaseDescriptor = this.databaseDescriptorsBasedOnPath.get(databaseDescriptorPath);
		this.databaseDescriptorsBasedOnPath.remove(databaseDescriptorPath);
		
		this.databaseDescriptorsBasedOnName.values().remove(databaseDescriptor);
	}
	
	/**
	 * Remove Database Descriptor from Resources based in database name provided, as per defined in DatabaseDescriptor.si.xml file
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
	 * Get all event handlers as per defined in ApplicationDescriptor.si.xml file.
	 * @return All event handlers defined in ApplicationDescriptor.si.xml file
	 */
	public  Iterator<String> getEvents() {
		return this.events.iterator();
	}
	
	/**
	 * Add event as per defined in ApplicationDescriptor.si.xml file.
	 * @param event Event Handler class name.
	 */
	public void addEvent(final String event) {
		this.events.add(event);
	}
	
}