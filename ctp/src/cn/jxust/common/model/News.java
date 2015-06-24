package cn.jxust.common.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="news")
public class News extends Model<News>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final News dao=new News();
}
