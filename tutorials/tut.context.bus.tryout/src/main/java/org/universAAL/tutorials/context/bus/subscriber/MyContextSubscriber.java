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
package org.universAAL.tutorials.context.bus.subscriber;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;

/**
 * Our tutorial application registers as a subscriber for certain context events
 * at the context bus. The events we are interested in are defined in a (set of)
 * context event pattern(s). The method
 * {@link #handleContextEvent(ContextEvent)} is our callback method.
 *
 * @author Carsten Stockloew
 */
public class MyContextSubscriber extends ContextSubscriber {

	// to check that the event was received, used only by the JUnit test to end
	// the test
	public boolean evtReceived = false;

	/**
	 * Create the context event patterns that describe which context events we
	 * want to receive.
	 *
	 * @return an array of context event patterns.
	 */
	public static ContextEventPattern[] getContextSubscriptionParams() {
		// I am interested in all events that the brightness of a light source
		// has changed. According to the device ontology the event describes a
		// triple of the form:
		// LightActuator hasValue x

		// We start with a general context event pattern that we will restrict
		// further in the following lines..
		ContextEventPattern cep = new ContextEventPattern();
		// The subject of the event must be of type LightActuator
		cep.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightActuator.MY_URI));
		// The predicate of the event must be the value
		// ValueDevice.PROP_HAS_VALUE
		cep.addRestriction(MergedRestriction.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
				ValueDevice.PROP_HAS_VALUE));

		// We can create more patterns here and return everything in an array.
		return new ContextEventPattern[] { cep };
	}

	public MyContextSubscriber(ModuleContext context) {
		// The constructor register us to the context bus for some context event
		// patterns.
		super(context, getContextSubscriptionParams());
	}

	/** @see ContextSubscriber#handleContextEvent(ContextEvent) */
	public void handleContextEvent(ContextEvent event) {
		// This method is called when the desired context event is published by
		// a context publisher. In this example, we simply log the event and its
		// content.
		LogUtils.logInfo(Activator.mc, MyContextSubscriber.class, "handleContextEvent",
				new Object[] { "Received context event:\n", "    Subject     = ", event.getSubjectURI(), "\n",
						"    Subject type= ", event.getSubjectTypeURI(), "\n", "    Predicate   = ",
						event.getRDFPredicate(), "\n", "    Object      = ", event.getRDFObject() },
				null);
		evtReceived = true;
	}

	/** @see ContextSubscriber#communicationChannelBroken() */
	public void communicationChannelBroken() {
		// This method is called when the connection to the context bus is
		// broken. We don't do anything here, but applications should handle
		// this case.
	}
}
