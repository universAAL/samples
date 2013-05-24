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
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.ontology.device.StatusValue;
import org.universAAL.ontology.profile.User;
import org.universAAL.support.utils.ui.Forms;
import org.universAAL.support.utils.ui.low.Dialog;
import org.universAAL.support.utils.ui.low.Message;
import org.universAAL.support.utils.ui.mid.UtilUICaller;

// UI Caller uAAL wrapper
public class UIExample extends UtilUICaller{
    // Declare URI & paths constants to be used in requests and forms
    private static final String REF_ON = Activator.APP_NAMESPACE+"SubmitON";
    private static final String REF_OFF = Activator.APP_NAMESPACE+"SubmitOFF";
    private static final String REF_EXIT = Activator.APP_NAMESPACE+"SubmitEXIT";
    private User sampleUser;

    // Extended constructor
    protected UIExample(ModuleContext context, String namespace,
	    String url, String desc) {
	super(context, namespace, url, desc);
    }
    
    // Called when initial dialog is requested by user -> show main dialog
    @Override
    public void executeStartUI(Resource user) {
	// User safe check (not necessary in 1.2.0)
	if(user instanceof User){
	    sampleUser=(User)user;
	}else{
	    sampleUser=new User(user.getURI());
	}
	boolean status=Activator.heater.getValue().equals(StatusValue.Activated);
	// Create Dialog
	Dialog d=new Dialog(sampleUser,Activator.APP_NAME);
	d.add(Forms.out("", "Control the status of the Heater"));
	d.add(Forms.out("Current Status", "The Heater is currently switched "+(status?"on":"off")));
	if(!status){
	    d.add(Forms.submit(REF_ON, "Turn On"));
	}else{
	    d.add(Forms.submit(REF_OFF, "Turn Off"));
	}
	d.addSubmit(Forms.submit(REF_EXIT, "Exit"));
	sendUIRequest(d);
    }
    
    // Display a pop up message describing status
    public void showMessage(){
	boolean status=Activator.heater.getValue().equals(StatusValue.Activated);
	if(sampleUser!=null){
	    Message m=new Message(sampleUser, Activator.APP_NAME, 
		    "The Heater is now switched "+(status?"on":"off"));
	    sendUIRequest(m);
	}
    }
    
    // Called when user has completed a dialog
    @Override
    public void handleUIResponse(UIResponse input) {
	String submit = input.getSubmissionID();
	if (submit.contains(REF_ON)) {
	    // Turn on heater
	    Activator.callee.executeOn();
	    // Show message over dialog
	    showMessage();
	}
	if (submit.contains(REF_OFF)) {
	    // Turn on heater
	    Activator.callee.executeOff();
	    // Show message over dialog
	    showMessage();
	}
    }
    
    @Override
    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub
    }

}
