/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research

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
package org.universAAL.samples.lighting.server_regular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.aapi.AapiServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;

/**
 * @author mtazari
 * @author mpsiuk
 *
 */
public class LightingProviderLevel2 extends ServiceCallee {

	// this is just to prepare a standard error message for later use
	private static final ServiceResponse invalidInput = new ServiceResponse(CallStatus.serviceSpecificFailure);
	static {
		invalidInput.addOutput(new ProcessOutput(ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));
	}

	// the ontologified server
	private MyLightingOntologified theServer;

	public LightingProviderLevel2(ModuleContext context) {
		// as a service providing component, we have to extend ServiceCallee
		// this in turn requires that we introduce which services we would like
		// to
		// provide to the universAAL-based Space
		super(context, ProvidedLightingServiceLevel2.profiles);

		// this is just an example that wraps a faked "original server"
		theServer = new MyLightingOntologified(LightingServerURIs.NAMESPACE);

	}

	public void communicationChannelBroken() {
	}

	public ServiceResponse handleCall(ServiceCall call) {
		if (call == null)
			return null;

		String operation = call.getProcessURI();
		if (operation == null)
			return null;

		if (operation.startsWith(LightingServerURIs.GetControlledLamps.URI))
			return getControlledLamps();

		LightSource input = (LightSource) call.getInputValue(LightingServerURIs.GetLampInfo.Input.LAMP_URI);
		if (input == null)
			return null;

		if (operation.startsWith(LightingServerURIs.GetLampInfo.URI))
			return getLampInfo(input);

		if (operation.startsWith(LightingServerURIs.TurnOff.URI))
			return turnOff(input);

		if (operation.startsWith(LightingServerURIs.TurnOn.URI))
			return turnOn(input);

		return null;
	}

	// create a service response that including all available light sources
	private ServiceResponse getControlledLamps() {
		AapiServiceResponse sr = new AapiServiceResponse(CallStatus.succeeded);
		List al = new ArrayList(Arrays.asList(theServer.getControlledLamps()));
		// allow output even if not specified in service profile
		sr.allowUnboundOutput();
		// create and add a ProcessOutput-Event that binds the output URI to the
		// created list of lamps
		sr.addOutput(new ProcessOutput(LightingServerURIs.GetControlledLamps.Output.CONTROLLED_LAMPS, al));
		return sr;
	}

	// create a service response with informations about the available lights
	private ServiceResponse getLampInfo(LightSource lamp) {
		try {
			AapiServiceResponse sr = new AapiServiceResponse(CallStatus.succeeded);
			Object lampInfo[] = theServer.getLampInfo(lamp);
			// allow output even if not specified in service profile
			sr.allowUnboundOutput();
			// create and add a ProcessOutput-Event that binds the output URI to
			// the state of the lamp
			sr.addOutput(new ProcessOutput(LightingServerURIs.GetLampInfo.Output.LAMP_BRIGHTNESS, lampInfo[0]));
			// create and add a ProcessOutput-Event that binds the output URI to
			// the location of the lamp
			sr.addOutput(new ProcessOutput(LightingServerURIs.GetLampInfo.Output.LAMP_LOCATION, lampInfo[1]));
			return sr;
		} catch (Exception e) {
			return invalidInput;
		}
	}

	// Simple use the turnOff method from the ProvidedLightingService
	private ServiceResponse turnOff(LightSource lamp) {
		try {
			theServer.turnOff(lamp);
			return new ServiceResponse(CallStatus.succeeded);
		} catch (Exception e) {
			return invalidInput;
		}
	}

	// Simple use the turnOn method from the ProvidedLightingService
	private ServiceResponse turnOn(LightSource lamp) {
		try {
			theServer.turnOn(lamp);
			return new ServiceResponse(CallStatus.succeeded);
		} catch (Exception e) {
			return invalidInput;
		}
	}
}
