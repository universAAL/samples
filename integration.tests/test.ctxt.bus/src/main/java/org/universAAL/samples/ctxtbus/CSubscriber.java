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
