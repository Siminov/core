/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2015] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/



using Siminov.Core.Utils;
using Siminov.Core.Database.Design;
using Siminov.Core.Events;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using Siminov.Core.Resource;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace Siminov.Core.Database
{

    /// <summary>
    /// It provides utility methods to deal with database. 
    /// It has methods to create, delete, and perform other common database management tasks.
    /// </summary>
    public abstract class DatabaseHelper : Constants
    {
        private static ResourceManager resourceManager = ResourceManager.GetInstance();

        private static Semaphore transactionSafe = new Semaphore(1, 1);

        /// <summary>
        /// It is used to create instance of IDatabase implementation
        /// </summary>
        /// <param name="databaseDescriptor">Database Bundle instance object</param>
        /// <returns>DatabaseBundle</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException"></exception>
        public static DatabaseBundle CreateDatabase(DatabaseDescriptor databaseDescriptor)
        {
            return DatabaseFactory.GetInstance().GetDatabaseBundle(databaseDescriptor);
        }



        /// <summary>
        /// Upgrade Existing Database
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">throws If any exception thrown</exception>
        public static void UpgradeDatabase(DatabaseDescriptor databaseDescriptor)
        {

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "UpgradeDatabase", "No Database Instance Found For, DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DatabaseException(typeof(DatabaseHelper).FullName, "UpgradeDatabase", "No Database Instance Found For, DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }


            /*
             * Fetch Database Version
             */
            String fetchDatabaseVersionQuery = queryBuilder.FormFetchDatabaseVersionQuery(null);
            Log.Log.Debug(typeof(DatabaseHelper).FullName, "UpgradeDatabase", "Fetch Database Version Query: " + fetchDatabaseVersionQuery);


            double currentDatabaseVersion = 0;
            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(databaseDescriptor, null, fetchDatabaseVersionQuery);

            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        currentDatabaseVersion = Double.Parse((String)value);
                    }
                    else if (value.GetType().FullName.Equals(typeof(Double).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        currentDatabaseVersion = (double)value;
                    }
                }
            }


            if (currentDatabaseVersion == databaseDescriptor.GetVersion())
            {
                return;
            }

            ICollection<EntityDescriptor> allEntityDescriptor = new List<EntityDescriptor>();
            IEnumerator<EntityDescriptor> allEntityDescriptorIterator = resourceManager.GetEntityDescriptors();
            while (allEntityDescriptorIterator.MoveNext())
            {
                allEntityDescriptor.Add(allEntityDescriptorIterator.Current);
            }



            ICollection<String> tableNames = new List<String>();

            IEnumerator<EntityDescriptor> entityDescriptors = databaseDescriptor.GetEntityDescriptors();

            /*
             * Get Table Names
             */
            String fetchTableNamesQuery = queryBuilder.FormTableNames(null);
            Log.Log.Debug(typeof(DatabaseHelper).FullName, "UpgradeDatabase", "Fetch Table Names, " + fetchTableNamesQuery);

            datas = database.ExecuteSelectQuery(databaseDescriptor, null, fetchTableNamesQuery);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                IEnumerator<String> keys = data.Keys.GetEnumerator();

                while (keys.MoveNext())
                {
                    String key = keys.Current;

                    if (key.Equals(Constants.FORM_TABLE_NAMES_NAME, StringComparison.OrdinalIgnoreCase))
                    {
                        tableNames.Add((String)data[key]);
                    }
                }
            }


            /*
             * Create Or Upgrade Table
             */
            while (entityDescriptors.MoveNext())
            {
                EntityDescriptor entityDescriptor = entityDescriptors.Current;

                bool contain = false;
                foreach (String tableName in tableNames)
                {

                    if (tableName.Equals(entityDescriptor.GetTableName(), StringComparison.OrdinalIgnoreCase))
                    {
                        contain = true;
                        break;
                    }
                }

                if (contain)
                {
                    DatabaseHelper.UpgradeTable(entityDescriptor);
                }
                else
                {
                    DatabaseHelper.CreateTable(entityDescriptor);
                }
            }


            /*
             * Drop Table
             */
            foreach (String tableName in tableNames)
            {
                if (tableName.Equals(Constants.ANDROID_METADATA_TABLE_NAME, StringComparison.OrdinalIgnoreCase))
                {
                    continue;
                }


                bool contain = false;

                foreach (EntityDescriptor entityDescriptor in allEntityDescriptor)
                {

                    if (tableName.Equals(entityDescriptor.GetTableName(), StringComparison.OrdinalIgnoreCase))
                    {
                        contain = true;
                        break;
                    }
                }

                if (!contain)
                {

                    IDictionary<String, Object> childParameters = new Dictionary<String, Object>();
                    childParameters.Add(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);

                    database.ExecuteQuery(databaseDescriptor, null, queryBuilder.FormDropTableQuery(childParameters));
                }
            }




            /*
             * Update Database Version
             */
            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER, databaseDescriptor.GetVersion());

            String updateDatabaseVersionQuery = queryBuilder.FormUpdateDatabaseVersionQuery(parameters);
            Log.Log.Debug(typeof(DatabaseHelper).FullName, "UpgradeDatabase", "Update Database Version Query: " + updateDatabaseVersionQuery);

            database.ExecuteQuery(databaseDescriptor, null, updateDatabaseVersionQuery);
        }


        /// <summary>
        /// Upgrade Table
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object related to table</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any exception thrown while upgrading table</exception>
        public static void UpgradeTable(EntityDescriptor entityDescriptor)
        {

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "UpgradeTable", "No Database Instance Found For, TABLE-NAME: " + entityDescriptor.GetTableName());
                throw new DatabaseException(typeof(DatabaseHelper).FullName, "UpgradeTable", "No Database Instance Found For, TABLE-NAME: " + entityDescriptor.GetTableName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());

            String tableInfoQuery = queryBuilder.FormTableInfoQuery(parameters);
            Log.Log.Debug(typeof(DatabaseHelper).FullName, "UpgradeTable", "Table Info Query: " + tableInfoQuery);


            ICollection<EntityDescriptor.Attribute> newAttributes = new List<EntityDescriptor.Attribute>();
            ICollection<String> oldAttributes = new List<String>();
            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();


            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(null, null, tableInfoQuery);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                IEnumerator<String> keys = data.Keys.GetEnumerator();

                while (keys.MoveNext())
                {
                    String key = keys.Current;

                    if (key.Equals(Constants.FORM_TABLE_INFO_QUERY_NAME, StringComparison.OrdinalIgnoreCase))
                    {
                        oldAttributes.Add((String)data[key]);
                    }
                }
            }


            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                bool contain = false;
                foreach (String oldColumn in oldAttributes)
                {

                    if (oldColumn.Equals(attribute.GetColumnName()))
                    {
                        contain = true;
                        break;
                    }
                }

                if (!contain)
                {
                    newAttributes.Add(attribute);
                }
            }


            foreach (EntityDescriptor.Attribute column in newAttributes)
            {

                String columnName = column.GetColumnName();

                parameters = new Dictionary<String, Object>();
                parameters.Add(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
                parameters.Add(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER, columnName);

                String addColumnQuery = queryBuilder.FormAlterAddColumnQuery(parameters);
                Log.Log.Debug(typeof(DatabaseHelper).FullName, "UpgradeTable", "Add New Column Query: " + addColumnQuery);


                database.ExecuteQuery(null, null, addColumnQuery);
            }
        }


        /// <summary>
        /// Is used to create a new table in an database
        /// <para>
        /// Using SIMINOV there are three ways to create table in database.
        /// Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
        /// SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.
        /// <code>
        ///        <entity-descriptor>
        ///        
        ///            <property name="table_name">LIQUOR</property>
        ///            <property name="class_name">Siminov.Core.Sample.Model.Liquor</property>
        ///
        ///            <attributes>
        ///		
        ///                <attribute>
        ///                    <property name="variable_name">liquorType</property>
        ///                    <property name="column_name">LIQUOR_TYPE</property>
        ///                    <property name="type">System.String</property>
        ///                    <property name="primary_key">true</property>
        ///                    <property name="not_null">true</property>
        ///                    <property name="unique">true</property>
        ///                </attribute>		
        ///
        ///                <attribute>
        ///                    <property name="variable_name">description</property>
        ///                    <property name="column_name">DESCRIPTION</property>
        ///                    <property name="type">System.String</property>
        ///                </attribute>
        ///
        ///                <attribute>
        ///                    <property name="variable_name">history</property>
        ///                    <property name="column_name">HISTORY</property>
        ///                    <property name="type">System.String</property>
        ///                </attribute>
        ///
        ///                <attribute>
        ///                    <property name="variable_name">link</property>
        ///                    <property name="column_name">LINK</property>
        ///                    <property name="type">System.String</property>
        ///                    <property name="default">www.wikipedia.org</property>
        ///                </attribute>
        ///
        ///                <attribute>
        ///                    <property name="variable_name">alcholContent</property>
        ///                    <property name="column_name">ALCHOL_CONTENT</property>
        ///                    <property name="type">System.String</property>
        ///                </attribute>
        ///
        ///                <index>
        ///                    <property name="name">LIQUOR_INDEX_BASED_ON_LINK</property>
        ///                    <property name="unique">true</property>
        ///                    <property name="type">System.String</property>
        ///                    <property name="column">HISTORY</property>
        ///                </index>
        ///
        ///                <relationships>
        ///
        ///                    <relationship ="" ="" ="" ="">
        ///                        <property name="type">one-to-many</property>
        ///                        <property name="refer">liquorBrands</property>
        ///                        <property name="refer_to">Siminov.Core.Sample.Model.LiquorBrand</property>
        ///                        <property name="on_update">cascade</property>
        ///                        <property name="on_delete">cascade</property>
        ///                        <property name="load">true</property>
        ///                    </one-to-many>		
        ///		    
        ///                </relationships>
        ///				
        ///        </entity-descriptor>		
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptors">Entity Descriptor objects which defines the structure of each table</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create table in SQLite</exception>
        public static void CreateTables(IEnumerator<EntityDescriptor> entityDescriptors)
        {

            while (entityDescriptors.MoveNext())
            {
                CreateTable(entityDescriptors.Current);
            }
        }

        /// <summary>
        /// Is used to create a new table in an database
        /// Manually creating table structure using Entity Descriptor mapped class. 
        /// Example:
        /// <para>
        /// <code>
        ///        Liquor liquor = new Liquor();
        ///
        ///        try {
        ///            Database.CreateTables(liquor.GetEntityDescriptor());
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which defines the structure of table</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create table in SQLite</exception>
        public static void CreateTable(EntityDescriptor entityDescriptor)
        {

            /*
             * 1. Get IDatabase with respect to current entity descriptor class name.
             * 2. Get Table Name, and all columns.
             * 3. Get all attributes and properties from entity descriptor.
             * 		LIKE(COLUMN NAMES, COLUMN TYPES, PRIMARY KEYS, UNIQUE's, NOT NULL, DEFAULT VALUES, CHECKS, ).
             * 
             * 4. If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined, 
             * 		because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
             * 		Else get foreign keys.
             * 
             * 5. Call QueryBuilder.formCreateTableQuery, get query to create table.
             * 	After forming create table query call executeQuery method to create table in database.
             *
             * 6. Create all triggers.
             * 7. Create Index for table if its defined.
             *		Get all attributes and properties of index.
             * 		LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
             * 		After forming index query call executeQuery method to create index.
             * 
             */

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();
            IDataTypeHandler dataTypeHandler = databaseBundle.GetDataTypeHandler();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "CreateTable", "No Database Instance Found For ENITTY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "CreateTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            String tableName = entityDescriptor.GetTableName();

            /*
             * Get all attributes and properties from entity descriptor. 
             * LIKE(COLUMN NAMES, COLUMN TYPES, DEFAULT VALUES, CHECKS, NOT NULL, PRIMARY KEYS, UNIQUE's ).
             */
            ICollection<String> columnNames = new LinkedList<String>();
            ICollection<String> columnTypes = new LinkedList<String>();

            ICollection<String> defaultValues = new LinkedList<String>();
            ICollection<String> checks = new LinkedList<String>();

            ICollection<Boolean> isNotNull = new LinkedList<Boolean>();

            ICollection<String> primaryKeys = new LinkedList<String>();
            ICollection<String> uniqueKeys = new LinkedList<String>();

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                columnNames.Add(attribute.GetColumnName());
                columnTypes.Add(dataTypeHandler.Convert(attribute.GetType()));
                isNotNull.Add(attribute.IsNotNull());

                defaultValues.Add(attribute.GetDefaultValue());
                checks.Add(attribute.GetCheck());

                bool isPrimary = attribute.IsPrimaryKey();
                bool isUnique = attribute.IsUnique();

                if (isPrimary)
                {
                    primaryKeys.Add(attribute.GetColumnName());
                }

                if (isUnique)
                {
                    uniqueKeys.Add(attribute.GetColumnName());
                }
            }


            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                ICollection<EntityDescriptor.Attribute> foreignAttributes = GetForeignKeys(referedEntityDescriptor);
                IEnumerator<EntityDescriptor.Attribute> foreignAttributesIterator = foreignAttributes.GetEnumerator();

                while (foreignAttributesIterator.MoveNext())
                {
                    EntityDescriptor.Attribute foreignAttribute = foreignAttributesIterator.Current;

                    columnNames.Add(foreignAttribute.GetColumnName());
                    columnTypes.Add(dataTypeHandler.Convert(foreignAttribute.GetType()));
                    isNotNull.Add(foreignAttribute.IsNotNull());

                    defaultValues.Add(foreignAttribute.GetDefaultValue());
                    checks.Add(foreignAttribute.GetCheck());

                    bool isPrimary = foreignAttribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        primaryKeys.Add(foreignAttribute.GetColumnName());
                    }

                    bool isUnique = foreignAttribute.IsUnique();
                    if (isUnique)
                    {
                        uniqueKeys.Add(foreignAttribute.GetColumnName());
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        columnNames.Add(attribute.GetColumnName());
                        columnTypes.Add(dataTypeHandler.Convert(attribute.GetType()));
                        isNotNull.Add(attribute.IsNotNull());

                        defaultValues.Add(attribute.GetDefaultValue());
                        checks.Add(attribute.GetCheck());

                        if (isPrimary)
                        {
                            primaryKeys.Add(attribute.GetColumnName());
                        }

                        bool isUnique = attribute.IsUnique();
                        if (isUnique)
                        {
                            uniqueKeys.Add(attribute.GetColumnName());
                        }
                    }
                }
            }

            /*
             * If current version of OS is lower then 8 (FROYO) then we have to create triggers for all foreign keys defined, 
             * because Android OS Version lower then 8 (FROYO) does not support FOREIGN KEY SYNTAX.
             * Else get foreign keys.
             */
            String foreignKeys = "";

            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER, entityDescriptor);

            foreignKeys = queryBuilder.FormForeignKeyQuery(parameters);


            /*
             * Call QueryBuilder.formCreateTableQuery, get query to create table.
             * After forming create table query call executeQuery method to create table in database.
             */

            parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER, columnNames.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER, columnTypes.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER, defaultValues.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER, checks.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER, primaryKeys.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER, isNotNull.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER, uniqueKeys.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER, foreignKeys);

            String query = queryBuilder.FormCreateTableQuery(parameters);
            database.ExecuteQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);


            /*
             * Create Index for table if its defined.
             */
            IEnumerator<EntityDescriptor.Index> indexes = entityDescriptor.GetIndexes();
            while (indexes.MoveNext())
            {

                /*
                 * Get all attributes and properties of index.
                 * LIKE(INDEX NAME, IS UNIQUE INDEX, COLUMN NAMES).
                 * 
                 * After forming index query call executeQuery method to create index.
                 */

                CreateIndex(entityDescriptor, indexes.Current);
            }

            IDatabaseEvents databaseEventHandler = resourceManager.GetDatabaseEventHandler();
            if (databaseEventHandler != null)
            {
                databaseEventHandler.OnTableCreated(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor);
            }
        }

        /// <summary>
        /// It drop's the table from database based on entity descriptor
        /// <para>
        /// Drop the Liquor table
        /// <code>
        ///        EntityDescriptor entityDescriptor = new Liquor().GetEntityDescriptor();
        ///	
        ///        try {
        ///            Database.DropTable(entityDescriptor);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which defines the structure of table</param>
        /// <excpetion cref="Siminov.Core.Exception.DatabaseException">If not able to drop table</excpetion>
        public static void DropTable(EntityDescriptor entityDescriptor)
        {

            Siminov.IsActive();


            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "dropTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "dropTable", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            String tableName = entityDescriptor.GetTableName();

            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER, tableName);

            database.ExecuteQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, queryBuilder.FormDropTableQuery(parameters));

            IDatabaseEvents databaseEventHandler = resourceManager.GetDatabaseEventHandler();
            if (databaseEventHandler != null)
            {
                databaseEventHandler.OnTableDropped(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor);
            }
        }

        /// <summary>
        /// Is used to create a new index on a table in database
        /// <para>
        /// Create Index On Liquor table
        /// <code>
        ///        EntityDescriptor.Index indexOnLiquor = entityDescriptor.new Index();
        ///        indexOnLiquor.SetName("LIQUOR_INDEX_BASED_ON_LINK");
        ///        indexOnLiquor.SetUnique(true);
        ///	
        ///        //Add Columns on which we need index.
        ///        indexOnLiquor.AddColumn("LINK");
        ///
        ///        EntityDescriptor entityDescriptor = new Liquor().GetEntityDescriptor();
        ///
        ///        try {
        ///            Database.CreateIndex(entityDescriptor, indexOnLiquor);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which defines the structure of table</param>
        /// <param name="index">Index object which defines the structure of index needs to create</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create index on table</exception>
        public static void CreateIndex(EntityDescriptor entityDescriptor, EntityDescriptor.Index index)
        {

            String indexName = index.GetName();
            IEnumerator<String> columnNames = index.GetColumns();
            bool isUnique = index.IsUnique();

            CreateIndex(entityDescriptor, indexName, columnNames, isUnique);
        }


        /// <summary>
        /// Is used to create a new index on a table in database
        /// <para>
        /// Create Index On Liquor table
        /// <code>
        ///        String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
        ///        boolean isUnique = true;
        ///
        ///        Collection<String> columnNames = new ArrayList<String>();
        ///        columnNames.Add("LINK");
        ///
        ///        try {
        ///            new Liquor().CreateIndex(indexName, columnNames.GetEnumerator(), isUnique);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which defines the structure of table</param>
        /// <param name="indexName">Name of index</param>
        /// <param name="columnNames">Iterator over column names</param>
        /// <param name="isUnique">true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create index on table</exception>
        public static void CreateIndex(EntityDescriptor entityDescriptor, String indexName, IEnumerator<String> columnNames, bool isUnique)
        {

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "CreateIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "CreateIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            ICollection<String> columnNamesCollection = new List<String>();
            while (columnNames.MoveNext())
            {
                columnNamesCollection.Add(columnNames.Current);
            }



            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);
            parameters.Add(IQueryBuilder.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER, columnNamesCollection.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER, isUnique);


            String query = queryBuilder.FormCreateIndexQuery(parameters);
            database.ExecuteQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);

            IDatabaseEvents databaseEventHandler = resourceManager.GetDatabaseEventHandler();
            if (databaseEventHandler != null)
            {
                EntityDescriptor.Index index = new EntityDescriptor.Index();
                index.SetName(indexName);
                index.SetUnique(isUnique);

                IEnumerator<String> columnNamesIterator = columnNamesCollection.GetEnumerator();
                while (columnNamesIterator.MoveNext())
                {
                    index.AddColumn(columnNamesIterator.Current);
                }

                databaseEventHandler.OnIndexCreated(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, index);
            }
        }

        /// <summary>
        /// Is used to drop a index on a table in database
        /// <para>
        /// Create Index On Liquor table
        /// <code>
        ///        String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
        ///        EntityDescriptor entityDescriptor = new Liquor().GetEntityDescriptor();
        ///	
        ///        try {
        ///            Database.DropIndex(entityDescriptor, indexName);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor object which defines the structure of table</param>
        /// <param name="indexName">Name of a index needs to be drop</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop index on table</exception>
        public static void DropIndex(EntityDescriptor entityDescriptor, String indexName)
        {
            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "DropIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + " INDEX-NAME: " + indexName);
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "DropIndex", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + " INDEX-NAME: " + indexName);
            }



            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER, indexName);


            String query = queryBuilder.FormDropIndexQuery(parameters);
            database.ExecuteQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);

            IDatabaseEvents databaseEventHandler = resourceManager.GetDatabaseEventHandler();
            if (databaseEventHandler != null)
            {
                databaseEventHandler.OnIndexDropped(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, entityDescriptor.GetIndex(indexName));
            }
        }


        /// <summary>
        /// It drop's the whole database based on database-descriptor
        /// <para>
        /// Drop the Liquor table
        /// <code>
        ///        DatabaseDescriptor databaseDescriptor = new Liquor().GetDatabaseDescriptor();
        ///    
        ///        try {
        ///            Database.DropDatabase(databaseDescriptor);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor object which defines the structure of table</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop database</exception>
        public static void DropDatabase(DatabaseDescriptor databaseDescriptor)
        {
            Siminov.IsActive();

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "DropDatabase", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "DropDatabase", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }

            String databasePath = new DatabaseUtils().GetDatabasePath(databaseDescriptor);


            database.Close(databaseDescriptor);

            String databaseName = databaseDescriptor.GetDatabaseName();
            if (!databaseName.EndsWith(".db"))
            {
                databaseName = databaseName + ".db";
            }
            FileUtils.DeleteFile(databasePath, databaseName, FileUtils.LOCAL_FOLDER);

            resourceManager.RemoveDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseEvents databaseEventHandler = resourceManager.GetDatabaseEventHandler();
            if (databaseEventHandler != null)
            {
                databaseEventHandler.OnDatabaseDropped(databaseDescriptor);
            }
        }

        /// <summary>
        /// Begins a transaction in EXCLUSIVE mode.
        /// <para>
        /// Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
        /// The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
        /// Example: Make Beer Object
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        ///
        ///        DatabaseDescriptor databaseDescriptor = beer.GetDatabaseDescriptor();
        /// 
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        ///	
        ///            beer.Save();
        /// 
        ///            Database.CommitTransaction(databaseDescriptor);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        } finally {
        ///            Database.EndTransaction(databaseDescriptor);
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor"></param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If beginTransaction does not starts</exception>
        public static void BeginTransaction(DatabaseDescriptor databaseDescriptor)
        {

            Siminov.IsActive();

            if(databaseDescriptor.IsTransactionSafe())
            {
                transactionSafe.WaitOne();
            }

            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "BeginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "BeginTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }

            database.ExecuteMethod(Constants.SQLITE_DATABASE_BEGIN_TRANSACTION, null);
        }

        /// <summary>
        /// Marks the current transaction as successful
        /// <para>
        /// Finally it will End a transaction
        /// Example: Make Beer Object
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        ///
        ///        DatabaseDescriptor databaseDescriptor = beer.GetDatabaseDescriptor();
        /// 
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        ///
        ///            beer.Save();
        /// 
        ///            Database.CommitTransaction(databaseDescriptor);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        } finally {
        ///            Database.EndTransaction(databaseDescriptor);
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        /// <exception cref="Siminiov.Core.Exception.DatabaseException">If not able to commit the transaction</exception>
        public static void CommitTransaction(DatabaseDescriptor databaseDescriptor)
        {
            Siminov.IsActive();

            if(databaseDescriptor.IsTransactionSafe())
            {
                transactionSafe.Release();
            }


            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "CommitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "CommitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }

            database.ExecuteMethod(Constants.SQLITE_DATABASE_COMMIT_TRANSACTION, null);
        }

        /// <summary>
        /// End the current transaction
        /// <para>
        /// Example:
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        ///
        ///        DatabaseDescriptor databaseDescriptor = beer.GetDatabaseDescriptor();
        /// 
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        ///
        ///            beer.Save();
        /// 
        ///            Database.CommitTransaction(databaseDescriptor);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        } finally {
        ///            Database.EndTransaction(databaseDescriptor);
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseDescriptor">Database Descriptor Object</param>
        public static void EndTransaction(DatabaseDescriptor databaseDescriptor)
        {
            Siminov.IsActive();

            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "CommitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "CommitTransaction", "No Database Instance Found For DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }

            try
            {
                database.ExecuteMethod(Constants.SQLITE_DATABASE_END_TRANSACTION, null);
            }
            catch (DatabaseException databaseException)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "CommitTransaction", "DatabaseException caught while executing end transaction method, " + databaseException.GetMessage());
            }
        }


        internal static Object[] Select(EntityDescriptor entityDescriptor, bool distinct, String whereClause, IEnumerator<String> columnNames, IEnumerator<String> groupBy, String having, IEnumerator<String> orderBy, String whichOrderBy, String limit)
        {
            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             * 2. Traverse group by's and form a single string.
             * 3. Traverse order by'z and form a single string.
             * 4. Pass all parameters to executeFetchQuery and get cursor.
             * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
             * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
             */

            Siminov.IsActive();

            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            /*
             * 4. Pass all parameters to executeFetchQuery and get cursor.
             */

            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER, distinct);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER, columnNames);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER, groupBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER, having);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER, orderBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER, whichOrderBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER, limit);


            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, queryBuilder.FormSelectQuery(parameters));
            List<Dictionary<String, Object>> datasBundle = new List<Dictionary<String, Object>>();
            while (datas.MoveNext())
            {
                datasBundle.Add((Dictionary<String, Object>)datas.Current);
            }

            IEnumerator<Object> tuples = ParseAndInflateData(entityDescriptor, datasBundle.GetEnumerator());
            datas = datasBundle.GetEnumerator();

            /*
             * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
             */

            ICollection<Object> tuplesCollection = new LinkedList<Object>();
            while (tuples.MoveNext() && datas.MoveNext())
            {
                Object tuple = tuples.Current;
                IDictionary<String, Object> data = datas.Current;

                tuplesCollection.Add(tuple);

                ProcessOneToOneRelationship(tuple);
                ProcessOneToManyRelationship(tuple);

                ProcessManyToOneRelationship(tuple, data);
                ProcessManyToManyRelationship(tuple, data);

            }


            Type classObject = null;
            try
            {
                classObject = ClassUtils.CreateClass(entityDescriptor.GetClassName());
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
                throw new DatabaseException(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
            }

            Object returnType = Array.CreateInstance(classObject, tuplesCollection.Count);
            IEnumerator<Object> tuplesIterator = tuplesCollection.GetEnumerator();

            int index = 0;
            while (tuplesIterator.MoveNext())
            {
                ((Object[])returnType)[index++] = tuplesIterator.Current;
            }

            Object[] returnTypes = (Object[])returnType;
            return returnTypes;
        }

        static Object[] LazyFetch(EntityDescriptor entityDescriptor, bool distinct, String whereClause, IEnumerator<String> columnNames, IEnumerator<String> groupBy, String having, IEnumerator<String> orderBy, String whichOrderBy, String limit)
        {
            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             * 2. Traverse group by's and form a single string.
             * 3. Traverse order by'z and form a single string.
             * 4. Pass all parameters to executeFetchQuery and get cursor.
             * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
             * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
             */

            Siminov.IsActive();

            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "LazyFetch", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "LazyFetch", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            /*
             * 4. Pass all parameters to executeFetchQuery and get cursor.
             */

            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER, distinct);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER, columnNames);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER, groupBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER, having);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER, orderBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER, whichOrderBy);
            parameters.Add(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER, limit);


            String query = queryBuilder.FormSelectQuery(parameters);
            IEnumerator<Object> tuples = ParseAndInflateData(entityDescriptor, database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query));

            /*
             * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
             */

            ICollection<Object> tuplesCollection = new LinkedList<Object>();
            while (tuples.MoveNext())
            {
                Object tuple = tuples.Current;
                tuplesCollection.Add(tuple);
            }

            Type classObject = null;
            try
            {
                classObject = ClassUtils.CreateClass(entityDescriptor.GetClassName());
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "LazyFetch", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
                throw new DatabaseException(typeof(DatabaseHelper).FullName, "LazyFetch", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
            }

            Object returnType = Array.CreateInstance(classObject, tuplesCollection.Count);
            IEnumerator<Object> tuplesIterator = tuplesCollection.GetEnumerator();

            int index = 0;
            while (tuplesIterator.MoveNext())
            {
                ((Object[])returnType)[index++] = tuplesIterator.Current;
            }

            Object[] returnTypes = (Object[])returnType;
            return returnTypes;
        }

        /// <summary>
        /// Returns all tuples based on manual query from mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        String query = "SELECT * FROM LIQUOR";
        ///	
        ///        Liquor[] liquors = null;
        ///        try {
        ///            liquors = new Liquor().Select(query);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <param name="query">Manual query on which tuples need to be fetched</param>
        /// <returns>Array Of Objects</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting tuples from a single table</exception>
        public static Object[] Select(Object model, String query)
        {
            Siminov.IsActive();

            /*
             * 1. Get entity descriptor object for mapped invoked class object.
             */

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(model.GetType().FullName);

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Select", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            /*
             * 4. Pass all parameters to executeFetchQuery and get cursor.
             */
            IEnumerator<Object> tuples = ParseAndInflateData(entityDescriptor, database.ExecuteSelectQuery(GetDatabaseDescriptor(model.GetType().FullName), entityDescriptor, query));

            /*
             * 5. Pass got cursor and mapped entity descriptor object for invoked class object, and pass it parseCursor method which will return all tuples in form of actual objects.
             */

            ICollection<Object> tuplesCollection = new LinkedList<Object>();
            while (tuples.MoveNext())
            {
                Object tuple = tuples.Current;
                tuplesCollection.Add(tuple);
            }

            Type classObject = null;
            try
            {
                classObject = Type.GetType(entityDescriptor.GetClassName());
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
                throw new DatabaseException(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
            }

            Object returnType = Array.CreateInstance(classObject, tuplesCollection.Count);
            IEnumerator<Object> tuplesIterator = tuplesCollection.GetEnumerator();

            int index = 0;
            while (tuplesIterator.MoveNext())
            {
                ((Object[])returnType)[index++] = tuplesIterator.Current;
            }

            Object[] returnTypes = (Object[])returnType;
            return returnTypes;
        }

        /// <summary>
        /// It adds a record to any single table in a relational database.
        /// <para>
        /// Example: Make Liquor Object
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        ///
        ///        try {
        ///            beer.Save();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public static void Save(Object model)
        {
            Siminov.IsActive();

            /*
             * 1. Get mapped entity descriptor object for object parameter class name.
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
             * 3. Using QueryBuilder form insert bind query.
             * 4. Pass query to executeBindQuery method for insertion.
             * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
             */

            if (model == null)
            {
                Log.Log.Debug(typeof(DatabaseHelper).FullName, "Save", "Invalid Object Found.");
                return;
            }

            /*
             * 1. Get mapped entity descriptor object for invoked class object.
             */

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(model.GetType().FullName);

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Save", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Save", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            /*
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, by parsing each fields.
             */
            String tableName = entityDescriptor.GetTableName();

            ICollection<String> columnNames = new LinkedList<String>();
            ICollection<Object> columnValues = new LinkedList<Object>();

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                try
                {
                    columnNames.Add(attribute.GetColumnName());
                    columnValues.Add(ClassUtils.GetValue(model, attribute.GetGetterMethodName()));
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "Save", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "Save", siminovException.GetMessage());
                }
            }

            ProcessManyToOneRelationship(model, columnNames, columnValues);
            ProcessManyToManyRelationship(model, columnNames, columnValues);


            /*
             * 3. Using QueryBuilder form insert bind query.
             */
            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER, tableName);
            parameters.Add(IQueryBuilder.FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER, columnNames.GetEnumerator());


            String query = queryBuilder.FormSaveBindQuery(parameters);


            /*
             * 4. Pass query to executeBindQuery method for insertion.
             */
            database.ExecuteBindQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query, columnValues.GetEnumerator());


            /*
             * 5. Check for relationship's if any, IF EXISTS: process it, ELSE: return all objects.
             */
            IEnumerator<EntityDescriptor.Relationship> relationships = entityDescriptor.GetRelationships();
            while (relationships.MoveNext())
            {
                EntityDescriptor.Relationship relationship = relationships.Current;

                bool isLoad = relationship.IsLoad();
                if (!isLoad)
                {
                    continue;
                }

                String relationshipType = relationship.GetRelationshipType();
                if (relationshipType == null || relationshipType.Length <= 0)
                {
                    continue;
                }

                if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE, StringComparison.OrdinalIgnoreCase))
                {
                    Object value = null;
                    try
                    {
                        value = ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Save", siminovException.GetMessage());
                    }


                    if (value == null)
                    {
                        continue;
                    }

                    SaveOrUpdate(value);
                }
                else if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    IEnumerator<Object> values = null;
                    try
                    {
                        values = (IEnumerator<Object>)ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Save", siminovException.GetMessage());
                    }


                    if (values == null)
                    {
                        continue;
                    }

                    while (values.MoveNext())
                    {
                        SaveOrUpdate(values.Current);
                    }
                }
                else if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    IEnumerator<Object> values = null;
                    try
                    {
                        values = (IEnumerator<Object>)ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Save", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Save", siminovException.GetMessage());
                    }


                    if (values == null)
                    {
                        continue;
                    }

                    while (values.MoveNext())
                    {
                        SaveOrUpdate(values.Current);
                    }
                }
            }
        }

        /// <summary>
        /// It updates a record to any single table in a relational database
        /// <para>
        /// Example: Make Beer Object
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        /// 
        ///        try {
        ///            beer.Update();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public static void Update(Object model)
        {
            Siminov.IsActive();

            /*
             * 1. Get mapped entity descriptor object for object parameter class name.
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
             * 3. Form where clause based on primary keys for updation purpose.
             * 4. Using QueryBuilder form update bind query.
             * 5. Pass query to executeBindQuery method for updation.
             * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
             */

            if (model == null)
            {
                Log.Log.Debug(typeof(DatabaseHelper).FullName, "Update", "Invalid Object Found.");
                return;
            }

            /*
             * 1. Get mapped entity descriptor object for invoked class object.
             */
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(model.GetType().FullName);

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Update", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Update", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            StringBuilder whereClause = new StringBuilder();
            String tableName = entityDescriptor.GetTableName();

            /*
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
             */
            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();

            ICollection<String> columnNames = new LinkedList<String>();
            ICollection<Object> columnValues = new LinkedList<Object>();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;
                Object columnValue = null;
                try
                {
                    columnNames.Add(attribute.GetColumnName());
                    columnValue = ClassUtils.GetValue(model, attribute.GetGetterMethodName());
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "Update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "Update", siminovException.GetMessage());
                }

                columnValues.Add(columnValue);

                if (attribute.IsPrimaryKey())
                {
                    if (whereClause.Length == 0)
                    {
                        whereClause.Append(attribute.GetColumnName() + "= '" + columnValue + "'");
                    }
                    else
                    {
                        whereClause.Append(" AND " + attribute.GetColumnName() + "= '" + columnValue + "'");
                    }
                }
            }

            ProcessManyToOneRelationship(model, whereClause);
            ProcessManyToManyRelationship(model, whereClause);

            ProcessManyToOneRelationship(model, columnNames, columnValues);
            ProcessManyToManyRelationship(model, columnNames, columnValues);


            /*
             * 4. Using QueryBuilder form update bind query.
             */
            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER, tableName);
            parameters.Add(IQueryBuilder.FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER, columnNames.GetEnumerator());
            parameters.Add(IQueryBuilder.FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER, whereClause.ToString());


            String query = queryBuilder.FormUpdateBindQuery(parameters);

            /*
             * 5. Pass query to executeBindQuery method for updation.
             */

            IEnumerator<Object> values = columnValues.GetEnumerator();
            database.ExecuteBindQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query, values);

            /*
             * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
             */
            IEnumerator<EntityDescriptor.Relationship> relationships = entityDescriptor.GetRelationships();
            while (relationships.MoveNext())
            {
                EntityDescriptor.Relationship relationship = relationships.Current;

                bool isLoad = relationship.IsLoad();
                if (!isLoad)
                {
                    continue;
                }

                String relationshipType = relationship.GetRelationshipType();
                if (relationshipType == null || relationshipType.Length <= 0)
                {
                    continue;
                }

                if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_ONE, StringComparison.OrdinalIgnoreCase))
                {
                    Object value = null;
                    try
                    {
                        value = ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Update", siminovException.GetMessage());
                    }


                    if (value == null)
                    {
                        continue;
                    }

                    SaveOrUpdate(value);
                }
                else if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_ONE_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    IEnumerator<Object> relationshipValues = null;
                    try
                    {
                        relationshipValues = (IEnumerator<Object>)ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Update", siminovException.GetMessage());
                    }


                    if (relationshipValues == null)
                    {
                        continue;
                    }

                    while (relationshipValues.MoveNext())
                    {
                        SaveOrUpdate(relationshipValues.Current);
                    }
                }
                else if (relationshipType.Equals(Constants.ENTITY_DESCRIPTOR_RELATIONSHIP_TYPE_MANY_TO_MANY, StringComparison.OrdinalIgnoreCase))
                {
                    IEnumerator<Object> relationshipValues = null;
                    try
                    {
                        relationshipValues = (IEnumerator<Object>)ClassUtils.GetValue(model, relationship.GetGetterReferMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Update", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Update", siminovException.GetMessage());
                    }


                    if (relationshipValues == null)
                    {
                        continue;
                    }

                    while (relationshipValues.MoveNext())
                    {
                        SaveOrUpdate(relationshipValues.Current);
                    }
                }
            }
        }


        /// <summary>
        /// It finds out whether tuple exists in table or not.
        ///    IF NOT EXISTS:
        ///        adds a record to any single table in a relational database.
        ///    ELSE:
        ///        updates a record to any single table in a relational database.
        ///	
        /// <para>
        /// Example: Make Beer Object
        /// <code>
        ///        Liquor beer = new Liquor();
        ///        beer.SetLiquorType(Liquor.LIQUOR_TYPE_BEER);
        ///        beer.SetDescription("beer_description");
        ///        beer.SetHistory("beer_history");
        ///        beer.SetLink("beer_link");
        ///        beer.SetAlcholContent("beer_alchol_content");
        /// 
        ///        try {
        ///            beer.SaveOrUpdate();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public static void SaveOrUpdate(Object model)
        {
            Siminov.IsActive();

            /*
             * 1. Get mapped entity descriptor object for object class name.
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
             * 3. Form where clause based on primary keys to fetch objects from database table. IF EXISTS: call update method, ELSE: class save method.
             * 4. IF EXISTS: call update method, ELSE: call save method.
             */

            if (model == null)
            {
                Log.Log.Debug(typeof(DatabaseHelper).FullName, "SaveOrUpdate", "Invalid Object Found.");
                return;
            }

            /*
             * 1. Get mapped entity descriptor object for object class name.
             */
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(model.GetType().FullName);

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "SaveOrUpdate", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "SaveOrUpdate", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            /*
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
             */

            StringBuilder whereClause = new StringBuilder();
            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                if (attribute.IsPrimaryKey())
                {
                    Object columnValue = null;
                    try
                    {
                        columnValue = ClassUtils.GetValue(model, attribute.GetGetterMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "SaveOrUpdate", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "SaveOrUpdate", siminovException.GetMessage());
                    }


                    if (whereClause.Length <= 0)
                    {
                        whereClause.Append(attribute.GetColumnName() + "= '" + columnValue + "'");
                    }
                    else
                    {
                        whereClause.Append(" AND " + attribute.GetColumnName() + "= '" + columnValue + "'");
                    }
                }
            }

            ProcessManyToOneRelationship(model, whereClause);
            ProcessManyToManyRelationship(model, whereClause);

            if (whereClause == null || whereClause.Length <= 0)
            {
                Save(model);
                return;
            }


            /*
             * 4. IF EXISTS: call update method, ELSE: call save method.
             */
            int count = Count(entityDescriptor, null, false, whereClause.ToString(), null, null);
            if (count <= 0)
            {
                Save(model);
            }
            else
            {
                Update(model);
            }
        }


        public static void Delete(Object model, String whereClause)
        {
            /*
             * 1. Get mapped entity descriptor object for object parameter class name.
             * 2. Get Table Name, All Method Names, All Column Names, All Column Values, All Column Types, All, Primary Keys, by parsing each fields.
             * 3. Form where clause based on primary keys for deletion purpose.
             * 4. Using QueryBuilder form update bind query.
             * 5. Pass query to executeBindQuery method for deletion.
             * 6. Check for relationship's if any, IF EXISTS: process it, ELSE: return.
             */

            if (model == null)
            {
                Log.Log.Debug(typeof(DatabaseHelper).FullName, "Delete", "Invalid Object Found.");
                return;
            }

            /*
             * 1. Get mapped entity descriptor object for object parameter class name.
             */
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(model.GetType().FullName);

            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());
            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Delete", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Delete", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }

            StringBuilder where = new StringBuilder();

            if (whereClause == null || whereClause.Length <= 0)
            {
                IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();

                while (attributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = attributes.Current;

                    Object columnValue = null;
                    try
                    {
                        columnValue = ClassUtils.GetValue(model, attribute.GetGetterMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "Delete", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "Delete", siminovException.GetMessage());
                    }


                    /*
                     * 3. Form where clause based on primary keys for deletion purpose.
                     */
                    if (attribute.IsPrimaryKey())
                    {
                        if (where.Length <= 0)
                        {
                            where.Append(attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                        else
                        {
                            where.Append(" AND " + attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                    }
                }

                ProcessManyToOneRelationship(model, where);
                ProcessManyToManyRelationship(model, where);

            }
            else
            {
                where.Append(whereClause);
            }

            /*
             * 4. Using QueryBuilder form update bind query.
             */

            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER, where.ToString());


            String query = queryBuilder.FormDeleteQuery(parameters);
            /*
             * 5. Pass query to executeBindQuery method for deletion.
             */
            database.ExecuteQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
        }


        public static int Count(EntityDescriptor entityDescriptor, String column, bool distinct, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Count(" + whereClause + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Count(" + whereClause + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_DISTINCT_PARAMETER, distinct);
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_COUNT_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormCountQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }

                }
            }

            return 0;

        }

        public static double Avg(EntityDescriptor entityDescriptor, String column, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Avg(" + column + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Avg(" + column + ")", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_AVG_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_AVG_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_AVG_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_AVG_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormAvgQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }
                }
            }

            return 0;

        }


        public static double Sum(EntityDescriptor entityDescriptor, String column, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Sum", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Sum", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_SUM_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_SUM_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_SUM_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_SUM_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormSumQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }
                }
            }

            return 0;

        }

        public static double Total(EntityDescriptor entityDescriptor, String column, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Total", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Total", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_TOTAL_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_TOTAL_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormTotalQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }
                }
            }

            return 0;

        }

        public static double Min(EntityDescriptor entityDescriptor, String column, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Min", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Min", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_MIN_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_MIN_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_MIN_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_MIN_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormMinQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }
                }
            }

            return 0;

        }

        public static double Max(EntityDescriptor entityDescriptor, String column, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "Max", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "Max", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_MAX_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_MAX_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_MAX_QUERY_GROUP_BYS_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_MAX_QUERY_HAVING_PARAMETER, having);


            String query = queryBuilder.FormMaxQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {

                    Object value = values.Current;
                    if (value.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                    {
                        return int.Parse((String)value);
                    }
                }
            }

            return 0;

        }

        public static String GroupConcat(EntityDescriptor entityDescriptor, String column, String delimiter, String whereClause, IEnumerator<String> groupBys, String having)
        {

            Siminov.IsActive();

            DatabaseDescriptor databaseDescriptor = GetDatabaseDescriptor(entityDescriptor.GetClassName());
            DatabaseBundle databaseBundle = resourceManager.GetDatabaseBundle(databaseDescriptor.GetDatabaseName());

            IDatabaseImpl database = databaseBundle.GetDatabase();
            IQueryBuilder queryBuilder = databaseBundle.GetQueryBuilder();

            if (database == null)
            {
                Log.Log.Error(typeof(DatabaseHelper).FullName, "GroupConcat", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
                throw new DeploymentException(typeof(DatabaseHelper).FullName, "GroupConcat", "No Database Instance Found For ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName());
            }


            IDictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER, entityDescriptor.GetTableName());
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER, column);
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER, delimiter);
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER, whereClause);
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER, groupBys);
            parameters.Add(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER, having);


            String query = queryBuilder.FormGroupConcatQuery(parameters);

            IEnumerator<IDictionary<String, Object>> datas = database.ExecuteSelectQuery(GetDatabaseDescriptor(entityDescriptor.GetClassName()), entityDescriptor, query);
            while (datas.MoveNext())
            {
                IDictionary<String, Object> data = datas.Current;
                ICollection<Object> parse = data.Values;

                IEnumerator<Object> values = parse.GetEnumerator();
                while (values.MoveNext())
                {
                    return ((String)values.Current);
                }
            }

            return null;

        }

        /// <summary>
        /// Returns database descriptor object based on the POJO class called.
        /// <para>
        /// Example:
        /// <code>
        ///        try {
        ///            DatabaseDescriptor databaseDescriptor = new Liquor().GetDatabaseDescriptor();
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="className"></param>
        /// <returns>Database Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting database descriptor object.</exception>
        public static DatabaseDescriptor GetDatabaseDescriptor(String className)
        {
            return resourceManager.GetDatabaseDescriptorBasedOnClassName(className);
        }

        /// <summary>
        /// Returns the actual entity descriptor object mapped for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        EntityDescriptor entityDescriptor = null;
        ///        try {
        ///            entityDescriptor = new Liquor().GetEntityDescriptor();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="className">Name of the class</param>
        /// <returns>Entity Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If entity descriptor object not mapped for invoked class object.</exception>
        public static EntityDescriptor GetEntityDescriptor(String className)
        {
            return resourceManager.RequiredEntityDescriptorBasedOnClassName(className);
        }

        /// <summary>
        /// Returns the mapped table name for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        String tableName = null;
        ///        try {
        ///            tableName = new Liquor().getTableName();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>Mapped Table name</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public static String GetTableName(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            return entityDescriptor.GetTableName();
        }


        /// <summary>
        /// Returns all column names of mapped table
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> columnNames = null;
        ///        try {
        ///            columnNames = new Liquor().GetColumnNames();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All column names of mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>

        public static IEnumerator<String> GetColumnNames(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            ICollection<String> columnNames = new List<String>();

            while (attributes.MoveNext())
            {
                columnNames.Add(attributes.Current.GetColumnName());
            }

            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        columnNames.Add(attributes.Current.GetColumnName());
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        columnNames.Add(attributes.Current.GetColumnName());
                    }
                }
            }


            return columnNames.GetEnumerator();
        }



        /// <summary>
        /// Returns all column values in the same order of column names for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IDictionary<String, Object> values = null;
        ///        try {
        ///            values = new Liquor().GetColumnValues();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All column values for invoked object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public static IDictionary<String, Object> GetColumnValues(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IDictionary<String, Object> columnNameAndItsValues = new Dictionary<String, Object>();
            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                try
                {
                    columnNameAndItsValues.Add(attribute.GetColumnName(), ClassUtils.GetValue(model, attribute.GetGetterMethodName()));
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "GetColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "GetColumnValues", siminovException.GetMessage());
                }
            }

            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        try
                        {
                            columnNameAndItsValues.Add(attribute.GetColumnName(), ClassUtils.GetValue(model, attribute.GetGetterMethodName()));
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "GetColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "GetColumnValues", siminovException.GetMessage());
                        }
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        try
                        {
                            columnNameAndItsValues.Add(attribute.GetColumnName(), ClassUtils.GetValue(model, attribute.GetGetterMethodName()));
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "GetColumnValues", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "GetColumnValues", siminovException.GetMessage());
                        }
                    }
                }
            }

            return columnNameAndItsValues;
        }


        /// <summary>
        /// Returns all columns with there data types for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IDictionary<String, String> columnTypes = null;
        ///        try {
        ///            columnTypes = new Liquor().GetColumnTypes();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }	
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All columns with there data types</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public static IDictionary<String, String> GetColumnTypes(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IDictionary<String, String> columnTypes = new Dictionary<String, String>();
            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;
                columnTypes.Add(attribute.GetColumnName(), attribute.GetType());
            }

            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        columnTypes.Add(attribute.GetColumnName(), attribute.GetType());
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        columnTypes.Add(attribute.GetColumnName(), attribute.GetType());
                    }
                }
            }

            return columnTypes;
        }


        /// <summary>
        /// Returns all primary keys of mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> primaryKeys = null;
        ///        try {
        ///            primaryKeys = new Liquor().GetPrimeryKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All primary keys</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not mapped table found for invoked class object</exception>
        public static IEnumerator<String> GetPrimaryKeys(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            ICollection<String> primaryKeys = new List<String>();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                bool isPrimary = attribute.IsPrimaryKey();
                if (isPrimary)
                {
                    primaryKeys.Add(attribute.GetColumnName());
                }
            }

            return primaryKeys.GetEnumerator();
        }


        /// <summary>
        /// Returns all mandatory fields which are associated with mapped table for invoked class object
        /// <para>
        /// <code>
        ///        IEnumerator<String> mandatoryFields = null;
        ///        try {
        ///            mandatoryFields = new Liquor().GetMandatoryFields();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///       }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All mandatory fields for mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public static IEnumerator<String> GetMandatoryFields(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            ICollection<String> isMandatoryFieldsVector = new List<String>();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                if (attribute.IsNotNull())
                {
                    isMandatoryFieldsVector.Add(attribute.GetColumnName());
                }
            }


            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        if (attribute.IsNotNull())
                        {
                            isMandatoryFieldsVector.Add(attribute.GetColumnName());
                        }
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        if (attribute.IsNotNull())
                        {
                            isMandatoryFieldsVector.Add(attribute.GetColumnName());
                        }
                    }
                }
            }

            return isMandatoryFieldsVector.GetEnumerator();
        }

        /// <summary>
        /// Returns all unique fields which are associated with mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> uniqueFields = null;
        ///        try {
        ///            uniqueFields = new Liquor().GetUniqueFields();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All unique fields for mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        public static IEnumerator<String> GetUniqueFields(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            ICollection<String> isUniqueFieldsVector = new List<String>();

            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;

                bool isUnique = attribute.IsUnique();
                if (isUnique)
                {
                    isUniqueFieldsVector.Add(attribute.GetColumnName());
                }
            }

            /*
             * Add ONE-TO-MANY And MANY-TO-MANY Relationship Columns.
             */
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {

                        bool isUnique = attribute.IsUnique();
                        if (isUnique)
                        {
                            isUniqueFieldsVector.Add(attribute.GetColumnName());
                        }
                    }
                }
            }

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor parentEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = parentEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {

                        bool isUnique = attribute.IsUnique();
                        if (isUnique)
                        {
                            isUniqueFieldsVector.Add(attribute.GetColumnName());
                        }
                    }
                }
            }


            return isUniqueFieldsVector.GetEnumerator();
        }

        /// <summary>
        /// Returns all foreign keys of mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> foreignKeys = null;
        ///        try {
        ///             foreignKeys = new Liquor().GetForeignKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="model"></param>
        /// <returns>All foreign keys of mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public static IEnumerator<String> GetForeignKeys(Object model)
        {
            Siminov.IsActive();

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            ICollection<EntityDescriptor.Attribute> attributes = GetForeignKeys(entityDescriptor);
            IEnumerator<EntityDescriptor.Attribute> attributesIterate = attributes.GetEnumerator();

            ICollection<String> foreignKeys = new List<String>();
            while (attributesIterate.MoveNext())
            {
                foreignKeys.Add(attributesIterate.Current.GetColumnName());
            }

            return foreignKeys.GetEnumerator();
        }

        public static ICollection<EntityDescriptor.Attribute> GetForeignKeys(EntityDescriptor entityDescriptor)
        {
            IEnumerator<EntityDescriptor.Relationship> oneToManyRealtionships = entityDescriptor.GetManyToOneRelationships();
            IEnumerator<EntityDescriptor.Relationship> manyToManyRealtionships = entityDescriptor.GetManyToManyRelationships();

            ICollection<EntityDescriptor.Attribute> foreignAttributes = new List<EntityDescriptor.Attribute>();

            IEnumerator<EntityDescriptor.Attribute> attributes = entityDescriptor.GetAttributes();
            while (attributes.MoveNext())
            {
                EntityDescriptor.Attribute attribute = attributes.Current;
                if (attribute.IsPrimaryKey())
                {
                    foreignAttributes.Add(attribute);
                }
            }

            while (oneToManyRealtionships.MoveNext())
            {

                EntityDescriptor.Relationship relationship = oneToManyRealtionships.Current;
                EntityDescriptor referedEntityDescriptor = relationship.GetReferedEntityDescriptor();

                ICollection<EntityDescriptor.Attribute> referedForeignKeys = GetForeignKeys(referedEntityDescriptor);
                IEnumerator<EntityDescriptor.Attribute> referedForeignKeysIterate = referedForeignKeys.GetEnumerator();

                while (referedForeignKeysIterate.MoveNext())
                {
                    foreignAttributes.Add(referedForeignKeysIterate.Current);
                }
            }

            while (manyToManyRealtionships.MoveNext())
            {

                EntityDescriptor.Relationship relationship = manyToManyRealtionships.Current;
                EntityDescriptor referedEntityDescriptor = relationship.GetReferedEntityDescriptor();

                ICollection<EntityDescriptor.Attribute> referedForeignKeys = GetForeignKeys(referedEntityDescriptor);
                IEnumerator<EntityDescriptor.Attribute> referedForeignKeysIterate = referedForeignKeys.GetEnumerator();

                while (referedForeignKeysIterate.MoveNext())
                {
                    foreignAttributes.Add(referedForeignKeysIterate.Current);
                }
            }

            return foreignAttributes;
        }

        /// <summary>
        /// Iterates the provided cursor, and returns tuples in form of actual objects.
        /// </summary>
        /// <param name="entityDescriptor">Entity Descriptor Object</param>
        /// <param name="values">Values</param>
        /// <returns></returns>
        private static IEnumerator<Object> ParseAndInflateData(EntityDescriptor entityDescriptor, IEnumerator<IDictionary<String, Object>> values)
        {
            Siminov.IsActive();

            ICollection<Object> tuples = new LinkedList<Object>();
            while (values.MoveNext())
            {

                IDictionary<String, Object> value = values.Current;

                ICollection<String> columnNames = value.Keys;
                IEnumerator<String> columnNamesIterate = columnNames.GetEnumerator();

                IDictionary<String, Object> data = new Dictionary<String, Object>();
                while (columnNamesIterate.MoveNext())
                {
                    String columnName = columnNamesIterate.Current;

                    if (entityDescriptor.ContainsAttributeBasedOnColumnName(columnName))
                    {
                        data.Add(entityDescriptor.GetAttributeBasedOnColumnName(columnName).GetSetterMethodName(), value[columnName]);
                    }
                }

                Object model = null;

                try
                {
                    model = ClassUtils.CreateAndInflateObject(entityDescriptor.GetClassName(), data);
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ParseAndInflateData", "SiminovException caught while create and inflate object through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ParseAndInflateData", siminovException.GetMessage());
                }

                tuples.Add(model);
            }

            return tuples.GetEnumerator();
        }

        private static void ProcessOneToOneRelationship(Object model)
        {

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> oneToOneRelationships = entityDescriptor.GetOneToOneRelationships();

            while (oneToOneRelationships.MoveNext())
            {

                EntityDescriptor.Relationship oneToOneRelationship = oneToOneRelationships.Current;

                bool isLoad = oneToOneRelationship.IsLoad();
                if (!isLoad)
                {
                    continue;
                }


                StringBuilder whereClause = new StringBuilder();
                IEnumerator<String> foreignKeys = GetPrimaryKeys(model);
                while (foreignKeys.MoveNext())
                {
                    String foreignKey = foreignKeys.Current;
                    EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(foreignKey);
                    Object columnValue = null;

                    try
                    {
                        columnValue = ClassUtils.GetValue(model, attribute.GetGetterMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessOneToOneRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + attribute.GetGetterMethodName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessOneToOneRelationship", siminovException.GetMessage());
                    }

                    if (whereClause.Length <= 0)
                    {
                        whereClause.Append(foreignKey + "='" + columnValue.ToString() + "'");
                    }
                    else
                    {
                        whereClause.Append(" AND " + foreignKey + "='" + columnValue.ToString() + "'");
                    }
                }

                EntityDescriptor referedEntityDescriptor = oneToOneRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToOneRelationship.GetReferTo());
                    oneToOneRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                Object referedObject = LazyFetch(referedEntityDescriptor, false, whereClause.ToString(), null, null, null, null, null, null);
                Object[] referedObjects = (Object[])referedObject;

                if (referedObjects == null || referedObjects.Length <= 0)
                {
                    return;
                }

                if (referedObjects[0] == null)
                {
                    return;
                }

                try
                {
                    ClassUtils.InvokeMethod(model, oneToOneRelationship.GetSetterReferMethodName(), new Type[] { referedObjects[0].GetType() }, new Object[] { referedObjects[0] });
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessOneToOneRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + oneToOneRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessOneToOneRelationship", siminovException.GetMessage());
                }

            }
        }

        private static void ProcessOneToManyRelationship(Object model)
        {

            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> oneToManyRelationships = entityDescriptor.GetOneToManyRelationships();

            while (oneToManyRelationships.MoveNext())
            {

                EntityDescriptor.Relationship oneToManyRelationship = oneToManyRelationships.Current;

                bool isLoad = oneToManyRelationship.IsLoad();
                if (!isLoad)
                {
                    continue;
                }


                StringBuilder whereClause = new StringBuilder();
                IEnumerator<String> foreignKeys = GetPrimaryKeys(model);
                while (foreignKeys.MoveNext())
                {
                    String foreignKey = foreignKeys.Current;
                    EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(foreignKey);
                    Object columnValue = null;

                    try
                    {
                        columnValue = ClassUtils.GetValue(model, attribute.GetGetterMethodName());
                    }
                    catch (SiminovException siminovException)
                    {
                        Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessOneToManyRelationship", "SiminovException caught while getting column value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + attribute.GetGetterMethodName() + ", " + siminovException.GetMessage());
                        throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessOneToManyRelationship", siminovException.GetMessage());
                    }

                    if (whereClause.Length <= 0)
                    {
                        whereClause.Append(foreignKey + "='" + columnValue.ToString() + "'");
                    }
                    else
                    {
                        whereClause.Append(" AND " + foreignKey + "='" + columnValue.ToString() + "'");
                    }
                }

                EntityDescriptor referedEntityDescriptor = oneToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(oneToManyRelationship.GetReferTo());
                    oneToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }


                Object referedObject = LazyFetch(referedEntityDescriptor, false, whereClause.ToString(), null, null, null, null, null, null);
                Object[] referedObjects = (Object[])referedObject;

                Type classObject = null;
                try
                {
                    classObject = ClassUtils.CreateClass(referedEntityDescriptor.GetClassName());
                }
                catch (System.Exception exception)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "Select", "Exception caught while making class object for return type, ENTITY-DESCRIPTOR: " + entityDescriptor.GetClassName() + ", " + exception.Message);
                }

                Type listT = typeof(List<>).MakeGenericType(new[] { ClassUtils.CreateClass(referedEntityDescriptor.GetClassName()) });
                IList list = (IList)Activator.CreateInstance(listT);

                for (int i = 0; i < referedObjects.Length; i++)
                {
                    list.Add(referedObjects[i]);
                }


                try
                {
                    ClassUtils.InvokeMethod(model, oneToManyRelationship.GetSetterReferMethodName(), new Type[] { typeof(IEnumerator) }, new Object[] { list.GetEnumerator() });
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessOneToManyRelationship", "SiminovException caught while invoking method through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + oneToManyRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessOneToManyRelationship", siminovException.GetMessage());
                }
            }

        }

        private static void ProcessManyToOneRelationship(Object model, ICollection<String> columnNames, ICollection<Object> columnValues)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToOneRelationships = entityDescriptor.GetManyToOneRelationships();

            while (manyToOneRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToOneRelationship = manyToOneRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToOneRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToOneRelationship.GetReferTo());
                    manyToOneRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = null;
                try
                {
                    referedObject = ClassUtils.GetValue(model, manyToOneRelationship.GetGetterReferMethodName());
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", siminovException.GetMessage());
                }

                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                }

                ProcessManyToOneRelationship(referedObject, columnNames, columnValues);

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        try
                        {
                            columnNames.Add(attribute.GetColumnName());
                            columnValues.Add(ClassUtils.GetValue(referedObject, attribute.GetGetterMethodName()));
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + referedObject.GetType().FullName + ", " + " METHOD-NAME: " + attribute.GetGetterMethodName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", siminovException.GetMessage());
                        }
                    }
                }
            }
        }

        private static void ProcessManyToOneRelationship(Object model, StringBuilder whereClause)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToOneRelationships = entityDescriptor.GetManyToOneRelationships();

            while (manyToOneRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToOneRelationship = manyToOneRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToOneRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToOneRelationship.GetReferTo());
                    manyToOneRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = null;
                try
                {
                    referedObject = ClassUtils.GetValue(model, manyToOneRelationship.GetGetterReferMethodName());
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + manyToOneRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", siminovException.GetMessage());
                }

                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                }

                ProcessManyToOneRelationship(referedObject, whereClause);

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        Object columnValue = null;
                        try
                        {
                            columnValue = ClassUtils.GetValue(referedObject, attribute.GetGetterMethodName());
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", siminovException.GetMessage());
                        }


                        if (whereClause.Length <= 0)
                        {
                            whereClause.Append(attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                        else
                        {
                            whereClause.Append(" AND " + attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                    }
                }
            }

        }

        private static void ProcessManyToOneRelationship(Object model, IDictionary<String, Object> data)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToOneRelationships = entityDescriptor.GetManyToOneRelationships();

            while (manyToOneRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToOneRelationship = manyToOneRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToOneRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToOneRelationship.GetReferTo());
                    manyToOneRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = ClassUtils.CreateClassInstance(manyToOneRelationship.GetReferedEntityDescriptor().GetClassName());
                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                }


                ProcessManyToOneRelationship(referedObject, data);

                if (manyToOneRelationship.IsLoad())
                {

                    StringBuilder whereClause = new StringBuilder();

                    IEnumerator<String> foreignKeys = GetPrimaryKeys(referedObject);
                    while (foreignKeys.MoveNext())
                    {
                        String foreignKey = foreignKeys.Current;
                        EntityDescriptor.Attribute attribute = referedEntityDescriptor.GetAttributeBasedOnColumnName(foreignKey);
                        Object columnValue = data[attribute.GetColumnName()];

                        if (whereClause.Length <= 0)
                        {
                            whereClause.Append(foreignKey + "='" + columnValue.ToString() + "'");
                        }
                        else
                        {
                            whereClause.Append(" AND " + foreignKey + "='" + columnValue.ToString() + "'");
                        }
                    }

                    Object[] fetchedObjects = LazyFetch(referedEntityDescriptor, false, whereClause.ToString(), null, null, null, null, null, null);
                    referedObject = fetchedObjects[0];

                }
                else
                {
                    IEnumerator<String> foreignKeys = GetPrimaryKeys(referedObject);
                    while (foreignKeys.MoveNext())
                    {
                        String foreignKey = foreignKeys.Current;
                        EntityDescriptor.Attribute attribute = referedEntityDescriptor.GetAttributeBasedOnColumnName(foreignKey);

                        Object columnValue = data[attribute.GetColumnName()];
                        if (columnValue == null)
                        {
                            continue;
                        }

                        try
                        {
                            ClassUtils.InvokeMethod(referedObject, attribute.GetSetterMethodName(), new Type[] { columnValue.GetType() }, new Object[] { columnValue });
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.GetClassName() + ", METHOD-NAME: " + attribute.GetSetterMethodName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.GetClassName() + ", METHOD-NAME: " + attribute.GetSetterMethodName() + ", " + siminovException.GetMessage());
                        }
                    }
                }


                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "Unable To Create Parent Relationship. REFER-TO: " + manyToOneRelationship.GetReferTo());
                }


                try
                {
                    ClassUtils.InvokeMethod(model, manyToOneRelationship.GetSetterReferMethodName(), new Type[] { referedObject.GetType() }, new Object[] { referedObject });
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.GetClassName() + ", METHOD-NAME: " + manyToOneRelationship.GetSetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToOneRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.GetClassName() + ", METHOD-NAME: " + manyToOneRelationship.GetSetterReferMethodName() + ", " + siminovException.GetMessage());
                }
            }

        }

        private static void ProcessManyToManyRelationship(Object model, ICollection<String> columnNames, ICollection<Object> columnValues)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToManyRelationship.GetReferTo());
                    manyToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = null;
                try
                {
                    referedObject = ClassUtils.GetValue(model, manyToManyRelationship.GetGetterReferMethodName());
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", siminovException.GetMessage());
                }

                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                }

                ProcessManyToManyRelationship(referedObject, columnNames, columnValues);

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        try
                        {
                            columnNames.Add(attribute.GetColumnName());
                            columnValues.Add(ClassUtils.GetValue(model, attribute.GetGetterMethodName()));
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", siminovException.GetMessage());
                        }
                    }
                }
            }
        }

        private static void ProcessManyToManyRelationship(Object model, StringBuilder whereClause)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToManyRelationships();

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToManyRelationship.GetReferTo());
                    manyToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = null;
                try
                {
                    referedObject = ClassUtils.GetValue(model, manyToManyRelationship.GetGetterReferMethodName());
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while get method values through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + " METHOD-NAME: " + manyToManyRelationship.GetGetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", siminovException.GetMessage());
                }

                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                }

                ProcessManyToManyRelationship(referedObject, whereClause);

                IEnumerator<EntityDescriptor.Attribute> parentAttributes = referedEntityDescriptor.GetAttributes();
                while (parentAttributes.MoveNext())
                {
                    EntityDescriptor.Attribute attribute = parentAttributes.Current;

                    bool isPrimary = attribute.IsPrimaryKey();
                    if (isPrimary)
                    {
                        Object columnValue = null;
                        try
                        {
                            columnValue = ClassUtils.GetValue(referedObject, attribute.GetGetterMethodName());
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while get method value through reflection, CLASS-NAME: " + entityDescriptor.GetClassName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", siminovException.GetMessage());
                        }


                        if (whereClause.Length <= 0)
                        {
                            whereClause.Append(attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                        else
                        {
                            whereClause.Append(" AND " + attribute.GetColumnName() + "= '" + columnValue + "'");
                        }
                    }
                }
            }


        }

        private static void ProcessManyToManyRelationship(Object model, IDictionary<String, Object> data)
        {
            EntityDescriptor entityDescriptor = GetEntityDescriptor(model.GetType().FullName);
            IEnumerator<EntityDescriptor.Relationship> manyToManyRelationships = entityDescriptor.GetManyToOneRelationships();

            while (manyToManyRelationships.MoveNext())
            {
                EntityDescriptor.Relationship manyToManyRelationship = manyToManyRelationships.Current;
                EntityDescriptor referedEntityDescriptor = manyToManyRelationship.GetReferedEntityDescriptor();
                if (referedEntityDescriptor == null)
                {
                    referedEntityDescriptor = GetEntityDescriptor(manyToManyRelationship.GetReferTo());
                    manyToManyRelationship.SetReferedEntityDescriptor(referedEntityDescriptor);
                }

                Object referedObject = ClassUtils.CreateClassInstance(manyToManyRelationship.GetReferedEntityDescriptor().GetClassName());
                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                }


                ProcessManyToManyRelationship(referedObject, data);

                if (manyToManyRelationship.IsLoad())
                {

                    StringBuilder whereClause = new StringBuilder();

                    IEnumerator<String> foreignKeys = GetPrimaryKeys(referedObject);
                    while (foreignKeys.MoveNext())
                    {
                        String foreignKey = foreignKeys.Current;
                        EntityDescriptor.Attribute attribute = referedEntityDescriptor.GetAttributeBasedOnColumnName(foreignKey);
                        Object columnValue = data[attribute.GetColumnName()];

                        if (whereClause.Length <= 0)
                        {
                            whereClause.Append(foreignKey + "='" + columnValue.ToString() + "'");
                        }
                        else
                        {
                            whereClause.Append(" AND " + foreignKey + "='" + columnValue.ToString() + "'");
                        }
                    }

                    Object[] fetchedObjects = LazyFetch(referedEntityDescriptor, false, whereClause.ToString(), null, null, null, null, null, null);
                    referedObject = fetchedObjects[0];

                }
                else
                {
                    IEnumerator<String> primaryKeys = GetPrimaryKeys(referedObject);
                    while (primaryKeys.MoveNext())
                    {
                        String foreignKey = primaryKeys.Current;
                        EntityDescriptor.Attribute attribute = referedEntityDescriptor.GetAttributeBasedOnColumnName(foreignKey);

                        Object columnValue = data[attribute.GetColumnName()];
                        if (columnValue == null)
                        {
                            continue;
                        }

                        try
                        {
                            ClassUtils.InvokeMethod(referedObject, attribute.GetSetterMethodName(), new Type[] { columnValue.GetType() }, new Object[] { columnValue });
                        }
                        catch (SiminovException siminovException)
                        {
                            Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.GetClassName() + ", METHOD-NAME: " + attribute.GetSetterMethodName() + ", " + siminovException.GetMessage());
                            throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + referedEntityDescriptor.GetClassName() + ", METHOD-NAME: " + attribute.GetSetterMethodName() + ", " + siminovException.GetMessage());
                        }
                    }
                }

                if (referedObject == null)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "Parent Object Not Set, Please Provide Proper Relationship. REFER-TO: " + manyToManyRelationship.GetReferTo());
                }

                try
                {
                    ClassUtils.InvokeMethod(model, manyToManyRelationship.GetSetterReferMethodName(), new Type[] { referedObject.GetType() }, new Object[] { referedObject });
                }
                catch (SiminovException siminovException)
                {
                    Log.Log.Error(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.GetClassName() + ", METHOD-NAME: " + manyToManyRelationship.GetSetterReferMethodName() + ", " + siminovException.GetMessage());
                    throw new DatabaseException(typeof(DatabaseHelper).FullName, "ProcessManyToManyRelationship", "SiminovException caught while invoking method, CLASS-NAME: " + entityDescriptor.GetClassName() + ", METHOD-NAME: " + manyToManyRelationship.GetSetterReferMethodName() + ", " + siminovException.GetMessage());
                }
            }
        }
    }
}
