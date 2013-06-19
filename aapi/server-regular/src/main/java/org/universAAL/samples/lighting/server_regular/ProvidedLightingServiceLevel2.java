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
package org.universAAL.samples.lighting.server_regular;

import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.SimpleOntology;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.ResourceFactory;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;

/**
 * @author mtazari
 * @author mpsiuk
 * 
 */
public class ProvidedLightingServiceLevel2 extends Lighting {

    static final ServiceProfile[] profiles = new ServiceProfile[4];

    static {
	// we need to register all classes in the ontology for the serialization
	// of the object
	OntologyManagement.getInstance().register(Activator.mc,
		new SimpleOntology(MY_URI, Lighting.MY_URI,
			new ResourceFactory() {
			    public Resource createInstance(String classURI,
				    String instanceURI, int factoryIndex) {
				return new ProvidedLightingServiceLevel2(
					instanceURI);
			    }
			}));
	// Help structures to define property paths used more than once below
	String[] ppBrightness = new String[] { Lighting.PROP_CONTROLS,
		LightSource.PROP_SOURCE_BRIGHTNESS };

	/*
	 * create the service description #1 to be registered with the service
	 * bus
	 */

	// Create the service-object for retrieving the controlled light bulbs
	ProvidedLightingServiceLevel2 getControlledLamps = new ProvidedLightingServiceLevel2(
		LightingServerURIs.GetControlledLamps.URI);
	profiles[0] = getControlledLamps.myProfile;

	/*
	 * create the service description #2 to be registered with the service
	 * bus
	 */

	// Create the service-object for retrieving info about the location and
	// state of each controlled light bulb
	ProvidedLightingServiceLevel2 getLampInfo = new ProvidedLightingServiceLevel2(
		LightingServerURIs.GetLampInfo.URI);
	profiles[1] = getLampInfo.myProfile;

	/*
	 * create the service description #3 to be registered with the service
	 * bus
	 */

	// Create the service-object for turning off each controlled light bulb
	ProvidedLightingServiceLevel2 turnOff = new ProvidedLightingServiceLevel2(
		LightingServerURIs.TurnOff.URI);
	turnOff.myProfile.addChangeEffect(ppBrightness, new Integer(0));
	profiles[2] = turnOff.myProfile;

	/*
	 * create the service description #4 to be registered with the service
	 * bus
	 */

	// Create the service-object for turning on each controlled light bulb
	ProvidedLightingServiceLevel2 turnOn = new ProvidedLightingServiceLevel2(
		LightingServerURIs.TurnOn.URI);
	turnOn.myProfile.addChangeEffect(ppBrightness, new Integer(100));
	profiles[3] = turnOn.myProfile;
    }

    private ProvidedLightingServiceLevel2(String uri) {
	super(uri);
    }

    public String getClassURI() {
	return MY_URI;
    }
}
