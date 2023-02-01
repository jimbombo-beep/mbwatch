package org.mbild.mbwatch.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the finish dialog.
 * 
 * @author Matthias Bild
 *
 */
public class FXFinish implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXFinish.fxml";
	
	// private fields
	private Stage _owner;
	
	// -> components
	@FXML private Button btnClose;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXFinish() { }
	
	// public methods
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
