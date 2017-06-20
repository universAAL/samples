/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science 

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
package org.universAAL.samples.lighting.server_simple;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.api.SimpleServiceRegistrator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel1;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel2;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel3;

public class Activator implements BundleActivator {

	public static ModuleContext mc;
	private SimpleServiceRegistrator ssr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(final BundleContext context) throws Exception {
		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });
		try {
			// ssr = new SimpleServiceRegistrator(mc);
			// LightingInterfaceLevel1 providerLevel1 = new
			// LightingSimplifiedServiceLevel1();
			// ssr.registerService(providerLevel1);
			// LightingInterfaceLevel2 providerLevel2 = new
			// LightingSimplifiedServiceLevel2();
			// ssr.registerService(providerLevel2);
			// LightingInterfaceLevel3 providerLevel3 = new
			// LightingSimplifiedServiceLevel3();
			// ssr.registerService(providerLevel3);
		} catch (Error er) {
			er.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (ssr != null) {
			ssr.unregisterAll();
		}
	}
}
