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
package org.universAAL.tutorials.service.bus.tryout;

import org.universAAL.middleware.container.ModuleContext;
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
 * An experimental callee. Run this tutorial as JUnit test (for a fast startup)
 * and set the request in MyTest.
 *
 * @author Carsten Stockloew
 */
public class MyServiceCallee extends ServiceCallee {

	public static String SERVICE_TURN_ON = "urn:org.universAAL.tutorial:tut.callee#srvTurnOn";
	public static String INPUT_LIGHT_URI = "urn:org.universAAL.tutorial:tut.callee#inLampURI";

	/**
	 * Create the service profile that describes what the service does.
	 *
	 * @return an array of service profiles.
	 */
	public static ServiceProfile[] getProfiles() {
		Service turnOn = new DeviceService(SERVICE_TURN_ON);
		turnOn.addFilteringInput(INPUT_LIGHT_URI, LightActuator.MY_URI, 1, 1,
				new String[] { DeviceService.PROP_CONTROLS });
		turnOn.getProfile().addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, ValueDevice.PROP_HAS_VALUE },
				new Integer(100));

		return new ServiceProfile[] { turnOn.getProfile() };
	}

	public MyServiceCallee(ModuleContext context) {
		super(context, getProfiles());
	}

	/** @see ServiceCallee#handleCall(ServiceCall) */
	public ServiceResponse handleCall(ServiceCall call) {
		String operation = call.getProcessURI();
		if (operation.startsWith(SERVICE_TURN_ON)) {
			Object input = call.getInputValue(INPUT_LIGHT_URI);
			System.out.println("Received service call for service 'turn on' with parameter " + input);
			return new ServiceResponse(CallStatus.succeeded);
		}

		return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}

	/** @see ServiceCallee#communicationChannelBroken() */
	public void communicationChannelBroken() {
	}
}
