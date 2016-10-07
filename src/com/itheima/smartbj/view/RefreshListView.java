package com.itheima.smartbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.smartbj.R;

public class RefreshListView extends ListView {
	private static final int PULL_REFRESH = 0;
	private static final int RELEASE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private ProgressBar mOvalPb;
	private ImageView mArrowIv;
	private TextView mStateTv;
	private TextView mTimeTv;
	private int downY;
	private int headerHei;
	private int currentState = PULL_REFRESH;
	private View header;
	private RotateAnimation up;
	private RotateAnimation down;
	private OnRefreshingListener mListener;
	private View footer;
	/*private ProgressBar mFooterOvalpb;
	private TextView mFooterStateTv;
	private TextView mFooterTimeTv;*/
	private int footerHei;
	private boolean isLoadMore;
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader();
		initFooter();
		initAnimation();
	}
	public void initFooter() {
		footer = View.inflate(getContext(), R.layout.loadmore_listview, null);
		/*mFooterOvalpb = (ProgressBar) footer.findViewById(R.id.pb_fotter_progress);
		mFooterStateTv = (TextView) footer.findViewById(R.id.tv_fotter_state);
		mFooterTimeTv = (TextView) footer.findViewById(R.id.tv_fotter_time);*/
		//隐藏脚布局
		footer.measure(0, 0);
		footerHei = footer.getMeasuredHeight();
		footer.setPadding(0, -footerHei, 0, 0);
		this.addFooterView(footer);		
		this.setOnScrollListener(new MyOnScrollListener());
	}
	class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_IDLE) {
				
				if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) {
					//显示加载更多提示控件
					footer.setPadding(0, 0, 0, 0);
					isLoadMore = true;
					//让listView切换到某一位置
					setSelection(getCount() - 1);
					//调用加载更多的业务
					if (mListener != null) {
						mListener.onLoading();
					}
				}
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	}
	public void initAnimation() {
		up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		up.setDuration(500);
		up.setFillAfter(true);
		down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		down.setDuration(500);
		down.setFillAfter(true);
	}
	private void initHeader() {
		header = View.inflate(getContext(), R.layout.refresh_listview, null);
		mOvalPb = (ProgressBar) header.findViewById(R.id.pb_header_progress);
		mArrowIv = (ImageView) header.findViewById(R.id.iv_header_arrow);
		mStateTv = (TextView) header.findViewById(R.id.tv_header_state);
		mTimeTv = (TextView) header.findViewById(R.id.tv_header_time);
		//隐藏头布局
		//获取高度
		header.measure(0, 0);
		headerHei = header.getMeasuredHeight();
		header.setPadding(0, -headerHei, 0, 0);
		this.addHeaderView(header);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (currentState == REFRESHING) {
				break;
			}
			//在move中重新赋值
			if (downY == -1) {
				downY = (int) ev.getY();
			}
			int offsetY = (int) (ev.getY() - downY);
			//只处理上到下的滑动
			if(offsetY > 0 && getFirstVisiblePosition() == 0) {
				int offsetTop = -headerHei + offsetY;
				if (offsetTop < 0 && currentState!= PULL_REFRESH) {
					currentState  = PULL_REFRESH;
					switchState(currentState);
				}else if (offsetTop > 0 && currentState != RELEASE_TO_REFRESH) {
					currentState = RELEASE_TO_REFRESH;
					switchState(currentState);
				}
				header.setPadding(0, offsetTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if (currentState == PULL_REFRESH) {
				//隐藏头布局
				header.setPadding(0, -headerHei, 0, 0);
				//switchState(currentState);
			}else if (currentState == RELEASE_TO_REFRESH) {
				currentState = REFRESHING;
				header.setPadding(0,0,0,0);
				switchState(currentState);
			}
		}
		return super.onTouchEvent(ev);
	}
	public void switchState(int state) {
		switch (state) {
		case PULL_REFRESH:
			mStateTv.setText("下拉刷新");
			mArrowIv.setVisibility(View.VISIBLE);
			mOvalPb.setVisibility(View.INVISIBLE);
			mArrowIv.startAnimation(down);
			break;
		case RELEASE_TO_REFRESH:
			mStateTv.setText("松开刷新");
			mArrowIv.setVisibility(View.VISIBLE);
			mOvalPb.setVisibility(View.INVISIBLE);
			mArrowIv.startAnimation(up);
			break;
		case REFRESHING:
			//清除动画
			mArrowIv.clearAnimation();
			mStateTv.setText("正在刷新");
			mArrowIv.setVisibility(View.INVISIBLE);
			mOvalPb.setVisibility(View.VISIBLE);
			if (mListener != null) {
				mListener.OnRefreshing();
			}
			break;
		}
	}
	//往外界实现业务,对外提供监听
	public interface OnRefreshingListener {
		public void OnRefreshing();
		public void onLoading();
	}
	//让外界设置Listener监听器
	public void setOnRefreshingListener(OnRefreshingListener listener) {
		this.mListener = listener;
	}
	public void refreshFinished(boolean success) {
		mStateTv.setText("下拉刷新");
		currentState = PULL_REFRESH;
		mArrowIv.setVisibility(View.VISIBLE);
		mOvalPb.setVisibility(View.INVISIBLE);
		header.setPadding(0, -headerHei, 0, 0);
		if(success) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = dateFormat.format(new Date());
			mTimeTv.setText("最后刷新时间:"+time);
		}
	}
	public void loadMoreFinished(){
		isLoadMore = false;
		footer.setPadding(0, -footerHei, 0, 0);
	}

}
