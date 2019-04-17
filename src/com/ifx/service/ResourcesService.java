/**
 * 
 */
package com.ifx.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ResourcesService {
	
	public void addResource(String resources){
		Record record = new Record();
		JSONObject resourcesObj = JSONObject.parseObject(resources);
		record.set("name", resourcesObj.getString("name"));
		record.set("photo", resourcesObj.getString("photo"));
		record.set("introduce", resourcesObj.getString("introduce"));
		record.set("disklink", resourcesObj.getString("disklink"));
		record.set("diskpassword", resourcesObj.getString("diskpassword"));
		record.set("needintegral", resourcesObj.getString("needintegral"));
		record.set("kindid", resourcesObj.getString("kindid"));
		record.set("state", resourcesObj.getString("state"));
		Db.save("resources", "resourcesid", record);
	}
	
	public Record getResource(String resourcesid){
		String sql = "select * from resources where resourcesid = ?";
		Record resources = Db.findFirst(sql,resourcesid);
		return resources;
	}
	
	public List<Record> getAllResources(){
		String sql = "select * from Resources";
		List<Record> lRecord = Db.find(sql);
		return lRecord;
	}
	
	public void delResource(String resourcesid){
		Db.deleteById("Resources", "resourcesid", resourcesid);
	}
	
	public Page<Record> pageResources(int pageNumber,int pageSize,String kindid,String conditions){
		Page<Record> paginate = Db.paginate(pageNumber, pageSize, "select * ", "from resources where kindid = ? and name like ?",kindid,"%"+conditions+"%");
		return paginate;
	}
	
	public Page<Record> pageResources2(int pageNumber,int pageSize,String conditions){
		Page<Record> paginate = Db.paginate(pageNumber, pageSize, "select * ", "from resources where name like ?","%"+conditions+"%");
		return paginate;
	}
	
	public List<Record> latest(){
		String sql = "select * from resources order by publish desc limit 10";
		List<Record> find = Db.find(sql);
		return find;
	}
	
	public List<Record> getSameKindResources(String kindid){
		String sql = "select * from Resources where kindid = ?";
		List<Record> lRecord = Db.find(sql,kindid);
		return lRecord;
	}
}
