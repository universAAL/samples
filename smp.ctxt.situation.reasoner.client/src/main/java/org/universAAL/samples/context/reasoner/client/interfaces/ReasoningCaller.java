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
package org.universAAL.samples.context.reasoner.client.interfaces;

import java.util.List;
import java.util.Random;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.DefaultServiceCaller;
import org.universAAL.middleware.service.ServiceCaller;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.ontology.reasoner.Persistent;
import org.universAAL.ontology.reasoner.Query;
import org.universAAL.ontology.reasoner.ReasoningService;
import org.universAAL.ontology.reasoner.Rule;
import org.universAAL.ontology.reasoner.Situation;
import org.universAAL.samples.context.reasoner.client.osgi.Activator;

/**
 * This class is used to add/remove/get Situations/Queries/Rules to/from the
 * Reasoner. Since all of the three operations are very similar for the three
 * used types of objects a lot of methods can be reused using generics.
 *
 * @author amarinc
 *
 */
public class ReasoningCaller {

	public static final String REASONER_CLIENT_NAMESPACE = "http://ontology.igd.fhg.de/ReasonerClient.owl#";

	private static final String OUTPUT_SITUATIONS = REASONER_CLIENT_NAMESPACE + "currentSituations";
	private static final String OUTPUT_QUERIES = REASONER_CLIENT_NAMESPACE + "currentQueries";
	private static final String OUTPUT_RULES = REASONER_CLIENT_NAMESPACE + "currentRules";
	public static final Random random = new Random();

	static {
		random.setSeed(System.currentTimeMillis());
	}

	private ServiceCaller caller = null;
	private ContextEventRecorder recorder = null;

	public ReasoningCaller() {
		caller = new DefaultServiceCaller(Activator.context);
		recorder = new ContextEventRecorder(Activator.context);
		recorder.loadData();
	}

	/**
	 * Creates a new Situation-Object with a random URI, add it to the reasoner
	 * and return the new object.
	 *
	 * @param subject
	 *            Can be a type-URI or an URI of a concrete instance
	 * @param predicate
	 *            Need to be a valid property URI of the subject or can be null
	 * @param object
	 *            Need to be a valid property-value of the subject or can be
	 *            null
	 * @param persistent
	 *            If it is true the Situation will be available also after a
	 *            restart of the system, otherwise not.
	 * @return The generated Situation-Object. Can be null in case of any
	 *         trouble.
	 */
	public Situation addSituation(String subject, String predicate, String object, boolean persistent) {
		return this.addSituation(ReasoningCaller.REASONER_CLIENT_NAMESPACE + "Situation" + random.nextInt(9999999),
				subject, predicate, object, persistent);
	}

	/**
	 * Creates a new Situation-Object with the given URI, add it to the reasoner
	 * and return the new object.
	 *
	 * @param uri
	 *            URI to be given to the new Situation
	 * @param subject
	 *            Can be a type-URI or an URI of a concrete instance
	 * @param predicate
	 *            Need to be a valid property URI of the subject or can be null
	 * @param object
	 *            Need to be a valid property-value of the subject or can be
	 *            null
	 * @param persistent
	 *            If it is true the Situation will be available also after a
	 *            restart of the system, otherwise not.
	 * @return The generated Situation-Object. Can be null in case of any
	 *         trouble.
	 */
	public Situation addSituation(String uri, String subject, String predicate, String object, boolean persistent) {
		Situation situation = new Situation(uri, subject, predicate, object);
		situation.setPersistent(persistent);
		if (addObject(situation, new String[] { ReasoningService.PROP_SITUATIONS }))
			return situation;
		return null;
	}

	/**
	 * Creates a new query with the given Query-String and a random URI. The
	 * Query need to be a valid SPARQL-CONSTRUCT query that generates a valid
	 * ContextEvent. Currently there is not checked if the query is valid or
	 * not!
	 *
	 * @param fullQuery
	 *            SPARQL-CONSTRUCT query to be performed at the CHE
	 * @return Generated Query object. Can be null in case of any trouble.
	 */
	public Query addQuery(String fullQuery) {
		Query query = new Query(ReasoningCaller.REASONER_CLIENT_NAMESPACE + "Query" + random.nextInt(9999999),
				fullQuery);
		if (addObject(query, new String[] { ReasoningService.PROP_QUERIES }))
			return query;
		return null;
	}

