package com.ifx.common;


import java.util.Properties;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.ifx.controller.ExchangeController;
import com.ifx.controller.MessageController;
import com.ifx.controller.ResourcesController;
import com.ifx.controller.ResourcesKindController;
import com.ifx.controller.SubmitController;
import com.ifx.controller.UserController;
import com.ifx.model.Exchange;
import com.ifx.model.Message;
import com.ifx.model.Resources;
import com.ifx.model.ResourcesKind;
import com.ifx.model.Submit;
import com.ifx.model.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;


/**
* <p>描述: 系统配置</p>
* <p>Copyright: Copyright (c) 2017 allen</p>
* @author allen
* @date Aug 2, 2017 3:01:30 PM
* @version 1.0
*/
public class SysConfig extends JFinalConfig {
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		PropKit.use("a_little_config.txt");				// 加载少量必要配置，随后可用PropKit.get(...)获取值
		me.setDevMode(PropKit.getBoolean("devMode", false));
		//me.setViewType(ViewType.FREE_MARKER); // 设置视图类型为FreeMarker，否则默认为FreeMarker
        me.setError404View("/404.html");
        me.setError500View("/500.html");
        //  me.setLogFactory(new LogBackFactory());
        //  me.setMaxPostSize(100*1024*1024);
        //默认10M,此处设置为最大1000M(设置文件上传大小)
        // 100*1024*1024*10 = 1048576000    2^32-1= 2147483647
		me.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
	}
	
	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		/*me.add("/", IndexController.class, "/index");
		me.add("/BuildZdsfController",BuildZdsfController.class);*/
		me.add("/user",UserController.class);
		me.add("/resourceskind",ResourcesKindController.class);
		me.add("/resources",ResourcesController.class);
		me.add("/submit",SubmitController.class);
		me.add("/exchange",ExchangeController.class);
		me.add("/message",MessageController.class);
	}
	
	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		Properties ps = PropKit.use("a_little_config.txt").getProperties();
		String dbs = ps.getProperty("dbs");
		String[] dbsArr = dbs.split(",");
		for(int i=0;i<dbsArr.length;i++){
			String db = dbsArr[i];
			DruidPlugin druidPlugin = new DruidPlugin(
						PropKit.get(db+"_jdbcUrl").trim()
					,	PropKit.get(db+"_user").trim()
					, 	PropKit.get(db+"_password").trim());
			druidPlugin.setValidationQuery("select 1 from dual");
			druidPlugin.setName(db);
			//开发时启用，部署时关闭
			druidPlugin.set(1, 2, 4);
			druidPlugin.addFilter(new StatFilter());
			druidPlugin.addFilter(new Slf4jLogFilter());
			if(PropKit.get(db+"_jdbcUrl").toLowerCase().indexOf("oracle")!=-1){
				druidPlugin.setDriverClass("net.sf.log4jdbc.DriverSpy");
			} else if(PropKit.get(db+"_jdbcUrl").toLowerCase().indexOf("mysql")!=-1){
				druidPlugin.setDriverClass("com.mysql.jdbc.Driver");
			}
			me.add(druidPlugin);
			// 配置ActiveRecord插件
			ActiveRecordPlugin arp = new ActiveRecordPlugin(db,druidPlugin);  
			//arp.setShowSql(true);
			if(PropKit.get(db+"_jdbcUrl").toLowerCase().indexOf("oracle")!=-1){
				arp.setDialect(new OracleDialect());
			} else if(PropKit.get(db+"_jdbcUrl").toLowerCase().indexOf("mysql")!=-1){
				arp.setDialect(new MysqlDialect());
			}
			arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));////false 是大写, true是小写, 不写是区分大小写  
			me.add(arp);
			if(db.equals("main")){
				arp.addMapping("user", "openid", User.class);
				arp.addMapping("resourceskind", "kindid", ResourcesKind.class);
				arp.addMapping("resources", "resourcesid", Resources.class);
				arp.addMapping("exchange", "openid,resourcesid", Exchange.class);
				arp.addMapping("message", "message", Message.class);
				arp.addMapping("submit", "submitid", Submit.class);
			}
		}
	}
	
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		 /*me.add(new SysInterceptor());*/
		
	}

	@Override
	public void configHandler(Handlers me) {
//		me.add(new ContextPathHandler("ctx")); 
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}

	@Override
	public void configEngine(Engine arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 在系统启动时调用的方法
	 */
	@Override
	public void afterJFinalStart(){
		
	}
}
