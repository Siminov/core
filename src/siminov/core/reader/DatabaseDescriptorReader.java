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
import siminov.core.model.DatabaseDescriptor;
import siminov.core.resource.ResourceManager;
import android.content.Context;


/**
 * Exposes methods to parse Database Descriptor information as per define in DatabaseDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	<database-descriptor>
	
	    <!-- General Database Descriptor Properties -->
	    
		    <!-- Mandatory Field -->
		<property name="database_name">name_of_database_file</property>
	
			<!-- Optional Field (Default is sqlite)-->
		<property name="type">type_of_database</property>
	
			<!-- Mandatory Field -->
		<property name="version">database_version</property>
				
			<!-- Optional Field -->
		<property name="description">database_description</property>
	
			<!-- Optional Field (Default is false) -->
		<property name="transaction_safe">true/false</property>
		
			<!-- Optional Field (Default is false) -->
		<property name="external_storage">true/false</property>
			
	
	
		<!-- Entity Descriptor Paths Needed Under This Database Descriptor -->
		
			<!-- Optional Field -->
		<entity-descriptors>
			<entity-descriptor>full_path_of_entity_descriptor_file</entity-descriptor>
		</entity-descriptors>
		
	</database-descriptor>


	}
	
		</pre>
	</p>
*/
public class DatabaseDescriptorReader extends SiminovSAXDefaultHandler implements Constants {

	private String databaseDescriptorPath = null;
	
	private DatabaseDescriptor databaseDescriptor = null;
	
	private ResourceManager resourceManager = ResourceManager.getInstance();

	private StringBuilder tempValue = new StringBuilder();
	private String propertyName = null;

	
	/**
	 * DatabaseDescriptorReader Constructor
	 * @param databaseDescriptorPath Path of the database descriptor
	 */
	public DatabaseDescriptorReader(final String databaseDescriptorPath) {
		
		if(databaseDescriptorPath == null || databaseDescriptorPath.length() <= 0) {
			Log.error(getClass().getName(), "Constructor", "Invalid Database Descriptor path found.");
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid Database Descriptor path found.");
		}
		
		this.databaseDescriptorPath = databaseDescriptorPath;
		
		Context context = resourceManager.getApplicationContext();
		if(context == null) {
			Log.error(getClass().getName(), "Constructor", "Invalid Application Context found.");
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid Application Context found.");
		}

		/*
		 * Parse ApplicationDescriptor.
		 */
		InputStream databaseDescriptorStream = null;

		try {

			databaseDescriptorStream = getClass().getClassLoader().getResourceAsStream(this.databaseDescriptorPath);
			if(databaseDescriptorStream == null) {
				databaseDescriptorStream = context.getAssets().open(this.databaseDescriptorPath);
			}
		} catch(IOException ioException) {
			Log.error(getClass().getName(), "Constructor", "IOException caught while getting input stream of database descriptor, DATABASE-DESCRIPTOR-PATH: " + databaseDescriptorPath + ", " + ioException.getMessage());

			try {
			} catch(Exception exception) {
				Log.error(DatabaseDescriptorReader.class.getName(), "Constructor", "Exception caught while getting database descriptor file stream, " + exception.getMessage());
				throw new DeploymentException(DatabaseDescriptorReader.class.getName(), "Constructor", exception.getMessage());
			}
		}

		
		try {
			parseMessage(databaseDescriptorStream);
		} catch(Exception exception) {
			Log.error(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + exception.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-DESCRIPTOR: " + databaseDescriptorPath + ", " + exception.getMessage());
		}
		
		doValidation();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = new StringBuilder();
		
		if(localName.equalsIgnoreCase(DATABASE_DESCRIPTOR)) {
			databaseDescriptor = new DatabaseDescriptor();
		} else if(localName.equalsIgnoreCase(DATABASE_DESCRIPTOR_PROPERTY)) {
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
		
		if(localName.equalsIgnoreCase(DATABASE_DESCRIPTOR_PROPERTY)) {
			databaseDescriptor.addProperty(propertyName, tempValue.toString());
		} else if(localName.equalsIgnoreCase(DATABASE_DESCRIPTOR_ENTITY_DESCRIPTOR)) {
			databaseDescriptor.addEntityDescriptorPath(tempValue.toString());
		} 
	}
	
	private void initializeProperty(final Attributes attributes) {
		propertyName = attributes.getValue(DATABASE_DESCRIPTOR_PROPERTY_NAME);
	}
	
	private void doValidation() throws DeploymentException {
		
		/*
		 * Validate Database Name field.
		 */
		Iterator<String> entityDescriptorPaths = databaseDescriptor.getEntityDescriptorPaths();
		while(entityDescriptorPaths.hasNext()) {
			String entityDescriptorPath = entityDescriptorPaths.next();
			
			if(entityDescriptorPath.endsWith(XML_FILE_EXTENSION)) {
				if(!entityDescriptorPath.contains(SIMINOV_DESCRIPTOR_EXTENSION)) {
					Log.error(getClass().getName(), "doValidation", "INVALID ENTITY DESCRIPTOR PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + entityDescriptorPath);
					throw new DeploymentException(getClass().getName(), "doValidation", "INVALID ENTITY DESCRIPTOR PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + entityDescriptorPath);
				}
			}
		}

	}

	/**
	 * Get database descriptor object.
	 * @return Database Descriptor Object.
	 */
	public DatabaseDescriptor getDatabaseDescriptor() {
		if(databaseDescriptor.getType() == null || databaseDescriptor.getType().length() <= 0) {
			databaseDescriptor.setType(SQLITE_DATABASE);
		}
		
		return this.databaseDescriptor;
	}

}
