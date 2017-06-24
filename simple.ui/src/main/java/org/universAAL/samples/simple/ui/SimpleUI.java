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
package org.universAAL.samples.simple.ui;

import java.util.Locale;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.UICaller;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.PrivacyLevel;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;

public class SimpleUI extends UICaller {

	protected SimpleUI(ModuleContext context) {
		super(context);
	}

	@Override
	public void communicationChannelBroken() {
	}

	@Override
	public void dialogAborted(String dialogID, Resource data) {
	}

	@Override
	public void handleUIResponse(UIResponse input) {
		// TODO Auto-generated method stub
	}

	public void showDialog(Resource inputUser) {
		Form f = Form.newDialog("simple UI", new Resource());
		// start of the form model
		new SimpleOutput(f.getIOControls(), null, null, "Hello world!");
		// ...
		new Submit(f.getSubmits(), new Label("Done", null), "doneForm");
		// stop of form model
		UIRequest req = new UIRequest(inputUser, f, LevelRating.none, Locale.ENGLISH, PrivacyLevel.insensible);
		this.sendUIRequest(req);
	}

}
