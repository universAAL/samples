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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;


/**
 * @author amarinc
 *
 */
class LightingConsumer extends ContextSubscriber {
	
	private static ServiceCaller caller;
	
	private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";
	
	private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE + "controlledLamps";

	
	private static ContextEventPattern[] getContextSubscriptionParams() {
		// I am interested in all events with a light source as subject
		ContextEventPattern cep = new ContextEventPattern();
		cep.addRestriction(Restriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT,
				LightSource.MY_URI));
		return new ContextEventPattern[] {cep};
	}
	
	LightingConsumer(ModuleContext context){
		// the constructor register us to the bus
		super(context,getContextSubscriptionParams());

		// the DefaultServiceCaller will be used to make ServiceRequest (surprise ;-) )
		caller = new DefaultServiceCaller(context);
		
		Device[] d = getControlledLamps();
		//LightClient c = new LightClient();
	}
	
	
	// *****************************************************************
	// Services Requests
	// *****************************************************************
	
	// This method create a ServiceRequest to shut-off a light-source with the given URI
	private static ServiceRequest turnOffRequest(String lampURI){
		// At first create a ServiceRequest by passing a appropriate service-object
		// Additional an involved user can be passed to create user-profiles or react to special needs
		ServiceRequest turnOff = new ServiceRequest( new Lighting(), null);
		
		// Add the URI of the lamp to the request
		turnOff.getRequestedService()
			.addInstanceLevelRestriction(
					Restriction.getFixedValueRestriction(								
					Lighting.PROP_CONTROLS, new LightSource(
							lampURI)),
							new String[] { Lighting.PROP_CONTROLS });
		
		// Add the property that have to be changed and the new value
		turnOff.addChangeEffect(
				(new PropertyPath(null, true, new String[] {
						Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS})).getThePath(),
				new Integer(0));
			return turnOff;
	}
	
	// see turnOffRequest
	private static ServiceRequest turnOnRequest(String lampURI){
		ServiceRequest turnOn = new ServiceRequest( new Lighting(), null);
		
		turnOn.getRequestedService()
			.addInstanceLevelRestriction(
					Restriction.getFixedValueRestriction(								
					Lighting.PROP_CONTROLS, new LightSource(
							lampURI)),
							new String[] { Lighting.PROP_CONTROLS });
		
		turnOn.addChangeEffect(
				(new PropertyPath(null, true, new String[] {
						Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS})).getThePath(),
				new Integer(100));
			return turnOn;
	}
	
	// see turnOffRequest
	private static ServiceRequest dimRequest(String lampURI, Integer percent){
		ServiceRequest dim = new ServiceRequest( new Lighting(), null);
		
		dim.getRequestedService()
			.addInstanceLevelRestriction(
					Restriction.getFixedValueRestriction(								
					Lighting.PROP_CONTROLS, new LightSource(
							lampURI)),
							new String[] { Lighting.PROP_CONTROLS });	
		
		dim.addChangeEffect(
				(new PropertyPath(null, true, new String[] {
						Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS})).getThePath(),
				percent);
		
			return dim;
	}
	
	public  static  ServiceRequest getAllLampsRequest() {
		// Again we want to create a ServiceRequest regarding LightSources
		ServiceRequest getAllLamps = new ServiceRequest(
				new Lighting(), null);
	
		// But here we do not to change anything, furthermore we want to get an output (the one at OUTPUT_LIST_OF_LAMPS)
		getAllLamps.addSimpleOutputBinding(new ProcessOutput(
				OUTPUT_LIST_OF_LAMPS), (new PropertyPath(null, true,
				new String[] { Lighting.PROP_CONTROLS })).getThePath());
	
		return getAllLamps;
	}
	
	// *****************************************************************
	// Controller Methods
	// *****************************************************************
	
	// Get a list of all available light-source in the system
	public static Device[] getControlledLamps(){
		
		// Make a call for the lamps and get the request
		ServiceResponse sr = caller.call(getAllLampsRequest());
		
		if (sr.getCallStatus() == CallStatus.succeeded){
			try {
				List lampList = new ArrayList();

				// get the output from the request
				List outputs = sr.getOutputs();

				// if there is no output anything is wrong
				if (outputs == null || outputs.size() == 0) {
//					Activator.log.log(LogService.LOG_ERROR,"LocalVideoStreamMultiplexer:   No outputs available");
					System.out.println("LightingConsumer:   outputs are null in getControlledLamps()");
					return null;
				}
				
				// otherwise iterate over the provided outputs
				for (Iterator iter1 = outputs.iterator(); iter1.hasNext();) {
					Object obj = iter1.next();
					if(obj instanceof ProcessOutput) {
						ProcessOutput output = (ProcessOutput) obj;
						// if we got the right output can we check by the URI given in the request
						if (output.getURI().equals(OUTPUT_LIST_OF_LAMPS)) {
							Object ob = output.getParameterValue();
							if(!(ob instanceof List))
								break;
							// now we can add the list of lamps the our local "memory" and use them later
							List lamps = (List)ob;
							lampList.addAll(lamps);
						}
					}
					
				}

				if (lampList.size() == 0) {
					System.out.println("LightingConsumer:   there are no results for lamps in getControlledLamps()");
					return null;
				}

				// simple create an array out of the lamp-array and give it back --> finished
				LightSource[] lamps = (LightSource[]) lampList.toArray(new LightSource[lampList.size()]);

				return lamps;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println("LightingConsumer:   callstatus is not succeeded in getControlledLamps()");
			return null;
		}
	}
	
	// this method turn off the light at lampURI and give back if the operation was an access
	public static boolean turnOff(String lampURI){
		// check if input is valid
		if((lampURI == null) || !(lampURI instanceof String)) {
			System.out.println("LightingConsumer: wrong lampURI in turnOff(String lampURI)");
			return false;
		}
		
		// make a call with the appropriate request
		ServiceResponse sr = caller.call(turnOffRequest(lampURI));
		System.out.println(sr.getCallStatus());
		
		// check the call status and return true if succeeded
		if (sr.getCallStatus() == CallStatus.succeeded) return true;
		else{
			System.out.println("LightingConsumer: the lamp couldn't turned off in turnOff(String lampURI)");
			return false;
		}
	}
	
	// see turnOff
	public static boolean turnOn(String lampURI){
		
		if((lampURI == null) || !(lampURI instanceof String)) {
			System.out.println("LightingConsumer: wrong lampURI in turnOn(String lampURI)");
			return false;
		}
		
		ServiceResponse sr = caller.call(turnOnRequest(lampURI));
		
		if (sr.getCallStatus() == CallStatus.succeeded) {
			System.out.println("LightingConsumer: SUCCESS: the lamp was turned on: " + lampURI);
			return true;
		} else{
			System.out.println("LightingConsumer: the lamp couldn't turned on in turnOn(String lampURI)");
			return false;
		}
	}
	
	// see turnOff
	public static boolean dimToValue(String lampURI, Integer percent){
		
		if((lampURI == null) || (percent == null) || !(lampURI instanceof String) || !(percent instanceof Integer)) {
			System.out.println("LightingConsumer: wrong inputs in dimToValue(String lampURI, Integer percent)");
			return false;
		}
		
		ServiceResponse sr = caller.call(dimRequest(lampURI, percent));
		
		if (sr.getCallStatus() == CallStatus.succeeded) return true;
		else{
			System.out.println("LightingConsumer: the lamp couldn't dimmed to wanted value in dimToValue(String lampURI, integer percent)");
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.persona.middleware.context.ContextSubscriber#handleContextEvent(org.persona.middleware.context.ContextEvent)
	 */
	public void handleContextEvent(ContextEvent event) {
	    LogUtils.logInfo(SharedResources.moduleContext, this.getClass(), "handleContextEvent",
		    new Object[] {
				"Received context event:\n" +
				"    Subject     ="+event.getSubjectURI()+"\n" +
				"    Subject type="+event.getSubjectTypeURI()+"\n" +
				"    Predicate   ="+event.getRDFPredicate()+"\n" +
				"    Object      ="+event.getRDFObject() },
				null);
	}
	

	/* (non-Javadoc)
	 * @see org.persona.middleware.context.ContextSubscriber#communicationChannelBroken()
	 */
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}
}
