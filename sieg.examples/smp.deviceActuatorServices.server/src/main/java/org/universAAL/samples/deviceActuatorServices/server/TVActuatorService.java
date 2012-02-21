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
public class TVActuatorService extends DeviceActuatorService {

	// constructor
	private TVActuatorService(String uri) {
		super(uri);
	}

	// All the static Strings are used to unique identify special functions and
	// objects
	public static final String TVACTUATOR_SERVER_NAMESPACE = "http://ontology.universAAL.org/TVActuatorServiceServer.owl#";

	public static final String MY_URI = TVACTUATOR_SERVER_NAMESPACE
			+ "TVActuatorServer";
	static final String SERVICE_TURN_OFF_TV = TVACTUATOR_SERVER_NAMESPACE
			+ "turnOffTV";

	static final String OUTPUT_DEVICE_STATUS = TVACTUATOR_SERVER_NAMESPACE
			+ "deviceStatus";
	public static final String INPUT_DEVICE_STATUS = TVACTUATOR_SERVER_NAMESPACE
			+ "inDeviceStatus";

	// Instantiate the profiles
	static final ServiceProfile[] profiles = new ServiceProfile[1];
	private static Hashtable serverTVActuatorRestrictions = new Hashtable();

	static {
		// register of all classes in the ontology for the serialization of the
		// object
		register(TVActuatorService.class);

		// structures for property path definition
		String[] ppTVStatus = new String[] { ElectricalDevice.PROP_DEVICE_STATUS };

		// we restrict the values for the possible status of the devices
		// to only false and true (0,1)
		addRestriction(Restriction.getAllValuesRestrictionWithCardinality(
				ElectricalDevice.PROP_DEVICE_STATUS, new Enumeration(
						new Boolean[] { Boolean.FALSE, Boolean.TRUE }), 1, 1),
				ppTVStatus, serverTVActuatorRestrictions);

		// Create the turnOff profile
		TVActuatorService turnOffTV = new TVActuatorService(SERVICE_TURN_OFF_TV);

		turnOffTV.myProfile.addChangeEffect(ppTVStatus, new Boolean(false));

		// The service profiles to be registered with the service bus
		profiles[0] = turnOffTV.myProfile;

		System.out.println("Added Service profiles turnOff TV:" + profiles[0]);

	}

}
