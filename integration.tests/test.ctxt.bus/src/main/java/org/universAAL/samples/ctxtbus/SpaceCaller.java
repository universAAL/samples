/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion
	Avanzadas - Grupo Tecnologias para la Salud y el
	Bienestar (TSB)

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
package org.universAAL.samples.ctxtbus;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.device.GasSensor;
import org.universAAL.ontology.phThing.Device;
import org.universAAL.ontology.profile.AppSubProfile;
import org.universAAL.ontology.profile.AppService;
import org.universAAL.ontology.profile.AppServiceProfile;
import org.universAAL.ontology.profile.Space;
import org.universAAL.ontology.profile.SpaceProfile;
import org.universAAL.ontology.profile.HRSubProfile;
import org.universAAL.ontology.profile.HWSubProfile;
import org.universAAL.ontology.profile.OntologyEntry;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.support.utils.service.Arg;
import org.universAAL.support.utils.service.Path;
import org.universAAL.support.utils.service.low.Request;
import org.universAAL.support.utils.service.mid.UtilEditor;

public class SpaceCaller {
	public static final int _GET = 0x000;
	public static final int _ADD = 0x100;
	public static final int _CHANGE = 0x200;
	public static final int _REMOVE = 0x300;

	public static final int __SPACE = 0x000;
	public static final int __SPACEPROF = 0x010;
	public static final int __SERV = 0x020;
	public static final int __SERVPROF = 0x030;
	public static final int __DEV = 0x040;
	public static final int __ONT = 0x050;
	public static final int __HR = 0x060;
	public static final int __HW = 0x070;
	public static final int __APP = 0x080;

	public static final int ___SPEC = 0x000;
	public static final int ___ALL = 0x001;
	public static final int ___OFTOSPACE = 0x002;
	public static final int ___OFTOSERV = 0x003;

	private static final String PROFILE_CLIENT_NAMESPACE = "http://ontology.itaca.es/ProfileClient.owl#";
	private final static Logger log = LoggerFactory.getLogger(HistoryCaller.class);
	public static final String NONE = "none";
	private static final String OUTPUT = PROFILE_CLIENT_NAMESPACE + "outX";

	private ServiceCaller caller;

	public SpaceCaller(ModuleContext context) {
		caller = new DefaultServiceCaller(context);
	}

