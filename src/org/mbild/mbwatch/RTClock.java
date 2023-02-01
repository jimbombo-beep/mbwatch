package org.mbild.mbwatch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.mbild.mbwatch.fx.FXMessage;
import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.event.Event;
import javafx.scene.Node;

/**
 * Provides a real-time clock.
 * 
 * @author Matthias Bild
 *
 */
public class RTClock extends WatchBase {
	// private fields
	private Clockwork _clockwork;
	private WatchBaseFactory _factory;
	private Node _timerHour;
	private Node _timerMinute;
	private Node _timerSecond;
	
	// constructors
	/**
	 * Constructor.
	 * @param conf the object providing information on the look of the clock.
	 */
	public RTClock(Configuration conf) {
		super(conf);
		
		_clockwork = new Clockwork(getConfiguration().getTimeZone());
		_clockwork.addFXTimerEventHandler(this);
		_factory = new WatchBaseFactory();
	}
	
	// public methods
	/**
	 * Returns the mechanics of the real-time clockwork.
	 * @return Any clockwork.
	 */
	public Clockwork getClockwork() { return _clockwork; }

	/**
	 * Returns the current date.
	 * @return A local date.
	 */
	public LocalDate getDate() { return _clockwork.getDate(); }
	/**
	 * Returns the current time.
	 * @return A local time.
	 */
	public LocalTime getTime() { return _clockwork.getTime(); }

	/**
	 * Sets a timer.
	 * @param time the alarm time.
	 */
	public void setTimer(LocalTime time) {
		getChildren().removeAll(_timerHour, _timerMinute, _timerSecond);
		_clockwork.setTimer(time);
		
		_timerHour = _factory.createHourHand(time, getConfiguration().getTimerHandColor());
		_timerMinute = _factory.createMinuteHand(time, getConfiguration().getTimerHandColor());
		_timerSecond = _factory.createSecondHand(time, getConfiguration().getTimerHandColor());
		
		getChildren().addAll(
				_timerHour,
				_timerMinute,
				_timerSecond
			);
	}
	
	// public override
	@Override
	public void handle(Event evt) {
		try {
			if(evt instanceof FXTimerEvent tevt) {
				if(tevt.getEventType() == FXTimerEvent.APPLY)
					setTimer(tevt.getTime());
				else if(tevt.getEventType() == FXTimerEvent.HIT)
					_timerHit(tevt.getTime());
				else if(tevt.getEventType() == FXTimerEvent.UPDATE_DATE)
					_updateDate();
				else if(tevt.getEventType() == FXTimerEvent.UPDATE_TIME)
					fireFXTimerEvent(tevt);
			}
		} catch (Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// public static methods
	/**
	 * Creates a real-time clock.
	 * @param conf the object providing information on the look of the clock.
	 * @return A real-time clock or null.
	 */
	public static RTClock create(Configuration conf) {
		try {
			WatchBaseFactory factory = new WatchBaseFactory();
			RTClock res = factory.createRealTimeClock(conf);
			res.getHourHandRotation().angleProperty().bind(res.getClockwork().hour.multiply(360 / 12));
			res.getMinuteHandRotation().angleProperty().bind(res.getClockwork().minute.multiply(360 / 60));
			res.getSecondsHandRotation().angleProperty().bind(res.getClockwork().second.multiply(360 / 60));
			
			LocalDate date = res.getClockwork().getDate();
			String strDate = date.format(DateTimeFormatter.ofPattern(res.getConfiguration().getDateFormat()));
			
			res.getDateLabel().setText(strDate);
						
			return res;
		
		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	// private methods
	private void _timerHit(LocalTime time) {
		System.out.println("Timer hit: " + time.toString());
		
		getChildren().removeAll(_timerHour, _timerMinute, _timerSecond);
		_clockwork.setTimer(null);
		
		fireFXTimerEvent(new FXTimerEvent(FXTimerEvent.HIT, time));
		
		getAudio().loop(getConfiguration().getAlertCycles());
	}
	
	private void _updateDate() {
		LocalDate date = _clockwork.getDate();
		String strDate = date.format(DateTimeFormatter.ofPattern(getConfiguration().getDateFormat()));
		
		getDateLabel().setText(strDate);
	}
}
