package com.yiij.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiij.base.Application;
import com.yiij.base.ComponentConfig;
import com.yiij.base.HttpException;
import com.yiij.base.interfaces.IContext;
import com.yiij.base.interfaces.IWebApplication;

public class WebApplication extends Application implements IWebApplication
{
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
	public void processRequest() throws Exception
	{
		String route = getUrlManager().parseUrl(getRequest());
		runController(route);
	}

	public void runController(String route) throws Exception
	{
		Object[] ca = createController(route);
		
		if (ca.length == 2)
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
			throw new HttpException(404, "Unable to resolve the request {route}.");
		
	}

	public Object[] createController(String route)
	{
		return createController(route, null);
	}
	
	public Object[] createController(String route, WebModule $owner)
	{
		return new Object[] { new Controller(), "index" };
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
