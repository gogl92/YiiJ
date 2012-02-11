package com.yiij.base;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

import com.yiij.base.interfaces.IComponent;

public class Component implements IComponent
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static IComponent newInstance(Object config, Object... arguments) throws 
		java.lang.Exception
	{
		String type = null;
		ComponentConfig cconfig = null;
		
		if (config instanceof String)
		{
			type = (String)config;
		}
		else if (config instanceof ComponentConfig)
		{
			cconfig = (ComponentConfig) ((ComponentConfig)config).clone();
			type = cconfig.className;
		}
		else
			throw new Exception("Object configuration must be an array containing a 'class' element.");
		
		Class<Component> cclass = (Class<Component>) Class.forName(type);
		
		Class[] argumentsTypes = new Class[arguments.length];
		for (int ctarg = 0; ctarg < arguments.length; ctarg++ )
		{
			argumentsTypes[ctarg] = arguments[ctarg].getClass();
		}
		
		Constructor<Component> c = cclass.getConstructor(argumentsTypes);
		Component object = c.newInstance(arguments);
		
		object.setConfig(cconfig);
		object.init();
		
		return object;
	}
	
	public void setConfig(ComponentConfig config) throws java.lang.Exception
	{
		PropertyDescriptor pdesc;
		
		ArrayList<String> innerConfig = new ArrayList<String>();
		
		Iterator<String> i = config.keySet().iterator();
		while (i.hasNext())
		{
			pdesc = PropertyUtils.getPropertyDescriptor(this, i.next());
			if (pdesc.getClass().isAssignableFrom(Component.class))
				innerConfig.add(i.next());
			else
				PropertyUtils.setProperty(this, i.next(), config.get(i.next()));
		}
		
		for (String ic : innerConfig)
		{
			((Component)PropertyUtils.getProperty(this, ic)).setConfig((ComponentConfig)config.get(ic));
		}
	}
	
	public void init()
	{
		
	}
}
