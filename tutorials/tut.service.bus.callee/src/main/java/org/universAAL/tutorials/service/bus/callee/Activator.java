/*
    Copyright 2016-2020 Carsten Stockloew

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
package org.universAAL.tutorials.service.bus.callee;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;

/**
 * The module activator handles the starting and stopping of this module. It is
 * similar to the OSGi BundleActivator.
 * 
 * In our case, we simply want to publish a context event, so we do this
 * directly in the start method.
 * 
 * @author Carsten Stockloew
 */
public class Activator implements ModuleActivator {

	public static ModuleContext mc;
	private MyServiceCallee callee = null;

	/**
	 * This method is called by the framework to start the module.
	 */
	public void start(ModuleContext mc) throws Exception {
		Activator.mc = mc;
		if (callee == null) {
			// create our service callee
			callee = new MyServiceCallee(mc);
		}
	}

	/**
	 * This method is called by the framework to stop the module.
	 */
	public void stop(ModuleContext arg0) throws Exception {
		if (callee != null) {
			// close our service callee and free all resources
			callee.close();
			callee = null;
		}
	}
}
