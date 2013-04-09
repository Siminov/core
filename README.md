Siminov ORM (Object Relationship Mapping) - Android
===================================================

Siminov ORM is a open source Object/Relational Mapping solution for Android environments. It maps data from an object model representation to a relational data model representation (and visa versa). 

Siminov ORM not only takes care of the mapping from Java classes to database tables (and from Java data types to SQL data types), but also provides data query and retrieval facilities. 

Get Started
-----------
Get the source

  git clone git://github.com/siminov/andiorm.git
  
	
Features
--------

###### 1. Easy Configuration
Siminov provides a easy set of defined descriptors which can be broadly classified as 
	
	|- ApplicationDescriptor.si.xml 
	|- DatabaseDescriptor.si.xml
	|- LibraryDescriptor.si.xml
	|- DatabaseMappingDescriptor.si.xml.

###### 2. Handle Application Initialization
All resources required by application are created and managed by siminov orm. (Eg: Creating Database, Deploying Application).

###### 3. Handle Multiple Schema's
It also supports multiple schema's if required by application.

###### 4. Events Notifier
It provides event notifiers which gets triggered based on particular action

	Eaxmple: 
	|- Siminov Initialized
	|- Siminov Stopped
	|- Database Created and Dropped
	|- Table Create and Dropped
	|- Index Created and Dropped
	
###### 5. Database API's

	|- Database Create and Drop
	|- Table Create and Drop
	|- Index Create and Drop
	|- Fetch
	|- Save
	|- Update
	|- Save Or Update
	|- Delete
	
###### 6. Aggregation API's
	
	|- Count
	|- Average
	|- Sum
	|- Total
	|- Minimum
	|- Maximum
	|- Group Concat
	
###### 7. Database Transaction API's

	|- Begin Transaction
	|- Commit Transaction
	|- End Transaction
	
	

###### 8. Database Encryption (SQLCipher)
Data Secuirty plays important role when we talk about database. It protect your database from desctructive forces and the unwanted actions of unauthorized users.

Siminov provides implementation for SQLCipher to protect application database from any unauthorized users.


###### 9. Handling Libraries
An android library project is a development project that holds shared android source code and resources. Other android application projects can reference the library project and, at build time, include its compiled sources in their .apk files.

Siminov provides mechanism to configure ORM for your library projects.


LICENSE
-------

 
<b> SIMINOV FRAMEWORK </b>
 <p>
 Copyright [2013] [Siminov Software Solution|support@siminov.com]
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.



[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/7a4fa4337bbaef7efea367892bcc7e12 "githalytics.com")](http://githalytics.com/Siminov/andiorm)
