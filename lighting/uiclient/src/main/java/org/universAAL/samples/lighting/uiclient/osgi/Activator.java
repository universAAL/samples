package org.universAAL.samples.lighting.uiclient.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.samples.lighting.uiclient.SharedResources;

public class Activator implements BundleActivator {

    SharedResources sr;

    public void start(BundleContext context) throws Exception {
	SharedResources.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });

	sr = new SharedResources();
	sr.start();
    }

    public void stop(BundleContext context) throws Exception {
    }
}
