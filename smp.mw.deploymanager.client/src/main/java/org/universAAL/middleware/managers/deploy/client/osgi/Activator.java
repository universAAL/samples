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

    private final static String UAAP_FOLDER = "///C:/Users/root/Programmi/uAAL-coordinator/distro/HWOService.usrv_FILES/bin/HWOApp.uapp_FILES";
    private final static String UAAP_URI ="///C:/Users/root/Programmi/uAAL-coordinator/distro/HWOService.usrv_FILES/bin/HWOApp.uapp_FILES/config/hwo.uapp.xml";

    private final static String USRV_ID = "111";
    private final static String UAAP_ID = "prova";

    private final static Object[] DEPLOY_MANAGER_FILTER = new Object[]{ DeployManager.class.getName().toString() };
    private final static Object[] AAL_SPACE_FILTER = new Object[]{ AALSpaceManager.class.getName().toString() };

    private Object getObject(ModuleContext mc, Object[] filter) {
        Object sr = null;
        sr = mc.getContainer().fetchSharedObject(mc, filter );
        return sr;
    }

    private void initClient(ModuleContext mc) {
        Object dm = getObject(mc, DEPLOY_MANAGER_FILTER );
        Object aalSpace = getObject( mc, AAL_SPACE_FILTER );
        if (dm != null && aalSpace != null) {
            deployManager = (DeployManager) dm;
            aalSpaceManager = (AALSpaceManager) aalSpace;
        } else {
            throw new IllegalStateException("Either Deploy Manger or AAL Space Mangar shared object are not available");
        }
    }

    public void start(BundleContext context) throws Exception {
        ModuleContext mc = uAALBundleContainer.THE_CONTAINER
                .registerModule(new Object[] { context });

        initClient(mc);
        URI uAAPUri = null;
        URI uAPPFolder = null;
        try {
            uAPPFolder = new URI( "file", UAAP_FOLDER, null);
            uAAPUri = new URI( "file", UAAP_URI, null);
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
            System.out.println("Issue with XML parsing: "+e.getErrorCode() );
            e.getLinkedException().printStackTrace();
            e.printStackTrace();
        }
        int i = 0;
        if (uAAP == null ) {
            throw new IllegalStateException("Unable to parse or find the uAAP xml");
        }
        for (String peerKey : peers.keySet()) {
            if ( i >=  uAAP.getApplicationPart().getPart().size() ) {
                System.out.println("All the parts of the application have been assigned to all the peers");
                break;
            } else {
                System.out.println("Assigned "+uAAP.getApplicationPart().getPart().get(i).getPartId()+" to peer "+peerKey);
                layout.put(peers.get(peerKey), uAAP.getApplicationPart().getPart().get(i));
            }
            i++;
        }
        UAPPPackage pkg = new UAPPPackage(USRV_ID, UAAP_ID, uAPPFolder, layout);
        InstallationResults result = deployManager.requestToInstall( pkg );
        if ( result != InstallationResults.SUCCESS ) {
            throw new IllegalStateException("Failed to installe the uAAP with "+result);
        }
    }

    public void stop(BundleContext context) throws Exception {
        URI uAAPUri = null;
        URI uAPPFolder = null;
        try {
            uAPPFolder = new URI( "file", UAAP_FOLDER, null);
            uAAPUri = new URI( "file", UAAP_URI, null);
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
            System.out.println("Issue with XML parsing: "+e.getErrorCode() );
            e.getLinkedException().printStackTrace();
            e.printStackTrace();
        }
        int i = 0;
        if (uAAP != null ) {
            for (String peerKey : peers.keySet()) {
                if ( i >=  uAAP.getApplicationPart().getPart().size() ) {
                    System.out.println("All the parts of the application have been assigned to all the peers");
                    break;
                } else {
                    System.out.println("Assigned "+uAAP.getApplicationPart().getPart().get(i).getPartId()+" to peer "+peerKey);
                    layout.put(peers.get(peerKey), uAAP.getApplicationPart().getPart().get(i));
                }
                i++;
            }
        }

        deployManager.requestToUninstall(USRV_ID, UAAP_ID);
    }

}
