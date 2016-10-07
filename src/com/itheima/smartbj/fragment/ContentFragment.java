package com.itheima.smartbj.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itheima.smartbj.R;
import com.itheima.smartbj.activity.MainUi;
import com.itheima.smartbj.base.BaseFragment;
import com.itheima.smartbj.base.BasePager;
import com.itheima.smartbj.base.imp.GovaffairsPager;
import com.itheima.smartbj.base.imp.HomePager;
import com.itheima.smartbj.base.imp.NewsCenterPager;
import com.itheima.smartbj.base.imp.SettingPager;
import com.itheima.smartbj.base.imp.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContentFragment extends BaseFragment {

	private ArrayList<BasePager> pagers;
	private ViewPager mContentVp;
	private RadioGroup mBottomRg;
	@Override
	protected View initView() {
		View view = View.inflate(activity, R.layout.content_fragment, null);
		mContentVp = (ViewPager) view.findViewById(R.id.vp_content);
		mBottomRg = (RadioGroup) view.findViewById(R.id.rg_bottom_button);
		mContentVp.setOnPageChangeListener(new MyOnPageChangeListener());
		mBottomRg.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		mBottomRg.check(R.id.rb_home);
		return view;
	}
	class MyOnCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_home:
				mContentVp.setCurrentItem(0,false);
				enableSlidingMenu(false);
				break;
			case R.id.rb_newscenter:
				mContentVp.setCurrentItem(1,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_service:
				mContentVp.setCurrentItem(2,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_govaffairs:
				mContentVp.setCurrentItem(3,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_setting:
				mContentVp.setCurrentItem(4,false);
				enableSlidingMenu(false);
				break;
			}
		}

	}
	public void enableSlidingMenu(boolean enable) {
		MainUi mainUi = (MainUi) activity;
		SlidingMenu menu = mainUi.getSlidingMenu();
		if (enable) {
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else {
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

	}
	class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int position) {
			pagers.get(position).initData();//当某一页被选中时才加载数据
		}
	}
	@Override
	protected void initData() {

		pagers = new ArrayList<BasePager>();
		//准备数据
		pagers.add(new HomePager(activity));
		pagers.add(new NewsCenterPager(activity));
		pagers.add(new SmartServicePager(activity));
		pagers.add(new GovaffairsPager(activity));
		pagers.add(new SettingPager(activity));
		//设置适配器
		mContentVp.setAdapter(new MyViewPager());
		//初始化数据
		pagers.get(0).initData();
	}
	class MyViewPager extends PagerAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pagers.get(position);
			//把当前位置pager对象升上的布局添加到ViewPager中
			View view = pager.rootView;
			container.addView(view);
			//当前位置的pager对象的布局更新数据
			//pager.initData(); //避免浪费流量
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
	//获取NewscenterPager
	public NewsCenterPager getNewsenterPager () {
		return (NewsCenterPager) pagers.get(1);
	}
}
