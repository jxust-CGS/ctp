package cn.jxust.common.controller;

import java.util.HashMap;
import java.util.Map;

import cn.jxust.common.model.User;
import cn.jxust.core.Constant;

import com.jfinal.core.Controller;

public abstract class BaseController extends Controller
{
	protected int pageSize = Constant.pageSize;

	public abstract void index();

	@Override
	public void render(String view)
	{
		this.setAttr("ctx", getRequest().getContextPath());
		super.render(view);
	}
	
	/*获得教师,学生id*/
	public int getCurrentID()
	{
		int n = 0;
		User user=getSessionAttr("user");
		n=user.get("id");
		return n;
	}


	/**
	 * dwz上传文件Json
	 * @param id
	 * @param fileName
	 * @param attachmentPath
	 * @param attachmentSize
	 */
	public void toUploadJson(String id, String fileName, String attachmentPath, String attachmentSize)
	{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("id", id);
		jsonMap.put("fileName", fileName);
		jsonMap.put("attachmentPath", attachmentPath);
		jsonMap.put("attachmentSize", attachmentSize);
		renderJson(jsonMap);
	}
}
