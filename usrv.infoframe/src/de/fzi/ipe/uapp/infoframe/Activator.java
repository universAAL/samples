package de.fzi.ipe.uapp.infoframe;

import java.io.File;
import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;
import de.fzi.ipe.uapp.infoframe.listeners.AddCalendarPluginListener;
import de.fzi.ipe.uapp.infoframe.listeners.AddMailPluginListener;
import de.fzi.ipe.uapp.infoframe.listeners.AddWeatherPluginListener;
import de.fzi.ipe.uapp.infoframe.listeners.ModListener;
import de.fzi.ipe.uapp.infoframe.listeners.factories.SizeDependencyListenerFactory;
import de.fzi.ipe.uapp.infoframe.validation.factories.RowValidatorFactory;

public class Activator implements BundleActivator {

	HttpServiceTracker http;
	ConfigurationDefinitionRegistry configReg;
	URL configURL;
	
	public void start(BundleContext context) throws Exception {
		createDirectory();
		
		configURL = FrameworkUtil.getBundle(this.getClass()).getResource("/config/complexInfoframe.xml");
		ServiceReference reference = context.getServiceReference(ConfigurationDefinitionRegistry.class.getName());
		configReg = (ConfigurationDefinitionRegistry) context.getService(reference);
		// register the configuration definition file.
		configReg.registerConfigurationDefinition(configURL);
		
		//register the Listeners and Validators as osgi services
		context.registerService(AddCalendarPluginListener.class.getName(), new AddCalendarPluginListener(), null);
		context.registerService(AddMailPluginListener.class.getName(), new AddMailPluginListener(), null);
		context.registerService(ModListener.class.getName(), new ModListener(), null);
		context.registerService(SizeDependencyListenerFactory.class.getName(), new SizeDependencyListenerFactory(), null);
		context.registerService(RowValidatorFactory.class.getName(), new RowValidatorFactory(), null);
		context.registerService(AddWeatherPluginListener.class.getName(), new AddWeatherPluginListener(), null);
		http = new HttpServiceTracker(context);
		http.open();
	}

	private void createDirectory() {
		File directory = new File(System.getenv("systemdrive")+"\\rss");
		if(!directory.exists()){
			directory.mkdir();
		}
	}

	public void stop(BundleContext context) throws Exception {
		http.close();
		configReg.unregisterConfigurationDefinition(configURL);
	}
}
