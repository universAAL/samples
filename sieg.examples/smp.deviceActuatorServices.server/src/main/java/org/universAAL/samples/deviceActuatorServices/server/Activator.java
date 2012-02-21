package org.universAAL.samples.deviceActuatorServices.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.samples.deviceActuatorServices.server.OvenActuatorCallee;
import org.universAAL.samples.deviceActuatorServices.server.TVActuatorCallee;
import org.universAAL.samples.deviceActuatorServices.server.AirConditionerActuatorCallee;
import org.universAAL.samples.deviceActuatorServices.server.WashingMachineActuatorCallee;
import org.universAAL.samples.deviceActuatorServices.server.StereoSystemActuatorCallee;

/**
 * @author joemoul
 * 
 */
public class Activator implements BundleActivator {

	public static OvenActuatorCallee servceeOven = null;
	public static TVActuatorCallee servceeTV = null;
	public static AirConditionerActuatorCallee servceeAirConditioner = null;
	public static WashingMachineActuatorCallee servceeWashingMachine = null;
	public static StereoSystemActuatorCallee servceeStereoSystem = null;
	public static Logger logger = LoggerFactory.getLogger(Activator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(final BundleContext context) throws Exception {
		// Instantiate the ServiceCallees with the associated profiles
		servceeOven = new OvenActuatorCallee(context,
				OvenActuatorService.profiles);
		servceeTV = new TVActuatorCallee(context, TVActuatorService.profiles);
		servceeAirConditioner = new AirConditionerActuatorCallee(context,
				AirConditionerActuatorService.profiles);
		servceeWashingMachine = new WashingMachineActuatorCallee(context,
				WashingMachineActuatorService.profiles);
		servceeStereoSystem = new StereoSystemActuatorCallee(context,
				StereoSystemActuatorService.profiles);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		servceeOven.close();
		servceeTV.close();
		servceeAirConditioner.close();
		servceeWashingMachine.close();
		servceeStereoSystem.close();
	}
}
