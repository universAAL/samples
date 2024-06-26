package org.universAAL.samples.lighting.server.test;

import org.universAAL.itests.IntegrationTest;

/**
 * Here developer's of this artifact should code their integration tests.
 *
 * @author rotgier
 *
 */
public class ArtifactIT extends IntegrationTest {

	public ArtifactIT() {
		setRunArguments("net.slp.multicastTimeouts", "500,750");
		setRunArguments("java.net.preferIPv4Stack", "true");
		setRunArguments("net.slp.port", "7000");
	}

	public void testComposite() {
		logAllBundles();
	}

}
