/*
	Copyright 2008-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer Gesellschaft - Institut f�r Graphische Datenverarbeitung

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
package org.universAAL.samples.context.reasoner.client.osgi;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.samples.context.reasoner.client.gui.ReasoningGUI;
import org.universAAL.samples.context.reasoner.client.uaalinterface.CHECaller;
import org.universAAL.samples.context.reasoner.client.uaalinterface.ReasoningCaller;

public class GUIActivator {
	public static ReasoningCaller scaller = null;
	public static ReasoningGUI gui = null;
	public static CHECaller cheCaller = null;

	GUIActivator(BundleContext osgiContext, ModuleContext context) {
		scaller = new ReasoningCaller();
		gui = new ReasoningGUI(scaller);
		cheCaller = new CHECaller(context);
	}

	public void stop() throws Exception {
		scaller.unregister();
		scaller = null;
		gui.close();
		gui = null;
		cheCaller.close();
		cheCaller = null;
	}
}