	/**
	 * Creates a new query with the given Query-String and the given URI. The
	 * Query need to be a valid SPARQL-CONSTRUCT query that generates a valid
	 * ContextEvent. Currently there is not checked if the query is valid or
	 * not!
	 *
	 * @param uri
	 *            URI for the new Query
	 * @param fullQuery
	 *            SPARQL-CONSTRUCT query to be performed at the CHE
	 * @return Generated Query object. Can be null in case of any trouble.
	 */
	public Query addQuery(String uri, String fullQuery) {
		Query query = new Query(uri, fullQuery);
		if (addObject(query, new String[] { ReasoningService.PROP_QUERIES }))
			return query;
		return null;
	}

	/**
	 * Add a new Query with a random URI to the Reasoner. To construct the query
	 * the given ContextEvent and a part of a SPARQL-Query is used (this
	 * condition part). This means that if the conditions are fulfilled at the
	 * CHE the given ContextEvent will be generated.
	 *
	 * @param event
	 *            Event to be generated by the Query
	 * @param queryString
	 *            SPARQL Conditions-String to be checked at the CHE
	 * @param persistent
	 *            If it is true the Query will be available also after a restart
	 *            of the system, otherwise not.
	 * @return The created Query. Can be null in case of any trouble.
	 */
	public Query addQuery(ContextEvent event, String queryString, boolean persistent) {
		return this.addQuery(ReasoningCaller.REASONER_CLIENT_NAMESPACE + "Query" + random.nextInt(9999999), event,
				queryString, persistent);
	}

	/**
	 *
	 * Add a new Query with the given URI to the Reasoner. To construct the
	 * query the given ContextEvent and a part of a SPARQL-Query is used (this
	 * condition part). This means that if the conditions are fulfilled at the
	 * CHE the given ContextEvent will be generated.
	 *
	 * @param event
	 *            Event to be generated by the Query
	 * @param queryString
	 *            SPARQL Conditions-String to be checked at the CHE
	 * @param persistent
	 *            If it is true the Query will be available also after a restart
	 *            of the system, otherwise not.
	 * @return The created Query. Can be null in case of any trouble.
	 */
	public Query addQuery(String uri, ContextEvent event, String queryString, boolean persistent) {
		Query query = new Query(uri, event, queryString);
		query.setPersistent(persistent);
		if (addObject(query, new String[] { ReasoningService.PROP_QUERIES }))
			return query;
		return null;
	}

	/**
	 * A rule combines the given Situation with the given Query. This means that
	 * if the Situation-Patter was matched by an event at the context-bus the
	 * query will be performed at the CHE and if this matches it send out the
	 * ContextEvent constructed by the query at the ContextBus.
	 *
	 * @param situation
	 *            Situation for the Rule
	 * @param query
	 *            Query to be performed for the Rule
	 * @param persistent
	 *            If it is true the Rule will be available also after a restart
	 *            of the system, otherwise not.
	 * @return Created Rule object with a random URI. Can be null in case of any
	 *         trouble.
	 */
	public Rule addRule(Situation situation, Query query, boolean persistent) {
		return addRule(ReasoningCaller.REASONER_CLIENT_NAMESPACE + "Rule" + random.nextInt(9999999), situation, query,
				persistent);
	}

	/**
	 * A rule combines the given Situation with the given Query. This means that
	 * if the Situation-Patter was matched by an event at the context-bus the
	 * query will be performed at the CHE and if this matches it send out the
	 * ContextEvent constructed by the query at the ContextBus.
	 *
	 * @param uri
	 *            URI for the new Rule-Object
	 * @param situation
	 *            Situation for the Rule
	 * @param query
	 *            Query to be performed for the Rule
	 * @param persistent
	 *            If it is true the Rule will be available also after a restart
	 *            of the system, otherwise not.
	 * @return Created Rule object the given URI. Can be null in case of any
	 *         trouble.
	 */
	public Rule addRule(String uri, Situation situation, Query query, boolean persistent) {
		Rule rule = new Rule(uri, situation, query);
		rule.setPersistent(persistent);
		if (addObject(rule, new String[] { ReasoningService.PROP_RULES }))
			return rule;
		return null;
	}

	/**
	 * Generic methods to add an object based on Persistent to the Reasoner.
	 *
	 * @param <M>
	 *            Type of the object to be added (Need to be Situation, Query or
	 *            Rule)
	 * @param object
	 * @param ppPath
	 * @return True if the new element has been successful added to the
	 *         Reasoner, false otherwise
	 */
	private <M extends Persistent> boolean addObject(M object, String[] ppPath) {
		ServiceRequest addRequest = new ServiceRequest(new ReasoningService(), null);

		addRequest.addAddEffect(ppPath, object);

		ServiceResponse sr = caller.call(addRequest);

		if (sr.getCallStatus() == CallStatus.succeeded) {
			return true;
		} else {
			LogUtils.logWarn(Activator.context, ReasoningCaller.class, "addObject",
					new Object[] { "callstatus is not succeeded!" }, null);
			return false;
		}
	}

