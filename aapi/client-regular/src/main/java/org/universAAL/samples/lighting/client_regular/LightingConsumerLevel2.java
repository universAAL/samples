/*
	Copyright 2008-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer Gesellschaft - Institut fuer Graphische Datenverarbeitung

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
package org.universAAL.samples.lighting.client_regular;
/**
 *
 */

import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.aapi.AapiServiceRequest;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.lighting.simple.LightingSimplified;

/**
 * @author amarinc
 * @author mpsiuk
 *
 */
public class LightingConsumerLevel2 {

	private static ServiceCaller caller;

	LightingConsumerLevel2(ModuleContext context) {
		// the DefaultServiceCaller will be used to make ServiceRequest
		// (surprise ;-) )
		caller = new DefaultServiceCaller(context);

	}

	// *****************************************************************
	// Controller Methods
	// *****************************************************************

	public static List<LightSource> getControlledLamps() {
		// The service URI has to be provided in order to match the service.
		ServiceRequest getAllLampsRequest = new ServiceRequest(new Lighting(LightingServerURIs.GetControlledLamps.URI),
				null);

		ServiceResponse sr = caller.call(getAllLampsRequest);

		if (sr.getCallStatus() == CallStatus.succeeded) {
			return sr.getOutput(LightingServerURIs.GetControlledLamps.Output.CONTROLLED_LAMPS, true);
		}
		return null;
	}

	public static Object[] getLampInfo(LightSource lamp) {
		// The service URI has to be provided in order to match the service.
		AapiServiceRequest getLampInfo = new AapiServiceRequest(
				new LightingSimplified(LightingServerURIs.GetLampInfo.URI), null);
		getLampInfo.addInput(LightingServerURIs.GetLampInfo.Input.LAMP_URI, lamp);

		ServiceResponse sr = caller.call(getLampInfo);

		Object[] output = new Object[2];
		output[0] = sr.getOutput(LightingServerURIs.GetLampInfo.Output.LAMP_BRIGHTNESS, false).get(0);
		output[1] = sr.getOutput(LightingServerURIs.GetLampInfo.Output.LAMP_LOCATION, false).get(0);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return output;
		else {
			return null;
		}
	}

	public static boolean turnOn(LightSource lamp) {
		// The URI of service does no longer need to be provided. Now the
		// service will be matched through the change effect.
		AapiServiceRequest turnOnRequest = new AapiServiceRequest(new Lighting(), null);
		turnOnRequest.addInput(LightingServerURIs.TurnOn.Input.LAMP_URI, lamp);
		turnOnRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(100));

		ServiceResponse sr = caller.call(turnOnRequest);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			return false;
		}
	}

	public static boolean turnOff(LightSource lamp) {
		// The URI of service does no longer need to be provided. Now the
		// service will be matched through the change effect.
		AapiServiceRequest turnOffRequest = new AapiServiceRequest(new Lighting(), null);
		turnOffRequest.addInput(LightingServerURIs.TurnOn.Input.LAMP_URI, lamp);
		turnOffRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(0));

		ServiceResponse sr = caller.call(turnOffRequest);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			return false;
		}
	}

	public static boolean dimToValue(LightSource lamp, Integer percent) {
		// Here for purpose of matchmaking we are also relying on changeEffect
		AapiServiceRequest dimRequest = new AapiServiceRequest(new Lighting(), null);
		dimRequest.addInput(LightingServerURIs.TurnOn.Input.LAMP_URI, lamp);
		dimRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				percent);

		ServiceResponse sr = caller.call(dimRequest);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			return false;
		}
	}

	public void communicationChannelBroken() {
	}

}
