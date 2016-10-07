package com.itheima.smartbj.base.imp;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.smartbj.base.BasePager;

public class GovaffairsPager extends BasePager {

	public GovaffairsPager(Context context) {
		super(context);
	}
	@Override
	public void initData() {
		mTitleTv.setText("政务");
		mMenuIbtn.setVisibility(View.VISIBLE);
		//添加内容
		TextView textView = new TextView(mContext);
		textView.setText("政务");
		textView.setGravity(Gravity.CENTER);
		//因为是帧布局,需要将原来的内容移除掉
		mContentFl.removeAllViews();
		mContentFl.addView(textView);
	}

}
