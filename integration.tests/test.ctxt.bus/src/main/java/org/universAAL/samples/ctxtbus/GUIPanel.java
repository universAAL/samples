/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
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
package org.universAAL.samples.ctxtbus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;

import org.universAAL.ontology.device.BlindController;
import org.universAAL.ontology.device.DimmerController;
import org.universAAL.ontology.device.LightController;
import org.universAAL.ontology.device.PanicButtonSensor;
import org.universAAL.ontology.device.StatusValue;
import org.universAAL.ontology.device.TemperatureSensor;
import org.universAAL.ontology.device.WindowController;
import org.universAAL.ontology.furniture.Furniture;
import org.universAAL.ontology.furniture.FurnitureType;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.location.address.PhysicalAddress;
import org.universAAL.ontology.phThing.PhysicalThing;
import org.universAAL.ontology.profile.Profilable;
import org.universAAL.ontology.profile.User;
import org.universAAL.ontology.profile.UserProfile;

public class GUIPanel extends javax.swing.JFrame {

    // Variables declaration - do not modify
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel labelMain;
    private javax.swing.JButton button1p1;
    private javax.swing.JButton button2p1;
    private javax.swing.JPanel panel1;
    private javax.swing.JTextField text1p1;
    private javax.swing.JLabel label1p1;
    private javax.swing.JButton button1p2;
    private javax.swing.JButton button2p2;
    private javax.swing.JButton button3p2;
    private javax.swing.JPanel panel2;
    private javax.swing.JTextField text1p2;
    private javax.swing.JTextField text2p2;
    private javax.swing.JLabel label1p2;
    private javax.swing.JLabel label2p2;
    private javax.swing.JComboBox combo1p2;
    private javax.swing.JTextArea area1p2;
    private javax.swing.JPanel panel3;
    private javax.swing.JLabel label1p3;
    private javax.swing.JButton button1p3;
    private javax.swing.JPanel panel4;
    private javax.swing.JLabel label1p4;
    private javax.swing.JButton button1p4;
    private javax.swing.JButton button2p4;
    private javax.swing.JPanel panel5;
    private javax.swing.JComboBox combo1p5;
    private javax.swing.JButton button1p5;
    private javax.swing.JTextField text1p5;
    private javax.swing.JTextField text2p5;
    private javax.swing.JTextField text3p5;
    private javax.swing.JLabel label1p5;
    private javax.swing.JLabel label2p5;
    private javax.swing.JPanel panel6;
    private javax.swing.JLabel label1p6;
    private javax.swing.JButton button1p6;
    private javax.swing.JPanel panel7;
    private javax.swing.JComboBox combo1p7;
    private javax.swing.JComboBox combo2p7;
    private javax.swing.JComboBox combo3p7;
    private javax.swing.JTextField text1p7;
    private javax.swing.JLabel label1p7;
    private javax.swing.JButton button1p7;
    private int eventsReceived=0;
    private long starttime=0;
    
    // End of variables declaration

