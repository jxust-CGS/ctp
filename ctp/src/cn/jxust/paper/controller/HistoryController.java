package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;
import cn.jxust.common.model.User;
import cn.jxust.paper.model.User_result;
import cn.jxust.paper.service.HistoryService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/history")
public class HistoryController extends BaseController
{
	HistoryService historyService=new HistoryService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		User user=getSessionAttr("user");
		Page<User_result> page=historyService.getPage(pageNumber,pageSize,user);
		this.setAttr("page", page);
		render("list.html");
	}
	

}
