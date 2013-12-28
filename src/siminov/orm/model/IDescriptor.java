package siminov.orm.model;

import java.util.Iterator;

public interface IDescriptor {

	public Iterator<String> getProperties();
	
	public String getProperty(String name);

	public boolean containProperty(String name);
	
	public void addProperty(String name, String value);	
	
	public void removeProperty(String name);
	
}
