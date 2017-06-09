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

import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.lighting.simple.LightingSimplified;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.phThing.PhysicalThing;

public class LightingConsumerLevel1 {

	private static ServiceCaller caller;

	private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";

	private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE + "controlledLamps";

	private static final String OUTPUT_LOCATION = LIGHTING_CONSUMER_NAMESPACE + "location";

	private static final String OUTPUT_BRIGHTNESS = LIGHTING_CONSUMER_NAMESPACE + "brightness";

	LightingConsumerLevel1(ModuleContext context) {
		caller = new DefaultServiceCaller(context);
	}

	// *****************************************************************
	// Controller Methods
	// *****************************************************************

	public static List<LightSource> getControlledLamps() {
		ServiceRequest getAllLampsRequest = new ServiceRequest(new Lighting(), null);
		getAllLampsRequest.addRequiredOutput(OUTPUT_LIST_OF_LAMPS, new String[] { Lighting.PROP_CONTROLS });

		ServiceResponse sr = caller.call(getAllLampsRequest);

		if (sr.getCallStatus() == CallStatus.succeeded) {
			return sr.getOutput(OUTPUT_LIST_OF_LAMPS, true);
		}
		return null;
	}

	public static Object[] getLampInfo(String lampURI) {
		ServiceRequest getLampInfo = new ServiceRequest(new Lighting(), null);
		getLampInfo.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));
		getLampInfo.addRequiredOutput(OUTPUT_BRIGHTNESS,
				new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS });
		getLampInfo.addRequiredOutput(OUTPUT_LOCATION,
				new String[] { Lighting.PROP_CONTROLS, PhysicalThing.PROP_PHYSICAL_LOCATION });

		ServiceResponse sr = caller.call(getLampInfo);

		Object[] output = new Object[2];
		output[0] = sr.getOutput(OUTPUT_BRIGHTNESS, false).get(0);
		output[1] = sr.getOutput(OUTPUT_LOCATION, false).get(0);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return output;
		else {
			return null;
		}
	}

	public static boolean turnOn(String lampURI) {
		ServiceRequest turnOnRequest = new ServiceRequest(new Lighting(), null);
		turnOnRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));
		turnOnRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(100));

		ServiceResponse sr = caller.call(turnOnRequest);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			return false;
		}
	}

	public static boolean turnOff(String lampURI) {
		ServiceRequest turnOffRequest = new ServiceRequest(new Lighting(), null);
		turnOffRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));
		turnOffRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(0));

		ServiceResponse sr = caller.call(turnOffRequest);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			return false;
		}
	}

	public static boolean dimToValue(String lampURI, Integer percent) {
		ServiceRequest dimRequest = new ServiceRequest(new Lighting(), null);
		dimRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));
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
