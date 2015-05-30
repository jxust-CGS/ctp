package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.service.PaperService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/paper")
public class PaperController extends BaseController
{
	PaperService paperService=new PaperService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		Page<Paper> page = paperService.findPage_paper(pageNumber,pageSize);
		this.setAttr("page", page);
		render("list.html");
	}

}
