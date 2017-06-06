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
package org.universAAL.samples.uibus;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;

import org.osgi.framework.BundleContext;

public class GUIPanel extends javax.swing.JFrame {

    // Variables declaration - do not modify
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel labelMain;
    private javax.swing.JButton button1p1;
//    private javax.swing.JButton button2p1;
    private javax.swing.JPanel panel1;
    private javax.swing.JTextField text1p1;
//    private javax.swing.JTextField text2p1;
    private javax.swing.JLabel label1p1;
    private javax.swing.JButton button1p2;
//    private javax.swing.JButton button2p2;
    private javax.swing.JPanel panel2;
    private javax.swing.JTextField text1p2;
    private javax.swing.JLabel label1p2;

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

	panel1 = new javax.swing.JPanel();
	text1p1 = new javax.swing.JTextField();
//	text2p1 = new javax.swing.JTextField();
	label1p1 = new javax.swing.JLabel();
	button1p1 = new javax.swing.JButton();
//	button2p1 = new javax.swing.JButton();

	panel2 = new javax.swing.JPanel();
	text1p2 = new javax.swing.JTextField();
	label1p2 = new javax.swing.JLabel();
	button1p2 = new javax.swing.JButton();
//	button2p2 = new javax.swing.JButton();

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

	text1p1.setText("Outputs burst");
	panel1.add(text1p1);
	text1p1.setBounds(20, 25, 210, 23);

	button1p1.setText("Publish");
	button1p1.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		sendButton1ActionPerformed(evt);
	    }
	});
	panel1.add(button1p1);
	button1p1.setBounds(20, 50, 75, 29);

//	text2p1.setText("Inputs burst");
//	panel1.add(text2p1);
//	text2p1.setBounds(20, 80, 210, 23);

//	button2p1.setText("Publish (unique)");
//	button2p1.addActionListener(new java.awt.event.ActionListener() {
//	    public void actionPerformed(java.awt.event.ActionEvent evt) {
//		sendButton2ActionPerformed(evt);
//	    }
//	});
//	panel1.add(button2p1);
//	button2p1.setBounds(20, 105, 75, 29);

	label1p1.setText("Delay");
	panel1.add(label1p1);
	label1p1.setBounds(20, 130, 210, 23);

	tabbedPane.addTab("UI Bus", panel1);

	// TAB 2
	panel2.setLayout(null);

	text1p2.setText("Amount of forms");
	panel2.add(text1p2);
	text1p2.setBounds(20, 25, 210, 23);

	button1p2.setText("Send");
	button1p2.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
		handlerButton1ActionPerformed(evt);
	    }
	});
	panel2.add(button1p2);
	button1p2.setBounds(20, 50, 75, 29);

	label1p2.setText("Delay: ");
	panel2.add(label1p2);
	label1p2.setBounds(20, 75, 150, 23);

//	button2p2.setText("Main");
//	button2p2.addActionListener(new java.awt.event.ActionListener() {
//	    public void actionPerformed(java.awt.event.ActionEvent evt) {
//		handlerButton2ActionPerformed(evt);
//	    }
//	});
//	panel2.add(button2p2);
//	button2p2.setBounds(20, 100, 75, 29);

	tabbedPane.addTab("Handler", panel2);

	// MAIN
	getContentPane().add(tabbedPane);
	tabbedPane.setBounds(10, 40, 400, 260);
	labelMain.setFont(new java.awt.Font("Verdana", 1, 14));
	labelMain.setText("Test utility, UI");
	getContentPane().add(labelMain);
	labelMain.setBounds(10, 10, 360, 18);

	pack();
    }

    private void sendButton1ActionPerformed(ActionEvent evt) {
	int siz = 0;
	try {
	    siz = Integer.parseInt(this.text1p1.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of burst");
	    return;
	}
	long del = Activator.uoutput.showRandomBurst(Activator.sampleUser, siz);
	this.label1p1.setText("Delay: " + del + " ms");
    }

//    private void sendButton2ActionPerformed(ActionEvent evt) {
//	int siz = 0;
//	try {
//	    siz = Integer.parseInt(this.text2p1.getText());
//	} catch (Exception e) {
//	    this.label1p1.setText("Invalid size of burst");
//	    return;
//	}
//	InputEvent ie = new InputEvent(Activator.sampleUser, null,
//		InputEvent.uAAL_MAIN_MENU_REQUEST);
//	long t0 = System.currentTimeMillis();
//	for (int i = 0; i < siz; i++) {
//	    Activator.uipub.publish(ie);
//	}
//	long t1 = System.currentTimeMillis();
//	this.label1p1.setText("Delay: " + (t1 - t0) + " ms");
//    }

    private void handlerButton1ActionPerformed(ActionEvent evt) {
	int siz = 0;
	try {
	    siz = Integer.parseInt(this.text1p2.getText());
	} catch (Exception e) {
	    this.label1p1.setText("Invalid size of event");
	    return;
	}
	long del = Activator.uoutput.showDynamicDialog(Activator.sampleUser,
		siz);
	this.label1p2.setText("Delay: " + del + " ms");
    }

//    private void handlerButton2ActionPerformed(ActionEvent evt) {
//	Activator.uipub.publish(new InputEvent(Activator.sampleUser, null,
//		InputEvent.uAAL_MAIN_MENU_REQUEST));
//    }

}
