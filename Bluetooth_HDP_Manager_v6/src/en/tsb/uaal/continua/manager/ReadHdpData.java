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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;
import android.widget.ToggleButton;

// Main class
public class ReadHdpData extends AsyncTask<ParcelFileDescriptor,Double,Boolean> {

	// Attributes
	/** Tag for debbuging/logging */
	private static final String TAG = "uAAL";

	/** File descriptor identifier */
	private FileDescriptor fileDescriptor;

	/** Input stream from agent/source */
	private FileInputStream fis = null;

	/** Received data from agent/source */
	private byte data[] = new byte[1024];

	/** Toggle button */
	private ToggleButton toggleButtonView = null;

	/** Aux */
	private byte aux = (byte) 0x00;

	/** HDP manager reference */
	private HdpManager hdpManager = null;

	/** Flags */
	private boolean e2Flag = true;
	private boolean e4Flag = true;
	private boolean e6Flag = true;
	private boolean e7MdcNotiConfigFlag = true;
	private boolean e7GetAllMdsAttFlag = true;

	/** Received data */

	// Constructor
	public ReadHdpData(ToggleButton tb,HdpManager hm) {
		toggleButtonView = tb;
		hdpManager = hm;
	}

	// Methods

	@SuppressLint("UseValueOf")
	@Override
	protected Boolean doInBackground(ParcelFileDescriptor... params) {
		Boolean output = null;
		fileDescriptor = params[0].getFileDescriptor();
		fis = new FileInputStream(fileDescriptor);
		try {
			while(fis.read(data) > -1) {
				if(isCancelled()) {
					output = new Boolean(false);
					break;
				} else {
					if(data[0] != (byte)0x00) {
						// Association request
						if((data[0] == (byte) 0xE2)&&(e2Flag)) {
							Log.d(TAG,"===========================================================");
							Log.d(TAG,"Association request (agent -> manager)");
							Log.d(TAG,"===========================================================");
							Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
							Constants.setConfigDeviceId(data[44],data[45]);
							(new WriteHdpData(fileDescriptor,Constants.COMMUNICATION_ASSOCIATION_REQUEST)).run();
							e2Flag = false;
							continue;
						} else if(data[0] == (byte) 0xE7) {
							// Check EVENT_TYPE signal and CHOICE frame
							switch(Constants.getEventType(data[8],data[9],data[18],data[19])) {
							// Configuration information exchange
							case Constants.EVENT_TYPE_MDC_NOTI_CONFIG:
								if(e7MdcNotiConfigFlag) {
									Log.d(TAG,"===========================================================");
									Log.d(TAG,"Remote operation invoke event report configuration (agent -> manager)");
									Log.d(TAG,"===========================================================");
									Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
									(new WriteHdpData(fileDescriptor,Constants.COMMUNICATION_CONFIGURATION_INFORMATION_EXCHANGE)).run();
								}
								e7MdcNotiConfigFlag = false;
								break;
								// Agent initiated measurement data transmission
							case Constants.EVENT_TYPE_NOTI_SCAN_REPORT_FIXED:
								if(data[7] != aux) {
									aux = data[7];
									Log.d(TAG,"===========================================================");
									Log.d(TAG,"Agent initiated measurement data transmission (agent -> manager)");
									Log.d(TAG,"===========================================================");
									Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
									int sys=Constants.convertHexByteToInt(data[44],data[45]);
									int dia=Constants.convertHexByteToInt(data[46],data[47]);
									int pul=Constants.convertHexByteToInt(data[62],data[63]);
									Log.d(TAG, "SYS: "+sys);
									Log.d(TAG, "DIA: "+dia);
									Log.d(TAG, "PUL: "+pul);
									Log.d(TAG, "AVE: "+Constants.convertHexByteToInt(data[48],data[49]));
									Log.d(TAG, "Timestamp: "+Constants.setTimestamp(data[50],data[51],data[52],data[53]));
									Log.d(TAG, "Hour: "+Constants.setHour(data[54],data[55],data[56],data[57]));
									Log.d(TAG,"===========================================================");
									BloodPressureMonitorActivity.sendEvent(pul, sys, dia);
									Constants.setRxHdpFrameId(data[6],data[7]);
									(new WriteHdpData(fileDescriptor,Constants.COMMUNICATION_RESPONSE_AGENT_INITIATED_MEASUREMENT_DATA_TRANSMISSION)).run();
								}
								break;
								// Get response with all MDS attributes
							case Constants.EVENT_TYPE_MDC_ATTR_SYS_TYPE_SPEC_LIST:
								if(e7GetAllMdsAttFlag) {
									Log.d(TAG,"===========================================================");
									Log.d(TAG,"Get all MDS attributes response (agent -> manager)");
									Log.d(TAG,"===========================================================");
									Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
								}
								e7GetAllMdsAttFlag = false;
								break;
							default:
								break;
							}
							continue;
						// Data release	request
						} else if((data[0] == (byte) 0xE4)&&(e4Flag)) {
							Log.d(TAG,"===========================================================");
							Log.d(TAG,"Data release request (agent -> manager)");
							Log.d(TAG,"===========================================================");
							Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
							(new WriteHdpData(fileDescriptor,Constants.COMMUNICATION_DATA_RELEASE_REQUEST)).run();
							output = new Boolean(true);
							e4Flag = false;
						// Abort communication process
						} else if((data[0] == (byte) 0xE6)&&(e6Flag)) {
							Log.d(TAG,"===========================================================");
							Log.d(TAG,"Abort communication process (agent -> manager)");
							Log.d(TAG,"===========================================================");
							Constants.showReceivedData(data,Constants.convertHexByteToInt(data[2],data[3])+4);
							(new WriteHdpData(fileDescriptor,Constants.COMMUNICATION_ABORT_PROCESS)).run();
							output = new Boolean(false);
							e6Flag = false;
						}
					}
				}
			}
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {}
		} catch(IOException ioe) {}
		// Output
		return output;
	}

	@Override
	protected void onPostExecute(Boolean b) {
		if(b) {
			//TODO update data app gui
		}
		// Flags ready again
		e2Flag = true;
		e4Flag = true;
		e6Flag = true;
		e7MdcNotiConfigFlag = true;
		e7GetAllMdsAttFlag = true;
		// Toggle button
		if(toggleButtonView != null) {
			if(toggleButtonView.isChecked()) {
				toggleButtonView.setChecked(false);
				toggleButtonView = null;
			}
		}
		// Unregister app
		if(hdpManager != null) {
			hdpManager.unregisterApp();
		}
	}

    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Double... values) {

    }

    @Override
    protected void onCancelled() {
    	// Toggle button
    	if(toggleButtonView != null) {
    		if(toggleButtonView.isChecked()) {
    			toggleButtonView.setChecked(false);
    			toggleButtonView = null;
    		}
    	}
    	try {
			if(fis != null) {
				fis.close();
			}
		} catch (IOException e) {}
    	if(hdpManager != null) {
			hdpManager.unregisterApp();
		}
    }
}