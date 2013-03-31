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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import siminov.orm.Constants;
import siminov.orm.annotation.Column;
import siminov.orm.annotation.ColumnProperty;
import siminov.orm.annotation.Index;
import siminov.orm.annotation.IndexColumn;
import siminov.orm.annotation.Indexes;
import siminov.orm.annotation.ManyToMany;
import siminov.orm.annotation.ManyToOne;
import siminov.orm.annotation.OneToMany;
import siminov.orm.annotation.OneToOne;
import siminov.orm.annotation.RelationshipProperty;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor.Relationship;



/**
 * Exposes methods to parse annotation defined in POJO class. 
 * It has methods to provide class name or class object.
 */
public class AnnotationParser {

	/**
	 * Get database mapping object after parsing annotation based on class name provided.
	 * @param className Name Of Class.
	 * @return Database Mapping Object.
	 * @throws SiminovException If any exception occur while parsing annotations of class.
	 */
	public DatabaseMappingDescriptor parseClass(final String className) throws SiminovException {
		
		Class<?> classObject = null;
		try {
			classObject = Class.forName(className);
		} catch(Exception exception) {
			Log.loge(AnnotationParser.class.getName(), "parseClass", "Exception caught while creating class object, CLASS-NAME: " + className + ", " + exception.getMessage());
			throw new SiminovException(AnnotationParser.class.getName(), "parseClass", "Exception caught while creating class object, CLASS-NAME: " + className + ", " + exception.getMessage());
		}
		
		return parseClass(classObject);
	}

	/**
	 * Get database mapping object after parsing annotation based on class object provided.
	 * @param classObject Class Object.
	 * @return Database Mapping Object.
	 * @throws SiminovException If any exception occur while parsing annotations of class object.
	 */
	public DatabaseMappingDescriptor parseClass(final Class<?> classObject) throws SiminovException {
		Annotation[] annotations = classObject.getAnnotations();
		
		if(annotations == null || annotations.length <= 0) {
			return null;
		}

		DatabaseMappingDescriptor databaseMapping = null;
        for(int i = 0;i < annotations.length;i++) {
        	String databaseMappingAnnotationClassName = siminov.orm.annotation.Table.class.getName();
        	String databaseMappingAnnotationIndexesName = siminov.orm.annotation.Indexes.class.getName();
        	
        	String classAnnotationClassName = annotations[i].annotationType().getName();

        	if(databaseMappingAnnotationClassName.equalsIgnoreCase(classAnnotationClassName)) {
        		databaseMapping = new DatabaseMappingDescriptor();
        		
        		String tableName = (String) getAnnotationValue(annotations[i], siminov.orm.annotation.Table.METHOD_GET_TABLE_NAME);
        		
        		databaseMapping.setTableName(tableName);
        		databaseMapping.setClassName(classObject.getName());
        		
        		parserField(classObject.getDeclaredFields(), databaseMapping);
        	} else if(databaseMappingAnnotationIndexesName.equalsIgnoreCase(classAnnotationClassName)) {
        		if(databaseMapping == null) {
        			continue;
        		}
        		
        		Object[] databaseMappingIndexes =  (Object[]) getAnnotationValue(annotations[i], Indexes.METHOD_GET_VALUES);
				if(databaseMappingIndexes == null || databaseMappingIndexes.length <= 0) {
					continue;
				}
				
				for(int j = 0;j < databaseMappingIndexes.length;j++) {
	        		String name =  (String) getAnnotationValue(databaseMappingIndexes[j], Index.METHOD_GET_NAME);
					boolean isUnique = (Boolean) getAnnotationValue(databaseMappingIndexes[j], Index.METHOD_GET_IS_UNIQUE);
					Object[] columns =  (Object[]) getAnnotationValue(databaseMappingIndexes[j], Indexes.METHOD_GET_VALUES);
					
					if(columns == null || columns.length <= 0) {
						continue;
					}
					
					siminov.orm.model.DatabaseMappingDescriptor.Index index = new DatabaseMappingDescriptor.Index();
					index.setName(name);
					index.setUnique(isUnique);

					for(int k = 0;k < columns.length;k++) {
						index.addColumn((String) getAnnotationValue(columns[k], IndexColumn.METHOD_GET_COLUMN));
					}
					
					databaseMapping.addIndex(index);
				}
        	}
        }
		
        return databaseMapping;
	}

