package com.yiij;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiij.base.Component;
import com.yiij.base.ComponentConfig;
import com.yiij.web.WebApplication;

public class Root
{
	public static WebApplication createWebApplication(ComponentConfig config, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		if (config.contains("appplication") && (
			((ComponentConfig)config.get("appplication")).className == null || ((ComponentConfig)config.get("appplication")).className.equals("") ) )
			((ComponentConfig)config.get("appplication")).className = WebApplication.class.getCanonicalName();
		try
		{
			return (WebApplication)Component.newInstance(null, config.contains("application")?config.get("application"):WebApplication.class.getCanonicalName(), request, response);
		} catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
}
