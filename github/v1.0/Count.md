## Count

Exposes API's to get count of the number of times that X is not NULL in a group. The count(*) function (with no arguments) returns the total number of rows in the group.

#### Android API: Count

```java
	
    public ICount count() throws DatabaseException;

```

- _Android: ICount_ 

```java

    public interface ICount {

        public ICount distinct();
	
        public ICountClause where(String column);
	
        public ICount whereClause(String whereClause);
	
        public ICountClause and(String column);
	
        public ICountClause or(String column);

        public ICount groupBy(String...columns);
	
        public ICountClause having(String column);

        public ICount havingClause(String havingClause);

        public ICount column(String column);
	
        public Object execute() throws DatabaseException;
	
    }

```

- _Android: ICountClause_

```java

    public interface ICountClause {

        public ICount equalTo(String value);

        public ICount notEqualTo(String value);
	
        public ICount greaterThan(String value);
	
        public ICount greaterThanEqual(String value);
	
        public ICount lessThan(String value);
	
        public ICount lessThanEqual(String value);
	
        public ICount between(String start, String end);
	
        public ICount like(String like);
	
        public ICount in(String...values);

    }

```

###### Android Sample: Get Count

```java

    int count = 0;
	
    try {
        count = new Liquor().count().where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }
	
```


#### iOS API: Count

```objective-c

    - (id<SICICount>)count;

```

- _iOS: ICount_

```objective-c

    @protocol SICICount <NSObject>

    - (id<SICICount>)distinct;

    - (id<SICICountClause>)where:(NSString *)column;

    - (id<SICICount>)whereClause:(NSString *)whereClause;
 
    - (id<SICICountClause>)and:(NSString *)column;

    - (id<SICICountClause>)or:(NSString *)column;

    - (id<SICICount>)groupBy:(NSArray *)columns;

    - (id<SICICountClause>)having:(NSString *)column;

    - (id<SICICount>)havingClause:(NSString *)havingClause;
 
    - (id<SICICount>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICICountClause_

```objective-c

    @protocol SICICountClause <NSObject>

    - (id<SICICount>)equalTo:(id)value;

    - (id<SICICount>)notEqualTo:(id)value;

    - (id<SICICount>)greaterThan:(id)value;

    - (id<SICICount>)greaterThanEqual:(id)value;

    - (id<SICICount>)lessThan:(id)value;

    - (id<SICICount>)lessThanEqual:(id)value;

    - (id<SICICount>)between:(id)start end:(id)end;

    - (id<SICICount>)like:(id)like;

    - (id<SICICount>)in:(id)values;

    @end

```

###### iOS Sample: Get Count

```objective-c

    int count = 0;
	
    @try {
        count = [[[[[[Liquor alloc] init] count] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }
	
```


#### Windows API: Count

```c#

    public ICount Count();

```

- _Windows: ICount_ 

```c#

    public interface ICount 
    {

        ICount Distinct();
	
        ICountClause Where(String column);
	
        ICount WhereClause(String whereClause);
	
        ICountClause And(String column);
	
        ICountClause Or(String column);

        ICount GroupBy(String[] columns);
	
        ICountClause Having(String column);

        ICount HavingClause(String havingClause);
	
        ICount Column(String column);
	
        int execute();
	
    }

```

- _Windows: ICountClause_

```c#

    public interface ICountClause 
    {

        ICount EqualTo(String value);

        ICount NotEqualTo(String value);
	
        ICount GreaterThan(String value);
	
        ICount GreaterThanEqual(String value);
	
        ICount LessThan(String value);
	
        ICount LessThanEqual(String value);
	
        ICount Between(String start, String end);
	
        public ICount Like(String like);
	
        public ICount In(String[] values);

    }

```

###### Windows Sample: Get Count

```c#

    int count = 0;
	
    try 
    {
        count = new Liquor().Count().Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }
	
```

