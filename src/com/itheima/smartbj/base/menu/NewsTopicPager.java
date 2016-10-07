package com.itheima.smartbj.base.menu;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.itheima.smartbj.base.BaseMenuPager;

public class NewsTopicPager extends BaseMenuPager {

	public NewsTopicPager(Context context) {
		super(context);
	}

	@Override
	protected View initView() {
		TextView view = new TextView(mContext);
		view.setText("新闻专题");
		return view;
	}

}
