package siminov.orm.database.impl;

public interface ITotalClause {

	public ITotal equalTo(String value);

	public ITotal notEqualTo(String value);
	
	public ITotal greaterThan(String value);
	
	public ITotal greaterThanEqual(String value);
	
	public ITotal lessThan(String value);
	
	public ITotal lessThanEqual(String value);
	
	public ITotal between(String start, String end);
	
	public ITotal like(String like);
	
	public ITotal in(String...values);
	
}
