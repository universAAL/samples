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
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.rdf.ResourceFactory;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.lighting.simple.LightingServerURIs;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.phThing.PhysicalThing;

public class ProvidedLightingServiceLevel1 extends Lighting {

	public static final String LIGHTING_SERVER_NAMESPACE = "http://ontology.igd.fhg.de/LightingServer.owl#";
	public static final String MY_URI = LIGHTING_SERVER_NAMESPACE + "LightingService";

	static final ServiceProfile[] profiles = new ServiceProfile[4];
	private static Hashtable serverLightingRestrictions = new Hashtable();
	static {
		OntologyManagement.getInstance().register(Activator.mc,
				new SimpleOntology(MY_URI, Lighting.MY_URI, new ResourceFactory() {
					public Resource createInstance(String classURI, String instanceURI, int factoryIndex) {
						return new ProvidedLightingServiceLevel1(instanceURI);
					}
				}));

		String[] ppControls = new String[] { Lighting.PROP_CONTROLS };
		String[] ppBrightness = new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS };

		ProvidedLightingServiceLevel1 getControlledLamps = new ProvidedLightingServiceLevel1(
				LightingServerURIs.GetControlledLamps.URI);
		getControlledLamps.addOutput(LightingServerURIs.GetControlledLamps.Output.CONTROLLED_LAMPS, LightSource.MY_URI,
				0, 0, ppControls);
		profiles[0] = getControlledLamps.myProfile;

		ProvidedLightingServiceLevel1 getLampInfo = new ProvidedLightingServiceLevel1(
				LightingServerURIs.GetLampInfo.URI);
		getLampInfo.addFilteringInput(LightingServerURIs.GetLampInfo.Input.LAMP_URI, LightSource.MY_URI, 1, 1,
				ppControls);
		getLampInfo.addOutput(LightingServerURIs.GetLampInfo.Output.LAMP_BRIGHTNESS,
				TypeMapper.getDatatypeURI(Integer.class), 1, 1, ppBrightness);
		getLampInfo.addOutput(LightingServerURIs.GetLampInfo.Output.LAMP_LOCATION, Location.MY_URI, 1, 1,
				new String[] { Lighting.PROP_CONTROLS, PhysicalThing.PROP_PHYSICAL_LOCATION });
		profiles[1] = getLampInfo.myProfile;

		ProvidedLightingServiceLevel1 turnOff = new ProvidedLightingServiceLevel1(LightingServerURIs.TurnOff.URI);
		turnOff.addFilteringInput(LightingServerURIs.TurnOff.Input.LAMP_URI, LightSource.MY_URI, 1, 1, ppControls);
		turnOff.myProfile.addChangeEffect(ppBrightness, new Integer(0));
		profiles[2] = turnOff.myProfile;

		ProvidedLightingServiceLevel1 turnOn = new ProvidedLightingServiceLevel1(LightingServerURIs.TurnOn.URI);
		turnOn.addFilteringInput(LightingServerURIs.TurnOn.Input.LAMP_URI, LightSource.MY_URI, 1, 1, ppControls);
		turnOn.myProfile.addChangeEffect(ppBrightness, new Integer(100));
		profiles[3] = turnOn.myProfile;
	}

	private ProvidedLightingServiceLevel1(String uri) {
		super(uri);
	}

	public String getClassURI() {
		return MY_URI;
	}
}
