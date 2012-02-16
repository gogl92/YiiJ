package com.yiij.web.renderers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringEscapeUtils;

import com.floreysoft.jmte.DefaultModelAdaptor;
import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.ErrorHandler;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.ProcessListener;
import com.floreysoft.jmte.Processor;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.TemplateContext;
import com.floreysoft.jmte.token.StringToken;
import com.floreysoft.jmte.token.Token;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.BaseController;
import com.yiij.web.WebApplicationComponent;
import com.yiij.web.interfaces.IApplicationViewRenderer;

public class JTMERenderer extends WebApplicationComponent implements
		IApplicationViewRenderer
{
	public JTMERenderer(IContext context)
	{
		super(context);
	}

	@Override
	public String getFileExtension()
	{
		return ".mte";
	}

	@Override
	public String renderFile(BaseController controller, String file,
			Object data, boolean doReturn) throws IOException
	{
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("this", new BaseControllerWrapper(controller));
		model.put("data", data);
		
		Engine engine = new Engine();
		engine.registerNamedRenderer(new HtmlEncodeRenderer());
		engine.setModelAdaptor(new YiiJModelAdaptor());
		if (doReturn)
			return engine.transform(readTextFile(file), model);
		webApp().getResponse().getWriter().print(engine.transform(readTextFile(file), model));
		return null;
	}

	private static class EngineProcessListener implements ProcessListener
	{

		@Override
		public void log(TemplateContext context, Token token, Action action)
		{
		}
	}
	
	private String readTextFile(String file) throws IOException {
	    StringBuilder result = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(Object.class.getResourceAsStream(file)));
	    try {

	        char[] buf = new char[1024];

	        int r = 0;

	        while ((r = reader.read(buf)) != -1) {
	            result.append(buf, 0, r);
	        }
	    }
	    finally {
	        reader.close();
	    }

	    return result.toString();     
	}
	
	private static class HtmlEncodeRenderer implements NamedRenderer
	{
		@Override
		public String render(Object o, String format) {
			return StringEscapeUtils.escapeHtml4(o.toString());
		}

		@Override
		public String getName() {
			return "htmlencode";
		}

		@Override
		public RenderFormatInfo getFormatInfo() {
			return null;
		}

		@Override
		public Class<?>[] getSupportedClasses() {
			return new Class<?>[] { Object.class };
		}
		
	}
	
	private static interface MethodCallable
	{
		Object call(TemplateContext context, String method, Token token);
	}
	
	public static class MethodCallableCall implements Processor<Object>
	{
		private MethodCallable _callable;
		private String _method;
		private Token _token;
		
		public MethodCallableCall(MethodCallable callable, String method, Token token)
		{
			super();
			_callable = callable;
			_method = method;
			_token = token;
			
		}
		
		@Override
		public Object eval(TemplateContext context)
		{
			return _callable.call(context, _method, _token);
		}
	}
	
	private static class YiiJModelAdaptor extends DefaultModelAdaptor
	{
		protected Object nextStep(Object o, String attributeName,
				ErrorHandler errorHandler, Token token) {
			if (o instanceof MethodCallable)
			{
				return new MethodCallableCall((MethodCallable)o, attributeName, token);
			}
			return super.nextStep(o, attributeName, errorHandler, token);
		}
	}
	
	public static class NullOutput
	{
		public String toString()
		{
			return "";
		}
	}
	
	
	public static class BaseControllerWrapper implements MethodCallable
	{
		private BaseController _controller;
		
		public BaseControllerWrapper(BaseController controller)
		{
			super();
			_controller = controller;
		}
		
		@Override
		public Object call(TemplateContext context, String method, Token token)
		{
			try
			{
				// widget must always capture output
				if (method.equals("beginWidget"))
				{
					_controller.webApp().getResponse().ob_start();
					_controller.beginWidget(((StringToken)token).getDefaultValue());
					return new NullOutput();
				}
				else if (method.equals("endWidget"))
				{
					_controller.endWidget(((StringToken)token).getDefaultValue());
					_controller.webApp().getResponse().ob_end_clean();
					return new NullOutput();
				}
				else if (method.equals("widget"))
				{
					//String[] params = ((StringToken)token).getDefaultValue().split(",");
					
					//boolean captureOutput = params.length>1 && params[1].equals("true");
					
					Object result = _controller.widget(((StringToken)token).getDefaultValue(), true);
					return result==null||result.equals("") ? new NullOutput() : result;
				}
			} catch (InstantiationException e)
			{
				throw new RuntimeException(e);
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}
			return null;
		}
	}
	
	
}
