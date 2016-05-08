package net.yangziwen.httptest.util.http;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;

/**
 * 可以追加请求体的GET请求
 * 用于访问elasticsearch
 * @author yangziwen
 */
public class HttpEntityEncloseingGet extends HttpEntityEnclosingRequestBase {

    public HttpEntityEncloseingGet() {
        super();
    }

    public HttpEntityEncloseingGet(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpEntityEncloseingGet(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return HttpGet.METHOD_NAME;
    }

}
