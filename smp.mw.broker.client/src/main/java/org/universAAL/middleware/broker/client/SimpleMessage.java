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

import org.json.JSONException;
import org.json.JSONObject;
import org.universAAL.middleware.brokers.message.BrokerMessage;
import org.universAAL.middleware.brokers.message.BrokerMessageFields;
import org.universAAL.middleware.brokers.message.aalspace.AALSpaceMessageException;
import org.universAAL.middleware.interfaces.PeerCard;

/**
 * Simple Message type exchanged among AAL-aware nodes
 * 
 * @author <a href="mailto:michele.girolami@isti.cnr.it">Michele Girolami</a>
 * @author <a href="mailto:francesco.furfari@isti.cnr.it">Francesco Furfari</a>
 */
public class SimpleMessage implements BrokerMessage {

    /**
	 * 
	 */
    private SimpleMessageTypes type;
    private BrokerMessageTypes mtype;

    public enum SimpleMessageTypes {
	PING, PONG
    }

    public SimpleMessage(SimpleMessageTypes type) {
	this.type = type;
	this.mtype = BrokerMessageTypes.SimpleMessage;

    }

    public SimpleMessageTypes getType() {
	return this.type;
    }

    public String toString() {
	JSONObject obj = new JSONObject();
	try {

	    // marshall the broker message type
	    obj.put(BrokerMessageFields.BROKER_MESSAGE_TYPE, mtype.toString());

	    // marshall the aal space message type
	    obj.put(SimpleMessageFields.Simple_Message_MTYPE, type.toString());

	    return obj.toString();

	} catch (JSONException e) {

	    throw new AALSpaceMessageException(
		    "Unable to unmashall AALSpaceMessage. Full Stack: "
			    + e.toString());
	} catch (Exception e) {
	    throw new AALSpaceMessageException(
		    "Unable to unmashall AALSpaceMessage. Full Stack: "
			    + e.toString());
	}
    }

    /**
     * To implement
     */
    public PeerCard[] getReceivers() {
	// TODO Auto-generated method stub
	return null;
    }

    public BrokerMessageTypes getMType() {

	return mtype;
    }

}
