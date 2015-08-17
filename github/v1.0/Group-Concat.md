## Group Concat

Exposes API's to get group concat that returns a string which is the concatenation of all non-NULL values of X. If parameter Y is present then it is used as the separator between instances of X. A comma (",") is used as the separator if Y is omitted. The order of the concatenated elements is arbitrary.


#### Android API: Group Concat

```java

    public IGroupConcat groupConcat() throws DatabaseException;

```

- _Android: IGroupConcat Interface_

```java
    
    public interface IGroupConcat {

        public IGroupConcat delimiter(String delimiter);
	
        public IGroupConcatClause where(String column);
	
        public IGroupConcat whereClause(String whereClause);
	
        public IGroupConcatClause and(String column);
	
        public IGroupConcatClause or(String column);

        public IGroupConcat groupBy(String...columns);
	
        public IGroupConcatClause having(String column);

        public IGroupConcat havingClause(String havingClause);
	
        public IGroupConcat column(String column);
	
        public Object execute() throws DatabaseException;

    }

```

- _Android: IGroupConcatClause Interface_

```java

    public interface IGroupConcatClause {

        public IGroupConcat equalTo(String value);

        public IGroupConcat notEqualTo(String value);
	
        public IGroupConcat greaterThan(String value);
	
        public IGroupConcat greaterThanEqual(String value);
	
        public IGroupConcat lessThan(String value);
	
        public IGroupConcat lessThanEqual(String value);
	
        public IGroupConcat between(String start, String end);
	
        public IGroupConcat like(String like);
	
        public IGroupConcat in(String...values);

    }

```

###### Android Sample: Group Concat 

```java

    int groupConcat = 0;
	
    try {
        groupConcat = new Liquor().groupConcat().column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).where(Liquor.LIQUOR_TYPE).equalTo("RUM").execute();				
    } catch(DatabaseException de) {
		//Log it.
    }

```

#### iOS API: Group Concat

```objective-c

    - (id<SICIGroupConcat>)groupConcat;

```

- _iOS: SICIGroupConcat Interface_

```objective-c

    @protocol SICIGroupConcat <NSObject>

    - (id<SICIGroupConcat>)delimiter:(NSString *)delimiter;

    - (id<SICIGroupConcatClause>)where:(NSString *)column;

    - (id<SICIGroupConcat>)where:(NSString *)whereClause;

    - (id<SICIGroupConcatClause>)and:(NSString *)column;

    - (id<SICIGroupConcatClause>)or:(NSString *)column;

    - (id<SICIGroupConcat>)groupBy:(NSArray *)columns;

    - (id<SICIGroupConcatClause>)having:(NSString *)column;

    - (id<SICIGroupConcat>)havingClause:(NSString *)havingClause;

    - (id<SICIGroupConcat>)column:(NSString *)column;

    - (id)execute;

    @end

```

- _iOS: SICIGroupConcatClause Interface_

```objective-c

    @protocol SICIGroupConcatClause <NSObject>

    - (id<SICIGroupConcat>)equalTo:(id)value;

    - (id<SICIGroupConcat>)notEqualTo:(id)value;

    - (id<SICIGroupConcat>)greaterThan:(id)value;

    - (id<SICIGroupConcat>)greaterThan:(id)value;

    - (id<SICIGroupConcat>)lessThan:(id)value;

    - (id<SICIGroupConcat>)lessThan:(id)value;

    - (id<SICIGroupConcat>)between:(id)start end:(id)end;

    - (id<SICIGroupConcat>)like:(id)like;

    - (id<SICIGroupConcat>)in:(id)values;

    @end

```

###### iOS Sample: Get Group Concat 

```objective-c

    NSString *groupConcat = 0;
	
    @try {
        groupConcat = [[[[[[[Liquor alloc] init] groupConcat] column:[Liquor COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE]] where:[Liquor LIQUOR_TYPE]] equalTo:@"RUM"] execute];
    } @catch(SICDatabaseException *databaseException) {
		//Log it.
    }

```

###### Windows API: Group Concat

```c#

    public IGroupConcat GroupConcat();

```

- _Windows: IGroupConcat Interface_

```c#

    public interface IGroupConcat 
    {

        IGroupConcat Delimiter(String delimiter);
	
        IGroupConcatClause Where(String column);
	
        IGroupConcat WhereClause(String whereClause);
	
        IGroupConcatClause And(String column);
	
        IGroupConcatClause Or(String column);

        IGroupConcat GroupBy(String[] columns);
	
        IGroupConcatClause Having(String column);

        IGroupConcat HavingClause(String havingClause);
	
        IGroupConcat Column(String column);
	
        String Execute();

    }

```

- _Windows: IGroupConcatClause Interface_

```c#

    public interface IGroupConcatClause 
    {

        IGroupConcat EqualTo(String value);

        IGroupConcat NotEqualTo(String value);
	
        IGroupConcat GreaterThan(String value);
	
        IGroupConcat GreaterThanEqual(String value);
	
        IGroupConcat LessThan(String value);
	
        IGroupConcat LessThanEqual(String value);
	
        IGroupConcat Between(String start, String end);
	
        IGroupConcat Like(String like);
	
        IGroupConcat In(String[] values);

    }

```

###### Windows Sample: Get Group Concat 

```c#

    String groupConcat = 0;
	
    try 
    {
        groupConcat = new Liquor().GroupConcat().Column(Liquor.COLUMN_NAME_WHICH_CONTAIN_NUMBRIC_VALUE).Where(Liquor.LIQUOR_TYPE).EqualTo("RUM").Execute();				
    } 
    catch(DatabaseException de) 
    {
		//Log it.
    }

```