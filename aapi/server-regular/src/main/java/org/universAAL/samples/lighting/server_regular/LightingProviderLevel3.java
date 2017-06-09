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
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.location.indoor.Room;
import org.universAAL.samples.lighting.server_regular.unit_impl.MyLighting;

/**
 * @author mtazari
 * @author mpsiuk
 * 
 */
public class LightingProviderLevel3 extends ServiceCallee {

	// this is just to prepare a standard error message for later use
	private static final ServiceResponse invalidInput = new ServiceResponse(CallStatus.serviceSpecificFailure);
	static {
		invalidInput.addOutput(new ProcessOutput(ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Invalid input!"));
	}

	private static String constructLampURIfromLocalID(int localID) {
		return Activator.LAMP_URI_PREFIX + localID;
	}

	private static String constructLocationURIfromLocalID(String localID) {
		return Activator.LOCATION_URI_PREFIX + localID;
	}

	private static int extractLocalIDfromLampURI(String lampURI) {
		return Integer.parseInt(lampURI.substring(Activator.LAMP_URI_PREFIX.length()));
	}

	// the original server being here wrapped and bound to universAAL
	private MyLighting theServer;

	// needed for publishing context events (whenever you think that it might be
	// important to share a new info with other components in a universAAL-based
	// AAL SPace, you have to publish that info as a context event

	public LightingProviderLevel3(ModuleContext context) {
		// as a service providing component, we have to extend ServiceCallee
		// this in turn requires that we introduce which services we would like
		// to
		// provide to the universAAL-based AAL Space
		super(context, ProvidedLightingServiceLevel3.profiles);

		// this is just an example that wraps a faked "original server"
		theServer = new MyLighting();

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

		Object input = call.getInputValue(LightingServerURIs.GetLampInfo.Input.LAMP_URI);
		if (input == null)
			return null;

		if (operation.startsWith(LightingServerURIs.GetLampInfo.URI))
			return getLampInfo((Integer) input);

		if (operation.startsWith(LightingServerURIs.TurnOff.URI))
			return turnOff((Integer) input);

		if (operation.startsWith(LightingServerURIs.TurnOn.URI))
			return turnOn((Integer) input);

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
	private ServiceResponse getLampInfo(int lampID) {
		try {
			AapiServiceResponse sr = new AapiServiceResponse(CallStatus.succeeded);
			Object lampInfo[] = theServer.getLampInfo(lampID);
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
	private ServiceResponse turnOff(int lampID) {
		try {
			theServer.turnOff(lampID);
			return new ServiceResponse(CallStatus.succeeded);
		} catch (Exception e) {
			return invalidInput;
		}
	}

	// Simple use the turnOn method from the ProvidedLightingService
	private ServiceResponse turnOn(int lampID) {
		try {
			theServer.turnOn(lampID);
			return new ServiceResponse(CallStatus.succeeded);
		} catch (Exception e) {
			return invalidInput;
		}
	}
}
