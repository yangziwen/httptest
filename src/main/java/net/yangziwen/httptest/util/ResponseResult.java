package net.yangziwen.httptest.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public class ResponseResult {
	
	public static final String CONTENT_TYPE = "Content-Type";
	
	public static final ContentType DEFAULT_CONTENT_TYPE = ContentType.parse("text/plain; charset=utf8");

	private String statusLine;
	
	private List<Header> headers;
	
	private ContentType contentType;
	
	private String content;
	
	public ResponseResult(HttpResponse response) {
		statusLine = response.getStatusLine().toString();
		Header header = response.getFirstHeader(CONTENT_TYPE);
		if(header != null) {
			contentType = ContentType.parse(header.getValue());
		} else {
			contentType = DEFAULT_CONTENT_TYPE;
		}
		headers = Arrays.asList(response.getAllHeaders());
		String mimeType = contentType.getMimeType();
		if(StringUtils.isNotBlank(mimeType)
				&& (mimeType.equals("application/json") || mimeType.startsWith("text"))) {
			try {
				content = EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
			}
		}
	}

	public String getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
