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
package org.universAAL.support.tutorial.sbus.server;

import java.util.ArrayList;

import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.location.indoor.IndoorPlace;
import org.universAAL.ontology.phThing.PhysicalThing;
import org.universAAL.support.tutorial.sbus.ontology.DeviceService;
import org.universAAL.support.tutorial.sbus.ontology.Lamp;
import org.universAAL.support.tutorial.sbus.ontology.LampService;

/**
 * @author mtazari
 * 
 */
public class ProfileUtil {
    // All the static Strings are used to unique identify special functions and
    // objects
    public static String LAMP_SERVER_NAMESPACE = "http://ontology.igd.fhg.de/TestLampServer.owl#";

    public static String SERVICE_GET_LAMPS;
    public static String SERVICE_GET_LAMP_INFO;
    public static String SERVICE_GET_LAMPS_INDOORS;
    public static String SERVICE_TURN_OFF;
    public static String SERVICE_TURN_ON;
    public static String SERVICE_DIM;

    public static String INPUT_LAMP_URI;
    public static String INPUT_LAMP_BRIGHTNESS;
    public static String INPUT_LAMP_LOCATION;

    public static String OUTPUT_CONTROLLED_LAMPS;
    public static String OUTPUT_LAMP_BRIGHTNESS;
    public static String OUTPUT_LAMP_LOCATION;

    public static String[] ppControls;
    public static String[] ppBrightness;
    public static String[] ppLocation;
    public static String[] ppLocationType;

    public static ArrayList<ServiceProfile> profiles = new ArrayList<ServiceProfile>();

    static {
	SERVICE_GET_LAMPS = LAMP_SERVER_NAMESPACE + "srv_getControlledLamps";
	SERVICE_GET_LAMP_INFO = LAMP_SERVER_NAMESPACE + "srv_getLampInfo";
	SERVICE_GET_LAMPS_INDOORS = LAMP_SERVER_NAMESPACE
		+ "srv_getLampsIndoor";
	SERVICE_TURN_OFF = LAMP_SERVER_NAMESPACE + "srv_turnOff";
	SERVICE_TURN_ON = LAMP_SERVER_NAMESPACE + "srv_turnOn";
	SERVICE_DIM = LAMP_SERVER_NAMESPACE + "srv_dim";

	INPUT_LAMP_URI = LAMP_SERVER_NAMESPACE + "in_lampURI";
	INPUT_LAMP_BRIGHTNESS = LAMP_SERVER_NAMESPACE + "in_brightness";
	INPUT_LAMP_LOCATION = LAMP_SERVER_NAMESPACE + "in_location";

	OUTPUT_CONTROLLED_LAMPS = LAMP_SERVER_NAMESPACE + "out_controlledLamps";
	OUTPUT_LAMP_BRIGHTNESS = LAMP_SERVER_NAMESPACE + "out_brightness";
	OUTPUT_LAMP_LOCATION = LAMP_SERVER_NAMESPACE + "out_location";

	// Help structures to define property paths used more than once below
	ppControls = new String[] { DeviceService.PROP_CONTROLS };
	ppBrightness = new String[] { DeviceService.PROP_CONTROLS,
		Lamp.PROP_SOURCE_BRIGHTNESS };
	ppLocation = new String[] { DeviceService.PROP_CONTROLS,
		PhysicalThing.PROP_PHYSICAL_LOCATION };
	ppLocationType = new String[] { DeviceService.PROP_CONTROLS,
		PhysicalThing.PROP_PHYSICAL_LOCATION };

	create_getControlledLamps(true);
	create_getLampInfo(true);
	create_turnOff(true);
	create_turnOn(true);
	create_dim(true);
	create_getControlledLamps(false);
	create_getLampInfo(false);
	create_turnOff(false);
	create_turnOn(false);
	create_dim(false);
//	create_getIndoors1();
//	create_getIndoors2();
//	create_getIndoors3();
    }

    private static Service createService(String uri, boolean dev) {
	if (dev)
	    return new DeviceService(uri + "__Device");
	else
	    return new LampService(uri + "__Lamp");
    }

