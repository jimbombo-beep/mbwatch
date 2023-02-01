package org.mbild.mbwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.mbild.mbwatch.Configuration.Numbering;

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
 * A factory for creating basic and functional clocks.
 * 
 * @author Matthias Bild
 *
 */
public class WatchBaseFactory implements ICSVSerializable {
	// private static fields
	private static final String _DEF_FILEPATH = "factory.conf";
	
	private static final String _KEY_24LABELFONTSIZE = "24LabelFontSize";
	private static final String _KEY_24LABELSHIFTX = "24LabelShiftX";
	private static final String _KEY_24LABELSHIFTY = "24LabelShiftY";
	private static final String _KEY_24LABELSTRETCHX = "24LabelStretchX";
	private static final String _KEY_24LABELSTRETCHY = "24LabelStretchY";
	private static final String _KEY_CENTERRADIUSSTRETCH = "centerRadiusStretch";
	private static final String _KEY_DATESTRETCHX = "dateStretchX";
	private static final String _KEY_DATESTRETCHY = "dateStretchY";
	private static final String _KEY_DATELABELSIZE = "dateLabelSize";
	private static final String _KEY_HANDLINESTRETCHA = "handLineStretchA";
	private static final String _KEY_HANDLINESTRETCHB = "handLineStretchB";
	private static final String _KEY_HOURFONTSIZE = "hourFontSize";
	private static final String _KEY_HOURQUARTERFONTSIZE = "hourQuarterFontSize";
	private static final String _KEY_HOURHANDRELSTRETCH = "hourHandRelStretch";
	private static final String _KEY_LAYOUTSTRETCHX = "layoutStretchX";
	private static final String _KEY_LAYOUTSTRETCHY = "layoutStretchY";
	private static final String _KEY_MINUTECIRCLERADIUSSTRETCH = "minuteCircleRadiusStretch";
	private static final String _KEY_MINNUTELABELFONTSIZE = "minuteLabelFontSize";
	private static final String _KEY_MINUTEHANDRELSTRETCH = "minuteHandRelStretch";
	private static final String _KEY_MINUTELABELSHIFTX = "minuteLabelShiftX";
	private static final String _KEY_MINUTELABELSHIFTY = "minuteLabelShiftY";
	private static final String _KEY_MINUTELABELSTRETCHX = "minuteLabelStretchX";
	private static final String _KEY_MINUTELABELSTRETCHY = "minuteLabelStretchY";
	private static final String _KEY_RIMRADIUS = "rimRadius";
	private static final String _KEY_SECONDSHANDSTRETCHENDY = "secondsHandStretchEndY";
	private static final String _KEY_SECONDSHANDSTRETCHSTARTY = "secondsHandStretchStartY";
	private static final String _KEY_TICKMARKSHIFTX = "tickMarkShiftX";
	private static final String _KEY_TICKMARKSHIFTY = "tickMarkShiftY";
	private static final String _KEY_TICKMARKSTRETCHX = "tickMarkStretchX";
	private static final String _KEY_TICKMARKSTRETCHY = "tickMarkStretchY";
	private static final String _KEY_TICKMARKSTRETCHENDX = "tickMarkStretchEndX";
	private static final String _KEY_TICKMARKSTRETCHENDY = "tickMarkStretchEndY";
	private static final String _KEY_TICKMARKQUARTERSTRETCHENDY = "tickMarkQuarterStretchEndY";
	private static final String _KEY_TICKMARKSTRETCHSTARTX = "tickMarkStretchStartX";
	private static final String _KEY_TICKMARKSTRETCHSTARTY = "tickMarkStretchStartY";
	private static final String _KEY_TICKMARKSTROKEWIDTH = "tickMarkStrokeWidth";
	private static final String _KEY_TICKMARKQUARTERSTROKEWIDTH = "tickMarkQuarterStrokeWidth";
	private static final String _KEY_TIMERHOURHANDRELSTRETCH = "timerHourHandRelStretch";
	private static final String _KEY_TIMERMINUTEHANDRELSTRETCH = "timerMinuterHandRelStretch";
	private static final String _KEY_TIMERSECONDSHANDSTRETCHENDY = "timerSecondsHandStretchEndY";
	private static final String _KEY_TIMERSECONDSHANDSTRETCHSTARTY = "timerSecondsHandStretchStartY";

