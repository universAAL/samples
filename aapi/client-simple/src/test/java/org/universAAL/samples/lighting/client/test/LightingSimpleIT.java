/*
Copyright 2011-2014 AGH-UST, http://www.agh.edu.pl
Faculty of Computer Science, Electronics and Telecommunications
Department of Computer Science

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
package org.universAAL.samples.lighting.client.test;

import java.util.List;

import org.osgi.framework.Constants;
import org.springframework.util.Assert;
import org.universAAL.itests.IntegrationTest;
import org.universAAL.middleware.api.SimpleServiceLocator;
import org.universAAL.middleware.api.SimpleServiceRegistrator;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel1;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel2;
import org.universAAL.ontology.lighting.simple.LightingInterfaceLevel3;
import org.universAAL.ontology.location.Location;
import org.universAAL.samples.lighting.client_regular.Activator;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel1;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel2;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel3;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel1;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel2;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel3;
import org.universAAL.samples.lighting.server_simple.LightingSimplifiedServiceLevel1;
import org.universAAL.samples.lighting.server_simple.LightingSimplifiedServiceLevel2;
import org.universAAL.samples.lighting.server_simple.LightingSimplifiedServiceLevel3;

/**
 * LightingTest is an example of JUnit OSGi integration test. The TestCase uses
 * LightingConsumer to verify if the lighting sample works correctly. Each
 * integration TestCase has to extend MiddlewareIntegrationTest provided in the
 * itests maven artifact. Thanks to using JUnit the TestCase will be executed
 * during each maven build. However please mind that the tests are by default
 * disabled in the main middleware pom file (mw.pom). To enable them an argument
 * "-DskipTests=false" has to be added to the "mvn" invocation in the command
 * line.
 *
 * @author rotgier
 *
 */
public class LightingSimpleIT extends IntegrationTest {

	private SimpleServiceRegistrator ssr;

	protected void onSetUp() {
		ssr = new SimpleServiceRegistrator(Activator.mc);
	}

	/**
	 * Constructor of each integration TestCase has to call constructor of upper
	 * class providing path to the launch configuration and path to the
	 * configuration directory of the uAAL runtime. Launch configuration will be
	 * used to setup uAAL runtime for the purpose of TestCase. All bundles
	 * needed for the TestCase have to be included in the launch configuration.
	 */
	public LightingSimpleIT() {
		// super("../../pom/launches/LightingExample_Complete_0_3_2.launch");
		// setBundleConfLocation("../../../itests/rundir/confadmin");
	}

	/**
	 * Helper method for logging.
	 *
	 * @param msg
	 */
	protected void logInfo(String format, Object... args) {
		StackTraceElement callingMethod = Thread.currentThread().getStackTrace()[2];
		LogUtils.logInfo(Activator.mc, getClass(), callingMethod.getMethodName(),
				new Object[] { formatMsg(format, args) }, null);
	}

	/**
	 * Helper method for logging.
	 *
	 * @param msg
	 */
	protected void logError(Throwable t, String format, Object... args) {
		StackTraceElement callingMethod = Thread.currentThread().getStackTrace()[2];
		LogUtils.logError(Activator.mc, getClass(), callingMethod.getMethodName(),
				new Object[] { formatMsg(format, args) }, t);
	}

	public void testComposite() {
		logAllBundles();
	}

	/**
	 * Verifies that runtime platform has correctly started. It prints basic
	 * information about framework (vendor, version) and lists installed
	 * bundles.
	 *
	 * @throws Exception
	 */
	public void testOsgiPlatformStarts() throws Exception {
		logInfo("FRAMEWORK_VENDOR %s", bundleContext.getProperty(Constants.FRAMEWORK_VENDOR));
		logInfo("FRAMEWORK_VERSION %s", bundleContext.getProperty(Constants.FRAMEWORK_VERSION));
		logInfo("FRAMEWORK_EXECUTIONENVIRONMENT %s",
				bundleContext.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));

