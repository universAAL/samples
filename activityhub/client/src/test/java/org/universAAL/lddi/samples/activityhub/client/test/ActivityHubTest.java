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

//package org.universAAL.lddi.samples.activityhub.client.test;
//
//import java.util.Iterator;
//import java.util.List;
//
//import org.osgi.framework.Constants;
//import org.springframework.util.Assert;
//import org.universAAL.itests.IntegrationTest;
//import org.universAAL.lddi.samples.activityhub.client.Activator;
//import org.universAAL.lddi.samples.activityhub.client.MyActivityHubServiceConsumer;
//import org.universAAL.middleware.container.utils.LogUtils;
//import org.universAAL.ontology.activityhub.ActivityHubSensor;
//
///**
// * 
// * @author Thomas Fuxreiter (foex@gmx.at)
// */
//public class ActivityHubTest extends IntegrationTest {
//
//	public ActivityHubTest() {
//    	setBundleConfLocation("conf");
//	}
//
//
//    public void testComposite() {
//	logAllBundles();
//    }
// 
//	public void testActivityHubClient() throws Exception {
//		
//		/** Test1: are there any ActicityHub sensors available **/
//		
//		List controlledAHS = MyActivityHubServiceConsumer.getControlledActivityHubSensors();
//		Assert.isTrue(controlledAHS.size() > 0);
//		
//		if (controlledAHS != null && !controlledAHS.isEmpty()) {
//			int i = 0;
//			for ( Iterator it = controlledAHS.iterator(); it.hasNext(); i++ ) {
//				ActivityHubSensor singleAHS = (ActivityHubSensor) it.next();
////				logInfo("!!!!!!! Testing Sensor %s!!!!!!!", i);
////				String sensorUri = singleAHS.getURI();//,singleAHS.getSensorType()
//				
//				
//			    /** Test2: getSensorInfo for specific sensor; should end with success **/
//				
//			    logInfo("!!!!!!! Testing Sensor %s getInfo!!!!!!!", i);
//			    Assert.isTrue(MyActivityHubServiceConsumer.getActivityHubSensorInfo(singleAHS) != null);
//
//				// No methods to control devices implemented yet! So, no more tests.
//			}
//		}
//		
//		
//	}
//
//    /**
//     * Helper method for logging.
//     * 
//     * @param msg
//     */
//    protected void logInfo(String format, Object... args) {
//	StackTraceElement callingMethod = Thread.currentThread()
//		.getStackTrace()[2];
//	LogUtils.logInfo(Activator.mc, getClass(), callingMethod
//		.getMethodName(), new Object[] { formatMsg(format, args) },
//		null);
//    }
//
//    /**
//     * Helper method for logging.
//     * 
//     * @param msg
//     */
//    protected void logError(Throwable t, String format, Object... args) {
//	StackTraceElement callingMethod = Thread.currentThread()
//		.getStackTrace()[2];
//	LogUtils.logError(Activator.mc, getClass(), callingMethod
//		.getMethodName(), new Object[] { formatMsg(format, args) }, t);
//    }    
//   
//    /**
//     * Verifies that runtime platform has correctly started. It prints basic
//     * information about framework (vendor, version) and lists installed
//     * bundles.
//     * 
//     * @throws Exception
//     */
//    public void testOsgiPlatformStarts() throws Exception {
//	logInfo("FRAMEWORK_VENDOR %s", bundleContext
//		.getProperty(Constants.FRAMEWORK_VENDOR));
//	logInfo("FRAMEWORK_VERSION %s", bundleContext
//		.getProperty(Constants.FRAMEWORK_VERSION));
//	logInfo("FRAMEWORK_EXECUTIONENVIRONMENT %s", bundleContext
//		.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));
//
//	logInfo("!!!!!!! Listing bundles in integration test !!!!!!!");
//	for (int i = 0; i < bundleContext.getBundles().length; i++) {
//	    logInfo("name: " + bundleContext.getBundles()[i].getSymbolicName());
//	}
//	logInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//    }
//}
