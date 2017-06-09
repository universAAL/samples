/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.ontology;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.owl.Ontology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.ontology.tutorial.TutorialOntology;

//You need an Activator in your ontology bundle because it must be started...
public class TutorialActivator implements ModuleActivator {

	private Ontology tutorialOntology = new TutorialOntology();

	public void start(ModuleContext context) throws Exception {
		OntologyManagement om = OntologyManagement.getInstance();
		// For every general Ontology class included in your ontology bundle you
		// must register it here
		om.register(context, tutorialOntology);
	}

	public void stop(ModuleContext context) throws Exception {
		// Unload the ontologies
		OntologyManagement om = OntologyManagement.getInstance();
		om.unregister(context, tutorialOntology);
	}
}
