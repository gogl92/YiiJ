package com.yiij;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiij.base.AbstractContext;
import com.yiij.base.Component;
import com.yiij.base.ComponentConfig;
import com.yiij.web.WebApplication;

public class Root
{
	public static WebApplication createWebApplication(ComponentConfig config, 
			ServletConfig servletConfig,
			HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		if (config.containsKey("application") && (
			((ComponentConfig)config.get("application")).className == null || ((ComponentConfig)config.get("application")).className.equals("") ) )
			((ComponentConfig)config.get("application")).className = WebApplication.class.getCanonicalName();
		try
		{
			return (WebApplication)Component.newInstance(new AbstractContext(), 
					config.containsKey("application")?config.get("application"):WebApplication.class.getCanonicalName(), 
					servletConfig, request, response);
		} catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
	
	public static String getVersion()
	{
		return "1.1.9";
	}
}
