package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

/**
 * Exposes API's to delete tuples from table.
 */
public interface IDelete {

	public String INTERFACE_NAME = IDelete.class.getName();

	
	/**
	 * Column name of which condition will be specified.
	 * @param column Name of column.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause where(String column);
	
	/**
	 * Used to provide manually created Where clause, instead of using API's.
	 * @param whereClause Manually created where clause.
	 * @return IDelete Interface.
	 */
	public IDelete whereClause(String whereClause); 

	/**
	 * Used to specify AND condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause and(String column);
	
	/**
	 * Used to specify OR condition between where clause.
	 * @param column Name of column on which condition need to be specified.
	 * @return IDeleteClause Interface.
	 */
	public IDeleteClause or(String column);

	
	/**
	 * Used to delete, this method should be called in last to delete tuples from table.
	 * @throws DatabaseException Throws exception if any error occur while deleting tuples from table. 
	 */
	public Object execute() throws DatabaseException;

}
