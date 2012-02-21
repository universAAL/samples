package org.universAAL.ontology.deviceActuator;

import java.util.Hashtable;

import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.service.owl.Service;

/**
 * Service that handles different types of home devices.
 * 
 * @author joemoul
 * 
 */
public class DeviceActuatorService extends Service {
	public static final String MY_URI;
	public static final String PROP_CONTROLS;
	public static final String DEVICEACTUATOR_NAMESPACE = "http://ontology.univesAAL.org/DeviceActuatorService.owl#";;

	private static Hashtable deviceActuatorRestrictions = new Hashtable(2);
	static {

		MY_URI = DEVICEACTUATOR_NAMESPACE + "DeviceActuatorService";
		System.out.println("1:" + MY_URI);
		PROP_CONTROLS = DEVICEACTUATOR_NAMESPACE + "controls";
		System.out.println("2:" + PROP_CONTROLS);
		register(DeviceActuatorService.class);
	}

	public static Restriction getClassRestrictionsOnProperty(String propURI) {
		if (propURI == null)
			return null;
		Object r = deviceActuatorRestrictions.get(propURI);
		if (r instanceof Restriction)
			return (Restriction) r;
		return Service.getClassRestrictionsOnProperty(propURI);
	}

	public static String getRDFSComment() {
		return "The class of services controling home devices.";
	}

	public static String getRDFSLabel() {
		return "DeviceActuatorService";
	}

	public DeviceActuatorService() {
		super();
	}

	public DeviceActuatorService(String uri) {
		super(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.universAAL.ontology.Service#getClassLevelRestrictions()
	 */
	protected Hashtable getClassLevelRestrictions() {
		return deviceActuatorRestrictions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universAAL.middleware.owl.ManagedIndividual#getPropSerializationType
	 * (java.lang.String)
	 */
	public int getPropSerializationType(String propURI) {
		return PROP_CONTROLS.equals(propURI) ? PROP_SERIALIZATION_FULL : super
				.getPropSerializationType(propURI);
	}

	public boolean isWellFormed() {
		return true;
	}
}