    public GUIPanel() {
	try {
	    UIManager
		    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch (Exception e) {
	    System.err.println("Windows Look&Feel Not Found !");
	}
	initComponents();
    }

    private void initComponents() {
	tabbedPane = new javax.swing.JTabbedPane();
	labelMain = new javax.swing.JLabel();

	panel1 = new javax.swing.JPanel();
	text1p1 = new javax.swing.JTextField();
	label1p1 = new javax.swing.JLabel();
	button1p1 = new javax.swing.JButton();
	button2p1 = new javax.swing.JButton();

	panel2 = new javax.swing.JPanel();
	text1p2 = new javax.swing.JTextField();
	text2p2 = new javax.swing.JTextField();
	label1p2 = new javax.swing.JLabel();
	label2p2 = new javax.swing.JLabel();
	button1p2 = new javax.swing.JButton();
	button2p2 = new javax.swing.JButton();
	button3p2 = new javax.swing.JButton();
	area1p2 = new javax.swing.JTextArea();
	combo1p2 = new javax.swing.JComboBox(new Object[] { "User",
		"Bilnd", "Chair", "Light", "Socket", "Temperature",
		"Window", "Panic" });

	panel3 = new javax.swing.JPanel();
	label1p3 = new javax.swing.JLabel();
	button1p3 = new javax.swing.JButton();

	panel4 = new javax.swing.JPanel();
	label1p4 = new javax.swing.JLabel();
	button1p4 = new javax.swing.JButton();
	button2p4 = new javax.swing.JButton();
	
	panel5 = new javax.swing.JPanel();
	combo1p5 = new javax.swing.JComboBox(new Object[] {
		"Get (user)",
		"Add (user)",
		"Change (user)", 
		"Remove (user)",
		"Get (profile)",
		"Add (profile)",
		"Change (profile)", 
		"Remove (profile)",
		"Get (subprofile)",
		"Add (subprofile)",
		"Change (subprofile)", 
		"Remove (subprofile)",
		"Get users", 
		"Get profile of (user)",
		"Get subprofiles of (user)",
		"Get subprofiles of (profile)",
		"Add to (user) a (profile)",
		"Add to (user) a (subprofile)",
		"Add to (profile) a (subprofile)",
		"Get one subprofile from (user)"});
	button1p5 = new javax.swing.JButton();
	text1p5 = new javax.swing.JTextField();
	text2p5 = new javax.swing.JTextField();
	label1p5 = new javax.swing.JLabel();
	label2p5 = new javax.swing.JLabel();
	text3p5 = new javax.swing.JTextField();
	
	panel6 = new javax.swing.JPanel();
	label1p6 = new javax.swing.JLabel();
	button1p6 = new javax.swing.JButton();
	
	panel7 = new javax.swing.JPanel();
	combo1p7 = new javax.swing.JComboBox(new Object[] { "Get", "Add",
		"Change", "Remove" });
	combo2p7 = new javax.swing.JComboBox(new Object[] { "AALSpace",
		"AALSpaceProfile", "AALService", "AALServiceProfile", "Device",
		"Ontology", "HRProfile", "HWProfile", "AppProfile" });
	combo3p7 = new javax.swing.JComboBox(new Object[] { "Specific",
		"All of them", "Of/To an AALSpace", "Of/To an AALService" });
	text1p7 = new javax.swing.JTextField();
	label1p7 = new javax.swing.JLabel();
	button1p7 = new javax.swing.JButton();

	// WINDOW
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	this.setBounds(800, 660, 420, 330);
	this.setMaximumSize(new Dimension(420, 330));
	this.setMinimumSize(new Dimension(420, 330));
	this.setPreferredSize(new Dimension(420, 330));
	setResizable(true);
	getContentPane().setLayout(null);

	// TAB 1
	panel1.setLayout(null);

	text1p1.setText("Size of burst");
	panel1.add(text1p1);
	text1p1.setBounds(20, 50, 210, 23);

	label1p1.setText("Delay");
	panel1.add(label1p1);
	label1p1.setBounds(20, 75, 210, 23);

	button1p1.setText("Publish");
	button1p1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendButton1ActionPerformed(evt);
	    }
	});
	panel1.add(button1p1);
	button1p1.setBounds(20, 125, 75, 29);

	button2p1.setText("Publish (unique)");
	button2p1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendButton2ActionPerformed(evt);
	    }
	});
	panel1.add(button2p1);
	button2p1.setBounds(20, 175, 75, 29);

	tabbedPane.addTab("Context", panel1);

	// TAB 2
	panel2.setLayout(null);

	combo1p2.setEditable(false);
	panel2.add(combo1p2);
	combo1p2.setBounds(20, 25, 210, 23);

	text1p2.setText("From Timestamp (UNIX ms)");
	panel2.add(text1p2);
	text1p2.setBounds(20, 50, 210, 23);

	text2p2.setText("To Timestamp (UNIX ms)");
	panel2.add(text2p2);
	text2p2.setBounds(20, 75, 210, 23);

	button1p2.setText("GET");
	button1p2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		cheButton1ActionPerformed(evt);
	    }
	});
	panel2.add(button1p2);
	button1p2.setBounds(250, 25, 75, 29);

	area1p2.setText("SPARQL Query");
	area1p2.setLineWrap(false);
	panel2.add(area1p2);
	area1p2.setBounds(20, 100, 210, 75);

	button2p2.setText("SPARQL");
	button2p2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		cheButton2ActionPerformed(evt);
	    }
	});
	panel2.add(button2p2);
	button2p2.setBounds(250, 100, 75, 29);

	button3p2.setText("SPARQL(e)");
	button3p2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		cheButton3ActionPerformed(evt);
	    }
	});
	panel2.add(button3p2);
	button3p2.setBounds(250, 150, 75, 29);

	label1p2.setText("Returned events: ");
	panel2.add(label1p2);
	label1p2.setBounds(20, 200, 210, 23);

	label2p2.setText("Delay: ");
	panel2.add(label2p2);
	label2p2.setBounds(250, 200, 150, 23);

	tabbedPane.addTab("CHE", panel2);

	// TAB 3
	panel3.setLayout(null);

	label1p3.setText("Nothing received");
	panel3.add(label1p3);
	label1p3.setBounds(20, 50, 210, 23);

	button1p3.setText("Publish");
	button1p3.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		srButton1ActionPerformed(evt);
	    }
	});
	panel3.add(button1p3);
	button1p3.setBounds(20, 100, 75, 29);

	tabbedPane.addTab("Reasoner", panel3);

	// TAB 4
	panel4.setLayout(null);

	label1p4.setText("Nothing received");
	panel4.add(label1p4);
	label1p4.setBounds(20, 50, 210, 23);

	button1p4.setText("Publish Data");
	button1p4.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		dataButton1ActionPerformed(evt);
	    }
	});
	panel4.add(button1p4);
	button1p4.setBounds(20, 100, 75, 29);

	button2p4.setText("Request Data");
	button2p4.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		dataButton2ActionPerformed(evt);
	    }
	});
	panel4.add(button2p4);
	button2p4.setBounds(20, 130, 75, 29);

	tabbedPane.addTab("Data", panel4);
	
	// TAB 5
	panel5.setLayout(null);
	
	combo1p5.setEditable(false);
	panel5.add(combo1p5);
	combo1p5.setBounds(20, 25, 210, 23);

	text1p5.setText(CPublisher.URIROOT+"argument1");
	panel5.add(text1p5);
	text1p5.setBounds(20, 50, 250, 23);

	text2p5.setText(CPublisher.URIROOT+"argument2");
	panel5.add(text2p5);
	text2p5.setBounds(20, 75, 250, 23);
	
	text3p5.setText("Repeats");
	panel5.add(text3p5);
	text3p5.setBounds(20, 100, 100, 23);

	button1p5.setText("Call");
	button1p5.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		profileButton1ActionPerformed(evt);
	    }
	});
	panel5.add(button1p5);
	button1p5.setBounds(20, 130, 75, 29);
	
	label1p5.setText("Nothing received");
	panel5.add(label1p5);
	label1p5.setBounds(20, 155, 400, 29);
	
	label2p5.setText("Delay: ");
	panel5.add(label2p5);
	label2p5.setBounds(20, 180, 400, 29);

	tabbedPane.addTab("Profile", panel5);

	// TAB 6
	panel6.setLayout(null);

	label1p6.setText("Not Enabled: Nothing received");
	panel6.add(label1p6);
	label1p6.setBounds(20, 50, 210, 23);

	button1p6.setText("Enable");
	button1p6.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		subscribeButton1ActionPerformed(evt);
	    }
	});
	panel6.add(button1p6);
	button1p6.setBounds(20, 100, 75, 29);

	tabbedPane.addTab("Subscriber", panel6);
	
	// TAB 7
	panel7.setLayout(null);
	
	combo1p7.setEditable(false);
	panel7.add(combo1p7);
	combo1p7.setBounds(20, 25, 210, 23);
	
	combo2p7.setEditable(false);
	panel7.add(combo2p7);
	combo2p7.setBounds(20, 50, 210, 23);
	
	combo3p7.setEditable(false);
	panel7.add(combo3p7);
	combo3p7.setBounds(20, 75, 210, 23);
	
	text1p7.setText(CPublisher.URIROOT+"argument1");
	panel7.add(text1p7);
	text1p7.setBounds(20, 100, 250, 23);

	label1p7.setText("Select a valid combination");
	panel7.add(label1p7);
	label1p7.setBounds(20, 130, 310, 23);

	button1p7.setText("Call");
	button1p7.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		spaceButton1ActionPerformed(evt);
	    }
	});
	panel7.add(button1p7);
	button1p7.setBounds(20, 160, 75, 29);

	tabbedPane.addTab("Space", panel7);
		
	// MAIN
	getContentPane().add(tabbedPane);
	tabbedPane.setBounds(10, 40, 400, 260);
	labelMain.setFont(new java.awt.Font("Verdana", 1, 14));
	labelMain.setText("Test utility");
	getContentPane().add(labelMain);
	labelMain.setBounds(10, 10, 360, 18);

	pack();
    }

    private void sendButton1ActionPerformed(ActionEvent evt) {
	int siz = 0;
	long start=System.currentTimeMillis();
	try {
	    siz = Integer.parseInt(this.text1p1.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of burst");
	    return;
	}
	long del = Activator.cpublisher.sendBurst(siz);
	this.label1p1.setText("Delay: " + del + " ms ("+start+")");
    }

    private void sendButton2ActionPerformed(ActionEvent evt) {
	int siz = 0;
	long start=System.currentTimeMillis();
	try {
	    siz = Integer.parseInt(this.text1p1.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of burst");
	    return;
	}
	long del = Activator.cpublisher.sendUniqueBurst(siz);
	this.label1p1.setText("Delay: " + del + " ms ("+start+")");
    }

    private void cheButton1ActionPerformed(ActionEvent evt) {
	long from = -1, to = -1;
	try {
	    from = Long.parseLong(this.text1p2.getText());
	    to = Long.parseLong(this.text2p2.getText());
	} catch (Exception e) {
	    this.label1p2.setText("Invalid timestamp format");
	    return;
	}
	String type = "?";
	if (from > 0) {
	    if (to > 0) {
		// GET BETWEEN
		type = "-Between-";
	    } else {
		// GET FROM
		type = "-From-";
	    }
	} else {
	    if (to > 0) {
		// GET TO
		type = "-To-";
	    } else {
		// GET ALL
		type = "-All-";
	    }
	}
	long t0 = System.currentTimeMillis();
	this.label1p2.setText("Returned "
		+ type
		+ " events: "
		+ Activator.hcaller.callGetEvents(
			getSample(this.combo1p2.getSelectedIndex()), from, to));

	this.label2p2.setText("Delay: " + (System.currentTimeMillis() - t0)
		+ " ms");
    }

    private void cheButton2ActionPerformed(ActionEvent evt) {
	String text = this.area1p2.getText();
	if (!text.isEmpty()) {
	    long t0 = System.currentTimeMillis();
	    Activator.hcaller.callDoSPARQL(text);
	    this.label1p2.setText("Returned events: N/A");
	    this.label2p2.setText("Delay: " + (System.currentTimeMillis() - t0)
		    + " ms");
	}
    }

    private void cheButton3ActionPerformed(ActionEvent evt) {
	String text = this.area1p2.getText();
	if (!text.isEmpty()) {
	    long t0 = System.currentTimeMillis();
	    this.label1p2.setText("Returned events: "
		    + Activator.hcaller.callGetEventsSPARQL(text));
	    this.label2p2.setText("Delay: " + (System.currentTimeMillis() - t0)
		    + " ms");
	}
    }

    private void srButton1ActionPerformed(ActionEvent evt) {
	this.label1p3.setText("Nothing received");
	Activator.cpublisher.sendSituation();
    }

    public void setSRtextResponse(String text) {
	this.label1p3.setText(text);
    }

    private org.universAAL.ontology.che.ContextEvent getSample(
	    int sample) {
	switch (sample) {
	case 0:
	    // 1 User is awake
	    User u1 = new User(CPublisher.URIROOT + "user1");
	    u1.setProperty(Profilable.PROP_HAS_PROFILE, new UserProfile(CPublisher.URIROOT
		    + "user1Profile"));
	    return new org.universAAL.ontology.che.ContextEvent(u1, Profilable.PROP_HAS_PROFILE);
	case 1:
	    // 2 Blind is open
	    BlindController b2 = new BlindController(CPublisher.URIROOT + "blind4");
	    b2.setProperty(BlindController.PROP_HAS_VALUE, new Integer(100));
	    return new org.universAAL.ontology.che.ContextEvent(b2, BlindController.PROP_HAS_VALUE);
	case 2:
	    // 3 chair is in place
	    Furniture f3 = new Furniture(CPublisher.URIROOT + "furniture5");
	    f3.setFurnitureType(FurnitureType.Chair);
	    f3.setLocation(new Location(CPublisher.URIROOT + "location" + new Integer(6)));
	    return new org.universAAL.ontology.che.ContextEvent(f3, Furniture.PROP_PHYSICAL_LOCATION);
	case 3:
	    // 4 light is on
	    LightController ls4 = new LightController(CPublisher.URIROOT + "light6");
	    ls4.setValue(new Integer(100));
	    return new org.universAAL.ontology.che.ContextEvent(ls4, LightController.PROP_HAS_VALUE);
	case 4:
	    // 7 socket at 50%
	    DimmerController ss5 = new DimmerController(CPublisher.URIROOT + "socket7");
	    ss5.setValue(new Integer(100));
	    return new org.universAAL.ontology.che.ContextEvent(ss5, DimmerController.PROP_HAS_VALUE);
	case 5:
	    // 6 temperature measured
	    TemperatureSensor ts6 = new TemperatureSensor(CPublisher.URIROOT + "tempsensor8");
	    ts6.setValue(30);
	    return new org.universAAL.ontology.che.ContextEvent(ts6, TemperatureSensor.PROP_HAS_VALUE);
	case 6:
	    // 7 window closed
	    WindowController w7 = new WindowController(CPublisher.URIROOT + "window9");
	    w7.setValue(StatusValue.Activated);
	    return new org.universAAL.ontology.che.ContextEvent(w7, WindowController.PROP_HAS_VALUE);
	default:
	    // 10 situation
	    PanicButtonSensor p10 = new PanicButtonSensor(CPublisher.URIROOT + "panic10");
	    p10.setProperty(PhysicalThing.PROP_CARRIED_BY, new User(CPublisher.URIROOT
		    + "user" + new Integer(5)));
	    p10.setProperty(PhysicalThing.PROP_IS_PORTABLE,
		    new Boolean(true));
	    p10.setLocation(new Location(CPublisher.URIROOT + "location"
		    + new Integer(10)));
	    return new org.universAAL.ontology.che.ContextEvent(p10, PhysicalThing.PROP_PHYSICAL_LOCATION);
	}
//	switch (sample) {
//	case 0:
//	    // 1 User is awake
//	    User u1 = new User(CPublisher.URIROOT + "user1");
//	    u1.setProperty(Profilable.PROP_HAS_PROFILE,new UserProfile(CPublisher.URIROOT + "user1Profile"));
//	    return new org.universAAL.ontology.che.ContextEvent(u1,
//		    Profilable.PROP_HAS_PROFILE);
//	    // case 1:
//	    // //2 Building has address
//	    // BuildingPlace bp2=new
//	    // BuildingPlace(CPublisher.URIROOT+"buidlingplace2","Living_Lab");
//	    // PhysicalAddress pa2=new
//	    // PhysicalAddress(CPublisher.URIROOT+"address2", "Paterna",
//	    // "Ronda Auguste y Louis Lumiere", "TSB");
//	    // pa2.setCountryName(new String[]{"Spain"});
//	    // pa2.setPostalCode("46???");
//	    // pa2.setRegion("C.Valenciana");
//	    // bp2.setHasAddress(pa2);
//	    // return new
//	    // org.universAAL.context.che.ontology.ContextEvent(bp2,BuildingPlace.PROP_HAS_ADDRESS);
//	    // case 2:
//	    // //3 Glass broken
//	    // GlassBreakSensor gb3=new
//	    // GlassBreakSensor(CPublisher.URIROOT+"glassbreak3");
//	    // gb3.setMeasuredValue(true);
//	    // return new
//	    // org.universAAL.context.che.ontology.ContextEvent(gb3,GlassBreakSensor.PROP_MEASURED_VALUE);
//	case 3:
//	    // 4 Blind is open
//	    BlindActuator b4 = new BlindActuator(CPublisher.URIROOT + "blind4");
//	    b4.setProperty(BlindActuator.PROP_PHYSICAL_LOCATION, new Integer(100));
//	    return new org.universAAL.ontology.che.ContextEvent(b4,
//		    BlindActuator.PROP_PHYSICAL_LOCATION);
//	case 4:
//	    // 5 chair is in place
//	    Furniture f5 = new Furniture(CPublisher.URIROOT + "furniture5");
//	    f5.setFurnitureType(FurnitureType.Chair);
//	    f5.setLocation(new Location(CPublisher.URIROOT + "location5"));
//	    return new org.universAAL.ontology.che.ContextEvent(f5,
//		    Furniture.PROP_PHYSICAL_LOCATION);
//	case 5:
//	    // 6 light is on
//	    LightSource ls6 = new LightSource(CPublisher.URIROOT + "light6");
//	    ls6.setBrightness(100);
//	    return new org.universAAL.ontology.che.ContextEvent(ls6,
//		    LightSource.PROP_SOURCE_BRIGHTNESS);
//	case 6:
//	    // 7 socket at 50%
//	    Powersocket ss7 = new Powersocket(CPublisher.URIROOT + "socket7");
//	    ss7.setValue(50);
//	    return new org.universAAL.ontology.che.ContextEvent(ss7,
//		    Powersocket.PROP_SOCKET_VALUE);
//	case 7:
//	    // 8 temperature measured
//	    TempSensor ts8 = new TempSensor(CPublisher.URIROOT + "tempsensor8");
//	    ts8.setMeasuredValue(27.5f);
//	    return new org.universAAL.ontology.che.ContextEvent(ts8,
//		    TempSensor.PROP_MEASURED_VALUE);
//	case 8:
//	    // 9 window closed
//	    WindowActuator w9 = new WindowActuator(CPublisher.URIROOT
//		    + "window9");
//	    w9.setProperty(WindowActuator.PROP_WINDOW_STATUS, new Integer(0));
//	    return new org.universAAL.ontology.che.ContextEvent(w9,
//		    WindowActuator.PROP_WINDOW_STATUS);
//	default:
//	    // 10 situation
//	    PanicButton p10 = new PanicButton(CPublisher.URIROOT + "panic10");
//	    p10.setProperty(PhysicalThing.PROP_CARRIED_BY, new User(
//		    CPublisher.URIROOT + "user1"));
//	    p10.setProperty(PhysicalThing.PROP_IS_PORTABLE, new Boolean(true));
//	    p10.setLocation(new Location(CPublisher.URIROOT + "location10"));
//	    return new org.universAAL.ontology.che.ContextEvent(p10,
//		    PhysicalThing.PROP_PHYSICAL_LOCATION);
//	}
    }

    private void dataButton1ActionPerformed(ActionEvent evt) {
	this.label1p4.setText("Nothing received");
	Activator.cpublisher.sendEventWithData();
    }

    private void dataButton2ActionPerformed(ActionEvent evt) {
	String query = "SELECT  ?a "
		+ "WHERE"
		+ "  { ?a  "
		+ "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  "
		+ "<http://ontology.universAAL.org/PhysicalWorld.owl#Address> ."
		+ "  }";
	this.label1p4.setText("Nothing received");
	PhysicalAddress address = (PhysicalAddress) Activator.parser
		.deserialize(Activator.hcaller
			.callDoSPARQL("DESCRIBE <http://ontology.itaca.upv.es/Test.owl#address1>"));
	this.label1p4.setText(address.toReadableString());
    }
    
    private void profileButton1ActionPerformed(ActionEvent evt) {
	String arg1 = this.text1p5.getText();
	String arg2 = this.text2p5.getText();
	Integer repeat;
	try {
	    repeat = Integer.parseInt(this.text3p5.getText());
	} catch (Exception e) {
	    repeat=Integer.valueOf(0);
	}
	long t0 = System.currentTimeMillis();
	this.label1p5.setText(Activator.pcaller.callProfile(
		this.combo1p5.getSelectedIndex(), arg1, arg2, repeat));
	this.label2p5.setText("Delay: " + (System.currentTimeMillis() - t0) + " ms");

    }
    
    private void subscribeButton1ActionPerformed(ActionEvent evt) {
	eventsReceived = 0;
	if (this.button1p6.getText().equals("Enable")) {
	    // Enable and set button to "Disable"
	    this.button1p6.setText("Disable");
	    this.label1p6.setText("Received: "+eventsReceived);
	    Activator.csubscriber2.enable();
	} else {
	    // Disable and set button to "Enable"
	    this.button1p6.setText("Enable");
	    this.label1p6.setText("Not Enabled: Reset");
	    Activator.csubscriber2.disable();
	}
    }
    
    public void subscribeReceived() {
	eventsReceived++;
	if (eventsReceived==1) starttime=System.currentTimeMillis();
	this.label1p6.setText("Received: "+eventsReceived+ " ("+starttime+")");
    }
    
    private void spaceButton1ActionPerformed(ActionEvent evt) {
	int first=0, second=0, third=0;
	switch (combo1p7.getSelectedIndex()) {
	case 0:
	    first=SpaceCaller._GET;
	    break;
	case 1:
	    first=SpaceCaller._ADD;
	    break;
	case 2:
	    first=SpaceCaller._CHANGE;
	    break;
	case 3:
	    first=SpaceCaller._REMOVE;
	    break;
	default:
	    label1p7.setText("You must select a valid operation form the 1st combo!");
	    return;
	}
	switch (combo2p7.getSelectedIndex()) {
	case 0:
	    second=SpaceCaller.__SPACE;
	    break;
	case 1:
	    second=SpaceCaller.__SPACEPROF;
	    break;
	case 2:
	    second=SpaceCaller.__SERV;
	    break;
	case 3:
	    second=SpaceCaller.__SERVPROF;
	    break;
	case 4:
	    second=SpaceCaller.__DEV;
	    break;
	case 5:
	    second=SpaceCaller.__ONT;
	    break;
	case 6:
	    second=SpaceCaller.__HR;
	    break;
	case 7:
	    second=SpaceCaller.__HW;
	    break;
	case 8:
	    second=SpaceCaller.__APP;
	    break;
	default:
	    label1p7.setText("You must select a valid operation form the 2nd combo!");
	    return;
	} 
	switch (combo3p7.getSelectedIndex()) {
	case 0:
	    third=SpaceCaller.___SPEC;
	    break;
	case 1:
	    third=SpaceCaller.___ALL;
	    break;
	case 2:
	    third=SpaceCaller.___OFTOSPACE;
	    break;
	case 3:
	    third=SpaceCaller.___OFTOSERV;
	    break;
	default:
	    label1p7.setText("You must select a valid operation form the 3rd combo!");
	    return;
	}
	String result = Activator.scaller.callSpace(first | second | third,
		this.text1p7.getText(),
		this.text1p7.getText() + System.currentTimeMillis()); //Random added Ont/Dev/Serv for 2-valued ADD services
	if (result.equals(SpaceCaller.NONE)) {
	    label1p7.setText("That combination is not available");
	} else {
	    label1p7.setText(result);
	}
    }

}
