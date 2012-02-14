package com.yiij.base;

public class EndApplicationException extends Exception
{
	public EndApplicationException()
	{
		super();
	}
	
	public EndApplicationException(String message)
	{
		super(message);
	}
	
	public EndApplicationException(Throwable t)
	{
		super(t);
	}
}
