package com.shwm.freshmallpos.inter;

import android.view.View;

public interface IOnItemClickListener<T> {
	void onItemClick(View view, T item, int postion);
}
