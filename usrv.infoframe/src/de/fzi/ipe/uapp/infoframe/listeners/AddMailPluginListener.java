package de.fzi.ipe.uapp.infoframe.listeners;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;

/**
 * This listener activates and disables the mail fields depending on the mail field value.
 * @author Sebastian Schoebinger
 *
 */
public class AddMailPluginListener implements
		OnConfigurationChangedListener {
	
	@Override
	public void configurationChanged(ConfigOptionRegistry registry,
			ConfigurationOption option) {
		if("1".equals(option.getValue())){
			registry.getConfigOptionForId("mailProtocol").setIsActive(true);
			registry.getConfigOptionForId("mailServer").setIsActive(true);
			registry.getConfigOptionForId("mailUsername").setIsActive(true);
			registry.getConfigOptionForId("mailPassword").setIsActive(true);
			registry.getConfigOptionForId("mailMsgCount").setIsActive(true);
		}else{
			registry.getConfigOptionForId("mailProtocol").setIsActive(false);
			registry.getConfigOptionForId("mailServer").setIsActive(false);
			registry.getConfigOptionForId("mailUsername").setIsActive(false);
			registry.getConfigOptionForId("mailPassword").setIsActive(false);
			registry.getConfigOptionForId("mailMsgCount").setIsActive(false);
		}
	}

}
