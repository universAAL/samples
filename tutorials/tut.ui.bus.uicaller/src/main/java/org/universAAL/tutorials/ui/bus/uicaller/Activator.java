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

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.ui.UICaller;

/**
 * The module activator handles the starting and stopping of this module. It is
 * similar to the OSGi BundleActivator.
 *
 * In our case, we simply want to create a UI Main Menu service, so we do this
 * directly in the start method.
 *
 * @author amedrano
 *
 */
public class Activator implements ModuleActivator {

	static ModuleContext context;
	public ServiceCallee service;
	public UICaller uiCaller;

	public void start(ModuleContext mc) throws Exception {
		context = mc;

		if (uiCaller == null) {
			uiCaller = new MyUICaller(context);
		}
		if (service == null) {
			service = new MainButtonServiceCallee(context, uiCaller);
		}
	}

	public void stop(ModuleContext mc) throws Exception {
		if (service != null) {
			service.close();
			service = null;
		}
		if (uiCaller != null) {
			uiCaller.close();
			uiCaller = null;
		}
	}

}
