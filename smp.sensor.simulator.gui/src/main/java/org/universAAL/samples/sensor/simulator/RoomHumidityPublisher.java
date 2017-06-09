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

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.device.HumiditySensor;

/**
 * Class used for publishing room humidity
 * 
 * 
 */
public class RoomHumidityPublisher {

	public static final String NAMESPACE = HumiditySensor.MY_URI;

	public static final String HUMIDITY_SENSOR_1 = NAMESPACE + "humiditySensor1";

	public static final String HUMIDITY_SENSOR_2 = NAMESPACE + "humiditySensor2";

	public static ContextPublisher myContextPublisher;
	public static ModuleContext myModuleContext;

	/**
	 * Constructor Here we set ContextEventPattern and add restrictions to it
	 * that tell universAAL platform which infofmation we want to recieve
	 * 
	 * @param context
	 */
	protected RoomHumidityPublisher(ModuleContext context) {

		RoomHumidityPublisher.myModuleContext = context;

		ContextProvider myContextProvider = new ContextProvider(NAMESPACE + "roomHumidityContextProvider");

		ContextEventPattern myContextEventPattern = new ContextEventPattern();

		myContextEventPattern.addRestriction(MergedRestriction.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
				HumiditySensor.PROP_HAS_VALUE));

		myContextEventPattern.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, HumiditySensor.MY_URI));

		ContextEventPattern[] contextEventPatterns = new ContextEventPattern[] { myContextEventPattern };

		myContextProvider.setType(ContextProviderType.controller);
		myContextProvider.setProvidedEvents(contextEventPatterns);
		myContextPublisher = new DefaultContextPublisher(context, myContextProvider);
	}

	/**
	 * Method used for publishing context event that has HumiditySensor as a
	 * subject and it's value as a predicate
	 * 
	 * @param humiditySensor
	 */
	public void publishContextEvent(HumiditySensor humiditySensor) {

		ContextEvent myContextEvent = new ContextEvent(humiditySensor, HumiditySensor.PROP_HAS_VALUE);

		myContextPublisher.publish(myContextEvent);
		System.out.println("++++++++++++\n++++++++++++\n+++++++++++\n++++++++++++++++++\n\n\n+++++++");

	}

}
