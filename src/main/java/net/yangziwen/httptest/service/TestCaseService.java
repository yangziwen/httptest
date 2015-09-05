package net.yangziwen.httptest.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.yangziwen.httptest.dao.CaseParamDao;
import net.yangziwen.httptest.dao.TestCaseDao;
import net.yangziwen.httptest.dao.base.Page;
import net.yangziwen.httptest.dao.base.QueryParamMap;
import net.yangziwen.httptest.model.CaseParam;
import net.yangziwen.httptest.model.TestCase;

@Service
public class TestCaseService {

	@Autowired
	private TestCaseDao testCaseDao;
	
	@Autowired
	private CaseParamDao caseParamDao;
	
	public TestCase getTestCaseById(long id) {
		return testCaseDao.getById(id);
	}
	
	public Page<TestCase> getTestCasePageResult(int offset, int limit, Map<String, Object> params) {
		return testCaseDao.paginate(offset, limit, params);
	}
	
	public void createTestCase(TestCase testCase) {
		testCaseDao.save(testCase);
	}
	
	public void updateTestCase(TestCase testCase) {
		testCaseDao.update(testCase);
	}
	
	public List<CaseParam> getCaseParamListResult(Map<String, Object> params) {
		return caseParamDao.list(params); 
	}
	
	@Transactional
	public void deleteTestCase(long caseId) {
		testCaseDao.deleteById(caseId);
		caseParamDao.deleteByCaseIds(caseId);
	}
	
	@Transactional
	public void renewCaseParams(long caseId, List<CaseParam> caseParamList) {
		if(CollectionUtils.isEmpty(caseParamList)) {
			caseParamDao.deleteByCaseIds(caseId);
			return;
		}
		for(CaseParam cp: caseParamList) {
			cp.setCaseId(caseId);
		}
		List<CaseParam> existedCaseParamList = caseParamDao.list(new QueryParamMap().addParam("caseId", caseId));
		Set<Long> toDeleteIdSet = extractToDeleteCaseParamIdSet(caseParamList, existedCaseParamList);
		caseParamDao.batchDeleteByIds(toDeleteIdSet);
		caseParamDao.batchSaveOrUpdate(caseParamList, 20);
	}
	
	private Set<Long> extractToDeleteCaseParamIdSet(List<CaseParam> caseParamList, List<CaseParam> existedCaseParamList) {
		Set<Long> idSet = new HashSet<Long>();
		Set<Long> toDeleteIdSet = new HashSet<Long>();
		for(CaseParam cp: caseParamList) {
			if(cp.getId() <= 0) {
				continue;
			}
			idSet.add(cp.getId());
		}
		for(CaseParam ecp: existedCaseParamList) {
			if(!idSet.contains(ecp.getId())) {
				toDeleteIdSet.add(ecp.getId());
			}
		}
		return toDeleteIdSet;
	}
	
}