	public String callSpace(int selectedIndex, String arg1, String arg2) {
		log.info("Space Client: " + selectedIndex + "-> b:" + Integer.toBinaryString(selectedIndex));
		String ret = null;
		switch (selectedIndex) {
		case (_GET | __SPACE | ___SPEC):
			ret = getSpace(new Space(arg1));
			break;
		case (_ADD | __SPACE | ___SPEC):
			Space space = new Space(arg1);
			SpaceProfile spaceprof = new SpaceProfile(arg1 + "prof");
			space.setProfile(spaceprof);
			ret = addSpace(space);
			break;
		case (_CHANGE | __SPACE | ___SPEC):
			ret = changeSpace(new Space(arg1));
			break;
		case (_REMOVE | __SPACE | ___SPEC):
			ret = removeSpace(new Space(arg1));
			break;

		case (_GET | __SPACEPROF | ___SPEC):
			ret = getSpaceProfile(new SpaceProfile(arg1));
			break;
		case (_ADD | __SPACEPROF | ___SPEC):
			ret = addSpaceProfile(new SpaceProfile(arg1));
			break;
		case (_CHANGE | __SPACEPROF | ___SPEC):
			ret = changeSpaceProfile(new SpaceProfile(arg1));
			break;
		case (_REMOVE | __SPACEPROF | ___SPEC):
			ret = removeSpaceProfile(new SpaceProfile(arg1));
			break;

		case (_GET | __SERV | ___SPEC):
			ret = getService(new AppService(arg1));
			break;
		case (_ADD | __SERV | ___SPEC):
			AppService serv = new AppService(arg1);
			HRSubProfile hr = new HRSubProfile(arg1 + "hrsubprof");
			HWSubProfile hw = new HWSubProfile(arg1 + "hwsubprof");
			AppSubProfile app = new AppSubProfile(arg1 + "appsubprof");
			AppServiceProfile servprof = new AppServiceProfile(arg1 + "prof");
			servprof.setProperty(AppServiceProfile.PROP_HUMAN_RESOURCE_SUBPROFILE, hr);
			servprof.setProperty(AppServiceProfile.PROP_HARDWARE_SUBPROFILE, hw);
			servprof.setProperty(AppServiceProfile.PROP_APPLICATION_SUBPROFILE, app);
			serv.setProfile(servprof);
			ret = addService(serv);
			break;
		case (_CHANGE | __SERV | ___SPEC):
			ret = changeService(new AppService(arg1));
			break;
		case (_REMOVE | __SERV | ___SPEC):
			ret = removeService(new AppService(arg1));
			break;

		case (_GET | __SERVPROF | ___SPEC):
			ret = getServiceProf(new AppServiceProfile(arg1));
			break;
		case (_ADD | __SERVPROF | ___SPEC):
			ret = addServiceProf(new AppServiceProfile(arg1));
			break;
		case (_CHANGE | __SERVPROF | ___SPEC):
			ret = changeServiceProf(new AppServiceProfile(arg1));
			break;
		case (_REMOVE | __SERVPROF | ___SPEC):
			ret = removeServiceProf(new AppServiceProfile(arg1));
			break;

		case (_GET | __DEV | ___SPEC):
			ret = getDevice(new GasSensor(arg1));
			break;
		case (_ADD | __DEV | ___SPEC):
			ret = addDevice(new GasSensor(arg1));
			break;
		case (_CHANGE | __DEV | ___SPEC):
			ret = changeDevice(new GasSensor(arg1));
			break;
		case (_REMOVE | __DEV | ___SPEC):
			ret = removeDevice(new GasSensor(arg1));
			break;

		case (_GET | __ONT | ___SPEC):
			ret = getOnt(new OntologyEntry(arg1));
			break;
		case (_ADD | __ONT | ___SPEC):
			ret = addOnt(new OntologyEntry(arg1));
			break;
		case (_CHANGE | __ONT | ___SPEC):
			ret = changeOnt(new OntologyEntry(arg1));
			break;
		case (_REMOVE | __ONT | ___SPEC):
			ret = removeOnt(new OntologyEntry(arg1));
			break;

		case (_GET | __SPACE | ___ALL):
			ret = getSpaces();
			break;
		case (_GET | __SERV | ___ALL):
			ret = getServices();
			break;

		case (_GET | __SERV | ___OFTOSPACE):
			ret = getServicesOfSpace(new Space(arg1));
			break;
		case (_GET | __DEV | ___OFTOSPACE):
			ret = getDevicesOfSpace(new Space(arg1));
			break;
		case (_GET | __ONT | ___OFTOSPACE):
			ret = getOntsOfSpace(new Space(arg1));
			break;

		case (_GET | __HR | ___OFTOSERV):
			ret = getHROfServ(new AppService(arg1));
			break;
		case (_GET | __HW | ___OFTOSERV):
			ret = getHWOfServ(new AppService(arg1));
			break;
		case (_GET | __APP | ___OFTOSERV):
			ret = getAppOfServ(new AppService(arg1));
			break;

		case (_ADD | __SERV | ___OFTOSPACE):
			ret = addServicesToSpace(new Space(arg1), new AppService(arg2));
			break;
		case (_ADD | __DEV | ___OFTOSPACE):
			ret = addDevicesToSpace(new Space(arg1), new GasSensor(arg2));
			break;
		case (_ADD | __ONT | ___OFTOSPACE):
			ret = addOntsToSpace(new Space(arg1), new OntologyEntry(arg2));
			break;

		case (_ADD | __SPACEPROF | ___OFTOSPACE):
			ret = addSpaceprofToSpace(new Space(arg1), new SpaceProfile(arg1 + "prof"));
			break;
		case (_GET | __SPACEPROF | ___OFTOSPACE):
			ret = getSpaceprofOfSpace(new Space(arg1));
			break;
		case (_ADD | __SERVPROF | ___OFTOSERV):
			ret = addServprofToServ(new AppService(arg1), new AppServiceProfile(arg1 + "prof"));
			break;
		case (_GET | __SERVPROF | ___OFTOSERV):
			ret = getServprofOfServ(new AppService(arg1));
			break;

		default:
			return NONE;
		}

		return ret;
	}

