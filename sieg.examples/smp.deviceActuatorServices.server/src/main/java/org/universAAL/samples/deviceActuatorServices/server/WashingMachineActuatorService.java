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
public class WashingMachineActuatorService extends DeviceActuatorService {

	// constructor
	private WashingMachineActuatorService(String uri) {
		super(uri);
	}

	// All the static Strings are used to unique identify special functions and
	// objects
	public static final String WASHINGMACHINEACTUATOR_SERVER_NAMESPACE = "http://ontology.universAAL.org/WashingMachineActuatorServiceServer.owl#";

	public static final String MY_URI = WASHINGMACHINEACTUATOR_SERVER_NAMESPACE
			+ "WashingMachineActuatorServer";
	static final String SERVICE_TURN_OFF_WASHINGMACHINE = WASHINGMACHINEACTUATOR_SERVER_NAMESPACE
			+ "turnOffWashingMachine";

	static final String OUTPUT_DEVICE_STATUS = WASHINGMACHINEACTUATOR_SERVER_NAMESPACE
			+ "deviceStatus";
	public static final String INPUT_DEVICE_STATUS = WASHINGMACHINEACTUATOR_SERVER_NAMESPACE
			+ "inDeviceStatus";

	// Instantiate the profiles
	static final ServiceProfile[] profiles = new ServiceProfile[1];
	private static Hashtable serverWashingMachineActuatorRestrictions = new Hashtable();

	static {
		// register of all classes in the ontology for the serialization of the
		// object
		register(WashingMachineActuatorService.class);

		// structures for property path definition
		String[] ppWashingMachineStatus = new String[] { ElectricalDevice.PROP_DEVICE_STATUS };

		// we restrict the values for the possible status of the devices
		// to only false and true (0,1)
		addRestriction(Restriction.getAllValuesRestrictionWithCardinality(
				ElectricalDevice.PROP_DEVICE_STATUS, new Enumeration(
						new Boolean[] { Boolean.FALSE, Boolean.TRUE }), 1, 1),
				ppWashingMachineStatus,
				serverWashingMachineActuatorRestrictions);

		// Create the turnOff profiles
		WashingMachineActuatorService turnOffWashingMachine = new WashingMachineActuatorService(
				SERVICE_TURN_OFF_WASHINGMACHINE);

		turnOffWashingMachine.myProfile.addChangeEffect(ppWashingMachineStatus,
				new Boolean(false));

		// The service profiles to be registered with the service bus
		profiles[0] = turnOffWashingMachine.myProfile;

		System.out.println("Added Service profiles turnOff WashingMachine:"
				+ profiles[0]);

	}

}
