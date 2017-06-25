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
package org.universAAL.samples.utils.client;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.profile.User;
import org.universAAL.support.utils.service.Arg;
import org.universAAL.support.utils.service.Path;
import org.universAAL.support.utils.service.low.Request;
import org.universAAL.support.utils.ui.Forms;
import org.universAAL.support.utils.ui.SelectOne;
import org.universAAL.support.utils.ui.SelectRange;
import org.universAAL.support.utils.ui.SubmitCmd;
import org.universAAL.support.utils.ui.low.Dialog;
import org.universAAL.support.utils.ui.low.Message;
import org.universAAL.support.utils.ui.mid.UtilUICaller;

// UI Caller universAAL wrapper
public class UIExample extends UtilUICaller {
	// Declare URI & paths constants to be used in requests and forms
	private static final String OUTPUT_LIST_OF_LAMPS = Activator.CLIENT_NAMESPACE + "controlledLamps";
	private static final String OUTPUT_BR = Activator.CLIENT_NAMESPACE + "lampBrightness";
	private static final String OUTPUT_LOC = Activator.CLIENT_NAMESPACE + "lampLocation";
	private static final String REF_ON = Activator.CLIENT_NAMESPACE + "SubmitON";
	private static final String REF_OFF = Activator.CLIENT_NAMESPACE + "SubmitOFF";
	private static final String REF_GET = Activator.CLIENT_NAMESPACE + "SubmitGET";
	private static final String REF_DIM = Activator.CLIENT_NAMESPACE + "SubmitGET";
	private static final String REF_EXIT = Activator.CLIENT_NAMESPACE + "SubmitEXIT";
	private static final String PROP_PATH_SELECTED_LAMP = Activator.CLIENT_NAMESPACE + "selectLight";
	private static final String PROP_PATH_DIM = Activator.CLIENT_NAMESPACE + "dimLight";
	// user variabel identifying who is this app interacting with
	private User sampleUser;

	// Extended constructor
	protected UIExample(ModuleContext context, String namespace, String url, String desc) {
		super(context, namespace, url, desc);
	}

	// Called when initial dialog is requested by user -> show main dialog
	@Override
	public void executeStartUI(Resource user) {
		// Call the GET LAMPS service
		Request req = new Request(new DeviceService());
		req.put(Path.at(DeviceService.PROP_CONTROLS), Arg.out(OUTPUT_LIST_OF_LAMPS));
		ServiceResponse resp = Activator.caller.call(req);
		Object[] lights = Request.recoverOutputs(resp, OUTPUT_LIST_OF_LAMPS);

		// User safe check (not necessary in 1.2.0)
		if (user instanceof User) {
			sampleUser = (User) user;
		} else {
			sampleUser = new User(user.getURI());
		}

		// Create the main dialog: SelectOne<lights> Range<0-100> Submit<ON>
		// Submit<OFF> Submit<DIM> Submit<EXIT>
		Dialog d = new Dialog(sampleUser, Activator.CLIENT_APPNAME);
		SelectOne one = new SelectOne(PROP_PATH_SELECTED_LAMP, "Lamps");
		for (int i = 0; i < lights.length; i++) {
			one.addOption(((LightController) lights[i]).getURI());
		}
		d.add(one);
		SelectRange range = new SelectRange(PROP_PATH_DIM, "Dimmer", 0, 100, 50);
		d.add(range);
		SubmitCmd sub1 = new SubmitCmd(REF_ON, "Turn On");
		sub1.addMandatoryInput(one);
		d.addSubmit(sub1);
		SubmitCmd sub2 = new SubmitCmd(REF_OFF, "Turn Off");
		sub2.addMandatoryInput(one);
		d.addSubmit(sub2);
		SubmitCmd sub3 = new SubmitCmd(REF_GET, "Get Info");
		sub3.addMandatoryInput(one);
		d.addSubmit(sub3);
		SubmitCmd sub4 = new SubmitCmd(REF_DIM, "Dim");
		sub4.addMandatoryInput(one);
		sub4.addMandatoryInput(range);
		d.addSubmit(sub4);
		d.addSubmit(Forms.submit(REF_EXIT, "Exit"));
		sendUIRequest(d);
	}

	// Display a pop up message describing a light status
	public void showMessage(String light, String loc, Integer integer) {
		if (sampleUser != null) {
			Message m = new Message(sampleUser, "Simple Client Subscriber",
					"Brightness of Light " + light + " in " + loc + " is " + integer);
			sendUIRequest(m);
		}
	}

	// Called when user has completed a dialog
	@Override
	public void handleUIResponse(UIResponse input) {
		String submit = input.getSubmissionID();
		if (submit.contains(REF_ON) || submit.contains(REF_OFF)) {
			// Turn ON/OFF the light selected in SelectOne
			Integer val = Integer.valueOf(submit.contains(REF_ON) ? 100 : 0);
			String selected = (String) input.getUserInput(Path.at(PROP_PATH_SELECTED_LAMP).path);
			// Call TURN ON or TURN OFF
			Request req = new Request(new DeviceService());
			req.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(new LightController(selected)));
			req.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.change(val));
			Activator.caller.call(req);
		}
		if (submit.contains(REF_DIM)) {
			// Dim the light selected in SelectOne
			Integer val = (Integer) input.getUserInput(Path.at(PROP_PATH_DIM).path);
			String selected = (String) input.getUserInput(Path.at(PROP_PATH_SELECTED_LAMP).path);
			// Call DIM
			Request req = new Request(new DeviceService());
			req.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(new LightController(selected)));
			req.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.change(val));
			Activator.caller.call(req);
		}
		if (submit.contains(REF_GET)) {
			// Get the status of the light selected in SelectOne
			String selected = (String) input.getUserInput(Path.at(PROP_PATH_SELECTED_LAMP).path);
			// Call GET LAMP INFO
			Request req = new Request(new DeviceService());
			req.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(new LightController(selected)));
			req.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_HAS_VALUE), Arg.out(OUTPUT_BR));
			req.put(Path.at(DeviceService.PROP_CONTROLS).to(LightController.PROP_PHYSICAL_LOCATION),
					Arg.out(OUTPUT_LOC));
			ServiceResponse resp = Activator.caller.call(req);
			// Display pop up message with light status
			showMessage(selected, resp.getOutput(OUTPUT_LOC, true).get(0).toString(),
					(Integer) resp.getOutput(OUTPUT_BR, true).get(0));
		}
	}

	@Override
	public void dialogAborted(String dialogID, Resource data) {
		// TODO Auto-generated method stub
	}

}
