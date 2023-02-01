package org.mbild.mbwatch.fx;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mbild.mbwatch.fx.events.BorderResizeListener;
import org.mbild.mbwatch.fx.events.FXStageEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Helper class to show customized dialog windows.
 * 
 * @author Matthias Bild
 * @version 0.1
 *
 */
public class FXDialogView extends Stage implements Initializable, EventHandler<Event> {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXDialogView.fxml";
	
	// private fields
	private List<EventHandler<Event>> _stageEH = new CopyOnWriteArrayList<>();
	private double _offsetX;
	private double _offsetY;
	
	// -> components
	@FXML private BorderPane pRoot;
	@FXML private Button btnExit;
	@FXML private Button btnMaximize;
	@FXML private Button btnMinimize;
	@FXML private ToolBar tbMain;
	
	// constructors
	/**
	 * Default constructor.
	 */
	public FXDialogView() { super(); }
	
	// public methods
	/**
	 * Adds an event-handler for stage-related events.
	 * @param h any event handler.
	 */
	public void addFXStageEventHandler(EventHandler<Event> h) { _stageEH.add(h); }
	
	/**
	 * Fires a stage-related event.
	 * @param evt any event.
	 */
	public void fireFXStageEvent(FXStageEvent evt) {
		for(EventHandler<Event> h : _stageEH) {
			h.handle(evt);
		}
	}
	
	/**
	 * Returns the path to the FXML template file.
	 * @return Any string.
	 */
	public String getTemplate() { return TPL; }
	
	/**
	 * Removes the given event-handler for stage-related events.
	 * @param h any event-handler.
	 */
	public void removeFXStageEventHandler(EventHandler<Event> h) { _stageEH.remove(h); }
	
	/**
	 * Sets the dialog as being decorated.
	 * @param b true if decorated. False else.
	 */
	public void setDecorated(boolean b) { btnMaximize.setVisible(b); btnMinimize.setVisible(b); }
	
	/**
	 * Sets the content of the dialog pane.
	 * @param n any node.
	 */
	public void setContent(Node n) { ScrollPane sp = new ScrollPane(n); sp.setFitToHeight(true); sp.setFitToWidth(true); pRoot.setCenter(sp); }
	
	// public override
	@Override
	public void handle(Event evt) {
		if(evt.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST))
			close();
		
		else if(evt.getEventType().equals(FXStageEvent.CLOSE)) {
			fireFXStageEvent((FXStageEvent) evt);
			close();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnExit.setOnAction(evt -> { fireFXStageEvent(new FXStageEvent(FXStageEvent.CLOSE)); this.close(); });
		btnMaximize.setOnAction(evt -> this.setMaximized(!this.isMaximized()));
		btnMinimize.setOnAction(evt -> this.setIconified(!this.isIconified()));
		
		tbMain.setOnMousePressed(evt -> {
			_offsetX = getX() - evt.getScreenX();
			_offsetY = getY() - evt.getScreenY();
		});
		tbMain.setOnMouseDragged(evt -> {
			setX(evt.getScreenX() + _offsetX);
			setY(evt.getScreenY() + _offsetY);
		});
				
		this.initStyle(StageStyle.UNDECORATED);
		this.setDecorated(false);
		//this.setAlwaysOnTop(true);		
		this.setResizable(true);
	}
	
	// public static methods
	/**
	 * Creates a dialog view.
	 * @param resizable set the window as resizable or not.
	 * @return A new dialog view.
	 * @throws IOException file-related.
	 */
	public static FXDialogView create(boolean resizable) throws IOException {
		String path = "/" + FXDialogView.class.getPackageName().replaceAll("[.]", "/") + "/" + TPL;
		URL loc = FXDialogView.class.getResource(path);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loc);
		Parent p = loader.load();
		FXDialogView ctrl = (FXDialogView) loader.getController();
		ctrl.setResizable(true);
		
		Scene scene = new Scene(p);
		
		if(resizable) {
			BorderResizeListener resizeL = new BorderResizeListener(ctrl, scene, 3);

			scene.setOnMouseMoved(resizeL);
			scene.setOnMousePressed(resizeL);
			scene.setOnMouseDragged(resizeL);
		}
		
		ctrl.setScene(scene);
		
		return ctrl;
	}
}
