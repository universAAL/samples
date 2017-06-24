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
import android.util.Log;

// Main class
public class Constants {

	// Attributes

	/** Tag for debbuging/logging */
	public static final String TAG = "uAAL";

	/** Config device ID and recived HDP frame ID */
	public static byte highByteConfigDeviceID = (byte) 0x02;
	public static byte lowByteConfigDeviceID = (byte) 0xBC;
	public static byte highByteRxHdpFrameID = (byte) 0x00;
	public static byte lowByteRxHdpFrameID = (byte) 0x02;

	/** Bluetooth standard */
	public static final String BT_BASE_UUID = "-0000-1000-8000-00805F9B34FB";
	public static final int SERVICE_CLASS_ID_HDP_SOURCE = 0x1401;
	public static final int SERVICE_CLASS_ID_HDP_SINK = 0x1402;
	public static final int MDEP_DATA_TYPE_PULSE_OXIMETER = 0x1004;
	public static final int MDEP_DATA_TYPE_BLOOD_PRESSURE_MONITOR = 0x1007;
	public static final int MDEP_DATA_TYPE_THERMOMETER = 0x1008;
	public static final int MDEP_DATA_TYPE_WEIGHING_SCALE = 0x100F;
	public static final int MDEP_DATA_TYPE_GLUCOSE_METER = 0x1011;

	/** Continua devices in a user friendly name */
	public static final String CONTINUA_DEVICE_PULSE_OXIMETER ="Pulse oximeter";
	public static final String CONTINUA_DEVICE_ECG ="Electrocardiograph";
	public static final String CONTINUA_DEVICE_BLOOD_PRESSURE_MONITOR ="Blood pressure monitor";
	public static final String CONTINUA_DEVICE_THERMOMETER ="Thermometer";
	public static final String CONTINUA_DEVICE_WEIGHTING_SCALE ="Weighing scale";
	public static final String CONTINUA_DEVICE_GLUCOSE_METER ="Glucosemeter";
	public static final String CONTINUA_DEVICE_INR_MONITOR ="INR monitor";
	public static final String CONTINUA_DEVICE_INSULINE_PUMP ="Insuline pump";
	public static final String CONTINUA_DEVICE_BODY_COMPOSITION_ANALYZER ="Body composition analyzer";
	public static final String CONTINUA_DEVICE_PEAK_FLOW_MONITOR ="Peak flow monitor";
	public static final String CONTINUA_DEVICE_CARDIOVASCULAR_FITNESS ="Cardiovascular fitness";
	public static final String CONTINUA_DEVICE_STRENGTH_FITNESS_EQUIPMENT ="Strength fitness equipment";
	public static final String CONTINUA_DEVICE_PHYSICAL_ACTIVITY_MONITOR ="Physical activity monitor";
	public static final String CONTINUA_DEVICE_INDEPENDENT_LIVING_ACTIVITY_HUB ="Independent living activity hub";
	public static final String CONTINUA_DEVICE_MEDICATION_MONITOR ="Medication monitor";

	/** Choice and event types words */
	public static final byte CHOICE_REMOTE_OPERATION_INVOKE_CONFIRMED_EVENT_REPORT_LOW_BYTE = (byte) 0x01;
	public static final byte CHOICE_REMOTE_OPERATION_INVOKE_CONFIRMED_EVENT_REPORT_HIGH_BYTE = (byte) 0x01;
	public static final byte CHOICE_REMOTE_OPERATION_RESPONSE_GET_LOW_BYTE = (byte) 0x02;
	public static final byte CHOICE_REMOTE_OPERATION_RESPONSE_GET_HIGH_BYTE = (byte) 0x03;
	public static final byte CHOICE_REMOTE_OPERATION_RESPONSE_CONFIRMED_ACTION_LOW_BYTE = (byte) 0x02;
	public static final byte CHOICE_REMOTE_OPERATION_RESPONSE_CONFIRMED_ACTION_HIGH_BYTE = (byte) 0x07;
	public static final byte EVENT_TYPE_MDC_NOTI_CONFIG_LOW_BYTE = (byte) 0x0D;
	public static final byte EVENT_TYPE_MDC_NOTI_CONFIG_HIGH_BYTE = (byte) 0x1C;
	public static final byte EVENT_TYPE_MDC_ATTR_SYS_TYPE_SPEC_LIST_LOW_BYTE = (byte) 0x0A;
	public static final byte EVENT_TYPE_MDC_ATTR_SYS_TYPE_SPEC_LIST_HIGH_BYTE = (byte) 0x5A;
	public static final byte EVENT_TYPE_MCD_NOTI_SCAN_REPORT_FIXED_LOW_BYTE = (byte) 0x0D;
	public static final byte EVENT_TYPE_MCD_NOTI_SCAN_REPORT_FIXED_HIGH_BYTE = (byte) 0x1F;

