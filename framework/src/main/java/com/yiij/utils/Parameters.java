package com.yiij.utils;

import java.util.HashMap;

/**
 * Helper class used as parameters (Map<String, Object>).
 * Use {@link Builder} to create an instance easily 
 * @author Rangel Reale
 */
@SuppressWarnings("serial")
public class Parameters extends HashMap<String, Object>
{
	/**
	 * Parameter builder class
	 * @author Rangel Reale
	 */
	public static class Builder
	{
		private Parameters _result;

		/**
		 * Builder construtor.
		 */
		public Builder()
		{
			super();
			_result = new Parameters();
		}
		
		/**
		 * Adds a new value to the resulting Parameters
		 * @param name
		 * @param value
		 * @return
		 */
		public Builder add(String name, Object value)
		{
			_result.put(name,  value);
			return this;
		}
		
		/**
		 * Returns the resulting built Parameters
		 * @return
		 */
		public Parameters build()
		{
			return _result;
		}
	}
}
