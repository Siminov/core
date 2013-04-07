package siminov.orm.database.sqlite;

import siminov.orm.database.design.ISyntaxProvider;

public class SyntaxProvider implements ISyntaxProvider {

	private static final String EQUAL_TO = "=";
	private static final String NOT_EQUAL_TO = "!=";
	private static final String GREATER_THAN = ">";
	private static final String GREATER_THAN_EQUAL = ">=";
	private static final String LESS_THAN = "<";
	private static final String LESS_THAN_EQUAL = "<=";
	private static final String BETWEEN = "BETWEEN";
	private static final String LIKE = "LIKE";
	private static final String IN = "IN";
	private static final String AND = "AND";
	private static final String OR = "OR";
	
	private static final String ASCENDING_ORDER_BY = "";
	private static final String DESCENDING_ORDER_BY = ""; 
	
	public String equalTo() {
		return EQUAL_TO;
	}
	
	public String notEqualTo() {
		return NOT_EQUAL_TO;
	}
	
	public String greaterThan() {
		return GREATER_THAN;
	}
	
	public String greaterThanEqual() {
		return GREATER_THAN_EQUAL;
	}
	
	public String lessThan() {
		return LESS_THAN;
	}
	
	public String lessThanEqual() {
		return LESS_THAN_EQUAL;
	}
	
	public String between() {
		return BETWEEN;
	}
	
	public String in() {
		return IN;
	}
	
	public String and() {
		return AND;
	}
	
	public String or() {
		return OR;
	}

	public String like() {
		return LIKE;
	}

	public String ascendingOrderBy() {
		return ASCENDING_ORDER_BY;
	}
	
	public String descendingOrderBy() {
		return DESCENDING_ORDER_BY;
	}

}
