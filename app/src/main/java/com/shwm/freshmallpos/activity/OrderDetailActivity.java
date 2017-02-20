package com.shwm.freshmallpos.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.CommonAdapter;
import com.shwm.freshmallpos.adapter.ViewHolder;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.been.OrderEntity;
import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.bluetooth.PicFromPrintUtils;
import com.shwm.freshmallpos.bluetooth.UtilBluetoothPrintFormat;
import com.shwm.freshmallpos.bluetooth.UtilBluetoothValue;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.myview.MyListViewScroll;
import com.shwm.freshmallpos.presenter.MOrderDetailPresenter;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.view.IOrderDetailView;
import com.shwm.freshmallpos.base.BaseActivity;

/**
 * 订单详情
 * 
 * @author wr 2016-12-22
 */
public class OrderDetailActivity extends BaseActivity<IOrderDetailView, MOrderDetailPresenter> implements IOrderDetailView {
	private String title;

	private MyListViewScroll mListViewOrder;
	private MyListViewScroll mListViewFood;
	private CommonAdapter<String> mAdapterOrder;
	private CommonAdapter<FoodEntity> mAdapterFood;

	private OrderEntity order;
	private OrderEntity orderDetail;
	private List<FoodEntity> listFood;

	private View viewShowFood;
	private View viewFoodInfo;
	private ImageView ivMore;

	private Button btnRefund;
	private Button btnPrint;

