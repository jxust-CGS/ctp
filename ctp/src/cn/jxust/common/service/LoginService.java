package cn.jxust.common.service;

import cn.jxust.common.model.User;
import cn.jxust.utils.MD5;

public class LoginService
{
	public User userLogin(String loginName,String loginPwd,String loginType)
	{
		loginPwd=MD5.getMD5Str2(loginPwd);
		User user=User.dao.findFirst("select * from user where name=?", loginName);
		if(user==null)
		{
			return null;
		}
		else
		{
			user= User.dao.findFirst("select * from user where name=? and password=?",loginName,loginPwd);
			if(null==user)
			{
				user=new User();
				user.set("name", loginName);
				return user;
			}
			else 
			{
				user=User.dao.findFirst("select * from user where name=? and password=? and role=?",loginName,loginPwd,loginType);
				if(null==user)
				{
					user=new User();
					user.set("name", loginName);
					user.set("password", loginPwd);
					return user;
				}
				else
				{
					return user;
				}
			}
		}
	}

}
