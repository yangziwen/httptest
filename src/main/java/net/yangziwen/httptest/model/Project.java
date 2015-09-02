package net.yangziwen.httptest.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import net.yangziwen.httptest.model.base.AbstractModel;

@Table
public class Project extends AbstractModel {
	
	@Id
	@Column
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String baseUrl;		// scheme, host, port and even contextPath
	
	public Project() {}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
}
