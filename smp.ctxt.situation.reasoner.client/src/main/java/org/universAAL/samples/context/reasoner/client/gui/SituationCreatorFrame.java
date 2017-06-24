/*
    Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
    Fraunhofer-Gesellschaft - Institute for Computer Graphics Research

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
package org.universAAL.samples.context.reasoner.client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.universAAL.ontology.reasoner.Situation;
import org.universAAL.samples.context.reasoner.client.uaalinterface.ReasoningCaller;

/**
 * Since a Situation consist of an URI for subject, predicate and object of an
 * event-pattern there are three ComboBoxes provided to give them. To support
 * the user all the recorded URI's are already given in the boxes, but is also
 * possible to give completely new one.
 *
 * @author amarinc
 *
 */
@SuppressWarnings("serial")
public class SituationCreatorFrame extends JFrame {

	private ReasoningCaller caller = null;
	private ReasoningGUI parentGUI = null;

	private JCheckBox predicateCheck = null;
	private JCheckBox objectCheck = null;
	private JCheckBox saveCheck = null;
	private JComboBox subjectBox = null;
	private JComboBox predicateBox = null;
	private JComboBox objectBox = null;

	public SituationCreatorFrame(ReasoningGUI parentGUI, ReasoningCaller caller) {
		this.caller = caller;
		this.parentGUI = parentGUI;
		initialize();
	}

	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.setSize(1000, 200);
		this.setVisible(true);
		this.getContentPane().setLayout(new GridBagLayout());
		this.setTitle("Situation Creator");
		this.setEnabled(true);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.2;
		JLabel uriLabel = new JLabel("SituationURI");
		this.getContentPane().add(uriLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		JTextField uriText = new JTextField(Situation.MY_URI + ReasoningCaller.random.nextInt(9999999));
		this.getContentPane().add(uriText, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.2;
		JLabel subjectLabel = new JLabel("Subject");
		this.getContentPane().add(subjectLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.0;
		subjectBox = new JComboBox(caller.getRecorder().getAllTypeAndInstanceURIs().toArray(new String[0]));
		subjectBox.setEditable(true);
		this.getContentPane().add(subjectBox, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.2;
		predicateCheck = new JCheckBox("Predicate");
		predicateCheck.setSelected(false);
		predicateCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				predicateBox.setEnabled(predicateCheck.isSelected());
				objectBox.setEnabled(objectCheck.isSelected() & predicateCheck.isSelected());
				objectCheck.setEnabled(predicateCheck.isSelected());
			}
		});
		this.getContentPane().add(predicateCheck, c);
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1.0;
		predicateBox = new JComboBox(caller.getRecorder().getRecordedPredicates().toArray(new String[0]));
		predicateBox.setEnabled(false);
		predicateBox.setEditable(true);
		this.getContentPane().add(predicateBox, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.2;
		objectCheck = new JCheckBox("Object");
		objectCheck.setSelected(false);
		objectCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				objectBox.setEnabled(objectCheck.isSelected());
			}
		});
		this.getContentPane().add(objectCheck, c);

		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 1.0;
		objectBox = new JComboBox(caller.getRecorder().getAllTypeAndInstanceURIs().toArray(new String[0]));
		objectBox.setEnabled(false);
		objectBox.setEditable(true);
		this.getContentPane().add(objectBox, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		Box footerBox = Box.createHorizontalBox();
		this.getContentPane().add(footerBox, c);
		footerBox.add(Box.createHorizontalGlue());
		saveCheck = new JCheckBox("Save permanent: ");
		saveCheck.setSelected(true);
		footerBox.add(saveCheck);
		footerBox.add(Box.createHorizontalStrut(50));
		JButton addButton = new JButton("Add");
		addButton.setPreferredSize(new Dimension(80, 30));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String predicate = predicateCheck.isSelected() ? predicateBox.getSelectedItem().toString() : null;
				String object = predicateCheck.isSelected() && objectCheck.isSelected()
						? objectBox.getSelectedItem().toString() : null;
				caller.addSituation(subjectBox.getSelectedItem().toString(), predicate, object, saveCheck.isSelected());
				parentGUI.closeSituationFrame();
			}
		});
		footerBox.add(addButton);
		footerBox.add(Box.createHorizontalStrut(5));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 30));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentGUI.closeSituationFrame();
			}
		});
		footerBox.add(cancelButton);

		this.pack();
	}
}
