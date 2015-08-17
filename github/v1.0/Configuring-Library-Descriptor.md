Library Descriptor is the one who defines the properties of library.

```xml

               <!-- Design Of LibraryDescriptor.si.xml -->

    <library-descriptor>

        <!-- General Properties Of Library -->
            <!-- Mandatory Field -->
        <property name="name">name_of_library</property>
	
            <!-- Optional Field -->
        <property name="description">description_of_library</property>

	
        <!-- Database Mappings Needed Under This Library Descriptor -->
            <!-- Optional Field -->
                <!-- Database Mapping Descriptors -->
        <database-mapping-descriptors>
            <database-mapping-descriptor>name_of_database_descriptor.full_path_of_database_mapping_descriptor_file</database-mapping-descriptor>
        </database-mapping-descriptors>
	 
    </library-descriptor>

```


```xml

                   <!-- Android Sample: LibraryDescriptor.si.xml -->

    <library-descriptor>

        <property name="name">SIMINOV CORE LIBRARY SAMPLE</property>
        <property name="description">Siminov Core Library Sample</property>

        <database-mapping-descriptors>
            <database-mapping-descriptor>SIMINOV-CORE-SAMPLE.Credential.si.xml</database-mapping-descriptor>
        </database-mapping-descriptors>
	 
    </library-descriptor>

```

```xml

                  <!-- iOS Sample: LibraryDescriptor.si.xml -->

    <library-descriptor>

        <property name="name">SIMINOV CORE LIBRARY SAMPLE</property>
        <property name="description">Siminov Core Library Sample</property>

        <database-mapping-descriptors>
            <database-mapping-descriptor>SIMINOV-CORE-SAMPLE.Credential.si.xml</database-mapping-descriptor>
        </database-mapping-descriptors>
	 
    </library-descriptor>

```

```xml

                  <!-- Windows Sample: LibraryDescriptor.si.xml -->

    <library-descriptor>

        <property name="name">SIMINOV CORE LIBRARY SAMPLE</property>
        <property name="description">Siminov Core Library Sample</property>

        <database-mapping-descriptors>
            <database-mapping-descriptor>SIMINOV-CORE-SAMPLE.Credential.si.xml</database-mapping-descriptor>
        </database-mapping-descriptors>
	 
    </library-descriptor>

```


> **Note**: Application Developer can provide their own properties also, and by using following API's they can use properties.
>
> - **Get Properties - [Android:getProperties | iOS:getProperties | Windows:GetProperties]**: It will return all properties associated with Library Descriptor.
>
> - **Get Property - [Android:getProperty(Name-of-Property) | iOS:getProperty:Name-Of-Property | Windows:GetProperty(Name-Of-Property)]**: It will return property value associated with property name provided.
>
> - **Contains Property - [Android:containsProperty(Name-of-Property) | iOS:containsProperty:Name-of-Property | Windows:ContainsProperty(Name-of-Property)]**: It will return TRUE/FALSE whether property exists or not.
>
> - **Add Property - [Android:addProperty(Name-of-Property, Value-of-Property) | iOS:addProperty:Name-of-Property value:Value-of-Property | Windows:AddProperty(Name-of-Property, Value-of-Property)]**: It will add new property to the  collection of Library Descriptor properties.
>
> - **Remove Property - [Android:removeProperty(Name-of-Property) | iOS:removeProperty:Name-of-Property | Windows:RemoveProperty(Name-of-Property)]**: It will remove property from Library Descriptor properties based on name provided.
>


## Library Descriptor Elements

###### 1. General properties about library descriptor

- _**name**_*: Name of library. It is mandatory field.

- _**descriptor**_: Description of library. It is optional field.

###### 2. Database mapping descriptor paths needed under this database descriptor.

> **Note**
>
> - Provide database mapping descriptor file name and path.

###### 3. Important points about library descriptor
> **Note**
>
> - Library descriptor file name should be same as LibraryDescriptor.si.xml.
>
> - It should always be in root package specified in DatabaseDescriptor.si.xml file.

### Android Sample: Library Descriptor

***

![Android Sample: Library Descriptor] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_library_sample_path_example.png "Android Sample: Library Descriptor")

***

### iOS Sample: Library Descriptor

***

![iOS Sample: Library Descriptor] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/siminov_core_library_sample_path_example.png "iOS Sample: Library Descriptor")

***


### Windows Sample: Library Descriptor

***

![Windows Sample: Library Descriptor] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/siminov_core_library_sample_path_example.png "Windows Sample: Library Descriptor")

***
