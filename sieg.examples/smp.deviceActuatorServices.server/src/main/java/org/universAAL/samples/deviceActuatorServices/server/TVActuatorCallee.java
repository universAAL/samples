package org.universAAL.samples.deviceActuatorServices.server;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;

/**
 * @author joemoul
 * 
 */
public class TVActuatorCallee extends ServiceCallee {

	static final String DEVICE_URI_PREFIX = TVActuatorService.TVACTUATOR_SERVER_NAMESPACE
			+ "controlledDevice";

	// ServiceResponse for communication failures
	private static final ServiceResponse invalidInput = new ServiceResponse(
			CallStatus.serviceSpecificFailure);
	static {
		invalidInput.addOutput(new ProcessOutput(
				ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));
	}

	protected TVActuatorCallee(BundleContext context,
			ServiceProfile[] realizedServices) {
		// The parent need to know the profiles of the available functions
		// to
		// register them
		super(context, realizedServices);
	}

	protected TVActuatorCallee(BundleContext context) {
		super(context, TVActuatorService.profiles);
	}

	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	@Override
	// This method handles a request
	public ServiceResponse handleCall(ServiceCall call) {
		if (call == null)
			return null;

		String operation = call.getProcessURI();
		if (operation == null)
			return null;

		if (operation.startsWith(TVActuatorService.SERVICE_TURN_OFF_TV)) {

			/*
			 * Object setStatus = call.
			 * getInputValue(TVActuatorProfile.INPUT_DEVICE_STATUS);
			 */
			return turnOffTV((Boolean) false);
		}
		return invalidInput;
	}

	// use the turnOff method from the TVActuatorService
	private ServiceResponse turnOffTV(boolean inputStatus) {

		try {
			ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
			System.out.println("1. TV status (TV callee): " + inputStatus);
			return sr;
		} catch (Exception e) {
			return invalidInput;
		}
	}

}
