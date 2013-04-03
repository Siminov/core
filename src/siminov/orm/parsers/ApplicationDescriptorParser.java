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
import siminov.orm.log.Log;
import siminov.orm.model.ApplicationDescriptor;
import siminov.orm.resource.Resources;

import android.content.Context;



/**
 * Exposes methods to parse Application Descriptor information as per define in ApplicationDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	<core>
	
		<property name="name">SIMINOV TEMPLATE</property>	
		<property name="description">Siminov Template Application</property>
		<property name="version">0.9</property>
	
		<property name="load_initially">true</property>
	
		<!-- DATABASE-DESCRIPTORS -->
		<database-descriptors>
			<database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
		</database-descriptors>
	
		
		<!-- SIMINOV EVENTS -->
		<event-handlers>
		    <event-handler>com.core.template.events.SiminovEventHandler</event-handler>
		    <event-handler>com.core.template.events.DatabaseEventHandler</event-handler>
		</event-handlers>
			
	</core>
	}
	
		</pre>
	</p>
 *
 */
public class ApplicationDescriptorParser extends SiminovSAXDefaultHandler implements Constants {

	private ApplicationDescriptor applicationDescriptor = null;
	
	private Resources resources = Resources.getInstance();

	private String tempValue = null;
	private String propertyName = null;
	
	public ApplicationDescriptorParser() throws SiminovException, DeploymentException {
		
		Context context = resources.getApplicationContext();
		if(context == null) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Application Context found.");
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Application Context found.");
		}

		/*
		 * Parse ApplicationDescriptor.
		 */
		InputStream applicationDescriptorStream = null;
		
		try {
			applicationDescriptorStream = context.getAssets().open(APPLICATION_DESCRIPTOR_FILE_NAME);
		} catch(IOException ioException) {
			Log.loge(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
			throw new SiminovException(getClass().getName(), "Constructor", "IOException caught while getting input stream of application descriptor, " + ioException.getMessage());
		}
		
		try {
			parseMessage(applicationDescriptorStream);
		} catch(Exception exception) {
			Log.loge(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
			throw new SiminovException(getClass().getName(), "Constructor", "Exception caught while parsing APPLICATION-DESCRIPTOR, " + exception.getMessage());
		}
		
		doValidation();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		tempValue = "";
		
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_SIMINOV)) {
			applicationDescriptor = new ApplicationDescriptor();
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			initializeProperty(attributes);
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
		if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_PROPERTY)) {
			applicationDescriptor.addProperty(propertyName, tempValue);
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_DATABASE_DESCRIPTOR)) {
			applicationDescriptor.addDatabaseDescriptorPath(tempValue);
		} else if(localName.equalsIgnoreCase(APPLICATION_DESCRIPTOR_EVENT_HANDLER)) {
			
			if(tempValue == null || tempValue.length() <= 0) {
				return;
			}
			
			applicationDescriptor.addEvent(tempValue);
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
			Log.loge(getClass().getName(), "doValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
			throw new DeploymentException(getClass().getName(), "doValidation", "NAME IS MANDATORY FIELD - APPLICATION-DESCRIPTOR");
		}
		
		Iterator<String> databaseDescriptorPaths = applicationDescriptor.getDatabaseDescriptorPaths();
		while(databaseDescriptorPaths.hasNext()) {
			String databaseDescriptorPath = databaseDescriptorPaths.next();
			
			if(!databaseDescriptorPath.contains(SIMINOV_DESCRIPTOR_EXTENSION)) {
				Log.loge(getClass().getName(), "doValidation", "INVALID DATABASE DESCRIPTOR PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + databaseDescriptorPath);
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
