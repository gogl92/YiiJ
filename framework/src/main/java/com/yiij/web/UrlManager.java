package com.yiij.web;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.yiij.base.HttpException;
import com.yiij.base.interfaces.IContext;

public class UrlManager extends WebApplicationComponent
{
	public static final String GET_FORMAT="get";
	public static final String PATH_FORMAT="path";
	
	private String _routeVar = "r";
	private boolean _caseSensitive = true; 
	private String _urlFormat = UrlManager.GET_FORMAT;
	private String _baseUrl;
	private String _urlSuffix = "";
	private boolean _useStrictParsing = false;
	
	public UrlManager(IContext context)
	{
		super(context);
	}
	
	/**
	 * Initializes the application component.
	 */
	@Override
	public void init()
	{
		super.init();
		//processRules();
	}

	/**
	 * The URL suffix used when in 'path' format.
	 * For example, ".html" can be used so that the URL looks like pointing to a static HTML page. Defaults to empty.
	 * @return 
	 */
	public String getUrlSuffix()
	{
		return _urlSuffix;
	}
	
	/**
	 * @see #getUrlSuffix()
	 */
	public void setUrlSuffix(String value)
	{
		_urlSuffix = value;
	}
	
	/**
	 * The GET variable name for route. Defaults to 'r'.
	 * @return
	 */
	public String getRouteVar()
	{
		return _routeVar;
	}
	
	/**
	 * @see #getRouteVar()
	 */
	public void setRouteVar(String value)
	{
		_routeVar = value;
	}
	
	/**
	 * Whether routes are case-sensitive. Defaults to true. By setting this to false,
	 * the route in the incoming request will be turned to lower case first before further processing.
	 * As a result, you should follow the convention that you use lower case when specifying
	 * controller mapping ({@link WebApplication#controllerMap}) and action mapping
	 * ({@link Controller#actions}). Also, the directory names for organizing controllers should
	 * be in lower case.
	 * @return
	 */
	public boolean getCaseSensitive()
	{
		return _caseSensitive;
	}
	
	/**
	 * @see #getCaseSensitive()
	 */
	public void setCaseSensitive(boolean value)
	{
		_caseSensitive = value;
	}
	
	/**
	 * Whether to enable strict URL parsing.
	 * This property is only effective when {@link #getUrlFormat()} is 'path'.
	 * If it is set true, then an incoming URL must match one of the {@link #rules URL rules}.
	 * Otherwise, it will be treated as an invalid request and trigger a 404 HTTP exception.
	 * Defaults to false.
	 * @return
	 */
	public boolean getUseStrictParsing()
	{
		return _useStrictParsing;
	}
	
	/**
	 * @see #getUseStrictParsing()
	 */
	public void setUseStrictParsing(boolean value)
	{
		_useStrictParsing = value;
	}

	/**
	 * Constructs a URL.
	 * @param route the controller and the action (e.g. article/read)
	 * @param params list of GET parameters (name=>value). Both the name and value will be URL-encoded.
	 * If the name is '#', the corresponding value will be treated as an anchor
	 * and will be appended at the end of the URL.
	 * @param ampersand the token separating name-value pairs in the URL. Defaults to '&'.
	 * @return the constructed URL
	 */
	public String createUrl(String route, Map<String, String> params, String ampersand)
	{
		return null;
	}
	
	/**
	 * Creates a URL based on default settings.
	 * @param route the controller and the action (e.g. article/read)
	 * @param params list of GET parameters
	 * @param ampersand the token separating name-value pairs in the URL.
	 * @return the constructed URL
	 */
	public String createUrlDefault(String route, Map<String, String> params, String ampersand)
	{
		return null;
	}
	
	/**
	 * Parses the user request.
	 * @param CHttpRequest $request the request application component
	 * @return string the route (controllerID/actionID) and perhaps GET parameters in path format.
	 */
	public String parseUrl(HttpRequest request)
	{
		if (getUrlFormat().equals(UrlManager.PATH_FORMAT))
		{
			String rawPathInfo = request.getPathInfo();
			String pathInfo = removeUrlSuffix(rawPathInfo,_urlSuffix);
			/*
			foreach($this->_rules as $i=>$rule)
			{
				if(is_array($rule))
					$this->_rules[$i]=$rule=Yii::createComponent($rule);
				if(($r=$rule->parseUrl($this,$request,$pathInfo,$rawPathInfo))!==false)
					return isset($_GET[$this->routeVar]) ? $_GET[$this->routeVar] : $r;
			}
			*/
			if(_useStrictParsing)
				throw new HttpException(404, "Unable to resolve the request '"+pathInfo+"'.");
			else
				return StringUtils.strip(pathInfo, "\\/");
		}
		else if (request.getParam(_routeVar, null) != null)
		{
			return StringUtils.strip(request.getParam(_routeVar), "\\/");
		}
		else
			return "";
	}	
	
	/**
	 * Parses a path info into URL segments and saves them to {@link HttpRequest#getParam(String)}.
	 * @param pathInfo path info
	 */
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
	
	/**
	 * Removes the URL suffix from path info.
	 * @param string $pathInfo path info part in the URL
	 * @param string $urlSuffix the URL suffix to be removed
	 * @return string path info with URL suffix removed.
	 */
	public String removeUrlSuffix(String pathInfo, String urlSuffix)
	{
		if(urlSuffix!="" && pathInfo.length() > urlSuffix.length() && pathInfo.substring(pathInfo.length()-urlSuffix.length()).equals(urlSuffix))
			return pathInfo.substring(0, pathInfo.length()-urlSuffix.length());
		else
			return pathInfo;
	}
	
	/**
	 * Returns the base URL of the application.
	 * @return the base URL of the application (the part after host name and before query string).
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
	 * Sets the base URL of the application (the part after host name and before query string).
	 * This method is provided in case the {@link #getBaseUrl()} cannot be determined automatically.
	 * The ending slashes should be stripped off. 
	 * @param value the base URL of the application
	 */
	public void setBaseUrl(String value)
	{
		_baseUrl = value;
	}	
	
	/**
	 * Returns the URL format.
	 * @return the URL format. Defaults to 'path'. Valid values include 'path' and 'get'.
	 * Please refer to the guide for more details about the difference between these two formats.
	 */
	public String getUrlFormat()
	{
		return _urlFormat;
	}
	
	/**
	 * Sets the URL format.
	 * @param value the URL format. It must be either 'path' or 'get'.
	 */
	public void setUrlFormat(String value)
	{
		if(value.equals(UrlManager.PATH_FORMAT) || value.equals(UrlManager.GET_FORMAT))
			_urlFormat = value;
		else
			throw new com.yiij.base.Exception("CUrlManager.UrlFormat must be either 'path' or 'get'.");
	}
	
}
