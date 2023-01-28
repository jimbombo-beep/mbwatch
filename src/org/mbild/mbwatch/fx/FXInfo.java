package org.mbild.mbwatch.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for a message dialog.
 * 
 * @author Matthias Bild
 *
 */
public class FXInfo implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXInfo.fxml";
	
	// private fields
	private Stage _owner;
	
	// -> components
	@FXML private Button btnExit;

	// constructors
	/**
	 * Default constructor.
	 */
	public FXInfo() { }
	
	// public methods
	/**
	 * Returns the control used to close the window.
	 * @return A button.
	 */
	public Button getCloseControl() { return btnExit; }
	
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
		btnExit.setOnAction(evt -> {
			if(getOwner() != null)
				getOwner().close();
		});
	}
}
