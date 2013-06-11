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

@UniversAALService(namespace = "LightingNamespace", name = "LightingService")
@OntologyClasses(value = { })
public interface LightingInterface {
	
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
