Database Mapping Descriptor is one which does ORM, it maps your model class to relation database table.

```xml

                   <!-- Design Of DatabaseMappingDescriptor.si.xml -->

    <database-mapping-descriptor>

        <!-- General Properties Of Table And Class -->
    
            <!-- TABLE_NAME: Mandatory Field -->
            <!-- CLASS_NAME: Mandatory Field -->
        <entity table_name="name_of_table" class_name="mapped_model_class_name">
		
            <!-- Column Properties Required Under This Table -->
                <!-- Optional Field -->
		
                    <!-- VARIABLE_NAME: Mandatory Field -->
                    <!-- COLUMN_NAME: Mandatory Field -->
            <attribute column_name="column_name_of_table" variable_name="class_variable_name">
		    
                        <!-- Mandatory Field -->
                    <property name="type">android_variable_data_type|ios_variable_data_type|windows|variable_data_type</property>
			
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

             <!-- Index Properties -->
                 <!-- Optional Field -->

                     <!-- NAME: Mandatory Field -->
                     <!-- UNIQUE: Optional Field (Default is false) -->
             <index name="name_of_index" unique="true/false">
                 <column>column_name_needs_to_add</column>
             </index>
		
             <!-- Map Relationship Properties -->
                  <!-- Optional Field's -->	
             <relationships>
		    
                  <!-- REFER: Mandatory Field -->
                      <!-- REFER_TO: Mandatory Field -->
                  <one-to-one refer="class_variable_name" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
					
                          <!-- Optional Field (Default is false) -->
                      <property name="load">true/false</property>
                  </one-to-one>		
			
                  <!-- REFER: Mandatory Field -->
                      <!-- REFER_TO: Mandatory Field -->
                  <one-to-many refer="class_variable_name" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
					
                          <!-- Optional Field (Default is false) -->
                      <property name="load">true/false</property>
                  </one-to-many>		

                  <!-- REFER: Mandatory Field -->
                      <!-- REFER_TO: Mandatory Field -->
                  <many-to-one refer="class_variable_name" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
					
                           <!-- Optional Field (Default is false) -->
                      <property name="load">true/false</property>
                  </many-to-one>		

                  <!-- REFER: Mandatory Field -->
                      <!-- REFER_TO: Mandatory Field -->
                  <many-to-many refer="class_variable_name" refer_to="map_to_model_class_name" on_update="cascade/restrict/no_action/set_null/set_default" on_delete="cascade/restrict/no_action/set_null/set_default">
					
                           <!-- Optional Field (Default is false) -->
                      <property name="load">true/false</property>
                  </many-to-many>		
									
              </relationships>

          </entity>

    </database-mapping-descriptor>

```


```xml

                   <!-- Android Sample: DatabaseMappingDescriptor.si.xml -->

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="siminov.core.sample.model.Liquor">
		
            <attribute variable_name="liquorType" column_name="LIQUOR_TYPE">
                <property name="type">java.lang.String</property>
                <property name="primary_key">true</property>
                <property name="not_null">true</property>
                <property name="unique">true</property>
            </attribute>		

            <attribute variable_name="description" column_name="DESCRIPTION">
                <property name="type">java.lang.String</property>
            </attribute>

            <attribute variable_name="history" column_name="HISTORY">
                <property name="type">java.lang.String</property>
            </attribute>

            <attribute variable_name="link" column_name="LINK">
                <property name="type">java.lang.String</property>
                <property name="default">www.wikipedia.org</property>
            </attribute>

            <attribute variable_name="alcholContent" column_name="ALCHOL_CONTENT">
                <property name="type">java.lang.String</property>
            </attribute>

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <attribute>HISTORY</attribute>
            </index>

            <relationships>

                <one-to-many refer="liquorBrands" refer_to="siminov.core.sample.model.LiquorBrand" on_update="cascade" on_delete="cascade">
                    <property name="load">true</property>
                </one-to-many>		
		    
            </relationships>
											
        </entity>

    </database-mapping-descriptor>		
			
```


