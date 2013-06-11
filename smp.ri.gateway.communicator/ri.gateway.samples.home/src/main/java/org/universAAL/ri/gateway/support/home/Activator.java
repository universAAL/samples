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
package org.universAAL.ri.gateway.support.home;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;

public class Activator implements BundleActivator {

    private LightingProvider1 provider1;
    private LightingProvider1 provider2;

    public static ModuleContext mc;

    public void start(final BundleContext context) throws Exception {
	mc = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });

	new Thread() {

	    @Override
	    public void run() {
		System.out.println("getting ready...");
		provider1 = new LightingProvider1(mc);
	    }

	}.start();
	
    }

    /**
     * Util logging function.
     * 
     * @param format
     *            format
     * @param args
     *            args
     */
    static void logInfo(final String format, final Object... args) {
	String callingMethod = Thread.currentThread().getStackTrace()[2]
		.getMethodName();
	System.out.format("[%s] %s%n", callingMethod, String.format(format,
		args));
	LogUtils.logInfo(Activator.mc, Activator.class.getClass(),
		callingMethod, new Object[] { String.format(format, args) },
		null);
    }

    public void stop(final BundleContext context) throws Exception {
	if (provider1 != null) {
	    provider1.close();
	    provider1 = null;
	}
    }

}
