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
import siminov.core.model.EntityDescriptor;
import siminov.core.model.EntityDescriptor.Attribute;
import siminov.core.resource.ResourceManager;
import android.content.Context;


/**
 * Exposes methods to parse Library Descriptor information as per define in DatabaseDescriptor.si.xml or LibraryDescriptor.si.xml  file by application.
	<p>
		<pre>
		
Example:
	{@code
	
	<!-- Design Of EntityDescriptor.si.xml -->
	
	<entity-descriptor>
	
	    <!-- General Properties Of Table And Class -->
	    
	    	<!-- Mandatory Field -->
	    		<!-- NAME OF TABLE -->
	    <property name="table_name">name_of_table</property>
	    
	    	<!-- Mandatory Field -->
	    		<!-- MAPPED CLASS NAME -->
	    <property name="class_name">mapped_class_name</property>
	    
	    
	    	<!-- Optional Field -->
	    <attributes>
	        
		    <!-- Column Properties Required Under This Table -->
		    
				<!-- Optional Field -->
			<attribute>
			    
				    <!-- Mandatory Field -->
						<!-- COLUMN_NAME: Mandatory Field -->
	   		    <property name="column_name">column_name_of_table</property>
			    			
	    		    <!-- Mandatory Field -->
						<!-- VARIABLE_NAME: Mandatory Field -->
			    <property name="variable_name">class_variable_name</property>
			    		    
				    <!-- Mandatory Field -->
				<property name="type">java_variable_data_type</property>
				
					<!-- Optional Field (Default is false) -->
				<property name="primary_key">true/false</property>
				
					<!-- Optional Field (Default is false) -->
				<property name="not_null">true/false</property>
				
					<!-- Optional Field (Default is false) -->
				<property name="unique">true/false</property>
				
					<!-- Optional Field -->
				<property name="check">condition_to_be_checked (Eg: variable_name 'condition' value; variable_name > 0)</property>
				
					<!-- Optional Field -->
				<property name="default">default_value_of_column (Eg: 0.1)</property>
			
			</attribute>		
	
	    </attributes>
			
			
			<!-- Optional Field -->
	    <indexes>
	        
			<!-- Index Properties -->
			<index>
			    
				    <!-- Mandatory Field -->
				    	<!-- NAME OF INDEX -->
			    <property name="name">name_of_index</property>
			    
				    <!-- Mandatory Field -->
						<!-- UNIQUE: Optional Field (Default is false) -->
			    <property name="unique">true/false</property>
			    
			    	<!-- Optional Field -->
			    		<!-- Name of the column -->
			    <property name="column">column_name_needs_to_add</property>
			    
			</index>
	        
	    </indexes>
	    
			
		<!-- Map Relationship Properties -->
					
			<!-- Optional Field's -->	
		<relationships>
			    
		    <relationship>
		        
		        	<!-- Mandatory Field -->
		        		<!-- Type of Relationship -->
		        <property name="type">one-to-one|one-to-many|many-to-one|many-to-many</property>
		        
		        	<!-- Mandatory Field -->
		        		<!-- REFER -->
		        <property name="refer">class_variable_name</property>
		        
		        	<!-- Mandatory Field -->
		        		<!-- REFER TO -->
		        <property name="refer_to">map_to_class_name</property>
		            
		        	<!-- Optional Field -->
		        <property name="on_update">cascade/restrict/no_action/set_null/set_default</property>    
		            
		        	<!-- Optional Field -->    
		        <property name="on_delete">cascade/restrict/no_action/set_null/set_default</property>    
		            
					<!-- Optional Field (Default is false) -->
		       	<property name="load">true/false</property>	            
		        
		    </relationship>
		    
		</relationships>
	
	</entity-descriptor>

		}
	
		</pre>
	</p>
 *
 */
public class EntityDescriptorReader extends SiminovSAXDefaultHandler implements Constants {

	private StringBuilder tempValue = new StringBuilder();
	private String propertyName = null;
	
	private String entityDescriptorName = null;

	private ResourceManager resourceManager = ResourceManager.getInstance();

	private EntityDescriptor entityDescriptor = null;

	private EntityDescriptor.Attribute currentAttribute = null;
	private EntityDescriptor.Index currentIndex = null;
	private EntityDescriptor.Relationship currectRelationship = null;

