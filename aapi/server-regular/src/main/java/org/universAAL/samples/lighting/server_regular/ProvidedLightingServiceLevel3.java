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

import java.util.Hashtable;

import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.SimpleOntology;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.impl.ResourceFactoryImpl;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.lighting.simple.LightingSimplified;

/**
 * @author mtazari
 * @author mpsiuk
 * 
 */
public class ProvidedLightingServiceLevel3 extends LightingSimplified {

    static final ServiceProfile[] profiles = new ServiceProfile[4];

    static {
	// we need to register all classes in the ontology for the serialization
	// of the object
	OntologyManagement.getInstance().register(Activator.mc,
		new SimpleOntology(MY_URI, LightingSimplified.MY_URI,
			new ResourceFactoryImpl() {
			    @Override
			    public Resource createInstance(String classURI,
				    String instanceURI, int factoryIndex) {
				return new ProvidedLightingServiceLevel3(
					instanceURI);
			    }
			}));

	/*
	 * create the service description #1 to be registered with the service
	 * bus
	 */

	// Create the service-object for retrieving the controlled light bulbs
	ProvidedLightingServiceLevel3 getControlledLamps = new ProvidedLightingServiceLevel3(
		LightingServerURIs.GetControlledLamps.URI);
	profiles[0] = getControlledLamps.myProfile;

	/*
	 * create the service description #2 to be registered with the service
	 * bus
	 */

	// Create the service-object for retrieving info about the location and
	// state of each controlled light bulb
	ProvidedLightingServiceLevel3 getLampInfo = new ProvidedLightingServiceLevel3(
		LightingServerURIs.GetLampInfo.URI);
	profiles[1] = getLampInfo.myProfile;

	/*
	 * create the service description #3 to be registered with the service
	 * bus
	 */

	// Create the service-object for turning off each controlled light bulb
	ProvidedLightingServiceLevel3 turnOff = new ProvidedLightingServiceLevel3(
		LightingServerURIs.TurnOff.URI);
	profiles[2] = turnOff.myProfile;

	/*
	 * create the service description #4 to be registered with the service
	 * bus
	 */

	// Create the service-object for turning on each controlled light bulb
	ProvidedLightingServiceLevel3 turnOn = new ProvidedLightingServiceLevel3(
		LightingServerURIs.TurnOn.URI);
	profiles[3] = turnOn.myProfile;
    }

    private ProvidedLightingServiceLevel3(String uri) {
	super(uri);
    }

    public String getClassURI() {
	return MY_URI;
    }
}
