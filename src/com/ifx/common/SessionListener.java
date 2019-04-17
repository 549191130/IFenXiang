package com.ifx.common;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
 
/**
 *
 * ClassName: SessionListener
 *
 * @Description: sessionid web.xml监听器
 * @author liangbo
 * @date 2016-3-18
 */
public class SessionListener implements HttpSessionListener {
 
    private SessionContext context = SessionContext.getInstance();
 
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        context.addSession(sessionEvent.getSession());
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        context.delSession(session);
    }
 
}