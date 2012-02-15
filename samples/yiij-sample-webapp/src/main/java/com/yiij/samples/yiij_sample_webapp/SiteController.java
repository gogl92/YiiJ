package com.yiij.samples.yiij_sample_webapp;

import java.io.IOException;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.Controller;
import com.yiij.web.interfaces.IWebModule;

public class SiteController extends Controller
{
	public SiteController(IContext context, String id, IWebModule module)
	{
		super(context, id, module);
	}
	
	@Override
	public void init()
	{
		super.init();
	}
	
	
	public void actionIndex() throws IOException
	{
		setPageTitle("Index page");
		
		render("index", "data from controller to view");
	}
}
