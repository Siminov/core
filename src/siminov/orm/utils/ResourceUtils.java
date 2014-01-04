package siminov.orm.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;

import siminov.orm.Constants;
import siminov.orm.exception.SiminovException;
import siminov.orm.model.IDescriptor;

public class ResourceUtils {

	public static String resolve(final String resourceName, final String resourceValue, final IDescriptor...descriptors) throws SiminovException {
		
		if(resourceValue == null) {
			return resourceValue;
		}
		
		if(resourceValue.contains(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_REFER_REFERENCE)) {

			//Find {}
			int openingCurlyBracketIndex = resourceValue.indexOf(Constants.RESOURCE_SPACE) + 1;
			
			int singleClosingCurlyBracketIndex = resourceValue.indexOf(Constants.RESOURCE_CLOSE_CURLY_BRACKET);
			int doubleClosingCurlyBracketIndex = resourceValue.indexOf(Constants.RESOURCE_CLOSE_CURLY_BRACKET + Constants.RESOURCE_CLOSE_CURLY_BRACKET);

			String resourceKey;
			
			if(doubleClosingCurlyBracketIndex != -1) {

				resourceKey = resourceValue.substring(openingCurlyBracketIndex, doubleClosingCurlyBracketIndex + 1);
				int slashIndex = resourceKey.lastIndexOf(Constants.RESOURCE_SLASH);

				//Find {-
				String resourceClass = resourceKey.substring(0, resourceKey.substring(0, slashIndex).lastIndexOf(Constants.RESOURCE_DOT));
				String resourceAPI = resourceKey.substring(resourceKey.substring(0, slashIndex).lastIndexOf(Constants.RESOURCE_DOT) + 1, resourceKey.substring(0, slashIndex).length());

				Collection<Class<?>> resourceAPIParameterTypes = new LinkedList<Class<?>>();
				Collection<String> resourceAPIParameters = new LinkedList<String>();
				
				
				//Find -}}
				String apiParameters = resourceKey.substring(slashIndex + 1, resourceKey.lastIndexOf(Constants.RESOURCE_CLOSE_CURLY_BRACKET) + 1);
				
				//Resolve all API parameters
				StringTokenizer apiParameterTokenizer = new StringTokenizer(apiParameters, Constants.RESOURCE_COMMA);
				
				while(apiParameterTokenizer.hasMoreElements()) {
					String apiParameter = apiParameterTokenizer.nextToken();
					
					resourceAPIParameterTypes.add(String.class);
					resourceAPIParameters.add(resolve(resourceName, apiParameter, descriptors));
				}
				
			
				int count = 0;
				Class<?>[] apiParameterTypes = new Class<?>[resourceAPIParameters.size()];
				for(Class<?> resourceAPIParameterType : resourceAPIParameterTypes) {
					apiParameterTypes[count++] = resourceAPIParameterType;
				}
				

				Object classObject = ClassUtils.createClassInstance(resourceClass);
				String resolvedValue = (String) ClassUtils.invokeMethod(classObject, resourceAPI, apiParameterTypes, resourceAPIParameters.toArray());
				
				return resolve(resourceName, resolvedValue, descriptors);
			} else {

				resourceKey = resourceValue.substring(openingCurlyBracketIndex, singleClosingCurlyBracketIndex);
				int dotIndex = resourceKey.lastIndexOf(Constants.RESOURCE_DOT);

				String resourceClass = resourceKey.substring(0, dotIndex);

				String resourceAPI = resourceKey.substring(resourceKey.lastIndexOf(Constants.RESOURCE_DOT) + 1, resourceKey.length());
				
				Object classObject = ClassUtils.createClassInstance(resourceClass);
				String value = (String) ClassUtils.getValue(classObject, resourceAPI);
			
				String resolvedValue = resourceValue.replace(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_REFER_REFERENCE + Constants.RESOURCE_SPACE + resourceKey + Constants.RESOURCE_CLOSE_CURLY_BRACKET, value);
				return resolve(resourceName, resolvedValue, descriptors);
			}
		} else if(resourceValue.contains(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_SELF_REFERENCE)) {
			
			String key = resourceValue.substring(resourceValue.indexOf(Constants.RESOURCE_SPACE) + 1, resourceValue.indexOf(Constants.RESOURCE_CLOSE_CURLY_BRACKET));
			String value = null;
			
			
			for(IDescriptor descriptor: descriptors) {
				
				if(descriptor.containProperty(key)) {
					value = descriptor.getProperty(key);
					break;
				}
			}
			
			return resolve(key, value, descriptors);
		} else if(resourceValue.contains(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_INLINE_REFERENCE)) {
			
			String key = resourceValue.substring(resourceValue.indexOf(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_INLINE_REFERENCE) + (Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_INLINE_REFERENCE).length() + 1, resourceValue.indexOf(Constants.RESOURCE_CLOSE_CURLY_BRACKET));
			String value = null;
			
			for(IDescriptor descriptor: descriptors) {
				
				if(descriptor.containProperty(key)) {
					value = descriptor.getProperty(key);
					break;
				}
			}

			
			String resolvedValue = resourceValue.replace(Constants.RESOURCE_OPEN_CURLY_BRACKET + Constants.RESOURCE_INLINE_REFERENCE + " " + key + Constants.RESOURCE_CLOSE_CURLY_BRACKET, value);
			return resolve(resourceName, resolvedValue, descriptors);
		} 
		
		return resourceValue;
	}
	
}
