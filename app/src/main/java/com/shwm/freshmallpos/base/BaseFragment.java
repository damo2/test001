package com.shwm.freshmallpos.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.MyProgressDialog;
import com.shwm.freshmallpos.util.StringFormatUtil;
import com.shwm.freshmallpos.util.UL;

public abstract class BaseFragment<V, T extends MBasePresenter<V>> extends Fragment implements OnClickListener {
    protected String TAG = getClass().getSimpleName();
    public T mPresenter;
    protected Activity mActivity;
    protected Context context;
    /**
     * 根view
     */
    protected View mRootView;
    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    private MyProgressDialog mDialogProgress;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        context = mActivity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UL.v(TAG, "onCreateView");
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        mPresenter = initPresenter();
        initData(getArguments());
        init();
        initView();
        mIsPrepare = true;
        setValue();
        setListener();
        return mRootView;
    }

    /**
     * 设置根布局资源id
     */

    protected abstract int setLayoutResouceId();

    // 实例化presenter
    protected abstract T initPresenter();

    /**
     * 初始化数据
     */
    protected void init() {

    }

    /**
     * 初始化数据 @param arguments接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments) {
    }

    ;

    /**
     * 初始化View
     */
    protected void initView() {
    }

    ;

    /**
     * View赋值
     */
    protected void setValue() {

    }

    /**
     * 设置监听事件
     */
    protected void setListener() {

    }

    ;

    /**
     * View点击onClick防止快速点击
     **/
    public abstract void mOnClick(View v);

    protected View findViewById(int id) {
        // TODO Auto-generated method stub
        return mRootView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if (com.shwm.freshmallpos.util.CommonUtil.fastClick())
            mOnClick(v);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser) {
            onVisibleToUser();
        } else {
            onInVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 用户不可见时执行的操作
     */
    protected void onInVisibleToUser() {
        if (mIsPrepare && !mIsVisible) {
            onLazyClear();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    protected void onLazyLoad() {
        UL.d(TAG, "onLazyLoad()");
    }

    /**
     * 不可见时清理
     */
    protected void onLazyClear() {
        UL.d(TAG, "onLazyClear()");
    }

    // 显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public void showDialogProgress(String message) {
        if (mDialogProgress != null) {
            mDialogProgress.dismiss();
            mDialogProgress = null;
        }
        mDialogProgress = MyProgressDialog.show(mActivity, message, false, false, null);
    }

    public void showDialogProgress() {
        showDialogProgress(null);
    }

    /**
     * 隐藏加载进度
     */
    public void dismissDialogProgress() {
        if (mDialogProgress != null) {
            mDialogProgress.dismiss();
        }
    }

    public void showFailInfo(int statu, Exception exception) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
        builder.setTitle(getString(R.string.fail));
        builder.setMessage(StringFormatUtil.getFailInfoStatu(statu, exception));
        builder.setPositiveButton(getString(R.string.sure), null);
        builder.show();
    }

    public void toastInfo(String info) {
        // TODO Auto-generated method stub
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    // 显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        UL.v(TAG, "onDestroy");
        super.onDestroy();
        dismissDialogProgress();
    }
}