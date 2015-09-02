package net.yangziwen.httptest.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.yangziwen.httptest.dao.base.AbstractEditPropertyJdbcDaoImpl;
import net.yangziwen.httptest.dao.base.CustomPropertyEditor;
import net.yangziwen.httptest.model.CaseInfo;

@Repository
public class CaseInfoDao extends AbstractEditPropertyJdbcDaoImpl<CaseInfo> {
	
	@SuppressWarnings("serial")
	private Map<Class<?>, CustomPropertyEditor> propertyEditorMap = new HashMap<Class<?>, CustomPropertyEditor>() {{
		put(CaseInfo.Type.class, new CaseInfo.TypePropertyEditor());
	}};

	@Override
	protected Map<Class<?>, CustomPropertyEditor> getPropertyEditorMap() {
		return propertyEditorMap;
	}

}
