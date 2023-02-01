package org.mbild.mbwatch.fx.events;

import java.time.LocalTime;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event class for timer events.
 * 
 * @author Matthias Bild
 *
 */
@SuppressWarnings("serial")
public class FXTimerEvent extends Event {
	// public static fields
	/**
	 * Event type.
	 */
	public static final EventType<FXTimerEvent> APPLY = new EventType<>("TIMER_APPLY");
	/**
	 * Event type.
	 */
	public static final EventType<FXTimerEvent> HIT = new EventType<>("TIMER_HIT");
	/**
	 * Event type.
	 */
	public static final EventType<FXTimerEvent> UPDATE_DATE = new EventType<>("UPDATE_DATE");
	/**
	 * Event type.
	 */
	public static final EventType<FXTimerEvent> UPDATE_TIME = new EventType<>("UPDATE_TIME");
	
	// private fields
	/**
	 * The given time.
	 */
	private LocalTime _time;
	
	// constructors
	/**
	 * Constructor.
	 * @param eventType the event type.
	 * @param time the timer value.
	 */
	public FXTimerEvent(EventType<FXTimerEvent> eventType, LocalTime time) { super(eventType); _time = time; } 
	
	// public methods
	/**
	 * Returns the timer value.
	 * @return A local time.
	 */
	public LocalTime getTime() { return _time; }
	
	/**
	 * Sets the timer value.
	 * @param t a local time.
	 */
	public void setTime(LocalTime t) { _time = t; }
}
