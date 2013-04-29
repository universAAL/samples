package de.fzi.ipe.uapp.infoframe.validation.factories;

import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidator;
import org.universAAL.ucc.configuration.model.interfaces.ConfigurationValidatorFactory;
import de.fzi.ipe.uapp.infoframe.validation.RowValidator;

/**
 * This is the factory class for the row validator
 * @author Sebastian Schoebinger
 *
 */
public class RowValidatorFactory implements ConfigurationValidatorFactory {

	@Override
	public ConfigurationValidator create() {
		return new RowValidator();
	}
}
