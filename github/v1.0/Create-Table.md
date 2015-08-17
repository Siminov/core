## Create Table

Database and its table creation is handled by Core itself. Table structure is defined using database mapping descriptor file.

Database class also provides API to create table programmatically:


#### Android API: Create Table

```java

    public void createTable() throws DatabaseException;

```

###### Android Sample: Create Table

```java
	
    Liquor liquor = new Liquor();
	
    try {
        liquor.createTable();
    } catch(DatabaseException databaseException) {
		//Log It
    }

```

#### iOS API: Create Table

```objective-c

    - (void)createTable;

```

###### iOS Sample: Create Table

```objective-c
	
    Liquor *liquor = [[Liquor alloc] init];
	
    @try {
        [liquor createTable];
    } @catch(SICDatabaseException *databaseException) {
		//Log It
    }

```


#### Windows API: Create Table

```c#

    public void CreateTable();

```

###### Windows Sample: Create Table

```c#
	
    Liquor liquor = new Liquor();
	
    try 
    {
        liquor.CreateTable();
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It
    }

```
