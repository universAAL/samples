package org.universAAL.ri.gateway.support.android;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import org.universAAL.ontology.phThing.Device;




/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class LightClient extends javax.swing.JPanel {
	static private JButton onButton;
	private JTextField percent;
	private JButton scaleButton;
	private JButton getLampsButton;
	static private JList jList1;
	static private JButton offButton;
	

	private AbstractAction Scale;
	private AbstractAction Off;
	private AbstractAction On;
	private AbstractAction GetLamps;
	
	static {
		// Vadim - turn off the logging 
		java.util.logging.Logger.getLogger("sun").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("java").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("javax").setLevel(java.util.logging.Level.OFF);
	}
	
	// create the GUI 
	public void start(){

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Lamp Controller");
		frame.setEnabled(true);
		{
			onButton = new JButton();
			frame.getContentPane().add(onButton);
			onButton.setText("On");
			onButton.setBounds(50, 12, 80, 35);
			onButton.setAction(getOn());
		}
		{
			offButton = new JButton();
			frame.getContentPane().add(offButton);
			offButton.setText("Off");
			offButton.setBounds(160, 12, 80, 35);
			offButton.setAction(getOff());
		}
		{
			scaleButton = new JButton();
			frame.getContentPane().add(scaleButton);
			scaleButton.setText("Scale");
			scaleButton.setBounds(210, 62, 80, 35);
			scaleButton.setAction(getScale());
		}
		
		{
			percent = new JTextField();
			frame.getContentPane().add(percent);
			percent.setText("Percent");
			percent.setBounds(103, 69, 80, 21);
		}
		{
			getLampsButton = new JButton();
			frame.getContentPane().add(getLampsButton);
			getLampsButton.setText("Get Lamps");
			getLampsButton.setBounds(25, 120, 160, 35);
			getLampsButton.setAction(getGetLampsAction());
		}
		{
			ListModel jList1Model = 
				new DefaultComboBoxModel();
			jList1 = new JList();
			frame.getContentPane().add(jList1);
//			frame.getContentPane().add(getScaleButton());
//			frame.getContentPane().add(getPercent());
			jList1.setModel(jList1Model);
			jList1.setBounds(25, 170, 400, 170);
			
		}
	}



	/**
	 * @return
	 */
	private ListModel getLampsListModel() {
		Device[] d = LightingConsumer.getControlledLamps();
	
		String[] lamps = new String[d != null ? d.length:0];
		for(int i =0; i < lamps.length; i++){
			lamps[i] = d[i].getURI();
		}
		
		// Sort the list
		Arrays.sort(lamps);
		
		ListModel jList1Model = 
			new DefaultComboBoxModel(
					lamps);
		return jList1Model;
	}
	

	
	/**
	 * @return
	 */
	private Action getGetLampsAction() {
		if(GetLamps == null) {
			GetLamps = new AbstractAction("Get Lamps", null) {
				public void actionPerformed(final ActionEvent evt) {
					ListModel jList1Model = getLampsListModel();
					jList1.setModel(jList1Model);
				}
			};
		}
		return GetLamps;
	}



	public LightClient() {
		super();	
		initGUI();
		start();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Auto-generated method for setting the popup menu for a component
	*/
	private void setComponentPopupMenu(final java.awt.Component parent, final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(final java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			@Override
			public void mouseReleased(final java.awt.event.MouseEvent e) {
				if(e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}
	

	
	private AbstractAction getOn() {
		if(On == null) {
			On = new AbstractAction("On", null) {
				public void actionPerformed(final ActionEvent evt) {
					LightingConsumer.turnOn((String)jList1.getSelectedValue());
				}
			};
		}
		return On;
	}
	
	private AbstractAction getOff() {
		if(Off == null) {
			Off = new AbstractAction("Off", null) {
				public void actionPerformed(final ActionEvent evt) {
					LightingConsumer.turnOff((String)jList1.getSelectedValue());
				}
			};
		}
		return Off;
	}
	

	private AbstractAction getScale() {
		if(Scale == null) {
			Scale = new AbstractAction("Scale", null) {
				public void actionPerformed(final ActionEvent evt) {
					String lamp = (String)jList1.getSelectedValue();
					String svalue = percent.getText();
					int ivalue = Integer.valueOf( svalue ).intValue();
					LightingConsumer.dimToValue(lamp, new Integer(ivalue));
					
				}
			};
		}
		return Scale;
	}

}
