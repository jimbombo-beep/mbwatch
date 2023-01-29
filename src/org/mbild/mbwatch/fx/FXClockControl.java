package org.mbild.mbwatch.fx;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mbild.mbwatch.Configuration;
import org.mbild.mbwatch.Watch;
import org.mbild.mbwatch.fx.events.FXStageEvent;
import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controller for the clock graphics.
 * 
 * @author Matthias Bild
 *
 */
public class FXClockControl implements EventHandler<Event>, Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXClockControl.fxml";
	
	// private fields
	private Configuration _config;
	private FXDigitalClock _digClock;
	private Stage _owner;
	private Watch _watch;
	private List<EventHandler<Event>> _stageEH = new CopyOnWriteArrayList<>();
		
	// -> components
	@FXML private Button btnClock;
	@FXML private Button btnConfig;
	@FXML private Button btnDigital;
	@FXML private Button btnInfo;
	@FXML private Button btnTimer;
	@FXML private BorderPane pRoot;
	@FXML private BorderPane pWatch;
	@FXML private ToolBar tbMain;
	
	// constructors.
	/**
	 * Default constructor.
	 */
	public FXClockControl() { /* */ }
	
	// public methods
	/**
	 * Adds a handler for stage-related events.
	 * @param h any event handler.
	 */
	public void addFXStageEventHandler(EventHandler<Event> h) { _stageEH.add(h); }
	
	/**
	 * Configures the control using the given configuration.
	 * @param conf a watch configuration.
	 */
	public void configure(Configuration conf) {
		_config = conf;
		_loadWatch();
		_showClock();
	}

	/**
	 *  Fires a stage-related event. 
	 * @param evt the event.
	 */
	public void fireFXStageEvent(FXStageEvent evt) {
		for(EventHandler<Event> h : _stageEH) {
			h.handle(evt);
		}
	}
	
	/**
	 * Returns the owner window.
	 * @return A stage or null.
	 */
	public Stage getOwner() { return _owner; }
	
	/**
	 * Sets the owner window.
	 * @param s a stage or null.
	 */
	public void setOwner(Stage s) { _owner = s; }

	/**
	 * Updates the digital clock.
	 */
	public void updateDigitalClock() { if(_digClock != null) _digClock.updateTime(_watch.getTime().format(DateTimeFormatter.ofPattern(_config.getTimeFormat()))); }
	
	// public override
	@Override
	public void handle(Event evt) {
		try {
			if(evt instanceof FXStageEvent sevt) {
				if(sevt.getEventType().equals(FXStageEvent.CLOSE)) 
					fireFXStageEvent(sevt);

				else if(sevt.getEventType().equals(FXStageEvent.APPLY_SETTINGS)) {
					_config.loadXML(Configuration.DEF_PATH);
					_showClock();
					
					fireFXStageEvent(sevt);
				}
				
			} else if(evt instanceof FXTimerEvent tevt) {
				if(tevt.getEventType().equals(FXTimerEvent.HIT))
					_blink();
				else if(tevt.getEventType().equals(FXTimerEvent.UPDATE_TIME))
					updateDigitalClock();
			}

		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnClock.setOnAction(evt -> _showClock());
		btnConfig.setOnAction(evt -> _showConfig());
		btnDigital.setOnAction(evt -> _showDigital());
		btnInfo.setOnAction(evt -> _showInfo());
		btnTimer.setOnAction(evt -> _showTimer());
	}

	// private methods
	private void _blink() {
		try {
			FXTandem res = FXTandem.loadFXML(new FXFinish(), FXFinish.TPL, FXFinish.class);
			
			FXFinish ctrl = (FXFinish) res.getController();
			
			FXDialogView view = FXDialogView.create(false);
			view.setDecorated(false);
			view.setResizable(false);
			view.setContent(res.getParent());
			view.setAlwaysOnTop(true);
			
			ctrl.setOwner(view);
			
			view.show();
			
		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void _loadWatch() {
		_watch = new Watch(_config);
		_watch.addFXTimerEventHandler(this);
	}
	
	private void _showClock() {
		_loadWatch();
		pRoot.setCenter(_watch);
	}

	private void _showConfig() {
		try {
			FXTandem res = FXTandem.loadFXML(new FXSettings(), FXSettings.TPL, FXSettings.class);

			FXSettings ctrl = (FXSettings) res.getController();

			FXDialogView view = FXDialogView.create(false);
			view.setDecorated(false);
			view.setResizable(false);
			view.setContent(res.getParent());
			view.setAlwaysOnTop(true);

			ctrl.setOwner(view);
			ctrl.addFXStageEventHandler(view);
			ctrl.addFXStageEventHandler(this);
			ctrl.setConfiguration(_config);

			view.show();
			
		} catch (IOException e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void _showDigital() {
		try {
			FXTandem res = FXTandem.loadFXML(new FXDigitalClock(), FXDigitalClock.TPL, FXDigitalClock.class);
			_digClock = (FXDigitalClock) res.getController();
			
			FXDialogView view = FXDialogView.create(false);
			view.setDecorated(false);
			view.setResizable(false);
			view.setContent(res.getParent());
			view.setAlwaysOnTop(true);
			
			_digClock.setOwner(view);
			
			updateDigitalClock();
			
			view.show();
			
		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void _showInfo() {
		try {
			FXTandem res = FXTandem.loadFXML(new FXInfo(), FXInfo.TPL, FXInfo.class);
			FXInfo ctrl = (FXInfo) res.getController();
			
			FXDialogView view  = FXDialogView.create(false);
			view.setDecorated(false);
			view.setResizable(false);
			view.setContent(res.getParent());
			view.setAlwaysOnTop(true);
			
			ctrl.setOwner(view);
			
			view.show();
			
		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void _showTimer() {
		try {
			FXTandem res = FXTandem.loadFXML(new FXTimerSettings(), FXTimerSettings.TPL, FXTimerSettings.class);
			FXTimerSettings ctrl = (FXTimerSettings) res.getController();
			
			FXDialogView view = FXDialogView.create(false);
			view.setDecorated(false);
			view.setResizable(false);
			view.setContent(res.getParent());
			view.setAlwaysOnTop(true);
			
			ctrl.setOwner(view);
			ctrl.addFXTimerEventHandler(_watch);
			
			view.show();
			
		} catch (IOException e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
