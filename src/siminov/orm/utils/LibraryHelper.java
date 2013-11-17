package siminov.orm.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import siminov.orm.Constants;
import siminov.orm.exception.SiminovCriticalException;
import siminov.orm.log.Log;
import dalvik.system.PathClassLoader;

public class LibraryHelper {

	public Iterator<String> getLibraries() {
			
		Collection<String> libraries = new ArrayList<String> ();
		
		Field dexField = null;
		try {
			dexField = PathClassLoader.class.getDeclaredField("mZips");
		} catch(Exception e) {
			Log.loge(LibraryHelper.class.getName(), "getLibraries", "Exception caught while getting dex zip field, " + e.getMessage());
			throw new SiminovCriticalException(LibraryHelper.class.getName(), "getLibraries", e.getMessage());
		}
		
		
		dexField.setAccessible(true);

		PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();
	    
		ZipFile[] dexs = null;
		try {
			dexs = (ZipFile[]) dexField.get(classLoader);
		} catch(Exception e) {
			Log.loge(LibraryHelper.class.getName(), "getLibraries", "Exception caught while getting zip file from class loader, " + e.getMessage());
			throw new SiminovCriticalException(LibraryHelper.class.getName(), "getLibraries", e.getMessage());
		}

		
		for(ZipFile dex : dexs) {
			
			Enumeration enumeration = dex.entries();
	    	  
			while(enumeration.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) enumeration.nextElement();
				
				if(entry.getName().contains(Constants.LIBRARY_DESCRIPTOR_FILE_NAME)) {
					
					String library = entry.getName().substring(0, entry.getName().indexOf(Constants.LIBRARY_DESCRIPTOR_FILE_NAME) - 1);
					libraries.add(library);
				}
			}
		}
		
		
		return libraries.iterator();
	}
}
