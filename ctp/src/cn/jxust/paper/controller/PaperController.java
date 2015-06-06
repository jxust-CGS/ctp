package cn.jxust.paper.controller;

import java.util.Calendar;
import java.util.Date;

import cn.jxust.common.controller.BaseController;
import cn.jxust.paper.model.Paper;
import cn.jxust.paper.service.PaperService;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;

@ControllerBind(controllerKey = "/paper")
public class PaperController extends BaseController
{
	PaperService paperService=new PaperService();
	
	@Override
	public void index()
	{
		int pageNumber=getParaToInt("pageNum",1);
		int pageSize = getParaToInt("numPerPage", this.pageSize);
		Page<Paper> page = paperService.findPage_paper(pageNumber,pageSize);
		this.setAttr("page", page);
		render("list.html");
	}

	public void join()
	{
		int paperId=getParaToInt();
		Paper paper=paperService.getPaperById(paperId);
		Date now=new Date();
		Date start_time=paper.getTimestamp("test_start_time");
		Date end_time=paper.getTimestamp("test_end_time");
		if(start_time.before(now)&&end_time.after(now))
		{
			this.setAttr("paper", paper);
			this.setAttr("end_time", end_time);
			this.setAttr("now", now);
			render("join.html");
		}
		else
		{
			Long countTo=this.getCountTo(now,start_time);
			this.setAttr("countTo", countTo);
			render("wait.html");
		}
	}

	private Long getCountTo(Date now_date, Date start_time_date)
	{
		Calendar now_calender =Calendar.getInstance();
		now_calender.setTime(now_date);
		Calendar start_time_calender = Calendar.getInstance();
		start_time_calender.setTime(start_time_date);
		return start_time_calender.getTimeInMillis()-now_calender.getTimeInMillis();
	}
}
