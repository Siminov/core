Application Descriptor is the one who connects application to Siminov Core Framework. It provide basic information about application, which defines the behavior of application. 
 
```xml
    
                   <!-- Application Descriptor Design -->
    <siminov>
    
        <!-- General Application Description Properties -->
	
            <!-- Mandatory Field -->
        <property name="name">application_name</property>	
	
            <!-- Optional Field -->
        <property name="description">application_description</property>
	
             <!-- Mandatory Field (Default is 0.0) -->
        <property name="version">application_version</property>


        <!-- Database Descriptors Used By Application (zero-to-many) -->	
             <!-- Optional Field's -->
        <database-descriptors>
             <database-descriptor>database_descriptor_file_name</database-descriptor>
        </database-descriptors>
		

        <!-- Library Descriptors Used By Application (zero-to-many) -->
            <!-- Optional Field's -->
        <library-descriptors>
            <library-descriptor>library_descriptor_file_name</library-descriptor>   
        </library-descriptors>
	

        <!-- Event Handlers Implemented By Application (zero-to-many) -->
            <!-- Optional Field's -->
        <event-handlers>
            <event-handler>event_handler_name(ISiminovHandler/IDatabaseHandler/SICSiminovHandler/SICDatabaseHandler)          </event-handler>
        </event-handlers>
    </siminov>
```


```xml

             <!-- Android Sample: ApplicationDescriptor.si.xml -->
    <siminov>

        <property name="name">SIMINOV CORE SAMPLE</property>	
        <property name="description">Siminov Core Sample Application</property>
        <property name="version">1.0</property>

	
        <!-- Database Descriptors -->
        <database-descriptors>
            <database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
        </database-descriptors>

	
        <!-- Event Handlers -->
        <event-handlers>
            <event-handler>siminov.core.sample.events.SiminovEventHandler</event-handler>
            <event-handler>siminov.core.sample.events.DatabaseEventHandler</event-handler>
        </event-handlers>
	
        <!-- Library Descriptors -->
        <library-descriptors>
            <library-descriptor>siminov.core.library.sample.resources</library-descriptor>   
        </library-descriptors>
			
    </siminov>

```


```xml

              <!-- iOS Sample: ApplicationDescriptor.si.xml -->
    <siminov>

        <property name="name">SIMINOV CORE SAMPLE</property>	
        <property name="description">Siminov Core Sample Application</property>
        <property name="version">1.0</property>

	
        <!-- Database Descriptors -->
        <database-descriptors>
             <database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
        </database-descriptors>

	
        <!-- Event Handlers -->
        <event-handlers>
            <event-handler>SiminovEventHandler</event-handler>
            <event-handler>DatabaseEventHandler</event-handler>
        </event-handlers>
	
        <!-- Library Descriptors -->
        <library-descriptors>
            <library-descriptor>Siminov.Core.Library.Sample.Resources</library-descriptor>   
        </library-descriptors>
			
    </siminov>

```


```xml

              <!-- Windows Sample: ApplicationDescriptor.si.xml -->
    <siminov>

        <property name="name">SIMINOV CORE SAMPLE</property>	
        <property name="description">Siminov Core Sample Application</property>
        <property name="version">1.0</property>

	
        <!-- Database Descriptors -->
        <database-descriptors>
            <database-descriptor>DatabaseDescriptor.si.xml</database-descriptor>
        </database-descriptors>

	
        <!-- Event Handlers -->
        <event-handlers>
            <event-handler>Siminov.Core.Sample.Events.SiminovEventHandler</event-handler>
            <event-handler>Siminov.Core.Sample.Events.DatabaseEventHandler</event-handler>
        </event-handlers>
	
        <!-- Library Descriptors -->
        <library-descriptors>
            <library-descriptor>Siminov.Core.Library.Sample.Resources</library-descriptor>   
        </library-descriptors>
			
    </siminov>

```

