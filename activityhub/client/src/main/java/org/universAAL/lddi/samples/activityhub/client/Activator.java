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

package org.universAAL.lddi.samples.activityhub.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * Activator for ActivityHub Client demo bundle
 * 
 * @author Thomas Fuxreiter
 *
 */
public class Activator implements BundleActivator {

	public static ModuleContext mc;
	private static ActivityHubClient ahc;
	public MyActivityHubServiceConsumer serviceConsumer;
	private MyActivityHubContextListener contextListener;
	private Thread thread;

	public void start(BundleContext context) throws Exception {
		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		try {
			ahc = new ActivityHubClient(this);
		} catch (java.awt.HeadlessException ex) {
			LogUtils.logInfo(mc, Activator.class, "Activator",
					new Object[] { "client activates GUI-off mode because of no screen access" }, null);
		}

		// start uAAL bus consumer threads
		MyThread runnable = new MyThread();
		thread = new Thread(runnable);
		thread.start();
		// new Thread() {
		// public void run() {
		// new MyActivityHubContextListener(mc);
		// new MyActivityHubServiceConsumer(mc);
		// }
		// }.start();
	}

	public void stop(BundleContext context) throws Exception {
		if (serviceConsumer != null)
			serviceConsumer.deleteGui();
		thread.interrupt();
		// uninstall myself from uAALBundleContainer ??
		// uAALBundleContainer.THE_CONTAINER.unregister...
		// System.out.println("Stoppable: " + mc.canBeStopped(mc) ); -> false
		// System.out.println("Uninstallable: " + mc.canBeUninstalled(mc) );
		// ->false
		// TODO how to stop/uninstall a bundle ???

		if (mc.stop(mc)) // mc.uninstall(mc)
			System.out.println("smp.activityhub.client bundle successfully stopped from uAALBundleContainer!");
		else
			System.out.println("Problem stopping smp.activityhub.client bundle in uAALBundleContainer!");
	}

	/**
	 * Runnable helper class for starting ActivityHubServiceProvider
	 * 
	 * @author fuxreitert
	 *
	 */
	class MyThread implements Runnable {
		public MyThread() {
		}

		public void run() {
			serviceConsumer = new MyActivityHubServiceConsumer(mc, ahc);
			contextListener = new MyActivityHubContextListener(mc, ahc);
		}
	}

}