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

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.input.InputSubscriber;


public class InputConsumer extends InputSubscriber {

	protected InputConsumer(BundleContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	public void dialogAborted(String arg0) {
		// TODO Auto-generated method stub
	}

	public void handleInputEvent(InputEvent ie) {
		if (ie != null) {
			// Cancel Dialog, go back to main dialog
			if (OutputProvider.SUBMISSION_EXIT.equals(ie.getSubmissionID()))
				return;

			Object o;
			
			// get lamp index
			int lampindex = -1;
			String lampURI = null;
			o = ie.getUserInput(new String[]{OutputProvider.PROP_SELECTED_LAMP_INDEX});
			if (o instanceof Integer) {
				lampindex = ((Integer) o).intValue();
				lampURI = Activator.outputProvider.getDeviceURI(lampindex);
			}
			
			if (lampURI != null) {

				// button
				if (OutputProvider.SUBMISSION_ON.equals(ie.getSubmissionID())) {
					LightingConsumer.turnOn(lampURI);
				} else if (OutputProvider.SUBMISSION_OFF.equals(ie.getSubmissionID())) {
					LightingConsumer.turnOff(lampURI);
				} else if (OutputProvider.SUBMISSION_SCALE.equals(ie.getSubmissionID())) {
					// get scale value
					String scaleval = null;
					o = ie.getUserInput(new String[]{OutputProvider.PROP_SCALE_VALUE});
					if (o instanceof String) {
						scaleval = (String)o;
						try {
							int scale = Integer.parseInt(scaleval);
							LightingConsumer.dimToValue(lampURI, new Integer(scale));
						} catch (NumberFormatException e) {}
					}
				}
			}
		}
		Activator.outputProvider.startMainDialog();
	}

	void subscribe(String dialogID) {
		super.addNewRegParams(dialogID);
	}
}
