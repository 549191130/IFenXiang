/**
 * 
 */
package com.ifx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class MessageService {
	
	public void addMessage(String message){
		Record record = new Record();
		JSONObject messageObj = JSONObject.parseObject(message);
		record.set("openid", messageObj.getString("openid"));
		record.set("content", messageObj.getString("content"));
		record.set("date", new SimpleDateFormat("yyyy-M-d").format(new Date()));
		Db.save("message", "messageid", record);
	}
	
	public Record getMessage(String messageid){
		String sql = "select * from message where messageid = ?";
		Record message = Db.findFirst(sql,messageid);
		return message;
	}
	
	public List<Record> getAllMessages(){
		String sql = "select * from message";
		List<Record> lRecord = Db.find(sql);
		return lRecord;
	}
	
	public void delMessage(String messageid){
		Db.deleteById("message", "messageid", messageid);
	}
	
	public Page<Record> pageMessage(int pageNumber,int pageSize){
		Page<Record> paginate = Db.paginate(pageNumber, pageSize, "select * ", "from message where approved=1 order by date desc");
		return paginate;
	}
	
}
