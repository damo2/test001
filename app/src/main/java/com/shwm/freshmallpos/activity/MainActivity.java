package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.SectionsPagerAdapter;
import com.shwm.freshmallpos.contentprovider.Admin;
import com.shwm.freshmallpos.contentprovider.FreshPosHelper;
import com.shwm.freshmallpos.contentprovider.FreshmallPosContent;
import com.shwm.freshmallpos.manage.ActivityCollector;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.fragment.MainCashFragment;
import com.shwm.freshmallpos.fragment.MainMyFragment;
import com.shwm.freshmallpos.fragment.MainOrderFragment;
import com.shwm.freshmallpos.manage.BusinessInfo;
import com.shwm.freshmallpos.presenter.MBasePresenter;
import com.shwm.freshmallpos.util.UL;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> listFragment;

    //	 定义菜单项的ItemId
    private final int NEWS = 00;

    private BottomNavigationBar bottomNavigationBar;

    private Toolbar toolbar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewpager();
        initBottom();
        changeTab(0);
        changeFragment(0);
        contentProviderUpdateData();
    }
    private void contentProviderUpdateData() {
        // ContentProvider共享数据
        Admin admin = new Admin();
        admin.setId(BusinessInfo.getAdminID());
        admin.setUsername(BusinessInfo.getAdminName());
        admin.setPassword(BusinessInfo.getPassword());
        admin.setImg(BusinessInfo.getBusinessLogo());
        admin.setType(BusinessInfo.getAdminType());
        admin.setNickname(BusinessInfo.getAdminNickname());
        admin.setTime(FreshmallPosContent.getDBCurrentTime());
        FreshPosHelper freshPosHelper = new FreshPosHelper(getApplicationContext());
        if (freshPosHelper.queryByAdminId(BusinessInfo.getAdminID()) == null) {
            UL.d(TAG, "不存在 insert"+BusinessInfo.getAdminName());
            freshPosHelper.insert(admin);
        } else {
            UL.d(TAG, "已经存在 update"+BusinessInfo.getAdminName());
            freshPosHelper.updateByAdminId(BusinessInfo.getAdminID(), admin);
        }
        Admin adminLast = freshPosHelper.queryLast();
        UL.d(TAG+"admin", "LAST _id=" + adminLast.get_id() + " adId=" + adminLast.getId() + " adNm=" + adminLast.getUsername() + " adPw="
                + adminLast.getPassword() + " adImg=" + adminLast.getImg() + " adTm=" + adminLast.getTime());
        List<Admin> listAdmin = freshPosHelper.queryAll();
        for (Admin ad : listAdmin) {
            UL.d(TAG+"admin", "_id=" + ad.get_id() + " adId=" + ad.getId() + " adNm=" + ad.getUsername() + " adPw=" + ad.getPassword() + " adImg="
                    + ad.getImg() + " adTm=" + ad.getTime());
        }
    }
    @Override
    public int bindLayout() {
        // TODO Auto-generated method stub
        return R.layout.activity_pos_main;
    }

    @Override
    public MBasePresenter initPresenter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        toolbar=setToolbar(R.id.toolbar_main,"");
        toolbar.setNavigationIcon(null);
        tvTitle= (TextView) findViewById(R.id.toolbar_title_main);

    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
        listFragment = new ArrayList<Fragment>();
        listFragment.add(MainCashFragment.newInstance("a"));
        listFragment.add(MainOrderFragment.newInstance(getString(R.string.title_mainOrder)));
        listFragment.add(MainMyFragment.newInstance(getString(R.string.title_mainMy)));
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    @Override
    protected void setValue() {
        // TODO Auto-generated method stubF
        super.setValue();
    }

    private void initBottom() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.selector_main_tab_cash, getString(R.string.main_tab_cash)).setActiveColorResource(R.color.bg_orange))
                .addItem(new BottomNavigationItem(R.drawable.selector_main_tab_order, getString(R.string.main_tab_order)).setActiveColorResource(R.color.bg_orange))
                .addItem(new BottomNavigationItem(R.drawable.selector_main_tab_my, getString(R.string.main_tab_my)).setActiveColorResource(R.color.bg_orange))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        bottomNavigationBar.setTabSelectedListener(onTabSelectedListener);
    }


    private BottomNavigationBar.OnTabSelectedListener onTabSelectedListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
        // changeTab(position);
            changeFragment(position);
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {

        }
    };


    private void initViewpager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter.setListFragment(listFragment, null);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }
        });
    }


    // 0是收银 1是流水 2是我的
    private void changeTab(int item) {
        bottomNavigationBar.selectTab(item);
        switch (item){
            case 0:
                toolbar.setVisibility(View.GONE);
                break;
            case 1:
                toolbar.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.main_tab_order));
                break;
            case 2:
                toolbar.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.main_tab_my));
                break;
        }
    }

    private void changeFragment(int item) {
        mViewPager.setCurrentItem(item);
    }

    @Override
    public void mOnClick(View v) {
        // TODO Auto-generated method stub

    }

    // @Override
    // public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    // getMenuInflater().inflate(R.menu.main, menu);
    // }
    //
    // // 用户单击Menu键时触发
    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // 添加新建菜单项
    // menu.add(0, NEWS, 0, "新建");
    // return super.onCreateOptionsMenu(menu);
    // }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exitBy2Click(); // 调用双击退出函数
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityCollector.finishAll();
        }
    }
}
