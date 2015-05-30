package cn.jxust.core;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;

/**
 * API引导式配置
 */
public class WebConfig extends JFinalConfig
{
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me)
	{
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("classes/config.properties");
		me.setDevMode(getPropertyToBoolean("devMode", false));

		me.setError401View("/WEB-INF/pages/common/401.html");// 没登录
		me.setError403View("/WEB-INF/pages/common/403.html");// 没权限
		me.setError404View("/WEB-INF/pages/common/404.html");
		me.setError500View("/WEB-INF/pages/common/500.html");
		me.setBaseViewPath("/WEB-INF/pages/");
		me.setFreeMarkerTemplateUpdateDelay(1);
		
		Constant.VERSION = getProperty("version");
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me)
	{
		me.add(new AutoBindRoutes());
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me)
	{
		// 配置alibaba数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbc.url"), getProperty("jdbc.username"), getProperty("jdbc.password"), getProperty("jdbc.driver"));
		WallFilter wall = new WallFilter();
		wall.setDbType(getProperty("jdbc.dbType"));
		druidPlugin.addFilter(wall);
		druidPlugin.addFilter(new StatFilter());
		me.add(druidPlugin);

		// 添加自动绑定model与表插件
		AutoTableBindPlugin autoTableBindPlugin = new AutoTableBindPlugin(druidPlugin, SimpleNameStyles.UP);
		autoTableBindPlugin.setShowSql(getPropertyToBoolean("jdbc.showsql", true));
		String db = ConfigKit.getStr("jdbc.dbType");
		if ("mysql".equals(db))
		{
			autoTableBindPlugin.setDialect(new MysqlDialect());
		}
		else if ("oracle".equals(db))
		{
			autoTableBindPlugin.setDialect(new OracleDialect());
		}
		else
		{
			autoTableBindPlugin.setDialect(new AnsiSqlDialect());
		}
		me.add(autoTableBindPlugin);

		// ehcache缓存插件
		me.add(new EhCachePlugin());
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me)
	{
		//解决session在freemarker中不能取得的问题 获取方法：${session["manager"].username}
		me.add(new SessionInViewInterceptor());
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me)
	{
		me.add(new ContextPathHandler());
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
		me.add(dvh);
	}
}
