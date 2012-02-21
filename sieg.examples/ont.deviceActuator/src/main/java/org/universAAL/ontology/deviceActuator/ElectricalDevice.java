package org.universAAL.ontology.deviceActuator;

import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.phThing.Device;

/**
 * @author joemoul
 * 
 */
public class ElectricalDevice extends Device {

	public static final String MY_URI;
	public static final String PROP_DEVICE_LOCATION;
	public static final String PROP_DEVICE_STATUS;

	static {
		MY_URI = DeviceActuatorService.DEVICEACTUATOR_NAMESPACE
				+ "ElectricalDevice";
		PROP_DEVICE_LOCATION = DeviceActuatorService.DEVICEACTUATOR_NAMESPACE
				+ "deviceLocation";
		PROP_DEVICE_STATUS = DeviceActuatorService.DEVICEACTUATOR_NAMESPACE
				+ "deviceStatus";
	}

	public static Restriction getClassRestrictionsOnProperty(String propURI) {
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
	}

	public static String getRDFSComment() {
		return "The class of all ElectricalDevices.";
	}

	public static String getRDFSLabel() {
		return "ElectricalDevice";
	}

	public ElectricalDevice() {
		super();
	}

	public ElectricalDevice(String uri) {
		super(uri);
	}

	public ElectricalDevice(String uri, Location loc, Boolean state) {
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
