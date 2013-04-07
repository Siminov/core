package siminov.orm.database.impl;

public interface IAverageClause {

	public IAverage equalTo(String value);

	public IAverage notEqualTo(String value);
	
	public IAverage greaterThan(String value);
	
	public IAverage greaterThanEqual(String value);
	
	public IAverage lessThan(String value);
	
	public IAverage lessThanEqual(String value);
	
	public IAverage between(String start, String end);
	
	public IAverage like(String like);
	
	public IAverage in(String...values);

}
