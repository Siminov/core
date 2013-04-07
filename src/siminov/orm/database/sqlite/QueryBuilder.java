/** 
 * [SIMINOV FRAMEWORK]
 * Copyright [2013] [Siminov Software Solution|support@siminov.com]
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

package siminov.orm.database.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

import siminov.orm.Constants;
import siminov.orm.database.design.IQueryBuilder;
import siminov.orm.exception.DatabaseException;
import siminov.orm.exception.DeploymentException;
import siminov.orm.exception.SiminovException;
import siminov.orm.log.Log;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.model.DatabaseMappingDescriptor.Column;
import siminov.orm.model.DatabaseMappingDescriptor.Relationship;
import siminov.orm.resource.Resources;
import android.text.TextUtils;


public class QueryBuilder implements Constants, IQueryBuilder {

	/**
	 * It generates query to create table in database.
	 * 
	 * @param tableName Name of table.
	 * @param columnNames All column names needed in table.
	 * @param columnTypes All column types, It can be TEXT, INTEGER, LONG, BLOG, FLOAT
	 * @param defaultValues Default value columns.
	 * @param checks Constraint needed on column value.
	 * @param primaryKeys Primary keys needed in table.
	 * @param isNotNull Weather column can contain empty value or not.
	 * @param uniqueColumns Weather column values should be unique or not.
	 * @param foreignKeys Foreign key contained in table.
	 * @return Generated query.
	 */
	public String formCreateTableQuery(final String tableName, final Iterator<String> columnNames, final Iterator<String> columnTypes, final Iterator<String> defaultValues, final Iterator<String> checks, final Iterator<String> primaryKeys, final Iterator<Boolean> isNotNull, final Iterator<String> uniqueColumns, final String foreignKeys) {
		
		StringBuilder query = new StringBuilder();
			query.append("CREATE TABLE IF NOT EXISTS " + tableName + " (");
			
			int index = 0;
			while(columnNames.hasNext() && columnTypes.hasNext() && isNotNull.hasNext() && defaultValues.hasNext() && checks.hasNext()) {
				String columnName = columnNames.next();
				String columnType = columnTypes.next();
				
				boolean notNull = isNotNull.next();
				
				String defaultValue = defaultValues.next();
				String check = checks.next();
				
				if(index == 0) {
					query.append(columnName + " " + columnType);
				} else {
					query.append(", " + columnName + " " + columnType);
				}
				
				if(notNull) {
					query.append(" " + "NOT NULL");
				}
				
				if(defaultValue != null && defaultValue.length() > 0) {
					query.append(" DEFAULT '" + defaultValue + "' "); 
				}
					
				if(check != null && check.length() > 0) {
					query.append(" CHECK('" + check + "')"); 
				}
				
				index++;
			}

			StringBuilder primaryKey = new StringBuilder();
			
			index = 0;

			boolean isPrimaryKeysPresent = false;
			primaryKey.append("PRIMARY KEY(");
			while(primaryKeys.hasNext()) {
				
				if(index == 0) {
					primaryKey.append(primaryKeys.next());
					isPrimaryKeysPresent = true;
				} else {
					primaryKey.append(", " + primaryKeys.next());
				}
				
				index++;
			}
			primaryKey.append(")");
			
			StringBuilder uniqueColumn = new StringBuilder();
			
			index = 0;

			boolean isUniqueKeysPresent = false;
			uniqueColumn.append("UNIQUE (");
			while(uniqueColumns.hasNext()) {
				
				if(index == 0) {
					uniqueColumn.append(uniqueColumns.next());
					isUniqueKeysPresent = true;
				} else {
					uniqueColumn.append(", " + uniqueColumns.next());
				}
				
				index++;
			}
			
			uniqueColumn.append(")");

			if(isPrimaryKeysPresent) {
				query.append(", " + primaryKey);
			}
	
			if(isUniqueKeysPresent) {
				query.append(", " + uniqueColumn);
			}
			
			if(foreignKeys.length() > 0) {
				query.append(", " + foreignKeys);
			}
			
			query.append(")");
		
		return query.toString();
 	}
	
	/**
	 * It generates query to create index on table specified.
	 * 
	 * @param indexName Name of index.
	 * @param tableName Name of table on which index is required.
	 * @param columnNames Column names needed in index.
	 * @param isUnique true/false whether index needs to be unique or not. (A unique index guarantees that the index key contains no duplicate values and therefore every row in the table is in some way unique.)
	 * @return Generated query.
	 */
	public String formCreateIndexQuery(final String indexName, final String tableName, final Iterator<String> columnNames, final boolean isUnique) {
		
		StringBuilder query = new StringBuilder();
			if(isUnique) {
				query.append(" CREATE UNIQUE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + "(");
			} else {
				query.append(" CREATE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + "(");
			}
			
			int index = 0;
			while(columnNames.hasNext()) {
				if(index == 0) {
					query.append(columnNames.next());
				} else {
					query.append(", " + columnNames.next());
				}
				
				index++;
			}
			
			query.append(")");
		return query.toString();
	}
	
	/**
	 * It generates query to drop table from database.
	 * 
	 * @param tableName Name of table.
	 * @return Generated query.
	 */
	public String formDropTableQuery(final String tableName) {
		
		StringBuilder query = new StringBuilder();
			query.append("DROP TABLE IF EXISTS " + tableName);
		
		return query.toString();
	}

	/**
	 * It generates query to drop index from table.
	 * 
	 * @param tableName Name of table.
	 * @param indexName Name of index.
	 * @return Generated query.
	 */
	public String formDropIndexQuery(String tableName, String indexName) {
		
		StringBuilder query = new StringBuilder();
			query.append("DROP INDEX IF EXISTS " + indexName + " ON " + tableName);
			
		return query.toString();
	}
	
	/**
	 * It generated query to fetch tuples from table.
	 * 
	 * @param tableName Name of table.
	 * @param whereClause Condition based on tuples need to fetch.
	 * @param columnNames Column names.
	 * @param groupBys Group by clause.
	 * @param having Having clause.
	 * @param orderBy Order by column names.
	 * @param limit Limit of tuples needed.
	 * @return Generated query.
	 */
	public String formSelectQuery(final String tableName, final boolean distinct, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBys, final String having, final Iterator<String> orderBy, final String whichOrderBy, final String limit) {

		StringBuilder groupBysBuilder = new StringBuilder();
		
		int index = 0;
		if(groupBys != null) {
			while(groupBys.hasNext()) {
				if(index == 0) {
					groupBysBuilder.append(groupBys.next());
				} else {
					groupBysBuilder.append(", " + groupBys.next());
				}
				
				index++;
			}
		}
		
		StringBuilder orderBysBuilder = new StringBuilder();
		
		index = 0;
		if(orderBy != null) {
			while(orderBy.hasNext()) {
				if(index == 0) {
					orderBysBuilder.append(orderBy.next());
				} else {
					orderBysBuilder.append(", " + orderBy.next());	
				}
				
				index++;
			}
		}

		return formSelectQuery(tableName, distinct, whereClause, columnNames, groupBysBuilder.toString(), having, orderBysBuilder.toString(), whichOrderBy, limit);
	}
	
	private String formSelectQuery(final String table, final boolean distinct, final String whereClause, final Iterator<String> columnsNames, final String groupBys, final String having, final String orderBys, final String whichOrderBy, final String limit) {
        if (TextUtils.isEmpty(groupBys) && !TextUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        
        Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
        if (!TextUtils.isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if(distinct) {
            query.append("DISTINCT ");
        } else {
        	query.append("* ");
        }
        
        if(columnsNames != null) {
            if (columnsNames.hasNext()) {
                appendColumns(query, columnsNames);
            }
        }
        
        query.append("FROM ");
        query.append(table);
        appendClause(query, " WHERE ", whereClause);
        appendClause(query, " GROUP BY ", groupBys);
        appendClause(query, " HAVING ", having);
        
        if(whichOrderBy != null && whichOrderBy.length() > 0) {
            appendClause(query, " ORDER BY ", orderBys + " " + whichOrderBy);
        } else {
            appendClause(query, " ORDER BY ", orderBys);        	
        }

        appendClause(query, " LIMIT ", limit);

        return query.toString();
    }

    private static void appendClause(final StringBuilder s, final String name, final String clause) {
        if (!TextUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }
	
    private static void appendColumns(final StringBuilder s, final Iterator<String> columns) {
        
    	int index = 0;
    	while(columns.hasNext()) {
            String column = columns.next();

            if (column != null) {
                if (index > 0) {
                    s.append(", ");
                }
                
                s.append(column);
                index++;
            }
        }

        s.append(' ');
    }

	/**
	 * It generates bind query to insert tuple in table.
	 * 
	 * @param tableName Name of table.
	 * @param columnNames Column names.
	 * @return Generated query.
	 */
	public String formSaveBindQuery(final String tableName, final Iterator<String> columnNames) {
		
		StringBuilder query = new StringBuilder();
			query.append("INSERT INTO " + tableName + "(");

			int index = 0;
			while(columnNames.hasNext()) {
				if(index == 0) {
					query.append(columnNames.next());
				} else {
					query.append(", " + columnNames.next());
				}
				
				index++;
			}
			
			query.append(") VALUES(");
			
			for(int i = 0;i < index;i++) {
				if(i == 0) {
					query.append("?");
				} else {
					query.append(", ?"); 
				}
			}
			
			query.append(")");
		return query.toString();
	}

	
	/**
	 * It generates bind query to update tuple in table.
	 * 
	 * @param tableName Name of table.
	 * @param columnNames Column names.
	 * @param whereClause Condition of which tuple need to be update.
	 * @return Generated query.
	 */
	public String formUpdateBindQuery(final String tableName, final Iterator<String> columnNames, final String whereClause) {
		
		StringBuilder query = new StringBuilder();
			query.append("UPDATE " + tableName + " SET ");
			
			int index = 0;
			while(columnNames.hasNext()) {
				if(index == 0) {
					query.append(columnNames.next() + "= ?");
				} else {
					query.append(", " + columnNames.next() + "= ?");
				}
				
				index++;
			}
	
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" WHERE " + whereClause);
			}
			
		return query.toString();
	}

	
	/**
	 * It generates query to delete tuple from table.
	 * 
	 * @param tableName Name of table.
	 * @param whereClause Condition on which tuples need to delete.
	 * @return Generated query.
	 */
	public String formDeleteQuery(final String tableName, final String whereClause) {
		
		StringBuilder query = new StringBuilder();
			query.append("DELETE FROM " + tableName);
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" WHERE " + whereClause);
			}
			
		return query.toString();
	}

	
	/**
	 * It generated query to get count of tuples from table based on condition provided.
	 * 
	 * @param tableName Name of table.
	 * @param whereClause Condition on which count needed.
	 * @return Generated query.
	 */
	public String formCountQuery(final String tableName, final String column, final boolean distinct, final String whereClause, final Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			if(column != null && column.length() > 0) {
				if(distinct) {
					query.append("SELECT COUNT(DISTINCT " + column + " ) FROM " + tableName);					
				} else {
					query.append("SELECT COUNT(" + column + ") FROM " + tableName);
				}
			} else {
				query.append("SELECT COUNT(*) FROM " + tableName);
			}
		
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}

	
	/**
	 * It generates query to get average of column.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which average needed.
	 * @return Generated query.
	 */
	public String formAvgQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having) {

		StringBuilder query = new StringBuilder();
			query.append("SELECT AVG(" + column + ")" + " FROM " + tableName);
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}
	
	/**
	 * It generates query to get sum of column values.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which sum needed.
	 * @return Generated query.
	 */
	public String formSumQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			query.append("SELECT SUM(" + column + ")" + " FROM " + tableName);
			
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}
	

	/**
	 * It generates query to get total of a column.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which total needed.
	 * @return Generated query.
	 */
	public String formTotalQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			query.append("SELECT TOTAL(" + column + ")" + " FROM " + tableName);
			
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}

	/**
	 * It generates query to get maximum value of column based on group.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which maximum value needed.
	 * @param groupBy Group by clause.
	 * @return Generated query.
	 */
	public String formMaxQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			query.append("SELECT MAX(" + column + ")" + " FROM " + tableName);
			
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}

	
	/**
	 * It generates query to get minimum value of column based on group by clause.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which minimum value needed.
	 * @param groupBy Group by clause.
	 * @return Generated query.
	 */
	public String formMinQuery(final String tableName, final String column, final String whereClause, final Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			query.append("SELECT MIN(" + column + ")" + " FROM " + tableName);
			
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" " + " WHERE " + whereClause);
			}

			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);
			
		return query.toString();
	}

	
	/**
	 * It generates query to get group concat value.
	 * 
	 * @param tableName Name of table.
	 * @param columnName Column name of which group concat needed.
	 * @param delimiter Delimiter value.
	 * @param whereClause Condition on which group concat needed.
	 * @return Generated query.
	 */
	public String formGroupConcatQuery(final String tableName, final String column, final String delimiter, final String whereClause, Iterator<String> groupBys, final String having) {
		
		StringBuilder query = new StringBuilder();
			if(delimiter == null || delimiter.length() <= 0) {
				query.append("SELECT GROUP_CONCAT(" + column + ")" + " FROM " + tableName);
			} else {
				query.append("SELECT GROUP_CONCAT(" + column + ", " + delimiter + ")" + " FROM " + tableName);
			}
		
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" WHERE " + whereClause);
			} 
			
			StringBuilder groupBysBuilder = new StringBuilder();
			
			int index = 0;
			if(groupBys != null) {
				while(groupBys.hasNext()) {
					if(index == 0) {
						groupBysBuilder.append(groupBys.next());
					} else {
						groupBysBuilder.append(", " + groupBys.next());
					}
					
					index++;
				}
			}

	        appendClause(query, " GROUP BY ", groupBysBuilder.toString());
	        appendClause(query, " HAVING ", having);

		return query.toString();
	}


	
	/*
	 * Make Trigger On Update Cascade Query.
	 * 
	 * REF:
	 * 	CREATE TRIGGER update_customer_address UPDATE OF address ON customers 
  	 *	BEGIN
     *	UPDATE orders SET address = new.address WHERE customer_name = old.name;
  	 *	END;
	 */
	
	/**
	 * It generates query to create trigger to put cascade update constraint on table.
	 * 
	 * @param triggerName Name of trigger needed.
	 * @param parentTable Name of parent table.
	 * @param childTable Name of child table.
	 * @param parentKeys Column names of parent table.
	 * @param childKeys Column names of child table.
	 * @return Generated query.
	 */
	private String formTriggerOnUpdateCascade(final String triggerName, final String parentTable, final String childTable, final Iterator<String> foreignKeys) {
		
		StringBuilder query = new StringBuilder();
			query.append("CREATE TRIGGER IF NOT EXISTS " + triggerName + " UPDATE OF ");
				
			Collection<String> duplicateForeignKeys = new LinkedList<String>();
			
			int index = 0;
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				
				if(index == 0) {
					query.append(foreignKey);
				} else {
					query.append(", " + foreignKey);
				}
				
				duplicateForeignKeys.add(foreignKey);
				index++;
			}
			
			Iterator<String> duplicateForeignKeysIterator = duplicateForeignKeys.iterator();
			
			query.append(" ON " + parentTable);
			query.append(" BEGIN ");
			query.append("UPDATE " + childTable);

			index = 0;
			while(duplicateForeignKeysIterator.hasNext()) {
				String foreignKey = duplicateForeignKeysIterator.next();
				
				if(index == 0) {
					query.append(" SET " + foreignKey + "=" + "NEW" + "." + foreignKey);
				} else {
					query.append(", " + foreignKey + "=" + "NEW" + "." + foreignKey);
				}
				
				index++;
			}
			
			duplicateForeignKeysIterator = duplicateForeignKeys.iterator();
			
			index = 0;
			while(duplicateForeignKeysIterator.hasNext()) {
				String foreignKey = duplicateForeignKeysIterator.next();
				
				if(index == 0) {
					query.append(" WHERE " + foreignKey + "=" + "OLD" + "." + foreignKey);
				} else {
					query.append(" AND " + foreignKey + "=" + "OLD" + "." + foreignKey);
				}
				
				index++;
			}
			
			query.append(";");
			query.append(" END;");
			
		return query.toString();
	}
	
	/*
	 * Make Trigger On Delete Or Update Set Null Query.
	 * 
	 * REF:
	 * 	CREATE TRIGGER update_customer_address UPDATE OF address ON customers 
  	 *	BEGIN
     *	UPDATE orders SET address = null WHERE customer_name = old.name;
  	 *	END;
	 */
	

	/**
	 * It generates query to create trigger to put set null update constraint on table.
	 * 
	 * @param triggerName Name of trigger needed.
	 * @param parentTable Name of parent table.
	 * @param childTable Name of child table.
	 * @param parentKeys Column names of parent table.
	 * @param childKeys Column names of child table.
	 * @return Generated query.
	 */
	private String formTriggerOnDeleteOrUpdateSetNull(final String triggerName, final String parentTable, final String childTable, final Iterator<String> foreignKeys) {
		
		StringBuilder query = new StringBuilder();
			query.append("CREATE TRIGGER IF NOT EXISTS " + triggerName + " UPDATE OF ");
			
			Collection<String> duplicateForeignKeys = new LinkedList<String>();
			
			int index = 0;
			while(foreignKeys.hasNext()) {
				String parentKey = foreignKeys.next();
				
				if(index == 0) {
					query.append(parentKey);
				} else {
					query.append(", " + parentKey);
				}
				
				duplicateForeignKeys.add(parentKey);
				index++;
			}
		
			Iterator<String> duplicateForeignKeysIterator = duplicateForeignKeys.iterator();
			
			query.append(" ON " + parentTable);
			query.append(" BEGIN ");
			query.append("UPDATE " + childTable);
			
			index = 0;
			while(duplicateForeignKeysIterator.hasNext()) {
				String foreignKey = duplicateForeignKeysIterator.next();
				
				if(index == 0) {
					query.append(" SET " + foreignKey + "=" + "NULL");
				} else {
					query.append(", " + foreignKey + "=" + "NULL");
				}

				index++;
			}
			
			index = 0;
			while(duplicateForeignKeysIterator.hasNext()) {
				String foreignKey = duplicateForeignKeysIterator.next();
				
				if(index == 0) {
					query.append(" WHERE " + foreignKey + "=" + "OLD" + "." + foreignKey);
				} else {
					query.append(" AND " + foreignKey + "=" + "OLD" + "." + foreignKey);
				}
				
				index++;
			}
			
			query.append(";");
			query.append(" END;");
			
		return query.toString();
	}

	
	/*
	 * Make Trigger On Update Enforce Referential Integrity Query.
	 * 
	 * REF:
	 * 	CREATE TRIGGER trigger_name BEFORE UPDATE ON child_table BEGIN 
	 *	SELECT CASE 
	 *	WHEN ((SELECT parent_table . primary_key FROM parent_table WHERE parent_table . primary_key = NEW. foreign_key ) ISNULL) 
	 *	THEN RAISE(ABORT, 'Error Message') 
	 *	END; 
	 *	END;
	 */
	
	/**
	 * It generates query to create trigger to put update referential integrity.
	 * 
	 * @param triggerName Name of table.
	 * @param parentTable Name of parent table.
	 * @param childTable Name of child table.
	 * @param parentKeys Column names of parent table.
	 * @param childKeys Column names of child table.
	 * @return Generated query.
	 */
	private String formTriggerOnUpdateEnforceReferentialIntegrity(final String triggerName, final String parentTable, final String childTable, final Iterator<String> foreignKeys) {

		StringBuilder query = new StringBuilder();
			query.append("CREATE TRIGGER IF NOT EXISTS " + triggerName + " "); 
			query.append("BEFORE UPDATE ON " + childTable + " BEGIN ");
			query.append("SELECT CASE ");
			
			int index = 0;
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				
				if(index == 0) {
					query.append(" WHEN ((SELECT " + foreignKey + " ");
					
					query.append(" FROM " + parentTable + " ");
					query.append(" WHERE ");
					
					query.append(foreignKey + "=" + "NEW" + "." + foreignKey);
				} else {
					query.append(" AND " + foreignKey + "=" + "NEW" + "." + foreignKey);
				}
				
				index++;
			}
			
			query.append(") " + QUERY_BUILDER_IS_NULL + ")");
			query.append(" THEN RAISE(ABORT, 'UPDATE ON TABLE " + childTable + " VOILATES FOREIGN KEY UPDATE ON TABLE')");
			query.append(" END;");
			query.append(" END;");
			
		return query.toString();
	}

	
	/*
	 * Make Trigger On Insert Referential Integrity Query.
	 * 
	 * REF:
	 * 	CREATE TRIGGER trigger_name BEFORE INSERT ON child_table BEGIN 
	 *	SELECT CASE 
	 *	WHEN ((SELECT parent_table . primary_key FROM parent_table WHERE parent_table . primary_key = NEW. foreign_key ) ISNULL) 
	 *	THEN RAISE(ABORT, 'Error Message') 
	 *	END; 
	 *	END;
	 */
	

	/**
	 * It generates query to create trigger to put delete referential integrity.
	 * 
	 * @param triggerName Name of table.
	 * @param parentTable Name of parent table.
	 * @param childTable Name of child table.
	 * @param parentKeys Column names of parent table.
	 * @param childKeys Column names of child table.
	 * @return Generated query.
	 */
	private String formTriggerOnInsertEnforceReferentialIntegrity(final String triggerName, final String parentTable, final String childTable, final Iterator<String> foreignKeys) {

		StringBuilder query = new StringBuilder();
			query.append("CREATE TRIGGER IF NOT EXISTS " + triggerName + " "); 
			query.append("BEFORE INSERT ON " + childTable + " BEGIN ");
			query.append("SELECT CASE ");

			int index = 0;
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				
				if(index == 0) {
					query.append(" WHEN ((SELECT " + foreignKey + " ");
					
					query.append(" FROM " + parentTable + " ");
					query.append(" WHERE ");
					
					query.append(foreignKey + "=" + "NEW" + "." + foreignKey);
				} else {
					query.append(" AND " + foreignKey + "=" + "NEW" + "." + foreignKey);
				}
				
				index++;
			}
			
			query.append(") " + QUERY_BUILDER_IS_NULL + ")");
			query.append(" THEN RAISE(ABORT, 'INSERT ON TABLE " + childTable + " VOILATES FOREIGN KEY UPDATE ON TABLE')");
			query.append(" END;");
			query.append(" END;");
			
		return query.toString();
	}
	
	/*
	 * Make Trigger On Delete Cascade Query.
	 * 
	 * REF: 
	 *	CREATE TRIGGER trigger_name 
	 *	BEFORE DELETE ON parent_table 
	 *	FOR EACH ROW BEGIN 
	 *	DELETE FROM child_table WHERE child_table.foreign_key = OLD. primary_key ; 
	 *	END;
	 */
	

	/**
	 * It generates query to create trigger to put cascade delete constraint on table.
	 * 
	 * @param triggerName Name of trigger needed.
	 * @param parentTable Name of parent table.
	 * @param childTable Name of child table.
	 * @param parentKeys Column names of parent table.
	 * @param childKeys Column names of child table.
	 * @return Generated query.
	 */
	private String formTriggerOnDeleteCascade(final String triggerName, final String parentTable, final String childTable, final Iterator<String> foreignKeys) {
		
		StringBuilder query = new StringBuilder();
			query.append("CREATE TRIGGER IF NOT EXISTS " + triggerName + " ");
			query.append("BEFORE DELETE ON " + parentTable + " ");
			query.append("FOR EACH ROW BEGIN ");
			query.append("DELETE FROM " + childTable);
			
			int index = 0;
			while(foreignKeys.hasNext()) {
				String foreignKey = foreignKeys.next();
				
				if(index == 0) {
					query.append(" WHERE " + childTable + "." + foreignKey + "=" + "OLD" + "." + foreignKey);
				} else {
					query.append(" AND " + childTable + "." + foreignKey + "=" + "OLD" + "." + foreignKey);
				}
				
				index++;
			}
			
			query.append(" ;");
			query.append(" END;");
			
		return query.toString();
	}

	/**
	 * It generates query to create trigger based on parameters provided.
	 * 
	 * @param childTable Name of child table.
	 * @param relationships References provided to create triggers.
	 * @return Generated query.
	 */
	public Iterator<String> formTriggers(final DatabaseMappingDescriptor child) {
		Collection<String> triggers = new LinkedList<String>();
		
		Iterator<DatabaseMappingDescriptor.Relationship> manyToOneRelationships = child.getManyToOneRelationships();
		while(manyToOneRelationships.hasNext()) {
			Relationship oneToManyRelationship = manyToOneRelationships.next();
			Iterator<String> trigger = formTriggers(child, oneToManyRelationship);
			
			while(trigger.hasNext()) {
				triggers.add(trigger.next());
			}
		}
		
		Iterator<DatabaseMappingDescriptor.Relationship> manyToManyRelationships = child.getManyToManyRelationships();
		while(manyToManyRelationships.hasNext()) {
			Relationship oneToManyRelationship = manyToManyRelationships.next();
			Iterator<String> trigger = formTriggers(child, oneToManyRelationship);
			
			while(trigger.hasNext()) {
				triggers.add(trigger.next());
			}
		}

		return triggers.iterator();
	}

	private Iterator<String> formTriggers(final DatabaseMappingDescriptor child, final Relationship relationship) {
		Collection<String> triggers = new LinkedList<String>();
		String childName = child.getTableName();

		String onDeleteAction = relationship.getOnDelete();
		String onUpdateAction = relationship.getOnUpdate();

		DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
		if(referedDatabaseMappingDescriptor == null) {
			try {
				referedDatabaseMappingDescriptor = Resources.getInstance().requiredDatabaseMappingDescriptorBasedOnClassName(relationship.getReferTo());
				relationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
			} catch(SiminovException siminovException) {
				Log.loge(QueryBuilder.class.getName(), "formTriggers", "SiminovException caught while getting required database mapping descriptor, CLASS-NAME: " + relationship.getReferTo() + ", " + siminovException.getMessage());
				throw new DeploymentException(QueryBuilder.class.getName(), "formTriggers", siminovException.getMessage());
			}
			
			relationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
		}

		
		String parentTable = referedDatabaseMappingDescriptor.getTableName();
		
		Iterator<Column> columns = referedDatabaseMappingDescriptor.getColumns();
		Collection<String> foreignKeys = new LinkedList<String>();

		while(columns.hasNext()) {
			Column column = columns.next();
			
			boolean isPrimary = column.isPrimaryKey();
			if(isPrimary) {
				foreignKeys.add(column.getColumnName());
			}
		}

		if(onDeleteAction != null && onDeleteAction.length() > 0) {
			if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_CASCADE)) {
				String triggerName = parentTable + "_ON_DELETE_CASCADE_" + childName;

				String trigger = formTriggerOnDeleteCascade(triggerName, parentTable, childName, foreignKeys.iterator());
				triggers.add(trigger);
			} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_RESTRICT)) {
			} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_NO_ACTION)) {
			} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_NULL)) {
				String triggerName = parentTable + "_ON_DELETE_SET_NULL_" + childName;

				String trigger = formTriggerOnDeleteOrUpdateSetNull(triggerName, parentTable, childName, foreignKeys.iterator());
				triggers.add(trigger);
			} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_DEFAULT)) {
			}
		}

		if(onUpdateAction != null && onUpdateAction.length() > 0) {
			if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_CASCADE)) {
				String triggerName = parentTable + "_ON_UPDATE_CASCADE_" + childName;

				String trigger = formTriggerOnUpdateCascade(triggerName, parentTable, childName, foreignKeys.iterator());
				triggers.add(trigger);
			} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_RESTRICT)) {
			} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_NO_ACTION)) {
			} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_NULL)) {
				String triggerName = parentTable + "_ON_UPDATE_SET_NULL_" + childName;

				String trigger = formTriggerOnDeleteOrUpdateSetNull(triggerName, parentTable, childName, foreignKeys.iterator());
				triggers.add(trigger);
			} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_DEFAULT)) {
			}
		}

		/*
		 * On Update Enforce Referential Integrity.
		 */
		String triggerName = parentTable + "_ON_UPDATE_ENFORCE_REFERENTIAL_INTEGRITY_" + childName;
		String trigger = formTriggerOnUpdateEnforceReferentialIntegrity(triggerName, parentTable, childName, foreignKeys.iterator());
		triggers.add(trigger);

		/*
		 * On Insert Enforce Referential Integrity.
		 */
		triggerName = parentTable + "_ON_INSERT_ENFORCE_REFERENTIAL_INTEGRITY_" + childName;
		trigger = formTriggerOnInsertEnforceReferentialIntegrity(triggerName, parentTable, childName, foreignKeys.iterator());
		triggers.add(trigger);
		
		return triggers.iterator();
	}


	/**
	 * It generates query to create foreign keys in table.
	 * 
	 * @param relationships References provided to create foreign key.
	 * @return  Generated query.
	 */
	public String formForeignKeys(final DatabaseMappingDescriptor child) {
		
		Iterator<Relationship> oneToManyRealtionships = child.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = child.getManyToManyRelationships();
		
		Collection<Relationship> relationships = new ArrayList<Relationship>();
		
		while(oneToManyRealtionships.hasNext()) {
			Relationship relationship = oneToManyRealtionships.next();
			
			relationships.add(relationship);
		}
		
		while(manyToManyRealtionships.hasNext()) {
			Relationship relationship = manyToManyRealtionships.next();
			
			relationships.add(relationship);
		}
		
		StringBuilder foreignKeysQuery = new StringBuilder();
		Iterator<Relationship> relationshipsIterator = relationships.iterator();
			while(relationshipsIterator.hasNext()) {
				
				StringBuilder foreignKeyQuery = new StringBuilder(); 
				Relationship relationship = relationshipsIterator.next();
				
				DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
				if(referedDatabaseMappingDescriptor == null) {
					try {
						referedDatabaseMappingDescriptor = Resources.getInstance().requiredDatabaseMappingDescriptorBasedOnClassName(relationship.getReferTo());
						relationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
					} catch(SiminovException siminovException) {
						Log.loge(QueryBuilder.class.getName(), "formForeignKeys", "SiminovException caught while getting required database mapping descriptor, CLASS-NAME: " + relationship.getReferTo() + ", " + siminovException.getMessage());
						throw new DeploymentException(QueryBuilder.class.getName(), "formForeignKeys", siminovException.getMessage());
					}
					
					relationship.setReferedDatabaseMappingDescriptor(referedDatabaseMappingDescriptor);
				}

				
				String parentTable = referedDatabaseMappingDescriptor.getTableName();
				Collection<Column> foreignColumns = null;
				try {
					foreignColumns = getForeignKeys(referedDatabaseMappingDescriptor);
				} catch(DatabaseException databaseException) {
					Log.loge(QueryBuilder.class.getName(), "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.getMessage());
					throw new DeploymentException(QueryBuilder.class.getName(), "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.getMessage());
				}
				
				Iterator<Column> foreignColumnsIterate = foreignColumns.iterator();
				
				Collection<String> foreignKeys = new ArrayList<String>();
				while(foreignColumnsIterate.hasNext()) {
					foreignKeys.add(foreignColumnsIterate.next().getColumnName());
				}
				
				Iterator<String> foreignKeysIterate = foreignKeys.iterator();
				
				foreignKeyQuery.append("FOREIGN KEY(");
				
				int index = 0;
				while(foreignKeysIterate.hasNext()) {
					if(index == 0) {
						foreignKeyQuery.append(foreignKeysIterate.next());
					} else {
						foreignKeyQuery.append(", " + foreignKeysIterate.next());
					}
					
					index++;
				}

				foreignKeyQuery.append(") REFERENCES " + parentTable + "(");
				foreignKeysIterate = foreignKeys.iterator();
				
				index = 0;
				while(foreignKeysIterate.hasNext()) {
					if(index == 0) {
						foreignKeyQuery.append(foreignKeysIterate.next());
					} else {
						foreignKeyQuery.append(", " + foreignKeysIterate.next());
					}
					
					index++;
				}
				
				foreignKeyQuery.append(")");
				
				String onDeleteAction = relationship.getOnDelete();
				String onUpdateAction = relationship.getOnUpdate();
				
				if(onDeleteAction != null && onDeleteAction.length() > 0) {
					if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_CASCADE)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_CASCADE);	
					} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_RESTRICT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_RESTRICT);
					} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_NO_ACTION)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_NO_ACTION);
					} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_NULL)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_SET_NULL);
					} else if(onDeleteAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_DEFAULT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_SET_DEFAULT);
					}
				}
				
				if(onUpdateAction != null && onUpdateAction.length() > 0) {
					if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_CASCADE)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_CASCADE);	
					} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_RESTRICT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_RESTRICT);
					} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_NO_ACTION)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_NO_ACTION);
					} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_NULL)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_SET_NULL);
					} else if(onUpdateAction.equalsIgnoreCase(DATABASE_MAPPING_DESCRIPTOR_RELATIONSHIPS_SET_DEFAULT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_SET_DEFAULT);
					}
				}
				
				if(foreignKeyQuery.length() > 0) {
					if(foreignKeysQuery.length() <= 0) {
						foreignKeysQuery.append(" " + foreignKeyQuery);
					} else {
						foreignKeysQuery.append(", " + foreignKeyQuery);
					}
				}
			}
		
		return foreignKeysQuery.toString();
	}
	
	private Collection<Column> getForeignKeys(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		Iterator<Relationship> oneToManyRealtionships = databaseMappingDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = databaseMappingDescriptor.getManyToManyRelationships();
		
		Collection<Column> foreignColumns = new ArrayList<Column>();
		
		Iterator<Column> columns = databaseMappingDescriptor.getColumns();
		while(columns.hasNext()) {
			Column column = columns.next();
			if(column.isPrimaryKey()) {
				foreignColumns.add(column);
			}
		}
		
		while(oneToManyRealtionships.hasNext()) {
			
			Relationship relationship = oneToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Column> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Column> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignColumns.add(referedForeignKeysIterate.next());
			}
		}

		while(manyToManyRealtionships.hasNext()) {
			
			Relationship relationship = manyToManyRealtionships.next();
			DatabaseMappingDescriptor referedDatabaseMappingDescriptor = relationship.getReferedDatabaseMappingDescriptor();
			
			Collection<Column> referedForeignKeys = getForeignKeys(referedDatabaseMappingDescriptor);
			Iterator<Column> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignColumns.add(referedForeignKeysIterate.next());
			}
		}

		return foreignColumns;
	}

	
}
