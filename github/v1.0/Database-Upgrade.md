Over time the requirement of the database within the application may change, this is almost inevitable if your application is in active development and you're constantly adding new features. Properly upgrading the database in application is important, because if something goes wrong application will have unexpected behaviour (such as crashing) or app may may loose its all data and have to start over.

**SQLite Limitation**: SQLite supports a limited subset of ALTER TABLE. The ALTER TABLE command in SQLite allows the user to rename a tablet or to add a new column to an existing table. It is not possible to rename a column, remove a column, or add or remove constraints from a table. But you can alter table column datatype or other property by the following steps:

- BEGIN TRANSACTION
- CREATE TEMPORARY TABLE t1_backup(a, b);
- INSERT INTO t1_backup SELECT a, b FROM t1;
- DROP TABLE
- CREATE TABLE t1(a, b);
- INSERT INTO t1 SELECT a, b FROM t1_backup;
- DROP TABLE t1_backup;
- COMMIT

For more detail you can refer the [SQLite Alter Table](http://www.sqlite.org/lang_altertable.html)

### Siminov Database Upgradation
Siminov provides easy way to upgrade application database. It only support adding new table to existing database, adding a new column to existing table, and deleting existing table.

```xml

              <!-- Example: DatabaseDescriptor.si.xml Of Siminov Core Sample -->

    <database-descriptor>

        <property name="database_name">SIMINOV-CORE-SAMPLE</property>
        <property name="description">Siminov Core Sample Database Config</property>
        <property name="version">1</property>
        <property name="transaction_safe">true</property>
        <property name="external_storage">false</property>
		
        <database-mapping-descriptors>
            <database-mapping-descriptor path="Liquor-Mappings/Liquor.si.xml" />
            <database-mapping-descriptor path="Liquor-Mappings/LiquorBrand.si.xml" />
        </database-mapping-descriptors>
	
    </database-descriptor>

```


> **Note**
>
> - Database Version should not contain decimal value.
