package org.universAAL.samples.uibus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputSubscriber;
import org.universAAL.ontology.profile.User;

public class ISubscriber extends InputSubscriber {
    private final static Logger log = LoggerFactory
	    .getLogger(ISubscriber.class);

    protected ISubscriber(ModuleContext context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub

    }

    public void handleInputEvent(InputEvent event) {
	User user = (User) event.getUser();
	log.info("Received an Input Event from user {}", user.getURI());
	String submit = event.getSubmissionID();
	try {
	    // if(submit.startsWith("testsubmit")){
	    // String[] formsNames=new
	    // String[]{"Input 1","Input 2","Input 3","Select 1 1","Select 1 2","Select M 1","Select M 2","Area","Range"};
	    // String[] formsResults=new String[9];
	    // formsResults[0]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input1")}).toString();
	    // formsResults[1]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input2")}).toString();
	    // formsResults[2]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input3")}).toString();
	    // formsResults[3]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input4")}).toString();
	    // formsResults[4]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input5")}).toString();
	    // formsResults[5]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input6")}).toString();
	    // formsResults[6]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input7")}).toString();
	    // formsResults[7]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input11")}).toString();
	    // formsResults[8]=event.getUserInput(new
	    // String[]{("http://ontology.aal-persona.org/Tests.owl#input12")}).toString();
	    // Activator.uoutput.showAllRespDialog(user,formsNames,formsResults);
	    // }
	} catch (Exception e) {
	    log.error("Error while processing the user input: {}", e);
	}

    }

    public void subscribe(String dialogID) {
	addNewRegParams(dialogID);
    }

}
