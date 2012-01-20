/*******************************************************************************
 * Copyright (c) 2012, Andrzej Zawadzki (azawadzki@gmail.com)
 * 
 * jefsr is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * jefsr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with jefsr; if not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
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

