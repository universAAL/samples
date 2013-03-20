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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.run.uAALBundleExtender;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.managers.api.AALSpaceManager;
import org.universAAL.middleware.managers.api.DeployManager;
import org.universAAL.middleware.managers.api.InstallationResults;
import org.universAAL.middleware.managers.api.UAPPPackage;

import org.universAAL.middleware.deploymaneger.uapp.model.AalUapp;
import org.universAAL.middleware.deploymaneger.uapp.model.ObjectFactory;
import org.universAAL.middleware.deploymaneger.uapp.model.Part;

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
    private AALSpaceManager aalSpaceManager;
    // JAXB
    private JAXBContext jc;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;

    public void start(BundleContext context) throws Exception {
	ModuleContext moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });

	Object refs = moduleContext.getContainer().fetchSharedObject(
		moduleContext,
		new Object[] { DeployManager.class.getName().toString() });
	Object refs1 = moduleContext.getContainer().fetchSharedObject(
		moduleContext,
		new Object[] { AALSpaceManager.class.getName().toString() });
	if (refs != null && refs1 != null) {
	    deployManager = (DeployManager) refs;
	    aalSpaceManager = (AALSpaceManager) refs1;
	    URI uAAPUri = null;
	    URI uAPPFolder = null;
	    try {

		uAPPFolder = new URI(
			"file",
			"///home/michele/Scrivania/HWOService.usrv_FILES/bin/HWOApp.uapp_FILES",
			null);
		uAAPUri = new URI(
			"file",
			"///home/michele/Scrivania/HWOService.usrv_FILES/bin/HWOApp.uapp_FILES/config/hwo.uapp.xml",
			null);

	    } catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    Map<PeerCard, Part> layout = new HashMap<PeerCard, Part>();
	    Map<String, PeerCard> peers = aalSpaceManager.getPeers();
	    try {
		jc = JAXBContext.newInstance(ObjectFactory.class);
		unmarshaller = jc.createUnmarshaller();
		marshaller = jc.createMarshaller();
	    } catch (JAXBException e) {
		System.out.println(e);
	    }

	    AalUapp uAAP = null;
	    try {
		uAAP = (AalUapp)unmarshaller.unmarshal(new File(uAAPUri));
	    } catch (JAXBException e) {
		System.out.println(e);
	    }
	    int i = 0;
	    if (uAAP != null ) {
		for (String peerKey : peers.keySet()) {
		    layout.put(peers.get(peerKey), uAAP.getApplicationPart()
			    .getPart().get(i));
		    i++;
		}

	    }

	    deployManager.requestToInstall(new UAPPPackage("111", "prova",
		    uAPPFolder, layout));
	}
    }

    public void stop(BundleContext context) throws Exception {
	// TODO Auto-generated method stub

    }

    public static void main(String[] args) throws JAXBException,
	    UnsupportedEncodingException, URISyntaxException,
	    MalformedURLException {
	File file = new File("file:///mw.broker.client-1.0.0-SNAPSHOT.jar");
    }

}
