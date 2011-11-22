package org.universAAL.platform.ui.tester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	public static BundleContext context = null;
	public static SCallee ncallee = null;
	public static ISubscriber ninput = null;
	public static OPublisher noutput = null;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		ncallee = new SCallee(Activator.context);
		ninput = new ISubscriber(Activator.context);
		noutput = new OPublisher(Activator.context);
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}
