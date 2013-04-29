package de.fzi.ipe.uapp.infoframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;
import de.fzi.ipe.uapp.infoframe.plugins.CalendarPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.FeedPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.MailPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.WeatherPlugin;

/**
 * Main class for Infoframe RSS
 * @author melnikov
 *
 */
public class InfoFrameRSS implements Runnable  {
	
	private Config config;
	private int imageWidth;
	private int imageHeight;
	
	public InfoFrameRSS(Config config) {
		this.config = config;
	}

	public void run() {	
		
		while(true){
			createRSSFeed();
			//Wait for update
			try {
				Thread.sleep(config.getUpdatetime()*1000);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Main method to create and publish IF RSS feed 
	 */
	public void createRSSFeed(){
		
		LoggerFactory.getLogger(getClass()).info("create RSS feed!");
		
		//Get image width & height
		this.imageWidth = config.getImage_width();
		this.imageHeight = config.getImage_height();
		
		//Create image
		BufferedImage rssImage = createImage();
		
		try {
			ImageIO.write(rssImage, "jpeg", 
					new File(System.getenv("systemdrive")+"\\rss/rssimg.jpeg"));
		} catch (IOException e) {
			System.out.println("Can't save RSS image in to /rss");
			e.printStackTrace();
		}

		//Create RSS feed
		try {
			String feed = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
							"<rss version=\"2.0\" xmlns:media=\"http://search.yahoo.com/mrss/\">\n"+
						 	"<channel>\n" +
							"<title>UC: Infoframe</title>\n" +
							"<link>http://www.fzi.de</link>\n" + 
							"<description>Infoframe</description>\n" +
							"<ttl>1</ttl>\n" + 
							"<item>\n" +
							"<guid isPermaLink=\"false\">" +System.currentTimeMillis()+ "</guid>\n" +
							"<title>InfoFrame Image</title>\n" +
							"<link>" + InetAddress.getLocalHost().getHostAddress() + "/rss/rssimg.jpeg</link>\n" +
							"<description>IF Image</description>\n" +
							"<media:content url=\"http://" + InetAddress.getLocalHost().getHostAddress() + "/rss/rssimg.jpeg\" type=\"image/jpeg\"> </media:content>\n" +
							"</item>\n"+
							"</channel>\n" +
			 				"</rss>";
			
			
			FileWriter outFile = new FileWriter(System.getenv("systemdrive")+"\\rss/infoframe.rss");
			PrintWriter out = new PrintWriter(outFile);
			out.print(feed);
			out.close();
		} catch (UnknownHostException e) {
			System.out.println("Can't get local host address");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Can't save RSS feed file in to /rss");
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Creates image with IF information using plugins (mail, weather etc)
	 * @return Image with actually infoframe information
	 */
	public BufferedImage createImage(){
		//Dummy Image 
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage background = gfxConf.createCompatibleImage(imageWidth, imageHeight);
		try {
			background = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/background.jpg"));
			//Scale background image
			if(background.getWidth() != imageWidth || background.getHeight() != imageHeight){
				BufferedImage image = gfxConf.createCompatibleImage( imageWidth, imageHeight );
				Graphics gc = image.getGraphics ();
				gc.drawImage(background.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
				background = image;
			}
		
		} catch (IOException e) {
			System.out.println("Can't load background image from /img");
			Graphics gc  = background.getGraphics();
			gc.setColor(Color.BLACK);
			gc.fillRect(0, 0, imageWidth, imageHeight);
			e.printStackTrace();
		}
		
		//Pointer for Y coord.
		config.setPointerY(20 + (int)Math.round(imageHeight * 0.20));
		
		//Draw header
		drawHeader(background);
			
		//Draw weather
		if(config.getAdd_weahterplugin() == 1){
			WeatherPlugin wp = new WeatherPlugin();
			wp.drawWeather(background, config);
		}
		
		//Draw mail
		if(config.getAdd_mailplugin() == 1){
			MailPlugin mp = new MailPlugin();
			mp.drawMail(background, config);
		}
		
//		Draw Calendar
		if(config.getAdd_calendarplugin() == 1){
			CalendarPlugin cp = new CalendarPlugin();
			cp.drawCalendar(background, config);
		}
			
		//Draw feeds
		if(config.getAdd_feedplugin() == 1){
			FeedPlugin fp = new FeedPlugin();
			fp.drawFeeds(background, config);
		}
		
		return background;
	}
	
	/**
	 * Method to draw IF header (time & date) and header line
	 * @param image Background image
	 */
	private void drawHeader (Image image){
		Graphics gc = image.getGraphics ();
		
		double fontScale = config.getFont_scale();

		//Time	
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		
		gc.setColor(Color.WHITE);
		Font f = new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(80*fontScale));
		gc.setFont(f);
		String time =  sdf.format(cal.getTime());
		gc.drawString(time, 20, 20 + (int)Math.round(imageHeight * 0.10)); 
		
		//Day	
		gc.setColor(Color.WHITE);
		gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int) Math.round(24*fontScale)));
		sdf = new SimpleDateFormat("EEEEE, d. MMMMM yyyy");
		gc.drawString( sdf.format(cal.getTime()), 20, 20 + (int)Math.round(imageHeight * 0.15)); 
		
		//Line
		gc.setColor(Color.WHITE);
		gc.fillRect(20, 20+ (int)Math.round(imageHeight * 0.16), imageWidth-41, (int) Math.round(3*fontScale)); 
	}
}
