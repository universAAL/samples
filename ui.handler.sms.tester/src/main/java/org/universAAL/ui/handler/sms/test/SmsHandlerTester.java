/*******************************************************************************
 * Copyright 2012 Ericsson Nikola Tesla d.d.
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
package org.universAAL.ui.handler.sms.test;

import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.profile.User;

public class SmsHandlerTester extends UICaller {
    String mobileNumber = new String("385913653092");
    String sms = new String("Test message from sms handler tester");

    protected SmsHandlerTester(ModuleContext context) {
	super(context);

	Form form = createDialogForSMSHandler();
	sendMessageDialogtoUIBus(form);
    }

    Form createDialogForSMSHandler() {
	Form f = Form.newDialog((String) null, (Resource) null);
	new SimpleOutput(f.getIOControls(), null, null, mobileNumber);
	new SimpleOutput(f.getIOControls(), null, null, sms);
	return f;
    }

    void sendMessageDialogtoUIBus(Form formToSend) {
	System.out
		.println("Form to test sms handler: " + formToSend.toString());

	String userURI = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX + "saied";
	// "urn:org.universAAL.aal_space:test_env#saied"
	UIRequest uiRequest = new UIRequest(new User(userURI), formToSend,
		null, Locale.ENGLISH, PrivacyLevel.insensible);

	// modality is most important
	uiRequest.setPresentationModality(Modality.sms);
	this.sendUIRequest(uiRequest);
	System.out.println("sms tester: Test form sent to sms handler");

    }

    @Override
    public void communicationChannelBroken() {
	// TODO Auto-generated method stub

    }

    @Override
    public void dialogAborted(String dialogID) {
	// TODO Auto-generated method stub

    }

    @Override
    public void handleUIResponse(UIResponse input) {
	// TODO Auto-generated method stub

    }

}
