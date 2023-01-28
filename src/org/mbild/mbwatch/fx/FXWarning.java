package org.mbild.mbwatch.fx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller for warning dialogs.
 * 
 * @author Matthias Bild
 *
 */
public class FXWarning implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXWarning.fxml";

	// private fields
	// -> components
	@FXML private ImageView img;
	@FXML private Label lbl;
	@FXML private TextArea txt;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXWarning() { /* */ }
	
	// public methods
	/**
	 * Returns the image shown within the dialog.
	 * @return Any image.
	 */
	public Image getImage() { return img.getImage(); }
	/**
	 * Returns the control displaying the image.
	 * @return An image view.
	 */
	public ImageView getImageControl() { return img; }
	/**
	 * Returns the text shown within this dialog.
	 * @return Any string.
	 */
	public String getLabel() { return lbl.getText(); }
	/**
	 * Returns the control displaying the label.
	 * @return A label.
	 */
	public Label getLabelControl() { return lbl; }
	/**
	 * Returns the message to show.
	 * @return Any string.
	 */
	public String getMessage() { return txt.getText(); }
	/**
	 * Returns the control displaying the message.
	 * @return A text area.
	 */
	public TextArea getMessageControl() { return txt; }
	
	/**
	 * Sets the image show within the dialog.
	 * @param i any image.
	 */
	public void setImage(Image i) { img.setImage(i); }
	/**
	 * Sets the label shown within this dialog.
	 * @param s any string.
	 */
	public void setLabel(String s) { lbl.setText(s); }
	/**
	 * Sets the message to show.
	 * @param message any string.
	 */
	public void setMessage(String message) { txt.setText(message); }

	// public override
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// 
	}

}
