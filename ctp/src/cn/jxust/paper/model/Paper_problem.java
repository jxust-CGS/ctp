package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="paper_problem")
public class Paper_problem extends Model<Paper_problem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Paper_problem dao=new Paper_problem();
}
