package org.universAAL.samples.ctxtbus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

public class Activator implements BundleActivator {
    public static BundleContext context = null;
    public static CPublisher cpublisher = null;
    private static ModuleContext moduleContext = null;

    public void start(BundleContext context) throws Exception {
	Activator.context = context;
	Activator.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	cpublisher = new CPublisher(moduleContext);
	cpublisher.sendUniqueBurst(1000);
    }

    public void stop(BundleContext arg0) throws Exception {
	cpublisher.close();
    }

}
