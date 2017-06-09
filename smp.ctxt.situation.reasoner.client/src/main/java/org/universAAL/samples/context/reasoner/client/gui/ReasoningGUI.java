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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import org.universAAL.ontology.reasoner.Persistent;
import org.universAAL.ontology.reasoner.Query;
import org.universAAL.ontology.reasoner.Rule;
import org.universAAL.ontology.reasoner.Situation;
import org.universAAL.samples.context.reasoner.client.uaalinterface.ReasoningCaller;

/**
 *
 * This is the main-frame of the reasoner-client. It is basically used to
 * show/add/remove Situation, Query and Rule items to the Reasoner. For every of
 * the three types there is given on Panel. Since they differ not much from each
 * other (primary the events for add/delete/refresh) an internal class
 * (ElementBox) is used to enclose the redundant parts. Information about a
 * list-entry are given by using tool-tips.
 *
 * @author amarinc
 *
 */
@SuppressWarnings("serial")
public class ReasoningGUI extends javax.swing.JPanel {
	static {
		java.util.logging.Logger.getLogger("sun").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("java").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("javax").setLevel(java.util.logging.Level.OFF);
	}

	private abstract class ElementAction extends AbstractAction {
		public ElementBox<?> box = null;

		public ElementAction(ElementBox<?> box, String title) {
			super(title, null);
			this.box = box;
		}
	}

	/**
	 * This class enclosed the redundant parts to be visualized to manage an
	 * object of the reasoners ontology.
	 *
	 * @author amarinc
	 *
	 * @param <P>
	 *            Currently can be only one the following: Situation, Query and
	 *            Rule
	 */
	private class ElementBox<P extends Persistent> {
		public JPanel elementsBox = null;
		public JPanel elementsButtonBox = null;
		public JButton addElementButton = null;
		public JButton removeElementButton = null;
		public JButton getElementsButton = null;
		public JList elementsList = null;
		public HashMap<String, P> map = null;

		public ElementBox(JFrame frame, String title, HashMap<String, P> map) {
			this.map = map;
			this.addElementDefault(frame, title);
		}

		private void addElementDefault(JFrame frame, String title) {
			elementsBox = new JPanel();
			BorderLayout layout = new BorderLayout();
			layout.setHgap(5);
			layout.setVgap(5);
			elementsBox.setLayout(layout);
			elementsBox.setBorder(BorderFactory.createTitledBorder(title));
			elementsBox.setPreferredSize(new Dimension(370, 255));
			frame.getContentPane().add(elementsBox);

			elementsButtonBox = new JPanel();
			elementsButtonBox.setLayout(new FlowLayout());
			elementsButtonBox.setPreferredSize(new Dimension(350, 45));
			elementsBox.add(elementsButtonBox, BorderLayout.PAGE_START);

			addElementButton = new JButton();
			addElementButton.setText("Add");
			addElementButton.setPreferredSize(new Dimension(80, 35));
			elementsButtonBox.add(addElementButton);

			removeElementButton = new JButton();
			removeElementButton.setText("Remove");
			removeElementButton.setPreferredSize(new Dimension(80, 35));
			elementsButtonBox.add(removeElementButton);

			getElementsButton = new JButton();
			getElementsButton.setText("Get");
			getElementsButton.setPreferredSize(new Dimension(80, 35));
			elementsButtonBox.add(getElementsButton);

			ListModel jList1Model = new DefaultComboBoxModel();
			elementsList = new JList() {
				public String getToolTipText(MouseEvent e) {
					int index = locationToIndex(e.getPoint());

					if (-1 < index) {
						String item = (String) getModel().getElementAt(index);
						return ((P) map.get(item)).toString();
					}
					return null;
				}
			};
			elementsList.setModel(jList1Model);
			elementsList.setPreferredSize(new Dimension(350, 170));
			elementsBox.add(elementsList, BorderLayout.CENTER);
		}

		public void addAddAction(ElementAction action) {
			this.addElementButton.setAction(action);
		}

		public void addRemoveAction(ElementAction action) {
			this.removeElementButton.setAction(action);
		}

		public void addGetAction(ElementAction action) {
			this.getElementsButton.setAction(action);
		}

		/**
		 * Refresh the view of the list of currently available elements from the
		 * type P.
		 *
		 * @param elements
		 *            Available elements of the type P to be added to the JList.
		 */
		public void refreshElements(List<P> elements) {
			if (map == null)
				return;

			elements = elements == null ? new ArrayList<P>() : elements;
			List<String> displayList = new ArrayList<String>();

			map.clear();
			for (P element : elements) {
				map.put(element.getURI(), element);
				displayList.add(element.getURI());
			}

			ListModel jListModel = new DefaultComboBoxModel(displayList.toArray(new String[0]));
			if (jListModel != null)
				elementsList.setModel(jListModel);
		}
	}

	private ReasoningGUI self = this;

	// Frames to add the according elements to the reasoner
	private SituationCreatorFrame situationFrame = null;
	private QueryCreatorFrame queryFrame = null;
	private RuleCreatorFrame ruleFrame = null;

	private ReasoningCaller caller = null;

	// Overview panels for each type
	private ElementBox<Situation> situationBox = null;
	private ElementBox<Query> queryBox = null;
	private ElementBox<Rule> ruleBox = null;

