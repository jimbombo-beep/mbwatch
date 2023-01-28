package org.mbild.mbwatch.fx;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 * Helper class for loading FXML files.
 * 
 * @author Matthias Bild
 * @version 0.1
 *
 */
public class FXTandem {
	// private fields
	private Initializable _ctrl;
	private Parent _parent;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXTandem() { }
	/**
	 * Constructor.
	 * @param ctrl the controller instance.
	 * @param parent the parent instance.
	 */
	public FXTandem(Initializable ctrl, Parent parent) { _ctrl = ctrl; _parent = parent; }
	
	// public methods
	/**
	 * Returns the controller object.
	 * @return Any object.
	 */
	public Initializable getController() { return _ctrl; }
	/**
	 * Returns the parent container.
	 * @return Any parent.
	 */
	public Parent getParent() { return _parent; }
	
	// public static methods
	/**
	 * Loads a FXML template and its controller.
	 * @param ctrl the controller.
	 * @param template the path to the FXML template.
	 * @param ctrlType the controller class.
	 * @return A tandem containing the controller and the parent defined by the FXML template.
	 * @throws IOException on file operations.
	 */
	public static FXTandem loadFXML(Initializable ctrl, String template, Class<? extends Initializable> ctrlType) throws IOException {
		String path = "/" + ctrlType.getPackageName().replaceAll("\\.", "/") + "/" + template;
		URL loc = ctrlType.getResource(path);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loc);
		Parent p = null; 
		
		try {
		p = (Parent) loader.load();
		ctrl = (Initializable) loader.getController();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new FXTandem(ctrl, p);
	}
}
