package com.shwm.freshmallpos.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.adapter.SectionsPagerAdapter;
import com.shwm.freshmallpos.base.ActivityCollector;
import com.shwm.freshmallpos.base.BaseActivity;
import com.shwm.freshmallpos.fragment.MainCashFragment;
import com.shwm.freshmallpos.fragment.MainMyFragment;
import com.shwm.freshmallpos.fragment.MainOrderFragment;
import com.shwm.freshmallpos.presenter.MBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> listFragment;

    private RadioGroup mRadioGroup;
    private RadioButton rbCash, rbOrder, rbMy;
    //	 定义菜单项的ItemId
    private final int NEWS = 00;

    private BottomNavigationBar bottomNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBarAndViewpager();
        initBottom();
        changeTab(0);
        changeFragment(0);
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
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_main);
        rbCash = (RadioButton) findViewById(R.id.rb_main_channel);
        rbOrder = (RadioButton) findViewById(R.id.rb_main_order);
        rbMy = (RadioButton) findViewById(R.id.rb_main_my);
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
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.selector_main_tab_cash, getString(R.string.main_tab_cash)).setActiveColorResource(R.color.bg_orange))
                .addItem(new BottomNavigationItem(R.drawable.selector_main_tab_order, getString(R.string.main_tab_order)).setActiveColorResource(R.color.bg_orange))
                .addItem(new BottomNavigationItem(R.drawable.selector_main_tab_my, getString(R.string.main_tab_my)).setActiveColorResource(R.color.bg_orange))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    protected void setListener() {
        // TODO Auto-generated method stub
        super.setListener();
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                changeTab(getItemByCheckedId(checkedId));
                changeFragment(getItemByCheckedId(checkedId));
            }
        });
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

    private void initActionBarAndViewpager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter.setListFragment(listFragment, null);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }
        });
    }

    // 通过Check的Id 得到item
    private int getItemByCheckedId(int checkedId) {
        int item = 0;
        if (checkedId == rbCash.getId()) {
            item = 0;
        } else if (checkedId == rbOrder.getId()) {
            item = 1;
        } else if (checkedId == rbMy.getId()) {
            item = 2;
        }
        return item;
    }

    // 0是收银 1是流水 2是我的
    private void changeTab(int item) {
        bottomNavigationBar.selectTab(item);
    }
//	private void changeTab(int item) {
//		switch (item) {
//		case 0:
//			if (!rbCash.isChecked()) {
//				mRadioGroup.check(rbCash.getId());
//			}
//			break;
//		case 1:
//			if (!rbOrder.isChecked()) {
//				mRadioGroup.check(rbOrder.getId());
//			}
//			break;
//		case 2:
//			if (!rbMy.isChecked()) {
//				mRadioGroup.check(rbMy.getId());
//			}
//			break;
//		default:
//			break;
//		}
//	}

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
