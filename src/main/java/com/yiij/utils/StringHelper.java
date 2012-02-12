package com.yiij.utils;

public class StringHelper
{
	public static String upperCaseFirst(String value)
	{
		return value.substring(0,1).toUpperCase() + value.substring(1);
	}
}
