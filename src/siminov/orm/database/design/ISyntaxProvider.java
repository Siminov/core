package siminov.orm.database.design;

/**
 * Exposes API's to get database related syntax.
 */
public interface ISyntaxProvider {

	/**
	 * Database Equal To Syntax.
	 * @return Equal To Syntax.
	 */
	public String equalTo();
	
	/**
	 * Database Not Equal To Syntax.
	 * @return Not Equal To Syntax.
	 */
	public String notEqualTo();
	
	/**
	 * Database Greater Than Syntax.
	 * @return Greater Than Syntax.
	 */
	public String greaterThan();
	
	/**
	 * Database Greater Than Equal Syntax.
	 * @return Greater Than Equal Syntax.
	 */
	public String greaterThanEqual();
	
	/**
	 * Database Less Than Syntax.
	 * @return Less Than Syntax.
	 */
	public String lessThan();
	
	/**
	 * Database Less Than Equal Syntax.
	 * @return Less Than Equal Syntax.
	 */
	public String lessThanEqual();
	
	/**
	 * Database Between Syntax.
	 * @return Between Syntax.
	 */
	public String between();
	
	/**
	 * Database Like Syntax.
	 * @return Like Syntax.
	 */
	public String like();
	
	/**
	 * Database In Syntax.
	 * @return In Syntax.
	 */
	public String in();
	
	/**
	 * Database And Syntax.
	 * @return And Syntax.
	 */
	public String and();
	
	/**
	 * Database Or Syntax.
	 * @return Or Syntax.
	 */
	public String or();

	/**
	 * Database Ascending Order By Syntax.
	 * @return Ascending Order By Syntax.
	 */
	public String ascendingOrderBy();
	
	/**
	 * Database Descending Order By Syntax.
	 * @return Descending Order By Syntax.
	 */
	public String descendingOrderBy();
	
}
