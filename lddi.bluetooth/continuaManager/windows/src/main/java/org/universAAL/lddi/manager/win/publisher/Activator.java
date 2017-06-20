/*
    Copyright 2007-2014 TSB, http://www.tsbtecnologias.es
    Technologies for Health and Well-being - Valencia, Spain

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
/**
 * x073 Continua agent publisher (agent events will be published over uAAL bus)
 * 
 * @author Angel Martinez-Cavero
 * @version 0
 *  
 * TSB Technologies for Health and Well-being
 */

// Package
package org.universAAL.lddi.manager.win.publisher;

// Imports
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.universAAL.lddi.manager.win.gui.GUI;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;

// Main class
public class Activator implements BundleActivator {

	// /Continua Health
	// Manager|http://www.tsbtecnologias.es|http://ontologies.universAAL.com/CONTINUAHEALTHMANAGERUI.owl#ContinuaManager

	// Attributes
	/** Main GUI object */
	private GUI gui = null;

	// /** Bundle context object */
	// private BundleContext ctx = null;

	private ModuleContext mdlContext;
	private BundleContext bndContext;
	private ServiceProvider service;
	public static boolean dllReadyLatch = true;

	// Methods
	/** Start */
	public void start(BundleContext context) throws Exception {
		mdlContext = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });
		// Create and show main GUI frame
		bndContext = context;
		gui = new GUI(bndContext);
		gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gui.setVisible(false);
			}
		});
		// TODO cambiar para la demo true -> false
		gui.setVisible(false);
		// Service callee
		service = new ServiceProvider(mdlContext, gui);
	}

	/** Stop */
	public void stop(BundleContext arg0) throws Exception {
		gui.setVisible(false);
		dllReadyLatch = true;
		// gui.stopGUI();
		// bndContext = null;
		// mdlContext = null;
	}

	void stopSafe(final Bundle bundle) {
		new Thread() {
			public void run() {
				try {
					bundle.stop();
				} catch (BundleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}