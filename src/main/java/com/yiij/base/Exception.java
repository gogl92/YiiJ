package com.yiij.base;

@SuppressWarnings("serial")
public class Exception extends java.lang.RuntimeException
{
	public Exception()
	{
		super();
	}
	
	public Exception(String message)
	{
		super(message);
	}
	
	public Exception(Throwable t)
	{
		super(t);
	}
}
