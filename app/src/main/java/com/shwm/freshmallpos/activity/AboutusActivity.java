package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.sys.AppPackageInfo;
import com.shwm.freshmallpos.sys.CheckUpdateInfo;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;

public class AboutusActivity extends BaseActivity {

    private String title;
    private Button btnUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_aboutus,title);
    }

    @Override
    public int bindLayout() {
        // TODO Auto-generated method stub
        return R.layout.activity_about_us;
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
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        btnUpdate = (Button) findViewById(R.id.btn_aboutus_update);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stub
        super.setValue();
        btnUpdate.setText(getString(R.string.update_version) + "  " + AppPackageInfo.getCurVerName(context, getPackageName()));
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btnUpdate.getId()) {
            CheckUpdateInfo checkUpdateInfo = new CheckUpdateInfo(mActivity);
            checkUpdateInfo.checkUpdateInfo();
        }
    }

}
