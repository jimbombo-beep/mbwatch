package org.mbild.mbwatch;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mbild.mbwatch.fx.FXDialogView;
import org.mbild.mbwatch.fx.FXMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javafx.scene.paint.Color;

/**
 * Parameter settings defining the look and behavior of the clock.
 * 
 * @author Matthias Bild
 *
 */
public class Configuration implements ISerializable {
	// enumerations
	/**
	 * Enumeration of constants for adjustment of the clock's tick labels.
	 * 
	 * @author Matthias Bild
	 *
	 */
	public enum Numbering {
		/**
		 * No numbering.
		 */
		None,
		/**
		 * Show each hour in roman cipher.
		 */
		EachRoman,
		/**
		 * Show only the quarters in roman cipher.
		 */
		QuarterRoman,
		/**
		 * Show each hour in arabic cipher.
		 */
		EachArabic,
		/**
		 * Show only the quarters in arabic cipher.
		 */
		QuarterArabic;
		
		// public methods
		@Override
		public String toString() {
			if(ordinal() == None.ordinal()) return "None";
			else if(ordinal() == EachRoman.ordinal()) return "EachRoman";
			else if(ordinal() == QuarterRoman.ordinal()) return "QuarterRoman";
			else if(ordinal() == EachArabic.ordinal()) return "EachArabic";
			else if(ordinal() == QuarterArabic.ordinal()) return "QuarterArabic";
			
			return null;
		}
		
		// public static methods
		/**
		 * Returns a list of available values.
		 * @return The list of numberings.
		 */
		public static List<Numbering> getAvailableNumberings() {
			List<Numbering> res = new ArrayList<>();
			res.add(None);
			res.add(EachRoman);
			res.add(QuarterRoman);
			res.add(EachArabic);
			res.add(QuarterArabic);
			
			return res;
		}
		/**
		 * Parses the given string for a numbering value.
		 * @param s any string.
		 * @return A numbering or null.
		 */
		public static Numbering parseNumbering(String s) {
			switch(s) {
			case "None": return None;
			case "EachRoman": return EachRoman;
			case "QuarterRoman": return QuarterRoman;
			case "EachArabic": return EachArabic;
			case "QuarterArabic": return QuarterArabic;
			default: return null;
			}
		}
	}
	
	// public static fields
	/**
	 * Default style sheet.
	 */
	public static final String CSS = FXDialogView.class.getPackageName().replaceAll("\\.", "/") + "/default.css";
	/**
	 * Default number of alert cycles.
	 */
	public static final int DEF_ALERTCYCLES = 20;
	/**
	 * Default color of the clock hands.
	 */
	public static final Color DEF_CLOCKHANDCOLOR = Color.BLACK;
	/**
	 * Default date format.
	 */
	public static final String DEF_DATEFORMAT = "dd.MM.yyyy";
	/**
	 * Default file path.
	 */
	public static final String DEF_PATH = "mbwatch.config.xml";
	/**
	 * Default color of the timer hands.
	 */
	public static final Color DEF_TIMERHANDCOLOR = Color.RED;
	
	/**
	 * Backup filename extension.
	 */
	public static final String EXT_BACKUP = ".bak";
	
	/**
	 * Storage key.
	 */
	public static final String KEY_ALERTCYCLES = "alertCycles";
	/**
	 * Storage key.
	 */
	public static final String KEY_ALPHA = "a";
	/**
	 * Storage key.
	 */
	public static final String KEY_B = "b";
	/**
	 * Storage key.
	 */
	public static final String KEY_CLOCKHANDCOLOR = "clockHandColor";
	/**
	 * Storage key.
	 */
	public static final String KEY_DATEFORMAT = "dateFormat";
	/**
	 * Storage key.
	 */
	public static final String KEY_G = "g";
	/**
	 * Storage key.
	 */
	public static final String KEY_JAVAFX = "javafx";
	/**
	 * Storage key.
	 */
	public static final String KEY_NUMBERING = "numbering";
	/**
	 * Storage key.
	 */
	public static final String KEY_R = "r";
	/**
	 * Storage key.
	 */
	public static final String KEY_SHOWDATE = "showDate";
	/**
	 * Storage key.
	 */
	public static final String KEY_TIMERHANDCOLOR = "timerHandColor";
	/**
	 * Storage key.
	 */
	public static final String KEY_TIMEZONE = "timeZone";
	/**
	 * Storage tag.
	 */
	public static final String TAG_ROOT = "mbwatch";
	
