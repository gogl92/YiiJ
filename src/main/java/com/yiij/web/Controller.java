package com.yiij.web;

import com.yiij.base.HttpException;
import com.yiij.base.Module;
import com.yiij.base.interfaces.IContext;
import com.yiij.web.actions.Action;
import com.yiij.web.actions.InlineAction;

public class Controller extends BaseController
{
	private String _id;
	private Module _module;
	private Action _action = null;
	
	public Controller(IContext context, String id, Module module)
	{
		super(context);
		_id = id;
		_module = module;
	}

	public Controller(IContext context, String id)
	{
		this(context, id, null);
	}

	public String getId()
	{
		return _id;
	}
	
	public String getUniqueId()
	{
		return (_module != null) ?_module.getId()+"/"+_id : _id;
	}
	
	public Module getModule()
	{
		return _module;
	}
	
	public Action getAction()
	{
		return _action;
	}
	
	public void setAction(Action value)
	{
		_action = value;
	}
	
	public void run(String actionID) throws Exception
	{
		Action action = createAction(actionID);
		if (action != null)
		{
			//if(($parent=$this->getModule())===null)
				//$parent=Yii::app();
			//if($parent->beforeControllerAction($this,$action))
			//{
				runActionWithFilters(action,filters());
				//$parent->afterControllerAction($this,$action);
			//}
			
		}
		else
			missingAction(actionID);
	}
	
	public Object filters()
	{
		return null;
	}
	
	public void runActionWithFilters(Action action, Object filters) throws Exception
	{
		if (filters == null)
			runAction(action);
		else
		{
			// TODO
		}
		
	}
	
	public void runAction(Action action) throws Exception
	{
		action.run();
	}
	
	public Action createAction(String actionID) throws Exception
	{
		String actionMethodName = "action"+actionID.substring(0,1).toUpperCase() + actionID.substring(1);
		try
		{
			getClass().getMethod(actionMethodName, new Class[] {});
			
			return new InlineAction(this, actionMethodName);
		} catch (SecurityException e)
		{
		} catch (NoSuchMethodException e)
		{
		}
		// TODO
		throw new Exception("Action class {class} must implement the 'run' method.");
	}

	public void missingAction(String actionID) throws HttpException
	{
		throw new HttpException(404, "The system is unable to find the requested action '{action}'");
	}	
}
