package com.yiij.base;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.yiij.base.interfaces.IComponent;
import com.yiij.base.interfaces.IContext;

public class Component implements IComponent
{
	private IContext _context;
	
	@SuppressWarnings({ "unchecked" })
	public static IComponent newInstance(IContext context, Object config, Object... arguments) throws 
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
		
		Object[] realArguments = new Object[arguments.length+1];
		realArguments[0] = context!=null ? context : new AbstractContext(null);
		for (int ctarg = 0; ctarg < arguments.length; ctarg++ )
		{
			realArguments[ctarg+1] = arguments[ctarg];
		}
		
		Component object = (Component)ConstructorUtils.invokeConstructor(cclass, realArguments);
		
		if (context == null && object instanceof Application)
		{
			object._context = new AbstractContext((Application)object);
		}
		
		object.configure(cconfig);
		object.init();
		
		return object;
	}
	
	public Component(IContext context)
	{
		super();
		_context = context;
	}
	
	public IContext context()
	{
		return _context;
	}
	
	public void configure(ComponentConfig config) throws java.lang.Exception
	{
		if (config == null)
			return;
		
		PropertyDescriptor pdesc;
		
		ArrayList<String> innerConfig = new ArrayList<String>();
		
		Iterator<String> i = config.keySet().iterator();
		while (i.hasNext())
		{
			String key = i.next();
			
			pdesc = PropertyUtils.getPropertyDescriptor(this, key);
			if (pdesc.getClass().isAssignableFrom(Component.class))
				innerConfig.add(key);
			else
				PropertyUtils.setProperty(this, key, config.get(key));
		}
		
		for (String ic : innerConfig)
		{
			((Component)PropertyUtils.getProperty(this, ic)).configure((ComponentConfig)config.get(ic));
		}
	}
	
	public void init()
	{
		
	}
}
