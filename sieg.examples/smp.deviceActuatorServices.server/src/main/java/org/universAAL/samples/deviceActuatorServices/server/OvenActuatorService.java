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
public class OvenActuatorService extends DeviceActuatorService {

	// constructor
	private OvenActuatorService(String uri) {
		super(uri);
	}

	// All the static Strings are used to unique identify special functions and
	// objects
	public static final String OVENACTUATOR_SERVER_NAMESPACE = "http://ontology.universAAL.org/OvenActuatorServiceServer.owl#";

	public static final String MY_URI = OVENACTUATOR_SERVER_NAMESPACE
			+ "OvenActuatorServer";
	static final String SERVICE_TURN_OFF_OVEN = OVENACTUATOR_SERVER_NAMESPACE
			+ "turnOffOven";
	static final String OUTPUT_DEVICE_STATUS = OVENACTUATOR_SERVER_NAMESPACE
			+ "deviceStatus";
	public static final String INPUT_DEVICE_STATUS = OVENACTUATOR_SERVER_NAMESPACE
			+ "inDeviceStatus";

	// Instantiate the profiles
	static final ServiceProfile[] profiles = new ServiceProfile[1];
	private static Hashtable serverOvenActuatorRestrictions = new Hashtable();

	static {
		// register of all classes in the ontology for the serialization of the
		// object
		register(OvenActuatorService.class);

		// structures for property path definition
		String[] ppOvenStatus = new String[] { ElectricalDevice.PROP_DEVICE_STATUS };

		// we restrict the values for the possible status of the devices
		// to only false and true (0,1)
		addRestriction(Restriction.getAllValuesRestrictionWithCardinality(
				ElectricalDevice.PROP_DEVICE_STATUS, new Enumeration(
						new Boolean[] { Boolean.FALSE, Boolean.TRUE }), 1, 1),
				ppOvenStatus, serverOvenActuatorRestrictions);

		// Create the turnOff profiles
		OvenActuatorService turnOffOven = new OvenActuatorService(
				SERVICE_TURN_OFF_OVEN);

		turnOffOven.myProfile.addChangeEffect(ppOvenStatus, new Boolean(false));

		// The service profile to be registered with the service bus
		profiles[0] = turnOffOven.myProfile;

		System.out
				.println("Added Service profiles turnOff Oven:" + profiles[0]);

	}

}
