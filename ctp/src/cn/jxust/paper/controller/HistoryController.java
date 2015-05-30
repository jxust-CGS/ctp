package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;

import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/history")
public class HistoryController extends BaseController
{

	@Override
	public void index()
	{
		render("list.html");
	}

}
