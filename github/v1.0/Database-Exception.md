This is general exception, which is thrown through Siminov database API's, if any exception occur which doing any database operations.

#### Android API: Database Exception

```java

    public class DatabaseException extends RuntimeException {

        public String getClassName() { }
        public void setClassName(String className) { }

        public String getMethodName() { }
        public void setMethodName(String methodName) { }

        public String getMessage() { }
        public void setMessage(String message) { }
 
    }
```

#### iOS API: Database Exception


```objective-c

    @interface SICDatabaseException:SICSiminovException

    - (id)initWithClassName:(NSString* const)className methodName:(NSString* const)methodName message:(NSString* const)message;

    @end

```

#### Windows API: Database Exception

```c#

    public class DatabaseException : SiminovCriticalException 
    {

        String GetClassName() { }
        void SetClassName(String className) { }

        String GetMethodName() { }
        void SetMethodName(String methodName) { }

        String GetMessage() { }
        void SetMessage(String message) { }
 
    }
```