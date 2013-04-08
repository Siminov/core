package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

/**
 * Exposes API's to get tuples from table based on information provided.
 */
public interface IFetch {

	/**
	 * Used to specify DISTINCT condition.
	 * @return ICount Interface.
	 */
	public IFetch distinct();
	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return IFetchClause Interface.
	 */
	public IFetchClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return IFetch Interface.
	 */
	public IFetch whereClause(String whereClause);
	
	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IFetchClause Interface.
	 */
	public IFetchClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IFetchClause Interface.
	 */
	public IFetchClause or(String column);
	
	/**
	 * Used to specify ORDER BY keyword to sort the result-set.
	 * @param columns Name of columns which need to be sorted.
	 * @return IFetch Interface.
	 */
	public IFetch orderBy(String...columns);
	
	/**
	 * Used to specify ORDER BY ASC keyword to sort the result-set in ascending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return IFetch Interface.
	 */
	public IFetch ascendingOrderBy(String...columns);
	
	/**
	 * Used to specify ORDER BY DESC keyword to sort the result-set in descending order.
	 * @param columns Name of columns which need to be sorted.
	 * @return IFetch Interface.
	 */
	public IFetch descendingOrderBy(String...columns);

	/**
	 * Used to specify the range of data need to fetch from table.
	 * @param limit LIMIT of data.
	 * @return IFetch Interface.
	 */
	public IFetch limit(int limit);
	
	/**
	 * Used to specify GROUP BY statement in conjunction with the aggregate functions to group the result-set by one or more columns.
	 * @param columns Name of columns.
	 * @return IFetch Interface.
	 */
	public IFetch groupBy(String...columns);
	
	/**
	 * Used to specify HAVING clause to SQL because the WHERE keyword could not be used with aggregate functions.
	 * @param column Name of column on which condition need to be applied.
	 * @return IFetchClause Interface.
	 */
	public IFetchClause having(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param havingClause Where clause.
	 * @return IFetch Interface.
	 */
	public IFetch havingClause(String havingClause);
	
	/**
	 * Used to provide name of columns only for which data will be fetched.
	 * @param column Name of columns.
	 * @return IFetch Interface.
	 */
	public IFetch columns(String...columns);
	
	/**
	 * Used to get tuples, this method should be called in last to get tuples from table.
	 * @return Return array of model objects.
	 * @throws DatabaseException Throws exception if any error occur while getting tuples from table. 
	 */
	public Object[] fetch() throws DatabaseException;
	
}
