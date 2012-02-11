package com.yiij.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;

import com.yiij.base.ApplicationComponent;
import com.yiij.base.interfaces.IContext;
import com.yiij.base.interfaces.IWebApplication;
import com.yiij.base.Exception;

public class HttpRequest extends ApplicationComponent
{
	private IWebApplication _webApplication;
	private String _hostInfo = null;
	private String _baseUrl = null;
	
	public HttpRequest(IContext context)
	{
		super(context);
		
		if (!(context.getApplication() instanceof IWebApplication))
			throw new Exception("HttpRequest only works with IWebApplication");
		_webApplication = (IWebApplication)context.getApplication();
	}

	public String getParam(String name)
	{
		return getParam(name, null);
	}
	
	public String getParam(String name, String defaultValue)
	{
		String ret = _webApplication.getServletRequest().getParameter(name);
		if (ret == null)
			ret = defaultValue;
		return ret;
	}
	
	public String getQuery(String name)
	{
		return getParam(name, null);
	}
	
	public String getQuery(String name, String defaultValue)
	{
		return getParam(name, defaultValue);
	}
	
	public String getPost(String name)
	{
		return getParam(name, null);
	}
	
	public String getPost(String name, String defaultValue)
	{
		return getParam(name, defaultValue);
	}
	
	public String getUrl()
	{
		return _webApplication.getServletRequest().getRequestURL().toString();
	}

	public String getHostInfo()
	{
		return getHostInfo("");
	}
	
	public String getHostInfo(String schema)
	{
		if (_hostInfo == null)
		{
			boolean secure = getIsSecureConnection();
			String http;
			if(secure)
				http="https";
			else
				http="http";
			if(!_webApplication.getServletRequest().getLocalName().equals(""))
				_hostInfo=http+"://"+_webApplication.getServletRequest().getLocalName();
			else
			{
				_hostInfo=http+"://"+_webApplication.getServletRequest().getServerName();
				int port=secure ? getSecurePort() : getPort();
				if((port!=80 && !secure) || (port!=443 && secure))
					_hostInfo+=':'+port;
			}
			
		}
		
		
		if (!schema.equals(""))
		{
			boolean secure = getIsSecureConnection();
			if(secure && schema.equals("https") || !secure && schema.equals("http"))
				return _hostInfo;

			int port = schema.equals("https") ? getSecurePort() : getPort();
			String portStr;
			if(port!=80 && schema.equals("http") || port!=443 && schema.equals("https"))
				portStr=":"+port;
			else
				portStr="";

			int pos = _hostInfo.indexOf(":");
			return schema+_hostInfo.substring(pos, _hostInfo.indexOf(":", pos+1))+portStr;
			
		}
		else
			return _hostInfo;
	}
	
	public void setHostInfo(String value)
	{
		_hostInfo = StringUtils.stripEnd(value, "/");
	}
	
	public String getBaseUrl()
	{
		/*
		if(_baseUrl == null)
			_baseUrl = StringUtils.stripEnd(dirname($this->getScriptUrl()),"\\/");
		return $absolute ? $this->getHostInfo() . $this->_baseUrl : $this->_baseUrl;
		*/
		return null;
	}
	
	public void setBaseUrl(String value)
	{
		_baseUrl = value;
	}
	
	public String getScriptUrl()
	{
		return null;
	}
	
	public String getPathInfo()
	{
		return _webApplication.getServletRequest().getPathInfo();
	}
	
	public String urldecode(String value)
	{
		try
		{
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return value;
		}
	}
	
	public String getRequestUri()
	{
		return _webApplication.getServletRequest().getRequestURI();
	}
	
	public String getQueryString()
	{
		return _webApplication.getServletRequest().getQueryString();
	}
	
	public boolean getIsSecureConnection()
	{
		return _webApplication.getServletRequest().isSecure();
	}
	
	public String getRequestType()
	{
		return _webApplication.getServletRequest().getMethod();
	}
	
	public boolean getIsPostRequest()
	{
		return _webApplication.getServletRequest().getMethod().equals("POST");
	}
	
	public String getServerName()
	{
		return _webApplication.getServletRequest().getServerName();
	}
	
	public int getServerPort()
	{
		return _webApplication.getServletRequest().getServerPort();
	}
	
	public String getUrlReferrer()
	{
		return _webApplication.getServletRequest().getHeader("Referer");
	}
	
	public String getUserAgent()
	{
		return _webApplication.getServletRequest().getHeader("User-Agent");
	}
	
	public String getUserHostAddress()
	{
		return _webApplication.getServletRequest().getRemoteAddr();
	}
	
	public String getUserHost()
	{
		return _webApplication.getServletRequest().getRemoteHost();
	}
	
	public String getAcceptTypes()
	{
		return _webApplication.getServletRequest().getHeader("Accept");
	}
	
	private Integer _port = null;
	
	public int getPort()
	{
		if (_port == null)
			_port = !getIsSecureConnection() ? _webApplication.getServletRequest().getServerPort() : 80;
		return _port;
	}
	
	public void setPort(int value)
	{
		_port = value;
		_hostInfo = null;
	}
	
	private Integer _securePort = null;
	
	public int getSecurePort()
	{
		if (_securePort == null)
			_securePort = getIsSecureConnection() ? _webApplication.getServletRequest().getServerPort() : 443;
		return _securePort;
	}
	
	public void setSecurePort(int value)
	{
		_securePort = value;
		_hostInfo = null;
	}
	
}
