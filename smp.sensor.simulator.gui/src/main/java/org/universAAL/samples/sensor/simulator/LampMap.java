/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.universAAL.samples.sensor.simulator;

import java.util.HashMap;

import org.universAAL.ontology.lighting.LightSource;

/**
 * Helper class used for storing light source URI and lightsource in a map
 *
 *
 */
public class LampMap {

	private HashMap<String, LightSource> lightSourceMap = new HashMap<String, LightSource>();

	public LampMap() {

	}

	/**
	 * Inserts lightsource and its URI to the map
	 *
	 * @param uri
	 * @param lightSource
	 */
	public void insertLightSource(String uri, LightSource lightSource) {
		lightSource.setBrightness(0);
		this.lightSourceMap.put(uri, lightSource);
	}

	/**
	 * Returns lightSource value that corresponds to a given URI
	 *
	 * @param uri
	 * @return
	 */
	public LightSource getLightSource(String uri) {
		return this.lightSourceMap.get(uri);
	}

}
