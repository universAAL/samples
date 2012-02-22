/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.full.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ui.full.test.forms.A1Main;
import org.universAAL.ui.full.test.osgi.Activator;

public class SCallee extends ServiceCallee {
    private static final ServiceResponse failure = new ServiceResponse(
	    CallStatus.serviceSpecificFailure);
    private final static Logger log = LoggerFactory.getLogger(SCallee.class);
    private static final String SERVICE_ID = "http://ontology.universAAL.org/Tests.owl#startUI";

    public SCallee(ModuleContext context) {
	super(context, getProfiles());
    }

    protected SCallee(ModuleContext context, ServiceProfile[] realizedServices) {
	super(context, realizedServices);
	log.debug("Registered the SCallee");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.service.ServiceCallee#communicationChannelBroken
     * ()
     */
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.service.ServiceCallee#handleCall(org.universAAL
     * .middleware.service.ServiceCall)
     */
    public ServiceResponse handleCall(ServiceCall call) {
	log.debug("Received call");
	User user = null;
	if (call == null) {
	    failure
		    .addOutput(new ProcessOutput(
			    ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
			    "Null call!?!"));
	    log.error("Null call");
	    return failure;
	}

	String operation = call.getProcessURI();
	if (operation == null) {
	    failure.addOutput(new ProcessOutput(
		    ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
		    "Null operation!?!"));
	    log.error("Null op");
	    return failure;
	}

	if (operation.startsWith(SERVICE_ID)) {
	    log.debug("Start UI op");
	    Object inputUser = call
		    .getProperty(ServiceRequest.PROP_uAAL_INVOLVED_HUMAN_USER);
	    if ((inputUser == null) || !(inputUser instanceof User)) {
		failure.addOutput(new ProcessOutput(
			ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
			"Invalid User Input!"));
		log.debug("No user");
		return failure;
	    } else {
		user = (User) inputUser;
	    }
	    log.debug("Show dialog from call");
	    Activator.uiCaller.setUser(user);
	    Activator.uiCaller.mapAndSendUIRequest(new A1Main());
	    ServiceResponse response = new ServiceResponse(CallStatus.succeeded);
	    return response;
	}
	log.debug("finished");
	return null;
    }

    static ServiceProfile[] getProfiles() {
	ServiceProfile prof = InitialServiceDialog.createInitialDialogProfile(
		"http://ontology.universAAL.org/Tests.owl#IORDF",
		"http://www.upm.es", "Full User Interface Forms tests",
		SERVICE_ID);
	return new ServiceProfile[] { prof };
    }
}
