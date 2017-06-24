/*
     Copyright 2010-2014 AIT Austrian Institute of Technology GmbH
	 http://www.ait.ac.at

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

package org.universAAL.lddi.samples.device.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

/**
 * This is the connector class to the uAAL related ontology classes, mainly
 * DeviceContextListener.java This class knows nothing about Ontologies! It
 * could be part of a Java application. Here, it is a single class application
 * with a simple GUI. 2 Buttons are provided for 'getAllSensors' and
 * 'getSensorInfo' Furthermore, 2 textAreas for incoming context events and log
 * messages
 *
 * @author Thomas Fuxreiter (foex@gmx.at)
 */
public class DeviceClient extends javax.swing.JPanel {

	private static final long serialVersionUID = 885696167607678920L;

	// /**
	// * key is the instanceURI of the device, from the ontology, but is just a
	// String
	// * value is an ActivityHubSensor object from the iso11073 library; not
	// from the ontology model!
	// */
	// private Map<String,Device> devices = new LinkedHashMap<String,Device>();
	//
	private Activator myParent;
	private static JFrame frame;
	private static ListModel jListModel = new DefaultComboBoxModel(new Object[] { "Init..." });
	static private JList jList1 = new JList(jListModel);
	private static JScrollPane jsp0 = new JScrollPane(jList1);
	// private static JTextArea deviceArea = new JTextArea();
	private static JTextArea deviceInfoArea = new JTextArea();
	private static JScrollPane jsp1 = new JScrollPane(deviceInfoArea);
	private static JTextArea contextArea = new JTextArea();
	private static JScrollPane jsp2 = new JScrollPane(contextArea);
	private static JTextArea logArea = new JTextArea();
	private static JScrollPane jsp3 = new JScrollPane(logArea);
	private static JLabel label1;
	private static JLabel label2;
	private static JLabel label3;
	private static JLabel label4;
	private JButton switchOnButton;
	private JButton switchOffButton;
	private JButton devicesButton;
	private AbstractAction Info1;
	private AbstractAction Info2;
	private AbstractAction Info3;

	private JTextField field1;

	/**
	 * Constructor
	 *
	 * @param link
	 *            to Activator
	 */
	public DeviceClient(Activator parent) {
		super();
		this.myParent = parent;
		initGUI();
		start();
	}

