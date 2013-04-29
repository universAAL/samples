package de.fzi.ipe.uapp.infoframe.plugins;


import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.Flags.Flag;

import org.osgi.framework.FrameworkUtil;
import de.fzi.ipe.uapp.infoframe.Config;

/**
 * Mail Plugin
 * Allows to read eMail from server definded in config file.
 * @author melnikov
 *
 */
public class MailPlugin{

    private String protocol;
    private String host = null;
    private String user = null;
    private String password = null;
    private String mbox = null;
    private String url = null;
    private int port = -1;
    private int msgCount = -1;
    private int pointerY;
	private Graphics gc;
    private Config config;
    private double fontScale;
    private int imageWidth;
    private int imageHeight;
    
    /**
     * Main method to draw mail plugin.
     * Allows to connect to mail server to recieve mails
     * @param image Background image to draw on
     * @param config Config file from IF project
     */
    public void drawMail(Image image, Config config) {
    	
    	this.pointerY = config.getPointerY();
    	this.config = config;
    	this.gc = image.getGraphics();
    	
    	this.fontScale = config.getFont_scale();
		this.imageWidth = config.getImage_width();
		this.imageHeight = config.getImage_height();
    	
    	
    	//Draw eMail Plugin Header
		Graphics gc = image.getGraphics();
		
		BufferedImage emailIcon;
		try {
			emailIcon = ImageIO.read(FrameworkUtil.getBundle(this.getClass()).getResource("/img/mail.png"));
			int emailIconWidth = (int)Math.round(emailIcon.getWidth()*fontScale);
			int emailIconHeight = (int)Math.round(emailIcon.getHeight()*fontScale);
			gc.drawImage((emailIcon.getScaledInstance(emailIconWidth, emailIconHeight, Image.SCALE_SMOOTH)), 20, pointerY-emailIconHeight, null);
		} catch (IOException e) {
			System.out.println("Can't load images from /img");
			e.printStackTrace();
		}
    	
		try {		
		    protocol = config.getProtocol();
		    host = config.getHost();
		    user = config.getUser();
		    password = config.getPassword();
		    msgCount = config.getMsg_count();
		    
		    // Get a Session object
		    Session session = Session.getInstance(new Properties(), null);
	
		    // Get a Store object
		    Store store = null;
		    if (url != null) {
			URLName urln = new URLName(url);
			store = session.getStore(urln);
			store.connect();
		    } else {
			if (protocol != null)		
			    store = session.getStore(protocol);
			else
			    store = session.getStore();
	
			// Connect
			if (host != null || user != null || password != null)
			    store.connect(host, port, user, password);
			else
			    store.connect();
		    }
	
		    // Open the Folder
		    Folder folder = store.getDefaultFolder();
		    if (folder == null) {
		    	System.out.println("No default folder");
		    }
	
		    if (mbox == null)
				mbox = "INBOX";
			    folder = folder.getFolder(mbox);
			    if (folder == null) {
			    	System.out.println("Invalid folder");
		    }
	
		    // try to open read/write and if that fails try read-only
		    try {
		    	folder.open(Folder.READ_WRITE);
		    } catch (MessagingException ex) {
		    	folder.open(Folder.READ_ONLY);
		    }
		    
		    int totalMessages = folder.getMessageCount();
	
		    if (totalMessages == 0) {
				System.out.println("Empty folder");
				folder.close(false);
				store.close();
		    }
	
			Message[] msgs = folder.getMessages();
	
			// Use a suitable FetchProfile
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.FLAGS);
			folder.fetch(msgs, fp);
	
			
			gc.setColor(Color.WHITE);
	    	Font f = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(20*fontScale));
	    	gc.setFont(f);
	    	FontMetrics metrics = gc.getFontMetrics(f);
	    	String s = "New mails:";
	    	if(config.getLanguage().equals("de")){
	    		s = "Neue eMails:";
	    	}
	    	gc.drawString(s, 20 + (int)Math.round(imageWidth * 0.04), pointerY);
	    	pointerY += metrics.getHeight();
	    	
			
			int newMsg = 0;
			for (int i =  msgs.length-1; i >=0; i--) {
				//Only new mails & not all
				if(!msgs[i].getFlags().contains(Flag.SEEN)){
					newMsg++;
					if(newMsg <= msgCount){
						drawMessage(msgs[i], image);
					} 
				}
			}
			
