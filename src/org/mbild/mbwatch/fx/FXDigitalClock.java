package org.mbild.mbwatch.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the digital watch.
 * 
 * @author Matthias Bild
 *
 */
public class FXDigitalClock implements Initializable {
	// public static fields
	/**
	 * Path to the FXML template file.
	 */
	public static final String TPL = "FXDigitalClock.fxml";
	
	// private fields
	private Stage _owner;
	
	//-> components
	@FXML private Button btnClose;
	@FXML private Label lblV;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXDigitalClock() { /* */ }
	
	// public methods
	/**
	 * Returns the owner window.
	 * @return A stage or null;
	 */
	public Stage getOwner() { return _owner; }
	
	/**
	 * Sets the owner window.
	 * @param s a stage or null.
	 */
	public void setOwner(Stage s) { _owner = s; }
	
	/**
	 * Updates the label by the given string.
	 * @param timeString any string.
	 */
	public void updateTime(String timeString) { lblV.setText(timeString); }
	
	// public override
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnClose.setOnAction(evt -> _close());
	}
	
	// private methods
	private void _close() {
		if(getOwner() != null)
			getOwner().close();
	}

}
