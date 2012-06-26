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
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.profile.AssistedPerson;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.Profile;
import org.universAAL.ontology.profile.SubProfile;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;
import org.universAAL.ontology.profile.health.HealthProfile;
import org.universAAL.ontology.profile.service.ProfilingService;
import org.universAAL.samples.service.utils.Arg;
import org.universAAL.samples.service.utils.Path;
import org.universAAL.samples.service.utils.low.SimpleRequest;
import org.universAAL.samples.service.utils.mid.SimpleEditor;
import org.universaal.ontology.useridprofileontology.owl.UserIDProfile;

public class ProfileCaller {
    private static final String PROFILE_CLIENT_NAMESPACE = "http://ontology.itaca.es/ProfileClient.owl#";
    private final static Logger log = LoggerFactory
	    .getLogger(HistoryCaller.class);
    private static final String OUTPUT_GETPROFILABLE = PROFILE_CLIENT_NAMESPACE
	    + "out1";
    private static final String OUTPUT_GETPROFILE = PROFILE_CLIENT_NAMESPACE
	    + "out2";
    private static final String OUTPUT_GETUSERS = PROFILE_CLIENT_NAMESPACE
	    + "out3";
    private static final String OUTPUT_GETSUBPROFILES = PROFILE_CLIENT_NAMESPACE
	    + "out4";
    private static final String OUTPUT_GETSUBPROFILE = PROFILE_CLIENT_NAMESPACE
	    + "out5";

    private ServiceCaller caller;

    public ProfileCaller(ModuleContext context) {
	caller = new DefaultServiceCaller(context);
    }

    private Object getReturnValue(List outputs, String expectedOutput) {
	Object returnValue = null;
	if (outputs == null)
	    log.info("Profile Client: No results found!");
	else
	    for (Iterator i = outputs.iterator(); i.hasNext();) {
		ProcessOutput output = (ProcessOutput) i.next();
		if (output.getURI().equals(expectedOutput))
		    if (returnValue == null)
			returnValue = output.getParameterValue();
		    else
			log.info("Profile Client: redundant return value!");
		else
		    log.info("Profile Client - output ignored: "
			    + output.getURI());
	    }

	return returnValue;
    }

    public boolean checkPWD(String user, String PWD) {
//	User userreceived=null;
//	UserProfile profilereceived=null;
//	UserIDProfile idreceived=null;
//	ServiceResponse resp = caller.call(SimpleEditor.requestGet(
//		ProfilingService.MY_URI,
//		Path.at(ProfilingService.PROP_CONTROLS).path,
//		Arg.in(new User(user)), Arg.out(OUTPUT_GETPROFILABLE)));
//	if (resp.getCallStatus() == CallStatus.succeeded) {
//	     userreceived = (User)getReturnValue(resp.getOutputs(), OUTPUT_GETPROFILABLE);
//	}
//	resp = caller.call(SimpleEditor.requestGet(
//		ProfilingService.MY_URI,
//		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
//		Arg.in(new UserProfile(userreceived.getUserProfile().getURI())), Arg.out(OUTPUT_GETPROFILE)));
//	if (resp.getCallStatus() == CallStatus.succeeded) {
//	    profilereceived=(UserProfile)getReturnValue(resp.getOutputs(),OUTPUT_GETPROFILE);
//	}
//	resp = caller.call(SimpleEditor.requestGet(
//		ProfilingService.MY_URI,
//		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
//		Arg.in(new UserIDProfile(profilereceived.getSubProfile().getURI())), Arg.out(OUTPUT_GETPROFILE)));
//	if (resp.getCallStatus() == CallStatus.succeeded) {
//	    idreceived=(UserIDProfile)getReturnValue(resp.getOutputs(),OUTPUT_GETPROFILE);
//	}
//	String storedpwd=idreceived.getPASSWORD();
//	return storedpwd.equals(PWD);
	return false;
    }

    public String callProfile(int selectedIndex, String arg1, String arg2) {
	switch (selectedIndex) {
	case 0:
	    return getProfilable(new AssistedPerson(arg1));
	case 1:
	    return addProfilable(new AssistedPerson(arg1));
	case 2:
	    return changeProfilable(new AssistedPerson(arg1));
	case 3:
	    return removeProfilable(new AssistedPerson(arg1));
	    
	case 4:
	    return getProfile(new UserProfile(arg1));
	case 5:
	    return addProfile(new UserProfile(arg1));
	case 6:
	    return changeProfile(new UserProfile(arg1));
	case 7:
	    return removeProfile(new UserProfile(arg1));
	    
	case 8:
	    return getSubProfile(new HealthProfile(arg1));
	case 9:
	    return addSubProfile(new HealthProfile(arg1));
	case 10:
	    return changeSubProfile(new HealthProfile(arg1));
	case 11:
	    return removeSubProfile(new HealthProfile(arg1));
	case 12:
	    return getUsers();
	case 13:
	    return getProfile(new User(arg1));
	case 14:
	    return getSubprofiles(new User(arg1));
	case 15:
	    return getSubprofiles(new UserProfile(arg1));
	case 16:
	     return addProfile(new User(arg1),new UserProfile(arg2));
	case 17:
	     return addSubprofile(new User(arg1),new HealthProfile(arg2));
	case 18:
	     return addSubprofile(new UserProfile(arg1),new HealthProfile(arg2));
//	case 17:
//	    return changeProfile(new User(arg1),new UserProfile(arg2));
	default:
	    break;
	}
	return null;
    }