	// :::::::::::::SPACE GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getSpace(Space space) {
		return genericGet(space, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String addSpace(Space space) {
		return genericAdd(space, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String changeSpace(Space space) {
		return genericChange(space, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String removeSpace(Space space) {
		return genericRemove(space, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	// :::::::::::::SPACEPROF GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getSpaceProfile(SpaceProfile aalSpaceProfile) {
		return genericGet(aalSpaceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String addSpaceProfile(SpaceProfile aalSpaceProfile) {
		return genericAdd(aalSpaceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String changeSpaceProfile(SpaceProfile aalSpaceProfile) {
		return genericChange(aalSpaceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String removeSpaceProfile(SpaceProfile aalSpaceProfile) {
		return genericRemove(aalSpaceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	// :::::::::::::SERVICE GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getService(AppService aalService) {
		return genericGet(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String addService(AppService aalService) {
		return genericAdd(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String changeService(AppService aalService) {
		return genericChange(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	private String removeService(AppService aalService) {
		return genericRemove(aalService, Path.at(ProfilingService.PROP_CONTROLS).path);
	}

	// :::::::::::::SERVICEPROF GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getServiceProf(AppServiceProfile aalServiceProfile) {
		return genericGet(aalServiceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String addServiceProf(AppServiceProfile aalServiceProfile) {
		return genericAdd(aalServiceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String changeServiceProf(AppServiceProfile aalServiceProfile) {
		return genericChange(aalServiceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	private String removeServiceProf(AppServiceProfile aalServiceProfile) {
		return genericRemove(aalServiceProfile,
				Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path);
	}

	// :::::::::::::DEVICE GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getDevice(Device device) {
		return genericGet(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}

	private String addDevice(Device device) {
		return genericAdd(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}

	private String changeDevice(Device device) {
		return genericChange(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}

	private String removeDevice(Device device) {
		return genericRemove(device, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_HARDWARE).path);
	}

	// :::::::::::::ONT GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String getOnt(OntologyEntry ontologyEntry) {
		return genericGet(ontologyEntry, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES).path);
	}

	private String addOnt(OntologyEntry ontologyEntry) {
		return genericAdd(ontologyEntry, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES).path);
	}

	private String changeOnt(OntologyEntry ontologyEntry) {
		return genericChange(ontologyEntry, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES).path);
	}

	private String removeOnt(OntologyEntry ontologyEntry) {
		return genericRemove(ontologyEntry, Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES).path);
	}

	// :::::::::::::GENERIC GET/ADD/CHANGE/REMOVE:::::::::::::::::

	private String genericGet(Resource res, String[] path) {
		ServiceResponse resp = caller
				.call(UtilEditor.requestGet(ProfilingService.MY_URI, path, Arg.in(res), Arg.out(OUTPUT)));

		return getListOfResults(resp);
	}

	private String genericAdd(Resource res, String[] path) {
		ServiceResponse resp = caller.call(UtilEditor.requestAdd(ProfilingService.MY_URI, path, Arg.add(res)));
		return resp.getCallStatus().name();
	}

	private String genericChange(Resource res, String[] path) {
		ServiceResponse resp = caller.call(UtilEditor.requestChange(ProfilingService.MY_URI, path, Arg.change(res)));
		return resp.getCallStatus().name();
	}

	private String genericRemove(Resource res, String[] path) {
		ServiceResponse resp = caller.call(UtilEditor.requestRemove(ProfilingService.MY_URI, path, Arg.remove(res)));
		return resp.getCallStatus().name();
	}

	// :::::::::::::OTHER GETS & ADDS:::::::::::::::::

	private String getSpaces() {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.out(OUTPUT));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI)); // This
																						// only
																						// works
																						// if
																						// type
																						// is
																						// set
																						// as
																						// instance
																						// restriction
																						// in
																						// serv
		// ServiceRequest req=new ServiceRequest(new
		// ProfilingService(null),null);
		// ProcessOutput out=new ProcessOutput(OUTPUT);
		// out.setParameterType(AALSpace.MY_URI);
		// req.addSimpleOutputBinding(out,
		// Path.at(ProfilingService.PROP_CONTROLS).path);
		// req.addTypeFilter(Path.at(ProfilingService.PROP_CONTROLS).path,
		// AALSpace.MY_URI);
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getServices() {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.out(OUTPUT));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AppService.MY_URI)); // This
																						// only
																						// works
																						// if
																						// type
																						// set
																						// as
																						// instance
																						// restriction
																						// in
																						// serv
		// ServiceRequest req=new ServiceRequest(new
		// ProfilingService(null),null);
		// ProcessOutput out=new ProcessOutput(OUTPUT);
		// out.setParameterType(AALService.MY_URI);
		// req.addSimpleOutputBinding(out,
		// Path.at(ProfilingService.PROP_CONTROLS).path);
		// req.addTypeFilter(Path.at(ProfilingService.PROP_CONTROLS).path,
		// AALService.MY_URI);
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getServicesOfSpace(Space aalSpace) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_SERVICES), Arg.out(OUTPUT));
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getDevicesOfSpace(Space aalSpace) {
		// Request req=new Request(new ProfilingService(null));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS),
		// Arg.type(AALSpace.MY_URI));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE),
		// Arg.out(OUTPUT));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(AALSpaceProfile.PROP_INSTALLED_HARDWARE),
		// Arg.type(GasSensor.MY_URI));
		ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
		req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS }, aalSpace);
		// req.addTypeFilter(new String[]{ProfilingService.PROP_CONTROLS},
		// AALSpace.MY_URI);//removed to try bug 298
		req.addRequiredOutput(OUTPUT, new String[] { ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE,
				SpaceProfile.PROP_INSTALLED_HARDWARE });
		req.addTypeFilter(new String[] { ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE,
				SpaceProfile.PROP_INSTALLED_HARDWARE }, Device.MY_URI);

		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getOntsOfSpace(Space aalSpace) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES), Arg.out(OUTPUT));
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getHROfServ(AppService aalService) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalService));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AppService.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(AppServiceProfile.PROP_HUMAN_RESOURCE_SUBPROFILE), Arg.out(OUTPUT));
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getHWOfServ(AppService aalService) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalService));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AppService.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(AppServiceProfile.PROP_HARDWARE_SUBPROFILE), Arg.out(OUTPUT));
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String getAppOfServ(AppService aalService) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalService));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AppService.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(AppServiceProfile.PROP_APPLICATION_SUBPROFILE), Arg.out(OUTPUT));
		ServiceResponse resp = caller.call(req);
		return getListOfResults(resp);
	}

	private String addOntsToSpace(Space aalSpace, OntologyEntry ont) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_ONTOLOGIES), Arg.add(ont));
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	private String addDevicesToSpace(Space aalSpace, Device dev) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_HARDWARE), Arg.add(dev));
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	private String addServicesToSpace(Space aalSpace, AppService serv) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE)
				.to(SpaceProfile.PROP_INSTALLED_SERVICES), Arg.add(serv));
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	private String getServprofOfServ(AppService aalService) {
		// Request req=new Request(new ProfilingService(null));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalService));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS),
		// Arg.type(AALService.MY_URI));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE),
		// Arg.out(OUTPUT));
		// ServiceResponse resp=caller.call(req);
		// return getListOfResults(resp);

		ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
		req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS }, aalService);
		req.addRequiredOutput(OUTPUT, new String[] { ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE });
		ServiceResponse resp = caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
			// Object out=getReturnValue(resp.getOutputs(),OUTPUT);
			Object out = resp.getOutput(OUTPUT, true);
			if (out != null) {
				log.debug(out.toString());
				return out.toString();
			} else {
				log.debug("NOTHING!");
				return "nothing";
			}
		} else {
			return resp.getCallStatus().name();
		}
	}

	private String addServprofToServ(AppService aalService, AppServiceProfile aalServiceProfile) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalService));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(AppService.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.add(aalServiceProfile));
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	private String getSpaceprofOfSpace(Space aalSpace) {
		// Request req=new Request(new ProfilingService(null));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS),
		// Arg.type(AALSpace.MY_URI));
		// req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE),
		// Arg.out(OUTPUT));
		// ServiceResponse resp=caller.call(req);
		// return getListOfResults(resp);

		ServiceRequest req = new ServiceRequest(new ProfilingService(), null);
		req.addValueFilter(new String[] { ProfilingService.PROP_CONTROLS }, aalSpace);
		req.addRequiredOutput(OUTPUT, new String[] { ProfilingService.PROP_CONTROLS, Profilable.PROP_HAS_PROFILE });
		ServiceResponse resp = caller.call(req);
		if (resp.getCallStatus() == CallStatus.succeeded) {
			// Object out=getReturnValue(resp.getOutputs(),OUTPUT);
			Object out = resp.getOutput(OUTPUT, true);
			if (out != null) {
				log.debug(out.toString());
				return out.toString();
			} else {
				log.debug("NOTHING!");
				return "nothing";
			}
		} else {
			return resp.getCallStatus().name();
		}
	}

	private String addSpaceprofToSpace(Space aalSpace, SpaceProfile aalSpaceProfile) {
		Request req = new Request(new ProfilingService(null));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(aalSpace));
		req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.type(Space.MY_URI));
		req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.add(aalSpaceProfile));
		ServiceResponse resp = caller.call(req);
		return resp.getCallStatus().name();
	}

	// :::::::::::::UTILITIES:::::::::::::::::

	private static String getListOfResults(ServiceResponse resp) {
		if (resp.getCallStatus() == CallStatus.succeeded) {
			List out = resp.getOutput(OUTPUT, true);
			// Object out=getReturnValue(resp.getOutputs(),OUTPUT);
			if (out != null) {
				String s = "";
				for (Object o : out)
					s += o.toString() + "\r\n";
				log.debug(s);
				return s;
			} else {
				log.debug("NOTHING!");
				return "nothing";
			}
		} else {
			return resp.getCallStatus().name();
		}
	}

	// private static Object getReturnValue(List outputs, String expectedOutput)
	// {
	// Object returnValue = null;
	// if (outputs == null)
	// log.info("Profile Client: No results found!");
	// else
	// for (Iterator i = outputs.iterator(); i.hasNext();) {
	// ProcessOutput output = (ProcessOutput) i.next();
	// if (output.getURI().equals(expectedOutput))
	// if (returnValue == null)
	// returnValue = output.getParameterValue();
	// else
	// log.info("Profile Client: redundant return value!");
	// else
	// log.info("Profile Client - output ignored: "
	// + output.getURI());
	// }
	//
	// return returnValue;
	// }
}