	// private fields
	private int _alertCycles = DEF_ALERTCYCLES;
	private Color _clockHandColor = DEF_CLOCKHANDCOLOR;
	private String _dateFormat = DEF_DATEFORMAT;
	private boolean _javafx = true;
	private Numbering _numbering = Numbering.EachArabic;
	private boolean _showDate = false;
	private Color _timerHandColor = DEF_TIMERHANDCOLOR;
	private String _timeZone = "Europe/Berlin";
	
	// constructors
	/**
	 * Constructor.
	 */
	public Configuration() { 
		try {
			File f = new File(DEF_PATH);
			
			if(!f.exists())
				storeXML(DEF_PATH);
			
			loadXML(DEF_PATH);
			
		} catch (Exception e) {
			if(_javafx) 
				FXMessage.showError(e.getMessage());
			
			e.printStackTrace();
		} 
	}
	
	// public methods
	/**
	 * Returns the number of cycles playing the alert sound.
	 * @return Any positive integer.
	 */
	public int getAlertCycles() { return _alertCycles; }
	/**
	 * Returns the color of the clock hands.
	 * @return Any color.
	 */
	public Color getClockHandColor() { return _clockHandColor; }
	/**
	 * Returns the date format string.
	 * @return Any string.
	 */
	public String getDateFormat() { return _dateFormat; }
	/**
	 * Returns true, if JavaFX is used.
	 * @return True or false.
	 */
	public boolean getJavaFX() { return _javafx; }
	/**
	 * Returns the style of clock's tick labels.
	 * @return A numbering constant.
	 */
	public Numbering getNumbering() { return _numbering; }
	/**
	 * Indicates if the date shall be shown.
	 * @return True or false.
	 */
	public boolean getShowDate() { return _showDate; }
	/**
	 * Returns the color of the timer hands.
	 * @return Any color.
	 */
	public Color getTimerHandColor() { return _timerHandColor; }
	/**
	 * Returns the time zone.
	 * @return Any string.
	 */
	public String getTimeZone() { return _timeZone; }
	
	/**
	 * Sets the number of cycles playing the alert sound.
	 * @param i any positive integer.
	 */
	public void setAlertCycles(int i) { _alertCycles = i; }
	/**
	 * Sets the color of the clock hands.
	 * @param c any color.
	 */
	public void setClockHandColor(Color c) { _clockHandColor = c; }
	/**
	 * Sets the date format.
	 * @param s any string.
	 */
	public void setDateFormat(String s) { _dateFormat = s; }
	/**
	 * Set to true if JavaFX shall be used.
	 * @param b true or false.
	 */
	public void setJavaFX(boolean b) { _javafx = b; }
	/**
	 * Sets the style of the clock's tick labels.
	 * @param n any positive integer.
	 */
	public void setNumbering(Numbering n) { _numbering = n; }
	/**
	 * Set to true if the date shall be shown.
	 * @param b true or false.
	 */
	public void setShowDate(boolean b) { _showDate = b; }
	/**
	 * Sets the color of the timer hands.
	 * @param c any color.
	 */
	public void setTimerHandColor(Color c) { _timerHandColor = c; }
	/**
	 * Sets the time zone.
	 * @param s any string.
	 */
	public void setTimeZone(String s) { _timeZone = s; }
	
	// public override
	@Override
	public void loadXML(String filepath) throws IOException, ParserConfigurationException, SAXException {
		String backupPath = filepath + EXT_BACKUP;
		
		Files.copy(FileSystems.getDefault().getPath(filepath, ""), FileSystems.getDefault().getPath(backupPath, ""), StandardCopyOption.REPLACE_EXISTING);
		
		DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = dbFac.newDocumentBuilder();
		Document doc = builder.parse( new File(filepath));
		
		Element root = doc.getDocumentElement();
		root.normalize();
		
		parseXML(root);
	}
	
