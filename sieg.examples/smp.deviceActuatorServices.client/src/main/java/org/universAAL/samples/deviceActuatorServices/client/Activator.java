package org.universAAL.samples.deviceActuatorServices.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.deviceActuator.DeviceActuatorService;
import org.universAAL.ontology.deviceActuator.ElectricalDevice;

public class Activator implements BundleActivator {
	public static BundleContext context = null;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		// The ServiceCaller makes the .call (ServiceRequest)
		ServiceCaller sc = new DefaultServiceCaller(context);
		// the ServiceResponse gets response from a .call (ServiceRequest)
		ServiceResponse sr = sc.call(switchRequest(false));

		if (sr.getCallStatus() == CallStatus.succeeded) {
			System.out.println("SUCCESS!");
		}

	}

	// Services Requests
	// Create the ServiceRequest that matches the profile to turn off
	private static ServiceRequest switchRequest(Boolean status) {
		// Instatiate a ServiceRequest with the specific service-object
		ServiceRequest sr = new ServiceRequest(new DeviceActuatorService(),
				null);

		sr.addChangeEffect(
				new String[] { ElectricalDevice.PROP_DEVICE_STATUS }, status);

		return sr;

	}

	public void stop(BundleContext arg0) throws Exception {

	}

}
