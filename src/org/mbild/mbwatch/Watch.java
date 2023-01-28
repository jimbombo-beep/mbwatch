package org.mbild.mbwatch;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;

import org.mbild.mbwatch.Configuration.Numbering;
import org.mbild.mbwatch.fx.FXMessage;
import org.mbild.mbwatch.fx.events.FXTimerEvent;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * The watch provides the graphics and functionality of the clock.
 * 
 * Based on: https://blog.crisp.se/2012/08/09/perlundholm/an-analog-clock-in-javafx
 * 
 * @author Matthias Bild
 *
 *
 */
public class Watch extends Group implements EventHandler<Event> {
	// public static fields
	/**
	 * The default radius of the clocks shape.
	 */
	public static final double DEF_RADIUS = 200.0;
	
	// private static fields
	private static String[] _ROMAN = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII" };
	
	// private fields
	private Clip _audio;
	private Configuration _conf;
	private Clockwork _clockwork;
	private Label _lblDate;
	private double _outerR = DEF_RADIUS;
	private List<EventHandler<Event>> _timerEH = new ArrayList<>();	
	private Node _timerHour;
	private Node _timerMinute;
	private Node _timerSecond;
	
	// constructors
	/**
	 * Constructor.
	 * @param conf a watch configuration.
	 */
	public Watch(Configuration conf) {
		_conf = conf;
		_clockwork = new Clockwork(_conf.getTimeZone());
		_clockwork.addFXTimerEventHandler(this);
		
		_init();
	}
	
