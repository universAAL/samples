package org.universAAL.samples.lighting.server;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.api.SimpleServiceRegistrator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
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
	mc = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	try {
//	    ssr = new SimpleServiceRegistrator(mc);
//	    LightingInterfaceLevel1 providerLevel1 = new LightingSimplifiedServiceLevel1();
//	    ssr.registerService(providerLevel1);
//	    LightingInterfaceLevel2 providerLevel2 = new LightingSimplifiedServiceLevel2();
//	    ssr.registerService(providerLevel2);
//	    LightingInterfaceLevel3 providerLevel3 = new LightingSimplifiedServiceLevel3();
//	    ssr.registerService(providerLevel3);
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
