package com.yiij.base;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;

import com.yiij.base.interfaces.IComponent;
import com.yiij.base.interfaces.IContext;

public class Component implements IComponent
{
	private IContext _context;

	public static IComponent newInstance(IContext context, Object config, Object... arguments) 
		throws InstantiationException
	{
		return Component.newInstance(context, config, arguments, null);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static IComponent newInstance(IContext context, Object config, Object[] arguments, Class<?>[] types) 
			throws InstantiationException
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
			throw new Exception("Object configuration must be a string class name or a ComponentConfig object.");
		
		Class<Component> cclass;
		try {
			cclass = (Class<Component>) Class.forName(type);
		} catch (ClassNotFoundException e) {
			throw new InstantiationException(e.getMessage());
		}
		
		Object[] realArguments = new Object[arguments.length+1];
		realArguments[0] = context;
		for (int ctarg = 0; ctarg < arguments.length; ctarg++ )
		{
			realArguments[ctarg+1] = arguments[ctarg];
		}
		
		Class<?>[] realTypes = null;
		if (types != null)
		{
			realTypes = new Class[types.length+1];
			realTypes[0] = IContext.class;
			for (int ctarg = 0; ctarg < types.length; ctarg++ )
			{
				realTypes[ctarg+1] = types[ctarg];
			}
		}
		
		Component object;
		try
		{
			if (types == null)
				object = (Component)ConstructorUtils.invokeConstructor(cclass, realArguments);
			else
				object = (Component)ConstructorUtils.invokeExactConstructor(cclass, realArguments, realTypes);
		} catch (NoSuchMethodException e) {
			throw new InstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new InstantiationException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new InstantiationException(e.getMessage());
		}
		
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
	
	public void configure(ComponentConfig config) throws 
		InstantiationException
	{
		if (config == null)
			return;
	
		PropertyDescriptor pdesc;
		
		ArrayList<String> innerConfig = new ArrayList<String>();
		
		try
		{
			Iterator<String> i = config.keySet().iterator();
			while (i.hasNext())
			{
				String key = i.next();
				
				pdesc = PropertyUtils.getPropertyDescriptor(this, key);
				if (pdesc == null)
					throw new NoSuchMethodException("Property '"+key+"' not found in class '"+getClass().getCanonicalName()+"'");
				if (pdesc.getClass().isAssignableFrom(Component.class))
					innerConfig.add(key);
				else
					BeanUtils.setProperty(this, key, config.get(key));
			}
			
			for (String ic : innerConfig)
			{
				((Component)PropertyUtils.getProperty(this, ic)).configure((ComponentConfig)config.get(ic));
			}
		} catch (Exception e) {
			
		} catch (IllegalAccessException e) {
			throw new InstantiationException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new InstantiationException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new InstantiationException(e.getMessage());
		}
	}
	
	public void init()
	{
		
	}
}
