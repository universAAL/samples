/**
 * 
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

/**
 * @author amarinc
 * 
 */
public class LightingConsumerLevel1 {

    private static ServiceCaller caller;

    private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";

    private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE
	    + "controlledLamps";

    private static final String OUTPUT_LOCATION = LIGHTING_CONSUMER_NAMESPACE
	    + "location";

    private static final String OUTPUT_BRIGHTNESS = LIGHTING_CONSUMER_NAMESPACE
	    + "brightness";

    LightingConsumerLevel1(ModuleContext context) {
	// the DefaultServiceCaller will be used to make ServiceRequest
	// (surprise ;-) )
	caller = new DefaultServiceCaller(context);
    }

    // *****************************************************************
    // Controller Methods
    // *****************************************************************

    public static List<LightSource> getControlledLamps() {
	// The URI of service does no longer need to be provided. Now the
	// service will be matched through required output.		
	ServiceRequest getAllLampsRequest = new ServiceRequest(new Lighting(),
		null);
	getAllLampsRequest.addRequiredOutput(OUTPUT_LIST_OF_LAMPS,
		new String[] { Lighting.PROP_CONTROLS });

	ServiceResponse sr = caller.call(getAllLampsRequest);

	if (sr.getCallStatus() == CallStatus.succeeded) {
	    return sr.getOutput(OUTPUT_LIST_OF_LAMPS, true);
	}
	return null;
    }

    public static Object[] getLampInfo(String lampURI) {
	// The URI of service does no longer need to be provided. Now the
	// service will be matched through the value filter and required output.	
	ServiceRequest getLampInfo = new ServiceRequest(new Lighting(), null);
	getLampInfo.addValueFilter(new String[] { Lighting.PROP_CONTROLS },
		new LightSource(lampURI));
	getLampInfo.addRequiredOutput(OUTPUT_BRIGHTNESS, new String[] {
		Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS });
	getLampInfo.addRequiredOutput(OUTPUT_LOCATION, new String[] {
		Lighting.PROP_CONTROLS, PhysicalThing.PROP_PHYSICAL_LOCATION });

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
	// The URI of service does no longer need to be provided. Now the
	// service will be matched through the change effect and value filter.
	ServiceRequest turnOnRequest = new ServiceRequest(new Lighting(), null);
	turnOnRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS },
		new LightSource(lampURI));
	turnOnRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS,
		LightSource.PROP_SOURCE_BRIGHTNESS }, new Integer(100));

	ServiceResponse sr = caller.call(turnOnRequest);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return true;
	else {
	    return false;
	}
    }

    public static boolean turnOff(String lampURI) {
	// The URI of service does no longer need to be provided. Now the
	// service will be matched through the change effect and value filter.	
	ServiceRequest turnOffRequest = new ServiceRequest(new Lighting(), null);
	turnOffRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS },
		new LightSource(lampURI));
	turnOffRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS,
		LightSource.PROP_SOURCE_BRIGHTNESS }, new Integer(0));

	ServiceResponse sr = caller.call(turnOffRequest);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return true;
	else {
	    return false;
	}
    }

    public static boolean dimToValue(String lampURI, Integer percent) {
	// The URI of service does no longer need to be provided. Now the
	// service will be matched through the change effect and value filter.		
	ServiceRequest dimRequest = new ServiceRequest(new Lighting(), null);
	dimRequest.addValueFilter(new String[] { Lighting.PROP_CONTROLS },
		new LightSource(lampURI));
	dimRequest.addChangeEffect(new String[] { Lighting.PROP_CONTROLS,
		LightSource.PROP_SOURCE_BRIGHTNESS }, percent);

	ServiceResponse sr = caller.call(dimRequest);

	if (sr.getCallStatus() == CallStatus.succeeded)
	    return true;
	else {
	    return false;
	}
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

}
