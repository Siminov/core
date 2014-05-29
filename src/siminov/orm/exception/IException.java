package siminov.orm.exception;

public interface IException {

	public String getClassName();

		
	public void setClassName(final String className);
	
	
	public String getMethodName();
	
	
	public void setMethodName(final String methodName);

	
	public String getMessage();
	
	
	public void setMessage(final String message);
}
