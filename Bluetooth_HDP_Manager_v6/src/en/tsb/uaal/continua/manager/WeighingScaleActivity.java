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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

//Main activity
public class WeighingScaleActivity extends Activity implements OnClickListener {

	// Attributes

	/** HDP manager */
	private HdpManager hdpManager = null;

	/** Toggle button */
	private ToggleButton toggleButtonBloodPressure = null;

	// Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_weighing_scale);
		toggleButtonBloodPressure = (ToggleButton)findViewById(R.id.toggleButtonBloodPressure);
		toggleButtonBloodPressure.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(((ToggleButton)v).isChecked()) {
			// Start a new health measurement
			hdpManager = new HdpManager(toggleButtonBloodPressure);
			if(hdpManager.isHdpManagerReady()) {
				hdpManager.registerApp(Constants.MDEP_DATA_TYPE_WEIGHING_SCALE);
			}
		} else {
			// Stop a new health measurement
			if(hdpManager.isHdpManagerReady()) {
				hdpManager.unregisterApp();
			}
		}
	}
}