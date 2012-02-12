package com.yiij.web;

import com.yiij.base.interfaces.IContext;

public abstract class BaseController extends WebComponent
{
	public BaseController(IContext context)
	{
		super(context);
	}

	/**
	 * Returns the view script file according to the specified view name.
	 * This method must be implemented by child classes.
	 * @param String viewName view name
	 * @return String the file path for the named view. null if the view cannot be found.
	 */
	//abstract public String getViewFile(String viewName);
	
}
