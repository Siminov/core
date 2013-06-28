package siminov.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import siminov.orm.resource.Resources;

import android.content.Context;

public class Initializer implements IInitializer {

	private Resources resources = Resources.getInstance();
	
	private List<Object> parameters = new ArrayList<Object> ();
	
	public void addParameter(Object object) {
		parameters.add(object);
	}
	
	public void start() {
		
		Context context = null;
		
		Iterator<Object> iterator = parameters.iterator();
		while(iterator.hasNext()) {
			
			Object object = iterator.next();
			if(object instanceof Context) {
				context = (Context) object;
			}
			
		}
		
		resources.setApplicationContext(context);
		
		Siminov.start();
		
	}
	
}
