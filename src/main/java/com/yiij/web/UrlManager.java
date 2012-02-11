package com.yiij.web;

import com.yiij.base.ApplicationComponent;
import com.yiij.base.interfaces.IContext;

public class UrlManager extends ApplicationComponent
{
	public UrlManager(IContext context)
	{
		super(context);
	}
	
	@Override
	public void init()
	{
		super.init();
		//processRules();
	}

	/**
	 * Parses the user request.
	 * @param CHttpRequest $request the request application component
	 * @return string the route (controllerID/actionID) and perhaps GET parameters in path format.
	 */
	public String parseUrl(HttpRequest request)
	{
		return null;
	}	
}
