package com.shwm.freshmallpos.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ViewPage 适配器
 * 
 * @author wr 2016-11-29
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> listFragment;
	private List<String> listTitle;

	public SectionsPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	public void setListFragment(List<Fragment> listFragment, List<String> listTitle) {
		this.listFragment = listFragment;
		this.listTitle = listTitle;
	}

	// public void setListTitle(List<String> listTitle) {
	// this.listTitle = listTitle;
	// }

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class below).
		return listFragment.get(position);
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return listFragment == null ? 0 : listFragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		// 返回title 可以配合TabLayout 使用
		if (listTitle != null && listTitle.size() > position) {
			return listTitle.get(position);
		}
		return null;
	}

}