> _**Note**_: Application Developer can provide their own properties also, and by using following API's they can use properties.
>
> - **Get Properties - [Android:getProperties | iOS:getProperties | Windows:GetProperties]**: It will return all properties associated with Application Descriptor.
>
> - **Get Property - [Android:getProperty(Name-of-Property) | iOS:getProperty:Name-Of-Property | Windows:GetProperty(Name-Of-Property)]**: It will return property value associated with property name provided.
>
> - **Contains Property - [Android:containsProperty(Name-of-Property) | iOS:containsProperty:Name-Of-Property | Windows:ContainsProperty(Name-Of-Property)]**: It will return TRUE/FALSE whether property exists or not.
>
> - **Add Property - [Android:addProperty(Name-of-Property, Value-of-Property) | iOS:addProperty:Name-of-Property value:Value-of-Property | Windows:AddProperty(Name-of-Property, Value-of-Property)]**: It will add new property to the  collection of Application Descriptor properties.
>
> - **Remove Property - [Android:removeProperty(Name-of-Property) | iOS:removeProperty:Name-of-Property | Windows:RemoveProperty(Name-of-Property)]**: It will remove property from Application Descriptor properties based on name provided.

## Application Descriptor Elements

###### 1. General properties about application

- _**name**_*: Name of application. It is mandatory field. If any resources is created by Core, then it will be under this folder name.

**Android Sample - Application data folder based on name defined**
*** 

![Application Data Folder Based On Name Defined] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_application_data_folder_structure_for_application_name.png "Application Data Folder Based On Name Defined")

***

**iOS Sample - Application data folder based on name defined**
*** 

![Application Data Folder Based On Name Defined] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/siminov_core_sample_application_data_folder_structure_for_application_name.png "Application Data Folder Based On Name Defined")

***

**Windows Sample - Application data folder based on name defined**
*** 

![Application Data Folder Based On Name Defined] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/siminov_core_sample_application_data_folder_structure_for_application_name.png "Application Data Folder Based On Name Defined")

***

- _**descriptor**_: Description of application. It is optional field. 

- _**version**_: Version of application. It is mandatory field. Default is 0.0.

###### 3. Paths of database descriptor's used in application
- Path of all database descriptor's used in application.
- Every database descriptor will have its own database object.

###### 4. Paths of all library descriptors needed by the application.

> <b>Note</b>: 
> - Provide full package name under which LibraryDescriptor.si.xml file is placed.
> - Siminov framework will automatically read LibraryDescriptor.si.xml file defined under package name provided.

###### 5. Event handlers implemented by application

- Siminov Framework provides two type of event handlers

**- Android:ISiminovEvents | iOS:SICISiminovEvents | Windows:ISiminovEvents:** : It contains events associated with life cycle of Siminov Framework. such as:

**Android:onSiminovInitialized | iOS:onSiminovInitialized | Windows:OnSiminovInitialized**

**Android:onFirstTimeSiminovInitialized | iOS:onFirstTimeSiminovInitialized | Windows:OnFirstTimeSiminovInitialized** 

**Android:onSiminovStopped | iOS:onSiminovStopped | Windows:OnSiminovStopped**.

**- Android:IDatabaseEvents | iOS:SICIDatabaseEvents | Windows:IDatabaseEvents :** : It contains events associated with database operations. such as:

**Android:onDatabaseCreated | iOS:onDatabaseCreated | Windows:OnDatabaseCreated** 

**Android:onDatabaseDropped | iOS:onDatabaseDropped | Windows:OnDatabaseDropped** 

**Android:onTableCreated | iOS:onTableCreated | Windows:OnTableCreated** 

**Android:onTableDropped | iOS:onTableDropped | Windows:OnTableDropped** 

**Android:onIndexCreated | iOS:onIndexCreated | Windows:OnIndexCreated** 

**Android:onIndexDropped | iOS:onIndexDropped | Windows:onIndexDropped**.

- Application can implement these event handlers based on there requirement.

> **Note**
>
> - Application descriptor file name should always be same as ApplicationDescriptor.si.xml only.
>
> - It should always be in root folder of application assets.


### Android Sample: Application Descriptor

***

![Android Sample: Application Descriptor] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_application_sample_application_descriptor_path_example.png "Android Sample: Application Descriptor")

***

### iOS Sample: Application Descriptor

***

![iOS Sample: Application Descriptor] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/siminov_application_sample_application_descriptor_path_example.png "iOS Sample: Application Descriptor")

***

### Windows Sample: Application Descriptor

***

![Windows Sample: Application Descriptor] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/siminov_application_sample_application_descriptor_path_example.png "Windows Sample: Application Descriptor")

***

