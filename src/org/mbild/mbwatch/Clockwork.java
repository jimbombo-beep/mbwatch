package org.mbild.mbwatch;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * The clock's mechanics. Uses a timeline animation sequence to realize the real-time clock.
 * 
 * Based on: https://blog.crisp.se/2012/08/09/perlundholm/an-analog-clock-in-javafx
 * 
 * @author Matthias Bild
 *
 */
public class Clockwork {
	// public fields
	/**
	 * Day field.
	 */
	public SimpleStringProperty day = new SimpleStringProperty();
	/**
	 * Hour field.
	 */
    public SimpleIntegerProperty hour = new SimpleIntegerProperty(0);
    /**
     * Minute field.
     */
    public SimpleIntegerProperty minute = new SimpleIntegerProperty(0);
    /**
     * Month field.
     */
    public SimpleStringProperty month = new SimpleStringProperty();
    /**
     * Second field.
     */
    public SimpleIntegerProperty second = new SimpleIntegerProperty(0);
    /**
     * Year field.
     */
    public SimpleStringProperty year = new SimpleStringProperty();

    // private fields
    private String _timeZone;
    private LocalTime _timer;
	private List<EventHandler<Event>> _timerEH = new ArrayList<>();	
    
    // constructors
	/**
	 * Constructor.
	 * @param timeZone the time zone to use.
	 */
    public Clockwork(String timeZone) {
    	_timeZone = timeZone;
    	
        _startTicking();
    }

    // public methods
    /**
     * Adds an object handling timer events.
     * @param h any event handler.
     */
	public void addFXTimerEventHandler(EventHandler<Event> h) { _timerEH.add(h); }
	
	/**
	 * Fires a timer event.
	 * @param evt any timer event.
	 */
	public void fireFXTimerEvent(FXTimerEvent evt) {
		for(EventHandler<Event> h : _timerEH) {
			h.handle(evt);
		}
	}
	
	/**
	 * Sets the timer value.
	 * @param timer a local time.
	 */
	public void setTimer(LocalTime timer) { _timer = timer; }
	
	// private methods
    private void _startTicking() {
    	Timeline tl = new Timeline();
    	tl.setCycleCount(Animation.INDEFINITE);
    	tl.getKeyFrames().add(new KeyFrame(Duration.seconds(1), _updateTime()));
    	tl.play();
    }

    private EventHandler<ActionEvent> _updateTime() {
        return event -> {
        	LocalDateTime ldt = LocalDateTime.now(ZoneId.of(_timeZone));
        	day.set(Integer.toString(ldt.getDayOfMonth()));
        	hour.set(ldt.getHour());
        	minute.set(ldt.getMinute());
        	month.set(Integer.toString(ldt.getMonthValue()));
        	second.set(ldt.getSecond());
        	year.set(Integer.toString(ldt.getYear()));
        	
        	if(_timer != null && LocalTime.now(ZoneId.of(_timeZone)).isAfter(_timer))
        		fireFXTimerEvent(new FXTimerEvent(FXTimerEvent.HIT, _timer));
		};
    }
}
