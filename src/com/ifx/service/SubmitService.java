/**
 * 
 */
package com.ifx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SubmitService {
	
	public void addSubmit(String openId,String subValue){
		Record record = new Record();
		JSONObject submitObj = JSONObject.parseObject(subValue);
		record.set("openid", openId);
		record.set("resourcesname", submitObj.getString("resourcesname"));
		record.set("resourcesintroduction", submitObj.getString("resourcesintroduction"));
		record.set("resourceslink", submitObj.getString("resourceslink"));
		record.set("resourcespassword", submitObj.getString("resourcespassword"));
		record.set("publish", new SimpleDateFormat("yyyy-M-d").format(new Date()));
		Db.save("submit", "submitid", record);
	}
	
	/**
	* @author djh
	* @Description 			返回指定用户提交的所有资源
	* @param 
	* @return List<Record>    返回类型
	*/
	public List<Record> getSubmitByOpenId(String openId){
		String sql = "select * from submit where openid = ? order by publish desc";
		List<Record> lRecord = Db.find(sql, openId);
		return lRecord;
	}
	
}
