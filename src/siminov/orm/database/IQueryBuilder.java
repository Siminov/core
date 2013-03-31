package siminov.orm.database;

import java.util.Iterator;

import siminov.orm.model.DatabaseMappingDescriptor;

public interface IQueryBuilder {

	public String formCreateTableQuery(final String tableName, final Iterator<String> columnNames, final Iterator<String> columnTypes, final Iterator<String> defaultValues, final Iterator<String> checks, final Iterator<String> primaryKeys, final Iterator<Boolean> isNotNull, final Iterator<String> uniqueColumns, final String foreignKeys);

	public String formCreateIndexQuery(final String indexName, final String tableName, final Iterator<String> columnNames, final boolean isUnique);
	
	public String formDropTableQuery(final String tableName);
	
	public String formDropIndexQuery(String tableName, String indexName);
	
	public String formFetchQuery(final String tableName, final String whereClause, final Iterator<String> columnNames, final Iterator<String> groupBys, final String having, final Iterator<String> orderBy, final String limit);
	
	public String formSaveBindQuery(final String tableName, final Iterator<String> columnNames);
	
	public String formUpdateBindQuery(final String tableName, final Iterator<String> columnNames, final String whereClause);
	
	public String formDeleteQuery(final String tableName, final String whereClause);
	
	public String formCountQuery(final String tableName, final String whereClause);
	
	public String formAvgQuery(final String tableName, final String columnName);
	
	public String formMaxQuery(final String tableName, final String columnName, final String groupBy);
	
	public String formMinQuery(final String tableName, final String columnName, final String groupBy);
	
	public String formGroupConcatQuery(final String tableName, final String columnName, final String delimiter, final String whereClause);
	
	public String formSumQuery(final String tableName, final String columnName);
	
	public String formTotalQuery(final String tableName, final String columnName);
	
	public Iterator<String> formTriggers(final DatabaseMappingDescriptor databaseMappingDescriptor);
	
	public String formForeignKeys(final DatabaseMappingDescriptor databaseMappingDescriptor);
	
}
