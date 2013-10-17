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
package org.universAAL.samples.lighting.server.test;

import org.universAAL.middleware.bus.junit.ManifestTestCase;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.ontology.lighting.LightingOntology;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.samples.lighting.server.LightingProvider;
import org.universAAL.samples.lighting.server.ProvidedLightingService;
import org.universAAL.samples.lighting.server.unit_impl.MyLighting;

public class ManifestTest extends ManifestTestCase {

    @Override
    protected void setUp() throws Exception {
	super.setUp();

	OntologyManagement.getInstance().register(mc, new LocationOntology());
	OntologyManagement.getInstance().register(mc, new ShapeOntology());
	OntologyManagement.getInstance().register(mc, new PhThingOntology());
	OntologyManagement.getInstance().register(mc, new LightingOntology());
    }

    public void testCreateManifest() {
	System.out.println("-- testCreateManifest");
	// service profiles
	add("Get controlled lamps",
		"get the identifiers of all lamps that are controlled by this component.",
		ProvidedLightingService.profiles[0]);
	add("Get lamp info",
		"Get information about a specific light source, i.e. its location and brightness.",
		ProvidedLightingService.profiles[1]);
	add("Turn light off", "Turn off a specific light source.",
		ProvidedLightingService.profiles[2]);
	add("Turn light on", "Turn on a specific light source.",
		ProvidedLightingService.profiles[3]);

	// context event patterns
	ContextEventPattern[] cep = LightingProvider
		.providedEvents(new MyLighting());
	add("Light source brightness", "Changes in the brightness of a light source.", cep[0],
		true);
	add("Light source brightness",
		"Changes in the brightness of a light source, but only for lightbulbs that are in rooms.",
		cep[0], true);
	
	writeManifest();
    }
}
