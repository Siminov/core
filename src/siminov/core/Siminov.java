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

package siminov.core;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import siminov.core.database.DatabaseBundle;
import siminov.core.database.DatabaseHelper;
import siminov.core.database.DatabaseUtils;
import siminov.core.database.design.IDatabaseImpl;
import siminov.core.database.design.IQueryBuilder;
import siminov.core.events.IDatabaseEvents;
import siminov.core.events.ISiminovEvents;
import siminov.core.exception.DatabaseException;
import siminov.core.exception.DeploymentException;
import siminov.core.exception.SiminovCriticalException;
import siminov.core.exception.SiminovException;
import siminov.core.log.Log;
import siminov.core.model.ApplicationDescriptor;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.LibraryDescriptor;
import siminov.core.reader.ApplicationDescriptorReader;
import siminov.core.reader.DatabaseDescriptorReader;
import siminov.core.reader.DatabaseMappingDescriptorReader;
import siminov.core.reader.LibraryDescriptorReader;
import siminov.core.resource.ResourceManager;


/**
 * Exposes methods to deal with SIMINOV FRAMEWORK.
 *	<p>
 *		Such As
 *		<p>
 *			1. Initializer: Entry point to the SIMINOV.
 *		</p>
 *	
 *		<p>
 *			2. Shutdown: Exit point from the SIMINOV.
 *		</p>
 *	</p>
 */
public class Siminov {

	protected static boolean isActive = false;
	protected static boolean firstTimeProcessed = false;

	protected static ResourceManager ormResourceManager = ResourceManager.getInstance();
	
	
	/**
	 * It is used to check whether SIMINOV FRAMEWORK is active or not.
	 * <p>
	 * SIMINOV become active only when deployment of application is successful.
	 * 
	 * @exception If SIMINOV is not active it will throw DeploymentException which is RuntimeException.
	 * 
	 */
	public static void isActive() {
		if(!isActive) {
			throw new DeploymentException(Siminov.class.getName(), "isActive", "Siminov Not Active.");
		}
	}
	
	
	/**
	 * Returns the IInitializer instance.
	 * @return Instance of IInitializer
	 */
	public static IInitializer initializer() {
		return new Initializer();
	}
	
	/**
	 * It is the entry point to the SIMINOV FRAMEWORK.
	 * <p>
	 * When application starts it should call this method to activate SIMINOV-FRAMEWORK, by providing ApplicationContext as the parameter.
	 * </p>
	 * 
	 * <p>
	 * Siminov will read all descriptor defined by application, and do necessary processing.
	 * </p>
	 * 
	 * 	EXAMPLE
	 * 		There are two ways to make a call.
	 * 
			<pre> 
	  			<ul>
	  				<li> Call it from Application class.

	public class Siminov extends Application {

		public void onCreate() { 
			super.onCreate();
	
			initializeSiminov();
		}
		
		private void initializeSiminov() {
			siminov.core.Siminov.initialize(this);
		}

	}
					</li>
					
					<li> Call it from LAUNCHER Activity

	public class SiminovActivity extends Activity {
	
		public void onCreate(Bundle savedInstanceState) {
		
		}

		private void initializeSiminov() {
			siminov.core.Siminov.initialize(getApplicationContext())
		}

	}
					</li>
				</ul>
			</pre>
	 * @param context Application content.
	 * @exception If any exception occur while deploying application it will through DeploymentException, which is RuntimeException.
	 */
	static void start() {
		
		if(isActive) {
			return;
		}
		
		process();
		
		isActive = true;

		ISiminovEvents coreEventHandler = ormResourceManager.getSiminovEventHandler();
		if(ormResourceManager.getSiminovEventHandler() != null) {
			if(firstTimeProcessed) {
				coreEventHandler.onFirstTimeSiminovInitialized();
			} else {
				coreEventHandler.onSiminovInitialized();
			}
		} 
	}


	/**
	 * It is used to stop all service started by SIMINOV.
	 * <p>
	 * When application shutdown they should call this. It do following services: 
	 * <p>
	 * 		<pre>
	 * 			<ul>
	 * 				<li> Close all database's opened by SIMINOV.
	 * 				<li> Deallocate all resources held by SIMINOV.
	 * 			</ul>
	 *		</pre>
	 *	</p>
	 * 
	 * @throws SiminovException If any error occur while shutting down SIMINOV.
	 */
	public static void shutdown() {
		isActive();
		
		Iterator<DatabaseDescriptor> databaseDescriptors = ormResourceManager.getDatabaseDescriptors();

		boolean failed = false;
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			DatabaseBundle databaseBundle = ormResourceManager.getDatabaseBundle(databaseDescriptor.getDatabaseName());
			IDatabaseImpl database = databaseBundle.getDatabase();
			
			try {
				database.close(databaseDescriptor);
			} catch(DatabaseException databaseException) {
				failed = true;
				
				Log.error(Siminov.class.getName(), "shutdown", "DatabaseException caught while closing database, " + databaseException.getMessage());
				continue;
			}
		}
		
