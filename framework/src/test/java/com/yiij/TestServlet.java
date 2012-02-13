package com.yiij;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockServletContext;

public class TestServlet extends HttpServlet
{
	private MockServletConfig _config; 
	private MockServletContext _context; 
	
	public TestServlet()
	{
		super();
	}

	@Override
	public void init() throws ServletException
	{
		super.init();
		if (getServletConfig() == null)
		{
			_config = new MockServletConfig();
			_context = new MockServletContext();
			
			_config.setServletContext(_context);
			
			init(_config);
			
			return;
		}
	}
}
