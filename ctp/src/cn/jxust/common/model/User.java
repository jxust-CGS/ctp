package cn.jxust.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="user")
public class User extends Model<User>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final User dao=new User();
}
