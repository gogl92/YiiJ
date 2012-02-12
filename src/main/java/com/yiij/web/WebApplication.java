package com.yiij.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.yiij.base.Application;
import com.yiij.base.Component;
import com.yiij.base.ComponentConfig;
import com.yiij.base.HttpException;
import com.yiij.base.Module;
import com.yiij.base.interfaces.IContext;
import com.yiij.base.interfaces.IWebApplication;
import com.yiij.utils.StringHelper;
import com.yiij.web.interfaces.IWebModule;

public class WebApplication extends Application implements IWebApplication, IWebModule
{
	private String _defaultController = "site";
	
	private HttpServletRequest _servletRequest;
	private HttpServletResponse _servletResponse;

	public WebApplication(IContext context, HttpServletRequest servletRequest,
			HttpServletResponse servletResponse)
	{
		super(context);
		_servletRequest = servletRequest;
		_servletResponse = servletResponse;
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
	
	@Override
	public void processRequest() throws Exception
	{
		String route = getUrlManager().parseUrl(getRequest());
		runController(route);
	}

	public void runController(String route) throws Exception
	{
		Object[] ca = createController(route);
		
		if (ca != null && ca.length == 2)
		{
			Controller controller = (Controller)ca[0];
			String actionID = (String)ca[1];
			
			//list($controller,$actionID)=$ca;
			//$oldController=$this->_controller;
			//$this->_controller=$controller;
			//controller.init();
			controller.run(actionID);
			//$this->_controller=$oldController;
		}
		else
			throw new HttpException(404, "Unable to resolve the request '"+route+"'.");
		
	}

	public Object[] createController(String route) throws java.lang.Exception
	{
		return createController(route, null);
	}
	
	public Object[] createController(String route, Module owner) throws java.lang.Exception
	{
		if (owner == null)
			owner = this;
		if (StringUtils.stripEnd(route, "/").equals(""))
		{
			if (owner instanceof IWebModule)
				route = ((IWebModule)owner).getDefaultController();
			else
				route = "default";
		}
		boolean caseSensitive = getUrlManager().caseSensitive;
		String basePath = null;
		String controllerID = null;
		
		route += "/";
		int pos;
		while ((pos=route.indexOf("/"))!= -1)
		{
			String id = route.substring(0, pos);
			if (!caseSensitive)
				id = id.toLowerCase();
			route = route.substring(pos+1);
			if (basePath == null) // first segment
			{
				Module module = owner.getModule(id);
				if (module != null)
					return createController(route, module);
				
				basePath = owner.getPackageName();
				controllerID = "";
			}
			else
			{
				controllerID += "/";
			}
			
			String className = StringHelper.upperCaseFirst(id)+"Controller";
			String classFile = basePath+"."+className;

			try
			{
				Class classClass = Class.forName(classFile);
				if (Controller.class.isAssignableFrom(classClass))
				{
					return new Object[] { Component.newInstance(context(), classClass.getCanonicalName(), 
							new Object[] {controllerID+id, owner==this?null:owner},
							new Class[] {String.class, Module.class}),
							parseActionParams(route) };
				}
				return null;
			} catch (ClassNotFoundException e) {
			}
			controllerID += id;
			//basePath = "/" + id;
		}
		return null;
		
		//return new Object[] { new Controller(context(), "home"), "index" };
	}
	
	protected String parseActionParams(String pathInfo) throws Exception
	{
		int pos;
		if ((pos = pathInfo.indexOf("/")) != -1)
		{
			UrlManager manager = getUrlManager();
			manager.parsePathInfo(pathInfo.substring(pos+1));
			String actionID = pathInfo.substring(0, pos);
			return manager.caseSensitive ? actionID : actionID.toLowerCase();
		}
		else
			return pathInfo;		
	}
	
	public UrlManager getUrlManager() throws java.lang.Exception
	{
		return (UrlManager) getComponent("urlManager");
	}

	public HttpRequest getRequest() throws java.lang.Exception
	{
		return (HttpRequest) getComponent("request");
	}

	@Override
	protected void registerCoreComponents()
	{
		super.registerCoreComponents();
		
		ComponentConfig config = new ComponentConfig();
		config.put("urlManager", new ComponentConfig("com.yiij.web.UrlManager"));
		config.put("request", new ComponentConfig("com.yiij.web.HttpRequest"));
		
		setComponents(config);
		
		//setComponent("urlManager", new UrlManager());
		//setComponent("request", new HttpRequest(this));
	}

	@Override
	public HttpServletRequest getServletRequest()
	{
		return _servletRequest;
	}

	@Override
	public HttpServletResponse getServletRresponse()
	{
		return _servletResponse;
	}
}
