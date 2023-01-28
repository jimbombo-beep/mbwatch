package org.mbild.mbwatch.fx;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

/**
 * Controller for timer settings.
 * 
 * @author Matthias Bild
 *
 */
public class FXTimerSettings implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXTimerSettings.fxml";
	
	// private fields
	private Stage _owner;
	private LocalTime _setting;
	private List<EventHandler<Event>> _timerEH = new ArrayList<>();	
	
	// -> components
	@FXML private Button btnStart;
	@FXML private Button btnClose;
	@FXML private Spinner<Integer> spHour;
	@FXML private Spinner<Integer> spMinute;
	@FXML private Spinner<Integer> spSecond;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXTimerSettings() { /* */ }
	
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
	 * Returns the owner window.
	 * @return A stage or null.
	 */
	public Stage getOwner() { return _owner; }
	
	/**
	 * Returns the user-defined time.
	 * @return A local time.
	 */
	public LocalTime getTime() {
		_setting = LocalTime.of(spHour.getValue(), spMinute.getValue(), spSecond.getValue());
		
		return _setting;
	}
	
	/**
	 * Sets the owner window.
	 * @param s a stage or null.
	 */
	public void setOwner(Stage s) { _owner = s; }
	
	// public override
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnStart.setOnAction(evt -> _apply());
		btnClose.setOnAction(evt -> _close());
		
		spHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0, 1));
		spMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
		spSecond.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 1));
	}

	// private methods
	private void _apply() {
		fireFXTimerEvent(new FXTimerEvent(FXTimerEvent.APPLY, getTime()));
		
		_close();
	}
	
	private void _close() {
		if(getOwner() != null)
			getOwner().close();
	}
}