	private void parserField(final Field[] fields, final DatabaseMappingDescriptor databaseMapping) throws SiminovException {

		if(fields == null || fields.length <= 0) {
			return;
		}
		
		for(int i = 0;i < fields.length;i++) {
			Annotation[] annotations = fields[i].getAnnotations();
			
			if(annotations == null || annotations.length <= 0) {
				continue;
			}

			DatabaseMappingDescriptor.Column column = null;
			
			for(int j = 0;j < annotations.length;j++) {
				
				//Database Mapping Column 
				String databaseMappingColumnAnnotationClassName = Column.class.getName();
				
				
				//Database Mapping Relationships
				String databaseMappingOneToOneRelationshipClassName = OneToOne.class.getName();
				String databaseMappingOneToManyRelationshipClassName = OneToMany.class.getName();
				String databaseMappingManyToOneRelationshipClassName = ManyToOne.class.getName();
				String databaseMappingManyToManyRelationshipClassName = ManyToMany.class.getName();
				
				
				String fieldAnnotationClassName = annotations[j].annotationType().getName();
				if(databaseMappingColumnAnnotationClassName.equalsIgnoreCase(fieldAnnotationClassName)) {
					column = new DatabaseMappingDescriptor.Column();
					
					String columnName = (String) getAnnotationValue(annotations[j], Column.METHOD_GET_COLUMN_NAME);
					String variableName = fields[i].getName();
					
					column.setColumnName(columnName);
					column.setVariableName(variableName); 
					
					char[] charArray = variableName.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]);
					String getMethodName = "get" + new String(charArray);
					String setMethodName = "set" + new String(charArray);

					column.setGetterMethodName(getMethodName);
					column.setSetterMethodName(setMethodName);
					
					Class<?> type = fields[i].getType();
					column.setType(type.getName());

					databaseMapping.addColumn(column);

					/*
					 * Read Column Properties.
					 */
					Object[] databaseMappingColumnProperties =  (Object[]) getAnnotationValue(annotations[j], Column.METHOD_GET_PROPERTIES);
					if(databaseMappingColumnProperties == null || databaseMappingColumnProperties.length <= 0) {
						continue;
					}
					
					for(int k = 0;k < databaseMappingColumnProperties.length;k++) {
						String name = (String) getAnnotationValue(databaseMappingColumnProperties[k], ColumnProperty.METHOD_GET_NAME);
						String value = (String) getAnnotationValue(databaseMappingColumnProperties[k], ColumnProperty.METHOD_GET_VALUE);
						
						if(name.equalsIgnoreCase(ColumnProperty.NOT_NULL)) {
							
							if(value != null && value.length() > 0 && value.equalsIgnoreCase(Constants.TRUE)) {
								column.setNotNull(true);
							} else {
								column.setNotNull(false);
							}
						} else if(name.equalsIgnoreCase(ColumnProperty.PRIMARY_KEY)) {

							if(value != null && value.length() > 0 && value.equalsIgnoreCase(Constants.TRUE)) {
								column.setPrimaryKey(true);
							} else {
								column.setPrimaryKey(false);
							}
						} else if(name.equalsIgnoreCase(ColumnProperty.UNIQUE)) {

							if(value != null && value.length() > 0 && value.equalsIgnoreCase(Constants.TRUE)) {
								column.setUnique(true);
							} else {
								column.setUnique(false);
							}
						} else if(name.equalsIgnoreCase(ColumnProperty.DEFAULT)) {
							column.setDefaultValue(value);
						} else if(name.equalsIgnoreCase(ColumnProperty.CHECK)) {
							column.setCheck(value);
						}
					}
				} else if(databaseMappingOneToOneRelationshipClassName.equalsIgnoreCase(fieldAnnotationClassName)) {
					Relationship oneToOneRelationship = new Relationship();
					oneToOneRelationship.setRelationshipType(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_ONE);

					String refer = fields[i].getName();
					String referTo = fields[i].getType().getName();
					
					String onUpdate = (String) getAnnotationValue(annotations[j], OneToOne.METHOD_GET_ON_UPDATE);
					String onDelete = (String) getAnnotationValue(annotations[j], OneToOne.METHOD_GET_ON_DELETE);
					
					oneToOneRelationship.setRefer(refer);
					oneToOneRelationship.setReferTo(referTo);
					
					char[] charArray = refer.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]);
					String getterReferMethodName = "get" + new String(charArray);
					String setterReferMethodName = "set" + new String(charArray);

