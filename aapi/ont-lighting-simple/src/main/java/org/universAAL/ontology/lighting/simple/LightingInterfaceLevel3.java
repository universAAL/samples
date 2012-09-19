package org.universAAL.ontology.lighting.simple;

import org.universAAL.middleware.api.annotation.Input;
import org.universAAL.middleware.api.annotation.Output;
import org.universAAL.middleware.api.annotation.Outputs;
import org.universAAL.middleware.api.annotation.OntologyClasses;
import org.universAAL.middleware.api.annotation.ServiceOperation;
import org.universAAL.middleware.api.annotation.UniversAALService;
import org.universAAL.ontology.lighting.Lighting;

@UniversAALService(namespace = LightingInterfaceLevel3.namespace, name = "LightingService")
@OntologyClasses(value = { Lighting.class })
public interface LightingInterfaceLevel3 {

    public final static String namespace = "http://ontology.igd.fhg.de/LightingServer.owl#";

    @ServiceOperation
    public Integer[] getControlledLamps();

    @ServiceOperation
    @Outputs(value = { @Output(name = "brightness"), @Output(name = "location") })
    public Object[] getLampInfo(@Input(name = "lampURI") int lampID);

    @ServiceOperation
    public void turnOff(@Input(name = "lampURI") int lampID);

    @ServiceOperation
    public void turnOn(@Input(name = "lampURI") int lampID);
}
