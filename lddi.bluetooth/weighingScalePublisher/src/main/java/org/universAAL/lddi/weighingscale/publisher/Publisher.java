/*
    Copyright 2007-2014 TSB, http://www.tsbtecnologias.es
    Technologies for Health and Well-being - Valencia, Spain

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
package org.universAAL.lddi.weighingscale.publisher;
/**
 * x073 Continua agent publisher (agent events will be published over uAAL bus)
 * 
 * @author Angel Martinez-Cavero (thx to Miguel-Angel Llorente)
 * @version 0
 *  
 * TSB Technologies for Health and Well-being
 */

// Imports
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.ontology.healthmeasurement.owl.PersonWeight;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.measurement.Measurement;
import org.universAAL.ontology.personalhealthdevice.WeighingScale;

// Main class
public class Publisher {

	// Atributes
	// Default context publisher
	private ContextPublisher cp;
	// Context provider info (provider type)
	ContextProvider cpInfo = new ContextProvider();
	//
	ModuleContext mc;

	// Constructor

	/**
	 * Publisher contructor
	 * 
	 * @param context
	 *            - framework bundle context
	 */
	public Publisher(BundleContext context) {
		// Instantiate the context provider info with a valid provider URI
		cpInfo = new ContextProvider("http://www.tsbtecnologias.es/ContextProvider.owl#weighingScalePublisher");
		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });
		// Set to type gauge (only publishes data information it senses)
		cpInfo.setType(ContextProviderType.gauge);
		// Set the provided events to unknown with an empty pattern
		cpInfo.setProvidedEvents(new ContextEventPattern[] { new ContextEventPattern() });
		// Create and register the context publisher
		cp = new DefaultContextPublisher(mc, cpInfo);
	}

	// Methods
	/**
	 * Publish weighting scale events to uAAL bus.
	 * 
	 * @param weight
	 *            - weight measured value
	 */
	public void publishEvent(String weight) {
		System.out.println("[TEST] WS event published to uaal context bus");
		PersonWeight w = new PersonWeight();
		w.setProperty(Measurement.PROP_VALUE, Float.parseFloat(weight));
		WeighingScale ws = new WeighingScale("http://www.tsbtecnologias.es/WeighingScale.owl#WeighingScale");
		ws.setLocation(new Location("http://www.tsbtecnologias.es/location.owl#TSBlocation", "TSB"));
		ws.setValue(w);
		System.out.println("Sending weight");
		cp.publish(new ContextEvent(ws, WeighingScale.PROP_HAS_VALUE));
		System.out.println("Sent weight");
	}
}