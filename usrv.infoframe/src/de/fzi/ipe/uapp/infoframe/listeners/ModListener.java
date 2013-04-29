package de.fzi.ipe.uapp.infoframe.listeners;

import org.universAAL.ucc.configuration.model.Cardinality;
import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;

/**
 * This listener activates and disables the feed URL field depending to the feed field value.
 * @author Sebastian Schoebinger
 *
 */
public class ModListener implements OnConfigurationChangedListener {
	
	@Override
	public void configurationChanged(ConfigOptionRegistry registry,
			ConfigurationOption option) {
		
		ConfigurationOption configOption = null;
		
		if(option.getValue().equals("1")){
			configOption = registry.getConfigOptionForId("FeedURL1");
			configOption.setIsActive(true);
			configOption.setCardinality(new Cardinality("1..1"));
		}else{
			configOption = registry.getConfigOptionForId("FeedURL1");
			configOption.setIsActive(false);
			configOption.setCardinality(new Cardinality("0..1"));
		}
		
	}

}
