package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="problem_select")
public class Problem_select extends Model<Problem_select>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Problem_select dao=new Problem_select();
}
