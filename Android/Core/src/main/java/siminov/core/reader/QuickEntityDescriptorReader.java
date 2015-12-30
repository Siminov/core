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


package siminov.core.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.core.Constants;
import siminov.core.exception.DeploymentException;
import siminov.core.exception.PrematureEndOfParseException;
import siminov.core.exception.SiminovException;
import siminov.core.log.Log;
import siminov.core.model.ApplicationDescriptor;
import siminov.core.model.DatabaseDescriptor;
import siminov.core.model.EntityDescriptor;
import siminov.core.resource.ResourceManager;
import android.content.Context;


/**
 * Exposes methods to quickly parse entity descriptor defined by application.
 */
public class QuickEntityDescriptorReader extends SiminovSAXDefaultHandler implements Constants {

	private StringBuilder tempValue = new StringBuilder();
	private String finalEntityDescriptorBasedOnClassName = null;
	
	private Context context = null;
	
	private EntityDescriptor entityDescriptor = null;
	
	private String propertyName;
	private boolean doesMatch = false;
	
	private ResourceManager resourceManager = ResourceManager.getInstance();
	
	/**
	 * QucikEntityDescriptorReader Constructor
	 * @param findEntityDescriptorBasedOnClassName Name of the entity descriptor class name
	 * @throws SiminovException
	 */
	public QuickEntityDescriptorReader(final String findEntityDescriptorBasedOnClassName) throws SiminovException {
		
		if(findEntityDescriptorBasedOnClassName == null || findEntityDescriptorBasedOnClassName.length() <= 0) {
			Log.error(getClass().getName(), "Constructor", "Invalid Entity Descriptor Class Name Which Needs To Be Searched.");
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Entity Descriptor Class Name Which Needs To Be Searched.");
		}
		
		this.finalEntityDescriptorBasedOnClassName = findEntityDescriptorBasedOnClassName;
	}
	
	/**
	 * Parse the entity descriptor defined
	 * @throws SiminovException Any exception during parsing the descriptor file
	 */
	public void process() throws SiminovException {
		context = resourceManager.getApplicationContext();
		if(context == null) {
			Log.error(getClass().getName(), "process", "Invalid Application Context found.");
			throw new SiminovException(getClass().getName(), "process", "Invalid Application Context found.");
		}

			
		ApplicationDescriptor applicationDescriptor = resourceManager.getApplicationDescriptor();
		if(applicationDescriptor == null) {
			Log.error(getClass().getName(), "process", "Invalid Application Descriptor Found");
			throw new DeploymentException(getClass().getName(), "process", "Invalid Application Descriptor Found.");
		}
		
		if(!applicationDescriptor.isDatabaseNeeded()) {
			doesMatch = false;
			return;
		}

		
		Iterator<DatabaseDescriptor> databaseDescriptors = applicationDescriptor.getDatabaseDescriptors();
		while(databaseDescriptors.hasNext()) {
			DatabaseDescriptor databaseDescriptor = databaseDescriptors.next();
			Iterator<String> entityDescriptors = databaseDescriptor.getEntityDescriptorPaths();
					
			while(entityDescriptors.hasNext()) {
				String entityDescriptorPath = entityDescriptors.next();
				
				InputStream entityDescriptorStream = null;
				
				try {
					
					entityDescriptorStream = getClass().getClassLoader().getResourceAsStream(entityDescriptorPath);
					if(entityDescriptorStream == null) {
						entityDescriptorStream = context.getAssets().open(entityDescriptorPath);
					}
				} catch(IOException ioException) {
					Log.error(getClass().getName(), "process", "IOException caught while getting input stream of ENTITY-DESCRIPTOR: " + entityDescriptorPath + ", " + ioException.getMessage());
					throw new SiminovException(getClass().getName(), "process", "IOException caught while getting input stream of application descriptor: " + entityDescriptorPath + ", " + ioException.getMessage());
				}
				
				try {
					parseMessage(entityDescriptorStream);
				} catch(Exception exception) {
					Log.error(getClass().getName(), "process", "Exception caught while parsing ENTITY-DESCRIPTOR: " + entityDescriptorPath + ", " + exception.getMessage());
					throw new SiminovException(getClass().getName(), "process", "Exception caught while parsing ENTITY-DESCRIPTOR: " + entityDescriptorPath + ", " + exception.getMessage());
				}
				
				if(doesMatch) {

					EntityDescriptorReader entityDescriptorParser = new EntityDescriptorReader(entityDescriptorPath);
					
					this.entityDescriptor = entityDescriptorParser.getEntityDescriptor();
					databaseDescriptor.addEntityDescriptor(entityDescriptorPath, entityDescriptor);
					
					return;
				}
			}
			
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = new StringBuilder();
		
		if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_PROPERTY)) {
			propertyName = attributes.getValue(ENTITY_DESCRIPTOR_NAME);
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

		if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_PROPERTY)) {

			if(propertyName.equalsIgnoreCase(ENTITY_DESCRIPTOR_CLASS_NAME)) {
				
				if(tempValue.toString().equalsIgnoreCase(finalEntityDescriptorBasedOnClassName)) {
					doesMatch = true;
				}
				
				throw new PrematureEndOfParseException(getClass().getName(), "startElement", "Class Name: " + tempValue.toString());
			}
		} 
	}

	/**
	 * Get entity descriptor object.
	 * @return Entity Descriptor Object.
	 */
	public EntityDescriptor getEntityDescriptor() {
		return this.entityDescriptor;
	}
}
