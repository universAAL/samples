importPackage(java.util);
importClass(org.universAAL.ontology.lighting.LightSource);
importClass(java.lang.Integer);

var NAMESPACE = "http://ontology.igd.fhg.de/ASORLightingServer.owl#";
var LAMP = NAMESPACE + "lamp";

// publish context event
ctxt.publish(new LightSource(LAMP+"1"), LightSource.PROP_SOURCE_BRIGHTNESS, Integer.valueOf(100));
