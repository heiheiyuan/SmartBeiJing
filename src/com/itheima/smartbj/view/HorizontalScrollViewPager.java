package com.itheima.smartbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {

	private int downX;
	private int downY;
	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * 一.上下滑动不处理事件
	 * 二.左右滑动
	 * 		1.当处于第一页,手指从左往右,自己不处理getParent().requestDisallowInterceptTouchEvent(false)
	 * 		2.当处于最后一页,手指从左往右,自己不处理
	 * 		3.其他情况自己处理getParent().requestDisallowInterceptTouchEvent(true)
	 * */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			//得到down事件后,请求父控件不要拦截事件
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			int offsetX = moveX - downX;
			int offsetY = moveY - downY;
			//上下滑动不处理
			if (Math.abs(offsetX) < Math.abs(offsetY)) {
				getParent().requestDisallowInterceptTouchEvent(false);
			}else {
				//左右滑动
				//1.当处于第一页时手指从左往右,自己不处理
				if(getCurrentItem() == 0 && offsetX > 0) {
					getParent().requestDisallowInterceptTouchEvent(false);
					
				}else if (getCurrentItem() == getAdapter().getCount() - 1 && offsetX < 0) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}else
					getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
