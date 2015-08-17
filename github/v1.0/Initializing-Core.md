Every application when it starts they need to first initialize Core. This can be done by invoking initialize API of Android:_siminov.core.Siminov_ | iOS:_SICSiminov_ | Windows:_Siminov.Core.Siminov_ class by passing required parameters to the method.


## Android Initialization

There are two ways to initialize Core on Android.

#### - _**Initializing Core by extending application class**_

```java

    public class ApplicationSiminov extends Application {

        public void onCreate() { 
            super.onCreate();

            initializeSiminov();
        }
	
        private void initializeSiminov() {
		
            IInitializer initializer = siminov.core.Siminov.initialize();
            initializer.addParameter(this);

            initializer.start();
        }
    }

```

> **Note**
>
> - Android provides support in every application to create an application wide class. The base class for this is  the _android.app.Application_ class.


#### - _**Initializing Core by extending activity class**_

```java

    public class HomeActivity extends Activity {
	
        public void onCreate(Bundle savedInstanceState) {
            initializeSiminov();
        }

        private void initializeSiminov() {
            IInitializer initializer = siminov.core.Siminov.initialize();
            initializer.addParameter(getApplicationContext());

            initializer.start();
        }
    }

```

> Note
>
> - If you are initializing Core from activity sub class then you should pass application context not the activity context. See above example.


> **Note**
>
> - Application should call <i>siminov.core.Siminov</i> initialize only once in the life time of application.
>
> - Once Core is initialized it can not be re-initialized.


#### Android:IInitializer Interface
_siminov.core.Siminov.initialize()_ API returns IInitializer interface implemented class through which we can pass parameters needed by Siminov Framework to work functionally.


```java

    public interface IInitializer {

        public void addParameter(Object object);
	
        public void start();
	
    }

```


## iOS Initialization

#### - _**Initializing Core from AppDelegate**_

```objective-c

    id<SICIInitializer> initializer = [SICSiminov initializer];
    [initializer initialize];

```


#### iOS:SICIInitializer Interface
[SICSiminov initializer] API returns SICIInitializer interface through which we can pass parameters needed by Siminov Framework to work functionally.


```objective-c

    @protocol SICIInitializer <NSObject>

    - (void)addParameter:(id)object;

    - (void)initialize;

    @end

```


> Note
>
> - Application should call _SICSiminov_ initialize only once in the life time of application.
>
> - Once Core is initialized it can not be re-initialized.


## Windows Initialization

#### - _**Initializing Core by extending application class**_

```c#

    sealed partial class App : Application
    {
        /// <summary>
        /// Initializes the singleton application object.  This is the first line of authored code
        /// executed, and as such is the logical equivalent of main() or WinMain().
        /// </summary>
        public App()
        {
            this.InitializeComponent();
            this.Suspending += OnSuspending;

            new Siminov.Core.Sample.Siminov().InitializeSiminov();
        }
    }

```


#### Windows:IInitializer Interface
_Siminov.Core.Siminov.Initialize()_ API returns IInitializer interface implemented class through which we can pass parameters needed by Siminov Framework to work functionally.


```c#

    public interface IInitializer 
    {

        void AddParameter(Object object);
	
        void Start();
	
    }

```


> **Note**
>
> - Windows provides support in every application to create an application wide class. The base class for this is the _Windows.UI.Xaml.Application_ class.


> **Note**
>
> - Application should call _Siminov.Core.Siminov_ initialize only once in the life time of application.
>
> - Once Core is initialized it can not be re-initialized.


## Steps Performed While Initializing Core

#### 1. Database Creation
Siminov provides **Initialization Layer** which handles the creation of databases required by application. Siminov follows below steps to create databases required by application.

- Step 1: Then application invokes initialize method, it checks whether database exists or not.

- Step 2: If application database does not exists, Core will create database required by the application.

- Step 3: If application database exists, then it will read all descriptors defined by application based on load initially property defined in ApplicationDescriptor.si.xml file.

#### 2. Initialize Resource Manager Layer
Any resource created by siminov framework is places in resource layer of siminov. You can use API's provided by Resources class to get required object.


#### Android: Resource Manager

