package org.universAAL.samples.tta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.MediaObject;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.io.rdf.Submit;

public class RiskGUI{
	public static final String PERSONA_INPUT_NAMESPACE = "http://ontology.aal-persona.org/Input.owl#"; //$NON-NLS-1$
	public static final String SUBMIT_HOME = PERSONA_INPUT_NAMESPACE + "home"; //$NON-NLS-1$
	public static final String SUBMIT_CANCEL = PERSONA_INPUT_NAMESPACE + "cancel"; //$NON-NLS-1$
	public static final String SUBMIT_MANUAL = PERSONA_INPUT_NAMESPACE + "manual"; //$NON-NLS-1$
	public static final String SUBMIT_TAKE = PERSONA_INPUT_NAMESPACE + "take"; //$NON-NLS-1$
	public static final String BUTTON_TITLE = Messages.getString("RiskGUI.1"); //$NON-NLS-1$
	public static final String SMS_TITLE = Messages.getString("RiskGUI.2"); //$NON-NLS-1$
	public static final String HOME_SUBMIT = Messages.getString("RiskGUI.3"); //$NON-NLS-1$
	public static final String SMS_IMG_LABEL = Messages.getString("RiskGUI.4"); //$NON-NLS-1$
	public static final String SMS_TEXT = Messages.getString("RiskGUI.5"); //$NON-NLS-1$
	public static final String SMS_LABEL = Messages.getString("RiskGUI.6"); //$NON-NLS-1$
	public static final String BUTTON_LABEL = Messages.getString("RiskGUI.7"); //$NON-NLS-1$
	public static final String SMS_NO_TEXT = Messages.getString("RiskGUI.8"); //$NON-NLS-1$
	public static final String VC_TITLE = Messages.getString("RiskGUI.9"); //$NON-NLS-1$
	public static final String VC_NO_TEXT = Messages.getString("RiskGUI.10"); //$NON-NLS-1$
	public static final String BATTERY_TEXT = Messages.getString("RiskGUI.11"); //$NON-NLS-1$
	public static final String BATTERY_TITLE = Messages.getString("RiskGUI.12"); //$NON-NLS-1$
	public static final String BUTTON_MANUAL_TITLE = Messages.getString("RiskGUI.13"); //$NON-NLS-1$
	public static final String BUTTON_MANUAL_LABEL = Messages.getString("RiskGUI.14"); //$NON-NLS-1$
	public static final String BUTTON_TEXT = Messages.getString("RiskGUI.15"); //$NON-NLS-1$
	public static final String TAKE_HOME_TITLE = Messages.getString("RiskGUI.16"); //$NON-NLS-1$
	public static final String TAKE_HOME_LABEL = Messages.getString("RiskGUI.17"); //$NON-NLS-1$
	public static final String TAKE_HOME_TEXT = Messages.getString("RiskGUI.18"); //$NON-NLS-1$

	private static final String imgroot="android.handler/"; //$NON-NLS-1$ //$NON-NLS-2$
	
	private final static Logger log=LoggerFactory.getLogger(RiskGUI.class);
	
	public Form getUserStateForm(){
		log.debug("Generating abort button form");
		Form f = Form.newDialog(BUTTON_TITLE, (String)null);
		Group controls = f.getIOControls();
		new SimpleOutput(controls,null,null,BUTTON_TEXT);
		Label labelBoton = new Label(BUTTON_LABEL,imgroot + "ShinyButtonGreen5.jpg");
		new Submit(controls,labelBoton,SUBMIT_CANCEL);
		
		return f;
	}
	
	public Form getSMSForm(boolean sent){
		log.debug("Generating sms form");
		Form f = Form.newDialog(SMS_TITLE, (String)null);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();
		
		new Submit(submits, new Label(HOME_SUBMIT,(String)null), SUBMIT_HOME);
		
		new MediaObject(controls,new Label(SMS_IMG_LABEL,imgroot + (sent?"enviarSMS.jpg":"smsNoEnviado.gif")),"image",imgroot + (sent?"enviarSMS.jpg":"smsNoEnviado.gif"));
		new SimpleOutput(controls,new Label("",(String)null), null, sent?SMS_TEXT:SMS_NO_TEXT);
		
		return f;
	}

	public Form getNoVCForm() {
		log.debug("Generating vc failed form");
		Form f=Form.newMessage(VC_TITLE, VC_NO_TEXT);
		return f;
	}
	
	public Form getBatteryForm() {
		log.debug("Generating battery form");
		Form f=Form.newMessage(BATTERY_TITLE, BATTERY_TEXT);
		return f;
	}

	public Form getPanicButtonForm() {
		log.debug("Generating panic button form");
		Form f = Form.newDialog(BUTTON_MANUAL_TITLE, (String)null);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();
		Label labelBoton = new Label(BUTTON_MANUAL_LABEL,null);
		new Submit(controls,labelBoton,SUBMIT_MANUAL);
		new Submit(submits, new Label(HOME_SUBMIT,(String)null), SUBMIT_HOME);
		return f;
	}

	public Form getTakeHomeForm() {
		log.debug("Generating take home form");
		Form f = Form.newDialog(TAKE_HOME_TITLE, (String)null);
		Group controls = f.getIOControls();
		Group submits = f.getSubmits();
		new SimpleOutput(controls,null,null,TAKE_HOME_TEXT);
		Label labelBoton = new Label(TAKE_HOME_LABEL,null);
		new Submit(controls,labelBoton,SUBMIT_TAKE);
		new Submit(submits, new Label(HOME_SUBMIT,(String)null), SUBMIT_HOME);
		return f;
	}
}