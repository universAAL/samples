package org.universAAL.configurable.module;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.managers.api.ConfigurationManager;

public class ProjectActivator implements ModuleActivator {

	static ModuleContext context;
	private ConfigurationCentral module;
	
	public void start(ModuleContext ctxt) throws Exception {	
		context = ctxt;
		LogUtils.logDebug(context, getClass(), "start", "Starting.");
		/*
		 * uAAL stuff
		 */
		//create and initialize the configurableModule
		module = new ConfigurationCentral();
		//get the configuration manager
		ConfigurationManager configM = (ConfigurationManager) context.getContainer().fetchSharedObject(context, new Object[]{ConfigurationManager.class.getName()});
		//register the ConfigurableModule
		configM.register(ConfigurationCentral.configurations, module);
		LogUtils.logDebug(context, getClass(), "start", "Started.");
	}


	public void stop(ModuleContext ctxt) throws Exception {
		LogUtils.logDebug(context, getClass(), "stop", "Stopping.");
		/*
		 * close uAAL stuff
		 */
		//get the configuration manager
		ConfigurationManager configM = (ConfigurationManager) context.getContainer().fetchSharedObject(context, new Object[]{ConfigurationManager.class.getName()});
		//unregister the configurableModule
		configM.unregister(module);
		
		//other possible unloading procedures of the module like
		//module.close();
		module = null;
		LogUtils.logDebug(context, getClass(), "stop", "Stopped.");

	}

}
