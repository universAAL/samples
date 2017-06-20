/*
    Copyright 2016-2020 Carsten Stockloew

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
package org.universAAL.tutorials.service.bus.tryout;

import org.universAAL.middleware.container.JUnit.JUnitContainer;
import org.universAAL.middleware.junit.MWTestCase;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.ontology.device.DeviceOntology;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.measurement.MeasurementOntology;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.unit.UnitOntology;

public class MyTest extends MWTestCase {

	MyServiceCallee callee;
	ServiceCaller caller;

	@Override
	protected void setUp() throws Exception {
		// Initialization
		super.setUp();

		// as we do not start the platform in a regular way, we have to register
		// the required ontologies manually
		OntologyManagement.getInstance().register(mc, new LocationOntology());
		OntologyManagement.getInstance().register(mc, new ShapeOntology());
		OntologyManagement.getInstance().register(mc, new PhThingOntology());
		OntologyManagement.getInstance().register(mc, new UnitOntology());
		OntologyManagement.getInstance().register(mc, new MeasurementOntology());
		OntologyManagement.getInstance().register(mc, new DeviceOntology());

		callee = new MyServiceCallee(mc);
		caller = new DefaultServiceCaller(mc);

		// If you want to start the LogMonitor as well, add the following
		// dependency to the pom file (adapt version number to latest):
		// <dependency>
		// <groupId>org.universAAL.tools</groupId>
		// <artifactId>tools.log-monitor</artifactId>
		// <version>3.4.1-SNAPSHOT</version>
		// </dependency>
		// and start it by uncommenting the following lines:

		// org.universAAL.tools.logmonitor.Activator lm = new
		// org.universAAL.tools.logmonitor.Activator();
		// org.universAAL.tools.logmonitor.Activator.mc = mc;
		// lm.start();
		// ((JUnitContainer) mc.getContainer())
		// .registerLogListener(org.universAAL.tools.logmonitor.Activator.lm);
		// org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor.checkModule
		// = false;

		System.out.println(" - JUnit Framework started -");
	}

	/**
	 * This is our main method. You can change the request and the profile in
	 * {@link MyServiceCallee} as needed.
	 */
	public void test() {
		ServiceRequest turnOn = new ServiceRequest(new DeviceService(), null);
		turnOn.addValueFilter(new String[] { DeviceService.PROP_CONTROLS },
				new LightActuator("urn:org.universAAL.aal_space:KitchenLight"));
		turnOn.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, ValueDevice.PROP_HAS_VALUE },
				new Integer(100));
		caller.call(turnOn);

		// // an endless loop to investigate output in LogMonitor
		// while (true) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}
}
