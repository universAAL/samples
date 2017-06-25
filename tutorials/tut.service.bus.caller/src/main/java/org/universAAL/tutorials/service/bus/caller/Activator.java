/*
    Copyright 2016-2020 Carsten Stockloew

    See the NOTICE file distributed with this work for additional
    information regarding copyright ownership

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.universAAL.tutorials.service.bus.caller;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.phThing.DeviceService;

/**
 * The module activator handles the starting and stopping of this module. It is
 * similar to the OSGi BundleActivator.
 *
 * In our case, we simply want to call a service, so we do this directly in the
 * start method.
 *
 * @author Carsten Stockloew
 */
public class Activator implements ModuleActivator {

	public void start(ModuleContext mc) throws Exception {
		// Create the service caller
		ServiceCaller caller = new DefaultServiceCaller(mc);

		// Create a service request to turn on a light source. The requested
		// service (a subclass of 'Service', here: DeviceService) acts as 'entry
		// point' in the ontology. Starting from this class we go along some
		// property path and describe what the service should do/return and
		// what input parameters we provide at that path.

		// In this tutorial, we request a service that can turn on a light
		// source (= setting the value of a LightActuator to 100%)
		ServiceRequest turnOn = new ServiceRequest(new DeviceService(), null);

		// We add a value filter: at the path 'controls' the value must be a
		// specific light actuator. All other operations (e.g. change effects or
		// return values) only operate on those filtered instances.
		turnOn.addValueFilter(new String[] { DeviceService.PROP_CONTROLS },
				new LightActuator("urn:org.universAAL.space:KitchenLight"));

		// We add a change effect: at the path 'controls-hasValue' the service
		// should change the value to 100
		turnOn.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, ValueDevice.PROP_HAS_VALUE },
				new Integer(100));

		// Now call the service
		caller.call(turnOn);

		// Close our service caller and free all resources. The caller
		// should be re-used if multiple calls need to be made.
		caller.close();
	}

	public void stop(ModuleContext arg0) throws Exception {
	}
}
