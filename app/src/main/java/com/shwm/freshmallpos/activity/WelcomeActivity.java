package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.presenter.MWelcomePresenter;
import com.shwm.freshmallpos.view.IWelcomeView;

public class WelcomeActivity extends BaseActivity<IWelcomeView,MWelcomePresenter> implements  IWelcomeView , Animation.AnimationListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        mPresenter.login();
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public MWelcomePresenter initPresenter() {
        return new MWelcomePresenter();
    }

    @Override
    public void mOnClick(View v) {

    }

    private void setAnimation(){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
        //设置执行的时间
        alpha.setDuration(3000);
        animationSet.addAnimation(alpha);
        animationSet.setAnimationListener(this);
        findViewById(R.id.iv_welcome_img).setAnimation(animationSet);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    mPresenter.end();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
