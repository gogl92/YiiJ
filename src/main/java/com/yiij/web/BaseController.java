package com.yiij.web;

import java.io.IOException;
import java.util.Stack;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.interfaces.IViewRenderer;

public abstract class BaseController extends WebComponent
{
	private Stack<BaseController> _widgetStack = new Stack<BaseController>();
	
	public BaseController(IContext context)
	{
		super(context);
	}

	/**
	 * Returns the view script file according to the specified view name.
	 * This method must be implemented by child classes.
	 * @param viewName view name
	 * @return the file path for the named view. null if the view cannot be found. blank ("") if not file based.
	 */
	abstract public String getViewFile(String viewName);
	
	abstract public String getViewPackageName();
	
	/**
	 * @see #renderWithRenderer(String, Object, boolean)
	 */
	public String renderWithRenderer(IViewRenderer renderer, String viewFile, Object data) throws IOException
	{
		return renderWithRenderer(renderer, viewFile, data, false);
	}
	
	/**
	 * Renders a view file.
	 *
	 * @param string $viewFile view file path
	 * @param array $data data to be extracted and made available to the view
	 * @param boolean $return whether the rendering result should be returned instead of being echoed
	 * @return string the rendering result. Null if the rendering result is not required.
	 * @throws Exception if the view file does not exist
	 * @throws IOException
	 */
	public String renderWithRenderer(IViewRenderer renderer, String viewFile, Object data, boolean doReturn) throws IOException
	{
		int widgetCount = _widgetStack.size();
		String content = renderer.renderFile(this, viewFile, data, doReturn);
		if(_widgetStack.size()==widgetCount)
			return content;
		else
		{
			BaseController widget=_widgetStack.peek();
			throw new com.yiij.base.Exception(getClass().getCanonicalName()+" contains improperly nested widget tags in its view '"+viewFile+"'. A "+widget.getClass().getCanonicalName()+" widget does not have an endWidget() call.");
		}
	}
}
