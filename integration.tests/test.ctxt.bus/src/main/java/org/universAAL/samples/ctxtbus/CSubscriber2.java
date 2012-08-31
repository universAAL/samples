package org.universAAL.samples.ctxtbus;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;

public class CSubscriber2 extends ContextSubscriber {
    static ContextEventPattern[] ceps = { new ContextEventPattern() };
    private boolean enabled = false;

    protected CSubscriber2(ModuleContext context,
	    ContextEventPattern[] initialSubscriptions) {
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
    }

    public void enable() {
	if (!enabled){
	    this.addNewRegParams(ceps);
	    enabled=true;
	}
    }

    public void disable() {
	if (enabled){
	    this.removeMatchingRegParams(ceps);
	    enabled=false;
	}
    }

}
