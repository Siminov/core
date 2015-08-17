## Begin Transaction

Begins a transaction in EXCLUSIVE mode.

Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back. The changes will be rolled back if any transaction is ended without being marked as clean(by calling _commitTransaction_). Otherwise they will be committed.


#### Android API: Begin Transaction

```java

    public static final void beginTransaction(final DatabaseDescriptor databaseDescriptor) throws DatabaseException;

```

###### Android Sample: Begin Transaction

```java

    Liquor beer = new Liquor();
    beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.setDescription(applicationContext.getString(R.string.beer_description));
    beer.setHistory(applicationContext.getString(R.string.beer_history));
    beer.setLink(applicationContext.getString(R.string.beer_link));
    beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));

    DatabaseDescriptor databaseDescriptor = beer.getDatabaseDescriptor();
  
    try {
        Database.beginTransaction(databaseDescriptor);
	
        beer.save();
  
        Database.commitTransaction(databaseDescriptor);
    } catch(DatabaseException de) {
		//Log it.
    } finally {
        Database.endTransaction(databaseDescriptor);
    }

```

#### iOS API: Begin Transaction

```objective-c
	
    + (void)beginTransaction:(SICDatabaseDescriptor *)databaseDescriptor;

```

###### iOS Sample: Begin Transaction

```objective-c

    Liquor *beer = [[Liquor alloc] init];
    [beer setLiquorType:[Liquor LIQUOR_TYPE_BEER]];
    [beer setDescription:@"beer_description"];
    [beer setHistory:@"beer_history"];
    [beer setLink:@"beer_link"];
    [beer setAlcholContent:@"beer_alchol_content"];

    SICDatabaseDescriptor *databaseDescriptor = [beer getDatabaseDescriptor];
  
    @try {
        [SICDatabase beginTransaction:databaseDescriptor];
	
        [beer save];
  
        [SICDatabase commitTransaction:databaseDescriptor];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    } @finally {
        [SICDatabase endTransaction:databaseDescriptor];
    }

```

#### Windows API: Begin Transaction

```c#

	public static void BeginTransaction(DatabaseDescriptor databaseDescriptor);

```


###### Windows Sample: Begin Transaction

```c#

    Liquor beer = new Liquor();
    beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.SetDescription("beer_description");
    beer.SetHistory("beer_history");
    beer.SetLink("beer_link");
    beer.SetAlcholContent("beer_alchol_content");

    DatabaseDescriptor databaseDescriptor = beer.GetDatabaseDescriptor();
  
    try 
    {
        Database.BeginTransaction(databaseDescriptor);
	
        beer.Save();
  
        Database.CommitTransaction(databaseDescriptor);
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    } 
    finally 
    {
        Database.EndTransaction(databaseDescriptor);
    }

```