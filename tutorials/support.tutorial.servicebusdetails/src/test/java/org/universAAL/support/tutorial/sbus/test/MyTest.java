package org.universAAL.support.tutorial.sbus.test;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.container.JUnit.JUnitModuleContext.LogLevel;
import org.universAAL.middleware.bus.junit.BusTestCase;
import org.universAAL.middleware.bus.permission.AccessControl;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.support.tutorial.sbus.client.MyConsumer;
import org.universAAL.support.tutorial.sbus.ontology.TutorialOntology;
import org.universAAL.support.tutorial.sbus.server.LampProvider;

public class MyTest extends BusTestCase {

    MyConsumer lc;
    LampProvider lp;

    @Override
    protected void setUp() throws Exception {
	super.setUp();

	OntologyManagement.getInstance().register(mc, new LocationOntology());
	OntologyManagement.getInstance().register(mc, new ShapeOntology());
	OntologyManagement.getInstance().register(mc, new PhThingOntology());
	OntologyManagement.getInstance().register(mc, new TutorialOntology());

	mc.setAttribute(AccessControl.PROP_MODE, "none");
	((JUnitModuleContext) mc).setLogLevel(LogLevel.ERROR);

	lp = new LampProvider(mc);
	lc = new MyConsumer(mc);
    }

    public void test() {
    }
}
