package cn.jxust.common.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.jxust.common.model.User;
import cn.jxust.common.service.LoginService;
import cn.jxust.utils.MD5;

import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/login")
public class loginController extends BaseController {
	private LoginService loginService = new LoginService();
	/**
	 * 输入登录信息后，验证登陆
	 * 登陆成功则跳转回index.html
	 */
	public void login() { 
		String loginName = this.getPara("user.id");
		String loginPwd = this.getPara("user.password");
		String loginType = this.getPara("type");
		String code = this.getPara("code").toLowerCase();
		String check = this.getSessionAttr("check");
		this.removeSessionAttr("check");
		if (!check.equals(code)) {//判断用户输入的验证码是否正确
			setAttr("sign", "验证码输入错误！");//发送警告框的文本
			render("login.html");
		}
		else 
		{
			User user = loginService.userLogin(loginName,loginPwd,loginType);
			if (null == user)
			{
				setAttr("sign", "用户名不存在！");
				render("login.html");
			} 
			else
			{
				if (null == user.get("password"))
				{
					setAttr("sign", "密码输入错误！");
					render("login.html");
				} 
				else
				{
					if (null == user.get("role"))
					{//判断教师是否为教研室主任								
						setAttr("sign", "身份输入错误！");
						render("login.html");
					}
					else
					{
						if("0".equals(user.get("flag")))
						{
							setAttr("sign", "账号未激活！");
							render("login.html");
						}
						else
						{
							this.setSessionAttr("user", user);
							this.redirect("/index");
						}
					}
				}
			}
		}		
	}
	
	
	/**
	 * 注销登陆，清除Session中的登陆用户的信息
	 */
	public void logout() {
		this.removeSessionAttr("user");
		this.redirect("/index");
	}
	
	/**
	 * 打开用户注册页面
	 */
	public void register()
	{
		this.render("register.html");
	}
	
	public void check_userName()
	{
		String yourName=getPara();
		User user=User.dao.findFirst("select * from user where name=?",yourName);
		if(null==user)
		{
			setAttr("user", 1);
		}
		else
		{
			setAttr("user", 0);
		}
		renderJson();
	}
	
	/**
	 * 保存用户注册信息
	 */
	public void save_register()
	{
		String password_check=this.getPara("password_check");
		User user=this.getModel(User.class);
		String code = this.getPara("code").toLowerCase();
		String check = this.getSessionAttr("check");
		this.removeSessionAttr("check");
		if (!check.equals(code)) {//判断用户输入的验证码是否正确
			setAttr("sign", "验证码输入错误！");//发送警告框的文本
			render("register.html");
		}
		else{
			if(!password_check.equals(user.get("password")))
			{
				setAttr("sign", "前后密码输入错误");
				this.render("register.html");
			}
			else
			{
				user.set("password", MD5.getMD5Str2((String)user.get("password")));//对用户密码加密
				if(user.get("role").equals("1"))
				{
					user.set("flag", "1");
				}
				else
				{
					user.set("flag", "0");
				}
				user.save();
				this.redirect("/login");
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 绘制验证码
	 */
	private static final String chars = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
	private static final int WIDTH = 150;
	private static final int HEIGHT = 50;

	public void jpg() {
		HttpServletResponse response = this.getResponse();
		HttpSession session = this.getSession();
		response.setContentType("image/jpeg");
		response.setCharacterEncoding("utf-8");
		// 防止浏览器缓冲
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		char[] rands = getCode(4);
		drawBackground(g);
		drawRands(g, rands);
		g.dispose();
		try {
			ServletOutputStream out = response.getOutputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bos);
			byte[] buf = bos.toByteArray();
			response.setContentLength(buf.length);
			out.write(buf);
			bos.close();
			out.close();
			session.setAttribute("check", new String(rands).toLowerCase());
		} catch (Exception e) {
		}
		renderNull();
	}

	/**
	 * 产生随机数
	 * @return
	 */
	public char[] getCode(int length) {
		char[] rands = new char[length];
		for (int i = 0; i < length; i++) {
			int rand = (int) (Math.random() * chars.length());
			rands[i] = chars.charAt(rand);
		}
		return rands;
	}

	/**
	 * 绘制背景
	 * @param g
	 */
	public void drawBackground(Graphics g) {
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		Random random = new Random();
		int len = 0;
		while (len <= 5) {
			len = random.nextInt(15);
		}
		for (int i = 0; i < len; i++) {
			int x = (random.nextInt(WIDTH));
			int y = (random.nextInt(HEIGHT));
			int red = 255 - random.nextInt(200);
			int green = 255 - random.nextInt(200);
			int blue = 255 - random.nextInt(200);
			g.setColor(new Color(red, green, blue));
			// g.drawLine(x, y, random.nextInt(WIDTH)-x,
			// random.nextInt(HEIGHT)-y);
			g.drawOval(x, y, 2, 2);
		}
	}

	/**
	 * 绘制验证码
	 * @param g
	 * @param rands
	 */
	public void drawRands(Graphics g, char[] rands) {
		Random random = new Random();

		g.setFont(new Font("黑体", Font.ITALIC | Font.BOLD, 45));
		for (int i = 0; i < rands.length; i++) {
			int red = (random.nextInt(255));
			int green = (random.nextInt(255));
			int blue = (random.nextInt(255));
			g.setColor(new Color(red, green, blue));
			g.drawString("" + rands[i], i * 40, 40);
		}
	} 
	
	
	/**
	 * 默认方法，跳转至login.html
	 */
	@Override
	public void index() {
		render("login.html");
	}
}
