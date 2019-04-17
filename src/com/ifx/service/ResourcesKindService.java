/**
 * 
 */
package com.ifx.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ResourcesKindService {
	
	public void addResourcesKind(String kindName){
		Record record = new Record();
		record.set("kindname", kindName);
		Db.save("resourceskind", "kindid", record);
	}
	
	public Record getResourcesKind(String kindid){
		String sql = "select * from resourcesKind where kindid = ?";
		Record resourcesKind = Db.findFirst(sql,kindid);
		return resourcesKind;
	}
	
	public List<Record> getAllResourcesKinds(){
		String sql = "select * from resourcesKind";
		List<Record> lRecord = Db.find(sql);
		return lRecord;
	}
	
	public void delResourcesKind(String kindid){
		Db.deleteById("resourcesKind", "kindid", kindid);
	}
	
}
