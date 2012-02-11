package com.yiij.base;

import java.io.IOException;

import javax.servlet.ServletException;

public abstract class Application extends Module
{
	public Application()
	{
		super("", null);
	}

	/*
	@Override
	protected void bootstrap()
	{
		preinit();

		registerCoreComponents();
		
		init();
	}
	*/
	
	/**
	 * Processes the request.
	 * This is the place where the actual request processing work is done.
	 * Derived classes should override this method.
	 */
	abstract public void processRequest() throws java.lang.Exception;

	public void run() throws ServletException, IOException
	{
		try
		{
			processRequest();
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (java.lang.Exception e) {
			throw new ServletException(e);
		}
	}
	
	/*
	protected void registerCoreComponents()
	{
	}
	*/
}