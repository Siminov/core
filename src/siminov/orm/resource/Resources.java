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

package siminov.orm.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import siminov.orm.Siminov;
import siminov.orm.database.DatabaseBundle;
import siminov.orm.events.EventHandler;
import siminov.orm.events.IDatabaseEvents;
import siminov.orm.events.ISiminovEvents;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.LibraryDescriptor;
import siminov.orm.parsers.QuickDatabaseMappingParser;
import siminov.orm.utils.EmptyIterator;
import android.content.Context;



/**
 * It handles and provides all resources needed by SIMINOV.
 * <p>
 * Such As: Provides Application Descriptor, Database Descriptor, Library Descriptor, Database Mapping.
 */
public class Resources {

	/*
	 * Resources.
	 */
	private Context applicationContext = null;
	
	private ApplicationDescriptor applicationDescriptor = null;
	
	private Map<String, DatabaseBundle> databaseBundleBasedOnDatabaseDescriptorName = new HashMap<String, DatabaseBundle>();
	private Map<String, DatabaseBundle> databaseBundleBasedOnDatabaseMappingClassName = new HashMap<String, DatabaseBundle>();
	private Map<String, DatabaseBundle> databaseBundleBasedOnDatabaseMappingTableName = new HashMap<String, DatabaseBundle>();

	private static Resources resources = null;
	
	private Resources() {

	}

	/**
	 * It provides an instance of Resources class.
	 * 
	 * @return Resources instance.
	 */
	public static Resources getInstance() {
		if(resources == null) {
			resources = new Resources();
		}
		
		return resources;
	}
	

	/**
	 * Returns Application Context provided by application.
	 * 
	 * @return Application Context (android.content.Context).
	 */
	public Context getApplicationContext() {
		Siminov.validateSiminov();
		
		return this.applicationContext;
	}
	
	/**
	 * Set Application context provided by application.
	 * @param context Application Context (android.content.Context).
	 */
	public void setApplicationContext(final Context context) {
		Siminov.validateSiminov();
		
		this.applicationContext = context;
	}
	
	/**
	 * Get Application Descriptor object of application.
	 * @return Application Descriptor.
	 */
	public ApplicationDescriptor getApplicationDescriptor() {
		Siminov.validateSiminov();
		
		return this.applicationDescriptor;
	}
	
	/**
	 * Set Application Descriptor of application.
	 * @param applicationDescriptor Application Descriptor object.
	 */
	public void setApplicationDescriptor(final ApplicationDescriptor applicationDescriptor) {
		Siminov.validateSiminov();
		
		this.applicationDescriptor = applicationDescriptor;
	}
	
	/**
	 * Get iterator of all database descriptors provided in Application Descriptor file.
		<p>
			<pre>
Example: ApplicationDescriptor.xml
	
	{@code
	<core>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
		</database-descriptors>

	</core>
	}
	
			</pre>
		</p>
	 * @return Iterator which contains all database descriptor paths provided.
	 */
	public Iterator<String> getDatabaseDescriptorPaths() {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorsPaths", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorPaths();
	}

	/**
	 * Get DatabaseDescriptor based on path provided as per defined in Application Descriptor file.
		<p>
			<pre>
			
Example: ApplicationDescriptor.xml
	
	{@code
	<core>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.xml</database-descriptor>
		</database-descriptors>

	</core>
	}
	
			</pre>
		</p>
	 
	 * @param databaseDescriptorPath Iterator which contains all database descriptor paths provided.
	 * @return
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnPath", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorBasedOnPath(databaseDescriptorPath);
	}

	
	/**
	 * Get Database Descriptor based on database descriptor name provided as per defined in Database Descriptor file.
		<p>
			<pre>
			
Example: DatabaseDescriptor.xml
	
	{@code
	<database-descriptor>
	
		<property name="database_name">SIMINOV-TEMPLATE</property>
		
	</database-descriptor>
	}
	
			</pre>
		</p>
	 
	 * 
	 * @param databaseDescriptorName Database Descriptor object based on database descriptor name provided.
	 * @return
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnName(final String databaseDescriptorName) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorBasedOnName(databaseDescriptorName);
	}
	
	/**
	 * Get all Database Descriptors object.
	 * @return Iterator which contains all Database Descriptors.
	 */
	public Iterator<DatabaseDescriptor> getDatabaseDescriptors() {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptors", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		return this.applicationDescriptor.getDatabaseDescriptors();
	}
	
