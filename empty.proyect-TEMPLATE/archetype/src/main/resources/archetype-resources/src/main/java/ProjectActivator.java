#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

public class ProjectActivator implements BundleActivator {

	static ModuleContext context;

	public void start(BundleContext arg0) throws Exception {	
		context = uAALBundleContainer.THE_CONTAINER
                .registerModule(new Object[] {arg0});	
		context.logDebug("Initialising Project", null);

		/*
		 * uAAL stuff
		 */
		context.logInfo("Project started", null);
	}


	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
