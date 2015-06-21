package cn.jxust.common.controller;

import java.util.List;

import cn.jxust.common.model.News;
import cn.jxust.common.model.User;

import com.jfinal.ext.route.ControllerBind;
@ControllerBind(controllerKey = "/")
public class IndexController extends BaseController
{
	@Override
	public void index()
	{	
		User user=getSessionAttr("user");
		this.setAttr("user", user);
		list();
		render("index.html");
	}
	public void list(){
		List<News> news = News.dao.find("select * from news where flag = ? order by createtime desc", 1);//获取新闻列表
		if(null != news)this.setAttr("data", news);//发送的页面新闻list
	}

	public void news_details()
	{
		String id=getPara("id");
		News news=News.dao.findById(id);
		this.setAttr("news", news);
		render("news_details.html");
	}
}
