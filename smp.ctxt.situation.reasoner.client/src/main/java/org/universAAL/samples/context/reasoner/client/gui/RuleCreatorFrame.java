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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import org.universAAL.ontology.reasoner.Query;
import org.universAAL.ontology.reasoner.Situation;
import org.universAAL.samples.context.reasoner.client.uaalinterface.ReasoningCaller;

/**
 * This frame is quite easy, because all have to be done to create a rule is to
 * combine a Situation with a Query. Therefore two ComboBoxes are needed to
 * display the available elements of both types and select the one you need. To
 * make the identification of the elements more easy they are display below the
 * boxes.
 *
 * @author amarinc
 *
 */
@SuppressWarnings("serial")
public class RuleCreatorFrame extends JFrame {

	private ReasoningCaller caller = null;
	private ReasoningGUI parentGUI = null;

	private JLabel situationContentLabel = null;
	private JTextArea searchStringArea = null;
	private JComboBox situationBox = null;
	private JComboBox queryBox = null;
	private JCheckBox saveCheck = null;

	private RuleCreatorFrame self = this;
	private HashMap<String, Situation> situations = new HashMap<String, Situation>();
	private HashMap<String, Query> queries = new HashMap<String, Query>();

	public RuleCreatorFrame(ReasoningGUI parentGUI, ReasoningCaller caller) {
		this.caller = caller;
		this.parentGUI = parentGUI;
		initialize();
	}

	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.setSize(600, 300);
		this.setVisible(true);
		this.getContentPane().setLayout(new GridBagLayout());
		this.setTitle("Rule Creator");
		this.setEnabled(true);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weighty = 1.0;

		// first line
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.3;
		JLabel situationLabel = new JLabel("Situation:");
		this.getContentPane().add(situationLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		JLabel queryLabel = new JLabel("Query:");
		this.getContentPane().add(queryLabel, c);

		// second line
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.3;
		situationBox = new JComboBox(refreshSituations().toArray(new String[0]));
		situationBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshSituationLabels();
			}
		});
		this.getContentPane().add(situationBox, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.0;
		queryBox = new JComboBox(refreshQueries().toArray(new String[0]));
		queryBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshQueryLabel();
			}
		});
		this.getContentPane().add(queryBox, c);

		// third line
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.3;
		situationContentLabel = new JLabel("");
		this.getContentPane().add(situationContentLabel, c);
		refreshSituationLabels();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 2.0;
		c.fill = GridBagConstraints.BOTH;
		searchStringArea = new JTextArea();
		searchStringArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(searchStringArea);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		this.getContentPane().add(scrollPane, c);
		refreshQueryLabel();

		// footer
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
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
				String situationURI = situationBox.getSelectedItem().toString();
				Situation situation = situations.get(situationURI);
				String queryURI = queryBox.getSelectedItem().toString();
				Query query = queries.get(queryURI);
				if (situation == null || query == null)
					JOptionPane.showMessageDialog(self, "Please select a query and a situation. Rule not added!");
				boolean persistent = saveCheck.isSelected();
				if (persistent && (!situation.isPersistent() || !query.isPersistent())) {
					JOptionPane.showMessageDialog(self,
							"The rule can not be persistent since the given Situation and Query are not. A temporary rule will be created instead.");
					persistent = false;
				}
				caller.addRule(situation, query, persistent);
				parentGUI.closeRuleFrame();
			}
		});
		footerBox.add(addButton);
		footerBox.add(Box.createHorizontalStrut(5));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 30));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentGUI.closeRuleFrame();
			}
		});
		footerBox.add(cancelButton);

		this.pack();
	}

	/**
	 * Returns a list of URI's from the current available situations and saves
	 * the situation objects in a map to make them available by giving their
	 * URI's
	 *
	 * @return List of URI's from currently available situations
	 */
	private List<String> refreshSituations() {
		this.situations.clear();
		List<Situation> curSituations = caller.getSituations();
		List<String> result = new ArrayList<String>();
		for (Situation situation : curSituations) {
			this.situations.put(situation.getURI(), situation);
			result.add(situation.getURI());
		}
		return result;
	}

	/**
	 * Returns a list of URI's from the current available queries and saves the
	 * query objects in a map to make them available by giving their URI's
	 *
	 * @return List of URI's from currently available queries
	 */
	private List<String> refreshQueries() {
		this.queries.clear();
		List<Query> curQueries = caller.getQueries();
		List<String> result = new ArrayList<String>();
		for (Query query : curQueries) {
			this.queries.put(query.getURI(), query);
			result.add(query.getURI());
		}
		return result;
	}

	/**
	 * Used to refresh the content of a Situation after selecting a new one from
	 * the according ComboBox.
	 */
	private void refreshSituationLabels() {
		String eventURI = situationBox.getSelectedItem().toString();
		Situation situation = situations.get(eventURI);
		if (situation != null)
			situationContentLabel.setText(situation.toString());
	}

	/**
	 * Used to refresh the content of a Query after selecting a new one from the
	 * according ComboBox.
	 */
	private void refreshQueryLabel() {
		String eventURI = queryBox.getSelectedItem().toString();
		Query query = queries.get(eventURI);
		if (query != null) {
			searchStringArea.setText(query.getResultingQuery());
		}
	}
}
