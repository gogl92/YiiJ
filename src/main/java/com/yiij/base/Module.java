package com.yiij.base;

import java.util.Hashtable;
import java.util.Map;

import com.yiij.base.interfaces.IApplicationComponent;

public class Module
{
	private String _id;
	private Module _parentModule;
	private Map<String, Module> _modules = new Hashtable<String, Module>(); 
	private Map<String, IApplicationComponent> _components = new Hashtable<String, IApplicationComponent>(); 
	
	public Module(String id, Module parent)
	{
		_id = id;
		_parentModule =  parent;
		
		//bootstrap();
	}
	
	public String getId()
	{
		return _id;
	}
	
	public Module getParentModule()
	{
		return _parentModule;
	}
	
	public Module getModule(String id)
	{
		return null;
	}
	
	public IApplicationComponent getComponent(String id)
	{
		return _components.get(id);
	}

	public void setComponent(String id, IApplicationComponent component)
	{
		_components.put(id, component);
	}
	
	/*
	protected void bootstrap()
	{
		preinit();

		init();
	}
	
	protected void preinit()
	{
		
	}
	
	protected void init()
	{
		
	}
	*/
}