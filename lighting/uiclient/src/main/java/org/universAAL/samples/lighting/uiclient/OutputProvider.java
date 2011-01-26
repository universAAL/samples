/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
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
package org.universAAL.samples.lighting.uiclient;

import java.util.Locale;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.io.rdf.ChoiceItem;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Select1;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputPublisher;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.middleware.io.owl.PrivacyLevel;
import org.universAAL.middleware.owl.OrderingRestriction;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.middleware.io.rdf.InputField;

/**
 * @author cstockloew
 *
 */
public class OutputProvider extends OutputPublisher {

	static final String OUTPUT_NAMESPACE = Activator.CLIENT_LIGHTING_UI_NAMESPACE+"OutputProvider#";
	
	
	static final String SUBMISSION_ON				= OUTPUT_NAMESPACE+"on";
	static final String SUBMISSION_OFF				= OUTPUT_NAMESPACE+"off";
	static final String SUBMISSION_SCALE			= OUTPUT_NAMESPACE+"scale";
	static final String SUBMISSION_EXIT				= OUTPUT_NAMESPACE+"exit";
	static final String PROP_SCALE_VALUE			= OUTPUT_NAMESPACE+"scale_val";
	static final String PROP_SELECTED_LAMP_INDEX	= OUTPUT_NAMESPACE+"lamp_index";

	static final PropertyPath PROP_PATH_SCALE_VALUE =
		new PropertyPath(null, false,
			new String[] {
				PROP_SCALE_VALUE
			});
	static final PropertyPath PROP_PATH_LAMP_INDEX =
		new PropertyPath(null, false,
			new String[] {
				PROP_SELECTED_LAMP_INDEX
			});

	
	private Form mainDialog = null;
	// Save the devices for this dialog so that we can query the URI of the selected
	// device later. This is necessary, because the InputConsumer only gets the selection
	// index, and we cannot call getControlledLamps later because the list of devices
	// may have changed in the meantime
	private Device[] devices = null;
	
	protected OutputProvider(BundleContext context) {
		super(context);
		//mainDialog = initMainDialog();
	}

	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	private Form initMainDialog() {
		Form f = Form.newDialog("Sample Lighting UI: Lamp Controller", new Resource());
		devices = LightingConsumer.getControlledLamps();		
		Select1 radio = new Select1(f.getIOControls(),
				new Label("Lamps", null),
				PROP_PATH_LAMP_INDEX,
				OrderingRestriction.newOrderingRestriction(
					new Integer(devices.length),
					new Integer(0),
					false, true,
					Restriction.getAllValuesRestrictionWithCardinality(
						PROP_SELECTED_LAMP_INDEX,
						TypeMapper.getDatatypeURI(Integer.class),
						1, 1)),
				0);
		for (int i=0; i<devices.length; i++)
			radio.addChoiceItem(
				new ChoiceItem(devices[i].getURI(),
				null,
				new Integer(i)));
		new Submit(f.getSubmits(), new Label("On", null), SUBMISSION_ON);
		new Submit(f.getSubmits(), new Label("Off", null), SUBMISSION_OFF);
		new Submit(f.getSubmits(), new Label("Scale", null), SUBMISSION_SCALE);
		new Submit(f.getSubmits(), new Label("Exit", null), SUBMISSION_EXIT);
		new InputField(f.getIOControls(), new Label("Scale Value", null), PROP_PATH_SCALE_VALUE, null, "scale");
		return f;
	}
	
	String getDeviceURI(int index) {
		if (index < devices.length)
			return devices[index].getURI();
		return null;
	}

	Form startMainDialog() {
		if (mainDialog == null)
			mainDialog = initMainDialog();
		OutputEvent out = new OutputEvent(
				Activator.testUser, mainDialog,
				LevelRating.middle, Locale.ENGLISH,
				PrivacyLevel.insensible);
		Activator.inputConsumer.subscribe(mainDialog.getDialogID());
		publish(out);
		return mainDialog;
	}
}
