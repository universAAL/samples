
package de.fzi.ipe.uapp.infoframe.plugins;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.osgi.framework.FrameworkUtil;
import de.fzi.ipe.uapp.infoframe.Config;

/**
 * Weather Plugin
 * Collects weather information from Wunderground Weather API and draws it
 * on given Image.
 * @author melnikov
 * 
 * http://www.wunderground.com api key: cade3f1e0fe47b56
 *
 */
public class WeatherPlugin {
	
	private Graphics gc;
	private Config config;
	private int pointerY;
	private int imageWidth;
	private int imageHeight;
	private double fontScale;
	private String location;
	private String language;
	private long timeStamp = System.currentTimeMillis();
	/**Root element of the weather XML response **/
	private Element xmlRoot;
	
	
	/**
	 * Main method to draw weather information on given image.
	 * Uses counfig Propterties like language and location.
	 * @param image Background image 
	 * @param config Propterties for infoframe Project
	 */
	public void drawWeather(Image image, Config config) {
		
		this.gc = image.getGraphics ();
		this.config  = config;
    	this.pointerY = config.getPointerY();
		
		this.fontScale = config.getFont_scale();
		this.imageWidth = config.getImage_width();
		this.imageHeight = config.getImage_height();

		
//		XMLInputFactory factory = XMLInputFactory.newInstance();
		
		this.location = config.getLocation();
		this.language = config.getLanguage();

		//Umlaut in URL
		if(language.equals("de")){
			location = location.toLowerCase();
			location = location.replaceAll("ü", "ue");
			location = location.replaceAll("ö", "oe");
			location = location.replaceAll("ä", "ae");
			location = location.replaceAll("ß", "ss");
		}
				
		// update information 
//		if(System.currentTimeMillis() - this.timeStamp > 600000 || xmlRoot == null) {
			try {	
				SAXBuilder builder = new SAXBuilder();
				URL url;
				if(language.equals("de")){
					url = new URL("http://api.wunderground.com/api/1eb3bf22973d83ac/conditions/forecast/lang:DL/q/" + location + ".xml");
				} else {
					url = new URL("http://api.wunderground.com/api/1eb3bf22973d83ac/conditions/forecast/lang:"+ language +"/q/" + location + ".xml");
				}
				xmlRoot = builder.build(url).getRootElement();
				timeStamp = System.currentTimeMillis();
				
			} catch (IOException e) {
				// Could not open URL
				String s1 = "Wunderground Weather API not found";
	    		String s2 = "Please check internet connection";
	        	if(language.equals("de")){
	        		s1 = "Wunderground Wetter API nicht erreichbar";
	        		s2 = "Bitte Internetverbindung überprüfen";
	        	}
	        	drawError(s1, s2);
				e.printStackTrace();
				return;
			} catch (JDOMException e) {
				// Not well-formed XML
				String s1 = "Weather XML is not well-formed";
	    		String s2 = "Please contact support";
	        	if(config.getLanguage().equals("de")){
	        		s1 = "Wetter XML ist nicht wohlgeformt";
	        		s2 = "Bitte support-Dienst kontaktieren";
	        	}
	        	drawError(s1, s2);
				e.printStackTrace();
				return;
			}
			
//		} 
		
		
		draw();

	}
	
	
	private void draw(){
		//--------------------------- Current conditions ---------------------------
		Element currentObs = xmlRoot.getChild("current_observation");
		//current icon
    	String fileName = currentObs.getChildText("icon") + ".png";
    	BufferedImage currIcon;
			try {
				currIcon = ImageIO.read(FrameworkUtil.getBundle(getClass()).getResource("/img/weather/"+fileName));
				int currIconHeight = (int) Math.round(imageHeight * 0.15);
				int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
				gc.drawImage((currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH)), imageWidth-(currIconWidth+40), 20, null);
			} catch (IOException e) {
				System.out.println("Can't load images from /img/weather/");
				e.printStackTrace();
			}
		//current temp
        if(!language.equals("en")){
        	gc.setColor(Color.WHITE);
        	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(24*fontScale));
        	gc.setFont(f);
        	FontMetrics metrics = gc.getFontMetrics(f);
           	String s = currentObs.getChildText("temp_c") + "°C";
           	int width = metrics.stringWidth(s);
        	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
        } else {
        	gc.setColor(Color.WHITE);
        	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(24*fontScale));
        	gc.setFont(f);
        	FontMetrics metrics = gc.getFontMetrics(f);
           	String s = currentObs.getChildText("temp_f") + "°F";
           	int width = metrics.stringWidth(s);
        	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
        }
        //current condition
    	gc.setColor(Color.WHITE);
    	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
    	gc.setFont(f);
    	FontMetrics metrics = gc.getFontMetrics(f);
    	String s = "Current: ";
    	if(language.equals("de")){
    		s = "Aktuell: ";
    	}
    	s += currentObs.getChildText("weather");
    	int width = metrics.stringWidth(s);
    	gc.drawString(s, imageWidth-(20 + width), pointerY);
        
    	//current humidity
    	gc.setColor(Color.WHITE);
    	f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
    	gc.setFont(f);
    	metrics = gc.getFontMetrics(f);
    	s = "Humidity: ";
    	if(language.equals("de")){
    		s = "Luftfeuchtigkeit: ";
    	}
    	s += currentObs.getChildText("relative_humidity");
    	width = metrics.stringWidth(s);
    	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight());
		
    	//current wind
    	gc.setColor(Color.WHITE);
    	f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
    	gc.setFont(f);
    	metrics = gc.getFontMetrics(f);
    	s = "Wind: " + currentObs.getChildText("wind_dir");
    	width = metrics.stringWidth(s);
    	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight()*2);
    	
    	
    	//--------------------------- Forecast conditions ---------------------------
    	
    	
    	Element forecast = xmlRoot.getChild("forecast").getChild("simpleforecast").getChild("forecastdays");
    	List<Element> forecastDays  = forecast.getChildren();
    	int yForecast = (int) Math.round(imageHeight*0.40); //250;
   
    	for (Element day : forecastDays) {
    		//forecast icon
        	fileName = day.getChildText("icon") + ".png";

			try { 
				currIcon = ImageIO.read(FrameworkUtil.getBundle(getClass()).getResource("/img/weather/"+fileName));
				int currIconHeight = (int) Math.round(imageHeight * 0.10);
				int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
				gc.drawImage(currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH), imageWidth - (20 + currIconWidth), yForecast, null);
			} catch (IOException e) {
				System.out.println("Can't load images from /img/weather");
				e.printStackTrace();
			}
    		
			int textOffset = imageWidth - ( 40 +   175 * (int) Math.round(imageHeight * 0.10) / 120);
			
			//forecast days
        	gc.setColor(Color.WHITE);
        	f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(16*fontScale));
        	gc.setFont(f);
        	metrics = gc.getFontMetrics(f);
        	if(day.getChildText("period").equals("1")) {
	        	s = "Today";
	        	if(language.equals("de")){
	        		s = "Heute";
	        	} 
        	} else if(day.getChildText("period").equals("2")) {
	        	s = "Tomorrow";
	        	if(language.equals("de")){
	        		s = "Morgen";
	        	} 
        	} else {
        		s = day.getChild("date").getChildText("weekday_short") + ".";
        	}
        	width = metrics.stringWidth(s);
        	gc.drawString(s, textOffset - width, yForecast+metrics.getHeight());
			
			//forecast temp
        	gc.setColor(Color.WHITE);
        	f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
        	gc.setFont(f);
        	metrics = gc.getFontMetrics(f);
        	String temp = day.getChild("high").getChildText("celsius") + "° | " + day.getChild("low").getChildText("celsius") + "°";
        	width = metrics.stringWidth(temp);
        	gc.drawString(temp, textOffset-width, yForecast+metrics.getHeight()*2);
			
        	//forecast condition
        	gc.setColor(Color.WHITE);
        	f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
        	gc.setFont(f);
        	metrics = gc.getFontMetrics(f);
        	s = day.getChildText("conditions");
        	width = metrics.stringWidth(s);
        	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight()*3);
        	
        	
    		//increase y coord
    		yForecast += (int)Math.round(imageHeight * 0.15);
		}
    	
	}
	
	
	/**
	 * Draw an error with two error messages: s1 and s2
	 * @param s1 Error message 1
	 * @param s2 Error message 2

	 */
	private void drawError(String s1, String s2){	
    	gc.setColor(Color.YELLOW);
    	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
    	gc.setFont(f);
    	FontMetrics metrics = gc.getFontMetrics(f);
    	int width = metrics.stringWidth(s1);
    	gc.drawString(s1,imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
    	width = metrics.stringWidth(s2);
    	gc.drawString(s2, imageWidth-(20 + width), pointerY);
	}
	
//	private void draw(XMLEventReader parser) throws XMLStreamException {
//		
//		int yForecast = (int) Math.round(imageHeight*0.40); //250;
//		boolean current_conditions = false;
//		int day = 1;
//		String temp = "";
//		
//		while( parser.hasNext() ) {
//			XMLEvent event = parser.nextEvent();
//		    int eventType = event.getEventType();
//		    
//		    switch (eventType) {
//		        case XMLStreamConstants.END_DOCUMENT:
//		            parser.close();
//		        break;
//		        case XMLStreamConstants.START_ELEMENT:
//		            StartElement element = event.asStartElement();
//		            //Debug
//		            //System.out.println("START_ELEMENT: " + element.getName() +": "+ element.getAttributeByName(new QName("","data")));		
//		            
//		            //--------------------------- Google Weather API Error ---------------------------			        
////		            if(element.getName().toString().equals("problem_cause")){
////		            	String s1 = "Weather station not found";
////	            		String s2 = "Please check config file";
////		            	if(language.equals("de")){
////		            		s1 = "Wetterstation nicht gefunden";
////		            		s2 = "Bitte Config Datei überprüfen";
////		            	}
////		            	drawError(s1, s2);
////		            }
//		            
//		            //--------------------------- Current conditions ---------------------------
//		            //Check if this element is of type current conditions
//		            if(element.getName().toString().equals("current_observation")){
//		            	current_conditions = true;
//		            }
//		            //Draw the current conditions
//		            if(current_conditions) {
//			            if(element.getName().toString().equals("icon")){
//			            	String fileName = element.getAttributeByName(new QName("","data")).getValue();
//			            	String [] str = fileName.split("/");
//			            	fileName = str[str.length-1].split("\\.")[0] + ".png";
//			            	
//			            	BufferedImage currIcon;
//							try {
//								currIcon = ImageIO.read(new File ( (Activator.class.getProtectionDomain().getCodeSource().getLocation().getPath()).substring(1) + "/img/weather/"+fileName));
//								int currIconHeight = (int) Math.round(imageHeight * 0.15);
//								int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
//								gc.drawImage((currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH)), imageWidth-(currIconWidth+40), 20, null);
//							} catch (IOException e) {
//								System.out.println("Can't load images from /img/weather/");
//								e.printStackTrace();
//							}
//			            	
//			            	
//			            }
//			            if(element.getName().toString().equals("temp_c")){
//			            	if(!language.equals("en")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(24*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				               	String s = element.getAttributeByName(new QName("","data")).getValue() + "°C";
//				               	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
//			            	}
//			            }
//			            if(element.getName().toString().equals("temp_f")&& language.equals("en")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(24*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			               	String s = element.getAttributeByName(new QName("","data")).getValue() + "°F";
//			               	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
//			            }
//			            if(element.getName().toString().equals("condition")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = "Current: ";
//			            	if(language.equals("de")){
//			            		s = "Aktuell: ";
//			            	}
//			            	s += element.getAttributeByName(new QName("","data")).getValue();
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, imageWidth-(20 + width), pointerY);
//			            }
//			            if(element.getName().toString().equals("humidity")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = element.getAttributeByName(new QName("","data")).getValue();
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight());
//			            }
//			            if(element.getName().toString().equals("wind_condition")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = element.getAttributeByName(new QName("","data")).getValue();
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight()*2);
//			            }
//			            
//			            
//			        //--------------------------- Forecast conditions ---------------------------
//		            } else {
//		            
//			            if(element.getName().toString().equals("icon")){
//			            	String fileName = element.getAttributeByName(new QName("","data")).getValue();
//			            	String [] str = fileName.split("/");
//			            	fileName = str[str.length-1].split("\\.")[0] + ".png";
//			            	
//			            	BufferedImage currIcon;
//							try {
//								currIcon = ImageIO.read(new File ((Activator.class.getProtectionDomain().getCodeSource().getLocation().getPath()).substring(1) + "/img/weather/"+fileName));
//								int currIconHeight = (int) Math.round(imageHeight * 0.10);
//								int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
//								gc.drawImage(currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH), imageWidth - (20 + currIconWidth), yForecast, null);
//							} catch (IOException e) {
//								System.out.println("Can't load images from /img/weather");
//								e.printStackTrace();
//							}
//			            	
//			            }
//			            
//			            int textOffset = imageWidth - ( 40 +   175 * (int) Math.round(imageHeight * 0.10) / 120);
//			            
//			            if(element.getName().toString().equals("day_of_week") && day == 1){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(16*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = "Today";
//			            	if(language.equals("de")){
//			            		s = "Heute";
//			            	}
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, textOffset - width, yForecast+metrics.getHeight());
//			            }
//			            if(element.getName().toString().equals("day_of_week") && day == 2){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			           
//			            	String s = "Tomorrow"; 
//			            	if(language.equals("de")){
//			            		s = "Morgen";
//			            	}
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight());
//			            }
//			            if(element.getName().toString().equals("day_of_week") && day > 2){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(16*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = element.getAttributeByName(new QName("","data")).getValue();
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight());
//			            }
//			            if(element.getName().toString().equals("low")){
//			            	temp = element.getAttributeByName(new QName("","data")).getValue()+ "°";
//			            }
//			            if(element.getName().toString().equals("high")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	temp = element.getAttributeByName(new QName("","data")).getValue()+"° | " + temp;
//			            	int width = metrics.stringWidth(temp);
//			            	gc.drawString(temp, textOffset-width, yForecast+metrics.getHeight()*2);
//			            }
//			            if(element.getName().toString().equals("condition")){
//			            	gc.setColor(Color.WHITE);
//			            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//			            	gc.setFont(f);
//			            	FontMetrics metrics = gc.getFontMetrics(f);
//			            	String s = element.getAttributeByName(new QName("","data")).getValue();
//			            	int width = metrics.stringWidth(s);
//			            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight()*3);
//			            }
//		            }
//		        break;
//		        case XMLStreamConstants.CHARACTERS:
//		            Characters characters = event.asCharacters();
//		            if( !characters.isWhiteSpace() )
//		            	System.out.println("CHARACTERS: " + characters.getData() );
//		        break;
//		        case XMLStreamConstants.END_ELEMENT:
//		        	 EndElement endElement = event.asEndElement();
//		        	// System.out.println("END_ELEMENT: " + event.asEndElement().getName() );
//		        	if(endElement.getName().toString().equals("current_observation")){
//		        		//set current_conditions to false if we have parse end element of current condtions
//		        		current_conditions = false;
//			        }
//		        	if(endElement.getName().toString().equals("forecast_conditions")){
//		        		day++;
//		        		yForecast += (int)Math.round(imageHeight * 0.15);
//		        		temp = "";
//			        }   
//		        break;
//		    }
//		}
//	}

}

