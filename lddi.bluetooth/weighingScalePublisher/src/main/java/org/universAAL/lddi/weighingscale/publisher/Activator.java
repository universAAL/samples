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
package org.universAAL.continua.weighingscale.publisher;
/**
 * x073 Continua agent publisher (agent events will be published over uAAL bus)
 * 
 * @author Angel Martinez-Cavero
 * @version 0
 *  
 * TSB Technologies for Health and Well-being
 */


// Imports
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

// Main class
public class Activator implements BundleActivator {

	// Attributes

	// Publisher object to send events
	private Publisher uaalPublisher = null;	

	// HDP manager object
	private hdpManager manager = null;	
	
	//
	private BundleContext ctx = null;    

	// Methods
	/** Start */
	public void start(BundleContext context) throws Exception {		
		ctx = context;
		new Thread(){
			public void run(){   
				uaalPublisher = new Publisher(ctx);
				// Start manager and wait for agent events
				manager = new hdpManager(uaalPublisher);
				manager.init();					
			}			
		}.start();		
	}

	/** Stop */
	public void stop(BundleContext arg0) throws Exception {		
		manager.exit();		
		ctx = null;
		uaalPublisher = null;
		manager = null;	
	}
}