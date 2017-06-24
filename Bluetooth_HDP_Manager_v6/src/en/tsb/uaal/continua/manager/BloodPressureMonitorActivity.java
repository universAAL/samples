/**
 * Author:  Angel Martinez-Cavero
 * Email:   amartinez@tsbtecnologias.es
 * Version: 0
 * License: TSB
 *
 * */

// Package
package en.tsb.uaal.continua.manager;

//Imports
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ToggleButton;

//Main activity
public class BloodPressureMonitorActivity extends Activity implements OnClickListener {

	// Attributes

	/** HDP manager */
	private HdpManager hdpManager = null;

	/** Toggle button */
	private ToggleButton toggleButtonBloodPressure = null;

	/** EditText components */
	protected static EditText sysEditText = null, diaEditText = null, pulEditText = null;

	private static Activity reference=null;

	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_blood_pressure);
		reference=this;
		toggleButtonBloodPressure = (ToggleButton)findViewById(R.id.toggleButtonBloodPressure);
		sysEditText = (EditText) findViewById(R.id.sysEditText);
		diaEditText = (EditText) findViewById(R.id.diaEditText);
		pulEditText = (EditText) findViewById(R.id.pulEditText);
		toggleButtonBloodPressure.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(((ToggleButton)v).isChecked()) {
			// Start a new health measurement
			hdpManager = new HdpManager(toggleButtonBloodPressure);
			if(hdpManager.isHdpManagerReady()) {
				hdpManager.registerApp(Constants.MDEP_DATA_TYPE_BLOOD_PRESSURE_MONITOR);
			}
		} else {
			// Stop a new health measurement
			if(!hdpManager.isHdpManagerReady()) {
				hdpManager.unregisterApp();
			}
			// Reset editText components
			sysEditText.setText("");
			diaEditText.setText("");
			pulEditText.setText("");
		}
	}



	@Override
	protected void onDestroy() {
		reference=null;
		super.onDestroy();
	}

	public static void sendEvent(final int pul, final int sys, final int dia){
		System.out.println("Attempting to send intent");
		Intent broadcast = new Intent("en.tsb.uaal.continua.manager.ACTION_BLOODP_EVENT");
		broadcast.addCategory(Intent.CATEGORY_DEFAULT);
		broadcast.putExtra("heartrate_value", Integer.toString(pul));
		broadcast.putExtra("systolic_value", Integer.toString(sys));
		broadcast.putExtra("diastolic_value", Integer.toString(dia));
		if(reference!=null){
			reference.sendBroadcast(broadcast);
			reference.runOnUiThread(
					new Thread() {
						@Override
						public void run() {
							sysEditText.setText(Integer.toString(sys));
							diaEditText.setText(Integer.toString(dia));
							pulEditText.setText(Integer.toString(pul));
						}
					}
			);
		}else{
			System.out.println("Could not send measurement to uAAL");
		}

	}
}