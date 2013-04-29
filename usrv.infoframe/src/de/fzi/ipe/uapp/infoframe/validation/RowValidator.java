package de.fzi.ipe.uapp.infoframe.validation;

import org.universAAL.ucc.configuration.model.ConfigOptionRegistry;
import org.universAAL.ucc.configuration.model.ConfigurationOption;
import org.universAAL.ucc.configuration.model.configurationinstances.Value;
import org.universAAL.ucc.configuration.model.exceptions.ValidationException;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;

/**
 * This validator validates, that there are not too much rows on the image.
 * @author Sebastian Schoebinger
 *
 */

public class RowValidator implements ConfigurationValidator {
	
	int maxrows;

	@Override
	public boolean isValid(ConfigOptionRegistry registry, Value value) {
		int rows = 0;
		ConfigurationOption option = registry.getConfigOptionForId("addMailPlugin"); 
		if("1".equals(option.getValue())){
			rows += Integer.parseInt(registry.getConfigOptionForId("mailMsgCount").getValue());
		}
		
		option = registry.getConfigOptionForId("addCalendarPlugin");
		if("1".equals(option.getValue())){
			rows += Integer.parseInt(registry.getConfigOptionForId("calendarMsgCount").getValue());
		}
		
		option = registry.getConfigOptionForId("AddFeedPlugin");
		if("1".equals(option.getValue())){
			rows += Integer.parseInt(registry.getConfigOptionForId("MaxDispItems1").getValue());
		}
		return maxrows >= rows ;
	}

	@Override
	public void validate(ConfigOptionRegistry registry, Value value) throws ValidationException {
		if(!isValid(registry, value)){
			throw new ValidationException("All rows together need to be smaller than or equal to " + maxrows);
		}
	}

	@Override
	public void setAttributes(String[] attributes) {
		if(attributes.length > 0){
			try {
				maxrows = Integer.parseInt(attributes[0]);
			} catch (NumberFormatException e) {
				// Nothing
			}
		}
	}

}
