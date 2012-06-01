/**
 * 
 *  OCO Source Materials 
 *      © Copyright IBM Corp. 2012 
 *
 *      See the NOTICE file distributed with this work for additional 
 *      information regarding copyright ownership 
 *       
 *      Licensed under the Apache License, Version 2.0 (the "License"); 
 *      you may not use this file except in compliance with the License. 
 *      You may obtain a copy of the License at 
 *       	http://www.apache.org/licenses/LICENSE-2.0 
 *       
 *      Unless required by applicable law or agreed to in writing, software 
 *      distributed under the License is distributed on an "AS IS" BASIS, 
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *      See the License for the specific language governing permissions and 
 *      limitations under the License. 
 *
 */
package org.universAAL.samples.lighting.server.unit_impl;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * 
 *  @author <a href="mailto:vadime@il.ibm.com"> vadime </a>
 *
 */
public class UILampsView extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final int LAMP_SIZE = 70;
	private static final int LAMP_OFFSET = 70;
	private static final int VIEW_WIDTH = 400;
	private static final int VIEW_HEIGHT = 400;
	
	private static final int VIEW_X_LOCATION = 600;
	private static final int VIEW_Y_LOCATION = 0;
	
	private static final Color OFF_COLOR = Color.blue;
	private static final Color ON_COLOR = Color.red;
	
	private static final Color LAMP_TEXT_COLOR = Color.white;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -954851339191098267L;
	
	private Map lampComponents = new HashMap();
	
	static {
		// Vadim - turn off the logging 
		java.util.logging.Logger.getLogger("sun").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("java").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("javax").setLevel(java.util.logging.Level.OFF);
	}
	/**
	 * @param lampIDs
	 */
	public UILampsView(int[] lampIDs)  {
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		
		frame.setSize(VIEW_WIDTH, VIEW_HEIGHT);
		frame.setLocation(VIEW_X_LOCATION, VIEW_Y_LOCATION);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Lamp Server");
		
		if (lampIDs.length < 1) {
			return;
		}
		
		if (lampIDs.length != 4) {
			throw new UnsupportedOperationException("Only layoyut of 4 lamps is implemented");
		}

		Component lampComponent = null; 
		
		lampComponent = addLampComponent(frame, lampIDs[0]);
		lampComponent.setLocation(LAMP_OFFSET, LAMP_OFFSET);
		lampComponent = addLampComponent(frame, lampIDs[1]);
		lampComponent.setLocation(LAMP_OFFSET, VIEW_HEIGHT - (LAMP_OFFSET + LAMP_SIZE));
		lampComponent = addLampComponent(frame, lampIDs[2]);
		lampComponent.setLocation(VIEW_WIDTH - (LAMP_OFFSET + LAMP_SIZE), LAMP_OFFSET);
		lampComponent = addLampComponent(frame, lampIDs[3]);
		lampComponent.setLocation(VIEW_WIDTH - (LAMP_OFFSET + LAMP_SIZE), 
				VIEW_HEIGHT - (LAMP_OFFSET + LAMP_SIZE));
		
		frame.setEnabled(true);	
		
	}

	/**
	 * @param frame
	 * @param lampID
	 */
	private Component addLampComponent(JFrame frame, int lampID) {
		JPanel lampPanel = new JPanel();
		JLabel lampLabel = new JLabel(Integer.toString(lampID));
		lampLabel.setForeground(LAMP_TEXT_COLOR);
		lampLabel.setHorizontalTextPosition(JLabel.CENTER);
		lampLabel.setVerticalTextPosition(JLabel.CENTER);
		lampPanel.add(lampLabel);
		lampLabel.setEnabled(true);
		lampPanel.setSize(LAMP_SIZE,LAMP_SIZE);
		
		lampPanel.setBorder(BorderFactory.createLineBorder(Color.black));		
		Component lampComponent = frame.getContentPane().add(lampPanel);
		lampComponents.put(Integer.valueOf(lampID),lampComponent);
		setLampState(lampID, false);
		lampPanel.setEnabled(true);
		return lampComponent;
	}
		

		
		

	/**
	 * @param lampID
	 * @param isOn
	 */
	public void setLampState(int lampID, boolean isOn) {
		Component lamp = (Component)lampComponents.get(Integer.valueOf(lampID));
		if (lamp != null) {
			lamp.setBackground(isOn ? ON_COLOR:OFF_COLOR);
		}
	}

	

}
