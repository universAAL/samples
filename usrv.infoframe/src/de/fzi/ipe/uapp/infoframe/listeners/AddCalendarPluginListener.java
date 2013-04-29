package de.fzi.ipe.uapp.infoframe.listeners;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;

/**
 * This listener activates and disables the calendar fields depending to the calendar field value.
 * @author Sebastian Schoebinger
 *
 */

public class AddCalendarPluginListener implements
		OnConfigurationChangedListener {

	@Override
	public void configurationChanged(ConfigOptionRegistry registry,
			ConfigurationOption option) {
		if("1".equals(option.getValue())){
			registry.getConfigOptionForId("calendarUsername").setIsActive(true);
			registry.getConfigOptionForId("calendarPassword").setIsActive(true);
			registry.getConfigOptionForId("calendarMsgCount").setIsActive(true);
		}else{
			registry.getConfigOptionForId("calendarUsername").setIsActive(false);
			registry.getConfigOptionForId("calendarPassword").setIsActive(false);
			registry.getConfigOptionForId("calendarMsgCount").setIsActive(false);
		}
	}

}
