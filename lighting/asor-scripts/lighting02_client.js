importPackage(java.util);
importClass(java.lang.Integer);
importClass(org.universAAL.ontology.lighting.Lighting);
importClass(org.universAAL.ontology.lighting.LightSource);

// register context event pattern
function handleEvent(event) {
	println("Received context event:\n"+ "    Subject     = " +
		event.getSubjectURI() + "\n" + "    Subject type= " +
		event.getSubjectTypeURI() + "\n" + "    Predicate   = " +
		event.getRDFPredicate() + "\n" + "    Object      = " +
		event.getRDFObject());
}

var cep = new ContextEventPattern();
cep.addRestriction(MergedRestriction.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT, LightSource.MY_URI));
ctxt.register("handleEvent", cep);

// setup and issue service requests
var OUTPUT_CONTROLLED_LAMPS = 'http://ontology.igd.fhg.de/LightingConsumer.owl#controlledLamps';

var ppControls = [Lighting.PROP_CONTROLS];
var ppBrightness = [Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS];

// request and print all lamps
var sr = new ServiceRequest(new Lighting(), null);
sr.addRequiredOutput(OUTPUT_CONTROLLED_LAMPS, ppControls);

var map = service.call(sr);
if (map != null) {
	var lamps = map.get(OUTPUT_CONTROLLED_LAMPS);

	println("--- All lamps:");
	for (var i=0; i<lamps.size(); i++) {
	  println(lamps.get(i));
	}

	if (lamps.size() > 0) {
		// request turn on for first lamp
		sr = new ServiceRequest(new Lighting(), null);
		sr.addValueFilter(ppControls, new LightSource(lamps.get(0)));
		sr.addChangeEffect(ppBrightness, Integer.valueOf(100));
		service.call(sr);

		// request turn off for first lamp
		sr = new ServiceRequest(new Lighting(), null);
		sr.addValueFilter(ppControls, new LightSource(lamps.get(0)));
		sr.addChangeEffect(ppBrightness, Integer.valueOf(0));
		service.call(sr);

		// request again (to see that there is no context event)
		service.call(sr);
	}
}
