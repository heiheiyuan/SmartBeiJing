package com.itheima.smartbj.base.menu;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.BaseMenuPager;
import com.itheima.smartbj.bean.PhotoBean;
import com.itheima.smartbj.bean.PhotoBean.Data.News;
import com.itheima.smartbj.global.GlobalConstant;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsImagePager extends BaseMenuPager {

	private GridView mMakePicGd;
	private ListView mMakePicLv;
	private List<News> newsData;
	private BitmapUtils bitmapUtils;
	private boolean isListType = true;
	private MyAdapter adapter;

	public NewsImagePager(Context context) {
		super(context);
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.make_image, null);
		mMakePicGd = (GridView) view.findViewById(R.id.make_image_gv);
		mMakePicLv = (ListView) view.findViewById(R.id.make_image_lv);
		return view;
	}
	@Override
	public void initData() {
		getDataFromServer();
	}

	private void getDataFromServer() {
		//请求网络
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstant.PICTURE_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responInfo) {
				parseJson(responInfo.result);
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(mContext, "请求网络失败了...",Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void parseJson(String result) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		adapter = new MyAdapter();
		newsData = photoBean.data.news;
		mMakePicLv.setAdapter(adapter);
	}
	class MyAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsData.size();
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_make_image, null);
				holder.picIv = (ImageView) convertView.findViewById(R.id.item_pic_iv);
				holder.desTv = (TextView) convertView.findViewById(R.id.item_des_tv);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			//绑定数据
			bitmapUtils = new BitmapUtils(mContext); 
			holder.picIv.setScaleType(ScaleType.CENTER_CROP);
			News news = newsData.get(position);
			bitmapUtils.display(holder.picIv, news.listimage);
			holder.desTv.setText(news.title);
			return convertView;
		}
		class ViewHolder {
			public ImageView picIv;
			public TextView desTv;
		}

	}
	public void switchType(ImageButton type) {
		if (isListType ) {
			//切换背景
			type.setBackgroundResource(R.drawable.icon_pic_list_type);
			//切换视图
			mMakePicGd.setVisibility(View.VISIBLE);
			mMakePicLv.setVisibility(View.GONE);
			//加载页面
			mMakePicGd.setAdapter(adapter);
		}else {
			//切换背景
			type.setBackgroundResource(R.drawable.icon_pic_grid_type);
			//切换视图
			mMakePicGd.setVisibility(View.GONE);
			mMakePicLv.setVisibility(View.VISIBLE);
			//加载页面
			mMakePicLv.setAdapter(adapter);
		}
		isListType = !isListType;
	}
}
