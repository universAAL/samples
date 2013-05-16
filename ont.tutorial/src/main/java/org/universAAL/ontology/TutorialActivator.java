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
	// For every general Ontology class included in your ontology bundle you must register it here
	om.register(context, tutorialOntology);
    }

    public void stop(ModuleContext context) throws Exception {
	// Unload the ontologies
	OntologyManagement om = OntologyManagement.getInstance();
	om.unregister(context, tutorialOntology);
    }
}
