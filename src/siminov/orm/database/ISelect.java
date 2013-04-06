package siminov.orm.database;

import siminov.orm.database.Clause;
import siminov.orm.database.Select;
import siminov.orm.exception.DatabaseException;

public interface ISelect {

	public Clause where(String column);
	
	public Select whereClause(String whereClause);
	
	public Clause and(String column);
	
	public Clause or(String column);
	
	public Select orderBy(String...columns);
	
	public Select ascendingOrderBy(String...columns);
	
	public Select descendingOrderBy(String...columns);
	
	public Select limit(int limit);
	
	public Select groupBy(String...columns);
	
	public Clause having(String column);
	
	public Select havingClause(String havingClause);
	
	public Select columns(String...columns);
	
	public Object[] fetch() throws DatabaseException;
	
}
