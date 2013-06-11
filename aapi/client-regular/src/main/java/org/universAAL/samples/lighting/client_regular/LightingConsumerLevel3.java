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
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.aapi.AapiServiceRequest;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.lighting.simple.LightingSimplified;

/**
 * @author amarinc
 * @author mpsiuk
 * 
 */
public class LightingConsumerLevel3 {

    private static ServiceCaller caller;

    LightingConsumerLevel3(ModuleContext context) {
	// the DefaultServiceCaller will be used to make ServiceRequest
	// (surprise ;-) )
	caller = new DefaultServiceCaller(context);

    }

    // *****************************************************************
    // Controller Methods
    // *****************************************************************

    // Get a list of all available light-source in the system
    public static List<Integer> getControlledLamps() {
	ServiceRequest getAllLampsRequest = new ServiceRequest(
		new LightingSimplified(
			LightingServerURIs.GetControlledLamps.URI), null);

	ServiceResponse sr = caller.call(getAllLampsRequest);

	if (sr.getCallStatus() == CallStatus.succeeded) {
	    return sr
		    .getOutput(
			    LightingServerURIs.GetControlledLamps.Output.CONTROLLED_LAMPS,
			    true);
	}
	return null;
    }

    public static boolean turnOn(int lampID) {
    	AapiServiceRequest turnOnRequest = new AapiServiceRequest(
		new LightingSimplified(LightingServerURIs.TurnOn.URI),
		null);
	turnOnRequest.addInput(LightingServerURIs.TurnOn.Input.LAMP_URI,
		lampID);

	ServiceResponse sr = caller.call(turnOnRequest);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return true;
	else {
	    return false;
	}
    }

    public static boolean turnOff(int lampID) {
    	AapiServiceRequest turnOffRequest = new AapiServiceRequest(
		new LightingSimplified(
			LightingServerURIs.TurnOff.URI), null);
	turnOffRequest.addInput(
		LightingServerURIs.TurnOn.Input.LAMP_URI, lampID);

	ServiceResponse sr = caller.call(turnOffRequest);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return true;
	else {
	    return false;
	}
    }

    public static Object[] getLampInfo(int lampID) {
    	AapiServiceRequest getLampInfo = new AapiServiceRequest(new LightingSimplified(
		LightingServerURIs.GetLampInfo.URI), null);
	getLampInfo.addInput(LightingServerURIs.GetLampInfo.Input.LAMP_URI,
		lampID);

	ServiceResponse sr = caller.call(getLampInfo);

	Object[] output = new Object[2];
	output[0] = sr.getOutput(
		LightingServerURIs.GetLampInfo.Output.LAMP_BRIGHTNESS, false)
		.get(0);
	output[1] = sr.getOutput(
		LightingServerURIs.GetLampInfo.Output.LAMP_LOCATION, false)
		.get(0);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return output;
	else {
	    return null;
	}
    }

    public void communicationChannelBroken() {
    }

}
