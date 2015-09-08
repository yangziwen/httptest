package net.yangziwen.httptest.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.protocol.HttpClientContext;

public class HttpContextStore {
	
	public static final String CONTEXT_STORE = "http_context_store";
	
	private ConcurrentHashMap<Long, HttpClientContext> store = new ConcurrentHashMap<Long, HttpClientContext>();
	
	public HttpClientContext getContext(Long projectId) {
		HttpClientContext context = store.get(projectId);
		HttpClientContext prevContext = null;
		if(context == null) {
			context = HttpClientContext.create();
			prevContext = store.putIfAbsent(projectId, context);
		}
		return prevContext != null? prevContext: context;
	}
	
	public void removeContext(Long projectId) {
		store.remove(projectId);
	}
	

}
