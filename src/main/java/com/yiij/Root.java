package com.yiij;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiij.base.Application;
import com.yiij.web.WebApplication;

public class Root
{
	public static WebApplication createWebApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		return new WebApplication(request, response);
	}
}
