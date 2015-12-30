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


package siminov.core.resource;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import siminov.core.database.DatabaseBundle;
import siminov.core.database.DatabaseFactory;
import siminov.core.events.EventHandler;
import siminov.core.events.IDatabaseEvents;
import siminov.core.events.ISiminovEvents;
import siminov.core.exception.DeploymentException;
import siminov.core.exception.SiminovCriticalException;
import siminov.core.exception.SiminovException;
import siminov.core.log.Log;
import siminov.core.model.ApplicationDescriptor;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.EntityDescriptor;
import siminov.core.reader.QuickEntityDescriptorReader;
import android.content.Context;



/**
 * It handles and provides all resources needed by SIMINOV.
 * <p>
 * Such As: Provides Application Descriptor, Database Descriptor, Library Descriptor, Entity Descriptor.
 */
public class ResourceManager {

	/*
	 * Resources.
	 */
	private Context applicationContext = null;
	
	private ApplicationDescriptor applicationDescriptor = null;
	private DatabaseFactory databaseFactory = null;
	
	private static ResourceManager resources = null;
	
	/**
	 * Resource Private Constructor
	 */
	private ResourceManager() {

		databaseFactory = DatabaseFactory.getInstance();
	}

	/**
	 * It provides an singleton instance of ResourceManager class.
	 * 
	 * @return Resources instance.
	 */
	public static ResourceManager getInstance() {
		if(resources == null) {
			resources = new ResourceManager();
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
Example: ApplicationDescriptor.xml
	
	{@code
	<siminov>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.xml</database-descriptor>
		</database-descriptors>

	</siminov>
	}
	
			</pre>
		</p>
	 * @return Iterator which contains all database descriptor paths provided.
	 */
	public Iterator<String> getDatabaseDescriptorPaths() {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorsPaths", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorPaths();
	}

	/**
	 * Get DatabaseDescriptor based on path provided as per defined in Application Descriptor file.
		<p>
			<pre>
			
Example: ApplicationDescriptor.xml
	
	{@code
	<siminov>
	
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.xml</database-descriptor>
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
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorBasedOnPath", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
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
	
		<property name="database_name">SIMINOV-CORE-SAMPLE</property>
		
	</database-descriptor>
	}
	
			</pre>
		</p>
	 
	 * 
	 * @param databaseDescriptorName Database Descriptor object based on database descriptor name provided.
	 */
	public DatabaseDescriptor getDatabaseDescriptorBasedOnName(final String databaseDescriptorName) {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorBasedOnName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND.");
		}

		return this.applicationDescriptor.getDatabaseDescriptorBasedOnName(databaseDescriptorName);
	}
	
	/**
	 * Get all Database Descriptors object.
	 * @return Iterator which contains all Database Descriptors.
	 */
	public Iterator<DatabaseDescriptor> getDatabaseDescriptors() {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptors", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
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
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnClassName(className);

			if(containsEntityDescriptorInDatabaseDescriptor) {
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
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorNameBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnClassName(className);

			if(containsEntityDescriptorInDatabaseDescriptor) {
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
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnTableName(tableName);

			if(containsEntityDescriptorInDatabaseDescriptor) {
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
			throw new DeploymentException(ResourceManager.class.getName(), "getDatabaseDescriptorNameBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnTableName(tableName);

			if(containsEntityDescriptorInDatabaseDescriptor) {
				return databaseDescriptor.getDatabaseName();
			}
		}
		
		return null;
	}

	/**
	 * Get Entity Descriptor based on mapped class name provided.
	 * 
	 * @param className Mapped class name.
	 * @return Entity Descriptor object in respect to mapped class name.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnClassName(final String className) {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(ResourceManager.class.getName(), "getEntityDescriptorBasedOnClassName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnClassName(className);

			if(containsEntityDescriptorInDatabaseDescriptor) {
				return databaseDescriptor.getEntityDescriptorBasedOnClassName(className);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Get Entity Descriptor based on table name provided.
	 * 
	 * @param tableName Name of table.
	 * @return Database Descriptor object in respect to table name.
	 */
	public EntityDescriptor getEntityDescriptorBasedOnTableName(final String tableName) {
		if(this.applicationDescriptor == null) {
			throw new DeploymentException(ResourceManager.class.getName(), "getEntityDescriptorBasedOnTableName", "Siminov Not Active, INVALID APPLICATION-DESCRIPTOR FOUND");
		}

		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			boolean containsEntityDescriptorInDatabaseDescriptor = databaseDescriptor.containsEntityDescriptorBasedOnTableName(tableName);

			if(containsEntityDescriptorInDatabaseDescriptor) {
				return databaseDescriptor.getEntityDescriptorBasedOnTableName(tableName);
			}
		}
		
		return null;
	}

	
	/**
	 * Get all entity descriptors
	 * @return Entity Descriptors
	 */
	public Iterator<EntityDescriptor> getEntityDescriptors() {
		Collection<EntityDescriptor> entityDescriptorCollection = new LinkedList<EntityDescriptor>();
		Iterator<DatabaseDescriptor> databaseDescriptors = this.applicationDescriptor.getDatabaseDescriptors();
	
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			Iterator<EntityDescriptor> entityDescriptors = databaseDescriptor.getEntityDescriptors();
			while(entityDescriptors.hasNext()) {
				EntityDescriptor entityDescriptor = entityDescriptors.next();
				entityDescriptorCollection.add(entityDescriptor);
			}
		}
		
		return entityDescriptorCollection.iterator();
	}

	
	/**
	 * Get entity descriptor Object based on class name provided. If entity descriptor object not present in resource layer, it will parse EntityDescriptor.xml file defined by application and will place it in resource layer.
	 * @param className Full name of class.
	 * @return EntityDescriptor object.
	 * @throws SiminovException If any exception occur while getting entity descriptor object.
	 */
	public EntityDescriptor requiredEntityDescriptorBasedOnClassName(final String className) {
		EntityDescriptor entityDescriptor = getEntityDescriptorBasedOnClassName(className);

		if(entityDescriptor == null) {
			Log.debug(getClass().getName(), "requiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, MODEL: " + className);
			
			QuickEntityDescriptorReader quickEntityDescriptorParser = null;
			try {
				quickEntityDescriptorParser = new QuickEntityDescriptorReader(className);
				quickEntityDescriptorParser.process();
			} catch(SiminovException ce) {
				Log.error(getClass().getName(), "requiredEntityDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: " + className + ", " + ce.getMessage());
				throw new SiminovCriticalException(getClass().getName(), "requiredEntityDescriptorBasedOnClassName(" + className + ")", "SiminovException caught while doing quick entity descriptor parsing, ENTITY-DESCRIPTOR-CLASS-NAME: " + className  + ", " + ce.getMessage());
			}
			
			EntityDescriptor foundEntityDescriptor = quickEntityDescriptorParser.getEntityDescriptor();
			if(foundEntityDescriptor == null) {
				Log.error(getClass().getName(), "requiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: " + className);
				throw new SiminovCriticalException(getClass().getName(), "requiredEntityDescriptorBasedOnClassName(" + className + ")", "Entity Descriptor Model Not registered With Siminov, ENTITY-DESCRIPTOR-MODEL: " + className);
			}
			
			return foundEntityDescriptor;
		}
		
		return entityDescriptor;
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
