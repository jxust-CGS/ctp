package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="user_result")
public class User_result extends Model<User_result>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final User_result dao=new User_result();
}
