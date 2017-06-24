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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.owl.ManagedIndividual;
import org.universAAL.ontology.reasoner.Query;
import org.universAAL.samples.context.reasoner.client.osgi.GUIActivator;
import org.universAAL.samples.context.reasoner.client.uaalinterface.ReasoningCaller;

/**
 *
 * A JFrame with the basic components to build a query for the Reasoner. The
 * result need to be a valid CONTRUCT SPARQL query. This is displayed at the
 * right side of the frame. It is possible to add URI's recorded from the
 * context-bus to this query and also to verify it by using the according
 * button. To make the construction a little bit easier it is possible to use
 * recorded context-events to create a query. Therefore at the left side of the
 * frame you can find a ComboBox where you can select an event to construct and
 * give below the search-string like needed for SPARQL. With the button in the
 * middle the query will be generated then.
 *
 * @author amarinc
 *
 */
@SuppressWarnings("serial")
public class QueryCreatorFrame extends JFrame {

	private ReasoningCaller caller = null;
	private ReasoningGUI parentGUI = null;

	private JComboBox eventBox = null;
	private JComboBox uriAddBox = null;
	private JTextArea searchStringArea = null;
	private JTextField uriText = null;
	private JTextArea fullSearchText = null;
	private JLabel subjectLabel = null;
	private JLabel predicateLabel = null;
	private JLabel objectLabel = null;
	private JCheckBox saveCheck = null;

	private HashMap<String, ContextEvent> events = new HashMap<String, ContextEvent>();

	private QueryCreatorFrame self = this;

	public QueryCreatorFrame(ReasoningGUI parentGUI, ReasoningCaller caller) {
		this.caller = caller;
		this.parentGUI = parentGUI;
		initialize();
	}

	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.setSize(800, 600);
		this.setVisible(true);
		this.getContentPane().setLayout(new GridBagLayout());
		this.setTitle("Query Creator");
		this.setEnabled(true);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1.0;
		c.weighty = 1.0;

		// URI Header
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		Box uriBox = Box.createHorizontalBox();
		this.getContentPane().add(uriBox, c);
		JLabel uriLabel = new JLabel("URI:");
		uriBox.add(uriLabel);
		uriBox.add(Box.createHorizontalStrut(5));
		uriText = new JTextField(Query.MY_URI + ReasoningCaller.random.nextInt(9999999));
		uriText.setPreferredSize(new Dimension(600, 30));
		uriBox.add(uriText);

