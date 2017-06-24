/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science

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
package org.universAAL.ontology.lighting.simple;

/**
 * This interface specifies all URIs related to LightingServer. The interface
 * can be either written by hand or generated automatically from annotated
 * interface of LightingServer.
 *
 * @author mpsiuk
 *
 */
public interface LightingServerURIs {
	String NAMESPACE = "http://ontology.igd.fhg.de/LightingServer.owl#";

	/*
	 * Specification of service utilities URIs together with URIs of their input
	 * and output parameters
	 */
	public interface GetControlledLamps {
		String URI = NAMESPACE + "getControlledLamps";

		public interface Output {
			String CONTROLLED_LAMPS = NAMESPACE + "controlledLamps";
		}
	}

	public interface GetLampInfo {
		String URI = NAMESPACE + "getLampInfo";

		public interface Input {
			String LAMP_URI = NAMESPACE + "lampURI";
		}

		public interface Output {
			String LAMP_BRIGHTNESS = NAMESPACE + "brightness";
			String LAMP_LOCATION = NAMESPACE + "location";
		}
	}

	public interface TurnOn {
		String URI = NAMESPACE + "turnOn";

		public interface Input {
			String LAMP_URI = NAMESPACE + "lampURI";
		}
	}

	public interface TurnOff {
		String URI = NAMESPACE + "turnOff";

		public interface Input {
			String LAMP_URI = NAMESPACE + "lampURI";
		}
	}
}
