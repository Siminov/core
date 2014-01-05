/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution LLP|support@siminov.com]
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

package siminov.orm.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.orm.Constants;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.PrematureEndOfParseException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.model.DatabaseDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.resource.Resources;
import android.content.Context;


/**
 * Exposes methods to quickly parse database mapping descriptor defined by application.
 */
public class QuickDatabaseMappingDescriptorReader extends SiminovSAXDefaultHandler implements Constants {

	private StringBuilder tempValue = new StringBuilder();
	private String finalDatabaseMappingBasedOnClassName = null;
	
	private Context context = null;
	
	private DatabaseMappingDescriptor databaseMappingDescriptor = null;
	
	private boolean doesMatch = false;
	
	private Resources resources = Resources.getInstance();
	
	public QuickDatabaseMappingDescriptorReader(final String findDatabaseMappingBasedOnClassName) throws SiminovException {
		
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
			Iterator<String> databaseMappingDescriptors = databaseDescriptor.getDatabaseMappingDescriptorPaths();
					
			while(databaseMappingDescriptors.hasNext()) {
				String databaseMappingDescriptorPath = databaseMappingDescriptors.next();
				
				InputStream databaseMappingDescriptorStream = null;
				
				try {
					
					databaseMappingDescriptorStream = getClass().getClassLoader().getResourceAsStream(databaseMappingDescriptorPath);
					if(databaseMappingDescriptorStream == null) {
						databaseMappingDescriptorStream = context.getAssets().open(databaseMappingDescriptorPath);
					}
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

					DatabaseMappingDescriptorReader databaseMappingParser = new DatabaseMappingDescriptorReader(databaseMappingDescriptorPath);
					
					this.databaseMappingDescriptor = databaseMappingParser.getDatabaseMappingDescriptor();
					databaseDescriptor.addDatabaseMappingDescriptor(databaseMappingDescriptorPath, databaseMappingDescriptor);
					
					return;
				}
			}
			
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = new StringBuilder();

		if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_TABLE)) {
			String className = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_CLASS_NAME);

			if(className.equalsIgnoreCase(finalDatabaseMappingBasedOnClassName)) {
				doesMatch = true;
			} else if(className.substring(className.lastIndexOf(".") + 1, className.length()).equalsIgnoreCase(finalDatabaseMappingBasedOnClassName)) {
				doesMatch = true;
			}
			
			throw new PrematureEndOfParseException(getClass().getName(), "startElement", "Class Name: " + className);
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

	}

	/**
	 * Get database mapping object.
	 * @return Database Mapping Object.
	 */
	public DatabaseMappingDescriptor getDatabaseMapping() {
		return this.databaseMappingDescriptor;
	}
}
