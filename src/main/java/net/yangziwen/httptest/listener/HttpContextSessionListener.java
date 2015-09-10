package net.yangziwen.httptest.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.http.client.protocol.HttpClientContext;

import net.yangziwen.httptest.controller.TestCaseController;

public class HttpContextSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		sessionEvent.getSession().setAttribute(TestCaseController.HTTP_CONTEXT, HttpClientContext.create());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		
	}

}
