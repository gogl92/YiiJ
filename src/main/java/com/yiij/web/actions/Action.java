package com.yiij.web.actions;

import com.yiij.base.interfaces.IAction;
import com.yiij.web.Controller;

public abstract class Action implements IAction
{
	private Controller _controller;
	private String _id;
	
	public Action(Controller controller, String id)
	{
		super();
		_controller = controller;
		_id = id;
	}

	@Override
	public String getId()
	{
		return _id;
	}

	@Override
	public Controller getController()
	{
		return _controller;
	}
	
	public abstract void run() throws Exception;
}
