package com.yiij.web;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.yiij.base.interfaces.IContext;

public class UrlManager extends WebApplicationComponent
{
	public static final String GET_FORMAT="get";
	public static final String PATH_FORMAT="path";
	
	public String routeVar = "r";
	public boolean caseSensitive = true; 
	
	private String _urlFormat = UrlManager.GET_FORMAT;
	private String _baseUrl;
	
	public UrlManager(IContext context)
	{
		super(context);
	}
	
	@Override
	public void init()
	{
		super.init();
		//processRules();
	}

	/**
	 * Returns the base URL of the application.
	 * @return String the base URL of the application (the part after host name and before query string).
	 * If {@link showScriptName} is true, it will include the script name part.
	 * Otherwise, it will not, and the ending slashes are stripped off.
	 */
	public String getBaseUrl()
	{
		if(_baseUrl!=null)
			return _baseUrl;
		else
		{
			_baseUrl = webApp().getRequest().getBaseUrl();
			return _baseUrl;
		}		
	}
	
	/**
	 * Parses the user request.
	 * @param CHttpRequest $request the request application component
	 * @return string the route (controllerID/actionID) and perhaps GET parameters in path format.
	 */
	public String parseUrl(HttpRequest request)
	{
		if (getUrlFormat() == UrlManager.PATH_FORMAT)
		{
			return "";
		}
		else if (request.getParam(routeVar, null) != null)
		{
			return StringUtils.strip(request.getParam(routeVar), "\\/");
		}
		else
			return "";
	}	
	
	public void parsePathInfo(String pathInfo)
	{
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m;
		
		if(pathInfo.equals(""))
			return;
		String []segs = (pathInfo+"/").split("/");
		int n = segs.length;
		for(int i=0; i < n-1; i += 2)
		{
			String key = segs[i];
			if(key.equals("")) continue;
			String value = segs[i+1];
			m=pattern.matcher(key);
			@SuppressWarnings("unused")
			int pos;
			if((pos = key.indexOf("["))!=-1 && m.find())
			{
				// TODO array params
				/*
				String name = key.substring(0, pos+1); // substr($key,0,$pos);
				for(int j = m.groupCount() -1 ; j >= 0; --j)
				{
					if($matches[1][$j]==='')
						$value=array($value);
					else
						$value=array($matches[1][$j]=>$value);
				}
				if(isset($_GET[$name]) && is_array($_GET[$name]))
					$value=CMap::mergeArray($_GET[$name],$value);
				$_REQUEST[$name]=$_GET[$name]=$value;
				*/
			}
			else
				((WebApplication)context().getApplication()).getRequest().setParam(key, value);
		}		
	}
	
	public String getUrlFormat()
	{
		return _urlFormat;
	}
	
	public void setUrlFormat(String value)
	{
		if(value.equals(UrlManager.PATH_FORMAT) || value.equals(UrlManager.GET_FORMAT))
			_urlFormat = value;
		else
			throw new com.yiij.base.Exception("CUrlManager.UrlFormat must be either 'path' or 'get'.");
	}
	
	public String createUrl(String route, Map<String, String> params, String ampersand)
	{
		return null;
	}
}