    public static void create_getControlledLamps(boolean dev) {
	Service getControlledLamps = createService(SERVICE_GET_LAMPS, dev);
	getControlledLamps.addOutput(OUTPUT_CONTROLLED_LAMPS, Lamp.MY_URI, 0,
		0, ppControls);
	profiles.add(getControlledLamps.getProfile());
    }

    public static void create_getLampInfo(boolean dev) {
	Service getLampInfo = createService(SERVICE_GET_LAMP_INFO, dev);
	getLampInfo.addFilteringInput(INPUT_LAMP_URI, Lamp.MY_URI, 1, 1,
		ppControls);
	getLampInfo.addOutput(OUTPUT_LAMP_BRIGHTNESS,
		TypeMapper.getDatatypeURI(Integer.class), 1, 1, ppBrightness);
	getLampInfo.addOutput(OUTPUT_LAMP_LOCATION, Location.MY_URI, 1, 1,
		ppLocation);
	profiles.add(getLampInfo.getProfile());
    }

    public static void create_turnOff(boolean dev) {
	Service turnOff = createService(SERVICE_TURN_OFF, dev);
	turnOff.addFilteringInput(INPUT_LAMP_URI, Lamp.MY_URI, 1, 1, ppControls);
	turnOff.getProfile().addChangeEffect(ppBrightness, new Integer(0));
	profiles.add(turnOff.getProfile());
    }

    public static void create_turnOn(boolean dev) {
	Service turnOn = createService(SERVICE_TURN_ON, dev);
	turnOn.addFilteringInput(INPUT_LAMP_URI, Lamp.MY_URI, 1, 1, ppControls);
	turnOn.getProfile().addChangeEffect(ppBrightness, new Integer(100));
	profiles.add(turnOn.getProfile());
    }

    public static void create_dim(boolean dev) {
	Service dim = createService(SERVICE_DIM, dev);
	dim.addFilteringInput(INPUT_LAMP_URI, Lamp.MY_URI, 1, 1, ppControls);
	dim.addInputWithChangeEffect(INPUT_LAMP_BRIGHTNESS,
		TypeMapper.getDatatypeURI(Integer.class), 1, 1, ppBrightness);
	profiles.add(dim.getProfile());
    }

    public static void create_getIndoors1() {
	Service getIndoors = createService(SERVICE_GET_LAMPS_INDOORS + "_1_",
		false);
	getIndoors.addInstanceLevelRestriction(MergedRestriction
		.getFixedValueRestriction(PhysicalThing.PROP_PHYSICAL_LOCATION,
			IndoorPlace.MY_URI), ppLocation);
	getIndoors.addOutput(OUTPUT_CONTROLLED_LAMPS, Lamp.MY_URI, 0, 0,
		ppControls);
	profiles.add(getIndoors.getProfile());
//	System.out.println(getIndoors.getProfile().toStringRecursive());
    }

    public static void create_getIndoors2() {
	Service getIndoors = createService(SERVICE_GET_LAMPS_INDOORS + "_2_",
		false);
	getIndoors.addFilteringType(INPUT_LAMP_LOCATION, ppLocation);
	getIndoors.addOutput(OUTPUT_CONTROLLED_LAMPS, Lamp.MY_URI, 0, 0,
		ppControls);
	profiles.add(getIndoors.getProfile());
    }

    public static void create_getIndoors3() {
	Service getIndoors = createService(SERVICE_GET_LAMPS_INDOORS + "_3_",
		false);
	getIndoors.addInstanceLevelRestriction(MergedRestriction
		.getFixedValueRestriction(PhysicalThing.PROP_PHYSICAL_LOCATION,
			IndoorPlace.MY_URI), ppLocation);
	getIndoors.addFilteringType(INPUT_LAMP_LOCATION, ppLocation);
	getIndoors.addOutput(OUTPUT_CONTROLLED_LAMPS, Lamp.MY_URI, 0, 0,
		ppControls);
	profiles.add(getIndoors.getProfile());
    }
}
