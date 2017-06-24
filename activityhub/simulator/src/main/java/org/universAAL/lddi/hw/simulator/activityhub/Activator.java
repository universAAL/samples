/*
     Copyright 2010-2014 AIT Austrian Institute of Technology GmbH
	 http://www.ait.ac.at

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

package org.universAAL.lddi.hw.simulator.activityhub;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.lddi.hw.simulator.activityhub.util.LogTracker;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;

/**
 * This bundle is a simulator for ActivityHub events. It creates random events
 * for various ActivityHub Sensor types and sends them to the uAAL context bus.
 *
 * @author Thomas Fuxreiter (foex@gmx.at)
 */

public class Activator implements BundleActivator {

	public static BundleContext context = null;
	public static ModuleContext mc = null;
	private AHSimulator ahSimulator;
	// private AHServiceProvider serviceProvider;
	// private AHContextPublisher contextProvider;
	private LogTracker logTracker;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Activator.mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		// use a service Tracker for LogService
		logTracker = new LogTracker(context);
		logTracker.open();

		// init server
		ahSimulator = new AHSimulator(context, logTracker, mc);

	}

	public void stop(BundleContext arg0) throws Exception {
	}

}
