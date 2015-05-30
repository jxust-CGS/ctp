package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="problem_judge")
public class Problem_judge extends Model<Problem_judge>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Problem_judge dao=new Problem_judge();
}
