package net.yangziwen.httptest.model;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import net.yangziwen.httptest.dao.base.EnumPropertyEditor;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.base.AbstractModel;
import net.yangziwen.httptest.util.EnumUtil.EnumConverter;

@Table(name = "test_case")
public class TestCase extends AbstractModel {

	@Id
	@Column
	private long id;
	
	@Column
	private long projectId;
	
	@Column
	private String path;

	@Column
	private Method method;
	
	@Column
	private String description;
	
	public TestCase() {}
	
	public TestCase(long projectId, String path, Method method, String description) {
		this.projectId = projectId;
		this.path = path;
		this.method = method;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public enum Method {
		GET {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				HttpGet get = null;
				try {
					URIBuilder builder = new URIBuilder(url);
					for(CaseParam param: paramList) {
						if(param.getType() == CaseParam.Type.PARAM) {
							builder.setParameter(param.getName(), param.getValue());
						}
					}
					get = new HttpGet(builder.build());
				} catch (URISyntaxException e) {
					throw HttpTestException.illegalUrlException("Url is invalid [%s]", url);
				}
				for(CaseParam param: paramList) {
					if(param.getType() == CaseParam.Type.HEADER) {
						get.addHeader(param.getName(), param.getValue());
					}
				}
				return get;
			}
		}, POST {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				HttpPost post = new HttpPost(url);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
		        for (CaseParam param: paramList) {
		        	switch(param.getType()) {
		        		case PARAM:
		        			params.add(new BasicNameValuePair(param.getName(), param.getValue()));
		        			break;
		        		case HEADER:
		        			post.addHeader(param.getName(), param.getValue());
		        			break;
		        		default:;
		        	}
		        }
				return new HttpPost(url);
			}
		};
		public abstract HttpUriRequest createRequest(String url, List<CaseParam> paramList);
	}
	
	
	public static class MethodPropertyEditor extends EnumPropertyEditor<Method> {

		public MethodPropertyEditor() {
			super(Method.class, EnumConverter.ORDINAL_CONVERTER);
		}
		
	}
	
}
