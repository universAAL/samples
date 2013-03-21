/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License .
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.helper.location.publisher;

import java.awt.EventQueue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * @author eandgrg
 * 
 */
public class Activator implements BundleActivator {
    /**
     * {@link ModuleContext}
     */
    private static ModuleContext mcontext;
    LocationContextPublisher lp = null;
    LocationPublisherGUI frame = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext bcontext) throws Exception {

	Activator.mcontext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { bcontext });

	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    LocationContextPublisher lp = new LocationContextPublisher(
			    mcontext);
		    LocationPublisherGUI frame = new LocationPublisherGUI(lp);
		    frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});

	LogUtils
		.logInfo(
			mcontext,
			this.getClass(),
			"start",
			new Object[] { "smp.location.publisher.gui bundle has started." },
			null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
	LogUtils
		.logInfo(
			mcontext,
			this.getClass(),
			"stop",
			new Object[] { "smp.location.publisher.gui bundle has stopped." },
			null);
	mcontext = null;
	lp = null;
	frame = null;

    }

}
