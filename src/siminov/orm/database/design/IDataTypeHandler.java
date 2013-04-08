package siminov.orm.database.design;

/**
 * Exposes convert API which is responsible to provide column data type based on java variable data type.
 */
public interface IDataTypeHandler {

	/**
	 * Converts java variable data type to database column data type.
	 * @param dataType Java variable data type.
	 * @return column data type.
	 */
	public String convert(String dataType);
	
}
