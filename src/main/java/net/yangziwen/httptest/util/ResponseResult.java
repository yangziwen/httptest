package net.yangziwen.httptest.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
	
	private long timeSpentMillis;
	
	private long bodySize;
	
	public ResponseResult(HttpResponse response, long timeSpentMillis) {
		this.timeSpentMillis = timeSpentMillis;
		statusLine = response.getStatusLine().toString();
		Header header = response.getFirstHeader(CONTENT_TYPE);
		if(header != null) {
			contentType = ContentType.parse(header.getValue());
		} else {
			contentType = DEFAULT_CONTENT_TYPE;
		}
		headers = Arrays.asList(response.getAllHeaders());
		String mimeType = contentType.getMimeType();
		if(StringUtils.isNotBlank(mimeType)) {
			try {
				if (mimeType.equals("application/json") || mimeType.startsWith("text")) {
					content = EntityUtils.toString(response.getEntity());
					bodySize = content.getBytes().length;
				} else if (mimeType.startsWith("image")) {
					byte[] bytes = EntityUtils.toByteArray(response.getEntity());
					content = Base64.encodeBase64String(bytes);
					bodySize = bytes.length;
				}
			} catch (Exception e) {
			}
		}
	}

	public long getTimeSpentMillis() {
		return timeSpentMillis;
	}

	public String getStatusLine() {
		return statusLine;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public String getContent() {
		return content;
	}

	public long getBodySize() {
		return bodySize;
	}

}
