package com.yiij.web.renderers;

import java.io.IOException;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.BaseController;
import com.yiij.web.WebApplicationComponent;
import com.yiij.web.interfaces.IPluginViewRenderer;
import com.yiij.web.interfaces.IViewRenderer;

public class ClassRenderer extends WebApplicationComponent implements IPluginViewRenderer
{
	public ClassRenderer(IContext context)
	{
		super(context);
	}
	
	@Override
	public String getFileExtension()
	{
		return null;
	}

	@Override
	public String renderFile(BaseController controller, String file,
			Object data, boolean doReturn) throws IOException
	{
		try
		{
			Class<?> c = Class.forName(file);
			return ((IViewRenderer)c.newInstance()).renderFile(controller, file, data, doReturn);
		} catch (ClassNotFoundException e)
		{
			throw new com.yiij.base.Exception(e);
		} catch (InstantiationException e)
		{
			throw new com.yiij.base.Exception(e);
		} catch (IllegalAccessException e)
		{
			throw new com.yiij.base.Exception(e);
		}
	}
}
