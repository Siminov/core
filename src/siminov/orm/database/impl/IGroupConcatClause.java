package siminov.orm.database.impl;

/**
 * Exposes API's to provide condition on where clause to calculate group concat.
 */
public interface IGroupConcatClause {

	/**
	 * Used to specify EQUAL TO (=) condition.
	 * @param value Value for which EQUAL TO (=) condition will be applied.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat equalTo(String value);

	/**
	 * Used to specify NOT EQUAL TO (!=) condition.
	 * @param value Value for which NOT EQUAL TO (=) condition will be applied.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat notEqualTo(String value);
	
	/**
	 * Used to specify GREATER THAN (>) condition.
	 * @param value Value for while GREATER THAN (>) condition will be specified.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat greaterThan(String value);
	
	/**
	 * Used to specify GREATER THAN EQUAL (>=) condition.
	 * @param value Value for which GREATER THAN EQUAL (>=) condition will be specified.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat greaterThanEqual(String value);
	
	/**
	 * Used to specify LESS THAN (<) condition.
	 * @param value Value for which LESS THAN (<) condition will be specified.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat lessThan(String value);
	
	/**
	 * Used to specify LESS THAN EQUAL (<=) condition.
	 * @param value Value for which LESS THAN EQUAL (<=) condition will be specified.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat lessThanEqual(String value);
	
	/**
	 * Used to specify BETWEEN condition.
	 * @param start Start Range.
	 * @param end End Range.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat between(String start, String end);
	
	/**
	 * Used to specify LIKE condition.
	 * @param like LIKE condition.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat like(String like);
	
	/**
	 * Used to specify IN condition.
	 * @param values Values for IN condition.
	 * @return IGroupConcat Interface.
	 */
	public IGroupConcat in(String...values);

}
