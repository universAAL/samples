package org.universAAL.samples.soap.cxf.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.serialization.MessageContentSerializer;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.samples.soap.cxf.service.ServiceInterface;

public class ServiceImpl implements ServiceInterface {
	public static ServiceResponse sr;

	private static ServiceCaller caller;
	private static final String LIGHTING_CONSUMER_NAMESPACE = "http://ontology.igd.fhg.de/LightingConsumer.owl#";
	private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CONSUMER_NAMESPACE + "controlledLamps";
	MessageContentSerializer m;
	ContextEvent event;

	ArrayList<String> list = new ArrayList<String>();

	public Collection<String> getAllLamps() {
		Device[] d = getControlledLamps();

		String[] lamps = new String[d != null ? d.length : 0];
		for (int i = 0; i < lamps.length; i++) {
			lamps[i] = d[i].getURI();
			list.add(lamps[i]);
			System.out.println("Server: returning Lamp list OK");
		}
		Object[] contentSerializerParams = new Object[] { MessageContentSerializer.class.getName() };
		MessageContentSerializer s = (MessageContentSerializer) Activator.mc.getContainer()
				.fetchSharedObject(Activator.mc, contentSerializerParams);
		String sGetLamps = s.serialize(sr);

		// System.out.println("\n" + sGetLamps + "\n");

		return list;
	}

	// create a ServiceRequest to get the lamp list
	public static ServiceRequest getAllLampsRequest() {

		ServiceRequest getAllLamps = new ServiceRequest(new Lighting(), null);

		getAllLamps.addRequiredOutput(OUTPUT_LIST_OF_LAMPS, new String[] { Lighting.PROP_CONTROLS });

		return getAllLamps;
	}

	// create a ServiceRequest for different type of calls, using Turle strings
	public String getTurtleServiceReq(String turtleStr) {
		caller = new DefaultServiceCaller(Activator.mc);

		sr = caller.call(turtleStr);
		Object[] contentSerializerParams = new Object[] { MessageContentSerializer.class.getName() };
		MessageContentSerializer s = (MessageContentSerializer) Activator.mc.getContainer()
				.fetchSharedObject(Activator.mc, contentSerializerParams);
		String serializedStr = s.serialize(sr);

		return serializedStr;
	}

	// Get a list of all available lamps
	public static Device[] getControlledLamps() {
		caller = new DefaultServiceCaller(Activator.mc);
		// Make a call for the lamps and get the request
		ServiceRequest srq = getAllLampsRequest();

		sr = null;

		sr = caller.call(srq);

		if (sr.getCallStatus() == CallStatus.succeeded) {
			try {
				List lampList = sr.getOutput(OUTPUT_LIST_OF_LAMPS, true);

				if (lampList == null || lampList.size() == 0) {
					LogUtils.logInfo(Activator.mc, ServiceImpl.class, "getControlledLamps",
							new Object[] { "there are no lamps" }, null);
					return null;
				}

				LightSource[] lamps = (LightSource[]) lampList.toArray(new LightSource[lampList.size()]);

				return lamps;

			} catch (Exception e) {
				LogUtils.logError(Activator.mc, ServiceImpl.class, "getControlledLamps",
						new Object[] { "got exception", e.getMessage() }, e);
				return null;
			}
		} else {
			LogUtils.logWarn(Activator.mc, ServiceImpl.class, "getControlledLamps",
					new Object[] { "callstatus is not succeeded" }, null);
			return null;
		}
	}

	// create a ServiceRequest for turning off a lamp using its URI
	public String turnOffLamp(String lampURI) {

		caller = new DefaultServiceCaller(Activator.mc);
		ServiceResponse sr1 = new ServiceResponse();
		sr1 = caller.call(turnOffRequest(lampURI));
		if (sr1.getCallStatus() == CallStatus.succeeded) {

			return lampURI + " turned Off";
		} else
			return lampURI + " service_specific_failure";

	}

	private static ServiceRequest turnOffRequest(String lampURI) {

		ServiceRequest turnOff = new ServiceRequest(new Lighting(), null);

		turnOff.addValueFilter(new String[] { Lighting.PROP_CONTROLS }, new LightSource(lampURI));

		turnOff.addChangeEffect(new String[] { Lighting.PROP_CONTROLS, LightSource.PROP_SOURCE_BRIGHTNESS },
				new Integer(0));
		return turnOff;
	}
}