//
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Properties;
//
//import javax.imageio.ImageIO;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamConstants;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Characters;
//import javax.xml.stream.events.EndElement;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//
//import org.osgi.framework.FrameworkUtil;
//
//import de.cas.merlin.aal.configuration.api.ConfigPreferences;
//import de.cas.uc.infoframe.Activator;
//import de.cas.uc.infoframe.Config;
//
///**
// * Weather Plugin
// * Collects weather information from Google Weather API and draws it
// * on given Image.
// * @author melnikov
// *
// */
//public class WeatherPlugin {
//	
//	private Graphics gc;
//	private Config config;
//	private int pointerY;
//	private int imageWidth;
//	private int imageHeight;
//	private double fontScale;
//	
//	/**
//	 * Main method to draw weather information on given image.
//	 * Uses counfig Propterties like language and location.
//	 * @param image Background image 
//	 * @param config Propterties for infoframe Project
//	 */
//	public void drawWeather(Image image, Config config) {
//		
//		gc = image.getGraphics ();
//		this.config  = config;
//    	this.pointerY = config.getPointerY();
//		
//		this.fontScale = config.getFont_scale();
//		this.imageWidth = config.getImage_width();
//		this.imageHeight = config.getImage_height();
//		
//		int yForecast = (int) Math.round(imageHeight*0.40); //250;
//		boolean current_conditions = false;
//		int day = 1;
//		String temp = "";
//		
//		XMLInputFactory factory = XMLInputFactory.newInstance();
//		
//		String location = config.getLocation();
//		String language = config.getLanguage();
//		
//		//Umlaut in URL
//		if(language.equals("de")){
//			location = location.toLowerCase();
//			location = location.replaceAll("ü", "ue");
//			location = location.replaceAll("ö", "oe");
//			location = location.replaceAll("ä", "ae");
//			location = location.replaceAll("ß", "ss");
//		}
//		
//		InputStream is = null;
//		try {
//			URL url = new URL( "http://www.google.com/ig/api?weather=" + location + "&hl=" + language );
//			is = url.openStream();
//		} catch (IOException e) {
//			// Could not open URL
//			String s1 = "Google Weather API not found";
//    		String s2 = "Please check internet connection";
//        	if(language.equals("de")){
//        		s1 = "Google Wetter API nicht erreichbar";
//        		s2 = "Bitte Internetverbindung überprüfen";
//        	}
//        	
//        	drawError(s1, s2);
//			e.printStackTrace();
//			return;
//		}
//		
//		try {
//			XMLEventReader parser = factory.createXMLEventReader(is, "ISO-8859-1");
//			while( parser.hasNext() ) {
//				XMLEvent event = parser.nextEvent();
//			    int eventType = event.getEventType();
//			    
//			    switch (eventType) {
//			        case XMLStreamConstants.END_DOCUMENT:
//			            parser.close();
//			        break;
//			        case XMLStreamConstants.START_ELEMENT:
//			            StartElement element = event.asStartElement();
//			            //Debug
//			            //System.out.println("START_ELEMENT: " + element.getName() +": "+ element.getAttributeByName(new QName("","data")));		
//			            
//			            //--------------------------- Google Weather API Error ---------------------------			        
//			            if(element.getName().toString().equals("problem_cause")){
//			            	String s1 = "Weather station not found";
//		            		String s2 = "Please check config file";
//			            	if(language.equals("de")){
//			            		s1 = "Wetterstation nicht gefunden";
//			            		s2 = "Bitte Config Datei überprüfen";
//			            	}
//			            	drawError(s1, s2);
//			            }
//			            
//			            //--------------------------- Current conditions ---------------------------
//			            //Check if this element is of type current conditions
//			            if(element.getName().toString().equals("current_conditions")){
//			            	current_conditions = true;
//			            }
//			            //Draw the current conditions
//			            if(current_conditions) {
//				            if(element.getName().toString().equals("icon")){
//				            	String fileName = element.getAttributeByName(new QName("","data")).getValue();
//				            	String [] str = fileName.split("/");
//				            	fileName = str[str.length-1].split("\\.")[0] + ".png";
//				            	
//				            	BufferedImage currIcon;
//								try {
//									currIcon = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/weather/"+fileName));
//									int currIconHeight = (int) Math.round(imageHeight * 0.15);
//									int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
//									gc.drawImage((currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH)), imageWidth-(currIconWidth+40), 20, null);
//								} catch (IOException e) {
//									System.out.println("Can't load images from /img/weather/");
//									e.printStackTrace();
//								}
//				            	
//				            	
//				            }
//				            if(element.getName().toString().equals("temp_c")){
//				            	if(!language.equals("en")){
//					            	gc.setColor(Color.WHITE);
//					            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(24*fontScale));
//					            	gc.setFont(f);
//					            	FontMetrics metrics = gc.getFontMetrics(f);
//					               	String s = element.getAttributeByName(new QName("","data")).getValue() + "°C";
//					               	int width = metrics.stringWidth(s);
//					            	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
//				            	}
//				            }
//				            if(element.getName().toString().equals("temp_f")&& language.equals("en")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(24*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				               	String s = element.getAttributeByName(new QName("","data")).getValue() + "°F";
//				               	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
//				            }
//				            if(element.getName().toString().equals("condition")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = "Current: ";
//				            	if(language.equals("de")){
//				            		s = "Aktuell: ";
//				            	}
//				            	s += element.getAttributeByName(new QName("","data")).getValue();
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, imageWidth-(20 + width), pointerY);
//				            }
//				            if(element.getName().toString().equals("humidity")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = element.getAttributeByName(new QName("","data")).getValue();
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight());
//				            }
//				            if(element.getName().toString().equals("wind_condition")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(20*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = element.getAttributeByName(new QName("","data")).getValue();
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, imageWidth-(20 + width), pointerY + metrics.getHeight()*2);
//				            }
//				            
//				            
//				        //--------------------------- Forecast conditions ---------------------------
//			            } else {
//			            
//				            if(element.getName().toString().equals("icon")){
//				            	String fileName = element.getAttributeByName(new QName("","data")).getValue();
//				            	String [] str = fileName.split("/");
//				            	fileName = str[str.length-1].split("\\.")[0] + ".png";
//				            	
//				            	BufferedImage currIcon;
//								try {
//									currIcon = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/weather/"+fileName));
//									int currIconHeight = (int) Math.round(imageHeight * 0.10);
//									int currIconWidth = currIcon.getWidth()*currIconHeight/currIcon.getHeight();
//									gc.drawImage(currIcon.getScaledInstance(currIconWidth, currIconHeight, Image.SCALE_SMOOTH), imageWidth - (20 + currIconWidth), yForecast, null);
//								} catch (IOException e) {
//									System.out.println("Can't load images from /img/weather");
//									e.printStackTrace();
//								}
//				            	
//				            }
//				            
//				            int textOffset = imageWidth - ( 40 +   175 * (int) Math.round(imageHeight * 0.10) / 120);
//				            
//				            if(element.getName().toString().equals("day_of_week") && day == 1){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(16*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = "Today";
//				            	if(language.equals("de")){
//				            		s = "Heute";
//				            	}
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, textOffset - width, yForecast+metrics.getHeight());
//				            }
//				            if(element.getName().toString().equals("day_of_week") && day == 2){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				           
//				            	String s = "Tomorrow"; 
//				            	if(language.equals("de")){
//				            		s = "Morgen";
//				            	}
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight());
//				            }
//				            if(element.getName().toString().equals("day_of_week") && day > 2){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(16*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = element.getAttributeByName(new QName("","data")).getValue();
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight());
//				            }
//				            if(element.getName().toString().equals("low")){
//				            	temp = element.getAttributeByName(new QName("","data")).getValue()+ "°";
//				            }
//				            if(element.getName().toString().equals("high")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	temp = element.getAttributeByName(new QName("","data")).getValue()+"° | " + temp;
//				            	int width = metrics.stringWidth(temp);
//				            	gc.drawString(temp, textOffset-width, yForecast+metrics.getHeight()*2);
//				            }
//				            if(element.getName().toString().equals("condition")){
//				            	gc.setColor(Color.WHITE);
//				            	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//				            	gc.setFont(f);
//				            	FontMetrics metrics = gc.getFontMetrics(f);
//				            	String s = element.getAttributeByName(new QName("","data")).getValue();
//				            	int width = metrics.stringWidth(s);
//				            	gc.drawString(s, textOffset-width, yForecast+metrics.getHeight()*3);
//				            }
//			            }
//			        break;
//			        case XMLStreamConstants.CHARACTERS:
//			            Characters characters = event.asCharacters();
//			            if( !characters.isWhiteSpace() )
//			            	System.out.println("CHARACTERS: " + characters.getData() );
//			        break;
//			        case XMLStreamConstants.END_ELEMENT:
//			        	 EndElement endElement = event.asEndElement();
//			        	// System.out.println("END_ELEMENT: " + event.asEndElement().getName() );
//			        	if(endElement.getName().toString().equals("current_conditions")){
//			        		//set current_conditions to false if we have parse end element of current condtions
//			        		current_conditions = false;
//				        }
//			        	if(endElement.getName().toString().equals("forecast_conditions")){
//			        		day++;
//			        		yForecast += (int)Math.round(imageHeight * 0.15);
//			        		temp = "";
//				        }   
//			        break;
//			    }
//			}
//			
//		} catch (XMLStreamException e) {
//			// Not well-formed XML
//			String s1 = "Weather XML is not well-formed";
//    		String s2 = "Please contact support";
//        	if(config.getLanguage().equals("de")){
//        		s1 = "Wetter XML ist nicht wohlgeformt";
//        		s2 = "Bitte support-Dienst kontaktieren";
//        	}
//        	drawError(s1, s2);
//			e.printStackTrace();
//		} 
//	}
//	
//	/**
//	 * Draw an error with 2 error messages: s1 and s2
//	 * @param s1 Error message 1
//	 * @param s2 Error message 2
//
//	 */
//	private void drawError(String s1, String s2){
//		
//    	gc.setColor(Color.YELLOW);
//    	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
//    	gc.setFont(f);
//    	FontMetrics metrics = gc.getFontMetrics(f);
//    	int width = metrics.stringWidth(s1);
//    	gc.drawString(s1,imageWidth-(20 + width), 20 +(int)Math.round(imageHeight * 0.15));
//    	width = metrics.stringWidth(s2);
//    	gc.drawString(s2, imageWidth-(20 + width), pointerY);
//	}
//}