			//No new messages
			if(newMsg == 0){
				gc.setColor(Color.WHITE);
				f = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
		    	gc.setFont(f);
		    	metrics = gc.getFontMetrics(f);
				String s1 = "No new mails!";
		    	if(config.getLanguage().equals("de")){
		    		s1 = "Keine neue eMails!";
		    	}
		    	gc.drawString(s1, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
		    	pointerY +=  metrics.getHeight() + (int)Math.round(imageHeight * 0.01);
			} 
			
			//not visited mails
			if(newMsg > msgCount){
				gc.setColor(Color.WHITE);
				f = new Font(config.getFont(),Font.BOLD, (int)Math.round(16*fontScale));
		    	gc.setFont(f);
		    	metrics = gc.getFontMetrics(f);
				String s3 = "... " + (newMsg - msgCount) + " more new mail(s)";
				if(config.getLanguage().equals("de")){
					s3 =  "... " + (newMsg - msgCount) + " weitere neue eMail(s)";
				}
				gc.drawString(s3, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
				pointerY = pointerY +  metrics.getHeight() + (int)Math.round(imageHeight * 0.01);
			}
			//write Y pointer back
			config.setPointerY(pointerY);
			
		    folder.close(false);
		    store.close();
		} catch (NoSuchProviderException ex) {
			String s = "Protocol not known. Please check config file";
			if(config.getLanguage().equals("de")){
				s = "Protokoll nicht bekannt. Config Datei überprüfen";
			}
			drawError(s);
		    ex.printStackTrace();
		} catch (AuthenticationFailedException e){
			String s = "Login/Password incorrect. Please check config file";
			if(config.getLanguage().equals("de")){
				s = "Login/Passwort falsch. Config Datei überprüfen";
			}
			drawError(s);
			e.printStackTrace();
		} catch (MessagingException e) {
			if(e.getCause().toString().contains("UnknownHostException")){
				String s = "Server not found. Check config file / internet connection";
				if(config.getLanguage().equals("de")){
					s = "Server nicht gefunden. Config Datei / Internetverbindung überprüfen";
				}
				drawError(s);
				e.printStackTrace();
			} else {
				String s = "Problem with mail plugin";
				if(config.getLanguage().equals("de")){
					s = "Problem mit mail Plugin";
				}
				drawError(s);
				e.printStackTrace();
			}
		}
		//write Y pointer back
		config.setPointerY(pointerY);
    }

    /**
     * Method to draw single mail on given image
     * @param m message to draw
     * @param image background image
     * @throws MessagingException
     */
    public  void drawMessage(Message m, Image image) throws MessagingException{
    	
    	Graphics gc = image.getGraphics();
    	
    	// DATE
		Date d = m.getSentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("H:mm, d. MMM");
		
		String date = sdf.format(d.getTime());
		
		gc.setColor(Color.WHITE);
		Font f  = new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale));
    	gc.setFont(f);
    	FontMetrics metrics = gc.getFontMetrics(f);
    	int dateWidth = metrics.stringWidth(date);
    	gc.drawString(date, 20 + (int)Math.round( imageWidth * 0.65 ) -dateWidth, pointerY);	
    	
    	// SUBJECT
		String sub = m.getSubject();	
		gc.setColor(Color.WHITE);
    	gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale)));
    	int maxSubLength = getMaxStringLength(sub, (int)Math.round( imageWidth * 0.40), gc);
		if(sub.length() > maxSubLength){
			sub = sub.substring(0, maxSubLength) + "...";
		}
    	gc.drawString(sub, 20 + (int)Math.round(imageWidth * 0.05), pointerY);
    	

		Address[] a;
		// FROM 
		String addr;
		if ((a = m.getFrom()) != null) {
		    	addr = a[0].toString();
		    	gc.setColor(Color.WHITE);
		    	gc.setFont(new Font(config.getFont(),Font.ROMAN_BASELINE, (int)Math.round(16*fontScale)));
		    	int maxAddrLength = getMaxStringLength(addr, (int)Math.round( imageWidth * 0.40), gc);
				if(addr.length() > maxAddrLength){
					addr = addr.substring(0, maxSubLength) + "...";
				}
		    	gc.drawString(addr, 20 + (int)Math.round(imageWidth * 0.06), pointerY + metrics.getHeight());
		}
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
	
}