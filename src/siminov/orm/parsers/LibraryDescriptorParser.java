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

import java.io.InputStream;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import siminov.orm.Constants;
import siminov.orm.exception.SiminovException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.log.Log;
import siminov.orm.model.LibraryDescriptor;
import siminov.orm.resource.Resources;

import android.content.Context;


/**
 * Exposes methods to parse Library Descriptor information as per define in LibraryDescriptor.si.xml file by application.
	<p>
		<pre>
		
Example:
	{@code
	<library>
	
		<property name="name">SIMINOV LIBRARY TEMPLATE</property>
		<property name="description">Siminov Library Template</property>
	
		<!-- Database Mappings -->
			<database-mappings>
				<database-mapping path="Credential.core.xml" />
			</database-mappings>
	
			 	<!-- OR -->
			 
			<database-mappings>
				<database-mapping path="com.core.library.template.model.Credential" />
			</database-mappings>
		 
	</library>
	}
	
		</pre>
	</p>
 *
 */
public class LibraryDescriptorParser extends SiminovSAXDefaultHandler implements Constants {

	private String libraryName = null;

	private LibraryDescriptor libraryDescriptor = null;
	
	private Resources resources = Resources.getInstance();

	private String tempValue = null;
	private String propertyName = null;
	
	
	public LibraryDescriptorParser(final String libraryName) throws SiminovException {

		if(libraryName == null || libraryName.length() <= 0) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Library Name Found.");
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Library Name Found.");
		}
		
		this.libraryName = libraryName;
		
		Context context = resources.getApplicationContext();
		if(context == null) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Application Context Found.");
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Application Context Found.");
		}

		InputStream libraryDescriptorStream = null;
		libraryDescriptorStream = getClass().getClassLoader().getResourceAsStream(libraryName.replace(".", "/") + "/" + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);

		if(libraryDescriptorStream == null) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Library Descriptor Stream Found, LIBRARY-NAME: " + this.libraryName + ", PATH: " + libraryName.replace(".", "/") + "/" + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Library Descriptor Stream Found, LIBRARY-NAME: " + this.libraryName + ", PATH: " + libraryName.replace(".", "/") + "/" + Constants.LIBRARY_DESCRIPTOR_FILE_NAME);
		}
		
		try {
			parseMessage(libraryDescriptorStream);
		} catch(Exception exception) {
			Log.loge(getClass().getName(), "Constructor", "Exception caught while parsing LIBRARY-DESCRIPTOR: " + this.libraryName + ", " + exception.getMessage());
			throw new SiminovException(getClass().getName(), "Constructor", "Exception caught while parsing LIBRARY-DESCRIPTOR: " + this.libraryName + ", " + exception.getMessage());
		}
		
		doValidation();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = "";

		if(localName.equalsIgnoreCase(LIBRARY_DESCRIPTOR_LIBRARY)) {
			libraryDescriptor = new LibraryDescriptor();
		} else if(localName.equalsIgnoreCase(LIBRARY_DESCRIPTOR_PROPERTY)) {
			initializeProperty(attributes);
		} else if(localName.equalsIgnoreCase(LIBRARY_DESCRIPTOR_DATABASE_MAPPING)) {
			initializeMapping(attributes);
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
		
		if(localName.equalsIgnoreCase(LIBRARY_DESCRIPTOR_PROPERTY)) {
			libraryDescriptor.addProperty(propertyName, tempValue);
		} 
	}
	
	private void initializeProperty(final Attributes attributes) {
		propertyName = attributes.getValue(LIBRARY_DESCRIPTOR_NAME);
	}
	
	private void initializeMapping(final Attributes attributes) {
		libraryDescriptor.addDatabaseMappingPath(attributes.getValue(LIBRARY_DESCRIPTOR_PATH));
	}
	
	private void doValidation() throws DeploymentException {
		
		/*
		 * Validation for name field.
		 */
		String name = libraryDescriptor.getName();
		if(name == null || name.length() <= 0) {
			Log.loge(getClass().getName(), "doValidation", "LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: " + this.libraryName);
			throw new DeploymentException(getClass().getName(), "doValidation", "LIBRARY-NAME IS MANDATORY FIELD - LIBRARY-DESCRIPTOR: " + this.libraryName);
		}
		
		Iterator<String> databaseMappingPaths = libraryDescriptor.getDatabaseMappingPaths();
		while(databaseMappingPaths.hasNext()) {
			String databaseMappingPath = databaseMappingPaths.next();
			
			if(databaseMappingPath.endsWith(XML_FILE_EXTENSION)) {
				if(!databaseMappingPath.contains(SIMINOV_DESCRIPTOR_EXTENSION)) {
					Log.loge(getClass().getName(), "doValidation", "INVALID LIBRARY DATABASE MAPPING PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + databaseMappingPath);
					throw new DeploymentException(getClass().getName(), "doValidation", "INVALID LIBRARY DATABASE MAPPING PATH FOUND, it should contain .core extension in path, PATH-DEFINED: " + databaseMappingPath);
				}
			}
		}

	}

	/**
	 * Get library descriptor object.
	 * @return Library Descriptor Object.
	 */
	public LibraryDescriptor getLibraryDescriptor() {
		return this.libraryDescriptor;
	}

}
