/**
 * 
 */
package com.ifx.controller;


import java.util.List;

import com.ifx.service.SubmitService;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class SubmitController extends Controller{
	private SubmitService submitService = Enhancer.enhance(SubmitService.class);
	
	/**
	* @author djh
	* @Description 用户提交资源
	*/
	public void submit(){
		String openId = this.getPara("openId");
		String subValue = this.getPara("subValue");
		try {
			submitService.addSubmit(openId, subValue);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 	     返回指定用户提交的所有资源
	* @param 
	* @return void    返回类型
	*/
	public void getSubmitByOpenId(){
		String openId = this.getPara("openId");
		try {
			List<Record> lRecord = submitService.getSubmitByOpenId(openId);
			this.setAttr("data", lRecord);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
}