    //:::::::::::::PROFILABLE GET/ADD/CHANGE/REMOVE:::::::::::::::::
    private String removeProfilable(Resource profilable) {
	log.info("Profile Client: RemoveProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestRemove(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).path,
		Arg.remove(profilable)));
	return resp.getCallStatus().name();
    }

    private String changeProfilable(Resource profilable) {
	log.info("Profile Client: changeProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestChange(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).path,
		Arg.change(profilable)));
	return resp.getCallStatus().name();
    }

    private String addProfilable(Resource profilable) {
	log.info("Profile Client: addProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestAdd(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).path,
		Arg.add(profilable)));
	return resp.getCallStatus().name();
    }

    private String getProfilable(Resource assistedPerson) {
	log.info("Profile Client: callGetProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestGet(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).path,
		Arg.in(assistedPerson), Arg.out(OUTPUT_GETPROFILABLE)));
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETPROFILABLE);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }
    
    //:::::::::::::PROFILE GET/ADD/CHANGE/REMOVE:::::::::::::::::
    private String removeProfile(UserProfile profile) {
	log.info("Profile Client: RemoveProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestRemove(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
		Arg.remove(profile)));
	return resp.getCallStatus().name();
    }

    private String changeProfile(UserProfile profile) {
	log.info("Profile Client: changeProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestChange(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
		Arg.change(profile)));
	return resp.getCallStatus().name();
    }

    private String addProfile(UserProfile profile) {
	log.info("Profile Client: addProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestAdd(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
		Arg.add(profile)));
	return resp.getCallStatus().name();
    }

    private String getProfile(UserProfile profile) {
	log.info("Profile Client: callGetProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestGet(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).path,
		Arg.in(profile), Arg.out(OUTPUT_GETPROFILE)));
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETPROFILE);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }
    
    //:::::::::::::SUBPROFILE GET/ADD/CHANGE/REMOVE:::::::::::::::::
    private String removeSubProfile(SubProfile profile) {
	log.info("Profile Client: RemoveProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestRemove(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE).path,
		Arg.remove(profile)));
	return resp.getCallStatus().name();
    }

    private String changeSubProfile(SubProfile profile) {
	log.info("Profile Client: changeProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestChange(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE).path,
		Arg.change(profile)));
	return resp.getCallStatus().name();
    }

    private String addSubProfile(SubProfile profile) {
	log.info("Profile Client: addProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestAdd(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE).path,
		Arg.add(profile)));
	return resp.getCallStatus().name();
    }

    private String getSubProfile(SubProfile profile) {
	log.info("Profile Client: callGetProfilable");
	ServiceResponse resp = caller.call(SimpleEditor.requestGet(
		ProfilingService.MY_URI,
		Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE).path,
		Arg.in(profile), Arg.out(OUTPUT_GETSUBPROFILE)));
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILE);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }
    
    //:::::::::::::OTHER GETS:::::::::::::::::
    private String getUsers() {
	log.info("Profile Client: getUsers");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.out(OUTPUT_GETUSERS));
	ServiceResponse resp=caller.call(req);
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETUSERS);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }
    
    private String getProfile(User profilable) {
 	log.info("Profile Client: getProfile1");
 	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
 	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(profilable));
 	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.out(OUTPUT_GETPROFILE));
 	ServiceResponse resp=caller.call(req);
 	if (resp.getCallStatus() == CallStatus.succeeded) {
 	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETPROFILE);
 	    if (out != null) {
 		log.debug(out.toString());
 		return out.toString();
 	    } else {
 		log.debug("NOTHING!");
 		return "nothing";
 	    }
 	}else{
 	    return resp.getCallStatus().name();
 	}
    }
    
    private String getSubprofiles(User user) {
	log.info("Profile Client: getSubprofiles1");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(user));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE), Arg.type(HealthProfile.MY_URI));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE), Arg.out(OUTPUT_GETSUBPROFILES));
	ServiceResponse resp=caller.call(req);
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILES);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }
    
    private String getSubprofiles(UserProfile profile) {
	log.info("Profile Client: getSubprofiles2");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.in(profile));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE), Arg.out(OUTPUT_GETSUBPROFILES));
	ServiceResponse resp=caller.call(req);
	if (resp.getCallStatus() == CallStatus.succeeded) {
	    Object out=getReturnValue(resp.getOutputs(),OUTPUT_GETSUBPROFILES);
	    if (out != null) {
		log.debug(out.toString());
		return out.toString();
	    } else {
		log.debug("NOTHING!");
		return "nothing";
	    }
	}else{
	    return resp.getCallStatus().name();
	}
    }

    //:::::::::::::OTHER ADDS:::::::::::::::::
    private String addProfile(User profilable, UserProfile profile) {
	log.info("Profile Client: addProfile");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(profilable));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.add(profile));
	ServiceResponse resp=caller.call(req);
	return resp.getCallStatus().name();
    }

    private String addSubprofile(User profilable, SubProfile subProfile) {
	log.info("Profile Client: addProfile2");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS), Arg.in(profilable));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE), Arg.add(subProfile));
	ServiceResponse resp=caller.call(req);
	return resp.getCallStatus().name();
    }

    private String addSubprofile(UserProfile userProfile, SubProfile subProfile) {
	log.info("Profile Client: addProfile3");
	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.in(userProfile));
	req.put(Path.at(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE).to(Profile.PROP_HAS_SUB_PROFILE), Arg.add(subProfile));
	ServiceResponse resp=caller.call(req);
	return resp.getCallStatus().name();
    }

//  private String changeProfile(User profilable, UserProfile profile) {
//	log.info("Profile Client: changeProfile");
//	SimpleRequest req=new SimpleRequest(new ProfilingService(null));
//	req.put(Path.start(ProfilingService.PROP_CONTROLS), Arg.in(profilable));
//	req.put(Path.start(ProfilingService.PROP_CONTROLS).to(Profilable.PROP_HAS_PROFILE), Arg.change(profile));
//	ServiceResponse resp=caller.call(req);
//	return resp.getCallStatus().name();
//  }
}
