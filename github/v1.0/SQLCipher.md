Data Security plays important role when we talk about database. It protect your database from destructive forces and the unwanted actions of unauthorized users.

Android SQLite does not provide any protection against your database. There are many third party security implementation's which application developer can use in their application to protect their database.

#### Introduction
SQLCipher is an open source extension to SQLite that provides transparent 256-bit AES encryption of database le. SQLCipher has a small footprint and great performance so it's ideal for protecting embedded application databases and is well suited for mobile development.

Core provide implementation for SQLCipher database encryption security. Its easy and secured to use it.

Below are steps to make your application totally secured.

## Android Setup: SQLCipher

- Download SQLCipher from their website for android [SQLCipher](http://sqlcipher.net/
downloads/).

- Configure SQLCipher in your application. Follow below steps:
    - (a). Copy **sqlcipher.jar**, **guava-r09.jar**, **commons-codec.jar** in your application libs folder.
***
![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_sqlcipher_application_setup_1.png)
***
    - (b). Copy jar file in your libs folder.
***
![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_sqlcipher_application_setup_2.png)
***
    - (c). Copy **icudt461.zip** in your application assets folder.
***
    ![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_sqlcipher_application_setup_3.png)
***

- Download and copy **Core SQLCipher** jar in application libs folder.

***
![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sqlcipher_sample_application_add_siminov_sqlcipher_jar.png)
***

-  To configure SQLCipher with Core add type property as **sqlcipher** and **password** attribute in **DatabaseDescriptor.si.xml**.

***
![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_sqlcipher_application_sqlcipherdatabaseimpl_class_configure.png)
***

> **Note**
>
> - For any future reference you can download **SIMINOV-CORE-SQLCIPHER-SAMPLE** Application and can check how we have configured application with SQLCipher.

##### Sample: Core SQLCipher Sample Application Structure

***
![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_sqlcipher_application_setup.png)
***


## iOS Setup: SQLCipher

- Download the **sqlcipher-db-impl** library code using Git from [Builds](https://www.siminov.com/build.html).
- Extract build folder inside the library.
- Right click your iOS project in Xcode and choose ‘Add Files to App’. Select **include** folder from the build folder and check all options except **'Create groups'**. 
- Expand Link Binary With Libraries, and add **libsqlcipher-db-impl.a** library.
- Configure Build Settings
- Select Build Settings in app’s main target. Enter **"$(SRCROOT)/include"** in Header Search Paths.
- Set Build Active Architecture Only to ‘No’ for iOS app.
- Add the '-ObjC' flag to the **Other Linker Flags** build setting.
 

***
![] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/sqlcipher_1.png "")
***


- To configure SQLCipher with Siminov add type property as sqlcipher and password attribute in DatabaseDescriptor.si.xml.

*** 
![] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/sqlcipher_2.png "")
***
