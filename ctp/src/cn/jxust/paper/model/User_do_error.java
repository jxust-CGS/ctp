package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="user_do_error")
public class User_do_error extends Model<User_do_error>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final User_do_error dao=new User_do_error();
}
