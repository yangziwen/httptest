package net.yangziwen.httptest.util.http;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;

public class HttpEntityEncloseingPost extends HttpEntityEnclosingRequestBase {

    public HttpEntityEncloseingPost() {
        super();
    }

    public HttpEntityEncloseingPost(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpEntityEncloseingPost(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return HttpPost.METHOD_NAME;
    }
	
}
