package com.yiij.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

import javax.servlet.ServletOutputStream;

import com.yiij.base.interfaces.IContext;

public class HttpResponse extends WebApplicationComponent
{
	private OutputBufferManager _outputBuffers = new OutputBufferManager();
	
	public HttpResponse(IContext context)
	{
		super(context);
	}
	
	public boolean isHeadersSent()
	{
		return webApp().getServletResponse().isCommitted();
	}
	
	public void flush() throws IOException
	{
		webApp().getServletResponse().flushBuffer();
	}
	
	public String getCharacterEncoding()
	{
		return webApp().getServletResponse().getCharacterEncoding();
	}
	
	public String getContentType()
	{
		return webApp().getServletResponse().getContentType();
	}
	
	public ServletOutputStream getOutputStream() throws IOException
	{
		return webApp().getServletResponse().getOutputStream();
	}

	public java.io.PrintWriter getWriter() throws IOException
	{
		return _outputBuffers.peekDefault(webApp().getServletResponse().getWriter());
	}
	
	public void setContentType(String type)
	{
		webApp().getServletResponse().setContentType(type);
	}
	
	public void setContentLength(int len)
	{
		webApp().getServletResponse().setContentLength(len);
	}
	
	public void setCharacterEncoding(String charset)
	{
		webApp().getServletResponse().setCharacterEncoding(charset);
	}
	
	public void addHeader(String name, String value)
	{
		webApp().getServletResponse().addHeader(name, value);
	}

	public void setHeader(String name, String value)
	{
		webApp().getServletResponse().setHeader(name, value);
	}
	
	public boolean containsHeader(String name)
	{
		return webApp().getServletResponse().containsHeader(name);
	}
	
	public void sendRedirect(String location) throws IOException
	{
		webApp().getServletResponse().sendRedirect(location);
	}
	
	public void setStatus(int sc)
	{
		webApp().getServletResponse().setStatus(sc);
	}

	@SuppressWarnings("deprecation")
	public void setStatus(int sc, String message)
	{
		webApp().getServletResponse().setStatus(sc, message);
	}
	
	public void sendError(int sc) throws IOException
	{
		webApp().getServletResponse().sendError(sc);
	}
	
	public void sendError(int sc, java.lang.String msg) throws IOException
	{
		webApp().getServletResponse().sendError(sc, msg);
	}
	
	public boolean ob_start()
	{
		_outputBuffers.push();
		return true;
	}
	
	public String ob_get_contents()
	{
		return _outputBuffers.peekOutput();
	}
	
	public boolean ob_end_clean()
	{
		return _outputBuffers.pop() != null;
	}
	
	public String ob_get_clean()
	{
		return _outputBuffers.popOutput();
	}
	
	private static class OutputBufferManager extends Writer
	{
		private Stack<OutputBufferWriter> _outputBuffers;
		
		public OutputBufferManager()
		{
			this(new Stack<OutputBufferWriter>());
		}

		private OutputBufferManager(Stack<OutputBufferWriter> buffers)
		{
			super(buffers);
			_outputBuffers = buffers;
		}
		
		public void push()
		{
			_outputBuffers.push(new OutputBufferWriter());
		}
		
		public OutputBufferWriter pop()
		{
			if (_outputBuffers.empty())
				return null;
			return _outputBuffers.pop();
		}

		public String popOutput()
		{
			if (_outputBuffers.empty())
				return null;
			return _outputBuffers.pop().getOutput();
		}
		
		/*
		public PrintWriter peek()
		{
			if (_outputBuffers.empty())
				return null;
			return _outputBuffers.peek();
		}
		*/

		public String peekOutput()
		{
			if (_outputBuffers.empty())
				return null;
			return _outputBuffers.peek().getOutput();
		}
		
		public PrintWriter peekDefault(PrintWriter def)
		{
			if (_outputBuffers.empty())
				return def;
			return _outputBuffers.peek();
		}
		
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException 
		{
			for (OutputBufferWriter buffer : _outputBuffers)
			{
				buffer.write(cbuf, off, len);
			}
		}

		@Override
		public void flush() throws IOException 
		{
			for (OutputBufferWriter buffer : _outputBuffers)
			{
				buffer.flush();
			}
		}

		@Override
		public void close() throws IOException 
		{
			for (OutputBufferWriter buffer : _outputBuffers)
			{
				buffer.close();
			}
		}
		
	}
	
	private static class OutputBufferWriter extends PrintWriter
	{
		private final StringWriter _output;

		public OutputBufferWriter()
		{
			this(new StringWriter());
		}
		
		public OutputBufferWriter(StringWriter output)
		{
			super(output);
			_output = output;
		}
		
		public String getOutput()
		{
			return _output.toString();
		}
	}
}
