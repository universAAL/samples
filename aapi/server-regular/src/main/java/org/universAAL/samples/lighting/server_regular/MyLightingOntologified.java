/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science 

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

import org.universAAL.ontology.lighting.ElectricLight;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.location.indoor.Room;
import org.universAAL.samples.lighting.server_regular.unit_impl.MyLighting;

public class MyLightingOntologified {
    /**
     * Real implementation controlling light sources.
     */
    private MyLighting realLighting = new MyLighting();

    public MyLightingOntologified(String namespace) {
	/* Initializing fields with ontological constants */
	this.LAMP_URI_PREFIX = namespace + "controlledLamp";
	this.LOCATION_URI_PREFIX = "urn:aal_space:myHome#";
    }
    
    /*
     * Fields and methods for mapping real implementation onto ontology.
     */

    private final String LAMP_URI_PREFIX;
    private final String LOCATION_URI_PREFIX; 

    private String constructLampURIfromLocalID(int localID) {
	return LAMP_URI_PREFIX + localID;
    }

    private String constructLocationURIfromLocalID(String localID) {
	return LOCATION_URI_PREFIX + localID;
    }

    private int extractLocalIDfromLampURI(String lampURI) {
	return Integer.parseInt(lampURI.substring(LAMP_URI_PREFIX.length()));
    }

    /*
     * Implementation of ontological interface
     */

    public LightSource[] getControlledLamps() {
	ArrayList<LightSource> al = new ArrayList<LightSource>();
	Integer[] controlledLamps = realLighting.getControlledLamps();
	for (int i = 0; i < controlledLamps.length; i++) {
	    LightSource ls = new LightSource(constructLampURIfromLocalID(i));
	    ls.setLightType(ElectricLight.lightBulb);
	    al.add(ls);
	}
	return al.toArray(new LightSource[0]);
    }

    public Object[] getLampInfo(LightSource lamp) {
	int lampID = extractLocalIDfromLampURI(lamp.getURI());
	int state = realLighting.getState(lampID);
	String loc = realLighting.getLampLocation(lampID);
	Room ontologyLoc = new Room(constructLocationURIfromLocalID(loc));
	return new Object[] { state, ontologyLoc };
    }

    public void turnOff(LightSource lamp) {
	int lampID = extractLocalIDfromLampURI(lamp.getURI());
	realLighting.turnOff(lampID);
    }

    public void turnOn(LightSource lamp) {
	int lampID = extractLocalIDfromLampURI(lamp.getURI());
	realLighting.turnOn(lampID);
    }
}
