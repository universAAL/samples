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
package org.universAAL.samples.lighting.uiclient;

import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.location.Location;

/**
 * @author amarinc
 * 
 */
public class LightingConsumer extends ContextSubscriber {

	private static ServiceCaller caller;
	private static LightSource[] allLightSources = null;

	private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";

	private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE + "controlledLamps";
	private static final String OUTPUT_LAMP_LOCATION = LIGHTING_CONSUMER_NAMESPACE + "lampLocation";

	public static ContextEventPattern[] getContextSubscriptionParams() {
		// I am interested in all events with a light source as subject
		ContextEventPattern cep = new ContextEventPattern();
		cep.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightSource.MY_URI));
		return new ContextEventPattern[] { cep };
	}

	LightingConsumer(ModuleContext mc) {
		// the constructor register us to the bus
		super(mc, getContextSubscriptionParams());

		// the DefaultServiceCaller will be used to make ServiceRequest
		// (surprise ;-) )
		caller = new DefaultServiceCaller(mc);

		getControlledLamps();
		// LightClient c = new LightClient();
	}

	// *****************************************************************
	// Services Requests
	// *****************************************************************

	// This method create a ServiceRequest to shut-off a light-source with the
	// given URI
	public static ServiceRequest turnOffRequest(String lampURI) {
		// At first create a ServiceRequest by passing a appropriate
		// service-object
		// Additional an involved user can be passed to create user-profiles or
		// react to special needs
		ServiceRequest turnOff = new ServiceRequest(new Lighting(), null);

		// Add the URI of the lamp to the request
		turnOff.getRequestedService().addInstanceLevelRestriction(
				MergedRestriction.getFixedValueRestriction(Lighting.PROP_CONTROLS, new LightSource(lampURI)),
				new String[] { Lighting.PROP_CONTROLS });

		// Add the property that have to be changed and the new value
		turnOff.addChangeEffect(
				(new PropertyPath(null, true,
						new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS })).getThePath(),
				new Integer(0));
		return turnOff;
	}

	// see turnOffRequest
	public static ServiceRequest turnOnRequest(String lampURI) {
		ServiceRequest turnOn = new ServiceRequest(new Lighting(), null);

		turnOn.getRequestedService().addInstanceLevelRestriction(
				MergedRestriction.getFixedValueRestriction(Lighting.PROP_CONTROLS, new LightSource(lampURI)),
				new String[] { Lighting.PROP_CONTROLS });

		turnOn.addChangeEffect(
				(new PropertyPath(null, true,
						new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS })).getThePath(),
				new Integer(100));
		return turnOn;
	}

	// see turnOffRequest
	public static ServiceRequest dimRequest(String lampURI, Integer percent) {
		ServiceRequest dim = new ServiceRequest(new Lighting(), null);

		dim.getRequestedService().addInstanceLevelRestriction(
				MergedRestriction.getFixedValueRestriction(Lighting.PROP_CONTROLS, new LightSource(lampURI)),
				new String[] { Lighting.PROP_CONTROLS });

		dim.addChangeEffect(
				(new PropertyPath(null, true,
						new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS })).getThePath(),
				percent);

		return dim;
	}

	public static ServiceRequest getGetLampsLocationRequest(String lampURI) {
		ServiceRequest getLampLocation = new ServiceRequest(new Lighting(), null);
		getLampLocation.getRequestedService().addInstanceLevelRestriction(
				MergedRestriction.getFixedValueRestriction(Lighting.PROP_CONTROLS, new LightSource(lampURI)),
				new String[] { Lighting.PROP_CONTROLS });
		getLampLocation.addSimpleOutputBinding(new ProcessOutput(OUTPUT_LAMP_LOCATION),
				new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_PHYSICAL_LOCATION });
		return getLampLocation;
	}

	private static Object getReturnValue(List outputs, String expectedOutput) {
		Object returnValue = null;
		if (outputs == null)
			LogUtils.logInfo(SharedResources.moduleContext, LightingConsumer.class, "getReturnValue",
					new Object[] { "No return values found!" }, null);
		else
			for (Iterator i = outputs.iterator(); i.hasNext();) {
				ProcessOutput output = (ProcessOutput) i.next();
				if (output.getURI().equals(expectedOutput))
					if (returnValue == null)
						returnValue = output.getParameterValue();
					else
						LogUtils.logInfo(SharedResources.moduleContext, LightingConsumer.class, "getReturnValue",
								new Object[] { "Redundant return value ignored!" }, null);
				else
					LogUtils.logInfo(SharedResources.moduleContext, LightingConsumer.class, "getReturnValue",
							new Object[] { "Irrelevant return value ignored: ", output.getURI() }, null);
			}

		return returnValue;
	}

	public static ServiceRequest getAllLampsRequest() {
		// Again we want to create a ServiceRequest regarding LightSources
		ServiceRequest getAllLamps = new ServiceRequest(new Lighting(), null);

		// But here we do not to change anything, furthermore we want to get an
		// output (the one at OUTPUT_LIST_OF_LAMPS)
		getAllLamps.addSimpleOutputBinding(new ProcessOutput(OUTPUT_LIST_OF_LAMPS),
				new String[] { Lighting.PROP_CONTROLS });

		return getAllLamps;
	}

	// *****************************************************************
	// Controller Methods
	// *****************************************************************

	// Get a list of all available light-source in the system
	public static LightSource[] getControlledLamps() {
		if (allLightSources == null) {

			// Make a call for the lamps and get the request
			ServiceResponse sr = caller.call(getAllLampsRequest());

			if (sr.getCallStatus() == CallStatus.succeeded) {
				// the return value should be a list of light sources, but we
				// check this in the following try-catch block
				Object value = getReturnValue(sr.getOutputs(), OUTPUT_LIST_OF_LAMPS);
				try {
					// try to convert the assumed list of light sources to an
					// array of them
					allLightSources = (LightSource[]) ((List) value).toArray(new LightSource[((List) value).size()]);

					// check lamp locations
					for (int j = 0; j < allLightSources.length; j++) {
						Location l = allLightSources[j].getLocation();
						if (l == null) {
							// get the location by calling a service
							sr = caller.call(getGetLampsLocationRequest(allLightSources[j].getURI()));
							if (sr.getCallStatus() == CallStatus.succeeded) {
								try {
									allLightSources[j].setLocation(
											(Location) getReturnValue(sr.getOutputs(), OUTPUT_LAMP_LOCATION));
								} catch (Exception e) {
									LogUtils.logError(SharedResources.moduleContext, LightingConsumer.class,
											"getControlledLamps",
											new Object[] {
													"Exception caught while trying to interpret a return value as a location!" },
											e);
								}
							} else
								LogUtils.logInfo(SharedResources.moduleContext, LightingConsumer.class,
										"getControlledLamps",
										new Object[] { "Status of getLocation(): ", sr.getCallStatus() }, null);
						}
					}

				} catch (Exception e) {
					LogUtils.logError(SharedResources.moduleContext, LightingConsumer.class, "getControlledLamps",
							new Object[] {
									"Exception caught while trying to interpret a return value as a list of light sources!" },
							e);
				}
			} else {
				LogUtils.logError(SharedResources.moduleContext, LightingConsumer.class, "getControlledLamps",
						new Object[] { "Status of getAllLamps(): ", sr.getCallStatus() }, null);
			}
		}
		return allLightSources;
	}

	// this method turn off the light at lampURI and give back if the operation
	// was an access
	public static boolean turnOff(String lampURI) {
		// check if input is valid
		if ((lampURI == null) || !(lampURI instanceof String)) {
			System.out.println("LightingConsumer: wrong lampURI in turnOff(String lampURI)");
			return false;
		}

		// make a call with the appropriate request
		ServiceResponse sr = caller.call(turnOffRequest(lampURI));
		System.out.println(sr.getCallStatus());

		// check the call status and return true if succeeded
		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			System.out.println("LightingConsumer: the lamp couldn't turned off in turnOff(String lampURI)");
			return false;
		}
	}

	// see turnOff
	public static boolean turnOn(String lampURI) {

		if ((lampURI == null) || !(lampURI instanceof String)) {
			System.out.println("LightingConsumer: wrong lampURI in turnOn(String lampURI)");
			return false;
		}

		ServiceResponse sr = caller.call(turnOnRequest(lampURI));

		if (sr.getCallStatus() == CallStatus.succeeded) {
			System.out.println("LightingConsumer: SUCCESS: the lamp was turned on: " + lampURI);
			return true;
		} else {
			System.out.println("LightingConsumer: the lamp couldn't turned on in turnOn(String lampURI)");
			return false;
		}
	}

	// see turnOff
	public static boolean dimToValue(String lampURI, Integer percent) {

		if ((lampURI == null) || (percent == null) || !(lampURI instanceof String) || !(percent instanceof Integer)) {
			System.out.println("LightingConsumer: wrong inputs in dimToValue(String lampURI, Integer percent)");
			return false;
		}

		ServiceResponse sr = caller.call(dimRequest(lampURI, percent));

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			System.out.println(
					"LightingConsumer: the lamp couldn't dimmed to wanted value in dimToValue(String lampURI, integer percent)");
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.persona.middleware.context.ContextSubscriber#handleContextEvent(org
	 * .persona.middleware.context.ContextEvent)
	 */
	public void handleContextEvent(ContextEvent event) {
		LogUtils.logInfo(SharedResources.moduleContext, getClass(), "handleContextEvent",
				new Object[] { "Received context event:\n    Subject     =", event.getSubjectURI(),
						"\n    Subject type=", event.getSubjectTypeURI(), "\n    Predicate   =",
						event.getRDFPredicate(), "\n    Object      =", event.getRDFObject() },
				null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.persona.middleware.context.ContextSubscriber#
	 * communicationChannelBroken ()
	 */
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}
}
