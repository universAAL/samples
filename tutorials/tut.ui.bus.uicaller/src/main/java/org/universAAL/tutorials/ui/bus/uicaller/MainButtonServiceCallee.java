/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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
package org.universAAL.tutorials.ui.bus.uicaller;

import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;

/**
 * MainButtonProvider implements a special kind of service which is called by
 * the Dialog Manager when the user selects it in the main menu.
 *
 * @author amedrano
 *
 */
public class MainButtonServiceCallee extends ServiceCallee {

	private static final String NAMESPACE = "http://ontologies.universAAL.com/MyService.owl#";
	private static final String MY_URI = NAMESPACE + "MainButtonService";
	private static final String START_UI_URI = NAMESPACE + "startUI";

	private UICaller uiCaller;

	public MainButtonServiceCallee(ModuleContext context, ServiceProfile[] realizedServices) {
		super(context, realizedServices);
	}

	public MainButtonServiceCallee(ModuleContext context, UICaller uiCaller) {
		this(context, getProfiles());
		this.uiCaller = uiCaller;
	}

	/**
	 * Provide the special profile which is called by the DM when the user selects
	 * the app.
	 *
	 * @return
	 */
	private static ServiceProfile[] getProfiles() {
		// The special service profile is built by this static method.
		ServiceProfile initDP = InitialServiceDialog.createInitialDialogProfile(MY_URI, "Vendor",
				"Description of the application, or UI called", START_UI_URI);
		return new ServiceProfile[] { initDP };
	}

	@Override
	public void communicationChannelBroken() {
		// This method is called when the connection to the service bus is
		// broken. We don't do anything here, but applications should handle
		// this case.
	}

	/**
	 * The handleCall of this service callee is the implementation of the special
	 * profile. In most cases this means the service just issues a UIRequest.
	 */
	public ServiceResponse handleCall(ServiceCall call) {
		// we need the user who has selected this service
		// it is passed by the DM through the standard property for the involved human
		// user
		Resource inputUser = (Resource) call.getProperty(ServiceRequest.PROP_uAAL_INVOLVED_HUMAN_USER);

		/*
		 * Generate a form for the user to see
		 */
		// Select an interactive type of main Form
		// the data root will be used as basis for presenting AND collecting data
		// the rest of the form is created declaratively with respect to this dataroot
		// through propertyPaths.
		// Thus you could put there the internal Resource representing your data
		// and create a declarative form for the user to DIRECTLY edit said data.
		Form f = Form.newDialog("My UI Caller Dialog Title", new Resource());
		// Dialogs contain FormControls, which are added to 2 main groups
		// the IOControls group is the main part of the dialog, and where most of the
		// form controls go.
		// In this case we just add a simple Text saluting the universe
		new SimpleOutput(f.getIOControls(), null, null, "Hello world!");
		// ...
		// we could add more form controls
		// ...

		// The other Group is called the submits
		// This group should contain only the submits (or SubdialogTrigger).
		// these are buttons representing the main actions with rerespect to the form
		// for example "Edit", "Reset", "Remove" can be different actions on the same
		// form.
		new Submit(f.getSubmits(), new Label("Done", null), "doneForm");

		/*
		 * Now we generate the UIRequest
		 */
		UIRequest req = new UIRequest(
				// use the user from the service request
				inputUser,
				// send the form we have just created
				f,
				// set the Priority of this form, this is useful to sort which dialog the user
				// must pay attention to first
				LevelRating.none,
				// set the language used in this form
				Locale.ENGLISH,
				// set the privacy level of the content, as it is not the same displaying
				// private information on a small smartphone screen, than reading it out loud
				// in a TTS system
				PrivacyLevel.insensible);

		// Issue the request from the UICaller, responses will be gathered by that
		// component.
		uiCaller.sendUIRequest(req);

		// return as successful service performed.
		// It is now up to the DM and the user to complete the operation
		return new ServiceResponse(CallStatus.succeeded);
	}

}
