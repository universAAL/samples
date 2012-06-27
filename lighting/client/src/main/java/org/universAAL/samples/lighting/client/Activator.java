package org.universAAL.samples.lighting.client;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.uAALModuleActivator;

public class Activator implements uAALModuleActivator {

    public static ModuleContext mc;

    public void start(final ModuleContext mc) throws Exception {
	Activator.mc = mc;
	new Thread() {
	    public void run() {
		new LightingConsumer(mc);
	    }
	}.start();
	
    }

    public void stop(ModuleContext mc) throws Exception {
	
    }
}
