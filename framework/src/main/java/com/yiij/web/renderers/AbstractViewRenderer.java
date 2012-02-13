package com.yiij.web.renderers;

import com.yiij.web.interfaces.IViewRenderer;

public abstract class AbstractViewRenderer implements IViewRenderer
{
	public AbstractViewRenderer()
	{
		super();
	}
	
	@Override
	public String getFileExtension()
	{
		return null;
	}
}
