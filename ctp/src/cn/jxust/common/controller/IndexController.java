package cn.jxust.common.controller;

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
		render("index.html");
	}
//	public void list(){
//		int pageNumber = getParaToInt("pageNum", 1);
//		int pageSize = getParaToInt("numPerPage", this.pageSize);
//		Page<News> news = News.dao.paginate(pageNumber, pageSize, "select *"," from news where Status = ? order by ID desc", 1);//获取新闻列表
//		if(null != news)this.setAttr("data", news);//发送的页面新闻list
//	}

}
