/** 
 * Author:  Angel Martinez-Cavero
 * Email:   amartinez@tsbtecnologias.es
 * Version: 0
 * License: TSB
 * 
 * */

// Package
package en.tsb.uaal.continua.manager;

// Imports
import java.util.Set;
import java.util.Vector;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

// Main activity
public class MainActivity extends Activity implements OnClickListener {

	// Attributes
	/** System callback */
	private static final int REQUEST_ENABLE_BT = 1;	

	/** GUI components */
	private RadioGroup radioGroupMainActivityLayout = null;	
	private LinearLayout radioGroupParentLinearLayout = null;

	/** TAG for debugging */
	private static final String TAG ="uAAL_HDP_service";
	
	/** Application context */
	public static Context applicationContext = null;

	/** Bluetooth objects and attributes */
	
	// Objects
	public static BluetoothAdapter localBluetoothAdapter = null;
	private Set<BluetoothDevice> remoteBluetoothPairedDevices = null;
	public static BluetoothDevice selectedBluetoothRemoteDevice = null;
	private Vector<String> healthRemoteBluetoothPairedDevicesName = new Vector<String>();
	private Vector<String> healthRemoteBluetoothPairedDevicesMAC = new Vector<String>();
	private Vector<Integer> healthRemoteBluetoothPairedDevicesType = new Vector<Integer>();
	private Vector<String> healthRemoteBluetoothPairedDevicesRole = new Vector<String>();
	private Vector<Integer> healthRemoteBluetoothPairedDevicesMdepDataType = new Vector<Integer>();	
	private boolean hdpPairedDevices = false;	

	// Methods	
	public void onCreate(Bundle savedInstanceState) {				
		super.onCreate(savedInstanceState);			
		// Check for Bluetooth availability on the Android device
		localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();	
		applicationContext = getApplicationContext();		
		if(localBluetoothAdapter == null) {
			// Device does not support Bluetooth connections			
			abortContinuaManagerApp(R.string.toast_msg_bluetooth_not_available);
			setResult(RESULT_CANCELED);
		} else {			
			// We need to ensure that Bluetooth is enabled also
			if(!localBluetoothAdapter.isEnabled()) {
				// Bluetooth off
				Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBluetoothIntent,REQUEST_ENABLE_BT);							
			} else {
				// Bluetooth on
				checkHdp();
			}			
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();		
		if(radioGroupMainActivityLayout.getCheckedRadioButtonId() != -1) {
			radioGroupMainActivityLayout.clearCheck();			
		}	
	}

	@Override
	/** Called when Bluetooth switch on process finishes (with or without success) */
	protected void onActivityResult(int requestCode,int resultCode,Intent data) {		
		if((requestCode == REQUEST_ENABLE_BT)&&(resultCode == RESULT_OK))					
			checkHdp();								
		else						
			abortContinuaManagerApp(R.string.toast_msg_bluetooth_not_available);				
	}	

	/** Enabling Bluetooth process succeeds OR BT was activated from the beginning. Time to ensure that HDP 
	profile is supported now */
	public void checkHdp() {		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {			
			abortContinuaManagerApp(R.string.toast_msg_hdp_profile_not_supported);		
		} else {
			// Searching Continua agents paired devices
			getPairedDevicesList();		
			// Creating GUI
			if(hdpPairedDevices) {
				// Loading final layout due to Bluetooth is enabled and HDP profile supported
				setContentView(R.layout.main_activity_layout);				
				createGUI();				
			} else {
				abortContinuaManagerApp(R.string.toast_msg_hdp_not_paired_devices);
			}
		}		
	}			

	/** Searching Continua agents paired devices */
	public void getPairedDevicesList() {		
		remoteBluetoothPairedDevices = localBluetoothAdapter.getBondedDevices();
		// There are paired devices		
		if(remoteBluetoothPairedDevices.size() > 0) {								 
			// Loop through paired devices
			for(BluetoothDevice device:remoteBluetoothPairedDevices) {
				if(device.getBluetoothClass().getMajorDeviceClass() == BluetoothClass.Device.Major.HEALTH) {
					hdpPairedDevices = true;
					// Store main attributes: Friendly name, MAD address, device type and Continua role
					healthRemoteBluetoothPairedDevicesName.add(device.getName());
					healthRemoteBluetoothPairedDevicesMAC.add(device.getAddress());
//					Log.d(TAG, "MAC: "+device.getAddress().toString());
					healthRemoteBluetoothPairedDevicesType.add(device.getBluetoothClass().getDeviceClass());					
					ParcelUuid uuid[] = device.getUuids();
					for(int i=0;i<uuid.length;i++) {
						int aux = getRemoteDeviceRole(uuid[i].toString());
						if((aux != Constants.SERVICE_CLASS_ID_HDP_SOURCE)&&(aux != Constants.SERVICE_CLASS_ID_HDP_SINK))
							continue;
						else
							healthRemoteBluetoothPairedDevicesRole.add(aux == Constants.SERVICE_CLASS_ID_HDP_SOURCE?"Source":"Sink");
					}					
				}
			}
		}
	}	

	/** Returns the service class ID in a friendly way */
	public int getRemoteDeviceRole(String inputUuid) {   	
		if(inputUuid == null) 
			return -1;  
		else 
			return Integer.parseInt(inputUuid.substring(4,(inputUuid.length() - Constants.BT_BASE_UUID.length())),16);   		
	}

