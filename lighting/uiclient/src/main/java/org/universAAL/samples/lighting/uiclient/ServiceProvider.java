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
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.middleware.service.owl.InitialServiceDialog;

/**
 * @author mtazari
 *
 */
public class ServiceProvider extends ServiceCallee {

	public static final String UI_LIGHTING_CLIENT_NAMESPACE = Activator.CLIENT_LIGHTING_UI_NAMESPACE +"ServiceProvider#";
	public static final String CLASS_URI = UI_LIGHTING_CLIENT_NAMESPACE + "Main";
	public static final String START_URI = UI_LIGHTING_CLIENT_NAMESPACE + "MainMenu";

	
	ServiceProvider(BundleContext context) {
		super(context, new ServiceProfile[]{
				InitialServiceDialog.createInitialDialogProfile(
						CLASS_URI, //Lighting.MY_URI,
						"http://www.igd.fraunhofer.de",
						"Sample Lighting UI Client",
						START_URI)
		});
	}

	/**
	 * @see org.persona.middleware.service.ServiceCallee#communicationChannelBroken()
	 */
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see org.persona.middleware.service.ServiceCallee#handleCall(org.persona.middleware.service.ServiceCall)
	 */
	public ServiceResponse handleCall(ServiceCall call) {
		if (call != null) {
			String operation = call.getProcessURI();
			if (operation != null
					&& operation.startsWith(START_URI)) {
				System.out.println("-- Lighting UI Client Main Menu --");
				Form dialog = Activator.outputProvider.startMainDialog();
				
				ServiceResponse sr = new ServiceResponse(CallStatus.succeeded);
//				sr.addOutput(new ProcessOutput(DialogService.OUTPUT_STARTED_DIALOG, dialog));
				return sr;
			}
		}
		return null;
	}
}