```xml

                <!-- iOS Sample: DatabaseMappingDescriptor.si.xml -->

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="Liquor">
		
            <attribute variable_name="liquorType" column_name="LIQUOR_TYPE">
                <property name="type">NSString</property>
                <property name="primary_key">true</property>
                <property name="not_null">true</property>
                <property name="unique">true</property>
            </attribute>		

            <attribute variable_name="description" column_name="DESCRIPTION">
                <property name="type">NSString</property>
            </attribute>

            <attribute variable_name="history" column_name="HISTORY">
                <property name="type">NSString</property>
            </attribute>

            <attribute variable_name="link" column_name="LINK">
                <property name="type">NSString</property>
                <property name="default">www.wikipedia.org</property>
            </attribute>

            <attribute variable_name="alcholContent" column_name="ALCHOL_CONTENT">
                <property name="type">NSString</property>
            </attribute>

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <attribute>HISTORY</attribute>
            </index>

            <relationships>

                <one-to-many refer="liquorBrands" refer_to="LiquorBrand" on_update="cascade" on_delete="cascade">
                    <property name="load">true</property>
                </one-to-many>		
		    
            </relationships>
											
        </entity>

    </database-mapping-descriptor>		
			
```


```xml

                   <!-- Windows Sample: DatabaseMappingDescriptor.si.xml -->

    <database-mapping-descriptor>

        <entity table_name="LIQUOR" class_name="Siminov.Core.Sample.Model.Liquor">
		
            <attribute variable_name="liquorType" column_name="LIQUOR_TYPE">
                <property name="type">System.String</property>
                <property name="primary_key">true</property>
                <property name="not_null">true</property>
                <property name="unique">true</property>
            </attribute>		

            <attribute variable_name="description" column_name="DESCRIPTION">
                <property name="type">System.String</property>
            </attribute>

            <attribute variable_name="history" column_name="HISTORY">
                <property name="type">System.String</property>
            </attribute>

            <attribute variable_name="link" column_name="LINK">
                <property name="type">System.String</property>
                <property name="default">www.wikipedia.org</property>
            </attribute>

            <attribute variable_name="alcholContent" column_name="ALCHOL_CONTENT">
                <property name="type">System.String</property>
            </attribute>

            <index name="LIQUOR_INDEX_BASED_ON_LINK" unique="true">
                <attribute>HISTORY</attribute>
            </index>

            <relationships>

                <one-to-many refer="liquorBrands" refer_to="Siminov.Core.Sample.Model.LiquorBrand" on_update="cascade" on_delete="cascade">
                    <property name="load">true</property>
                </one-to-many>		
		    
            </relationships>
											
        </entity>

    </database-mapping-descriptor>		
			
```


## Database Mapping Descriptor Elements

###### 1. Entity Tag
It map database table to its corresponding model class.

- **table_name***: Name of table. It is mandatory field.
- **class_name***: Name of model class name which is to be mapped to relational table name. It is mandatory field.

###### 2. Attribute Tag
It map database table column to its corresponding variable of model class.

- **column_name***: Name of column. It is mandatory field.
- **variable_name***: Name of variable. It is mandatory field.

**Properties Of Attribute Tag**
- _**type**_*: 

**Android Variable Types**: (_int, java.lang.Integer, long, java.lang.Long, 
float, java.lang.Float, boolean, java.lang.Boolean, char, java.lang.Character, java.lang.String,
byte, java.lang.Byte, void, java.lang.Void, short, java.lang.Short_). It is mandatory property.

**iOS Variable Types**: (_int, long, float, bool, char, NSString, byte, void, short_). It is mandatory property.

**Windows Variable Types**: (_int, long, float, bool, char, System.String, byte, void, short_). It is mandatory property.

- **primary_key***: **TRUE/FALSE**. It defines whether the column is primary key of table or not. It is optional property. Default value is false.

- **not_null***: **TRUE/FALSE**. It defines whether the column value can be empty or not. It is optional property. Default value is false.

