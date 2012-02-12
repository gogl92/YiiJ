package com.yiij.web;

import com.yiij.base.Component;
import com.yiij.base.Exception;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.interfaces.IWebComponent;

public class WebComponent extends Component implements IWebComponent
{
	public WebComponent(IContext context)
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
