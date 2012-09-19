package org.universAAL.samples.lighting.server;

import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel1;

/**
 * Simplified API in Level 1. Implementation of the server side.
 * 
 * @author dzmuda
 * @author mpsiuk
 */
public class LightingSimplifiedServiceLevel1 extends MyLightingOntologified
	implements LightingInterfaceLevel1 {
    public LightingSimplifiedServiceLevel1() {
	super(namespace);
    }
}
