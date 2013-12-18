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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.bluetooth.BluetoothHealthCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ToggleButton;

//Main activity
public class HdpManager extends BluetoothHealthCallback implements ServiceListener {
	
	// Attributes	
	
	/** TAG for debugging */
	private static final String TAG ="uAAL";	
	
	/** Bluetooth objects */
	private BluetoothAdapter localBluetoothAdapter = null;
	private BluetoothHealthAppConfiguration bluetoothHealthAppConfig = null;
	private BluetoothHealthAppConfiguration bluetoothHealthAppConfigRx = null;
	private BluetoothHealth bluetoothHealthProxy = null;
	private Context applicationContext = null;
	
	/** HDP channel ID */
	private int hdpChannelID = -1;	
	
	/** HDP availability */
	private boolean hdpReady = false;	
	
	/** Toggle button */
	private ToggleButton toggleButtonView = null;
	
	/** HDP reader */
	private ReadHdpData hdpReader = null;
	
	/** Asyntask thread handler */
	private Handler handler = null;
	
	// Constructor
	public HdpManager(ToggleButton tb) {		
		localBluetoothAdapter = MainActivity.localBluetoothAdapter;
		applicationContext = MainActivity.applicationContext;
		if((localBluetoothAdapter != null)&&(applicationContext != null)) {			
			localBluetoothAdapter.getProfileProxy(applicationContext,this,BluetoothProfile.HEALTH);
			toggleButtonView = tb;
			hdpReady = true;
		}
	}
	
	// Methods	
	
	/** Check HDP availability */
	public boolean isHdpManagerReady() {
		return hdpReady;
	}
	
	/** Register health application */
	public void registerApp(int dataType) {		
//		Log.d(TAG, "Register HDP app method");
		if(bluetoothHealthProxy != null)
			bluetoothHealthProxy.registerSinkAppConfiguration(TAG,dataType,this);
		hdpReady = false;
	}

	/** Connect HDP channel with a valid file descriptor */ 
	public void connectChannel() {  		
//		Log.d(TAG, "Connect channel HDP app method");	
		if(bluetoothHealthProxy != null)
			bluetoothHealthProxy.connectChannelToSource(MainActivity.selectedBluetoothRemoteDevice,bluetoothHealthAppConfig);        
	}

	/** Unregister health application */
	public void unregisterApp() {		
		Log.d(TAG, "Unregister HDP app method");
		if(bluetoothHealthProxy != null) {
			bluetoothHealthProxy.unregisterAppConfiguration(bluetoothHealthAppConfig);
			bluetoothHealthProxy = null;
			if(hdpReader != null) {
				hdpReader.cancel(true);
				hdpReader = null;
			}
			hdpReady = true;
		}		
	}

	/** Disconnect HDP channel */
	public void disconnectChannel() {		
//		Log.d(TAG, "Disconnect channel HDP app method");
		if(bluetoothHealthProxy != null)
			bluetoothHealthProxy.disconnectChannel(MainActivity.selectedBluetoothRemoteDevice,bluetoothHealthAppConfig,hdpChannelID);
		hdpReady = false;
	}
	
	/** Callback to inform change in registration state of the health application */
	public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config,int status) {		
		if(status == BluetoothHealth.APP_CONFIG_REGISTRATION_FAILURE) { 
			bluetoothHealthAppConfig = null;
//			Log.d(TAG,"APP_CONFIG_REGISTRATION_FAILURE");
		} else if(status == BluetoothHealth.APP_CONFIG_REGISTRATION_SUCCESS) {
			bluetoothHealthAppConfig = config;	
//			Log.d(TAG,"APP_CONFIG_REGISTRATION_SUCCESS");
		} else if (status == BluetoothHealth.APP_CONFIG_UNREGISTRATION_FAILURE) {
			bluetoothHealthAppConfig = null;	
//			Log.d(TAG,"APP_CONFIG_UNREGISTRATION_FAILURE");
		} else if (status == BluetoothHealth.APP_CONFIG_UNREGISTRATION_SUCCESS) { 
			bluetoothHealthAppConfig = null;
//			Log.d(TAG,"APP_CONFIG_UNREGISTRATION_SUCCESS");
		}
	}		

	/**  Callback to inform change in channel state */
	public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config,BluetoothDevice device,
			int prevState,int newState,ParcelFileDescriptor fd,int channelId) {	
		final ParcelFileDescriptor pfd = fd; 
		bluetoothHealthAppConfigRx = config;
		handler = new Handler(Looper.getMainLooper());
		// Remember:
		// 0 - Disconnected | 1 - Connecting | 2 - Connected | 3 - Disconnecting		
		if(newState == BluetoothHealth.STATE_CHANNEL_CONNECTED && prevState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED) {		
			if(bluetoothHealthAppConfigRx.equals(bluetoothHealthAppConfig)) {				
				hdpChannelID = channelId;		
				handler.post(new Runnable() {
				    public void run() {
				    	hdpReader = new ReadHdpData(toggleButtonView,getHdpManagerReference());
						hdpReader.execute(pfd);
				    }
				});				
			}		
		} else if((newState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED)&& prevState == BluetoothHealth.STATE_CHANNEL_CONNECTED) {		
			if(bluetoothHealthAppConfigRx.equals(bluetoothHealthAppConfig)) {				
				unregisterApp();				
			}			
		} 
	}
	
	/************************************************************************************************************/
	/** ServiceListener methods interface */
	/************************************************************************************************************/

	/** onServiceConnected */
	public void onServiceConnected(int profile,BluetoothProfile proxy) {	
		Log.d(TAG, "onServiceConnected by ServiceListener");
		if(profile == BluetoothProfile.HEALTH) 
			bluetoothHealthProxy = (BluetoothHealth) proxy;        
	}

	/** onServiceDisconnected */
	public void onServiceDisconnected(int profile) {
		Log.d(TAG, "onServiceDisconnected by ServiceListener");
		if(profile == BluetoothProfile.HEALTH) 
			bluetoothHealthProxy = null;		
	}	
	
	/** Get Hdp manager reference */
	private HdpManager getHdpManagerReference() {
		return this;
	}
}