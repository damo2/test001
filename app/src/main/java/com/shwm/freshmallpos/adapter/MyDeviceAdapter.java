package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.MyDeviceAdapter.MyDeviceHolder;

public class MyDeviceAdapter extends Adapter<MyDeviceHolder> {
	private List<BluetoothDevice> listBluetooth;
	private Context context;

	public MyDeviceAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<BluetoothDevice> listBluetooth) {
		this.listBluetooth = listBluetooth;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return listBluetooth == null ? 0 : listBluetooth.size();
	}

	@Override
	public void onBindViewHolder(MyDeviceHolder viewholder, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public MyDeviceHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.item_mydevice, null);
		return new MyDeviceHolder(view);
	}

	protected class MyDeviceHolder extends ViewHolder {

		public MyDeviceHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

	}
}
