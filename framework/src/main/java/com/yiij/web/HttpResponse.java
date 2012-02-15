package com.yiij.web;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import com.yiij.base.interfaces.IContext;

public class HttpResponse extends WebApplicationComponent
{
	public HttpResponse(IContext context)
	{
		super(context);
	}
	
	public boolean isHeadersSent()
	{
		return webApp().getServletResponse().isCommitted();
	}
	
	public void flush() throws IOException
	{
		webApp().getServletResponse().flushBuffer();
	}
	
	public String getCharacterEncoding()
	{
		return webApp().getServletResponse().getCharacterEncoding();
	}
	
	public String getContentType()
	{
		return webApp().getServletResponse().getContentType();
	}
	
	public ServletOutputStream getOutputStream() throws IOException
	{
		return webApp().getServletResponse().getOutputStream();
	}

	public java.io.PrintWriter getWriter() throws IOException
	{
		return webApp().getServletResponse().getWriter();
	}
	
	public void setContentType(String type)
	{
		webApp().getServletResponse().setContentType(type);
	}
	
	public void setContentLength(int len)
	{
		webApp().getServletResponse().setContentLength(len);
	}
	
	public void setCharacterEncoding(String charset)
	{
		webApp().getServletResponse().setCharacterEncoding(charset);
	}
	
	public void addHeader(String name, String value)
	{
		webApp().getServletResponse().addHeader(name, value);
	}

	public void setHeader(String name, String value)
	{
		webApp().getServletResponse().setHeader(name, value);
	}
	
	public boolean containsHeader(String name)
	{
		return webApp().getServletResponse().containsHeader(name);
	}
	
	public void sendRedirect(String location) throws IOException
	{
		webApp().getServletResponse().sendRedirect(location);
	}
	
	public void setStatus(int sc)
	{
		webApp().getServletResponse().setStatus(sc);
	}

	@SuppressWarnings("deprecation")
	public void setStatus(int sc, String message)
	{
		webApp().getServletResponse().setStatus(sc, message);
	}
	
	public void sendError(int sc) throws IOException
	{
		webApp().getServletResponse().sendError(sc);
	}
	
	public void sendError(int sc, java.lang.String msg) throws IOException
	{
		webApp().getServletResponse().sendError(sc, msg);
	}
}
