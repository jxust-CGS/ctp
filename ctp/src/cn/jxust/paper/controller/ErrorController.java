package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;
import cn.jxust.common.model.User;
import cn.jxust.paper.model.User_do_error;
import cn.jxust.paper.service.ErrorService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/error")
public class ErrorController extends BaseController
{
	ErrorService errorService=new ErrorService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		User user=getSessionAttr("user");
		Page<User_do_error> page=errorService.getPage(pageNumber,pageSize,user);
		this.setAttr("page", page);
		render("list.html");
	}

	public void delete()
	{
		int id= getParaToInt("id");
		errorService.delete(id);
		redirect("/error");
	}
}
