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

package siminov.core.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.core.Constants;
import siminov.core.exception.DeploymentException;
import siminov.core.log.Log;
import siminov.core.model.ApplicationDescriptor;
import siminov.core.resource.ResourceManager;
import android.content.Context;



/**
 * Exposes methods to parse Application Descriptor information as per define in ApplicationDescriptor.si.xml file by application.
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
public class ApplicationDescriptorReader extends SiminovSAXDefaultHandler implements Constants {

	private ApplicationDescriptor applicationDescriptor = null;
	
	private ResourceManager resourceManager = ResourceManager.getInstance();

	private StringBuilder tempValue = new StringBuilder();
	private String propertyName = null;
	
	/**
	 * ApplicationDescriptorReader Constructor
	 */
	public ApplicationDescriptorReader() {
		
		Context context = resourceManager.getApplicationContext();
		if(context == null) {
			Log.error(getClass().getName(), "Constructor", "Invalid Application Context found.");
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid Application Context found.");
		}

		/*
		 * Parse ApplicationDescriptor.
		 */
		InputStream applicationDescriptorStream = null;
		
		try {
			applicationDescriptorStream = context.getAssets().open(APPLICATION_DESCRIPTOR_FILE_NAME);
		} catch(IOException ioException) {
			Log.error(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
		}
		
		try {
			parseMessage(applicationDescriptorStream);
		} catch(Exception exception) {
			Log.error(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
		}
		
		doValidation();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		tempValue = new StringBuilder();
		
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_SIMINOV)) {
			applicationDescriptor = new ApplicationDescriptor();
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			initializeProperty(attributes);
		} 
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String value = new String(ch,start,length);
		
		if(value == null || value.length() <= 0 || value.equalsIgnoreCase(NEW_LINE)) {
			return;
		}
		
		value = value.trim();
		tempValue.append(value);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			applicationDescriptor.addProperty(propertyName, tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR)) {
			applicationDescriptor.addDatabaseDescriptorPath(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_EVENT_HANDLER)) {
			
			if(tempValue == null || tempValue.length() <= 0) {
				return;
			}
			
			applicationDescriptor.addEvent(tempValue.toString());
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_LIBRARY_DESCRIPTOR)) {
			
			if(tempValue == null || tempValue.length() <= 0) {
				return;
			}
			
			applicationDescriptor.addLibraryDescriptorPath(tempValue.toString());
		}
	}
	
	private void initializeProperty(final Attributes attributes) {
		propertyName = attributes.getValue(APPLICATION_DESCRIPTOR_NAME);
	}
	
	private void doValidation() throws DeploymentException {
		
		/*
		 * Validate Application Name field.
		 */
		String name = applicationDescriptor.getName();
		if(name == null || name.length() <= 0) {
			Log.error(getClass().getName(), "doValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
			throw new DeploymentException(getClass().getName(), "doValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
		}
		
		Iterator<String> databaseDescriptorPaths = applicationDescriptor.getDatabaseDescriptorPaths();
		while(databaseDescriptorPaths.hasNext()) {
			String databaseDescriptorPath = databaseDescriptorPaths.next();
			
			if(!databaseDescriptorPath.contains(SIMINOV_DESCRIPTOR_EXTENSION)) {
				Log.error(getClass().getName(), "doValidation", "INVALID DATABASE DESCRIPTOR PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + databaseDescriptorPath);
				throw new DeploymentException(getClass().getName(), "doValidation", "INVALID DATABASE DESCRIPTOR PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + databaseDescriptorPath);
			}
		}
	}

	/**
	 * Get application descriptor object. 
	 * @return Application Descriptor Object.
	 */
	public ApplicationDescriptor getApplicationDescriptor() {
		return this.applicationDescriptor;
	}

}
