/*
        Copyright 2007-2014 CNR-ISTI, http://isti.cnr.it
        Institute of Information Science and Technologies
        of the Italian National Research Council

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
package org.universAAL.middleware.managers.deploy.client.osgi;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.managers.api.SpaceManager;
import org.universAAL.middleware.managers.api.DeployManager;
import org.universAAL.middleware.managers.api.InstallationResults;
import org.universAAL.middleware.managers.api.InstallationResultsDetails;
import org.universAAL.middleware.managers.api.MatchingResult;
import org.universAAL.middleware.managers.api.UAPPPackage;

import org.universAAL.middleware.deploymanager.uapp.model.AalUapp;
import org.universAAL.middleware.deploymanager.uapp.model.ObjectFactory;
import org.universAAL.middleware.deploymanager.uapp.model.Part;

/**
 * Activator for the Deploymanager client
 *
 * @author <a href="mailto:michele.girolami@isti.cnr.it">Michele Girolami</a>
 * @author <a href="mailto:francesco.furfari@isti.cnr.it">Francesco Furfari</a>
 * @author <a href="mailto:stefano.lenzi@isti.cnr.it">Stefano Lenzi</a>
 *
 * @version $LastChangedRevision$ ( $LastChangedDate$ )
 */

public class Activator implements BundleActivator {
	private DeployManager deployManager;
	private SpaceManager aalSpaceManager;
	// JAXB
	private JAXBContext jc;
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;

	private static String UAAP_FOLDER = "/home/pulz/distro/example-A/packager";
	private static String UAAP_URI = "/home/pulz/distro/example-A/packager/config/Single Part App.uapp.xml";

	private final static String USRV_ID = "111";
	private final static String UAAP_ID = "prova";

	private final static Object[] DEPLOY_MANAGER_FILTER = new Object[] { DeployManager.class.getName().toString() };
	private final static Object[] AAL_SPACE_FILTER = new Object[] { SpaceManager.class.getName().toString() };

	private Object getObject(ModuleContext mc, Object[] filter) {
		Object sr = null;
		sr = mc.getContainer().fetchSharedObject(mc, filter);
		return sr;
	}

	private void initClient(ModuleContext mc) {
		Object dm = getObject(mc, DEPLOY_MANAGER_FILTER);
		Object aalSpace = getObject(mc, AAL_SPACE_FILTER);
		if (dm != null && aalSpace != null) {
			deployManager = (DeployManager) dm;
			aalSpaceManager = (SpaceManager) aalSpace;
		} else {
			throw new IllegalStateException("Either Deploy Manger or AAL Space Mangar shared object are not available");
		}
	}

	public void start(BundleContext context) throws Exception {
		ModuleContext mc = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { context });

		initClient(mc);
		URI uAAPUri = null;
		URI uAPPFolder = null;
		String x = context.getProperty("uAAL.deploymanager.test.folder");
		if (x != null) {
			UAAP_FOLDER = x;
		}
		context.getProperty("uAAL.deploymanager.test.uri");
		if (x != null) {
			UAAP_URI = x;
		}
		try {
			LogUtils.logInfo(mc, Activator.class, "start", "Using upacked folder" + UAAP_FOLDER);
			uAPPFolder = new URI("file", UAAP_FOLDER, null);
			LogUtils.logInfo(mc, Activator.class, "start", "uApp XML file" + UAAP_URI);
			uAAPUri = new URI("file", UAAP_URI, null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<PeerCard, List<Part>> layout = new HashMap<PeerCard, List<Part>>();
		ArrayList<PeerCard> peerList = new ArrayList<PeerCard>(aalSpaceManager.getPeers().values());
		peerList.add(aalSpaceManager.getMyPeerCard());
		PeerCard[] peers = peerList.toArray(new PeerCard[] {});
		HashMap<String, Serializable> peersFilter = new HashMap<String, Serializable>();
		peersFilter.put("karaf.version", null);
		MatchingResult matchingPeers = aalSpaceManager.getMatchingPeers(peersFilter);
		peers = matchingPeers.getPeers();

		UAPPPackage pkg = new UAPPPackage(USRV_ID, UAAP_ID, uAPPFolder, layout);
		InstallationResultsDetails result = deployManager.requestToInstall(pkg);
		if (result.getGlobalResult() != InstallationResults.SUCCESS) {
			throw new IllegalStateException("Failed to installe the uAAP with " + result);
		}
	}

	public void stop(BundleContext context) throws Exception {
		deployManager.requestToUninstall(USRV_ID, UAAP_ID);
	}

}
