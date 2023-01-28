package org.mbild.mbwatch.fx.events;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Listener for mouse events. Provides the possibility to resize a window by dragging
 * its border.
 * Adapted from: https://geektortoise.wordpress.com/2014/02/07/how-to-programmatically-resize-the-stage-in-a-javafx-app/
 * 
 * @author Matthias Bild
 * @version 0.1
 *
 */
public class BorderResizeListener implements EventHandler<MouseEvent> {
	// private static fields
	private static final double DEF_BORDERW = 10;
	
	// private fields
	private double _borderW = DEF_BORDERW;
	private double _dx;
	private double _dy;
	private double _deltaX;
	private double _deltaY;
	private boolean _mouseH;
	private boolean _mouseV;
	private boolean _resizeH = false;
	private boolean _resizeV = false;
	private Scene _scene;
	private Stage _stage;
	
	// constructors
	/**
	 * Constructor.
	 * @param stage the window to resize.
	 * @param scene the scene shown within stage.
	 */
	public BorderResizeListener(Stage stage, Scene scene) { _stage = stage; _scene = scene; }
	/**
	 * Constructor.
	 * @param stage the window to resize.
	 * @param scene the scene shown within stage.
	 * @param borderWidth the size of the border.
	 */
	public BorderResizeListener(Stage stage, Scene scene, double borderWidth) { this(stage, scene); _borderW = borderWidth; }
	
	// public override
	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
			if(event.getX() < _borderW && event.getY() < _borderW) {
				_scene.setCursor(Cursor.NW_RESIZE);
				_resizeH = true;
				_resizeV = true;
				_mouseH = true;
				_mouseV = true;
				
			} else if(event.getX() < _borderW && event.getY() > _scene.getHeight() - _borderW) {
				_scene.setCursor(Cursor.SW_RESIZE);
				_resizeH = true;
				_resizeV = true;
				_mouseH = true;
				_mouseV = false;
				
			} else if(event.getX() > _scene.getWidth() - _borderW && event.getY() < _borderW) {
				_scene.setCursor(Cursor.NE_RESIZE);
				_resizeH = true;
				_resizeV = true;
				_mouseH = false;
				_mouseV = true;
				
			} else if(event.getX() > _scene.getWidth() - _borderW && event.getY() > _scene.getHeight() - _borderW) {
				_scene.setCursor(Cursor.SE_RESIZE);
				_resizeH = true;
				_resizeV = true;
				_mouseH = false;
				_mouseV = false;
				
			} else if(event.getX() < _borderW || event.getX() > _scene.getWidth() - _borderW) {
				_scene.setCursor(Cursor.E_RESIZE);
				_resizeH = true;
				_resizeV = false;
				_mouseH = (event.getX() < _borderW);
				_mouseV = false;
				
			} else if(event.getY() < _borderW || event.getY() > _scene.getHeight() - _borderW) {
				_scene.setCursor(Cursor.N_RESIZE);
				_resizeH = false;
				_resizeV = true;
				_mouseH = true;
				_mouseV = (event.getY() < _borderW);
				
			} else {
				_scene.setCursor(Cursor.DEFAULT);
				_resizeH = false;
				_resizeV = false;
				_mouseH = false;
				_mouseV = false;
			}
			
		} else if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
			_dx = _stage.getWidth() - event.getX();
			_dy = _stage.getHeight() - event.getY();
			
		} else if(event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
			if(_resizeH) {
				if(_stage.getWidth() <= _stage.getMinWidth()) {
					if(_mouseH) {
						_deltaX = _stage.getX() - event.getScreenX();
						
						if(event.getX() < 0) {
							_stage.setWidth(_deltaX + _stage.getWidth());
							_stage.setX(event.getScreenX());
						}
						
					} else {
						if(event.getX() + _dx - _stage.getWidth() > 0)
							_stage.setWidth(event.getX() + _dx);
					}
					
				} else if(_stage.getWidth() > _stage.getMinWidth()) {
					if(_mouseH) {
						_deltaX = _stage.getX() - event.getScreenX();
						_stage.setWidth(_deltaX + _stage.getWidth());
						_stage.setX(event.getScreenX());
						
					} else {
						_stage.setWidth(event.getX() + _dx);
					}
				}
			}
			
			if(_resizeV) {
				if(_stage.getHeight() <= _stage.getMinHeight()) {
					if(_mouseV) {
						_deltaY = _stage.getY() - event.getScreenY();
						
						if(event.getY() < 0) {
							_stage.setHeight(_deltaY + _stage.getHeight());
							_stage.setY(event.getScreenY());
						}
						
					} else {
						if(event.getY() + _dy - _stage.getHeight() > 0)
							_stage.setHeight(event.getY() + _dy);
					}
					
				} else if(_stage.getHeight() > _stage.getMinHeight()) {
					if(_mouseV) {
						_deltaY = _stage.getY() - event.getScreenY();
						_stage.setHeight(_deltaY + _stage.getHeight());
						_stage.setY(event.getScreenY());
						
					} else {
						_stage.setHeight(event.getY() + _dy);
					}
				}
			}
		}
	}
	
}

