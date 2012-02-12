package com.yiij.base;

import java.io.IOException;

import javax.servlet.ServletException;

import com.yiij.base.interfaces.IContext;

public abstract class Application extends Module
{
	private String _basePath;
	
	public Application(IContext context)
	{
		super(context, "", null);
		
		context.setApplication(this);
		
		registerCoreComponents();
	}

	/**
	 * Processes the request.
	 * This is the place where the actual request processing work is done.
	 * Derived classes should override this method.
	 */
	abstract public void processRequest() throws java.lang.Exception;

	
	/**
	 * Returns the root path of the application.
	 * @return the root directory of the application. Defaults to 'protected'.
	 */
	public String getBasePath()
	{
		return _basePath;
	}

	/**
	 * Sets the root directory of the application.
	 * This method can only be invoked at the begin of the constructor.
	 * @param path the root directory of the application.
	 * @throws Exception if the directory does not exist.
	 */
	public void setBasePath(String path)
	{
		_basePath = path;
		
		/*
		if(($this->_basePath=realpath($path))===false || !is_dir($this->_basePath))
			throw new CException(Yii::t('yii','Application base path "{path}" is not a valid directory.',
				array('{path}'=>$path)));
        $this->_autoBasePathUrl = null;
        */
	}
	
	
	
	public void run() throws ServletException, IOException
	{
		try
		{
			processRequest();
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (java.lang.Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void registerCoreComponents()
	{
	}
}
