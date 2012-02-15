package com.yiij.web.renderers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
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
		model.put("this", controller);
		model.put("data", data);
		
		Engine engine = new Engine();
		engine.registerNamedRenderer(new HtmlEncodeRenderer());
		if (doReturn)
			return engine.transform(readTextFile(file), model);
		webApp().getResponse().getWriter().print(engine.transform(readTextFile(file), model));
		return null;
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
}
