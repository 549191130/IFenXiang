package com.ifx.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 
import javax.servlet.http.HttpSession;
 
/**
 *
 * ClassName: SessionContext
 *
 * @Description: 获取sessionID
 * @author liangbo
 * @date 2016-3-18
 */
public class SessionContext {
 
    private static SessionContext instance;
    private Map<String, HttpSession> sessionMap;
 
    private SessionContext() {
        sessionMap = new HashMap<String, HttpSession>();
    }
 
    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }
 
    public synchronized void addSession(HttpSession session) {
        if (session != null) {
        	//System.out.println("=======加入session:"+session.getId());
            sessionMap.put(session.getId(), session);
        }
    }
 
    public synchronized void delSession(HttpSession session) {
        if (session != null) {
            sessionMap.remove(session.getId());
        }
    }
 
    public synchronized HttpSession getSession(String sessionId) {
        if (sessionId == null){
        	return null;
        }
        Set<String> st = sessionMap.keySet();
        for(String key : st){
        	System.out.println(key);
        	System.out.println(sessionMap.get(key));
        }
        return sessionMap.get(sessionId);
    }
}