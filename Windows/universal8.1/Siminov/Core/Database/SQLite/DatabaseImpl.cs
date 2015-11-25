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


#if __MOBILE__
#define XAMARIN
#endif

#if !__MOBILE__
#define WINDOWS
#endif


using Siminov.Core.Utils;
using Siminov.Core.Database.Design;
using Siminov.Core.Exception;
using Siminov.Core.Log;
using Siminov.Core.Model;
using SQLite;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace Siminov.Core.Database.Sqlite
{

    /// <summary>
    /// Provides IDatabase implementation for SQLite database.
    /// </summary>
    public class DatabaseImpl : IDatabaseImpl
    {

        private SQLiteConnection sqliteDatabase = null;

        public void OpenOrCreate(DatabaseDescriptor databaseDescriptor)
        {

            String databasePath = new DatabaseUtils().GetDatabasePath(databaseDescriptor);

            if (!FileUtils.DoesFolderExists(databasePath, FileUtils.LOCAL_FOLDER))
            {
                try
                {
                    FileUtils.CreateFolder(databasePath, FileUtils.LOCAL_FOLDER);
                }
                catch (System.Exception exception)
                {
                    Log.Log.Error(typeof(DatabaseImpl).FullName, "OpenOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName() + ", " + exception.Message);
                    throw new DeploymentException(typeof(DatabaseImpl).FullName, "OpenOrCreate", "Exception caught while creating database directories, DATABASE-PATH: " + databasePath + ", DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName() + ", " + exception.Message);
                }
            }

            String databaseName = databaseDescriptor.GetDatabaseName();
            if (databaseName == null || databaseName.Length <= 0)
            {
                Log.Log.Error(typeof(DatabaseImpl).FullName, "OpenOrCreate", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "OpenOrCreate", "DATABASE-NAME IS MANDATORY FIELD - DATABASE-DESCRIPTOR: " + databaseDescriptor.GetDatabaseName());
            }

            if (!databaseName.EndsWith(".db"))
            {
                databaseName = databaseName + ".db";
            }

            try
            {

                #if XAMARIN
                    String databaseURI = Path.Combine (databasePath, databaseName);
                    sqliteDatabase = new SQLiteConnection(databaseURI);
                #elif WINDOWS
                    sqliteDatabase = new SQLiteConnection(databasePath + databaseName);
                #endif
            }
            catch (SQLiteException sqliteException)
            {
                Log.Log.Error(typeof(DatabaseImpl).FullName, "OpenOrCreate", "SQLiteException caught while opening database, " + sqliteException.Message);
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "OpenOrCreate", "SQLiteException caught while opening database, " + sqliteException.Message);
            }
        }

        public void Close(DatabaseDescriptor databaseDescriptor)
        {
            sqliteDatabase.Close();
        }

        public void ExecuteQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query)
        {
            Log.Log.Debug(typeof(DatabaseImpl).FullName, "ExecuteQuery(" + query + ")", "QUERY: " + query);

            try
            {
                sqliteDatabase.Execute(query);
            }
            catch (SQLiteException sqliteException)
            {
                if (sqliteDatabase.IsInTransaction)
                {
                    sqliteDatabase.Rollback();
                }

                Log.Log.Error(typeof(DatabaseImpl).FullName, "ExecuteQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.Message);
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "ExecuteQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.Message);
            }
        }

        public void ExecuteBindQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query, IEnumerator<Object> columnValues)
        {
            Log.Log.Debug(typeof(DatabaseImpl).FullName, "ExecuteBindQuery", "QUERY: " + query);

            ICollection<Object> values = new List<Object>();
            while (columnValues.MoveNext())
            {
                values.Add(columnValues.Current);
            }

            try
            {
                sqliteDatabase.Execute(query, values.ToArray());
            }
            catch (SQLiteException sqliteException)
            {
                if (sqliteDatabase.IsInTransaction)
                {
                    sqliteDatabase.Rollback();
                }

                Log.Log.Error(typeof(DatabaseImpl).FullName, "ExecuteBindQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.Message);
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "ExecuteBindQuery(" + query + ")", "SQLiteException caught while executing query, QUERY: " + query + ", " + sqliteException.Message);
            }
        }

        public IEnumerator<IDictionary<String, Object>> ExecuteSelectQuery(DatabaseDescriptor databaseDescriptor, EntityDescriptor entityDescriptor, String query)
        {

            #if XAMARIN
                SQLitePCL.sqlite3_stmt statement = SQLite3.Prepare2(sqliteDatabase.Handle, query);
            #elif WINDOWS
                List<SQLiteQueryRow> statement = null;
            #endif

            try
            {

                #if XAMARIN
                    statement = SQLite3.Prepare2(sqliteDatabase.Handle, query);
                #elif WINDOWS
                    statement = sqliteDatabase.Query2(query);
                #endif
                
            }
            catch (System.Exception exception)
            {
                if (sqliteDatabase.IsInTransaction)
                {
                    sqliteDatabase.Rollback();
                }

                Log.Log.Error(typeof(Database).FullName, "ExecuteSelectQuery", "Exception caught while executing the select query, QUERY: " + query);
                throw new DatabaseException(typeof(Database).FullName, "ExecuteSelectQuery", exception.Message);
            }

            List<Dictionary<String, Object>> tuples = new List<Dictionary<String, Object>>();

            #if XAMARIN

                SQLite3.Result result;
			    while ((result = SQLite3.Step (statement)) == SQLite3.Result.Row)
			    {
				    IDictionary<String, Object> tuple = new Dictionary<String, Object>();

				    String stmtResult = result.ToString ();

				    //string[] names = SQLite3.ColType.GetNames ();
				    //string[] values = SQLite3.ColType.GetValues ();
				    int columnsCount = SQLite3.ColumnCount (statement);
				    for(int i = 0;i < columnsCount;i++) 
				    {
					    String columnName = SQLite3.ColumnName (statement, i);
					    SQLite3.ColType columnType = SQLite3.ColumnType (statement, i);
					    Object columnValue = SQLite3.ColumnText (statement, i);

					    bool isString = false;
					    bool isLong = false;
					    bool isFloat = false;
					    bool isBlob = false;

					    if (columnType == SQLite3.ColType.Text) 
					    {
						    isString = true;
					    } 
					    else if (columnType == SQLite3.ColType.Integer) 
					    {
						    isLong = true;
					    }
					    else if(columnType == SQLite3.ColType.Float) 
					    {
						    isFloat = true;
					    }
					    else if(columnType == SQLite3.ColType.Blob) 
					    {
						    isBlob = true;
					    }
					    else if(columnType == SQLite3.ColType.Null)
					    {
					    }


					    if (isString)
					    {
						
						    if (entityDescriptor != null)
						    {
							    EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

							    if (attribute != null)
							    {

								    if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, (String)columnValue);
								    }
								    else if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_BOOLEAN_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, columnValue.Equals(Boolean.TrueString.ToString()) ? true : false);
								    }
								    else if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_BOOLEAN_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, columnValue.Equals(Boolean.TrueString.ToString()) ? Boolean.TrueString : Boolean.FalseString);
								    }
								    else if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, (String)columnValue);
								    }
							    }
							    else
							    {
								    tuple.Add(columnName, columnValue);
							    }
						    }
						    else
						    {
							    tuple.Add(columnName, columnValue);
						    }
					    }
					    else if (isLong)
					    {

						    if (entityDescriptor != null)
						    {
							    EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

							    if (attribute != null)
							    {
								    if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_NUMBER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, columnValue);
								    }
							    }
							    else
							    {
								    tuple.Add(columnName, columnValue);
							    }
						    }
						    else
						    {
							    tuple.Add(columnName, columnValue);
						    }
					    }
					    else if (isFloat)
					    {

						    if (entityDescriptor != null)
						    {
							    EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

							    if (attribute != null)
							    {
								    if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_NUMBER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
								    {
									    tuple.Add(columnName, columnValue);
								    }
							    }
							    else
							    {
								    tuple.Add(columnName, columnValue);
							    }
						    }
						    else
						    {
							    tuple.Add(columnName, columnValue);
						    }

						    tuple.Add(columnName, columnValue);
					    }
					    else if (isBlob)
					    {
						    tuple.Add(columnName, columnValue);
					    }
				    }

				    tuples.Add((Dictionary<String, Object>)tuple);
			    }

			    SQLite3.Finalize(statement);

			    return tuples.GetEnumerator();

            #elif WINDOWS
                foreach (SQLiteQueryRow cursor in statement)
                {

                    IDictionary<String, Object> tuple = new Dictionary<String, Object>();

                    List<SQLiteQueryColumn> columns = cursor.column;
                    if (columns == null || columns.Count <= 0)
                    {
                        continue;
                    }

                    foreach (SQLiteQueryColumn column in columns)
                    {

                        String columnName = column.Key;
                        Object columnValue = column.Value;


                        bool isString = false;
                        bool isLong = false;
                        bool isFloat = false;
                        bool isBlob = false;

                        if (columnValue != null)
                        {

                            if (columnValue.GetType().FullName.Equals(typeof(String).FullName, StringComparison.OrdinalIgnoreCase))
                            {
                                isString = true;
                            }
                            else if (columnValue.GetType().FullName.Equals(typeof(long).FullName, StringComparison.OrdinalIgnoreCase))
                            {
                                isLong = true;
                            }
                            else if (columnValue.GetType().FullName.Equals(typeof(float).FullName, StringComparison.OrdinalIgnoreCase))
                            {
                                isFloat = true;
                            }
                            else if (columnValue.GetType().FullName.Equals(typeof(byte).FullName, StringComparison.OrdinalIgnoreCase))
                            {
                                isBlob = true;
                            }
                        }
                        else
                        {
                            isString = true;
                        }


                        if (isString)
                        {

                            if (entityDescriptor != null)
                            {
                                EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

                                if (attribute != null)
                                {

                                    if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, (String)columnValue);
                                    }
                                    else if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_BOOLEAN_PRIMITIVE_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, columnValue.Equals(Boolean.TrueString.ToString()) ? true : false);
                                    }
                                    else if (attribute.GetType().Equals(IDataTypeHandler.CSHARP_BOOLEAN_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, columnValue.Equals(Boolean.TrueString.ToString()) ? Boolean.TrueString : Boolean.FalseString);
                                    }
                                    else if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_STRING_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, (String)columnValue);
                                    }
                                }
                                else
                                {
                                    tuple.Add(columnName, columnValue);
                                }
                            }
                            else
                            {
                                tuple.Add(columnName, columnValue);
                            }
                        }
                        else if (isLong)
                        {

                            if (entityDescriptor != null)
                            {
                                EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

                                if (attribute != null)
                                {
                                    if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_NUMBER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, columnValue);
                                    }
                                }
                                else
                                {
                                    tuple.Add(columnName, columnValue);
                                }
                            }
                            else
                            {
                                tuple.Add(columnName, columnValue);
                            }
                        }
                        else if (isFloat)
                        {

                            if (entityDescriptor != null)
                            {
                                EntityDescriptor.Attribute attribute = entityDescriptor.GetAttributeBasedOnColumnName(columnName);

                                if (attribute != null)
                                {
                                    if (attribute.GetType().Equals(IDataTypeHandler.JAVASCRIPT_NUMBER_DATA_TYPE, StringComparison.OrdinalIgnoreCase))
                                    {
                                        tuple.Add(columnName, columnValue);
                                    }
                                }
                                else
                                {
                                    tuple.Add(columnName, columnValue);
                                }
                            }
                            else
                            {
                                tuple.Add(columnName, columnValue);
                            }

                            tuple.Add(columnName, columnValue);
                        }
                        else if (isBlob)
                        {
                            tuple.Add(columnName, columnValue);
                        }
                    }

                    tuples.Add((Dictionary<String, Object>)tuple);
                }


                return tuples.GetEnumerator();
            #endif

        }

        public void ExecuteMethod(String methodName, Object parameter)
        {
            MethodInfo method = null;

            try
            {
                method = sqliteDatabase.GetType().GetTypeInfo().GetDeclaredMethod(methodName);
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(DatabaseImpl).FullName, "ExecuteMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.Message);
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "ExecuteMethod", "Exception caught while getting method, METHOD-NAME: " + methodName + ", " + exception.Message);
            }

            try
            {
                method.Invoke(sqliteDatabase, new Object[] { });
            }
            catch (System.Exception exception)
            {
                Log.Log.Error(typeof(DatabaseImpl).FullName, "ExecuteMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.Message);
                throw new DatabaseException(typeof(DatabaseImpl).FullName, "ExecuteMethod", "Exception caught while getting return value from method, METHOD-NAME: " + methodName + ", " + exception.Message);
            }
        }
    }
}
