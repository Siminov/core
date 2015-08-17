Siminov Framework is divided into Six different layers

![] (https://raw.github.com/Siminov/android-core/doc-resources/github-wiki-resources/siminov_layers.png "")

- _**Deployment Layer**_
When application starts for first time it does not have its database's. Siminov provides deployment layer, it creates application database's by reading all descriptors from application assests and its libraries.

- _**Database Layer**_
It holds meta data about application needed by Siminov.

- _**Resources Layer**_
This layer basically deals with database operations.

- _**Model And Annotation Layer**_
It holds Model POJO classes and Annotation defination required for database mapping descriptor.

- _**Parser Layer**_
It contain parses which parses all descriptor defined by application.

- _**Utils Layer**_ 
It provides util classes which provides additional benefits.