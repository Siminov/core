package siminov.orm.database.impl;

public interface ISumClause {

	public ISum equalTo(String value);

	public ISum notEqualTo(String value);
	
	public ISum greaterThan(String value);
	
	public ISum greaterThanEqual(String value);
	
	public ISum lessThan(String value);
	
	public ISum lessThanEqual(String value);
	
	public ISum between(String start, String end);
	
	public ISum like(String like);
	
	public ISum in(String...values);

}