	public BluetoothService mService;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mPresenter.getOrderDetail();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_orderdetail;
	}

	@Override
	public MOrderDetailPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MOrderDetailPresenter(this);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);
		order = (OrderEntity) bundle.getSerializable(ValueKey.ORDER);

		if (mService == null) {
			mService = BluetoothService.getInstance(getApplicationContext(), null);
		}
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_orderdetail,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		mListViewFood = (MyListViewScroll) findViewById(R.id.lv_orderdetail_food);
		mListViewOrder = (MyListViewScroll) findViewById(R.id.lv_orderdetail_order);
		viewShowFood = findViewById(R.id.rl_orderditail_showFood);
		viewFoodInfo = findViewById(R.id.ll_orderdetail_foodinfo);
		ivMore = (ImageView) findViewById(R.id.iv_orderdetail_more);
		btnRefund = (Button) findViewById(R.id.btn_orderdetail_moneyReturn);
		btnPrint = (Button) findViewById(R.id.btn_orderdetail_print);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		setAdapter();
		if (order != null) {
			((TextView) findViewById(R.id.tv_orderdetail_totalMoney)).setText(StringFormatUtil.moneyFormat(order.getMoney()));
			if (!StringUtil.isEmpty(order.getRefund())) {
				btnRefund.setVisibility(View.GONE);
			}
		}
		viewFoodInfo.setVisibility(View.GONE);
		ivMore.setEnabled(false);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		viewShowFood.setOnClickListener(this);
		btnRefund.setOnClickListener(this);
		btnPrint.setOnClickListener(this);
	}

	private void setAdapter() {
		final List<String> listKey = new ArrayList<String>();
		listKey.add("交易时间");
		listKey.add("流水号");
		listKey.add("收款方式");
		listKey.add("金额");

		final List<String> listValue = new ArrayList<String>();
		if (order != null) {
			listValue.add(order.getDate() + " " + order.getTime());
			listValue.add(order.getOrderno());
			listValue.add(order.getPayTypeTag());
			listValue.add(UtilMath.currency(order.getMoney()));
		}

		mAdapterOrder = new CommonAdapter<String>(mActivity, listKey, R.layout.item_orderdetail_order) {
			@Override
			public void convert(ViewHolder viewHolder, String item, int position) {
				// TODO Auto-generated method stub
				viewHolder.setText(R.id.tv_item_orderdetail_order_left, listKey.get(position));
				if (listValue.size() > position) {
					viewHolder.setText(R.id.tv_item_orderdetail_order_right, listValue.get(position));
				}
			}
		};
		mListViewOrder.setAdapter(mAdapterOrder);

		mAdapterFood = new CommonAdapter<FoodEntity>(mActivity, listFood, R.layout.item_ordersubmit) {
			@Override
			public void convert(ViewHolder viewHolder, FoodEntity item, int position) {
				// TODO Auto-generated method stub
				viewHolder.setText(R.id.tv_item_ordersumit_foodname, item.getName());
				viewHolder.setText(R.id.tv_item_ordersumit_foodunit, item.getUnit());
				viewHolder.setText(R.id.tv_item_ordersumit_foodnum, item.getNum());
				viewHolder.setText(R.id.tv_item_ordersumit_foodprice, item.getPrice());
				double money = UtilMath.mul(item.getPrice(), item.getNum());
				viewHolder.setText(R.id.tv_item_ordersumit_foodMoney, UtilMath.currency(money));
			}
		};
		mListViewFood.setAdapter(mAdapterFood);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == viewShowFood.getId()) {
			if (viewFoodInfo.getVisibility() == View.GONE) {
				viewFoodInfo.setVisibility(View.VISIBLE);
				ivMore.setEnabled(true);
			} else {
				viewFoodInfo.setVisibility(View.GONE);
				ivMore.setEnabled(false);
			}
		}
		if (v.getId() == btnRefund.getId()) {
			mPresenter.orderRefund();
		}
		if (v.getId() == btnPrint.getId()) {
			if (BluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
				mService.printCenter();
				mService.printSize(UtilBluetoothValue.Text_Size_large6);
				sendMessage(UtilBluetoothPrintFormat.printTitle(BusinessInfo.getBusinessName(), 8));
				mService.printLeft();// 左对齐打印
				mService.printSize(UtilBluetoothValue.Text_Size_default);
				sendMessage(UtilBluetoothPrintFormat.getPrintForOrderInfo(mActivity.getApplicationContext(), orderDetail, listFood));
				// sendMessage(System.currentTimeMillis() + "\n");
			} else {
				startActivity(new Intent(mActivity, BluetoothListActivity.class).putExtra(ValueKey.TITLE,
						getString(R.string.title_devicemanage)));
			}
		}
	}

	@Override
	public OrderEntity getOrder() {
		// TODO Auto-generated method stub
		return order;
	}

	@Override
	public void setOrderDetail(OrderEntity order) {
		this.orderDetail = order;
	}

	@Override
	public void showListFood(List<FoodEntity> listfood) {
		// TODO Auto-generated method stub
		this.listFood = listfood;
		mAdapterFood.setData(listfood);
		mAdapterFood.notifyDataSetChanged();
	}

	/**
	 * 打印
	 * 
	 * @param message
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (BluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(context, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
			return;
		}
		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send;
			try {
				send = message.getBytes("GB2312");
			} catch (UnsupportedEncodingException e) {
				send = message.getBytes();
			}
			mService.write(send);
		}
	}

	private void sendMessageBitmap(Bitmap bitmap) {
		// Check that we're actually connected before trying anything
		if (BluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(context, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
			return;
		}
		// 发送打印图片前导指令
		byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B, 0x40, 0x1B, 0x33, 0x00 };
		mService.write(start);

		/** 获取打印图片的数据 **/
		// byte[] send = getReadBitMapBytes(bitmap);

		mService.printCenter();
		byte[] draw2PxPoint = PicFromPrintUtils.draw2PxPointYiweima(bitmap);

		mService.write(draw2PxPoint);
		// 发送结束指令
		byte[] end = { 0x1d, 0x4c, 0x1f, 0x00 };
		mService.write(end);
	}

	@Override
	public void refundSuccess() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(mActivity, OverActivity.class);
		intent.putExtra(ValueKey.TITLE, getString(R.string.title_refundOver));
		intent.putExtra(ValueKey.TYPE, ValueType.OverType_Refund);
		intent.putExtra(ValueKey.MoneyReceivable, order == null ? "" : order.getMoney());
		startActivityForResult(intent, ValueRequest.Refund_Over);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == ValueRequest.Refund_Over && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
