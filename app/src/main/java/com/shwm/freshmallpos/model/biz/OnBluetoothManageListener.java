package com.shwm.freshmallpos.model.biz;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.bluetooth.UtilBluetoothValue;
import com.shwm.freshmallpos.inter.IBlueboothListener;

public class OnBluetoothManageListener implements IBluetoothManageListener {
	private String TAG = getClass().getSimpleName();
	private WeakReference<Activity> mActivityReference;
	private IBlueboothListener iBlueboothListener;
	private BluetoothDevice mBluetoothDivceConnect;
	private boolean isSaomiao;
	private HashMap<String, BluetoothDevice> blueboothMap = new HashMap<String, BluetoothDevice>();
	/** 请求配对 */
	private final static String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	/** 取消配对 */
	private static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";
	private BluetoothAdapter mBtAdapter;
	private BluetoothService mService = null;

	@Override
	public void setOnBluetoothListener(IBlueboothListener iBlueboothListener) {
		// TODO Auto-generated method stub
		this.iBlueboothListener = iBlueboothListener;
	}

	@Override
	public void setActivity(Activity activity) {
		// TODO Auto-generated method stub
		mActivityReference = new WeakReference<Activity>(activity);
	};

	@Override
	public void connectBluetooth(BluetoothDevice device) {
		// TODO Auto-generated method stub
		mService.connect(device);
	}

