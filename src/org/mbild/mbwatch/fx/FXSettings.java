package org.mbild.mbwatch.fx;

import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.mbild.mbwatch.Configuration;
import org.mbild.mbwatch.Configuration.Numbering;
import org.mbild.mbwatch.fx.events.FXStageEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller for the settings dialog.
 * 
 * @author Matthias Bild
 *
 */
public class FXSettings implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXSettings.fxml";
	
	// private fields
	private Configuration _conf;
	private Stage _owner;
	private List<EventHandler<Event>> _stageEH = new ArrayList<>();
	
	// -> components
	@FXML private Button btnApply;
	@FXML private Button btnClose;
	@FXML private ComboBox<Numbering> cbNumbering;
	@FXML private ComboBox<String> cbTimeZone;
	@FXML private CheckBox chkShowDate;
	@FXML private ColorPicker colorPClockHand;
	@FXML private ColorPicker colorPTimerHand;
	@FXML private Spinner<Integer> spAlertCycles;
	@FXML private TextField txtDateFormat;
	@FXML private TextField txtTimeFormat;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXSettings() { /* */ }
	
	// public methods
	/**
	 * Adds an object handling stage events.
	 * @param h any event handler.
	 */
	public void addFXStageEventHandler(EventHandler<Event> h) { _stageEH.add(h); }
	
	/**
	 * Fires a stage event.
	 * @param evt any stage event.
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
	 * Sets the watch configuration object underlying the settings.
	 * @param c a watch configuration.
	 */
	public void setConfiguration(Configuration c) { _conf = c; updateContents(); }
	/**
	 * Sets the owner window.
	 * @param s a stage or null.
	 */
	public void setOwner(Stage s) { _owner = s; }

	/**
	 * Updates the contents.
	 */
	public void updateContents() {
		if(_conf == null)
			_conf = new Configuration();
		
		spAlertCycles.getValueFactory().setValue(_conf.getAlertCycles());
		colorPClockHand.setValue(_conf.getClockHandColor());
		txtDateFormat.setText(_conf.getDateFormat());
		txtTimeFormat.setText(_conf.getTimeFormat());
		cbNumbering.setValue(_conf.getNumbering());
		chkShowDate.setSelected(_conf.getShowDate());
		colorPTimerHand.setValue(_conf.getTimerHandColor());
		cbTimeZone.setValue(_conf.getTimeZone());
	}
	
	// public override
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnApply.setOnAction(evt -> _apply());
		btnClose.setOnAction(evt -> _close());
		
		cbNumbering.setCellFactory(new Callback<ListView<Numbering>, ListCell<Numbering>>() {
			@Override
			public ListCell<Numbering> call(ListView<Numbering> arg) {
				return new ListCell<Numbering>() {
					@Override
					public void updateItem(Numbering item, boolean empty) {
						super.updateItem(item, empty);
						
						if(item == null || empty)
							setText(null);
						
						else
							setText(item.toString());
					}
				};
			}
		});
		cbNumbering.setButtonCell(cbNumbering.getCellFactory().call(null));
		cbNumbering.getItems().addAll(Numbering.values());
		
		cbTimeZone.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> arg) {
				return new ListCell<String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						
						if(item == null || empty)
							setText(null);
						else
							setText(item);
					}
				};
			}
		});
		cbTimeZone.setButtonCell(cbTimeZone.getCellFactory().call(null));
		cbTimeZone.getItems().addAll(ZoneId.getAvailableZoneIds());
		
		spAlertCycles.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0, 1));
	}

	// private methods
	private void _apply() {
		try {
			if(_conf == null)
				_conf = new Configuration();

			_conf.setAlertCycles(spAlertCycles.getValue());
			_conf.setClockHandColor(colorPClockHand.getValue());
			_conf.setDateFormat(txtDateFormat.getText());
			_conf.setNumbering(cbNumbering.getValue());
			_conf.setShowDate(chkShowDate.isSelected());
			_conf.setTimeFormat(txtTimeFormat.getText());
			_conf.setTimerHandColor(colorPTimerHand.getValue());
			_conf.setTimeZone(cbTimeZone.getValue());

			_conf.storeXML(Configuration.DEF_PATH);
			
			fireFXStageEvent(new FXStageEvent(FXStageEvent.APPLY_SETTINGS));
			
			_close();

		} catch(Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void _close() {
		if(getOwner() != null)
			getOwner().close();
	}
}
