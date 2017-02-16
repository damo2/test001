package com.shwm.freshmallpos.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.FoodEntity;
import com.shwm.freshmallpos.model.biz.IPayWechatListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.UtilMath;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.wechatpay.ResultInfo;
import com.shwm.freshmallpos.wechatpay.WechatConfi;
import com.shwm.freshmallpos.wechatpay.WechatPay;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.DecodeHandlerInterface;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

/**
 * 微信支付 商家扫描顾客的微信二维码支付
 */
public class PaytypeWechatScanUserActivity extends BaseActivity implements Callback, DecodeHandlerInterface {
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
	private String title;

	private double totalYuan;
	private int totalFee;
	private String body = "支付-新鲜收银";

	private TextView tvMoney;
	private Button btnGetcode;

	private IPayWechatListener wechatPay = new WechatPay();
	private List<FoodEntity> listFood = new ArrayList<FoodEntity>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_wechatscan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_wechatscan);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		// quit the scan view
		// cancelScanButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// CodeActivity.this.finish();
		// }
		// });
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public int bindLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_camera_wechatscan;
	}

	@Override
	public MBasePresenter initPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mOnClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == btnGetcode.getId()) {
			wechatPay.getScanCodeBusiness(totalFee, WechatConfi.getDetailByListFood(listFood), new IRequestListener<String>() {
				@Override
				public void onSuccess(String codeUrl) {
					// TODO Auto-generated method stub
					dismissDialogProgress();
					Intent intent = new Intent(mActivity, PaytypeWechatScanBusinessActivity.class);
					intent.putExtra(ValueKey.TITLE, getString(R.string.title_paytype_wechat));
					intent.putExtra(ValueKey.CODERQ, codeUrl);
					intent.putExtra(ValueKey.MoneyReceivable, totalYuan);
					startActivity(intent);
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
					showFailInfo(statu, exception);
				}
			});

		}
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(ValueKey.TITLE);
		totalYuan = bundle.getDouble(ValueKey.Money);
		listFood = (List<FoodEntity>) bundle.getSerializable(ValueKey.LISTFOOD);
		totalFee = (int) UtilMath.div(UtilMath.mul(totalYuan + "", "100"), 1, 0);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		tvMoney = (TextView) findViewById(R.id.tv_money);
		btnGetcode = (Button) findViewById(R.id.btn_getCode);
	}

	@Override
	protected void setValue() {
		// TODO Auto-generated method stub
		super.setValue();
		tvMoney.setText(UtilMath.currency(totalYuan));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		super.setListener();
		btnGetcode.setOnClickListener(this);
	}

	@Override
	protected void initToolbar() {
		// TODO Auto-generated method stub
		super.initToolbar();
		setToolbar(R.id.toolbar_wechatscan,title);
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
		String trimCode = result.getText();
		// FIXME
		if (trimCode.equals("")) {
			Toast.makeText(PaytypeWechatScanUserActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			wechatPay.scanCodeUser(trimCode, totalFee, WechatConfi.getDetailByListFood(listFood), new IRequestListener<HashMap<String, Object>>() {
				@Override
				public void onPreExecute(int type) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(HashMap<String, Object> hashmap) {
					// TODO Auto-generated method stub
					ResultInfo resultInfoWX = (ResultInfo) hashmap.get(ValueKey.ResultInfoWx);
					double settlementMoney = UtilMath.div(resultInfoWX.getSettlementTotalFee() + "", "100", 2);
					double cashMoney = UtilMath.div(resultInfoWX.getCashFee() + "", "100", 2);
					AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
					builder.setTitle(getString(R.string.wechatpay_paySuc));
					builder.setMessage(String.format(getString(R.string.wechatpay_sucInfo), settlementMoney + "", cashMoney + ""));
					builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							finish();
						}
					});
					builder.show();
				}

				@Override
				public void onFail(int statu, Exception exception) {
					// TODO Auto-generated method stub
					showFailInfo(statu, exception);
				}
			});
		}
		// PaytypeWechatScanUserActivity.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.camera_camcel_scancode_title);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					finish();
				}
			});
			builder.create().show();
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
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
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

	@Override
	public void resturnScanResult(int resultCode, Intent data) {
		setResult(resultCode, data);
		finish();
	}

	@Override
	public void launchProductQuary(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(intent);
	}

}
