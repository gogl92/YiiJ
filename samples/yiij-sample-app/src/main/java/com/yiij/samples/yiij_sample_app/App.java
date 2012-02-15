package com.yiij.samples.yiij_sample_app;

import java.io.IOException;

import javax.servlet.ServletException;

import com.yiij.mock.TestServlet;

public class App 
{
    public static void main( String[] args ) throws ServletException, IOException
    {
    	TestServlet t = new TestServlet();
    	t.init();
		t.loadYiiJConfig(Object.class.getResourceAsStream("/yiij.xml"));

		TestServlet.Response response = t.simulateGet("http://localhost");
		//TestServlet.Response response = t.simulateGet("http://localhost/admin");
		
		
		System.out.println("CONTENT:\n"+response.output);
    }
}
