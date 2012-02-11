package com.yiij.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiij.web.Controller;

public class interfaces
{
	public interface IContext
	{
		public Application getApplication();
	}
	
	public interface IComponent
	{
		void setConfig(ComponentConfig config) throws java.lang.Exception;
		void init();
	}
	
	public interface IApplicationComponent
	{
		/**
		 * Initializes the application component.
		 * This method is invoked after the application completes configuration.
		 */
		public void init();
		/**
		 * @return boolean whether the {@link init()} method has been invoked.
		 */
		public boolean getIsInitialized();
	}
	
	public interface IAction
	{
		/**
		 * @return string id of the action
		 */
		public String getId();
		/**
		 * @return CController the controller instance
		 */
		public Controller getController();
	}
	
	public interface IWebApplication
	{
		HttpServletRequest getServletRequest();
		HttpServletResponse getServletRresponse();
	}
}
