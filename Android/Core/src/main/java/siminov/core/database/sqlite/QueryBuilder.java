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

package siminov.core.database.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import siminov.core.Constants;
import siminov.core.database.design.IQueryBuilder;
import siminov.core.exception.DatabaseException;
import siminov.core.exception.DeploymentException;
import siminov.core.log.Log;
import siminov.core.model.EntityDescriptor;
import siminov.core.model.EntityDescriptor.Attribute;
import siminov.core.model.EntityDescriptor.Relationship;
import siminov.core.resource.ResourceManager;
import android.text.TextUtils;


/**
 * Provides the IQueryBuilder implementation for SQLite
 */
public class QueryBuilder implements Constants, IQueryBuilder {

	
	public String formTableInfoQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_TABLE_INFO_QUERY_TABLE_NAME_PARAMETER);
		return "pragma table_info(" + tableName + ")";
	}


	
	public String formFetchDatabaseVersionQuery(final Map<String, Object> parameters) {
		return "PRAGMA user_version;";
	}
	
	
	public String formUpdateDatabaseVersionQuery(final Map<String, Object> parameters) {
		
		final Double databaseVersion = (Double) parameters.get(IQueryBuilder.FORM_UPDATE_DATABASE_VERSION_QUERY_DATABASE_VERSION_PARAMETER);
		return "PRAGMA user_version=" + databaseVersion;
	}
	

	public String formAlterAddColumnQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_TABLE_NAME_PARAMETER);
		final String columnName = (String) parameters.get(IQueryBuilder.FORM_ALTER_ADD_COLUMN_QUERY_COLUMN_NAME_PARAMETER);
		
		return "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT";
	}
	
	
	
	public String formTableNames(final Map<String, Object> parameters) {
		return "SELECT * FROM sqlite_master WHERE type='table'";
	}

	
	
	@SuppressWarnings("unchecked")
	public String formCreateTableQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_TABLE_NAME_PARAMETER);
		final Iterator<String> columnNames = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_NAMES_PARAMETER);
		final Iterator<String> columnTypes = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_COLUMN_TYPES_PARAMETER);
		final Iterator<String> defaultValues = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_DEFAULT_VALUES_PARAMETER);
		final Iterator<String> checks = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_CHECKS_PARAMETER);
		final Iterator<String> primaryKeys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_PRIMARY_KEYS_PARAMETER);
		final Iterator<Boolean> isNotNull = (Iterator<Boolean>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_NOT_NULLS_PARAMETER);
		final Iterator<String> uniqueColumns = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_UNIQUE_COLUMNS_PARAMETER);
		final String foreignKeys = (String) parameters.get(IQueryBuilder.FORM_CREATE_TABLE_QUERY_FOREIGN_KEYS_PARAMETER);

		
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
	

	@SuppressWarnings("unchecked")
	public String formCreateIndexQuery(final Map<String, Object> parameters) {
		
		final String indexName = (String) parameters.get(IQueryBuilder.FORM_CREATE_INDEX_QUERY_INDEX_NAME_PARAMETER);
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_CREATE_INDEX_QUERY_TABLE_NAME_PARAMETER);
		final Iterator<String> columnNames = (Iterator<String>) parameters.get(IQueryBuilder.FORM_CREATE_INDEX_QUERY_COLUMN_NAMES_PARAMETER);
		final boolean isUnique = (Boolean) parameters.get(IQueryBuilder.FORM_CREATE_INDEX_QUERY_IS_UNIQUE_PARAMETER);
		
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
	
	public String formDropTableQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_DROP_TABLE_QUERY_TABLE_NAME_PARAMETER);
		
		
		StringBuilder query = new StringBuilder();
			query.append("DROP TABLE IF EXISTS " + tableName);
		
		return query.toString();
	}

	public String formDropIndexQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_DROP_INDEX_QUERY_TABLE_NAME_PARAMETER);
		final String indexName = (String) parameters.get(IQueryBuilder.FORM_DROP_INDEX_QUERY_INDEX_NAME_PARAMETER);
		
		
		StringBuilder query = new StringBuilder();
			query.append("DROP INDEX IF EXISTS " + indexName + " ON " + tableName);
			
		return query.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String formSelectQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_TABLE_NAME_PARAMETER);
		final boolean distinct = (Boolean) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_DISTINCT_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> columnNames = (Iterator<String>) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_COLUMN_NAMES_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_HAVING_PARAMETER);
		final Iterator<String> orderBy = (Iterator<String>) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_ORDER_BYS_PARAMETER);
		final String whichOrderBy = (String) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_WHICH_ORDER_BY_PARAMETER);
		final String limit = (String) parameters.get(IQueryBuilder.FORM_SELECT_QUERY_LIMIT_PARAMETER);
		
		
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

        	int columnNamesCount = 0;
        	if(columnsNames != null) {
                if (columnsNames.hasNext()) {
                	++columnNamesCount;
                    appendColumns(query, columnsNames);
                }
            }
        	
        	if(columnNamesCount <= 0) {
            	query.append("* ");
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

	@SuppressWarnings("unchecked")
	public String formSaveBindQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_SAVE_BIND_QUERY_TABLE_NAME_PARAMETER);
		final Iterator<String> columnNames = (Iterator<String>) parameters.get(IQueryBuilder.FORM_SAVE_BIND_QUERY_COLUMN_NAMES_PARAMETER);
		
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

	
	@SuppressWarnings("unchecked")
	public String formUpdateBindQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_UPDATE_BIND_QUERY_TABLE_NAME_PARAMETER);
		final Iterator<String> columnNames = (Iterator<String>) parameters.get(IQueryBuilder.FORM_UPDATE_BIND_QUERY_COLUMN_NAMES_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_UPDATE_BIND_QUERY_WHERE_CLAUSE_PARAMETER);
		
		
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

	
	public String formDeleteQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_DELETE_QUERY_TABLE_NAME_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_DELETE_QUERY_WHERE_CLAUSE_PARAMETER);
		
		
		StringBuilder query = new StringBuilder();
			query.append("DELETE FROM " + tableName);
			
			if(whereClause != null && whereClause.length() > 0) {
				query.append(" WHERE " + whereClause);
			}
			
		return query.toString();
	}

	
	@SuppressWarnings("unchecked")
	public String formCountQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_COLUMN_PARAMETER);
		final boolean distinct = (Boolean) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_DISTINCT_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_COUNT_QUERY_HAVING_PARAMETER);
		
		
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

	
	@SuppressWarnings("unchecked")
	public String formAvgQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_AVG_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_AVG_QUERY_COLUMN_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_AVG_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_AVG_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_AVG_QUERY_HAVING_PARAMETER);
		
		
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
	
	@SuppressWarnings("unchecked")
	public String formSumQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_SUM_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_SUM_QUERY_COLUMN_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_SUM_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_SUM_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_SUM_QUERY_HAVING_PARAMETER);

		
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
	

	@SuppressWarnings("unchecked")
	public String formTotalQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_TOTAL_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_TOTAL_QUERY_COLUMN_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_TOTAL_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_TOTAL_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_TOTAL_QUERY_HAVING_PARAMETER);
		
		
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

	@SuppressWarnings("unchecked")
	public String formMaxQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_MAX_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_MAX_QUERY_COLUMN_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_MAX_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_MAX_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_MAX_QUERY_HAVING_PARAMETER);

		
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

	
	@SuppressWarnings("unchecked")
	public String formMinQuery(final Map<String, Object> parameters) {
		
		final String tableName = (String) parameters.get(IQueryBuilder.FORM_MIN_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_MIN_QUERY_COLUMN_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_MIN_QUERY_WHERE_CLAUSE_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_MIN_QUERY_GROUP_BYS_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_MIN_QUERY_HAVING_PARAMETER);
		
		
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

	
	@SuppressWarnings("unchecked")
	public String formGroupConcatQuery(final Map<String, Object> parameters) {

		final String tableName = (String) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_TABLE_NAME_PARAMETER);
		final String column = (String) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_COLUMN_PARAMETER);
		final String delimiter = (String) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_DELIMITER_PARAMETER);
		final String whereClause = (String) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_WHERE_CLAUSE_PARAMETER);
		final String having = (String) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_HAVING_PARAMETER);
		final Iterator<String> groupBys = (Iterator<String>) parameters.get(IQueryBuilder.FORM_GROUP_CONCAT_QUERY_GROUP_BYS_PARAMETER);
		
		
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



	public String formForeignKeyQuery(final Map<String, Object> parameters) {
		
		final EntityDescriptor child = (EntityDescriptor) parameters.get(IQueryBuilder.FORM_FOREIGN_KEYS_DATABASE_DESCRIPTOR_PARAMETER);
		
		Iterator<Relationship> oneToManyRelationships = child.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRelationships = child.getManyToManyRelationships();
		
		Collection<Relationship> relationships = new ArrayList<Relationship>();
		
		while(oneToManyRelationships.hasNext()) {
			Relationship relationship = oneToManyRelationships.next();
			
			relationships.add(relationship);
		}
		
		while(manyToManyRelationships.hasNext()) {
			Relationship relationship = manyToManyRelationships.next();
			
			relationships.add(relationship);
		}
		
		StringBuilder foreignKeysQuery = new StringBuilder();
		Iterator<Relationship> relationshipsIterator = relationships.iterator();
			while(relationshipsIterator.hasNext()) {
				
				StringBuilder foreignKeyQuery = new StringBuilder(); 
				Relationship relationship = relationshipsIterator.next();
				
				EntityDescriptor referedEntityDescriptor = relationship.getReferedEntityDescriptor();
				if(referedEntityDescriptor == null) {
					referedEntityDescriptor = ResourceManager.getInstance().requiredEntityDescriptorBasedOnClassName(relationship.getReferTo());
					relationship.setReferedEntityDescriptor(referedEntityDescriptor);
					
					relationship.setReferedEntityDescriptor(referedEntityDescriptor);
				}

				
				String parentTable = referedEntityDescriptor.getTableName();
				Collection<Attribute> foreignAttributes = null;
				try {
					foreignAttributes = getForeignKeys(referedEntityDescriptor);
				} catch(DatabaseException databaseException) {
					Log.error(QueryBuilder.class.getName(), "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.getMessage());
					throw new DeploymentException(QueryBuilder.class.getName(), "formForeignKeys", "Database Exception caught while getting foreign columns, " + databaseException.getMessage());
				}
				
				Iterator<Attribute> foreignAttributesIterate = foreignAttributes.iterator();
				
				Collection<String> foreignKeys = new ArrayList<String>();
				while(foreignAttributesIterate.hasNext()) {
					foreignKeys.add(foreignAttributesIterate.next().getColumnName());
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
					if(onDeleteAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_CASCADE);	
					} else if(onDeleteAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_RESTRICT);
					} else if(onDeleteAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_NO_ACTION);
					} else if(onDeleteAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_SET_NULL);
					} else if(onDeleteAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_DELETE + " " + QUERY_BUILDER_SET_DEFAULT);
					}
				}
				
				if(onUpdateAction != null && onUpdateAction.length() > 0) {
					if(onUpdateAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_CASCADE)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_CASCADE);	
					} else if(onUpdateAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_RESTRICT)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_RESTRICT);
					} else if(onUpdateAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_NO_ACTION)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_NO_ACTION);
					} else if(onUpdateAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_SET_NULL)) {
						foreignKeyQuery.append(" " + QUERY_BUILDER_ON_UPDATE + " " + QUERY_BUILDER_SET_NULL);
					} else if(onUpdateAction.equalsIgnoreCase(ENTITY_DESCRIPTOR_RELATIONSHIP_SET_DEFAULT)) {
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
	
	private Collection<Attribute> getForeignKeys(EntityDescriptor entityDescriptor) throws DatabaseException {
		Iterator<Relationship> oneToManyRealtionships = entityDescriptor.getManyToOneRelationships();
		Iterator<Relationship> manyToManyRealtionships = entityDescriptor.getManyToManyRelationships();
		
		Collection<Attribute> foreignAttributes = new ArrayList<Attribute>();
		
		Iterator<Attribute> attributes = entityDescriptor.getAttributes();
		while(attributes.hasNext()) {
			Attribute attribute = attributes.next();
			if(attribute.isPrimaryKey()) {
				foreignAttributes.add(attribute);
			}
		}
		
		while(oneToManyRealtionships.hasNext()) {
			
			Relationship relationship = oneToManyRealtionships.next();
			EntityDescriptor referedEntityDescriptor = relationship.getReferedEntityDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedEntityDescriptor);
			Iterator<Attribute> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignAttributes.add(referedForeignKeysIterate.next());
			}
		}

		while(manyToManyRealtionships.hasNext()) {
			
			Relationship relationship = manyToManyRealtionships.next();
			EntityDescriptor referedEntityDescriptor = relationship.getReferedEntityDescriptor();
			
			Collection<Attribute> referedForeignKeys = getForeignKeys(referedEntityDescriptor);
			Iterator<Attribute> referedForeignKeysIterate = referedForeignKeys.iterator();
			
			while(referedForeignKeysIterate.hasNext()) {
				foreignAttributes.add(referedForeignKeysIterate.next());
			}
		}

		return foreignAttributes;
	}
}
