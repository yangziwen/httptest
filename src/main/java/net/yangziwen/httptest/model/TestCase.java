package net.yangziwen.httptest.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.http.Consts;
import org.apache.http.HttpMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
				
				HttpGet get = new HttpGet(buildUriWithParams(url, paramList));
				
				addHeaders(get, paramList);
				
				return get;
			}
		}, 
		
		PUT {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				
				HttpPut put = new HttpPut(url);
				
				addHeaders(put, paramList);
				
				return fillUrlEncodedTextEntity(put, paramList);
			}
		},
		
		POST {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				
				HttpPost post = new HttpPost(url);
				
				addHeaders(post, paramList);
				
				return needMultipart(paramList)
						? fillMultipartEntity(post, paramList)
						: fillUrlEncodedFormEntity(post, paramList);
			}
			
		},
		
		DELETE {
			@Override
			public HttpUriRequest createRequest(String url, List<CaseParam> paramList) {
				
				HttpDelete delete = new HttpDelete(buildUriWithParams(url, paramList));
				
				addHeaders(delete, paramList);
				
				return delete;
			}
		};
		
		public static final Charset DEFAULT_CHARSET = Consts.UTF_8;
		
		public abstract HttpUriRequest createRequest(String url, List<CaseParam> paramList);
		
		protected static boolean needMultipart(List<CaseParam> paramList) {
			for(CaseParam cp: paramList) {
				if(cp.getType() == Type.UPLOAD_PARAM) {
					return true;
				}
			}
			return false;
		}
		
		/** 文件上传 **/
		protected static <R extends HttpEntityEnclosingRequestBase> R fillMultipartEntity(R request, List<CaseParam> paramList) {
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
					default:;
				}
			}
			request.setEntity(builder.build());
			return request;
		}
		
		/** 表单提交 **/
		protected static <R extends HttpEntityEnclosingRequestBase> R fillUrlEncodedFormEntity(R request, List<CaseParam> paramList) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (CaseParam param : paramList) {
				if (param.getType() == CaseParam.Type.PARAM) {
					params.add(new BasicNameValuePair(param.getName(), param.getValue()));
				}
			}
			request.setEntity(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
			return request;
		}
		
		/** 
		 * 文本内容填充请求体
		 * paramList中存在多个TEXT_ENTITY类型的参数时，
		 * 只有第一个TEXT_ENTITY类型的参数有效
		 */
		protected static <R extends HttpEntityEnclosingRequestBase> R fillUrlEncodedTextEntity(R request, List<CaseParam> paramList) {
			for(CaseParam param: paramList) {
				if(param.getType() == CaseParam.Type.TEXT_ENTITY) {
					request.setEntity(new StringEntity(param.getValue(), DEFAULT_CHARSET));
					break;
				}
			}
			return request;
		}
		
		protected static <R extends HttpMessage> R addHeaders(R request, List<CaseParam> paramList) {
			for(CaseParam param: paramList) {
				if(param.getType() == CaseParam.Type.HEADER) {
					request.addHeader(param.getName(), param.getValue());
				}
			}
			return request;
		}
		
		protected static URI buildUriWithParams(String url, List<CaseParam> paramList) {
			try {
				URIBuilder builder = new URIBuilder(url);
				for(CaseParam param: paramList) {
					if(param.getType() == CaseParam.Type.PARAM) {
						builder.setParameter(param.getName(), param.getValue());
					}
				}
				return builder.build();
			} catch (URISyntaxException e) {
				throw HttpTestException.illegalUrlException("Url is invalid [%s]", url);
			}
		}
		
	}
	
	
	public static class MethodPropertyEditor extends EnumPropertyEditor<Method> {

		public MethodPropertyEditor() {
			super(Method.class, EnumConverter.ORDINAL_CONVERTER);
		}
		
	}
	
}
