package org.universAAL.samples.deviceActuatorServices.server;

import java.util.Hashtable;

import org.universAAL.middleware.owl.Enumeration;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.deviceActuator.DeviceActuatorService;
import org.universAAL.ontology.deviceActuator.ElectricalDevice;

/**
 * @author joemoul
 * 
 */
public class StereoSystemActuatorService extends DeviceActuatorService {

	// constructor
	private StereoSystemActuatorService(String uri) {
		super(uri);
	}

	// All the static Strings are used to unique identify special functions and
	// objects
	public static final String STEREOSYSTEMACTUATOR_SERVER_NAMESPACE = "http://ontology.universAAL.org/StereoSystemActuatorServiceServer.owl#";

	public static final String MY_URI = STEREOSYSTEMACTUATOR_SERVER_NAMESPACE
			+ "StereoSystemActuatorServer";
	static final String SERVICE_TURN_OFF_STEREOSYSTEM = STEREOSYSTEMACTUATOR_SERVER_NAMESPACE
			+ "turnOffStereoSystem";

	static final String OUTPUT_DEVICE_STATUS = STEREOSYSTEMACTUATOR_SERVER_NAMESPACE
			+ "deviceStatus";
	public static final String INPUT_DEVICE_STATUS = STEREOSYSTEMACTUATOR_SERVER_NAMESPACE
			+ "inDeviceStatus";

	// Instantiate the profiles
	static final ServiceProfile[] profiles = new ServiceProfile[1];
	private static Hashtable serverStereoSystemActuatorRestrictions = new Hashtable();

	static {
		// register of all classes in the ontology for the serialization of the
		// object
		register(StereoSystemActuatorService.class);

		// structures for property path definition
		String[] ppStereoSystemStatus = new String[] { ElectricalDevice.PROP_DEVICE_STATUS };

		// we restrict the values for the possible status of the devices
		// to only false and true (0,1)
		addRestriction(Restriction.getAllValuesRestrictionWithCardinality(
				ElectricalDevice.PROP_DEVICE_STATUS, new Enumeration(
						new Boolean[] { Boolean.FALSE, Boolean.TRUE }), 1, 1),
				ppStereoSystemStatus, serverStereoSystemActuatorRestrictions);

		// Create the turnOff profiles
		StereoSystemActuatorService turnOffStereoSystem = new StereoSystemActuatorService(
				SERVICE_TURN_OFF_STEREOSYSTEM);

		turnOffStereoSystem.myProfile.addChangeEffect(ppStereoSystemStatus,
				new Boolean(false));

		// The service profile to be registered with the service bus
		profiles[0] = turnOffStereoSystem.myProfile;

		System.out.println("Added Service profiles turnOff StereoSystem:"
				+ profiles[0]);

	}

}
