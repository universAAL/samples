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

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.lighting.LightSource;

/**
 * LampStatePublisher class publishes lamp state to universAAL Context Bus
 *
 *
 */
public class LampStatePublisher {

	LampMap lampMap = new LampMap();

	public static final String NAMESPACE = LightSource.MY_URI;

	public static final String SLEEPING_ROOM_LIGHTING = NAMESPACE + "sleepingRoomLampState";
	public static final String LIVING_ROOM_LIGHTING = NAMESPACE + "livingRoomLampState";
	public static final String BATHROOM_LIGHTING = NAMESPACE + "bathroomLampState";
	public static final String KITCHEN_LIGHTING = NAMESPACE + "kitchenLampState";
	public static final String HOBBY_ROOM_LIGHTING = NAMESPACE + "hobbyRoomLampState";

	public static ContextPublisher myContextPublisher;
	public static ModuleContext myModuleContext;

	private ImageIcon offImageIcon;
	private ImageIcon onImageIcon;

	/**
	 * Constructor 5 light sources are inserted for testing purposes We're
	 * setting ContextEventPattern[] directly in the constructor It could be
	 * moved to separate method just like in LocationContextPublisher class
	 * (method getPermanentSubscription())
	 *
	 * @param context
	 */
	protected LampStatePublisher(ModuleContext context) {

		Image offImage = Toolkit.getDefaultToolkit()
				.createImage(this.getClass().getClassLoader().getResource("lightOff.jpg"));
		Image onImage = Toolkit.getDefaultToolkit()
				.createImage(this.getClass().getClassLoader().getResource("lightOn.jpg"));

		this.offImageIcon = new ImageIcon(offImage);
		this.onImageIcon = new ImageIcon(onImage);

		// Insert house light sources to hashmap
		this.lampMap.insertLightSource(SLEEPING_ROOM_LIGHTING, new LightSource(SLEEPING_ROOM_LIGHTING));
		this.lampMap.insertLightSource(LIVING_ROOM_LIGHTING, new LightSource(LIVING_ROOM_LIGHTING));
		this.lampMap.insertLightSource(BATHROOM_LIGHTING, new LightSource(BATHROOM_LIGHTING));
		this.lampMap.insertLightSource(KITCHEN_LIGHTING, new LightSource(KITCHEN_LIGHTING));
		this.lampMap.insertLightSource(HOBBY_ROOM_LIGHTING, new LightSource(HOBBY_ROOM_LIGHTING));

		LampStatePublisher.myModuleContext = context;

		ContextProvider myContextProvider = new ContextProvider(NAMESPACE + "lampStateContextProvider");

		ContextEventPattern myContextEventPattern = new ContextEventPattern();

		myContextEventPattern.addRestriction(MergedRestriction.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
				LightSource.PROP_SOURCE_BRIGHTNESS));

		myContextEventPattern.addRestriction(
				MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightSource.MY_URI));

		ContextEventPattern[] contextEventPatterns = new ContextEventPattern[] { myContextEventPattern };

		myContextProvider.setType(ContextProviderType.controller);
		myContextProvider.setProvidedEvents(contextEventPatterns);
		myContextPublisher = new DefaultContextPublisher(context, myContextProvider);

	}

	/**
	 * Publishes context event that has lightSource as a subject and it's
	 * brightness as a predicate For testing purposes we set brightness to 0 or
	 * to 100, create new ContextEvent and publish it
	 *
	 * @param lightSource
	 */
	public void publishContextEvent(LightSource lightSource) {

		if (lightSource.getBrightness() == 0) {
			lightSource.setBrightness(100);
		} else {
			lightSource.setBrightness(0);
		}

		ContextEvent myContextEvent = new ContextEvent(lightSource, LightSource.PROP_SOURCE_BRIGHTNESS);
		myContextPublisher.publish(myContextEvent);

	}

	/**
	 * lampMap getter method
	 *
	 * @return
	 */
	public LampMap getLampMap() {
		return this.lampMap;
	}

	/**
	 * Check lightSource brightness and change button color accordingly (yellow
	 * for ON or white for OFF)
	 *
	 * @param button
	 * @param lightSource
	 */
	public void changeLabelBackground(JLabel label, LightSource lightSource) {

		if (lightSource.getBrightness() == 100) {

			label.setBackground(new Color(255, 255, 255));
			label.setOpaque(true);
			label.setIcon(this.onImageIcon);

		} else {

			label.setBackground(new Color(255, 255, 255));
			label.setOpaque(true);
			label.setIcon(this.offImageIcon);

		}

	}

}