					oneToOneRelationship.setGetterReferMethodName(getterReferMethodName);
					oneToOneRelationship.setSetterReferMethodName(setterReferMethodName);

					oneToOneRelationship.setOnUpdate(onUpdate);
					oneToOneRelationship.setOnDelete(onDelete);
					
					/*
					 * Read Column Properties.
					 */
					
					Object[] databaseMappingRelationshipProperties =  (Object[]) getAnnotationValue(annotations[j], OneToOne.METHOD_GET_PROPERTIES);
					if(databaseMappingRelationshipProperties == null || databaseMappingRelationshipProperties.length <= 0) {
						continue;
					}
					
					for(int k = 0;k < databaseMappingRelationshipProperties.length;k++) {
						String name = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_NAME);
						String value = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_VALUE);
						
						if(name.equalsIgnoreCase(RelationshipProperty.LOAD)) {
							
							if(value != null && value.equalsIgnoreCase(Constants.TRUE)) {
								oneToOneRelationship.setLoad(true);
							}
						}
					}

					databaseMapping.addRelationship(oneToOneRelationship);
				} else if(databaseMappingOneToManyRelationshipClassName.equalsIgnoreCase(fieldAnnotationClassName)) {
					Relationship oneToManyRelationship = new Relationship();
					oneToManyRelationship.setRelationshipType(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_ONE_TO_MANY);

					ParameterizedType referToClassType = (ParameterizedType) fields[i].getGenericType();
					Class<?> referToClass = (Class<?>) referToClassType.getActualTypeArguments()[0];
					
					String refer = fields[i].getName();
					String referTo = referToClass.getName();

					String onUpdate = (String) getAnnotationValue(annotations[j], OneToMany.METHOD_GET_ON_UPDATE);
					String onDelete = (String) getAnnotationValue(annotations[j], OneToMany.METHOD_GET_ON_DELETE);
					
					oneToManyRelationship.setRefer(refer);
					oneToManyRelationship.setReferTo(referTo);
					
					char[] charArray = refer.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]);
					String getterReferMethodName = "get" + new String(charArray);
					String setterReferMethodName = "set" + new String(charArray);

					oneToManyRelationship.setGetterReferMethodName(getterReferMethodName);
					oneToManyRelationship.setSetterReferMethodName(setterReferMethodName);

					oneToManyRelationship.setOnUpdate(onUpdate);
					oneToManyRelationship.setOnDelete(onDelete);
					
					/*
					 * Read Column Properties.
					 */
					
					Object[] databaseMappingRelationshipProperties =  (Object[]) getAnnotationValue(annotations[j], OneToMany.METHOD_GET_PROPERTIES);
					if(databaseMappingRelationshipProperties == null || databaseMappingRelationshipProperties.length <= 0) {
						continue;
					}
					
					for(int k = 0;k < databaseMappingRelationshipProperties.length;k++) {
						String name = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_NAME);
						String value = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_VALUE);
						
						if(name.equalsIgnoreCase(RelationshipProperty.LOAD)) {
							
							if(value != null && value.equalsIgnoreCase(Constants.TRUE)) {
								oneToManyRelationship.setLoad(true);
							}
						}
					}

					databaseMapping.addRelationship(oneToManyRelationship);					
				} else if(databaseMappingManyToOneRelationshipClassName.equalsIgnoreCase(fieldAnnotationClassName)) {
					Relationship manyToOneRelationship = new Relationship();
					manyToOneRelationship.setRelationshipType(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_ONE);
					
					String refer = fields[i].getName();
					String referTo = fields[i].getType().getName();

					String onUpdate = (String) getAnnotationValue(annotations[j], ManyToOne.METHOD_GET_ON_UPDATE);
					String onDelete = (String) getAnnotationValue(annotations[j], ManyToOne.METHOD_GET_ON_DELETE);
					
					manyToOneRelationship.setRefer(refer);
					manyToOneRelationship.setReferTo(referTo);
					
					char[] charArray = refer.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]);
					String getterReferMethodName = "get" + new String(charArray);
					String setterReferMethodName = "set" + new String(charArray);

					manyToOneRelationship.setGetterReferMethodName(getterReferMethodName);
					manyToOneRelationship.setSetterReferMethodName(setterReferMethodName);

					manyToOneRelationship.setOnUpdate(onUpdate);
					manyToOneRelationship.setOnDelete(onDelete);
					
					/*
					 * Read Column Properties.
					 */
					
					Object[] databaseMappingRelationshipProperties =  (Object[]) getAnnotationValue(annotations[j], ManyToOne.METHOD_GET_PROPERTIES);
					if(databaseMappingRelationshipProperties == null || databaseMappingRelationshipProperties.length <= 0) {
						continue;
					}
					
					for(int k = 0;k < databaseMappingRelationshipProperties.length;k++) {
						String name = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_NAME);
						String value = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_VALUE);
						
						if(name.equalsIgnoreCase(RelationshipProperty.LOAD)) {
							
							if(value != null && value.equalsIgnoreCase(Constants.TRUE)) {
								manyToOneRelationship.setLoad(true);
							}
						}
					}

					databaseMapping.addRelationship(manyToOneRelationship);					
				} else if(databaseMappingManyToManyRelationshipClassName.equalsIgnoreCase(fieldAnnotationClassName)) {
					Relationship manyToManyRelationship = new Relationship();
					manyToManyRelationship.setRelationshipType(Constants.DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_MANY_TO_MANY);
					
					ParameterizedType referToClassType = (ParameterizedType) fields[i].getGenericType();
					Class<?> referToClass = (Class<?>) referToClassType.getActualTypeArguments()[0];

					String refer = fields[i].getName();
					String referTo = referToClass.getName();

					String onUpdate = (String) getAnnotationValue(annotations[j], ManyToMany.METHOD_GET_ON_UPDATE);
					String onDelete = (String) getAnnotationValue(annotations[j], ManyToMany.METHOD_GET_ON_DELETE);
					
					manyToManyRelationship.setRefer(refer);
					manyToManyRelationship.setReferTo(referTo);
					
					char[] charArray = refer.toCharArray();
					charArray[0] = Character.toUpperCase(charArray[0]);
					String getterReferMethodName = "get" + new String(charArray);
					String setterReferMethodName = "set" + new String(charArray);

					manyToManyRelationship.setGetterReferMethodName(getterReferMethodName);
					manyToManyRelationship.setSetterReferMethodName(setterReferMethodName);

					manyToManyRelationship.setOnUpdate(onUpdate);
					manyToManyRelationship.setOnDelete(onDelete);
					
					/*
					 * Read Column Properties.
					 */
					
					Object[] databaseMappingRelationshipProperties =  (Object[]) getAnnotationValue(annotations[j], ManyToMany.METHOD_GET_PROPERTIES);
					if(databaseMappingRelationshipProperties == null || databaseMappingRelationshipProperties.length <= 0) {
						continue;
					}
					
					for(int k = 0;k < databaseMappingRelationshipProperties.length;k++) {
						String name = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_NAME);
						String value = (String) getAnnotationValue(databaseMappingRelationshipProperties[k], RelationshipProperty.METHOD_GET_VALUE);
						
						if(name.equalsIgnoreCase(RelationshipProperty.LOAD)) {
							
							if(value != null && value.equalsIgnoreCase(Constants.TRUE)) {
								manyToManyRelationship.setLoad(true);
							}
						}
					}

					databaseMapping.addRelationship(manyToManyRelationship);										
				}
			}
		}
	}
	
	private Object getAnnotationValue(final Annotation annotation, final String methodName) throws SiminovException {
		try {
    		return annotation.annotationType().getMethod(methodName, null).invoke(annotation, null);
		} catch(Exception e) {
			Log.loge(getClass().getName(), "getAnnotationValue", "Exception caught while getting annotation value, ANNOTATION-CLASS-NAME: " + annotation.annotationType().getName() + ", METHOD NAME: " + methodName + ", " + e.getMessage());
			throw new SiminovException(getClass().getName(), "getAnnotationValue", "Exception caught while getting annotation value, ANNOTATION-CLASS-NAME: " + annotation.annotationType().getName() + ", METHOD NAME: " + methodName + ", " + e.getMessage());
		}
	}
	
	private Object getAnnotationValue(final Object object, final String methodName) throws SiminovException {
		try {
    		return object.getClass().getMethod(methodName, null).invoke(object, null);
		} catch(Exception e) {
			Log.loge(getClass().getName(), "getAnnotationValue", "Exception caught while getting annotation value, CLASS-NAME: " + object.getClass().getName() + ", METHOD NAME: " + methodName + ", " + e.getMessage());
			throw new SiminovException(getClass().getName(), "getAnnotationValue", "Exception caught while getting annotation value, CLASS-NAME: " + object.getClass().getName() + ", METHOD NAME: " + methodName + ", " + e.getMessage());
		}
	}
	
}
