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
package org.universAAL.middleware.broker.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.universAAL.middleware.broker.client.SimpleMessage.SimpleMessageTypes;
import org.universAAL.middleware.brokers.Broker;
import org.universAAL.middleware.brokers.message.BrokerMessage;
import org.universAAL.middleware.connectors.exception.CommunicationConnectorException;
import org.universAAL.middleware.connectors.util.ChannelMessage;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.interfaces.PeerCard;
import org.universAAL.middleware.interfaces.PeerRole;
import org.universAAL.middleware.interfaces.space.SpaceCard;
import org.universAAL.middleware.managers.api.SpaceManager;
import org.universAAL.middleware.modules.CommunicationModule;
import org.universAAL.middleware.modules.listener.MessageListener;

import com.google.gson.Gson;

/**
 * Simple Broker showing how to access to an AALSpace and how to send messages
 * 
 * @author <a href="mailto:michele.girolami@isti.cnr.it">Michele Girolami</a>
 * @author <a href="mailto:francesco.furfari@isti.cnr.it">Francesco Furfari</a>
 * @author <a href="mailto:stefano.lenzi@isti.cnr.it">Stefano Lenzi</a>
 */
public class BrokerClientImpl implements Broker, MessageListener {

	private ModuleContext context;
	private SpaceManager aalSpaceManager;
	private CommunicationModule communicationModule;
	private boolean stop = false;
	private String brokerName;

	// private static final String PING = "PING";
	// private static final String PONG = "PONG";

	public BrokerClientImpl(ModuleContext module) {
		this.context = module;
	}

	public void startBrokerClient() {

		Object ref = context.getContainer().fetchSharedObject(context,
				new Object[] { SpaceManager.class.getName().toString() });
		if (ref != null) {

			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "AALSpaceManager found!" }, null);
			aalSpaceManager = (SpaceManager) ref;
			brokerName = getBrokerName();
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "AALSpaceModule fetched" }, null);

		} else {
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "Terminating the session!" }, null);

			return;
		}
		ref = context.getContainer().fetchSharedObject(context,
				new Object[] { CommunicationModule.class.getName().toString() });
		if (ref != null) {

			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "CommunicationModule found!" }, null);

			communicationModule = (CommunicationModule) ref;
			communicationModule.addMessageListener(this, brokerName);

			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "CommunicationModule fetched!" }, null);

		} else {
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "Terminating the session" }, null);
			return;
		}

		LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
				new Object[] { "---------------------" }, null);

		LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
				new Object[] { "---------Session 1--------------" }, null);

		Set<SpaceCard> aalSpaces = aalSpaceManager.getSpaces();
		if (aalSpaces != null)
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "Found:" + aalSpaces.size() + " AALSpaces" }, null);

		LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "----" }, null);

		if (aalSpaceManager.getSpaceDescriptor() != null)
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "Currently member of the AALSpace: "
							+ aalSpaceManager.getSpaceDescriptor().getSpaceCard().toString() },
					null);

		LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "----" }, null);

		if (aalSpaceManager.getManagedSpaces() != null)
			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient", new Object[] {
					"This MW instance manages: " + aalSpaceManager.getManagedSpaces().size() + " AALSpaces" }, null);

		LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "-----" }, null);

		if (aalSpaceManager.getPeers() != null)

			LogUtils.logDebug(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "There are: " + aalSpaceManager.getPeers().size() + "peers" }, null);

		LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "Peers are: " }, null);

		for (String key : aalSpaceManager.getPeers().keySet())
			LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "Peer: " + key },
					null);

		LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient", new Object[] { "--------------------" },
				null);
		LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient",
				new Object[] { "----STEP 2: Sending Ping - Pong messages---" }, null);

		// first discover the other peer
		if (aalSpaceManager.getMyPeerCard().isCoordinator()) {

			LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient",
					new Object[] { "Waiting for a Ping message..." }, null);

			return;
		} else {
			String dest = aalSpaceManager.getSpaceDescriptor().getSpaceCard().getCoordinatorID();
			PeerCard dstCard = new PeerCard(dest, PeerRole.COORDINATOR);
			SimpleMessage ping = new SimpleMessage(SimpleMessageTypes.PING);

			// ...and wrap it as ChannelMessage
			List<String> channelName = new ArrayList<String>();
			channelName.add(getBrokerName());
			ChannelMessage channelMessage = new ChannelMessage(aalSpaceManager.getMyPeerCard(), ping.toString(),
					channelName);
			communicationModule.send(channelMessage, this, dstCard);
		}
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void handleSendError(ChannelMessage message, CommunicationConnectorException e) {

		LogUtils.logError(context, BrokerClientImpl.class, "startBrokerClient", new Object[] {
				"Error sending a message in the AALSPace: " + message.toString() + "with error: " + e.toString() },
				null);

	}

	public void messageReceived(ChannelMessage message) {
		if (stop)
			return;
		if (message != null && unmarshall(message.getContent()) instanceof SimpleMessage) {
			SimpleMessage sM = (SimpleMessage) unmarshall(message.getContent());
			if (sM.getType().equals(SimpleMessageTypes.PING)) {
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient",
						new Object[] { "PING FROM: " + message.getSender().getPeerID() }, null);

				SimpleMessage ping = new SimpleMessage(SimpleMessageTypes.PONG);

				// ...and wrap it as ChannelMessage
				List<String> channelName = new ArrayList<String>();
				channelName.add(getBrokerName());
				ChannelMessage channelMessage = new ChannelMessage(aalSpaceManager.getMyPeerCard(), ping.toString(),
						channelName);

				communicationModule.send(channelMessage, this, message.getSender());
			} else if (sM.getType().equals(SimpleMessageTypes.PONG)) {

				LogUtils.logInfo(context, BrokerClientImpl.class, "startBrokerClient",
						new Object[] { "PONG FROM: " + message.getSender().getPeerID() }, null);

				SimpleMessage ping = new SimpleMessage(SimpleMessageTypes.PING);
				// ...and wrap it as ChannelMessage
				List<String> channelName = new ArrayList<String>();
				channelName.add(getBrokerName());
				ChannelMessage channelMessage = new ChannelMessage(aalSpaceManager.getMyPeerCard(), ping.toString(),
						channelName);
				communicationModule.send(channelMessage, this, message.getSender());
			}
		}
	}

	public String getBrokerName() {
		return context.getID();
	}

	public void dispose() {

	}

	public boolean init() {

		return false;
	}

	public BrokerMessage unmarshall(String message) {
		try {

			Gson gson = new Gson();
			return gson.fromJson(message, SimpleMessage.class);

		} catch (Exception e) {
			System.err.println(e);
			return null;

		}
	}

}
