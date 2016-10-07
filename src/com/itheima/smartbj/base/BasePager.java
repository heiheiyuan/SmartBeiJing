package com.itheima.smartbj.base;

import com.itheima.smartbj.R;
import com.itheima.smartbj.activity.MainUi;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class BasePager implements OnClickListener{
	public Context mContext;
	public View rootView;
	public TextView mTitleTv;
	public ImageButton mMenuIbtn;
	public FrameLayout mContentFl;
	public ImageButton mType;
	public BasePager(Context context) {
		mContext = context;
		rootView = initView();
	}
	public View initView() {
		View view = View.inflate(mContext, R.layout.basepager, null);
		mTitleTv = (TextView) view.findViewById(R.id.tv_basepager_title);
		mMenuIbtn = (ImageButton) view.findViewById(R.id.ib_basepager_menu);
		mMenuIbtn.setOnClickListener(this);
		mContentFl = (FrameLayout) view.findViewById(R.id.fl_basepager_content);
		mType = (ImageButton) view.findViewById(R.id.ib_titlebar_grid);
		return view;
	}
	public void initData() {
	}
	@Override
	public void onClick(View v) {
		if (v == mMenuIbtn) {
			MainUi mainUi = (MainUi) mContext;
			mainUi.toggle();
		}

	}
}