	@Override
	public void initBluetooth() {
		// TODO Auto-generated method stub
		if (mActivityReference.get() == null) {
			return;
		}
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			if (iBlueboothListener != null) {
				iBlueboothListener.onNotSupported();
			}
			return;
		}
		registerReceiver();
		if (!mBtAdapter.isEnabled()) {
			// 打开蓝牙
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mActivityReference.get().startActivityForResult(enableIntent, UtilBluetoothValue.REQUEST_ENABLE_BT);
		}
		mService = BluetoothService.getInstance(mActivityReference.get(), mHandler);

	}

	// 注册Receiver来获取蓝牙设备相关的结果
	private void registerReceiver() {
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);// 设备连接状态改变的广播
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intent.addAction(ACTION_PAIRING_REQUEST);// 自動匹配的广播
		intent.addAction(ACTION_PAIRING_CANCEL);// 自動匹配的广播
		mActivityReference.get().registerReceiver(mReceiver, intent);
	}

	@Override
	public void doDiscovery() {
		// TODO Auto-generated method stub
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// 蓝牙适配器请求找到的
		mBtAdapter.startDiscovery();
		isSaomiao = true;
	}

	@Override
	public void stopDiscovery() {
		mBtAdapter.cancelDiscovery();
		isSaomiao = false;
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {// 获得已经搜索到的蓝牙设备
				blueboothMap.put(device.getAddress(), device);
				// Get the BluetoothDevice object from the Intent
				// 没有配对的蓝牙设备
				// if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
				// // 列表里不存在
				// if (!deviceOther.containsKey(device.getAddress())) {
				// listOtherDevices.add(device);
				// }
				// }
				// // Toast.makeText(getApplicationContext(), "搜索到设备" + device.getName(), Toast.LENGTH_SHORT).show();
				// mOtherDevicesAdapter.setData(listOtherDevices);
				// mOtherDevicesAdapter.notifyDataSetChanged();
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// Toast.makeText(getApplicationContext(), "没有匹配的设备", Toast.LENGTH_SHORT).show();
				stopDiscovery();
			} else if (ACTION_PAIRING_REQUEST.equals(action)) {// 请求匹配设备
				// int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
				// Log.d(TAG, "ACTION_PAIRING_REQUEST-" + type);
				// PAIRING_VARIANT_CONSENT 3 Hingmed WBP
				// if (type == BluetoothDevice.PAIRING_VARIANT_PIN) {
				// device.setPin(strPin.getBytes());// 弹框后自动输入密码、自动确定
				// } else if (type == BluetoothDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION) {
				device.setPairingConfirmation(true);
				// }
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {// 设备状态改变
				int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
				int previousBondState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, 0);// 远程设备以前的匹配状态
				if (bondState == BluetoothDevice.BOND_BONDED) {
					Log.d(TAG, "配对成功：" + device.getName());
					// setPaireDevice();
					// blueboothMap.put(device.getAddress(), TypeBondEnd);
					// listOtherDevices.remove(device);
					// mOtherDevicesAdapter.notifyDataSetChanged();
					// 配对成功 开始连接
					connectBluetooth(device);
					// // if (isRunning)
					// // showDialogTitle("配对成功,开始连接...");
					// tvDeviceConnect.setText("正在连接...");
				} else if (bondState == BluetoothDevice.BOND_NONE) {
					Log.d(TAG, "取消配对：" + device.getName());
					// setPaireDevice();
					// mapOther.put(device.getAddress(), TypeBondEnd);
					// mOtherDevicesAdapter.notifyDataSetChanged();

				} else if (bondState == BluetoothDevice.BOND_BONDING) {
					Log.d(TAG, "配对中：" + device.getName());
					// mapOther.put(device.getAddress(), TypeBonding);
					// mOtherDevicesAdapter.notifyDataSetChanged();
				} else {
					Log.d(TAG, "未知：" + device.getName());
					// mapOther.put(device.getAddress(), TypeBondEnd);
					// mOtherDevicesAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UtilBluetoothValue.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					// mBluetoothDivceConnect = mService.getmBluetoothDivceConnect();
					// if (mBluetoothDivceConnect != null) {
					// // Toast.makeText(getApplicationContext(), "以连接至" + mBluetoothDivceConnect.getName(),
					// // Toast.LENGTH_SHORT).show();
					// tvDeviceConnect.setText(mBluetoothDivceConnect.getName() + "\n" +
					// mBluetoothDivceConnect.getAddress());
					// }
					// if (isRunning)
					// showDialogSuccessDismiss("成功", "以连接至:" + mBluetoothDivceConnect.getName(), new
					// OnSweetClickListener() {
					// @Override
					// public void onClick(SweetAlertDialog sweetAlertDialog) {
					// // TODO Auto-generated method stub
					// BluetoothListActivity.this.finish();
					// }
					// });
					break;
				case BluetoothService.STATE_CONNECTING:
					// if (isRunning)
					// showDialogTitle("正在连接...");
					// mBluetoothDivceConnect = null;
					// tvDeviceConnect.setText("正在连接...");
					// Toast.makeText(getApplicationContext(), "正在连接...", Toast.LENGTH_SHORT).show();
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					// if (isRunning)
					// showDialogFail("连接失败", "无连接");
					// mBluetoothDivceConnect = null;
					// tvDeviceConnect.setText("无连接");
					break;
				}
				break;
			case UtilBluetoothValue.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				// mBluetoothDivceConnect = msg.getData().getParcelable(UtilBluetoothValue.DEVICE);
				break;
			case UtilBluetoothValue.MESSAGE_TOAST:
				// mBluetoothDivceConnect = null;
				// tvDeviceConnect.setText("连接失败");
				// if (isRunning) {
				// // showDialogFail("连接失败", msg.getData().getString(UtilBluetoothValue.TOAST));
				// // Toast.makeText(getApplicationContext(), msg.getData().getString(UtilBluetoothValue.TOAST),
				// // Toast.LENGTH_SHORT).show();
				// }
				break;

			case UtilBluetoothValue.MESSAGE_READ:
				// byte[] data = toByteArray(msg.obj);
				// if (data != null && data.length > 0) {
				// final StringBuilder stringBuilder = new StringBuilder(data.length);
				// for (byte byteChar : data)
				// stringBuilder.append(String.format("%02X ", byteChar));
				// // Log.d(TAG, "读取16进制->" + stringBuilder.toString());
				// // Log.d(TAG, "读取String->" + new String(data));
				// }
				// break;
			}
		}
	};

	protected void onDestoryPresenter() {
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		if (mActivityReference.get() != null) {
			mActivityReference.get().unregisterReceiver(mReceiver);
		}
	}

}
