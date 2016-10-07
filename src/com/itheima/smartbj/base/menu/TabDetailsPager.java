package com.itheima.smartbj.base.menu;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.R;
import com.itheima.smartbj.activity.NewsDetailUI;
import com.itheima.smartbj.base.BaseMenuPager;
import com.itheima.smartbj.bean.NewscenterBean.NewsTab;
import com.itheima.smartbj.bean.TabDetailsBean;
import com.itheima.smartbj.bean.TabDetailsBean.Data.News;
import com.itheima.smartbj.bean.TabDetailsBean.Data.Topnews;
import com.itheima.smartbj.global.GlobalConstant;
import com.itheima.smartbj.utils.BitmapUtilsHelper;
import com.itheima.smartbj.utils.SharePreferenceUtils;
import com.itheima.smartbj.view.RefreshListView;
import com.itheima.smartbj.view.RefreshListView.OnRefreshingListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TabDetailsPager extends BaseMenuPager implements OnPageChangeListener,OnItemClickListener{
	public NewsTab mData;
	private ViewPager mNewsImgVp;
	private RefreshListView mNewsLv;
	private TextView mImgInfoTv;
	private LinearLayout mPointsLl;
	private List<Topnews> topnewsData;
	private int preLocation;
	private List<News> news;
	private BitmapUtils bitmapUtils;
	private boolean isFreshing;// 是否处于下拉刷新进入的getDataFromServer方法
	private String moreUrl;
	private boolean isLoadMore;
	private NewsAdapter newsAdpater;
	private MyHandler handler;
	public TabDetailsPager(Context context, NewsTab newsTab) {
		super(context);
		this.mData = newsTab;
		bitmapUtils = BitmapUtilsHelper.getBitmapUtils(mContext);
	}
	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.tabdetails, null);
		mNewsLv = (RefreshListView) view.findViewById(R.id.lv_tabdetails);
		View header = View.inflate(mContext, R.layout.top_tabdetail, null);
		mNewsImgVp = (ViewPager) header.findViewById(R.id.vp_tabdetails);
		mImgInfoTv = (TextView) header.findViewById(R.id.tv_tabdetail_imginfo);
		mPointsLl = (LinearLayout) header.findViewById(R.id.ll_tabdetail_points);
		mNewsLv.addHeaderView(header);
		mNewsLv.setOnRefreshingListener(new MyOnRefreshingListener());
		mNewsLv.setOnItemClickListener(this);
		return view;
	}
	class MyOnRefreshingListener implements OnRefreshingListener {
		@Override
		public void OnRefreshing() {
			isFreshing = true;
			getDataFromServer();
		}
		@Override
		public void onLoading() {
			if(TextUtils.isEmpty(moreUrl) ) {
				Toast.makeText(mContext, "没有更多数据了啊", 0).show();
				mNewsLv.loadMoreFinished();
				return;
			}
			isLoadMore = true;
			//加载更多
			gerMoreDataFromServer();
		}


	}
	private void gerMoreDataFromServer() {
		//访问网络
		HttpUtils httpUtils = new HttpUtils();
		//发送请求
		httpUtils.send(HttpMethod.GET, GlobalConstant.HTTP_URL + moreUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responInfo) {
				parseJson(responInfo.result);
				mNewsLv.loadMoreFinished();
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mContext, "访问网络失败", 0).show();
				mNewsLv.loadMoreFinished();
			}
		});

	}
	@Override
	public void initData() {
		//取出缓存数据
		String cacheJson = SharePreferenceUtils.getString(mContext, mData.url);
		if (!TextUtils.isEmpty(cacheJson)) {
			//System.out.println(cacheJson);
			parseJson(cacheJson);
		}
		getDataFromServer();
	}
	public void getDataFromServer() {
		//访问网络
		HttpUtils httpUtils = new HttpUtils();
		//发送请求
		httpUtils.send(HttpMethod.GET, GlobalConstant.HTTP_URL + mData.url, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responInfo) {
				//缓存新闻页签详情数据
				SharePreferenceUtils.putString(mContext, mData.url, responInfo.result);
				parseJson(responInfo.result);
				if (isFreshing){
					mNewsLv.refreshFinished(true);
					isFreshing = false;
				}
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mContext, "访问网络失败", 0).show();
				if (isFreshing){
					mNewsLv.refreshFinished(true);
					isFreshing = false;
				}
			}
		});
	}
	public void parseJson(String result) {
		Gson gson = new Gson();
		TabDetailsBean detailsBean = gson.fromJson(result, TabDetailsBean.class);
		moreUrl = detailsBean.data.more;
		if (!isLoadMore) {
			topnewsData = detailsBean.data.topnews;
			TopNewsAdapter adapter = new TopNewsAdapter();
			mNewsImgVp.setAdapter(adapter);
			mNewsImgVp.setOnPageChangeListener(this);
			//移除之前的红点
			mPointsLl.removeAllViews();
			//初始化图片描述
			mImgInfoTv.setText(topnewsData.get(0).title);
			for (int i = 0; i < topnewsData.size(); i++) {
				ImageView imageView = new ImageView(mContext);
				LayoutParams params = new LayoutParams(5,5);
				params.leftMargin = 10;
				imageView.setLayoutParams(params);
				imageView.setBackgroundResource(R.drawable.selector_tabdetails_points);
				mPointsLl.addView(imageView);
				imageView.setEnabled(false);
			}
			//初始化第一个红点
			mPointsLl.getChildAt(0).setEnabled(true);
			preLocation = 0;
			//更新新闻列表
			news = detailsBean.data.news;
			newsAdpater = new NewsAdapter();
			mNewsLv.setAdapter(newsAdpater);
		}else {//把访问moreUrl获得的数据添加
			List<News> moreList = detailsBean.data.news;
			news.addAll(moreList);
			newsAdpater.notifyDataSetChanged();
		}
		if (handler == null) {
			handler = new MyHandler();
		}
		handler.sendMessageDelayed(Message.obtain(), 3000);

	}
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mNewsImgVp.getWindowVisibility() == View.GONE) {
				handler.removeCallbacksAndMessages(null);
				//handler = null;
				return;
			}
			int nextItem = (mNewsImgVp.getCurrentItem() + 1) % topnewsData.size();
			if (nextItem == 0) {
				mNewsImgVp.setCurrentItem(nextItem,false);
			}
			mNewsImgVp.setCurrentItem(nextItem);
			//实现无线循环
			handler.sendMessageDelayed(Message.obtain(), 3000);
			super.handleMessage(msg);
		}
	}
	class NewsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return news.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsHolder holder = null;
			if (convertView == null) {
				holder = new NewsHolder();
				convertView = View.inflate(mContext, R.layout.item_tabdetail_listview, null);
				holder.imageIv = (ImageView) convertView.findViewById(R.id.item_pic_iv);
				holder.titleTv = (TextView) convertView.findViewById(R.id.item_title_tv);
				holder.timeTv = (TextView) convertView.findViewById(R.id.item_time_tv);
				holder.commentIv = (ImageView) convertView.findViewById(R.id.item_comment_ib);
				convertView.setTag(holder);
			}else {
				holder = (NewsHolder) convertView.getTag();
			}
			int id = news.get(position).id;
			String cacheId = SharePreferenceUtils.getString(mContext, GlobalConstant.READ_NEWS_ID);
			if (!TextUtils.isEmpty(cacheId)) {
				if (cacheId.contains((id + ""))) {
					holder.titleTv.setTextColor(Color.RED);
				}else {
					holder.titleTv.setTextColor(Color.BLACK);
				}
			}
			News newsData = news.get(position);
			bitmapUtils.display(holder.imageIv,newsData.listimage);
			holder.titleTv.setText(newsData.title);
			holder.timeTv.setText(newsData.pubdate);

			return convertView;
		}
	}
	class NewsHolder {
		public ImageView imageIv;
		public TextView titleTv;
		public TextView timeTv;
		public ImageView commentIv;
	}
	class TopNewsAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topnewsData.size();
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			bitmapUtils.display(imageView, topnewsData.get(position).topimage);
			container.addView(imageView);
			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						handler.removeCallbacksAndMessages(null);
						break;
					case MotionEvent.ACTION_UP:
						handler.sendMessageDelayed(Message.obtain(), 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						handler.sendMessageDelayed(Message.obtain(), 3000);
						break;
					}
					return true;
				}
			});
			return imageView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	@Override
	public void onPageSelected(int position) {
		mPointsLl.getChildAt(preLocation).setEnabled(false);
		mImgInfoTv.setText(topnewsData.get(position).title);
		mPointsLl.getChildAt(position).setEnabled(true);
		preLocation = position;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//获取条目信息在集合中的position
		int realPosition = position - 2;
		News info = news.get(realPosition);
		String tempId = "";
		//获取缓存的id
		String cacheIds = SharePreferenceUtils.getString(mContext, GlobalConstant.READ_NEWS_ID);
		if (TextUtils.isEmpty(cacheIds)) {
			tempId = info.id+"";
		}else if (!cacheIds.contains(info.id + "")){
			tempId = cacheIds +","+ info.id;
		}
		SharePreferenceUtils.putString(mContext, GlobalConstant.READ_NEWS_ID, tempId);
		newsAdpater.notifyDataSetChanged();
		Intent intent = new Intent(mContext,NewsDetailUI.class);
		intent.putExtra("url",info.url);
		mContext.startActivity(intent);
	}

}
