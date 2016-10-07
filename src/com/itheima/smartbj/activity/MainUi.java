package com.itheima.smartbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.itheima.smartbj.R;
import com.itheima.smartbj.fragment.ContentFragment;
import com.itheima.smartbj.fragment.LeftFrament;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainUi extends SlidingFragmentActivity {
	private static final String LEFT_TAG = "left";
	private static final String MAIN_TAG = "main";
	private FragmentManager fragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置正文
		setContentView(R.layout.activity_main_ui);
		//设置左侧菜单
		setBehindContentView(R.layout.left_menu);
		//设置菜单模式
		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		/*//设置右边菜单
		menu.setSecondaryMenu(R.layout.right_menu);
		menu.setMode(SlidingMenu.LEFT_RIGHT);*/
		//设置触摸范围
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置正文界面保持宽度
		menu.setBehindOffset(200);
		//使用fragment
		initFragment();
	}

	public void initFragment() {
		//获取fragment管理器
		fragmentManager = getSupportFragmentManager();
		//开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//替换fragment
		transaction.replace(R.id.fl_left_menu, new LeftFrament(), LEFT_TAG);
		transaction.replace(R.id.fl_main, new ContentFragment(), MAIN_TAG);
		//提交事务
		transaction.commit();
	}
	public LeftFrament getLeftFrament () {
		return (LeftFrament) fragmentManager.findFragmentByTag(LEFT_TAG);
	}
	//获取contentFragment
	public ContentFragment getContentFragment() {
		return (ContentFragment) fragmentManager.findFragmentByTag(MAIN_TAG);
	}
}
