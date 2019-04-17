/**
 * 
 */
package com.ifx.controller;

import java.util.List;

import com.ifx.service.ResourcesService;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ResourcesController extends Controller{
	private ResourcesService resourcesService = Enhancer.enhance(ResourcesService.class);
	
	/**
	* @author djh
	* @Description 添加资源
	*/
	public void addResource(){
		String resources = this.getPara("resources");
		try {
			resourcesService.addResource(resources);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 查询单个资源
	*/
	public void getResource(){
		String resourcesid = this.getPara("resourcesid");
		try {
			Record resources = resourcesService.getResource(resourcesid);
			this.setAttr("success", true);
			this.setAttr("data", resources);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 查询所有资源
	*/
	public void getAllResources(){
		try {
			List<Record> allResources = resourcesService.getAllResources();
			this.setAttr("success", true);
			this.setAttr("data", allResources);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 获取相同类型的资源
	*/
	public void getSameKindResources(){
		String kindid = this.getPara("kindid");
		try {
			List<Record> sameKindResources = resourcesService.getSameKindResources(kindid);
			this.setAttr("success", true);
			this.setAttr("data", sameKindResources);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 分页获取指定类型资源
	*/
	public void pageResources(){
		//int pageNumber,int pageSize
		int pageNumber = this.getParaToInt("pageNumber");
		int pageSize = this.getParaToInt("pageSize");
		String kindid = this.getPara("kindid");
		String conditions = this.getPara("conditions");
		try {
			Page<Record> pageResources = resourcesService.pageResources(pageNumber, pageSize,kindid,conditions);
			this.setAttr("success", true);
			this.setAttr("data", pageResources);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 分页获取资源
	*/
	public void pageResources2(){
		int pageNumber = this.getParaToInt("pageNumber");
		int pageSize = this.getParaToInt("pageSize");
		String conditions = this.getPara("conditions");
		try {
			Page<Record> pageResources = resourcesService.pageResources2(pageNumber, pageSize,conditions);
			this.setAttr("success", true);
			this.setAttr("data", pageResources);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 最新发布
	*/
	public void latest(){
		try {
			List<Record> latest = resourcesService.latest();
			this.setAttr("success", true);
			this.setAttr("data", latest);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 删除指定资源
	*/
	public void delResource(){
		String resourcesid = this.getPara("resourcesid");
		try {
			resourcesService.delResource(resourcesid);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/*public void add(){
		
		for(int i=0;i<300;i++){
			
			Record r = new Record();
			r.set("photo", "图片路径"+i);
			r.set("introduce", "资源介绍"+i);
			r.set("disklink", "资源链接"+i);
			r.set("diskpassword", "资源密码"+i);
			r.set("needintegral", i+1);
			r.set("publish", "2019-1-27");
			//前端
			if(i%3==0){
				r.set("kindid", 1);
				r.set("name", "前端"+i);
			}//后台
			else if(i%3==1){
				r.set("kindid", 2);
				r.set("name", "后台"+i);
			}//大数据
			else if(i%3==2){
				r.set("kindid", 3);
				r.set("name", "大数据"+i);
			}
			Db.save("resources", "resourcesid", r);
		}
	}*/
}
