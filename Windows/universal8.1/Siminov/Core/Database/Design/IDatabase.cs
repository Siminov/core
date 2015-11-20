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



using Siminov.Core.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Core.Database.Design
{

    /// <summary>
    /// Exposes methods to deal with database. 
    /// It has methods to create, delete, and perform other common database management tasks.
    /// </summary>
    public interface IDatabase
    {


        /// <summary>
        /// Is used to create a new table in an database.
        /// <para>
        /// Using SIMINOV there are three ways to create table in database.
        /// </para>
        /// 
        /// <list type="bullet">
        /// <item>
        /// Describing table structure in form of ENTITY-DESCRIPTOR XML file. And creation of table will be handled by SIMINOV.
        /// <para>
        /// SIMINOV will parse each ENTITY-DESCRIPTOR XML defined by developer and create table's in database.
        /// <code>
        ///     <entity-descriptor>
        ///
        ///        <property name="table_name">LIQUOR</property>
        ///        <property name="class_name">Siminov.Core.Sample.Model.Liquor</property>
        /// 
        ///        <attributes>
        ///		
        ///            <attribute>
        ///                <property name="variable_name">liquorType</property>
        ///                <property name="column_name">LIQUOR_TYPE</property>
        ///                <property name="type">string</property>
        ///                <property name="primary_key">true</property>
        ///                <property name="not_null">true</property>
        ///                <property name="unique">true</property>
        ///            </attribute>		
        ///
        ///            <attribute>
        ///                <property name="variable_name">description</property>
        ///                <property name="column_name">DESCRIPTION</property>
        ///                <property name="type">string</property>
        ///            </attribute>
        ///
        ///            <attribute>
        ///                <property name="variable_name">history</property>
        ///                <property name="column_name">HISTORY</property>
        ///                <property name="type">string</property>
        ///            </attribute>
        ///
        ///            <attribute>
        ///                <property name="variable_name">link</property>
        ///                <property name="column_name">LINK</property>
        ///                <property name="type">string</property>
        ///                <property name="default">www.wikipedia.org</property>
        ///            </attribute>
        ///
        ///            <attribute>
        ///                <property name="variable_name">alcholContent</property>
        ///                <property name="column_name">ALCHOL_CONTENT</property>
        ///                <property name="type">String</property>
        ///            </attribute>
        ///
        ///            <indexes>
        ///                <index>
        ///                    <property name="name">LIQUOR_INDEX_BASED_ON_LINK</property>
        ///                    <property name="unique">true</property>
        ///                    <property name="column">HISTORY</property>
        ///                </index>
        ///            </indexes>
        ///
        ///            <relationships>
        ///
        ///                <relationship>
        ///                    <property name="type">one-to-many</property>
        ///                    <property name="refer">liquorBrands</property>
        ///                    <property name="refer_to">Siminov.Core.Sample.Model.LiquorBrand</property>
        ///                    <property name="on_update">cascade</property>
        ///                    <property name="on_delete">cascade</property>
        ///                    <property name="load">true</property>
        ///                </relationship>		
        ///		    
        ///            </relationships>
        ///
        ///    </entity-descriptor>		
        ///  
        /// </code>
        /// </para>
        /// </item>
        /// </list>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to create table in SQLite.</exception>
        void CreateTable();


        /// <summary>
        /// It drop's the table from database based on entity descriptor.
        /// <para>
        /// Drop the Liquor table.
        /// <code>
        ///        Liquor liquor = new Liquor();
        ///
        ///        try {
        ///            liquor.DropTable();
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// 
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop table.</exception>
        void DropTable();


        /// <summary>
        /// Is used to drop a index on a table in database.
        /// <para>
        /// Create Index On Liquor table.
        /// <code>
        ///        String indexName = "LIQUOR_INDEX_BASED_ON_LINK";
        ///        Liquor liquor = new Liquor();
        ///	
        ///        try {
        ///            liquor.DropIndex(indexName);
        ///        } catch(DatabaseException databaseException) {
        ///            //Log It.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="indexName">Name of a index needs to be drop.</param>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not able to drop index on table.</exception>
        void DropIndex(String indexName);


        /// <summary>
        /// Returns all tuples based on query from mapped table for invoked class object.
        /// <para>
        /// <code>
        ///        Liquor[] liquors = null;
        ///        try {
        ///            liquors = new Liquor().Select().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        ISelect Select();


        /// <summary>
        /// Returns all tuples based on manual query from mapped table for invoked class object.
        /// 
        /// <para>
        /// <code>
        ///        String query = "SELECT * FROM LIQUOR";
        ///
        ///        Liquor[] liquors = null;
        ///        try {
        ///            liquors = new Liquor().Select(query);
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        ///
        ///        } 			
        /// </code>
        /// </para>
        /// </summary>
        /// <param name="query">Query to get tuples from database.</param>
        /// <returns><c>ISelect </c>object.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting tuples from a single table.</exception>
        Object[] Select(String query);



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
        ///     
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        void Save();


        /// <summary>
        /// It updates a record to any single table in a relational database.
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
        /// 
        /// </code>
        /// </para>
        /// </summary>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        void Update();


        /// <summary>
        /// It finds out whether tuple exists in table or not.
        /// <para>
        /// IF NOT EXISTS:
        /// adds a record to any single table in a relational database.
        /// ELSE:
        /// updates a record to any single table in a relational database.
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
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        void SaveOrUpdate();


        /// <summary>
        /// It deletes a record from single table in a relational database.
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
        ///            beer.Delete();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while saving tuples in database.</exception>
        IDelete Delete();


        /// <summary>
        /// Returns the count of rows based on where clause provided.
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
        ///        int noOfBeers = 0;
        ///
        ///        try {
        ///            noOfBeers = beer.Count().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting count of tuples from database.</exception>
        ICount Count();


        /// <summary>
        /// Returns the average based on where clause provided.
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
        ///        int noOfBeers = 0;
        ///
        ///        try {
        ///            noOfBeers = beer.Avg().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting average from database.</exception>
        IAverage Avg();

        /**


<pre>



{@code


</pre>
 
@throws DatabaseException If any error occurs while getting sum from database.
 */

        /// <summary>
        /// Returns the sum based on where clause provided.
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
        ///        int noOfBeers = 0;
        ///
        ///        try {
        ///            noOfBeers = beer.Sum().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        ISum Sum();


        /// <summary>
        /// Returns the total based on where clause provided.
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
        ///        int totalBeers = 0;
        ///
        ///        try {
        ///            totalBeers = beer.Avg().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        ITotal Total();


        /// <summary>
        /// Returns the min based on where clause provided.
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
        ///        int minBeers = 0;
        ///
        ///        try {
        ///            minBeers = beer.Min().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting min from database.</exception>
        IMin Min();


        /// <summary>
        /// Returns the max based on where clause provided.
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
        ///        int maxBeers = 0;
        ///
        ///        try {
        ///            maxBeers = beer.Max().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting max from database.</exception>
        IMax Max();


        /// <summary>
        /// Returns the group concat based on where clause provided.
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
        ///        int groupConcatBeers = 0;
        ///
        ///        try {
        ///            groupConcatBeers = beer.GroupConcat().Execute();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns></returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occurs while getting group concat from database.</exception>
        IGroupConcat GroupConcat();


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
        /// <returns>Database Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If any error occur while getting database descriptor object.</exception>
        DatabaseDescriptor GetDatabaseDescriptor();


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
        /// <returns>Entity Descriptor Object</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If entity descriptor object not mapped for invoked class object.</exception>
        EntityDescriptor GetEntityDescriptor();


        /// <summary>
        /// Returns the mapped table name for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        String tableName = null;
        ///        try {
        ///            tableName = new Liquor().GetTableName();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>Mapped Table name.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        String GetTableName();


        /// <summary>
        /// Returns all column names of mapped table.
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
        /// <returns>All column names of mapped table.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IEnumerator<String> GetColumnNames();


        /// <summary>
        /// Returns all column values in the same order of column names for invoked class object.
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
        /// <returns>All column values for invoked object.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IDictionary<String, Object> GetColumnValues();


        /// <summary>
        /// Returns all columns with there data types for invoked class object.
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
        /// <returns>All columns with there data types</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IDictionary<String, String> GetColumnTypes();


        /// <summary>
        /// Returns all primary keys of mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> primaryKeys = null;
        ///        try {
        ///            primaryKeys = new Liquor().getPrimeryKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All primary keys</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If not mapped table found for invoked class object.</exception> 
        IEnumerator<String> GetPrimaryKeys();


        /// <summary>
        /// Returns all mandatory fields which are associated with mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> mandatoryFields = null;
        ///        try {
        ///            mandatoryFields = new Liquor().GetMandatoryFields();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All mandatory fields for mapped table</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IEnumerator<String> GetMandatoryFields();


        /// <summary>
        /// Returns all unique fields which are associated with mapped table for invoked class object.
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
        /// <returns>All unique fields for mapped table.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IEnumerator<String> GetUniqueFields();



        /// <summary>
        /// Returns all foreign keys of mapped table for invoked class object.
        /// <para>
        /// Example:
        /// <code>
        ///        IEnumerator<String> foreignKeys = null;
        ///        try {
        ///            foreignKeys = new Liquor().GetForeignKeys();
        ///        } catch(DatabaseException de) {
        ///            //Log it.
        ///        }
        /// </code>
        /// </para>
        /// </summary>
        /// <returns>All foreign keys of mapped table.</returns>
        /// <exception cref="Siminov.Core.Exception.DatabaseException">If no mapped table found for invoked class object.</exception>
        IEnumerator<String> GetForeignKeys();

    }
}
