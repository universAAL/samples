package org.universAAL.ontology.lighting.simple;

import org.universAAL.middleware.api.annotation.ChangeEffect;
import org.universAAL.middleware.api.annotation.Input;
import org.universAAL.middleware.api.annotation.OntologyClasses;
import org.universAAL.middleware.api.annotation.Output;
import org.universAAL.middleware.api.annotation.Outputs;
import org.universAAL.middleware.api.annotation.ServiceOperation;
import org.universAAL.middleware.api.annotation.UniversAALService;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;

@UniversAALService(namespace = LightingInterfaceLevel2.namespace, name = "LightingService")
@OntologyClasses(value = { Lighting.class })
public interface LightingInterfaceLevel2 {

    public final static String namespace = "http://ontology.igd.fhg.de/LightingServer.owl#";

    @ServiceOperation
    public LightSource[] getControlledLamps();

    @ServiceOperation
    @Outputs(value = { @Output(name = "brightness"), @Output(name = "location") })
    public Object[] getLampInfo(@Input(name = "lampURI") LightSource lamp);

    @ServiceOperation
    @ChangeEffect(propertyPaths = { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
	    value = "0", valueType = Integer.class)
    public void turnOff(@Input(name = "lampURI") LightSource lamp);

    @ServiceOperation
    @ChangeEffect(propertyPaths = { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
	    value = "100", valueType = Integer.class)
    public void turnOn(@Input(name = "lampURI") LightSource lamp);
}
