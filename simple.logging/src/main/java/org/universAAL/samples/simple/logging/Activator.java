/*******************************************************************************
 * Copyright 2013 Universidad Polit�cnica de Madrid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.samples.simple.logging;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    final String[] prefixes = new String[] { "org.jgroups", "org.universAAL",
            "org.universAAL.mw", "org.universAAL.ui", "ch.ethz.iks.slp", "org.unhandled.logger" };
    final Logger[] logger = new Logger[prefixes.length];
    private Thread thread;

    public void start(BundleContext bc) throws Exception {
        for (int i = 0; i < prefixes.length; i++) {
            logger[i] = LoggerFactory.getLogger(prefixes[i] + ".LogMe");
        }
        thread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    for (int i = 0; i < prefixes.length; i++) {
                        try {
                            logger[i].error("LOGGIN AS ERROR");
                            Thread.sleep(300);
                            logger[i].info("LOGGIN AS INFO");
                            Thread.sleep(300);
                            logger[i].warn("LOGGIN AS WARNING");
                            Thread.sleep(300);
                            logger[i].debug("LOGGIN AS DEBUG");
                            Thread.sleep(300);
                            logger[i].trace("LOGGIN AS TRACING");
                            Thread.sleep(300);
                        } catch (Exception ex) {
                        }
                        ;
                    }
                }
            }
        }, "SimpleLogThread");
        thread.start();
    }

    public void stop(BundleContext bc) throws Exception {
        thread.stop();
    }

}
