package de.fzi.ipe.uapp.infoframe;

import java.util.Locale;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;


/**
 * ServiceTracker class for infoframe.
 * Allows to lead config, register resources by web server and start RSS feed generator
 */
public class HttpServiceTracker extends ServiceTracker {
//	
//	
    public HttpServiceTracker(BundleContext context) {
        super(context, HttpService.class.getName(), null);
      }
    
    @Override
	public Object addingService(ServiceReference reference) {
		
        HttpService httpService = (HttpService) context.getService(reference);
        try {
        	
        	//Load config
        	Config config = new Config();
        	
        	//Set locale
        	Locale.setDefault(new Locale(config.getLanguage()));
        	
        	//Create and start RSS feed in a new thread
        	ResourceServlet servlet = new ResourceServlet();
        	httpService.registerServlet("/modifiedInfoframe", servlet, null, null);
        	Thread t = new Thread( new InfoFrameRSS(config));
        	t.start();
//        	
        	//Debug only
//        	InfoFrameServlet IFServlet = new InfoFrameServlet(config);
//        	httpService.registerServlet("/modifiedInfoframeDebug", IFServlet, null, null);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return httpService;
    }
    
    @Override
	public void removedService(ServiceReference reference, Object service) {    	
        HttpService httpService = (HttpService) service;
        httpService.unregister("/modifiedInfoframe");
        super.removedService(reference, service);
    }

}