	/** HDP frames: manager responses */

	public static final byte data_association_response_known_config[] = new byte[] {
		(byte) 0xE3, (byte) 0x00, // choice APDU (aare apdu)
		(byte) 0x00, (byte) 0x2C, // choice length = 44
		(byte) 0x00, (byte) 0x00, // result accepted with unknown configuration
		(byte) 0x50, (byte) 0x79, // data protocol id 20601
		(byte) 0x00, (byte) 0x26, // data protocol info length = 38
		(byte) 0x40, (byte) 0x00, // protocol version
		(byte) 0x00, (byte) 0x00,
		(byte) 0x80, (byte) 0x00, // encoding rules (MDER)
		(byte) 0x80, (byte) 0x00, // nomenclature version
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, // functional units
		(byte) 0x00, (byte) 0x00,
		(byte) 0x80, (byte) 0x00, // system type (manager)
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x08, // system id length = 8
		(byte) 0x4D, (byte) 0x61, // system id value
		(byte) 0x6E, (byte) 0x61,
		(byte) 0x67, (byte) 0x65,
		(byte) 0x72, (byte) 0x31,
		(byte) 0x00, (byte) 0x00, // manager response to configuration id (always 0)
		(byte) 0x00, (byte) 0x00, // manager response to data-req-mode-capab (always 0)
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, // option list count = 0
		(byte) 0x00, (byte) 0x00 }; // option list.length = 0

	public static final byte data_association_response_unknown_config[] = new byte[] {
		(byte) 0xE3, (byte) 0x00, // choice APDU (aare apdu)
		(byte) 0x00, (byte) 0x2C, // choice length = 44
		(byte) 0x00, (byte) 0x03, // result accepted with unknown configuration
		(byte) 0x50, (byte) 0x79, // data protocol id 20601
		(byte) 0x00, (byte) 0x26, // data protocol info length = 38
		(byte) 0x40, (byte) 0x00, // protocol version
		(byte) 0x00, (byte) 0x00,
		(byte) 0x80, (byte) 0x00, // encoding rules (MDER)
		(byte) 0x80, (byte) 0x00, // nomenclature version
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, // functional units
		(byte) 0x00, (byte) 0x00,
		(byte) 0x80, (byte) 0x00, // system type (manager)
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x08, // system id length = 8
		(byte) 0x4D, (byte) 0x61, // system id value
		(byte) 0x6E, (byte) 0x61,
		(byte) 0x67, (byte) 0x65,
		(byte) 0x72, (byte) 0x31,
		(byte) 0x00, (byte) 0x00, // manager response to configuration id (always 0)
		(byte) 0x00, (byte) 0x00, // manager response to data-req-mode-capab (always 0)
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, // option list count = 0
		(byte) 0x00, (byte) 0x00 }; // option list.length = 0

	public static final byte data_get_MDS_attributes_request[] = new byte[] {
		(byte) 0xE7, (byte) 0x00, // choice APDU (prst apdu)
		(byte) 0x00, (byte) 0x0E, // choice length = 14
		(byte) 0x00, (byte) 0x0C, // octet string = 12
		(byte) 0x00, (byte) 0x02, // start of data apdu MDER (invoke id = 0x3456)
		(byte) 0x01, (byte) 0x03, // remote operation invoke get
		(byte) 0x00, (byte) 0x06, // length = 6
		(byte) 0x00, (byte) 0x00, // MDS object
		(byte) 0x00, (byte) 0x00, // attribute id list count = 0
		(byte) 0x00, (byte) 0x00 }; // attribute id list length = 0

	public static byte data_remote_operation_response_event_report_config[] = new byte[] {
		(byte) 0xE7, (byte) 0x00, // choice APDU (prst apdu)
		(byte) 0x00, (byte) 0x16, // choice length = 22
		(byte) 0x00, (byte) 0x14, // octet string = 20
		(byte) 0x00, (byte) 0x01, // start of data apdu MDER (invoke id = 0x4321)
		(byte) 0x02, (byte) 0x01, // remote operation response. Confirmed event report
		(byte) 0x00, (byte) 0x0E, // length = 14
		(byte) 0x00, (byte) 0x00, // MDS object
		(byte) 0xFF, (byte) 0xFF, // current time = 0
		(byte) 0xFF, (byte) 0xFF,
		(byte) 0x0D, (byte) 0x1C, // event type = MDC_NOTI_CONFIG
		(byte) 0x00, (byte) 0x04, // event reply info length = 4
		highByteConfigDeviceID, lowByteConfigDeviceID, // config report response configuration report id = 0x4000
		(byte) 0x00, (byte) 0x00 }; // config report response configuration result = accepted configuration

