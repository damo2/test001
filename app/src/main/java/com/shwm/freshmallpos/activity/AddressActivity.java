package com.shwm.freshmallpos.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.been.AddressEntity;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.model.biz.IBusinessListener;
import com.shwm.freshmallpos.model.biz.OnBusinessListener;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.value.ValueKey;

/**
 * 门店地址
 *
 * @author wr 2017-1-11
 */
public class AddressActivity extends BaseActivity {
    private String title;
    private View viewChoose;
    private AddressEntity address;
    private String addr;
    private EditText edtAddress;
    private IBusinessListener iBusinessListener;

    @Override
    public int bindLayout() {
        // TODO Auto-generated method stub
        return R.layout.activity_business_address;
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

        iBusinessListener = new OnBusinessListener();
    }

    @Override
    protected void initToolbar() {
        // TODO Auto-generated method stub
        super.initToolbar();
        setToolbar(R.id.toolbar_business_address, title);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_right) {
                    addr = edtAddress.getText().toString().trim();
                    Intent intent = new Intent();
                    intent.putExtra(ValueKey.Business_ADDRESS, addr);
                    setResult(RESULT_OK, intent);
                    mActivity.finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right, menu);
        MenuItem item = menu.findItem(R.id.menu_right);
        item.setTitle(getString(R.string.save));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        viewChoose = findViewById(R.id.rl_businessaddress_choose);
        edtAddress = (EditText) findViewById(R.id.edt_businessaddress_addr);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stub
        super.setValue();
        edtAddress.setText(BusinessInfo.getBusinessAddress());
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        viewChoose.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == viewChoose.getId()) {
            // startActivityForResult(
            // new Intent(mActivity, AmapLocationActivity.class).putExtra(ValueKeyUtil.TITLE,
            // getString(R.string.title_mapchoose)),
            // ValueRequestUtil.Address_Location);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//		if (requestCode == ValueRequestUtil.Address_Location && resultCode == RESULT_OK) {
//			address = (AddressEntity) data.getExtras().getSerializable(ValueKeyUtil.AddressEntity);
//			if (address != null) {
//				edtAddress.setText(address.getAddress());
//			}
//		}
        super.onActivityResult(requestCode, resultCode, data);
    }
}