	// Maps between URI's and its according objects
	private HashMap<String, Situation> curSituations = new HashMap<String, Situation>();
	private HashMap<String, Query> curQueries = new HashMap<String, Query>();
	private HashMap<String, Rule> curRules = new HashMap<String, Rule>();

	JFrame mainFrame = null;

	/**
	 * Creates the main-view of the reasoner-client. The "caller" is more or
	 * less the controller and model of the project and needed to make
	 * service-calls to the listener.
	 *
	 * @param caller
	 */
	public ReasoningGUI(ReasoningCaller caller) {
		super();
		this.caller = caller;
		initGUI();
		start();
		refreshSituations();
		refreshQueries();
		refreshRules();
	}

	public void refreshSituations() {
		situationBox.refreshElements(caller.getSituations());
	}

	public void refreshQueries() {
		queryBox.refreshElements(caller.getQueries());
	}

	public void refreshRules() {
		ruleBox.refreshElements(caller.getRules());
	}

	public boolean removeSelectedSituation() {
		String selItem;
		try {
			selItem = situationBox.elementsList.getSelectedValue().toString();
		} catch (NullPointerException e) {
			return false;
		}

		if (selItem == null)
			return false;

		Situation situation = curSituations.get(selItem);

		if (situation == null)
			return false;

		curSituations.remove(selItem);
		return caller.remove(situation);
	}

	public boolean removeSelectedQuery() {
		String selItem;
		try {
			selItem = queryBox.elementsList.getSelectedValue().toString();
		} catch (NullPointerException e) {
			return false;
		}

		if (selItem == null)
			return false;

		Query query = curQueries.get(selItem);

		if (query == null)
			return false;

		curQueries.remove(selItem);
		return caller.remove(query);
	}

	public boolean removeSelectedRule() {
		String selItem = ruleBox.elementsList.getSelectedValue().toString();

		if (selItem == null)
			return false;

		Rule rule = curRules.get(selItem);

		if (rule == null)
			return false;

		curRules.remove(selItem);
		return caller.remove(rule);
	}

	public void close() {
		mainFrame.dispose();
	}

	public void closeSituationFrame() {
		this.situationFrame.dispose();
		this.situationFrame = null;
		this.refreshSituations();
	}

	public void closeQueryFrame() {
		this.queryFrame.dispose();
		this.queryFrame = null;
		this.refreshQueries();
	}

	public void closeRuleFrame() {
		this.ruleFrame.dispose();
		this.ruleFrame = null;
		this.refreshRules();
	}

	/**
	 * Initialize the GUI-Elements. Basically three instances of ElementBox has
	 * to be created and for each the three methods to add/remove/get the
	 * according elements need to be given.
	 */
	private void start() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setSize(1200, 400);
		mainFrame.setVisible(true);
		mainFrame.getContentPane().setLayout(new FlowLayout());
		mainFrame.setTitle("Reasoning view");
		mainFrame.setEnabled(true);

		// Add GUI elements to handle situations
		situationBox = new ElementBox<Situation>(mainFrame, "Situations", curSituations);
		situationBox.addAddAction(new ElementAction(situationBox, "Add") {
			public void actionPerformed(ActionEvent evt) {
				if (situationFrame == null) {
					situationFrame = new SituationCreatorFrame(self, caller);
				} else {
					situationFrame.toFront();
				}
			}
		});
		situationBox.addRemoveAction(new ElementAction(situationBox, "Remove") {
			public void actionPerformed(ActionEvent evt) {
				removeSelectedSituation();
				refreshSituations();
			}
		});
		situationBox.addGetAction(new ElementAction(situationBox, "Get") {
			public void actionPerformed(ActionEvent evt) {
				refreshSituations();
			}
		});

		// Add GUI elements to handle queries
		queryBox = new ElementBox<Query>(mainFrame, "Queries", curQueries);
		queryBox.addAddAction(new ElementAction(queryBox, "Add") {
			public void actionPerformed(ActionEvent evt) {
				if (queryFrame == null) {
					queryFrame = new QueryCreatorFrame(self, caller);
				} else {
					queryFrame.toFront();
				}
			}
		});
		queryBox.addRemoveAction(new ElementAction(queryBox, "Remove") {
			public void actionPerformed(ActionEvent evt) {
				removeSelectedQuery();
				refreshQueries();
			}
		});
		queryBox.addGetAction(new ElementAction(queryBox, "Get") {
			public void actionPerformed(ActionEvent evt) {
				refreshQueries();
			}
		});

		// Add GUI elements to handle rules
		ruleBox = new ElementBox<Rule>(mainFrame, "Rules", curRules);
		ruleBox.addAddAction(new ElementAction(ruleBox, "Add") {
			public void actionPerformed(ActionEvent evt) {
				if (ruleFrame == null) {
					ruleFrame = new RuleCreatorFrame(self, caller);
				} else {
					ruleFrame.toFront();
				}
			}
		});
		ruleBox.addRemoveAction(new ElementAction(ruleBox, "Remove") {
			public void actionPerformed(ActionEvent evt) {
				removeSelectedRule();
				refreshRules();
			}
		});
		ruleBox.addGetAction(new ElementAction(ruleBox, "Get") {
			public void actionPerformed(ActionEvent evt) {
				refreshRules();
			}
		});
		mainFrame.pack();
	}

	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
