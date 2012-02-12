package com.yiij.web;

public class interfaces
{
	public interface IWebModule
	{
		public String getId();
		public String getDefaultController();
		//public String getControllerPath();
		public String getViewPath();
	}
	
	public interface IWebComponent
	{
		public WebApplication webApp();
	}
}
