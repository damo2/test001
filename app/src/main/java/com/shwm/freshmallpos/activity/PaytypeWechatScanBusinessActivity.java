package com.shwm.freshmallpos.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.BitmapUtil;
import com.shwm.freshmallpos.util.BrightnessUtil;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.SDPathUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.base.BaseActivity;

public class PaytypeWechatScanBusinessActivity extends BaseActivity {
	private String title;
	private String codeUrl;
	private double money;
	private ImageView ivCode;

	//
	private boolean isAutoBrightness;
	private int brightness = 255;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestPermission(ValuePermission.PermissionRequest_SETTINGS, ValuePermission.PermissionGroupSETTINGS, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isAutoBrightness = BrightnessUtil.isAutoBrightness(getContentResolver());
				brightness = BrightnessUtil.getScreenBrightness(mActivity);
				if (isAutoBrightness) {
					BrightnessUtil.stopAutoBrightness(mActivity);
				}
				BrightnessUtil.setBrightness(mActivity, 255);
			}
		}, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		requestPermission(ValuePermission.PermissionRequest_SETTINGS, ValuePermission.PermissionGroupSETTINGS, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isAutoBrightness) {
					BrightnessUtil.startAutoBrightness(mActivity);
				}
				BrightnessUtil.setBrightness(mActivity, brightness);
			}
		}, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});

		super.onDestroy();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_paytype_wechat_code;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);
		codeUrl = bundle.getString(ValueKey.CODERQ);
		money = bundle.getDouble(ValueKey.MoneyReceivable);
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_wechat_code,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		ivCode = (ImageView) findViewById(R.id.iv_wechat_code);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		String filepath = SDPathUtil.getSDCardPrivateCacheDir(context) + "/wechatpaycode.jpg";
		boolean success = BitmapUtil.createQRImage(codeUrl, ConfigUtil.CodeQRWH, ConfigUtil.CodeQRWH,
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), filepath);
		UL.d(TAG, "保存二维码" + success + "\n codeurl=" + codeUrl);
		if (success) {
			ivCode.setImageBitmap(BitmapFactory.decodeFile(filepath));
		}
		((TextView) findViewById(R.id.tv_wechat_money)).setText(UtilMath.currency(money));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		findViewById(R.id.btn_wechat_scanCode).setOnClickListener(this);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_wechat_scanCode:
			finish();
			break;
		default:
			break;
		}
	}

}
