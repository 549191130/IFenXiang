/**
 * 
 */
package com.ifx.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;



public class ExchangeService {
	
	/**
	* @author djh
	* @Description 		返回指定用户兑换的所有资源
	* @param 
	* @return void    返回类型
	*/
	public List<Record> getExchangeByOpenId(String openId){
		String sql = "select * from exchange where openid = ?";
		List<Record> lRecord = Db.find(sql,openId);
		return lRecord;
	}
	
	/**
	* @author djh
	* @Description 	用户兑换资源(先判断用户是否已经兑换过该资源,然后判断用户剩余积分是否够用)
	* @param 	opneId	用户标识
	* @param	resourcesId	资源标识
	* @param	needIntegral	兑换资源所需积分
	* @return void    返回类型
	*/
	@Before(Tx.class)
	public JSONObject exchange(String openId,Long resourcesId,Long needIntegral){
		JSONObject obj = new JSONObject();
		Boolean flag = true;
		String msg = "恭喜兑换成功!";
		//先判断用户是否兑换过该资源
		Record findFirst = Db.findFirst("select * from exchange where openid = ? and resourcesid = ?", openId,resourcesId);
		if(findFirst==null){
			String sql = "select integral from user where openid = ?";
			Record record = Db.findFirst(sql, openId);
			if(record!=null){
				Long integral = record.getLong("integral");
				//然后判断用户积分够不够用
				if(needIntegral<=integral){
					integral = integral-needIntegral;
					Record userRecord = new Record();
					userRecord.set("integral", integral);
					userRecord.set("openid", openId);
					Db.update("user", "openid", userRecord);
					
					Record resourcesRecord = Db.findById("resources", "resourcesid",resourcesId);
					
					Record exchangeRecord = new Record();
					exchangeRecord.set("openid", openId);
					exchangeRecord.set("resourcesid", resourcesId);
					exchangeRecord.set("resourcesname", resourcesRecord.getStr("name"));
					exchangeRecord.set("resourcesintroduce", resourcesRecord.getStr("introduce"));
					exchangeRecord.set("resourceslink", resourcesRecord.getStr("disklink"));
					exchangeRecord.set("resourcespassword", resourcesRecord.getStr("diskpassword"));
					Db.save("exchange", "openid,resourcesid", exchangeRecord);
				}else{
					flag = false;
					msg = "积分不够啊!";
				}
			}else{
				flag = false;
				msg = "请退出重新登录!";
			}
		}else{
			flag = false;
			msg = "你兑换过了!";
		}
		obj.put("flag", flag);
		obj.put("msg", msg);
		return obj;
	}
	
}