		logInfo("!!!!!!! Listing bundles in integration test !!!!!!!");
		for (int i = 0; i < bundleContext.getBundles().length; i++) {
			logInfo("name: " + bundleContext.getBundles()[i].getSymbolicName());
		}
		logInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	private void clientRegular_Level1() {
		List<LightSource> controlledLamps = LightingConsumerLevel1.getControlledLamps();
		Assert.isTrue(controlledLamps.size() == 4, "Lamps size: " + controlledLamps.size() + " is not equal to 4!");

		int i = 0;
		for (LightSource lamp : controlledLamps) {
			logInfo("!!!!!!! Testing Lamp %s!!!!!!!", i);
			String lampUri = lamp.getURI();

			Object[] lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);

			int brightness = (Integer) lampInfo[0];
			Location location = (Location) lampInfo[1];

			Assert.isTrue(brightness == 0);
			String uri = location.getURI();
			Assert.isTrue(location.getURI().equals("urn:aal_space:myHome#loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.turnOn(lampUri));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.turnOn(lampUri));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.turnOff(lampUri));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.turnOff(lampUri));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(0));

			/* dimToValue with argument other than 0, 100 should fail */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 45!!!!!!!", i);
			Assert.isTrue(!LightingConsumerLevel1.dimToValue(lampUri, 45));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(0));

			/* dimToValue with argument 100 should end with success */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 100!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.dimToValue(lampUri, 100));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(100));

