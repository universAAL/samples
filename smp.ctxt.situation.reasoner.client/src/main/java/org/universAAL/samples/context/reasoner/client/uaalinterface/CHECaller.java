/*	
	Copyright 2008-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer Gesellschaft - Institut f�r Graphische Datenverarbeitung 
	
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
package org.universAAL.samples.context.reasoner.client.uaalinterface;

import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.container.utils.StringUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.che.ContextHistoryService;
import org.universAAL.samples.context.reasoner.client.osgi.Activator;
import org.universAAL.samples.context.reasoner.client.osgi.UAALInterfaceActivator;

/**
 * This class is just a shortened version of the CHE-Caller in the
 * ctxt.situation.reasoner project. Here all methods remains that are needed to
 * perform a SPARQL query at the CHE. This is here only used to offer the
 * functionality of a written query. For further information please have a look
 * at the other project
 * 
 * @author alfiva
 * @author amarinc
 * 
 */
public class CHECaller extends ServiceCaller {
    private static final String HISTORY_CLIENT_NAMESPACE = "http://ontology.itaca.es/Reasoner.owl#";
    private static final String OUTPUT_RESULT_STRING = HISTORY_CLIENT_NAMESPACE
	    + "resultString";
    private static final String GENERIC_EVENT = "urn:org.universAAL.middleware.context.rdf:ContextEvent#_:0000000000000000:00";

    public CHECaller(ModuleContext context) {
	super(context);
    }

    public void communicationChannelBroken() {
    }

    public void handleResponse(String reqID, ServiceResponse response) {
    }

    /**
     * Execute a SPARQL CONSTRUCT query on the CHE that will return a reasoned
     * event. That event will be published.
     * 
     * @param theQuery
     *            The SPARQL CONSTRUCT query
     */
    public ContextEvent executeQuery(String theQuery) {
	String query = theQuery.replace(
		GENERIC_EVENT,
		ContextEvent.CONTEXT_EVENT_URI_PREFIX
			+ StringUtils.createUniqueID());
	String ser = callDoSPARQL(query);
	if (!ser.isEmpty()) {
	    ContextEvent event = (ContextEvent) UAALInterfaceActivator.serializer
		    .deserialize(ser);
	    event.setTimestamp(new Long(System.currentTimeMillis()));
	    return event;
	}
	return null;
    }

    /**
     * Call the CHE service
     * 
     * @param query
     *            The CONSTRUCT query
     * @return Serialized event constructed
     */
    public String callDoSPARQL(String query) {
	ServiceResponse response = this.call(getDoSPARQLRequest(query));
	if (response.getCallStatus() == CallStatus.succeeded) {
	    try {
		String results = (String) getReturnValue(response.getOutputs(),
			OUTPUT_RESULT_STRING);
		return results;
	    } catch (Exception e) {
		LogUtils.logInfo(Activator.context, CHECaller.class,
			"callDoSPARQL",
			new Object[] { "History Client: Result corrupt!" }, e);
		return "";
	    }
	} else
	    LogUtils.logInfo(
		    Activator.context,
		    CHECaller.class,
		    "callDoSPARQL",
		    new Object[] { "History Client - status of doSparqlQuery(): "
			    + response.getCallStatus() }, null);
	return "";
    }

    /**
     * Prepare the call for CHE
     * 
     * @param query
     *            The CONSTRUCT query
     * @return The request for the call
     */
    private ServiceRequest getDoSPARQLRequest(String query) {
	ServiceRequest getQuery = new ServiceRequest(new ContextHistoryService(
		null), null);

	MergedRestriction r = MergedRestriction.getFixedValueRestriction(
		ContextHistoryService.PROP_PROCESSES, query);

	getQuery.getRequestedService().addInstanceLevelRestriction(r,
		new String[] { ContextHistoryService.PROP_PROCESSES });
	getQuery.addSimpleOutputBinding(
		new ProcessOutput(OUTPUT_RESULT_STRING), new PropertyPath(null,
			true,
			new String[] { ContextHistoryService.PROP_RETURNS })
			.getThePath());
	return getQuery;
    }

    /**
     * Process service call response
     * 
     * @param outputs
     *            The outputs of the response
     * @param expectedOutput
     *            The URI of the desired output
     * @return The desired output value
     */
    @SuppressWarnings("unchecked")
    private Object getReturnValue(List outputs, String expectedOutput) {
	Object returnValue = null;
	if (outputs == null)
	    LogUtils.logInfo(Activator.context, CHECaller.class,
		    "getReturnValue",
		    new Object[] { "History Client: No events found!" }, null);
	else
	    for (Iterator i = outputs.iterator(); i.hasNext();) {
		ProcessOutput output = (ProcessOutput) i.next();
		if (output.getURI().equals(expectedOutput))
		    if (returnValue == null)
			returnValue = output.getParameterValue();
		    else
			LogUtils.logInfo(
				Activator.context,
				CHECaller.class,
				"getReturnValue",
				new Object[] { "History Client: redundant return value!" },
				null);
		else
		    LogUtils.logInfo(Activator.context, CHECaller.class,
			    "getReturnValue",
			    new Object[] { "History Client - output ignored: "
				    + output.getURI() }, null);
	    }

	return returnValue;
    }
}
