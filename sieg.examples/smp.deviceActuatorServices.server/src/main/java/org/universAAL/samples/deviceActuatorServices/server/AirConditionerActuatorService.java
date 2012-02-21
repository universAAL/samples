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
public class AirConditionerActuatorService extends DeviceActuatorService {

	// constructor
	private AirConditionerActuatorService(String uri) {
		super(uri);
	}

	// All the static Strings are used to unique identify special functions and
	// objects
	public static final String AIRCONDITIONERACTUATOR_SERVER_NAMESPACE = "http://ontology.universAAL.org/AirConditionerActuatorServiceServer.owl#";

	public static final String MY_URI = AIRCONDITIONERACTUATOR_SERVER_NAMESPACE
			+ "AirConditionerActuatorServer";
	static final String SERVICE_TURN_OFF_AIRCONDITIONER = AIRCONDITIONERACTUATOR_SERVER_NAMESPACE
			+ "turnOffAirConditioner";

	static final String OUTPUT_DEVICE_STATUS = AIRCONDITIONERACTUATOR_SERVER_NAMESPACE
			+ "deviceStatus";
	public static final String INPUT_DEVICE_STATUS = AIRCONDITIONERACTUATOR_SERVER_NAMESPACE
			+ "inDeviceStatus";

	// Instantiate the profiles
	static final ServiceProfile[] profiles = new ServiceProfile[1];
	private static Hashtable serverAirConditionerActuatorRestrictions = new Hashtable();

	static {
		// register of all classes in the ontology for the serialization of the
		// object
		register(AirConditionerActuatorService.class);

		// structures for property path definition
		String[] ppAirConditionerStatus = new String[] { ElectricalDevice.PROP_DEVICE_STATUS };

		// we restrict the values for the possible status of the devices
		// to only false and true (0,1)
		addRestriction(Restriction.getAllValuesRestrictionWithCardinality(
				ElectricalDevice.PROP_DEVICE_STATUS, new Enumeration(
						new Boolean[] { Boolean.FALSE, Boolean.TRUE }), 1, 1),
				ppAirConditionerStatus,
				serverAirConditionerActuatorRestrictions);

		// Create the turnOff profile
		AirConditionerActuatorService turnOffAirConditioner = new AirConditionerActuatorService(
				SERVICE_TURN_OFF_AIRCONDITIONER);

		turnOffAirConditioner.myProfile.addChangeEffect(ppAirConditionerStatus,
				new Boolean(false));

		// The service profile to be registered with the service bus
		profiles[0] = turnOffAirConditioner.myProfile;

		System.out.println("Added Service profiles turnOff AirConditioner:"
				+ profiles[0]);

	}

}
