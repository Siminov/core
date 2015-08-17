## Total

Exposes API's to return total of all non-NULL values in the group. The non-standard total() function is provided as a convenient way to work around this design problem in the SQL language. The result of total() is always a floating point value.

#### Android API: Total

```java

    public ITotal total() throws DatabaseException;

```

- _Android: ITotal Interface_

```java

    public interface ITotal {

        public ITotalClause where(String column);
	
        public ITotal whereClause(String whereClause);
	
        public ITotalClause and(String column);
	
        public ITotalClause or(String column);

        public ITotal groupBy(String...columns);
	
        public ITotalClause having(String column);

        public ITotal havingClause(String havingClause);
	
        public ITotal column(String column);
	
        public Object execute() throws DatabaseException;
	
    }

```

- _Android: ITotalClause Interface_

```java

    public interface ITotalClause {

        public ITotal equalTo(String value);

        public ITotal notEqualTo(String value);
	
        public ITotal greaterThan(String value);
	
        public ITotal greaterThanEqual(String value);
	
        public ITotal lessThan(String value);
	
        public ITotal lessThanEqual(String value);
	
        public ITotal between(String start, String end);
	
        public ITotal like(String like);
	
        public ITotal in(String...values);
	
    }

```

###### Android Sample: Get Total 

```java

    int total = 0;
	
    try {
        total = new Liquor().total().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Total


```objective-c

    - (id<SICITotal>)total;

```

- _iOS: SICITotal Interface_

```objective-c

    @protocol SICITotal <NSObject>

    - (id<SICITotalClause>)where:(NSString *)column;

    - (id<SICITotal>)whereClause:(NSString *)whereClause;

    - (id<SICITotalClause>)and:(NSString *)column;
  
    - (id<SICITotalClause>)or:(NSString *)column;

    - (id<SICITotal>)groupBy:(NSArray *)columns;

    - (id<SICITotalClause>)having:(NSString *)column;

    - (id<SICITotal>)havingClause:(NSString *)havingClause;

    - (id<SICITotal>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICITotalClause Interface

```objective-c

    @protocol SICITotalClause <NSObject>

    - (id<SICITotal>)equalTo:(id)value;

    - (id<SICITotal>)notEqualTo:(id)value;

    - (id<SICITotal>)greaterThan:(id)value;
 
    - (id<SICITotal>)greaterThanEqual:(id)value;

    - (id<SICITotal>)lessThan:(id)value;

    - (id<SICITotal>)lessThanEqual:(id)value;

    - (id<SICITotal>)between:(id)start end:(id)end;

    - (id<SICITotal>)like:(id)like;

    - (id<SICITotal>)in:(id)values;

    @end

```


###### iOS Sample: Get Total 

```objective-c

    double total = 0;
	
    @try {
        total = [[[[[[[Liquor alloc] init] total] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

#### Windows API: Total

```c#

    public ITotal Total();

```

- _Windows: ITotal Interface_

```c#

    public interface ITotal 
    {

        ITotalClause Where(String column);
	
        ITotal WhereClause(String whereClause);
	
        ITotalClause And(String column);
	
        ITotalClause Or(String column);

        ITotal GroupBy(String[] columns);
	
        ITotalClause Having(String column);

        ITotal HavingClause(String havingClause);
	
        ITotal Column(String column);
	
        double Execute();
	
    }

```

- _Windows: ITotalClause Interface_

```c#

    public interface ITotalClause 
    {

        ITotal EqualTo(String value);

        ITotal NotEqualTo(String value);
	
        ITotal GreaterThan(String value);
	
        ITotal GreaterThanEqual(String value);
	
        ITotal LessThan(String value);
	
        ITotal LessThanEqual(String value);
	
        ITotal Between(String start, String end);
	
        ITotal Like(String like);
	
        ITotal In(String[] values);
	
    }

```

###### Windows Sample: Get Total 

```c#

    double total = 0;
	
    try 
    {
        total = new Liquor().Total().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```