	public List<Situation> getSituations() {
		return getObjects(Situation.class, OUTPUT_SITUATIONS, new String[] { ReasoningService.PROP_SITUATIONS });
	}

	public List<Query> getQueries() {
		return getObjects(Query.class, OUTPUT_QUERIES, new String[] { ReasoningService.PROP_QUERIES });
	}

	public List<Rule> getRules() {
		return getObjects(Rule.class, OUTPUT_RULES, new String[] { ReasoningService.PROP_RULES });
	}

	/**
	 * Generic methods to get a list of saved elements from type M from the
	 * Reasoner.
	 *
	 * @param <M>
	 *            Type of the object to be added (Need to be Situation, Query or
	 *            Rule)
	 * @param requestClass
	 *            Class object of type M
	 * @param output_param
	 *            URI of the output-parameter used for the service-request (can
	 *            be any valid URI)
	 * @param ppPath
	 *            Property-Path where type M is controlled
	 *            (ReasoningService.PROP_SITUATIONS,
	 *            ReasoningService.PROP_QUERIES or ReasoningService.PROP_RULES)
	 * @return List of currently available elements from type M
	 */
	@SuppressWarnings("unchecked")
	private <M extends Persistent> List<M> getObjects(Class<M> requestClass, String output_param, String[] ppPath) {
		ServiceRequest getRequest = new ServiceRequest(new ReasoningService(), null);

		getRequest.addRequiredOutput(output_param, ppPath);

		ServiceResponse sr = caller.call(getRequest);
		if (sr.getCallStatus() == CallStatus.succeeded) {
			try {
				List<M> list = (List<M>) sr.getOutput(output_param, true);

				if (list == null || (list.size() < 1 || (list.size() > 0 && !requestClass.isInstance(list.get(0))))) {
					postInfo(ReasoningCaller.class, "getObjects",
							"there are no objects from type " + requestClass.getName());
					return null;
				}

				return list;

			} catch (Exception e) {
				postInfo(ReasoningCaller.class, "getObjects",
						"Error during gathering objects from type " + requestClass.getName());
				return null;
			}
		} else {
			postInfo(ReasoningCaller.class, "getObjects",
					"callstatus is not succeeded for objects from type " + requestClass.getName());
			return null;
		}
	}

	public boolean remove(Situation situation) {
		return removeObject(situation, new String[] { ReasoningService.PROP_SITUATIONS });
	}

	public boolean remove(Query query) {
		return removeObject(query, new String[] { ReasoningService.PROP_QUERIES });
	}

	public boolean remove(Rule rule) {
		return removeObject(rule, new String[] { ReasoningService.PROP_RULES });
	}

	public ContextEventRecorder getRecorder() {
		return this.recorder;
	}

	public void communicationChannelBroken() {

	}

	public void unregister() {
		this.caller.close();
		recorder.saveData();
		recorder.close();
		recorder = null;
	}

	/**
	 * Generic methods to remove an object based on Persistent from the
	 * Reasoner.
	 *
	 * @param <M>
	 *            Type of the object to be removed (Need to be Situation, Query
	 *            or Rule)
	 * @param object
	 *            Element to be removed
	 * @param ppPath
	 *            Property-Path where type M is controlled
	 *            (ReasoningService.PROP_SITUATIONS,
	 *            ReasoningService.PROP_QUERIES or ReasoningService.PROP_RULES)
	 * @return True if the element has been successful removed from the
	 *         Reasoner, false otherwise
	 */
	private <M extends Persistent> boolean removeObject(M object, String[] ppPath) {
		ServiceRequest removeRequest = new ServiceRequest(new ReasoningService(), null);

		removeRequest.addRemoveEffect(ppPath);
		removeRequest.addValueFilter(ppPath, object);

		ServiceResponse sr = caller.call(removeRequest);

		if (sr.getCallStatus() == CallStatus.succeeded) {
			return true;
		} else {
			LogUtils.logWarn(Activator.context, ReasoningCaller.class, "removeObject",
					new Object[] { "callstatus is not succeeded!" }, null);
			return false;
		}
	}

	public static void postInfo(Class<?> targetClass, String methodName, String message) {
		LogUtils.logInfo(Activator.context, targetClass, methodName, new Object[] { message }, null);
	}
}
