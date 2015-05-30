package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="paper")
public class Paper extends Model<Paper>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Paper dao=new Paper();
}
