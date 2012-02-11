package com.yiij.base;

import java.util.Hashtable;

@SuppressWarnings("serial")
public class ComponentConfig extends Hashtable<String, Object>
{
	public String className;
	
	public ComponentConfig(String className)
	{
		super();
		this.className = className;
	}
}
