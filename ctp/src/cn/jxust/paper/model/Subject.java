package cn.jxust.paper.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="subject")
public class Subject extends Model<Subject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Subject dao=new Subject();
}

