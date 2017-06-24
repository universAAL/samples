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
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Log;

// Main class
public class WriteHdpData extends Thread {

	// Attributes
	/** Tag for debbuging/logging */
	private static final String TAG = "uAAL";

	/** File descriptor identifier */
	private FileDescriptor fileDescriptor;

	/** File output stream from manager/sink */
	private FileOutputStream fos = null;

	/** Protocol state */
	private int protocolState = -1;

	// Constructor
	public WriteHdpData(FileDescriptor fd,int c) {
		super();
		fileDescriptor = fd;
		protocolState = c;
	}

	// Methods

	@Override
	public void run() {
		fos = new FileOutputStream(fileDescriptor);
		try {
			switch(protocolState) {
			case Constants.COMMUNICATION_ASSOCIATION_REQUEST:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Association response (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_association_response_unknown_config,Constants.data_association_response_unknown_config.length);
				fos.write(Constants.data_association_response_unknown_config);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Get all MDS attributes request (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_get_MDS_attributes_request,Constants.data_get_MDS_attributes_request.length);
				fos.write(Constants.data_get_MDS_attributes_request);
				break;
			case Constants.COMMUNICATION_CONFIGURATION_INFORMATION_EXCHANGE:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Remote operation response event report configuration (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_remote_operation_response_event_report_config,Constants.data_remote_operation_response_event_report_config.length);
				fos.write(Constants.data_remote_operation_response_event_report_config);
				break;
			case Constants.COMMUNICATION_RESPONSE_AGENT_INITIATED_MEASUREMENT_DATA_TRANSMISSION:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Response to agent initiated measurement data transmission (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_response_agent_initiated_measurement_data_transmission,Constants.data_response_agent_initiated_measurement_data_transmission.length);
				fos.write(Constants.data_response_agent_initiated_measurement_data_transmission);
				break;
			case Constants.COMMUNICATION_MDS_ATTRIBUTES_REQUEST:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Get all MDS attributes request (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_get_MDS_attributes_request,Constants.data_get_MDS_attributes_request.length);
				fos.write(Constants.data_get_MDS_attributes_request);
				break;
			case Constants.COMMUNICATION_DATA_RELEASE_REQUEST:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Response to data release request (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_association_release_response,Constants.data_association_release_response.length);
				fos.write(Constants.data_association_release_response);
				break;
			case Constants.COMMUNICATION_ABORT_PROCESS:
				Log.d(TAG,"===========================================================");
				Log.d(TAG,"Response to abort communication process (manager -> agent)");
				Log.d(TAG,"===========================================================");
				Constants.showReceivedData(Constants.data_abort,Constants.data_abort.length);
				fos.write(Constants.data_abort);
				break;
			default:
				break;
			}
		} catch(IOException ex) {}
		// Close
		try {
			if(fos != null)
				fos.close();
		} catch (IOException e) {}
	}
}