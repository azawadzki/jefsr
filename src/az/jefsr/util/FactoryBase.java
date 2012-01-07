package az.jefsr.util;

import java.util.HashMap;
import java.util.Map;

public class FactoryBase<T> {
	
	protected FactoryBase() {
		
	}
	
	public void registerType(String token, Class<? extends T> type) {
		registeredTypes.put(token, type);
	}

	public Class<? extends T> fetchType(String token) {
		return registeredTypes.get(token); 
	}
	
	private Map<String, Class<? extends T>> registeredTypes = new HashMap<String, Class<? extends T>>();
}

