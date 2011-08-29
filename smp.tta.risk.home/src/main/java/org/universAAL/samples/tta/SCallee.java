package org.universAAL.samples.tta;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.profile.User;


public class SCallee extends ServiceCallee{
	private static final ServiceResponse failure = new ServiceResponse(
			CallStatus.serviceSpecificFailure);
	private final static Logger log=LoggerFactory.getLogger(SCallee.class);

	public SCallee(BundleContext context){
		super(context, getProfiles());
	}
	
	protected SCallee(BundleContext context, ServiceProfile[] realizedServices) {
		super(context, realizedServices);
		log.debug("Registered the SCallee");
	}

	public void communicationChannelBroken() {
		
	}

	public ServiceResponse handleCall(ServiceCall call) {
		log.debug("Received call");
		User user=null;
		if (call == null){
			failure.addOutput(new ProcessOutput(ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,"Null call!?!"));
			log.error("Null call");
			return failure;
		}
		
		String operation = call.getProcessURI();
		if (operation == null){
			failure.addOutput(new ProcessOutput(ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,"Null operation!?!"));
			log.error("Null op");
			return failure;
		}
		
		if (operation.startsWith("http://ontology.aal-persona.org/tta.owl#startUI")){
			log.debug("Start UI op");
			Object inputUser = call.getInvolvedUser(); 
			if ((inputUser == null)||!(inputUser instanceof User)){
				failure.addOutput(new ProcessOutput(ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,"Invalid User Input!"));
				log.debug("No user");
				return failure;
			}else{
				user=(User)inputUser;
			}
			log.debug("Show dialog from call");
			ServiceResponse response=failure;
			if (operation.startsWith("http://ontology.aal-persona.org/tta.owl#startUIpanic")){
				Activator.routput.showButtonManualForm(user);
				response= new ServiceResponse(CallStatus.succeeded);
			}else if (operation.startsWith("http://ontology.aal-persona.org/tta.owl#startUIhome")){
				Activator.routput.showTakeHomeForm(user);
				response= new ServiceResponse(CallStatus.succeeded);
			}
			return response;
		}
		log.debug("finished");
		return null;
	}

	static ServiceProfile[] getProfiles() {
		ServiceProfile prof = InitialServiceDialog.createInitialDialogProfile(
				"http://ontology.aal-persona.org/tta.owl#Panic",
				"http://www.tsb.upv.es", "Panic Button",
				"http://ontology.aal-persona.org/tta.owl#startUIpanic");
		ServiceProfile prof2 = InitialServiceDialog.createInitialDialogProfile(
				"http://ontology.aal-persona.org/tta.owl#TakeHome",
				"http://www.tsb.upv.es", "Take me Home",
				"http://ontology.aal-persona.org/tta.owl#startUIhome");
		return new ServiceProfile[]{prof,prof2};
	}




}
