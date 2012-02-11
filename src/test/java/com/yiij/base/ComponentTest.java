package com.yiij.base;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.yiij.base.interfaces.IComponent;

public class ComponentTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ComponentTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ComponentTest.class );
    }

    public void testComponentInstantiation() throws java.lang.Exception
    {
		IComponent c;

		c = Component.newInstance("com.yiij.base.ComponentTest$TC");
		assertNotNull(c);
		assertTrue(c instanceof TC);

		c = null;
		
		c = Component.newInstance(new ComponentConfig("com.yiij.base.ComponentTest$TC") {{
			put("name", "john");
		}});
		assertNotNull(c);
		assertTrue(c instanceof TC);
		assertEquals("john", ((TC)c).getName());
    }
    
    public static class TC extends Component
    {
    	private String _name;
    	
    	public String getName()
    	{
    		return _name;
    	}
    	
    	public void setName(String value)
    	{
    		_name = value;
    	}
    }    
}
