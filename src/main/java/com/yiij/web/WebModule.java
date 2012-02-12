package com.yiij.web;

import com.yiij.base.Module;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.interfaces.IWebModule;

public class WebModule extends Module implements IWebModule
{
	private String _defaultController = "default";

	public WebModule(IContext context, String id)
	{
		this(context, id, null);
	}
	
	public WebModule(IContext context, String id, Module parent)
	{
		super(context, id, parent);
	}

	@Override
	public String getDefaultController()
	{
		return _defaultController;
	}

	public void setDefaultController(String value)
	{
		_defaultController = value;
	}
}
