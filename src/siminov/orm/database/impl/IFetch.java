package siminov.orm.database.impl;

import siminov.orm.exception.DatabaseException;

public interface IFetch {

	public IFetch distinct();
	
	public IFetchClause where(String column);
	
	public IFetch whereClause(String whereClause);
	
	public IFetchClause and(String column);
	
	public IFetchClause or(String column);
	
	public IFetch orderBy(String...columns);
	
	public IFetch ascendingOrderBy(String...columns);
	
	public IFetch descendingOrderBy(String...columns);
	
	public IFetch limit(int limit);
	
	public IFetch groupBy(String...columns);
	
	public IFetchClause having(String column);
	
	public IFetch havingClause(String havingClause);
	
	public IFetch columns(String...columns);
	
	public Object[] fetch() throws DatabaseException;
	
}
