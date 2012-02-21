package org.universAAL.ontology.deviceActuator;

import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.location.Location;

/**
 * @author joemoul
 * 
 */
public class AirConditioner extends ElectricalDevice {
	public static final String DEVICEACTUATOR_NAMESPACE = "http://ontology.univesAAL.org/AirConditioner.owl#";
	public static final String MY_URI;
	public static final String PROP_TEMPERATURE;
	public static final String PROP_DEVICE_LOCATION;
	public static final String PROP_DEVICE_STATUS;

	static {
		MY_URI = DEVICEACTUATOR_NAMESPACE + "AirConditioner";
		PROP_TEMPERATURE = DEVICEACTUATOR_NAMESPACE + "temperature";
		PROP_DEVICE_LOCATION = DEVICEACTUATOR_NAMESPACE + "deviceLocation";
		PROP_DEVICE_STATUS = DEVICEACTUATOR_NAMESPACE + "deviceStatus";
	}

	public static Restriction getClassRestrictionsOnProperty(String propURI) {
		if (PROP_TEMPERATURE.equals(propURI))
			return Restriction.getCardinalityRestriction(propURI, 1, 1);
		if (PROP_DEVICE_LOCATION.equals(propURI))
			return Restriction.getAllValuesRestrictionWithCardinality(propURI,
					Location.MY_URI, 1, 1);
		if (PROP_DEVICE_STATUS.equals(propURI))
			return Restriction.getAllValuesRestrictionWithCardinality(propURI,
					TypeMapper.getDatatypeURI(Boolean.class), 1, 1);
		return ManagedIndividual.getClassRestrictionsOnProperty(propURI);
	}

	public static String[] getStandardPropertyURIs() {
		return new String[] { PROP_DEVICE_LOCATION, PROP_DEVICE_STATUS };
		// return new String[] {PROP_TEMPERATURE, PROP_DEVICE_LOCATION,
		// PROP_DEVICE_STATUS};
	}

	public static String getRDFSComment() {
		return "The class of all Air Conditioners.";
	}

	public static String getRDFSLabel() {
		return "Air Conditioner";
	}

	public AirConditioner() {
		super();
	}

	public AirConditioner(String uri) {
		super(uri);
	}

	public AirConditioner(String uri, Location loc, Boolean state) {
		super(uri);
		if (loc == null || state == null)
			throw new IllegalArgumentException();

	}

	public boolean getStatus() {
		return ((Boolean) props.get(PROP_DEVICE_STATUS)).booleanValue();
	}

	public Location getDeviceLocation() {
		return (Location) props.get(PROP_DEVICE_LOCATION);
	}

	public void setStatus(Boolean state) {
		if (state != null)
			props.put(PROP_DEVICE_STATUS, new Boolean(state));
	}

	public void setDeviceLocation(Location l) {
		if (l != null)
			props.put(PROP_DEVICE_LOCATION, l);
	}

	public int getPropSerializationType(String propURI) {
		return (PROP_DEVICE_LOCATION.equals(propURI)) ? PROP_SERIALIZATION_REDUCED
				: PROP_SERIALIZATION_FULL;
	}

	public boolean isWellFormed() {
		return props.containsKey(PROP_DEVICE_STATUS)
				&& props.containsKey(PROP_DEVICE_LOCATION);
	}

}
