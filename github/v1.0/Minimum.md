## Minimum

Exposes API's to returns the minimum non-NULL value of all values in the group. The minimum value is the first non-NULL value that would appear in an ORDER BY of the column. Aggregate min() returns NULL if and only if there are no non-NULL values in the group.

#### Android API: Minimum

```java

    public IMin min() throws DatabaseException;

```

- _Android: IMin Interface_

```java

    public interface IMin {

        public IMinClause where(String column);
	
        public IMin whereClause(String whereClause);
	
        public IMinClause and(String column);
	
        public IMinClause or(String column);

        public IMin groupBy(String...columns);
	
        public IMinClause having(String column);

        public IMin havingClause(String havingClause);
	
        public IMin column(String column);
	
        public Object execute() throws DatabaseException;

    }

```

- _Android: IMinClause Interface_

```java

    public interface IMinClause {

        public IMin equalTo(String value);

        public IMin notEqualTo(String value);
	
        public IMin greaterThan(String value);
	
        public IMin greaterThanEqual(String value);
	
        public IMin lessThan(String value);
	
        public IMin lessThanEqual(String value);
	
        public IMin between(String start, String end);
	
        public IMin like(String like);
	
        public IMin in(String...values);
	
    }

```

###### Android Sample: Get Minimum 

```java

    int minimum = 0;
	
    try {
        minimum = new Liquor().min().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }
	
```

#### iOS API: Minimum

```objective-c

    - (id<SICIMin>)min;

```

- _iOS: SICIMin Interface_

```objective-c

    @protocol SICIMin <NSObject>

    - (id<SICIMinClause>)where:(NSString *)column;

    - (id<SICIMin>)whereClause:(NSString *)whereClause;

    - (id<SICIMinClause>)and:(NSString *)column;

    - (id<SICIMinClause>)or:(NSString *)column;

    - (id<SICIMin>)groupBy:(NSArray *)columns;

    - (id<SICIMinClause>)having:(NSString *)column;

    - (id<SICIMin>)havingClause:(NSString *)havingClause;

    - (id<SICIMin>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICIMinClause Interface_

```objective-c

    @protocol SICIMinClause <NSObject>

    - (id<SICIMin>)equalTo:(id)value;

    - (id<SICIMin>)notEqual:(id)value;

    - (id<SICIMin>)greaterThan:(id)value;
   
    - (id<SICIMin>)greaterThanEqual:(id)value;

    - (id<SICIMin>)lessThan:(id)value;

    - (id<SICIMin>)lessThanEqual:(id)value;

    - (id<SICIMin>)between:(id)start end:(id)end;

    - (id<SICIMin>)like:(id)like;

    - (id<SICIMin>)in:(id)values;

    @end

```

###### iOS Sample: Minimum 

```objective-c

    double minimum = 0;
	
    @try {
        minimum = [[[[[[[Liquor alloc] init] min] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

	
```

#### Windows API: Minimum

```c#

    public IMin Min();

```

- _Windows: IMin Interface_

```c#

    public interface IMin 
    {
 
        IMinClause Where(String column);
	
        IMin WhereClause(String whereClause);
	
        IMinClause And(String column);
	
        IMinClause Or(String column);

        IMin GroupBy(String[] columns);
	
        IMinClause Having(String column);

        IMin HavingClause(String havingClause);
	
        IMin Column(String column);
	
        double Execute();

    }

```

- _Windows: IMinClause Interface_

```c#

    public interface IMinClause 
    {

        IMin EqualTo(String value);

        IMin NotEqualTo(String value);
	
        IMin GreaterThan(String value);
	
        IMin GreaterThanEqual(String value);
	
        IMin LessThan(String value);
	
        IMin LessThanEqual(String value);
	
        IMin Between(String start, String end);
	
        IMin Like(String like);
	
        IMin In(String[] values);
	
    }

```

###### Windows Sample: Minimum 

```c#
    double minimum = 0;
	
    try 
    {
        minimum = new Liquor().Min().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }
	
```
