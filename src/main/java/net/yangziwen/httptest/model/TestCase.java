package net.yangziwen.httptest.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import net.yangziwen.httptest.dao.base.EnumPropertyEditor;
import net.yangziwen.httptest.exception.HttpTestException;
import net.yangziwen.httptest.model.CaseParam.Type;
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
		}, 
		
		POST {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				HttpPost post = new HttpPost(url);
				
				boolean needMultipart = ListUtils.indexOf(paramList, new Predicate<CaseParam>() {
					@Override
					public boolean evaluate(CaseParam caseParam) {
						return caseParam.getType() == Type.UPLOAD_PARAM;
					}
				}) >= 0;
				
				return needMultipart
						? fillMultipartEntity(post, paramList)
						: fillUrlEncodedFormEntity(post, paramList);
			}
			
			/** 文件上传 **/
			private HttpPost fillMultipartEntity(HttpPost post, List<CaseParam> paramList) {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create()
						.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
						.setCharset(DEFAULT_CHARSET);

				ContentType contentType = ContentType.create("text/plain", DEFAULT_CHARSET);

				for (CaseParam param : paramList) {
					switch (param.getType()) {
						case PARAM:
							builder.addTextBody(param.getName(), param.getValue(), contentType);
							break;
						case UPLOAD_PARAM:
							File file = new File(param.getValue());
							if (!file.exists()) {
								throw HttpTestException.notExistException("File does not exist! [%s]", param.getValue());
							}
							builder.addBinaryBody(param.getName(), file);
							break;
						case HEADER:
							post.addHeader(param.getName(), param.getValue());
							break;
						default:;
					}
				}
				post.setEntity(builder.build());
				return post;
			}
			
			/** 表单提交 **/
			private HttpPost fillUrlEncodedFormEntity(HttpPost post, List<CaseParam> paramList) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (CaseParam param : paramList) {
					switch (param.getType()) {
						case PARAM:
							params.add(new BasicNameValuePair(param.getName(), param.getValue()));
							break;
						case HEADER:
							post.addHeader(param.getName(), param.getValue());
							break;
						default:;
					}
				}
				try {
					post.setEntity(new UrlEncodedFormEntity(params));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return post;
			}
		};
		
		public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
		
		public abstract HttpUriRequest createRequest(String url, List<CaseParam> paramList);
		
	}
	
	
	public static class MethodPropertyEditor extends EnumPropertyEditor<Method> {

		public MethodPropertyEditor() {
			super(Method.class, EnumConverter.ORDINAL_CONVERTER);
		}
		
	}
	
}
