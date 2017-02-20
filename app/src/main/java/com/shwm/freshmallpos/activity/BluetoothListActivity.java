package com.shwm.freshmallpos.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CommonAdapter;
import com.shwm.freshmallpos.adapter.ViewHolder;
import com.shwm.freshmallpos.bluetooth.BluetoothEntity;
import com.shwm.freshmallpos.bluetooth.BluetoothSave;
import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.bluetooth.UtilBluetoothCls;
import com.shwm.freshmallpos.bluetooth.UtilBluetoothValue;
import com.shwm.freshmallpos.myview.MyListViewScroll;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.ListUtil;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.base.BaseActivity;

public class BluetoothListActivity extends BaseActivity {
	private String title;
	// Debugging
	private static final String TAG = "DeviceListActivity";
	private TextView tvNo;
	// private TextView tvDeviceConnect;
	private Button scanButton;
	private MyListViewScroll connectListView;
	private MyListViewScroll pairedListView;
	private MyListViewScroll newDevicesListView;
	private ProgressBar progressbarOther;

	private BluetoothAdapter mBtAdapter;

	private CommonAdapter<BluetoothDevice> mConnectsAdapter;
	private CommonAdapter<BluetoothDevice> mPairedDevicesAdapter;
	private CommonAdapter<BluetoothDevice> mOtherDevicesAdapter;

	private List<BluetoothEntity> listBluetoothSaveConnect = new ArrayList<BluetoothEntity>();
	private List<BluetoothDevice> listConnectDevices = new ArrayList<BluetoothDevice>();
	private List<BluetoothDevice> listPairedDevices = new ArrayList<BluetoothDevice>();
	private List<BluetoothDevice> listOtherDevices = new ArrayList<BluetoothDevice>();
	private BluetoothDevice mBluetoothDivceConnect;
	// Member object for the services
	private BluetoothService mService = null;
	// private ProgressDialog myDialog;

	private boolean isSaomiao;
	/** 请求配对 */
	private final static String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	/** 取消配对 */
	private static final String ACTION_PAIRING_CANCEL = "android.bluetooth.device.action.PAIRING_CANCEL";

	private BluetoothSave bluetoothSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAdapter();
		setConnnect();
		setPaireDevice();
		// 开始扫描
		doDiscovery();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_bluetooth_list;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		// Set result CANCELED incase the user backs out
		title = getIntent().getExtras().getString(ValueKey.TITLE);

