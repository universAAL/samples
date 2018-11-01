package org.universAAL.tutorials.service.bus.callee;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.universAAL.middleware.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.impl.ServiceRealization;
import org.universAAL.ontology.device.DeviceOntology;
import org.universAAL.ontology.device.LightActuator;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.measurement.MeasurementOntology;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.unit.UnitOntology;

public class ServiceMatchmakingTest {
	private static JUnitModuleContext mc;
	
	@BeforeClass
	public static void loadOntologies() {
		mc = new JUnitModuleContext();
		
		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new UnitOntology());
		OntologyManagement.getInstance().register(mc, new MeasurementOntology());
		OntologyManagement.getInstance().register(mc, new LocationOntology());
		OntologyManagement.getInstance().register(mc, new PhThingOntology());
		OntologyManagement.getInstance().register(mc, new DeviceOntology());
	}

	@Test
	public void testTurnOn() {
		ServiceRealization srz = new ServiceRealization("urn:org.universAAL.tutorial:tut.callee#MyServiceCallee", MyServiceCallee.getProfiles()[0]);
		
		ServiceRequest srq = new ServiceRequest(new DeviceService(), null);
		srq.addValueFilter(new String[] { DeviceService.PROP_CONTROLS }, new LightActuator("urn:org.universAAL.tutorial:tut.lighting#lamp1"));
		srq.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, ValueDevice.PROP_HAS_VALUE },  100);
		
		assertTrue(srz.matches(srq, new HashMap<String, Object>(), null));
	}

}
