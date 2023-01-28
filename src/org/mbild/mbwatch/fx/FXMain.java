package org.mbild.mbwatch.fx;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.mbild.mbwatch.fx.events.FXStageEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller for the main window.
 * 
 * @author Matthias Bild
 *
 */
public class FXMain implements EventHandler<Event>, Initializable {
	// public static fields
	/**
	 * FXML template path.
	 */
	public static final String TPL = "FXMain.fxml";
	
	// private fields
	private double _offsetX;
	private double _offsetY;
	private Stage _owner;
	private List<EventHandler<Event>> _stageEH = new CopyOnWriteArrayList<>();
	
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
	public FXMain() { /* */ }

	// public methods
	/**
	 * Adds a handler for stage-related events.
	 * @param h any event handler.
	 */
	public void addFXStageEventHandler(EventHandler<Event> h) { _stageEH.add(h); }
	
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
	 * Returns the owner stage.
	 * @return Any stage.
	 */
	public Stage getOwner() { return _owner; }
	
	/**
	 * Adds a node to the root pane.
	 * @param n any node.
	 */
	public void setContent(Node n) { ScrollPane sp = new ScrollPane(n); sp.setFitToHeight(true); sp.setFitToWidth(true); pRoot.setCenter(sp); }
	/**
	 * Use decorations or not.
	 * @param b true when using decorations.
	 */
	public void setDecorated(boolean b) { btnMaximize.setVisible(b); btnMinimize.setVisible(b); }
	/**
	 * Sets the owner stage.
	 * @param w any window.
	 */
	public void setOwner(Stage w) { _owner = w; }
			
	// public override
	@Override
	public void handle(Event evt) {
		if(evt.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
			getOwner().close();
			
		} else if(evt.getEventType().equals(FXStageEvent.CLOSE)) {
			fireFXStageEvent((FXStageEvent) evt);
			getOwner().close();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnExit.setOnAction(evt -> { 
			fireFXStageEvent(new FXStageEvent(FXStageEvent.CLOSE));
			
			if(getOwner() != null) 
				((Stage) getOwner()).close(); 
			
			//fireEvent(new WindowEvent(getOwner(), WindowEvent.WINDOW_CLOSE_REQUEST));
		});
		btnMaximize.setOnAction(evt -> { if(getOwner() != null) ((Stage) getOwner()).setMaximized(!((Stage) getOwner()).isMaximized()); });
		btnMinimize.setOnAction(evt -> { if(getOwner() != null) ((Stage) getOwner()).setIconified(!((Stage) getOwner()).isIconified()); });
		
		tbMain.setOnMousePressed(evt -> {
			if(getOwner() != null) {
				_offsetX = getOwner().getX() - evt.getScreenX();
				_offsetY = getOwner().getY() - evt.getScreenY();
			}
		});
		tbMain.setOnMouseDragged(evt -> {
			if(getOwner() != null) {
				getOwner().setX(evt.getScreenX() + _offsetX);
				getOwner().setY(evt.getScreenY() + _offsetY);
			}
		});
		
		this.setDecorated(false);
	}

}
