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
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.middleware.owl.MergedRestriction;

public class CSubscriber extends ContextSubscriber {

    protected CSubscriber(ModuleContext context,
	    ContextEventPattern[] initialSubscriptions) {
	super(context, initialSubscriptions);
	// TODO Auto-generated constructor stub
    }

    protected CSubscriber(ModuleContext context) {
	super(context, getPermanentSubscriptions());
	// TODO Auto-generated constructor stub
    }

    private static ContextEventPattern[] getPermanentSubscriptions() {
	ContextEventPattern[] ceps = new ContextEventPattern[1];

	ceps[0] = new ContextEventPattern();
	
	ceps[0].addRestriction(MergedRestriction.getFixedValueRestriction(
		ContextProvider.PROP_CONTEXT_PROVIDER_TYPE,
		ContextProviderType.reasoner).appendTo(
		MergedRestriction.getAllValuesRestriction(
			ContextEvent.PROP_CONTEXT_PROVIDER,
			ContextProvider.MY_URI),
		new String[] { ContextEvent.PROP_CONTEXT_PROVIDER,
			ContextProvider.PROP_CONTEXT_PROVIDER_TYPE }));

	return ceps;
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    public void handleContextEvent(ContextEvent event) {
	Long tst=System.currentTimeMillis()-event.getTimestamp();
	Activator.panel.setSRtextResponse("Received. Delay: "+tst);
	System.out.println("----------------\n" + " sub="
		+ event.getSubjectURI() + "\n pred=" + event.getRDFPredicate()
		+ "\n obj=" + event.getRDFObject() + "\n tst="
		+ event.getTimestamp());
    }

}
