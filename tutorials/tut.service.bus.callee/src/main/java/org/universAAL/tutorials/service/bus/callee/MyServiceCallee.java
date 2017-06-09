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
package org.universAAL.tutorials.service.bus.callee;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.phThing.DeviceService;

/**
 * Our tutorial application registers as a subscriber for certain service
 * profiles at the service bus. The method {@link #handleCall(ServiceCall)} is
 * our callback method.
 * 
 * @author Carsten Stockloew
 */
public class MyServiceCallee extends ServiceCallee {

	// URI for the service
	public static String SERVICE_TURN_ON = "urn:org.universAAL.tutorial:tut.callee#srvTurnOn";

	// URI for an input parameter: we expect the URI of a LightActuator as a
	// parameter identified by this URI
	public static String INPUT_LIGHT_URI = "urn:org.universAAL.tutorial:tut.callee#inLampURI";

	/**
	 * Create the service profile that describes what the service does.
	 * 
	 * @return an array of service profiles.
	 */
	public static ServiceProfile[] getProfiles() {
		// Create a device service. Subclasses of the class 'Service' are our
		// 'entry points' in the ontology. Starting from this class we go along
		// some property path and describe what we do, return, or expect at that
		// path.
		// A service can
		// - do something: add, change, or remove a property
		// - return something
		// - expect something: an input parameter that the caller has to provide

		// In this tutorial, we create a service that can turn on a light
		// source (= setting the value of a LightActuator to 100%)
		Service turnOn = new DeviceService(SERVICE_TURN_ON);

		// We add a filtering input: at the path 'controls' we expect exactly
		// one parameter of type LightActuator. When the service is called, we
		// can query this parameter with the URI 'INPUT_LIGHT_URI'
		turnOn.addFilteringInput(INPUT_LIGHT_URI, LightActuator.MY_URI, 1, 1,
				new String[] { DeviceService.PROP_CONTROLS });
		// We add a change effect: at the path 'controls-hasValue' the service
		// can change the value to 100
		turnOn.getProfile().addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, ValueDevice.PROP_HAS_VALUE },
				new Integer(100));

		// We can create more profiles here and return everything in an array.
		// Please note that there is a difference between
		// - registering one calle with multiple profiles, and
		// - registering multiple callees, each with one profile
		// See also the section on service bus details in the Wiki
		return new ServiceProfile[] { turnOn.getProfile() };
	}

	public MyServiceCallee(ModuleContext context) {
		// The constructor register us to the service bus for some profiles
		super(context, getProfiles());
	}

	/** @see ServiceCallee#handleCall(ServiceCall) */
	public ServiceResponse handleCall(ServiceCall call) {
		// If we have registered multiple profiles, we can distinguish the
		// service by comparing the service URI with the process URI
		String operation = call.getProcessURI();
		if (operation.startsWith(SERVICE_TURN_ON)) {
			// Get the parameter
			Object input = call.getInputValue(INPUT_LIGHT_URI);

			// In this example, we simply log the call and its input parameter
			LogUtils.logDebug(Activator.mc, MyServiceCallee.class, "handleCall",
					"Received service call for service 'turn on' with parameter " + input);
			return new ServiceResponse(CallStatus.succeeded);
		}

		return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}

	/** @see ServiceCallee#communicationChannelBroken() */
	public void communicationChannelBroken() {
		// This method is called when the connection to the service bus is
		// broken. We don't do anything here, but applications should handle
		// this case.
	}
}
