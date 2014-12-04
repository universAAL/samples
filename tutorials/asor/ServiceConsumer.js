importPackage(java.util);
importClass(org.universAAL.ontology.lighting.Lighting);

// setup and issue service requests
var OUTPUT_CONTROLLED_LAMPS = 'http://ontology.igd.fhg.de/LightingConsumer.owl#controlledLamps';

// request and print all lamps
var sr = new ServiceRequest(new Lighting(), null);
sr.addRequiredOutput(OUTPUT_CONTROLLED_LAMPS, [Lighting.PROP_CONTROLS]);

var map = service.call(sr);
if (map != null) {
	var lamps = map.get(OUTPUT_CONTROLLED_LAMPS);

	println("--- All lamps:");
	for (var i=0; i<lamps.size(); i++) {
	  println(lamps.get(i));
	}
}
