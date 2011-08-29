package org.universAAL.samples.tta;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.util.Constants;

/**
 *
 * @author alfiva
 */
public class Activator implements BundleActivator{
	public static BundleContext context=null;
	
    public static SCaller rcaller=null;
    public static SCallee rcallee=null;
    public static ISubscriber rinput=null;
	public static OPublisher routput=null;
    public static CSubscriber csubscriber=null;
    public static RiskGUI gui=null;

	public static final String PROPS_FILE="tta.risk.properties";
	private static File confHome = new File(new File(Constants.getSpaceConfRoot()), "smp.tta.risk");
	public static final String TEXT="SMS.text";
	public static final String RISKTEXT="SMS.risk";
	public static final String NUMBER="SMS.number";
	public static final String SMSENABLE="SMS.enabled";
	public static final String DELAY="RISK.delay";
	public static final String DEFAULT="RISK.Room@Default";
	public static final String RISKENABLE="RISK.enabled";
	public static final String GPSTO="TTA.destination";
	protected static Properties properties=new Properties();

	public static final String COMMENTS="This file stores persistence info for the Risk Manager stub. Times in minutes. \n" +
			"To set a risk situation timer for a room at a specific time, use the following: \n" +
			"RISK.Room@<URISuffixOfTheRoom>=00:<TimerMinutes>,<StartingHourOfPeriod>:<TimerMinutes>,... \n" +
			"Example: RISK.Room@Bathroom=00:60,06:150,12:60";
	
	private final static Logger log=LoggerFactory.getLogger(Activator.class);

	public void start(BundleContext context) throws Exception {
		log.info("Starting Risk manager stub bundle");
		properties=loadProperties();
		Activator.context=context;
		rcaller=new SCaller(context);
		rcallee=new SCallee(context);
		gui=new RiskGUI();
		rinput=new ISubscriber(context);
		routput=new OPublisher(context);
		csubscriber=new CSubscriber(context);
		log.info("Started Risk manager stub bundle");
	}

	public void stop(BundleContext context) throws Exception {
		log.info("Stopping riskstub bundle");
		rcallee.close();
		rinput.close();
		routput.close();
		csubscriber.close();
		log.info("Stopped riskstub Advisor bundle");
	}
	
	private static synchronized void setProperties(Properties prop){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(confHome, PROPS_FILE));
	    	BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			prop.store(bufferedOutputStream, COMMENTS);
			bufferedOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			log.error("Could not set properties file: {}",e);
		}
	}
	
	private static synchronized Properties loadProperties(){
		Properties prop=new Properties();
		try {
			prop=new Properties();
			InputStream in = new FileInputStream(new File(confHome, PROPS_FILE));
			prop.load(in);
			in.close();
		} catch (java.io.FileNotFoundException e) {
			log.warn("Properties file does not exist; generating default...");

			prop.setProperty(TEXT,"Panic button pressed");
			prop.setProperty(RISKTEXT,"Risk situation detected");
			prop.setProperty(NUMBER,"123456789");
			prop.setProperty(SMSENABLE,"false");
			
			prop.setProperty(DELAY,"1");
			prop.setProperty(DEFAULT,"00:0");
			prop.setProperty(RISKENABLE,"false");
			
			prop.setProperty(GPSTO,"Rue Wiertz 60, 1047 Bruxelles, Belgique");
			setProperties(prop);
		}catch (Exception e) {
			log.error("Could not access properties file: {}",e);
		}
		return prop;
	}
	
	public static synchronized Properties getProperties(){
		return properties;
	}

}
