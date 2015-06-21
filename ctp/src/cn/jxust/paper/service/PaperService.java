package cn.jxust.paper.service;

import java.util.List;

import cn.jxust.common.model.User;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.model.Paper_problem;
import cn.jxust.paper.model.Problem;
import cn.jxust.paper.model.User_answer;
import cn.jxust.paper.model.User_result;

import com.jfinal.plugin.activerecord.Page;

public class PaperService
{

	public String getTypeList_json()
	{
		String json="[" +
          "{" +
            "'text': 'Parent 9999999'," +
            "'nodes': [" +
              "{" +
                "'text': 'Child 1'," +
                "'nodes': [" +
                  "{" +
                    "'text': 'Grandchild 1'" +
                  "}," +
                  "{" +
                    "'text': 'Grandchild 2'" +
                  "}" +
                "]" +
              "}," +
              "{" +
                "'text': 'Child 2'" +
              "}" +
            "]" +
          "}," +
          "{" +
            "'text': 'Parent 2'" +
          "}," +
          "{" +
            "'text': 'Parent 3'" +
          "}," +
          "{" +
            "'text': 'Parent 4'" +
          "}," +
          "{" +
            "'text': 'Parent 5'" +
          "}" +
        "]";
		return json;
	}

	public Page<Paper> findPage_paper(int pageNumber, int pageSize)
	{
		return Paper.dao.paginate(pageNumber, pageSize, "select *",
			"from paper order by createtime desc");
	}

	public Paper getPaperById(int id)
	{
		return Paper.dao.findById(id);
	}

	public List<Paper_problem> findPaper_and_answer(int paperId,User user)
	{
		return Paper_problem.dao.find("select paper_problem.*,user_answer.answer from user_answer RIGHT JOIN paper_problem "
				+ "ON paper_problem.paper_id = user_answer.paper "
				+ "AND paper_problem.problem_id = user_answer.problem "
				+ "AND user_answer.user=? "
				+ "where paper_problem.paper_id=? "
				+ "order by paper_problem.id asc",user.get("id"),paperId);
	}

	public User_answer getUser_answer(User user, Integer paper_id, Problem problem)
	{
		User_answer user_answer=User_answer.dao.findFirst("select * from user_answer where user=? and paper=? and problem=?",user.get("id"),paper_id,problem.get("id"));
		if(null==user_answer)
		{
			user_answer = new User_answer();
		}
		return user_answer;
	}

	public List<User_answer> getList_user_answer(User user, String paper_id)
	{
		return User_answer.dao.find("select user_answer.*,paper_problem.value from user_answer,paper_problem where "
				+ "paper_problem.paper_id=user_answer.paper and "
				+ "paper_problem.problem_id=user_answer.problem and "
				+ "user_answer.user=? and "
				+ "user_answer.paper=? ",user.get("id"),paper_id);
	}

	public User_result findUser_result(User user, int paperId)
	{
		return User_result.dao.findFirst("select * from user_result where user=? and paper=?",user.get("id"),paperId);
	}
}
