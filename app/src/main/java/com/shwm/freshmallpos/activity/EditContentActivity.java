package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.value.ValueKey;

public class EditContentActivity extends BaseActivity {
    private String title="";
    private EditText edtContent;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_edit_content;
    }

    @Override
    public MBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        Bundle bundle=getIntent().getExtras();
        title= bundle.getString(ValueKey.TITLE);
        content=bundle.getString(ValueKey.Content);
    }

    @Override
    protected void initView() {
        super.initView();
        edtContent= (EditText) findViewById(R.id.edt_content);
    }

    @Override
    protected void setValue() {
        super.setValue();
        edtContent.setText(content);
    }
    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_editContent,title);
    }

    @Override
    public void mOnClick(View v) {

    }

    @Override
    protected void onBack() {
        Intent intent=new Intent();
        intent.putExtra(ValueKey.Content,edtContent.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
