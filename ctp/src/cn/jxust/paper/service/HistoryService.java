package cn.jxust.paper.service;

import java.util.Date;

import com.jfinal.plugin.activerecord.Page;

import cn.jxust.common.model.User;
import cn.jxust.paper.model.User_result;


public class HistoryService
{

	public void add(Integer pro_id, User user)
	{
		User_result user_result=new User_result();
		user_result.set("problem", pro_id);
		user_result.set("user",user.get("id"));
		user_result.set("createtime", new Date());
		user_result.save();
	}

	public void delete(Integer id)
	{
		User_result.dao.deleteById(id);
	}

	public Page<User_result> getPage(int pageNumber, int pageSize, User user)
	{
		return User_result.dao.paginate(pageNumber, pageSize, "select user_result.*,paper.name","from user_result,paper where user_result.paper=paper.id and user_result.user=? order by user_result.createtime desc",user.get("id"));
	}

}
