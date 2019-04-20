/**
 * 
 */
package com.ifx.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ifx.service.UserService;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class UserController extends Controller{
	private UserService userService = Enhancer.enhance(UserService.class);
	
	/**
	* @author djh
	* @Description 添加用户或获取用户信息
	*/
	public void saveOrGetUser(){
		String code = this.getPara("code");
		try {
			String userInfo = userService.saveOrGetUser(code);
			this.setAttr("data", userInfo);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 	昵称存在返回true
	*/
	public void hasUserName(){
		String openid = this.getPara("openid");
		try {
			Boolean hasUserName = userService.hasUserName(openid);
			this.setAttr("data", hasUserName);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	public void add(){
		String openId = this.getPara("openid");
		String nickName = this.getPara("nickName");
		String avatarUrl = this.getPara("avatarUrl");
		try {
			userService.add(openId, nickName, avatarUrl);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	
	/**
	* @author djh
	* @Description 	用户签到
	* @param 
	* @return void    返回类型
	*/
	public void sign(){
		String openid = this.getPara("openid");
		try {
			JSONObject obj = userService.isSign(openid);
			this.setAttr("data", obj);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 	用户签到
	* @param 
	* @return void    返回类型
	*/
	public void signRandom(){
		String openid = this.getPara("openid");
		try {
			JSONObject obj = userService.signRandom(openid);
			this.setAttr("data", obj);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	
	/**
	* @author djh
	* @Description 查询单个用户
	*/
	public void getUser(){
		String openid = this.getPara("openid");
		try {
			Record user = userService.getUser(openid);
			this.setAttr("success", true);
			this.setAttr("data", user);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 查询所有用户
	*/
	public void getAllUsers(){
		try {
			List<Record> allUsers = userService.getAllUsers();
			this.setAttr("success", true);
			this.setAttr("data", allUsers);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	public void delUser(){
		String openid = this.getPara("openid");
		try {
			userService.delUser(openid);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description     返回指定用户当前积分
	*/
	public void getUserByOpenId(){
		String openid = this.getPara("openid");
		try {
			this.setAttr("data", userService.getUserByOpenId(openid));
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	
	/**
	* @author djh
	* @Description 积分排行榜
	*/
	public void integralList(){
		try {
			List<Record> sginList = userService.integralList();
			this.setAttr("data", sginList);
			this.setAttr("success", true);
		} catch (Exception e) {
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	/**
	* @author djh
	* @Description 展示今日已签到用户
	*/
	public void sginToday(){
		try {
			List<Record> sginToday = userService.sginToday();
			this.setAttr("data", sginToday);
			this.setAttr("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			this.setAttr("success", false);
		}
		this.renderJson();
	}
	
	
	
	
	
	
	
}
