package org.universAAL.lddi.samples.device.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * Activator for Device Client sample bundle
 * 
 * @author Thomas Fuxreiter
 *
 */
public class Activator implements BundleActivator {

    public static ModuleContext mc;
	private static DeviceClient dc;
//    public DeviceServiceConsumer serviceConsumer;
    private DeviceContextListener contextListener;
    private Thread thread;

	public void start(BundleContext context) throws Exception {
		mc = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { context });

		try {
			dc = new DeviceClient(this);
		} catch (java.awt.HeadlessException ex) {
		    LogUtils.logInfo(mc,Activator.class,"Activator",
			    new Object[] { "client activates GUI-off mode because of no screen access" },
			    null);
		}

		// start uAAL bus consumer threads
		MyThread runnable = new MyThread(); 
		thread=new Thread(runnable);
		thread.start();
//		new Thread() {
//			public void run() { 
//				new MyActivityHubContextListener(mc);
//				new MyActivityHubServiceConsumer(mc);
//			}
//		}.start();
	}

	public void stop(BundleContext context) throws Exception {
//		if (serviceConsumer != null)
//			serviceConsumer.deleteGui();
		thread.interrupt();
		// uninstall myself from uAALBundleContainer ??
		//uAALBundleContainer.THE_CONTAINER.unregister...
//		System.out.println("Stoppable: " + mc.canBeStopped(mc) );	-> false
//		System.out.println("Uninstallable: " + mc.canBeUninstalled(mc) );	->false
		// TODO how to stop/uninstall a bundle ???
		
		if ( mc.stop(mc) ) //mc.uninstall(mc)
			System.out.println("smp.device.client bundle successfully stopped from uAALBundleContainer!");
		else
			System.out.println("Problem stopping smp.device.client bundle in uAALBundleContainer!");
	}

	/**
	 * Runnable helper class for starting ActivityHubServiceProvider
	 * 
	 * @author fuxreitert
	 *
	 */
	class MyThread implements Runnable{
		public MyThread() {
		}
		public void run() {
//			serviceConsumer = new DeviceServiceConsumer(mc,dc);
			contextListener = new DeviceContextListener(mc,dc);
		}
	}

}