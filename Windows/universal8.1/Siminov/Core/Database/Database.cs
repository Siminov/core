/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
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



using Siminov.Core.Database.Design;
using Siminov.Core.Model;
using Siminov.Core.Resource;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database
{
    public class Database : IDatabase
    {

        private Object model = null;

        /// <summary>
        /// Database Constructor
        /// </summary>
        public Database()
        {

        }

        public Database(Object model)
        {
            this.model = model;
        }

        /// <summary>
        /// It drop's the whole database based on database name.
        /// <para>
        /// Drop the Book table.
        /// <code>
        ///        DatabaseDescriptor databaseDescriptor = new Book().GetDatabaseDescriptor();
        ///
        ///        try {
        ///            Database.DropDatabase(databaseDescriptor.GetDatabaseName());
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="databaseName">Entity Descriptor object which defines the structure of table</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop database</exception>
        public static void DropDatabase(String databaseName)
        {
            ResourceManager resourceManager = ResourceManager.GetInstance();
            DatabaseHelper.DropDatabase(resourceManager.GetDatabaseDescriptorBasedOnName(databaseName));
        }

        /// <summary>
        /// Is used to create a new table in an database.
        /// <para>
        /// Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
        /// Using SIMINOV there are three ways to create table in database.
        /// SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.
        /// <code>
        ///        <entity-descriptor>
        ///
        ///            <property name="table_name">BOOK</property>            
        ///            <property name="class_name">Siminov.Core.Sample.Model.Book</property>
        /// 
        ///            <attributes>
        ///		
        ///                <attribute>
        ///                    <property name="variable_name">title</property>
        ///                    <property name="column_name">TITLE</property>
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
        ///                    <property name="variable_name">author</property>
        ///                    <property name="column_name">AUTHOR</property>
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
        ///             </attributes>
        ///             
        ///             <indexes>
        ///                <index>
        ///                    <property name="name">BOOK_INDEX_BASED_ON_AUTHOR</property>
        ///                    <property name="unique">true</property>
        ///                    <property name="column">AUTHOR</property>
        ///                </index>
        ///             </indexes>
        ///
        ///             <relationships>
        ///
        ///                    <relationship>
        ///                        <property name="type">one-to-many</property>
        ///                        <property name="refer">lessions</property>
        ///                        <property name="refer_to">Siminov.Core.Sample.Model.Lession</property>
        ///                        <property name="on_update">cascade</property>
        ///                        <property name="on_delete">cascade</property>
        ///                        <property name="load">true</property>
        ///                    </one-to-many>		
        ///		    
        ///             </relationships>
        ///											
        ///        </entity-descriptor>		
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create table in SQLite.</exception>
        public void CreateTable()
        {
            DatabaseHelper.CreateTable(GetEntityDescriptor());
        }

        /// <summary>
        /// It drop's the table from database based on entity descriptor.
        /// <para>
        /// Drop the Book table.
        /// <code>
        ///        Book book = new Book();
        ///
        ///        try {
        ///            book.DropTable();
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop table.</exception>
        public void DropTable()
        {
            DatabaseHelper.DropTable(GetEntityDescriptor());
        }

        /// <summary>
        /// Is used to drop a index on a table in database.
        /// <para>
        /// Create Index On Book table
        /// <code>
        ///        String indexName = "BOOK_INDEX_BASED_ON_AUTHOR";
        ///        Book book = new Book();
        ///
        ///        try {
        ///            book.DropIndex(indexName);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="indexName">Name of a index needs to be drop</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop index on table</exception>
        public void DropIndex(String indexName)
        {
            DatabaseHelper.DropIndex(GetEntityDescriptor(), indexName);
        }

        /// <summary>
        /// Returns all tuples based on query from mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        Book[] books = null;
        ///        try {
        ///            books = new Book().Select().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>ISelect object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting tuples from a single table</exception>
        public ISelect Select()
        {
            return new SelectImpl(GetEntityDescriptor(), typeof(ISelect).FullName);
        }

        /// <summary>
        /// Returns all tuples based on manual query from mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        String query = "SELECT * FROM BOOK";
        ///
        ///        Book[] books = null;
        ///        try {
        ///            books = new Book().Select(query);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="query">Query to get tuples from database</param>
        /// <returns>ISelect object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting tuples from a single table</exception>
        public Object[] Select(String query)
        {

            if (model != null)
            {
                return DatabaseHelper.Select(model, query);
            }
            else
            {
                return DatabaseHelper.Select(this, query);
            }
        }

        /// <summary>
        /// It adds a record to any single table in a relational database.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        try {
        ///            cBook.Save();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public void Save()
        {

            if (model != null)
            {
                DatabaseHelper.Save(model);
            }
            else
            {
                DatabaseHelper.Save(this);
            }
        }

        /// <summary>
        /// It updates a record to any single table in a relational database.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_c);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        try {
        ///            cBook.Update();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public void Update()
        {

            if (model != null)
            {
                DatabaseHelper.Update(model);
            }
            else
            {
                DatabaseHelper.Update(this);
            }
        }


        /// <summary>
        /// It finds out whether tuple exists in table or not.
        /// <para>
        ///    IF NOT EXISTS:
        ///        adds a record to any single table in a relational database.
        ///    ELSE:
        ///        updates a record to any single table in a relational database.
        ///        
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        try {
        ///            cBook.SaveOrUpdate();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public void SaveOrUpdate()
        {

            if (model != null)
            {
                DatabaseHelper.SaveOrUpdate(model);
            }
            else
            {
                DatabaseHelper.SaveOrUpdate(this);
            }
        }

        /// <summary>
        /// It deletes a record from single table in a relational database.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        try {
        ///            cBook.Delete();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>IDelete Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        public IDelete Delete()
        {

            if (model != null)
            {
                return new DeleteImpl(GetEntityDescriptor(), model);
            }
            else
            {
                return new DeleteImpl(GetEntityDescriptor(), this);
            }
        }

        /// <summary>
        /// Returns the count of rows based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int noOfBooks = 0;
        ///
        ///        try {
        ///            noOfBooks = cBook.Count().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting count of tuples from database.</exception>
        public ICount Count()
        {
            return new CountImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the average based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int noOfBooks = 0;
        ///
        ///        try {
        ///            noOfBooks = cBook.Avg().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>IAverage Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting average from database.</exception>
        public IAverage Avg()
        {
            return new AverageImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the sum based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int noOfBooks = 0;
        ///   
        ///        try {
        ///            noOfBooks = cBook.Sum().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>ISum Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting sum from database.</exception>
        public ISum Sum()
        {
            return new SumImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the total based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int totalBooks = 0;
        ///
        ///        try {
        ///            totalBooks = cBook.Avg().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>ITotal Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting total tuples from database.</exception>
        public ITotal Total()
        {
            return new TotalImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the min based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int minBooks = 0;
        ///
        ///        try {
        ///            minBooks = cBook.Min().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>IMin Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting min from database.</exception>
        public IMin Min()
        {
            return new MinImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the max based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///
        ///        int maxBooks = 0;
        ///
        ///        try {
        ///            maxBooks = cBook.Max().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>IMax Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting max from database.</exception>
        public IMax Max()
        {
            return new MaxImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns the group concat based on where clause provided.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        int groupConcatBooks = 0;
        ///
        ///        try {
        ///            groupConcatBooks = cBook.GroupConcat().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>IGroupConcat Interface</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting group concat from database.</exception>
        public IGroupConcat GroupConcat()
        {
            return new GroupConcatImpl(GetEntityDescriptor());
        }

        /// <summary>
        /// Returns database descriptor object based on the POJO class called.
        /// <para>
        /// Example:
        /// <code>
        ///        try {
        ///            DatabaseDescriptor databaseDescriptor = new Book().GetDatabaseDescriptor();
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>Database Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting database descriptor object.</exception>
        public DatabaseDescriptor GetDatabaseDescriptor()
        {

            if (model != null)
            {
                return DatabaseHelper.GetDatabaseDescriptor(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetDatabaseDescriptor(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns the actual entity descriptor object mapped for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        EntityDescriptor entityDescriptor = null;
        ///        try {
        ///            entityDescriptor = new Book().GetEntityDescriptor();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>Entity Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If entity descriptor object not mapped for invoked class object</exception>
        public EntityDescriptor GetEntityDescriptor()
        {

            if (model != null)
            {
                return DatabaseHelper.GetEntityDescriptor(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetEntityDescriptor(this.GetType().FullName);
            }
        }


        /// <summary>
        /// Returns the mapped table name for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        String tableName = null;
        ///        try {
        ///            tableName = new Book().GetTableName();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>Mapped Table name</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public String GetTableName()
        {

            if (model != null)
            {
                return DatabaseHelper.GetTableName(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetTableName(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all column names of mapped table.
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> columnNames = null;
        ///        try {
        ///            columnNames = new Book().GetColumnNames();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All column names of mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IEnumerator<String> GetColumnNames()
        {

            if (model != null)
            {
                return DatabaseHelper.GetColumnNames(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetColumnNames(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all column values in the same order of column names for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IDictionary<String, Object> values = null;
        ///        try {
        ///            values = new Book().GetColumnValues();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All column values for invoked object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IDictionary<String, Object> GetColumnValues()
        {

            if (model != null)
            {
                return DatabaseHelper.GetColumnValues(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetColumnValues(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all columns with there data types for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IDictionary<String, String> columnTypes = null;
        ///        try {
        ///            columnTypes = new Book().GetColumnTypes();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }	
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All columns with there data types</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IDictionary<String, String> GetColumnTypes()
        {

            if (model != null)
            {
                return DatabaseHelper.GetColumnTypes(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetColumnTypes(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all primary keys of mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> primaryKeys = null;
        ///        try {
        ///            primaryKeys = new Book().GetPrimeryKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All primary keys</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not mapped table found for invoked class object</exception>
        public IEnumerator<String> GetPrimaryKeys()
        {

            if (model != null)
            {
                return DatabaseHelper.GetPrimaryKeys(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetPrimaryKeys(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all mandatory fields which are associated with mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> mandatoryFields = null;
        ///        try {
        ///            mandatoryFields = new Book().GetMandatoryFields();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All mandatory fields for mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IEnumerator<String> GetMandatoryFields()
        {

            if (model != null)
            {
                return DatabaseHelper.GetMandatoryFields(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetMandatoryFields(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Returns all unique fields which are associated with mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> uniqueFields = null;
        ///        try {
        ///            uniqueFields = new Book().GetUniqueFields();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All unique fields for mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IEnumerator<String> GetUniqueFields()
        {

            if (model != null)
            {
                return DatabaseHelper.GetUniqueFields(typeof(object).FullName);
            }
            else
            {
                return DatabaseHelper.GetUniqueFields(this.GetType().FullName);
            }
        }


        /// <summary>
        /// Returns all foreign keys of mapped table for invoked class object
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> foreignKeys = null;
        ///        try {
        ///            foreignKeys = new Book().GetForeignKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All foreign keys of mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object</exception>
        public IEnumerator<String> GetForeignKeys()
        {

            if (model != null)
            {
                return DatabaseHelper.GetForeignKeys(model.GetType().FullName);
            }
            else
            {
                return DatabaseHelper.GetForeignKeys(this.GetType().FullName);
            }
        }

        /// <summary>
        /// Begins a transaction in EXCLUSIVE mode.
        /// <para>
        ///   Transactions can be nested. When the outer transaction is ended all of the work done in that transaction and all of the nested transactions will be committed or rolled back.
        ///   The changes will be rolled back if any transaction is ended without being marked as clean(by calling commitTransaction). Otherwise they will be committed.
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        DatabaseDescriptor databaseDescriptor = cBook.GetDatabaseDescriptor();
        ///
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        ///	
        ///            cBook.Save();
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
        /// <exception cref="Siminov.Core.ExceptionDatabaseException">If beginTransaction does not starts</exception>
        public static void BeginTransaction(DatabaseDescriptor databaseDescriptor)
        {
            DatabaseHelper.BeginTransaction(databaseDescriptor);
        }


        /// <summary>
        /// Marks the current transaction as successful
        /// Finally it will End a transaction.
        /// <para>
        /// Example: Make Book Object
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        DatabaseDescriptor databaseDescriptor = cBook.GetDatabaseDescriptor();
        /// 
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        /// 		
        ///            cBook.Save();
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
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to commit the transaction</exception>
        public static void CommitTransaction(DatabaseDescriptor databaseDescriptor)
        {
            DatabaseHelper.CommitTransaction(databaseDescriptor);
        }

        /// <summary>
        /// End the current transaction
        /// <para>
        /// Example:
        /// <code>
        ///        Book cBook = new Book();
        ///        cBook.SetTitle(Book.BOOK_TYPE_C);
        ///        cBook.SetDescription("c_description");
        ///        cBook.SetAuthor("c_author");
        ///        cBook.SetLink("c_link");
        ///        
        ///        DatabaseDescriptor databaseDescriptor = cBook.GetDatabaseDescriptor();
        ///  
        ///        try {
        ///            Database.BeginTransaction(databaseDescriptor);
        ///		
        ///            cBook.Save();
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
            DatabaseHelper.EndTransaction(databaseDescriptor);
        }
    }
}
