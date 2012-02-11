package com.yiij.base;

import com.yiij.base.interfaces.IContext;

public class AbstractContext implements IContext
{
	private Application _application;
	
	public AbstractContext(Application application)
	{
		super();
		_application = application;
	}

	@Override
	public Application getApplication()
	{
		return _application;
	}

}
