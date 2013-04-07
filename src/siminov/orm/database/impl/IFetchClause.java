package siminov.orm.database.impl;



public interface IFetchClause {

	public IFetch equalTo(String value);

	public IFetch notEqualTo(String value);
	
	public IFetch greaterThan(String value);
	
	public IFetch greaterThanEqual(String value);
	
	public IFetch lessThan(String value);
	
	public IFetch lessThanEqual(String value);
	
	public IFetch between(String start, String end);
	
	public IFetch like(String like);
	
	public IFetch in(String...values);
	
}
