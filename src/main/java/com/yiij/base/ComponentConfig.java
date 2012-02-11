package com.yiij.base;

import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("serial")
public class ComponentConfig extends Hashtable<String, Object>
{
	public String className = null;

	public ComponentConfig()
	{
		super();
	}
	
	public ComponentConfig(String className)
	{
		super();
		this.className = className;
	}
	
	public void merge(ComponentConfig other)
	{
		for (String i : other.keySet())
		{
			if (containsKey(i))
			{
				if (get(i) instanceof ComponentConfig && other.get(i) instanceof ComponentConfig)
					((ComponentConfig)get(i)).merge((ComponentConfig)other.get(i));
				else
					put(i, other.get(i));
			}
			else
				put(i, other.get(i));
		}
	}
	
	public void parseConfigXml(InputStream is) throws java.lang.Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc = db.parse(is);
		
		if (!doc.getDocumentElement().getTagName().equals("yiij"))
			throw new Exception("Invalid configuration file");

		clear();
		className = "@Root";
		
		for(int i = 0; i<doc.getDocumentElement().getChildNodes().getLength(); i++)
		{
			Node n = doc.getDocumentElement().getChildNodes().item(i);
			
			if (n instanceof Element)
			{
				if (((Element) n).getTagName().equals("application"))
				{
					put("application", parseModule((Element)n));
				}	
			}
		}
	}
	
	private ComponentConfig parseModule(Element element)
	{
		ComponentConfig result = new ComponentConfig();
		
		for(int i = 0; i<element.getChildNodes().getLength(); i++)
		{
			Node n = element.getChildNodes().item(i);
			
			if (n instanceof Element)
			{
				Element ne = (Element)n;
				
				if (ne.getTagName().equals("class"))
					result.className = ne.getTextContent();
				else if (ne.getTagName().equals("components"))
				{
					result.put("components", parseComponentList(ne, new ComponentConfig()));
				}
				else if (ne.getTagName().equals("modules"))
				{
					result.put("modules", parseModuleList(ne, new ComponentConfig()));
				}
				else if (hasChildElements(ne))
				{
					result.put(ne.getTagName(), parseComponent(ne));
				}
				else
				{
					result.put(ne.getTagName(), ne.getTextContent());
				}
			}
		}	
		
		return result;
	}
	
	private ComponentConfig parseComponentList(Element element, ComponentConfig config)
	{
		for(int i = 0; i<element.getChildNodes().getLength(); i++)
		{
			Node n = element.getChildNodes().item(i);
			
			if (n instanceof Element)
			{
				Element ne = (Element)n;
				
				config.put(ne.getTagName(), parseComponent(ne));
			}
		}	
		
		return config;
	}
	
	
	private ComponentConfig parseModuleList(Element element, ComponentConfig config)
	{
		for(int i = 0; i<element.getChildNodes().getLength(); i++)
		{
			Node n = element.getChildNodes().item(i);
			
			if (n instanceof Element)
			{
				Element ne = (Element)n;
				
				config.put(ne.getTagName(), parseModule(ne));
			}
		}	
		
		return config;
	}
	
	private ComponentConfig parseComponent(Element element)
	{
		ComponentConfig result = new ComponentConfig();
		
		for(int i = 0; i<element.getChildNodes().getLength(); i++)
		{
			Node n = element.getChildNodes().item(i);
			
			if (n instanceof Element)
			{
				Element ne = (Element)n;
				
				if (ne.getTagName().equals("class"))
					result.className = ne.getTextContent();
				else if (hasChildElements(ne))
				{
					result.put(ne.getTagName(), parseComponent(ne));
				}
				else
				{
					result.put(ne.getTagName(), ne.getTextContent());
				}
			}
		}	
		
		return result;
	}
		
	private boolean hasChildElements(Element e)
	{
		for (int i = 0; i < e.getChildNodes().getLength(); i++)
		{
			if (e.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
				return true;
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return className+"+"+super.toString();
	}
}
