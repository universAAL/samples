/*
	Copyright 2013 CERTH, http://www.certh.gr

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
package org.universAAL.samples.soap.cxf.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.universAAL.samples.soap.cxf.service.ServiceInterface;

public class Activator implements BundleActivator {

	private ServiceTracker _tracker;

	public void start(BundleContext context) throws Exception {
		System.out.println(" Activator: starting remote client");
		_tracker = new ServiceTracker(context, ServiceInterface.class.getName(), null) {
			@Override
			public Object addingService(ServiceReference reference) {
				Object svc = context.getService(reference);
				System.out.println("Remote client: Trying to retrieve ServiceReference...");
				if (svc instanceof ServiceInterface) {
					System.out.println("Remote client: ServiceReference retrieved");
					init((ServiceInterface) svc);
				} else {
					System.out.println("Remote client: I took ...");
				}

				return super.addingService(reference);
			}
		};
		_tracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		_tracker.close();
	}

	private void init(ServiceInterface srv) {

		System.out.println(" Remote client: interacting with server");

		System.out.println("\n turnOff Request: \n");
		String lampURI = "http://ontology.igd.fhg.de/LightingServer.owl#controlledLamp0";
		srv.turnOffLamp(lampURI);

		// System.out.println("\n getALLLamps: \n");
		// String tStrgetAllLamps="@prefix ns:
		// <http://ontology.igd.fhg.de/LightingConsumer.owl#> .@prefix pvn:
		// <http://ontology.universAAL.org/uAAL.owl#> .@prefix :
		// <http://www.daml.org/services/owl-s/1.1/Process.owl#> ._:BN000000 a
		// pvn:ServiceRequest ; pvn:theServiceCaller
		// <urn:org.universAAL.space:test_environment#13b66363885@joe-PC+73640755_6>
		// ; pvn:requiredResult [ :withOutput ( [ a :OutputBinding ; :toParam
		// ns:controlledLamps ; :valueForm "+"\"\"\""+" @prefix :
		// <http://ontology.universAAL.org/Service.owl#> . _:BN000000 a
		// :PropertyPath ; :thePath (
		// <http://ontology.universaal.org/Lighting.owl#controls> ) .
		// "+"\"\"\""+"^^<http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral>
		// ] ) ; a :Result ] ; pvn:requestedService [ a
		// <http://ontology.universaal.org/Lighting.owl#Lighting> ]
		// .ns:controlledLamps a :Output .\n";
		// srv.getTurtleServiceReq(tStrgetAllLamps);

		// System.out.println("\n turnOn Lamp0: \n");
		// String tStrturnOnLamp0="@prefix owl: <http://www.w3.org/2002/07/owl#>
		// .@prefix ns: <http://ontology.igd.fhg.de/LightingServer.owl#>
		// .@prefix ns1: <http://ontology.universaal.org/Lighting.owl#> .@prefix
		// psn: <http://ontology.universAAL.org/Service.owl#> .@prefix ns2:
		// <http://www.daml.org/services/owl-s/1.1/Process.owl#> .@prefix :
		// <http://ontology.universAAL.org/uAAL.owl#> ._:BN000000 a
		// :ServiceRequest ; :theServiceCaller
		// <urn:org.universAAL.space:test_environment#13b66559ea3@joe-PC+5f6f4b69_7>
		// ; :requiredResult [ a ns2:Result ; ns2:hasEffect ( [
		// psn:affectedProperty [ a psn:PropertyPath ; psn:thePath (
		// ns1:controls ns1:srcBrightness ) ] ; a psn:ChangeEffect ;
		// psn:propertyValue 100 ] ) ] ; :requestedService [ a ns1:Lighting ;
		// :instanceLevelRestrictions ( [ owl:hasValue ns:controlledLamp0 ; a
		// owl:Restriction ; owl:onProperty ns1:controls ] ) ;
		// :numberOfValueRestrictions 1 ] .ns:controlledLamp0 a ns1:LightSource
		// , <http://ontology.universAAL.org/Device.owl#Device> , :PhysicalThing
		// .\n";
		// srv.getTurtleServiceReq(tStrturnOnLamp0);
	}
}
