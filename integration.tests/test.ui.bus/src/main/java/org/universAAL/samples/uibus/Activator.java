package org.universAAL.samples.uibus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
//import org.universAAL.middleware.input.DefaultInputPublisher;
//import org.universAAL.middleware.input.InputPublisher;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.profile.User;

public class Activator implements BundleActivator {
    public static BundleContext context = null;
    protected static GUIPanel panel;
    protected static MessageContentSerializer ser;
//    public static ISubscriber uinput;
    public static OPublisher uoutput;
//    public static InputPublisher uipub;
    public static User sampleUser;
    static final String sampleUserURI = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
	    + "saied";
    private static ModuleContext moduleContext = null;

    public void start(BundleContext context) throws Exception {
	Activator.context = context;
	Activator.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
	ser = (MessageContentSerializer) context.getService(context
		.getServiceReference(MessageContentSerializer.class.getName()));
//	uinput = new ISubscriber(moduleContext);
	uoutput = new OPublisher(moduleContext);
//	uipub = new DefaultInputPublisher(moduleContext);
	sampleUser = new User(sampleUserURI);
	panel = new GUIPanel(context);
	panel.setVisible(true);
    }

    public void stop(BundleContext arg0) throws Exception {
//	uinput.close();
	uoutput.close();
	panel.dispose();
    }

}
