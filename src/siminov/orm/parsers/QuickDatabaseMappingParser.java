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

package siminov.orm.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.orm.Constants;
import siminov.orm.exception.SiminovException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.PrematureEndOfParseException;
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.LibraryDescriptor;
import siminov.orm.resource.Resources;

import android.content.Context;


/**
 * Exposes methods to quickly parse database mapping descriptor defined by application.
 */
public class QuickDatabaseMappingParser extends SiminovSAXDefaultHandler implements Constants {

	private String tempValue = null;
	private String finalDatabaseMappingBasedOnClassName = null;
	
	private Context context = null;
	
	private DatabaseMappingDescriptor databaseMappingDescriptor = null;
	
	private boolean doesMatch = false;
	
	private Resources resources = Resources.getInstance();
	
	public QuickDatabaseMappingParser(final String findDatabaseMappingBasedOnClassName) throws SiminovException {
		
		if(findDatabaseMappingBasedOnClassName == null || findDatabaseMappingBasedOnClassName.length() <= 0) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Database Mapping Class Name Which Needs To Be Searched.");
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Database Mapping Class Name Which Needs To Be Searched.");
		}
		
		this.finalDatabaseMappingBasedOnClassName = findDatabaseMappingBasedOnClassName;
	}
	
	public void process() throws SiminovException {
		context = resources.getApplicationContext();
		if(context == null) {
			Log.loge(getClass().getName(), "process", "Invalid Application Context found.");
			throw new SiminovException(getClass().getName(), "process", "Invalid Application Context found.");
		}

		databaseMappingDescriptor = new AnnotationParser().parseClass(this.finalDatabaseMappingBasedOnClassName);
		if(databaseMappingDescriptor != null) {
			
			Iterator<DatabaseDescriptor> databaseDescriptors = Resources.getInstance().getDatabaseDescriptors();
			while(databaseDescriptors.hasNext()) {
				DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
				Iterator<String> databaseMappingPaths = databaseDescriptor.getDatabaseMappingPaths();
				
				while(databaseMappingPaths.hasNext()) {
					String databaseMappingDescriptorPath = databaseMappingPaths.next();
					if(this.finalDatabaseMappingBasedOnClassName.equalsIgnoreCase(databaseMappingDescriptorPath)) {
						databaseDescriptor.addDatabaseMapping(this.finalDatabaseMappingBasedOnClassName, databaseMappingDescriptor);
						Resources.getInstance().synchronizeDatabases();
						
						return;
					}
				}
				
		        Iterator<LibraryDescriptor> libraryDescriptors = databaseDescriptor.getLibraryDescriptors();
	        	while(libraryDescriptors.hasNext()) {
	        		LibraryDescriptor libraryDescriptor = libraryDescriptors.next();
	        		Iterator<String> libraryDatabaseMappingPaths = libraryDescriptor.getDatabaseMappingPaths();
	        		
	        		while(libraryDatabaseMappingPaths.hasNext()) {
	        			String libraryDatabaseMappingPath = libraryDatabaseMappingPaths.next();
	        			if(libraryDatabaseMappingPath.equalsIgnoreCase(this.finalDatabaseMappingBasedOnClassName)) {
	        				libraryDescriptor.addDatabaseMapping(libraryDatabaseMappingPath, databaseMappingDescriptor);
	    					Resources.getInstance().synchronizeDatabases();

	    					return;
	        			}
	        		}
	        	}
			}
			
			return;
		} 
		
		ApplicationDescriptor applicationDescriptor = resources.getApplicationDescriptor();
		if(applicationDescriptor == null) {
			Log.loge(getClass().getName(), "process", "Invalid Application Descriptor Found");
			throw new DeploymentException(getClass().getName(), "process", "Invalid Application Descriptor Found.");
		}
		
		if(!applicationDescriptor.isDatabaseNeeded()) {
			doesMatch = false;
			return;
		}
		
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			Iterator<String> databaseMappingDescriptors = databaseDescriptor.getDatabaseMappingPaths();
					
			while(databaseMappingDescriptors.hasNext()) {
				String databaseMappingDescriptorPath = databaseMappingDescriptors.next();
				
				InputStream databaseMappingDescriptorStream = null;
				
				try {
					databaseMappingDescriptorStream = context.getAssets().open(databaseMappingDescriptorPath);
				} catch(IOException ioException) {
					Log.loge(getClass().getName(), "process", "IOException caught while getting input stream of DATABASE-MAPPING: " + databaseMappingDescriptorPath + ", " + ioException.getMessage());
					throw new SiminovException(getClass().getName(), "process", "IOException caught while getting input stream of application descriptor: " + databaseMappingDescriptorPath + ", " + ioException.getMessage());
				}
				
				try {
					parseMessage(databaseMappingDescriptorStream);
				} catch(Exception exception) {
					Log.loge(getClass().getName(), "process", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseMappingDescriptorPath + ", " + exception.getMessage());
					throw new SiminovException(getClass().getName(), "process", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseMappingDescriptorPath + ", " + exception.getMessage());
				}
				
				if(doesMatch) {

					DatabaseMappingDescriptorParser databaseMappingParser = null;
					
					try {
						databaseMappingParser = new DatabaseMappingDescriptorParser(databaseMappingDescriptorPath);
					} catch(SiminovException siminovException) {
						Log.loge(getClass().getName(), "process", "SiminovException caught while parsing database mapping, NAME: " + databaseMappingDescriptorPath + ", " + siminovException.getMessage());
						throw new SiminovException(getClass().getName(), "process", "NAME: " + databaseMappingDescriptorPath + ", " + siminovException.getMessage());
					}
					
					this.databaseMappingDescriptor = databaseMappingParser.getDatabaseMapping();
					databaseDescriptor.addDatabaseMapping(databaseMappingDescriptorPath, databaseMappingDescriptor);
					Resources.getInstance().synchronizeDatabases();
					
					return;
				}
			}
			

			Iterator<String> libraryDescriptorPaths = databaseDescriptor.getLibraryPaths();
			while(libraryDescriptorPaths.hasNext()) {
				String libraryDescriptorPath = libraryDescriptorPaths.next();
				
				LibraryDescriptor libraryDescriptor = databaseDescriptor.getLibraryDescriptorBasedOnPath(libraryDescriptorPath);
				Iterator<String> libraryDatabaseMappings = libraryDescriptor.getDatabaseMappingPaths();
				
				while(libraryDatabaseMappings.hasNext()) {
					String libraryDatabaseMapping = libraryDatabaseMappings.next();
					InputStream databaseMappingStream = null;

					databaseMappingStream = getClass().getClassLoader().getResourceAsStream(libraryDescriptorPath.replace(".", "/") + "/" + libraryDatabaseMapping);
					
					try {
						parseMessage(databaseMappingStream);
					} catch(Exception exception) {
						Log.loge(getClass().getName(), "process", "Exception caught while parsing DATABASE-DESCRIPTOR: " + libraryDatabaseMapping + ", " + exception.getMessage());
						throw new SiminovException(getClass().getName(), "process", "Exception caught while parsing DATABASE-DESCRIPTOR: " + libraryDatabaseMapping + ", " + exception.getMessage());
					}
					
					if(doesMatch) {

						DatabaseMappingDescriptorParser databaseMappingParser = null;
						
						try {
							databaseMappingParser = new DatabaseMappingDescriptorParser(libraryDescriptorPath, libraryDatabaseMapping);
						} catch(SiminovException siminovException) {
							Log.loge(getClass().getName(), "process", "SiminovException caught while parsing database mapping, NAME: " + libraryDatabaseMapping + ", " + siminovException.getMessage());
							throw new SiminovException(getClass().getName(), "process", "NAME: " + libraryDatabaseMapping + ", " + siminovException.getMessage());
						}
						
						databaseMappingDescriptor = databaseMappingParser.getDatabaseMapping();
						libraryDescriptor.addDatabaseMapping(libraryDatabaseMapping, databaseMappingDescriptor);
						Resources.getInstance().synchronizeDatabases();
						
						return;
					}
				}
			}
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = "";

		if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_TABLE)) {
			String className = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_CLASS_NAME);
			if(className.equalsIgnoreCase(finalDatabaseMappingBasedOnClassName)) {
				doesMatch = true;
			}
			
			throw new PrematureEndOfParseException(getClass().getName(), "startElement", "Class Name: " + className);
		} 
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempValue = new String(ch,start,length);
		
		if(tempValue == null || tempValue.length() <= 0) {
			return;
		}
		
		tempValue.trim();
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	/**
	 * Get database mapping object.
	 * @return Database Mapping Object.
	 */
	public DatabaseMappingDescriptor getDatabaseMapping() {
		return this.databaseMappingDescriptor;
	}
}
