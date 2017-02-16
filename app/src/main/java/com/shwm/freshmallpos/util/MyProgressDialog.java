package com.shwm.freshmallpos.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shwm.freshmallpos.R;

import java.lang.ref.WeakReference;

/**
 * 自定义进度对话框ProgressDialog
 *
 * @author Administrator
 */
public class MyProgressDialog extends Dialog {

    private static WeakReference<Activity> mWeakReferenceActivity;

    public MyProgressDialog(Context context) {
        this(context, 0);
    }

    private MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }


    /**
     * 弹出自定义ProgressDialog
     *
     * @param activity               上下文
     * @param message                提示
     * @param cancelable             是否按返回键取消
     * @param canceledOnTouchOutside 是否点击区域外消失
     * @param cancelListener         按下返回键监听
     * @return
     */
    public static MyProgressDialog show(Activity activity, CharSequence message, boolean cancelable,
                                        boolean canceledOnTouchOutside, OnCancelListener cancelListener) {
        mWeakReferenceActivity = new WeakReference<Activity>(activity);
        if (mWeakReferenceActivity.get() == null) {
            return null;
        }
        Activity mActivity = mWeakReferenceActivity.get();
        MyProgressDialog mDialog = new MyProgressDialog(mActivity, R.style.loading_dialog);
        mDialog.setContentView(R.layout.view_dialog_loading);
        mDialog.setTitle("");
        TextView txt = (TextView) mDialog.findViewById(R.id.tv_dialogLoading_tv);
        if (message == null || message.length() == 0) {
            txt.setVisibility(View.GONE);
        } else {
            txt.setVisibility(View.VISIBLE);
            txt.setText(message);
        }
        // 按返回键是否取消
        mDialog.setCancelable(cancelable);
        // 监听返回键
        mDialog.setOnCancelListener(cancelListener);
        // 是否点击区域外消失 ，false点击对话框外边，不dismiss
        mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

        // 设置居中
        mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.5f;
        mDialog.getWindow().setAttributes(lp);
         mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//设置Dialog自身的黑暗度,WindowManager.LayoutParams.FLAG_BLUR_BEHIND（设置模糊）WindowManager.LayoutParams.FLAG_DIM_BEHIND（设置暗淡）
        // 动画
        // Animation animation = AnimationUtils.loadAnimation(mDialog.getContext(), R.anim.dialog_progress_anim);
        // ImageView imageView = (ImageView) mDialog.findViewById(R.id.iv_myProgressDialog_spinnerImageView);
        // imageView.startAnimation(animation);
        // ProgressBar diologProgress = (ProgressBar) mDialog.findViewById(R.id.dialog_progressBar);
        mDialog.show();
        return mDialog;
    }


    // @Override
    // public void dismiss() {
    // // TODO Auto-generated method stub
    // if (mWeakReferenceActivity.get() != null) {
    // if (!mWeakReferenceActivity.get().isFinishing() && isShowing())
    // super.dismiss();
    // }
    // }
    //
    // @Override
    // public void show() {
    // // TODO Auto-generated method stub
    // if (mWeakReferenceActivity.get() != null) {
    // if (!mWeakReferenceActivity.get().isFinishing() && !isShowing())
    // super.show();
    // }
    // }
}
