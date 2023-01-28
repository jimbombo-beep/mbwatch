package org.mbild.mbwatch.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Basic controller class for message dialogs.
 * 
 * @author Matthias Bild
 *
 */
public class FXMessage implements Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXMessage.fxml";
	
	// private fields
	// -> components
	@FXML private ImageView img;
	@FXML private Label lbl;
	@FXML private TextArea txt;
	
	// public methods
	/**
	 * Returns the image to display.
	 * @return Any image.
	 */
	public Image getImage() { return img.getImage(); }
	/**
	 * Returns the control displaying the image.
	 * @return An image view.
	 */
	public ImageView getImageControl() { return img; }
	/**
	 * Returns the label.
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
	
	// public static methods
	/**
	 * Creates an error-message dialog.
	 * @param message the message to show.
	 * @return A tandem holding the controller and parent pane.
	 * @throws IOException file-related.
	 */
	public static FXTandem createError(String message) throws IOException {
		FXTandem res = FXTandem.loadFXML(new FXError(), FXError.TPL, FXError.class);
		((FXError) res.getController()).setMessage(message);
		((FXError) res.getController()).setLabel("ERROR");
		
		return res;
	}
	/**
	 * Creates an message dialog.
	 * @param message the message to show.
	 * @return A tandem holding the controller and parent pane.
	 * @throws IOException file-related.
	 */
	public static FXTandem createMessage(String message) throws IOException {
		FXTandem res = FXTandem.loadFXML(new FXMessage(), FXMessage.TPL, FXMessage.class);
		((FXMessage) res.getController()).setMessage(message);
		((FXMessage) res.getController()).setLabel("MESSAGE");
		
		return res;
	}
	/**
	 * Creates an warning-message dialog.
	 * @param message the message to show.
	 * @return A tandem holding the controller and parent pane.
	 * @throws IOException file-related.
	 */
	public static FXTandem createWarning(String message) throws IOException {
		FXTandem res = FXTandem.loadFXML(new FXWarning(), FXWarning.TPL, FXWarning.class);
		((FXWarning) res.getController()).setMessage(message);
		((FXWarning) res.getController()).setLabel("WARNING");
		
		return res;
	}

	/**
	 * Shows an error-dialog.
	 * @param message the message to show.
	 * @return A dialog.
	 */
	public static FXDialogView showError(String message) {
		try {
			FXTandem res = createError(message);
			
			FXDialogView view = FXDialogView.create(false);
			view.setContent(res.getParent());
			view.show();
			
			return view;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * Shows an message-dialog.
	 * @param message the message to show.
	 * @return A dialog.
	 */
	public static FXDialogView showMessage(String message) {
		try {
			FXTandem res = createMessage(message);
			
			FXDialogView view = FXDialogView.create(false);
			view.setContent(res.getParent());
			view.show();
			
			return view;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * Shows an warning-dialog.
	 * @param message the message to show.
	 * @return A dialog.
	 * @throws IOException file-related.
	 */
	public static FXDialogView showWarning(String message) throws IOException {
		try {
			FXTandem res = createWarning(message);
			
			FXDialogView view = FXDialogView.create(false);
			view.setContent(res.getParent());
			view.show();
			
			return view;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
