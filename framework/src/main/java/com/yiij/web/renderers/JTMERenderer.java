package com.yiij.web.renderers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.floreysoft.jmte.Engine;
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
		return engine.transform(readTextFile(file), model);
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
	
}
