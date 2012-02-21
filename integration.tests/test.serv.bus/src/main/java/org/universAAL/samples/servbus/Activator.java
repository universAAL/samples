package org.universAAL.samples.servbus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;

public class Activator implements BundleActivator {
    public static BundleContext context = null;
    protected static GUIPanel panel;
    protected static MessageContentSerializer ser;
    public static ServiceCaller scaller;
    private static ModuleContext moduleContext = null;

    public void start(BundleContext context) throws Exception {
	Activator.context = context;
	ser = (MessageContentSerializer) context.getService(context
		.getServiceReference(MessageContentSerializer.class.getName()));
	Activator.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	scaller = new DefaultServiceCaller(moduleContext);
	// caller.addAvailabilitySubscription(this, getListLampsRequest());
	panel = new GUIPanel(context);
	panel.setVisible(true);
    }

    public void stop(BundleContext arg0) throws Exception {
	scaller.close();
	panel.dispose();
    }

}
