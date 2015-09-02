package net.yangziwen.httptest.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import net.yangziwen.httptest.dao.base.EnumPropertyEditor;
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
	
	@Column
	private int rank;
	
	public TestCase() {}

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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public enum Method {
		GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE;
	}
	
	public static class MethodPropertyEditor extends EnumPropertyEditor<Method> {

		public MethodPropertyEditor() {
			super(Method.class, EnumConverter.ORDINAL_CONVERTER);
		}
		
	}
	
}
