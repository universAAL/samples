package org.universAAL.tutorials.service.bus.tryout;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.container.JUnit.JUnitModuleContext.LogLevel;
import org.universAAL.middleware.bus.junit.BusTestCase;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.ontology.device.DeviceOntology;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.measurement.MeasurementOntology;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.unit.UnitOntology;

public class MyTest extends BusTestCase {

    MyServiceCallee callee;
    ServiceCaller caller;

    @Override
    protected void setUp() throws Exception {
	// Initialization
	super.setUp();
	((JUnitModuleContext) mc).setLogLevel(LogLevel.ERROR);

	// as we do not start the platform in a regular way, we have to register
	// the required ontologies manually
	OntologyManagement.getInstance().register(mc, new LocationOntology());
	OntologyManagement.getInstance().register(mc, new ShapeOntology());
	OntologyManagement.getInstance().register(mc, new PhThingOntology());
	OntologyManagement.getInstance().register(mc, new UnitOntology());
	OntologyManagement.getInstance()
		.register(mc, new MeasurementOntology());
	OntologyManagement.getInstance().register(mc, new DeviceOntology());

	callee = new MyServiceCallee(mc);
	caller = new DefaultServiceCaller(mc);
    }

    /**
     * This is our main method. You can change the request and the profile in
     * {@link MyServiceCallee} as needed.
     */
    public void test() {
	ServiceRequest turnOn = new ServiceRequest(new DeviceService(), null);
	turnOn.addValueFilter(new String[] { DeviceService.PROP_CONTROLS },
		new LightActuator("urn:org.universAAL.aal_space:KitchenLight"));
	turnOn.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS,
		ValueDevice.PROP_HAS_VALUE }, new Integer(100));
	caller.call(turnOn);
    }
}
