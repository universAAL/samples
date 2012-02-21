package org.universAAL.samples.testlocation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextPublisher;
import org.universAAL.middleware.context.DefaultContextPublisher;
import org.universAAL.middleware.context.owl.ContextProvider;
import org.universAAL.middleware.context.owl.ContextProviderType;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
/** 
 *  @author Miguel Angel Llorente-Carmona
 *  @author Angel Martinez-Cavero
 *  @author alfiva
 *  
 *  @version 1.0
 * 
 * */

public class Activator implements BundleActivator {

	private final static Logger log=LoggerFactory.getLogger(Activator.class);
//	protected ContextPublisher cp;
	private GUIPanel panel;

	public void start(BundleContext context) throws Exception {
//		ContextProvider cpinfo = new ContextProvider("http://ontology.tsbtecnologias.es/Test.owl#TestContextProvider");
//		cpinfo.setType(ContextProviderType.gauge);
//		cp = new DefaultContextPublisher(context, cpinfo);
//		
//		User user=new User("http://ontology.tsbtecnologias.es/Test.owl#bilbo");
//		Location loc=new Location("http://ontology.tsbtecnologias.es/Test.owl#livingRoom","LivingRoom");
//		user.setLocation(loc);
//
//		ContextEvent ev1 = new ContextEvent(user, User.PROP_PHYSICAL_LOCATION);
//
//		cp.publish(ev1);
//		cp.publish(ev1);
//		cp.publish(ev1);
		panel=new GUIPanel(context);
		panel.setVisible(true);
	}

	public void stop(BundleContext arg0) throws Exception {
//		cp.close();
		panel.dispose();
	}

}

