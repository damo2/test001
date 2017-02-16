package com.shwm.freshmallpos.fragment;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.bluetooth.BluetoothService;
import com.shwm.freshmallpos.inter.IOnCartListener;
import com.shwm.freshmallpos.presenter.MCashOrderCodePresenter;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValuePermission;
import com.shwm.freshmallpos.view.ICashOrderCodeView;
import com.shwm.freshmallpos.activity.BluetoothListActivity;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.base.BaseFragment;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.DecodeHandlerInterface;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

/**
 * 开单-扫描二维码
 */
public class CashOrderCodeFragment extends BaseFragment<ICashOrderCodeView, MCashOrderCodePresenter> implements ICashOrderCodeView,
		Callback, DecodeHandlerInterface {
	public static final String SCAN_RESULT_ACTION = "com.zxing.fragment.ACTION_SCAN_RESULT";
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	// private Button cancelScanButton;

	private EditText edtCode;
	private Button btnLight;

	private IOnCartListener iOnCartListener;

	private String foodCodeBefore;

	public BluetoothService mService;// 蓝牙

	public static CashOrderCodeFragment newInstance(String title) {
		CashOrderCodeFragment fragment = new CashOrderCodeFragment();
		Bundle args = new Bundle();
		args.putString(ValueKey.TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

	public CashOrderCodeFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof IOnCartListener) {
			iOnCartListener = (IOnCartListener) activity;
		} else {
			UL.e(TAG, "activity 必须实现 IOnCartListener 接口以便更新购物车");
		}
	}

	/** Called when the fragment is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.e(TAG, "onCreateView()");
		mActivity.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		CameraManager.init(getActivity().getApplicationContext());
		viewfinderView = (ViewfinderView) mRootView.findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(mActivity);
		return mRootView;
	}

	@Override
	protected int setLayoutResouceId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_cashorder_code;
	}

	@Override
	public MCashOrderCodePresenter initPresenter() {
		// TODO Auto-generated method stub
		return new MCashOrderCodePresenter(this, iOnCartListener);
	}

	@Override
	protected void onLazyLoad() {
		// TODO Auto-generated method stub
		super.onLazyLoad();
		((BaseActivity) mActivity).requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA,
				new Runnable() {
					@Override
					public void run() {
					}
				}, new Runnable() {
					@Override
					public void run() {
						android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
						builder.setTitle(getString(R.string.camera_camcel_scancode_title));
						builder.setPositiveButton(getString(R.string.sure), null);
						builder.show();
					}
				});
		initRes();
	}

	@Override
	protected void onLazyClear() {
		// TODO Auto-generated method stub
		super.onLazyClear();
		clear();
	}

	@Override
	public void onResume() {
		super.onResume();
		initRes();
	}

	@Override
	public void onPause() {
		super.onPause();
		clear();
	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initRes() {
		SurfaceView surfaceView = (SurfaceView) mRootView.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		// surfaceView.setZOrderOnTop(true);// 显示在顶层
		// // surfaceView.setZOrderMediaOverlay(true);// 显示在顶层防止遮挡
		// surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);// 设置为透明
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	private void clear() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		if (CameraManager.get() != null) {
			CameraManager.get().closeDriver(); // 关闭摄像头
		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		mService = BluetoothService.getInstance(context, null);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		edtCode = (EditText) mRootView.findViewById(R.id.edt_cashorder_code);
		btnLight = (Button) mRootView.findViewById(R.id.btn_cashorder_light);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnLight.setOnClickListener(this);
		edtCode.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		edtCode.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					if (!TextUtils.isEmpty(getCode())) {
						((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity
								.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
					mPresenter.getFoodByCode();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnLight.getId()) {

			((BaseActivity) mActivity).requestPermission(ValuePermission.PermissionRequest_CAMERA, ValuePermission.PermissionGroupCAMERA,
					new Runnable() {
						@Override
						public void run() {
							if (CameraManager.get().isOffLight()) {
								CameraManager.get().openOnLight();
								btnLight.setSelected(true);
							} else {
								CameraManager.get().closeOffLight();
								btnLight.setSelected(false);
							}
						}
					}, new Runnable() {
						@Override
						public void run() {
							android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
							builder.setTitle(getString(R.string.camera_camcel_light_title));
							builder.setPositiveButton(getString(R.string.sure), null);
							builder.show();
						}
					});
		}
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	@Override
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		String resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(context, getString(R.string.cashOrder_code_scanfail), Toast.LENGTH_SHORT).show();
		} else {
			sendBroadcastToFragment(resultString);
			edtCode.setText(resultString);
			mPresenter.getFoodByCode();
		}
		getHandler().sendMessageDelayed(getHandler().obtainMessage(R.id.restart_preview), 2000);
		// continuePreview();// 重新扫描
	}

	public void sendBroadcastToFragment(String result) {
		Intent intent = new Intent();
		intent.setAction(SCAN_RESULT_ACTION);
		intent.putExtra(ValueKey.BARCODE, result);
		mActivity.sendBroadcast(intent);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	@Override
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	@Override
	public Handler getHandler() {
		return handler;
	}

	@Override
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			mActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/**
	 * you should get result like this. String scanResult = data.getExtras().getString("result");
	 */
	@Override
	public void resturnScanResult(int resultCode, Intent data) {
		Toast.makeText(context, "fragment result:" + data.getExtras().getString("result"), 0).show();
	}

	@Override
	public void launchProductQuary(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return edtCode.getText().toString();
	}

	@Override
	public void showDilogNum(final FoodEntity food) {
		// TODO Auto-generated method stub
		if (food == null)
			return;
		if (food.isTypeWeight()) {
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
			builder.setTitle(food.getName());

			View viewWeight = LayoutInflater.from(context).inflate(R.layout.view_cashorder_codeweight_dialog, null);
			final EditText edtWeight = (EditText) viewWeight.findViewById(R.id.edt_dialogweight_weight);
			Button btnWeight = (Button) viewWeight.findViewById(R.id.btn_dialogweight_getWeight);
			btnWeight.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					double weight = 0;
					if (BluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
						weight = BluetoothService.getWeight();
						edtWeight.setText(weight + "");
					} else {
						startActivity(new Intent(mActivity, BluetoothListActivity.class).putExtra(ValueKey.TITLE,
								getString(R.string.title_devicemanage)));
					}
				}
			});
			builder.setView(viewWeight);
			builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					String numS = edtWeight.getText().toString().trim();
					if (!TextUtils.isEmpty(numS)) {
						food.setNum(StringUtil.getDouble(numS));
						mPresenter.addToCart(food);
					}
				}
			});
			builder.setNegativeButton(getString(R.string.cancel), null);
			builder.show();
		} else {
			mPresenter.addToCart(food);
		}
	}

	@Override
	public void showCodeSearch(String code) {
		// TODO Auto-generated method stub
		foodCodeBefore = code;
	}
}