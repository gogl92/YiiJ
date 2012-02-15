package com.yiij.web.widgets;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import com.yiij.base.interfaces.IContext;
import com.yiij.utils.StringHelper;
import com.yiij.web.BaseController;
import com.yiij.web.Controller;
import com.yiij.web.interfaces.IViewRenderer;
import com.yiij.web.interfaces.IWebModule;

public class Widget extends BaseController
{

	private static int _counter = 0;
	private String _id;
	private BaseController _owner;

	/**
	 * integer the counter for generating implicit IDs.
	 */
	/**
	 * @var array view paths for different types of widgets
	 */
	private static Map<String, String> _viewPaths = new Hashtable<String, String>();
	
	public Widget(IContext context, BaseController owner)
	{
		super(context);
		_owner = owner != null ? owner : webApp().getController();
	}

	/**
	 * @return owner/creator of this widget. It could be either a widget or a controller.
	 */
	public BaseController getOwner()
	{
		return _owner;
	}

	/**
	 * @see #getId(boolean)
	 */
	public String getId()
	{
		return getId(true);
	}
	
	/**
	 * Returns the ID of the widget or generates a new one if requested.
	 * @param autoGenerate whether to generate an ID if it is not set previously
	 * @return id of the widget.
	 */
	public String getId(boolean autoGenerate)
	{
		if(_id!=null)
			return _id;
		else if(autoGenerate)
			return _id="yw"+Widget._counter++;
		return null;
	}

	/**
	 * Sets the ID of the widget.
	 * @param value id of the widget.
	 */
	public void setId(String value)
	{
		_id = value;
	}

	/**
	 * Returns the controller that this widget belongs to.
	 * @return the controller that this widget belongs to.
	 */
	public Controller getController()
	{
		if(_owner instanceof Controller)
			return (Controller)_owner;
		else
			return webApp().getController();
	}
	
	/**
	 * Executes the widget.
	 * This method is called by {@link BaseController#endWidget()}.
	 */
	public void run() throws IOException
	{
	}
	
	/**
	 * Executes the widget, returning the output.
	 * @throws IOException 
	 */
	public String output() throws IOException
	{
		webApp().getResponse().ob_start();
		run();		
		String output = webApp().getResponse().ob_get_clean();
		return output;
	}

	/**
	 * Returns the directory containing the view files for this widget.
	 * The default implementation returns the 'views' subdirectory of the directory containing the widget class file.
	 * If $checkTheme is set true, the directory "ThemeID/views/ClassName" will be returned when it exists.
	 * @return the directory containing the view files for this widget.
	 */
	public String getViewPath()
	{
		String className=getClass().getCanonicalName();
		if (!Widget._viewPaths.containsKey(className))
		{
			Widget._viewPaths.put(className, webApp().getBasePath()+"/widgets");
		}
		return Widget._viewPaths.get(className);
	}
	
	
	/**
	 * Looks for the view script file according to the view name.
	 * This method will look for the view under the widget's {@link getViewPath viewPath}.
	 * The view script file is named as "ViewName.php". A localized view file
	 * may be returned if internationalization is needed. See {@link CApplication::findLocalizedFile}
	 * for more details.
	 * The view name can also refer to a path alias if it contains dot characters.
	 * @param viewName name of the view (without file extension)
	 * @return the view file path. False if the view file does not exist
	 * @see Application#findLocalizedFile()
	 */
	@Override
	public String getViewFile(String viewName)
	{
		IViewRenderer renderer = webApp().getViewRenderer();
		boolean isClassBased = renderer.getFileExtension() == null;
		String extension = renderer.getFileExtension();

		String viewFile;
		if(viewName.indexOf(".")!=-1)
		{
			if (!isClassBased)
				viewFile = webApp().getPathOfAlias(viewName);
			else
				viewFile = viewName; // if contains dot, is a full class name (ex.: "com.test.layouts.MainLayout")
		}
		else
		{
			String viewPath = getViewPath();
			if (!isClassBased)
				viewFile=viewPath+"/"+viewName;
			else
			{
				viewFile=getClass().getPackage().getName()+viewPath+""+StringHelper.upperCaseFirst(viewName)+"View";
			}
		}

		if (isClassBased)
			return viewFile;
		else if (getClass().getResource(viewFile+extension)!=null)
			return webApp().findLocalizedFile(viewFile+extension);
		//else if($extension!=='.php' && is_file($viewFile.'.php'))
			//return Yii::app()->findLocalizedFile($viewFile.'.php');
		else
			return null;
	}

	/**
	 * @see #render(String, Object, boolean)
	 */
	public String render(String view) throws IOException
	{
		return render(view, null, false);
	}

	/**
	 * @see #render(String, Object, boolean)
	 */
	public String render(String view, Object data) throws IOException
	{
		return render(view, data, false);
	}
	
	/**
	 * Renders a view.
	 *
	 * The named view refers to a PHP script (resolved via {@link getViewFile})
	 * that is included by this method. If $data is an associative array,
	 * it will be extracted as PHP variables and made available to the script.
	 *
	 * @param view name of the view to be rendered. See {@link getViewFile} for details
	 * about how the view script is resolved.
	 * @param data data to be extracted into PHP variables and made available to the view script
	 * @param return whether the rendering result should be returned instead of being displayed to end users
	 * @return the rendering result. Null if the rendering result is not required.
	 * @throws IOException 
	 * @see #getViewFile()
	 */
	public String render(String view, Object data, boolean doReturn) throws IOException
	{
		String viewFile;
		if((viewFile=getViewFile(view))!=null)
			return renderFile(viewFile,data,doReturn);
		else
			throw new com.yiij.base.Exception(getClass().getCanonicalName()+" cannot find the view '"+view+"'.");
	}
	
}
