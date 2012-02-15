package com.yiij.samples.yiij_sample_app.components;

import java.io.IOException;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.BaseController;
import com.yiij.web.widgets.Widget;

public class Login extends Widget
{
	public Login(IContext context, BaseController owner)
	{
		super(context, owner);
	}

	@Override
	public void run() throws IOException
	{
		webApp().getResponse().getWriter().println("** I AM A WIDGET **");
	}
}
