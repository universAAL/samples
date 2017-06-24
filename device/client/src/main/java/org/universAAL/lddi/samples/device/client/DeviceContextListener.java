/*
     Copyright 2010-2014 AIT Austrian Institute of Technology GmbH
	 http://www.ait.ac.at

     See the NOTICE file distributed with this work for additional
     information regarding copyright ownership

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/

package org.universAAL.lddi.samples.device.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.phThing.Device;

/**
 * This class handles ontological context events by subscribing specific event
 * patterns on the uAAL context bus This class is stateless; no objects
 * (sensors) are stored here LogUtil from uAAL-Middleware is used here
 *
 * @author Thomas Fuxreiter (foex@gmx.at)
 */
public class DeviceContextListener extends ContextSubscriber {

	/**
	 * This is the client application anchor; which knows nothing about
	 * Ontologies :-)
	 */
	private DeviceClient dc = null;

	// For log output on GUI
	DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Define the context event pattern: all Events where subject is instance of
	 * ActivityHubSensor
	 *
	 * @return ContextEventPattern[]
	 */
	private static ContextEventPattern[] getContextSubscriptionParams() {
		// I am interested in all events with a device as subject
		ContextEventPattern cep = new ContextEventPattern();
		cep.addRestriction(MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, Device.MY_URI));
		return new ContextEventPattern[] { cep };
	}

	/**
	 * Constructor Registration of context event patterns on the uAAL context
	 * bus
	 *
	 * @param mc
	 *            uAAL Middleware ModuleContext
	 * @param ahc
	 *            link to client application
	 */
	DeviceContextListener(ModuleContext mc, DeviceClient dc) {
		// the constructor register us to the bus
		super(mc, getContextSubscriptionParams());
		LogUtils.logInfo(Activator.mc, DeviceContextListener.class, "constructor",
				new Object[] { "context event patterns are registered now" }, null);
		this.dc = dc;
	}

	/**
	 * receive and process centext events log to console send event details to
	 * GUI as String[]
	 *
	 * @param event
	 *            from context bus
	 */
	@Override
	public void handleContextEvent(ContextEvent event) {
		LogUtils.logInfo(Activator.mc, DeviceContextListener.class, "handleContextEvent",
				new Object[] { "\n*****************************\n", "Received context event:\n", "    Subject     = ",
						event.getSubjectURI(), "\n", "    Subject type= ", event.getSubjectTypeURI(), "\n",
						"    Predicate   = ", event.getRDFPredicate(), "\n", "    Object      = ",
						event.getRDFObject() },
				null);

		String[] log = new String[] {
				"************ Last received context event: " + dfm.format(new Date(System.currentTimeMillis())),
				"  Subject     = " + event.getSubjectURI(), "  Subject type= " + event.getSubjectTypeURI(),
				"  Predicate   = " + event.getRDFPredicate(), "  Object      = " + event.getRDFObject() };

		this.dc.showContextEvent(log);
	}

	@Override
	public void communicationChannelBroken() {
		LogUtils.logWarn(Activator.mc, DeviceContextListener.class, "communicationChannelBroken", null, null);
	}
}
