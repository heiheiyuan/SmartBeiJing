package com.itheima.smartbj.base.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.smartbj.R;
import com.itheima.smartbj.activity.MainUi;
import com.itheima.smartbj.base.BaseMenuPager;
import com.itheima.smartbj.bean.NewscenterBean.NewsTab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

public class NewsDetailsPager extends BaseMenuPager {

	private TabPageIndicator mIndicator;
	private ViewPager mPager;
	private List<NewsTab> mData;
	private ArrayList<TabDetailsPager> tabPagers;

	public NewsDetailsPager(Context context, List<NewsTab> children) {
		super(context);
		this.mData = children;
	}
	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.newsdetails, null);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		mPager = (ViewPager) view.findViewById(R.id.pager);
		return view;
	}
	class MyListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int position) {
			//获取MainUi
			MainUi mainUi = (MainUi) mContext;
			SlidingMenu menu = mainUi.getSlidingMenu();
			//当显示第一个页签是,让slidingMenu抢事件,其他时不抢
			if(position == 0) {
				menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}else {
				menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}
	}
	//绑定数据
	@Override
	public void initData() {
		tabPagers = new ArrayList<TabDetailsPager>();
		for (int i = 0; i < mData.size(); i++) {
			tabPagers.add(new TabDetailsPager(mContext,mData.get(i)));
		}
		mPager.setAdapter(new MyAdapter());
		//绑定ViewPager和ViewPagerIndicator
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(new MyListener());
	}
	class MyAdapter extends PagerAdapter {
		//给指针控件提供标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mData.get(position).title;
		}
		@Override
		public int getCount() {
			return mData.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//根据页签获取相应的页签详情界面
			TabDetailsPager tabDetailsPager = tabPagers.get(position);
			View view = tabDetailsPager.rootView;
			container.addView(view);
			//更新页签详情界面
			tabDetailsPager.initData();
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}
