package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMapTouchListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.amap.AMapLocationUtil;
import com.shwm.freshmallpos.amap.AMapUtil;
import com.shwm.freshmallpos.been.AddressEntity;
import com.shwm.freshmallpos.presenter.MAmapLocationPresenter;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UT;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.view.IAmapLocationView;
import com.shwm.freshmallpos.base.BaseActivity;

public class AmapLocationActivity extends BaseActivity<IAmapLocationView, MAmapLocationPresenter> implements AMapLocationListener,
		OnCameraChangeListener, OnMapTouchListener, OnMapClickListener, OnMapLongClickListener, OnGeocodeSearchListener,
		AMap.InfoWindowAdapter, IAmapLocationView {
	private MapView mMapView = null;
	private AMap aMap;
	private GeocodeSearch geocodeSearch;
	//
	private Marker mLocMarker;

	private AMapLocationUtil aMapLocationUtil;
	private ImageButton iBtnLocal;
	private View viewAddr;
	private TextView tvAddr;
	private LatLng latLngNow;
	private RegeocodeAddress regeocodeAddressNow;
	private String addressNameNow;
	private String title;

	private AddressEntity addressEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 此方法必须重写 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
		mMapView.onCreate(savedInstanceState);
		location();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_amap_location;
	}

	@Override
	public MAmapLocationPresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MAmapLocationPresenter();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
		mMapView.onDestroy();
		aMapLocationUtil.onDestroyLocal();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
		mMapView.onPause();
		aMapLocationUtil.stopLocal();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		title = getIntent().getExtras().getString(ValueKey.TITLE);
		aMapLocationUtil = new AMapLocationUtil(context, this);
		geocodeSearch = new GeocodeSearch(context);
		initMap();
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_maplocation);
		mToolbar.setTitle(title);// 设置主标题
		mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(R.drawable.ic_action_back_light);// 设置导航栏图标
		mToolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		iBtnLocal = (ImageButton) findViewById(R.id.ibtn_maplocation_locat);
		tvAddr = (TextView) findViewById(R.id.tv_maplocation_addr);
		viewAddr = findViewById(R.id.ll_maplocation_addr);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
		aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器
		iBtnLocal.setOnClickListener(this);
		geocodeSearch.setOnGeocodeSearchListener(this);
		aMap.setOnMarkerClickListener(markerClickListener);// 绑定 Marker 被点击事件
		aMap.setInfoWindowAdapter(this);
	}

	private void initMap() {
		// 初始化地图控制器对象
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.amap_maplocation);
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		// aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		// aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

		UiSettings uiSettings = aMap.getUiSettings();
		// 是否允许显示缩放按钮
		uiSettings.setZoomControlsEnabled(false);
		// 设置缩放控件的位置
		// uiSettings.setZoomPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
		// 添加指南针
		// uiSettings.setCompassEnabled(true);

		// 控制比例尺控件是否显示
		uiSettings.setScaleControlsEnabled(true);
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == iBtnLocal.getId()) {
			location();
		}
	}

	// 定位
	private void location() {
		requestPermission(ValuePermission.PermissionRequest_LOCATION, ValuePermission.PermissionGroupLOCATION, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				aMapLocationUtil.startLocal();
			}
		}, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UT.showShort(context, getString(R.string.permission_noLocation));
			}
		});
	}

	// 定义 Marker 点击事件监听
	AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
		// 返回 true 则表示接口已响应事件，否则返回false
		@Override
		public boolean onMarkerClick(Marker marker) {
			UT.showShort(context, "onclick InfoWin");
			return true;
		}
	};

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		UL.e(TAG, "onLocationChanged()");
		if (amapLocation != null && amapLocation.getErrorCode() == 0) {
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(amapLocation), 17));
			addMarker(AMapUtil.convertToLatLng(amapLocation));// 添加定位图标
		} else {
			String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
			UL.e("AmapErr", errText);
		}
	}

	private void addMarker(LatLng latlng) {
		searchAddr(AMapUtil.convertToLatLonPoint(latlng));
		if (mLocMarker == null) {
			Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.poi_marker_pressed);
			BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);
			View view = View.inflate(mActivity, R.layout.view_locationmark, null);
			TextView tvText = (TextView) view.findViewById(R.id.tv_mark_text);
			BitmapDescriptor bd = BitmapDescriptorFactory.fromView(view);
			MarkerOptions options = new MarkerOptions();
			options.icon(des);
			options.anchor(0.5f, 0.5f);// 标记的锚点
			// options.position(latlng);
			options.title("");// 必须要有 ，否则不会显示标记上面的view
			mLocMarker = aMap.addMarker(options);
		}
		latLngNow = latlng;
		mLocMarker.setPosition(latlng);
		mLocMarker.showInfoWindow();
	}

	//
	private void searchAddr(LatLonPoint latLonPoint) {
		// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
		geocodeSearch.getFromLocationAsyn(query);
	}

	@Override
	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * 对正在移动地图事件回调
	 */
	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		// TODO Auto-generated method stub
		// LatLng latlng = cameraPosition.target;
		// addMarker(latlng);
	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		// TODO Auto-generated method stub
		// LatLng latlng = cameraPosition.target;
		// addMarker(latlng);
	}

	/**
	 * 对单击地图事件回调
	 */
	@Override
	public void onMapClick(LatLng latlng) {
		// TODO Auto-generated method stub
		addMarker(latlng);// 添加定位图标
		UL.e(TAG, "latlng" + latlng);
	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng latlng) {
		// TODO Auto-generated method stub
		// addMarker(latlng);// 添加定位图标
		// UL.e(TAG, "point" + latlng);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 返回逆地理编码的结果
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
				addressNameNow = result.getRegeocodeAddress().getFormatAddress();
				regeocodeAddressNow = result.getRegeocodeAddress();
				tvAddr.setText(addressNameNow);
				UL.d(TAG, "省getProvince=" + regeocodeAddressNow.getProvince() + "|市getCity=" + regeocodeAddressNow.getCity()
						+ "|县getDistrict=" + regeocodeAddressNow.getDistrict() + "|乡镇getTownship=" + regeocodeAddressNow.getTownship());
			} else {
				addressNameNow = "";
				tvAddr.setText(getString(R.string.amap_localtion_addrSearchno));
			}
		} else {
			addressNameNow = "";
			tvAddr.setText(getString(R.string.amap_localtion_addrSearchfail));
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		View infoWindow = LayoutInflater.from(context).inflate(R.layout.view_local_infowindow, null);
		TextView tvTitle = (TextView) infoWindow.findViewById(R.id.tv_infowindow_text);
		tvTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				addressEntity = new AddressEntity();
				addressEntity.setLat(latLngNow.latitude);
				addressEntity.setLng(latLngNow.longitude);
				addressEntity.setAddress(addressNameNow);
				if (regeocodeAddressNow != null) {
					addressEntity.setProvince(regeocodeAddressNow.getProvince());
					addressEntity.setCity(regeocodeAddressNow.getCity());
					addressEntity.setDistrict(regeocodeAddressNow.getDistrict());
				}

				mPresenter.setBusinessName();
			}
		});
		return infoWindow;
	}

	@Override
	public void changeBusinessAddressSuccess() {
		// TODO Auto-generated method stub
		UtilSPF.putString(ValueKey.Business_ADDRESS, addressNameNow);
		Intent intent = new Intent();
		intent.putExtra(ValueKey.AddressEntity, addressEntity);
		intent.putExtra(ValueKey.Business_ADDRESS, addressNameNow);
		setResult(RESULT_OK, intent);
		mActivity.finish();
	}

	@Override
	public AddressEntity getAddressEntity() {
		// TODO Auto-generated method stub
		return addressEntity;
	}
}