```java

    public final class ResourceManager {
        public static ResourceManager getInstance();
	
        public Context getApplicationContext();
	
        public void setApplicationContext(final Context context);
	
        public ApplicationDescriptor getApplicationDescriptor();
	
        public void setApplicationDescriptor(final ApplicationDescriptor applicationDescriptor);
	
        public Iterator<String> getDatabaseDescriptorPaths();
	
        public DatabaseDescriptor getDatabaseDescriptorBasedOnPath(final String databaseDescriptorPath);
	
        public DatabaseDescriptor getDatabaseDescriptorBasedOnName(final String databaseDescriptorName);
	
        public Iterator<DatabaseDescriptor> getDatabaseDescriptors();
	
        public DatabaseDescriptor getDatabaseDescriptorBasedOnClassName(final String className);
	
        public String getDatabaseDescriptorNameBasedOnClassName(final String className);
	
        public DatabaseDescriptor getDatabaseDescriptorBasedOnTableName(final String tableName);
	
        public String getDatabaseDescriptorNameBasedOnTableName(final String tableName);
	
        public DatabaseMappingDescriptor getDatabaseMappingDescriptorBasedOnClassName(final String className);
	
        public DatabaseMappingDescriptor getDatabaseMappingDescriptorBasedOnTableName(final String tableName);
	
        public Iterator<DatabaseMappingDescriptor> getDatabaseMappingDescriptors();
	
        public DatabaseMappingDescriptor requiredDatabaseMappingDescriptorBasedOnClassName(final String className);
	
        public DatabaseBundle getDatabaseBundle(final String databaseName);
	
        public Iterator<DatabaseBundle> getDatabaseBundles();
	
        public void removeDatabaseBundle(final String databaseDescriptorName);
	
        public ISiminovEvents getSiminovEventHandler();
	
        public IDatabaseEvents getDatabaseEventHandler();
    }
```


#### iOS: Resource Manager

```objective-c

    @interface SICResourceManager : NSObject {
    }

    + (SICResourceManager*)getInstance;

    - (SICApplicationDescriptor *)getApplicationDescriptor;

    - (void)setApplicationDescriptor:(SICApplicationDescriptor * const)applicationDescriptorInstance;

    - (NSEnumerator *)getDatabaseDescriptorPaths;

    - (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnPath:(NSString *)databaseDescriptorPath;

    - (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnName:(NSString *)databaseDescriptorName;

    - (NSEnumerator *)getDatabaseDescriptors;

    - (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnClassName:(NSString *)className;

    - (SICDatabaseDescriptor *)getDatabaseDescriptorBasedOnTableName:(NSString *)tableName;

    - (NSString *)getDatabaseDescriptorNameBasedOnTableName:(NSString *)tableName;

    - (SICDatabaseMappingDescriptor *)getDatabaseMappingDescriptorBasedOnClassName:(NSString * const)className;

    - (SICDatabaseMappingDescriptor *)getDatabaseMappingDescriptorBasedOnTableName:(NSString * const)tableName;

    - (NSEnumerator *)getDatabaseMappingDescriptors;

    - (SICDatabaseMappingDescriptor *)requiredDatabaseMappingDescriptorBasedOnClassName:(NSString * const)className;

    - (SICDatabaseBundle *)getDatabaseBundle:(NSString * const)databaseName;

    - (NSEnumerator *)getDatabaseBundles;

    - (void)removeDatabaseBundle:(NSString * const)databaseDescriptorName;

    - (id<SICISiminovEvents>)getSiminovEventHandler;

    - (id<SICIDatabaseEvents>)getDatabaseEventHandler;

    @end

```

#### Windows: Resource Manager


```c#

    public class ResourceManager 
    {

        static ResourceManager GetInstance();
	
        ApplicationDescriptor GetApplicationDescriptor();
	
        void SetApplicationDescriptor(ApplicationDescriptor applicationDescriptor);
	
        IEnumerator<String> GetDatabaseDescriptorPaths();
	
        DatabaseDescriptor GetDatabaseDescriptorBasedOnPath(String databaseDescriptorPath);
	
        DatabaseDescriptor GetDatabaseDescriptorBasedOnName(String databaseDescriptorName);
	
        IEnumerator<DatabaseDescriptor> GetDatabaseDescriptors();
	
        DatabaseDescriptor GetDatabaseDescriptorBasedOnClassName(String className);
	
        String GetDatabaseDescriptorNameBasedOnClassName(String className);
	
        DatabaseDescriptor GetDatabaseDescriptorBasedOnTableName(String tableName);
	
        String GetDatabaseDescriptorNameBasedOnTableName(String tableName);
	
        DatabaseMappingDescriptor GetDatabaseMappingDescriptorBasedOnClassName(String className);
	
        DatabaseMappingDescriptor GetDatabaseMappingDescriptorBasedOnTableName(String tableName);
	
        IEnumerator<DatabaseMappingDescriptor> GetDatabaseMappingDescriptors();
	
        DatabaseMappingDescriptor RequiredDatabaseMappingDescriptorBasedOnClassName(String className);
	
        DatabaseBundle GetDatabaseBundle(String databaseName);
	
        IEnumerator<DatabaseBundle> GetDatabaseBundles();
	
        void RemoveDatabaseBundle(String databaseDescriptorName);
	
        ISiminovEvents GetSiminovEventHandler();
	
        IDatabaseEvents GetDatabaseEventHandler();
    }

```
