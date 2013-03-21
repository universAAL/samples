/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License .
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.helper.location.publisher;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.universAAL.ontology.location.indoor.RoomFunction;

/**
 * @author eandgrg
 *
 */
public class LocationPublisherGUI extends JFrame {

    private static final long serialVersionUID = 6611196953807109089L;
    private JPanel contentPane;
    private LocationContextPublisher lp;


    /**
     * Create the frame.
     */
    public LocationPublisherGUI(LocationContextPublisher lp) {
	this.lp=lp;
	initGUI();
    }

    private void initGUI() {
	setTitle("universAAL - LocationPublisherGUI");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 589, 411);
	contentPane = new JPanel();
	contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
		null, null));
	setContentPane(contentPane);
	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

	JPanel panel = new JPanel();
	contentPane.add(panel);
	panel.setLayout(new GridLayout(2, 1, 0, 0));

	JButton sleepingButton = new JButton("Sleeping Room");
	sleepingButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    lp.publishLocation(LocationContextPublisher.SLEEPING_ROOM, RoomFunction.SleepingRoom);
		}
	});
	panel.add(sleepingButton);

	JButton livingRoomButton = new JButton("Living Room");
	livingRoomButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		lp.publishLocation(LocationContextPublisher.LIVING_ROOM, RoomFunction.LivingRoom);
	    }
	});
	panel.add(livingRoomButton);

	JPanel panel_1 = new JPanel();
	contentPane.add(panel_1);
	panel_1.setLayout(new GridLayout(3, 1, 0, 0));

	JButton bathroomButton = new JButton("Bathroom");
	bathroomButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    lp.publishLocation(LocationContextPublisher.BATHROOM, RoomFunction.BathRoom);
		}
	});
	panel_1.add(bathroomButton);

	JButton kitchenButton = new JButton("Kitchen");
	kitchenButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    lp.publishLocation(LocationContextPublisher.KITCHEN, RoomFunction.Kitchen);
		}
	});
	panel_1.add(kitchenButton);

	JButton hobbyRoomButton = new JButton("Hobby Room");
	hobbyRoomButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    lp.publishLocation(LocationContextPublisher.HOBBY_ROOM, RoomFunction.HobbyRoom);
		}
	});
	panel_1.add(hobbyRoomButton);
    }

}
