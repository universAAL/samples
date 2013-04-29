package de.fzi.ipe.uapp.infoframe.listeners;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;

public class AddWeatherPluginListener implements OnConfigurationChangedListener {

	@Override
	public void configurationChanged(ConfigOptionRegistry registry,
			ConfigurationOption option) {
		if ("1".equals(option.getValue())) {
			registry.getConfigOptionForId("WeatherStationLocation").setIsActive(true);
			registry.getConfigOptionForId("Language").setIsActive(true);
		} else {
			registry.getConfigOptionForId("WeatherStationLocation")
					.setIsActive(false);
			registry.getConfigOptionForId("Language")
					.setIsActive(false);

		}
	}
}