	/** Creates GUI according the number of Continua agents paired with the device */
	public void createGUI() {		
		// Radio group
		radioGroupMainActivityLayout = new RadioGroup(this);
		radioGroupMainActivityLayout.setOrientation(RadioGroup.VERTICAL);   		
		// Parent layout
		radioGroupParentLinearLayout = (LinearLayout) findViewById(R.id.appLinearLayout);   		
		radioGroupParentLinearLayout.addView(radioGroupMainActivityLayout);
		// Radio buttons
		for(int i=0;i<healthRemoteBluetoothPairedDevicesName.size();i++) {
			radioGroupMainActivityLayout.addView(getRadioButtonElement(i));
		}
		radioGroupMainActivityLayout.clearCheck();		
	}

	/** Creates Radio buttons elements with unique identifier and text */
	public RadioButton getRadioButtonElement(int id) {
		String text = healthRemoteBluetoothPairedDevicesName.get(id) + " - " +
				getHealthDeviceName(healthRemoteBluetoothPairedDevicesType.get(id));
		RadioButton aux = new RadioButton(this);		
		aux.setClickable(true);
		aux.setChecked(false);
		aux.setId(id);
		aux.setText(text);
		aux.setOnClickListener(this);
		return aux;
	}

	/** Returns the ID device name (friendly way) from minor device code detected */
	public String getHealthDeviceName(int minorCode) {
		//TODO complete with the full list. Only added those most representative
		switch(minorCode) {
		case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE: 
			healthRemoteBluetoothPairedDevicesMdepDataType.add(Constants.MDEP_DATA_TYPE_BLOOD_PRESSURE_MONITOR);
			return Constants.CONTINUA_DEVICE_BLOOD_PRESSURE_MONITOR;			
		case BluetoothClass.Device.HEALTH_THERMOMETER:
			healthRemoteBluetoothPairedDevicesMdepDataType.add(Constants.MDEP_DATA_TYPE_THERMOMETER);
			return Constants.CONTINUA_DEVICE_THERMOMETER;			
		case BluetoothClass.Device.HEALTH_WEIGHING:
			healthRemoteBluetoothPairedDevicesMdepDataType.add(Constants.MDEP_DATA_TYPE_WEIGHING_SCALE);
			return Constants.CONTINUA_DEVICE_WEIGHTING_SCALE;			
		case BluetoothClass.Device.HEALTH_GLUCOSE:
			healthRemoteBluetoothPairedDevicesMdepDataType.add(Constants.MDEP_DATA_TYPE_GLUCOSE_METER);
			return Constants.CONTINUA_DEVICE_GLUCOSE_METER;			
		case BluetoothClass.Device.HEALTH_PULSE_OXIMETER: 
			healthRemoteBluetoothPairedDevicesMdepDataType.add(Constants.MDEP_DATA_TYPE_PULSE_OXIMETER);
			return Constants.CONTINUA_DEVICE_PULSE_OXIMETER;				
		default: 
			return "Unknown device";			
		}
	}

	/** Output message. Abort due to any HDP Continua complication */
	public void abortContinuaManagerApp(int msg) {		
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
		finish();
		return;
	}	
	
	/** onClickListener method interface */
	public void onClick(View v) {		 
		RadioButton temp = (RadioButton) v;		
		if(getHealthDeviceName(healthRemoteBluetoothPairedDevicesType.get(temp.getId())).equals("Blood pressure monitor")) {			
			selectedBluetoothRemoteDevice = getSelectedRemoteBluetoothDevice(temp.getId());
			Intent intent = new Intent(this,BloodPressureMonitorActivity.class);
			startActivity(intent);
		} else if(healthRemoteBluetoothPairedDevicesType.get(temp.getId()).equals("Termometer")) {
			//TODO
		} else if(getHealthDeviceName(healthRemoteBluetoothPairedDevicesType.get(temp.getId())).equals("Weighing scale")) {			
			selectedBluetoothRemoteDevice = getSelectedRemoteBluetoothDevice(temp.getId());
			Intent intent = new Intent(this,WeighingScaleActivity.class);
			startActivity(intent); 
		} else if(healthRemoteBluetoothPairedDevicesType.get(temp.getId()).equals("Glucose meter")) {
			//TODO
		} else if(healthRemoteBluetoothPairedDevicesType.get(temp.getId()).equals("Pulse oximeter")) {
			//TODO
		} else if(healthRemoteBluetoothPairedDevicesType.get(temp.getId()).equals("Heart rate monitor")) {
			//TODO
		} else if(healthRemoteBluetoothPairedDevicesType.get(temp.getId()).equals("Heart rate display")) {
			//TODO
		}
	} 	

	/** Show HDP finite state machine status */
	public void showHdpProfileConnectionStatus(BluetoothAdapter lb) {	
		int output = -1;
		if(lb != null)
			output = localBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);		
		if(output == 0)
			Log.d(TAG, "HDP profile status: disconnected");
		else if(output == 1)
			Log.d(TAG, "HDP profile status: connecting");
		else if(output == 2)
			Log.d(TAG, "HDP profile status: connected");
		else
			Log.d(TAG, "HDP profile status: disconnecting");		
	}	
	
	/** Get selected remote Bluetooth device */
	public BluetoothDevice getSelectedRemoteBluetoothDevice(int id) {
		BluetoothDevice temp = null;
		int cont = 0;		
		for(BluetoothDevice device:remoteBluetoothPairedDevices) {			
			if(cont == id) {
				temp = device;
				break;
			}	
			cont++;			
		}				
		return temp;
	}
}	