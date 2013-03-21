/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License .
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.helper.location.publisher;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.location.indoor.Room;
import org.universAAL.ontology.location.indoor.RoomFunction;

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
	contextEventPattern.addRestriction(MergedRestriction
		.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT,
			Room.MY_URI));
	contextEventPattern.addRestriction(MergedRestriction
		.getFixedValueRestriction(ContextEvent.PROP_RDF_PREDICATE,
			Room.PROP_ROOM_FUNCTION));
	return new ContextEventPattern[] { contextEventPattern };
    }

    public LocationContextPublisher(ModuleContext moduleContext) {
	LocationContextPublisher.mc = moduleContext;

	ContextProvider info = new ContextProvider(NAMESPACE
		+ "UserLocationContextProvider");
	info.setType(ContextProviderType.reasoner);
	info.setProvidedEvents(getProvidedContextEvents());

	cp = new DefaultContextPublisher(moduleContext, info);

    }

    public void publishLocation(String roomUri, RoomFunction roomFunction) {
	Room room = new Room(roomUri, roomFunction);

	// FIXME check
	cp.publish(new ContextEvent(room, Room.PROP_ROOM_FUNCTION));

	LogUtils.logInfo(mc, this.getClass(), "publishLocation",
		new Object[] { "User entered room: " + roomUri }, null);
    }

}
