package org.universAAL.lddi.samples.device.client;
/*
     Copyright 2010-2014 AIT Austrian Institute of Technology GmbH
	 http://www.ait.ac.at
     
     See the NOTICE file distributed with this work for additional
     information regarding copyright ownership

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
*/

import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.device.StatusValue;
import org.universAAL.ontology.device.SwitchActuator;
import org.universAAL.ontology.device.SwitchController;
import org.universAAL.ontology.device.ValueDevice;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.phThing.DeviceService;

/**
 * This class provides ontological service requests and processes the service
 * responses This class is stateless; no objects (sensors) are stored here
 * LogUtil from uAAL-Middleware is used here
 * 
 * @author Thomas Fuxreiter (foex@gmx.at)
 */
public class DeviceServiceCaller {

	private static ServiceCaller caller;
	private static final String DEVICE_CONSUMER_NAMESPACE = "http://ontology.universAAL.org/DeviceConsumer.owl#";
	private static final String OUTPUT_LIST_OF_DEVICES = DEVICE_CONSUMER_NAMESPACE + "controlledDevices";

	protected DeviceServiceCaller(ModuleContext context) {
		caller = new DefaultServiceCaller(context);
	}

	public ServiceRequest getAllDevicesRequest() {
		ServiceRequest getAllDevices = new ServiceRequest(new DeviceService(), null);
		getAllDevices.addRequiredOutput(OUTPUT_LIST_OF_DEVICES, new String[] { DeviceService.PROP_CONTROLS });
		return getAllDevices;
	}

