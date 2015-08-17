Exposes methods to deal with actual database object. It has methods to open, create, close, and execute query's.

#### Android API: IDatabase Interface

```java

    public interface IDatabase {

        public void openOrCreate(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;

        public void close(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;

        public void executeQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException;

        public void executeBindQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query, final Iterator<Object> columnValues) throws DatabaseException;

        public Iterator<Map<String, Object>> executeFetchQuery(final DatabaseDescriptor databaseDescriptor, final DatabaseMappingDescriptor databaseMappingDescriptor, final String query) throws DatabaseException;

        public void executeMethod(final String methodName, final Object parameters) throws DatabaseException;

    }

```

#### iOS API: SICDatabase Interface

```objective-c

    @protocol SICIDatabaseImpl <NSObject>

    - (void)openOrCreate:(SICDatabaseDescriptor* const)databaseDescriptor;

    - (void)close:(SICDatabaseDescriptor* const)databaseDescriptor;

    - (void)executeQuery:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor query:(NSString* const)query;

    - (void)executeBindQuery:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor query:(NSString* const)query columnValues:(NSEnumerator* const)columnValues;

    - (NSEnumerator*)executeSelectQuery:(SICDatabaseDescriptor* const)databaseDescriptor databaseMappingDescriptor:(SICDatabaseMappingDescriptor* const)databaseMappingDescriptor query:(NSString* const)query;

    -(void)executeMethod:(NSString* const)methodName parameter:(id const)parameter;
 
    @end

```

#### Windows API: IDatabase Interface

```c#

    public interface IDatabase 
    {

        void OpenOrCreate(DatabaseDescriptor databaseDescriptor);

        void Close(DatabaseDescriptor databaseDescriptor);

        void ExecuteQuery(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMappingDescriptor, String query);

        void ExecuteBindQuery(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMappingDescriptor, String query, IEnumerator<Object> columnValues);

        IEnumerator<IDictionary<String, Object>> ExecuteFetchQuery(DatabaseDescriptor databaseDescriptor, DatabaseMappingDescriptor databaseMappingDescriptor, String query);

        void ExecuteMethod(String methodName, Object parameters);

    }

```


##### 1. Open Or Create - openOrCreate(Path)
Open/Create the database through Database Descriptor. By default add **CREATE_IF_NECESSARY** flag so that if database does not exist it will create.

_Android Sample: Open Or Create_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }


    try {
        database.openOrCreate(databasePath + databaseDescriptor.getDatabaseName());			
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Open Or Create_


```objective-c
    SICDatabaseDescriptor *databaseDescriptor = [[SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name];
    SICIDatabase *database = nil;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }


    @try {
        [database openOrCreate:databasePath + [databaseDescriptor getDatabaseName]];			
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```


_Windows Sample: Open Or Create_

```c#

    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }


    try 
    {
        database.OpenOrCreate(databasePath + databaseDescriptor.GetDatabaseName());			
    } 
    catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```


##### 2. Close - close()
Close the existing opened database through Database Descriptor.

_Android Sample: Close_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }


    try {
        database.close();
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Close_

```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name]];
    SICIDatabase *database = nil;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }


    @try {
        [database close];
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```

_Windows Sample: Close_


```c#
	
    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }


    try 
    {
        database.Close();
    } 
    catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```


##### 3. Execute Query - executeQuery(Query)

Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data. It has no
means to return any data (such as the number of affected rows). Instead, you're encouraged to use insert, update, delete, when possible.

_Android Sample: Execute Query_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }


    try {
        database.executeQuery(Query);
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Execute Query_

```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name]];
    SICIDatabase *database = null;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }


    @try {
        [database executeQuery:Query];
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```

_Windows Sample: Execute Query_

```c#

    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }


    try 
    {
        database.ExecuteQuery(Query);
    } 
    catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```

##### 4. Execute Bind Query - executeBindQuery(Query, Column Values)

A pre-compiled statement that can be reused. The statement cannot return multiple rows, but 1x1 result sets are allowed.

_Android Sample: Execute Bind Query_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }

    try {
        database.executeBindQuery(Query, Column Values);
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Execute Bind Query_


```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name]];
    SICIDatabase *database = null;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }

    @try {
        [database executeBindQuery:Query values:Column-Values];
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```

_Windows Sample: Execute Bind Query_

```c#
	
    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }

    try 
    {
        database.ExecuteBindQuery(Query, Column Values);
    } 
    catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```


##### 5. Execute Fetch Query - executeFetchQuery(Query)

Query the given table, returning a Cursor over the result set.

_Android Sample: Execute Fetch Query_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }

    try {
        database.executeFetchQuery(Query);
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Execute Fetch Query_

```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name];
    SICIDatabase *database = null;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }

    @try {
        [database executeFetchQuery:Query];
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```

_Windows Sample: Execute Fetch Query_

```c#
	
    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }

    try 
    {
        database.ExecuteFetchQuery(Query);
    } 
    catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```


##### 6. Execute Method - executeMethod(Method Name, Parameters)

Executes the method on database object.

_Android Sample: Execute Method_

```java

    DatabaseDescriptor databaseDescriptor = ResourceManager.getInstance().getDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try {
        database = Database.createDatabase(databaseDescriptor);
    } catch(DatabaseException databaseException) {
		//Log It.
    }


    try {
        database.executeMethod(Method Name, Parameters);
    } catch(DatabaseException databaseException) {
		// Log It.
    }

```

_iOS Sample: Execute Method_

```objective-c

    SICDatabaseDescriptor *databaseDescriptor = [SICResourceManager getInstance] getDatabaseDescriptorBasedOnName:database-descriptor-name]];
    SICIDatabase *database = null;
		
    @try {
        database = [SICDatabase createDatabase:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log It.
    }


    @try {
        [database executeMethod:Method Name parameters:Parameters;
    } @catch(SICDatabaseException *databaseException) {
		// Log It.
    }

```

_Windows API: Execute Method_

```c#

    DatabaseDescriptor databaseDescriptor = ResourceManager.GetInstance().GetDatabaseDescriptorBasedOnName(database-descriptor-name);
    IDatabase database = null;
		
    try 
    {
        database = Database.CreateDatabase(databaseDescriptor);
    } 
    catch(DatabaseException databaseException) 
    {
		//Log It.
    }


    try 
    {
        database.ExecuteMethod(Method Name, Parameters);
    } catch(DatabaseException databaseException) 
    {
		// Log It.
    }

```

