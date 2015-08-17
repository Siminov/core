## Update

#### Android API: Update

```java

    public final void update() throws DatabaseException;

```

###### Android Sample: Update

```java

    Liquor beer = new Liquor();
    beer.setLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.setDescription(applicationContext.getString(R.string.beer_description));
    beer.setHistory(applicationContext.getString(R.string.beer_history));
    beer.setLink(applicationContext.getString(R.string.beer_link));
    beer.setAlcholContent(applicationContext.getString(R.string.beer_alchol_content));
 
    try {
        beer.update();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Update

```objective-c

    - (void)update;

```

###### iOS Sample: Update

```objective-c

    Liquor *beer = [[Liquor alloc] init];
    [beer setLiquorType:[Liquor LIQUOR_TYPE_BEER]];
    [beer setDescription:@"beer_description"];
    [beer setHistory:@"beer_history"];
    [beer setLink:@"beer_link"];
    [beer setAlcholContent:@"beer_alchol_content"];

    @try {
        [beer update];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Update

```c#

    public void Update();

```

###### Windows Sample: Update

```c#

    Liquor beer = new Liquor();
    beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
    beer.SetDescription("beer_description");
    beer.SetHistory("beer_history");
    beer.SetLink("beer_link");
    beer.SetAlcholContent("beer_alchol_content");
 
    try 
    {
        beer.Update();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```