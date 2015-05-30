package cn.jxust.paper.controller;

import com.jfinal.ext.route.ControllerBind;

import cn.jxust.common.controller.BaseController;

@ControllerBind(controllerKey = "/error")
public class ErrorController extends BaseController
{

	@Override
	public void index()
	{
		render("list.html");
	}

}
