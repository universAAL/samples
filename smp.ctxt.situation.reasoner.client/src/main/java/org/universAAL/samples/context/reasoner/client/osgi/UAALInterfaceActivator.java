/*
	Copyright 2008-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer Gesellschaft - Institut f�r Graphische Datenverarbeitung

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
package org.universAAL.samples.context.reasoner.client.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.serialization.MessageContentSerializer;

public class UAALInterfaceActivator implements ServiceListener {
	private BundleContext osgiContext = null;
	public static MessageContentSerializer serializer = null;

	UAALInterfaceActivator(BundleContext osgiContext, ModuleContext context) throws InvalidSyntaxException {
		this.osgiContext = osgiContext;

		// Look for MessageContentSerializer of mw.data.serialization
		String filter = "(objectclass=" + MessageContentSerializer.class.getName() + ")";
		osgiContext.addServiceListener(this, filter);
		ServiceReference[] references = osgiContext.getServiceReferences(null, filter);
		for (int i = 0; references != null && i < references.length; i++) {
			this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, references[i]));
		}
	}

	public void serviceChanged(ServiceEvent event) {
		// Update the MessageContentSerializer
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
		case ServiceEvent.MODIFIED: {
			serializer = (MessageContentSerializer) osgiContext.getService(event.getServiceReference());
			break;
		}
		case ServiceEvent.UNREGISTERING:
			break;
		default:
			break;
		}
	}
}
