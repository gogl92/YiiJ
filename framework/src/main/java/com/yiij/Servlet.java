package com.yiij;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiij.base.ComponentConfig;

@SuppressWarnings("serial")
public class Servlet extends HttpServlet
{
	private final Logger logger = LoggerFactory.getLogger(Servlet.class);	
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		logger.debug("YiiJ Servlet request {} ({})", request.getPathInfo());
    	
    	// serve static files using the default handler
    	String absoluteFilePath = getServletContext().getRealPath(request.getPathInfo());
    	File file = new File (absoluteFilePath);
    	if (file.exists() && !file.isDirectory())
    	{
            RequestDispatcher rd = getServletContext().getNamedDispatcher("default");

            HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
                    public String getServletPath() { return ""; }
            };

            rd.forward(wrapped, response);
        }
    	
		ComponentConfig config = new ComponentConfig();
		config.parseConfigXml(new FileInputStream(getServletContext().getRealPath("/WEB-INF/yiij.xml")));
    	
    	com.yiij.Root.createWebApplication(config, getServletConfig(), request, response).run();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	doGet(request, response);
    }
    
    //protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    //protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
}