		if(failed) {
			throw new SiminovCriticalException(Siminov.class.getName(), "shutdown", "DatabaseException caught while closing database.");			
		}
		
		ISiminovEvents coreEventHandler = ormResourceManager.getSiminovEventHandler();
		if(ormResourceManager.getSiminovEventHandler() != null) {
			coreEventHandler.onSiminovStopped();
		}
	}
	
	
	private static void process() {
		
		processApplicationDescriptor();
		processDatabaseDescriptors();
		processLibraries();
		processDatabaseMappingDescriptors();

		processDatabase();
	}
	
	/**
	 * It process ApplicationDescriptor.si.xml file defined in Application, and stores in Resource Manager.
	 */
	protected static void processApplicationDescriptor() {
		ApplicationDescriptorReader applicationDescriptorParser = new ApplicationDescriptorReader();
		
		ApplicationDescriptor applicationDescriptor = applicationDescriptorParser.getApplicationDescriptor();
		if(applicationDescriptor == null) {
			Log.debug(Siminov.class.getName(), "processApplicationDescriptor", "Invalid Application Descriptor Found.");
			throw new DeploymentException(Siminov.class.getName(), "processApplicationDescriptor", "Invalid Application Descriptor Found.");
		}
		
		ormResourceManager.setApplicationDescriptor(applicationDescriptor);
	}
	
	
	/**
	 * It process all DatabaseDescriptor.si.xml files defined by Application and stores in Resource Manager.
	 */
	protected static void processDatabaseDescriptors() {
		Iterator<String> databaseDescriptorPaths = ormResourceManager.getApplicationDescriptor().getDatabaseDescriptorPaths();
		while(databaseDescriptorPaths.hasNext()) {

			String databaseDescriptorPath = databaseDescriptorPaths.next();
			
			DatabaseDescriptorReader databaseDescriptorParser = new DatabaseDescriptorReader(databaseDescriptorPath);
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptorParser.getDatabaseDescriptor();
			if(databaseDescriptor == null) {
				Log.error(Siminov.class.getName(), "processDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
				throw new DeploymentException(Siminov.class.getName(), "processDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
			}

			ormResourceManager.getApplicationDescriptor().addDatabaseDescriptor(databaseDescriptorPath, databaseDescriptor);
		}
	}
	
	
	/**
	 * It process all LibraryDescriptor.si.xml files defined by application, and stores in Resource Manager.
	 */
	protected static void processLibraries() {
		
		ApplicationDescriptor applicationDescriptor = ormResourceManager.getApplicationDescriptor();
		Iterator<String> libraries = applicationDescriptor.getLibraryDescriptorPaths();
		
		while(libraries.hasNext()) {
			
			String library = libraries.next();

			/*
			 * Parse LibraryDescriptor.
			 */
			LibraryDescriptorReader libraryDescriptorReader = new LibraryDescriptorReader(library);
			LibraryDescriptor libraryDescriptor = libraryDescriptorReader.getLibraryDescriptor();
			
		
			/*
			 * Map Database Mapping Descriptors
			 */
			Iterator<String> databaseMappingDescriptors = libraryDescriptor.getDatabaseMappingPaths();
			while(databaseMappingDescriptors.hasNext()) {

				String libraryDatabaseMappingDescriptorPath = databaseMappingDescriptors.next();
				
				String databaseDescriptorName = libraryDatabaseMappingDescriptorPath.substring(0, libraryDatabaseMappingDescriptorPath.indexOf(Constants.LIBRARY_DESCRIPTOR_DATABASE_MAPPING_SEPRATOR));
				String databaseMappingDescriptor = libraryDatabaseMappingDescriptorPath.substring(libraryDatabaseMappingDescriptorPath.indexOf(Constants.LIBRARY_DESCRIPTOR_DATABASE_MAPPING_SEPRATOR) + 1, libraryDatabaseMappingDescriptorPath.length());
				
				
				Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
				while(databaseDescriptors.hasNext()) {
					
					DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
					if(databaseDescriptor.getDatabaseName().equalsIgnoreCase(databaseDescriptorName)) {
						databaseDescriptor.addDatabaseMappingDescriptorPath(library.replace(".", "/") + File.separator + databaseMappingDescriptor);
					}
				}
			}
		}
	}

	
	
	/**
	 * It process all DatabaseMappingDescriptor.si.xml file defined in Application, and stores in Resource Manager.
	 */
	protected static void processDatabaseMappingDescriptors() {
		doesDatabaseExists();

		ApplicationDescriptor applicationDescriptor = ormResourceManager.getApplicationDescriptor();
		
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			Iterator<String> databaseMappingPaths = databaseDescriptor.getDatabaseMappingDescriptorPaths();

			while(databaseMappingPaths.hasNext()) {
				String databaseMappingPath = databaseMappingPaths.next();
				DatabaseMappingDescriptorReader databaseMappingParser = new DatabaseMappingDescriptorReader(databaseMappingPath);
				
				databaseDescriptor.addDatabaseMappingDescriptor(databaseMappingPath, databaseMappingParser.getDatabaseMappingDescriptor());
			}
		}
	}


	/**
	 * It process all DatabaseDescriptor.si.xml and initialize Database and stores in Resource Manager.
	 */
	protected static void processDatabase() {
		
		ApplicationDescriptor applicationDescriptor = ormResourceManager.getApplicationDescriptor();
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
				
		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);
			DatabaseBundle databaseBundle = null;
			
			try {
				databaseBundle = DatabaseHelper.createDatabase(databaseDescriptor);
			} catch(DatabaseException databaseException) {
				Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while getting database instance from database factory, DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

			IDatabaseImpl database = databaseBundle.getDatabase();
			IQueryBuilder queryBuilder = databaseBundle.getQueryBuilder();
			
			
			
			/*
			 * If Database exists then open and return.
			 * If Database does not exists create the database.
			 */
			
			String databaseName = databaseDescriptor.getDatabaseName();
			if(!databaseName.endsWith(".db")) {
				databaseName = databaseName + ".db";
			}

			
			File file = new File(databasePath + databaseName);
			if(file.exists()) {
				
				/*
				 * Open Database
				 */
				try {
					database.openOrCreate(databaseDescriptor);					
				} catch(DatabaseException databaseException) {
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while opening database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
				
				/*
				 * Enable Foreign Key Constraints
				 */
				try {
			        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				
				/*
				 * Safe MultiThread Transaction
				 */
				try {
			        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isTransactionSafe());
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				
				
				/*
				 * Upgrade Database
				 */
				try {
					DatabaseHelper.upgradeDatabase(databaseDescriptor);
				} catch(DatabaseException databaseException) {
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while upgrading database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
			} else {
				
				/*
				 * Create Database Directory
				 */
				file = new File(databasePath);
				try {
					file.mkdirs();
				} catch(Exception exception) {
					Log.error(Siminov.class.getName(), "processDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", exception.getMessage());
				}
				
				
				/*
				 * Create Database File.
				 */
				try {
					database.openOrCreate(databaseDescriptor);			
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				

				/*
				 * Set Database Version
				 */
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put(IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER, databaseDescriptor.getVersion());

				
				try {
					
					String updateDatabaseVersionQuery = queryBuilder.formUpdateDatabaseVersionQuery(parameters);
					database.executeQuery(databaseDescriptor, null, updateDatabaseVersionQuery);
				} catch(DatabaseException databaseException) {
					Log.error(Siminov.class.getName(), "processDatabase", "Database Exception caught while updating database version, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
				
				
				IDatabaseEvents databaseEventHandler = ormResourceManager.getDatabaseEventHandler();
				if(databaseEventHandler != null) {
					databaseEventHandler.onDatabaseCreated(databaseDescriptor);
				}

				
				/*
				 * Enable Foreign Key Constraints
				 */
				try {
			        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				
				/*
				 * Safe MultiThread Transaction
				 */
				try {
			        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isTransactionSafe());
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				
				
				/*
				 * Create Tables
				 */
				try {
					DatabaseHelper.createTables(databaseDescriptor.orderedDatabaseMappingDescriptors());			
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.error(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating tables, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
			}
			
		}
	}
	
	
	/**
	 * It is used to check whether database exists or not. 
	 */
	protected static void doesDatabaseExists() {
		
		ApplicationDescriptor applicationDescriptor = ormResourceManager.getApplicationDescriptor();
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		
		boolean databaseExists = true;
		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);

			String databaseName = databaseDescriptor.getDatabaseName();
			if(!databaseName.endsWith(".db")) {
				databaseName = databaseName + ".db";
			}

			
			File file = new File(databasePath + databaseName);
			if(!file.exists()) {
				databaseExists = false;
			}
		}
		
		if(!databaseExists) {
			firstTimeProcessed = true;
		} else {
			firstTimeProcessed = false;
		}
	}
	
}
