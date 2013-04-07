package siminov.orm.database;

import siminov.orm.database.impl.IAverageClause;
import siminov.orm.database.impl.ICountClause;
import siminov.orm.database.impl.IFetchClause;
import siminov.orm.database.impl.IGroupConcatClause;
import siminov.orm.database.impl.IMaxClause;
import siminov.orm.database.impl.IMinClause;
import siminov.orm.database.impl.ISumClause;
import siminov.orm.database.impl.ITotalClause;

public class Clause implements IFetchClause, ICountClause, ISumClause, ITotalClause, IAverageClause, IMaxClause, IMinClause, IGroupConcatClause {

	String EQUAL_TO = "=";
	String NOT_EQUAL_TO = "!=";
	String GREATER_THAN = ">";
	String GREATER_THAN_EQUAL = ">=";
	String LESS_THAN = "<";
	String LESS_THAN_EQUAL = "<=";
	String BETWEEN = "BETWEEN";
	String LIKE = "LIKE";
	String IN = "IN";
	String AND = "AND";
	String OR = "OR";

	private StringBuilder where = new StringBuilder();
	
	private Select select = null;
	
	public Clause(Select select) {
		this.select = select;
	}
	
	void addCol(String column) {
		where.append(column);
	}
	
	public Select equalTo(String value) {
		where.append(EQUAL_TO + " '" + value + "' ");
		return this.select;
	}
	
	public Select notEqualTo(String value) {
		where.append(NOT_EQUAL_TO + " '" + value + "' ");
		return this.select;
	}
	
	public Select greaterThan(String value) {
		where.append(GREATER_THAN + " '" + value + "' ");
		return this.select;
	}
	
	public Select greaterThanEqual(String value) {
		where.append(GREATER_THAN_EQUAL + " '" + value + "' ");
		return this.select;
	}
	
	public Select lessThan(String value) {
		where.append(LESS_THAN + " '" + value + "' ");
		return this.select;
	}
	
	public Select lessThanEqual(String value) {
		where.append(LESS_THAN_EQUAL + " '" + value + "' ");
		return this.select;
	}

	public Select between(String start, String end) {
		where.append(BETWEEN + " '" + start + "' " + AND + " '" + end + "' ");
		return this.select;
	}
	
	public Select like(String like) {
		where.append(LIKE + " '" + like + "' ");
		return this.select;
	}
	
	public Select in(String...values) {
		where.append(IN + "(");
		
		if(values != null && values.length > 0) {
			for(int i = 0;i < values.length;i++) {
				if(i == 0) {
					where.append("'" + values[i] + "'");
					continue;
				}
				
				where.append(" ,'" + values[i] + "'");
			}
		} 
		
		where.append(")");
		
		return this.select;
	}
	
	void and(String column) {
		where.append(" " + AND + " " + column);
	}
	
	void or(String column) {
		where.append(" " + OR + " " + column);
	}
	
	public String toString() {
		return where.toString();
	}
	
}
