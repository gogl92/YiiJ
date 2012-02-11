package com.yiij.base;

@SuppressWarnings("serial")
public class HttpException extends Exception
{
	int statusCode;
	
	public HttpException(int status, String message)
	{
		super(message);
		statusCode = status;
	}
}