	private void initGUI() {
		try {
			setPreferredSize(new Dimension(1000, 800));
			this.setLayout(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// create the GUI
	public void start() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // doesn't
																			// stop
																			// the
																			// whole
																			// framework
																			// on
																			// close
		frame.pack();
		frame.setSize(1000, 800);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Device Client Example");
		initComponents();
		frame.setEnabled(true);
	}

	private void initComponents() {
		{
			label1 = new JLabel("Devices");
			frame.getContentPane().add(label1);
			label1.setBounds(20, 20, 200, 20);

			frame.getContentPane().add(jsp0);
			jsp0.setBounds(20, 40, 680, 100);

			// jList1 = new JList();
			// jList1.add(new Scrollbar(Scrollbar.VERTICAL));
			// frame.getContentPane().add(jList1);
			// jList1.setBounds(20, 40, 680, 100);
		}

		{
			devicesButton = new JButton();
			frame.getContentPane().add(devicesButton);
			devicesButton.setText("getAllDevices");
			devicesButton.setBounds(720, 40, 250, 35);
			devicesButton.setAction(getAllDevices());
		}

		{
			switchOnButton = new JButton();
			frame.getContentPane().add(switchOnButton);
			// switchOnButton.setText("Switch ON");
			switchOnButton.setBounds(720, 105, 110, 35);
			switchOnButton.setAction(switchDeviceOn());
		}

		// {
		// field1 = new JTextField(20);
		// frame.getContentPane().add(field1);
		// field1.setText("");
		// field1.setBounds(940, 105, 20, 20);
		// }

		{
			switchOffButton = new JButton();
			frame.getContentPane().add(switchOffButton);
			// switchOffButton.setText("Switch OFF");
			switchOffButton.setBounds(860, 105, 110, 35);
			switchOffButton.setAction(switchDeviceOff());
		}

		{
			label2 = new JLabel("Device Info");
			frame.getContentPane().add(label2);
			label2.setBounds(20, 150, 200, 20);

			frame.getContentPane().add(jsp1);
			jsp1.setBounds(20, 170, 900, 150);
		}

		{
			label3 = new JLabel("Context Events");
			frame.getContentPane().add(label3);
			label3.setBounds(20, 330, 200, 20);

			frame.getContentPane().add(jsp2);
			jsp2.setBounds(20, 350, 900, 180);
		}

		{
			label4 = new JLabel("Log");
			frame.getContentPane().add(label4);
			label4.setBounds(20, 540, 200, 20);

			frame.getContentPane().add(jsp3);
			jsp3.setBounds(20, 560, 900, 180);
		}
	}

	// private AbstractAction setDeviceValue() {
	// if(Info3 == null) {
	// Info2 = new AbstractAction("set device value", null) {
	// public void actionPerformed(ActionEvent evt) {
	// myParent.serviceCaller.switchActuator((String)jList1.getSelectedValue(),
	// Integer.parseInt(field1.getText()));
	// }
	// };
	// }
	// return Info3;
	// }

	private AbstractAction switchDeviceOn() {
		if (Info2 == null) {
			Info2 = new AbstractAction("Switch ON", null) {
				public void actionPerformed(ActionEvent evt) {
					myParent.serviceCaller.switchdevice((String) jList1.getSelectedValue(), true);
				}
			};
		}
		return Info2;
	}

	private AbstractAction switchDeviceOff() {
		if (Info3 == null) {
			Info3 = new AbstractAction("Switch OFF", null) {
				public void actionPerformed(ActionEvent evt) {
					myParent.serviceCaller.switchdevice((String) jList1.getSelectedValue(), false);
				}
			};
		}
		return Info3;
	}

	/**
	 * AbstractAction for button getSensors initiates service call to parent
	 *
	 * @return
	 */
	private AbstractAction getAllDevices() {
		if (Info1 == null) {
			Info1 = new AbstractAction("get all Devices", null) {
				public void actionPerformed(ActionEvent evt) {
					String[] devices = myParent.serviceCaller.getControlledDevices();
					if (devices != null) {
						jList1.setListData(devices);
						// for (String device : devices)
						// addTextToDeviceArea(device);
					} else {
						addTextToLogArea("No devices available!");
					}

				}
			};
		}
		return Info1;
	}

	// /**
	// * AbstractAction for button getInfo
	// * initiates service call to parent
	// * @return
	// */
	// private AbstractAction getInfo() {
	// if(Info2 == null) {
	//
	// Info2 = new AbstractAction("get sensor details", null) {
	//
	// public void actionPerformed(ActionEvent evt) {
	//
	// int type = 0;
	// if (devices.get((String)jList1.getSelectedValue()) != null) {
	// type =
	// devices.get((String)jList1.getSelectedValue()).getDeviceCategory().getTypeCode();
	// }
	//
	// myParent.serviceConsumer.getDeviceInfo((String)jList1.getSelectedValue(),type
	// );
	// }
	// };
	// }
	// return Info2;
	// }

	// /**
	// * create new activityhubsensors from iso11073 library, not from ontology!
	// * store them in map this.activityHubSensors
	// *
	// * @param resourceURI instanceId
	// * @param sensorType must be identical from the ontology model to the
	// iso11073 library model !!
	// */
	// public void addDevice(String resourceURI, int sensorType) {
	// String deviceId = resourceURI.substring(resourceURI.indexOf('#')+1); //
	// e.g. controlledActivityHubDevice1/1/1
	// ActivityHubSensor ahs = (ActivityHubSensor)
	// ActivityHubFactory.createInstance(
	// ActivityHubDeviceCategory.get(sensorType), null,
	// deviceId, null);
	//
	// this.devices.put(resourceURI, ahs);
	//
	// addTextToLogArea("Added new ActivityHubSensor. Category: " +
	// ahs.getDeviceCategory().toString() +
	// " deviceId: " + ahs.getDeviceId());
	// }

	// /**
	// * just display the sensors that are already in the datastore
	// this.activityHubSensors
	// * in the first list on the GUI
	// * Should be called after all sensors are stored through multiple calls of
	// addActivityHubSensor
	// */
	// public void showSensorList() {
	// jList1.setListData(this.devices.keySet().toArray());
	// }

	/**
	 * send all text lines from the given array to addTextToDeviceArea
	 *
	 * @param multiple
	 *            text lines for 1 device info response
	 */
	public void showDeviceInfo(String[] listData) {
		for (String line : listData) {
			addTextToDeviceArea(line);
		}
	}

	/**
	 * Helper class that displays the given text on the deviceInfoArea on the
	 * GUI adding a line break and set focus always to the bottom of the
	 * textArea
	 *
	 * @param text
	 */
	public void addTextToDeviceArea(String text) {
		deviceInfoArea.setText(deviceInfoArea.getText() + text + "\n");
		deviceInfoArea.setCaretPosition(deviceInfoArea.getDocument().getLength());
	}

	/**
	 * send all text lines from the given array to addTextToContextArea
	 *
	 * @param multiple
	 *            text lines for 1 context event
	 */
	public void showContextEvent(String[] listData) {
		for (String line : listData) {
			addTextToContextArea(line);
		}
	}

	/**
	 * Helper class that displays the given text on the contextArea on the GUI
	 * adding a line break and set focus always to the bottom of the textArea
	 *
	 * @param text
	 */
	public void addTextToContextArea(String text) {
		contextArea.setText(contextArea.getText() + text + "\n");
		contextArea.setCaretPosition(contextArea.getDocument().getLength());
	}

	/**
	 * Helper class that displays the given text on the logArea on the GUI
	 * adding a line break
	 *
	 * @param text
	 */
	public void addTextToLogArea(String text) {
		logArea.setText(logArea.getText() + text + "\n");
	}

	/**
	 *
	 */
	public void deleteGui() {
		frame.dispose();
	}

}
