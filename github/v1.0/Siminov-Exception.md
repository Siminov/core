This is a general exception, which is thrown through basic Siminov API, if any exception occur while performing any tasks.

#### Android API: Siminov Exception

```java

    public class SiminovException extends Exception {

        public String getClassName() { }
        public void setClassName(String className) { }

        public String getMethodName() { }
        public void setMethodName(String methodName) { }

        public String getMessage() { }
        public void setMessage(String message) { }

    }

```

#### iOS API: Siminov Exception

```objective-c

    @interface SICSiminovException:NSException {
    }

    - (id)initWithClassName:(NSString* const)className methodName:(NSString* const)methodName message:(NSString* const)message;
    - (NSString *)getClassName;
    - (void)setClassName:(NSString* const)className;
    - (NSString *)getMethodName;
    - (void)setMethodName:(NSString* const)methodName;
    - (NSString *)getMessage;
    - (void)setMessage:(NSString* const)message;

    @end 

```

#### Windows API: Siminov Exception

```c#

    public class SiminovException : System.Exception, IException 
    {

        String GetClassName() { }
        void SetClassName(String className) { }

        String GetMethodName() { }
        void SetMethodName(String methodName) { }

        String GetMessage() { }
        void SetMessage(String message) { }

    }
```