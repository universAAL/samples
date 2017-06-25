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
/**
 * x073 Continua agent publisher (agent events will be published over universAAL bus)
 *
 * @author Angel Martinez-Cavero
 * @version 0
 *
 * TSB Technologies for Health and Well-being
 */

// Package
package org.universAAL.lddi.manager.win.gui;

// Imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import org.osgi.framework.BundleContext;
import org.universAAL.lddi.manager.win.publisher.Publisher;
import org.universAAL.lddi.manager.win.publisher.hdpManager;

//Main class
public class GUI extends JDialog implements ActionListener {

	// Attributes

	/** Serializable object */
	private static final long serialVersionUID = 1L;

	/** universAAL publisher window */
	private JDialog publisher = null;

	/** Main and secondary panels */
	public static JPanel mainPanel = null;
	private JPanel radiobuttonPanel = null;
	private JPanel mainPublisherPanel = null;

	/** Components */
	private JRadioButton realMeasurementButton = null;
	private JRadioButton simulatedMeasurementButton = null;
	private ButtonGroup radioButtonsGroup = null;
	private JButton bloodPressureButton = null;
	private JButton weighingScaleButton = null;
	private JLabel logoLabel = null;
	private JLabel publisherMainLabel = null;
	private JLabel publisherLogoLabel = null;
	private JLabel publisherWeightValueLabel = null;
	public static JTextField publisherWeightValueTextfield = null;
	private JLabel publisherWeightUnitLabel = null;
	public static JTextField publisherWeightUnitTextfield = null;
	private JLabel publisherBloodPressureSysValueLabel = null;
	public static JTextField publisherBloodPressureSysValueTextfield = null;
	private JLabel publisherBloodPressureDiaValueLabel = null;
	public static JTextField publisherBloodPressureDiaValueTextfield = null;
	private JLabel publisherBloodPressurePulValueLabel = null;
	public static JTextField publisherBloodPressurePulValueTextfield = null;
	private JButton publisherButton = null;

	/** Constants */
	private static final String weighingScaleImage = "ws.png";
	private static final String bloodPressureImage = "bp.png";
	private static final String logoImage = "uaal_logo_resized.jpg";

	/** Bundle context object */
	private static BundleContext ctx = null;

	/** Publisher object to send events */
	private static Publisher X73Publisher = null;

	/** HDP manager object */
	private hdpManager manager = null;

	/** Expected remote device type */
	private static String remoteDeviceType = null;

	/** Final data to be published */
	public static double finalMeasuredWeightData = -1.0;
	public static double finalSysBloodPressureData = -1;
	public static double finalDiaBloodPressureData = -1;
	public static double finalHrBloodPressureData = -1;

	/** Real or simulated measurement */
	public static boolean realMeasurement = false;

	// Constructor
	public GUI(BundleContext context) {
		ctx = context;
		init();
	}

	// Methods
	/** Create the main frame */
	public void init() {
		// Main dialog
		setResizable(false);
		setBounds(100, 100, 550, 375);
		setTitle("universAAL Continua manager client");
		getContentPane().setLayout(new BorderLayout());
		// Main panel (content pane)
		mainPanel = createJPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		// Create and add components
		createComponents();
		// Show
		setVisible(true);
	}

	/** Create components */
	public void createComponents() {
		// Label (universAAL image icon)
		logoLabel = new JLabel("");
		logoLabel.setIcon(new ImageIcon(ctx.getBundle().getResource(logoImage)));
		logoLabel.setBounds(75, 10, 300, 81);
		mainPanel.add(logoLabel);
		// Radio buttons group
		radiobuttonPanel = new JPanel();
		radiobuttonPanel.setLayout(new GridLayout(1, 0));
		radiobuttonPanel.setBounds(20, 100, 475, 50);
		radioButtonsGroup = new ButtonGroup();
		realMeasurementButton = createJRadioButton("Real measurement");
		realMeasurementButton.setToolTipText("Continua devices should be paired first");
		simulatedMeasurementButton = createJRadioButton("Simulated measurement");
		simulatedMeasurementButton
				.setToolTipText("Random values will be published to universAAL context bus (Continua devices not required)");
		radiobuttonPanel.add(realMeasurementButton);
		radiobuttonPanel.add(simulatedMeasurementButton);
		mainPanel.add(radiobuttonPanel);
		// Buttons
		bloodPressureButton = createJButton(bloodPressureImage, 214, 160, 218, 145, "bloodPressure");
		mainPanel.add(bloodPressureButton);
		weighingScaleButton = createJButton(weighingScaleImage, 18, 154, 178, 157, "weighingScale");
		mainPanel.add(weighingScaleButton);
	}

