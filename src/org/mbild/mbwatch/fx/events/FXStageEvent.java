package org.mbild.mbwatch.fx.events;


import javafx.event.Event;
import javafx.event.EventType;

/**
 * Stage-related event.
 * @author Matthias Bild
 * @version 0.1
 *
 */
@SuppressWarnings("serial")
public class FXStageEvent extends Event {
	// public static fields
	/**
	 * Event type.
	 */
	public static final EventType<FXStageEvent> APPLY_SETTINGS = new EventType<>("STAGE_APPLY_SETTINGS");
	/**
	 * Event type.
	 */
	public static final EventType<FXStageEvent> CLOSE = new EventType<>("STAGE_CLOSE");
	
	// private fields
	/**
	 * Constructor.
	 * @param eventType the type of event.
	 */
	public FXStageEvent(EventType<FXStageEvent> eventType) { super(eventType); }
}
