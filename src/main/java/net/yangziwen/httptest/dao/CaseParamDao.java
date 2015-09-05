package net.yangziwen.httptest.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.yangziwen.httptest.dao.base.AbstractEditPropertyJdbcDaoImpl;
import net.yangziwen.httptest.dao.base.CustomPropertyEditor;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.model.CaseParam;

@Repository
public class CaseParamDao extends AbstractEditPropertyJdbcDaoImpl<CaseParam> {
	
	@SuppressWarnings("serial")
	private Map<Class<?>, CustomPropertyEditor> propertyEditorMap = new HashMap<Class<?>, CustomPropertyEditor>() {{
		put(CaseParam.Type.class, new CaseParam.TypePropertyEditor());
	}};

	@Override
	protected Map<Class<?>, CustomPropertyEditor> getPropertyEditorMap() {
		return propertyEditorMap;
	}
	
	public int deleteByCaseId(long caseId) {
		String sql = "delete from " + getTableName() + "where case_id = :caseId";
		Map<String, Object> params = new QueryParamMap().addParam("caseId", caseId);
		return jdbcTemplate.update(sql, params);
	}

}
