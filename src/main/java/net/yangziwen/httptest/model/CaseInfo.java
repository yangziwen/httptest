package net.yangziwen.httptest.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import net.yangziwen.httptest.dao.base.EnumPropertyEditor;
import net.yangziwen.httptest.model.base.AbstractModel;
import net.yangziwen.httptest.util.EnumUtil.EnumConverter;

@Table
public class CaseInfo extends AbstractModel {
	
	@Id
	@Column
	private long id;
	
	@Column
	private long caseId;
	
	@Column
	private Type type;
	
	@Column
	private String name;
	
	@Column
	private String value;
	
	public CaseInfo() {}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCaseId() {
		return caseId;
	}

	public void setCaseId(long caseId) {
		this.caseId = caseId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public enum Type {
		PARAM, UPLOAD_PARAM, HEADER, COOKIE;
	}
	
	public static class TypePropertyEditor extends EnumPropertyEditor<Type> {

		public TypePropertyEditor() {
			super(Type.class, EnumConverter.ORDINAL_CONVERTER);
		}
		
	}

}