		setResult(Activity.RESULT_CANCELED);
		// Get the local Bluetooth adapter
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			Toast.makeText(context, "您的设备不支持蓝牙", Toast.LENGTH_SHORT).show();
			tvNo.setVisibility(View.VISIBLE);
			return;
		}

		// 注册Receiver来获取蓝牙设备相关的结果
		IntentFilter intent = new IntentFilter();
		intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
		intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);// 设备连接状态改变的广播
		intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intent.addAction(ACTION_PAIRING_REQUEST);// 自動匹配的广播
		intent.addAction(ACTION_PAIRING_CANCEL);// 自動匹配的广播
		registerReceiver(mReceiver, intent);
		//
		if (!mBtAdapter.isEnabled()) {
			// 打开蓝牙
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, UtilBluetoothValue.REQUEST_ENABLE_BT);
		}
		mService = BluetoothService.getInstance(context, mHandler);
		bluetoothSave = new BluetoothSave();
		listBluetoothSaveConnect = bluetoothSave.getBluetoothDataFromFile();
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_bluetooth,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		tvNo = (TextView) findViewById(R.id.tv_bluetooth_no);
		//
		scanButton = (Button) findViewById(R.id.button_scan);
		connectListView = (MyListViewScroll) findViewById(R.id.connect_devices);
		pairedListView = (MyListViewScroll) findViewById(R.id.paired_devices);
		newDevicesListView = (MyListViewScroll) findViewById(R.id.new_devices);
		// tvDeviceConnect = (TextView) findViewById(R.id.tv_paired_connet);
		progressbarOther = (ProgressBar) findViewById(R.id.pogressbar_more);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		tvNo.setVisibility(View.GONE);
	}

	/** 设置匹配的设备 */
	private void setPaireDevice() {
		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0) {
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			listPairedDevices.clear();
			listConnectDevices.clear();
			for (BluetoothDevice device : pairedDevices) {
				if (listBluetoothSaveConnect != null) {
					if (isExitSaveBluetooth(device, listBluetoothSaveConnect)) {
						listConnectDevices.add(device);
						listConnectDevices = ListUtil.removeDuplicateWithOrder((ArrayList) listConnectDevices);
					} else {
						listPairedDevices.add(device);
						listPairedDevices = ListUtil.removeDuplicateWithOrder((ArrayList) listPairedDevices);
					}
				} else {
					listPairedDevices.add(device);
				}
			}
			mPairedDevicesAdapter.setData(listPairedDevices);
			mPairedDevicesAdapter.notifyDataSetChanged();
			mConnectsAdapter.setData(listConnectDevices);
			mConnectsAdapter.notifyDataSetChanged();
		}
	}

	/** 蓝牙设备是否连接成功保存过 */
	private boolean isExitSaveBluetooth(BluetoothDevice device, List<BluetoothEntity> listBluetoothSaveConnect) {
		for (BluetoothEntity bluetoothEntity : listBluetoothSaveConnect) {
			if (device.getAddress().equals(bluetoothEntity.getAddress())) {
				return true;
			}
		}
		return false;
	}

	private void setAdapter() {
		mConnectsAdapter = new CommonAdapter<BluetoothDevice>(context, listConnectDevices, R.layout.activity_bluetooth_name) {
			@Override
			public void convert(ViewHolder viewHolder, BluetoothDevice item, final int position) {
				// TODO Auto-generated method stub
				TextView tvName = viewHolder.getView(R.id.tv_bluetooth_devicename);
				Button btn = viewHolder.getView(R.id.btn_bluetooth_connection);
				ImageView iv = viewHolder.getView(R.id.iv_bluetooth_icon);
				final ProgressBar progressbar = viewHolder.getView(R.id.pogressbar_bluetooth);
				int deviceType = item.getBluetoothClass().getMajorDeviceClass();
				String name = item.getName();

				tvName.setText(name);
				if (mBluetoothDivceConnect != null && item.getAddress().equals(mBluetoothDivceConnect.getAddress())) {
					btn.setText("断开");
					btn.setBackgroundResource(R.drawable.d_line_green_whitebg);
					btn.setTextColor(getResources().getColor(R.color.bg_green));
					iv.setImageResource(R.drawable.ic_device_manage_icon);
				} else {
					btn.setText("连接");
					btn.setBackgroundResource(R.drawable.d_line_gray_whitebg);
					btn.setTextColor(getResources().getColor(R.color.text_gray));
					iv.setImageResource(R.drawable.ic_device_manage_default_icon);
				}
				progressbar.setVisibility(View.GONE);
				btn.setVisibility(View.VISIBLE);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						progressbar.setVisibility(View.VISIBLE);
						view.setVisibility(View.GONE);
						stopDiscovery();
						if (listPairedDevices != null && listConnectDevices.size() > 0) {
							BluetoothDevice device = listConnectDevices.get(position);
							mService.connect(device);
						}
					}
				});
			}
		};
		connectListView.setAdapter(mConnectsAdapter);

		mPairedDevicesAdapter = new CommonAdapter<BluetoothDevice>(context, listPairedDevices, R.layout.activity_bluetooth_name) {
			@Override
			public void convert(ViewHolder viewHolder, BluetoothDevice item, final int position) {
				// TODO Auto-generated method stub
				TextView tvName = viewHolder.getView(R.id.tv_bluetooth_devicename);
				Button btn = viewHolder.getView(R.id.btn_bluetooth_connection);
				final ProgressBar progressbar = viewHolder.getView(R.id.pogressbar_bluetooth);
				tvName.setText(item.getName());
				progressbar.setVisibility(View.GONE);
				btn.setVisibility(View.VISIBLE);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						progressbar.setVisibility(View.VISIBLE);
						view.setVisibility(View.GONE);
						stopDiscovery();
						if (listPairedDevices != null && listPairedDevices.size() > 0) {
							BluetoothDevice device = listPairedDevices.get(position);
							if (mBluetoothDivceConnect != null && mBluetoothDivceConnect.getAddress().equals(device.getAddress())) {
								Toast.makeText(getApplicationContext(), "以连接" + device.getName(), Toast.LENGTH_SHORT).show();
							} else {
								mService.connect(listPairedDevices.get(position));
							}
						}
					}
				});
			}
		};
		pairedListView.setAdapter(mPairedDevicesAdapter);

		mOtherDevicesAdapter = new CommonAdapter<BluetoothDevice>(context, listOtherDevices, R.layout.activity_bluetooth_name) {
			@Override
			public void convert(ViewHolder viewHolder, BluetoothDevice item, final int position) {
				// TODO Auto-generated method stub
				TextView tvName = viewHolder.getView(R.id.tv_bluetooth_devicename);
				Button btn = viewHolder.getView(R.id.btn_bluetooth_connection);
				final ProgressBar progressbar = viewHolder.getView(R.id.pogressbar_bluetooth);
				viewHolder.setText(R.id.tv_bluetooth_devicename, item.getName());
				btn.setText("匹配");
				progressbar.setVisibility(View.GONE);
				btn.setVisibility(View.VISIBLE);

				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						progressbar.setVisibility(View.VISIBLE);
						view.setVisibility(View.GONE);
						// 取消搜索，即将连接
						stopDiscovery();
						BluetoothDevice device = listOtherDevices.get(position);
						pair(device.getAddress());
					}
				});
			}
		};
		newDevicesListView.setAdapter(mOtherDevicesAdapter);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		// tvDeviceConnect.setOnClickListener(onClick);
		// pairedListView.setOnItemClickListener(mDeviceClickListener);
		// newDevicesListView.setOnItemClickListener(mDeviceOtherClickListener);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSaomiao) {// 停止扫描
					// 取消搜索 ,已经搜索到
					stopDiscovery();
				} else {// 开始扫描
					doDiscovery();
				}
			}
		});
	}

	/**
	 * 使用蓝牙适配器启动设备
	 */
	private void doDiscovery() {
		// 如果正在扫描，停止它
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// 蓝牙适配器请求找到的
		mBtAdapter.startDiscovery();
		progressbarOther.setVisibility(View.VISIBLE);
		isSaomiao = true;
		scanButton.setText("停止扫描");
	}

	private void stopDiscovery() {
		mBtAdapter.cancelDiscovery();
		isSaomiao = false;
		scanButton.setText("开始扫描");
		progressbarOther.setVisibility(View.GONE);
	}

	private void setConnnect() {
		mBluetoothDivceConnect = BluetoothService.getmBluetoothDivceConnect();
		if (mBluetoothDivceConnect != null) {
			// tvDeviceConnect.setText(mBluetoothDivceConnect.getName() + "\n" + mBluetoothDivceConnect.getAddress());
			mConnectsAdapter.notifyDataSetChanged();
		}
	}

	private OnClickListener onClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	};

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
			stopDiscovery();
			if (listPairedDevices != null && listPairedDevices.size() > 0) {
				BluetoothDevice device = listPairedDevices.get(position);
				if (mBluetoothDivceConnect != null && mBluetoothDivceConnect.getAddress().equals(device.getAddress())) {
					Toast.makeText(getApplicationContext(), "以连接" + device.getName(), Toast.LENGTH_SHORT).show();
				} else {
					mService.connect(listPairedDevices.get(position));
				}
			}
		}
	};

	// The on-click listener for all devices in the ListViews
	private OnItemClickListener mDeviceOtherClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
			// 取消搜索，即将连接
			stopDiscovery();
			//
			BluetoothDevice device = listOtherDevices.get(position);
			pair(device.getAddress());
		}
	};

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			// UL.d(TAG, action + "|" + device.getName());

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {// 获得已经搜索到的蓝牙设备
				// Get the BluetoothDevice object from the Intent
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// UL.d(TAG, "FOUND New" + device.getName());
					// 列表里不存在
					listOtherDevices.add(device);
					listOtherDevices = ListUtil.removeDuplicateWithOrder((ArrayList) listOtherDevices);
				}
				// Toast.makeText(getApplicationContext(), "搜索到设备" + device.getName(), Toast.LENGTH_SHORT).show();
				mOtherDevicesAdapter.setData(listOtherDevices);
				mOtherDevicesAdapter.notifyDataSetChanged();
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				// Toast.makeText(getApplicationContext(), "没有匹配的设备", Toast.LENGTH_SHORT).show();
				stopDiscovery();
			} else if (ACTION_PAIRING_REQUEST.equals(action)) {// 请求匹配设备
				int type = intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_VARIANT, BluetoothDevice.ERROR);
				Log.d(TAG, "ACTION_PAIRING_REQUEST-" + type);
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
					setPaireDevice();
					// mapOther.put(device.getAddress(), TypeBondEnd);
					listOtherDevices.remove(device);
					mOtherDevicesAdapter.notifyDataSetChanged();
					// 配对成功 开始连接
					mService.connect(device);
					// if (isRunning)
					// showDialogTitle("配对成功,开始连接...");
					// tvDeviceConnect.setText("正在连接...");
				} else if (bondState == BluetoothDevice.BOND_NONE) {
					Log.d(TAG, "取消配对：" + device.getName());
					setPaireDevice();
					// mapOther.put(device.getAddress(), TypeBondEnd);
					mOtherDevicesAdapter.notifyDataSetChanged();
				} else if (bondState == BluetoothDevice.BOND_BONDING) {
					Log.d(TAG, "配对中：" + device.getName());
					// mapOther.put(device.getAddress(), TypeBonding);
					mOtherDevicesAdapter.notifyDataSetChanged();
				} else {
					Log.d(TAG, "未知：" + device.getName());
					// mapOther.put(device.getAddress(), TypeBondEnd);
					mOtherDevicesAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	// The Handler that gets information back from the BluetoothService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UtilBluetoothValue.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					mBluetoothDivceConnect = BluetoothService.getmBluetoothDivceConnect();
					if (mBluetoothDivceConnect != null) {
						// Toast.makeText(getApplicationContext(), "以连接至" + mBluetoothDivceConnect.getName(),
						// Toast.LENGTH_SHORT).show();
						if (!idExistDeviceForConnect(mBluetoothDivceConnect)) {
							listConnectDevices.add(mBluetoothDivceConnect);
							listConnectDevices = ListUtil.removeDuplicateWithOrder((ArrayList) listConnectDevices);
							if (listPairedDevices != null) {
								Iterator<BluetoothDevice> bluetoothIterator = listPairedDevices.iterator();
								if (bluetoothIterator.hasNext()) {
									BluetoothDevice bluetooth = bluetoothIterator.next();
									if (bluetooth.getAddress().equals(mBluetoothDivceConnect.getAddress())) {
										bluetoothIterator.remove();
									}
								}
							}
						}
						mPairedDevicesAdapter.setData(listPairedDevices);
						mPairedDevicesAdapter.notifyDataSetChanged();

						mConnectsAdapter.setData(listConnectDevices);
						mConnectsAdapter.notifyDataSetChanged();
						bluetoothSave.saveBluetoothDataToFile(listConnectDevices);
						// tvDeviceConnect.setText(mBluetoothDivceConnect.getName() + "\n" +
						// mBluetoothDivceConnect.getAddress());
					}
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
					mBluetoothDivceConnect = null;
					// mConnectsAdapter.notifyDataSetChanged();
					// mPairedDevicesAdapter.notifyDataSetChanged();
					// tvDeviceConnect.setText("正在连接...");
					// Toast.makeText(getApplicationContext(), "正在连接...", Toast.LENGTH_SHORT).show();
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					// if (isRunning)
					// showDialogFail("连接失败", "无连接");
					mBluetoothDivceConnect = null;
					mConnectsAdapter.notifyDataSetChanged();
					mPairedDevicesAdapter.notifyDataSetChanged();
					// tvDeviceConnect.setText("无连接");
					break;
				}
				break;
			case UtilBluetoothValue.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mBluetoothDivceConnect = msg.getData().getParcelable(UtilBluetoothValue.DEVICE);
				mConnectsAdapter.notifyDataSetChanged();
				mPairedDevicesAdapter.notifyDataSetChanged();
				break;
			case UtilBluetoothValue.MESSAGE_TOAST:
				mBluetoothDivceConnect = null;
				mConnectsAdapter.notifyDataSetChanged();
				mPairedDevicesAdapter.notifyDataSetChanged();
				// tvDeviceConnect.setText("连接失败");
				// if (isRunning) {
				// showDialogFail("连接失败", msg.getData().getString(UtilBluetoothValue.TOAST));
				// Toast.makeText(getApplicationContext(), msg.getData().getString(UtilBluetoothValue.TOAST),
				// Toast.LENGTH_SHORT).show();
				// }
				break;
			case UtilBluetoothValue.MESSAGE_READ:
				byte[] buffer = (byte[]) msg.obj;
				int bytes = msg.arg1;
				String data = new String(buffer, 0, bytes);
				// UL.d(TAG, data);
				break;
			case UtilBluetoothValue.MESSAGE_WEIGHT:
				break;
			}

		}
	};

	private boolean idExistDeviceForConnect(BluetoothDevice bluetoothDevice) {
		if (bluetoothDevice != null && listConnectDevices != null) {
			for (BluetoothDevice device : listConnectDevices) {
				if (bluetoothDevice.getAddress().equals(device.getAddress())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 对象转数组
	 * 
	 * @param obj
	 * @return
	 */
	public byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	public boolean pair(String strAddr) {
		boolean result = false;
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		bluetoothAdapter.cancelDiscovery();
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
		if (device == null) {
			return false;
		}
		if (device.getBondState() == BluetoothDevice.BOND_NONE) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				boolean flag2 = UtilBluetoothCls.createBond(device.getClass(), device);
				// boolean flag1 = UtilBluetoothCls.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //
		} else if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			Toast.makeText(context, device.getName() + "已经配对成功", Toast.LENGTH_SHORT).show();
		} else if (device.getBondState() != BluetoothDevice.BOND_BONDING) {
			Toast.makeText(context, device.getName() + "正在配对中", Toast.LENGTH_SHORT).show();
		}
		// else {
		// Log.d("mylog", "HAS BOND_BONDED");
		// try {
		// UtilBluetoothCls.removeBond(device.getClass(), device);
		// // ClsUtils.createBond(device.getClass(), device);
		// boolean flag1 = UtilBluetoothCls.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
		// boolean flag2 = UtilBluetoothCls.createBond(device.getClass(), device);
		// // remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
		// result = true;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// Log.d("mylog", "setPiN failed!");
		// e.printStackTrace();
		// }
		// }
		return result;
	}

	@Override
	public void onDestroy() {
		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
