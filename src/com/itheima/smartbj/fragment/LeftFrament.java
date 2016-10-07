package com.itheima.smartbj.fragment;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.smartbj.R;
import com.itheima.smartbj.activity.MainUi;
import com.itheima.smartbj.base.BaseFragment;
import com.itheima.smartbj.bean.NewscenterBean.MenuItem;

public class LeftFrament extends BaseFragment implements OnItemClickListener{
	private List<MenuItem> mData;
	private MyAdapter adapter;
	private ListView listView;
	private int currentClickItem;
	@Override
	protected View initView() {
		listView = new ListView(activity);
		listView.setSelector(android.R.color.transparent);
		listView.setPadding(0, 40, 0, 0);
		//去掉分割线
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(this);
		return listView;
	}
	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mData.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) View.inflate(activity, R.layout.item_left_menu, null);
			view.setText(mData.get(position).title);
			view.setEnabled(currentClickItem == position);
			return view;
		}

	}
	public void setData(List<MenuItem> data) {
		this.mData = data;
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.currentClickItem = position;
		adapter.notifyDataSetChanged();
		// 让侧滑菜单自动弹回来
		MainUi mainUi = (MainUi) activity;
		mainUi.getSlidingMenu().toggle();
		//获取NewsCenterPager对象
		mainUi.getContentFragment().getNewsenterPager().switchMenuPager(position);
	}
}
