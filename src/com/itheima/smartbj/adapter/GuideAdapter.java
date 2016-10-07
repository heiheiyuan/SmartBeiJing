package com.itheima.smartbj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideAdapter extends PagerAdapter {
	public Context mContext;
	public List<ImageView> mList;
	public GuideAdapter(Context context, ArrayList<ImageView> images) {
		mContext = context;
		mList = images;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 把药显示的条目添加到ViewPager中
		ImageView view = mList.get(position);
		container.addView(view);

		return view;
	}
}
