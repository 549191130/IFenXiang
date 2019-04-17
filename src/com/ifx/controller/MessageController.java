/**
 * 
 */
package com.ifx.controller;


import com.ifx.service.MessageService;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class MessageController extends Controller{
	private MessageService messageService = Enhancer.enhance(MessageService.class);
	
	/**
	* @author djh
	* @Description 添加用户评论
	*/
	public void addMessage(){
		String message = this.getPara("message");
		try {
			messageService.addMessage(message);
			this.setAttr("flag", true);
		} catch (Exception e) {
			this.setAttr("flag", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 分页获取评论
	*/
	public void pageMessage(){
		int pageNumber = this.getParaToInt("pageNumber");
		int pageSize = this.getParaToInt("pageSize");
		try {
			Page<Record> pageMessage = messageService.pageMessage(pageNumber, pageSize);
			this.setAttr("flag", true);
			this.setAttr("data", pageMessage);
		} catch (Exception e) {
			this.setAttr("flag", false);
		}
		this.renderJson();
	}
	
}
