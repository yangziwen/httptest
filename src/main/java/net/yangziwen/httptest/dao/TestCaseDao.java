package net.yangziwen.httptest.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.yangziwen.httptest.dao.base.AbstractEditPropertyJdbcDaoImpl;
import net.yangziwen.httptest.dao.base.CustomPropertyEditor;
import net.yangziwen.httptest.dao.base.QueryParamMap;
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
	
	public int deleteByProjectId(long projectId) {
		String sql = "delete from " + getTableName() + " where project_id = :projectId";
		Map<String, Object> params = new QueryParamMap().addParam("projectId", projectId);
		return jdbcTemplate.update(sql, params);
	}

}
