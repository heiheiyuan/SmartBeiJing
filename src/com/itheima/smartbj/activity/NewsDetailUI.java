package com.itheima.smartbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.itheima.smartbj.R;

public class NewsDetailUI extends Activity implements android.view.View.OnClickListener{
	private TextView mTitleTv;
	private ImageButton mMenuIb;
	private ImageButton mBackIb;
	private ImageButton mShareIb;
	private ImageButton mTextSizeIb;
	private WebView mTextWb;
	private ProgressBar mCachePb;
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail_ui);
		initView();
	}
	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.tv_basepager_title);
		mMenuIb = (ImageButton) findViewById(R.id.ib_basepager_menu);
		mBackIb = (ImageButton) findViewById(R.id.ib_titlebar_back);
		mShareIb = (ImageButton) findViewById(R.id.ib_titlebar_share);
		mTextSizeIb = (ImageButton) findViewById(R.id.ib_titlebar_textsize);
		mTextWb = (WebView) findViewById(R.id.newsdetail_text_wv);
		mCachePb = (ProgressBar) findViewById(R.id.newsdetail_cache_pb);
		mTitleTv.setVisibility(View.GONE);
		mMenuIb.setVisibility(View.GONE);
		mBackIb.setVisibility(View.VISIBLE);
		mShareIb.setVisibility(View.VISIBLE);
		mTextSizeIb.setVisibility(View.VISIBLE);
		mTextWb.setVisibility(View.VISIBLE);
		mCachePb.setVisibility(View.VISIBLE);
		settings = mTextWb.getSettings();
		settings.setUseWideViewPort(true);//启用双击效果
		settings.setBuiltInZoomControls(true);//启用缩放功能
		settings.setJavaScriptEnabled(true);//启用js功能
		String url = getIntent().getStringExtra("url");
		mTextWb.loadUrl(url);
		//监听webview
		mTextWb.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				mCachePb.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		mBackIb.setOnClickListener(this);
		mShareIb.setOnClickListener(this);
		mTextSizeIb.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == mBackIb) {
			finish();
		}else if (v == mShareIb) {
			showShare();
		}else if (v == mTextSizeIb) {
			showTextSize();
		}

	}
	private int currentIndex = 2;
	private int tempIndex;
	private void showTextSize() {
		AlertDialog.Builder builder = new Builder(this);
		String [] items = new String[] {"超大号","大号","正常","小号","超小号"};
		builder.setSingleChoiceItems(items, currentIndex, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tempIndex = which;
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentIndex = tempIndex;
				changeTextSize(currentIndex);
			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	private void changeTextSize(int currentIndex) {
		switch (currentIndex) {
		case 0:
			settings.setTextZoom(500);
			break;
		case 1:
			settings.setTextZoom(300);
			break;
		case 2:
			settings.setTextZoom(100);
			break;
		case 3:
			settings.setTextZoom(60);
			break;
		case 4:
			settings.setTextZoom(40);
			break;
		}
	}
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("今天代码敲完了么");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/test.JPG");//确保SDcard下面存在此张图片
		// 启动分享GUI
		 oks.show(this);
		 }
}
