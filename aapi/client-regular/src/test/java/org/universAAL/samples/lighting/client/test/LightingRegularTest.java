package org.universAAL.samples.lighting.client.test;

import java.util.List;

import org.osgi.framework.Constants;
import org.springframework.util.Assert;
import org.universAAL.itests.IntegrationTest;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.location.Location;
import org.universAAL.samples.lighting.client_regular.Activator;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel1;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel2;
import org.universAAL.samples.lighting.client_regular.LightingConsumerLevel3;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel1;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel2;
import org.universAAL.samples.lighting.server_regular.LightingProviderLevel3;

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
public class LightingRegularTest extends IntegrationTest {

    /**
     * Constructor of each integration TestCase has to call constructor of upper
     * class providing path to the launch configuration and path to the
     * configuration directory of the uAAL runtime. Launch configuration will be
     * used to setup uAAL runtime for the purpose of TestCase. All bundles
     * needed for the TestCase have to be included in the launch configuration.
     */
    public LightingRegularTest() {
	// super("../../pom/launches/LightingExample_Complete_0_3_2.launch");
	// setBundleConfLocation("../../../itests/rundir/confadmin");
    }

    /**
     * Helper method for logging.
     * 
     * @param msg
     */
    protected void logInfo(String format, Object... args) {
	StackTraceElement callingMethod = Thread.currentThread()
		.getStackTrace()[2];
	LogUtils.logInfo(Activator.mc, getClass(), callingMethod
		.getMethodName(), new Object[] { formatMsg(format, args) },
		null);
    }

    /**
     * Helper method for logging.
     * 
     * @param msg
     */
    protected void logError(Throwable t, String format, Object... args) {
	StackTraceElement callingMethod = Thread.currentThread()
		.getStackTrace()[2];
	LogUtils.logError(Activator.mc, getClass(), callingMethod
		.getMethodName(), new Object[] { formatMsg(format, args) }, t);
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
	logInfo("FRAMEWORK_VENDOR %s", bundleContext
		.getProperty(Constants.FRAMEWORK_VENDOR));
	logInfo("FRAMEWORK_VERSION %s", bundleContext
		.getProperty(Constants.FRAMEWORK_VERSION));
	logInfo("FRAMEWORK_EXECUTIONENVIRONMENT %s", bundleContext
		.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT));

	logInfo("!!!!!!! Listing bundles in integration test !!!!!!!");
	for (int i = 0; i < bundleContext.getBundles().length; i++) {
	    logInfo("name: " + bundleContext.getBundles()[i].getSymbolicName());
	}
	logInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void testLightingClientLevel3() throws Exception {
	LightingProviderLevel3 provider = new LightingProviderLevel3(
		org.universAAL.samples.lighting.server_regular.Activator.mc);

	try {
	    List<Integer> controlledLamps = LightingConsumerLevel3
		    .getControlledLamps();
	    Assert.isTrue(controlledLamps.size() == 4);

	    int i = 0;
	    for (int lampID : controlledLamps) {
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
	} finally {
	    provider.close();
	}
    }

    public void testLightingClientLevel2() throws Exception {
	LightingProviderLevel2 provider = new LightingProviderLevel2(
		org.universAAL.samples.lighting.server_regular.Activator.mc);

	try {
	    List<LightSource> controlledLamps = LightingConsumerLevel2
		    .getControlledLamps();
	    Assert.isTrue(controlledLamps.size() == 4);

	    int i = 0;
	    for (LightSource lamp : controlledLamps) {
		logInfo("!!!!!!! Testing Lamp %s!!!!!!!", i);
		String lampUri = lamp.getURI();

		Object[] lampInfo = LightingConsumerLevel2.getLampInfo(lamp);

		int brightness = (Integer) lampInfo[0];
		Location location = (Location) lampInfo[1];

		Assert.isTrue(brightness == 0);
		String uri = location.getURI();
		Assert.isTrue(location.getURI().equals(
			"urn:aal_space:myHome#loc" + (i + 1)));

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
	} finally {
	    provider.close();
	}
    }

    /**
     * Verifies the lighting sample with the use of LightingConsumer. Following
     * operations are tested: getControlledLamps, turnOn, turnOff, dimToValue.
     * 
     * @throws Exception
     */
    public void testLightingClientLevel1() throws Exception {
	LightingProviderLevel1 provider = new LightingProviderLevel1(
		org.universAAL.samples.lighting.server_regular.Activator.mc);

	try {
	    logInfo("!!!!!!! Testing Lighting Client !!!!!!!");
	    logInfo("!!!!!!! Getting controlled lamps and checking their amount !!!!!!!");

	    // LightingConsumer
	    // .turnOn("http://ontology.igd.fhg.de/LightingServer.owl#controlledLamp0");

	    /* There should be four lamps available. */
	    List<LightSource> controlledLamps = LightingConsumerLevel1
		    .getControlledLamps();
	    Assert.isTrue(controlledLamps.size() == 4);

	    int i = 0;
	    for (LightSource lamp : controlledLamps) {
		logInfo("!!!!!!! Testing Lamp %s!!!!!!!", i);
		String lampUri = lamp.getURI();

		Object[] lampInfo = LightingConsumerLevel1.getLampInfo(lampUri);

		int brightness = (Integer) lampInfo[0];
		Location location = (Location) lampInfo[1];

		Assert.isTrue(brightness == 0);
		String uri = location.getURI();
		Assert.isTrue(location.getURI().equals(
			"urn:aal_space:myHome#loc" + (i + 1)));

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
	} finally {
	    provider.close();
	}
    }

}
