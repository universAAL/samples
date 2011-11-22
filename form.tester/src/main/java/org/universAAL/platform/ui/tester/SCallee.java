package org.universAAL.platform.ui.tester;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.profile.User;

public class SCallee extends ServiceCallee {
	private static final ServiceResponse failure = new ServiceResponse(
			CallStatus.serviceSpecificFailure);
	private final static Logger log = LoggerFactory.getLogger(SCallee.class);

	public SCallee(BundleContext context) {
		super(context, getProfiles());
	}

	protected SCallee(BundleContext context, ServiceProfile[] realizedServices) {
		super(context, realizedServices);
		log.debug("Registered the SCallee");
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	public ServiceResponse handleCall(ServiceCall call) {
		log.debug("Received call");
		User user = null;
		if (call == null) {
			failure
					.addOutput(new ProcessOutput(
							ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
							"Null call!?!"));
			log.error("Null call");
			return failure;
		}

		String operation = call.getProcessURI();
		if (operation == null) {
			failure.addOutput(new ProcessOutput(
					ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
					"Null operation!?!"));
			log.error("Null op");
			return failure;
		}

		if (operation
				.startsWith("http://ontology.aal-persona.org/Tests.owl#startUI")) {
			log.debug("Start UI op");
			Object inputUser = call
					.getProperty(ServiceRequest.PROP_uAAL_INVOLVED_HUMAN_USER);
			if ((inputUser == null) || !(inputUser instanceof User)) {
				failure.addOutput(new ProcessOutput(
						ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
						"Invalid User Input!"));
				log.debug("No user");
				return failure;
			} else {
				user = (User) inputUser;
			}
			log.debug("Show dialog from call");
			Activator.noutput.showTestDialog(user);
			ServiceResponse response = new ServiceResponse(CallStatus.succeeded);
			return response;
		}
		log.debug("finished");
		return null;
	}

	static ServiceProfile[] getProfiles() {
		ServiceProfile prof = InitialServiceDialog.createInitialDialogProfile(
				"http://ontology.aal-persona.org/Tests.owl#Forms",
				"http://www.tsb.upv.es", "User Interface Forms tests",
				"http://ontology.aal-persona.org/Tests.owl#startUI");
		return new ServiceProfile[] { prof };
	}
}
