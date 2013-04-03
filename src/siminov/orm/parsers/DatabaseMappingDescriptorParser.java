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
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor.Column;
import siminov.orm.resource.Resources;
import android.content.Context;


/**
 * Exposes methods to parse Library Descriptor information as per define in DatabaseDescriptor.si.xml or LibraryDescriptor.si.xml  file by application.
	<p>
		<pre>
		
Example:
	{@code

	<database-mapping>
	
		<table table_name="LIQUOR" class_name="com.core.template.model.Liquor">
			
			<column variable_name="liquorType" column_name="LIQUOR_TYPE">
				<property name="type">TEXT</property>
				<property name="primary_key">true</property>
				<property name="not_null">true</property>
				<property name="unique">true</property>
			</column>		
	
			<column variable_name="description" column_name="DESCRIPTION">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="history" column_name="HISTORY">
				<property name="type">TEXT</property>
			</column>
	
			<column variable_name="link" column_name="LINK">
				<property name="type">TEXT</property>
				<property name="default">www.wikipedia.org</property>
			</column>
	
			<column variable_name="alcholContent" column_name="ALCHOL_CONTENT">
				<property name="type">TEXT</property>
			</column>
	
			<index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
				<column>HISTORY</column>
			</index>
										
		</table>
	
	</database-mapping>		
		
		}
	
		</pre>
	</p>
 *
 */
public class DatabaseMappingDescriptorParser extends SiminovSAXDefaultHandler implements Constants {

	private String tempValue = null;
	private String propertyName = null;
	
	private String databaseMappingName = null;

	private Resources resources = Resources.getInstance();

	private DatabaseMappingDescriptor databaseMapping = null;

	private DatabaseMappingDescriptor.Column currentColumn = null;
	private DatabaseMappingDescriptor.Index currentIndex = null;
	private DatabaseMappingDescriptor.Relationship currectRelationship = null;

