/*
    Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
    Fraunhofer-Gesellschaft - Institute for Computer Graphics Research

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.ontology.reasoner.util.ElementModel;
import org.universAAL.samples.context.reasoner.client.osgi.Activator;
import org.universAAL.samples.context.reasoner.client.osgi.UAALInterfaceActivator;

/**
 * 
 * This class records all events at the context-bus by registering itself with a
 * generic pattern (subject need to be a resource, what is true anyway). Since
 * URI's are heavily need to be used for creation of Situations and Queries it
 * can be helpful to show a user a selection of all URI's that up to now has
 * passed the system. All results will be saved and therefore are available
 * again after restarting the system.
 * 
 * @author amarinc
 * 
 */
@SuppressWarnings("unchecked")
public class ContextEventRecorder extends ContextSubscriber {

    private static ContextEventPattern[] createInitialSubscription() {
	ContextEventPattern contextEventPattern = new ContextEventPattern();
	contextEventPattern.addRestriction(MergedRestriction
		.getAllValuesRestriction(ContextEvent.PROP_RDF_SUBJECT,
			ManagedIndividual.MY_URI));
	return new ContextEventPattern[] { contextEventPattern };
    }

    private static final String URI_FILENAME = "ContextRecorderURIs.txt";
    private static final String TYPE_URI_FILENAME = "ContextRecorderTypeURIs.txt";
    private static final String PREDICATE_FILENAME = "PredicateURIs.txt";

    private ElementModel<ContextEvent> recordedEvents = new ElementModel(
	    ContextEvent.class, UAALInterfaceActivator.serializer, Activator.dataHome.getAbsolutePath());
    /**
     * List of recorded instance URI's
     */
    private ArrayList<String> recordedURIs = new ArrayList<String>();
    /**
     * List of recorded type URI's
     */
    private ArrayList<String> recordedTypeURIs = new ArrayList<String>();
    /**
     * List of recorded possibilities for URI's of properties
     */
    private ArrayList<String> recordedPredicates = new ArrayList<String>();

    ContextEventRecorder(ModuleContext context) {
	super(context, createInitialSubscription());
	Activator.dataHome.mkdirs();
	recordedEvents.loadElements();
    }

    @Override
    public void communicationChannelBroken() {
    }

    @Override
    public void handleContextEvent(ContextEvent event) {
	this.recordedEvents.add(event);

	if (!recordedURIs.contains(event.getSubjectURI()))
	    recordedURIs.add(event.getSubjectURI());
	if (!recordedTypeURIs.contains(event.getSubjectTypeURI()))
	    recordedTypeURIs.add(event.getSubjectTypeURI());

	if (event.getRDFPredicate() != null) {
	    if (!this.recordedPredicates.contains(event.getRDFPredicate()))
		this.recordedPredicates.add(event.getRDFPredicate());
	    if (event.getRDFObject() instanceof ManagedIndividual) {
		String uri = ((ManagedIndividual) event.getRDFObject())
			.getURI();
		if (!recordedURIs.contains(uri))
		    recordedURIs.add(uri);
		uri = ((ManagedIndividual) event.getRDFObject()).getType();
		if (!recordedTypeURIs.contains(uri))
		    recordedTypeURIs.add(uri);
	    } else {
		String type = TypeMapper.getDatatypeURI(event.getRDFObject());
		if (!recordedTypeURIs.contains(type))
		    recordedTypeURIs.add(type);
	    }
	}
    }

    public ArrayList<ContextEvent> getRecordedEvents() {
	return this.recordedEvents.getElements();
    }

    public ArrayList<String> getRecordedURIs() {
	return (ArrayList<String>) recordedURIs.clone();
    }

    public ArrayList<String> getRecordedTypeURIs() {
	return (ArrayList<String>) recordedTypeURIs.clone();
    }

    public ArrayList<String> getRecordedPredicates() {
	return (ArrayList<String>) recordedPredicates.clone();
    }

    public ArrayList<String> getAllTypeAndInstanceURIs() {
	ArrayList<String> result = (ArrayList<String>) recordedURIs.clone();
	result.addAll(recordedTypeURIs);
	return result;
    }

    public ArrayList<String> getAllURIs() {
	ArrayList<String> result = getAllTypeAndInstanceURIs();
	result.addAll(recordedPredicates);
	return result;
    }

    public boolean saveData() {
	return (saveArray(ContextEventRecorder.URI_FILENAME, this.recordedURIs)
		&& saveArray(ContextEventRecorder.TYPE_URI_FILENAME,
			this.recordedTypeURIs)
		&& saveArray(ContextEventRecorder.PREDICATE_FILENAME,
			this.recordedPredicates) && recordedEvents
		    .saveElements());
    }

    public void loadData() {
	this.recordedURIs = loadArray(ContextEventRecorder.URI_FILENAME);
	this.recordedTypeURIs = loadArray(ContextEventRecorder.TYPE_URI_FILENAME);
	this.recordedPredicates = loadArray(ContextEventRecorder.PREDICATE_FILENAME);
	this.recordedEvents.loadElements();
    }

    /**
     * Saves all elements in the given list at the given filename.
     * 
     * @param <O>
     *            Type to be saved. ToString() is used to serialize the object.
     * @param filename
     *            Filename relative to the clients home
     * @param list
     *            List to be saved
     * @return True if the save process has been successful, false otherwise
     */
    private <O extends Object> boolean saveArray(String filename,
	    ArrayList<O> list) {
	try {
	    File file = new File(Activator.dataHome, filename);
	    if (!file.exists() && !file.createNewFile())
		return false;

	    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
	    for (O object : list)
		bw.write(object.toString()
			+ System.getProperty("line.separator"));
	    bw.close();

	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * Load the given file from the root of the client and returns an array with
     * lines from it.
     * 
     * @param filename
     *            Filename relative to the clients home
     * @return List of Strings according to the lines of the input-file or an
     *         empty list in case of any trouble
     */
    private ArrayList<String> loadArray(String filename) {
	ArrayList<String> result = new ArrayList<String>();
	try {
	    File file = new File(Activator.dataHome, filename);
	    if (!file.exists())
		return result;

	    BufferedReader br = new BufferedReader(new FileReader(file));
	    String line = null;
	    while ((line = br.readLine()) != null)
		result.add(line);

	    br.close();

	    return result;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return result;
    }
}
