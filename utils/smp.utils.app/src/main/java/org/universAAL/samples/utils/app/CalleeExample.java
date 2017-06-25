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
package org.universAAL.samples.utils.app;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.ontology.device.Actuator;
import org.universAAL.ontology.device.HeaterActuator;
import org.universAAL.ontology.device.StatusValue;
import org.universAAL.support.utils.service.mid.InvalidOntologyUtilException;
import org.universAAL.support.utils.service.top.UtilActuatorCallee;

//Service Callee universAAL wrapper for a typical actuator service with default profiles
public class CalleeExample extends UtilActuatorCallee {

	// Extended constructor
	public CalleeExample(ModuleContext context, String namespace, Actuator actuator)
			throws InvalidOntologyUtilException {
		super(context, namespace, actuator);
	}

	// Called when GET STATUS profile is requested
	@Override
	public boolean executeGet() {
		return Activator.heater.getValue().equals(StatusValue.Activated);
	}

	// Called when TURN OFF profile is requested
	@Override
	public boolean executeOff() {
		// Turn off and send event only if not already off
		if (!Activator.heater.getValue().equals(StatusValue.NotActivated)) {
			Activator.heater.setValue(StatusValue.NotActivated);
			Activator.publisher.publish(new ContextEvent(Activator.heater, HeaterActuator.PROP_HAS_VALUE));
		}
		return true;
	}

	// Called when TURN ON profile is requested
	@Override
	public boolean executeOn() {
		// Turn on and send event only if not already on
		if (!Activator.heater.getValue().equals(StatusValue.Activated)) {
			Activator.heater.setValue(StatusValue.Activated);
			Activator.publisher.publish(new ContextEvent(Activator.heater, HeaterActuator.PROP_HAS_VALUE));
		}
		return true;
	}

	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

}