	/** Create JPanel */
	public JPanel createJPanel() {
		JPanel output = null;
		output = new JPanel();
		output.setLayout(null);
		output.setBorder(new EmptyBorder(5, 5, 5, 5));
		output.setBackground(Color.WHITE);
		return output;
	}

	/** Create JButtons */
	public JButton createJButton(String image, int x, int y, int weight, int height, String name) {
		JButton output = null;
		output = new JButton("");
		output.setBackground(Color.WHITE);
		if (image != null)
			output.setIcon(new ImageIcon(ctx.getBundle().getResource(image)));
		output.setBounds(x, y, weight, height);
		output.setActionCommand(name);
		output.addActionListener(this);
		return output;
	}

	/** Create JRadioButtons */
	public JRadioButton createJRadioButton(String name) {
		JRadioButton output = null;
		output = new JRadioButton(name, false);
		output.setActionCommand(name);
		output.setBackground(Color.WHITE);
		output.addActionListener(this);
		radioButtonsGroup.add(output);
		return output;
	}

	/** Close GUI frame */
	public void closeGUI() {
		dispose();
	}

	/** Action listener for radio buttons and jbuttons */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("bloodPressure")) {
			// Blood pressure device
			if (realMeasurementButton.isSelected()) {
				realMeasurement = true;
				createPublisher("bloodPressure", "real");
			} else if (simulatedMeasurementButton.isSelected()) {
				realMeasurement = false;
				createPublisher("bloodPressure", "simulated");
			}
		} else if (e.getActionCommand().equals("weighingScale")) {
			// Weighing scale device
			if (realMeasurementButton.isSelected()) {
				realMeasurement = true;
				createPublisher("weighingScale", "real");
			} else if (simulatedMeasurementButton.isSelected()) {
				realMeasurement = false;
				createPublisher("weighingScale", "simulated");
			}
		} else if (e.getActionCommand().equals("publishData")) {
			// Publish data to universAAL context bus. Ensure that values are not NULL
			X73Publisher = new Publisher(ctx);
			if (realMeasurement) {
				// Real values
				if (remoteDeviceType.equals("WeightingScale")) {
					if (finalMeasuredWeightData != -1.0) {
						double temp_1 = shortDecimalNumber(finalMeasuredWeightData) * 1000;
						int temp = (int) temp_1;
						X73Publisher.publishWeightEvent(temp);
						// stopPublisherGUI();
					}
				} else {
					if ((finalDiaBloodPressureData != -1) && (finalHrBloodPressureData != -1)
							&& (finalSysBloodPressureData != -1)) {
						int temp_0 = (int) finalSysBloodPressureData;
						int temp_1 = (int) finalDiaBloodPressureData;
						int temp_2 = (int) finalHrBloodPressureData;
						X73Publisher.publishBloodPressureEvent(temp_0, temp_1, temp_2);
						// stopPublisherGUI();
					}
				}
			} else {
				// Random values
				if (remoteDeviceType.equals("WeightingScale")) {
					X73Publisher.publishWeightEvent(Integer.parseInt(publisherWeightValueTextfield.getText()));
					// stopPublisherGUI();
				} else {
					X73Publisher.publishBloodPressureEvent(
							Integer.parseInt(publisherBloodPressureSysValueTextfield.getText()),
							Integer.parseInt(publisherBloodPressureDiaValueTextfield.getText()),
							Integer.parseInt(publisherBloodPressurePulValueTextfield.getText()));
					// stopPublisherGUI();
				}
			}
		}
	}

	/** Create and show universAAL publisher frame */
	public void createPublisher(String device, String type) {
		// Hide main GUI
		setVisible(false);
		// Create dialog frame
		publisher = new JDialog(this, "universAAL publisher", true);
		publisher.setResizable(false);
		publisher.setBounds(100, 100, 650, 475);
		publisher.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		publisher.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stopPublisherGUI();
			}
		});
		// Main panel
		mainPublisherPanel = createJPanel();
		publisher.getContentPane().add(mainPublisherPanel, BorderLayout.CENTER);
		// Main text label
		publisherMainLabel = createJLabel("", 200, 5, 350, 50, Font.BOLD, 24);
		// Image icon
		publisherLogoLabel = new JLabel("");
		publisherLogoLabel.setBounds(25, 75, 218, 145);
		// Check Continua agent selected
		if (device.equals("bloodPressure")) {
			publisherMainLabel.setText("Blood pressure monitor");
			publisherLogoLabel.setIcon(new ImageIcon(ctx.getBundle().getResource(bloodPressureImage)));
			publisherBloodPressureSysValueLabel = createJLabel("SYS(mmHg)", 250, 100, 100, 50, Font.PLAIN, 16);
			publisherBloodPressureSysValueTextfield = createJTextfield(400, 100, 100, 50, Font.PLAIN, 16);
			publisherBloodPressureDiaValueLabel = createJLabel("DIA(mmHg)", 250, 150, 100, 50, Font.PLAIN, 16);
			publisherBloodPressureDiaValueTextfield = createJTextfield(400, 150, 100, 50, Font.PLAIN, 16);
			publisherBloodPressurePulValueLabel = createJLabel("     BPM", 250, 200, 100, 50, Font.PLAIN, 16);
			publisherBloodPressurePulValueTextfield = createJTextfield(400, 200, 100, 50, Font.PLAIN, 16);
		} else {
			publisherMainLabel.setText("Weighing scale");
			publisherLogoLabel.setIcon(new ImageIcon(ctx.getBundle().getResource(weighingScaleImage)));
			publisherWeightValueLabel = createJLabel("Weight value", 250, 100, 150, 50, Font.PLAIN, 16);
			publisherWeightValueTextfield = createJTextfield(400, 100, 100, 50, Font.PLAIN, 16);
			publisherWeightUnitLabel = createJLabel(" Weight unit", 250, 150, 150, 50, Font.PLAIN, 16);
			publisherWeightUnitTextfield = createJTextfield(400, 150, 100, 50, Font.PLAIN, 16);
		}
		publisherButton = createJButton(null, 250, 275, 200, 25, "publishData");
		publisherButton.setText("Publish to universAAL");
		publisherButton.setToolTipText("Public measured data to universAAL context bus");
		// Check type of measurement
		if (type.equals("real")) {
			// Run hdp manager and wait for agent values
			if (device.equals("bloodPressure")) {
				remoteDeviceType = "BloodPressureMonitor";
				instantiateHdpManager();
			} else {
				remoteDeviceType = "WeightingScale";
				instantiateHdpManager();
			}
		} else {
			// Generate random values
			if (device.equals("bloodPressure")) {
				remoteDeviceType = "BloodPressureMonitor";
				publisherBloodPressureSysValueTextfield.setText("" + getRandomValue(90, 119));
				publisherBloodPressureDiaValueTextfield.setText("" + getRandomValue(60, 79));
				publisherBloodPressurePulValueTextfield.setText("" + getRandomValue(49, 198));
			} else {
				remoteDeviceType = "WeightingScale";
				publisherWeightValueTextfield.setText("" + getRandomValue(50, 110));
				publisherWeightUnitTextfield.setText("kg");
			}
		}
		// Add components to the panel
		mainPublisherPanel.add(publisherMainLabel);
		mainPublisherPanel.add(publisherLogoLabel);
		addJLabelComponent(publisherBloodPressureSysValueLabel);
		addJLabelComponent(publisherBloodPressureSysValueTextfield);
		addJLabelComponent(publisherBloodPressureDiaValueLabel);
		addJLabelComponent(publisherBloodPressureDiaValueTextfield);
		addJLabelComponent(publisherBloodPressurePulValueLabel);
		addJLabelComponent(publisherBloodPressurePulValueTextfield);
		addJLabelComponent(publisherWeightValueLabel);
		addJLabelComponent(publisherWeightValueTextfield);
		addJLabelComponent(publisherWeightUnitLabel);
		addJLabelComponent(publisherWeightUnitTextfield);
		mainPublisherPanel.add(publisherButton);
		// Show
		// TODO cambio para review true -> false
		publisher.setVisible(false);
	}

	/** Create JLabel */
	public JLabel createJLabel(String name, int x, int y, int weight, int height, int fontType, int fontSize) {
		JLabel output = null;
		output = new JLabel(name);
		output.setBounds(x, y, weight, height);
		output.setFont(new Font("Courier", fontType, fontSize));
		output.setHorizontalTextPosition(SwingConstants.CENTER);
		return output;
	}

	/** Create JTextfield */
	public JTextField createJTextfield(int x, int y, int weight, int height, int fontType, int fontSize) {
		JTextField output = null;
		output = new JTextField(10);
		output.setBounds(x, y, weight, height);
		output.setEditable(false);
		output.setColumns(10);
		output.setHorizontalAlignment(JTextField.CENTER);
		output.setFont(new Font("Courier", fontType, fontSize));
		return output;
	}

	/** Add components to panel */
	public void addJLabelComponent(JComponent component) {
		if (component != null)
			mainPublisherPanel.add(component);
	}

	/** Reset components */
	public void resetComponentsStatus() {
		publisherWeightValueLabel = null;
		publisherWeightValueTextfield = null;
		publisherWeightUnitLabel = null;
		publisherWeightUnitTextfield = null;
		publisherBloodPressureSysValueLabel = null;
		publisherBloodPressureSysValueTextfield = null;
		publisherBloodPressureDiaValueLabel = null;
		publisherBloodPressureDiaValueTextfield = null;
		publisherBloodPressurePulValueLabel = null;
		publisherBloodPressurePulValueTextfield = null;
		remoteDeviceType = null;
		realMeasurement = false;
		finalMeasuredWeightData = -1.0;
		finalSysBloodPressureData = -1;
		finalDiaBloodPressureData = -1;
		finalHrBloodPressureData = -1;
	}

	/** Shorten number of decimals */
	public static double shortDecimalNumber(double d) {
		return Math.round(d * Math.pow(10, 2)) / Math.pow(10, 2);
	}

	/** Get randomized value */
	public int getRandomValue(int min, int max) {
		int output = -1;
		Random r = new Random();
		int minValue = min;
		int maxValue = max;
		output = r.nextInt(maxValue - minValue + 1) + minValue;
		return output;
	}

	// TODO
	public static void publishDataToContextBus() {
		// Publish data to universAAL context bus. Ensure that values are not NULL
		X73Publisher = new Publisher(ctx);
		if (realMeasurement) {
			// Real values
			if (remoteDeviceType.equals("WeightingScale")) {
				if (finalMeasuredWeightData != -1.0) {
					double temp_1 = shortDecimalNumber(finalMeasuredWeightData) * 1000;
					int temp = (int) temp_1;
					X73Publisher.publishWeightEvent(temp);
					// stopPublisherGUI();
				}
			} else {
				if ((finalDiaBloodPressureData != -1) && (finalHrBloodPressureData != -1)
						&& (finalSysBloodPressureData != -1)) {
					int temp_0 = (int) finalSysBloodPressureData;
					int temp_1 = (int) finalDiaBloodPressureData;
					int temp_2 = (int) finalHrBloodPressureData;
					X73Publisher.publishBloodPressureEvent(temp_0, temp_1, temp_2);
					// stopPublisherGUI();
				}
			}
		} else {
			// Random values
			if (remoteDeviceType.equals("WeightingScale")) {
				X73Publisher.publishWeightEvent(Integer.parseInt(publisherWeightValueTextfield.getText()));
				// stopPublisherGUI();
			} else {
				X73Publisher.publishBloodPressureEvent(
						Integer.parseInt(publisherBloodPressureSysValueTextfield.getText()),
						Integer.parseInt(publisherBloodPressureDiaValueTextfield.getText()),
						Integer.parseInt(publisherBloodPressurePulValueTextfield.getText()));
				// stopPublisherGUI();
			}
		}
	}

	/** Create a new HDP manager object */
	public void instantiateHdpManager() {
		new Thread() {
			public void run() {
				manager = new hdpManager(remoteDeviceType);
				manager.init();
			}
		}.start();
	}

	/** */
	public void stopPublisherGUI() {
		if (hdpManager.readyToCloseWindow) {
			if (manager != null) {
				manager.exit();
				manager = null;
			}
			// resetComponentsStatus();
			if (publisher != null)
				publisher.dispose();
			setVisible(false);
		}
	}

	/** Exit all */
	public void stopGUI() {
		if (manager != null) {
			manager.exit();
			manager = null;
		}
		// resetComponentsStatus();
		if (publisher != null)
			publisher.dispose();
	}
}