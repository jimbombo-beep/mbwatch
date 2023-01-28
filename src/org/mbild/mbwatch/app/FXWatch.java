package org.mbild.mbwatch.app;

import org.mbild.mbwatch.Configuration;
import org.mbild.mbwatch.fx.FXClockControl;
import org.mbild.mbwatch.fx.FXMain;
import org.mbild.mbwatch.fx.FXTandem;
import org.mbild.mbwatch.fx.events.BorderResizeListener;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Application class.
 * 
 * @author Matthias Bild
 *
 */
public class FXWatch extends Application {
	// constructors
	/**
	 * Default constructor.
	 */
	public FXWatch() { /* */ }
	
	// public override
	@Override
	public void start(Stage stage) throws Exception {
		FXTandem res = FXTandem.loadFXML(new FXClockControl(), FXClockControl.TPL, FXClockControl.class);
		FXClockControl editCtrl = (FXClockControl) res.getController();
		editCtrl.setOwner(stage);
		editCtrl.configure(new Configuration());
		
		FXTandem resMain = FXTandem.loadFXML(new FXMain(), FXMain.TPL, FXMain.class);
		FXMain mainCtrl = (FXMain) resMain.getController();
		mainCtrl.setOwner(stage);
		mainCtrl.addFXStageEventHandler(evt -> {
			stage.getOnCloseRequest().handle(new WindowEvent(null, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
		mainCtrl.setDecorated(true);
		mainCtrl.setContent(res.getParent());

		editCtrl.addFXStageEventHandler(mainCtrl);
		
		Scene scene = new Scene(resMain.getParent());
		stage.setScene(scene);
		
		BorderResizeListener resizeL = new BorderResizeListener(stage, scene, 4);
		
		scene.setOnMouseMoved(resizeL);
		scene.setOnMousePressed(resizeL);
		scene.setOnMouseDragged(resizeL);
		
		try {
			//Image img = new Image("AppIcon_MBWatch_01.png");
			//stage.getIcons().add(img);
			stage.setTitle("MBWatch");
			stage.setResizable(true);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			stage.setOnCloseRequest(evt -> {
				if(evt.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)) {
					mainCtrl.handle(evt);
					stop();
				}
			});
			stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
				WindowEvent wevt = new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST);
				mainCtrl.handle(wevt);
				stop();
			});
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.exit(0);
	}

	// public static methods
	/**
	 * Program entry.
	 * @param args program arguments.
	 */
	public static void main(String[] args) { Application.launch(args); }

}
