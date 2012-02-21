package org.universAAL.ontology;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author joemoul
 * 
 */
public class Activator implements BundleActivator {

	static BundleContext context = null;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Class.forName("org.universAAL.ontology.deviceActuator.DeviceActuatorService");
		Class.forName("org.universAAL.ontology.deviceActuator.ElectricalDevice");
		Class.forName("org.universAAL.ontology.deviceActuator.Oven");
		Class.forName("org.universAAL.ontology.deviceActuator.TV");
		Class.forName("org.universAAL.ontology.deviceActuator.AirConditioner");
		Class.forName("org.universAAL.ontology.deviceActuator.WashingMachine");
		Class.forName("org.universAAL.ontology.deviceActuator.StereoSystem");
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