	@Override
	public void parseXML(Element root) {
		if(root == null)
			throw new NullPointerException("Root element is undefined in " + getClass().getName() + ".");
		
		if(!root.getTagName().equals(TAG_ROOT))
			throw new IllegalArgumentException("Invalid root tag in " + getClass().getName() + ".");
		
		Element eClockHandColor = (Element) root.getElementsByTagName(KEY_CLOCKHANDCOLOR).item(0);
		
		double chcR = Double.parseDouble(eClockHandColor.getAttribute(KEY_R));
		double chcG = Double.parseDouble(eClockHandColor.getAttribute(KEY_G));
		double chcB = Double.parseDouble(eClockHandColor.getAttribute(KEY_B));
		double chcAlpha = Double.parseDouble(eClockHandColor.getAttribute(KEY_ALPHA));
		
		Element eTimerHandColor = (Element) root.getElementsByTagName(KEY_TIMERHANDCOLOR).item(0);
		
		double thcR = Double.parseDouble(eTimerHandColor.getAttribute(KEY_R));
		double thcG = Double.parseDouble(eTimerHandColor.getAttribute(KEY_G));
		double thcB = Double.parseDouble(eTimerHandColor.getAttribute(KEY_B));
		double thcAlpha = Double.parseDouble(eTimerHandColor.getAttribute(KEY_ALPHA));
		
		setAlertCycles(Integer.parseInt(((Element) root.getElementsByTagName(KEY_ALERTCYCLES).item(0)).getTextContent()));
		setClockHandColor(new Color(chcR, chcG, chcB, chcAlpha));
		setDateFormat(((Element) root.getElementsByTagName(KEY_DATEFORMAT).item(0)).getTextContent());
		setJavaFX(Boolean.parseBoolean(((Element) root.getElementsByTagName(KEY_JAVAFX).item(0)).getTextContent()));
		setNumbering(Numbering.parseNumbering(((Element) root.getElementsByTagName(KEY_NUMBERING).item(0)).getTextContent()));
		setShowDate(Boolean.parseBoolean(((Element) root.getElementsByTagName(KEY_SHOWDATE).item(0)).getTextContent()));
		setTimerHandColor(new Color(thcR, thcG, thcB, thcAlpha));
		setTimeZone(((Element) root.getElementsByTagName(KEY_TIMEZONE).item(0)).getTextContent());
	}

	@Override
	public void storeXML(String filepath) throws IOException, ParserConfigurationException, TransformerException {
		DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbFac.newDocumentBuilder();
		Document doc = builder.newDocument();
		
		if(!filepath.endsWith(".xml"))
			filepath += ".xml";
		
		Element root = toXML(doc);
		doc.appendChild(root);
		
		TransformerFactory trFac = TransformerFactory.newInstance();
		Transformer trans = trFac.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult res = new StreamResult(new File(filepath));
		trans.transform(source,  res);
	}
	
	@Override
	public Element toXML(Document doc) {
		if(doc == null)
			throw new NullPointerException("Undefined document in " + getClass().getName() + ".");
		
		Element eAlertCycles = doc.createElement(KEY_ALERTCYCLES);
		eAlertCycles.setTextContent(Integer.toString(getAlertCycles()));
		
		Element eClockHandColor = doc.createElement(KEY_CLOCKHANDCOLOR);
		eClockHandColor.setAttribute(KEY_R, Double.toString(getClockHandColor().getRed()));
		eClockHandColor.setAttribute(KEY_G, Double.toString(getClockHandColor().getGreen()));
		eClockHandColor.setAttribute(KEY_B, Double.toString(getClockHandColor().getBlue()));
		eClockHandColor.setAttribute(KEY_ALPHA, Double.toString(getClockHandColor().getOpacity()));
		
		Element eDateFormat = doc.createElement(KEY_DATEFORMAT);
		eDateFormat.setTextContent(getDateFormat());
		
		Element ejavafx = doc.createElement(KEY_JAVAFX);
		ejavafx.setTextContent(Boolean.toString(getJavaFX()));
		
		Element eNumbering = doc.createElement(KEY_NUMBERING);
		eNumbering.setTextContent(getNumbering().toString());
		
		Element eshowDate = doc.createElement(KEY_SHOWDATE);
		eshowDate.setTextContent(Boolean.toString(getShowDate()));
		
		Element eTimerHandColor = doc.createElement(KEY_TIMERHANDCOLOR);
		eTimerHandColor.setAttribute(KEY_R, Double.toString(getTimerHandColor().getRed()));
		eTimerHandColor.setAttribute(KEY_G, Double.toString(getTimerHandColor().getGreen()));
		eTimerHandColor.setAttribute(KEY_B, Double.toString(getTimerHandColor().getBlue()));
		eTimerHandColor.setAttribute(KEY_ALPHA, Double.toString(getTimerHandColor().getOpacity()));
		
		Element eTimeZone = doc.createElement(KEY_TIMEZONE);
		eTimeZone.setTextContent(getTimeZone());
		
		Element res = doc.createElement(TAG_ROOT);
		res.appendChild(eAlertCycles);
		res.appendChild(eClockHandColor);
		res.appendChild(eDateFormat);
		res.appendChild(ejavafx);
		res.appendChild(eNumbering);
		res.appendChild(eshowDate);
		res.appendChild(eTimerHandColor);
		res.appendChild(eTimeZone);
		
		return res;
	}

}