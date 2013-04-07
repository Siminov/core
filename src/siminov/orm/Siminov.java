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

	private static boolean isActive = true;
	private static boolean initialized = false;
	
	private static boolean firstTimeProcessed = false;
	
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
		
		if(initialized) {
			return;
		}
		
		if(context == null) {
			isActive = false;
			
			Log.logd(Siminov.class.getName(), "initialize", "Invalid Context Found.");
			throw new DeploymentException(Siminov.class.getName(), "initialize", "Invalid Context Found.");
		}
		
		Resources resources = Resources.getInstance();
		resources.setApplicationContext(context);

		parseApplicationDescriptor();

		initialize(resources.getApplicationContext(), resources.getApplicationDescriptor());
	}

	public static final void initialize(final Context context, final ApplicationDescriptor applicationDescriptor) {

		if(initialized) {
			return;
		}

		if(applicationDescriptor == null) {
			Log.loge(Siminov.class.getName(), "initialize", "Invalid ApplicationDescriptor Found.");
			throw new DeploymentException(Siminov.class.getName(), "initialize", "Invalid ApplicationDescriptor Found.");
		} else if(context == null) {
			Log.loge(Siminov.class.getName(), "initialize", "Invalid ApplicationContext Found.");
			throw new DeploymentException(Siminov.class.getName(), "initialize", "Invalid ApplicationContext Found.");
		}
		
		Resources resources = Resources.getInstance();
		resources.setApplicationDescriptor(applicationDescriptor);
		if(!resources.getApplicationDescriptor().isDatabaseNeeded()) {
			return;
		}

		process();
		
		
		initialized = true;

		ISiminovEvents coreEventHandler = resources.getSiminovEventHandler();
		if(resources.getSiminovEventHandler() != null) {
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
		
		Resources resources = Resources.getInstance();
		Iterator<DatabaseDescriptor> databaseDescriptors = resources.getDatabaseDescriptors();

		boolean failed = false;
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			DatabaseBundle databaseBundle = resources.getDatabaseBundleBasedOnDatabaseDescriptorName(databaseDescriptor.getDatabaseName());
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
		
		ISiminovEvents coreEventHandler = resources.getSiminovEventHandler();
		if(resources.getSiminovEventHandler() != null) {
			coreEventHandler.siminovStopped();
		}
	}
	
	private static void process() {
		Resources resources = Resources.getInstance();
		ApplicationDescriptor applicationDescriptor = Resources.getInstance().getApplicationDescriptor();
		
		Iterator<String> databaseDescriptorPaths = applicationDescriptor.getDatabaseDescriptorPaths();
		while(databaseDescriptorPaths.hasNext()) {
			parseDatabaseDescriptor(databaseDescriptorPaths.next());
		}

		boolean loaded = false;
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		if(resources.getApplicationDescriptor().isLoadInitially()) {
			databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
			while(databaseDescriptors.hasNext()) {
				parseDatabaseMappings(databaseDescriptors.next());
			}
			
			loaded = true;
		}

		databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			processDatabase(databaseDescriptor, loaded);
		}
	}
	
	private static void parseApplicationDescriptor() {
		validateSiminov();
		
		ApplicationDescriptorParser applicationDescriptorParser = null;
		
		try {
			applicationDescriptorParser = new ApplicationDescriptorParser();
		} catch(SiminovException siminovException) {
			isActive = false;
			
			Log.logd(Siminov.class.getName(), "parseApplicationDescriptor", "SiminovException caught while doing application descriptor parser, " + siminovException.getMessage());
			throw new DeploymentException(Siminov.class.getName(), "parseApplicationDescriptor", siminovException.getMessage());
		} catch(DeploymentException deploymentException) {
			isActive = false;
			
			Log.logd(Siminov.class.getName(), "parseApplicationDescriptor", "DeploymentException caught while doing application descriptor parser, " + deploymentException.getMessage());
			throw new DeploymentException(Siminov.class.getName(), "parseApplicationDescriptor", deploymentException.getMessage());
		}
		
		ApplicationDescriptor applicationDescriptor = applicationDescriptorParser.getApplicationDescriptor();
		if(applicationDescriptor == null) {
			isActive = false;
			
			Log.logd(Siminov.class.getName(), "parseApplicationDescriptor", "Invalid Application Descriptor Found.");
			throw new DeploymentException(Siminov.class.getName(), "parseApplicationDescriptor", "Invalid Application Descriptor Found.");
		}
		
		Resources.getInstance().setApplicationDescriptor(applicationDescriptor);
	}
	
	private static void parseDatabaseDescriptor(final String databaseDescriptorPath) {
		validateSiminov();
		
		Resources resources = Resources.getInstance();
		DatabaseDescriptorParser databaseDescriptorParser = null;
		
		try {
			databaseDescriptorParser = new DatabaseDescriptorParser(databaseDescriptorPath);
		} catch(SiminovException siminovException) {
			isActive = false;
			
			Log.loge(Siminov.class.getName(), "parseDatabaseDescriptor", "SiminovException caught while parsing database descriptor, DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + siminovException.getMessage());
			throw new DeploymentException(Siminov.class.getName(), "parseDatabaseDescriptor", siminovException.getMessage());
		}
		
		DatabaseDescriptor databaseDescriptor = databaseDescriptorParser.getDatabaseDescriptor();
		if(databaseDescriptor == null) {
			Log.loge(Siminov.class.getName(), "parseDatabaseDescriptor", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
			throw new DeploymentException(Siminov.class.getName(), "parseDatabaseDescriptor", "Invalid Database Descriptor Path Found, DATABASE-DESCRIPTOR: " + databaseDescriptorPath);
		}

		resources.getApplicationDescriptor().addDatabaseDescriptor(databaseDescriptorPath, databaseDescriptor);
		parseLibraries(databaseDescriptor);
	}
	
	private static void parseLibraries(final DatabaseDescriptor databaseDescriptor) {
		validateSiminov();

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
				isActive = false;
				
				Log.loge(Siminov.class.getName(), "parseLibraries", "SiminovException caught while parsing library descriptor, LIBRARY-NAME: " + libraryName + ", " + ce.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "parseLibraries", ce.getMessage());
			}
			
			databaseDescriptor.addLibrary(libraryName, libraryDescriptorParser.getLibraryDescriptor());
		}
	}

	private static void parseDatabaseMappings(final DatabaseDescriptor databaseDescriptor) {
		validateSiminov();

		Resources resources = Resources.getInstance();
		
		ApplicationDescriptor applicationDescriptor = resources.getApplicationDescriptor();
		if(!applicationDescriptor.isDatabaseNeeded()) {
			return;
		}
		
		Iterator<String> libraries = databaseDescriptor.getLibraryPaths();
		while(libraries.hasNext()) {
			String libraryPath = libraries.next();
			LibraryDescriptor libraryDescriptor = databaseDescriptor.getLibraryDescriptorBasedOnPath(libraryPath);
			
			parseDatabaseMappings(libraryPath, libraryDescriptor);
		}

		Iterator<String> databaseMappingPaths = databaseDescriptor.getDatabaseMappingPaths();
		while(databaseMappingPaths.hasNext()) {
			String databaseMappingPath = databaseMappingPaths.next();
			DatabaseMappingDescriptorParser databaseMappingParser = null;
			
			try {
				databaseMappingParser = new DatabaseMappingDescriptorParser(databaseMappingPath);
			} catch(SiminovException ce) {
				isActive = false;
				
				Log.loge(Siminov.class.getName(), "parseDatabaseMappings", "SiminovException caught while parsing database mapping, DATABASE-MAPPING: " + databaseMappingPath + ", " + ce.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "parseDatabaseMappings", "NAME: " + databaseMappingPath + ", " + ce.getMessage());
			}
			
			databaseDescriptor.addDatabaseMapping(databaseMappingPath, databaseMappingParser.getDatabaseMapping());
		}
	}
	
	private static void parseDatabaseMappings(final String libraryName, final LibraryDescriptor libraryDescriptor) {
		validateSiminov();
		
		Iterator<String> libraryDatabaseMappingPaths = libraryDescriptor.getDatabaseMappingPaths();
		while(libraryDatabaseMappingPaths.hasNext()) {
			String libraryDatabaseMappingPath = libraryDatabaseMappingPaths.next();
			DatabaseMappingDescriptorParser databaseMappingParser = null;
			
			try {
				databaseMappingParser = new DatabaseMappingDescriptorParser(libraryName, libraryDatabaseMappingPath);
			} catch(SiminovException ce) {
				isActive = false;
				
				Log.loge(Siminov.class.getName(), "parseDatabaseMappings", "SiminovException caught while parsing database mapping, LIBRARY-DATABASE-MAPPING: " + libraryDatabaseMappingPath + ", " + ce.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "parseDatabaseMappings", "LIBRARY-DATABASE-MAPPING: " + libraryDatabaseMappingPath + ", " + ce.getMessage());
			}
			
			libraryDescriptor.addDatabaseMapping(libraryDatabaseMappingPath, databaseMappingParser.getDatabaseMapping());
		}
	}
	
	private static void processDatabase(final DatabaseDescriptor databaseDescriptor, final boolean loaded) {
		Resources resources = Resources.getInstance();
		
		String databasePath = new DatabaseUtils().getDatabasePath(databaseDescriptor);
		DatabaseBundle databaseBundle = null;
		
		try {
			databaseBundle = Database.createDatabase(databaseDescriptor);
		} catch(DatabaseException databaseException) {
			isActive = false;
			
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
				resources.addDatabaseBundle(databaseDescriptor.getDatabaseName(), databaseBundle);
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while opening database, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}
			
			try {
				// Enable foreign key constraints
		        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

			try {
		        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isLockingRequired());
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

		} else {
			
			firstTimeProcessed = true;
			
			if(!loaded) {
				parseDatabaseMappings(databaseDescriptor);
			}
			
			file = new File(databasePath);
			try {
				file.mkdirs();
			} catch(Exception exception) {
				isActive = false;
				
				Log.loge(Siminov.class.getName(), "processDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.getDatabaseName() + ", " + exception.getMessage());
			}
			
			try {
				database.openOrCreate(databaseDescriptor);			
				resources.addDatabaseBundle(databaseDescriptor.getDatabaseName(), databaseBundle);
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating database, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}
			
			try {
				// Enable foreign key constraints
		        database.executeQuery(databaseDescriptor, null, Constants.SQLITE_DATABASE_QUERY_TO_ENABLE_FOREIGN_KEYS_MAPPING);
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while executing query to enable foreign keys, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

			try {
		        database.executeMethod(Constants.SQLITE_DATABASE_ENABLE_LOCKING, databaseDescriptor.isLockingRequired());
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while enabling locking on database, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}

			Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.orderedLibraryDescriptors();
			while(libraryDescriptors.hasNext()) {
				try {
					Database.createTables(libraryDescriptors.next().orderedDatabaseMappings());			
				} catch(DatabaseException databaseException) {
					isActive = false;
					
					new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
					
					Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating tables for library, " + databaseException.getMessage());
					throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
				}
			}

			try {
				Database.createTables(databaseDescriptor.orderedDatabaseMappings());			
			} catch(DatabaseException databaseException) {
				isActive = false;
				
				new File(databasePath + databaseDescriptor.getDatabaseName()).delete();
				
				Log.loge(Siminov.class.getName(), "processDatabase", "DatabaseException caught while creating tables, " + databaseException.getMessage());
				throw new DeploymentException(Siminov.class.getName(), "processDatabase", databaseException.getMessage());
			}
			
			IDatabaseEvents databaseEventHandler = resources.getDatabaseEventHandler();
			if(databaseEventHandler != null) {
				databaseEventHandler.databaseCreated(databaseDescriptor);
			}
		}
	}
}
