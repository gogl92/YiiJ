package com.yiij.base;

import java.util.EventListener;
import java.util.EventObject;

import com.yiij.utils.EventListenerList;

@SuppressWarnings("serial")
public class Event extends EventObject
{
	private boolean _handled = false;
	
	public Event(Object source)
	{
		super(source);
	}
	
	/**
	 * @return whether the event is handled. Defaults to false.
	 * When a handler sets this true, the rest of the uninvoked event handlers will not be invoked anymore.
	 */
	public boolean getHandled()
	{
		return _handled;
	}

	/**
	 * @see #getHandled()
	 */
	public void setHandled(boolean value)
	{
		_handled = value;
	}
	
	/**
	 * @see #getHandled()
	 */
	public void setHandled()
	{
		setHandled(true);
	}
	
	/**
	 * Event listener for Event
	 * @author Rangel Reale <rangelspam@gmail.com>
	 */
	public static interface Listener extends EventListener
	{
		public void onEvent(Event event);
	}
	
	/**
	 * Event listener list.
	 * @author Rangel Reale <rangelspam@gmail.com>
	 */
	//public static class ListenersList<T extends Listener> extends EventListenerList<T>
	public static class ListenersList extends EventListenerList<Listener>
	{
		/**
		 * Fire all events from the listenerClass using event
		 * @param listenerClass the event listener class
		 * @param event the event object. If any listener sets the event as handled, further events are not processed
		 */
		public boolean fire(final Class<?> listenerClass, Event event)
		{
			for (Listener toFire : getListeners(listenerClass))
			{
				if (event.getHandled())
					return true;
				toFire.onEvent(event);
			}
			return event.getHandled();
		}
	}
}
