package de.fzi.ipe.uapp.infoframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.FrameworkUtil;

import de.fzi.ipe.uapp.infoframe.plugins.CalendarPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.FeedPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.MailPlugin;
import de.fzi.ipe.uapp.infoframe.plugins.WeatherPlugin;
/**
 * USED ONLY FOR DEBUG 
 * @author melnikov
 *
 */
public class InfoFrameServlet extends HttpServlet {
	
	private static final long serialVersionUID = -8717122121893326904L;
	private Config config;
	
	public InfoFrameServlet(Config config) {
		this.config = config;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, IOException {
		
//		String ret = "width: " + config.getImage_width() + " height: " + config.getImage_height();
//		
//		response.getWriter().write(ret);
		int imageWidth = config.getImage_width();
		int imageHeight = config.getImage_height();
		
		BufferedImage background = createImage();
		
		//Scale
		if(background.getWidth() != imageWidth || background.getHeight() != imageHeight){
			GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			BufferedImage image = gfxConf.createCompatibleImage( imageWidth, imageHeight );
			Graphics gc = image.getGraphics ();
			gc.drawImage(background.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH), 0, 0, null);
			sendJpegImage (image, response);
		} else {
			// Send generated image
			sendJpegImage (background, response);
		}
	}
	
	public BufferedImage createImage(){

		//Dummy Image
		GraphicsConfiguration gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage background = gfxConf.createCompatibleImage(800, 600);
		try {
			background = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/background.jpg"));
		} catch (IOException e) {
			System.out.println("Can't load background image from /img");
			Graphics gc  = background.getGraphics();
			gc.setColor(Color.BLACK);
			gc.fillRect(0, 0, 800, 600);
			e.printStackTrace();
		}
		
		//Pointer for Y coord.
		config.setPointerY(20 + (int)Math.round(config.getImage_height() * 0.20));
		
		//Draw header
		drawHeader(background, background.getWidth(), background.getHeight());
		
		
		//Draw weather
		if(config.getAdd_weahterplugin() == 1) {
			WeatherPlugin wp = new WeatherPlugin();
			wp.drawWeather(background, config);
		}
		//Draw mail
		if(config.getAdd_mailplugin() == 1){
			MailPlugin mp = new MailPlugin();
			mp.drawMail(background, config);
		}
		
		//Draw Calendar
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
	
	private void drawHeader (Image image, int width, int height){
		Graphics gc = image.getGraphics ();
		
		//Time
		
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
		
		gc.setColor(Color.WHITE);
		gc.setFont(new Font(config.getFont(), Font.ROMAN_BASELINE, 80));
		gc.drawString( sdf.format(cal.getTime()), 10, 90);
		
		//Day
		
		gc.setColor(Color.WHITE);
		gc.setFont(new Font(config.getFont(), Font.ROMAN_BASELINE, 24));
		sdf = new SimpleDateFormat("EEEEE, d. MMMMM yyyy");
		gc.drawString( sdf.format(cal.getTime()), 20, 120);
		
		//Line
		
		gc.setColor(Color.WHITE);
		gc.fillRect(20, 130, 761, 3);
	}
	
	
	  /**
	   * Generates a GIF image on the response stream from image.
	   */
	  public void sendJpegImage (Image image, HttpServletResponse response) throws ServletException, IOException
	  {
	    OutputStream out = response.getOutputStream ();
	    try {
	    	ImageIO.write((RenderedImage) image, "jpeg", out);
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    }
	    out.flush ();
	  }
}
