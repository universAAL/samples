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
public class StereoSystemActuatorCallee extends ServiceCallee {

	static final String DEVICE_URI_PREFIX = StereoSystemActuatorService.STEREOSYSTEMACTUATOR_SERVER_NAMESPACE
			+ "controlledDevice";

	// ServiceResponse for communication failures
	private static final ServiceResponse invalidInput = new ServiceResponse(
			CallStatus.serviceSpecificFailure);
	static {
		invalidInput.addOutput(new ProcessOutput(
				ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));
	}

	protected StereoSystemActuatorCallee(BundleContext context,
			ServiceProfile[] realizedServices) {
		// The parent need to know the profiles of the available functions
		// to
		// register them
		super(context, realizedServices);
	}

	protected StereoSystemActuatorCallee(BundleContext context) {
		super(context, StereoSystemActuatorService.profiles);
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

		if (operation
				.startsWith(StereoSystemActuatorService.SERVICE_TURN_OFF_STEREOSYSTEM)) {

			/*
			 * Object setStatus = call
			 * .getInputValue(StereoSystemActuatorProfile.INPUT_DEVICE_STATUS);
			 */
			return turnOffStereoSystem((Boolean) false);
		}
		return invalidInput;
	}

	// use the turnOff method from the StereoSystemActuatorService
	private ServiceResponse turnOffStereoSystem(boolean inputStatus) {

		try {
			ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
			System.out.println("1. StereoSystem status(StereoSystem callee): "
					+ inputStatus);

			return sr;
		} catch (Exception e) {
			return invalidInput;
		}
	}

}
