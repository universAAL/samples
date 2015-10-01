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
package org.universAAL.support.tutorial.sbus.server;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;

public class LampProvider {

    public LampProvider(ModuleContext mc) {
	for (ServiceProfile prof : ProfileUtil.profiles) {
	    // create a separate callee for each profile
	    ServiceProfile[] profarr = new ServiceProfile[] { prof };

	    new ServiceCallee(mc, profarr) {
		@Override
		public void communicationChannelBroken() {
		}

		@Override
		public ServiceResponse handleCall(ServiceCall call) {
		    if (call == null)
			return null;

		    String op = call.getProcessURI();
		    if (op == null)
			return null;

		    System.out.println("Server received call for: "
			    + op.substring(0, op.length() - 7));

		    return new ServiceResponse(CallStatus.succeeded);
		}
	    };
	}
    }
}
