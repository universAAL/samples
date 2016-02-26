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
package org.universAAL.tutorials.context.bus.publisher;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;

/**
 * The module activator handles the starting and stopping of this module. It is
 * similar to the OSGi BundleActivator.
 * 
 * In our case, we simply want to publish a context event, so we do this
 * directly in the start method.
 * 
 * @author Carsten Stockloew
 */
public class Activator implements ModuleActivator {

    public void start(ModuleContext mc) throws Exception {
	// Set up the context publisher by providing some information about
	// ourselves. Especially, we need a URI that uniquely identifies this
	// component and the provider type (one of: controller, gauge,
	// reasoner).
	ContextProvider provInfo = new ContextProvider(
		"urn:org.universAAL.tutorial:tut.provider");
	provInfo.setType(ContextProviderType.controller);
	ContextPublisher cp = new DefaultContextPublisher(mc, provInfo);

	// Create a context event telling the system that the brightness of a
	// light source has changed. According to the device ontology the event
	// describes a triple of the form:
	// LightActuator hasValue x
	// In this example, the brightness of the kitchen light was dimmed to
	// 100% (= the kitchen light was turned on)
	ContextEvent evt = new ContextEvent(new LightActuator(
		"urn:org.universAAL.aal_space:KitchenLight"),
		ValueDevice.PROP_HAS_VALUE, 100);
	cp.publish(evt);

	// Close our context publisher and free all resources. The publisher
	// should be re-used if multiple events need to be published.
	cp.close();
    }

    public void stop(ModuleContext arg0) throws Exception {
    }
}
