/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.universAAL.samples.sensor.simulator;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.device.HumiditySensor;
import org.universAAL.ontology.device.TemperatureSensor;
import org.universAAL.ontology.lighting.LightSource;

import javax.swing.border.TitledBorder;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;

/**
 * @author eandgrg
 * 
 */
public class SensorSimulatorGUI extends JFrame {

    private static final long serialVersionUID = 6611196953807109089L;
    private JPanel contentPane;
    private LocationContextPublisher lp;
    private LampStatePublisher lampStatePublisher;
    private RoomTemperaturePublisher roomTemperaturePublisher;
    private RoomHumidityPublisher roomHumidityPublisher;

    private String USER_NAMESPACE = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JTextField textFieldUserLocation;

    private String LOCATION_NAMESPACE = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX;
    private String SLEEPING_ROOM = "sleepingRoom";
    private String BATHROOM = "bathroom";
    private String HOBBY_ROOM = "hobbyRoom";
    private String LIVING_ROOM = "livingRoom";
    private String KITCHEN = "kitchen";

    /**
     * Create the frame.
     */
    public SensorSimulatorGUI(LocationContextPublisher lp,
	    LampStatePublisher lampStatePublisher,
	    RoomTemperaturePublisher roomTemperaturePublisher,
	    RoomHumidityPublisher roomHumidityPublisher) {
	this.lp = lp;
	this.lampStatePublisher = lampStatePublisher;
	this.roomTemperaturePublisher = roomTemperaturePublisher;
	this.roomHumidityPublisher = roomHumidityPublisher;

	initGUI();
    }

    /**
     * Returns TypeOfUser depending on which radio button has been selected Only
     * requirement is that the buttons have their names set when they are
     * created
     * 
     * @return
     */
    private TypeOfUser getSelectedUserType() {

	Enumeration<AbstractButton> buttons = buttonGroup.getElements();
	AbstractButton button;

	while (buttons.hasMoreElements()) {
	    button = buttons.nextElement();

	    if (button.isSelected()) {
		String buttonName = button.getName();

		if (buttonName.equals(TypeOfUser.DEFAULT_USER.name())) {
		    return TypeOfUser.DEFAULT_USER;
		} else if (buttonName.equals(TypeOfUser.ASSISTED_PERSON.name())) {
		    return TypeOfUser.ASSISTED_PERSON;
		} else {
		    return TypeOfUser.CAREGIVER;
		}
	    }

	}

	return TypeOfUser.DEFAULT_USER;
    }

    private void initGUI() {
	setTitle(Messages.getString("SensorSimulatorGUI.universAAL-SensorSimulator")); //$NON-NLS-1$
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 829, 573);
	contentPane = new JPanel();
	setContentPane(contentPane);
	GridBagLayout gbl_contentPane = new GridBagLayout();
	gbl_contentPane.columnWidths = new int[] { 797, 0 };
	gbl_contentPane.rowHeights = new int[] { 700, 0 };
	gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
	gbl_contentPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
	contentPane.setLayout(gbl_contentPane);

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
	gbc_tabbedPane.fill = GridBagConstraints.BOTH;
	gbc_tabbedPane.gridx = 0;
	gbc_tabbedPane.gridy = 0;
	contentPane.add(tabbedPane, gbc_tabbedPane);

	JPanel panel_2 = new JPanel();
	tabbedPane.addTab(Messages.getString("SensorSimulatorGUI.LightingSensors"), null, panel_2, null); //$NON-NLS-1$
	panel_2.setLayout(new GridLayout(5, 2, 0, 0));

	final JButton sleepingRoomLightBtn = new JButton(Messages.getString("SensorSimulatorGUI.SleepingRoomLight")); //$NON-NLS-1$

	panel_2.add(sleepingRoomLightBtn);

	final JButton livingRoomLightBtn = new JButton(Messages.getString("SensorSimulatorGUI.LivingRoomLight")); //$NON-NLS-1$