	/**
	 * Get Database Descriptor based on POJO class name provided.
	 * 
	 * @param className POJO class name.
	 * @return Database Descriptor object in respect to POJO class name.
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnClassName(final String className) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingBasedOnClassName(className);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor;
			}
				
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
				
			Iterator<LibraryDescriptor> libraries = databaseDescriptor.getLibraryDescriptors();
			while(libraries.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraries.next();
				if(libraryDescriptor.containsDatabaseMappingBasedOnClassName(className)) {
					return databaseDescriptor;
				}
			}
		}
		
		return null;
	}

	/**
	 * Get Database Descriptor based on table name provided.
	 * 
	 * @param tableName Name of table.
	 * @return Database Descriptor object in respect to table name.
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnTableName(final String tableName) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingBasedOnTableName(tableName);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor;
			}
				
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
				
			Iterator<LibraryDescriptor> libraries = databaseDescriptor.getLibraryDescriptors();
			while(libraries.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraries.next();
				if(libraryDescriptor.containsDatabaseMappingBasedOnTableName(tableName)) {
					return databaseDescriptor;
				}
			}
		}
		
		return null;
	}

	

	/**
	 * Get Database Mapping based on POJO class name provided.
	 * 
	 * @param className POJO class name.
	 * @return Database Mapping object in respect to POJO class name.
	 */
	public DatabaseMappingDescriptor getDatabaseMappingDescriptorBasedOnClassName(final String className) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseMappingBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingBasedOnClassName(className);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabseMappingBasedOnClassName(className);
			}
				
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
				
			Iterator<LibraryDescriptor> libraries = databaseDescriptor.getLibraryDescriptors();
			while(libraries.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraries.next();
				if(libraryDescriptor.containsDatabaseMappingBasedOnClassName(className)) {
					return libraryDescriptor.getDatabseMappingBasedOnClassName(className);
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * Get Database Mapping based on table name provided.
	 * 
	 * @param tableName Name of table.
	 * @return Database Descriptor object in respect to table name.
	 */
	public DatabaseMappingDescriptor getDatabaseMappingDescriptorBasedOnTableName(final String tableName) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseMappingBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingBasedOnTableName(tableName);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabseMappingBasedOnTableName(tableName);
			}
				
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
				
			Iterator<LibraryDescriptor> libraries = databaseDescriptor.getLibraryDescriptors();
			while(libraries.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraries.next();
				if(libraryDescriptor.containsDatabaseMappingBasedOnTableName(tableName)) {
					return libraryDescriptor.getDatabseMappingBasedOnTableName(tableName);
				}
			}
		}
		
		return null;
	}

	
	public Iterator<DatabaseMappingDescriptor> getDatabaseMappingDescriptors() {
		Siminov.validateSiminov();
		
		Collection<DatabaseMappingDescriptor> databaseMappingDescriptors = new LinkedList<DatabaseMappingDescriptor>();
		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
	
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			Iterator<DatabaseMappingDescriptor> databaseMappings = databaseDescriptor.getDatabaseMappings();
			Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.getLibraryDescriptors();
			
			while(databaseMappings.hasNext()) {
				DatabaseMappingDescriptor databaseMappingDescriptor = databaseMappings.next();
				databaseMappingDescriptors.add(databaseMappingDescriptor);
			}
			
			while(libraryDescriptors.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraryDescriptors.next();
				Iterator<DatabaseMappingDescriptor> libraryDatabaseMappingDescriptors = libraryDescriptor.getDatabseMappings();
				
				while(libraryDatabaseMappingDescriptors.hasNext()) {
					DatabaseMappingDescriptor libraryDatabaseMappingDescriptor = libraryDatabaseMappingDescriptors.next();
					databaseMappingDescriptors.add(libraryDatabaseMappingDescriptor);
				}
			}
			
		}
		
		return databaseMappingDescriptors.iterator();
	}

	
	/**
	 * Get database mapping descriptor Object based on class name provided. If database mapping descriptor object not present in resource layer, it will parse DatabaseMappingDescriptor.si.xml file defined by application and will place it in resource layer.
	 * @param className Full name of class.
	 * @return DatabaseMappingDescriptor object.
	 * @throws SiminovException If any exception occur while getting database mapping descriptor object.
	 */
	public DatabaseMappingDescriptor requiredDatabaseMappingDescriptorBasedOnClassName(final String className) throws SiminovException {
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptorBasedOnClassName(className);

		if(databaseMapping == null) {
			Log.logd(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, MODEL: " + className);
			
			QuickDatabaseMappingParser quickDatabaseMappingParser = null;
			try {
				quickDatabaseMappingParser = new QuickDatabaseMappingParser(className);
				quickDatabaseMappingParser.process();
			} catch(SiminovException ce) {
				Log.loge(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick database mapping parsing, DATABASE-MAPPING-CLASS-NAME: " + className + ", " + ce.getMessage());
				throw new SiminovException(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick database mapping parsing, DATABASE-MAPPING-CLASS-NAME: " + className  + ", " + ce.getMessage());
			}
			
			DatabaseMappingDescriptor foundDatabaseMapping = quickDatabaseMappingParser.getDatabaseMapping();
			if(foundDatabaseMapping == null) {
				Log.loge(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, DATABASE-MAPPING-MODEL: " + className);
				throw new SiminovException(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, DATABASE-MAPPING-MODEL: " + className);
			}
			
			return foundDatabaseMapping;
		}
		
		return databaseMapping;
	}

	
	
	/**
	 * Get all Library Paths as per defined in all Database Descriptor file's.
	 * @return Iterator which contains all library paths defined in all Database Descriptor file's.
	 */
	public Iterator<String> getLibraryPaths() {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibraryPaths", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Collection<String> libraries = new ArrayList<String>();
		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
			
			Iterator<String> thisDatabaseDescriptorLibraries = databaseDescriptor.getLibraryPaths();
			while(thisDatabaseDescriptorLibraries.hasNext()) {
				libraries.add(thisDatabaseDescriptorLibraries.next());
			}
		}
		
		return libraries.iterator();
	}

	/**
	 * Get all library paths based on Database Descriptor name.
	 * @param databaseDescriptorName Name of Database Descriptor.
	 * @return Iterator which contains all library paths based on Database Descriptor.
	 */
	public Iterator<String> getLibraryPathsBasedOnDatabaseDescriptorName(final String databaseDescriptorName) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibraryPathsBasedOnDatabaseDescriptorName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		DatabaseDescriptor databaseDescriptor = this.applicationDescriptor.getDatabaseDescriptorBasedOnName(databaseDescriptorName);
		
		if(!databaseDescriptor.isLibrariesNeeded()) {
			return new EmptyIterator<String>();
		}
			
		return databaseDescriptor.getLibraryPaths();
	}

	/**
	 * Get all Library Descriptor objects as per contain in all Database Descriptor's.
	 * @return Iterator which contains all library descriptor objects defined in all Database Descriptor's.
	 */
	public Iterator<LibraryDescriptor> getLibraries() {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibraries", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Collection<LibraryDescriptor> libraries = new ArrayList<LibraryDescriptor>();
		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			if(!databaseDescriptor.isLibrariesNeeded()) {
				continue;
			}
			
			Iterator<LibraryDescriptor> databaseDescriptorLibraries = databaseDescriptor.getLibraryDescriptors();
			while(databaseDescriptorLibraries.hasNext()) {
				libraries.add(databaseDescriptorLibraries.next());
			}
		}
		
		return libraries.iterator();
	}


	/**
	 * Get all Library Descriptor objects based on Database Descriptor name.
	 * @param databaseDescriptorName Name of Database Descriptor.
	 * @return Iterator which contains all Library Descriptor objects based on Database Descriptor name.
	 */
	public Iterator<LibraryDescriptor> getLibrariesBasedOnDatabaseDescriptorName(final String databaseDescriptorName) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibrariesBasedOnDatabaseDescriptorName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		DatabaseDescriptor databaseDescriptor = this.applicationDescriptor.getDatabaseDescriptorBasedOnName(databaseDescriptorName);
		if(databaseDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibrariesBasedOnDatabaseDescriptorName", "Siminov Not Active, INVALID DATABASE-DESCRIPTOR FOUND");
		}
		
		if(!databaseDescriptor.isLibrariesNeeded()) {
			return new EmptyIterator<LibraryDescriptor>();
		}
			
		return databaseDescriptor.getLibraryDescriptors();
	}

	/**
	 * Get all Library Database Mapping objects based in library descriptor path.
	 * @param libraryPath Library Descriptor path.
	 * @return
	 */
	public Iterator<DatabaseMappingDescriptor> getLibraryDatabaseMappingDescriptorsBasedOnLibraryDescriptorPath(final String libraryPath) {
		Siminov.validateSiminov();
		
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getLibraryDatabaseMappingsBasedOnLibraryDescriptorPath", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			if(!databaseDescriptor.containsLibraryBasedOnPath(libraryPath)) {
				continue;
			}
			
			return databaseDescriptor.getLibraryDescriptorBasedOnPath(libraryPath).getDatabseMappings();
		}
		
		return new EmptyIterator<DatabaseMappingDescriptor>();
	}
	
	/**
	 * Get IDatabase object based on Database Descriptor name.
	 * @param databaseName Name of Database Descriptor.
	 * @return IDatabase object.
	 */
	public DatabaseBundle getDatabaseBundleBasedOnDatabaseDescriptorName(final String databaseName) {
		Siminov.validateSiminov();
		
		return this.databaseBundleBasedOnDatabaseDescriptorName.get(databaseName);
	}

	/**
	 * Get IDatabase object based on Database Mapping POJO class name.
	 * @param classObject POJO class object.
	 * @return IDatabase object.
	 */
	public DatabaseBundle getDatabaseBundleBasedOnDatabaseMappingDescriptorPojoClass(final Class<?> classObject) {
		Siminov.validateSiminov();

		return this.databaseBundleBasedOnDatabaseMappingClassName.get(classObject.getName());
	}
	
	/**
	 * Get IDatabase based on Database Mapping POJO class name.
	 * @param databaseMappingClassName POJO class name.
	 * @return IDatabase object.
	 */
	public DatabaseBundle getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(final String databaseMappingClassName) {
		Siminov.validateSiminov();

		return this.databaseBundleBasedOnDatabaseMappingClassName.get(databaseMappingClassName);
	}
	
	/**
	 * Get IDatabase object based on Database Mapping table name.
	 * @param databaseMappingTableName Database Mapping table name.
	 * @return IDatabase object.
	 */
	public DatabaseBundle getDatabaseBundleBasedOnDatabaseMappingDescriptorTableName(final String databaseMappingTableName) {
		Siminov.validateSiminov();

		return this.databaseBundleBasedOnDatabaseMappingTableName.get(databaseMappingTableName);
	}

	/**
	 * Get all IDatabase objects contain by application.
	 * @return Iterator which contains all IDatabase objects.
	 */
	public Iterator<DatabaseBundle> getDatabaseBundles() {
		Siminov.validateSiminov();
		
		return this.databaseBundleBasedOnDatabaseDescriptorName.values().iterator();
	}
	
	/**
	 * Add IDatabase object in respect to Database Descriptor name.
	 * @param databaseDescriptorName Database Descriptor name.
	 * @param database IDatabase object.
	 */
	public void addDatabaseBundle(final String databaseDescriptorName, final DatabaseBundle databaseBundle) {
		Siminov.validateSiminov();
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptorBasedOnName(databaseDescriptorName);
		if(databaseDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "addDatabase", "Siminov Not Active, INVALID DATABASE-DESCRIPTOR FOUND.");
		}
		
		Iterator<DatabaseMappingDescriptor> databaseMappingDescriptors = databaseDescriptor.getDatabaseMappings();
		while(databaseMappingDescriptors.hasNext()) {
			DatabaseMappingDescriptor databaseMapping = databaseMappingDescriptors.next();
			
			this.databaseBundleBasedOnDatabaseMappingClassName.put(databaseMapping.getClassName(), databaseBundle);
			this.databaseBundleBasedOnDatabaseMappingTableName.put(databaseMapping.getTableName(), databaseBundle);
		}
		
		Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.getLibraryDescriptors();
		while(libraryDescriptors.hasNext()) {
			LibraryDescriptor libraryDescriptor = libraryDescriptors.next();
			
			Iterator<DatabaseMappingDescriptor> libraryDatabaseMappings = libraryDescriptor.getDatabseMappings();
			while(libraryDatabaseMappings.hasNext()) {
				DatabaseMappingDescriptor libraryDatabaseMapping = libraryDatabaseMappings.next();
				
				this.databaseBundleBasedOnDatabaseMappingClassName.put(libraryDatabaseMapping.getClassName(), databaseBundle);
				this.databaseBundleBasedOnDatabaseMappingTableName.put(libraryDatabaseMapping.getTableName(), databaseBundle);
			}
		}
		
		this.databaseBundleBasedOnDatabaseDescriptorName.put(databaseDescriptorName, databaseBundle);
	}

	/**
	 * Remove IDatabase object from Resources.
	 * @param databaseBundle IDatabase object which needs to be removed.
	 */
	public void removeDatabaseBundle(final DatabaseBundle databaseBundle) {
		this.databaseBundleBasedOnDatabaseMappingClassName.values().remove(databaseBundle);
		this.databaseBundleBasedOnDatabaseMappingTableName.values().remove(databaseBundle);
		this.databaseBundleBasedOnDatabaseDescriptorName.values().remove(databaseBundle);
	}
	
	/**
	 * Remove IDatabase object from Resources based on Database Descriptor name.
	 * @param databaseName Database Descriptor name.
	 */
	public void removeDatabaseBundleBasedOnDatabaseDescriptorName(final String databaseName) {
		removeDatabaseBundle(this.databaseBundleBasedOnDatabaseDescriptorName.get(databaseName));
	}
	
	/**
	 * Remove IDatabase object from Resources based on Database Mapping POJO class name.
	 * @param className POJO class name
	 */
	public void removeDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(final String className) {
		removeDatabaseBundle(this.databaseBundleBasedOnDatabaseMappingClassName.get(className));
	}
	
	/**
	 * Remove IDatabase object from Resources based on Database Mapping table name.
	 * @param tableName Name of table.
	 */
	public void removeDatabaseBundleBasedOnDatabaseMappingDescriptorTableName(final String tableName) {
		removeDatabaseBundle(this.databaseBundleBasedOnDatabaseMappingTableName.get(tableName));
	}
	
	/**
	 * It used to synchronize IDatabase objects to its mapping.
	 * <p>
	 * It is used when SIMINOV is in lazy load mode.
	 */
	public void synchronizeDatabases() {
		Siminov.validateSiminov();

		Iterator<String> databaseNames = this.databaseBundleBasedOnDatabaseDescriptorName.keySet().iterator();
		while(databaseNames.hasNext()) {
			String databaseName = databaseNames.next();
			
			DatabaseBundle databaseBundle = getDatabaseBundleBasedOnDatabaseDescriptorName(databaseName);
			DatabaseDescriptor databaseDescriptor = getDatabaseDescriptorBasedOnName(databaseName);
			if(databaseDescriptor == null) {
				throw new DeploymentException(Resources.class.getName(), "synchronizeDatabase", "Siminov Not Active, INVALID DATABASE-DESCRIPTOR FOUND.");
			}
			
			Iterator<DatabaseMappingDescriptor> databaseMappings = databaseDescriptor.getDatabaseMappings();
			while(databaseMappings.hasNext()) {
				DatabaseMappingDescriptor databaseMapping = databaseMappings.next();
				
				this.databaseBundleBasedOnDatabaseMappingClassName.put(databaseMapping.getClassName(), databaseBundle);
				this.databaseBundleBasedOnDatabaseMappingTableName.put(databaseMapping.getTableName(), databaseBundle);
			}
			
			Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.getLibraryDescriptors();
			while(libraryDescriptors.hasNext()) {
				LibraryDescriptor libraryDescriptor = libraryDescriptors.next();
				
				Iterator<DatabaseMappingDescriptor> libraryDatabaseMappings = libraryDescriptor.getDatabseMappings();
				while(libraryDatabaseMappings.hasNext()) {
					DatabaseMappingDescriptor libraryDatabaseMapping = libraryDatabaseMappings.next();
					
					this.databaseBundleBasedOnDatabaseMappingClassName.put(libraryDatabaseMapping.getClassName(), databaseBundle);
					this.databaseBundleBasedOnDatabaseMappingTableName.put(libraryDatabaseMapping.getTableName(), databaseBundle);
				}
			}
		}
	}

	/**
	 * Get SIMINOV-EVENT Handler
	 * @return ISiminovEvents implementation object as per defined by application.
	 */
	public ISiminovEvents getSiminovEventHandler() {
		return EventHandler.getInstance().getSiminovEventHandler();
	}
	
	/**
	 * Get DATABASE-EVENT Handler
	 * @return IDatabaseEvents implementation object as per defined by application.
	 */
	public IDatabaseEvents getDatabaseEventHandler() {
		return EventHandler.getInstance().getDatabaseEventHandler();
	}
	
}
