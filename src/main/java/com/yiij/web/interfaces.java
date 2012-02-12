package com.yiij.web;

import java.io.IOException;

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
	
	public interface IViewRenderer
	{
		/**
		 * @return view file extension (ex.: ".jhtml")
		 */
		public String getFileExtension();
		public String renderFile(BaseController controller, String file, Object data, boolean doReturn) throws IOException;
	}	
}
