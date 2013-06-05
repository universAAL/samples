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
package org.universAAL.samples.utils.server;

import java.util.ArrayList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.location.indoor.Room;
import org.universAAL.support.utils.context.mid.UtilPublisher;

public class Activator implements BundleActivator {
    // Declare constants
    public static final String SERVER_NAMESPACE = "http://ontology.universAAL.org/SimpleLightServ.owl#";
    private static final String PROVIDER_URI = SERVER_NAMESPACE+"MyProvider";
    public static final String LIGHT_URI_PREFIX = SERVER_NAMESPACE + "MyLight";
    private static final String LIGHT_LOC_PREFIX = SERVER_NAMESPACE + "MyLocation";
    // OSGi & uAAL contexts
    public static BundleContext osgiContext = null;
    public static ModuleContext context = null;
    // uAAL wrappers: Context Publisher and Service Callee
    protected static UtilPublisher publisher;
    protected static CalleeExample callee;
    // The lights the app controls. It uses the ontology model directly but it could be any imaginable model.
    protected static ArrayList<LightController> myLights=new ArrayList<LightController>(4);
    
    // Initialize the lights the app controls.
    static {
	for (int i = 0; i < 4; i++) {
	    LightController light = new LightController(LIGHT_URI_PREFIX + i);
	    light.setLocation(new Room(LIGHT_LOC_PREFIX + i));
	    light.setValue(0);
	    myLights.add(light);
	}
    }

    // Start the wrapping to uAAL
    public void start(BundleContext bcontext) throws Exception {
	// Get the uAAL module context
	Activator.osgiContext = bcontext;
	Activator.context = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { bcontext });
	
	// Register the Context Publisher as controller and to send events about light brightness
	publisher = new UtilPublisher(context, PROVIDER_URI,
		ContextProviderType.controller, 
		LightController.MY_URI, 
		LightController.PROP_HAS_VALUE,
		TypeMapper.getDatatypeURI(Integer.class));
	// Register the Service Callee. The provided services are defined therein.
	callee = new CalleeExample(context);
    }

    // Stop the wrapping to uAAL
    public void stop(BundleContext arg0) throws Exception {
	callee.close();
	publisher.close();
    }

}
