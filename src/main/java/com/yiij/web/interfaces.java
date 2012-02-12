package com.yiij.web;

import java.io.IOException;

import com.yiij.base.Module;
import com.yiij.base.interfaces.IApplicationComponent;

public class interfaces
{
	public interface IWebModule
	{
		public String getId();
		public String getDefaultController();
		//public String getControllerPath();
		public String getViewPath();
		public String getPackageName();
		public String getViewPackageName();
		public String getLayout();
		public Module getParentModule();
		public String getLayoutPath();
	}
	
	public interface IWebComponent
	{
		public WebApplication webApp();
	}

	public interface IViewRenderer
	{
		/**
		 * @return view file extension (ex.: ".jhtml"), or null if not file-based
		 */
		public String getFileExtension();
		public String renderFile(BaseController controller, String file, Object data, boolean doReturn) throws IOException;
	}	
	
	public interface IPluginViewRenderer extends IViewRenderer, IApplicationComponent
	{
	}	
}
