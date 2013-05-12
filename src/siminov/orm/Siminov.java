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

package siminov.orm;

import java.io.File;
import java.util.Iterator;

import siminov.orm.database.Database;
import siminov.orm.database.DatabaseBundle;
import siminov.orm.database.DatabaseUtils;
import siminov.orm.database.design.IDatabase;
import siminov.orm.events.IDatabaseEvents;
import siminov.orm.events.ISiminovEvents;
import siminov.orm.exception.DatabaseException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.LibraryDescriptor;
import siminov.orm.parsers.ApplicationDescriptorParser;
import siminov.orm.parsers.DatabaseDescriptorParser;
import siminov.orm.parsers.DatabaseMappingDescriptorParser;
import siminov.orm.parsers.LibraryDescriptorParser;
import siminov.orm.resource.Resources;
import android.content.Context;


/**
 * Exposes methods to deal with SIMINOV FRAMEWORK.
 *	<p>
 *		Such As
 *		<p>
 *			1. Initialize: Entry point to the SIMINOV.
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
	protected static boolean processedDatabaseMappingDescriptors = false;

	protected static Resources ormResources = Resources.getInstance();
	
	
	/**
	 * It is used to check whether SIMINOV FRAMEWORK is active or not.
	 * <p>
	 * SIMINOV become active only when deployment of application is successful.
	 * 
	 * @exception If SIMINOV is not active it will throw DeploymentException which is RuntimeException.
	 * 
	 */
	public static void validateSiminov() {
		if(!isActive) {
			throw new DeploymentException(Siminov.class.getName(), "validateSiminov", "Siminov Not Active.");
		}
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

	public class ApplicationSiminov extends Application {

		public void onCreate() { 
			super.onCreate();
	
			initializeSiminov();
		}
		
		private void initializeSiminov() {
			com.core.Siminov.initialize(this);
		}

	}
					</li>
					
					<li> Call it from LAUNCHER Activity

	public class HomeActivity extends Activity {
	
		public void onCreate(Bundle savedInstanceState) {
		
		}

		private void initializeSiminov() {
			com.core.Siminov.initialize(getApplicationContext())
		}

	}
					</li>
				</ul>
			</pre>
	 * @param context Application content.
	 * @exception If any exception occur while deploying application it will through DeploymentException, which is RuntimeException.
	 */
	public static void initialize(final Context context) {
		
		if(isActive) {
			return;
		}
		
		if(context == null) {
			Log.logd(Siminov.class.getName(), "initialize", "Invalid Context Found.");
			throw new DeploymentException(Siminov.class.getName(), "initialize", "Invalid Context Found.");
		}
		
		ormResources.setApplicationContext(context);

		process();
		
		isActive = true;

		ISiminovEvents coreEventHandler = ormResources.getSiminovEventHandler();
		if(ormResources.getSiminovEventHandler() != null) {
			if(firstTimeProcessed) {
				coreEventHandler.firstTimeSiminovInitialized();
			} else {
				coreEventHandler.siminovInitialized();
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
	public static void shutdown() throws SiminovException {
		validateSiminov();
		
		Iterator<DatabaseDescriptor> databaseDescriptors = ormResources.getDatabaseDescriptors();

		boolean failed = false;
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			DatabaseBundle databaseBundle = ormResources.getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
			IDatabase database = databaseBundle.getDatabase();
			
			try {
				database.close(databaseDescriptor);
			} catch(DatabaseException databaseException) {
				failed = true;
				
				Log.loge(Siminov.class.getName(), "shutdown", "DatabaseException caught while closing database, " + databaseException.getMessage());
				continue;
			}
		}
		
		if(failed) {
			throw new SiminovException(Siminov.class.getName(), "shutdown", "DatabaseException caught while closing database.");			
		}
		
		ISiminovEvents coreEventHandler = ormResources.getSiminovEventHandler();
		if(ormResources.getSiminovEventHandler() != null) {
			coreEventHandler.siminovStopped();
		}
	}
	
	
	private static void process() {
		
		processApplicationDescriptor();
		processDatabaseDescriptors();
		processLibraries();
		processDatabaseMappingDescriptors();

		processDatabase();
		
	}
	
	
	protected static void processApplicationDescriptor() {
		ApplicationDescriptorParser applicationDescriptorParser = null;
		
		try {
			applicationDescriptorParser = new ApplicationDescriptorParser();
		} catch(SiminovException siminovException) {
			Log.logd(Siminov.class.getName(), "processApplicationDescriptor", "SiminovException caught while doing application descriptor parser, " + siminovException.getMessage());
			throw new DeploymentException(Siminov.class.getName(), "processApplicationDescriptor", siminovException.getMessage());
		} catch(DeploymentException deploymentException) {
			Log.logd(Siminov.class.getName(), "processApplicationDescriptor", "DeploymentException caught while doing application descriptor parser, " + deploymentException.getMessage());
			throw new DeploymentException(Siminov.class.getName(), "processApplicationDescriptor", deploymentException.getMessage());
		}
		
		ApplicationDescriptor applicationDescriptor = applicationDescriptorParser.getApplicationDescriptor();
		if(applicationDescriptor == null) {
			Log.logd(Siminov.class.getName(), "processApplicationDescriptor", "Invalid Application Descriptor Found.");
			throw new DeploymentException(Siminov.class.getName(), "processApplicationDescriptor", "Invalid Application Descriptor Found.");
		}
		
		ormResources.setApplicationDescriptor(applicationDescriptor);
	}
	
	
	protected static void processDatabaseDescriptors() {
		Iterator<String> databaseDescriptorPaths = ormResources.getApplicationDescriptor().getDatabaseDescriptorPaths();
		while(databaseDescriptorPaths.hasNext()) {
			String databaseDescriptorPath = databaseDescriptorPaths.next();
			
			DatabaseDescriptorParser databaseDescriptorParser = null;
			
			try {
				databaseDescriptorParser = new DatabaseDescriptorParser(databaseDescriptorPath);
			} catch(SiminovException siminovException) {
				Log.loge(Siminov.class.getName(), "processDatabaseDescriptors", "SiminovException caught while parsing database descriptor, DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + siminovException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabaseDescriptors", siminovException.getMessage());
			}
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptorParser.getDatabaseDescriptor();
			if(databaseDescriptor == null) {
				Log.loge(Siminov.class.getName(), "processDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
				throw new DeploymentException(Siminov.class.getName(), "processDatabaseDescriptors", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
			}

			ormResources.getApplicationDescriptor().addDatabaseDescriptor(databaseDescriptorPath, databaseDescriptor);
			
		}
		
	}
	
	
	protected static void processLibraries() {
		
		ApplicationDescriptor applicationDescriptor = ormResources.getApplicationDescriptor();
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();

		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			Iterator<String> libraries = databaseDescriptor.getLibraryPaths();
			while(libraries.hasNext()) {
				String libraryName = libraries.next();
				
				/*
				 * Parse LibraryDescriptor.
				 */
				LibraryDescriptorParser libraryDescriptorParser = null;
				
				try {
					libraryDescriptorParser = new LibraryDescriptorParser(libraryName);
				} catch(SiminovException ce) {
					Log.loge(Siminov.class.getName(), "processLibraries", "SiminovException caught while parsing library descriptor, LIBRARY-NAME: " + libraryName + ", " + ce.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processLibraries", ce.getMessage());
				}
				
				databaseDescriptor.addLibrary(libraryName, libraryDescriptorParser.getLibraryDescriptor());
			}
		}
		
	}

	
	protected static void processDatabaseMappingDescriptors() {
		ApplicationDescriptor applicationDescriptor = ormResources.getApplicationDescriptor();
		if(!applicationDescriptor.isLoadInitially()) {
			return;
		}
		
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			Iterator<String> libraries = databaseDescriptor.getLibraryPaths();
			while(libraries.hasNext()) {
				String libraryPath = libraries.next();
				LibraryDescriptor libraryDescriptor = databaseDescriptor.getLibraryDescriptorBasedOnPath(libraryPath);
				
				Iterator<String> libraryDatabaseMappingPaths = libraryDescriptor.getDatabaseMappingPaths();
				while(libraryDatabaseMappingPaths.hasNext()) {
					String libraryDatabaseMappingPath = libraryDatabaseMappingPaths.next();
					DatabaseMappingDescriptorParser databaseMappingParser = null;
					
					try {
						databaseMappingParser = new DatabaseMappingDescriptorParser(libraryPath, libraryDatabaseMappingPath);
					} catch(SiminovException ce) {
						Log.loge(Siminov.class.getName(), "processDatabaseMappingDescriptors", "SiminovException caught while parsing database mapping, LIBRARY-DATABASE-MAPPING: " + libraryDatabaseMappingPath + ", " + ce.getMessage());
						throw new DeploymentException(Siminov.class.getName(), "processDatabaseMappingDescriptors", "LIBRARY-DATABASE-MAPPING: " + libraryDatabaseMappingPath + ", " + ce.getMessage());
					}
					
					libraryDescriptor.addDatabaseMapping(libraryDatabaseMappingPath, databaseMappingParser.getDatabaseMapping());
				}
			}

			
			Iterator<String> databaseMappingPaths = databaseDescriptor.getDatabaseMappingPaths();
			while(databaseMappingPaths.hasNext()) {
				String databaseMappingPath = databaseMappingPaths.next();
				DatabaseMappingDescriptorParser databaseMappingParser = null;
				
				try {
					databaseMappingParser = new DatabaseMappingDescriptorParser(databaseMappingPath);
				} catch(SiminovException ce) {
					Log.loge(Siminov.class.getName(), "processDatabaseMappingDescriptors", "SiminovException caught while parsing database mapping, DATABASE-MAPPING: " + databaseMappingPath + ", " + ce.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabaseMappingDescriptors", "NAME: " + databaseMappingPath + ", " + ce.getMessage());
				}
				
				databaseDescriptor.addDatabaseMapping(databaseMappingPath, databaseMappingParser.getDatabaseMapping());
			}
		}
		
		processedDatabaseMappingDescriptors = true;
		
	}

	
	protected static void processDatabase() {
		
		ApplicationDescriptor applicationDescriptor = ormResources.getApplicationDescriptor();
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
				
		while(databaseDescriptors.hasNext()) {
			
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			
			String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);
			DatabaseBundle databaseBundle = null;
			
			try {
				databaseBundle = Database.createDatabase(databaseDescriptor);
			} catch(DatabaseException databaseException) {
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while getting database instance from database factory, DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

			IDatabase database = databaseBundle.getDatabase();
			
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
				try {
					database.openOrCreate(databaseDescriptor);					
					ormResources.addDatabaseBundle(databaseDescriptor.getDatabaseName(), databaseBundle);
				} catch(DatabaseException databaseException) {
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while opening database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
				try {
					// Enable foreign key constraints
			        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				try {
			        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isLockingRequired());
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

			} else {
				
				firstTimeProcessed = true;
				
				if(!processedDatabaseMappingDescriptors) {
					processDatabaseMappingDescriptors();
				}
				
				file = new File(databasePath);
				try {
					file.mkdirs();
				} catch(Exception exception) {
					Log.loge(Siminov.class.getName(), "processDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
				}
				
				try {
					database.openOrCreate(databaseDescriptor);			
					ormResources.addDatabaseBundle(databaseDescriptor.getDatabaseName(), databaseBundle);
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
				
				IDatabaseEvents databaseEventHandler = ormResources.getDatabaseEventHandler();
				if(databaseEventHandler != null) {
					databaseEventHandler.databaseCreated(databaseDescriptor);
				}

				
				try {
					// Enable foreign key constraints
			        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				try {
			        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isLockingRequired());
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}

				Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.orderedLibraryDescriptors();
				while(libraryDescriptors.hasNext()) {
					try {
						Database.createTables(libraryDescriptors.next().orderedDatabaseMappings());			
					} catch(DatabaseException databaseException) {
						new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
						
						Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating tables for library, " + databaseException.getMessage());
						throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
					}
				}

				try {
					Database.createTables(databaseDescriptor.orderedDatabaseMappings());			
				} catch(DatabaseException databaseException) {
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating tables, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
				
			}
			
		}
		
	}
}
