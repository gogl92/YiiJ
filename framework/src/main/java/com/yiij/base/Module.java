package com.yiij.base;

import java.util.Hashtable;
import java.util.Map;

import com.yiij.base.interfaces.IApplicationComponent;
import com.yiij.base.interfaces.IContext;

public class Module extends Component
{
	private String _basePath;
	private String _id;
	private String _packageName = null;
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
	
	public void setId(String value)
	{
		_id = value;
	}
	
	/**
	 * Returns the root directory of the module.
	 * @return string the root directory of the module. Defaults to the directory containing the module class.
	 */
	public String getBasePath()
	{
		if(_basePath==null)
		{
			if (_parentModule == null)
			{
				if (context().getApplication() == this)
					_basePath = "";
				else
					_basePath = context().getApplication().getBasePath()+"/"+_id;
			}
			else
				_basePath = _parentModule.getBasePath()+"/"+_id;
		}
		return _basePath;
	}
	
	/**
	 * Sets the root directory of the application.
	 * This method can only be invoked at the begin of the constructor.
	 * @param string $path the root directory of the application.
	 * @throws CException if the directory does not exist.
	 */
	public void setBasePath(String path)
	{
		_basePath = path;
		/*
		if(($this->_basePath=realpath($path))===false || !is_dir($this->_basePath))
			throw new CException(Yii::t('yii','Application base path "{path}" is not a valid directory.',
				array('{path}'=>$path)));
        $this->_autoBasePathUrl = null;
        */
	}
	
	
	public String getPackageName()
	{
		if (_packageName == null)
			_packageName = getClass().getPackage().getName();
		return _packageName;
	}
	
	public void setPackageName(String value)
	{
		_packageName = value;
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
				Module module = (Module)Component.newInstance(context(), config,
						(context().getApplication() == this) ?
								(new Object[] {id, null}) :
								(new Object[] {getId()+'/'+id, this}),
						new Class[] {String.class, Module.class}
				);
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
	
	public Map<String, ComponentConfig> getModulesConfig()
	{
		return _moduleConfig;
	}
	
	public void setModules(ComponentConfig config)
	{
		for (String key : config.keySet())
		{
			if (_moduleConfig.containsKey(key))
				_moduleConfig.get(key).merge((ComponentConfig)config.get(key));
			else
				_moduleConfig.put(key, (ComponentConfig)config.get(key));
		}
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
	
	public Map<String, ComponentConfig> getComponentsConfig()
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