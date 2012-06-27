package org.universAAL.lddi.samples.activityhub.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

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
		mc = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { context });

		ahc = new ActivityHubClient(this);

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
		if (serviceConsumer != null)
			serviceConsumer.deleteGui();
		thread.interrupt();
		// uninstall myself from uAALBundleContainer ??
		//uAALBundleContainer.THE_CONTAINER.unregister...
//		System.out.println("Stoppable: " + mc.canBeStopped(mc) );	-> false
//		System.out.println("Uninstallable: " + mc.canBeUninstalled(mc) );	->false
		// TODO how to stop/uninstall a bundle ???
		
		if ( mc.stop(mc) ) //mc.uninstall(mc)
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
	class MyThread implements Runnable{
		public MyThread() {
		}
		public void run() {
			serviceConsumer = new MyActivityHubServiceConsumer(mc,ahc);
			contextListener = new MyActivityHubContextListener(mc,ahc);
		}
	}

}