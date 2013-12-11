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
package org.universAAL.samples.lighting.uiclient.test;

import java.util.Locale;

import org.universAAL.middleware.bus.junit.ManifestTestCase;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ontology.lighting.LightingOntology;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.samples.lighting.uiclient.LightingConsumer;
import org.universAAL.samples.lighting.uiclient.ServiceProvider;

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
	// service requests
	ServiceRequest req;
	req = add("Get all light sources", "Get a list of all light sources.",
		LightingConsumer.getAllLampsRequest(), true);

	req = add("Get light source location",
		"Get the location of a specific light source.",
		LightingConsumer.getAllLampsRequest(), true);

	req = add("Turn light source on", "Turn on a specific light source.",
		LightingConsumer.turnOnRequest("testLampUri"),
		ManifestTestCase.EXTEND_HASVALUE);
	assertTrue(req.matches(LightingConsumer.turnOnRequest("someOtherUri")));

	req = add("Turn light source off", "Turn off a specific light source.",
		LightingConsumer.turnOffRequest("testLampUri"),
		ManifestTestCase.EXTEND_HASVALUE);
	assertTrue(req.matches(LightingConsumer.turnOffRequest("someOtherUri")));

	req = add("Dim light source",
		"Dim a specific light source to a given value.",
		LightingConsumer.dimRequest("testLampUri", 50), true);
	assertTrue(LightingConsumer.dimRequest("someOtherUri", 0).matches(req));
	assertTrue(LightingConsumer.dimRequest("someOtherUri", 50).matches(req));
	assertTrue(LightingConsumer.dimRequest("someOtherUri", 100)
		.matches(req));

	// service profiles
	add("Main menu callee",
		"Capability for being called from the main menu.",
		ServiceProvider.myProfile);

	// context event patterns
	add("light sources", "All changes for light sources.",
		LightingConsumer.getContextSubscriptionParams()[0], false);

	// ui requests
	UIRequest uireq = new UIRequest(new Resource(), new Form(null),
		LevelRating.middle, Locale.ENGLISH, PrivacyLevel.insensible);
	add("Lighting UI", "User Interface to control light sources.", uireq);

	writeManifest();
    }
}
