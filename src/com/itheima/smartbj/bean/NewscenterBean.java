package com.itheima.smartbj.bean;

import java.util.List;

public class NewscenterBean {
	public int retcode;
	public List<Integer> extend;
	public List<MenuItem> data;
	public class MenuItem {
		public int id;
		public String title;
		public String type;
		public String url;
		public String utl1;
		public String dayurl;
		public String weekurl;
		public String excurl;
		public List<NewsTab> children;
	}
	public class NewsTab {
		public int id;
		public String title;
		public int type;
		public String url;
	}
}
