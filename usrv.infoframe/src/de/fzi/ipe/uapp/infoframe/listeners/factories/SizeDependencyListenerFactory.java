package de.fzi.ipe.uapp.infoframe.listeners.factories;

import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListener;
import org.universAAL.ucc.configuration.model.interfaces.OnConfigurationChangedListenerFactory;
import de.fzi.ipe.uapp.infoframe.listeners.SizeDependencyListener;

/**
 * This is the factory class for the SizeDependencyListener.
 * @author Sebastian Schoebinger
 *
 */

public class SizeDependencyListenerFactory implements OnConfigurationChangedListenerFactory {

	@Override
	public OnConfigurationChangedListener create() {
		return new SizeDependencyListener();
	}

}
