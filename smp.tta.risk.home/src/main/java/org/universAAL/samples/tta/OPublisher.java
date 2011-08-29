package org.universAAL.samples.tta;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputPublisher;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.ontology.profile.User;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author alfiva
 */
public class OPublisher extends OutputPublisher{
	
	private final static Logger log=LoggerFactory.getLogger(OPublisher.class);
	protected static Timer responseWatch;
	
	protected OPublisher(BundleContext context) {
		super(context);
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
		
	}
	
	public void showButtonScreenForm(User user)
	{
		log.debug("Show button screen - Start delay timer");
		responseWatch=new Timer("Risk_ResponseTimer");
		responseWatch.schedule(new ResponseDelayTask(user), Long.parseLong(Activator.getProperties().getProperty(Activator.DELAY,"1"))*60000);
		Form f = Activator.gui.getUserStateForm();
		OutputEvent oe = new OutputEvent(user,f,LevelRating.high,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
		playWarning();
	}
	
	public void showSMSForm(User user, boolean smsSuccess)
	{
		log.debug("Show SMS screen");
		Form f = Activator.gui.getSMSForm(smsSuccess);
		OutputEvent oe = new OutputEvent(user,f,LevelRating.full,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
		playWarning();
	}
	
	public void showNoVCForm(User user)
	{
		log.debug("Show VC failed screen");
		Form f = Activator.gui.getNoVCForm();
		OutputEvent oe = new OutputEvent(user,f,LevelRating.full,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
	}
	
	public void showBatteryForm(User user)
	{
		log.debug("Show Battery message");
		Form f = Activator.gui.getBatteryForm();
		OutputEvent oe = new OutputEvent(user,f,LevelRating.middle,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
	}
	
	private static class ResponseDelayTask extends TimerTask{
		public static User user;
		public ResponseDelayTask(User usr){
			user=usr;
		}
		public void run() {
			log.debug("Did not cancel risk situation");
			if(Boolean.parseBoolean(Activator.getProperties().getProperty(Activator.SMSENABLE,"false"))){
				boolean sent=Activator.rcaller.sendRiskSMSText();
				Activator.routput.showSMSForm(user, sent);
			}
			OPublisher.responseWatch.cancel();
		}
	}
	
	public void showButtonManualForm(User user)
	{
		log.debug("Show manual panic button screen");
		Form f = Activator.gui.getPanicButtonForm();
		OutputEvent oe = new OutputEvent(user,f,LevelRating.high,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
	}
	
	private static void playWarning(){
		Calendar now=Calendar.getInstance();
		if(8<now.get(Calendar.HOUR_OF_DAY) && now.get(Calendar.HOUR_OF_DAY)<23)
		new Thread(){
			public void run(){
				InputStream audio=null;
				try {
					audio=new FileInputStream("panic.wav");
				} catch (Exception e) {
					try {
						audio=new FileInputStream("warn.wav");
					} catch (Exception ex) {
						log.error("Audio file not found (panic.wav or warn.wav in runner folder)");
					}
				}
				try {
					AudioStream as=new AudioStream((audio!=null)?audio:getClass().getClassLoader().getResourceAsStream("warn.wav"));
					AudioPlayer.player.start(as);
					Thread.sleep(3000);
					AudioPlayer.player.stop(as);
					as.close();
					as=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();	
		else
			log.warn("Too late to play a sound");
	}

	public void showTakeHomeForm(User user) {
		log.debug("Show take me home screen");
		Form f = Activator.gui.getTakeHomeForm();
		OutputEvent oe = new OutputEvent(user,f,LevelRating.high,Locale.ENGLISH,PrivacyLevel.insensible);
		Activator.rinput.subscribe(f.getDialogID());
		publish(oe);
	}
}
