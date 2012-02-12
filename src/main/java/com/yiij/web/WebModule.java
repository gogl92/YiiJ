package com.yiij.web;

import com.yiij.base.Module;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.interfaces.IWebModule;

public class WebModule extends Module implements IWebModule
{
	private String _defaultController = "default";
	private String _layout;
	private String _viewPath;
	private String _layoutPath;
	private String _viewPackageName;

	public WebModule(IContext context, String id, Module parent)
	{
		super(context, id, parent);
	}

	/**
	 * The ID of the default controller for this module. Defaults to 'default'.
	 * @return String 
	 */
	@Override
	public String getDefaultController()
	{
		return _defaultController;
	}

	/**
	 * @see #getDefaultController()
	 * @param String
	 */
	public void setDefaultController(String value)
	{
		_defaultController = value;
	}
	
	/**
	 * The layout that is shared by the controllers inside this module.
	 * If a controller has explicitly declared its own {@link Controller.layout layout},
	 * this property will be ignored.
	 * If this is null (default), the application's layout or the parent module's layout (if available)
	 * will be used. If this is blank, then no layout will be used.
	 * @return String 
	 */
	public String getLayout()
	{
		return _layout;
	}
	
	/**
	 * @see #getLayout()
	 * @param String
	 */
	public void setLayout(String value)
	{
		_layout = value;
	}
	
	/**
	 * @return the root directory of view files. Defaults to 'moduleDir/views' where
	 * moduleDir is the directory containing the module class.
	 */
	public String getViewPath()
	{
		if(_viewPath!=null)
			return _viewPath;
		else
			return _viewPath=getBasePath()+System.getProperty("file.separator")+"views";
	}

	/**
	 * @param path the root directory of view files.
	 * @throws Exception if the directory does not exist.
	 */
	public void setViewPath(String path)
	{
		_viewPath = path;
		
		/*
		if(($this->_viewPath=realpath($path))===false || !is_dir($this->_viewPath))
			throw new CException(Yii::t('yii','The view path "{path}" is not a valid directory.',
				array('{path}'=>$path)));
		*/
	}
	
	/**
	 * @return the root directory of layout files. Defaults to 'moduleDir/views/layouts' where
	 * moduleDir is the directory containing the module class.
	 */
	public String getLayoutPath()
	{
		if(_layoutPath!=null)
			return _layoutPath;
		else
			return _layoutPath=getViewPath()+"/layouts";
	}

	/**
	 * @param path the root directory of layout files.
	 * @throws Exception if the directory does not exist.
	 */
	public void setLayoutPath(String path)
	{
		_layoutPath = path;
		/*
		if(($this->_layoutPath=realpath($path))===false || !is_dir($this->_layoutPath))
			throw new CException(Yii::t('yii','The layout path "{path}" is not a valid directory.',
				array('{path}'=>$path)));
		*/
	}
	
	
	public String getViewPackageName()
	{
		if (_viewPackageName == null)
			_viewPackageName = getPackageName()+".views";
		return _viewPackageName;
	}
	
	public void setViewPackageName(String value)
	{
		_viewPackageName = value;
	}
}
