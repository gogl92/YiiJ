package com.yiij.base;

import java.util.Hashtable;
import java.util.Map;

import com.yiij.base.interfaces.IApplicationComponent;
import com.yiij.base.interfaces.IContext;

public class Module extends Component
{
	private String _id;
	private Module _parentModule;
	private Map<String, Module> _modules = new Hashtable<String, Module>(); 
	private Map<String, ComponentConfig> _moduleConfig = new Hashtable<String, ComponentConfig>(); 
	private Map<String, IApplicationComponent> _components = new Hashtable<String, IApplicationComponent>(); 
	private Map<String, ComponentConfig> _componentConfig = new Hashtable<String, ComponentConfig>(); 
	
	public Module(IContext context, String id, Module parent)
	{
		super(context);
		_id = id;
		_parentModule =  parent;
	}
	
	public String getId()
	{
		return _id;
	}
	
	public Module getParentModule()
	{
		return _parentModule;
	}
	
	public boolean hasModule(String id)
	{
		return _modules.containsKey(id) || _moduleConfig.containsKey(id);
	}
	
	public Module getModule(String id) throws java.lang.Exception
	{
		if (_modules.containsKey(id))
			return _modules.get(id);
		else if (_moduleConfig.containsKey(id))
		{
			ComponentConfig config = _moduleConfig.get(id);
			if (!config.contains("enabled") ||  Integer.parseInt(config.get("enabled").toString()) != 0)
			{
				Module module = (Module)Component.newInstance(context(), config, getId()+'/'+id, _parentModule!=null?this:null);
				_modules.put(id, module);
				return module;
			}
		}
		return null;
	}
	
	public void setModule(String id, Module module)
	{
		if (module == null)
			_modules.remove(id);
		else
		{
			_modules.put(id, module);
		}
	}
	
	public Map<String, ComponentConfig> getModules()
	{
		return _moduleConfig;
	}
	
	public boolean hasComponent(String id)
	{
		return _components.containsKey(id) || _componentConfig.containsKey(id);
	}
	
	public IApplicationComponent getComponent(String id) throws java.lang.Exception
	{
		if (_components.containsKey(id))
			return _components.get(id);
		else if (_componentConfig.containsKey(id))
		{
			ComponentConfig config = _componentConfig.get(id);
			if (!config.contains("enabled") ||  Integer.parseInt(config.get("enabled").toString()) != 0)
			{
				IApplicationComponent component = (IApplicationComponent)Component.newInstance(context(), config);
				_components.put(id, component);
				return component;
			}
		}
		return null;
	}

	public void setComponent(String id, IApplicationComponent component)
	{
		if (component == null)
			_components.remove(id);
		else
		{
			_components.put(id, component);
			if (!component.getIsInitialized())
				component.init();
		}
	}
	
	public Map<String, ComponentConfig> getComponents()
	{
		return _componentConfig;
	}
	
	public void setComponents(ComponentConfig config)
	{
		for (String key : config.keySet())
		{
			if (config.get(key) instanceof IApplicationComponent)
				setComponent(key, (IApplicationComponent)config.get(key));
			else if (_componentConfig.containsKey(key))
				_componentConfig.get(key).merge((ComponentConfig)config.get(key));
			else
				_componentConfig.put(key, (ComponentConfig)config.get(key));
		}
	}
}