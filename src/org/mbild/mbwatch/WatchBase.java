package org.mbild.mbwatch;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;

import org.mbild.mbwatch.fx.FXMessage;
import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;

/**
 * Class representing a simple, basic clock without any real-time functions.
 * 
 * @author Matthias Bild
 *
 */
public class WatchBase extends Group implements EventHandler<Event> {
	// private fields
	private Clip _audio;
	private Configuration _conf = new Configuration();
	private Node _hourHand;
	private Rotate _hourHandRotation;
	private Label _lblDate;
	private Node _minuteHand;
	private Rotate _minuteHandRotation;
	private Node _secondsHand;
	private Rotate _secondsHandRotation;
	private List<EventHandler<Event>> _timerEH = new CopyOnWriteArrayList<>();
	
	// constructors
	/**
	 * Default constructor.
	 * @param conf the object prooviding information on the look of the clock.
	 */
	public WatchBase(Configuration conf) {
		_conf = conf;
		_initSound();
	}
	
	// public methods
	/**
	 * Adds an object handling timer events.
	 * @param h any event handler.
	 */
	public void addFXTimerEventHandler(EventHandler<Event> h) { _timerEH.add(h); }
	
	/**
	 * Fires a timer event.
	 * @param evt any timer event.
	 */
	public void fireFXTimerEvent(FXTimerEvent evt) {
		for(EventHandler<Event> h : _timerEH) {
			h.handle(evt);
		}
	}
	
	/**
	 * Returns the signalling sound.
	 * @return An audio clip.
	 */
	public Clip getAudio() { return _audio; }
	/**
	 * Returns the clock's configuration.
	 * @return An object providing information on the look of the clock.
	 */
	public Configuration getConfiguration() { return _conf; }
	/**
	 * Returns the label displaying the date.
	 * @return A label.
	 */
	public Label getDateLabel() { return _lblDate; }
	/**
	 * Returns the hour-hand.
	 * @return A hand geometry.
	 */
	public Node getHourHand() { return _hourHand; }
	/**
	 * Returns the rotation of the hour-hand.
	 * @return A transformation.
	 */
	public Rotate getHourHandRotation() { return _hourHandRotation; }
	/**
	 * Returns the minute-hand.
	 * @return A hand geometry.
	 */
	public Node getMinuteHand() { return _minuteHand; }
	/**
	 * Returns the rotation of the minute-hand.
	 * @return A transformation.
	 */
	public Rotate getMinuteHandRotation() { return _minuteHandRotation; }
	/**
	 * Returns the seconds-hand.
	 * @return A hand geometry.
	 */
	public Node getSecondsHand() { return _secondsHand; }
	/**
	 * Returns the rotation of the seconds-hand.
	 * @return A transformation.
	 */
	public Rotate getSecondsHandRotation() { return _secondsHandRotation; }
	
	/**
	 * Sets the label displaying the date.
	 * @param l any label.
	 */
	public void setDateLabel(Label l) { _lblDate = l; }
	/**
	 * Sets the hour-hand.
	 * @param n a hand geometry.
	 */
	public void setHourHand(Node n) { _hourHand = n; }
	/**
	 * Sets the rotation of the hour-hand.
	 * @param r a transformation.
	 */
	public void setHourHandRotation(Rotate r) { _hourHandRotation = r; }
	/**
	 * Sets the minute-hand.
	 * @param n a hand geometry.
	 */
	public void setMinuteHand(Node n) { _minuteHand = n; }
	/**
	 * Sets the rotation of the minute-hand.
	 * @param r a transformation.
	 */
	public void setMinuteHandRotation(Rotate r) { _minuteHandRotation = r; }
	/**
	 * Sets the second-hand.
	 * @param n a hand geometry.
	 */
	public void setSecondsHand(Node n) { _secondsHand = n; }
	/**
	 * Sets the rotation of the second-hand.
	 * @param r a transformation.
	 */
	public void setSecondsHandRotation(Rotate r) { _secondsHandRotation = r; }
	
	// public override
	@Override
	public void handle(Event arg0) {
		// 
	}

	// private methods
	private void _initSound() {
		final String path = "/" + getClass().getPackageName().replaceAll("\\.", "/") + "/sounds/complete.wav";
		final URL resource = getClass().getResource(path);
		
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(resource);
			AudioFormat format = in.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			Mixer.Info selected = null;
			
			for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			    Mixer mixer = AudioSystem.getMixer(mixerInfo);
			    if (mixer.isLineSupported(info)) {
			    	selected = mixerInfo;
			        break;
			    }
			}

			if (selected != null) {
				_audio = AudioSystem.getClip(selected);
				_audio.open(in);
			}
			
		} catch (Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
}
