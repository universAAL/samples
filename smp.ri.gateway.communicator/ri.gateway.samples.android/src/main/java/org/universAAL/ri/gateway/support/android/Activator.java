package org.universAAL.ri.gateway.support.android;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ri.gateway.communicator.service.RemoteSpacesManager;
import org.universAAL.ri.gateway.eimanager.ImportEntry;

public class Activator implements BundleActivator {

    static ModuleContext mc;
    static LightingConsumer lc;
    private static RemoteSpacesManager remoteManager;

    public void start(final BundleContext context) throws Exception {
    	
    	
    
	mc = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	
	//starting the LightingConsumer
	new Thread() {
	    @Override
	    public void run() {
		lc = new LightingConsumer(mc);
	    }
	}.start();

	//acquiring the RemoteSpacesManager from the container
	//it is registered in the bus by ri.gateway.communicator bundle
	remoteManager = (RemoteSpacesManager) mc.getContainer()
		.fetchSharedObject(mc,
			new Object[] { RemoteSpacesManager.class.getName() });

	while (lc == null) {
	    Thread.sleep(1000);
	}
	
	new Thread(new Runnable(){

		public void run() {
			
			//importing services published in remote space (address defined in .properties file in confadmin dir)
			ImportEntry serviceEntry = null;
			while(serviceEntry == null){
				try {
					System.out.println("Trying to import remote service ...");
					serviceEntry = remoteManager.importRemoteService(
							lc.caller,
							Lighting.MY_URI, LightingService1.LIGHTING_SERVER_NAMESPACE);
				} catch (Exception e) {
					System.out.println("Error during import: " + e.getMessage() + ". Waiting 5 sec ...");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						//intentionally left blank
					}
				}
			}
			System.out.println("Remote service imported!!!");
			
			//importing context event published in remote space
			ImportEntry contextEntry = null;
			while(contextEntry == null){
				try {
					System.out.println("Trying to import remote context ...");
					contextEntry = remoteManager.importRemoteContextEvents(
							lc,LightingConsumer.getContextSubscriptionParams());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error during import: " + e.getMessage() + ". Waiting 5 sec ...");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						//intentionally left blank
					}
				}
			}
			System.out.println("Remote context imported!!!");
		}
		
	}).start();
	
    }

    public void stop(final BundleContext arg0) throws Exception {

    }

}
