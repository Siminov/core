This is run-time exception, which is thrown if any exception occur at time of initialization of Siminov.

#### Android API: Deployment Exception

```java

    public class DeploymentException extends RuntimeException {

        public String getClassName() { }
        public void setClassName(String className) { }

        public String getMethodName() { }
        public void setMethodName(String methodName) { }

        public String getMessage() { }
        public void setMessage(String message) { }
 
    }

```

#### iOS API: Deployment Exception

```objective-c

    @interface SICDeploymentException : SICSiminovCriticalException

    - (id)initWithClassName:(NSString* const)className methodName:(NSString* const)methodName message:(NSString* const)message;

    @end

```


#### Windows API: Deployment Exception

```c#
 
    public class DeploymentException : SiminovCriticalException 
    {

        String GetClassName() { }
        void SetClassName(String className) { }

        String GetMethodName() { }
        void SetMethodName(String methodName) { }

        String GetMessage() { }
        void SetMessage(String message) { }
 
    }

```