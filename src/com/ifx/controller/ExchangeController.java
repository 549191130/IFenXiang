/**
 * 
 */
package com.ifx.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ifx.service.ExchangeService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class ExchangeController extends Controller{
	ExchangeService exchangeService = new ExchangeService();
	/**
	* @author djh
	* @Description 	返回指定用户兑换的所有资源
	*/
	public void getExchangeByOpenId(){
		String openId = this.getPara("openId");
		try {
			List<Record> lReord = exchangeService.getExchangeByOpenId(openId);
			this.setAttr("data", lReord);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 	用户兑换指定资源
	*/
	public void exchange(){
		String opneId = this.getPara("opneId");
		Long resourcesId = this.getParaToLong("resourcesId");
		Long needIntegral = this.getParaToLong("needIntegral");
		try {
			JSONObject exchange = exchangeService.exchange(opneId, resourcesId, needIntegral);
			this.renderJson(exchange);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("flag", false);
			this.setAttr("msg", "服务端异常!");
			this.renderJson();
		}
	}
}