	public static byte data_response_agent_initiated_measurement_data_transmission[] = new byte[] {
		(byte) 0xE7, (byte) 0x00, // choice APDU (prst apdu)
		(byte) 0x00, (byte) 0x12, // choice length = 18
		(byte) 0x00, (byte) 0x10, // octet length = 16
		highByteRxHdpFrameID, lowByteRxHdpFrameID, // start of data apdu MDER encoded (invoke id = 0x4321)
		(byte) 0x02, (byte) 0x01, // remote operation response. Confirmed event report
		(byte) 0x00, (byte) 0x0A, // length = 10
		(byte) 0x00, (byte) 0x00, // MDS object
		(byte) 0xFF, (byte) 0xFF, // current time = 0
		(byte) 0xFF, (byte) 0xFF,
		(byte) 0x0D, (byte) 0x1F, // event type = MDC_NOTI_SCAN_REPORT_FIXED
		(byte) 0x00, (byte) 0x00 }; // event reply info length = 0

	public static final byte data_remote_operation_invoke_confirmed_action_data_request[] = new byte[] {
		(byte) 0xE7, (byte) 0x00, // choice APDU (prst apdu)
		(byte) 0x00, (byte) 0x1E, // choice length = 30
		(byte) 0x00, (byte) 0x1C, // octet length = 28
		(byte) 0x76, (byte) 0x54, // start of data apdu MDER encoded (invoke id = 0x7654)
		(byte) 0x01, (byte) 0x07, // remote operation invoke. Confirmed action
		(byte) 0x00, (byte) 0x16, // length = 22
		(byte) 0x00, (byte) 0x00, // MDS object
		(byte) 0x0C, (byte) 0x1B, // action type = MDC_ACT_DATA_REQUEST
		(byte) 0x00, (byte) 0x10, // length = 16
		(byte) 0x01, (byte) 0x00, // data request id = 0x0100
		(byte) 0x84, (byte) 0x80, // data request mode = start mode single response
		(byte) 0x00, (byte) 0x00, // data request time = not used
		(byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, // data request person id = not used
		(byte) 0x00, (byte) 0x06, // data request class = MDC_MOC_VMO_METRIC_NU
		(byte) 0x00, (byte) 0x00, // data request handle list = not used
		(byte) 0x00, (byte) 0x00 }; // event reply info length = 0

	public static final byte data_association_release_response[] = new byte[] {
		(byte) 0xE5, (byte) 0x00, // choice APDU (rlre apdu)
		(byte) 0x00, (byte) 0x02, // choice length = 2
		(byte) 0x00, (byte) 0x00 }; // reason = normal

	public static final byte data_release_request[] = new byte[] {
		(byte) 0xE4, (byte) 0x00, // choice APDU (rlrq apdu)
		(byte) 0x00, (byte) 0x02, // choice length = 2
		(byte) 0x00, (byte) 0x00 }; // reason = normal

	public static final byte data_abort[] = new byte[] {
		(byte) 0xE6, (byte) 0x00, // choice APDU (abrt apdu)
		(byte) 0x00, (byte) 0x02, // choice length = 2
		(byte) 0x00, (byte) 0x00 };	// reason = normal

	/** HDP communication process */
	public static final int COMMUNICATION_ASSOCIATION_REQUEST = 0;
	public static final int COMMUNICATION_INVOKE_EVENT_REPORT_CONFIG = 1;
	public static final int COMMUNICATION_MDS_ATTRIBUTES_REQUEST = 2;
	public static final int COMMUNICATION_RESPONSE_AGENT_INITIATED_MEASUREMENT_DATA_TRANSMISSION = 3;
	public static final int COMMUNICATION_DATA_REQUEST = 4;
	public static final int COMMUNICATION_CONFIGURATION_INFORMATION_EXCHANGE = 5;
	public static final int COMMUNICATION_ABORT_PROCESS = 6;
	public static final int COMMUNICATION_DATA_RELEASE_REQUEST = 7;

	/** Event types */
	public static final int EVENT_TYPE_MDC_NOTI_CONFIG = 10;
	public static final int EVENT_TYPE_NOTI_SCAN_REPORT_FIXED = 11;
	public static final int EVENT_TYPE_MDC_ATTR_SYS_TYPE_SPEC_LIST = 12;

	// Methods

	/** Convert byte array to hex */
	public static String convertByteToHex(byte[] b) {
		final String HEX = "0123456789ABCDEF";
		final StringBuilder hex = new StringBuilder(2*b.length);
		for(final byte raw:b) {
			hex.append(HEX.charAt((raw & 0xF0) >> 4)).append(HEX.charAt((raw & 0x0F)));
		}
		return hex.toString();
	}

	/** Convert hex byte to integer */
	public static int convertHexByteToInt(byte high,byte low) {
		return (high << 8) + (low & 0xff);
	}

	/** Show hex data in a user-friendly way */
	public static void showReceivedData(byte[] b,int length) {
		String temp = convertByteToHex(b);
		int aux = 0;
		for(int i=0;i<=2*length;i++) {
			if(i%2 != 0) {
				Log.d(TAG,"Byte "+aux+": 0x"+temp.charAt(i-1)+temp.charAt(i));
				aux++;
			}
		}
	}

	/** Convert int to byte array */
	public static byte[] convertIntToByteArray(int data) {
	    return new byte[] {
	            (byte)(data >>> 24),
	            (byte)(data >>> 16),
	            (byte)(data >>> 8),
	            (byte)data};
	}

	/** Convert string in hex format to byte array */
	public static byte[] convertHexStringToByteArray(String s) {
	    byte data[] = new byte[s.length()/2];
	    for(int i=0;i < s.length();i+=2) {
	        data[i/2] = (Integer.decode("0x"+s.charAt(i)+s.charAt(i+1))).byteValue();
	    }
	    return data;
	}

	/** Set config device ID */
	public static void setConfigDeviceId(byte b1,byte b2) {
		byte[] temp = {b1,b2};
		highByteConfigDeviceID = convertHexStringToByteArray(convertByteToHex(temp))[0];
		lowByteConfigDeviceID = convertHexStringToByteArray(convertByteToHex(temp))[1];
		// Update byte array
		data_remote_operation_response_event_report_config[22] = highByteConfigDeviceID;
		data_remote_operation_response_event_report_config[23] = lowByteConfigDeviceID;
	}

	/** Set rx HDP frame ID */
	public static void setRxHdpFrameId(byte b1,byte b2) {
		byte[] temp = {b1,b2};
		highByteRxHdpFrameID = convertHexStringToByteArray(convertByteToHex(temp))[0];
		lowByteRxHdpFrameID = convertHexStringToByteArray(convertByteToHex(temp))[1];
		// Update byte array
		data_response_agent_initiated_measurement_data_transmission[6] = highByteRxHdpFrameID;
		data_response_agent_initiated_measurement_data_transmission[7] = lowByteRxHdpFrameID;

	}

	/** Set timestamp */
	public static String setTimestamp(byte hyear,byte lyear,byte month,byte day) {
		String output, temp;
		byte[] data = {day,month,hyear,lyear};
		temp = convertByteToHex(data);
		output = temp.substring(0,2)+"/"+temp.substring(2,4)+"/"+temp.substring(4,8);
		return output;
	}

	/** Set hour */
	public static String setHour(byte hour,byte min,byte sec,byte ms) {
		String output, temp;
		byte[] data = {hour,min,sec,ms};
		temp = convertByteToHex(data);
		output = temp.substring(0,2)+":"+temp.substring(2,4)+":"+temp.substring(4,6)+":"+temp.substring(6,8);
		return output;
	}

	/** Return the event type */
	public static int getEventType(byte b8,byte b9,byte b18,byte b19) {
		if(b8 == CHOICE_REMOTE_OPERATION_INVOKE_CONFIRMED_EVENT_REPORT_LOW_BYTE && b9 == CHOICE_REMOTE_OPERATION_INVOKE_CONFIRMED_EVENT_REPORT_HIGH_BYTE) {
			if(b18 == EVENT_TYPE_MDC_NOTI_CONFIG_LOW_BYTE && b19 == EVENT_TYPE_MDC_NOTI_CONFIG_HIGH_BYTE)
				return EVENT_TYPE_MDC_NOTI_CONFIG;
			if(b18 == EVENT_TYPE_MCD_NOTI_SCAN_REPORT_FIXED_LOW_BYTE && b19 == EVENT_TYPE_MCD_NOTI_SCAN_REPORT_FIXED_HIGH_BYTE)
				return EVENT_TYPE_NOTI_SCAN_REPORT_FIXED;
		} else if(b8 == CHOICE_REMOTE_OPERATION_RESPONSE_GET_LOW_BYTE && b9 == CHOICE_REMOTE_OPERATION_RESPONSE_GET_HIGH_BYTE) {
			 return EVENT_TYPE_MDC_ATTR_SYS_TYPE_SPEC_LIST;
		}
		return -1;
	}
}