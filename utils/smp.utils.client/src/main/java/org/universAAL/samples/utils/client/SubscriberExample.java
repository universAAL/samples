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
package org.universAAL.samples.utils.client;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.ontology.device.LightController;
import org.universAAL.support.utils.context.mid.UtilSubscriber;

// Context Subscriber universAAL wrapper
public class SubscriberExample extends UtilSubscriber {

	// Extended constructor
	protected SubscriberExample(ModuleContext context, String subjTypeURI, String predicate, String objTypeURI) {
		super(context, subjTypeURI, predicate, objTypeURI);
	}

	// Called when received events
	@Override
	public void handleContextEvent(ContextEvent event) {
		// Show this just to know we received the event
		System.out.println("Received context event:\n    Subject     =" + event.getSubjectURI() + "\n    Subject type="
				+ event.getSubjectTypeURI() + "\n    Predicate   =" + event.getRDFPredicate() + "\n    Object      ="
				+ event.getRDFObject());
		// Display the event to user. DO NOT USE WITH SWING HANDLER YET->
		// COMMENT
		Activator.ui.showMessage(event.getSubjectURI(),
				((LightController) event.getRDFSubject()).getLocation().toString(),
				Integer.valueOf((Integer) event.getRDFObject()));
	}

	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

}
