package cn.jxust.paper.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.jxust.common.controller.BaseController;
import cn.jxust.common.model.User;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.model.Paper_problem;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.model.User_answer;
import cn.jxust.paper.model.User_result;
import cn.jxust.paper.service.PaperService;
import cn.jxust.paper.service.ProblemService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/paper")
public class PaperController extends BaseController
{
	PaperService paperService=new PaperService();
	ProblemService problemService=new ProblemService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		Page<Paper> page = paperService.findPage_paper(pageNumber,pageSize);
		this.setAttr("page", page);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		this.setAttr("now",sdf.format(new Date()));
		render("list.html");
	}

	/**
	 * 进入考试
	 */
	public void join()
	{
		int paperId=getParaToInt();
		int problem_id = getParaToInt("problem_id",0);
		Paper paper=paperService.getPaperById(paperId);
		User user= getSessionAttr("user");
		Date now=new Date();
		Date start_time=paper.getTimestamp("test_start_time");
		Date end_time=paper.getTimestamp("test_end_time");
		if(start_time.before(now)&&end_time.after(now))
		{
			this.setAttr("paper", paper);
			User_result user_result=paperService.findUser_result(user,paperId);
			if(null!=user_result)
			{
				this.setAttr("sign", "您已经提交了试卷，不能重复答题，请返回首页");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.setAttr("now", sdf.format(now).toString());
			List<Paper_problem> paper_problem=paperService.findPaper_and_answer(paperId,user);
			this.setAttr("paper_problem",paper_problem);
			if(paper_problem.size()!=0 && 0==problem_id)
			{
				problem_id=paper_problem.get(0).get("problem_id");
			}
			//获取题目
			Problem problem =null;
			Problem_select problem_select =null;
			Problem_judge problem_judge =null;
			if(problem_id!=0)
			{
				problem = problemService.getProblemById(problem_id);
				this.setAttr("problem", problem);
				if("select".equals(problem.get("type")))
				{
					problem_select=problemService.getProblem_selectByPro_id(problem.getInt("id"));
					this.setAttr("problem_select", problem_select);
				}
				else if("judge".equals(problem.get("type")))
				{
					problem_judge=problemService.getProblem_judgeByPro_id(problem.getInt("id"));
					this.setAttr("problem_judge", problem_judge);
				}
			}
			render("join.html");
		}
		else
		{
			Long countTo=this.getCountTo(now,start_time);
			this.setAttr("countTo", countTo);
			render("wait.html");
		}
	}

	/**
	 * 获取考试开始时间差
	 * @param now_date
	 * @param start_time_date
	 * @return
	 */
	private Long getCountTo(Date now_date, Date start_time_date)
	{
		Calendar now_calender =Calendar.getInstance();
		now_calender.setTime(now_date);
		Calendar start_time_calender = Calendar.getInstance();
		start_time_calender.setTime(start_time_date);
		return start_time_calender.getTimeInMillis()-now_calender.getTimeInMillis();
	}
	
	public void check_judge_answer()
	{
		Integer paper_id=getParaToInt();
		String judge_id=getPara("id");
		String answer=getPara("answer");
		Problem_judge problem_judge=problemService.getProblem_judgeById(judge_id);
		Problem problem=problemService.getProblemById(problem_judge.getInt("pro_id"));
		User user=getSessionAttr("user");
		User_answer user_answer=paperService.getUser_answer(user,paper_id,problem);
		//保存用户的答案
		if(null==user_answer.get("id"))
		{
			user_answer.set("user", user.get("id"));
			user_answer.set("paper", paper_id);
			user_answer.set("problem", problem.get("id"));
		}
		user_answer.set("answer", answer);
		user_answer.set("type", "judge");
		if(problem_judge.get("answer").equals(answer))
		{
			user_answer.set("is_right", 1);
		}
		else
		{
			user_answer.set("is_right", 0);
		}
		if(null==user_answer.get("id"))
		{
			user_answer.save();
		}
		else
		{
			user_answer.update();
		}
		
		//进入下一题
		List<Paper_problem> paper_problems=paperService.findPaper_and_answer(paper_id,user);
		int i;
		for(i=0;i<paper_problems.size();i++)
		{
			if(paper_problems.get(i).get("problem_id").equals(problem.get("id")))
			break;
		}
		if(i!=paper_problems.size()-1)
		{
			redirect("/paper/join/"+paper_id+"?problem_id="+paper_problems.get(i+1).get("problem_id"));
		}
		else
		{
			redirect("/paper/join/"+paper_id+"?problem_id="+paper_problems.get(0).get("problem_id"));
		}
	}
	
	public void check_select_answer()
	{
		Integer paper_id=getParaToInt();
		String select_id=getPara("id");
		String answer=getPara("answer");
		Problem_select problem_select=problemService.getProblem_selectById(select_id);
		Problem problem=problemService.getProblemById(problem_select.getInt("pro_id"));
		User user=getSessionAttr("user");
		User_answer user_answer=paperService.getUser_answer(user,paper_id,problem);
		if(null==user_answer.get("id"))
		{
			user_answer.set("user", user.get("id"));
			user_answer.set("paper", paper_id);
			user_answer.set("problem", problem.get("id"));
		}
		user_answer.set("answer", answer);
		user_answer.set("type", "select");
		if(problem_select.get("answer").equals(answer))
		{
			user_answer.set("is_right", 1);
		}
		else
		{
			user_answer.set("is_right", 0);
		}
		if(null==user_answer.get("id"))
		{
			user_answer.save();
		}
		else
		{
			user_answer.update();
		}
		List<Paper_problem> paper_problems=paperService.findPaper_and_answer(paper_id,user);
		int i;
		for(i=0;i<paper_problems.size();i++)
		{
			if(paper_problems.get(i).get("problem_id").equals(problem.get("id")))
			break;
		}
		if(i!=paper_problems.size()-1)
		{
			redirect("/paper/join/"+paper_id+"?problem_id="+paper_problems.get(i+1).get("problem_id"));
		}
		else
		{
			redirect("/paper/join/"+paper_id+"?problem_id="+paper_problems.get(0).get("problem_id"));
		}
	}
	 
	public void submit_result()
	{
		String paper_id = getPara();
		User user=getSessionAttr("user");
		List<User_answer> user_answers=paperService.getList_user_answer(user,paper_id);
		int result=0;
		for(User_answer user_answer:user_answers)
		{
			result+=Integer.valueOf(user_answer.get("is_right").toString())*user_answer.getInt("value");
		}
		User_result user_result=new User_result();
		user_result.set("user", user.get("id"));
		user_result.set("paper", paper_id);
		user_result.set("result", result);
		user_result.set("createtime", new Date());
		user_result.save(); 
		redirect("/paper");
	}
}
