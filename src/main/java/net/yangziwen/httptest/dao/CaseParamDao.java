package net.yangziwen.httptest.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	
	public int deleteByCaseIds(Long... caseIds) {
		if(caseIds == null) {
			return 0;
		}
		return deleteByCaseIds(Arrays.asList(caseIds));
	}
	
	public int deleteByCaseIds(List<Long> caseIdList) {
		if(caseIdList == null || caseIdList.isEmpty()) {
			return 0;
		}
		String sql = "delete from " + getTableName() + " where case_id in (:caseIdList)";
		Map<String, Object> params = new QueryParamMap().addParam("caseIdList", caseIdList);
		return jdbcTemplate.update(sql, params);
	}

}
