/*
    Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
    Fraunhofer-Gesellschaft - Institute for Computer Graphics Research

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
package org.universAAL.samples.lighting.client;

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
import org.universAAL.ontology.phThing.Device;

/**
 * @author amarinc
 *
 */
public class LightingConsumer extends ContextSubscriber {

	private LightClient lightClient;

	public static ServiceCaller caller;

	private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";

	private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE + "controlledLamps";

	public static ContextEventPattern[] getContextSubscriptionParams() {
		// I am interested in all events with a light source as subject
		ContextEventPattern cep = new ContextEventPattern();
		cep.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightSource.MY_URI));
		return new ContextEventPattern[] { cep };
	}

	public LightingConsumer(ModuleContext context) {
		// the constructor register us to the bus
		super(context, getContextSubscriptionParams());

		// the DefaultServiceCaller will be used to make ServiceRequest
		// (surprise ;-) )
		caller = new DefaultServiceCaller(context);

		try {
			lightClient = new LightClient();
		} catch (java.awt.HeadlessException ex) {
			LogUtils.logInfo(Activator.mc, LightingConsumer.class, "LightingConsumer",
					new Object[] { "client activates GUI-off mode because of no screen access" }, null);
		}
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

		// we are interested in only those realizations of 'Lighting'
		// that have control over the lamp with the given URI
		turnOff.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));

		// Add the property that have to be changed and the new value
		turnOff.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(0));
		return turnOff;
	}

	// see turnOffRequest
	public static ServiceRequest turnOnRequest(String lampURI) {
		ServiceRequest turnOn = new ServiceRequest(new Lighting(), null);

		// we are interested in only those realizations of 'Lighting'
		// that have control over the lamp with the given URI
		turnOn.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));

		turnOn.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(100));
		return turnOn;
	}

	// see turnOffRequest
	public static ServiceRequest dimRequest(String lampURI, Integer percent) {
		ServiceRequest dim = new ServiceRequest(new Lighting(), null);

		// we are interested in only those realizations of 'Lighting'
		// that have control over the lamp with the given URI
		dim.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));

		dim.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS }, percent);

		return dim;
	}

	public static ServiceRequest getAllLampsRequest() {
		// Again we want to create a ServiceRequest regarding LightSources
		ServiceRequest getAllLamps = new ServiceRequest(new Lighting(), null);

		// In this case, we do not intend to change anything but only retrieve
		// some info
		getAllLamps.addRequiredOutput(
				// this is OUR unique ID with which we can later retrieve the
				// returned
				// value
				OUTPUT_LIST_OF_LAMPS,
				// Specify the meaning of the required output
				// by pointing to the property in whose value you are interested
				// Because we haven't specified any filter before, this should
				// result
				// in returning all values associated with the specified
				// property
				new String[] { Lighting.PROP_CONTROLS });

		return getAllLamps;
	}

	// *****************************************************************
	// Controller Methods
	// *****************************************************************

	// Get a list of all available light-source in the system
	public static Device[] getControlledLamps() {

		// Make a call for the lamps and get the request
		ServiceResponse sr = caller.call(getAllLampsRequest());

		if (sr.getCallStatus() == CallStatus.succeeded) {
			try {
				List lampList = sr.getOutput(OUTPUT_LIST_OF_LAMPS);

				if (lampList == null || lampList.size() == 0) {
					LogUtils.logInfo(Activator.mc, LightingConsumer.class, "getControlledLamps",
							new Object[] { "there are no lamps" }, null);
					return null;
				}

				// simple create an array out of the lamp-array and give it back
				// --> finished
				LightSource[] lamps = (LightSource[]) lampList.toArray(new LightSource[lampList.size()]);

				return lamps;

			} catch (Exception e) {
				LogUtils.logError(Activator.mc, LightingConsumer.class, "getControlledLamps",
						new Object[] { "got exception", e.getMessage() }, e);
				return null;
			}
		} else {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "getControlledLamps",
					new Object[] { "callstatus is not succeeded" }, null);
			return null;
		}
	}

	// this method turn off the light at lampURI and give back if the operation
	// was a success
	public static boolean turnOff(String lampURI) {
		// check if input is valid
		if ((lampURI == null) || !(lampURI instanceof String)) {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "turnOff", new Object[] { "wrong lampURI" }, null);
			return false;
		}

		// make a call with the appropriate request
		ServiceResponse sr = caller.call(turnOffRequest(lampURI));
		LogUtils.logDebug(Activator.mc, LightingConsumer.class, "turnOff",
				new Object[] { "Call status: ", sr.getCallStatus().name() }, null);

		// check the call status and return true if succeeded
		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "turnOff",
					new Object[] { "the lamp couldn't be turned off" }, null);
			return false;
		}
	}

	// see turnOff
	public static boolean turnOn(String lampURI) {

		if ((lampURI == null) || !(lampURI instanceof String)) {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "turnOn", new Object[] { "wrong lampURI" }, null);
			return false;
		}

		ServiceResponse sr = caller.call(turnOnRequest(lampURI));
		LogUtils.logDebug(Activator.mc, LightingConsumer.class, "turnOn",
				new Object[] { "Call status: ", sr.getCallStatus().name() }, null);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "turnOn",
					new Object[] { "the lamp couldn't be turned on" }, null);
			return false;
		}
	}

	// see turnOff
	public static boolean dimToValue(String lampURI, Integer percent) {

		if ((lampURI == null) || (percent == null) || !(lampURI instanceof String) || !(percent instanceof Integer)) {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "dimToValue", new Object[] { "wrong inputs" }, null);
			return false;
		}

		ServiceResponse sr = caller.call(dimRequest(lampURI, percent));
		LogUtils.logDebug(Activator.mc, LightingConsumer.class, "dimToValue",
				new Object[] { "Call status: ", sr.getCallStatus().name() }, null);

		if (sr.getCallStatus() == CallStatus.succeeded)
			return true;
		else {
			LogUtils.logWarn(Activator.mc, LightingConsumer.class, "dimToValue",
					new Object[] { "the lamp couldn't be dimmed to the given value" }, null);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ContextSubscriber#handleContextEvent(ContextEvent)
	 */
	public void handleContextEvent(ContextEvent event) {
		LogUtils.logInfo(Activator.mc, LightingConsumer.class, "handleContextEvent",
				new Object[] { "Received context event:\n", "    Subject     = ", event.getSubjectURI(), "\n",
						"    Subject type= ", event.getSubjectTypeURI(), "\n", "    Predicate   = ",
						event.getRDFPredicate(), "\n", "    Object      = ", event.getRDFObject() },
				null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ContextSubscriber#communicationChannelBroken()
	 */
	public void communicationChannelBroken() {
	}

	@Override
	public void close() {
		super.close();
		caller.close();
		// Destroy the JFrame object
		lightClient.frame.setVisible(false);
		lightClient.frame.dispose();
	}
}
