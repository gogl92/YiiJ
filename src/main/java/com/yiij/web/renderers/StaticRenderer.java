package com.yiij.web.renderers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.BaseController;
import com.yiij.web.WebApplicationComponent;
import com.yiij.web.interfaces.IPluginViewRenderer;

public class StaticRenderer extends WebApplicationComponent implements IPluginViewRenderer
{
	public StaticRenderer(IContext context)
	{
		super(context);
	}

	@Override
	public String getFileExtension()
	{
		return ".html";
	}

	@Override
	public String renderFile(BaseController controller, String file,
			Object data, boolean doReturn) throws IOException
	{
		StringBuilder ret = new StringBuilder();

		InputStream in = Object.class.getResourceAsStream(file);

		Scanner scanner = new Scanner(in, "UTF-8");
		try
		{
			while (scanner.hasNextLine())
			{
				if (doReturn)
					ret.append(scanner.nextLine());
				else
					webApp().getResponse().getWriter().println(scanner.nextLine());
			}
		} finally
		{
			scanner.close();
		}
		
		return ret.toString();
	}

}
