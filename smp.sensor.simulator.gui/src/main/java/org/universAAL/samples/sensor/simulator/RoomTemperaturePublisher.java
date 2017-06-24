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
import org.universAAL.ontology.device.TemperatureSensor;

/**
 * Class used for publishing room temperature
 *
 *
 */
public class RoomTemperaturePublisher {

	public static final String NAMESPACE = TemperatureSensor.MY_URI;

	public static final String SLEEPING_ROOM_TEMP_SENSOR = NAMESPACE + "sleepingRoomTemperatureSensor";
	public static final String LIVING_ROOM_TEMP_SENSOR = NAMESPACE + "livingRoomTemperatureSensor";
	public static final String BATHROOM_TEMP_SENSOR = NAMESPACE + "bathroomTemperatureSensor";
	public static final String KITCHEN_TEMP_SENSOR = NAMESPACE + "kitchenTemperatureSensor";
	public static final String HOBBY_ROOM_TEMP_SENSOR = NAMESPACE + "hobbyRoomTemperatureSensor";

	public static ContextPublisher myContextPublisher;
	public static ModuleContext myModuleContext;

	/**
	 * Constructor Here we set ContextEventPattern and add restrictions to it
	 * that tell universAAL platform which infofmation we want to recieve
	 *
	 * @param context
	 */
	protected RoomTemperaturePublisher(ModuleContext context) {

		RoomTemperaturePublisher.myModuleContext = context;

		ContextProvider myContextProvider = new ContextProvider(NAMESPACE + "roomTemperatureContextProvider");

		ContextEventPattern myContextEventPattern = new ContextEventPattern();

		myContextEventPattern.addRestriction(MergedRestriction.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
				TemperatureSensor.PROP_HAS_VALUE));

		myContextEventPattern.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, TemperatureSensor.MY_URI));

		ContextEventPattern[] contextEventPatterns = new ContextEventPattern[] { myContextEventPattern };

		myContextProvider.setType(ContextProviderType.controller);
		myContextProvider.setProvidedEvents(contextEventPatterns);
		myContextPublisher = new DefaultContextPublisher(context, myContextProvider);
	}

	/**
	 * Publish ContextEvent connected to temperatureSensor parameter
	 *
	 * @param temperatureSensor
	 */
	public void publishContextEvent(TemperatureSensor temperatureSensor) {

		ContextEvent myContextEvent = new ContextEvent(temperatureSensor, TemperatureSensor.PROP_HAS_VALUE);

		myContextPublisher.publish(myContextEvent);
	}

}
