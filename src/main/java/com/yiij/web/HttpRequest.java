package com.yiij.web;

import com.yiij.base.ApplicationComponent;
import com.yiij.base.interfaces.IWebApplication;

public class HttpRequest extends ApplicationComponent
{
	private IWebApplication _application;
	
	public HttpRequest(IWebApplication application)
	{
		super();
		_application = application;
	}
}
