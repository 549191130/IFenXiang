/**
 * 
 */
package com.ifx.controller;

import java.util.List;

import com.ifx.service.ResourcesKindService;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class ResourcesKindController extends Controller{
	private ResourcesKindService resourcesKindService = Enhancer.enhance(ResourcesKindService.class);
	
	/**
	* @author djh
	* @Description 添加资源种类
	*/
	public void addResourcesKind(){
		String kindName = this.getPara("kindName");
		try {
			resourcesKindService.addResourcesKind(kindName);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 查询单个资源种类
	*/
	public void getResourcesKind(){
		String kindid = this.getPara("kindid");
		try {
			Record resourcesKind = resourcesKindService.getResourcesKind(kindid);
			this.setAttr("success", true);
			this.setAttr("data", resourcesKind);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 查询所有资源种类
	*/
	public void getAllResourcesKinds(){
		try {
			List<Record> allResourcesKinds = resourcesKindService.getAllResourcesKinds();
			this.setAttr("success", true);
			this.setAttr("data", allResourcesKinds);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	public void delResourcesKind(){
		String kindid = this.getPara("kindid");
		try {
			resourcesKindService.delResourcesKind(kindid);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
}
