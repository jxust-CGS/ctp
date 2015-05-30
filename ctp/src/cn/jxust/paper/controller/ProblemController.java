package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.service.ProblemService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/problem")
public class ProblemController extends BaseController
{
	ProblemService problemService=new ProblemService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		Page<Problem> page=problemService.getPage_Problem(pageNumber,pageSize);
		this.setAttr("json", problemService.getTypeList_json());
		this.setAttr("page", page);
		render("list.html");
	}
	
	public void test()
	{
		int id=getParaToInt();
		Problem problem =null;
		Problem_select problem_select =null;
		Problem_judge problem_judge =null;
		problem = problemService.getProblemById(id);
		if("select".equals(problem.get("type")))
		{
			problem_select=problemService.getProblem_selectByPro_id(problem.getInt("id"));
		}
		else if("judge".equals(problem.get("type")))
		{
			problem_judge=problemService.getProblem_judgeByPro_id(problem.getInt("id"));
		}
		this.setAttr("problem", problem);
		this.setAttr("problem_select", problem_select);
		this.setAttr("problem_judge", problem_judge);
		render("test.html");
	}

}
