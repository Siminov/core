## Making Transaction Thread Safe

By default any transaction executed on database is not thread safe, android provides API to make all transaction thread-safe by using locks around critical sections. This is pretty expensive, so if you know that your DB will only be used by a single thread then you should not use this in your application.

#### Android API: Transaction Thread Safe

```java

    public void setTransactionSafe(boolean isTransactionSafe);

```

#### iOS API: Transaction Thread Safe

```objective-c

    - (void)setTransactionSafe:(bool)isTransactionSafe;

```

#### Windows API: Transaction Thread Safe

```c#

    public void SetTransactionSafe(bool isTransactionSafe);

```

Configuring transaction thread-safe in siminov. To enable/disable transaction thread-safe in Core you have to use property is locking required defined in DatabaseDescriptor.si.xml file.


###### Android Sample: Transaction Thread Safe
***

![Android Sample: Transaction Thread Safe] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/siminov_core_sample_application_transaction_safe_example.png "Android Sample: Transaction Thread Safe")

***


###### iOS Sample: Transaction Thread Safe
***

![iOS Sample: Transaction Thread Safe] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/siminov_core_sample_application_transaction_safe_example.png "iOS Sample: Transaction Thread Safe")

***

###### Windows Sample: Transaction Thread Safe
***

![Windows Sample: Transaction Thread Safe] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/siminov_core_sample_application_transaction_safe_example.png "Windows Sample: Transaction Thread Safe")

***

