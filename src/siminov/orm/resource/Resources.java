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

package siminov.orm.resource;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import siminov.orm.database.DatabaseBundle;
import siminov.orm.database.DatabaseFactory;
import siminov.orm.events.EventHandler;
import siminov.orm.events.IDatabaseEvents;
import siminov.orm.events.ISiminovEvents;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovCriticalException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.reader.QuickDatabaseMappingDescriptorReader;
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
	private DatabaseFactory databaseFactory = null;
	
	private static Resources resources = null;
	
	private Resources() {

		databaseFactory = DatabaseFactory.getInstance();
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
		return this.applicationContext;
	}
	
	/**
	 * Set Application context provided by application.
	 * @param context Application Context (android.content.Context).
	 */
	public void setApplicationContext(final Context context) {
		this.applicationContext = context;
	}
	
	/**
	 * Get Application Descriptor object of application.
	 * @return Application Descriptor.
	 */
	public ApplicationDescriptor getApplicationDescriptor() {
		return this.applicationDescriptor;
	}
	
	/**
	 * Set Application Descriptor of application.
	 * @param applicationDescriptor Application Descriptor object.
	 */
	public void setApplicationDescriptor(final ApplicationDescriptor applicationDescriptor) {
		this.applicationDescriptor = applicationDescriptor;
	}
	
	/**
	 * Get iterator of all database descriptors provided in Application Descriptor file.
		<p>
			<pre>
Example: ApplicationDescriptor.si.xml
	
	{@code
	<siminov>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
		</database-descriptors>

	</siminov>
	}
	
			</pre>
		</p>
	 * @return Iterator which contains all database descriptor paths provided.
	 */
	public Iterator<String> getDatabaseDescriptorPaths() {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorsPaths", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorPaths();
	}

	/**
	 * Get DatabaseDescriptor based on path provided as per defined in Application Descriptor file.
		<p>
			<pre>
			
Example: ApplicationDescriptor.si.xml
	
	{@code
	<siminov>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
		</database-descriptors>

	</siminov>
	}
	
			</pre>
		</p>
	 
	 * @param databaseDescriptorPath Iterator which contains all database descriptor paths provided.
	 * @return Database Descriptor
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath) {
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
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnClassName(className);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor;
			}
		}
		
		return null;
	}


	/**
	 * Get database descriptor name based on class name
	 * @param className Name of Class
	 * @return Database Descriptor Name
	 */
	public String getDatabaseDescriptorNameBasedOnClassName(final String className) {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorNameBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnClassName(className);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabaseName();
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
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnTableName(tableName);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor;
			}
		}
		
		return null;
	}

	
	/**
	 * Get database descriptor name based on table name
	 * @param tableName Name of Table
	 * @return Database Descriptor Name
	 */
	public String getDatabaseDescriptorNameBasedOnTableName(final String tableName) {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseDescriptorNameBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnTableName(tableName);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabaseName();
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
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseMappingBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnClassName(className);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabseMappingDescriptorBasedOnClassName(className);
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
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(Resources.class.getName(), "getDatabaseMappingBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsDatabaseMappingInDatabaseDescriptor = databaseDescriptor.containsDatabaseMappingDescriptorBasedOnTableName(tableName);

			if(containsDatabaseMappingInDatabaseDescriptor) {
				return databaseDescriptor.getDatabseMappingDescriptorBasedOnTableName(tableName);
			}
		}
		
		return null;
	}

	
	/**
	 * Get all database mapping descriptors
	 * @return Database Mapping Descriptors
	 */
	public Iterator<DatabaseMappingDescriptor> getDatabaseMappingDescriptors() {
		Collection<DatabaseMappingDescriptor> databaseMappingDescriptors = new LinkedList<DatabaseMappingDescriptor>();
		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
	
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			Iterator<DatabaseMappingDescriptor> databaseMappings = databaseDescriptor.getDatabaseMappingDescriptors();
			while(databaseMappings.hasNext()) {
				DatabaseMappingDescriptor databaseMappingDescriptor = databaseMappings.next();
				databaseMappingDescriptors.add(databaseMappingDescriptor);
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
	public DatabaseMappingDescriptor requiredDatabaseMappingDescriptorBasedOnClassName(final String className) {
		DatabaseMappingDescriptor databaseMapping = getDatabaseMappingDescriptorBasedOnClassName(className);

		if(databaseMapping == null) {
			Log.debug(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, MODEL: " + className);
			
			QuickDatabaseMappingDescriptorReader quickDatabaseMappingDescriptorParser = null;
			try {
				quickDatabaseMappingDescriptorParser = new QuickDatabaseMappingDescriptorReader(className);
				quickDatabaseMappingDescriptorParser.process();
			} catch(SiminovException ce) {
				Log.error(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick database mapping parsing, DATABASE-MAPPING-CLASS-NAME: " + className + ", " + ce.getMessage());
				throw new SiminovCriticalException(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick database mapping parsing, DATABASE-MAPPING-CLASS-NAME: " + className  + ", " + ce.getMessage());
			}
			
			DatabaseMappingDescriptor foundDatabaseMapping = quickDatabaseMappingDescriptorParser.getDatabaseMapping();
			if(foundDatabaseMapping == null) {
				Log.error(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, DATABASE-MAPPING-MODEL: " + className);
				throw new SiminovCriticalException(getClass().getName(), "requiredDatabaseMappingDescriptorBasedOnClassName(" + className + ")", "Database Mapping Model Not registered With Siminov, DATABASE-MAPPING-MODEL: " + className);
			}
			
			return foundDatabaseMapping;
		}
		
		return databaseMapping;
	}

	
	
	/**
	 * Get IDatabase object based on Database Descriptor name.
	 * @param databaseName Name of Database Descriptor.
	 * @return IDatabase object.
	 */
	public DatabaseBundle getDatabaseBundle(final String databaseName) {
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptorBasedOnName(databaseName);
		return this.databaseFactory.getDatabaseBundle(databaseDescriptor);
	}

	/**
	 * Get all IDatabase objects contain by application.
	 * @return Iterator which contains all IDatabase objects.
	 */
	public Iterator<DatabaseBundle> getDatabaseBundles() {
		return this.databaseFactory.getDatabaseBundles();
	}
	

	/**
	 * Remove IDatabase object from Resources based on Database Descriptor name.
	 * @param databaseDescriptorName Database Descriptor name.
	 */
	public void removeDatabaseBundle(final String databaseDescriptorName) {
		
		DatabaseDescriptor databaseDescriptor = getDatabaseDescriptorBasedOnName(databaseDescriptorName);
		this.databaseFactory.removeDatabaseBundle(databaseDescriptor);
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
