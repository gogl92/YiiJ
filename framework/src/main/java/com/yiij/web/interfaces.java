package com.yiij.web;

import java.io.IOException;

import com.yiij.base.Module;
import com.yiij.base.interfaces.IApplicationComponent;
import com.yiij.web.widgets.Widget;

public class interfaces
{
	public interface IWebModule
	{
		public String getId();
		public String getDefaultController();
		//public String getControllerPath();
		public String getViewPath();
		public String getPackageName();
		//public String getViewPackageName();
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
	
	public interface IApplicationViewRenderer extends IViewRenderer, IApplicationComponent
	{
	}	
	
	/**
	 * IWidgetFactory is the interface that must be implemented by a widget factory class.
	 *
	 * When calling {@link BaseController#createWidget()}, if a widget factory is available,
	 * it will be used for creating the requested widget.
	 *
	 * @author Qiang Xue <qiang.xue@gmail.com>
	 * @version $Id: interfaces.php 1678 2010-01-07 21:02:00Z qiang.xue $
	 * @package system.web
	 * @since 1.1
	 */
	interface IWidgetFactory
	{
		/**
		 * Creates a new widget based on the given class name and initial properties.
		 * @param owner the owner of the new widget
		 * @param className the class name of the widget. This can also be a path alias (e.g. system.web.widgets.COutputCache)
		 * @param properties the initial property values (name=>value) of the widget.
		 * @return the newly created widget whose properties have been initialized with the given values.
		 */
		public Widget createWidget(BaseController owner, Object properties) 
				throws InstantiationException;
	}
	
}
