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
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import de.fzi.ipe.uapp.infoframe.Config;
/**
 * Main class to obtain and draw feeds information
 * @author melnikov
 *
 */
public class FeedPlugin {
	
	private Graphics gc;
	private Config config;
	private int pointerY;
	private double fontScale;
	private int imageWidth;
	private int imageHeight;
	
	/**
	 * Main method to obtain and draw feeds
	 * @param image Background image
	 * @param config Properties for Infoframe Project
	 */
	public void drawFeeds(Image image, Config config){
		
		//init
		this.gc = image.getGraphics();
		this.pointerY = config.getPointerY();
		this.config = config;
		
		this.fontScale = config.getFont_scale();
		this.imageWidth = config.getImage_width();
		this.imageHeight = config.getImage_height();
			
		SyndFeedInput input = new SyndFeedInput();
		try {
		
			//draw feeds header
			BufferedImage rssIcon;
			try {
				rssIcon = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/rss.png"));
				int rssIconWidth = (int)Math.round(rssIcon.getWidth()*fontScale);
				int rssIconHeight = (int)Math.round(rssIcon.getHeight()*fontScale);
				gc.drawImage((rssIcon.getScaledInstance(rssIconWidth, rssIconHeight, Image.SCALE_SMOOTH)), 20, pointerY-rssIconHeight, null);
			} catch (IOException e) {
				System.out.println("Can't load images from /img");
				e.printStackTrace();
			}
			LoggerFactory.getLogger(this.getClass()).debug(config.getFeed_url_1() );
			URL url = new URL(config.getFeed_url_1() );
			XmlReader xmlReader = new XmlReader(url);
			SyndFeed feed = input.build(xmlReader);
			
			//Feed Header (Title)
			gc.setColor(Color.WHITE);
	    	gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(20*fontScale)));
	    	FontMetrics metrics = gc.getFontMetrics(gc.getFont());
	    	gc.drawString(feed.getTitle(),  20 + (int)Math.round(imageWidth * 0.04), pointerY);
	    	pointerY += metrics.getHeight(); 
	    	
	    	int maxDisplItems = config.getMax_displayed_items_1();
	    	List<SyndEntry> entries = feed.getEntries();
	    	List<SyndEntry> matchedEntries = new LinkedList<SyndEntry>();
	    	
	    	String regex = config.getProperty("RegExp1");
	    	
	    	//Match entries
	    	if(!regex.equals("")){
	    		for (int i = 0; i < entries.size(); i++) {
					if(entries.get(i).getTitle().matches(regex)){						
						matchedEntries.add(entries.get(i));
					}
				}
	    	} else {
	    		matchedEntries = entries;
	    	}
	    	
	    	//Draw feed entries
	    	for(int i = 0; i < matchedEntries.size();  i++){
	    		if(i < maxDisplItems){
		    		String s = matchedEntries.get(i).getTitle();
		    		gc.setColor(Color.WHITE);
		    		Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE,  (int)Math.round(16*fontScale));
		        	gc.setFont(f);
		        	metrics = gc.getFontMetrics(gc.getFont());
		        	int maxSubLength = getMaxStringLength(s, (int)Math.round( imageWidth * 0.60), gc);
		        	while (s.length() > maxSubLength){
		    			String s1 = s.substring(0, maxSubLength) + "-";
		    			gc.drawString(s1, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
			        	pointerY += metrics.getHeight();
			        	s = s.substring(maxSubLength);
		    		}
		        	gc.drawString(s, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
		        	pointerY += metrics.getHeight() + (int)Math.round(imageHeight * 0.01);
	    		}
	    	}
	    	
	    	int size = matchedEntries.size();
	    	if(size > maxDisplItems){
				gc.setColor(Color.WHITE);
				gc.setFont(new Font(config.getFont(),Font.BOLD,  (int)Math.round(16*fontScale)));
				metrics = gc.getFontMetrics(gc.getFont());
				String s3 = "... " + (size - maxDisplItems) + " more message(s)";
				if(config.getLanguage().equals("de")){
					s3 =  "... " + (size - maxDisplItems) + " weitere neue Einträg(e)";
				}
				gc.drawString(s3, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
				pointerY +=  metrics.getHeight()+ (int)Math.round(imageHeight * 0.01);
			}
	    	
		
		} catch (MalformedURLException e) {
			String s = "Invalid URL. Please check config file";
			if(config.getLanguage().equals("de")){
				s = "Inkorrekte URL. Bitte config Datei überprüfen";
			}
			drawError(s);
			e.printStackTrace();
		} catch (FeedException e) {
			String s = "Invalid RSS feed: RSS is not well-formed";
			if(config.getLanguage().equals("de")){
				s = "RSS Feed ist nicht wohlgeformt";
			}
			drawError(s);
			e.printStackTrace();
		} catch (IOException e) {
			String s = "RSS not found. Please check internet connection";
			if(config.getLanguage().equals("de")){
				s = "RSS nicht gefunden. Bitte Internetverbindung überprüfen";
			}
			drawError(s);
			e.printStackTrace();
		}
		
		//write Y pointer back
		config.setPointerY(pointerY);
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
    	pointerY += metrics.getHeight() +  (int)Math.round(imageHeight * 0.01);
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
}
