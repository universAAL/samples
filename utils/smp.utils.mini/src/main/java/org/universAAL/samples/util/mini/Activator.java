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
package org.universAAL.samples.util.mini;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.support.utils.ICListener;
import org.universAAL.support.utils.ISListener;
import org.universAAL.support.utils.IUIListener;
import org.universAAL.support.utils.UAAL;
import org.universAAL.support.utils.context.Pattern;
import org.universAAL.support.utils.service.Path;
import org.universAAL.support.utils.service.mid.UtilEditor;
import org.universAAL.support.utils.ui.Forms;
import org.universAAL.support.utils.ui.low.Dialog;

public class Activator implements BundleActivator {
	// OSGi & uAAL contexts
	public static BundleContext osgiContext = null;
	public static ModuleContext context = null;
	// Namespace constant to reuse
	private static String NAMESPACE = "http://ontology.itaca.upv.es/Test.owl#";
	// The UAAL helper
	private UAAL u;

	public void start(BundleContext bcontext) throws Exception {
		// Get the uAAL module context
		Activator.osgiContext = bcontext;
		Activator.context = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { bcontext });

		// Instantiate the UAAL helper
		u = new UAAL(context);

		// Subscribe events [Subject: User, Predicate: hasLocation]
		Pattern cep = new Pattern(User.MY_URI, User.PROP_PHYSICAL_LOCATION, null);
		u.subscribeC(new ContextEventPattern[] { cep }, new ICListener() {
			public void handleContextEvent(ContextEvent event) {
				System.out.println(">>> Received Event: " + event.toString());
			}
		});

		// Send event [Subject: user1, Predicate: hasLocation, Object: loc1]
		User user1 = new User(Constants.MIDDLEWARE_LOCAL_ID_PREFIX + "saied");
		user1.setLocation(new Location(NAMESPACE + "loc1"));
		ContextEvent e = new ContextEvent(user1, User.PROP_PHYSICAL_LOCATION);
		u.sendC(e);

		// Provide service [Profiling services GET/SET/ADD/REMOVE a User]
		u.provideS(UtilEditor.getServiceProfiles(NAMESPACE, ProfilingService.MY_URI,
				Path.at(ProfilingService.PROP_CONTROLS).path, User.MY_URI), new ISListener() {
					public ServiceResponse handleCall(ServiceCall s) {
						System.out.println(">>> Received Service Call: " + s.toString());
						return new ServiceResponse(CallStatus.succeeded);
					}
				});

		// Call service [Profiling service REMOVE user1]
		ServiceResponse r = u.callS(
				UtilEditor.requestRemove(ProfilingService.MY_URI, Path.at(ProfilingService.PROP_CONTROLS).path, user1));
		System.out.println(">>> Received Service Response: " + r.getCallStatus());

		// Request UI [Output: "Successfully reached UI test", Submit:"OK"]
		Dialog d = new Dialog(user1, "UI example");
		d.add(Forms.out("Result:", "Successfully reached UI test"));
		d.addSubmit(Forms.submit(NAMESPACE + "button1", "OK"));
		u.requestUI(d, new IUIListener() {
			public void handleUIResponse(UIResponse r) {
				System.out.println(">>> Received UI Response: " + r.getSubmissionID());
			}
		});

	}

	public void stop(BundleContext arg0) throws Exception {
		// Close uAAL wrappers and free resources
		u.terminate();
	}

}
