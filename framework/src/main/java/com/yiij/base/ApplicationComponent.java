package com.yiij.base;

import com.yiij.base.interfaces.IApplicationComponent;
import com.yiij.base.interfaces.IContext;

public class ApplicationComponent extends Component implements IApplicationComponent
{
	private boolean _initialized = false;
	
	public ApplicationComponent(IContext context)
	{
		super(context);
	}
	
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
