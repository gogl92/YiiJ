package com.yiij.web;

import com.yiij.base.ApplicationComponent;
import com.yiij.base.Exception;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.interfaces.IWebComponent;

public class WebApplicationComponent extends ApplicationComponent implements IWebComponent
{
	public WebApplicationComponent(IContext context)
	{
		super(context);
		if (!(context().getApplication() instanceof WebApplication))
			throw new Exception("WebComponent only works with WebApplication");

	}

	public WebApplication webApp()
	{
		return (WebApplication)context().getApplication();
	}
}
