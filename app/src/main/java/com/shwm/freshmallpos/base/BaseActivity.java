package com.shwm.freshmallpos.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.manage.ActivityCollector;
import com.shwm.freshmallpos.manage.ThreadManager;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.sys.StatusBarCompat;
import com.shwm.freshmallpos.util.CommonUtil;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.view.IBaseView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseActivity<V, T extends MBasePresenter<V>> extends AppCompatActivity implements View.OnClickListener,
        IBaseView {
    public T mPresenter;
    protected String TAG = getClass().getSimpleName();
    protected Activity mActivity;
    protected Context context;
    protected View mViewParent;
    protected Toolbar mToolbar;
    /**
     * 线程名列表
     */
    protected List<String> threadNameList;
    private boolean isAlive = false;
    private boolean isRunning = false;
    private ProgressDialog mDialogProgress;
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<Integer, Runnable>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<Integer, Runnable>();

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mActivity = BaseActivity.this;
        isAlive = true;
        ActivityCollector.addActivity(TAG,this);
        context = getApplicationContext();
        mViewParent = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mViewParent);
        mPresenter = initPresenter();
        StatusBarCompat.compat(this);//沉浸状态栏
        init();
        initToolbar();
        initView();
        setValue();
        setListener();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isRunning = true;
        if (mPresenter != null) {
            mPresenter.attach((V) this);
            mPresenter.setActivity(mActivity);
        }
        MobclickAgent.onPageStart(TAG);//统计页面
        MobclickAgent.onResume(this);//统计时长
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isRunning = false;
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        isAlive = false;
        isRunning = false;
        dismissDialogProgress();
        if (mPresenter != null) {
            mPresenter.dettach();
            mPresenter.deleteActivity();
        }
        MobclickAgent.onKillProcess(mActivity);
        super.onDestroy();
        ActivityCollector.removeActivity(TAG);
        mViewParent = null;
        mToolbar = null;
        mDialogProgress = null;
        mPresenter = null;
        context = null;
        mActivity = null;
        ApplicationMy.getRefWatcher().watch(this);
    }

    /**
     * [绑定布局] @return 布局idF
     * 必须执行 绑定布局
     */
    public abstract int bindLayout();

    /**
     * 实例化presenter
     */
    public abstract T initPresenter();

    /**
     * 初始化数据
     */
    protected void init() {

    }

    /**
     * 设置Toolbar
     */
    protected void initToolbar() {

    }

    /**
     * 设置toolbar
     * @param resId Toolbar资源id
     */
    public Toolbar setToolbar(int resId, String title) {
        mToolbar = (Toolbar) findViewById(resId);
        if (mToolbar != null) {
            mToolbar.setTitle(title);// 设置主标题
            mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//            {
//                mToolbar.getLayoutParams().height = getAppBarHeight();
//                mToolbar.setPadding(mToolbar.getPaddingLeft(),
//                        getStatusBarHeight(),
//                        mToolbar.getPaddingRight(),
//                        mToolbar.getPaddingBottom());
//            }
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_action_back_light);// 设置导航栏图标
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });
        }
        return mToolbar;
    }

    /**
     * 获取顶部状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取顶部导航栏高度
     */
    public int getAppBarHeight() {
        int resourceId = 0;
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     * 初始化控件
     */
    protected void initView() {

    }

    /**
     * 设置值
     * 必须先执行initView()初始化控件
     */
    protected void setValue() {

    }

    /**
     * 设置监听
     * 必须先执行initView()初始化控件
     */
    protected void setListener() {
    }

    /** View点击onClick防止快速点击 **/
    public abstract void mOnClick(View v);

    /** 返回 */
    protected void onBack() {
        finish();
    }

    // 显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /** [沉浸状态栏] */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 展示加载进度条
     * @param message s
     */
    public void showDialogProgress(final String title, final String message) {
        runUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialogProgress == null) {
                    mDialogProgress = new ProgressDialog(mActivity);
                    mDialogProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置进度条风格，风格为圆形，旋转的
                }
                if (mDialogProgress.isShowing()) {
                    mDialogProgress.dismiss();
                }
                if (!StringUtil.isEmpty(title, false)) {
                    mDialogProgress.setTitle(title);
                }
                if (!StringUtil.isEmpty(message, false)) {
                    mDialogProgress.setMessage(message);
                }
                mDialogProgress.setCanceledOnTouchOutside(false);
                mDialogProgress.show();
            }
        });

    }

    @Override
    public void showDialogProgress() {
        showDialogProgress(null, getString(R.string.loading));
    }

    /**
     * 隐藏加载进度
     */
    @Override
    public void dismissDialogProgress() {
        if (mDialogProgress != null) {
            mDialogProgress.dismiss();
        }
    }

    @Override
    public void showFailInfo(int status, Exception exceptions) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.fail));
        builder.setMessage(StringFormatUtil.getFailInfoStatu(status, exceptions));
        builder.setPositiveButton(getString(R.string.sure), null);
        builder.show();
    }

    // 显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //运行线程 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void toastInfo(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 在UI线程中运行，建议用这个方法代替runOnUiThread
     * @param action
     */
    public final void runUiThread(Runnable action) {
        if (!isAlive()) {
            Log.w(TAG, "runUiThread  isAlive() == false >> return;");
            return;
        }
        runOnUiThread(action);
    }

    /**
     * 运行线程
     * @param name
     * @param runnable
     * @return
     */
    public final Handler runThread(String name, Runnable runnable) {
        if (!isAlive()) {
            Log.w(TAG, "runThread  isAlive() == false >> return null;");
            return null;
        }
        name = StringUtil.getTrimedString(name);
        Handler handler = ThreadManager.getInstance().runThread(name, runnable);
        if (handler == null) {
            Log.e(TAG, "runThread handler == null >> return null;");
            return null;
        }
        if (!threadNameList.contains(name)) {
            threadNameList.add(name);
        }
        return handler;
    }
    //运行线程 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public void onClick(View v) {
        if (CommonUtil.fastClick())
            if (v != null) {
                mOnClick(v);
            }
    }

    public final boolean isAlive() {
        return isAlive && mActivity != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }

    /**
     * 5.0权限管理
     * 请求权限
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    public void requestPermission(int id, String permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }
        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        // 版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            // 减少是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions(mActivity, new String[]{permission}, id);
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            allowRun.run();
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            disallowRun.run();
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideInput() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // 点击空白处隐藏软键盘
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideInput();
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            onBack();
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
