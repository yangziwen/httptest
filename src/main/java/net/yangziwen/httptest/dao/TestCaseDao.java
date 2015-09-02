package net.yangziwen.httptest.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.yangziwen.httptest.dao.base.AbstractEditPropertyJdbcDaoImpl;
import net.yangziwen.httptest.dao.base.CustomPropertyEditor;
import net.yangziwen.httptest.model.TestCase;

@Repository
public class TestCaseDao extends AbstractEditPropertyJdbcDaoImpl<TestCase> {
	
	@SuppressWarnings("serial")
	private Map<Class<?>, CustomPropertyEditor> propertyEditorMap = new HashMap<Class<?>, CustomPropertyEditor>(){{
		put(TestCase.Method.class, new TestCase.MethodPropertyEditor());
	}};

	@Override
	protected Map<Class<?>, CustomPropertyEditor> getPropertyEditorMap() {
		return propertyEditorMap;
	}

}
