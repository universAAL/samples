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

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;

public class CSubscriber2 extends ContextSubscriber {
	static ContextEventPattern[] ceps = { new ContextEventPattern() };
	private boolean enabled = false;

	protected CSubscriber2(ModuleContext context, ContextEventPattern[] initialSubscriptions) {
		super(context, initialSubscriptions);
		enabled = true;
	}

	protected CSubscriber2(ModuleContext context) {
		super(context, ceps);
		enabled = true;
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	public void handleContextEvent(ContextEvent event) {
		Activator.panel.subscribeReceived();
		Activator.panel.subscriberArea.append(Activator.ser.serialize(event) + "\n");
	}

	public void enable() {
		if (!enabled) {
			this.addNewRegParams(ceps);
			enabled = true;
		}
	}

	public void disable() {
		if (enabled) {
			this.removeMatchingRegParams(ceps);
			enabled = false;
		}
	}

}
