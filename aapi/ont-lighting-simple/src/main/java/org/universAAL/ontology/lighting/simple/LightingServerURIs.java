package org.universAAL.ontology.lighting.simple;

/**
 * This interface specifies all URIs related to LightingServer. The interface
 * can be either written by hand or generated automatically from annotated
 * interface of LightingServer.
 * 
 * @author mpsiuk
 * 
 */
public interface LightingServerURIs {
    String NAMESPACE = "http://ontology.igd.fhg.de/LightingServer.owl#";
    /*
     * Specification of service utilities URIs together with URIs of their input
     * and output parameters
     */
    public interface GetControlledLamps {
	String URI = NAMESPACE + "getControlledLamps";
	public interface Output {
	    String CONTROLLED_LAMPS = NAMESPACE + "controlledLamps";
	}
    }
    public interface GetLampInfo {
	String URI = NAMESPACE + "getLampInfo";
	public interface Input {
	    String LAMP_URI = NAMESPACE + "lampURI";
	}
	public interface Output {
	    String LAMP_BRIGHTNESS = NAMESPACE + "brightness";
	    String LAMP_LOCATION = NAMESPACE + "location";
	}
    }
    public interface TurnOn {
	String URI = NAMESPACE + "turnOn";
	public interface Input {
	    String LAMP_URI = NAMESPACE + "lampURI";
	}
    }
    public interface TurnOff {
	String URI = NAMESPACE + "turnOff";
	public interface Input {
	    String LAMP_URI = NAMESPACE + "lampURI";
	}
    }
}
