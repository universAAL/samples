package org.universAAL.samples.tta;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputSubscriber;
import org.universAAL.ontology.profile.User;

/**
 * 
 * @author alfiva
 */
public class ISubscriber extends InputSubscriber {

    private final static Logger log = LoggerFactory
	    .getLogger(ISubscriber.class);

    protected ISubscriber(BundleContext context) {
	super(context);
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.persona.middleware.input.InputSubscriber#handleInputEvent(org.persona
     * .middleware.input.InputEvent)
     */
    public void handleInputEvent(InputEvent event) {
	User user = (User) event.getUser();
	log.info("Received an Input Event from user {}", user.getURI());
	String submit = event.getSubmissionID();

	try {
	    if (submit.equals(RiskGUI.SUBMIT_HOME)) {
		log.debug("Input received was go Home");
		// do nothing-> return to main menu
	    } else if (submit.equals(RiskGUI.SUBMIT_CANCEL)) {
		log.debug("Input received was Cancel (abort risk)");
		OPublisher.responseWatch.cancel();
	    } else if (submit.equals(RiskGUI.SUBMIT_MANUAL)) {
		log.debug("Input received was manual panic button");
		boolean sent = Activator.rcaller.sendPanicButtonSMSText();
		Activator.routput.showSMSForm(user, sent);
	    } else if (submit.equals(RiskGUI.SUBMIT_TAKE)) {
		log.debug("Input received was start take me home");
		// TODO: TAKE ME HOME
		String url = ("http://maps.google.com/?saddr=Ingeniero Fausto Elio 1, Valencia, Spain&daddr="
			+ Activator.getProperties().getProperty(
				Activator.GPSTO, "Mestalla, Valencia") + "&dirflg=w")
			.replace(" ", "+");
		java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
	    }
	} catch (Exception e) {
	    log.error("Error while processing the user input: {}", e);
	}

    }

    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub

    }

    public void subscribe(String dialogID) {
	addNewRegParams(dialogID);
    }

}
