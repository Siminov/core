## Sum

Exposes API's to return sum of all non-NULL values in the group. If there are no non-NULL input rows then sum() returns NULL but total() returns 0.0. NULL is not normally a helpful result for the sum of no rows but the SQL standard requires it and most other SQL database engines implement sum() that way so SQLite does it in the same way in order to be compatible. The result of sum() is an integer value if all non-NULL inputs are integers. 

#### Android API: Sum

```java

    public ISum sum() throws DatabaseException;

```

- _Android: ISum Interface_

```java

    public interface ISum {

        public ISumClause where(String column);
	
        public ISum whereClause(String whereClause);
	
        public ISumClause and(String column);
	
        public ISumClause or(String column);

        public ISum groupBy(String...columns);
	
        public ISumClause having(String column);

        public ISum havingClause(String havingClause);
	
        public ISum column(String column);
	
        public Object execute() throws DatabaseException;

    }

```

- _Android: ISumClause Interface_

```java

    public interface ISumClause {

        public ISum equalTo(String value);

        public ISum notEqualTo(String value);
	
        public ISum greaterThan(String value);
	
        public ISum greaterThanEqual(String value);
	
        public ISum lessThan(String value);
	
        public ISum lessThanEqual(String value);
	
        public ISum between(String start, String end);
	
        public ISum like(String like);
	
        public ISum in(String...values);

    }

```


###### Android Sample: Get Sum 

```java

    int sum = 0;
	
    try {
        sum = new Liquor().sum().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();
    } catch(DatabaseException de) {
		//Log it.
    }
	
```


#### iOS API: Sum

```objective-c

    - (id<SICISum>)sum;

```

- _iOS: SICISum Interface_

```objective-c

    @protocol SICISum <NSObject>

    - (id<SICISumClause>)where:(NSString *)column;

    - (id<SICISum>)whereClause:(NSString *)whereClause;

    - (id<SICISumClause>)and:(NSString *)column;

    - (id<SICISumClause>)or:(NSString *)column;

    - (id<SICISum>)groupBy:(NSArray *)columns;

    - (id<SICISumClause>)having:(NSString *)column;

    - (id<SICISum>)havingClause:(NSString *)havingClause;

    - (id<SICISum>)column:(NSString *)column;

    - (id)execute;

   @end

```

- _iOS: SICISumClause Interface_

```objective-c

    @protocol SICISumClause <NSObject>

    - (id<SICISum>)equalTo:(id)value;

    - (id<SICISum>)notEqualTo:(id)value;

    - (id<SICISum>)greaterThan:(id)value;
 
    - (id<SICISum>)greaterThanEqual:(id)value;

    - (id<SICISum>)lessThan:(id)value;

    - (id<SICISum>)lessThanEqual:(id)value;

    - (id<SICISum>)between:(id)start end:(id)end;

    - (id<SICISum>)like:(id)like;

    - (id<SICISum>)in:(id)values;

    @end

```

###### iOS Sample: Get Sum 

```objective-c

    double sum = 0;
	
    @try {
        sum = [[[[[[[Liquor alloc] init] sum] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }
	
```

#### Windows API: Sum

```c#

    public ISum Sum();

```

- _Windows: ISum Interface_

```c#
    
    public interface ISum 
    {

        ISumClause Where(String column);
	
        ISum WhereClause(String whereClause);
	
        ISumClause And(String column);
	
        ISumClause Or(String column);

        ISum GroupBy(String[] columns);
	
        ISumClause Having(String column);

        ISum HavingClause(String havingClause);
	
        ISum Column(String column);
	
        double Execute();

    }

```

- _Windows: ISumClause Interface_

```c#

    public interface ISumClause 
    {

        ISum EqualTo(String value);

        ISum NotEqualTo(String value);
	
        ISum GreaterThan(String value);
	
        ISum GreaterThanEqual(String value);
	
        ISum LessThan(String value);
	
        ISum LessThanEqual(String value);
	
        ISum Between(String start, String end);
	
        ISum Like(String like);
	
        ISum In(String[] values);

    }

```

####### Windows Sample: Get Sum 

```java

    double sum = 0;
	
    try 
    {
        sum = new Liquor().Sum().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }
	
```