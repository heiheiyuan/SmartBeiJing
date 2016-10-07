package com.itheima.smartbj.base;

import android.content.Context;
import android.view.View;

public abstract class BaseMenuPager {
	public Context mContext;
	public View rootView;
	public BaseMenuPager(Context context) {
		this.mContext = context;
		this.rootView = initView();
	}
	protected abstract View initView();
	public void initData() {

	}
}
