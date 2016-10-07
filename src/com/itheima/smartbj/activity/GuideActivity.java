package com.itheima.smartbj.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.itheima.smartbj.R;
import com.itheima.smartbj.adapter.GuideAdapter;
import com.itheima.smartbj.global.GlobalConstant;
import com.itheima.smartbj.utils.SharePreferenceUtils;

public class GuideActivity extends Activity implements OnClickListener{
	private ViewPager mGuideVp;
	private ArrayList<ImageView> images;
	private LinearLayout mGuideSpotLl;
	private ImageView mGuideSpotIv;
	private Button mGuideEnterBtn;
	private int px;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_ui);
		initView();
		//准备数据
		initData();
		//设置数据适配器
		GuideAdapter adapter = new GuideAdapter(this,images);
		mGuideVp.setAdapter(adapter);
		mGuideVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == images.size() - 1) {
					mGuideEnterBtn.setVisibility(View.VISIBLE);
					mGuideSpotLl.setVisibility(View.GONE);
				}else {
					mGuideEnterBtn.setVisibility(View.GONE);
					mGuideSpotLl.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//计算红点移动的距离
				int redPointX = (int) ((positionOffset + position) * px * 2);
				//让红点移动:动态移动红点的左边距
				android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mGuideSpotIv.getLayoutParams();
				params.leftMargin = redPointX;
				mGuideSpotIv.setLayoutParams(params);
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}
	public void initData() {
		// 把资源图片变成imageView控件
		int[] imgIds = new int[] {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
		images = new ArrayList<ImageView>();
		for (int i = 0; i < imgIds.length; i++) {
			ImageView guideView = new ImageView(this);
			guideView.setImageResource(imgIds[i]);
			images.add(guideView);
			//动态创建灰点,添加到容器中
			ImageView spotView = new ImageView(this);
			//spotView.setScaleType(ScaleType.FIT_CENTER);
			px = dpToPx(10);
			LayoutParams params = new LayoutParams(px, px);
			//设置间距
			if (i != 0) {
				params.leftMargin = px;
			}
			spotView.setLayoutParams(params);
			spotView.setBackgroundResource(R.drawable.guide_spot);
			mGuideSpotLl.addView(spotView);
		}
	}
	public void initView() {
		mGuideVp = (ViewPager) findViewById(R.id.guide_vp);
		mGuideSpotLl = (LinearLayout) findViewById(R.id.guide_spot_ll);
		mGuideSpotIv = (ImageView) findViewById(R.id.guide_spot_iv);
		mGuideEnterBtn = (Button) findViewById(R.id.guide_enter_btn);
		mGuideEnterBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == mGuideEnterBtn) {
			startActivity(new Intent(this,MainUi.class));
			SharePreferenceUtils.putBoolean(this, GlobalConstant.IS_OPENED, true);
			finish();
		}

	}
	//dp转px
	public int dpToPx(int dp) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (dp * density);

	}
}