			/* dimToValue with argument 0 should end with success */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 0!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel1.dimToValue(lampUri, 0));

			lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	private void clientRegular_Level2() {
		List<LightSource> controlledLamps = LightingConsumerLevel2.getControlledLamps();
		Assert.isTrue(controlledLamps.size() == 4, "Lamps size: " + controlledLamps.size() + " is not equal to 4!");

		int i = 0;
		for (LightSource lamp : controlledLamps) {
			logInfo("!!!!!!! Testing Lamp %s!!!!!!!", i);

			Object[] lampInfo = LightingConsumerLevel2.getLampInfo(lamp);

			int brightness = (Integer) lampInfo[0];
			Location location = (Location) lampInfo[1];

			Assert.isTrue(brightness == 0);
			String uri = location.getURI();
			Assert.isTrue(location.getURI().equals("urn:aal_space:myHome#loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.turnOn(lamp));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.turnOn(lamp));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.turnOff(lamp));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.turnOff(lamp));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* dimToValue with argument other than 0, 100 should fail */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 45!!!!!!!", i);
			Assert.isTrue(!LightingConsumerLevel2.dimToValue(lamp, 45));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* dimToValue with argument 100 should end with success */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 100!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.dimToValue(lamp, 100));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* dimToValue with argument 0 should end with success */
			logInfo("!!!!!!! Testing Lamp %s dimToValue 0!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel2.dimToValue(lamp, 0));

			lampInfo = LightingConsumerLevel2.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	private void clientRegular_Level3() {
		List<Integer> controlledLamps = LightingConsumerLevel3.getControlledLamps();
		Assert.isTrue(controlledLamps.size() == 4, "Lamps size: " + controlledLamps.size() + " is not equal to 4!");

		int i = 0;
		for (Integer lampID : controlledLamps) {
			logInfo("!!!!!!! Testing Lamp %s!!!!!!!", i);

			Object[] lampInfo = LightingConsumerLevel3.getLampInfo(lampID);

			int brightness = (Integer) lampInfo[0];
			String location = (String) lampInfo[1];

			Assert.isTrue(brightness == 0);
			Assert.isTrue(location.equals("loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel3.turnOn(lampID));

			lampInfo = LightingConsumerLevel3.getLampInfo(lampID);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel3.turnOn(lampID));

			lampInfo = LightingConsumerLevel3.getLampInfo(lampID);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel3.turnOff(lampID));

			lampInfo = LightingConsumerLevel3.getLampInfo(lampID);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			Assert.isTrue(LightingConsumerLevel3.turnOff(lampID));

			lampInfo = LightingConsumerLevel3.getLampInfo(lampID);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	private void clientSimple_Level1() throws Exception {
		LightingInterfaceLevel1 service = (LightingInterfaceLevel1) new SimpleServiceLocator(Activator.mc)
				.lookupService(LightingInterfaceLevel1.class);

		/* There should be four lamps available. */
		LightSource[] controlledLamps = service.getControlledLamps();
		Assert.isTrue(controlledLamps.length == 4);

		int i = 0;
		for (LightSource lamp : controlledLamps) {
			Object[] lampInfo = service.getLampInfo(lamp);
			int brightness = (Integer) lampInfo[0];
			Location location = (Location) lampInfo[1];

			Assert.isTrue(brightness == 0);
			String uri = location.getURI();
			Assert.isTrue(location.getURI().equals("urn:aal_space:myHome#loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	private void clientSimple_Level2() throws Exception {
		LightingInterfaceLevel2 service = (LightingInterfaceLevel2) new SimpleServiceLocator(Activator.mc)
				.lookupService(LightingInterfaceLevel2.class);
		LightSource[] controlledLamps = service.getControlledLamps();
		Assert.isTrue(controlledLamps.length == 4);
		int i = 0;
		for (LightSource lamp : controlledLamps) {
			Object[] lampInfo = service.getLampInfo(lamp);
			int brightness = (Integer) lampInfo[0];
			Location location = (Location) lampInfo[1];

			Assert.isTrue(brightness == 0);
			String uri = location.getURI();
			Assert.isTrue(location.getURI().equals("urn:aal_space:myHome#loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	private void clientSimple_Level3() throws Exception {
		LightingInterfaceLevel3 service = (LightingInterfaceLevel3) new SimpleServiceLocator(Activator.mc)
				.lookupService(LightingInterfaceLevel3.class);
		Integer[] controlledLamps = service.getControlledLamps();
		Assert.isTrue(controlledLamps.length == 4);
		int i = 0;
		for (int lamp : controlledLamps) {
			Object[] lampInfo = service.getLampInfo(lamp);
			int brightness = (Integer) lampInfo[0];
			String location = (String) lampInfo[1];

			Assert.isTrue(brightness == 0);
			Assert.isTrue(location.equals("loc" + (i + 1)));

			/* turnOn should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* when repeated turnOn should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOn!!!!!!!", i);
			service.turnOn(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(100));

			/* turnOff should end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			/* when repeated turnOff should also end with success */
			logInfo("!!!!!!! Testing Lamp %s turnOff!!!!!!!", i);
			service.turnOff(lamp);

			lampInfo = service.getLampInfo(lamp);
			Assert.isTrue(lampInfo[0].equals(0));

			i++;
		}
	}

	/**
	 * Cross verification of Simplified API vs. Regular API on Level1. Tested
	 * combinations: - regular server vs. regular client - regular server vs.
	 * simplified client - simplified server vs. regular client - simplified
	 * server vs. simplified client
	 */
	public void test_Level1() throws Exception {
		// Testing regular provider
		LightingProviderLevel1 providerRegular = new LightingProviderLevel1(
				org.universAAL.samples.lighting.server_regular.Activator.mc);
		try {
			clientRegular_Level1();
			clientSimple_Level1();
		} finally {
			providerRegular.close();
		}

		// Testing simplified provider
		LightingSimplifiedServiceLevel1 providerSimple = new LightingSimplifiedServiceLevel1();
		ssr.registerService(providerSimple);
		try {
			clientRegular_Level1();
			clientSimple_Level1();
		} finally {
			ssr.unregisterAll();
		}
	}

	/**
	 * Cross verification of Simplified API vs. Regular API on Level2. Tested
	 * combinations: - regular server vs. regular client - regular server vs.
	 * simplified client - simplified server vs. regular client - simplified
	 * server vs. simplified client
	 */
	public void test_Level2() throws Exception {
		// Testing regular provider
		LightingProviderLevel2 providerRegular = new LightingProviderLevel2(
				org.universAAL.samples.lighting.server_regular.Activator.mc);
		try {
			clientRegular_Level2();
			clientSimple_Level2();
		} finally {
			providerRegular.close();
		}

		// Testing simplified provider
		LightingSimplifiedServiceLevel2 providerSimple = new LightingSimplifiedServiceLevel2();
		ssr.registerService(providerSimple);
		try {
			clientRegular_Level2();
			clientSimple_Level2();
		} finally {
			ssr.unregisterAll();
		}
	}

	/**
	 * Cross verification of Simplified API vs. Regular API on Level3. Tested
	 * combinations: - regular server vs. regular client - regular server vs.
	 * simplified client - simplified server vs. regular client - simplified
	 * server vs. simplified client
	 */
	public void test_Level3() throws Exception {
		// Testing regular provider
		LightingProviderLevel3 providerRegular = new LightingProviderLevel3(
				org.universAAL.samples.lighting.server_regular.Activator.mc);
		try {
			clientRegular_Level3();
			clientSimple_Level3();
		} finally {
			providerRegular.close();
		}

		// Testing simplified provider
		LightingSimplifiedServiceLevel3 providerSimple = new LightingSimplifiedServiceLevel3();
		ssr.registerService(providerSimple);
		try {
			clientRegular_Level3();
			clientSimple_Level3();
		} finally {
			ssr.unregisterAll();
		}
	}

}
