/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.samples.utils.server;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.support.utils.service.Arg;
import org.universAAL.support.utils.service.Path;
import org.universAAL.support.utils.service.low.Profile;

// Service Callee uAAL wrapper
public class CalleeExample extends ServiceCallee{
    // Declare service URI
    public static final String MY_URI = Activator.SERVER_NAMESPACE + "MyService";
    // Declare URI constants to be used in profiles
    static final String SERVICE_GET_CONTROLLED_LAMPS = Activator.SERVER_NAMESPACE + "getControlledLamps";
    static final String SERVICE_GET_LAMP_INFO = Activator.SERVER_NAMESPACE + "getLampInfo";
    static final String SERVICE_TURN_OFF = Activator.SERVER_NAMESPACE + "turnOff";
    static final String SERVICE_TURN_ON = Activator.SERVER_NAMESPACE + "turnOn";
    static final String SERVICE_TURN_DIM = Activator.SERVER_NAMESPACE + "turnDim";
    static final String INPUT_LAMP_URI = Activator.SERVER_NAMESPACE + "lampURI";
    static final String INPUT_BRIGHTNESS = Activator.SERVER_NAMESPACE  + "brightness";
    static final String OUTPUT_CONTROLLED_LAMPS = Activator.SERVER_NAMESPACE + "controlledLamps";
    static final String OUTPUT_LAMP_BRIGHTNESS = Activator.SERVER_NAMESPACE  + "brightness";
    static final String OUTPUT_LAMP_LOCATION = Activator.SERVER_NAMESPACE + "location";

    // Simplest constructor, which takes the provided profiles from this class
    protected CalleeExample(ModuleContext context) {
	super(context, realizedServices());
    }

    // Defines the provided service profiles
    private static ServiceProfile[] realizedServices() {
	ServiceProfile[] profiles = new ServiceProfile[5];
	
	// GET LAMPS profile: Output<lights>
	Profile prof0=new Profile(new DeviceService(SERVICE_GET_CONTROLLED_LAMPS));
	prof0.put(Path.at(DeviceService.PROP_CONTROLS), Arg.out(LightController.MY_URI), OUTPUT_CONTROLLED_LAMPS);
	profiles[0]=prof0.getTheProfile();
	
	// GET LAMP INFO profile: Input<light:?> Output<Brightness> Output<Location>
	Profile prof1=new Profile(new DeviceService(SERVICE_GET_LAMP_INFO));
	prof1.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(LightController.MY_URI), INPUT_LAMP_URI);
	prof1.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.out(TypeMapper.getDatatypeURI(Integer.class)), OUTPUT_LAMP_BRIGHTNESS);
	prof1.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_PHYSICAL_LOCATION), Arg.out(Location.MY_URI), OUTPUT_LAMP_LOCATION);
	profiles[1]=prof1.getTheProfile();
	
	// DIM LAMP profile: Input<light:?> Change<Brightness:?>
	Profile prof2=new Profile(new DeviceService(SERVICE_TURN_DIM));
	prof2.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(LightController.MY_URI), INPUT_LAMP_URI);
	prof2.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.change(TypeMapper.getDatatypeURI(Integer.class)), INPUT_BRIGHTNESS);
	profiles[2]=prof2.getTheProfile();
	
	// TURN ON LAMP profile: Input<light:?> Change<Brightness:100>
	Profile prof3=new Profile(new DeviceService(SERVICE_TURN_ON));
	prof3.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(LightController.MY_URI), INPUT_LAMP_URI);
	prof3.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.change(Integer.valueOf(100)), null);
	profiles[3]=prof3.getTheProfile();
	
	// TURN OFF LAMP profile: Input<light:?> Change<Brightness:0>
	Profile prof4=new Profile(new DeviceService(SERVICE_TURN_OFF));
	prof4.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(LightController.MY_URI), INPUT_LAMP_URI);
	prof4.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.change(Integer.valueOf(0)), null);
	profiles[4]=prof4.getTheProfile();

	return profiles;
    }

    // Handles the calls to the profiles
    @Override
    public ServiceResponse handleCall(ServiceCall call) {
	if (call == null)
	    return null;

	String operation = call.getProcessURI();
	if (operation == null)
	    return null;

	if (operation.startsWith(SERVICE_GET_CONTROLLED_LAMPS)){
	    // Return all lights (use clone because otherwise they get modified in the process)
	    ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
	    sr.addOutput(new ProcessOutput(OUTPUT_CONTROLLED_LAMPS, Activator.myLights.clone()));
	    return sr;
	}

	Object input = call.getInputValue(INPUT_LAMP_URI);
	if (input == null){
	    // The rest of profiles use a light input. If it´s not there, failure!
	    return new ServiceResponse(CallStatus.serviceSpecificFailure);
	}
	int index=Integer.parseInt(input.toString().substring(Activator.LIGHT_URI_PREFIX.length()));

	if (operation.startsWith(SERVICE_GET_LAMP_INFO)){
	    // Return brightness and location of light
	    ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
	    sr.addOutput(new ProcessOutput(OUTPUT_LAMP_BRIGHTNESS, Integer.valueOf(Activator.myLights.get(index).getValue())));
	    sr.addOutput(new ProcessOutput(OUTPUT_LAMP_LOCATION, Activator.myLights.get(index).getLocation()));
	    return sr;
	}

	if (operation.startsWith(SERVICE_TURN_DIM)) {
	    // Set light to input brightness and send event (if new value)
	    Integer bright = (Integer) call.getInputValue(INPUT_BRIGHTNESS);
	    if (Activator.myLights.get(index).getValue() != bright) {
		Activator.myLights.get(index).setValue(bright);
		Activator.publisher.publish(new ContextEvent(Activator.myLights
			.get(index), LightController.PROP_HAS_VALUE));
	    }
	    return new ServiceResponse(CallStatus.succeeded);
	}

	if (operation.startsWith(SERVICE_TURN_ON)) {
	    // Set light to 100 brightness and send event (if not ON before)
	    if (Activator.myLights.get(index).getValue() != 100) {
		Activator.myLights.get(index).setValue(100);
		Activator.publisher.publish(new ContextEvent(Activator.myLights
			.get(index), LightController.PROP_HAS_VALUE));
	    }
	    return new ServiceResponse(CallStatus.succeeded);
	}
	
	if (operation.startsWith(SERVICE_TURN_OFF)) {
	    // Set light to 0 brightness and send event (if not OFF before)
	    if (Activator.myLights.get(index).getValue() != 0) {
		Activator.myLights.get(index).setValue(0);
		Activator.publisher.publish(new ContextEvent(Activator.myLights
			.get(index), LightController.PROP_HAS_VALUE));
	    }
	    return new ServiceResponse(CallStatus.succeeded);
	}
	// In case of doubt, send failure
	return new ServiceResponse(CallStatus.serviceSpecificFailure);
    }
    
    @Override
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub
    }
    
}
