/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion
	Avanzadas - Grupo Tecnologias para la Salud y el
	Bienestar (TSB)

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
package org.universAAL.samples.ctxtbus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.serialization.MessageContentSerializerEx;

public class Activator implements BundleActivator, ServiceListener {
	public static BundleContext context = null;
	public static CSubscriber csubscriber = null;
	public static CSubscriber2 csubscriber2 = null;
	public static CPublisher cpublisher = null;
	public static HistoryCaller hcaller = null;
	public static ProfileCaller pcaller = null;
	public static SpaceCaller scaller = null;
	protected static GUIPanel panel;
	protected static MessageContentSerializerEx ser;
	private static ModuleContext moduleContext = null;
	public static MessageContentSerializerEx parser = null;

	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		Activator.moduleContext = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });
		ser = (MessageContentSerializerEx) context
				.getService(context.getServiceReference(MessageContentSerializerEx.class.getName()));
		csubscriber = new CSubscriber(moduleContext);
		cpublisher = new CPublisher(moduleContext);
		hcaller = new HistoryCaller(moduleContext);
		pcaller = new ProfileCaller(moduleContext);
		scaller = new SpaceCaller(moduleContext);
		panel = new GUIPanel();
		panel.setVisible(true);
		csubscriber2 = new CSubscriber2(moduleContext);
		csubscriber2.disable();
		// Look for MessageContentSerializer of mw.data.serialization
		String filter = "(objectclass=" + MessageContentSerializerEx.class.getName() + ")";
		context.addServiceListener(this, filter);
		ServiceReference references[] = context.getServiceReferences(null, filter);
		for (int i = 0; references != null && i < references.length; i++)
			this.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, references[i]));
	}

	public void stop(BundleContext arg0) throws Exception {
		csubscriber.close();
		csubscriber2.close();
		cpublisher.close();
		panel.dispose();
	}

	public void serviceChanged(ServiceEvent event) {
		// Update the MessageContentSerializer
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
		case ServiceEvent.MODIFIED:
			this.parser = ((MessageContentSerializerEx) context.getService(event.getServiceReference()));
			break;
		case ServiceEvent.UNREGISTERING:
			this.parser = (null);
			break;
		}
	}

}
