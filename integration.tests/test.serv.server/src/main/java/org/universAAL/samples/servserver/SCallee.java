package org.universAAL.samples.servserver;

import java.util.ArrayList;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.location.indoor.Room;

public class SCallee extends ServiceCallee {
    private static final String SAMPLE_LAMP_URI_PREFIX = "http://ontology.igd.fhg.de/LightingServer.owl#lamp";
    private static final int LAMP_AMOUNT = 10;

    protected SCallee(ModuleContext context, ServiceProfile[] realizedServices) {
	super(context, realizedServices);
	// TODO Auto-generated constructor stub
    }

    protected SCallee(ModuleContext context) {
	super(context, ProvidedService.profiles);
	// TODO Auto-generated constructor stub
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    public ServiceResponse handleCall(ServiceCall call) {
	if (call == null)
	    return null;

	String operation = call.getProcessURI();
	if (operation == null)
	    return null;

	if (operation.startsWith(ProvidedService.SERVICE_GET_CONTROLLED_LAMPS))
	    return getControlledLamps();

	Object input = call.getInputValue(ProvidedService.INPUT_LAMP_URI);
	if (input == null)
	    return null;

	if (operation.startsWith(ProvidedService.SERVICE_GET_LAMP_INFO))
	    return getLampInfo(input.toString());

	if (operation.startsWith(ProvidedService.SERVICE_TURN_OFF))
	    return new ServiceResponse(CallStatus.succeeded);

	if (operation.startsWith(ProvidedService.SERVICE_TURN_ON))
	    return new ServiceResponse(CallStatus.succeeded);

	return null;
    }

    private ServiceResponse getControlledLamps() {
	// We assume that the Service-Call always succeeds because we only
	// simulate the lights
	ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
	// create a list including the available lights
	ArrayList al = new ArrayList(LAMP_AMOUNT);
	for (int i = 0; i < LAMP_AMOUNT; i++)
	    al.add(new LightSource(SAMPLE_LAMP_URI_PREFIX + i));
	// create and add a ProcessOutput-Event that binds the output URI to the
	// created list of lamps
	sr.addOutput(new ProcessOutput(ProvidedService.OUTPUT_CONTROLLED_LAMPS,
		al));
	return sr;
    }

    private ServiceResponse getLampInfo(String lampURI) {
	// We assume that the Service-Call always succeeds because we only
	// simulate the lights
	ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
	// create and add a ProcessOutput-Event that binds the output URI to the
	// state of the lamp
	sr.addOutput(new ProcessOutput(ProvidedService.OUTPUT_LAMP_BRIGHTNESS,
		new Integer(100)));
	// create and add a ProcessOutput-Event that binds the output URI to the
	// location of the lamp
	sr.addOutput(new ProcessOutput(ProvidedService.OUTPUT_LAMP_LOCATION,
		new Room("urn:aal_space:myHome#kitchen")));
	return sr;
    }

}
