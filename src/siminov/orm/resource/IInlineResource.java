package siminov.orm.resource;

import java.util.Iterator;

public interface IInlineResource {

	public Iterator<String> getInlineResources();
	
	public String getInlineResource(final String name);

	public void addInlineResource(final String name, final String value);
	
	public boolean containInlineResource(final String name);
}
