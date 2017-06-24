/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.universAAL.samples.sensor.simulator;

import java.awt.EventQueue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * @author eandgrg
 *
 */
public class Activator implements BundleActivator {
	/**
	 * {@link ModuleContext}
	 */
	private static ModuleContext mcontext;
	LocationContextPublisher lp = null;
	SensorSimulatorGUI frame = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bcontext) throws Exception {

		Activator.mcontext = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { bcontext });

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LocationContextPublisher lp = new LocationContextPublisher(mcontext);

					LampStatePublisher lampStatePublisher = new LampStatePublisher(mcontext);
					RoomTemperaturePublisher roomTemperaturePublisher = new RoomTemperaturePublisher(mcontext);

					RoomHumidityPublisher roomHumidityPublisher = new RoomHumidityPublisher(mcontext);

					SensorSimulatorGUI frame = new SensorSimulatorGUI(lp, lampStatePublisher, roomTemperaturePublisher,
							roomHumidityPublisher);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		LogUtils.logInfo(mcontext, this.getClass(), "start",
				new Object[] { "smp.sensor.simulator.gui bundle has started." }, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		LogUtils.logInfo(mcontext, this.getClass(), "stop",
				new Object[] { "smp.sensor.simulator.gui bundle has stopped." }, null);
		mcontext = null;
		lp = null;
		frame = null;

	}

}
