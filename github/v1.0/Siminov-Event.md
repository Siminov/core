Core provides event handler which automatically gets triggered when any action happen in framework. Application have to provide implementation for event notifier and register them with Core.

#### Android Sample: Event Handler

```java

    public interface ISiminovEvents {

        public void onFirstTimeSiminovInitialized();

        public void onSiminovInitialized();

        public void onSiminovStopped();
    }
```

#### iOS Sample: Event Handler

```objective-c

    @protocol SICISiminovEvents <NSObject>

    - (void)onFirstTimeSiminovInitialized;

    - (void)onSiminovInitialized;

    - (void)onSiminovStopped;

    @end

```

#### Windows Sample: Event Handler

```c#

    public interface ISiminovEvents 
    {

        void OnFirstTimeSiminovInitialized();

        void OnSiminovInitialized();

        void OnSiminovStopped();
    }

```

###### 1. On First Time Siminov Initialized - [Android:onFirstTimSiminovInitialized | iOS:onFirstTimSiminovInitialized | Windows:OnFirstTimSiminovInitialized]

It is triggered when core is initialized for first time. In this you can perform tasks which are related to initialization of things only first time of application starts.

- Example: Preparing initial data for application, which is required by application in its life
time, Since it is to be done only once, therefore we will use this API:

#### Android Sample: First Time Siminov Initialized 

```java

    public void onFirstTimeSiminovInitialized() {
        new DatabaseUtils().prepareData();
    }

```

#### iOS Sample: First Time Siminov Initialized

```objective-c

    - (void)onFirstTimeSiminovInitialized {
        [DatabaseUtils prepareData];
    }

```

#### Windows Sample: First Time Siminov Initialized

```c#

    public void OnFirstTimeSiminovInitialized() 
    {
        new DatabaseUtils().PrepareData();
    }

```


> **Note**
>
> - This API will be triggered only once when Core is initialized first time.


###### 2. On Siminov Initialized - [Android:onSiminovInitialized | iOS:onSiminovInitialized | Windows:OnSiminovInitialized]
It is triggered whenever Core is initialized.

> **Note**
>
> - This doesn't gets triggered when Core is first time initialized, instead of this API will be triggered.


###### 3. On Siminov Stopped - [Android:onSiminovStopped | iOS:onSiminovStopped | Windows:OnSiminovStopped]
It is triggered when Core is shutdown.

## Android Sample: ISiminov Event Handler

***

![Android Sample: ISiminov Event Handler] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/isiminov_core_application_sample_example.png "Android Sample: ISiminov Event Handler")

***

## iOS Sample: SICISiminov Event Handler

***

![iOS Sample: SICISiminov Event Handler] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/isiminov_core_application_sample_example.png "iOS Sample: SICISiminov Event Handler")

***

## Windows Sample: ISiminov Event Handler

***

![Windows Sample: ISiminov Event Handler] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/isiminov_core_application_sample_example.png "Windows Sample: ISiminov Event Handler")

***