	private boolean isAttribute = false;
	private boolean isIndex = false;
	private boolean isRelationship = false;
	
	
	/**
	 * EntityDescriptor Constructor
	 * @param entityDescriptorName Name of the entity descriptor name
	 */
	public EntityDescriptorReader(final String entityDescriptorName) {
		this.entityDescriptorName = entityDescriptorName;
		
		if(entityDescriptorName == null || entityDescriptorName.length() <= 0) {
			Log.error(getClass().getName(), "Constructor", "Invalid name found. ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName);
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid name found. ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName);
		}
		
		Context context = resourceManager.getApplicationContext();
		if(context == null) {
			Log.error(getClass().getName(), "Constructor", "Invalid Application Context found. ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName);
			throw new DeploymentException(getClass().getName(), "Constructor", "Invalid Application Context found. ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName);
		}


		InputStream entityDescriptorStream = null;
			
		try {

			entityDescriptorStream = getClass().getClassLoader().getResourceAsStream(this.entityDescriptorName);
			if(entityDescriptorStream == null) {
				entityDescriptorStream = context.getAssets().open(this.entityDescriptorName);
			}
		} catch(IOException ioException) {
			Log.error(getClass().getName(), "Constructor", "IOException caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName + ", " + ioException.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "IOException caught while getting input stream of entity descriptor,  ENTITY-DESCRIPTOR-MODEL: " + this.entityDescriptorName + "," + ioException.getMessage());
		}

		
		try {
			parseMessage(entityDescriptorStream);
		} catch(Exception exception) {
			Log.error(getClass().getName(), "Constructor", "Exception caught while parsing ENTITY-DESCRIPTOR: " + this.entityDescriptorName + ", " + exception.getMessage());
			throw new DeploymentException(getClass().getName(), "Constructor", "Exception caught while parsing ENTITY-DESCRIPTOR: " + this.entityDescriptorName + ", " + exception.getMessage());
		}
		
		doValidation();
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		tempValue = new StringBuilder();
		
		if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR)) {
			entityDescriptor = new EntityDescriptor();
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_PROPERTY)) {
			propertyName = attributes.getValue(ENTITY_DESCRIPTOR_NAME);
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_ATTRIBUTE)) {
			currentAttribute = new EntityDescriptor.Attribute();
			isAttribute = true;
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_INDEX)) {
			currentIndex = new EntityDescriptor.Index();
			isIndex = true;
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP)) {
			currectRelationship = new EntityDescriptor.Relationship();
			isRelationship = true;
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
			processProperty();
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_ATTRIBUTE)) {
			entityDescriptor.addAttribute(currentAttribute);
			isAttribute = false;
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_INDEX)) {
			entityDescriptor.addIndex(currentIndex);
			isIndex = false;
		} else if(localName.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP)) {
			entityDescriptor.addRelationship(currectRelationship);
			isRelationship = false;
		}
	}
	
	public EntityDescriptor getEntityDescriptor() {
		return this.entityDescriptor;
	}
	
	private void processProperty() {
		
		if(isAttribute) {
			currentAttribute.addProperty(propertyName, tempValue.toString());
			
			if(propertyName.equalsIgnoreCase(ENTITY_DESCRIPTOR_ATTRIBUTE_VARIABLE_NAME)) {
				
				char[] charArray = tempValue.toString().toCharArray();
				charArray[0] = Character.toUpperCase(charArray[0]);
				String getterMethodName = "get" + new String(charArray);
				String setterMethodName = "set" + new String(charArray);
				
				currentAttribute.setGetterMethodName(getterMethodName);
				currentAttribute.setSetterMethodName(setterMethodName);
			}
		} else if(isIndex) {
			
			if(propertyName.equalsIgnoreCase(ENTITY_DESCRIPTOR_INDEX_COLUMN)) {
				currentIndex.addColumn(tempValue.toString());
			} else {
				currentIndex.addProperty(propertyName, tempValue.toString());
			}
		} else if(isRelationship) {
			currectRelationship.addProperty(propertyName, tempValue.toString());
			
			if(propertyName.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_REFER)) {
				
				char[] charArray = tempValue.toString().toCharArray();
				charArray[0] = Character.toUpperCase(charArray[0]);
				String getterReferMethodName = "get" + new String(charArray);
				String setterReferMethodName = "set" + new String(charArray);
				
				currectRelationship.setGetterReferMethodName(getterReferMethodName);
				currectRelationship.setSetterReferMethodName(setterReferMethodName);
			}
		} else {
			entityDescriptor.addProperty(propertyName, tempValue.toString());
		}
	}
	
	private void doValidation() {
		/*
		 * Validate Table Name field.
		 */
		String tableName = entityDescriptor.getTableName();
		if(tableName == null || tableName.length() <= 0) {
			Log.error(getClass().getName(), "doValidation", "TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
			throw new DeploymentException(getClass().getName(), "doValidation", "TABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
		}

		/*
		 * Validate Class Name field.
		 */
		String className = entityDescriptor.getClassName();
		if(className == null || className.length() <= 0) {
			Log.error(getClass().getName(), "doValidation", "CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
			throw new DeploymentException(getClass().getName(), "doValidation", "CLASS-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR: " + this.entityDescriptorName);
		}
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();

			/*
			 * Validate Variable Name field.
			 */
			String variableName = attribute.getVariableName();
			if(variableName == null || variableName.length() <= 0) {
				Log.error(getClass().getName(), "doValidation", "VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTRO - COLUMN: " + this.entityDescriptorName);
				throw new DeploymentException(getClass().getName(), "doValidation", "VARIABLE-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
			}
			
			/*
			 * Validate Column Name filed.
			 */
			String columnName = attribute.getColumnName();
			if(columnName == null || columnName.length() <= 0) {
				Log.error(getClass().getName(), "doValidation", "COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
				throw new DeploymentException(getClass().getName(), "doValidation", "COLUMN-NAME IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
			}
			
			/*
			 * Validate Type field.
			 */
			String type = attribute.getType();
			if(type == null || type.length() <= 0) {
				Log.error(getClass().getName(), "doValidation", "COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
				throw new DeploymentException(getClass().getName(), "doValidation", "COLUMN-TYPE IS MANDATORY FIELD - ENTITY-DESCRIPTOR - COLUMN: " + this.entityDescriptorName);
			}
		}
	}
}
