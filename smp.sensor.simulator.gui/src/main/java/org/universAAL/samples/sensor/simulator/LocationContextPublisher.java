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
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.Caregiver;
import org.universAAL.ontology.profile.User;

/**
 * @author eandgrg
 *
 */
public class LocationContextPublisher {
	public static final String NAMESPACE = "http://www.universAAL.org/Location.owl#";
	public static final String SLEEPING_ROOM = NAMESPACE + "sleepingRoom";
	public static final String LIVING_ROOM = NAMESPACE + "livingRoom";
	public static final String BATHROOM = NAMESPACE + "bathroom";
	public static final String KITCHEN = NAMESPACE + "kitchen";
	public static final String HOBBY_ROOM = NAMESPACE + "hobbyRoom";

	public static ContextPublisher cp;
	private static ModuleContext mc;

	private static ContextEventPattern[] getProvidedContextEvents() {
		ContextEventPattern contextEventPattern = new ContextEventPattern();
		contextEventPattern
				.addRestriction(MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, User.MY_URI));
		contextEventPattern.addRestriction(MergedRestriction.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
				User.PROP_PHYSICAL_LOCATION));
		return new ContextEventPattern[] { contextEventPattern };
	}

	public LocationContextPublisher(ModuleContext moduleContext) {
		LocationContextPublisher.mc = moduleContext;

		ContextProvider info = new ContextProvider(NAMESPACE + "UserLocationContextProvider");
		info.setType(ContextProviderType.reasoner);
		info.setProvidedEvents(getProvidedContextEvents());

		cp = new DefaultContextPublisher(moduleContext, info);

	}

	/**
	 * Publishes context event that informs us of user's location
	 *
	 * @param typeOfUser
	 *
	 * @param userUri
	 * @param roomFunction
	 */
	public void publishLocation(TypeOfUser typeOfUser, String userUri, String locationUri) {

		User user = null;

		switch (typeOfUser) {
		case ASSISTED_PERSON:
			user = new AssistedPerson(userUri);
			break;
		case CAREGIVER:
			user = new Caregiver(userUri);
			break;
		default:
			user = new User(userUri);
			break;
		}

		Location location = new Location(locationUri);

		user.setLocation(location);

		cp.publish(new ContextEvent(user, User.PROP_PHYSICAL_LOCATION));

		LogUtils.logInfo(mc, this.getClass(), "publishLocation",
				new Object[] {
						"User: " + user.getURI() + " user.getLocation().getURI()-> " + user.getLocation().getURI() },
				null);
	}

}
