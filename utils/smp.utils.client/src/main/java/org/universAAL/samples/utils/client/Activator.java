/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.samples.utils.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.ontology.device.LightController;

public class Activator implements BundleActivator {
    // Declare constants
    public static final String CLIENT_NAMESPACE = "http://ontology.universAAL.org/SimpleLightClient.owl#";
    public static final String CLIENT_URL = "http://www.samples.org";
    public static final String CLIENT_APPNAME = "Lighting Client Example";
    // OSGi & uAAL contexts
    public static BundleContext osgiContext = null;
    public static ModuleContext context = null;
    // uAAL wrappers: Service Caller, Context Subscriber and UI Caller
    protected static ServiceCaller caller;
    protected static SubscriberExample subscriber;
    protected static UIExample ui;

    // Start the wrapping to uAAL
    public void start(BundleContext bcontext) throws Exception {
	// Get the uAAL module context
	Activator.osgiContext = bcontext;
	Activator.context = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { bcontext });

	// Create a default Service Caller, enough to send service calls
	caller = new DefaultServiceCaller(context);
	// Register the UI Caller and add its button to Main Menu
	ui = new UIExample(context, CLIENT_NAMESPACE, CLIENT_URL, CLIENT_APPNAME);
	// Register the Context Subscriber to receive events about light brightness
	subscriber = new SubscriberExample(context, LightController.MY_URI, LightController.PROP_HAS_VALUE, null);
    }

    // Stop the wrapping to uAAL
    public void stop(BundleContext arg0) throws Exception {
	subscriber.close();
	ui.close();
	caller.close();
    }

}
