package org.universAAL.samples.servbus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.lighting.LightSource;
import org.universAAL.ontology.lighting.Lighting;

public class GUIPanel extends javax.swing.JFrame {
    private static final String LIGHTING_CLIENT_NAMESPACE = "http://ontology.tsb.itaca.es/LightingClient.owl#";
    private static final String OUTPUT_LIST_OF_LAMPS = LIGHTING_CLIENT_NAMESPACE
	    + "listOfLamps";
    private static final String OUTPUT_LAMP_LOCATION = LIGHTING_CLIENT_NAMESPACE
	    + "lampLocation";
    private static final String SAMPLE_LAMP_URI = "http://ontology.igd.fhg.de/LightingServer.owl#lamp0";

    // Variables declaration - do not modify
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel labelMain;
    private javax.swing.JButton button1p1;
    private javax.swing.JButton button2p1;
    private javax.swing.JPanel panel1;
    private javax.swing.JLabel label1p1;
    private javax.swing.JComboBox combo1p1;
    private javax.swing.JTextField text1p1;

    // End of variables declaration

    public GUIPanel(BundleContext context) {
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
	text1p1 = new javax.swing.JTextField();
	panel1 = new javax.swing.JPanel();
	label1p1 = new javax.swing.JLabel();
	button1p1 = new javax.swing.JButton();
	button2p1 = new javax.swing.JButton();
	combo1p1 = new javax.swing.JComboBox(new Object[] { "Get Lamps",
		"Get Lamp Info", "Turn On", "Turn Off", "Set 50%" });

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

	combo1p1.setEditable(false);
	panel1.add(combo1p1);
	combo1p1.setBounds(20, 25, 210, 23);

	text1p1.setText("Call burst");
	panel1.add(text1p1);
	text1p1.setBounds(20, 50, 210, 23);

	button1p1.setText("Call");
	button1p1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendButton1ActionPerformed(evt);
	    }
	});
	panel1.add(button1p1);
	button1p1.setBounds(20, 75, 75, 29);

	button2p1.setText("Send");
	button2p1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendButton2ActionPerformed(evt);
	    }
	});
	panel1.add(button2p1);
	button2p1.setBounds(20, 110, 75, 29);

	label1p1.setText("Delay");
	panel1.add(label1p1);
	label1p1.setBounds(20, 145, 210, 23);

	tabbedPane.addTab("Service Bus", panel1);

	// MAIN
	getContentPane().add(tabbedPane);
	tabbedPane.setBounds(10, 40, 400, 260);
	labelMain.setFont(new java.awt.Font("Verdana", 1, 14));
	labelMain.setText("Test utility, Serv");
	getContentPane().add(labelMain);
	labelMain.setBounds(10, 10, 360, 18);

	pack();
    }

    private void sendButton1ActionPerformed(ActionEvent evt) {
	this.label1p1.setText("Delay: ");
	int siz = 0;
	try {
	    siz = Integer.parseInt(this.text1p1.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of burst");
	    return;
	}
	ServiceRequest srq = null;
	switch (this.combo1p1.getSelectedIndex()) {
	case 0:
	    srq = getGETLAMPSrequest();
	    break;
	case 1:
	    srq = getGETLAMPINFOrequest();
	    break;
	case 2:
	    srq = getSETreqest(100);
	    break;
	case 3:
	    srq = getSETreqest(0);
	    break;
	case 4:
	    srq = getSETreqest(50);
	    break;
	default:
	    break;
	}
	long t0 = System.currentTimeMillis();
	for (int i = 0; i < siz; i++) {
	    ServiceResponse sr = Activator.scaller.call(srq);
	    if (sr.getCallStatus() != CallStatus.succeeded) {
		if (sr.getCallStatus() == CallStatus.noMatchingServiceFound) {
		    this.label1p1.setText("noMatchingServiceFound at call "
			    + (i + 1));
		    return;
		} else if (sr.getCallStatus() == CallStatus.responseTimedOut) {
		    this.label1p1
			    .setText("responseTimedOut at call " + (i + 1));
		    return;
		} else if (sr.getCallStatus() == CallStatus.serviceSpecificFailure) {
		    this.label1p1.setText("serviceSpecificFailure at call "
			    + (i + 1));
		    return;
		}
		return;
	    }
	}
	long t1 = System.currentTimeMillis();
	this.label1p1.setText("Delay: " + (t1 - t0) + " ms");
    }

    private void sendButton2ActionPerformed(ActionEvent evt) {
	this.label1p1.setText("Delay: ");
	int siz = 0;
	try {
	    siz = Integer.parseInt(this.text1p1.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of burst");
	    return;
	}
	ServiceRequest srq = null;
	switch (this.combo1p1.getSelectedIndex()) {
	case 0:
	    srq = getGETLAMPSrequest();
	    break;
	case 1:
	    srq = getGETLAMPINFOrequest();
	    break;
	case 2:
	    srq = getSETreqest(100);
	    break;
	case 3:
	    srq = getSETreqest(0);
	    break;
	case 4:
	    srq = getSETreqest(50);
	    break;
	default:
	    break;
	}
	long t0 = System.currentTimeMillis();
	for (int i = 0; i < siz; i++) {
	    Activator.scaller.sendRequest(srq);
	}
	long t1 = System.currentTimeMillis();
	this.label1p1.setText("Delay: " + (t1 - t0) + " ms");
    }

    private ServiceRequest getGETLAMPSrequest() {
	ServiceRequest listLamps = new ServiceRequest(
		LightSource.LIGHTING_NAMESPACE + "requestURI", new Lighting(
			null), null);
	listLamps.addSimpleOutputBinding(
		new ProcessOutput(OUTPUT_LIST_OF_LAMPS), new PropertyPath(null,
			true, new String[] { Lighting.PROP_CONTROLS })
			.getThePath());
	return listLamps;
    }

    private ServiceRequest getGETLAMPINFOrequest() {
	ServiceRequest getLampLocation = new ServiceRequest(new Lighting(),
		null);
	getLampLocation.getRequestedService().addInstanceLevelRestriction(
		Restriction.getFixedValueRestriction(Lighting.PROP_CONTROLS,
			new LightSource(SAMPLE_LAMP_URI)),
		new String[] { Lighting.PROP_CONTROLS });
	getLampLocation.addSimpleOutputBinding(new ProcessOutput(
		OUTPUT_LAMP_LOCATION), new PropertyPath(null, true,
		new String[] { Lighting.PROP_CONTROLS,
			LightSource.PROP_PHYSICAL_LOCATION }).getThePath());
	return getLampLocation;
    }

    private ServiceRequest getSETreqest(int brightness) {
	ServiceRequest setLampBrightness = new ServiceRequest(new Lighting(),
		null);
	setLampBrightness.getRequestedService().addInstanceLevelRestriction(
		Restriction.getFixedValueRestriction(Lighting.PROP_CONTROLS,
			new LightSource(SAMPLE_LAMP_URI)),
		new String[] { Lighting.PROP_CONTROLS });
	setLampBrightness.addChangeEffect(new PropertyPath(null, true,
		new String[] { Lighting.PROP_CONTROLS,
			LightSource.PROP_SOURCE_BRIGHTNESS }).getThePath(),
		new Integer(brightness));
	return setLampBrightness;
    }
}
