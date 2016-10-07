package com.itheima.smartbj.bean;
import java.util.List;
public class TabDetailsBean  {

	public int retcode;
	public  Data data;
	public class Data  {

		public String title;
		public String more;
		public String countcommenturl;
		public List<Topic> topic;
		public class Topic {

			public int id;
			public String title;
			public int sort;
			public String description;
			public String url;
			public String listimage;
		}
		public List<News> news;
		public class News  {

			public int id;
			public String title;
			public String commenturl;
			public String commentlist;
			public String pubdate;
			public String type;
			public Boolean comment;
			public String url;
			public String listimage;
		}
		public List<Topnews> topnews;
		public class Topnews  {

			public int id;
			public String title;
			public String topimage;
			public String commenturl;
			public String commentlist;
			public String pubdate;
			public String type;
			public Boolean comment;
			public String url;
		}

	}
}