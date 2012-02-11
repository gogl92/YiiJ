package com.yiij.base;

import com.yiij.base.interfaces.IApplicationComponent;

public class ApplicationComponent implements IApplicationComponent
{
	private boolean _initialized = false;
	
	@Override
	public void init()
	{
		_initialized = true;
	}

	@Override
	public boolean getIsInitialized()
	{
		return _initialized;
	}

}
