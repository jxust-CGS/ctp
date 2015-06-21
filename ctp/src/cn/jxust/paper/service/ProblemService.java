package cn.jxust.paper.service;

import java.util.List;

import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.Problem_judge;
import cn.jxust.paper.model.Problem_select;
import cn.jxust.paper.model.Subject;

import com.jfinal.plugin.activerecord.Page;

public class ProblemService
{
	public String getTypeList_json()
	{
		String json="[";
        List<Subject> p_subjects=getList_subject(null);
		for(Subject p_subject:p_subjects)
		{
			json+="{";
			json+="'text': '"+p_subject.getStr("name")+"',"
					+ "'href':'problem?subject="+p_subject.getStr("name")+"',"
					+ "'tags':['"+p_subject.getLong("haveChild")+"']";
			if(0!=p_subject.getLong("haveChild"))
			{
				json+=",'nodes':[";
				json=getChildNode(json, p_subject.getInt("id"));
				json+="]";
			}
			json+="},";
		}
		json=json.substring(0,json.length()-1);
		json+="]";
		return json;
	}

	public Page<Problem> getPage_Problem(int pageNumber, int pageSize)
	{
		Page<Problem> k=null;
		k=Problem.dao.paginate(pageNumber, pageSize, "select *",
				"from problem where sign=1 order by createtime desc");
		return k;
	}

	public List<Subject> getList_subject(Integer pid)
	{
		if(null==pid)
		{
			return Subject.dao.find("select * from subject where pid is null order by id asc");
		}
		return Subject.dao.find("select * from subject where pid=? order by id asc",pid);
	}
	
	public String getChildNode(String json,Integer pid)
	{
		List<Subject> p_subjects=getList_subject(pid);
		for(Subject p_subject:p_subjects)
		{
			json+="{";
			json+="'text': '"+p_subject.getStr("name")+"',"
					+ "'href':'problem?subject="+p_subject.getStr("name")+"',"
					+ "'tags':['"+p_subject.getLong("haveChild")+"']";
			if(0!=p_subject.getLong("haveChild"))
			{
				json+=",'nodes':[";
				json=getChildNode(json, p_subject.getInt("id"));
				json+="]";
			}
			json+="},";
		}
		json=json.substring(0,json.length()-1);
		return json;
	}

	public Problem getProblemById(int id)
	{
		return Problem.dao.findById(id);
	}

	public Problem_select getProblem_selectByPro_id(Integer pro_id)
	{
		return Problem_select.dao.findFirst("select * from problem_select where pro_id=?",pro_id);
	}
	
	public Problem_judge getProblem_judgeByPro_id(Integer pro_id)
	{
		return Problem_judge.dao.findFirst("select * from problem_judge where pro_id=?",pro_id);
	}

	public Problem_select getProblem_selectById(String id)
	{
		return Problem_select.dao.findById(id);
	}

	public Problem_judge getProblem_judgeById(String id)
	{
		return Problem_judge.dao.findById(id);
	}

	public void updateCorrect(Integer id, Boolean sign)
	{
		Problem problem=getProblemById(id);
		if(sign)
		{
			problem.set("do_right_num", problem.getLong("do_right_num")+1);
			
		}
		else
		{
			problem.set("do_error_num", problem.getLong("do_error_num")+1);
		}
		problem.update();
	}
}
