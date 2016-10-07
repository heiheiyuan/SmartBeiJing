package com.itheima.smartbj.base.imp;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.activity.MainUi;
import com.itheima.smartbj.base.BaseMenuPager;
import com.itheima.smartbj.base.BasePager;
import com.itheima.smartbj.base.menu.NewsDetailsPager;
import com.itheima.smartbj.base.menu.NewsImagePager;
import com.itheima.smartbj.base.menu.NewsInteractPager;
import com.itheima.smartbj.base.menu.NewsTopicPager;
import com.itheima.smartbj.bean.NewscenterBean;
import com.itheima.smartbj.global.GlobalConstant;
import com.itheima.smartbj.utils.SharePreferenceUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private NewscenterBean bean;
	private ArrayList<BaseMenuPager> menuList;
	public NewsCenterPager(Context context) {
		super(context);
	}
	@Override
	public void initData() {
		mTitleTv.setText("新闻中心");
		mMenuIbtn.setVisibility(View.VISIBLE);
		//缓存新闻数据
		String cacheData = SharePreferenceUtils.getString(mContext,GlobalConstant.CACHE_DATA);
		if (!TextUtils.isEmpty(cacheData)) {
			parseJson(cacheData);
		}
		getDataFromServer();
	}
	public void getDataFromServer() {
		//获取对象
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configDefaultHttpCacheExpiry(0);// 把默认的缓存时间改为0
		//发送请求
		httpUtils.send(HttpMethod.GET, GlobalConstant.NEWSCENTER_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseinfo) {
				SharePreferenceUtils.putString(mContext,GlobalConstant.CACHE_DATA,responseinfo.result);
				parseJson(responseinfo.result);
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mContext, "网络请求失败",0).show();
			}
		});
	}
	public void parseJson(String result) {
		Gson gson = new Gson();
		bean = gson.fromJson(result, NewscenterBean.class);
		//System.out.println(bean);
		MainUi mainUi = (MainUi) mContext;
		mainUi.getLeftFrament().setData(bean.data);
		menuList = new ArrayList<BaseMenuPager>();
		menuList.add(new NewsDetailsPager(mContext,bean.data.get(0).children));
		menuList.add(new NewsTopicPager(mContext));
		menuList.add(new NewsImagePager(mContext));
		menuList.add(new NewsInteractPager(mContext));
		switchMenuPager(0);
	}
	public void switchMenuPager(int position) {
		// 修改标题
		mTitleTv.setText(bean.data.get(position).title);
		//加载布局
		mContentFl.removeAllViews();
		mContentFl.addView(menuList.get(position).rootView);
		//加载数据
		menuList.get(position).initData();
		if (position == 2) {
			//设置可见
			mType.setVisibility(View.VISIBLE);
			//获取组图页对象
			final NewsImagePager imagePager = (NewsImagePager) menuList.get(2);
			mType.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imagePager.switchType(mType);

				}
			});
		}else {
			mType.setVisibility(View.GONE);
		}
	}
}
