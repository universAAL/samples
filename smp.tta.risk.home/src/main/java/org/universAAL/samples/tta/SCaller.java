package org.universAAL.samples.tta;

import java.util.Calendar;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;

/**
 *
 * @author alfiva
 */
public class SCaller {
	DefaultServiceCaller caller;
	public static final String RISK_MANAGEMENT_NAMESPACE= "http://ontology.persona.upm.es/RiskManagement.owl#";
	public static final String OUTPUT_VC = RISK_MANAGEMENT_NAMESPACE + "VCOutput";
	
	private final static Logger log=LoggerFactory.getLogger(SCaller.class);
	
    protected SCaller(BundleContext context) {
    	caller=new DefaultServiceCaller(context);
	}

    public boolean sendPanicButtonSMSText()
	{
    	log.debug("Calling sms service");
    	String txt=Activator.getProperties().getProperty(Activator.TEXT, "PERSONA SMS Alert. Contact relative.");
		String num=Activator.getProperties().getProperty(Activator.NUMBER, "123456789");
		Calendar now = Calendar.getInstance();
		ServiceResponse sr = caller.call(sendSMS(txt + "  ("
				+ now.get(Calendar.HOUR_OF_DAY) + ":"
				+ now.get(Calendar.MINUTE) + ")", num));
		return sr.getCallStatus() == CallStatus.succeeded;
	}	
    
    public boolean sendRiskSMSText()
	{
    	log.debug("Calling sms service");
    	String txt=Activator.getProperties().getProperty(Activator.RISKTEXT, "PERSONA SMS Alert. Contact relative.");
		String num=Activator.getProperties().getProperty(Activator.NUMBER, "123456789");
		Calendar now = Calendar.getInstance();
		ServiceResponse sr = caller.call(sendSMS(txt + "  ("
				+ now.get(Calendar.HOUR_OF_DAY) + ":"
				+ now.get(Calendar.MINUTE) + ")", num));
		return sr.getCallStatus()==CallStatus.succeeded;
	}
    
    private ServiceRequest sendSMS(String txt,String num){
    	ServiceRequest sendSMS = new ServiceRequest("http://ontology.aal-persona.org/tta.owl#FakeSMSService");
    	//TODO: SMS SERVICE
		return sendSMS;

    }

}
