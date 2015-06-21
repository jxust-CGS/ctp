package cn.jxust.paper.controller;

import cn.jxust.common.controller.BaseController;
import cn.jxust.common.model.User;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.service.ErrorService;
import cn.jxust.paper.service.ProblemService;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

@ControllerBind(controllerKey = "/problem")
public class ProblemController extends BaseController
{
	ProblemService problemService=new ProblemService();
	ErrorService errorService=new ErrorService();
	
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
		this.setAttr("problem", problem);
		if("select".equals(problem.get("type")))
		{
			problem_select=problemService.getProblem_selectByPro_id(problem.getInt("id"));
			this.setAttr("problem_select", problem_select);
			render("test/select.html");
		}
		else if("judge".equals(problem.get("type")))
		{
			problem_judge=problemService.getProblem_judgeByPro_id(problem.getInt("id"));
			this.setAttr("problem_judge", problem_judge);
			render("test/judge.html");
		}
	}
	
	@Before(Tx.class)
	public void check_select_answer()
	{
		String id=getPara("id");
		Problem_select problem_select=problemService.getProblem_selectById(id);
		this.setAttr("problem_select", problem_select);
		String answer=getPara("answer");
		if(answer.equals(problem_select.get("answer")))
		{
			this.setAttr("sign", "1");
			this.setAttr("massage", "恭喜回答正确！");
			problemService.updateCorrect(problem_select.getInt("pro_id"),true);
			render("test/select.html");
		}
		else
		{
			this.setAttr("sign", "0");
			this.setAttr("massage", "很遗憾，回答错误！答案不是'"+answer+"'哟");
			problemService.updateCorrect(problem_select.getInt("pro_id"),false);
			errorService.add(problem_select.getInt("pro_id"),(User)getSessionAttr("user"));
			render("test/select.html");
		}
	}
	
	@Before(Tx.class)
	public void check_judge_answer()
	{
		String id=getPara("id");
		Problem_judge problem_judge=problemService.getProblem_judgeById(id);
		this.setAttr("problem_judge", problem_judge);
		String answer=getPara("answer");
		if(answer.equals(problem_judge.get("answer")))
		{
			this.setAttr("sign", "1");
			this.setAttr("massage", "恭喜回答正确！");
			problemService.updateCorrect(problem_judge.getInt("pro_id"),true);
			render("test/judge.html");
		}
		else
		{
			this.setAttr("sign", "0");
			if(answer.equals("1"))
			{
				answer="正确";
			}
			else
			{
				answer="错误";
			}
			this.setAttr("massage", "很遗憾，回答错误！答案不是'"+answer+"'哟");
			problemService.updateCorrect(problem_judge.getInt("pro_id"),false);
			errorService.add(problem_judge.getInt("pro_id"),(User)getSessionAttr("user"));
			render("test/judge.html");
		}
	}
	
}