	public String[] getControlledDevices() {
		ServiceResponse sr = caller.call(getAllDevicesRequest());
		if (sr.getCallStatus() == CallStatus.succeeded) {
			List<Device> deviceList = sr.getOutput(OUTPUT_LIST_OF_DEVICES, true);
			if (deviceList != null) {
				if (deviceList.size() != 0) {
					String[] devices = new String[deviceList.size()];
					for (int i = 0; i < devices.length; i++)
						devices[i] = ((Device) deviceList.get(i)).getURI();
					return devices;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		return null;
	}

	private ServiceRequest switchDeviceRequest(String deviceURI, boolean value) {
		ServiceRequest request = new ServiceRequest(new DeviceService(), null);
		request.addValueFilter(new String[] { DeviceService.PROP_CONTROLS }, new SwitchController(deviceURI));
		request.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, SwitchController.PROP_HAS_VALUE },
				value ? StatusValue.ACTIVATED : StatusValue.NOT_ACTIVATED
		// new Integer(value)
		);

		return request;
	}

	public boolean switchDevice(String deviceURI, boolean on) {
		ServiceResponse sr = caller.call(switchDeviceRequest(deviceURI, on));
		return sr.getCallStatus() == CallStatus.succeeded;
	}

	private ServiceRequest setLightValueRequest(String lampURI, int value) {
		ServiceRequest request = new ServiceRequest(new DeviceService(), null);
		request.addValueFilter(new String[] { DeviceService.PROP_CONTROLS }, new LightController(lampURI));
		request.addChangeEffect(new String[] { DeviceService.PROP_CONTROLS, LightController.PROP_HAS_VALUE },
				new Integer(value));
		return request;
	}

	public boolean switchdevice(String deviceURI, boolean on) {
		// ServiceResponse sr = caller.call(setLightValueRequest(deviceURI, on ?
		// 100 : 0));
		ServiceResponse sr = caller.call(switchDeviceRequest(deviceURI, on));
		return sr.getCallStatus() == CallStatus.succeeded;
	}

}

//
// // DefaultServiceCaller
// private static ServiceCaller caller;
//
// private static final String ACTIVITYHUB_CONSUMER_NAMESPACE =
// "http://ontology.universAAL.org/ActivityHubConsumer.owl#";
//
// private static final String OUTPUT_LIST_OF_AHS =
// ACTIVITYHUB_CONSUMER_NAMESPACE + "controlledActivityHubSensors";
// private static final String OUTPUT_AHS_LOCATION =
// ACTIVITYHUB_CONSUMER_NAMESPACE + "activityHubSensorLocation";
// private static final String OUTPUT_AHS_MEASUREMENT =
// ACTIVITYHUB_CONSUMER_NAMESPACE + "activityHubSensorMeasurement";
//
// // Factory from Ontology
// private static ActivityHubFactory factory = new ActivityHubFactory();
//
// /** This is the client application anchor; which knows nothing about
// Ontologies :-) */
// private DeviceClient dc;
//
// /**
// * Contructor
// * @param mc uAAL-Middleware ModuleContext
// * @param ahc link to client application
// */
// public DeviceServiceConsumer(ModuleContext mc, DeviceClient dc) {
// // the DefaultServiceCaller will be used to make ServiceRequest
// caller = new DefaultServiceCaller(mc);
// this.dc = dc;
// }
//
//
// // *****************************************************************
// // Controller Methods
// // *****************************************************************
//
// /**
// * Get a list of all available ActivityHub sensors in the system
// *
// * Get the ontological service request
// * Send service request to service bus
// * Process the response
// * Log errors if the call was not successful
// *
// * @return ArrayList with ActivityHubSensors (Ontology Resource objects)
// */
// public static ArrayList getControlledActivityHubSensors() {
//
// // Make a call for the sensors and get the response
// ServiceResponse sr = caller.call(getAllActivityHubSensorsRequest());
//
// if (sr.getCallStatus() == CallStatus.succeeded) {
// try {
// List ahsList = sr.getOutput(OUTPUT_LIST_OF_AHS, true);
// /*
// * Debug test:
// * 2 elements in list:
// * MotionSensor with 1 element in props
// *
// {http://www.w3.org/1999/02/22-rdf-syntax-ns#type=[http://ontology.universAAL.org/ActivityHub.owl#MotionSensor]}
// * and uri
// *
// http://ontology.universAAL.org/ActivityHubServer.owl#controlledActivityHubDevice1/1/1
// *
// * ContactClosureSensor with props
// *
// {http://www.w3.org/1999/02/22-rdf-syntax-ns#type=[http://ontology.universAAL.org/ActivityHub.owl#ContactClosureSensor]}
// * and uri
// *
// http://ontology.universAAL.org/ActivityHubServer.owl#controlledActivityHubDevice0/0/2
// */
//
// // exit if no elements
// if (ahsList == null || ahsList.size() == 0) {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors",
// new Object[] { "there are no ActivityHubSensors" }, null);
// return null;
// }
// LogUtils.logInfo(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors",
// new Object[] { "there are " + ahsList.size() + " objects in the response
// list!" }, null);
//
// //String [] ahsNames = (String[]) ahsList.toArray(new
// Object[ahsList.size()]);
// // simple create an array out of the ahs-array and give it back
//// LightSource[] lamps = (LightSource[]) lampList.toArray(new
// LightSource[lampList.size()]);
//
//
// ArrayList activityHubSensors = new ArrayList(ahsList.size());
// // iterate through sensor list and add sensors to output array
// Iterator it = ahsList.iterator();
// while (it.hasNext()) {
// ActivityHubSensor ahs = (ActivityHubSensor) it.next();
//
// LogUtils.logInfo(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors",
// new Object[] { "resource URI: " + ahs.getURI() +
// " sensorType: " + ahs.getSensorType()}, null);
//
//
//// ActivityHubSensor activityHubSensor = (ActivityHubSensor)
// factory.createInstance(null,
//// ahs.getURI(),
//// ahs.getSensorType());
// // ahs.setLocation ??
//
//// activityHubSensors.add(activityHubSensor);
// activityHubSensors.add(ahs);
// }
// // --> finished
// return activityHubSensors;
//
// } catch (Exception e) {
// LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors", new Object[] { "got exception",
// e.getMessage() }, e);
// return null;
// }
// } else {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors",
// new Object[] { "callstatus is not succeeded" }, null);
//
// // error details output
// List outputs = sr.getOutputs();
// if (outputs != null) {
// for ( Iterator iter = outputs.iterator(); iter.hasNext(); ) {
// ProcessOutput po = (ProcessOutput) iter.next();
// if (po!=null) {
// LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getControlledActivityHubSensors", new Object[] { "Error message: ",
// po.getParameterValue() }, null);
// }
// }
// }
// return null;
// }
// }
//
//
// /**
// * Get details for a specific ActivityHub sensor
// *
// * Get the ontological service request
// * Send service request to service bus
// * Process the response
// * Log errors if the call was not successful
// *
// * @return List with ? (Ontology Resource objects)
// */
// public static List getActivityHubSensorInfo(ActivityHubSensor ahs) {
//
// ServiceResponse sr = caller.call(getSensorInfoRequest(ahs));
//
// LogUtils.logDebug(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo",
// new Object[] { "Call status: ", sr.getCallStatus().name() }, null);
//
// if (sr.getCallStatus() == CallStatus.succeeded) {
// try {
// // Obtain both output lists (specified in request)
// List listLocation = sr.getOutput(OUTPUT_AHS_LOCATION, true);
// List listMeasurement = sr.getOutput(OUTPUT_AHS_MEASUREMENT, true);
//
// if ((listLocation == null || listLocation.size() == 0) &&
// (listMeasurement == null || listMeasurement.size() == 0)) {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo",
// new Object[] { "there are no details in the response" }, null);
// return null;
// }
// LogUtils.logDebug(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo",
// new Object[] { "there are " + listLocation.size() + " locations ",
// "and " + listMeasurement.size() + " measurements in the response!"}, null);
//
//
// // convert unparameterized list to List of Resources
// List<Resource> result = (List<Resource>)(List<?>) listLocation;
// // simply add second output list
// result.addAll((List<Resource>)(List<?>) listMeasurement);
//
//
// // Debug log of results
// Iterator it = result.iterator();
// while (it.hasNext()) {
// Resource res = (Resource) it.next();
// LogUtils.logDebug(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo",
// new Object[] { "resource URI: " + res.getURI(),
// "; resource Type: " + res.getType()}, null);
// }
//
// // --> finished
// return result;
//
// } catch (Exception e) {
// LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo", new Object[] { "got exception",
// e.getMessage() }, e);
// return null;
// }
// }
// else {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo",
// new Object[] { "callstatus is not succeeded" }, null);
// // error details output
// List outputs = sr.getOutputs();
// if (outputs != null) {
// for ( Iterator iter = outputs.iterator(); iter.hasNext(); ) {
// ProcessOutput po = (ProcessOutput) iter.next();
// if (po!=null) {
// LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getActivityHubSensorInfo", new Object[] { "Error message: ",
// po.getParameterValue() }, null);
// }
// }
// }
// return null;
// }
// }
//
//
//
// // *****************************************************************
// // local Methods
// // *****************************************************************
//
// /**
// * Fetch sensors from service bus and send to gui
// * Process result list of ActivityHubSensors from service response
// * Send just URI-Strings to client application to decouple from Ontology-stuff
// * Display sensor list in GUI
// */
// public void getSensors() {
// List ahs = getControlledActivityHubSensors();
// if (ahs != null && !ahs.isEmpty()) {
// String[] ahsNames = new String[ahs.size()];
// String[] ahsTypes = new String[ahs.size()];
// int i = 0;
// for ( Iterator it = ahs.iterator(); it.hasNext(); i++ ) {
// ActivityHubSensor singleAHS = (ActivityHubSensor) it.next();
//
// /*
// * Debug test:
// * singleAHS is of Type MotionSensor, ContactClosureSensor !!
// *
// * ahsNames[i] = (singleAHS).getURI();
// *
// http://ontology.universAAL.org/ActivityHubServer.owl#controlledActivityHubDevice1/1/1
// *
// http://ontology.universAAL.org/ActivityHubServer.owl#controlledActivityHubDevice0/0/2
// * ahsTypes[i] = (singleAHS).getType();
// * http://ontology.universAAL.org/ActivityHub.owl#MotionSensor
// * http://ontology.universAAL.org/ActivityHub.owl#ContactClosureSensor
//
// LogUtils.logInfo(Activator.mc, MyActivityHubServiceConsumer.class,
// "getSensors",
// new Object[] { "Sensortype: " + ahsTypes[i],
// " with URI: " + ahsNames[i],
// " class Name: " + singleAHS.getClass().getName(),
// " package: " + singleAHS.getClass().getPackage(),
// " class simpleName: " + singleAHS.getClass().getSimpleName()
// },
// null);
// */
//
//
// // is GUI active?
// if (this.ahc != null && singleAHS != null)
// // add sensors to client
// this.ahc.addActivityHubSensor(singleAHS.getURI(),singleAHS.getSensorType());
//
// else LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getSensors",
// new Object[] { "No GUI object found! Discard response!" }, null);
// }
//
// if (this.ahc != null)
// // now display sensors on GUI
// this.ahc.showSensorList();
// else LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getSensors",
// new Object[] { "No GUI object found! Discard response!" }, null);
// }
// else {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "constructor",
// new Object[] { "No ActivityHubSensors found!" }, null);
// if (this.ahc != null)
// this.ahc.addTextToLogArea("No ActivityHubSensors found!");
// }
// }
//
//
// /**
// * The parameters are just URI-Strings to keep this class stateless
// * A new ActivityHubSensor is created from the Ontology according to the URIs
// * from the client application (client doesn't store Ontology Resource
// objects)
// * Pass this sensor object in service request
// *
// * Process result list from service response
// * Send result text array to GUI
// *
// * @param sensorURI is the resourceURI
// * @param deviceType is the type-id for the ActivityHubSensor
// */
// public void getDeviceInfo(String sensorURI, int deviceType) {
// if ((sensorURI == null) || !(sensorURI instanceof String)) {
// if (this.ahc != null) this.ahc.showDeviceInfo(new String[] {"No sensor
// selected!"});
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getDeviceInfo",
// new Object[] { "No sensor selected!" }, null);
// return;
// }
//
// ActivityHubSensor ahs = (ActivityHubSensor) factory.createInstance(null,
// sensorURI, deviceType);
//
// /* Debug test
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getDeviceInfo",
// new Object[] { "SensorType: " + ahs.getType(),
// "ClassURI: " + ahs.getClassURI(),
// "OntClassInfo: " + ahs.getOntClassInfo()
// // All the same URIs; e.g.
// http://ontology.universAAL.org/ActivityHub.owl#ContactClosureSensor
// }, null);
// */
//
// List l = getActivityHubSensorInfo(ahs);
//
// if (l != null && !l.isEmpty()) {
// String[] result = new String[l.size()+1];
// result[0] = "SensorType: " + ahs.getType();
// int i=0;
// // fill result in a loop
// for ( Iterator it = l.iterator(); it.hasNext(); ) {
// Resource res = (Resource) it.next();
// result[++i] = res.getType() + ": " +res.getURI();
// }
// // is GUI active?
// if (this.ahc != null)
// // send result text array to GUI
// this.ahc.showDeviceInfo(result);
// else LogUtils.logError(Activator.mc, MyActivityHubServiceConsumer.class,
// "getDeviceInfo",
// new Object[] { "No GUI object found! Discard response!" }, null);
// }
// else {
// LogUtils.logWarn(Activator.mc, MyActivityHubServiceConsumer.class,
// "getDeviceInfo",
// new Object[] { "No sensor details found!" }, null);
// if (this.ahc != null) this.ahc.showDeviceInfo( new String[] { "No sensor
// details found!" } );
// }
// }
//
//
//
// // *****************************************************************
// // Services Requests
// // *****************************************************************
//
// /**
// * Create ontological service request
// * no input
// * output: resources that are controled by ActivityHub service
// */
// public static ServiceRequest getAllActivityHubSensorsRequest() {
//
// // create a ServiceRequest regarding ActivityHubDevices
// ServiceRequest getAllActivityHubSensors = new ServiceRequest(new
// ActivityHub(), null);
//
// getAllActivityHubSensors.addRequiredOutput(
// // this is OUR unique ID with which we can later retrieve the returned value
// OUTPUT_LIST_OF_AHS,
// // Specify the meaning of the required output
// // by pointing to the property in whose value you are interested
// // Because we haven't specified any filter before, this should result
// // in returning all values associated with the specified property
// new String[] { ActivityHub.PROP_CONTROLS });
//
// return getAllActivityHubSensors;
// }
//
//
// /**
// * Create ontological service request
// * input is one specific sensor object
// * 2 outputs: location and last sensor event
// */
// private static ServiceRequest getSensorInfoRequest(ActivityHubSensor ahs) {
//
// ServiceRequest getActivityHubSensorInfo = new ServiceRequest(new
// ActivityHub(), null);
//
// // try to call a service with just the resourceURI as input
//// getActivityHubSensorInfo.addValueFilter(new String[] {
// ActivityHub.PROP_CONTROLS },
//// sensorURI);
// // couldn't get this to work on the server side since sensorURI is a String
// // found no possibility to create/match a sensor object out of this
//
//
// // hint:
// // use addValueFilter for instances, values, objects, individuals
// // use addTypeFilter for types, classes
//
//
// // we are interested in only those realizations of 'ActivityHub'
// // that have control over the given sensor
// getActivityHubSensorInfo.addValueFilter(new String[] {
// ActivityHub.PROP_CONTROLS }, ahs);
//
// // 1. output
// getActivityHubSensorInfo.addRequiredOutput(
// // this is OUR unique ID with which we can later retrieve the returned value
// OUTPUT_AHS_LOCATION,
// // Specify the meaning of the required output
// // by pointing to the property in whose value you are interested
// new String[] { ActivityHub.PROP_CONTROLS,
// ActivityHubSensor.PROP_PHYSICAL_LOCATION });
//
// // 2. output
// getActivityHubSensorInfo.addRequiredOutput(
// // this is OUR unique ID with which we can later retrieve the returned value
// OUTPUT_AHS_MEASUREMENT,
// new String[] { ActivityHub.PROP_CONTROLS, ActivityHubSensor.PROP_LASTEVENT
// });
//
// return getActivityHubSensorInfo;
// }
//
//
//
// /**
// * close the GUI window, e.g. if the bundle is stopped; called by Activator
// */
// public void deleteGui() {
// if (this.ahc != null) {
// this.ahc.deleteGui();
// this.ahc = null;
// }
// }
//
// }
