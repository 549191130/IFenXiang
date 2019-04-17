package com.ifx.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.alibaba.fastjson.JSONObject;
import com.ifx.common.AesCbcUtil;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class UserService {
	Prop prop = PropKit.use("a_little_config.txt");
	
	public Record getUser(String openid){
		String sql = "select * from user where openid = ?";
		Record user = Db.findFirst(sql,openid);
		return user;
	}
	
	public List<Record> getAllUsers(){
		String sql = "select * from user";
		List<Record> lRecord = Db.find(sql);
		return lRecord;
	}
	
	public void delUser(String openid){
		Db.deleteById("user", "openid", openid);
	}
	
	public String saveOrGetUser(String code){
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> param = new HashMap<>();
		param.put("appid", prop.get("appId"));
		param.put("secret", prop.get("appSecret"));
		param.put("js_code", code);
		param.put("grant_type", "authorization_code");
		String wxResult = HttpKit.get(url, param);
		JSONObject parseObject = JSONObject.parseObject(wxResult);
		//System.out.println("wxResult--->"+wxResult);
		String openid = parseObject.getString("openid");
		if(checkUser(openid)){
			return Db.findFirst("select * from user where openid = ?", openid).toJson();
		}else{
			Record record = new Record();
			record.set("openid", openid);
			record.set("nickname", parseObject.getString("nickname"));
			record.set("integral", 0);
			Db.save("user", "openid", record);
			return record.toJson();
		}
	}
	
	/**
	* @author djh
	* @Description 		昵称存在返回true
	* @param 
	* @return Boolean    返回类型
	*/
	public Boolean hasUserName(String openid){
		Record record = Db.findById("user", "openid", openid);
		String nickname = record.getStr("nickname");
		if("".equals(nickname)||nickname==null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	* @author djh
	* @Description 	添加微信用户信息
	* @param 
	* @return void    返回类型
	*/
	public void add(String openId,String nickName,String avatarUrl){
		Record record = new Record();
		record.set("openid", openId);
		record.set("nickname", nickName);
		record.set("avatarurl", avatarUrl);
		Db.update("user", "openid", record);
	}
	
	/**
	* @author djh
	* @Description 	检查用户是否已存在	true标识存在
	* @param 		微信平台针对该小程序  用户的唯一标识
	* @return Boolean    返回类型
	*/
	public Boolean checkUser(String openid){
		Boolean flag = true;
		Record user = Db.findById("user", "openid", openid);
		if(user==null) flag = false;
		return flag;
	}
	
	/**
	* @author djh
	* @Description 			签到(如今日未签到,可以签到,否则禁止重复签到)
	* @param 				openid	用户唯一标识
	* @return JSONObject    返回类型
	*/
	public JSONObject isSign(String openid){
		JSONObject obj = new JSONObject();
		Record user = Db.findById("user", "openid", openid);
		String lastSign = user.getStr("sign");//上次签到时间
		
		String  yearAndDay = new SimpleDateFormat("yyyy-M-d").format(new Date());//获取年月日
		String  currentSign = new SimpleDateFormat("yyyy-M-d hh:mm:ss").format(new Date());//当前签到时间
		//如果没签到过或者上一次签到时间跟这次不一样(指的是年月日不一样)
		if(lastSign==null||lastSign.indexOf(yearAndDay)==-1){
			String sql = "update user set integral = integral+15,sign = ? where openid = ?";
			Db.update(sql, currentSign,openid);
			obj.put("flag",1);//1表示签到成功
		}else{
			obj.put("flag",0);//0表示签到失败
		}
		Record record = Db.findFirst("select integral from user where openid = ?", openid);
		Long integral = record.getLong("integral");
		obj.put("integral",integral);
		return obj;
	}
	
	
	/**
	* @author djh
	* @Description 			随机签到(如今日未签到,可以签到,否则禁止重复签到)
	* @param 				openid	用户唯一标识
	* @return JSONObject    返回类型
	*/
	public JSONObject signRandom(String openid){
		JSONObject obj = new JSONObject();
		Record user = Db.findById("user", "openid", openid);
		String lastRandomSign = user.getStr("randomsign");//上次签到时间
		
		String  yearAndDay = new SimpleDateFormat("yyyy-M-d").format(new Date());//获取年月日
		String  currentSign = new SimpleDateFormat("yyyy-M-d hh:mm:ss").format(new Date());//当前签到时间
			//如果没签到过或者上一次签到时间跟这次不一样(指的是年月日不一样)
			if(lastRandomSign==null||lastRandomSign.indexOf(yearAndDay)==-1){
			//随机积分
			int randomIntegral = (int)(Math.random()*20);
			String sql = "update user set integral = integral+"+randomIntegral+",randomsign = ? where openid = ?";
			Db.update(sql, currentSign,openid);
			obj.put("randomIntegral", randomIntegral);
			obj.put("flag",1);//1表示签到成功
		}else{
			obj.put("flag",0);//0表示签到失败
		}
		Record record = Db.findFirst("select integral from user where openid = ?", openid);
		Long integral = record.getLong("integral");
		obj.put("integral",integral);
		return obj;
	}
	
	
	/**
	* @author djh
	* @Description 	    返回指定用户当前积分
	* @param 
	* @return void    返回类型
	*/
	public Long getIntegralByOpenId(String openid){
		Record record = Db.findFirst("select integral from user where openid = ?", openid);
		Long integral = record.getLong("integral");
		return integral;
	}
	
	/**
	* @author djh
	* @Description 	积分排行榜
	* @param 
	* @return List<Record>    返回类型
	*/
	public List<Record> integralList(){
		List<Record> lRecords = Db.find("select * from user order by integral desc");
		return lRecords;
	}
	
	/**
	* @author djh
	* @Description 	展示今日已签到用户
	* @param 
	* @return List<Record>    返回类型
	*/
	public List<Record> sginToday(){
		String  curTime = new SimpleDateFormat("yyyy-M-d").format(new Date());//当前时间
		List<Record> lRecords = Db.find("select * from user where sign like '"+curTime+"%' or randomsign like '"+curTime+"%' order by sign");
		return lRecords;
	}
	
	/**
	* @author djh
	* @Description 	解密用户敏感数据
	* @param 		encryptedData	明文,加密数据
	* @param		iv	加密算法的初始向量
	* @param		code	用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
	* @return Map    返回类型
	*/
	public JSONObject decodeUserInfo(String encryptedData, String iv, String code) {
		JSONObject map = new JSONObject();
		// 登录凭证不能为空
		if (code == null || code.length() == 0) {
			map.put("status", 0);
			map.put("msg", "code 不能为空");
			return map;
		}
		// 小程序唯一标识 (在微信小程序管理后台获取)
		String wxspAppid = prop.get("appId");
		// 小程序的 app secret (在微信小程序管理后台获取)
		String wxspSecret = prop.get("appSecret");
		// 授权（必填）
		String grant_type = "authorization_code";
		//////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
		// 请求参数
		Map<String, String> params = new HashMap<>();
		params.put("appid", wxspAppid);
		params.put("secret", wxspSecret);
		params.put("js_code", code);
		params.put("grant_type", grant_type);
		// 发送请求
		String sr = HttpKit.get("https://api.weixin.qq.com/sns/jscode2session", params);
		// 解析相应内容（转换成json对象）
		JSONObject json = JSONObject.parseObject(sr);
		// 获取会话密钥（session_key）
		String session_key = json.get("session_key").toString();
		// 用户的唯一标识（openid）
		//String openid = (String)json.get("openid");
		//////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
		try {
			String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			if (null != result && result.length() > 0) {
				map.put("status", 1);
				map.put("msg", "解密成功");

				JSONObject userInfoJSON = JSONObject.parseObject(result);
				Map<String,Object> userInfo = new HashMap<String,Object>();
				userInfo.put("openId", userInfoJSON.get("openId"));
				userInfo.put("nickName", userInfoJSON.get("nickName"));
				userInfo.put("gender", userInfoJSON.get("gender"));
				userInfo.put("city", userInfoJSON.get("city"));
				userInfo.put("province", userInfoJSON.get("province"));
				userInfo.put("country", userInfoJSON.get("country"));
				userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
				// 解密unionId & openId;
				userInfo.put("unionId", userInfoJSON.get("unionId"));
				map.put("userInfo", userInfo);
			} else {
				map.put("status", 0);
				map.put("msg", "解密失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}