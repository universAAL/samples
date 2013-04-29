package de.fzi.ipe.uapp.infoframe.plugins;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.osgi.framework.FrameworkUtil;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import de.fzi.ipe.uapp.infoframe.Config;


/**
 * Calendar Plugin
 * Allows to get calendar events from server definded in config file.
 * @author melnikov
 *
 */
public class CalendarPlugin {
	
	private Graphics gc;
	private Config config;
	private int pointerY;
	private int imageWidth;
	private int imageHeight;
	private double fontScale;
	
	/**
	 * Main method to draw google calendar.
	 * Allows to recieve events from google calendar
	 * @param image Background image to draw on
	 * @param config Config file from IF project
	 */
	public void drawCalendar(Image image, Config config){
		
		//init
		this.gc = image.getGraphics();
		this.config = config;
		this.pointerY = config.getPointerY();
		this.fontScale = config.getFont_scale();
		this.imageWidth = config.getImage_width();
		this.imageHeight = config.getImage_height();
		
		int mde = config.getMax_displayed_events();
		
		BufferedImage calIcon;
		try {
			calIcon = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/calendar.png"));
			int emailIconWidth = (int)Math.round(calIcon.getWidth()*fontScale);
			int emailIconHeight = (int)Math.round(calIcon.getHeight()*fontScale);
			gc.drawImage((calIcon.getScaledInstance(emailIconWidth, emailIconHeight, Image.SCALE_SMOOTH)), 20, pointerY-emailIconHeight, null);
		} catch (IOException e) {
			System.out.println("Can't load images from /img");
			e.printStackTrace();
		}
		
		try {		
			
			//Login
			URL feedUrl = new URL("https://www.google.com/calendar/feeds/default/private/full");
			CalendarService myService = new CalendarService("FZI-InfoFrame-1");
			myService.setUserCredentials(config.getCalendar_user(),config.getCalendar_password());

			CalendarQuery myQuery = new CalendarQuery(feedUrl);
			myQuery.setMinimumStartTime(DateTime.now());

			CalendarEventFeed resultFeed = myService.query(myQuery, CalendarEventFeed.class);
			List<CalendarEventEntry> entries = resultFeed.getEntries();
			
			//Sort entries
			Collections.sort(entries, ceeComp());
			
			gc.setColor(Color.WHITE);
	    	Font f = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(20*fontScale));
	    	gc.setFont(f);
	    	FontMetrics metrics = gc.getFontMetrics(f);
	    	String s = "Calendar:";
	    	if(config.getLanguage().equals("de")){
	    		s = "Kalender:";
	    	}
	    	gc.drawString(s, 20 + (int)Math.round(imageWidth * 0.04), pointerY);
	    	pointerY += metrics.getHeight();

	    	int max = entries.size();
	    	if(mde < max){
	    		max = mde;
	    	}
	    	
	    	//Pring messages
			for (int i = 0; i < max; i++) {
				drawCalendarEvent(entries.get(i), image);
			}
			
			//no new events
			if(entries.size() == 0){
				gc.setColor(Color.WHITE);
				f = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
		    	gc.setFont(f);
		    	metrics = gc.getFontMetrics(f);
				String s1 = "No events!";
		    	if(config.getLanguage().equals("de")){
		    		s1 = "Keine Termine vorhanden!";
		    	}
		    	gc.drawString(s1, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
		    	pointerY +=  metrics.getHeight() + (int)Math.round(imageHeight * 0.01);
			}
			
			//not printed events
			if(entries.size() > mde) {
				gc.setColor(Color.WHITE);
				f = new Font(config.getFont(),Font.BOLD, (int)Math.round(16*fontScale));
		    	gc.setFont(f);
		    	metrics = gc.getFontMetrics(f);
				String s3 = "... " + (entries.size() - mde) + " more event(s)";
				if(config.getLanguage().equals("de")){
					s3 =  "... " + (entries.size() - mde) + " weitere neue Termin(e)";
				}
				gc.drawString(s3, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
				pointerY = pointerY +  metrics.getHeight() + (int)Math.round(imageHeight * 0.01);	
			}
			
			
			
			
		} catch (AuthenticationException e) {
			String s = "Login/Password incorrect. Please check config file";
			if(config.getLanguage().equals("de")){
				s = "Login/Passwort falsch. Config Datei überprüfen";
			}
			drawError(s);
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("Impossible!");
			e.printStackTrace();
		} catch (IOException e) {
			String s = "Google Calendar not found. Check internet connection";
			if(config.getLanguage().equals("de")){
				s = "Google Kalendar nicht gefunden. Internetverbindung überprüfen";
			}
			drawError(s);
			e.printStackTrace();
		} catch (ServiceException e) {
			String s = "Error while processing a GDataRequest";
			if(config.getLanguage().equals("de")){
				s = "Fehler beim Bearbeiten von GDataRequest";
			}
			drawError(s);
			e.printStackTrace();
		}
		//write Y pointer back
		config.setPointerY(pointerY);
	}
	
	
	 /**
     * Method to draw single calendar event
     * @param cee entry to draw
     * @param image background image
     */
    public  void drawCalendarEvent(CalendarEventEntry cee, Image image) {
    	
    	Graphics gc = image.getGraphics();
    	
    	//Title
    	String title = cee.getTitle().getPlainText();
    	gc.setColor(Color.WHITE);
    	gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale)));
    	int maxSubLength = getMaxStringLength(title, (int)Math.round( imageWidth * 0.40), gc);
		if(title.length() > maxSubLength){
			title = title.substring(0, maxSubLength) + "...";
		}
    	gc.drawString(title, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
    	
    	//Time
    	String startTime = cee.getTimes().get(0).getStartTime().toUiString().split("\\s")[1];
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM");
    	String date = sdf.format(cee.getTimes().get(0).getStartTime().getValue());
    	
    	String endTime = cee.getTimes().get(0).getEndTime().toUiString().split("\\s")[1];
    	String time = startTime + " - " + endTime +", " + date;
    	gc.setColor(Color.WHITE);
		Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
    	gc.setFont(f);
    	FontMetrics metrics = gc.getFontMetrics(f);
    	int dateWidth = metrics.stringWidth(time);
    	gc.drawString(time, 20 + (int)Math.round( imageWidth * 0.65 ) -dateWidth, pointerY);	
    	
    
		//Location
    	String loc = cee.getLocations().get(0).getValueString();
    	gc.setColor(Color.WHITE);
    	gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale)));
    	 maxSubLength = getMaxStringLength(loc, (int)Math.round( imageWidth * 0.40), gc);
 		if(loc.length() > maxSubLength){
 			loc = loc.substring(0, maxSubLength) + "...";
 		}
    	gc.drawString(loc, 20 + (int)Math.round(imageWidth * 0.06), pointerY + metrics.getHeight());
    	
		//inc pointer
		pointerY += metrics.getHeight()*2 + (int)Math.round(imageHeight * 0.01);
    }
	
	/**
	 * Draw an error with error message s
	 * @param s Error message
	 */
	private void drawError(String s){
    	gc.setColor(Color.YELLOW);
    	Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
    	gc.setFont(f);
    	gc.drawString(s, 20 + (int)Math.round(imageWidth * 0.04), pointerY);
    	FontMetrics metrics = gc.getFontMetrics();
    	pointerY += metrics.getHeight() + (int)Math.round(imageHeight * 0.01);
	}
	
	/**
	 * Compute max substring length for given string s and given max width 
	 * @param s start string
	 * @param maxWidth max width of substring
	 * @param gc Graphics
	 * @return max substiring length
	 */
	private int getMaxStringLength(String s, int maxWidth, Graphics gc){
		int length = 0;
		int sLength = s.length();
		FontMetrics metrics = gc.getFontMetrics(gc.getFont());
		while(metrics.stringWidth(s)  > maxWidth){
			s = s.substring(0, s.length()-1);
			length ++;
		}
		return sLength-length;
	}
	
	 protected static Comparator<CalendarEventEntry> ceeComp() {
		    return new Comparator<CalendarEventEntry>() {
		      @Override
		      public int compare(CalendarEventEntry cee1, CalendarEventEntry cee2) {
		        return cee1.getTimes().get(0).getStartTime().compareTo(cee2.getTimes().get(0).getStartTime());
		      }
		    };
	 }
}
