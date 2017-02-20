package com.shwm.freshmallpos.bluetooth;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.shwm.freshmallpos.util.FileUtil;
import com.shwm.freshmallpos.util.UL;

@SuppressLint("NewApi")
public class BluetoothSave {
	private String TAG = BluetoothSave.this.getClass().getSimpleName();
	private FileUtil fileUtil;

	public BluetoothSave() {
		// TODO Auto-generated constructor stub
		fileUtil = new FileUtil();
	}

	public void saveBluetoothDataToFile(List<BluetoothDevice> listBluetoothDevice) {
		// TODO Auto-generated method stub
		List<BluetoothEntity> listBluetooth = new ArrayList<BluetoothEntity>();
		if (listBluetoothDevice != null) {
			for (BluetoothDevice bluetoothdevice : listBluetoothDevice) {
				BluetoothEntity bluetooth = new BluetoothEntity();
				bluetooth.setName(bluetoothdevice.getName());
				bluetooth.setAddress(bluetoothdevice.getAddress());
				bluetooth.setType(bluetoothdevice.getType());
				listBluetooth.add(bluetooth);
			}
		}
		fileUtil.save(UtilBluetoothValue.BluetoothFileName, bluetoothToJson(listBluetooth));
	}

	public List<BluetoothEntity> getBluetoothDataFromFile() {
		// TODO Auto-generated method stub
		String json = fileUtil.read(UtilBluetoothValue.BluetoothFileName);
		return jsonToBluetooth(json);
	}

	private String bluetoothToJson(List<BluetoothEntity> listBluetooth) {
		JSONObject objectData = new JSONObject();
		try {
			JSONArray arrayBluetoothDiscount = new JSONArray();
			if (listBluetooth != null) {
				for (BluetoothEntity bluetooth : listBluetooth) {
					JSONObject objectBluetooth = new JSONObject();
					objectBluetooth.put(UtilBluetoothValue.Bluetooth_Name, bluetooth.getName());
					objectBluetooth.put(UtilBluetoothValue.Bluetooth_Address, bluetooth.getAddress());
					objectBluetooth.put(UtilBluetoothValue.Bluetooth_Type, bluetooth.getType());
					arrayBluetoothDiscount.put(objectBluetooth);
				}
			}
			objectData.put(UtilBluetoothValue.Bluetooth_Device, arrayBluetoothDiscount);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UL.d(TAG, "保存=" + objectData.toString());
		return objectData.toString();
	}

	private List<BluetoothEntity> jsonToBluetooth(String json) {
		List<BluetoothEntity> listBluetooth = new ArrayList<BluetoothEntity>();
		if (!TextUtils.isEmpty(json)) {
			UL.d(TAG, "取出=" + json.toString());
			try {
				JSONObject object = new JSONObject(json);
				JSONArray arrayBluetoothDiscount = object.optJSONArray(UtilBluetoothValue.Bluetooth_Device);
				if (arrayBluetoothDiscount != null) {
					for (int i = 0; i < arrayBluetoothDiscount.length(); i++) {
						JSONObject objectBluetooth = arrayBluetoothDiscount.getJSONObject(i);
						String name = objectBluetooth.optString(UtilBluetoothValue.Bluetooth_Name);
						String addr = objectBluetooth.optString(UtilBluetoothValue.Bluetooth_Address);
						int type = objectBluetooth.optInt(UtilBluetoothValue.Bluetooth_Type);
						BluetoothEntity bluetooth = new BluetoothEntity();
						bluetooth.setType(type);
						bluetooth.setName(name);
						bluetooth.setAddress(addr);
						listBluetooth.add(bluetooth);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listBluetooth;
	}
}
