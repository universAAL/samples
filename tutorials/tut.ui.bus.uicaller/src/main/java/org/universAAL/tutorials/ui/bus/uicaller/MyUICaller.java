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

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;

/**
 * The {@link UICaller} is responsible of issuing {@link UIRequest}s and
 * handling the responses
 *
 * @author amedrano
 *
 */
public class MyUICaller extends UICaller {

	protected MyUICaller(ModuleContext context) {
		super(context);
	}

	@Override
	public void communicationChannelBroken() {
		// This method is called when the connection to the service bus is
		// broken. We don't do anything here, but applications should handle
		// this case.
	}

	@Override
	public void dialogAborted(String dialogID, Resource data) {
		// this method is called when the dialog is aborted, either because the user
		// decided to close it
		// or because the Dialog Manager has close it due to some context situation.
	}

	@Override
	public void handleUIResponse(UIResponse input) {
		// here we will receive the response from the user
		// first we want to know which submit the user has chosen
		LogUtils.logDebug(owner, getClass(), "handleUIResponse", "user has selected" + input.getSubmissionID());

		// then we might want to get the data the user has filled in (or edited)
		Resource data = input.getSubmittedData();
		// and do something with it
		// ...
		// To access the data you must use the property paths in the form controls
		// data will be the edited dataRoot you set up in the Form, with the information
		// provided by the user.
	}

}