	private static String[] _ROMAN = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII" };
	
	private static final String _SEP = "=";

	// private fields
	private Map<String, Double> _geomP = new HashMap<>();	
	private Configuration _conf = new Configuration();
	private WatchBase _base = new WatchBase(_conf);

	// constructors
	/**
	 * Default constructor.
	 */
	public WatchBaseFactory() { _initGeomP(); } 
	
	// public methods
	/**
	 * Creates a basic clock without real-time functions.
	 * @param conf the clock configuration.
	 * @return A simple basic clock.
	 * @throws IOException i/o-related.
	 */
	public WatchBase createBasicClock(Configuration conf) throws IOException {
		parseCSV(_DEF_FILEPATH);
		
		_conf = conf;
				
		_base = new WatchBase(_conf);
		_base.setLayoutX(_get(_KEY_LAYOUTSTRETCHX));
		_base.setLayoutY(_get(_KEY_LAYOUTSTRETCHY));
		
		_fill(_base);
		/*_base.getChildren().addAll(
				_createOuterRim(),
				_createHourHand(),
				_createMinuteHand(),
				_createSecondsHand(),
				_createTickMarks(),
				_createMinuteLabels(),
				_create24HourLabels(),
				_createCenter(),
				_createDate()
			);*/
		
		return _base;
	}
	/**
	 * Creates a hour-hand with the given color and positioned on the given time.
	 * @param time the time.
	 * @param color the color.
	 * @return A hand geometry.
	 */
	public Node createHourHand(LocalTime time, Color color) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getHour() * 360/12);
		
		return _createHand(_get(_KEY_RIMRADIUS) * _get(_KEY_HOURHANDRELSTRETCH), color, rotate);
	}
	/**
	 * Creates a minute-hand with the given color and positioned on the given time.
	 * @param time the time.
	 * @param color the color.
	 * @return A hand geometry.
	 */
	public Node createMinuteHand(LocalTime time, Color color) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getMinute() * 360/60);
		
		return _createHand(_get(_KEY_RIMRADIUS) * _get(_KEY_MINUTEHANDRELSTRETCH), color, rotate);
	}
	/**
	 * Creates a real-time clock.
	 * @param conf the clock configuration.
	 * @return A real-time clock.
	 * @throws IOException i/o-related.
	 */
	public RTClock createRealTimeClock(Configuration conf) throws IOException {
		parseCSV(_DEF_FILEPATH);
		
		_conf = conf;
		
		_base = new RTClock(conf);
		_base.setLayoutX(_get(_KEY_LAYOUTSTRETCHX));
		_base.setLayoutX(_get(_KEY_LAYOUTSTRETCHY));
		
		_fill(_base);
		
		return (RTClock) _base;
	}
	/**
	 * Creates a second-hand with the given color and positioned on the given time.
	 * @param time the time.
	 * @param color the color.
	 * @return A hand geometry.
	 */
	public Node createSecondHand(LocalTime time, Color color) {
		Rotate rot = _rotationAroundCenter();
	    rot.angleProperty().set(time.getSecond() * 360 / 60);
	    double r = _get(_KEY_RIMRADIUS);
	    
		Line l = new Line();
		l.setStroke(_conf.getTimerHandColor());
		l.setStartX(r);
		l.setEndX(r);
		l.setStartY(r * 1.1);
		l.setEndY(r * 0.2);
		
		l.getTransforms().add(rot);
		
		return l;
	}
	
	// public override
	@Override
	public void parseCSV(String filepath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		try(reader) {
			String data;
			
			while((data = reader.readLine()) != null) {
				String[] arr = data.split(_SEP);
				
				_parseArr(arr);
			}
			
		} finally {
			reader.close();
		}
	}
	@Override
	public void toCSV(String filepath) throws IOException {
		Iterator<Map.Entry<String, Double>> it = _geomP.entrySet().iterator();
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(new File(_DEF_FILEPATH));
			
			while(it.hasNext()) {
				Map.Entry<String, Double> entry = it.next();
				
				StringBuilder b = new StringBuilder();
				b.append(entry.getValue())
					.append(_SEP)
					.append(Double.toString(entry.getValue()));
					
				if(it.hasNext())
					b.append(System.lineSeparator());
				
				writer.write(b.toString());
			}
		} finally {
			if(writer != null)
				writer.close();
		}
	}
	
	// private methods
	private Node _create24HourLabel(int n) {
		double rad = Math.toRadians(30 * n - 90);
		double r = _get(_KEY_RIMRADIUS);
		Translate trans = new Translate();
		trans.setX(r + _get(_KEY_24LABELSHIFTX) * r * Math.cos(rad) + _get(_KEY_24LABELSTRETCHX) * r);
		trans.setY(r + _get(_KEY_24LABELSHIFTY) * r * Math.sin(rad) + _get(_KEY_24LABELSTRETCHY) * r);
		
		Text t = new Text();
		t.setFont(new Font(_get(_KEY_24LABELFONTSIZE)));
		t.setStroke(_conf.get24LabelColor());
		
		t.setText(Integer.toString(n));
		t.getTransforms().add(trans);
		t.setTextAlignment(TextAlignment.CENTER);
		
		Group res = new Group();
		res.getChildren().add(t);
		
		return res;
	}
	private Node _create24HourLabels() {
		Group res = new Group();
		
		if(_conf.getShow24()) {
			for(int i = 13; i <= 24; i++) {
				res.getChildren().add(_create24HourLabel(i));
			}
		}
		
		return res;
	}
	private Node _createCenter() {
		double r = _get(_KEY_RIMRADIUS);
		Circle c = new Circle(_get(_KEY_CENTERRADIUSSTRETCH) * r);
		c.setCenterX(r);
		c.setCenterY(r);
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
			double r = _get(_KEY_RIMRADIUS);
			
			Translate trans = new Translate();
			trans.setX(r - _get(_KEY_DATESTRETCHX) * r);
			trans.setY(r * _get(_KEY_DATESTRETCHY));
			
			RadialGradient grad = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);
			BorderStroke stroke = new BorderStroke(
					grad,
					BorderStrokeStyle.SOLID,
					new CornerRadii(5),
					BorderStroke.MEDIUM
				);
			Border brd = new Border(stroke);
			
			Label lbl = new Label();
			lbl.setMinWidth(_get(_KEY_DATELABELSIZE));
			lbl.setPrefWidth(_get(_KEY_DATELABELSIZE));
			lbl.setMaxWidth(_get(_KEY_DATELABELSIZE));
			lbl.setAlignment(Pos.CENTER);
			lbl.getTransforms().add(trans);
			lbl.setBorder(brd);
			
			_base.setDateLabel(lbl);
			
			return lbl;
		}
		
		return null;
	}
	private Node _createHand(double stretchRelativeToRim, Color color, Rotate rot) {
		double r = _get(_KEY_RIMRADIUS);
		
		Path p = new Path();
		p.setFill(color);
		p.setStroke(Color.TRANSPARENT);
		p.getElements().addAll(
				new MoveTo(r, r),
                new LineTo(r * _get(_KEY_HANDLINESTRETCHA), r * _get(_KEY_HANDLINESTRETCHA)),
                new LineTo(r, stretchRelativeToRim),
                new LineTo(r * _get(_KEY_HANDLINESTRETCHB), r * _get(_KEY_HANDLINESTRETCHA)),
                new LineTo(r, r)
			);
		p.getTransforms().add(rot);
		
		return p;
	}
	private Node _createHourHand() { 
		 Rotate rotate = _rotationAroundCenter();
		 Node n = _createHand(_get(_KEY_RIMRADIUS) * _get(_KEY_HOURHANDRELSTRETCH), _conf.getClockHandColor(), rotate);

		 _base.setHourHandRotation(rotate);
		 _base.setHourHand(n);
		 
		 return n;
	}
	private Node _createMinuteHand() { 
		Rotate rotate = _rotationAroundCenter();
		Node n = _createHand(_get(_KEY_RIMRADIUS) * _get(_KEY_MINUTEHANDRELSTRETCH), _conf.getClockHandColor(), rotate);
		
		_base.setMinuteHand(n);
		_base.setMinuteHandRotation(rotate);
		
		return n;
	}
	private Node _createMinuteLabel(int n) {
		double rad = Math.toRadians(30 * n - 90);
		double r = _get(_KEY_RIMRADIUS);
		Translate trans = new Translate();
		trans.setX(r + _get(_KEY_MINUTELABELSHIFTX) * r * Math.cos(rad) + _get(_KEY_MINUTELABELSTRETCHX) * r);
		trans.setY(r + _get(_KEY_MINUTELABELSHIFTY) * r * Math.sin(rad) + _get(_KEY_MINUTELABELSTRETCHY) * r);
		
		Text t = new Text();
		t.setFont(new Font(_get(_KEY_MINNUTELABELFONTSIZE)));
		t.setStroke(_conf.getMinuteLabelColor());
		
		t.setText(Integer.toString(n * 5));
		t.getTransforms().add(trans);
		t.setTextAlignment(TextAlignment.CENTER);
		
		Group res = new Group();
		res.getChildren().add(t);
		
		return res;
	}
	private Node _createMinuteLabels() {
		Group res = new Group();
		double r = _get(_KEY_RIMRADIUS);

		Color ccol = new Color(
				Color.YELLOW.getRed(), 
				Color.YELLOW.getGreen(),
				Color.YELLOW.getBlue(),
				0.2);
		
		Circle c = new Circle(_get(_KEY_MINUTECIRCLERADIUSSTRETCH) * r);
		c.setStrokeWidth(0.1);
		c.setFill(ccol);
		c.setCenterX(r);
		c.setCenterY(r);
		
		if(_conf.getShowMinutes()) {
			for(int i = 0; i < 12; i++) {
				res.getChildren().add(_createMinuteLabel(i));
			}
		}

		res.getChildren().add(c);
				
		return res;
	}
	private Node _createOuterRim() {
		Stop[] stops = {
	            new Stop(0.8, Color.WHITE),
	            new Stop(0.85, Color.YELLOW),
	            new Stop(0.9, Color.BLACK),
	            new Stop(0.95, Color.WHITE),
	            new Stop(1.0, Color.BLACK)
	    };
		double r = _get(_KEY_RIMRADIUS);
	    RadialGradient gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops);

	    Circle result = new Circle(r, gradient);
	    result.setCenterX(r);
	    result.setCenterY(r);
	    return result;
	}
	private Node _createSecondsHand() {
		Rotate rot = _rotationAroundCenter();
	    double r = _get(_KEY_RIMRADIUS);
		
		Line l = new Line();
		l.setStroke(_conf.getClockHandColor());
		l.setStartX(r);
		l.setEndX(r);
		l.setStartY(r * _get(_KEY_SECONDSHANDSTRETCHSTARTY));
		l.setEndY(r * _get(_KEY_SECONDSHANDSTRETCHENDY));
		
		l.getTransforms().add(rot);
		
		_base.setSecondsHand(l);
		_base.setSecondsHandRotation(rot);
		
		return l;
	}
	private Node _createTickMark(int n) {
		double r = _get(_KEY_RIMRADIUS);
		
		Rotate rot = new Rotate();
		rot.setPivotX(r);
		rot.setPivotY(r);
		rot.setAngle(360 / 12 * n);
		
		Line l = new Line();
		l.setStartX(r * _get(_KEY_TICKMARKSTRETCHSTARTX));
		l.setEndX(r * _get(_KEY_TICKMARKSTRETCHENDX));
		l.setStartY(r * _get(_KEY_TICKMARKSTRETCHSTARTY));
		l.setEndY(r * (n % 3 == 0 ? _get(_KEY_TICKMARKQUARTERSTRETCHENDY) : _get(_KEY_TICKMARKSTRETCHENDY)));
		l.getTransforms().add(rot);
		l.setStrokeWidth((n % 3 == 0 ? _get(_KEY_TICKMARKQUARTERSTROKEWIDTH) : _get(_KEY_TICKMARKSTROKEWIDTH)));

		Text t = new Text();
		double rad = Math.toRadians(30 * n - 90);
		
		Translate trans = new Translate();
		trans.setX(r + _get(_KEY_TICKMARKSHIFTX) * r * Math.cos(rad) + _get(_KEY_TICKMARKSTRETCHX) * r);
		trans.setY(r + _get(_KEY_TICKMARKSHIFTY) * r * Math.sin(rad) + _get(_KEY_TICKMARKSTRETCHY) * r);
		
		switch(_conf.getNumbering()) {
		case QuarterRoman, QuarterArabic:
			{
				if(n % 3 == 0) {
					
					t.setFont(Font.font(null, FontWeight.BOLD, _get(_KEY_HOURQUARTERFONTSIZE)));
					
					t.setText(_conf.getNumbering() == Numbering.QuarterRoman ? _ROMAN[n - 1] : Integer.toString(n));
					t.getTransforms().add(trans);
					t.setTextAlignment(TextAlignment.CENTER);
				}
				
			}
			
			break;
		case EachRoman, EachArabic:
			{
				if(n % 3 == 0)
					t.setFont(Font.font(null, FontWeight.BOLD, _get(_KEY_HOURQUARTERFONTSIZE)));
				else
					t.setFont(new Font(_get(_KEY_HOURFONTSIZE)));
				
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
	/*private Node _createTimerHourHand(LocalTime time) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getHour() * 360/12);
		
		return _createHand(_rimRadius * _timerHourHandRelStretch, _conf.getTimerHandColor(), rotate);
	}
	private Node _createTimerMinuteHand(LocalTime time) {
		Rotate rotate = _rotationAroundCenter();
		rotate.angleProperty().set(time.getMinute() * 360 / 60);
		
		return _createHand(_rimRadius * _timerMinuteHandRelStretch, _conf.getTimerHandColor(), rotate);
	}
	private Node _createTimerSecondHand(LocalTime time) {
		Rotate rot = _rotationAroundCenter();
	    rot.angleProperty().set(time.getSecond() * 360 / 60);
	    
		Line l = new Line();
		l.setStroke(_conf.getTimerHandColor());
		l.setStartX(_rimRadius);
		l.setEndX(_rimRadius);
		l.setStartY(_rimRadius * _timerSecondsHandStretchStartY);
		l.setEndY(_rimRadius * _timerSecondsHandStretchEndY);
		
		l.getTransforms().add(rot);
		
		return l;
	}
	*/
	private void _fill(WatchBase base) {
		base.getChildren().addAll(
				_createOuterRim(),
				_createHourHand(),
				_createMinuteHand(),
				_createSecondsHand(),
				_createTickMarks(),
				_createMinuteLabels(),
				_create24HourLabels(),
				_createCenter(),
				_createDate()
			);
	}
	private double _get(String key) { return _geomP.get(key); }
	private void _initGeomP() {
		_geomP.put(_KEY_24LABELFONTSIZE, 16.0);
		_geomP.put(_KEY_24LABELSHIFTX, 0.75);
		_geomP.put(_KEY_24LABELSHIFTY, 0.75);
		_geomP.put(_KEY_24LABELSTRETCHX, -0.05);
		_geomP.put(_KEY_24LABELSTRETCHY, 0.05);
		_geomP.put(_KEY_CENTERRADIUSSTRETCH, 0.05);
		_geomP.put(_KEY_DATELABELSIZE, 100.0);
		_geomP.put(_KEY_DATESTRETCHX, 0.25);
		_geomP.put(_KEY_DATESTRETCHY, 1.2);
		_geomP.put(_KEY_HANDLINESTRETCHA, 0.95);
		_geomP.put(_KEY_HANDLINESTRETCHB, 1.05);
		_geomP.put(_KEY_HOURFONTSIZE, 20.0);
		_geomP.put(_KEY_HOURHANDRELSTRETCH, 0.5);
		_geomP.put(_KEY_HOURQUARTERFONTSIZE, 26.0);
		_geomP.put(_KEY_LAYOUTSTRETCHX, 0.3);
		_geomP.put(_KEY_LAYOUTSTRETCHY, 0.3);
		_geomP.put(_KEY_MINNUTELABELFONTSIZE, 16.0);
		_geomP.put(_KEY_MINUTECIRCLERADIUSSTRETCH, 0.6);
		_geomP.put(_KEY_MINUTEHANDRELSTRETCH, 0.2);
		_geomP.put(_KEY_MINUTELABELSHIFTX, 0.5);
		_geomP.put(_KEY_MINUTELABELSHIFTY, 0.5);
		_geomP.put(_KEY_MINUTELABELSTRETCHX, -0.05);
		_geomP.put(_KEY_MINUTELABELSTRETCHY, 0.05);
		_geomP.put(_KEY_RIMRADIUS, 200.0);
		_geomP.put(_KEY_SECONDSHANDSTRETCHENDY, 0.2);
		_geomP.put(_KEY_SECONDSHANDSTRETCHSTARTY, 1.1);
		_geomP.put(_KEY_TICKMARKQUARTERSTRETCHENDY, 0.225);
		_geomP.put(_KEY_TICKMARKQUARTERSTROKEWIDTH, 3.0);
		_geomP.put(_KEY_TICKMARKSHIFTX, 1.1);
		_geomP.put(_KEY_TICKMARKSHIFTY, 1.1);
		_geomP.put(_KEY_TICKMARKSTRETCHENDX, 1.0);
		_geomP.put(_KEY_TICKMARKSTRETCHENDY, 0.175);
		_geomP.put(_KEY_TICKMARKSTRETCHSTARTX, 1.0);
		_geomP.put(_KEY_TICKMARKSTRETCHSTARTY, 0.12);
		_geomP.put(_KEY_TICKMARKSTRETCHX, -0.05);
		_geomP.put(_KEY_TICKMARKSTRETCHY, 0.05);
		_geomP.put(_KEY_TICKMARKSTROKEWIDTH, 2.0);
		_geomP.put(_KEY_TIMERHOURHANDRELSTRETCH, 0.5);
		_geomP.put(_KEY_TIMERMINUTEHANDRELSTRETCH, 0.2);
		_geomP.put(_KEY_TIMERSECONDSHANDSTRETCHENDY, 0.2);
		_geomP.put(_KEY_TIMERSECONDSHANDSTRETCHSTARTY, 1.1);
	}
	private void _parseArr(String[] arr) {
		_geomP.put(arr[0], Double.parseDouble(arr[1]));
	}
	private Rotate _rotationAroundCenter() {
		double r = _get(_KEY_RIMRADIUS);
		
		Rotate rot = new Rotate();
		rot.setPivotX(r);
		rot.setPivotY(r);
		
		return rot;
	}
	
}
