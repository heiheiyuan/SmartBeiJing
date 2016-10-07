package com.itheima.smartbj.bean;
import java.util.List;
public class PhotoBean  {

	public int retcode  ;
	public  Data data;
	public class Data  {

		public String title  ;
		public String more  ;
		public String countcommenturl  ;public List<Topic> topic;
		public class Topic {

		}
		public List<News> news;
		public class News  {

			public String smallimage  ;
			public int id  ;
			public String title  ;
			public String commenturl  ;
			public String commentlist  ;
			public String pubdate  ;
			public String largeimage  ;
			public String type  ;
			public Boolean comment  ;
			public String url  ;
			public String listimage  ;
		}

	}
}