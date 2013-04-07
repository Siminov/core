package siminov.orm.database.design;

public interface ISyntaxProvider {

	public String equalTo();
	
	public String notEqualTo();
	
	public String greaterThan();
	
	public String greaterThanEqual();
	
	public String lessThan();
	
	public String lessThanEqual();
	
	public String between();
	
	public String like();
	
	public String in();
	
	public String and();
	
	public String or();

	public String ascendingOrderBy();
	
	public String descendingOrderBy();
	
}
