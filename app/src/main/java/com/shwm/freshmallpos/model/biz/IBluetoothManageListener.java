package com.shwm.freshmallpos.model.biz;

import com.shwm.freshmallpos.inter.IBlueboothListener;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

public interface IBluetoothManageListener {
	void setActivity(Activity activity);

	void setOnBluetoothListener(IBlueboothListener iBlueboothListener);

	void initBluetooth();

	/**
	 * 使用蓝牙适配器启动设备
	 */
	void doDiscovery();

	void stopDiscovery();

	void connectBluetooth(BluetoothDevice divice);
}