	/**
	 * Constructor.
	 * @param conf a watch configuration.
	 * @param radius the radius of the clock's shape.
	 */
	public Watch(Configuration conf, double radius) {
		_conf = conf;
		_outerR = radius;
		
		_init();
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
	 * Returns the current date.
	 * @return A local date.
	 */
	public LocalDate getDate() { return _clockwork.getDate(); }
	/**
	 * Returns the current time.
	 * @return A local time.
	 */
	public LocalTime getTime() { return _clockwork.getTime(); }
	
	// public override
	@Override
	public void handle(Event evt) {
		try {
			if(evt instanceof FXTimerEvent tevt) {
				if(tevt.getEventType() == FXTimerEvent.APPLY)
					_setTimer(tevt.getTime());
				else if(tevt.getEventType() == FXTimerEvent.HIT)
					_timerHit(tevt.getTime());
				else if(tevt.getEventType() == FXTimerEvent.UPDATE_DATE)
					_updateDate();
				else if(tevt.getEventType() == FXTimerEvent.UPDATE_TIME)
					fireFXTimerEvent(tevt);
			}
		} catch (Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	// private methods
	private Node _createCenter() {
		Circle c = new Circle(0.05 * _outerR);
		c.setCenterX(_outerR);
		c.setCenterY(_outerR);
		c.setFill(Color.BLACK);
		
		return c;
	}
	private Node _createDate() {
		if(_conf.getShowDate()) {
			Stop[] stops = {
	            new Stop(0.8, Color.WHITE),
	            new Stop(0.85, Color.YELLOW),
	            new Stop(0.9, Color.BLACK),
	            new Stop(0.95, Color.WHITE),
	            new Stop(1.0, Color.BLACK)
			};
			LocalDate date = _clockwork.getDate();
			String strDate = date.format(DateTimeFormatter.ofPattern(_conf.getDateFormat()));

			Translate trans = new Translate();
			trans.setX(_outerR - 0.25 * _outerR);
			trans.setY(_outerR * 1.35);
			
			RadialGradient grad = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
			BorderStroke stroke = new BorderStroke(
					grad,
					BorderStrokeStyle.SOLID,
					new CornerRadii(5),
					BorderStroke.MEDIUM
				);
			Border brd = new Border(stroke);
			
			_lblDate = new Label(strDate);
			_lblDate.setMinWidth(100);
			_lblDate.setPrefWidth(100);
			_lblDate.setMaxWidth(100);
			_lblDate.setAlignment(Pos.CENTER);
			_lblDate.getTransforms().add(trans);
			_lblDate.setBorder(brd);
			
			return _lblDate;
		}
		
		return null;
	}
	private Node _createHand(double stretchRelativeToRim, Color color, Rotate rot) {
		/*Rotate rot = new Rotate();
		rot.setPivotX(_outerR);
		rot.setPivotY(_outerR);
		rot.setAngle(startAngle);*/
		
		Path p = new Path();
		p.setFill(color);
		p.setStroke(Color.TRANSPARENT);
		p.getElements().addAll(
				new MoveTo(_outerR, _outerR),
                new LineTo(_outerR * 0.95, _outerR * 0.95),
                new LineTo(_outerR, stretchRelativeToRim),
                new LineTo(_outerR * 1.05, _outerR * 0.95),
                new LineTo(_outerR, _outerR)
			);
		p.getTransforms().add(rot);
		
		return p;
	}
	private Node _createHourHand() { 
		 Rotate rotate = _rotationAroundCenter();
		 rotate.angleProperty().bind(_clockwork.hour.multiply(360 / 12));
		 
		 return _createHand(_outerR * 0.5, _conf.getClockHandColor(), rotate);
	}
	private Node _createMinuteHand() { 
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().bind(_clockwork.minute.multiply(360 / 60));
		return _createHand(_outerR * 0.2, _conf.getClockHandColor(), rotate); 
	}
	private Node _createOuterRim() {
		Stop[] stops = {
	            new Stop(0.8, Color.WHITE),
	            new Stop(0.85, Color.YELLOW),
	            new Stop(0.9, Color.BLACK),
	            new Stop(0.95, Color.WHITE),
	            new Stop(1.0, Color.BLACK)
	    };
	    RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);

	    Circle result = new Circle(_outerR, gradient);
	    result.setCenterX(_outerR);
	    result.setCenterY(_outerR);
	    return result;
	}
	private Node _createSecondsHand() {
		/*Rotate rot = new Rotate();
		rot.setPivotX(_outerR);
		rot.setPivotY(_outerR);
		rot.setAngle(360 / 12 * 9);*/
		
		Rotate rot = _rotationAroundCenter();
	    rot.angleProperty().bind(_clockwork.second.multiply(360 / 60));
	    
		Line l = new Line();
		l.setStroke(_conf.getClockHandColor());
		l.setStartX(_outerR);
		l.setEndX(_outerR);
		l.setStartY(_outerR * 1.1);
		l.setEndY(_outerR * 0.2);
		
		l.getTransforms().add(rot);
		
		return l;
	}
	private Node _createTickMark(int n) {
		Rotate rot = new Rotate();
		rot.setPivotX(_outerR);
		rot.setPivotY(_outerR);
		rot.setAngle(360 / 12 * n);
		
		Line l = new Line();
		l.setStartX(_outerR);
		l.setEndX(_outerR);
		l.setStartY(_outerR * 0.12);
		l.setEndY(_outerR * (n % 3 == 0 ? 0.3 : 0.2));
		l.getTransforms().add(rot);
		l.setStrokeWidth(2.0);

		Text t = new Text();
		
		switch(_conf.getNumbering()) {
		case QuarterRoman, QuarterArabic:
			{
				if(n % 3 == 0) {
					double rad = Math.toRadians(30 * n - 90);
					Translate trans = new Translate();
					trans.setX(_outerR + 1.1 * _outerR * Math.cos(rad) - 0.05 * _outerR);
					trans.setY(_outerR + 1.1 * _outerR * Math.sin(rad) + 0.05 * _outerR);
					
					t.setFont(Font.font(null, FontWeight.BOLD, 26));
					
					t.setText(_conf.getNumbering() == Numbering.QuarterRoman ? _ROMAN[n - 1] : Integer.toString(n));
					t.getTransforms().add(trans);
				}
				
			}
			
			break;
		case EachRoman, EachArabic:
			{
				double rad = Math.toRadians(30 * n - 90);
				Translate trans = new Translate();
				trans.setX(_outerR + 1.1 * _outerR * Math.cos(rad) - 0.05 * _outerR);
				trans.setY(_outerR + 1.1 * _outerR * Math.sin(rad) + 0.05 * _outerR);

				if(n % 3 == 0)
					t.setFont(Font.font(null, FontWeight.BOLD, 26));
				else
					t.setFont(new Font(20));
				
				t.setText(_conf.getNumbering() == Numbering.EachRoman ? _ROMAN[n - 1] : Integer.toString(n));
				t.getTransforms().add(trans);
				t.setTextAlignment(TextAlignment.CENTER);
			}
			
			break;
		case None: break;
		default: break;
		}
		
		Group res = new Group();
		res.getChildren().addAll(l, t);
		
		return res;
	}
	private Node _createTickMarks() {
		 Group tickMarkGroup = new Group();
		 
	     for (int n = 1; n <= 12; n++) {
	         tickMarkGroup.getChildren().add(_createTickMark(n));
	     }
	     
	     return tickMarkGroup;
	}
	private Node _createTimerHourHand(LocalTime time) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getHour() * 360/12);
		
		return _createHand(_outerR * 0.5, _conf.getTimerHandColor(), rotate);
	}
	private Node _createTimerMinuteHand(LocalTime time) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getMinute() * 360 / 60);
		
		return _createHand(_outerR * 0.2, _conf.getTimerHandColor(), rotate);
	}
	private Node _createTimerSecondHand(LocalTime time) {
		Rotate rot = _rotationAroundCenter();
	    rot.angleProperty().set(time.getSecond() * 360 / 60);
	    
		Line l = new Line();
		l.setStroke(_conf.getTimerHandColor());
		l.setStartX(_outerR);
		l.setEndX(_outerR);
		l.setStartY(_outerR * 1.1);
		l.setEndY(_outerR * 0.2);
		
		l.getTransforms().add(rot);
		
		return l;
	}
	private void _init() {
		setLayoutX(_outerR * 0.3);
		setLayoutY(_outerR * 0.3);
		getChildren().addAll(
				_createOuterRim(),
				_createHourHand(),
				_createMinuteHand(),
				_createSecondsHand(),
				_createTickMarks(),
				_createCenter()
			);
		
		Node ndate = _createDate();
		
		if(ndate != null)
			getChildren().add(ndate);
		
		try {
			_initSound();
			
		} catch (Exception e) {
			FXMessage.showError(e.getMessage());
			e.printStackTrace();
		}
	}
	private void _setTimer(LocalTime time) {
		getChildren().removeAll(_timerHour, _timerMinute, _timerSecond);
		_clockwork.setTimer(time);
		
		_timerHour = _createTimerHourHand(time);
		_timerMinute = _createTimerMinuteHand(time);
		_timerSecond = _createTimerSecondHand(time);
		
		getChildren().addAll(
				_timerHour,
				_timerMinute,
				_timerSecond
			);
	}
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
	private void _timerHit(LocalTime time) {
		System.out.println("Timer hit: " + time.toString());
		
		getChildren().removeAll(_timerHour, _timerMinute, _timerSecond);
		_clockwork.setTimer(null);
		
		fireFXTimerEvent(new FXTimerEvent(FXTimerEvent.HIT, time));
		
		_audio.loop(_conf.getAlertCycles());
	}
	private Rotate _rotationAroundCenter() {
		Rotate rot = new Rotate();
		rot.setPivotX(_outerR);
		rot.setPivotY(_outerR);
		
		return rot;
	}
	private void _updateDate() {
		LocalDate date = _clockwork.getDate();
		String strDate = date.format(DateTimeFormatter.ofPattern(_conf.getDateFormat()));
		
		_lblDate.setText(strDate);
	}
}
