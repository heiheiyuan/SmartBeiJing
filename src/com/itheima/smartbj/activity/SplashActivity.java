package com.itheima.smartbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.itheima.smartbj.R;
import com.itheima.smartbj.global.GlobalConstant;
import com.itheima.smartbj.utils.SharePreferenceUtils;

public class SplashActivity extends Activity {
	private RelativeLayout mWelcomeRl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mWelcomeRl = (RelativeLayout) findViewById(R.id.rl_welcome);
		animation();
	}

	public void animation() {
		//旋转动画
		RotateAnimation ra = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, .5f);
		ra.setFillAfter(true);
		ra.setDuration(1000);
		//缩放动画
		ScaleAnimation sa = new ScaleAnimation(0, 1f, 0, 1f, RotateAnimation.RELATIVE_TO_SELF,  0.5f, RotateAnimation.RELATIVE_TO_SELF,  0.5f);
		sa.setFillAfter(true);
		sa.setDuration(1000);
		//透明动画
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setFillAfter(true);
		aa.setDuration(1000);
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ra);
		as.addAnimation(sa);
		as.addAnimation(aa);
		mWelcomeRl.startAnimation(as);
		as.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean isOpened = SharePreferenceUtils.getBoolean(getApplicationContext(),GlobalConstant.IS_OPENED);
				if (!isOpened) {
					startActivity(new Intent(SplashActivity.this,GuideActivity.class));
				}else{
					startActivity(new Intent(SplashActivity.this,MainUi.class));
				}
				finish();	
			}
		});
	}
}
