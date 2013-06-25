/*
    Copyright 2007-2014 TSB, http://www.tsbtecnologias.es
    Technologies for Health and Well-being - Valencia, Spain

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
package org.universAAL.continua.manager.publisher;
/**
 *  Interface that should be necessary implemented by the HDP manager in order to attend "connection", 
 *	"disconnection" and "received data" agents (Continua sources) events
 *  
 *  @author Angel Martinez (amartinez@tsbtecnologias.es)
 *  @author Luis Gigante (lgigante@tsbtecnologias.es) *  
 *  @version 0 June, 2012
 * 
 */


// Imports

// Class
public interface hdpManagerListener {

	public void onChannelConnected();	
	public void onChannelDisconnected();	
	public void onWeightDataReceived(String str);
	public void onDiastolicDataReceived(String str);
	public void onSystolicDataReceived(String str);
	public void onHeartRateDataReceived(String str);	
	public void onMessage(String str);
	
}
