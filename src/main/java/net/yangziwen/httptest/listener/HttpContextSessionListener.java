package net.yangziwen.httptest.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.yangziwen.httptest.util.HttpContextStore;

public class HttpContextSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		sessionEvent.getSession().setAttribute(HttpContextStore.CONTEXT_STORE, new HttpContextStore());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
	}

}
