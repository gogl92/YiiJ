package com.yiij.base;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiij.Root;
import com.yiij.base.interfaces.IContext;
import com.yiij.utils.Parameters;
import com.yiij.web.WebApplication;
import com.yiij.web.interfaces.IViewRenderer;
import com.yiij.web.renderers.JTMERenderer;

public class ErrorHandler extends ApplicationComponent
{
	private String _errorAction;
	//private java.lang.Exception _error;
	private Parameters _error;
	private boolean _discardOutput=true;
	private String _adminInfo = "the webmaster";
	
	private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);	
	
	public ErrorHandler(IContext context)
	{
		super(context);
	}

	/**
	 * Handles the exception/error event.
	 * This method is invoked by the application whenever it captures
	 * an exception.
	 * @param event the event containing the exception/error information
	 */
	public void handle(ExceptionEvent event) 
			throws ServletException
	{
		// set event as handled to prevent it from being handled by other event handlers
		event.setHandled();

		if(_discardOutput)
		{
			if (context().getApplication() instanceof WebApplication)
			{
				try
				{
					((WebApplication)context().getApplication()).getServletResponse().reset();
				} catch (IllegalStateException e) {
					logger.warn("Could not discard output: "+e.getMessage());
				}
			}
		}

		handleException(event.exception);
	}
	
	
	/**
	 * @return the route (eg 'site/error') to the controller action that will be used to display external errors.
	 * Inside the action, it can retrieve the error information by webApp().getErrorHandler().getError().
	 * This property defaults to null, meaning ErrorHandler will handle the error display.
	 */
	public String getErrorAction()
	{
		return _errorAction;
	}
	
	/**
	 * @see #getErrorAction()
	 */
	public void setErrorAction(String value)
	{
		_errorAction = value;
	}
	
	/**
	 * @return the application administrator information (could be a name or email link). It is displayed in error pages to end users. Defaults to 'the webmaster'.
	 */
	public String getAdminInfo()
	{
		return _adminInfo;
	}

	/**
	 * @see #getAdminInfo()
	 */
	public void setAdminInfo(String value)
	{
		_adminInfo = value;
	}
	
	/**
	 * Returns the details about the error that is currently being handled.
	 * The error is returned in terms of an array, with the following information:
	 * <ul>
	 * <li>code - the HTTP status code (e.g. 403, 500)</li>
	 * <li>type - the error type (e.g. 'HttpException', 'PHP Error')</li>
	 * <li>message - the error message</li>
	 * <li>file - the name of the PHP script file where the error occurs</li>
	 * <li>line - the line number of the code where the error occurs</li>
	 * <li>trace - the call stack of the error</li>
	 * <li>source - the context source code where the error occurs</li>
	 * </ul>
	 * @return the error details. Null if there is no error.
	 */
	public Map<String, Object> getError()
	{
		return _error;
	}
	
	/**
	 * Handles the exception.
	 * @param exception the exception captured
	 */
	protected void handleException(java.lang.Exception exception) 
			throws ServletException
	{
		try
		{
			Application app = context().getApplication();
			if(app instanceof WebApplication)
			{
				WebApplication webApp = (WebApplication)app;
				
				_error = new Parameters();
				_error.put("code", exception instanceof HttpException ? ((HttpException)exception).statusCode : 500);
				_error.put("type", exception);
				_error.put("message", exception.getMessage());
				_error.put("file", exception.getStackTrace()[0].getFileName());
				_error.put("line", exception.getStackTrace()[0].getLineNumber());
				_error.put("trace", ExceptionUtils.getStackTrace(exception));
				
				if (!webApp.getResponse().isHeadersSent())
					webApp.getResponse().setStatus(exception instanceof HttpException?((HttpException)exception).statusCode:500, exception.getClass().getCanonicalName());
	
				if(exception instanceof HttpException || !app.isDebugMode())
					render("error",_error);
				else
				{
					if(isAjaxRequest())
						app.displayException(exception);
					else
						render("exception",_error);
				}
			}
			else
				app.displayException(exception);
		}
		catch (java.lang.Exception e)
		{
			logger.error("Error handling exception: "+e.getMessage());
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	/**
	 * whether the current request is an AJAX (XMLHttpRequest) request.
	 * @return whether the current request is an AJAX request.
	 */
	protected boolean isAjaxRequest()
	{
		if ((context().getApplication() instanceof WebApplication))
		{
			String xreq = ((WebApplication)context().getApplication()).getServletRequest().getHeader("HTTP_X_REQUESTED_WITH");
			return xreq != null &&  xreq.equals("XMLHttpRequest");
		}
		return false;
	}
	
	/**
	 * Renders the view.
	 * @param view the view name (file name without extension).
	 * See {@link #getViewFile()} for how a view file is located given its name.
	 * @param data data to be passed to the view
	 * @throws java.lang.Exception 
	 */
	protected void render(String view, Object data) throws java.lang.Exception
	{
		if (context().getApplication() instanceof WebApplication)
		{
			if(view.equals("error") && _errorAction!=null)
				((WebApplication)context().getApplication()).runController(_errorAction);
			else
			{
				// additional information to be passed to view
				((Parameters)data).put("version", getVersionInfo());
				((Parameters)data).put("time", new Date());
				((Parameters)data).put("admin", getAdminInfo());
				
				IViewRenderer renderer = new JTMERenderer(context());
				renderer.renderFile(null, getViewFile(view, Integer.parseInt(((Parameters)data).get("code").toString())), data, false);
			}
		}
		else
			context().getApplication().displayException((java.lang.Exception)_error.get("type"));
	}
	
	/**
	 * Determines which view file should be used.
	 * @param string $view view name (either 'exception' or 'error')
	 * @param integer $code HTTP status code
	 * @return string view file path
	 */
	protected String getViewFile(String view, int code)
	{
		String[] viewPaths = new String[] {
			//Yii::app()->getTheme()===null ? null :  Yii::app()->getTheme()->getSystemViewPath(),
			//Yii::app() instanceof CWebApplication ? Yii::app()->getSystemViewPath() : null,
			"/com/yiij/views",
		};

		for(int i = 0; i < viewPaths.length; i++)
		{
			String viewPath=viewPaths[i];
			if(viewPath!=null)
			{
				 String viewFile=getViewFileInternal(viewPath,view,code,i==0?"en_us":null);
				 if(Object.class.getResource(viewFile)!=null)
				 	 return viewFile;
			}
		}
		return null;
	}
	
	protected String getViewFileInternal(String viewPath, String view, int code)
	{
		return getViewFileInternal(viewPath, view, code, null);
	}
	
	/**
	 * Looks for the view under the specified directory.
	 * @param viewPath the directory containing the views
	 * @param view view name (either 'exception' or 'error')
	 * @param code HTTP status code
	 * @param srcLanguage the language that the view file is in
	 * @return view file path
	 */
	protected String getViewFileInternal(String viewPath, String view, int code, String srcLanguage)
	{
		Application app = context().getApplication();
		String viewFile;
		if(view.equals("error"))
		{
			viewFile=app.findLocalizedFile(viewPath+"/"+"error"+code+".mte",srcLanguage);
			if(Object.class.getResource(viewFile)==null)
				viewFile=app.findLocalizedFile(viewPath+"/"+"error.mte",srcLanguage);
		}
		else
			viewFile=viewPath+"/"+"exception.mte";
		return viewFile;
	}
	
	/**
	 * Returns server version information.
	 * If the application is in production mode, empty string is returned.
	 * @return server version information. Empty if in production mode.
	 */
	protected String getVersionInfo()
	{
		String version;
		if(context().getApplication().isDebugMode())
		{
			version="<a href=\"http://www.yiiframework.com/\">Yii Framework</a>/"+Root.getVersion();
			//if(isset($_SERVER['SERVER_SOFTWARE']))
				//$version=$_SERVER['SERVER_SOFTWARE'].' '.$version;
		}
		else
			version="";
		return version;
	}
	
	
	@SuppressWarnings("serial")
	public static class ExceptionEvent extends Event
	{
		protected transient java.lang.Exception exception;
		
		public ExceptionEvent(Object source, java.lang.Exception exception)
		{
			super(source);
			this.exception = exception;
		}
		
		public java.lang.Exception getException()
		{
			return exception;
		}
	}
}