	private boolean isColumn = false;
	private boolean isIndex = false;
	private boolean isRelationship = false;
	
	
	public DatabaseMappingDescriptorParser(final String databaseMappingName) throws SiminovException {
		this.databaseMappingName = databaseMappingName;
		
		if(databaseMappingName == null || databaseMappingName.length() <= 0) {
			Log.loge(getClass().getName(), "Constructor", "Invalid name found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid name found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
		}
		
		Context context = resources.getApplicationContext();
		if(context == null) {
			Log.loge(getClass().getName(), "Constructor", "Invalid Application Context found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid Application Context found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
		}

		/*
		 * Parse DatabaseMapping.
		 */
		if(!databaseMappingName.endsWith(Constants.XML_FILE_EXTENSION)) {
			this.databaseMapping = new AnnotationParser().parseClass(databaseMappingName);
		} else {
			InputStream databaseMappingStream = null;
			
			try {
				databaseMappingStream = context.getAssets().open(this.databaseMappingName);
			} catch(IOException ioException) {
				Log.loge(getClass().getName(), "Constructor", "IOException caught while getting input stream of database mapping descriptor,  DATABASE-MAPPING-MODEL: " + this.databaseMappingName + ", " + ioException.getMessage());
				throw new SiminovException(getClass().getName(), "Constructor", "IOException caught while getting input stream of database mapping descriptor,  DATABASE-MAPPING-MODEL: " + this.databaseMappingName + "," + ioException.getMessage());
			}
			
			try {
				parseMessage(databaseMappingStream);
			} catch(Exception exception) {
				Log.loge(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-MAPPING: " + this.databaseMappingName + ", " + exception.getMessage());
				throw new SiminovException(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-MAPPING: " + this.databaseMappingName + ", " + exception.getMessage());
			}
		}
		
		doValidation();
	}

	public DatabaseMappingDescriptorParser(final String libraryPackageName, final String databaseMappingName) throws SiminovException {
		this.databaseMappingName = databaseMappingName;
		
		if(databaseMappingName == null || databaseMappingName.length() <= 0) {
			Log.loge(getClass().getName(), "Constructor", "Invalid name found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid name found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
		}
		
		Context context = resources.getApplicationContext();
		if(context == null) {
			Log.loge(getClass().getName(), "Constructor", "Invalid context found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
			throw new SiminovException(getClass().getName(), "Constructor", "Invalid context found. DATABASE-MAPPING-MODEL: " + this.databaseMappingName);
		}

		/*
		 * Parse ApplicationDescriptor.
		 */
		if(!databaseMappingName.endsWith(Constants.XML_FILE_EXTENSION)) {
			this.databaseMapping = new AnnotationParser().parseClass(databaseMappingName);
		} else {
			InputStream databaseMappingStream = null;
			databaseMappingStream = getClass().getClassLoader().getResourceAsStream(libraryPackageName.replace(".", "/") + "/" + this.databaseMappingName);

			try {
				parseMessage(databaseMappingStream);
			} catch(Exception exception) {
				Log.loge(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-MAPPING: " + this.databaseMappingName + ", " + exception.getMessage());
				throw new SiminovException(getClass().getName(), "Constructor", "Exception caught while parsing DATABASE-MAPPING: " + this.databaseMappingName + ", " + exception.getMessage());
			}
		}
		
		doValidation();
	}

	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = "";
		
		if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_DATABASE_MAPPING)) {
			databaseMapping = new DatabaseMappingDescriptor();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_TABLE)) {
			initializeTable(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_COLUMN)) {
			if(!isIndex) {
				initializeColumn(attributes);
			}
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_PROPERTY)) {
			initializeProperty(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_INDEX)) {
			initalizeIndex(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS)) {
			isRelationship = true;
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE)) {
			currectRelationship = new DatabaseMappingDescriptor.Relationship();
			currectRelationship.setRelationshipType(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE);
			
			initializeRelationship(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
			currectRelationship = new DatabaseMappingDescriptor.Relationship();
			currectRelationship.setRelationshipType(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY);
		
			initializeRelationship(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE)) {
			currectRelationship = new DatabaseMappingDescriptor.Relationship();
			currectRelationship.setRelationshipType(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE);

			initializeRelationship(attributes);
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
			currectRelationship = new DatabaseMappingDescriptor.Relationship();
			currectRelationship.setRelationshipType(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY);
		
			initializeRelationship(attributes);
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
		if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_PROPERTY)) {
			processProperty();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_COLUMN)) {
			if(currentIndex != null) {
				currentIndex.addColumn(tempValue);
				return;
			}
			
			databaseMapping.addColumn(currentColumn);
			isColumn = false;
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_INDEX)) {
			databaseMapping.addIndex(currentIndex);
			isIndex = false;
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE)) {
			processRelationship();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY)) {
			processRelationship();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE)) {
			processRelationship();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY)) {
			processRelationship();
		} else if(localName.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS)) {
			isRelationship = false;
		}

	}
	
	public DatabaseMappingDescriptor getDatabaseMapping() {
		return this.databaseMapping;
	}
	
	private void initializeTable(Attributes attributes) {
		String tableName = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_TABLE_NAME);
		String className = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_CLASS_NAME);
		
		databaseMapping.setTableName(tableName);
		databaseMapping.setClassName(className);
	}
	
	private void initializeColumn(final Attributes attributes) {
		
		String variableName = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_VARIABLE_NAME);
		String columnName = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_COLUMN_NAME);

		char[] charArray = variableName.toCharArray();
		charArray[0] = Character.toUpperCase(charArray[0]);
		String getterMethodName = "get" + new String(charArray);
		String setterMethodName = "set" + new String(charArray);
		
		currentColumn = new DatabaseMappingDescriptor.Column();
		
		currentColumn.setVariableName(variableName);
		currentColumn.setColumnName(columnName);
		currentColumn.setGetterMethodName(getterMethodName);
		currentColumn.setSetterMethodName(setterMethodName);

		isColumn = true;

	}
	
	private void initializeProperty(final Attributes attributes) {
		propertyName = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_NAME);
	}
	
	private void initalizeIndex(final Attributes attributes) {
		String name = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_NAME);
		String unique = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_UNIQUE);
		
		currentIndex = new DatabaseMappingDescriptor.Index();
		
		currentIndex.setName(name);
		
		if(unique != null && unique.length() > 0 && unique.equalsIgnoreCase("true")) {
			currentIndex.setUnique(true);
		} else {
			currentIndex.setUnique(false);
		}

		isIndex = true;
	}
	
	private void initializeRelationship(final Attributes attributes) {
		String refer = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER);
		String referTo = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_REFER_TO);
		
		String onUpdate = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_UPDATE);
		String onDelete = attributes.getValue(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ON_DELETE);
		
		currectRelationship.setRefer(refer);
		currectRelationship.setReferTo(referTo);
		
		char[] charArray = refer.toCharArray();
		charArray[0] = Character.toUpperCase(charArray[0]);
		String getterReferMethodName = "get" + new String(charArray);
		String setterReferMethodName = "set" + new String(charArray);
		
		currectRelationship.setGetterReferMethodName(getterReferMethodName);
		currectRelationship.setSetterReferMethodName(setterReferMethodName);
		
		currectRelationship.setOnUpdate(onUpdate);
		currectRelationship.setOnDelete(onDelete);
		
	}
	
	private void processProperty() {
		
		if(isRelationship) {
			currectRelationship.addProperty(propertyName, tempValue);
		} else if(isColumn) {
			currentColumn.addProperty(propertyName, tempValue);
		}
	}
	
	private void processRelationship() {
		databaseMapping.addRelationship(currectRelationship);
	}
	
	private void doValidation() {
		/*
		 * Validate Table Name field.
		 */
		String tableName = databaseMapping.getTableName();
		if(tableName == null || tableName.length() <= 0) {
			Log.loge(getClass().getName(), "doValidation", "TABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING: " + this.databaseMappingName);
			throw new DeploymentException(getClass().getName(), "doValidation", "TABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING: " + this.databaseMappingName);
		}

		/*
		 * Validate Class Name field.
		 */
		String className = databaseMapping.getClassName();
		if(className == null || className.length() <= 0) {
			Log.loge(getClass().getName(), "doValidation", "CLASS-NAME IS MANDATORY FIELD - DATABASE-MAPPING: " + this.databaseMappingName);
			throw new DeploymentException(getClass().getName(), "doValidation", "CLASS-NAME IS MANDATORY FIELD - DATABASE-MAPPING: " + this.databaseMappingName);
		}
		
		Iterator<Column> columns = databaseMapping.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();

			/*
			 * Validate Variable Name field.
			 */
			String variableName = column.getVariableName();
			if(variableName == null || variableName.length() <= 0) {
				Log.loge(getClass().getName(), "doValidation", "VARIABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
				throw new DeploymentException(getClass().getName(), "doValidation", "VARIABLE-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
			}
			
			/*
			 * Validate Column Name filed.
			 */
			String columnName = column.getColumnName();
			if(columnName == null || columnName.length() <= 0) {
				Log.loge(getClass().getName(), "doValidation", "COLUMN-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
				throw new DeploymentException(getClass().getName(), "doValidation", "COLUMN-NAME IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
			}
			
			/*
			 * Validate Type field.
			 */
			String type = column.getType();
			if(type == null || type.length() <= 0) {
				Log.loge(getClass().getName(), "doValidation", "COLUMN-TYPE IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
				throw new DeploymentException(getClass().getName(), "doValidation", "COLUMN-TYPE IS MANDATORY FIELD - DATABASE-MAPPING - COLUMN: " + this.databaseMappingName);
			}
		}
	}
}
