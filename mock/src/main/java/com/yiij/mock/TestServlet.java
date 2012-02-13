package com.yiij.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockServletContext;
import com.yiij.Root;
import com.yiij.base.ComponentConfig;
import com.yiij.web.WebApplication;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet
{
	private MockServletConfig _config; 
	private MockServletContext _context; 
	private ComponentConfig _yiijConfig = new ComponentConfig();
	
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
			_context.setRealPath("/", "/YIIJ-INF");
			
			_config.setServletContext(_context);
			
			init(_config);
			
			return;
		}
	}
	
	public void loadYiiJConfig(InputStream is) throws IOException
	{
		_yiijConfig.parseConfigXml(is);
	}

	public Response simulateGet(String url) throws ServletException, IOException
	{
		URL surl = new URL(url);
		
		Map<String, String> params = new HashMap<String, String>();
		if (surl.getQuery()!=null)
		{
		    String[] parseparams = surl.getQuery().split("&");
		    for (String param : parseparams)
		    {
		        String name = param.split("=")[0];
		        String value = param.split("=")[1];
		        params.put(name, value);
		    }
		}
		return simulateGet(surl.getPath(), params);
	}
	
	public Response simulateGet(String uri, Map<String, String> parameters) throws ServletException, IOException
	{
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		WebApplication app;

		request.setMethod("GET");
		request.setRequestURI(uri);
		request.setPathInfo(uri);
		for (String param : parameters.keySet())
			request.setupAddParameter(param, parameters.get(param));
		
		app = Root.createWebApplication(_yiijConfig, _config, request, response);
		app.run();

		response.getWriter().flush();
		return new Response(response.getOutputStreamContent(), response);
	}
	
	public static class Response
	{
		public Response(String output, HttpServletResponse response)
		{
			super();
			this.output = output;
			this.response = response;
		}
		
		public String output;
		public HttpServletResponse response;
	}
}
