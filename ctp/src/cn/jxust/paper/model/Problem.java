package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="problem")
public class Problem extends Model<Problem>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Problem dao=new Problem();
}
