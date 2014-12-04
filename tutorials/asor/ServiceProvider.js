importPackage(java.util);
importClass(org.universAAL.ontology.lighting.Lighting);
importClass(org.universAAL.ontology.lighting.LightSource);

var NAMESPACE = "http://ontology.igd.fhg.de/ASORLightingServer.owl#";
var SERVICE_GET_CONTROLLED_LAMPS = NAMESPACE + "getControlledLamps";
var OUTPUT_CONTROLLED_LAMPS = NAMESPACE + "controlledLamps";
var LAMP = NAMESPACE + "lamp";

// callback function for the service profile
function handleCall(call) {
	println(" -- handleCall");
	var operation = call.getProcessURI();
	if (operation.startsWith(SERVICE_GET_CONTROLLED_LAMPS)) {
		var sr = new ServiceResponse(CallStatus.succeeded);
		var lamps = Arrays.asList([new LightSource(LAMP + "0"), new LightSource(LAMP + "1")]);
		sr.addOutput(new ProcessOutput(OUTPUT_CONTROLLED_LAMPS, lamps));
		return sr;
	}
}

// service profile for getLamps
var getControlledLamps = new Lighting(SERVICE_GET_CONTROLLED_LAMPS);
getControlledLamps.addOutput(OUTPUT_CONTROLLED_LAMPS, LightSource.MY_URI, 0, -1, [Lighting.PROP_CONTROLS]);

// service profile registration, will call function handleCall
var profiles = [getControlledLamps.getProfile()];
service.register("handleCall", profiles);
