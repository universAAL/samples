package org.universAAL.ontology.lighting.simple;

import org.universAAL.middleware.api.annotation.ChangeEffect;
import org.universAAL.middleware.api.annotation.Input;
import org.universAAL.middleware.api.annotation.OntologyClasses;
import org.universAAL.middleware.api.annotation.Output;
import org.universAAL.middleware.api.annotation.Outputs;
import org.universAAL.middleware.api.annotation.ServiceOperation;
import org.universAAL.middleware.api.annotation.UniversAALService;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.location.Location;

@UniversAALService(namespace = LightingInterfaceLevel1.namespace, name = "LightingService")
@OntologyClasses(value = { Lighting.class })
public interface LightingInterfaceLevel1 {

    public final static String namespace = "http://ontology.igd.fhg.de/LightingServer.owl#";
    
    @ServiceOperation(value = "getControlledLamps")
    @Output(name = "controlledLamps", propertyPaths = { Lighting.PROP_CONTROLS })
    public LightSource[] getControlledLamps();

    @ServiceOperation
    @Outputs(value = {
	    @Output(name = "brightness", filteringClass = Integer.class, propertyPaths = {
		    Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS }),
	    @Output(name = "location", filteringClass = Location.class, propertyPaths = {
		    Lighting.PROP_CONTROLS, Resource.uAAL_VOCABULARY_NAMESPACE + "hasLocation" }) })
    public Object[] getLampInfo(
	    @Input(name = "lampURI", propertyPaths = { Lighting.PROP_CONTROLS }) LightSource lamp);

    @ServiceOperation
    @ChangeEffect(propertyPaths = { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
	    value = "0", valueType = Integer.class)
    public void turnOff(
	    @Input(name = "lampURI", propertyPaths = { Lighting.PROP_CONTROLS }) LightSource lamp);

    @ServiceOperation
    @ChangeEffect(propertyPaths = { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
	    value = "100", valueType = Integer.class)
    public void turnOn(
	    @Input(name = "lampURI", propertyPaths = { Lighting.PROP_CONTROLS }) LightSource lamp);
    
}
