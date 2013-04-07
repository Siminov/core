package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface IAverage {

	public IAverageClause where(String column);
	
	public IAverage whereClause(String whereClause);
	
	public IAverageClause and(String column);
	
	public IAverageClause or(String column);

	public IAverage groupBy(String...columns);
	
	public IAverageClause having(String column);

	public IAverage havingClause(String havingClause);
	
	public IAverage column(String column);
	
	public Object execute() throws DatabaseException;

}
