package com.yiij.web.actions;

import java.lang.reflect.Method;

import com.yiij.web.Controller;

public class InlineAction extends Action
{
	public InlineAction(Controller controller, String id)
	{
		super(controller, id);
	}

	@Override
	public void run() throws Exception
	{
		Method actionMethod = getController().getClass().getMethod(getId(), new Class[] {});
		actionMethod.invoke(getController(), new Object[]{});
	}

}
