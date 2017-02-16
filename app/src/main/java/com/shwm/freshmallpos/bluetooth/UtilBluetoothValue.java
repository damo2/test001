package com.shwm.freshmallpos.bluetooth;

public class UtilBluetoothValue {
	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_WEIGHT = 6;// 称重
	// Intent request codes
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
	// Key names received from the BluetoothService Handler
	public static final String DEVICE = "device";
	public static final String TOAST = "toast";

	/** 设置默认大小 */
	public static final int Text_Size_default = 0;
	/** 设置大字体 */
	public static final int Text_Size_large2 = 1;
	/** 设置更大字体 */
	public static final int Text_Size_large6 = 2;

	public static final String BluetoothFileName = "mybluetoothfile0";

	public static final String Bluetooth_Device = "device";
	public static final String Bluetooth_Name = "name";
	public static final String Bluetooth_Address = "addr";
	public static final String Bluetooth_Uuids = "uuid";
	public static final String Bluetooth_Type = "type";
}