		// first column
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weighty = 0.1;
		JLabel eventLabel = new JLabel("Recored Context-Events:");
		this.getContentPane().add(eventLabel, c);
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 1.0;
		eventBox = new JComboBox(refreshEvents().toArray(new String[0]));
		eventBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshEventLabels();
			}
		});
		eventBox.setPreferredSize(new Dimension(400, 30));
		this.getContentPane().add(eventBox, c);
		c.gridx = 0;
		c.gridy = 3;
		Box eventLabelBox = Box.createVerticalBox();
		subjectLabel = new JLabel("Subject: ");
		eventLabelBox.add(subjectLabel, c);
		predicateLabel = new JLabel("Predicate: ");
		eventLabelBox.add(predicateLabel, c);
		objectLabel = new JLabel("Object: ");
		eventLabelBox.add(objectLabel, c);
		this.getContentPane().add(eventLabelBox, c);
		refreshEventLabels();
		c.gridx = 0;
		c.gridy = 4;
		c.weighty = 0.1;
		JLabel searchLabel = new JLabel("SPARQL Search-String:");
		this.getContentPane().add(searchLabel, c);
		c.gridx = 0;
		c.gridy = 5;
		c.weighty = 3.0;
		c.fill = GridBagConstraints.BOTH;
		String example = "<http://ontology.igd.fhg.de/LightingServer.owl#controlledLamp3> <http://ontology.universaal.org/Lighting.owl#srcBrightness> ?b ."
				+ System.getProperty("line.separator")
				+ "<http://ontology.igd.fhg.de/LightingServer.owl#controlledLamp1> <http://ontology.universaal.org/Lighting.owl#srcBrightness> ?a ."
				+ System.getProperty("line.separator") + "FILTER (?b < 100  &&  ?a < 100)";
		searchStringArea = new JTextArea(example);
		JScrollPane scrollPane = new JScrollPane(searchStringArea);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		this.getContentPane().add(scrollPane, c);

		// second column
		c.gridx = 1;
		c.gridy = 4;
		c.weighty = 1.0;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.NONE;
		JButton createQueryButton = new JButton(">>");
		createQueryButton.setPreferredSize(new Dimension(60, 30));
		createQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (eventBox.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(self, "No ContextEvent available up to now.");
					return;
				}
				String eventURI = eventBox.getSelectedItem().toString();
				ContextEvent event = events.get(eventURI);
				if (event == null) {
					JOptionPane.showMessageDialog(self, "Need to select a ContextEvent first.");
					return;
				}
				String search = searchStringArea.getText();
				if (search == null || search.equals("")) {
					JOptionPane.showMessageDialog(self, "No query-string is given!");
					return;
				}
				Query query = new Query(event, search);
				fullSearchText.setText(query.getResultingQuery());
			}
		});
		this.getContentPane().add(createQueryButton, c);

		// third column
		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		c.weightx = 2.0;
		c.fill = GridBagConstraints.BOTH;
		Box addURIBox = Box.createHorizontalBox();
		JLabel uriAddLabel = new JLabel("URI to add:");
		addURIBox.add(uriAddLabel);
		addURIBox.add(Box.createHorizontalStrut(5));
		uriAddBox = new JComboBox(caller.getRecorder().getAllURIs().toArray(new String[0]));
		addURIBox.add(uriAddBox);
		addURIBox.add(Box.createHorizontalStrut(5));
		JButton addURIButton = new JButton("Add");
		addURIButton.setPreferredSize(new Dimension(60, 30));
		addURIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String uri = uriAddBox.getSelectedItem().toString();
				if (uri == null)
					return;
				fullSearchText.insert(uri, fullSearchText.getCaretPosition());
			}
		});
		addURIBox.add(addURIButton);
		addURIBox.add(Box.createHorizontalStrut(50));
		JButton checkQuery = new JButton("Check query");
		checkQuery.setPreferredSize(new Dimension(80, 30));
		checkQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fullQuery = fullSearchText.getText();
				if (fullQuery == null || fullQuery.equals("")) {
					JOptionPane.showMessageDialog(self, "No query given.");
					return;
				}
				ContextEvent checkEvent = GUIActivator.cheCaller.executeQuery(fullQuery);
				if (checkEvent == null) {
					JOptionPane.showMessageDialog(self, "Error during execution of query!.");
					return;
				}
				if (!checkEvent.isWellFormed() || checkEvent.getSubjectTypeURI() == null) {
					JOptionPane.showMessageDialog(self,
							"Invalid CONSTRUCT query associated to " + "situation. CONSTRUCT queries must build "
									+ "graphs with a well-formed Context Event in" + " the root.");
					return;
				}
				JOptionPane.showMessageDialog(self,
						"Query successfull executed! \n" + "Subject: " + checkEvent.getSubjectURI() + "\n"
								+ "Predicate: " + checkEvent.getRDFPredicate() + "\n" + "Object: "
								+ checkEvent.getRDFObject());
			}
		});
		addURIBox.add(checkQuery);
		this.getContentPane().add(addURIBox, c);
		c.gridx = 2;
		c.gridy = 2;
		c.gridheight = 4;
		c.weightx = 2.0;
		c.fill = GridBagConstraints.BOTH;
		fullSearchText = new JTextArea();
		scrollPane = new JScrollPane(fullSearchText);
		scrollPane.setPreferredSize(new Dimension(800, 400));
		this.getContentPane().add(scrollPane, c);

		// Footer for buttons
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.weightx = 1.0;
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
				if (!addQuery())
					JOptionPane.showMessageDialog(self, "Error during add of the new query.");
				parentGUI.closeQueryFrame();
			}
		});
		footerBox.add(addButton);
		footerBox.add(Box.createHorizontalStrut(5));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(80, 30));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentGUI.closeQueryFrame();
			}
		});
		footerBox.add(cancelButton);

		this.pack();
	}

	/**
	 * If a new context-event is selected its subject, predicate and object is
	 * displayed to make it easier to identify it
	 */
	private void refreshEventLabels() {
		String eventURI = eventBox.getSelectedItem().toString();
		ContextEvent event = events.get(eventURI);
		if (event != null) {
			subjectLabel.setText("Subject: " + event.getSubjectURI());
			if (event.getRDFPredicate() != null) {
				predicateLabel.setText("Predicate: " + event.getRDFPredicate());
				if (event.getRDFObject() instanceof ManagedIndividual)
					objectLabel.setText("Object: " + ((ManagedIndividual) event.getRDFObject()).getURI());
				else
					objectLabel.setText("Object: " + event.getRDFObject().toString());
			}
		}
	}

	/**
	 * Creates and returns a list of the URI's from the actually recored
	 * context-events. The events are saved in a HashMap to be able to access
	 * the objects by giving the URI of it.
	 *
	 * @return List of URI's from the currently recorded context-events
	 */
	private List<String> refreshEvents() {
		this.events.clear();
		ArrayList<ContextEvent> curEvents = caller.getRecorder().getRecordedEvents();
		List<String> result = new ArrayList<String>();
		for (ContextEvent event : curEvents) {
			this.events.put(event.getURI(), event);
			result.add(event.getURI());
		}
		return result;
	}

	/**
	 * Add the currently given query to the Reasoner.
	 *
	 * @return True if the add process has been successful, false otherwise
	 */
	private boolean addQuery() {
		String uri = uriText.getText();
		String fullQuery = fullSearchText.getText();
		if (uri == null || uri.equals("") || fullQuery == null || fullQuery.equals(""))
			return false;
		return caller.addQuery(uri, fullQuery) != null;

	}
}
