package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="user_answer")
public class User_answer extends Model<User_answer>{
	/**
	 * 用户答案表
	 */
	private static final long serialVersionUID = 1L;
	public static final User_answer dao=new User_answer();
}

