package siminov.orm.database;

import java.util.Arrays;

import siminov.orm.database.design.ISyntaxProvider;
import siminov.orm.database.impl.IAverage;
import siminov.orm.database.impl.ICount;
import siminov.orm.database.impl.IFetch;
import siminov.orm.database.impl.IGroupConcat;
import siminov.orm.database.impl.IMax;
import siminov.orm.database.impl.IMin;
import siminov.orm.database.impl.ISum;
import siminov.orm.database.impl.ITotal;
import siminov.orm.exception.DatabaseException;
import siminov.orm.model.DatabaseMappingDescriptor;
import siminov.orm.resource.Resources;

public class Select implements IFetch, ICount, ISum, ITotal, IAverage, IMax, IMin, IGroupConcat {

	private DatabaseMappingDescriptor databaseMappingDescriptor = null;
	private DatabaseBundle databaseBundle = null;
	
	private String column = null;
	private String[] columns = new String[] {};
	
	private Clause where = null;
	private String whereClause = null;
	
	private String[] orderBy = null;
	private String whichOrderBy = null;

	private String[] groupBy;
	
	private Clause having = null;
	private String havingClause = null;
	
	private int limit;

	private boolean distinct = false;

	private String delimiter = null;
	
	public Select() {
		
	}
	
	public Select(DatabaseMappingDescriptor databaseMappingDescriptor) throws DatabaseException {
		this.databaseMappingDescriptor = databaseMappingDescriptor;
		this.databaseBundle = Resources.getInstance().getDatabaseBundleBasedOnDatabaseMappingDescriptorClassName(databaseMappingDescriptor.getClassName());
	}

	public Select distinct() {
		this.distinct = true;
		return this;
	}
	
	public Clause where(String column) {
		
		ISyntaxProvider syntaxProvider = this.databaseBundle.getSyntaxProvider();
		
		
		where = new Clause(this);
		where.EQUAL_TO = syntaxProvider.equalTo();
		where.NOT_EQUAL_TO = syntaxProvider.notEqualTo();
		where.GREATER_THAN = syntaxProvider.greaterThan();
		where.GREATER_THAN_EQUAL = syntaxProvider.greaterThanEqual();
		where.LESS_THAN = syntaxProvider.lessThan();
		where.LESS_THAN_EQUAL = syntaxProvider.lessThanEqual();
		where.BETWEEN = syntaxProvider.between();
		where.LIKE = syntaxProvider.like();
		where.IN = syntaxProvider.in();
		where.AND = syntaxProvider.and();
		where.OR = syntaxProvider.or();
		
		where.addCol(column);
		
		return where;
	}
	
	public Select whereClause(String whereClause) {
		this.whereClause = whereClause;
		return this;
	}

	public Clause and(String column) {
		this.where.and(column);
		return this.where;
	}
	
	public Clause or(String column) {
		this.where.or(column);
		return this.where;
	}
	
	
	public Select orderBy(String...columns) {
		this.orderBy = columns;
		return this;
	}
	
	public Select ascendingOrderBy(String...columns) {
		this.orderBy = columns;
		
		ISyntaxProvider syntaxProvider = this.databaseBundle.getSyntaxProvider();
		this.whichOrderBy = syntaxProvider.ascendingOrderBy();

		return this;
	}
	
	public Select descendingOrderBy(String...columns) {
		this.orderBy = columns;

		ISyntaxProvider syntaxProvider = this.databaseBundle.getSyntaxProvider();
		this.whichOrderBy = syntaxProvider.descendingOrderBy();

		return this;
	}
	
	public Select limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	public Select groupBy(String...columns) {
		this.groupBy = columns;
		return this;
	}

	public Clause having(String column) {
		ISyntaxProvider syntaxProvider = this.databaseBundle.getSyntaxProvider();
		
		
		having = new Clause(this);
		having.EQUAL_TO = syntaxProvider.equalTo();
		having.NOT_EQUAL_TO = syntaxProvider.notEqualTo();
		having.GREATER_THAN = syntaxProvider.greaterThan();
		having.GREATER_THAN_EQUAL = syntaxProvider.greaterThanEqual();
		having.LESS_THAN = syntaxProvider.lessThan();
		having.LESS_THAN_EQUAL = syntaxProvider.lessThanEqual();
		having.BETWEEN = syntaxProvider.between();
		having.LIKE = syntaxProvider.like();
		having.IN = syntaxProvider.in();
		having.AND = syntaxProvider.and();
		having.OR = syntaxProvider.or();
		
		having.addCol(column);
		
		return having;
	}
	
	public Select havingClause(String havingClause) {
		this.havingClause = havingClause;
		return this;
	}
	
	public Select column(String column) {
		this.column = column;
		return this;
	}
	
	public Select columns(String...columns) {
		this.columns = columns;
		return this;
	}

	public Select delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	public Object execute() throws DatabaseException {

		String where = "";
		if(this.whereClause != null && this.whereClause.length() > 0) {
			where = this.whereClause;
		} else {
			if(this.where != null) {
				where = this.where.toString();
			}
		}
		
		String having = "";
		if(this.havingClause != null && this.havingClause.length() > 0) {
			having = this.havingClause;
		} else {
			if(this.having != null) {
				having = this.having.toString();
			}
		}

		if(this.columns == null) {
			this.columns = new String[] {};			
		}
		
		if(this.orderBy == null) {
			this.orderBy = new String[] {};
		}
		
		if(this.groupBy == null) {
			this.groupBy = new String[] {};
		}

		
		
		if(this instanceof ICount) {
			return Database.count(databaseMappingDescriptor, column, distinct, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof IAverage) {
			return Database.avg(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof ISum) {
			return Database.sum(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof ITotal) {
			return Database.total(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof IMax) {
			return Database.max(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof IMin) {
			return Database.min(databaseMappingDescriptor, column, where, Arrays.asList(groupBy).iterator(), having);
		} else if(this instanceof IGroupConcat) {
			return Database.groupConcat(databaseMappingDescriptor, column, delimiter, where, Arrays.asList(groupBy).iterator(), having);
		}

		return null;
	}

	public Object[] fetch() throws DatabaseException {

		String where = "";
		if(this.whereClause != null && this.whereClause.length() > 0) {
			where = this.whereClause;
		} else {
			if(this.where != null) {
				where = this.where.toString();
			}
		}
		
		String having = "";
		if(this.havingClause != null && this.havingClause.length() > 0) {
			having = this.havingClause;
		} else {
			if(this.having != null) {
				having = this.having.toString();
			}
		}

		if(this.columns == null) {
			this.columns = new String[] {};			
		}
		
		if(this.orderBy == null) {
			this.orderBy = new String[] {};
		}
		
		if(this.groupBy == null) {
			this.groupBy = new String[] {};
		}
		
		String limit = null;
		if(this.limit != 0) {
			limit = String.valueOf(this.limit);
		}
		
		return Database.select(databaseMappingDescriptor, distinct, where, Arrays.asList(columns).iterator(), Arrays.asList(groupBy).iterator(), having, Arrays.asList(orderBy).iterator(), whichOrderBy, limit);
		
	}
	
}
