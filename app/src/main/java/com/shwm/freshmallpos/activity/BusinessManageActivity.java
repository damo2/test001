package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.model.biz.IBusinessListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnBusinessListener;
import com.shwm.freshmallpos.myviewutil.PopupPhoto;
import com.shwm.freshmallpos.myviewutil.PopupPhoto.IphotoChose;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.GetPhotoUtil;
import com.shwm.freshmallpos.util.GetPhotoUtil.OnPhotoListener;
import com.shwm.freshmallpos.util.ImageLoadUtil;
import com.shwm.freshmallpos.util.UT;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.value.ValueRequest;
import com.shwm.freshmallpos.value.ValueType;
import com.shwm.freshmallpos.base.BaseActivity;

public class BusinessManageActivity extends BaseActivity {
	private String title;
	private View viewLogo;
	private View viewName;
	private View viewRenzheng;
	private View viewTaika;
	private View viewShop;
	private View viewAddr;

	private ImageView ivBusinessLogo;
	private TextView tvBusinessName;
	private TextView tvAddr;

	private String businessAddr;
	private String businessName;

	private PopupPhoto popupPhoto;
	private GetPhotoUtil getPhotoUtil;

	private IBusinessListener iBusinessListener;

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_businessmanage;
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
		title = getIntent().getExtras().getString(ValueKey.TITLE);
		popupPhoto = new PopupPhoto(mActivity);
		getPhotoUtil = new GetPhotoUtil(mActivity, ConfigUtil.BusinessLogoWH, ConfigUtil.BusinessLogoWH);
		iBusinessListener = new OnBusinessListener();
	}
	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_businessmanage,title);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		viewLogo = findViewById(R.id.rl_businessmanage_logo);
		viewName = findViewById(R.id.rl_businessmanage_name);
		viewRenzheng = findViewById(R.id.rl_businessmanage_renzheng);
		viewTaika = findViewById(R.id.rl_businessmanage_2barcode);
		viewShop = findViewById(R.id.rl_businessmanage_shop);
		viewAddr = findViewById(R.id.rl_businessmanage_addr);

		ivBusinessLogo = (ImageView) findViewById(R.id.iv_businessmanage_logo);
		tvBusinessName = (TextView) findViewById(R.id.tv_businessmanage_name);
		tvAddr = (TextView) findViewById(R.id.tv_businessmanage_addr);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stubs
		super.setValue();
		tvBusinessName.setText(BusinessInfo.getBusinessName());
		tvAddr.setText(BusinessInfo.getBusinessAddress());

		ImageLoadUtil.displayImage(ivBusinessLogo, BusinessInfo.getBusinessLogo(), ImageLoadUtil.getOptionsImgRoundedUser());
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		viewLogo.setOnClickListener(this);
		viewName.setOnClickListener(this);
		viewRenzheng.setOnClickListener(this);
		viewTaika.setOnClickListener(this);
		viewShop.setOnClickListener(this);
		viewAddr.setOnClickListener(this);
		popupPhoto.setIPopupPhoto(iphotoChose);
		getPhotoUtil.setOnPhotoListener(onPhotoListener);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == viewAddr.getId()) {
			startActivityForResult(
					new Intent(mActivity, AmapLocationActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_mapchoose)),
					ValueRequest.Businessmanage_AddressLocation);
		}

		if (v.getId() == viewName.getId()) {
			startActivityForResult(
					new Intent(mActivity, ChangeNameActivity.class).putExtra(ValueKey.TITLE, getString(R.string.title_businessnameChange))
							.putExtra(ValueKey.TYPE, ValueType.ChangeType_Businessname), ValueRequest.Businessmanage_Name);
		}
		if (v.getId() == viewLogo.getId()) {
			popupPhoto.show(mViewParent);
		}

		if (v.getId() == viewTaika.getId()) {
			startActivity(new Intent(mActivity, PaytypeWechatCodeQRActivity.class).putExtra(ValueKey.TITLE,
					getString(R.string.title_business_codeQR)));
		}
	}

	private OnPhotoListener onPhotoListener = new OnPhotoListener() {
		@Override
		public void resultBitmapUri(Uri bitmapUri) {
			// TODO Auto-generated method stub
			iBusinessListener.setBusinessIcon(bitmapUri, new IRequestListener<String>() {

				@Override
				public void onSuccess(String img) {
					// TODO Auto-generated method stub
					dismissDialogProgress();
					BusinessInfo.setBusinessLogo(img);
					ImageLoadUtil.displayImage(ivBusinessLogo, img, ImageLoadUtil.getOptionsImgRoundedUser());
				}

				@Override
				public void onPreExecute(int type) {
					// TODO Auto-generated method stub
					showDialogProgress();
				}

				@Override
				public void onFail(int statu, Exception exception) {
					// TODO Auto-generated method stub
					dismissDialogProgress();
				}
			});
		}
	};

	private IphotoChose iphotoChose = new IphotoChose() {

		@Override
		public void onPhoto() {
			// TODO Auto-generated method stub
			requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getPhotoUtil.getPhoto(GetPhotoUtil.PHOTO_REQUEST_IMAGE);
				}
			}, new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					UT.showShort(context, getString(R.string.permission_nocamera));
				}
			});
		}

		@Override
		public void onCamera() {
			// TODO Auto-generated method stub
			requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getPhotoUtil.getPhoto(GetPhotoUtil.PHOTO_REQUEST_CAREMA);
				}
			}, new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UT.showShort(context, getString(R.string.permission_nocamera));
				}
			});
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		getPhotoUtil.onPhotoResult(requestCode, resultCode, data);
		if (requestCode == ValueRequest.Businessmanage_AddressLocation && resultCode == RESULT_OK) {
			if (data != null) {
				businessAddr = data.getExtras().getString(ValueKey.Business_ADDRESS);
				tvAddr.setText(businessAddr);
			}
		}

		if (requestCode == ValueRequest.Businessmanage_Name && resultCode == RESULT_OK) {
			if (data != null) {
				businessName = data.getExtras().getString(ValueKey.Business_NAME);
				tvBusinessName.setText(businessName);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
