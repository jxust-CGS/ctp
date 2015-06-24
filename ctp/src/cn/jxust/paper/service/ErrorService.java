package cn.jxust.paper.service;

import java.util.Date;

import com.jfinal.plugin.activerecord.Page;

import cn.jxust.common.model.User;
import cn.jxust.paper.model.User_do_error;


public class ErrorService
{

	public void add(Integer pro_id, User user)
	{
		User_do_error user_do_error=new User_do_error();
		user_do_error.set("problem", pro_id);
		user_do_error.set("user",user.get("id"));
		user_do_error.set("createtime", new Date());
		user_do_error.save();
	}

	public void delete(Integer id)
	{
		User_do_error.dao.deleteById(id);
	}

	public Page<User_do_error> getPage(int pageNumber, int pageSize, User user)
	{
		return User_do_error.dao.paginate(pageNumber, pageSize, "select user_do_error.*,problem.type,problem.keyword,problem.subject","from user_do_error,problem where user_do_error.problem=problem.id and user_do_error.user=? order by user_do_error.createtime desc",user.get("id"));
	}

}
