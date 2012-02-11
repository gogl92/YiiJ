package com.yiij.web;

import com.yiij.base.ApplicationComponent;

public class UrlManager extends ApplicationComponent
{
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