	Image offImage = Toolkit.getDefaultToolkit().createImage(
		this.getClass().getClassLoader().getResource("lightOff.jpg")); //$NON-NLS-1$
	ImageIcon offImageIcon = new ImageIcon(offImage);

	final JLabel lblSleepingRoomLight = new JLabel(offImageIcon);
	sleepingRoomLightBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		LightSource lightSource = lampStatePublisher.getLampMap()
			.getLightSource(
				LampStatePublisher.SLEEPING_ROOM_LIGHTING);

		lampStatePublisher.publishContextEvent(lightSource);

		// lampStatePublisher.changeButtonBackground(sleepingRoomLightBtn,
		// lightSource);

		lampStatePublisher.changeLabelBackground(lblSleepingRoomLight,
			lightSource);

	    }
	});
	panel_2.add(lblSleepingRoomLight);

	panel_2.add(livingRoomLightBtn);

	final JButton bathroomLightBtn = new JButton(Messages.getString("SensorSimulatorGUI.BathroomLight")); //$NON-NLS-1$

	final JLabel lblLivingRoomLight = new JLabel(offImageIcon);
	lblSleepingRoomLight.setBackground(new Color(255, 255, 255));
	lblSleepingRoomLight.setOpaque(true);

	lblLivingRoomLight.setBackground(new Color(255, 255, 255));
	lblLivingRoomLight.setOpaque(true);

	livingRoomLightBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		LightSource lightSource = lampStatePublisher
			.getLampMap()
			.getLightSource(LampStatePublisher.LIVING_ROOM_LIGHTING);

		lampStatePublisher.publishContextEvent(lightSource);

		// lampStatePublisher.changeButtonBackground(livingRoomLightBtn,
		// lightSource);

		lampStatePublisher.changeLabelBackground(lblLivingRoomLight,
			lightSource);

	    }
	});

	panel_2.add(lblLivingRoomLight);

	panel_2.add(bathroomLightBtn);

	final JLabel lblBathroomLight = new JLabel(offImageIcon);
	lblBathroomLight.setBackground(new Color(255, 255, 255));
	lblBathroomLight.setOpaque(true);

	bathroomLightBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		LightSource lightSource = lampStatePublisher.getLampMap()
			.getLightSource(LampStatePublisher.BATHROOM_LIGHTING);

		lampStatePublisher.publishContextEvent(lightSource);

		lampStatePublisher.changeLabelBackground(lblBathroomLight,
			lightSource);

	    }
	});
	panel_2.add(lblBathroomLight);

	final JButton kitchenLightBtn = new JButton(Messages.getString("SensorSimulatorGUI.KitchenLight")); //$NON-NLS-1$
	panel_2.add(kitchenLightBtn);

	final JButton hobbyRoomLightBtn = new JButton(Messages.getString("SensorSimulatorGUI.HobbyRoomLight")); //$NON-NLS-1$

	final JLabel lblKitchenLight = new JLabel(offImageIcon);
	lblKitchenLight.setBackground(new Color(255, 255, 255));
	lblKitchenLight.setOpaque(true);

	kitchenLightBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		LightSource lightSource = lampStatePublisher.getLampMap()
			.getLightSource(LampStatePublisher.KITCHEN_LIGHTING);

		lampStatePublisher.publishContextEvent(lightSource);

		lampStatePublisher.changeLabelBackground(lblKitchenLight,
			lightSource);
	    }
	});
	panel_2.add(lblKitchenLight);
	panel_2.add(hobbyRoomLightBtn);

	final JLabel lblHobbyRoomLight = new JLabel(offImageIcon);
	lblHobbyRoomLight.setBackground(new Color(255, 255, 255));
	lblHobbyRoomLight.setOpaque(true);

	hobbyRoomLightBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		LightSource lightSource = lampStatePublisher.getLampMap()
			.getLightSource(LampStatePublisher.HOBBY_ROOM_LIGHTING);

		lampStatePublisher.publishContextEvent(lightSource);

		lampStatePublisher.changeLabelBackground(lblHobbyRoomLight,
			lightSource);
	    }
	});

	panel_2.add(lblHobbyRoomLight);

	JPanel panel_5 = new JPanel();
	tabbedPane.addTab(Messages.getString("SensorSimulatorGUI.TemperatureSensors"), null, panel_5, null); //$NON-NLS-1$
	panel_5.setLayout(new GridLayout(5, 1, 0, 0));

	JPanel panel_10 = new JPanel();
	panel_10.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.SleepingRoomSensor"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_5.add(panel_10);

	JPanel panel_9 = new JPanel();
	panel_10.add(panel_9);
	panel_9.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

	final JSlider sliderTemp1 = new JSlider();
	sliderTemp1.setValue(24);

	sliderTemp1.setMaximum(55);
	sliderTemp1.setMinimum(-20);

	panel_9.add(sliderTemp1);

	final JLabel labelTemp1 = new JLabel(Messages.getString("SensorSimulatorGUI.temperature")); //$NON-NLS-1$
	labelTemp1.setHorizontalAlignment(SwingConstants.CENTER);
	labelTemp1.setText(Integer.toString(sliderTemp1.getValue()));

	panel_9.add(labelTemp1);

	// TEEEEEEST

	sliderTemp1.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelTemp1.setText(Integer.toString(sliderTemp1.getValue()));
	    }
	});

	JButton sendTemp1Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$
	sendTemp1Btn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		TemperatureSensor temperatureSensor = new TemperatureSensor(
			TemperatureSensor.MY_URI + Messages.getString("SensorSimulatorGUI.TemperatureSensor1")); //$NON-NLS-1$

		temperatureSensor.setValue((float) sliderTemp1.getValue());

		roomTemperaturePublisher.publishContextEvent(temperatureSensor);
	    }
	});
	panel_9.add(sendTemp1Btn);

	JPanel panel_11 = new JPanel();
	panel_11.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.LivingRoomSensor"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_5.add(panel_11);

	JPanel panel_12 = new JPanel();
	panel_11.add(panel_12);
	panel_12.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

	final JSlider sliderTemp2 = new JSlider();

	sliderTemp2.setToolTipText(""); //$NON-NLS-1$
	sliderTemp2.setValue(24);
	sliderTemp2.setMinimum(-20);
	sliderTemp2.setMaximum(55);
	panel_12.add(sliderTemp2);

	final JLabel labelTemp2 = new JLabel(Messages.getString("SensorSimulatorGUI.temperature")); //$NON-NLS-1$
	labelTemp2.setHorizontalAlignment(SwingConstants.CENTER);
	labelTemp2.setText(Integer.toString(sliderTemp2.getValue()));

	panel_12.add(labelTemp2);

	sliderTemp2.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelTemp2.setText(Integer.toString(sliderTemp2.getValue()));
	    }
	});

	JButton sendTemp2Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$
	sendTemp2Btn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		TemperatureSensor temperatureSensor = new TemperatureSensor(
			TemperatureSensor.MY_URI + Messages.getString("SensorSimulatorGUI.TemperatureSensor2")); //$NON-NLS-1$

		temperatureSensor.setValue((float) sliderTemp2.getValue());

		roomTemperaturePublisher.publishContextEvent(temperatureSensor);
	    }
	});
	panel_12.add(sendTemp2Btn);

	JPanel panel_13 = new JPanel();
	panel_13.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.BathroomSensor"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_5.add(panel_13);

	JPanel panel_14 = new JPanel();
	panel_13.add(panel_14);
	panel_14.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

	final JSlider sliderTemp3 = new JSlider();

	sliderTemp3.setValue(24);
	sliderTemp3.setToolTipText(""); //$NON-NLS-1$
	sliderTemp3.setMinimum(-20);
	sliderTemp3.setMaximum(55);
	panel_14.add(sliderTemp3);

	final JLabel labelTemp3 = new JLabel(Integer.toString(sliderTemp3
		.getValue()));
	labelTemp3.setHorizontalAlignment(SwingConstants.CENTER);
	panel_14.add(labelTemp3);

	sliderTemp3.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelTemp3.setText(Integer.toString(sliderTemp3.getValue()));
	    }
	});

	JButton sendTemp3Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$
	sendTemp3Btn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		TemperatureSensor temperatureSensor = new TemperatureSensor(
			TemperatureSensor.MY_URI + Messages.getString("SensorSimulatorGUI.TemperatureSensor3")); //$NON-NLS-1$

		temperatureSensor.setValue((float) sliderTemp3.getValue());

		roomTemperaturePublisher.publishContextEvent(temperatureSensor);
	    }
	});
	panel_14.add(sendTemp3Btn);

	JPanel panel_15 = new JPanel();
	panel_15.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.KitchenSensor"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_5.add(panel_15);

	JPanel panel_16 = new JPanel();
	panel_15.add(panel_16);
	panel_16.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

	final JSlider sliderTemp4 = new JSlider();
	sliderTemp4.setValue(24);
	sliderTemp4.setToolTipText(""); //$NON-NLS-1$
	sliderTemp4.setMinimum(-20);
	sliderTemp4.setMaximum(55);
	panel_16.add(sliderTemp4);

	final JLabel labelTemp4 = new JLabel(Messages.getString("SensorSimulatorGUI.24")); //$NON-NLS-1$
	labelTemp4.setHorizontalAlignment(SwingConstants.CENTER);
	panel_16.add(labelTemp4);

	sliderTemp4.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelTemp4.setText(Integer.toString(sliderTemp4.getValue()));
	    }
	});

	JButton sendTemp4Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$
	sendTemp4Btn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		TemperatureSensor temperatureSensor = new TemperatureSensor(
			TemperatureSensor.MY_URI + Messages.getString("SensorSimulatorGUI.TemperatureSensor4")); //$NON-NLS-1$

		temperatureSensor.setValue((float) sliderTemp4.getValue());

		roomTemperaturePublisher.publishContextEvent(temperatureSensor);
	    }
	});
	panel_16.add(sendTemp4Btn);

	JPanel panel_17 = new JPanel();
	panel_17.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.HobbyRoomSensor"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_5.add(panel_17);

	JPanel panel_18 = new JPanel();
	panel_17.add(panel_18);
	panel_18.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

	final JSlider sliderTemp5 = new JSlider();
	sliderTemp5.setValue(24);
	sliderTemp5.setToolTipText(""); //$NON-NLS-1$
	sliderTemp5.setMinimum(-20);
	sliderTemp5.setMaximum(55);
	panel_18.add(sliderTemp5);

	final JLabel labelTemp5 = new JLabel(Messages.getString("SensorSimulatorGUI.24")); //$NON-NLS-1$
	labelTemp5.setHorizontalAlignment(SwingConstants.CENTER);
	panel_18.add(labelTemp5);

	sliderTemp5.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelTemp5.setText(Integer.toString(sliderTemp5.getValue()));
	    }
	});

	JButton sendTemp5Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$
	sendTemp5Btn.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		TemperatureSensor temperatureSensor = new TemperatureSensor(
			TemperatureSensor.MY_URI + Messages.getString("SensorSimulatorGUI.TemperatureSensor5")); //$NON-NLS-1$

		temperatureSensor.setValue((float) sliderTemp5.getValue());

		roomTemperaturePublisher.publishContextEvent(temperatureSensor);
	    }
	});
	panel_18.add(sendTemp5Btn);

	JPanel panel_8 = new JPanel();
	tabbedPane.addTab(Messages.getString("SensorSimulatorGUI.HumiditySensors"), null, panel_8, null); //$NON-NLS-1$
	panel_8.setBorder(null);
	panel_8.setLayout(new GridLayout(0, 1, 0, 0));

	JPanel panel_7 = new JPanel();
	panel_7.setBorder(new TitledBorder(UIManager
		.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.HumiditySensor1"), //$NON-NLS-1$ //$NON-NLS-2$
		TitledBorder.LEADING, TitledBorder.TOP, null,
		new Color(0, 0, 0)));
	panel_8.add(panel_7);

	JPanel panel_6 = new JPanel();
	panel_7.add(panel_6);

	final JSlider sliderHumidity1 = new JSlider();
	sliderHumidity1.setValue(50);
	panel_6.add(sliderHumidity1);

	sliderHumidity1.setMaximum(100);
	sliderHumidity1.setMinimum(0);

	final JLabel labelHumidity1 = new JLabel(Integer
		.toString(sliderHumidity1.getValue()));

	sliderHumidity1.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelHumidity1.setText(Integer.toString(sliderHumidity1
			.getValue()));
	    }
	});

	panel_6.add(labelHumidity1);

	JButton sendHumidity1Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$

	sendHumidity1Btn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		HumiditySensor humiditySensor = new HumiditySensor(
			HumiditySensor.MY_URI + Messages.getString("SensorSimulatorGUI.humiditySensor1")); //$NON-NLS-1$

		humiditySensor.setValue((float) sliderHumidity1.getValue());

		roomHumidityPublisher.publishContextEvent(humiditySensor);
	    }
	});

	panel_6.add(sendHumidity1Btn);

	JPanel panel_19 = new JPanel();
	panel_19.setBorder(new TitledBorder(null, Messages.getString("SensorSimulatorGUI.HumiditySensor2"), //$NON-NLS-1$
		TitledBorder.LEADING, TitledBorder.TOP, null, null));
	panel_8.add(panel_19);

	JPanel panel_20 = new JPanel();
	panel_19.add(panel_20);

	final JSlider sliderHumidity2 = new JSlider();
	sliderHumidity2.setValue(50);
	panel_20.add(sliderHumidity2);

	sliderHumidity2.setMaximum(100);
	sliderHumidity2.setMinimum(0);

	final JLabel labelHumidity2 = new JLabel(Integer
		.toString(sliderHumidity2.getValue()));

	sliderHumidity2.addChangeListener(new ChangeListener() {
	    public void stateChanged(ChangeEvent e) {
		labelHumidity2.setText(Integer.toString(sliderHumidity2
			.getValue()));
	    }
	});

	panel_20.add(labelHumidity2);

	JButton sendHumidity2Btn = new JButton(Messages.getString("SensorSimulatorGUI.Send")); //$NON-NLS-1$

	sendHumidity2Btn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		HumiditySensor humiditySensor = new HumiditySensor(
			HumiditySensor.MY_URI + Messages.getString("SensorSimulatorGUI.humiditySensor2")); //$NON-NLS-1$

		humiditySensor.setValue((float) sliderHumidity2.getValue());

		roomHumidityPublisher.publishContextEvent(humiditySensor);
	    }
	});

	panel_20.add(sendHumidity2Btn);

	JPanel panel = new JPanel();
	tabbedPane.addTab(Messages.getString("SensorSimulatorGUI.LocationSensors"), null, panel, null); //$NON-NLS-1$
	panel.setLayout(new GridLayout(3, 1, 0, 0));

	JPanel panel_1 = new JPanel();
	panel.add(panel_1);
	GridBagLayout gbl_panel_1 = new GridBagLayout();
	gbl_panel_1.columnWidths = new int[] { 31, 29, 254, 279, 0 };
	gbl_panel_1.rowHeights = new int[] { 20, 0, 0, 0, 0 };
	gbl_panel_1.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
		Double.MIN_VALUE };
	gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
		Double.MIN_VALUE };
	panel_1.setLayout(gbl_panel_1);

	JRadioButton rdbtnDefaultUser = new JRadioButton(Messages.getString("SensorSimulatorGUI.DefaultUser")); //$NON-NLS-1$
	rdbtnDefaultUser.setName(TypeOfUser.DEFAULT_USER.name());
	System.out.println(rdbtnDefaultUser.getName()
		+ "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //$NON-NLS-1$
	buttonGroup.add(rdbtnDefaultUser);
	rdbtnDefaultUser.setSelected(true);
	GridBagConstraints gbc_rdbtnDefaultUser = new GridBagConstraints();
	gbc_rdbtnDefaultUser.anchor = GridBagConstraints.WEST;
	gbc_rdbtnDefaultUser.insets = new Insets(0, 0, 5, 0);
	gbc_rdbtnDefaultUser.gridx = 3;
	gbc_rdbtnDefaultUser.gridy = 0;
	panel_1.add(rdbtnDefaultUser, gbc_rdbtnDefaultUser);

	JLabel lbljustName = new JLabel(Messages.getString("SensorSimulatorGUI.UserName")); //$NON-NLS-1$
	GridBagConstraints gbc_lbljustName = new GridBagConstraints();
	gbc_lbljustName.fill = GridBagConstraints.VERTICAL;
	gbc_lbljustName.insets = new Insets(0, 0, 5, 5);
	gbc_lbljustName.gridx = 1;
	gbc_lbljustName.gridy = 1;
	panel_1.add(lbljustName, gbc_lbljustName);

	final JFormattedTextField userNameTextField = new JFormattedTextField();

	GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
	gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
	gbc_formattedTextField.fill = GridBagConstraints.BOTH;
	gbc_formattedTextField.gridx = 2;
	gbc_formattedTextField.gridy = 1;
	panel_1.add(userNameTextField, gbc_formattedTextField);

	JRadioButton rdbtnAssistedPerson = new JRadioButton(Messages.getString("SensorSimulatorGUI.AssistedPerson")); //$NON-NLS-1$
	rdbtnAssistedPerson.setName(TypeOfUser.ASSISTED_PERSON.name());
	buttonGroup.add(rdbtnAssistedPerson);
	GridBagConstraints gbc_rdbtnAssistedPerson = new GridBagConstraints();
	gbc_rdbtnAssistedPerson.anchor = GridBagConstraints.WEST;
	gbc_rdbtnAssistedPerson.insets = new Insets(0, 0, 5, 0);
	gbc_rdbtnAssistedPerson.gridx = 3;
	gbc_rdbtnAssistedPerson.gridy = 1;
	panel_1.add(rdbtnAssistedPerson, gbc_rdbtnAssistedPerson);

	JRadioButton rdbtnCaregiver = new JRadioButton(Messages.getString("SensorSimulatorGUI.Caregiver")); //$NON-NLS-1$
	rdbtnCaregiver.setName(TypeOfUser.CAREGIVER.name());
	buttonGroup.add(rdbtnCaregiver);
	GridBagConstraints gbc_rdbtnCaregiver = new GridBagConstraints();
	gbc_rdbtnCaregiver.insets = new Insets(0, 0, 5, 0);
	gbc_rdbtnCaregiver.anchor = GridBagConstraints.WEST;
	gbc_rdbtnCaregiver.gridx = 3;
	gbc_rdbtnCaregiver.gridy = 2;
	panel_1.add(rdbtnCaregiver, gbc_rdbtnCaregiver);

	final JLabel labelCreatedUser = new JLabel("New label"); //$NON-NLS-1$

	labelCreatedUser.setText(Messages.getString("SensorSimulatorGUI.Userfollowinguri") //$NON-NLS-1$
		+ USER_NAMESPACE);
	GridBagConstraints gbc_labelCreatedUser = new GridBagConstraints();
	gbc_labelCreatedUser.gridwidth = 2;
	gbc_labelCreatedUser.insets = new Insets(0, 0, 0, 5);
	gbc_labelCreatedUser.gridx = 2;
	gbc_labelCreatedUser.gridy = 3;
	panel_1.add(labelCreatedUser, gbc_labelCreatedUser);

	userNameTextField.addKeyListener(new KeyListener() {

	    public void keyTyped(KeyEvent e) {

	    }

	    public void keyReleased(KeyEvent e) {
		labelCreatedUser
			.setText(Messages.getString("SensorSimulatorGUI.Userfollowinguri") //$NON-NLS-1$
				+ USER_NAMESPACE + userNameTextField.getText());

	    }

	    public void keyPressed(KeyEvent e) {

	    }
	});

	JPanel panel_3 = new JPanel();
	panel_3.setToolTipText(""); //$NON-NLS-1$
	panel_3.setBorder(new TitledBorder(UIManager

	.getBorder("TitledBorder.border"), Messages.getString("SensorSimulatorGUI.Location"), //$NON-NLS-1$ //$NON-NLS-2$

	TitledBorder.LEADING, TitledBorder.TOP, null,

	new Color(0, 0, 0)));
	panel.add(panel_3);

	GridBagLayout gbl_panel_3 = new GridBagLayout();
	gbl_panel_3.columnWidths = new int[] { 398, 398, 0 };
	gbl_panel_3.rowHeights = new int[] { 29, 116, 0 };
	gbl_panel_3.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	panel_3.setLayout(gbl_panel_3);

	JLabel lblNewLabel = new JLabel(
		Messages.getString("SensorSimulatorGUI.Selectpredefinedlocation")); //$NON-NLS-1$
	lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
	gbc_lblNewLabel.gridwidth = 2;
	gbc_lblNewLabel.anchor = GridBagConstraints.SOUTH;
	gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
	gbc_lblNewLabel.gridx = 0;
	gbc_lblNewLabel.gridy = 0;
	panel_3.add(lblNewLabel, gbc_lblNewLabel);

	JPanel panel_4 = new JPanel();
	GridBagConstraints gbc_panel_4 = new GridBagConstraints();
	gbc_panel_4.fill = GridBagConstraints.BOTH;
	gbc_panel_4.insets = new Insets(0, 0, 0, 5);
	gbc_panel_4.gridx = 0;
	gbc_panel_4.gridy = 1;
	panel_3.add(panel_4, gbc_panel_4);
	panel_4.setLayout(new GridLayout(0, 1, 0, 0));

	JButton button = new JButton(Messages.getString("SensorSimulatorGUI.SleepingRoom")); //$NON-NLS-1$
	button.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE

		+ SLEEPING_ROOM);
	    }
	});
	panel_4.add(button);

	JButton button_1 = new JButton(Messages.getString("SensorSimulatorGUI.LivingRoom")); //$NON-NLS-1$
	button_1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE
			+ LIVING_ROOM);
	    }
	});
	panel_4.add(button_1);

	JPanel panel_21 = new JPanel();
	GridBagConstraints gbc_panel_21 = new GridBagConstraints();
	gbc_panel_21.fill = GridBagConstraints.BOTH;
	gbc_panel_21.gridx = 1;
	gbc_panel_21.gridy = 1;
	panel_3.add(panel_21, gbc_panel_21);
	panel_21.setLayout(new GridLayout(3, 1, 0, 0));

	JButton button_2 = new JButton(Messages.getString("SensorSimulatorGUI.Bathroom")); //$NON-NLS-1$
	button_2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE
			+ BATHROOM);
	    }
	});
	panel_21.add(button_2);

	JButton button_3 = new JButton(Messages.getString("SensorSimulatorGUI.Kitchen")); //$NON-NLS-1$
	button_3.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE
			+ KITCHEN);
	    }
	});
	panel_21.add(button_3);

	JButton button_4 = new JButton(Messages.getString("SensorSimulatorGUI.HobbyRoom")); //$NON-NLS-1$
	button_4.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE
			+ HOBBY_ROOM);
	    }
	});
	panel_21.add(button_4);

	JPanel panel_22 = new JPanel();
	panel.add(panel_22);
	GridBagLayout gbl_panel_22 = new GridBagLayout();
	gbl_panel_22.columnWidths = new int[] { 114, 527, 0, 0 };
	gbl_panel_22.rowHeights = new int[] { 42, 54, 0 };
	gbl_panel_22.columnWeights = new double[] { 0.0, 0.0, 0.0,
		Double.MIN_VALUE };
	gbl_panel_22.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	panel_22.setLayout(gbl_panel_22);

	JLabel lblUserLocation = new JLabel(Messages.getString("SensorSimulatorGUI.UserLocation")); //$NON-NLS-1$
	lblUserLocation.setHorizontalAlignment(SwingConstants.CENTER);
	GridBagConstraints gbc_lblUserLocation = new GridBagConstraints();
	gbc_lblUserLocation.fill = GridBagConstraints.BOTH;
	gbc_lblUserLocation.insets = new Insets(0, 0, 5, 5);
	gbc_lblUserLocation.gridx = 0;
	gbc_lblUserLocation.gridy = 0;
	panel_22.add(lblUserLocation, gbc_lblUserLocation);

	textFieldUserLocation = new JTextField();

	GridBagConstraints gbc_textFieldUserLocation = new GridBagConstraints();
	gbc_textFieldUserLocation.fill = GridBagConstraints.BOTH;
	gbc_textFieldUserLocation.insets = new Insets(0, 0, 5, 5);
	gbc_textFieldUserLocation.gridx = 1;
	gbc_textFieldUserLocation.gridy = 0;
	panel_22.add(textFieldUserLocation, gbc_textFieldUserLocation);
	textFieldUserLocation.setColumns(10);

	final JButton btnSendLocation = new JButton(Messages.getString("SensorSimulatorGUI.SendLocation")); //$NON-NLS-1$
	btnSendLocation.setEnabled(false);

	GridBagConstraints gbc_btnSendLocation = new GridBagConstraints();
	gbc_btnSendLocation.insets = new Insets(0, 0, 5, 0);
	gbc_btnSendLocation.fill = GridBagConstraints.BOTH;
	gbc_btnSendLocation.gridx = 2;
	gbc_btnSendLocation.gridy = 0;
	panel_22.add(btnSendLocation, gbc_btnSendLocation);

	final JLabel lblLocationUri = new JLabel(Messages.getString("SensorSimulatorGUI.Locationuri")); //$NON-NLS-1$

	textFieldUserLocation.addKeyListener(new KeyListener() {

	    public void keyTyped(KeyEvent e) {

	    }

	    public void keyReleased(KeyEvent e) {
		lblLocationUri.setText(Messages.getString("SensorSimulatorGUI.Locationuri") + LOCATION_NAMESPACE //$NON-NLS-1$
			+ textFieldUserLocation.getText());

		if (textFieldUserLocation.getText().length() > 0) {
		    btnSendLocation.setEnabled(true);
		} else {
		    btnSendLocation.setEnabled(false);
		}

	    }

	    public void keyPressed(KeyEvent e) {

	    }
	});

	btnSendLocation.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		lp.publishLocation(getSelectedUserType(), USER_NAMESPACE
			+ userNameTextField.getText(), LOCATION_NAMESPACE
			+ textFieldUserLocation.getText());
	    }

	});

	lblLocationUri.setHorizontalAlignment(SwingConstants.CENTER);
	GridBagConstraints gbc_lblLocationUri = new GridBagConstraints();
	gbc_lblLocationUri.gridwidth = 3;
	gbc_lblLocationUri.insets = new Insets(0, 0, 0, 5);
	gbc_lblLocationUri.fill = GridBagConstraints.BOTH;
	gbc_lblLocationUri.gridx = 0;
	gbc_lblLocationUri.gridy = 1;
	panel_22.add(lblLocationUri, gbc_lblLocationUri);

    }
}
