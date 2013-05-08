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
