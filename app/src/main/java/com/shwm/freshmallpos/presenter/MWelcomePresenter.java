package com.shwm.freshmallpos.presenter;

import android.content.Intent;

import com.shwm.freshmallpos.activity.LogActivity;
import com.shwm.freshmallpos.activity.MainActivity;
import com.shwm.freshmallpos.model.biz.ILoginListener;
import com.shwm.freshmallpos.model.biz.IRequestListener;
import com.shwm.freshmallpos.model.biz.OnLoginListener;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.view.IWelcomeView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by as on 2017/2/20.
 */

public class MWelcomePresenter extends MBasePresenter<IWelcomeView> {
    private ILoginListener iLoginListener = new OnLoginListener();
    private boolean isAutoLogin;
    private boolean isLoginSuccess;
    private Timer timer;

    public MWelcomePresenter() {
        isAutoLogin = UtilSPF.getBoolean(ValueKey.IsLoginAuto, false);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isAutoLogin && isLoginSuccess) {
                    end();
                    cancelTimer();
                }
            }
        };
        timer.schedule(timerTask, 0, 200);
    }

    private void cancelTimer() {
        timer.cancel();
    }

    /**
     * 自动登录
     */
    public void login() {
        if (isAutoLogin) {
            String username = UtilSPF.getString(ValueKey.ADMIN_USERNAME, "");
            String password = UtilSPF.getString(ValueKey.ADMIN_PASSWORD, "");
            iLoginListener.login(username, password, new IRequestListener<Boolean>() {
                @Override
                public void onPreExecute(int type) {
                }

                @Override
                public void onSuccess(Boolean isSuc) {
                    isLoginSuccess = isSuc;
                }

                @Override
                public void onFail(int statu, Exception exception) {

                }
            });
        }
    }

    /**
     * 结束
     */
    public void end() {
        cancelTimer();
        boolean isGuide = UtilSPF.getBoolean(ValueKey.IsGuide, false);
        if (isGuide) {
            UtilSPF.putBoolean(ValueKey.IsGuide, true);
        } else {
            if (isLoginSuccess) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }
        getActivity().finish();
    }


}
