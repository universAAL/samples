package org.universAAL.samples.lighting.server;

import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel2;

/**
 * Simplified API in Level 2. Implementation of the server side.
 * 
 * @author dzmuda
 * @author mpsiuk
 */
public class LightingSimplifiedServiceLevel2 extends MyLightingOntologified
	implements LightingInterfaceLevel2 {
    public LightingSimplifiedServiceLevel2() {
	super(namespace);
    }
}
