importPackage(java.util);
importClass(org.universAAL.ontology.lighting.Lighting);
importClass(org.universAAL.ontology.lighting.LightSource);
importClass(java.lang.Integer);
importClass(org.universAAL.middleware.service.owls.profile.ServiceProfile);

var NAMESPACE = "http://ontology.igd.fhg.de/ASORLightingServer.owl#";
var SERVICE_GET_CONTROLLED_LAMPS = NAMESPACE + "getControlledLamps";
var SERVICE_TURN_OFF = NAMESPACE + "turnOff";
var SERVICE_TURN_ON = NAMESPACE + "turnOn";
var OUTPUT_CONTROLLED_LAMPS = NAMESPACE + "controlledLamps";
var INPUT_LAMP_URI = NAMESPACE + "lampURI";
var LAMP = NAMESPACE + "lamp";
var value = [0,0];

// callback function for the service profile
function handleCall(call) {
	println(" -- handleCall");
	var operation = call.getProcessURI();
	if (operation.startsWith(SERVICE_GET_CONTROLLED_LAMPS))
		return getLamps();
		
	var input = call.getInputValue(INPUT_LAMP_URI);
	if (input == null)
	    return null;

	if (operation.startsWith(SERVICE_TURN_ON))
	    return turnX(input.toString(), Integer.valueOf(100));
		
	if (operation.startsWith(SERVICE_TURN_OFF))
	    return turnX(input.toString(), Integer.valueOf(0));
}

function getLamps() {
	println(" -- handleGetLamps");
	var sr = new ServiceResponse(CallStatus.succeeded);
	var lamps = Arrays.asList([new LightSource(LAMP + "0"), new LightSource(LAMP + "1")]);
	sr.addOutput(new ProcessOutput(OUTPUT_CONTROLLED_LAMPS, lamps));
	return sr;
}

function turnX(lampURI, newval) {
	var idx = lampURI.substr(LAMP.length);
	println(" -- turnX (to " + newval + ") for lamp " + idx + " (old val: " + value[idx] + ")");
	var change = value[idx] != newval;
	value[idx] = newval;
	if (change) {
		// publish context event for the change
		ctxt.publish(new LightSource(lampURI), LightSource.PROP_SOURCE_BRIGHTNESS, newval);
	}
	return new ServiceResponse(CallStatus.succeeded);
}

var ppControls = [Lighting.PROP_CONTROLS];
var ppBrightness = [Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS];

// service profile for getLamps
var getControlledLamps = new Lighting(SERVICE_GET_CONTROLLED_LAMPS);
getControlledLamps.addOutput(OUTPUT_CONTROLLED_LAMPS, LightSource.MY_URI, 0, -1, ppControls);

// service profile for turnOff
var turnOff = new Lighting(SERVICE_TURN_OFF);
turnOff.addFilteringInput(INPUT_LAMP_URI, LightSource.MY_URI, 1, 1, ppControls);
turnOff.getProfile().addChangeEffect(ppBrightness, Integer.valueOf(0));

// service profile for turnOn
var turnOn = new Lighting(SERVICE_TURN_ON);
turnOn.addFilteringInput(INPUT_LAMP_URI, LightSource.MY_URI, 1, 1, ppControls);
turnOn.getProfile().addChangeEffect(ppBrightness, Integer.valueOf(100));

// service profile registration, will call function handleCall
var	profiles = [getControlledLamps.getProfile(), turnOff.getProfile(), turnOn.getProfile()];
service.register("handleCall", profiles);

