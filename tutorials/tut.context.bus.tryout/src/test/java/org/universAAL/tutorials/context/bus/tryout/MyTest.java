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
package org.universAAL.tutorials.context.bus.tryout;

import org.universAAL.middleware.bus.msg.BusMessage;
import org.universAAL.middleware.container.JUnit.JUnitContainer;
import org.universAAL.middleware.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.JUnit.JUnitModuleContext.LogLevel;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.junit.MWTestCase;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.ontology.device.DeviceOntology;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.measurement.MeasurementOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.unit.UnitOntology;
import org.universAAL.tutorials.context.bus.subscriber.MyContextSubscriber;

public class MyTest extends MWTestCase {

	MyContextSubscriber subscriber;
	ContextPublisher publisher;

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
		//((JUnitModuleContext) mc).setLogLevel(LogLevel.INFO);
		BusMessage.setMessageContentSerializer(mcs.get("text/turtle"));

		subscriber = new MyContextSubscriber(mc);

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
		// ((JUnitContainer)
		// mc.getContainer()).registerLogListener(org.universAAL.tools.logmonitor.Activator.lm);
		// org.universAAL.tools.logmonitor.service_bus_matching.LogMonitor.checkModule
		// = false;

		System.out.println(" - JUnit Framework started -");
	}

	/**
	 * This is our main method. You can change the request and the profile in
	 * {@link MyServiceCallee} as needed.
	 */
	public void test() {
		// Set up the context publisher by providing some information about
		// ourselves. Especially, we need a URI that uniquely identifies this
		// component and the provider type (one of: controller, gauge,
		// reasoner).
		ContextProvider provInfo = new ContextProvider("urn:org.universAAL.tutorial:tut.provider");
		provInfo.setType(ContextProviderType.controller);
		publisher = new DefaultContextPublisher(mc, provInfo);

		// Create a context event telling the system that the brightness of a
		// light source has changed. According to the device ontology the event
		// describes a triple of the form:
		// LightActuator hasValue x
		// In this example, the brightness of the kitchen light was dimmed to
		// 100% (= the kitchen light was turned on)
		ContextEvent evt = new ContextEvent(new LightActuator("urn:org.universAAL.space:KitchenLight"),
				ValueDevice.PROP_HAS_VALUE, 100);
		publisher.publish(evt);

		// Close our context publisher and free all resources. The publisher
		// should be re-used if multiple events need to be published.
		publisher.close();

		// a loop to investigate output; stops after 5 seconds or when an event
		// was received
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (subscriber.evtReceived)
				break;
		}

		// an endless loop to investigate output in LogMonitor
		// while (true) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}
}