- _**unique**_*: **TRUE/FALSE**. It defines whether the column value should be unique or not. It is optional property. Default value is false.

- _**default**_: It defines the default value of column. It is optional property.

- _**check**_: It is used to put condition on column value. It is optional property.


> **Note**: Application Developer can provide their own properties also, and by using following API's they can use properties.
>
> - **Get Properties - [Android:getProperties | iOS:getProperties | Windows:GetProperties]**: It will return all properties associated with Database Mapping Descriptor Column.
>
> - **Get Property - [Android:getProperty:(Name-of-Property) | iOS:getProperty:Name-of-Property | Windows:GetProperty(Name-of-Property)]**: It will return property value associated with property name provided.
>
> - **Contains Property - [Android:containsProperty(Name-of-Property) | iOS:containsProperty:Name-of-Property | Windows:ContainsProperty(Name-of-Property)]**: It will return TRUE/FALSE whether property exists or not.
>
> - **Add Property - [Android:addProperty(Name-of-Property, Value-of-Property) | iOS:addProperty:Name-Of-Property Windows:AddProperty(Name-Of-Property, Value-Of-Property)]**: It will add new property to the  collection of Database Mapping Descriptor Column properties.
>
> - Remove Property - [Android:removeProperty(Name-of-Property) | iOS:removeProperty:Name-Of-Property | Windows:RemoveProperty(Name-Of-Property)]**: It will remove property from Database Mapping Descriptor Column properties based on name provided.

###### 3. Index Tag
It defines the structure of index needed on the table.

- _**name**_*: Name of the index. It is mandatory field.

- _**unique**_: _TRUE/FALSE_. It defines whether index needs to be unique or not. It is not mandatory property. Default value is false.

(A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique).

**Index Column Tag**
- _**column**_*: Name of columns included in index. At least one column should be included.

###### 4. Relationship Tag
It defines relationship between object. Relationship can be of four types:

- _**One To One Relationship (one-to-one)**_: In a one-to-one relationship, each row in one database table is linked to one and only one other row in another table.

- _**One To Many Relationship (one-to-many)**_: In a one-to-many relationship, each row in the related to table can be related to many rows in the relating table. This effectively save storage as the related
record does not need to be stored multiple times in the relating table.

- _**Many To One Relationship (many-to-one)**_: In a many-to-one relationship one entity (typically a column or set of columns) contains values that refer to another entity (a column or set of columns) that has unique values.

- _**Many To Many Relationship (many-to-many)**_: In a many-to-many relationship, one or more rows in a table can be related to 0, 1 or many rows in another table. A mapping table is required in order to implement such a relationship.


**Relationship Attributes**

- _**refer**_*: Name of variable which needs to be mapped. It is mandatory field.

- **refer_to***: Class name of mapped variable. It is mandatory field.

- **on_update***: **cascade/restrict/no action/set null/set_default**. It defines action needs to be done, when update occur.

- **on_delete***: **cascade/restrict/no action/set null/set_default**. It defines action needs to be done, when delete occur.


**Relationship Properties**

- _**load**_*: It defines whether it need to be load or not.

> **Note**
>
> - Application developer can assign any name to DatabaseMappingDescriptor.si.xml file.
>
> - Descriptor file should be in same place as per defined in DatabaseDescriptor.si.xml file.


### Android Sample: Database Mapping Descriptor

***

![Android Sample: Database Mapping Descriptor] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_database_descriptor_mapping_path_example.png "Android Sample: Database Mapping Descriptor")

***

### iOS Sample: Database Mapping Descriptor

***

![iOS Sample: Database Mapping Descriptor] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/siminov_core_sample_database_descriptor_mapping_path_example.png "iOS Sample: Database Mapping Descriptor")

***

### Windows Sample: Database Mapping Descriptor

***

![Windows Sample: Database Mapping Descriptor] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/siminov_core_sample_database_descriptor_mapping_path_example.png "Windows Sample: Database Mapping Descriptor")

***

