Exposes API's to build database queries.

#### Android API: Query Builder Interface

```java

    public interface IQueryBuilder {

        public String formTableInfoQuery(final Map<String, Object> parameters);

        public String formFetchDatabaseVersionQuery(final Map<String, Object> parameters);
	
        public String formUpdateDatabaseVersionQuery(final Map<String, Object> parameters);
	
        public String formAlterAddColumnQuery(final Map<String, Object> parameters);

        public String formTableNames(final Map<String, Object> parameters);
	
        public String formCreateTableQuery(final Map<String, Object> parameters);

        public String formCreateIndexQuery(final Map<String, Object> parameters);
	
        public String formDropTableQuery(final Map<String, Object> parameters);
	
        public String formDropIndexQuery(final Map<String, Object> parameters);
	
        public String formSelectQuery(final Map<String, Object> parameters);

        public String formSaveBindQuery(final Map<String, Object> parameters);
	
        public String formUpdateBindQuery(final Map<String, Object> parameters);
	
        public String formDeleteQuery(final Map<String, Object> parameters);
	
        public String formCountQuery(final Map<String, Object> parameters);
	
        public String formAvgQuery(final Map<String, Object> parameters);
	
        public String formSumQuery(final Map<String, Object> parameters);
	
        public String formTotalQuery(final Map<String, Object> parameters);

        public String formMaxQuery(final Map<String, Object> parameters);
	
        public String formMinQuery(final Map<String, Object> parameters);
	
        public String formGroupConcatQuery(final Map<String, Object> parameters);
	
        public String formForeignKeyQuery(final Map<String, Object> parameters);
    }

```

#### iOS API: Query Builder Interface

```objective-c

    @protocol SICIQueryBuilder <NSObject>

    - (NSString *)formTableInfoQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formFetchDatabaseVersionQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formUpdateDatabaseVersionQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formAlterAddColumnQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formTableNames:(NSMutableDictionary* const)parameters;

    - (NSString *)formCreateTableQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formCreateIndexQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formDropTableQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formDropIndexQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formSelectQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formSaveBindQuery:(NSMutableDictionary* const)parameters;
   
    - (NSString *)formUpdateBindQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formDeleteQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formCountQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formAvgQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formSumQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formTotalQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formMaxQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formMinQuery:(NSMutableDictionary* const)parameters;
  
    - (NSString *)formGroupConcatQuery:(NSMutableDictionary* const)parameters;

    - (NSString *)formForeignKeyQuery:(NSMutableDictionary* const)parameters;

    @end

```

#### Windows API: Query Builder Interface


```c#

    public interface IQueryBuilder 
    {

        String FormTableInfoQuery(IDictionary<String, Object> parameters);

        String FormFetchDatabaseVersionQuery(IDictionary<String, Object> parameters);
	
        String FormUpdateDatabaseVersionQuery(IDictionary<String, Object> parameters);
	
        String FormAlterAddColumnQuery(IDictionary<String, Object> parameters);

        String FormTableNames(IDictionary<String, Object> parameters);
	
        String FormCreateTableQuery(IDictionary<String, Object> parameters);

        String FormCreateIndexQuery(IDictionary<String, Object> parameters);
	
        String FormDropTableQuery(IDictionary<String, Object> parameters);
	
        String FormDropIndexQuery(IDictionary<String, Object> parameters);
	
        String FormSelectQuery(IDictionary<String, Object> parameters);

        String FormSaveBindQuery(IDictionary<String, Object> parameters);
	
        String FormUpdateBindQuery(IDictionary<String, Object> parameters);
	
        String FormDeleteQuery(IDictionary<String, Object> parameters);
	
        String FormCountQuery(IDictionary<String, Object> parameters);
	
        String FormAvgQuery(IDictionary<String, Object> parameters);
	
        String FormSumQuery(IDictionary<String, Object> parameters);
	
        String FormTotalQuery(IDictionary<String, Object> parameters);

        String FormMaxQuery(IDictionary<String, Object> parameters);
	
        String FormMinQuery(IDictionary<String, Object> parameters);
	
        String FormGroupConcatQuery(IDictionary<String, Object> parameters);
	
        String FormForeignKeyQuery(IDictionary<String, Object> parameters);
    }

```
